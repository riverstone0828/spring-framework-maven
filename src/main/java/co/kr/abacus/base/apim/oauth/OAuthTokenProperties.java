package co.kr.abacus.base.apim.oauth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "apim.oauth")
@Getter
@Setter
public class OAuthTokenProperties {
    private String endPoint;
    private String grantType;
    private String scope;
}
