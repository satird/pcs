package ru.satird.concurrent;

import java.util.concurrent.TimeUnit;

public final class DurationUtils {
    public static String durationFromStart(final long start) {
        return duration(System.currentTimeMillis() - start);
    }

    public static String duration(final long total) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(total);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(total) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(total));
        long millis = total - TimeUnit.MINUTES.toMillis(minutes) - TimeUnit.SECONDS.toMillis(seconds);
        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }
}
