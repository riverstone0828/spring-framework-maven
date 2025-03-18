package co.kr.abacus.base.common.tlo;

import co.kr.abacus.base.common.tlo.properties.TloLoggerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class TloLoggerWriter {

    private static final DateTimeFormatter TSL_DATETIME_FORMAT_MILLIS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private static final DateTimeFormatter TSL_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static final String[] DATE_TIME_FIELDS = new String[]{
            "REQ_TIME",
            "RSP_TIME"
    };


    private final TloLoggerProperties tloLoggerProperties;

    private final TloLogger tloLogger;

    public void write(TloLoggerMessage tloLoggerMessage) {
        LinkedHashMap<String, Object> tsl = new LinkedHashMap<>();
        tloLoggerMessage.fillMappedValues(tsl);

        // replace
        tsl.put("SEQ_ID", getSeqId());
        tsl.put("LOG_TIME", LocalDateTime.now().format(TSL_DATETIME_FORMAT));
        tsl.put("LOG_TYPE", "SVC");
        tsl.put("SVC_NAME", tloLoggerProperties.getServiceName());

        Arrays.stream(DATE_TIME_FIELDS).parallel().forEach(field -> {
            if (tsl.get(field) instanceof LocalDateTime) {
                LocalDateTime value = (LocalDateTime) tsl.get(field);
                tsl.replace(field, value, value.format(TSL_DATETIME_FORMAT_MILLIS));
            }
        });
        if (Boolean.TRUE.equals(tloLoggerProperties.getEnable())) {
            tloLogger.write(tsl);
        }
    }

    public String getSeqId() {
        String seqId = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        seqId = seqId + generateRandomString();
        return seqId;
    }

    private String generateRandomString() {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }


}
