package co.kr.abacus.base.common.useragent;

import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Component;

@Component
public class UserAgentParser {

    private static final String UNKNOWN = "UNKNOWN";
    private static final String MOBILE = "MOBILE";
    private static final String TABLET = "TABLET";
    private static final String PC = "PC";
    private static final String OTHER = "OTHER";
    private static final String WINDOWS = "Windows";
    private static final String IPHONE = "iPhone";
    private static final String IPAD = "iPad";
    private static final String IOS = "iOS";
    private static final String ANDROID = "Android";
    private static final String MAC = "Mac";
    private static final String LINUX = "Linux";

    public DeviceInfo parseUserAgent(String userAgentString) {
        if (userAgentString == null) {
            return new DeviceInfo(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
        }

        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

        return new DeviceInfo(
                getDeviceType(userAgent),
                getDetailedOS(userAgent.getOperatingSystem(), userAgentString.toLowerCase()),
                UNKNOWN, // 상세 모델 정보는 필요한 경우에만 추가
                userAgent.getBrowser().getName());
    }

    private String getDeviceType(UserAgent userAgent) {
        switch (userAgent.getOperatingSystem().getDeviceType()) {
            case MOBILE:
                return MOBILE;
            case TABLET:
                return TABLET;
            case COMPUTER:
                return PC;
            default:
                return OTHER;
        }
    }

    private String getDetailedOS(OperatingSystem os, String userAgent) {
        // Windows
        if (os.getGroup() == OperatingSystem.WINDOWS) {
            return WINDOWS;
        }

        // iOS 디바이스 구분
        if (os.getGroup() == OperatingSystem.IOS) {
            if (userAgent.contains("iphone"))
                return IPHONE;
            if (userAgent.contains("ipad"))
                return IPAD;
            return IOS;
        }

        // Android
        if (os.getGroup() == OperatingSystem.ANDROID) {
            return ANDROID;
        }

        // Mac
        if (os.getGroup() == OperatingSystem.MAC_OS_X) {
            return MAC;
        }

        // Linux
        if (os.getGroup() == OperatingSystem.LINUX) {
            return LINUX;
        }

        // 기타 OS는 라이브러리가 제공하는 기본 이름 사용
        return os.getName();
    }
}