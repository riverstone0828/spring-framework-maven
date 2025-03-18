package co.kr.abacus.base.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ExceptionResponseContract exceptionResponseContract;
    private String[] args;
    private Exception exception;

    public BusinessException(ExceptionResponseContract exceptionResponseContract, String... args) {
        this.exceptionResponseContract = exceptionResponseContract;
        this.args = args;
    }

    public BusinessException(ExceptionResponseContract exceptionResponseContract) {
        this.exceptionResponseContract = exceptionResponseContract;
    }

    public BusinessException(ExceptionResponseContract exceptionResponseContract, Exception exception) {
        this.exceptionResponseContract = exceptionResponseContract;
        this.exception = exception;
    }

    public BusinessException(ExceptionResponseContract exceptionResponseContract, Exception exception, String... args) {
        this.exceptionResponseContract = exceptionResponseContract;
        this.exception = exception;
        this.args = args;
    }
}
