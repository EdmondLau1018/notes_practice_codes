# 	NEPT 打工日志

<img src="./000NEPT 打工日志.assets/036bf6a6fe8b07560ccf209d56e749f.jpg" alt="036bf6a6fe8b07560ccf209d56e749f" style="zoom:30%;" />



# 0、环境配置相关坑

## 0-1 后端

> 后端问题 

在第一次配置完 Java 环境和 maven 之后 使用 idea 2021 打开项目 

出现错误如下：

```bash
java.lang.RuntimeException: java.lang.RuntimeException: org.codehaus.plexus.component.repository.exception.ComponentLookupException: com.google.inject.ProvisionException: Unable to provision, see the following errors:
 
1) Error injecting constructor, java.lang.NoSuchMethodError: org.apache.maven.model.validation.DefaultModelValidator: method <init>()V not found
  at org.jetbrains.idea.maven.server.embedder.CustomModelValidator.<init>(Unknown Source)
  while locating org.jetbrains.idea.maven.server.embedder.CustomModelValidator
  at ClassRealm[maven.ext, parent: ClassRealm[plexus.core, parent: null]] (via modules: org.eclipse.sisu.wire.WireModule -> org.eclipse.sisu.plexus.PlexusBindingModule)
  while locating org.apache.maven.model.validation.ModelValidator annotated with @com.google.inject.name.Named(value=ide)
 
 
	... 24 more
 
```

出现的错误的原因为Maven版本过高 ，与你使用的IDEA版本不兼容。

**解决方法**

下载低版本的 maven 重新配置maven 环境变量 ，之前用的maven 版本是 3.8.x 现在将 maven 版本 替换成 3.6.x 即可 

![e7f21497b09e98280bab414c22605b9](F:\NEPT_projects\CODE\notes\000NEPT 打工日志.assets\e7f21497b09e98280bab414c22605b9-1709858473134-4.png)

>  maven 手动安装 jar 包全过程

问题是 maven 找不到 jar 包 

可以使用Maven的`install:install-file`命令 进行手动安装

实例命令：

```bash
mvn install:install-file -Dfile=<path-to-file> -DgroupId=<group-id> -DartifactId=<artifact-id> -Dversion=<version> -Dpackaging=<packaging>
```

- `<path-to-file>` 是你的JAR文件的完整路径。
- `<group-id>` 是你的项目的组ID。
- `<artifact-id>` 是你的项目的工件ID。
- `<version>` 是你的项目的版本号。
- `<packaging>` 是你的包装类型，对于JAR文件，这应该是`jar`。

案例：

```bash
mvn install:install-file -Dfile=NetDeviceSDKP2-2.2.5.jar -DgroupId=rk.netDevice.sdk.p2                      -DartifactId=NetDeviceSDKP2 -Dversion=2.5.5 -Dpackaging=jar
```

在 pom 文件中的引用格式为：

```xml
        <dependency>
            <groupId>rk.netDevice.sdk.p2</groupId>
            <artifactId>NetDeviceSDKP2</artifactId>
            <version>2.2.5</version>
        </dependency>
```

## 0-2 前端

> 前端问题

使用 npm 安装依赖 出现的错误 

```bash
npm intasll 安装报错 Error: EPERM: operation not permitted, mkdir ‘C:\Program Files\nodejs\node_cache\_
```

没有权限创建文件夹（目录）

是因为 在配置 `npm_global ` 与 `npm_cache ` 的时候将这两个目录放在了nodejs 的安装目录下  在执行 install 的时候当前 Windows 用户不具有对 nodejs 安装目录完全控制的权限 

**解决方法：**

不在nodejs 安装目录下 重新新建这两个缓存目录 对这个路径进行重新配置

对应的命令分别是：

```bash
npm config set prefix "D:\envs\node_repository\node_global"
 
npm config set cache  "D:\envs\node_repository\node_cache"
```

配置完之后可以通过 命令 

```bash
npm config list 
```

查看当前 npm 所有的配置信息

在配置完之后 在 `	node_global` 目录下新建目录 `node_modules` ，然后进行环境变量配置

系统变量中配置 `node_modules` 目录所在位置 

<img src="F:\NEPT_projects\CODE\notes\000NEPT 打工日志.assets\image-20240304153019111.png" alt="image-20240304153019111" style="zoom:67%;" />

在 系统变量 `path` 中新增这个配置

<img src="F:\NEPT_projects\CODE\notes\000NEPT 打工日志.assets\1709537795009.png" alt="1709537795009" style="zoom:67%;" />

配置完环境变量之后可以 通过 全局安装 express 模块验证一下是否成了

```bash
npm install express -g    # -g是全局安装的意思
```

> 在使用 npm 安装依赖时出现服务器 证书过期的错误 

安装依赖时使用的命令为：

```javascript
    "pre": "cnpm install || yarn --registry https://registry.npm.taobao.org || npm install --registry  https://registry.npm.taobao.org ",
```

出现的错误：

```bash
npm ERR! request to https://registry.npm.taobao.org/@antv%2fg2-brush failed, reason: certificate has expired
```

提示的问题是 服务器证书过期

2021年淘宝就发文称，npm 淘宝镜像已经从 registry.npm.taobao.org 切换到了 registry.npmmirror.com，旧域名也将于 2022 年 5 月 31 日停止服务（但是实际上也不是2022年5月31日就不能用了，HTTPS 证书到期了才不能用

**解决方案：**

将本机的 registry 切换成 `registry.npmmirror.com`

首先需要清除 npm 缓存 ：`npm cache clean --force`

设置新的 registry :`npm config set registry https://registry.npmmirror.com`

通过 `npm config list` 查看所有配置信息：

```bash
C:\Users\Liulo>npm config list
; cli configs
metrics-registry = "https://registry.npmmirror.com/"
scope = ""
user-agent = "npm/6.14.18 node/v14.21.3 win32 x64"

; userconfig C:\Users\Liulo\.npmrc
cache = "D:\\envs\\node_repository\\node_cache"
prefix = "D:\\envs\\node_repository\\node_global"
registry = "https://registry.npmmirror.com/"

; builtin config undefined

; node bin location = D:\envs\nodeJS\node.exe
; cwd = C:\Users\Liulo
; HOME = C:\Users\Liulo
; "npm config ls -l" to show all defaults.
```

**针对公司项目情况**，**因为本机环境使用的是** npm **进行依赖下载** 需要将 pre **配置换成** **以下内容**

（npm 下载命令的 registry 换了）

```javascript
    "pre": "cnpm install || yarn --registry https://registry.npm.taobao.org || npm install --registry https://registry.npmmirror.com ",
```

## 0-3 数据库

# 1、使用 easyExcel 进行数据导入

# 1.3、现场代码部署-同步依赖

# 2、Java8 stream：根据特定字段将单个 List 拆分为多个

# 3、mysql 查询 union 用法

## 3.1、MYSQL 函数封装 生成采制化编码

> 生成采样编码：

```sql
CREATE
    DEFINER = `root`@`%` FUNCTION `generate_sample_code`() RETURNS VARCHAR(100)
BEGIN
    DECLARE iv_sampleCode varchar(100);
    DECLARE iv_batchNo_day varchar(100);
    DECLARE iv_count int(5) default 0;
    -- 使用 UNION ALL 确保总有一个返回值  即使 第一个select 语句中返回的是空行 通过当前时间生成的结果仍可以返回
    SELECT day_batch_no into iv_batchNo_day FROM (
                                 SELECT date_format(INSERT_TIME, '%Y%m%d') AS day_batch_no
                                 FROM batch_no_info
                                 WHERE DATE_FORMAT(insert_time, '%Y%m%d') = date_format(sysdate(), '%Y%m%d')
                                 UNION ALL
                                 SELECT date_format(sysdate(), '%Y%m%d')
                                 LIMIT 1  -- 假设原查询没有返回结果，这会确保默认值被返回
                             ) AS result
    ORDER BY day_batch_no DESC
    LIMIT 1;
    -- 生成采样编码
    set iv_count = 1;
    WHILE iv_count != 0
        DO
            set iv_sampleCode = CONCAT(iv_batchNo_day, lpad(round(RAND() * 1000), 3, '0'));
            select count(1)
            into iv_count
            from batch_no_info b
            where b.sample_code = iv_sampleCode
               or b.sampling_code = iv_sampleCode
               or b.labor_code = iv_sampleCode;
        END WHILE;
    RETURN iv_sampleCode;
END;
```

# 4、存储过程从 json 对象中获取属性

# 5、 Antd 表格设置

# 6、对象拷贝工具方法

在使用 Spring 自带的工具方法 `BeanUtils.copyProperties(source,target)` 进行对象拷贝的时候会将 `source` 对象中的所有属性全部拷贝到 `target` 中，并对 `target` 对象中原有的属性全部覆盖  

原来的代码

```java
 		List<CoLabEntity> coLabEntities = coUreportMapper.qryCoLabReportList(Param);
        for (CoLabEntity coLabEntity : coLabEntities) {
            HashMap<String, String> params = new HashMap<>();
            params.put("relaBatchNo",coLabEntity.getRelaBatchNo());
            List<CoLabEntity> list = coUreportMapper.qryCoLabReportCcyDiffList(params);
            if (list.size() == 0) continue;
            CoLabEntity entity = list.get(0);
            if (entity.getAadCcyDiff() == null||entity.getMtCcyDiff() == null) continue;
            BeanUtils.copyProperties(entity,coLabEntity);
        }
```

`coLabEntity` 中原有的属性会被覆盖

为了避免这种情况的出现 对 `BeanUtils.copyProperties();` 代码进行封装，通过自定义工具方法 完成对象拷贝

 ```java
  // spring.bean.BeanUtils     对象拷贝忽略 source 中的 null 属性
     public static void copyPropertiesIgnoreNull(Object source, Object target) {
         //	创建包装器
         final BeanWrapper src = new BeanWrapperImpl(source);
         //	通过包装器获取 source 对象中的属性
         PropertyDescriptor[] pds = src.getPropertyDescriptors();
 		
         //	用于存储 null 属性的名称
         Set<String> emptyNames = new HashSet<>();
         for(PropertyDescriptor pd : pds) {
             //	通过包装器获取属性值
             Object srcValue = src.getPropertyValue(pd.getName());
             //	如果当前属性值 为 null 将当前属性名称添加到 上述集合中
             if (srcValue == null) emptyNames.add(pd.getName());
         }
         //	将集合转换成数组，这个数组中包括所有 属性值为 null 的属性名称
         String[] result = new String[emptyNames.size()];
         String[] ignoreProperties = emptyNames.toArray(result);
 
         //	调用 spring 的 copyProperties 方法进行对象拷贝 将 ignoreProperties 数组作为参数传入 
         org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreProperties);
     }
 ```

在调用的时候直接将 

```java
 BeanUtils.copyProperties(entity,coLabEntity);
```

替换为 

```java
  copyPropertiesIgnoreNull(entity,coLabEntity);
```

即可

该方法在复制对象属性时忽略源对象中值为 `null` 的属性。这意味着，如果源对象的某个属性值为 `null`，那么目标对象中对应的属性值将不会被覆盖。下面逐步解释这段代码的工作原理：

1. **创建BeanWrapper实例**：
   使用 `BeanWrapperImpl` 创建一个源对象 `source` 的包装器(`BeanWrapper`)实例。`BeanWrapper` 是Spring Framework提供的一个工具类，用于操作JavaBean对象的属性。它提供了对JavaBean属性的获取、设置以及其他操作的功能。
2. **获取属性描述符**：
   通过 `getPropertyDescriptors` 方法获取源对象所有属性的描述符数组(`PropertyDescriptor[]`)。每个 `PropertyDescriptor` 描述了一个属性，包括属性的名称、读方法(getter)和写方法(setter)等信息。
3. **检查并收集值为null的属性名称**：
   遍历属性描述符数组，使用 `BeanWrapper` 的 `getPropertyValue` 方法获取每个属性的值。如果属性值为 `null`，则将该属性的名称添加到一个 `Set<String>` 集合 `emptyNames` 中。这个集合用于存储所有值为 `null` 的属性名称。
4. **将集合转换为数组**：
   将 `emptyNames` 集合转换为一个字符串数组 `ignoreProperties`。这个数组包含了所有应该在复制过程中被忽略的属性名称，即那些源对象中值为 `null` 的属性。
5. **使用Spring的BeanUtils复制属性**：
   调用Spring Framework的 `BeanUtils.copyProperties` 方法复制属性，但是传入 `ignoreProperties` 数组作为最后一个参数。这告诉 `BeanUtils.copyProperties` 方法在复制属性时忽略 `ignoreProperties` 数组中列出的那些属性。因此，源对象中值为 `null` 的属性不会被复制到目标对象中。

通过这种方式，可以在对象间复制属性时保留目标对象中已有的非空值，即使源对象在相应的属性上有 `null` 值。这对于处理只想更新部分属性或在某些属性未设置时保留旧值的场景非常有用。

# 7、存储过程中游标的使用

> 流程基本语法

```sql
    -- 声明结束标志变量
    DECLARE done INT DEFAULT FALSE;
    DECLARE 游标名称 cursor for  查询语句
    # 设置游标结束标志
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    ...
    
    	-- 打开游标
        OPEN 游标名称;
        -- 循环遍历游标中的所有行
        read_loop:
        LOOP
        	-- 从游标抓取下一行数据给对应变量赋值
            FETCH batchCursor INTO iv_sampleCode, iv_samplingCode, iv_laborCode;
            -- 没有更多的数据 （done） 被设置为true
            IF done THEN
                LEAVE read_loop; -- 跳出循环
            END IF;
			...
          	-- 业务代码 || 业务操作
			...
        END LOOP;
```

**注意事项：**

* 定义游标的时候一定要在 DECLARE 区域进行定义
* 循环是通过 `FETCH` 语句在没有更多数据可读时触发设置的处理器来结束的。处理器将 `done` 变量设置为 `TRUE`，然后在循环体内部检查该变量以决定是否继续执行循环。这种方法确保了只要游标中还有数据未被处理，循环就会继续执行；一旦数据被处理完毕，循环就会结束。
* 刚开始需要手动声明游标结束的变量

* 在存储过程中 如果游标声明的条件变量是在当前存储过程中的业务区域赋值的，不用在意顺序问题

**案例：**

```sql
create
    definer = root@`%` procedure delete_ccyBatchInfo_byBatch(IN i_jsonString varchar(4000), IN i_opCode varchar(100),
                                                             OUT o_resInfo varchar(1000), OUT o_resCode varchar(100),
                                                             OUT o_resMsg varchar(8000))
Main1:
BEGIN
    DECLARE iv_jsonString varchar(4000);
    DECLARE iv_resCode_lab VARCHAR(100); # 内部调用的存储过程返回值
    DECLARE iv_resMsg_lab text; # 内部调用的存储过程返回信息
    DECLARE iv_resInfo_lab VARCHAR(4000);# 内部调用的存储过程返回数据
    DECLARE iv_is_exception INT(10); # 手动异常退出标识符
    DECLARE iv_exception VARCHAR(4000);# 出现异常的自定义异常信息
    DECLARE iv_jsonParam json;

    -- 声明结束标志变量
    DECLARE done INT DEFAULT FALSE;

    -- 业务变量
    DECLARE iv_batchNo VARCHAR(100);
    DECLARE iv_relaBatchNo VARCHAR(100);
    DECLARE iv_sampleCode VARCHAR(100);
    DECLARE iv_samplingCode VARCHAR(100);
    DECLARE iv_laborCode VARCHAR(100);
    DECLARE batchCursor cursor for select b.SAMPLE_CODE, b.SAMPLING_CODE, b.LABOR_CODE
                                   from batch_no_info b
                                            inner join lab_report l on b.LABOR_CODE = l.LABOR_CODE
                                   where b.BATCH_RELA_ID = iv_batchNo;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;


    DECLARE EXIT HANDLER FOR SQLEXCEPTION # 定义OuterLayer最外层异常，接收到异常或者内部上抛的异常，直接跑到调用方，不再做任何事务处理
    # 定义捕获异常具体操作
        BEGIN
            -- 这两句是一个完整的操作，切记：是将错误代码和信息赋给变量
            GET DIAGNOSTICS CONDITION 1 iv_resCode_lab = RETURNED_SQLSTATE, iv_resMsg_lab = MESSAGE_TEXT;
            # 将错误信息代码及信息捕获
            ## 完善异常信息
            SET iv_resMsg_lab = CONCAT('Main1-->', iv_resMsg_lab); ## 捕获到异常，完善一下异常信息，调用方好获取具体问题所在位置
            RESIGNAL SET MESSAGE_TEXT = iv_resMsg_lab;
        END;

    set iv_resMsg_lab = '';
    -- 将传入的varchar 转换为json
    SET iv_jsonString = CAST(i_jsonString AS json);

    SET o_resCode = '0';
    SET o_resMsg = 'ok';
    SET o_resInfo = '删除存查样信息成功';

    -- 获取jsonString参数中的对应的json数据赋值到变量；
    set iv_jsonParam = iv_jsonString;
    set iv_batchNo = iv_jsonParam ->> '$.batchNo';

    IF LEFT(iv_batchNo, 2) = 'QC' then
        -- 打开游标
        OPEN batchCursor;
        -- 循环遍历游标中的所有行
        read_loop:
        LOOP
            FETCH batchCursor INTO iv_sampleCode, iv_samplingCode, iv_laborCode;
            IF done THEN
                LEAVE read_loop;
            END IF;

            select count(1) into @cnt from lab_report where LABOR_CODE = iv_laborCode;
            if @cnt > 0 then
                delete from lab_report where LABOR_CODE = iv_laborCode;
            end if;

        END LOOP;

        -- 关闭游标
        CLOSE batchCursor;
    elseif LEFT(iv_batchNo, 3) = 'CCY' then
        select LABOR_CODE into iv_laborCode from batch_no_info where BATCH_NO = iv_batchNo;
        select count(1) into @cnt from lab_report where LABOR_CODE = iv_laborCode;
        if @cnt > 0 then
            delete from lab_report where LABOR_CODE = iv_laborCode;
        end if;
        delete from batch_no_info where BATCH_NO = iv_batchNo;
    end if;
end;
```

# 8、使用反射写一个工具方法

遍历一个列表，检查属性名称中含有 Date Name No 的字段 如果属性值是  null 则将属性值赋值为 0

```java
    public static void processEntities(List<InOutEntity> inOutEntityList) {
        for (InOutEntity entity : inOutEntityList) {
            //	获取实体类中的所有字段并返回一个数组
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

1. getDeclaredFields() 是 Java 反射中的一个方法 这个方法可以获取一个类中声明的所有字段，包括公共（public）、保护（protected）、默认（package）访问和私有（private）字段，但不包括继承的字段。当你对一个 `Class` 类型的对象调用 `getDeclaredFields()` 方法时，它会返回一个 `Field` 类型的数组，其中包含了类中所有声明的字段。
2. Field 类型的对象通过 `get(这个类的对象)` 这种形式获取属性值 ，通过 `getName()` 方法获取属性名。
3. `getDeclaredFields()` 方法提供了对类的内部信息的访问，因此使用它时需要处理安全和权限问题。默认情况下，你可能无法访问私有字段，除非通过 `setAccessible(true)` 方法来覆盖访问控制。
4. 它允许程序在运行时检查和修改对象的内部状态，这在某些高级编程场景中非常有用，如序列化、对象映射、动态代理等。然而，由于它破坏了封装原则，可能导致代码难以理解和维护，因此建议谨慎使用。

# 98、问题思考

如果有的情况需要在前端隐藏记录的主键 如何操作？常用的前端隐藏主键的解决方案？



# 99、 结语

<img src="./000NEPT 打工日志.assets/52508eb0e6132700881aca12b362aa3.jpg" alt="52508eb0e6132700881aca12b362aa3" style="zoom:50%;" />





