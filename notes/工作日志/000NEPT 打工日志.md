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

​	



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





# 99、 结语

<img src="./000NEPT 打工日志.assets/52508eb0e6132700881aca12b362aa3.jpg" alt="52508eb0e6132700881aca12b362aa3" style="zoom:50%;" />



