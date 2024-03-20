package com.edmond.c1;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class createStream {
    public static void main(String[] args) {
//        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "jkl");
//        List<String> stringList = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
////        System.out.println(stringList);
//
//        //  forEach 迭代流中的每一个数据
//        strings.stream().filter(s -> !s.isEmpty()).forEach(System.out::println);
//-----------------------------------------------------------------------------------------------------------------------
        //  通过 Map 获取List 中 每个数的平方并输出
//        List<Integer> list = Arrays.asList(1, 2, 55, 66, 77, 34, 6, 99, 17);
//        list.stream().map(i -> i * i).forEach(System.out::println);

//        ---------------------------------------------------------------------------------------------------------------

        //  统计空字符串的数量
//        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "", "", "", "", "abcd", "jkl");
//        long count = strings.stream().filter(s -> s.isEmpty()).count();
//        System.out.println("当前列表中有：" + count + "个空字符串");

//        ----------------------------------------------------------------------------------------------------------------
        // 随机生成10个数字并进行排序
//        Random random = new Random();
//        random.ints().limit(10).sorted().forEach(System.out::println);
//        ---------------------------------------------------------------------------------------------------
//        parallelStream    并行流
//        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "", "", "", "", "abcd", "jkl");
//        long count = strings.parallelStream().filter(s -> !s.isEmpty()).count();
//        System.out.println("当前列表中有：" + count + "个空字符串");

//        ------------------------------------------------------------------------------------------------------------------
//        将 list 合并成 String 结果用 逗号分隔
//        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "", "", "", "", "abcd", "jkl");
//        String colString = strings.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(","));
//        System.out.println(colString);
//
//        --------------------------------------------------------------------------------------------------------------
//        统计
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5, 33, 44, 66, 88);
        //  返回的是一个统计对象
        IntSummaryStatistics statistics = numbers.stream().mapToInt(i -> i).summaryStatistics();
        System.out.println(statistics.getMax());
        System.out.println(statistics.getMin());
        System.out.println(statistics.getAverage());
        System.out.println(statistics.getSum());
    }
}
