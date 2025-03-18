package co.kr.abacus.base.common.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class MissingRequirementDataException extends RuntimeException {
    private final ExceptionResponseContract exceptionResponseContract;
    private final List<MissingRequirementDataDetail> missingRequirementDataDetails;
    private String[] args;
    private Exception exception;

    public MissingRequirementDataException(ExceptionResponseContract exceptionResponseContract, List<MissingRequirementDataDetail> missingRequirementDataDetails) {
        this.exceptionResponseContract = exceptionResponseContract;
        this.missingRequirementDataDetails = missingRequirementDataDetails;
    }

    public MissingRequirementDataException(ExceptionResponseContract exceptionResponseContract, List<MissingRequirementDataDetail> missingRequirementDataDetails, Exception exception) {
        this.exceptionResponseContract = exceptionResponseContract;
        this.missingRequirementDataDetails = missingRequirementDataDetails;
        this.exception = exception;
    }

    public MissingRequirementDataException(ExceptionResponseContract exceptionResponseContract, List<MissingRequirementDataDetail> missingRequirementDataDetails, String... args) {
        this.exceptionResponseContract = exceptionResponseContract;
        this.missingRequirementDataDetails = missingRequirementDataDetails;
        this.args = args;
    }
}
