package docker;

import com.google.common.flogger.FluentLogger;

import static com.google.common.flogger.FluentLogger.forEnclosingClass;

public final class Logger {
    private static final FluentLogger log = forEnclosingClass();

    private Logger() {
    }

    public static void logInfo(final String message) {
        log.atInfo().log(message);
    }
}
