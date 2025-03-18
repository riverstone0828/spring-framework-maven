package co.kr.abacus.base.apim.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "apim")
public class ApimProperties {

    // ####################################################################
    // # application.yml
    // ####################################################################

    // ####################################################################
    // # application-{profile}.yml
    // ####################################################################
    private String domainPublic;
    private String domainPrivate;
    private String clientId;
    private String clientSecret;
    private String hmacSharedKey;
}
