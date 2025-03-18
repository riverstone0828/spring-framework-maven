package co.kr.abacus.base.common.tlo.properties;

import lombok.Data;

@Data
public class TloLoggerProperties {
    private Boolean enable;
    private String serviceName;
    private String instanceName;
    private String instanceCode;
    private String baseDir;
    private Integer intervalMinutes;
    private Integer checkIntervalMillis;
    private Boolean useImmediatelyWrite;
}
