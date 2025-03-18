package co.kr.abacus.base.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionResponseContract {
    String getCode();

    HttpStatus getStatusCode();

    String getMessage();
}
