package co.kr.abacus.base.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import co.kr.abacus.base.common.response.APIResponseContract;

@Getter
public enum CommonResponseEnum implements APIResponseContract {
    OK("20000000", HttpStatus.OK, "성공"),
    CREATED("20000000", HttpStatus.CREATED, "생성됨"),
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