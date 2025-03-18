package co.kr.abacus.base.api.sample.controller;

import co.kr.abacus.base.api.sample.dto.ExceptionSampleDTO;
import co.kr.abacus.base.api.sample.dto.ExceptionSampleReqDTO;
import co.kr.abacus.base.common.exception.GlobalExceptionResponseEnum;
import co.kr.abacus.base.common.exception.MissingRequirementDataException;
import co.kr.abacus.base.common.response.APIResponse;
import co.kr.abacus.base.common.response.CommonResponseEnum;
import co.kr.abacus.base.common.validator.group.OnCreate;
import co.kr.abacus.base.common.validator.group.OnUpdate;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@RestController
@RequestMapping("/exception")
@Tag(name = "3. Exception Test", description = "Exception 테스트 API")
public class ExceptionSampleController {

    // 1. 일반적인 서버 내부 에러 (500)
    @GetMapping("/internal-server-error")
    public APIResponse<Void> internalServerErrorTest() {
        throw new RuntimeException("내부 서버 오류 테스트");
    }

    // HttpClientErrorException.Unauthorized (401)
    @GetMapping("/unauthorized")
    public APIResponse<Void> unauthorizedTest() {
        throw HttpClientErrorException.Unauthorized.create(HttpStatus.UNAUTHORIZED,  null, null, null, null);
    }

    // HttpClientErrorException.Forbidden (403)
    @GetMapping("/forbidden")
    public APIResponse<Void> forbiddenTest() {
        throw HttpClientErrorException.Forbidden.create(HttpStatus.FORBIDDEN, "Forbidden", null, null, null);
    }

    // HttpServerErrorException (5xx)
    @GetMapping("/server-error")
    public APIResponse<Void> serverErrorTest() {
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 테스트");
    }

    // NoHandlerFoundException
    // 이 예외는 존재하지 않는 URL 호출 시 자동 발생하므로 별도 메소드 작성 불필요.

    // MissingServletRequestParameterException
    @GetMapping("/missing-param")
    public APIResponse<Void> missingParameterTest(@RequestParam("requiredParam") String requiredParam) {
        return APIResponse.success(CommonResponseEnum.SUCCESS);
    }

    // HandlerMethodValidationException
    @GetMapping("/handler-validation")
    public APIResponse<Void> handlerValidationTest(@RequestParam @Min(10) int value) {
        return APIResponse.success(CommonResponseEnum.SUCCESS);
    }

    // MethodArgumentNotValidException
    @PostMapping("/method-argument-validation")
    public APIResponse<Void> methodArgumentValidationTest(@Valid @RequestBody ExceptionSampleReqDTO dto) {
        return APIResponse.success(CommonResponseEnum.SUCCESS);
    }

    // BindException
    @GetMapping("/bind-exception/create")
    public APIResponse<Void> bindExceptionTest(@Validated(OnCreate.class) @RequestBody ExceptionSampleDTO dto) {
        return APIResponse.success(CommonResponseEnum.SUCCESS);
    }

    @GetMapping("/bind-exception/update")
    public APIResponse<Void> bindExceptionTest2(@Validated(OnUpdate.class) @RequestBody ExceptionSampleDTO dto) {
        return APIResponse.success(CommonResponseEnum.SUCCESS);
    }

    // Custom Exception: MissingRequirementDataException
    @GetMapping("/missing-requirement-exception")
    public APIResponse<Void> missingRequirementDataTest() {
        throw new MissingRequirementDataException(GlobalExceptionResponseEnum.BAD_REQUEST, null);
    }

}
