package co.kr.abacus.base.api.sample.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionSampleDTO {
    @Min(value = 1, message = "id는 1 이상의 값을 가져야 합니다.")
    private Long id;

    @NotBlank(message = "name은 필수 입력 값입니다.")
    private String name;
}
