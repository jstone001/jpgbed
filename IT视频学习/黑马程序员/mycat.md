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





# 分片

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
