https://www.bilibili.com/video/BV17f4y1D7pm

# Mycat 简介

## P01. MyCat - 课程介绍
## P02. MyCat - 简介 - MyCat引入
## P03. MyCat - 简介 - MyCat历史

基于cobar

2大优势

1. 重构了cobar代码，将原来的BIO改成NIO，并发量有大幅提高。
2. 增加了对order by, group by , limit 等聚合功能的支持。同时兼容大多数数据库成为通用的数据库中间件。

## P04. MyCat - 简介 - MyCat优势

- 性能稳定
- 强大的技术团队
- 体系完善（形成许多周边的产品）
  - Mycat-Web
  - Mycat-NIO
  - Mycat-Balance
- 社区活跃

## P05. MyCat - 简介 - MyCat使用场合及下载

### 使用场合

- 高可用性MySql读写分离

- 业务数据分级存储保障

  数据重要性不同。可以采用不同的存储设备，通过分级存储管理软件实现数据客体在存储设备之间自动迁移及自动访问切换。

- 大表水平拆分，集群并行计算

- 数据库路由器

  mycat基于mysql实例的连接池利用机制，可以让每个应用最大程度共享一个mysql实例的所有连接池，让数据库的并发方问能力大大提升。

- 整合不同数据源

  可以使用mysql, oracle 等不同的数据库

  ### 下载

  https://github.com/MyCATApache/Mycat-download

  http://dl.mycat.io/

# Mycat 入门

## P06. MyCat - 入门 - 环境搭建 - MySQL安装配置

```sh
rpm -qa | grep -i mysql  # 搜索已安装的mysql (系统中老的mysql)
rpm -e mysql-libs-5.1.71-1.e16.x86_64 --nodeps # 卸载老的mysql  nodeps去除所有的依赖
yum -y install libaio.so.1 libgcc_s.so.1 libstdc++.so.6 libncurses.so.5  --setopt=protected_multilib=false   # 安装依赖包
yum update libstdc++-4.4.7-4.el6.x86_64

# 安装
rpm -ivh mysql-community-common-5.7.37-1.el7.x86_64.rpm
rpm -ivh mysql-community-libs-5.7.37-1.el7.x86_64.rpm
rpm -ivh mysql-community-client-5.7.37-1.el7.x86_64.rpm
rpm -ivh mysql-community-server-5.7.37-1.el7.x86_64.rpm

# 修改密码
set password= password('123')

# 授权远程访问
grant all privileges  on *.* to root@'%' identified by "123456";
flush privileges;
```



## P07. MyCat - 入门 - 环境搭建 - JDK安装配置
## P08. MyCat - 入门 - 环境搭建 - MyCat安装

# MyCat 核心概念

## P09. MyCat - 入门 - 核心概念 - 分片

- 分片
- 逻辑库 schema
- 逻辑表
- 分片节点
- 节点主机
- 分片规则 rule

## P10. MyCat - 入门 - 核心概念 - 相关概念

- 分片表
- 非分片表
- <font color='red'>ER（entity relationship）表：</font>子表数据和父表数据放在同一个分片中，通过表分组（table group），保证关联查询时不会跨库查询 
- 全局表

# MyCat 分片测试 

## P11. MyCat - 入门 - 分片测试 - 需求
## P12. MyCat - 入门 - 分片测试 - schema.xml配置
## P13. MyCat - 入门 - 分片测试 - server.xml配置
## P14. MyCat - 入门 - 分片测试 - 访问MyCat
## P15. MyCat - 入门 - 分片测试 - 分片配置测试
## P16. MyCat - 入门 - 原理介绍

# MyCat - 配置文件详解

## P17. MyCat - 配置文件详解 - server.xml之system配置详解
## P18. MyCat - 配置文件详解 - server.xml之user配置详解
## P19. MyCat - 配置文件详解 - server.xml之firewall配置详解
## P20. MyCat - 配置文件详解 - schema.xml之schema配置详解
## P21. MyCat - 配置文件详解 - schema.xml之dataNode与dataHost配置详解
## P22. MyCat - 配置文件详解 - rule.xml配置详解
## P23. MyCat - 配置文件详解 - sequence序列配置详解

# MyCat - 分片 

## P24. MyCat - 分片 - 垂直拆分 - 概述及案例场景
## P25. MyCat - 分片 - 垂直拆分 - 分片配置
## P26. MyCat - 分片 - 垂直拆分 - 测试
## P27. MyCat - 分片 - 垂直拆分 - 全局表配置
## P28. MyCat - 分片 - 水平拆分 - 概述及案例场景
## P29. MyCat - 分片 - 水平拆分 - 分片配置及测试
## P30. MyCat - 分片 - 分片规则 - 准备工作
## P31. MyCat - 分片 - 分片规则 - 取模分片
## P32. MyCat - 分片 - 分片规则 - 范围分片
## P33. MyCat - 分片 - 分片规则 - 枚举分片
## P34. MyCat - 分片 - 分片规则 - 范围求模算法
## P35. MyCat - 分片 - 分片规则 - 固定分片hash算法
## P36. MyCat - 分片 - 分片规则 - 取模范围算法
## P37. MyCat - 分片 - 分片规则 - 字符串Hash求模范围算法
## P38. MyCat - 分片 - 分片规则 - 应用指定算法
## P39. MyCat - 分片 - 分片规则 - 字符串hash解析算法
## P40. MyCat - 分片 - 分片规则 - 一致性hash算法
## P41. MyCat - 分片 - 分片规则 - 日期时间相关分片算法

# MyCat - 高级

## P42. MyCat - 高级 - MyCat-Web介绍及安装配置
## P43. MyCat - 高级 - MyCat-Web性能监控
## P44. MyCat - 高级 - 读写分离 - MySQL主从复制原理
## P45. MyCat - 高级 - 读写分离 - 主从复制(一主一从)搭建
## P46. MyCat - 高级 - 读写分离 - 一主一从读写分离配置
## P47. MyCat - 高级 - 读写分离 - 双主双从架构
## P48. MyCat - 高级 - 读写分离 - 双主双从搭建
## P49. MyCat - 高级 - 读写分离 - MyCat实现MySQL双主双从读写分离

# MyCat - 集群

## P50. MyCat - 集群 - 集群架构
## P51. MyCat - 集群 - 服务器环境规划
## P52. MyCat - 集群 - MySQL主从复制配置
## P53. MyCat - 集群 - 两台MyCat的安装配置
## P54. MyCat - 集群 - HAProxy安装配置
## P55. MyCat - 集群 - HAProxy启动访问
## P56. MyCat - 集群 - keepalived作用及流程介绍
## P57. MyCat - 集群 - keepalived安装配置
## P58. MyCat - 集群 - keepalived启动及haproxy的高可用测试

# MyCat - 架构剖析

## P59. MyCat - 架构剖析 - 总体架构介绍
## P60. MyCat - 架构剖析 - 网络IO架构
## P61. MyCat - 架构剖析 - MySQL协议简介
## P62. MyCat - 架构剖析 - MyCat实现MySQL协议
## P63. MyCat - 架构剖析 - MyCat线程池架构
## P64. MyCat - 架构剖析 - 内存管理及缓存框架
## P65. MyCat - 架构剖析 - MyCat连接池架构实现
## P66. MyCat - 架构剖析 - MyCat主从切换实现
## P67. MyCat - 架构剖析 - 核心技术之分布式事务的支持
## P68. MyCat - 架构剖析 - 核心技术之SQL路由实现
## P69. MyCat - 架构剖析 - 核心技术之跨库join实现
## P70. MyCat - 架构剖析 - 核心技术之数据汇聚与排序实现

# MyCat -案例

## P71. MyCat - 案例概述
## P72. MyCat - 需求说明
## P73. MyCat - 环境搭建 - 数据库准备
## P74. MyCat - 环境搭建 - 工程层级及架构介绍
## P75. MyCat - 环境搭建 - 基础工程搭建
## P76. MyCat - 环境搭建 - Eureka注册中心搭建
## P77. MyCat - 环境搭建 - 微服务网关gateway搭建
## P78. MyCat - 商品管理 - 需求分析
## P79. MyCat - 商品管理 - 微服务搭建
## P80. MyCat - 商品管理 - 根据ID查询SPU
## P81. MyCat - 商品管理 - 根据条件分页查询SPU列表
## P82. MyCat - 商品管理 - 根据ID查询SKU信息
## P83. MyCat - 订单模块 - 需求分析及微服务环境搭建
## P84. MyCat - 订单模块 - 提交订单业务分析
## P85. MyCat - 订单模块 - 根据条件分页订单数据
## P86. MyCat - 网关联合测试
## P87. MyCat - 日志模块 - 微服务搭建
## P88. MyCat - 日志模块 - 日志表的ID生成分析
## P89. MyCat - 日志模块 - 插入日志
## P90. MyCat - 日志模块 - 微服务通过AOP记录日志
## P91. MyCat - 日志模块 - 条件查询日志
## P92. MyCat - 日志模块 - 测试

# 分片

## P93. MyCat - 分片 - 思路分析
## P94. MyCat - 分片 - 服务器配置说明
## P95. MyCat - 分片 - 数据库分片配置
## P96. MyCat - 分片 - MyCat环境搭建

# 

全局表

ER表

catlet

## P96 Mycat 环境搭建

```sql
create database v_order default charset utf8mb4;
source tmp.sql  # 导入sql 语句
```

```sh
tail -f logs/wrapper.log  # 看mysql日志

```

##  P97 MyCat - 分片 - 微服务连接MyCat

## P98 MyCat - 分片 - MyCat监控
