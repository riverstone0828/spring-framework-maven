package co.kr.abacus.base.common.useragent;


import lombok.Data;

import java.util.List;


@Data
public class UserAgentProperties {
    private List<UserDeviceProperties> devices;
}

