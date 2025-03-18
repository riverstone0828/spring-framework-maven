package co.kr.abacus.base.config.gcp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "gcp")
public class GCPProperties {
    // ####################################################################
    // # application.yml
    // ####################################################################
    private List<String> scopes;
    private Integer tokenLifeTime;

    // ####################################################################
    // # application-{profile}.yml
    // ####################################################################
    private String simbaJdbcUrl;
    private String projectId;
    private String impersonatorAccountEmail;
}
