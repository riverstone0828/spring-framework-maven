package co.kr.abacus.base.common.tlo.filter;

import co.kr.abacus.base.common.response.APIResponse;
import co.kr.abacus.base.common.tlo.ReusableRequestWrapper;
import co.kr.abacus.base.common.tlo.TloLoggerMessage;
import co.kr.abacus.base.common.tlo.TloLoggerWriter;
import co.kr.abacus.base.common.tlo.TloThreadLocalComponent;
import co.kr.abacus.base.common.tlo.properties.FuncIdVO;
import co.kr.abacus.base.common.tlo.properties.FuncIdsProperties;
import co.kr.abacus.base.common.useragent.DeviceInfo;
import co.kr.abacus.base.common.useragent.UserAgentParser;
import com.fasterxml.jackson.core.type.TypeReference;
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

    private static final String NETWORK_TYPE_ETC = "ETC";
    private static final String CARRIER_TYPE_E = "E";
    private static final String CHANNEL_TYPE_IN = "IN";
    private static final String CHANNEL_WEB = "WEB";
    private static final String LOG_KEY = "logKey";
    private static final String USER_ID = "userId";
    private static final String X_LOG_KEY = "x-logkey";
    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    private static final String X_COMMAND = "x-command";
    private static final String USER_AGENT = "user-agent";

    private final TloLoggerWriter tloLoggerWriter;
    private final ObjectMapper objectMapper;
    private final FuncIdsProperties funcIdsProperties;
    private final UserAgentParser userAgentParser;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.contains("swagger-ui")
                || requestURI.contains("api-docs")
                || requestURI.contains("health")
                || requestURI.contains("dont-write-me-tlo");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ReusableRequestWrapper requestWrapper = new ReusableRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        TloLoggerMessage.TloLoggerMessageBuilder tloLoggerMessageBuilder;
        String httpMethod = requestWrapper.getMethod();
        String requestURI = requestWrapper.getRequestURI();
        tloLoggerMessageBuilder = tslBuilder(requestWrapper);
        log.info("[REQUEST] - HTTP {} {}", httpMethod, requestURI);
        String requestBody = requestWrapper.getCachedBody();
        log.info("[REQUEST] - Request body: {}", requestBody);

        TloThreadLocalComponent.setTloData(tloLoggerMessageBuilder.build());

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
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
                TloThreadLocalComponent.clearThreadLocal();
                ThreadContext.remove("logKey");
            } catch (Exception e) {
                log.debug("clearThreadLocal Exception");
            }
        }

        responseWrapper.copyBodyToResponse();
    }

    public TloLoggerMessage.TloLoggerMessageBuilder tslBuilder(HttpServletRequestWrapper requestWrapper) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String requestApiStr = requestWrapper.getRequestURI().replaceFirst(requestWrapper.getContextPath(), "");
        Optional<FuncIdVO> funcIdInfo = funcIdsProperties.getPermitAllFuncIds().stream()
                .filter(o -> o.getMethod().equals(requestWrapper.getMethod())
                        && antPathMatcher.match(o.getUri(), requestApiStr))
                .findFirst();
        String funcId = funcIdInfo.map(FuncIdVO::getFuncId).orElse("");

        DeviceInfo deviceInfo = userAgentParser.parseUserAgent(requestWrapper.getHeader(USER_AGENT));

        String logKey = Optional.ofNullable(requestWrapper.getHeader(X_LOG_KEY)).orElse(tloLoggerWriter.getSeqId());
        ThreadContext.put(LOG_KEY, logKey);

        return TloLoggerMessage.builder()
                .reqTime(LocalDateTime.now())
                .clientIp(Optional.ofNullable(requestWrapper.getHeader(X_FORWARDED_FOR)).orElse(""))
                .devInfo(deviceInfo.device())
                .osInfo(deviceInfo.os())
                .nwInfo(NETWORK_TYPE_ETC)
                .devModel(deviceInfo.model())
                .carrierType(CARRIER_TYPE_E)
                .logKey(logKey)
                .channelType(CHANNEL_TYPE_IN)
                .channel(CHANNEL_WEB)
                .funcId(funcId)
                .command(Optional.ofNullable(requestWrapper.getHeader(X_COMMAND)).orElse(""));
    }

    private TloLoggerMessage.TloLoggerMessageBuilder requestBodyLogging(HttpServletRequestWrapper requestWrapper,
            TloLoggerMessage.TloLoggerMessageBuilder builder) {
        if (requestWrapper != null) {
            try {
                HttpSession session = requestWrapper.getSession(false);
                String userId = session != null ? (String) session.getAttribute(USER_ID) : null;
                if (StringUtils.isNotEmpty(userId)) {
                    builder.sid(userId);
                }
            } catch (Exception e) {
                log.debug("[METHOD][requestBodyLogging] HttpSession Exception - {}", e.getMessage());
            }
        }
        return builder;
    }

    private TloLoggerMessage.TloLoggerMessageBuilder responseBodyLogging(ContentCachingResponseWrapper responseWrapper,
            TloLoggerMessage.TloLoggerMessageBuilder builder) throws IOException {
        String payload;
        if (responseWrapper != null) {
            if (StringUtils.isNotEmpty(responseWrapper.getContentType())
                    && responseWrapper.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
                byte[] buf = responseWrapper.getContentAsByteArray();
                if (buf.length > 0) {
                    try {
                        payload = new String(buf, StandardCharsets.UTF_8);
                        APIResponse<Object> result = objectMapper.readValue(payload,
                                new TypeReference<APIResponse<Object>>() {
                                });
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