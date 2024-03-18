# MYSQL 专项内容

# 客户端请求与存储引擎

## 存储引擎



>比如我们想创建一个存储引擎为`MyISAM`的表可以这么写：

```sql
create table if not EXISTS `engine_demo_table`(
	id int(5)
) ENGINE = MyISAM;
```

