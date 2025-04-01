package co.kr.abacus.base.api.sample.dto;

import co.kr.abacus.base.common.validator.AllowedValue;
import co.kr.abacus.base.common.validator.AllowedValueConstant;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidatorSampleDTO {

    @AllowedValue(cmmCdId = "COMMON_CODE_ID")
    private String cmmCdIdManaged;

    @AllowedValue(allowedValuesEnumClass = AllowedValueConstant.AllowedGenders.class)
    private String gender;

    @AllowedValue(allowedValuesEnumClass = AllowedValueConstant.AllowedGenderNumbers.class)
    private int genderNumber;

    @NotBlank(message = "이름은 필수입니다")
    private String name;
}
