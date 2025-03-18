package co.kr.abacus.base.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {AllowedValueValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedValue {
    String message() default "허용되지 않은 값이 포함되어 있습니다.";

    String cmmCdId() default "";    // DB로 유효값을 제어하는 경우 그 유효값의 그룹 key

    String[] allowedValues() default {};

    String[] excludedValues() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
