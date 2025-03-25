package co.kr.abacus.base.config.gcp;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GCPServiceToken {

    private final GCPProperties gcpProperties;

    public String getAccessToken() throws IOException {
        // TODO ADC 조회 가능여부 파악 - GCloud ADC 사용, GCP Compute Engine에서 서비스 기동시 자동으로 가져올 것으로 예상됨.
        GoogleCredentials googleCredentials = GoogleCredentials.getApplicationDefault();
        googleCredentials.refresh();
        return googleCredentials.getAccessToken().getTokenValue(); // 액세스 토큰 값 반환
    }

}
