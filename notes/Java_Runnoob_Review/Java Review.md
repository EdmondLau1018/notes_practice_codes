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

# JUC 包组织结构

| 包路径                      | 组织结构                                                     | 类型                                                         |
| --------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| java.util.concurrent        | 提供很多种最基本的并发工具类，包括对各类数据结构的并发封装，并发框架主要接口 | CountDownLatch，CyclicBarrier，Semaphore，Exchanger，Phaser，BlockingQueue，ConcurrentHashMap，ThreadPoolExecutor，ForkJoinPool |
| java.util.concurrent.atomic | 提供各类原子操作工具类                                       | AtomicInteger， DoubleAdder，LongAccumulator，AtomicReference |
| java.util.concurrent.locks  | 提供各类锁工具                                               | Lock，ReadWriteLock，ReentrantLock，StampedLock              |

# JUC 包内容结构

**锁（locks）部分**：提供适合各类场合的锁工具；
**原子变量（atomic）部分**：原子变量类相关，是构建非阻塞算法的基础；
**并发框架（executor）部分**：提供线程池相关类型；
**并发容器（collections） 部分**：提供一系列并发容器相关类型；
**同步工具（tools）部分**：提供相对独立，且场景丰富的各类同步工具，如信号量、闭锁、栅栏等功能；

![图片描述](Java%20Review.assets/5f68197e0955bfb210800620.jpg)

# AtomicInteger

> 什么是原子操作？

所谓原子操作，是一个独立的，不可分割的操作

AtomicInteger 工具类就是为了**简化整型变量的同步处理**而诞生的 || 当多线程同时操作一个整型变量的增减时，会出现运算结果错误的问题

## 核心方法

| 方法名称            | 作用                                                         |
| ------------------- | ------------------------------------------------------------ |
| **getAndAdd**       | 先获取当前 AtomicInter 中的数值 然后执行加法                 |
| **getAndIncrement** | 先获取值 ，再对当前值执行自增操作                            |
| **addAndGet**       | 先对 AtomicInter  中的数值执行 加法，获取执行加法之后的值    |
| **decrementAndGet** | 先对 AtomicInter   中的值进行自减 ，获取执行减法之后的值     |
| **compareAndSet**   | 将 AtomicInter  中的值与 expect 进行比较 如果相等 返回true 将 AtomicInter  更新为 update 中的值； 如果不相等 返回 false 不更新 AtomicInter   中的值 |

```java
  		AtomicInteger atomicInteger = new AtomicInteger();
        //  获取当前值
        System.out.println(atomicInteger.get());
        System.out.println("先获取值再执行加法：" + atomicInteger.getAndAdd(100));
        System.out.println("先获取值再自增：" + atomicInteger.getAndIncrement());


        System.out.println("先执行 add 再获取值：" + atomicInteger.addAndGet(-88));
        System.out.println("先自减再获取值：" + atomicInteger.decrementAndGet());

        System.out.println("操作之后的值：" + atomicInteger.get());
```

```java
    public static void testCompareAndSet(){
        AtomicInteger handler = new AtomicInteger(30);
        int expect = 30;
        int update  = 34;
        boolean res = handler.compareAndSet(expect, update);
        System.out.println("这俩值相等？" + res);
        System.out.println("结果：" + handler.get());
    }
```

## 案例

用售票作为案例，不同的售票窗口模拟不同的线程，操作同一份数据（当前余票数）

定义 全局变量 作为 当前余票数 通过开启不同的线程对 当前余票数量自减 

```java
public class AtomicIntegerDemo {

	//	定义十张票
    private static AtomicInteger currentTicketCount = new AtomicInteger(10);

    public static void main(String[] args) {
        //	定义 4 个售票窗口（开启 4 个线程）对总票数进行操作
        for (int i = 0; i <= 3; i++) {
            TicketOffice ticketOffice = new TicketOffice(currentTicketCount, i);
            new Thread(ticketOffice).start();
        }
    }
}
```

售票窗口业务线程类代码：

```java
public class TicketOffice implements Runnable {

    private AtomicInteger currentTicketCount;

    private String ticketOfficeNo;

    public TicketOffice(AtomicInteger currentTicketCount, Integer ticketOfficeNo) {
        this.currentTicketCount = currentTicketCount;
        this.ticketOfficeNo = "第" + ticketOfficeNo + "号售票窗口";
    }

    @Override
    public void run() {
        while (true) {
            if (currentTicketCount.get() < 1) {
                System.out.println("售票结束 " + ticketOfficeNo + "结束售票");
                return;
            }
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //  模拟售票过程
            int ticketNumber = currentTicketCount.decrementAndGet();
            if (ticketNumber >= 0) {
                System.out.println("第" + ticketOfficeNo + "号窗口" + "已出票，还剩：" + ticketNumber + "张票");
            }
        }
    }
}
```

# AtomicReference

AtomicReference 工具类直译为 “原子引用”

引用就是为对象另起一个名字，引用对象本身指向被引用对象，对引用对象的操作都会反映到被引用对象上。在 Java 中，引用对象本身存储的是被引用对象的 “索引值”。

![图片描述](Java%20Review.assets/5f6865cf0908c8c305760463-1712111101824-3.jpg)

如图，每个线程操作的都是 内存中真实对象的引用对象，而不是直接操作真实对象

## 核心方法

| 方法名称          | 作用                                                         |
| ----------------- | ------------------------------------------------------------ |
| **set**           | 可以使用不带参数的构造方法构造好对象后，再使用 set () 方法设置待封装的对象 |
| **getAndSet**     | 此方法以原子方式设置为给定值，并返回旧值。逻辑等同于先调用 get () 方法再调用 set () 方法 |
| **compareAndSet** | AtomicReference 初始化的对象 与 expect 比较 如果是 expect 引用，就将这个引用更新成 update 对象的引用 ，如果不是 返回 false 不更新这个引用 |

```java
        Car car = new Car("HX123","bmw","家用轿车");
        // 创建第一个对象的引用
        AtomicReference<Car> reference = new AtomicReference<>(car);
        //  新建一个对象
        Car newCar = new Car("AB456", "audi", "商务轿车");
        //  比较 将 第一个对象的引用 与 第一个对象进行比较
        reference.compareAndSet(car, newCar);
        System.out.println(reference.get());
```

最后输出的结果是 对象的引用被更改成为 newCar 对象

## 案例

通过 模拟抢车牌的业务逻辑 ，每个线程代表一个用户，每个用户有一次抢车牌的机会，我们对车牌对象创建引用，每个用户线程抢车牌的操作 相当于是 新建一个 随机数车牌对象与 原对象进行比较，之后修改这个引用对象为第一个线程新创建的对象，后续的线程在获取这个修改后的引用对象与原对象比较时 返回的值是 false 也就无法对 引用对象进行修改，相当于只有第一个修改引用对象的线程抢到了车牌

车牌实体类

```java
public class CarLicenseTag {

    private String licenseNo = "沪A 99900";

    private double price = 8000.0;

    public CarLicenseTag(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CarLicenseTag{" +
                "licenseNo='" + licenseNo + '\'' +
                ", price=" + price +
                '}';
    }
}
```

主函数

```java
public class AtomicReferenceDemo {

    //	创建唯一的车牌对象
    private static CarLicenseTag carLicenseTag =  new CarLicenseTag(8000);

    //	创建引用对象，第一个线程修改的也是这个引用对象的引用地址
    private static AtomicReference<CarLicenseTag> carLicenseTagAtomicReference = new AtomicReference<>(carLicenseTag);

    public static void main(String[] args) {

        for (int i = 0; i <= 5 ; i++) {
            AuctionCustomer auctionCustomer = new AuctionCustomer(carLicenseTagAtomicReference, 																carLicenseTag, i);

            new Thread(auctionCustomer).start();
        }
    }
}
```

用户业务类

```java
public class AuctionCustomer implements Runnable{

    private AtomicReference<CarLicenseTag> carLicenseTagAtomicReference;

    private CarLicenseTag carLicenseTag;

    private String customerNo;

    public AuctionCustomer(AtomicReference<CarLicenseTag> carLicenseTagAtomicReference, CarLicenseTag carLicenseTag, Integer customerNo) {
        this.carLicenseTagAtomicReference = carLicenseTagAtomicReference;
        this.carLicenseTag = carLicenseTag;
        this.customerNo = "第" +  customerNo + "位客户";
    }

    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(4000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //  模拟竞拍过程
        //	第一个线程将 引用对象修改为 新创建的对象 因为静态的引用对象被修改了 所以 与原对象的对比返回 false
        boolean result = carLicenseTagAtomicReference.compareAndSet(carLicenseTag,
                new CarLicenseTag(new Random().nextInt(10000)));
        System.out.println(customerNo + "竞拍结果：" + result + "当前竞拍信息：" + carLicenseTag);
    }
}
```

**注意：**

如果你通过 `atomicReference` 中的引用对对象进行了修改 （使用 `set()` 或者 `getAndSet()`方法对这个对象属性值进行修改），那么这个修改会影响到原来的对象，因为你是在操作对象本身。但 `compareAndSet` 方法本身不会修改对象的状态，它只是条件性地更新引用。

总结一下，`compareAndSet` 方法影响的是 `AtomicReference` 中存储的引用，而不是引用所指向的对象本身的状态。
