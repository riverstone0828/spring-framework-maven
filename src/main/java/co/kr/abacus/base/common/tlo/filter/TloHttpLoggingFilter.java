package co.kr.abacus.base.common.tlo.filter;


import co.kr.abacus.base.common.response.APIResponse;
import co.kr.abacus.base.common.tlo.ReusableRequestWrapper;
import co.kr.abacus.base.common.tlo.TloLoggerMessage;
import co.kr.abacus.base.common.tlo.TloLoggerWriter;
import co.kr.abacus.base.common.tlo.TloThreadLocalComponent;
import co.kr.abacus.base.common.tlo.properties.FuncIdVO;
import co.kr.abacus.base.common.tlo.properties.FuncIdsProperties;
import co.kr.abacus.base.common.useragent.UserAgentProperties;
import co.kr.abacus.base.common.useragent.UserAgentUtil;
import co.kr.abacus.base.common.useragent.UserDeviceVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Order의 숫자가 낮을수록 우선 순위
 */
@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class TloHttpLoggingFilter extends OncePerRequestFilter {

    private final TloLoggerWriter tloLoggerWriter;
    private final ObjectMapper objectMapper;
    private final FuncIdsProperties funcIdsProperties;
    private final UserAgentProperties userAgentProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        ReusableRequestWrapper requestWrapper = new ReusableRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        // ==============================================================================================
        // TLO 로그를 작성 여부 분기
        // ==============================================================================================
        if (requestWrapper.getRequestURI().contains("swagger-ui")
                || requestWrapper.getRequestURI().contains("api-docs")
                || requestWrapper.getRequestURI().contains("health")
                || requestWrapper.getRequestURI().contains("dont-write-me-tlo")
        ) {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } else {
            TloLoggerMessage.TloLoggerMessageBuilder tloLoggerMessageBuilder;
            String httpMethod = requestWrapper.getMethod();
            String requestURI = requestWrapper.getRequestURI();
            tloLoggerMessageBuilder = tslBuilder(requestWrapper);
            log.info("[REQUEST] - HTTP {} {}", httpMethod, requestURI);
            String requestBody = requestWrapper.getCachedBody();
            log.info("[REQUEST] - Request body: {}", requestBody);
            // 로그 작성시 필요한 데이터를 ThreadLocal에 담아서 생성함.
            // 다수 요청 발생시 데이터 꼬임 방지.
            TloThreadLocalComponent.setTloData(tloLoggerMessageBuilder.build());

            filterChain.doFilter(requestWrapper, responseWrapper);

            try {
                tloLoggerMessageBuilder = this.requestBodyLogging(requestWrapper, tloLoggerMessageBuilder);
                tloLoggerMessageBuilder = this.responseBodyLogging(responseWrapper, tloLoggerMessageBuilder);
            } finally {
                TloThreadLocalComponent.mergeWithBuilder(tloLoggerMessageBuilder);
                if (tloLoggerMessageBuilder != null) {
                    tloLoggerWriter.write(tloLoggerMessageBuilder
                            .rspTime(LocalDateTime.now())
                            .build());
                }
                try {
                    // tlo 로그 데이터 삭제
                    TloThreadLocalComponent.clearThreadLocal();
                    // log4j에서 사용하는 값 삭제 처리
                    ThreadContext.remove("logKey");
                } catch (Exception e) {
                    log.debug("clearThreadLocal Exception");
                }
            }
        }
        responseWrapper.copyBodyToResponse();
    }

    public TloLoggerMessage.TloLoggerMessageBuilder tslBuilder(HttpServletRequestWrapper requestWrapper) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String requestApiStr = requestWrapper.getRequestURI().replaceFirst(requestWrapper.getContextPath(), "");
        Optional<FuncIdVO> funcIdInfo = funcIdsProperties.getPermitAllFuncIds().stream().filter(o -> o.getMethod().equals(requestWrapper.getMethod()) && antPathMatcher.match(o.getUri(), requestApiStr)).findFirst();
        String funcId = "";
        if (funcIdInfo.isPresent()) {
            funcId = funcIdInfo.get().getFuncId();
        }

        UserDeviceVO userDeviceVO = UserAgentUtil.getDeviceInfo(requestWrapper.getHeader("user-agent"), userAgentProperties.getDevices());
        String devInfo = null;
        String osInfo = null;
        String modelInfo = null;
        if (userDeviceVO != null) {
            devInfo = userDeviceVO.getDevice();
            osInfo = userDeviceVO.getOs();
            modelInfo = userDeviceVO.getModel();
        }

        String logKey = Optional.ofNullable(requestWrapper.getHeader("x-logkey")).orElse(tloLoggerWriter.getSeqId());
        // log4j-local.xml에 SERVICE_LOG_PATTERN패턴 내에서 [%X{logKey}]에 사용하는 값.
        // 한 트랜잭션동안 같은 logKey를 가진 Service 로그가 생성되어야 TLO가 유의미함.
        ThreadContext.put("logKey", logKey);

        return TloLoggerMessage.builder()
                .reqTime(LocalDateTime.now())
                .clientIp(Optional.ofNullable(requestWrapper.getHeader("x-forwarded-for")).orElse(""))
                .devInfo(Optional.ofNullable(devInfo).orElse(""))
                .osInfo(Optional.ofNullable(osInfo).orElse(Optional.of(UserAgentUtil.getClientOS(requestWrapper.getHeader("user-agent"))).orElse("")))
                .nwInfo("ETC")
                .devModel(Optional.ofNullable(modelInfo).orElse(""))
                .carrierType("E")
                .logKey(logKey)
                .channelType("IN")
                .channel("WEB")
                .funcId(funcId)
                .command(Optional.ofNullable(requestWrapper.getHeader("x-command")).orElse(""));
    }

    private TloLoggerMessage.TloLoggerMessageBuilder requestBodyLogging(HttpServletRequestWrapper requestWrapper
            , TloLoggerMessage.TloLoggerMessageBuilder builder) {
        if (requestWrapper != null) {
            try {
                // FIXME 프로젝트에 맞게 수정 ---> SID 세팅 부분인데 Bizon에서는 '가입번호'를 사용했고
                // FIXME 샘플에서는 그냥 단순 Session에 저장된 userId를 사용토록 함
                HttpSession session = requestWrapper.getSession(false);
                String userId = session != null ? (String) session.getAttribute("userId") : null;
                if (StringUtils.isNotEmpty(userId)) {
                    builder.sid(userId);
                }
            } catch (Exception e) {
                log.debug("[METHOD][requestBodyLogging] HttpSession Exception - {}", e.getMessage());
            }

        }

        return builder;
    }

    private TloLoggerMessage.TloLoggerMessageBuilder responseBodyLogging(ContentCachingResponseWrapper responseWrapper
            , TloLoggerMessage.TloLoggerMessageBuilder builder) throws IOException {
        String payload;
        if (responseWrapper != null) {
            if (StringUtils.isNotEmpty(responseWrapper.getContentType()) && responseWrapper.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
                byte[] buf = responseWrapper.getContentAsByteArray();
                if (buf.length > 0) {
                    try {
                        payload = new String(buf, StandardCharsets.UTF_8);
                        APIResponse result = objectMapper.readValue(payload, APIResponse.class);
                        builder.resultCode(result.code());
                        log.info("[RESPONSE][BODY] - {}", result);
                    } catch (Exception e) {
                        log.error("[METHOD][responseBodyLogging] Exception - {}", e.getMessage());
                    }
                } else {
                    log.debug("[RESPONSE][BODY] - EMPTY");
                }
            }
        }

        return builder;
    }

}