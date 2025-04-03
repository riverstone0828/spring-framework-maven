package co.kr.abacus.base.common.tlo;

import static co.kr.abacus.base.common.tlo.TloConstants.DATETIME_FORMAT;
import static co.kr.abacus.base.common.tlo.TloConstants.DATETIME_FORMAT_MILLIS;
import static co.kr.abacus.base.common.tlo.TloConstants.DATE_TIME_FIELDS;
import static co.kr.abacus.base.common.tlo.TloConstants.FIELD_LOG_TIME;
import static co.kr.abacus.base.common.tlo.TloConstants.FIELD_LOG_TYPE;
import static co.kr.abacus.base.common.tlo.TloConstants.FIELD_SEQ_ID;
import static co.kr.abacus.base.common.tlo.TloConstants.FIELD_SVC_NAME;
import static co.kr.abacus.base.common.tlo.TloConstants.LOG_TYPE_SVC;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import co.kr.abacus.base.common.tlo.properties.TloLoggerProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TloLoggerWriter {
    private final TloLoggerProperties tloLoggerProperties;
    private final TloLogger tloLogger;
    private final AtomicLong sequence = new AtomicLong(0);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public void write(TloLoggerMessage tloLoggerMessage) {
        if (!tloLoggerProperties.getEnable()) {
            return;
        }

        // 서비스 로그 출력
        if (tloLoggerProperties.getEnableServiceLog()) {
            Map<String, Object> tsl = new ConcurrentHashMap<>();
            tloLoggerMessage.fillMappedValues(tsl);
            log.info("TLO_LOG: {}", tsl);
        }

        // 파일 로깅
        if (tloLoggerProperties.getEnableFileLog()) {
            LinkedHashMap<String, Object> tsl = new LinkedHashMap<>();
            tloLoggerMessage.fillMappedValues(tsl);

            // 필수 필드 설정
            tsl.put(FIELD_SEQ_ID, getSeqId());
            tsl.put(FIELD_LOG_TIME, LocalDateTime.now().format(DATETIME_FORMAT));
            tsl.put(FIELD_LOG_TYPE, LOG_TYPE_SVC);
            tsl.put(FIELD_SVC_NAME, tloLoggerProperties.getServiceName());

            // DateTime 필드 포맷팅
            formatDateTimeFields(tsl);

            tloLogger.write(tsl);
        }
    }

    private void formatDateTimeFields(Map<String, Object> tsl) {
        Arrays.stream(DATE_TIME_FIELDS)
                .filter(field -> tsl.get(field) instanceof LocalDateTime)
                .forEach(field -> {
                    LocalDateTime value = (LocalDateTime) tsl.get(field);
                    tsl.replace(field, value, value.format(DATETIME_FORMAT_MILLIS));
                });
    }

    public String getSeqId() {
        return String.format("%s%05d", dateTimeFormatter.format(LocalDateTime.now()),
                sequence.incrementAndGet() % 100000);
    }
}
