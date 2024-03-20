package com.edmond.c2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Demo1 {

    public static List<Person> createPeople() {
        ArrayList<Person> people = new ArrayList<>();
        Person person1 = new Person("小明", 15);
        Person person2 = new Person("小芳", 20);
        Person person3 = new Person("小李", 18);
        Person person4 = new Person("小付", 23);
        Person person5 = new Person("大飞", 22);
        people.add(person1);
        people.add(person2);
        people.add(person3);
        people.add(person4);
        people.add(person5);
        return people;
    }


    public static void main(String[] args) {
        //  获取年龄 >= 20 的所有人并进行打印
        List<Person> people = createPeople();
//        people.stream().filter(person -> person.getAge() >= 20).forEach(System.out::println);

        //  获取当前列表的前两个元素并进行打印
//        people.stream().limit(2).forEach(System.out::println);

        // 列表去重返回新列表
        List<Person> distinctedArr = people.stream().distinct().collect(Collectors.toList());
        System.out.println(distinctedArr);
    }

}
