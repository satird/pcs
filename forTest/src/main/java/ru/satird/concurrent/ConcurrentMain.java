package ru.satird.concurrent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ConcurrentMain {

    private ExecutorService executorService;
    private static List<ConcurrentSomeClass> list = new ArrayList<>();

    public ConcurrentMain() {
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public static void main(String[] args) throws InterruptedException {
        ConcurrentMain main = new ConcurrentMain();
        DurationStat<String> stat = new DurationStat<>();
        ConcurrentSomeClass someClass = new ConcurrentSomeClass("First", 1);
        ConcurrentSomeClass someClass2 = new ConcurrentSomeClass("Second", 2);
        ConcurrentSomeClass someClass3 = new ConcurrentSomeClass("Third", 3);
        ConcurrentSomeClass someClass4 = new ConcurrentSomeClass("Fourth", 4);
        list.add(someClass);
        list.add(someClass2);
        list.add(someClass3);
        list.add(someClass4);
        main.getAction(list, stat);
        Thread.sleep(1000);
        main.destroy();
        final Map<String, Duration> stat1 = stat.getStat();
        stat1.forEach((k, v) -> System.out.println("key: " + k + ", value: " + v));
    }

    public void getAction(List<ConcurrentSomeClass> someClassList, DurationStat<String> stat) {
        someClassList.stream().map(m -> CompletableFuture.supplyAsync(() -> {
            m.getName(stat);
            final Map<String, Duration> statT = stat.getStat();
            statT.forEach((k, v) -> System.out.println("LOCAL key: " + k + ", value: " + v));
            return null;
        }, executorService)).collect(Collectors.toList());
    }

    private void destroy() {
        executorService.shutdownNow();
    }
}
