package co.kr.abacus.base.common.useragent;

public record DeviceInfo(
        String device,
        String os,
        String model,
        String browser) {
}