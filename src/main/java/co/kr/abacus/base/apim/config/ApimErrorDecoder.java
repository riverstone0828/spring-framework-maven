package co.kr.abacus.base.apim.config;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class ApimErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        return FeignException.errorStatus(s, response);
    }
}
