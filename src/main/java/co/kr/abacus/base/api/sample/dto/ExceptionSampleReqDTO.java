package co.kr.abacus.base.api.sample.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionSampleReqDTO {

    @NotNull(message = "id는 필수 입력 값입니다.")
    private Long id;

    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    @Size(min = 3, max = 50, message = "name은 최소 3자에서 최대 50자까지 입력 가능합니다.")
    private String name;

    @Size(max = 200, message = "description은 최대 200자까지 입력 가능합니다.")
    private String description;

}
