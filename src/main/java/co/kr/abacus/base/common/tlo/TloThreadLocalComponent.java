package co.kr.abacus.base.common.tlo;


public class TloThreadLocalComponent {
    private static final ThreadLocal<TloLoggerMessage> tloData = new InheritableThreadLocal<>();

    public TloThreadLocalComponent() {
    }

    public static TloLoggerMessage getTloData() {
        return tloData.get();
    }


    public static void setTloData(TloLoggerMessage tloLoggerMessage) {
        tloData.set(tloLoggerMessage);
    }

    public static void clearThreadLocal() {
        tloData.remove();
    }

    public static void mergeWithBuilder(TloLoggerMessage.TloLoggerMessageBuilder builder) {
        TloLoggerMessage threadLocalData = getTloData();
        if (threadLocalData == null) {
            return;
        }

        if (threadLocalData.getSid() != null) builder.sid(threadLocalData.getSid());
        if (threadLocalData.getExtIf() != null && !threadLocalData.getExtIf().isEmpty())
            builder.extIf(threadLocalData.getExtIf());

    }
}
