package com.edmond.c4;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Demo2Reduce {
    public static void main(String[] args) {
        // 创建一个整型列表
        List<Integer> integers = Arrays.asList(10, 12, 9, 8, 20, 1);

        // 使用 reduce(T identity, BinaryOperator b) 计算列表中所有整数和
        Integer sum = integers.stream().reduce(0, Integer::sum);
        System.out.println(sum);

        // 使用 reduce(BinaryOperator b) 计算列表中所有整数和，返回一个 Optional<T>
        Optional<Integer> reduce = integers.stream().reduce(Integer::sum);
        System.out.println(reduce);
    }
}
