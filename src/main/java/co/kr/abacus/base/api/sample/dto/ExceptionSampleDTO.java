package co.kr.abacus.base.api.sample.dto;

import co.kr.abacus.base.common.validator.group.OnCreate;
import co.kr.abacus.base.common.validator.group.OnUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionSampleDTO {

    @Min(value = 1, message = "id는 1 이상의 값을 가져야 합니다.", groups = {OnUpdate.class})
    private Long id;

    @NotBlank(message = "name은 필수 입력 값입니다.", groups = {OnCreate.class, OnUpdate.class})
    private String name;

}
