package ru.satird.concurrent;

public class ConcurrentSomeClass<T> {
    private String name;
    private int count;

    public ConcurrentSomeClass(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public void getName(DurationStat<String> stat) {
        try {
            stat.start("getName");
            Thread thread = Thread.currentThread();
            thread.sleep(count * 100L);
            System.out.println("My name - " + name + ", count - " + count + ", threadName - " + Thread.currentThread().getName());
            stat.finish();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
