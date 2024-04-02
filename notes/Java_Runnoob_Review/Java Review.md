# Java Review

# 反射

Java 的反射（reflection）机制是指在程序的**运行状态中**，可以构造任意一个类的对象，可以了解任意一个对象所属的类，可以了解任意一个类的成员变量和方法，可以调用任意一个对象的属性和方法。这种**动态获取程序信息以及动态调用对象**的功能称为 Java 语言的反射机制。反射被视为动态语言的关键。

![img](Java%20Review.assets/5ed066e809e20c1022080670.jpg)

**程序的运行时刻：** JVM在运行 class 文件的时候

**反射的功能：** 在程序运行期间动态获取 类中的属性和方法，从而动态的完成 对象实例化 ，属性赋值，调用方法等操作

 ## 反射常用类

| 类名            | 作用                                                       |
| --------------- | ---------------------------------------------------------- |
| **Class**       | `Class` 类的实例表示正在运行的 `Java` 应用程序中的类和接口 |
| **Constructor** | 用于访问单个构造方法信息                                   |
| **Field**       | 用来访问类或者接口的字段信息                               |
| **Method**      | 提供类或接口的某个方法信息                                 |

通过  `类名.Class` 的方式获取 这个类的 Class 对象 

> **class** 获取类对象 

可以通过类对象获取字段和方法

```java
            Class<PkgEntity> pkgEntityClass = PkgEntity.class;
            System.out.println("通过类对象直接获取字段：" + pkgEntityClass.getDeclaredField("pkgName"));
            System.out.println("通过类对象获取方法名称：" + pkgEntityClass.getDeclaredMethod("getPkgName"));
```

同样的 `getClass()`方法也可以在程序运行时获取类的对象

> **Constructor** 构造方法通过反射的方式构造实体类

通过 `getConstructor()` 方法获取这个类的构造函数

```java
            //  反射 执行构造方法创建一个对象
            Constructor<PkgEntity> constructor = pkgEntityClass.getConstructor();
            constructor.setAccessible(true);
            PkgEntity pkgEntity = constructor.newInstance();
            pkgEntity.setPkgName("com.example.MyPackage");
            pkgEntity.setJsonString("{\"key\":\"value\"}");
            pkgEntity.setOpCode("user123");
            pkgEntity.setResInfo("Success");
            pkgEntity.setResCode("200");
            pkgEntity.setResMsg("Operation completed successfully.");
            System.out.println("----------------------构造方法创建的对象---------------------");
            System.out.println(pkgEntity);
```

> **getDeclaredField **() 返回 **Field** 类的对象修改字段值

通过 class 对象的 `getDeclaredField()` 获取字段 `Field`实例对象 

*  参数：类中的字段名称

```java
  			 //  通过反射获取字段 并且修改字段的值
            Field pkgName = pkgEntity.getClass().getDeclaredField("pkgName");
            pkgName.setAccessible(true);
            System.out.println("修改前的字段值为：" + pkgName.get(pkgEntity));
            pkgName.set(pkgEntity,"update_batch");
            System.out.println("修改后的字段值为：" + pkgEntity.getPkgName());
```

> **getDeclaredMethod()** 通过反射调用方法 

`getDeclaredMethod()` 获取方法 

* 参数1：方法名称
* 参数2 -- n ： 方法参数数据类型

```java
 			java.lang.String sObject = new java.lang.String("大破鞋");
            Method replace = sObject.getClass().getDeclaredMethod("replace", char.class, char.class);
            replace.invoke(sObject,"大","龙背上的");
            System.out.println(sObject);
```

## 反射工具方法

入参是一个 列表，这个方法通过获取字段 并比对字段名称的方式 将字段中含有 `Data`  `NO`   `Name`  且属性值为 null 的属性赋值为 0

```java
    // 将多余的元素设置为 0
    public static void processEntities(List<InOutEntity> inOutEntityList) {
        for (InOutEntity entity : inOutEntityList) {
            Field[] fields = InOutEntity.class.getDeclaredFields();
            for (Field field : fields) {
                try {
                    // 使私有字段也可以被访问
                    field.setAccessible(true);
                    // 检查字段值是否为null，且字段名不包含特定字符串
                    if (field.get(entity) == null && !field.getName().matches(".*(Date|Name|No).*")) {
                        // 将null值的字段设置为"0"
                        field.set(entity, "0");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
```

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

### 集合创建 Stream

1. `default Stream<E> stream()`：返回一个串行流（顺序流）；
2. `default Stream<E> parallelStream()`：返回一个并行流。

串行流并行流的区别是：串行流从集合中取数据是按照集合的顺序的；而并行流是并行操作的，获取到的数据是无序的

```java
// 创建一个集合，并添加几个元素  
List<String> stringList = new ArrayList<>();  
stringList.add("hello");  
stringList.add("world");  
stringList.add("java");  
​  
// 通过集合获取串行 stream 对象  
Stream<String> stream = stringList.stream();  
// 通过集合获取并行 stream 对象  
Stream<String> personStream = stringList.parallelStream();
```

### 数组创建 Stream

通过` Arrays.stream()` 创建流

```java
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamDemo1 {

    public static void main(String[] args) {
        // 初始化一个整型数组
        int[] arr = new int[]{1,2,3};
        // 通过整型数组，获取整形的 stream 对象
        IntStream stream1 = Arrays.stream(arr);

        // 通过字符串类型的数组，获取泛型类型为 String 的 stream 对象
        String[] stringArr = new String[]{"Hello", "imooc"};
        Stream<String> stream2 = Arrays.stream(stringArr);
    }
}
```

### Stream.of() 方法

```java
import java.util.stream.Stream;

public class StreamDemo1 {

    public static void main(String[] args) {
        // 通过 Stream 类下的 of() 方法，创建 stream 对象、
        Stream<Integer> stream = Stream.of(1, 2, 3);
    }
}
```

## 中间操作

| API             | 作用                                                         |
| --------------- | ------------------------------------------------------------ |
| **筛选与切片**  |                                                              |
| **filter**      | 有数据过滤的功能，相当于通过匿名函数的方式进行数据过滤，匿名函数的内容返回的是 ：过滤的条件 |
| **distinct**    | 通过流元素生成的 eauqals 和 hashCode 方法去除重复元素        |
| **limit**       | 截断流 获取流的 前 N 个元素                                  |
| **skip**        | 跳过元素 返回一个扔掉前 n 个 元素的流 如果当前流中的元素数量不足 n 个则返回一个空流 |
| **映射**        |                                                              |
| **map**         | 通常用来映射每个元素 ，返回每个元素被映射后的结果            |
| **mapToDouble** | 接收一个方法作为参数，该方法会被应用到每个元素上，产生一个新的`DoubleStream`； |
| **mapToLong**   | 接收一个方法作为参数，该方法会被应用到每个元素上，产生一个新的`LongStream`； |
| **flatMap**     | 接收一个方法作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流。 |
| **排序**        |                                                              |
| **sorted**      | 不传参数 按照自然排序方式排序 ，该操作的参数是一个比较器 按照比较器的顺序 进行排序 |

> 案例

**筛选与切片：**

```java
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
        people.stream().filter(person -> person.getAge() >= 20).forEach(System.out::println);

        //  获取当前列表的前两个元素并进行打印
        people.stream().limit(2).forEach(System.out::println);

        // 列表去重返回新列表
        List<Person> distinctedArr = people.stream().distinct().collect(Collectors.toList());
        System.out.println(distinctedArr);
    }

}
```

**映射：**

```java
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
```

**比较：**

```java
public class DemoSort {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(10, 12, 9, 8, 20, 1);
        integers.stream().sorted((a,b) -> Integer.compare(a,b)).forEach(System.out::println);
        //  lambda 表达式改写
        integers.stream().sorted(Integer::compare).forEach(System.out::println);
    }
}
```

## 终止操作

| API                   | 作用                                                         |
| --------------------- | ------------------------------------------------------------ |
| **匹配与查找**        |                                                              |
| **allMatch**          | 检查是否匹配所有元素； 返回的是 布尔值 `boolean`             |
| **anyMatch**          | 检查是否至少匹配一个元素； 返回的是 布尔值 `boolean`         |
| **noneMatch**         | 检查是否没有匹配所有元素；  返回的是 布尔值 `boolean`        |
| **findFirst**         | 返回第一个元素；返回的是 `Optional` 类                       |
| **findAny**           | 返回当前流中的任意元素；返回的是 `Optional` 类               |
| **count**             | 返回流中元素总数；返回的是 `Optional` 类                     |
| **max**               | 返回流中的最大值；返回的是 `Optional` 类                     |
| **min**               | 返回流中的最小值；返回的是 `Optional` 类                     |
| **forEach**           | 内部迭代                                                     |
| **规约**              |                                                              |
| **reduce**            | 可以将流中的元素反复结合起来，得到一个值                     |
| **收集**              |                                                              |
| **collect**           | 将流转换为其他形式。接收一个`Collector`接口的实现，用于给`Stream`中元素做汇总的方法。 |
| **统计**              |                                                              |
| **summaryStatistics** | 在结合映射操作之后 返回一个统计对象，通过统计对象可以获取当前流的统计、平均值、最大最小、个数等汇总信息 |

>  案例

**匹配与查找：**

内部参数匿名函数都是 `Predicate` 形式 

```java
public class DemoMatch1 {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(10, 12, 9, 8, 20, 1, -1);
        boolean res = integers.stream().allMatch(p -> p > 0);
        System.out.println("所有的元素都 > 0吗：" + res);

        boolean b = integers.stream().anyMatch(integer -> integer > 0);
        System.out.println("有大于 0 的元素吗？" + b);

        //  是不是一个匹配的元素都没有
        boolean b1 = integers.stream().noneMatch(integer -> integer > 10000);
        System.out.println("是不是一个  > 10000 的元素都没有？" + b1);

    }
}
```

```java
   		 // 查找元素
        Optional<Integer> first = integers.stream().findFirst();
        Integer integer = first.get();
        System.out.println(integer);

        Optional<Integer> max = integers.stream().max(Comparator.comparingInt(i -> i));
        System.out.println(max.get());

        Optional<Integer> min = integers.stream().min(Integer::compare);
        System.out.println(min.get());
```

**规约：**

```java
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
```

**收集：太常用了略**

**统计**

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

# JUC 



