package co.kr.abacus.base.api.sample.controller;

import co.kr.abacus.base.api.sample.service.BqSampleService;
import co.kr.abacus.base.common.response.APIResponse;
import co.kr.abacus.base.common.response.CommonResponseEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/bq")
@Tag(name = "2. BigQuery", description = "BigQuery Sample")
@RequiredArgsConstructor
public class BqSampleController {

    private final BqSampleService bqSampleService;

    @GetMapping("/simba/mybatis")
    public APIResponse<Void> getTaSampleData() {
        bqSampleService.getTaSampleData();
        return APIResponse.success(CommonResponseEnum.SUCCESS);
    }
}
