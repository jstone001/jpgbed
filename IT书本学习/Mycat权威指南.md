![image-20220417093654107](E:\JS\booknote\jpgBed\image-20220417093654107.png)

# 入门篇

## 第1章 概述

### **1.1 数据库切分概述**

#### **1.1.1 OLTP 和 OLAP**

- 联机事务处理（OLTP）也称为面向交易的处理系统，其基本特征是原始数据可以立即传送到计算中心进行处理，并在很短的时间内给出处理结果。
- 联机分析处理（OLAP）是指通过多维的方式对数据进行分析、查询和报表，可以同数据挖掘工具、统计分析工具配合使用，增强决策分析功能。

|          | OLTP                               | OLAP                             |
| -------- | ---------------------------------- | -------------------------------- |
| 系统功能 | 日常交易处理                       | 统计、分析、报表                 |
| DB 设计  | 面向实时交易类应用                 | 面向统计分析类应用               |
| 数据处理 | 当前的, 最新的细节的, 二维的分立的 | 历史的, 聚集的, 多维的集成的, 统 |
| 实时性   | 实时读写要求高                     | 实时读写要求低                   |
| 事务     | 强一致性                           | 弱事务                           |
| 分析要求 | 低、简单                           | 低、简单                         |

#### **1.1.3 何为数据切分？**

## **第 7 章 Mycat 的配置**

### **7.2 schema 标签**

#### **7.2.3 sqlMaxLimit**

​        当该值设置为某个数值时。每条执行的 SQL 语句，如果没有加上 limit 语句，MyCat 也会自动的加上所对应的值。例如设置值为 100，执行**select * from TESTDB.travelrecord;**的效果为和执行**select * from TESTDB.travelrecord limit 100;**相同。

​        需要注意的是，如果运行的 schema 为非拆分库的，那么该属性不会生效。需要手动添加 limit 语句。

### **7.3 table 标签**

## **第 8 章 Mycat 的分片 join**

全局表，ER 分片，catletT(人工智能)和 ShareJoin

### **8.3 ER Join**

### **8.4 Share join**

```sql
/*!mycat:catlet=demo.catlets.ShareJoin */
```

### **8.5 catlet（人工智能）**

```java
EngineCtx ctx=new EngineCtx();//包含 MyCat.SQLEngine
String sql=,“select a.id ,a.name from a 

ctx.executeNativeSQLSequnceJob(allAnodes,new DirectDBJoinHandler());
```



### **8.6 Spark/Storm 对 join 扩展**

# 高级进阶篇

# 生产实践篇

# 开发篇