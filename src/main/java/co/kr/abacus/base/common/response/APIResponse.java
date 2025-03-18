package co.kr.abacus.base.common.response;

import co.kr.abacus.base.common.exception.ExceptionResponseContract;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public record APIResponse<T>(
        String timestamp,
        int statusCode,
        String code,
        String message,
        T data,
        @JsonIgnore
        HttpStatus status

) {

    public static <T> APIResponse<T> success(APIResponseContract response) {
        return createResponse(response, null, null);
    }

    public static <T> APIResponse<T> success(APIResponseContract response, T data) {
        return createResponse(response, null, data);
    }

    public static <T> APIResponse<T> success(APIResponseContract response, String... args) {
        return createResponse(response, args, null);
    }

    public static <T> APIResponse<T> success(APIResponseContract response, T data, String... args) {
        return createResponse(response, args, data);
    }

    public static <T> APIResponse<T> fail(ExceptionResponseContract response) {
        return createResponse(response, null, null);
    }

    public static <T> APIResponse<T> fail(ExceptionResponseContract response, String... args) {
        return createResponse(response, args, null);
    }

    public static <T> APIResponse<T> fail(ExceptionResponseContract response, T errorMessage) {
        return createResponse(response, null, errorMessage);
    }

    public static <T> APIResponse<T> fail(ExceptionResponseContract response, T errorMessage, String... args) {
        return createResponse(response, args, errorMessage);
    }

    private static String getMessage(APIResponseContract response, String... args) {
        String message = response.getMessage();
        if (message == null || message.isEmpty()) {
            return response.getStatusCode().getReasonPhrase();
        }
        if (args != null && args.length > 0) {
            return String.format(message, (Object[]) args);
        }
        return message;
    }

    private static <T> APIResponse<T> createResponse(APIResponseContract response, String[] args, T data) {
        String message = getMessage(response, args);
        return new APIResponse<>(
                ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),
                response.getStatusCode().value(),
                response.getCode(),
                message,
                data,
                response.getStatusCode()
        );
    }

    private static String getMessage(ExceptionResponseContract response, String... args) {
        String message = response.getMessage();
        if (message == null || message.isEmpty()) {
            return response.getStatusCode().getReasonPhrase();
        }
        if (args != null && args.length > 0) {
            return String.format(message, (Object[]) args);
        }
        return message;
    }

    private static <T> APIResponse<T> createResponse(ExceptionResponseContract response, String[] args, T data) {
        String message = getMessage(response, args);
        return new APIResponse<>(
                ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),
                response.getStatusCode().value(),
                response.getCode(),
                message,
                data,
                response.getStatusCode()
        );
    }

}