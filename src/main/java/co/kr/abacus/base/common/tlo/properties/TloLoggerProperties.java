package co.kr.abacus.base.common.tlo.properties;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TloLoggerProperties {
    @Builder.Default
    private Boolean enable = false;
    @Builder.Default
    private Boolean enableServiceLog = true;
    @Builder.Default
    private Boolean enableFileLog = false;
    private String serviceName;
    private String instanceName;
    @Builder.Default
    private String instanceCode = "999";
    private String baseDir;
    @Builder.Default
    private Integer intervalMinutes = 5;
    @Builder.Default
    private Integer checkIntervalMillis = 1000;
    @Builder.Default
    private Boolean useImmediatelyWrite = false;

    public static TloLoggerProperties getDefault() {
        return TloLoggerProperties.builder().build();
    }
}
