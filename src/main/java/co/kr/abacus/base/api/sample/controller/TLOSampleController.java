package co.kr.abacus.base.api.sample.controller;

import co.kr.abacus.base.common.response.APIResponse;
import co.kr.abacus.base.common.response.CommonResponseEnum;
import co.kr.abacus.base.api.sample.dto.TLOSampleDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(("/tlo"))
@Tag(name = "1. TLO", description = "TLO Sample")
@RequiredArgsConstructor
public class TLOSampleController {

    @PostMapping("/set-log-key-header")
    public APIResponse<Void> setLogKeyHeader(
            @RequestHeader("x-logkey") String logKey) {
        return APIResponse.success(CommonResponseEnum.SUCCESS);
    }

    @PostMapping("/write-me-tlo")
    public APIResponse<Void> writeMeTlo() {
        return APIResponse.success(CommonResponseEnum.SUCCESS);
    }

    @PostMapping("/dont-write-me-tlo")
    public APIResponse<Void> dontWriteMeTlo() {
        return APIResponse.success(CommonResponseEnum.SUCCESS);
    }

    @PostMapping("/create-session")
    public APIResponse<Void> createSession(@RequestBody TLOSampleDTO TLOSampleDTO, HttpSession session) {
        String userId = TLOSampleDTO.getUserId();
        session.setAttribute("userId", userId);
        return APIResponse.success(CommonResponseEnum.SUCCESS);
    }

    @PostMapping("/result-code-fail")
    public APIResponse<Void> resultCodeFail() {
        return APIResponse.success(CommonResponseEnum.SUCCESS);
    }
}
