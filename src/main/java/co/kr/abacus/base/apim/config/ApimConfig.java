package co.kr.abacus.base.apim.config;

import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ApimConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ApimErrorDecoder();
    }

    @Bean
    public Encoder jacksonEncoder() {
        return new JacksonEncoder();
    }

    @Bean
    public Decoder apimResponseDecoder() {
        return new ApimResponseDecoder();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1), 3);
    }
}
