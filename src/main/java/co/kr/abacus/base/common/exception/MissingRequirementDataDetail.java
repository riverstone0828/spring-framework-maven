package co.kr.abacus.base.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MissingRequirementDataDetail {
    private String key;
    private String data;
    private String message;
}
