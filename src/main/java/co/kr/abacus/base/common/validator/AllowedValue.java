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

    Class<? extends Enum<?>> allowedValuesEnumClass() default DefaultEnum.class; // Enum 클래스 타입

    Class<? extends Enum<?>> excludedValuesEnumClass() default DefaultEnum.class; // Enum 클래스 타입

    String enumField() default "code"; // Enum 클래스 내에서 사용할 필드명 (기본값은 name)

    // 기본 빈 enum 클래스 (기본값 설정을 위해 사용)
    enum DefaultEnum {}

    @Deprecated
    String[] allowedValues() default {};

    @Deprecated
    String[] excludedValues() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
