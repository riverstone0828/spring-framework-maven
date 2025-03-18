package co.kr.abacus.base.apim.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ApimResponseDecoder implements Decoder {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (!(type instanceof ParameterizedType) ||
                !ApimResponse.class.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType())) {
            throw new DecodeException(response.status(), "Expected a CustomResponse", response.request());
        }

        Type bodyType = ((ParameterizedType) type).getActualTypeArguments()[0];
        Object body = objectMapper.readValue(response.body().asInputStream(), objectMapper.constructType(bodyType));

        return new ApimResponse<>(body, response.headers(), response.status());
    }
}