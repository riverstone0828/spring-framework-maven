package co.kr.abacus.base.common.tlo;

import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class TloThreadLocalComponent {
    private static final ThreadLocal<TloLoggerMessage> tloData = new InheritableThreadLocal<>();

    public static void setTloData(TloLoggerMessage message) {
        tloData.set(message);
    }

    public static void clearThreadLocal() {
        tloData.remove();
    }

    public static void mergeWithBuilder(TloLoggerMessage.TloLoggerMessageBuilder builder) {
        var threadLocalData = tloData.get();
        if (threadLocalData == null) {
            return;
        }

        Optional.ofNullable(threadLocalData.sid()).ifPresent(builder::sid);
        Optional.ofNullable(threadLocalData.extIf())
                .filter(list -> !list.isEmpty())
                .ifPresent(builder::extIf);
    }
}
