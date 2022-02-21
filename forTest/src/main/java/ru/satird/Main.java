package ru.satird;

import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.satird.somePackage.SomeClass;
import ru.satird.somePackage.SomeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        SomeClass someClass = new SomeClass();
//        final List<String> stringList = new ArrayList<>();
//        final List<String> updateList = new ArrayList<>();
//
//        someClass.getItemList().forEach(nextB -> stringList.add(nextB.getName()));
//
//        CompletableFuture<String> completableFuture =
//                CompletableFuture.supplyAsync(
//                        () -> stringList.stream().findAny().orElse("Hello world"),
//                        Runnable::run
//                );
//        logger.warn("Future: {}", completableFuture.get());

//        partition.forEach(s -> logger.error("Error: {}", s));

//        for (String s : stringList) {
//            logger.info(s);
//        }
//        final Integer[] count = {0};
//        final long[] startTime = {System.currentTimeMillis()};
//        List<Long> timeList = new ArrayList<>();
//        stringList.forEach((f) -> {
//            count[0]++;
//            try {
//                Thread.sleep((long)(Math.random() * 10));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            long finishTime = System.currentTimeMillis();
//            timeList.add(finishTime - startTime[0]);
//            startTime[0] = System.currentTimeMillis();
//        });
//
//        System.out.println("Counter: " + count[0]);
//        timeList.forEach(s -> System.out.println(s));
//        logger.warn(timeList.stream().reduce((s , r) -> s + r).toString());


        SomeInterface someInterface = new SomeInterfaceImpl();
        final String apply = someInterface.apply("Hello", "world");
        System.out.println(apply);
    }
}
