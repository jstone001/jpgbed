https://www.bilibili.com/video/BV1jJ411M78w?p=4

# 概述

## P1-Sharding-JDBC分库分表专题内容介绍

## P2-概述-分库分表是什么
## P3-概述-分库分表的方式-垂直分表

重直拆分：商品的基础信息  商品的详情

提升：

- 为了避免IO争抢并减少锁表的几率，查看详情的用户与商品信息浏览互不影响。

- 充分发挥热数遍的操作效率，商品信息的操作的商效率不会被冷数据拖累

  > <font color='red'>为什么大字段IO小效率低？</font>
  >
  > 1. 由于数据量本身大，需要更长的读取时间；
  > 2. 跨页。页是数据库存储单位，很多查找及定位操作都是以页为单位，单页内的数据行越多数据库整体性能越好，而大字段占用空间大，单页内存储行数少，因此IO效率低。
  > 3. 数据库以行为单位加载到内存中，这样表中字段长度较短且访问频率较高，内存能加载更多的数据，命中率更高，减少了磁盘IO，从而提升了数据库性能。

## P4-概述-分库分表的方式-垂直分库



## P5-概述-分库分表的方式-水平分库

将店铺ID为单数，或双数的商品信息分别放在2个库中

## P6-概述-分库分表的方式-水平分表
## P7-概述-分库分表的方式-总结
## P8-概述-分库分表所带来的问题

- 事务一致性
- 跨节点关联查询
- 跨节点分页，排序函数
- 主键重复
- 公共表

## P9-概述-Sharding-JDBC介绍

官网：https://shardingsphere.apache.org/document/current/cn/overview/

ShardingSphere

- Sharding-JDBC
- Sharding-Proxy
- Sharding-Sidecar

## P10-概述-Sharding-JDBC介绍-与jdbc性能对比

- 服务器资源使用到极限，相同场景JDBC与Sharding-JDBC的吞吐量相当。
- 服务器资源使用到极限，Sharding-JDBC采用分库分表后，Sharding-JDBC吞吐量较JDBC不分表有接近2倍的提升。

# Sharding-JDBC入门程序

## P11-Sharding-JDBC入门程序（水平分表）-环境搭建

```sql
# order表1
CREATE TABLE `t_order_1` (
	`order_id` bigint(20) not null comment '订单id',
	`price` decimal(10,2) not null comment  '订单价格',
	`user_id` bigint(20) not null comment '下单用户id',
	`status` varchar(50) not null comment '订单状态',
	primary key (`order_id`) using btree
) engine=InnoDB character set = utf8 collate=utf8_general_ci row_format=Dynamic;

# order表2
CREATE TABLE `t_order_2` (
	`order_id` bigint(20) not null comment '订单id',
	`price` decimal(10,2) not null comment  '订单价格',
	`user_id` bigint(20) not null comment '下单用户id',
	`status` varchar(50) not null comment '订单状态',
	primary key (`order_id`) using btree
) engine=InnoDB character set = utf8 collate=utf8_general_ci row_format=Dynamic;
```

pom.xml

```xml
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
    <version>4.0.0-RC1</version>
</dependency>

<!-- https://mvnrepository.com/artifact/com.alibaba/druid-spring-boot-starter -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.2.9</version>
</dependency>
```



## P12-Sharding-JDBC入门程序（水平分表）-分片配置
## P13-Sharding-JDBC入门程序（水平分表）-插入订单
## P14-Sharding-JDBC入门程序（水平分表）-查询订单
## P15-Sharding-JDBC入门程序（水平分表）-执行流程分析
## P16-Sharding-JDBC入门程序（水平分表）-集成SpringBoot方式

# Sharding-JDBC执行原理

## P17-Sharding-JDBC执行原理-基本概念
## P18-Sharding-JDBC执行原理-执行过程介绍
## P19-Sharding-JDBC执行原理-SQL解析和SQL路由
## P20-Sharding-JDBC执行原理-SQL改写和SQL执行
## P21-Sharding-JDBC执行原理-结果归并
## P22-Sharding-JDBC水平分库-分片策略配置
## P23-Sharding-JDBC水平分库-插入订单
## P24-Sharding-JDBC水平分库-查询订单
## P25-Sharding-JDBC垂直分库-分片策略配置
## P26-Sharding-JDBC垂直分库-插入和查询测试
## P27-Sharding-JDBC公共表
## P28-Sharding-JDBC读写分离-理解读写分离
## P29-Sharding-JDBC读写分离-MySQL主从同步配置
## P30-Sharding-JDBC读写分离-插入和查询用户

# 案例

## P31-综合案例-数据库设计
## P32-综合案例-主从数据库搭建
## P33-综合案例-分片策略配置
## P34-综合案例-添加商品-dao
## P35-综合案例-添加商品-service
## P36-综合案例-添加商品-测试
## P37-综合案例-查询商品-dao
## P38-综合案例-查询商品-service及测试
## P39-综合案例-统计商品

# 总结

## P40-课程总结
