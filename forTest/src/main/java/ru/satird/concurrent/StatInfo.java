package ru.satird.concurrent;

public class StatInfo {
    private String name;
    private long totalTime;
    private long minTime;
    private long maxTime;
    private String threadName;

    public String getName() {
        return name;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public long getMinTime() {
        return minTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public String getThreadName() {
        return threadName;
    }
}
