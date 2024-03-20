package com.edmond.c3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Demo1 {
    public static void main(String[] args) {

        // 创建一个包含小写字母元素的字符串列表
        List<String> stringList = Arrays.asList("php", "js", "python", "java");
        // 调用 map() 方法，将String下的toUpperCase()方法作为参数，这个方法会被应用到每个元素上，映射成一个新元素，最后打印映射后的元素
        stringList.stream().map(String::toUpperCase).forEach(System.out::println);

        //   返回一个 以 idx 为 key 的map
        Map<Integer, String> map = new HashMap<>();
        map = stringList.stream().collect(Collectors.toMap(str -> stringList.indexOf(str), str -> str));
        for (Integer idx : map.keySet()) {
            System.out.println(idx + ":" + map.get(idx));
        }

    }
}
