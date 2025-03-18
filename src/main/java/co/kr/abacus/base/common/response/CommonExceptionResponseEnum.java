package co.kr.abacus.base.common.response;

import co.kr.abacus.base.common.exception.ExceptionResponseContract;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonExceptionResponseEnum implements ExceptionResponseContract {
    INTERNAL_SERVER_ERROR("50000000", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러가 발생하였습니다.")
    , BAD_REQUESTED("50000001", HttpStatus.BAD_REQUEST, "잘못된 요청입니다.")
    , UNAUTHORIZED("50000002", HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다.")
    , FORBIDDEN("50000003", HttpStatus.FORBIDDEN, "리소스에 접근할 수 없습니다.")
    , NOT_FOUND("50000004", HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다.")
    , METHOD_NOT_ALLOWED("50000005", HttpStatus.METHOD_NOT_ALLOWED, "요청한 HTTP Method 가 올바르지 않습니다.")
    , NOT_ACCEPTABLE("50000006", HttpStatus.NOT_ACCEPTABLE, "올바르지 않은 미디어 형식입니다.")
    , INVALID_PROPERTIES("50000007", HttpStatus.INTERNAL_SERVER_ERROR, "OAuth 토큰 발급에 필요한 설정 값이 누락되었습니다.")
    , INVALID_TOKEN("50000008", HttpStatus.INTERNAL_SERVER_ERROR, "액세스 토큰이 유효하지 않습니다.")
    , NO_FALLBACK_AVAILABLE("50000009", HttpStatus.INTERNAL_SERVER_ERROR, "서비스에 장애가 발생하였습니다.")
    , INVALID_HMAC("50000010", HttpStatus.INTERNAL_SERVER_ERROR, "Hmac 인증 실패")

    ;

    private final String code;
    private final HttpStatus statusCode;
    private final String message;

    CommonExceptionResponseEnum(String code, HttpStatus statusCode, String message) {
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
    }
}
