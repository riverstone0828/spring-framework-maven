package co.kr.abacus.base.api.sample.controller;

import co.kr.abacus.base.api.sample.dto.ValidatorSampleDTO;
import co.kr.abacus.base.common.enums.CommonResponseEnum;
import co.kr.abacus.base.common.response.APIResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validator")
@Tag(name = "4. Validator", description = "Validator Sample")
@RequiredArgsConstructor
public class ValidatorSampleController {

    @PostMapping("/method-argument-validation")
    public APIResponse<Void> methodArgumentValidationTest(@Validated @RequestBody ValidatorSampleDTO dto) {
        return APIResponse.success(CommonResponseEnum.OK);
    }
}
