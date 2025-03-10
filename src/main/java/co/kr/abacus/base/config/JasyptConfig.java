package co.kr.abacus.base.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

    @Bean("jasyptEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        // 암호화 키 설정. 실제 서비스에서는 외부에서 안전하게 관리해야 함
        config.setPassword("jasyptEncryptor");

        // 암호화 알고리즘 설정. HMAC SHA512와 AES 256을 결합한 강력한 알고리즘 사용
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");

        // 암호화 키 생성 시 반복 횟수 설정. 높을수록 보안은 강화되지만 성능은 저하됨
        config.setKeyObtentionIterations("1000");

        // 암호화 작업을 수행할 스레드 풀의 크기 설정
        config.setPoolSize("1");

        // 암호화 제공자 설정. SunJCE는 Java의 표준 암호화 제공자
        config.setProviderName("SunJCE");

        // 암호화에 사용될 솔트(salt) 생성 클래스 설정. 무작위 솔트 사용
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");

        // 초기화 벡터(IV) 생성 클래스 설정. 무작위 IV 사용
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");

        // 암호화된 문자열의 출력 형식 설정. Base64 인코딩 사용
        config.setStringOutputType("base64");

        encryptor.setConfig(config);
        return encryptor;
    }

}
