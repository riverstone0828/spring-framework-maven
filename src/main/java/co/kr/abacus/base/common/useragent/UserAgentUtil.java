package co.kr.abacus.base.common.useragent;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class UserAgentUtil {
    public static UserDeviceVO getDeviceInfo(String userAgent, List<UserDeviceProperties> devices) {
        UserDeviceVO result = new UserDeviceVO();
        String REGEX_DEVICE = ".*(iPhone|iPad|Android|BlackBerry|Windows Phone).*";
        Pattern pattern = Pattern.compile(REGEX_DEVICE);
        Matcher matcher = pattern.matcher(userAgent);

        Optional<UserDeviceProperties> userDeviceProperty = null;
        if (matcher.find()) {
            log.debug("getDeviceInfo : " + matcher.group(1));
            String deviceInfo = matcher.group(1);
            userDeviceProperty = devices.stream().filter(o -> o.getName().equals(deviceInfo)).findFirst();
            result.setDevice("MOBILE");

            if (userDeviceProperty.isPresent()) {
                Pattern deviceInfoPattern = Pattern.compile(userDeviceProperty.get().getPatternRegex());
                Matcher deviceInfoMatcher = deviceInfoPattern.matcher(userAgent);
                if (deviceInfoMatcher.find()) {
                    String deviceVersionStr = deviceInfoMatcher.group(1);

                    if (!userDeviceProperty.get().getName().matches("(iPhone|iPad)")) {
                        result.setModel(deviceInfoMatcher.group(2));
                    } else {
                        result.setModel(userDeviceProperty.get().getName());
                    }

                    Pattern deviceVersionPattern = Pattern.compile(userDeviceProperty.get().getVersionRegex());
                    Matcher deviceVersionMatcher = deviceVersionPattern.matcher(deviceVersionStr);

                    if (deviceVersionMatcher.find()) {
                        String osVersion = deviceVersionMatcher.group(1);
                        StringBuilder sb = new StringBuilder();
                        if (userDeviceProperty.get().getOsTag() != null) {
                            sb.append(userDeviceProperty.get().getOsTag());
                            sb.append("_");
                        }
                        sb.append(osVersion);
                        result.setOs(sb.toString());
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            result.setDevice("PC");
        }

        return result;
    }

    public static String getClientOS(String userAgent) {
        String os = "";
        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("windows nt 10.0")) {
            os = "Windows10";
        } else if (userAgent.contains("windows nt 6.1")) {
            os = "Windows7";
        } else if (userAgent.contains("windows nt 6.2") || userAgent.contains("windows nt 6.3")) {
            os = "Windows8";
        } else if (userAgent.contains("windows nt 6.0")) {
            os = "WindowsVista";
        } else if (userAgent.contains("windows nt 5.1")) {
            os = "WindowsXP";
        } else if (userAgent.contains("windows nt 5.0")) {
            os = "Windows2000";
        } else if (userAgent.contains("windows nt 4.0")) {
            os = "WindowsNT";
        } else if (userAgent.contains("windows 98")) {
            os = "Windows98";
        } else if (userAgent.contains("windows 95")) {
            os = "Windows95";
        } else if (userAgent.contains("iphone")) {
            os = "iPhone";
        } else if (userAgent.contains("ipad")) {
            os = "iPad";
        } else if (userAgent.contains("android")) {
            os = "android";
        } else if (userAgent.contains("mac")) {
            os = "mac";
        } else if (userAgent.contains("linux")) {
            os = "Linux";
        } else {
            os = "Other";
        }
        return os;
    }
}
