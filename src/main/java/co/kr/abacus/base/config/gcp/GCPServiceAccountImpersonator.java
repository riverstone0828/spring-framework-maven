package co.kr.abacus.base.config.gcp;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ImpersonatedCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GCPServiceAccountImpersonator {

    private final GCPProperties gcpProperties;

    public String getAccessToken() throws IOException {
        // TODO ADC 조회 가능여부 파악 - GCloud ADC 사용, GCP Compute Engine에서 서비스 기동시 자동으로 가져올 것으로 예상됨.
        GoogleCredentials googleCredentials = GoogleCredentials.getApplicationDefault();
        ImpersonatedCredentials impersonatedCredentials = ImpersonatedCredentials.create(
                googleCredentials,
                gcpProperties.getImpersonatorAccountEmail(),
                null,
                gcpProperties.getScopes(),
                gcpProperties.getTokenLifeTime() // 3600 sec
        );

        // 임시 자격증명을 사용하여 액세스 토큰을 생성
        AccessToken accessToken = impersonatedCredentials.refreshAccessToken();
        return accessToken.getTokenValue(); // 액세스 토큰 값 반환
    }

}
