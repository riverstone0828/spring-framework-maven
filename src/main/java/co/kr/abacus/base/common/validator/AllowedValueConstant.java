package co.kr.abacus.base.common.validator;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AllowedValueConstant {

    @Getter
    @AllArgsConstructor
    public enum AllowedGenders {
        MALE("male"), FEMALE("female");
        private final String code;


    }

    @Getter
    @AllArgsConstructor
    public enum AllowedGenderNumbers {
        MALE(1), FEMALE(2);
        private final int code;
    }
}
