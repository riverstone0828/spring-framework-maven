package co.kr.abacus.base.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    private final SwaggerProperties swaggerProperties;

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("0-all")
                .pathsToMatch("/**")
                .pathsToExclude()
                .build();
    }

    @Bean
    public GroupedOpenApi bigQueryApi() {
        return GroupedOpenApi.builder()
                .group("BigQuery")
                .pathsToMatch("/bq/**")
                .build();
    }

    @Bean
    public GroupedOpenApi tloApi() {
        return GroupedOpenApi.builder()
                .group("TLO")
                .pathsToMatch("/tlo/**")
                .build();
    }

    @Bean
    public GroupedOpenApi exceptionApi() {
        return GroupedOpenApi.builder()
                .group("EXCEPTION")
                .pathsToMatch("/exception/**")
                .build();
    }


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(swaggerProperties.getTitle())
                        .version(swaggerProperties.getVersion())
                        .description(swaggerProperties.getDescription())
                ).servers(swaggerProperties.getServers())
                .openapi("3.0.1")
                ;
    }
}

