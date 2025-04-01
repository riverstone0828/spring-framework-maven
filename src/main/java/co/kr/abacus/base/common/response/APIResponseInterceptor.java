package co.kr.abacus.base.common.response;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice
public class APIResponseInterceptor implements ResponseBodyAdvice<APIResponse<?>> {

    @Override
    public boolean supports(MethodParameter returnType,
            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getParameterType() == APIResponse.class;
    }

    @Override
    public APIResponse<?> beforeBodyWrite(
            APIResponse<?> apiResponse,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response) {
        if (apiResponse != null) {
            if (isErrorResponse(apiResponse)) {
                response.getHeaders().add("BizError", "Y");
            } else {
                response.getHeaders().add("BizError", "N");
            }
            response.setStatusCode(apiResponse.status());
        }
        return apiResponse;
    }

    private boolean isErrorResponse(APIResponse<?> response) {
        // 4xx 또는 5xx 상태 코드를 오류로 간주
        return response.statusCode() >= 400;
    }

}