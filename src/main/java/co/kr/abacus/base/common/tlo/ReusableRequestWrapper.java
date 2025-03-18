package co.kr.abacus.base.common.tlo;

import co.kr.abacus.base.common.tlo.filter.TloHttpLoggingFilter;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 사용처: {@link TloHttpLoggingFilter#doFilterInternal} <br/>
 * 정의 이유(Chat- GPT): <br/>
 * ### **`ReusableRequestWrapper` 클래스의 주요 기능** <br/>
 * 1. **`HttpServletRequest`의 본문(Body) 캐싱** <br/>
 * - 기본적으로 `HttpServletRequest`의 본문은 한 번만 읽을 수 있습니다. <br/>
 * - 그런데 `ReusableRequestWrapper`는 `request.getInputStream()` 호출 시 데이터를 읽어 캐싱(`body` 배열)에 저장합니다. 이렇게 하면 이후에도 본문 데이터를 다시 읽거나 사용할 수 있습니다. <br/>
 * <br/>
 * 2. **`getInputStream()`과 `getReader()` 재정의** <br/>
 * - 읽은 데이터(바이트 배열)를 기반으로 `ServletInputStream`과 `BufferedReader`를 제공합니다. <br/>
 * - 이를 통해 본문을 여러 번 읽거나 재사용 가능하도록 구현하였습니다. <br/>
 * <br/>
 * 3. **본문 데이터를 String으로 캐싱하여 쉽게 사용 가능** <br/>
 * - 본문 데이터의 텍스트 버전을 `cachedBody` 필드에 저장합니다. <br/>
 * - 필요할 때 `getCachedBody()`를 호출하여 본문 데이터를 String으로 사용할 수 있습니다. <br/>
 */
public class ReusableRequestWrapper extends HttpServletRequestWrapper {
    private byte[] body;
    private String cachedBody;

    public ReusableRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = request.getInputStream().readAllBytes();
        cachedBody = new String(body, StandardCharsets.UTF_8);
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            private ByteArrayInputStream bais = new ByteArrayInputStream(body);

            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return bais.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    public String getCachedBody() {
        return cachedBody;
    }
}