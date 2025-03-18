package co.kr.abacus.base.apim.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthClientCredentialsResDTO {
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Long expiresIn;
    @JsonProperty("consented_on")
    private Long consentedOn;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("grant_type")
    private String grantType;
}
