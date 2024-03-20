# MYSQL 专项内容

# 客户端请求与存储引擎

## 服务器处理客户端请求

<img src="MYSQL%20%E4%B8%93%E9%A1%B9%E5%86%85%E5%AE%B9.assets/167f4c7b99f87e1ctplv-t2oaga2asx-jj-mark1890000q75.webp" alt="image_1c8d26fmg1af0ms81cpc7gm8lv39.png-97.9kB" style="zoom:80%;" />





## 存储引擎



>比如我们想创建一个存储引擎为`MyISAM`的表可以这么写：

```sql
create table if not EXISTS `engine_demo_table`(
	id int(5)
) ENGINE = MyISAM;
```

