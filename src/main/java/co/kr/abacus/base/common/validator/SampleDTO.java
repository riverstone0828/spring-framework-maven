package co.kr.abacus.base.common.validator;

public class SampleDTO {

    @AllowedValue(cmmCdId = "COMMON_CODE_ID")
    private String cmmCdIdManaged;

    @AllowedValue(allowedValues = {"MALE", "FEMALE"})
    private String gender;

    // 공통 코드 관리 데이터 중에 일부를 불허하고싶은 경우.
    @AllowedValue(cmmCdId = "COMMON_CODE_ID", excludedValues = {"ETC"})
    private String excluedValue;
}
