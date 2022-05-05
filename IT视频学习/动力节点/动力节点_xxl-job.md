## XXL-JOB分布式调优超细致讲解

https://www.bilibili.com/video/BV1gJ41117zo



## P01-什么是集中式任务调度
## P02-要实现定时任务有哪些方案
### Java实现定时任务方式

1. while(true)+Thread.sleep

   > 轮询+线程睡眠的方式实现定时任务。

2. javautil.Timer + java.util.TimerTask

   > - Timer 是一种定时器工具
   >
   > - TimerTask是一个抽象类

3. ScheduledExecutorService

   > ScheduledExecutorService是从jdk1.5开始为并发被引入，是最理想的定时任务实现方式。

4. Quatz

> Quatz 是一个开源的定时任务高度框架，由java编写，用于生态下的定时任务高度， 是一个灵活方便，使用简单的定时任务框架，可以和Spring整合

5. Spring Task

6. SpringBoot 注解 @EnableScheduling +@Scheduled

   > 底层依然是Spring Task

​			
## P03-分布式任务调度要解决哪些问题
### **单机的问题**

1. 多台机器集群部署定时任务如何保证不不被重复执行？
2. 如何动态地调整定时任务的执行时间？
3. 部署定时任务的机器 发生故障如何实现故障转移？
4. 如何对定时任务进行监控？
5. 业务量大，单机性能的瓶颈问题，如何扩展？
6. 等等...

## P04-分布式任务调度有哪些好的解决方案

**解决方法**

1. 数据库唯一约束；
2. 使用分布式锁实现调试的开关；
3. 使用分布式锁实现调度控制；
4. 使用分布式任务调试平台TBSchedule, Elastric-Job, Saturn, xxl-job, google Cron 系统；
5. 自研

## P05-各大互联网公司的分布式任务调度平台

### TBSchedule

​	淘宝推出的，现在已经不维护了

​	GitHub：https://github.com/taobao/TBSchedule

### Elastic-Job

​	当当网，官网：https://elasticjob.io

### Saturn

​	唯品会推出的开源分布式，它是基于Elastic-Job而开发的。新增了一些特性，在唯品会内部及一些互联网公司使用。

​	https://github.com/vipshop/Saturn

### xxl-job

​	美团

​	https://github.com/xuxueli/xxl-job

## P06-XXL-JOB分布式调度平台整体架构

[国产开源软件投票排行榜]( https://www.oschina.net/project/top_cn_2018)

[官网](www.xuxueli.com/xxl-job/#/)

### xxl-job整体架构

![image-20220428123626981](E:\JS\booknote\jpgBed\image-20220428123626981.png) 

## P07-XXL-JOB分布式调度中心

### 调度中心

http://localhost:8080/xxl-job-admin

1. 从github中获取源码
2. sql语句初始化数据库；
3. maven打包xxl-job-admin 并部署为调度中心。
4. 启动http://localhost:8080/xxl-job-admin

### 执行器（任务）

1. 创建执行器，可以支持集群
2. 配置文件需要填写xxl-job注册中心地址
3. 每个具体执行job服务器需要创建一个netty 

## P08-XXL-JOB分布式调度中心后台管理

## P09-XXL-JOB分布式任务执行器

```properties
xxl.job.executor.logretentiondays=-1 # 值大于 3 时生效，启用执行器 Log 文件定期清理功能，否则不生效； 
```



## P10-调度中心如何配置执行器任务
## P11-调度中心对执行器的调度
## P12-执行器任务集群高可用部署的调度
## P13-调度中心的高可用部署
## P14-调度中心执行器集群高可用部署调度
## P15-XXL-JOB调度平台的设计思路



## 