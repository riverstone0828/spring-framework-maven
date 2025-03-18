package co.kr.abacus.base.common.response;

import org.springframework.http.HttpStatus;

public interface APIResponseContract {
    String getCode();

    HttpStatus getStatusCode();

    String getMessage();
}
