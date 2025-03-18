package co.kr.abacus.base.apim.oauth;

import co.kr.abacus.base.apim.config.ApimConfig;
import co.kr.abacus.base.apim.config.ApimResponse;
import co.kr.abacus.base.apim.oauth.dto.OAuthClientCredentialsReqDTO;
import co.kr.abacus.base.apim.oauth.dto.OAuthClientCredentialsResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "oauth-public-fallback"
        , url = "${apim.domain-public}"
        , configuration = ApimConfig.class)
public interface OAuthTokenFallbackClient {

    @PostMapping(value = "${apim.oauth.end-point}"
            , consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ApimResponse<OAuthClientCredentialsResDTO> getAccessToken(
            OAuthClientCredentialsReqDTO requestBody
    );

}
