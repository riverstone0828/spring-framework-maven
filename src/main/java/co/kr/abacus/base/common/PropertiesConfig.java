package co.kr.abacus.base.common;

import co.kr.abacus.base.common.tlo.TloLogger;
import co.kr.abacus.base.common.tlo.properties.FuncIdsProperties;
import co.kr.abacus.base.common.tlo.properties.TloLoggerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "co.kr.abacus.base")
public class PropertiesConfig {

    // application-local.yml
    @Bean
    @ConfigurationProperties("logger.tlo")
    public TloLoggerProperties tslLoggerProperties() {
        return new TloLoggerProperties();
    }

    // application.yml
    // spring:
    // config:
    // import: classpath:endpoint-function-ids.yml
    // endpoint-function-ids.yml
    @Bean
    @ConfigurationProperties("base-project.api")
    public FuncIdsProperties funcIdsProperties() {
        return new FuncIdsProperties();
    }

}
