package com.edmond.c1;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Demo1 {
    /**
     * 列表取平方倒序排序
     * @param args
     */
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        List<Integer> integerList = numbers
                .stream()
                .map(i -> i * i)
                .sorted((a, b) -> b - a)
                .collect(Collectors.toList());
        System.out.println(integerList);
    }
}
