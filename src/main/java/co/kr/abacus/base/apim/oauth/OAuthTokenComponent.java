package co.kr.abacus.base.apim.oauth;

import co.kr.abacus.base.apim.config.ApimProperties;
import co.kr.abacus.base.apim.config.ApimResponse;
import co.kr.abacus.base.apim.oauth.dto.OAuthClientCredentialsReqDTO;
import co.kr.abacus.base.apim.oauth.dto.OAuthClientCredentialsResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthTokenComponent {

    private final ApimProperties apimProperties;
    private final OAuthTokenProperties oAuthTokenProperties;

    private final OAuthTokenPrimaryClient oAuthTokenPrimaryClient;
    private final OAuthTokenFallbackClient oAuthTokenFallbackClient;

    public String getOauthToken() {
        log.info("start...");
        log.info("APIM OAuth2.0 토큰 발급 요청");
        this.validateProperties();

        OAuthClientCredentialsReqDTO requestBody = this.createRequestBody();
        ApimResponse<OAuthClientCredentialsResDTO> response;
        try {
            response = oAuthTokenPrimaryClient.getAccessToken(requestBody);
        } catch (Exception e) {
            return this.handleException(e, requestBody);
        }

        String token = response.getBody().getAccessToken();
        if (token == null || token.isEmpty() || response.getBody().getExpiresIn() == null) {
            throw new RuntimeException();
        }
        log.info("APIM OAuth2.0 토큰 발급 완료");
        log.info("end...");
        return token;

    }

    private String handleException(Exception e
            , OAuthClientCredentialsReqDTO requestBody) {

        log.info("API 호출 실패, fallback API를 호출합니다: {}", e.getMessage());
        try {
            log.info("fallback start...");
            ApimResponse<OAuthClientCredentialsResDTO> response = oAuthTokenFallbackClient.getAccessToken(requestBody);
            String token = response.getBody().getAccessToken();
            if (token == null || token.isEmpty() || response.getBody().getExpiresIn() == null) {
                throw new Exception();
            }
            log.info("fallback end...");
            return token;
        } catch (Exception ex) {
            return this.fallbackHandleException(ex);
        }
    }

    private String fallbackHandleException(Exception e) {
        log.error("fallback API 실패: {}", e.getMessage());
        log.error("end with exception...");
        throw new RuntimeException(e);
    }


    private void validateProperties() {
        if (oAuthTokenProperties.getGrantType() == null || apimProperties.getClientId() == null
                || apimProperties.getClientSecret() == null || oAuthTokenProperties.getScope() == null) {
            throw new RuntimeException();
        }
    }

    private OAuthClientCredentialsReqDTO createRequestBody() {
        return OAuthClientCredentialsReqDTO.builder()
                .grantType(oAuthTokenProperties.getGrantType())
                .clientId(apimProperties.getClientId())
                .clientSecret(apimProperties.getClientSecret())
                .scope(oAuthTokenProperties.getScope())
                .build();
    }


}
