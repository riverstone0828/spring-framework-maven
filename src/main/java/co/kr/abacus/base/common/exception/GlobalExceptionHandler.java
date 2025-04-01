package co.kr.abacus.base.common.exception;

import co.kr.abacus.base.common.enums.CommonExceptionResponseEnum;
import co.kr.abacus.base.common.response.APIResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public APIResponse<?> exceptionHandle(HttpServletRequest request, Exception e) {
        log.error("[GlobalExceptionHandler.exceptionHandle] {}", request.getRequestURI());
        errorLogging(e, request);
        return APIResponse.fail(CommonExceptionResponseEnum.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = HttpClientErrorException.Unauthorized.class)
    public APIResponse<?> unauthorizedException() {
        log.error("[GlobalExceptionHandler.unauthorizedException]");
        return APIResponse.fail(CommonExceptionResponseEnum.UNAUTHORIZED);
    }

    @ExceptionHandler(value = HttpClientErrorException.Forbidden.class)
    public APIResponse<?> forbiddenException() {
        log.error("[GlobalExceptionHandler.forbiddenException]");
        return APIResponse.fail(CommonExceptionResponseEnum.FORBIDDEN);
    }

    @ExceptionHandler(value = HttpServerErrorException.class)
    public APIResponse<?> httpServerErrorException(HttpServletRequest request, HttpServerErrorException e) {
        log.error("[GlobalExceptionHandler.httpServerErrorException] {}", request.getRequestURI());
        errorLogging(e, request);
        return APIResponse.fail(CommonExceptionResponseEnum.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public APIResponse<?> noResourceFoundException(HttpServletRequest request, NoResourceFoundException e) {
        log.error("[GlobalExceptionHandler.noResourceFoundException] {}", request.getRequestURI());
        errorLogging(e, request);
        return APIResponse.fail(CommonExceptionResponseEnum.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public APIResponse<?> noHandlerFoundException(HttpServletRequest request, NoHandlerFoundException e) {
        log.error("[GlobalExceptionHandler.noHandlerFoundException] {}", request.getRequestURI());
        errorLogging(e, request);
        return APIResponse.fail(CommonExceptionResponseEnum.NOT_FOUND);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public APIResponse<?> missingServletRequestParameterException(HttpServletRequest request,
            MissingServletRequestParameterException e) {
        log.error("[GlobalExceptionHandler.missingServletRequestParameterException] {}", request.getRequestURI());
        String message = String.format("'%s' 파라미터가 누락되었습니다.", e.getParameterName());
        log.error("[GlobalExceptionHandler.missingServletRequestParameterException] {} : {}", request.getRequestURI(),
                message);
        return APIResponse.fail(CommonExceptionResponseEnum.BAD_REQUEST, message);
    }

    @ExceptionHandler(value = { HandlerMethodValidationException.class })
    public APIResponse<?> handlerMethodValidationException(HttpServletRequest request,
            HandlerMethodValidationException e) {
        log.error("[GlobalExceptionHandler.handlerMethodValidationException] {}", request.getRequestURI());
        String errorMessage = e.getValueResults().stream()
                .flatMap(allValidationResult -> allValidationResult.getResolvableErrors().stream())
                .map(error -> String.format("%s : %s", getFirstElement(error.getCodes()), error.getDefaultMessage()))
                .collect(Collectors.joining("\n"));
        log.error("[GlobalExceptionHandler.handlerMethodValidationException] {} : {}", request.getRequestURI(),
                errorMessage);
        return APIResponse.fail(CommonExceptionResponseEnum.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(value = { BindException.class, ConstraintViolationException.class,
            MethodArgumentNotValidException.class })
    public APIResponse<?> bindException(HttpServletRequest request, BindException e) {
        log.error("[GlobalExceptionHandler.bindException] {}", request.getRequestURI());
        String aggregatedErrors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("[%s : %s -> %s]", error.getField(), error.getDefaultMessage(),
                        error.getRejectedValue()))
                .collect(Collectors.joining("\n"));
        String detailedErrorMessage = String.format("요청 값을 확인해주세요. %s", aggregatedErrors);
        log.error("[GlobalExceptionHandler.bindException] {} : {}", request.getRequestURI(), detailedErrorMessage);
        return APIResponse.fail(CommonExceptionResponseEnum.BAD_REQUEST, detailedErrorMessage);
    }

    @ExceptionHandler(value = BusinessException.class)
    public APIResponse<?> businessException(HttpServletRequest request, BusinessException e) {
        log.error("[GlobalExceptionHandler.businessException] {}", request.getRequestURI());
        errorLogging(e.getException(), request);
        if (e.getException() != null) {
            if (e.getArgs() != null && e.getArgs().length > 0) {
                log.error("[GlobalExceptionHandler.businessException] {} . {}", e.getException().getMessage(),
                        e.getArgs());
                return APIResponse.fail(e.getExceptionResponseContract(), e.getArgs());
            } else {
                return APIResponse.fail(e.getExceptionResponseContract());
            }
        } else if (e.getArgs() != null && e.getArgs().length > 0) {
            return APIResponse.fail(e.getExceptionResponseContract(), e.getArgs());
        } else {
            return APIResponse.fail(e.getExceptionResponseContract());
        }
    }

    private String getFirstElement(String[] elements) {
        return (elements != null && elements.length > 0) ? elements[0] : "";
    }

    private void errorLogging(Exception e, HttpServletRequest request) {
        log.error("[GlobalExceptionHandler.errorLogging] : {}", request.getRequestURI(), e);
    }
}
