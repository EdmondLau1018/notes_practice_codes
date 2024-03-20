package com.edmond.c3;

import java.util.Arrays;
import java.util.List;

public class DemoSort {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(10, 12, 9, 8, 20, 1);
        integers.stream().sorted((a,b) -> Integer.compare(a,b)).forEach(System.out::println);
        //  lambda 表达式改写
        integers.stream().sorted(Integer::compare).forEach(System.out::println);
    }
}
