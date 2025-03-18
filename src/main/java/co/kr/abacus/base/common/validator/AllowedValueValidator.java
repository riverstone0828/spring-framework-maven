package co.kr.abacus.base.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

public class AllowedValueValidator implements ConstraintValidator<AllowedValue, String> {

    private Set<String> allowedValues;

    @Override
    public void initialize(AllowedValue constraintAnnotation) {
        List<String> allowedValueList = new ArrayList<>();

        if (StringUtils.hasText(constraintAnnotation.cmmCdId())) {
//            List<String> dbManagedAllowedValues = commonDao.selectList("fileName.queryId", constraintAnnotation.cmmCdId());
//            allowedValueList.addAll(dbManagedAllowedValues);
        }

        if (!ObjectUtils.isEmpty(constraintAnnotation.allowedValues())) {
            List<String> allowedValues = Arrays.asList(constraintAnnotation.allowedValues());
            allowedValueList.addAll(allowedValues);
        }

        if (!ObjectUtils.isEmpty(constraintAnnotation.excludedValues())) {
            List<String> excludedValues = Arrays.asList(constraintAnnotation.excludedValues());
            allowedValueList.removeAll(excludedValues);
        }

        allowedValues = new HashSet<>(allowedValueList);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(allowedValues.toString() + "중에 하나만 허용됩니다.").addConstraintViolation();
        if (s == null) {
            return true; // null은 @NotNull로 처리 // 필수값은 아니나 값을 초기화할 때 지정된 값만 사용해야하는 경우를 고려함.
        }
        return allowedValues.contains(s);
    }
}
