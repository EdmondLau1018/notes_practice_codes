package com.edmond.c4;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DemoMatch1 {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(10, 12, 9, 8, 20, 1, -1);
//        boolean res = integers.stream().allMatch(p -> p > 0);
//        System.out.println("所有的元素都 > 0吗：" + res);
//
//        boolean b = integers.stream().anyMatch(integer -> integer > 0);
//        System.out.println("有大于 0 的元素吗？" + b);
//
//        //  是不是一个匹配的元素都没有
//        boolean b1 = integers.stream().noneMatch(integer -> integer > 10000);
//        System.out.println("是不是一个  > 10000 的元素都没有？" + b1);

        Optional<Integer> any = integers.stream().findAny();
        System.out.println(any.get());

        // 查找元素
        Optional<Integer> first = integers.stream().findFirst();
        Integer integer = first.get();
        System.out.println(integer);

        Optional<Integer> max = integers.stream().max(Comparator.comparingInt(i -> i));
        System.out.println(max.get());

        Optional<Integer> min = integers.stream().min(Integer::compare);
        System.out.println(min.get());
    }
}
