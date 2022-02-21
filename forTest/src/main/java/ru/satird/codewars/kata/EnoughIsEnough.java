package ru.satird.codewars.kata;

import java.util.ArrayList;
import java.util.List;

public class EnoughIsEnough {
    public static int[] deleteNth(int[] elements, int maxOccurrences) {
        List<Integer> list = new ArrayList<>();
        if(maxOccurrences == 0) return new int[0];
        for(int j = 0; j < elements.length; j++) {
                if (list.isEmpty()) {
                    list.add(elements[j]);
                } else {
                    if (!list.contains(elements[j])) {
                        list.add(elements[j]);
                    } else {
                        int finalJ = j;
                        final long count = list.stream().filter(s -> s == elements[finalJ]).count();
                        if (count < maxOccurrences) {
                            list.add(elements[j]);
                        }
                    }
                }
        }
        final int[] ts = new int[list.size()];
        for(int i = 0; i < list.size(); i++) ts[i] = list.get(i);
        return ts;
    }

    public static void main(String[] args) {
        final int[] x = deleteNth(new int[] { 1, 1, 3, 3, 7, 2, 2, 2, 2 }, 0);
        for (int i : x) {
            System.out.println(i);
        }
    }
}
