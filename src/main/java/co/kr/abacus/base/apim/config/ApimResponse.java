package co.kr.abacus.base.apim.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.Map;

/**
 * @apiNote APIM API 호출 시 헤더 값을 추출하기 위한 DTO
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ApimResponse<T> {
    private T body;
    private Map<String, Collection<String>> headers;
    private int status;
}
