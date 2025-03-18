package co.kr.abacus.base.common.tlo;

import co.kr.abacus.base.common.tlo.filter.TloHttpLoggingFilter;
import com.google.common.base.CaseFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Getter
@Builder
public class TloLoggerMessage {
    public static Pattern CR_PATTERN = Pattern.compile("\\R+");

    // ===============================================================================
    // 가이드 상 필수
    // ===============================================================================
    /**
     * {@link TloLoggerWriter#write} 에서 tsl.put("SEQ_ID", getSeqId()); 부분 확인
     */
    private String seqId;           // 로그 단위 Unique한 ID
    private String logTime;         // 로그를 파일에 Write 시점 시간
    private String logType;         // 사용자의 요청 발생 시간

    // FIXME 고객과 상의하는 값
    private String sid;             // TLO 규격의 분류 코드 ----------------> Bizon에서는 '가입번호'를 사용함

    // 서비스 상태코드 (성공, 실패,구간실패) ----------------> 20000000 등의 약속된 응답코드.
    // 이게 추후에 LogPress? LogPressor?인지 뭔가 tlo로그 감시하는 웹 서비스에서 이 코드를 구분으로 서비스의 성공 여부를 판단함.
    private String resultCode;

    private LocalDateTime reqTime;  // 사용자의 요청 발생 시간          
    private LocalDateTime rspTime;  // 사용자의 응답 발생 시간

    private String clientIp;        // 접속 클라이언트 IP  -----------------> FIXME 무슨 값을 넣을지는 고객과 상의하세요.
    private String devInfo;         // 접속 단말 타입  -----------------> FIXME 무슨 값을 넣을지는 고객과 상의하세요.
    private String osInfo;          // OS정보  -----------------> FIXME 무슨 값을 넣을지는 고객과 상의하세요.
    private String nwInfo;          // 접속 네트워크 정보  -----------------> FIXME 무슨 값을 넣을지는 고객과 상의하세요.
    private String svcName;         // 각 서비스/시스템 명(통계사전 참조)  -----------------> FIXME 무슨 값을 넣을지는 고객과 상의하세요.
    private String devModel;        // 단말 모델명  -----------------> FIXME 무슨 값을 넣을지는 고객과 상의하세요.
    private String carrierType;     // 통신사 구분 ----------------> 구분이 어려우면 E  -----------------> FIXME 무슨 값을 넣을지는 고객과 상의하세요.


    // ===============================================================================
    // 가이드 설명: 해당 영역은 서비스 통계와 실시간 품질감시(통합통계)를 위한 항목 영역임. 통계 사전을 보고 항목을 선택하여 규격서를 만들고 인텔리전스기술개발팀에 검토 요청
    // FIXME 설명보니까 그냥 필요에 따라 추가하는 듯. logKey 개추~
    // ===============================================================================
    /**
     * {@link TloHttpLoggingFilter#tslBuilder} String logKey 부분 확인
     */
    private String logKey;
    private String channelType;
    private String channel;
    private String funcId;
    private String command;
    private String subCode;

    // FIXME 외부 입력 정보를 세팅할 경우에 고객과 상의해서 사용.
    // bizon에서는 외부 API 연동이 있었기 때문에 이 값을 사용함.
    // 참고 로그: SEQ_ID=20241108133003519fDEFa7kL|LOG_TIME=20241108133003|LOG_TYPE=SVC|SID=E|RESULT_CODE=20000000|REQ_TIME=20241108133000446|RSP_TIME=20241108133003519|CLIENT_IP=172.23.19.253|DEV_INFO=PC|OS_INFO=Other|NW_INFO=ETC|SVC_NAME=bizon-portal-was|DEV_MODEL=|CARRIER_TYPE=E|LOG_KEY=20241108133000446ENY1uXDX|CHANNEL_TYPE=IN|CHANNEL=WEB|FUNC_ID=FN01010|COMMAND=|SUB_CODE=|EXT_IF=[[EXT_CH=APIM_OAUTH;EXT_FUNC_ID=;EXT_REQ_TIME=20241108133000634;EXT_RSP_TIME=20241108133001411;EXT_RSLT_CD=20000000]]
    private List<String> extIf;

    // Java 변수명 규칙인 Camel 케이스를 TLO 로그 변수명 규칙인 대문자 snake로 변환시키는 함수
    public void fillMappedValues(Map<String, Object> tsl) {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            Object value = null;
            field.setAccessible(true);
            try {
                value = field.get(this);
                if (value instanceof Logger) {
                    continue;
                }
            } catch (IllegalAccessException e) {
                log.error("getTslMap: reflection error.", e);
            }
            String key = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, field.getName());
            if (value instanceof String) {
                tsl.put(key, value == null ? "" : CR_PATTERN.matcher((String) value).replaceAll(" "));
            } else {
                tsl.put(key, value == null ? "" : value);
            }
        }
    }
}
