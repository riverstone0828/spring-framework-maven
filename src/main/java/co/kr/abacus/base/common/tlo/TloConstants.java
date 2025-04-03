package co.kr.abacus.base.common.tlo;

import java.time.format.DateTimeFormatter;

public final class TloConstants {
    private TloConstants() {
    }

    public static final DateTimeFormatter DATETIME_FORMAT_MILLIS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static final String FIELD_SEQ_ID = "SEQ_ID";
    public static final String FIELD_LOG_TIME = "LOG_TIME";
    public static final String FIELD_LOG_TYPE = "LOG_TYPE";
    public static final String FIELD_SVC_NAME = "SVC_NAME";
    public static final String FIELD_REQ_TIME = "REQ_TIME";
    public static final String FIELD_RSP_TIME = "RSP_TIME";

    public static final String LOG_TYPE_SVC = "SVC";
    public static final String RANDOM_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int RANDOM_STRING_LENGTH = 8;

    public static final String[] DATE_TIME_FIELDS = { FIELD_REQ_TIME, FIELD_RSP_TIME };
}