# Java Review

# Stream 

Java 8 API添加了一个新的抽象称为流Stream，可以让你以一种声明的方式处理数据。

Stream 使用一种类似用 SQL 语句从数据库查询数据的直观方式来提供一种对 Java 集合运算和表达的高阶抽象。

这种风格将要处理的元素集合看作一种流， 流在管道中传输， 并且可以在管道的节点上进行处理， 比如筛选， 排序，聚合等。

**元素流** 在 **管道** 中经过 **中间操作**（intermediate operation）的处理，最后由 **最终操作** (terminal operation)得到前面处理的结果。

> Stream 的基本流程为：

```
+--------------------+       +------+   +------+   +---+   +-------+
| stream of elements +-----> |filter+-> |sorted+-> |map+-> |collect|
+--------------------+       +------+   +------+   +---+   +-------+
```

流：一组数据源的元素队列支持聚合操作

- **数据源** 流的来源。 可以是集合，数组，I/O channel， 产生器generator 等。
- **聚合操作** 类似SQL语句一样的操作， 比如filter, map, reduce, find, match, sorted等。
- **Pipelining**: 中间操作都会返回流对象本身。 这样多个操作可以串联成一个管道， 如同流式风格（fluent style）。 这样做可以对操作进行优化， 比如延迟执行(laziness)和短路( short-circuiting)。
- **内部迭代**： 以前对集合遍历都是通过Iterator或者For-Each的方式, 显式的在集合外部进行迭代， 这叫做外部迭代。 Stream提供了内部迭代的方式， 通过访问者模式(Visitor)实现。

## 生成流

在 **java8** 中集合有两种方式生成流

1. 使用 stream() 的方式生成流
2. 使用 parallelStream() 的方式生成流

## API 

| API         | 作用                                                         |
| ----------- | ------------------------------------------------------------ |
| **filter**  | 有数据过滤的功能，相当于通过匿名函数的方式进行数据过滤，匿名函数的内容返回的是 ：过滤的条件 |
| **forEach** | 在流处理的过程中 ，对每个元素进行处理 ；相当于 `for` 循环，在匿名函数中可以对 流中的每个元素进行业务操作 |
| **map**     | 通常用来映射每个元素 ，返回每个元素被映射后的结果            |
| **limit**   | 用来获取指定数量的流                                         |
| **sorted**  | 是一个排序匿名函数，通常用来根据 数据流中每个对象的某一属性进行排序 |

## 收集器

通常用于流操作的末尾 将操作完的数据流归档成为 可操作的数据类型 （如 :List Map String 等） 

也可以返回针对当前数据流的统计对象 调用的是当前流的 `summaryStatistics()` 方法

### Collectors

将一个列表去除空字符串之后合并成为一个 字符串，不同的元素之间用逗号分隔

```java
//        将 list 合并成 String 结果用 逗号分隔
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "", "", "", "", "abcd", "jkl");
        String colString = strings.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(","));
        System.out.println(colString);
```

### 统计

统计一个列表中的数据，返回统计对象

```java
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5, 33, 44, 66, 88);
        //  返回的是一个统计对象
        IntSummaryStatistics statistics = numbers.stream().mapToInt(i -> i).summaryStatistics();
        System.out.println(statistics.getMax());
        System.out.println(statistics.getMin());
        System.out.println(statistics.getAverage());
        System.out.println(statistics.getSum());
```



