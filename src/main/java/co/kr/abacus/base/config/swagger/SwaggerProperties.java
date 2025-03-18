package co.kr.abacus.base.config.swagger;

import io.swagger.v3.oas.models.servers.Server;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "springdoc")
public class SwaggerProperties {
    private String title;
    private String version;
    private String description;
    private List<Server> servers;
}
