package co.kr.abacus.base.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class AllowedValueValidator implements ConstraintValidator<AllowedValue, Object> {

    private Set<String> allowedValues;

    @Override
    public void initialize(AllowedValue constraintAnnotation) {
        List<String> allowedValueList = new ArrayList<>();

        // ###############################################################################
        // # TODO 공통 코드로 관리하는 경우 조회 로직 구현
        // ###############################################################################
        if (StringUtils.hasText(constraintAnnotation.cmmCdId())) {
//            List<String> dbManagedAllowedValues = commonDao.selectList("fileName.queryId", constraintAnnotation.cmmCdId());
//            allowedValueList.addAll(dbManagedAllowedValues);
        }

        // ###############################################################################
        // # allowedValuesEnumClass가 지정된 경우 enum의 값을 allowedValues에 추가.
        // ###############################################################################
        if (!constraintAnnotation.allowedValuesEnumClass().equals(AllowedValue.DefaultEnum.class)) {
            allowedValueList.addAll(getEnumValues(constraintAnnotation.allowedValuesEnumClass(), constraintAnnotation.enumField()));
        }

        // ###############################################################################
        // # excludedValuesEnumClass가 지정된 경우 enum의 값을 allowedValues에서 제거.
        // ###############################################################################
        if (!constraintAnnotation.excludedValuesEnumClass().equals(AllowedValue.DefaultEnum.class)) {
            allowedValueList.removeAll(getEnumValues(constraintAnnotation.allowedValuesEnumClass(), constraintAnnotation.enumField()));
        }

        allowedValues = new HashSet<>(allowedValueList);
    }

    @Override
    public boolean isValid(Object s, ConstraintValidatorContext context) {
        if (s == null) return true;

        boolean valid = allowedValues.contains(s.toString());

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    allowedValues.toString() + " 중 하나만 허용됩니다."
            ).addConstraintViolation();
        }

        return valid;
    }

    /**
     * Enum 클래스에서 지정된 필드의 값을 추출하여 리스트로 반환하는 메소드
     */
    private List<String> getEnumValues(Class<? extends Enum<?>> e, String fieldName) {
        return Arrays.stream(e.getEnumConstants())
                .map(enumConstant -> {
                    try {
                        Field field = e.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object value = field.get(enumConstant);
                        return value.toString();
                    } catch (NoSuchFieldException | IllegalAccessException ex) {
                        throw new RuntimeException("Enum 필드 접근 오류", ex);
                    }
                })
                .collect(Collectors.toList());
    }
}
