package co.kr.abacus.base.common.tlo;

import com.google.common.base.CaseFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public record TloLoggerMessage(
        // 필수 필드
        String seqId,
        String logTime,
        String logType,
        String sid,
        String resultCode,
        LocalDateTime reqTime,
        LocalDateTime rspTime,

        // 클라이언트 정보
        String clientIp,
        String devInfo,
        String osInfo,
        String nwInfo,
        String svcName,
        String devModel,
        String carrierType,

        // 서비스 정보
        String logKey,
        String channelType,
        String channel,
        String funcId,
        String command,
        String subCode,
        List<String> extIf) {

    private static final Pattern CR_PATTERN = Pattern.compile("\\R+");

    public void fillMappedValues(Map<String, Object> tsl) {
        var fields = this.getClass().getRecordComponents();
        for (var field : fields) {
            var value = getValue(field.getName());
            var key = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, field.getName());

            if (value instanceof String strValue) {
                tsl.put(key, strValue == null ? "" : CR_PATTERN.matcher(strValue).replaceAll(" "));
            } else {
                tsl.put(key, value == null ? "" : value);
            }
        }
    }

    private Object getValue(String fieldName) {
        try {
            var method = this.getClass().getMethod(fieldName);
            return method.invoke(this);
        } catch (Exception e) {
            return null;
        }
    }

    public static TloLoggerMessageBuilder builder() {
        return new TloLoggerMessageBuilder();
    }

    public static class TloLoggerMessageBuilder {
        private String seqId;
        private String logTime;
        private String logType;
        private String sid;
        private String resultCode;
        private LocalDateTime reqTime;
        private LocalDateTime rspTime;
        private String clientIp;
        private String devInfo;
        private String osInfo;
        private String nwInfo;
        private String svcName;
        private String devModel;
        private String carrierType;
        private String logKey;
        private String channelType;
        private String channel;
        private String funcId;
        private String command;
        private String subCode;
        private List<String> extIf;

        public TloLoggerMessageBuilder seqId(String seqId) {
            this.seqId = seqId;
            return this;
        }

        public TloLoggerMessageBuilder logTime(String logTime) {
            this.logTime = logTime;
            return this;
        }

        public TloLoggerMessageBuilder logType(String logType) {
            this.logType = logType;
            return this;
        }

        public TloLoggerMessageBuilder sid(String sid) {
            this.sid = sid;
            return this;
        }

        public TloLoggerMessageBuilder resultCode(String resultCode) {
            this.resultCode = resultCode;
            return this;
        }

        public TloLoggerMessageBuilder reqTime(LocalDateTime reqTime) {
            this.reqTime = reqTime;
            return this;
        }

        public TloLoggerMessageBuilder rspTime(LocalDateTime rspTime) {
            this.rspTime = rspTime;
            return this;
        }

        public TloLoggerMessageBuilder clientIp(String clientIp) {
            this.clientIp = clientIp;
            return this;
        }

        public TloLoggerMessageBuilder devInfo(String devInfo) {
            this.devInfo = devInfo;
            return this;
        }

        public TloLoggerMessageBuilder osInfo(String osInfo) {
            this.osInfo = osInfo;
            return this;
        }

        public TloLoggerMessageBuilder nwInfo(String nwInfo) {
            this.nwInfo = nwInfo;
            return this;
        }

        public TloLoggerMessageBuilder svcName(String svcName) {
            this.svcName = svcName;
            return this;
        }

        public TloLoggerMessageBuilder devModel(String devModel) {
            this.devModel = devModel;
            return this;
        }

        public TloLoggerMessageBuilder carrierType(String carrierType) {
            this.carrierType = carrierType;
            return this;
        }

        public TloLoggerMessageBuilder logKey(String logKey) {
            this.logKey = logKey;
            return this;
        }

        public TloLoggerMessageBuilder channelType(String channelType) {
            this.channelType = channelType;
            return this;
        }

        public TloLoggerMessageBuilder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public TloLoggerMessageBuilder funcId(String funcId) {
            this.funcId = funcId;
            return this;
        }

        public TloLoggerMessageBuilder command(String command) {
            this.command = command;
            return this;
        }

        public TloLoggerMessageBuilder subCode(String subCode) {
            this.subCode = subCode;
            return this;
        }

        public TloLoggerMessageBuilder extIf(List<String> extIf) {
            this.extIf = extIf;
            return this;
        }

        public TloLoggerMessage build() {
            return new TloLoggerMessage(seqId, logTime, logType, sid, resultCode, reqTime, rspTime,
                    clientIp, devInfo, osInfo, nwInfo, svcName, devModel, carrierType,
                    logKey, channelType, channel, funcId, command, subCode, extIf);
        }
    }
}
