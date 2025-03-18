package co.kr.abacus.base.apim.oauth.dto;

import feign.form.FormProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OAuthClientCredentialsReqDTO {
    @FormProperty("grant_type")
    private String grantType;
    @FormProperty("client_id")
    private String clientId;
    @FormProperty("client_secret")
    private String clientSecret;
    @FormProperty("scope")
    private String scope;
}
