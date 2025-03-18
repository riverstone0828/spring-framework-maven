package co.kr.abacus.base.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GlobalExceptionResponseEnum implements ExceptionResponseContract {
    BAD_REQUEST("40000000", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    PLEASE_CHECK_REQUEST_VALUES("40000000", HttpStatus.BAD_REQUEST, "요청 값을 확인해주세요."),
    BAD_REQUEST_CUSTOM_MESSAGE("40000000", HttpStatus.BAD_REQUEST, "%s"),
    UNAUTHORIZED("40100000", HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다."),
    FORBIDDEN("40300000", HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    NOT_FOUND("40400000", HttpStatus.NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED("40500000", HttpStatus.METHOD_NOT_ALLOWED, "요청하신 HTTP 메서드는 허용되지 않습니다."),
    IM_A_TEAPOT("41800000", HttpStatus.I_AM_A_TEAPOT, "찻주전자로는 커피를 만들 수 없습니다."),
    INTERNAL_SERVER_ERROR("50000000", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러가 발생하였습니다."),
    SERVICE_UNAVAILABLE("50300000", HttpStatus.SERVICE_UNAVAILABLE, "서비스가 잠시 사용 불가능합니다."),

    BIZON_UNAUTHORIZED_EXCEPTION("40100001", HttpStatus.UNAUTHORIZED, "세션이 존재하지 않습니다."),
    BIZON_FORBIDDEN_EXCEPTION("40300001", HttpStatus.FORBIDDEN, "사용자가 인증되지 않았습니다."),
    ;

    private final String code;
    private final HttpStatus statusCode;
    private final String message;

    GlobalExceptionResponseEnum(String code, HttpStatus statusCode, String message) {
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
    }
}
