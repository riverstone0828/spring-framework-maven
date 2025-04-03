package co.kr.abacus.base.common.tlo.controller;

import co.kr.abacus.base.common.enums.CommonResponseEnum;
import co.kr.abacus.base.common.response.APIResponse;
import co.kr.abacus.base.common.useragent.DeviceInfo;
import co.kr.abacus.base.common.useragent.UserAgentParser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tlo-test")
@RequiredArgsConstructor
public class TloTestController {

    private final UserAgentParser userAgentParser;

    @GetMapping("/user-agent")
    public APIResponse<DeviceInfo> testUserAgent() {
        DeviceInfo deviceInfo = userAgentParser.parseUserAgent(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        return APIResponse.success(CommonResponseEnum.OK, deviceInfo);
    }

    @GetMapping("/tlo-logging")
    public APIResponse<String> testTloLogging() {
        return APIResponse.success(CommonResponseEnum.OK, "TLO 로깅 테스트 성공");
    }
}