package com.edmond.c1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Demo2 {
    /**
     * 将 字符串列表 合并成字符串 和  Map 集合
     * @param args
     */
    public static void main(String[] args) {
        List<String> strList = Arrays.asList("a", "ba", "bb", "abc", "cbb", "bba", "cab");
        String collString = strList.stream().collect(Collectors.joining(","));
        System.out.println(collString);
        Map<Object, Object> map = new HashMap<>();
        //  收集器 toMap 第一个匿名函数是当前键值对的 key 值 第二个匿名函数是当前键值对的 value 值
        map = strList.stream().collect(Collectors.toMap(str -> strList.indexOf(str), str -> str));
        for (Object o : map.keySet()) {
            System.out.println(o + ":" + map.get(o));
        }
    }
}
