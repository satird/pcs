package ru.satird.concurrent;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class DurationStat<T> {
    private final Deque<T> stack = new ArrayDeque<>();
    private Instant prev;
    private final Map<T, Duration> accumulator = new HashMap<>();
    private final Supplier<Instant> timestampSupplier;
    private StatInfo statInfo;

    /**
     * For test purpose
     * @param timestampSupplier mock timestamp supplier
     */
    DurationStat(Supplier<Instant> timestampSupplier) {
        this.timestampSupplier = timestampSupplier;
        this.statInfo = new StatInfo();
    }

    public DurationStat() {
        this.timestampSupplier = Instant::now;
    }

    /**
     * Opens the timeslot for the passed key. Pauses the current interval if it exists
     * @param key key
     * @return current timestamp
     */
    public Instant start(T key) {
        final Instant now = storeDuration();
        stack.push(key);
        return now;
    }

    /**
     * Ends the current time interval. Restores a previously opened one, if it exists
     * @return current timestamp
     */
    public Instant finish() {
        final Instant now = storeDuration();
        stack.pop();
        return now;
    }

    /**
     * @return summary statistics as a Map
     */
    public Map<T, Duration> getStat() {
        return Collections.unmodifiableMap(accumulator);
    }

    public <R> R applyWithCheckedException(T key, Callable<R> action) throws Exception {
        final R result;
        start(key);
        try {
            result = action.call();
        } finally {
            finish();
        }
        return result;
    }

    public <R> R apply(T key, Callable<R> action) {
        try {
            return applyWithCheckedException(key, action);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void applyWithCheckedException(T key, RunnableWithException action) throws Exception {
        applyWithCheckedException(key, () -> {
            action.run();
            return null;
        });
    }

    public void apply(T key, Runnable action) {
        try {
            applyWithCheckedException(key, () -> {
                action.run();
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Instant storeDuration() {
        T currentKey = stack.peek();
        Instant now = timestampSupplier.get();
        if (currentKey != null) {
            accumulate(currentKey, Duration.between(prev, now));
        }
        prev = now;
        return now;
    }

    private void accumulate(T currentKey, Duration duration) {
        accumulator.merge(currentKey, duration, Duration::plus);

//        statInfo.
    }

    @FunctionalInterface
    public interface RunnableWithException {
        void run() throws Exception;
    }
}

