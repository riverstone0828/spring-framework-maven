package co.kr.abacus.base.common.enums;

import co.kr.abacus.base.common.exception.ExceptionResponseContract;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonExceptionResponseEnum implements ExceptionResponseContract {
    // ####################################################################################
    // # 기본 에러
    // ####################################################################################
    BAD_REQUEST("40000000", HttpStatus.BAD_REQUEST, "잘못된 요청입니다.")
    , UNAUTHORIZED( "4010000", HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다." )
    , FORBIDDEN("4030000", HttpStatus.FORBIDDEN, "접근이 거부되었습니다." )
    , NOT_FOUND("4040000", HttpStatus.NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다." )
    , METHOD_NOT_ALLOWED("4050000", HttpStatus.METHOD_NOT_ALLOWED, "요청하신 HTTP 메소드는 허용되지 않습니다." )
    , INTERNAL_SERVER_ERROR("5000000", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러가 발생하였습니다." )
    , SERVICE_UNAVAILABLE("5030000", HttpStatus.SERVICE_UNAVAILABLE, "서비스가 잠시 사용 불가능합니다." )

    // ####################################################################################
    // # 프로젝트 특화 에러
    // ####################################################################################


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
