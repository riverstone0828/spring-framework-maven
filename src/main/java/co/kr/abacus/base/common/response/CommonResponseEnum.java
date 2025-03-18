package co.kr.abacus.base.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonResponseEnum implements APIResponseContract {
    SUCCESS("20000000", HttpStatus.OK, "성공"),
    ;

    private final String code;
    private final HttpStatus statusCode;
    private final String message;

    CommonResponseEnum(String code, HttpStatus statusCode, String message) {
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
    }
}