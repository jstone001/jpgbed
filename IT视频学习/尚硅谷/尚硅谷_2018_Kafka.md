from:https://www.bilibili.com/video/BV1a4411B7V9

# Kafka概述

## P01_Kafka_课程介绍

## P02_Kafka_消息队列介绍

- 发布/订阅模式（一对多，数据生产后，推送给所有订阅者）
- 点对点模式（一对一，消费者主动<font color='red'>拉取</font>数据，消息收到后消息清除）

为什么需要message queue

1. 解耦
2. 冗余：队列可以备份
3. 扩展：可以集群
4. 削峰
5. 可恢复性
6. 顺序保证
7. 缓冲
8. 异步通信

## P03_Kafka_概念

- 在流式计算中，Kafka一般用来缓存数据，Storm通过消费Kafka的数据进行计算。
- Apache Kafka是一个开源消息系统，由Scala写成。是由apache软件基金会开发的一个开源消息系统项目。
- Kafka最初是由LinkedIn公司开发，并于2011年初开源。2012年10月从Apache Incubator毕业。该项目的目标是为处理实时数据提供一个统一、高通量、低等待的平台。
- Kafka是一个分布式消息队列。Kafka对消息保存时根据<font color='red'>Topic</font>进行归类，发送消息者称为Producer，消息接家驹者称为Consumer，此外kafka集群有多个kafka实例组成，每个实例（server）称为<font color='red'>broker</font>。
- 无论是kafka集群，还是consumer都依赖于<font color='red'>zookeeper</font>集群保存一些meta信息，来保证系统可用性。

## P04_Kafka_架构

![image-20210520103159590](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210520103159590.png)

![image-20210520113731056](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210520113731056.png)

- Leader起作用，Follower仅作备份用
- Producer不与zookeeper打交道

# Kafka集群部署 

## P05_Kafka_集群搭建&启动

Kafka server.properties的配置

```sh
# 需要修改的
broker.id=0		# broker的全局唯一编号
delete.topic.enable=true		# 删除topic功能为true
log.dirs=/opt/module/kafka/logs		#kafka运行日志存放的路径
zookeeper.connect=hadoop101:2181,hadoop102:2181	# 配置连接zookeep集群地址
```

```sh
# 不需要修改的
num.network.threads=3		# 处理网络请求的线程数量
num.io.threads=8	# 处理磁盘io的现成数量
socket.send.buffer.bytes=102400	# 发送套接字的缓冲区大小
socket.receive.buffer.bytes=102400	# 接收套接字的缓冲区大小
socket.request.max.bytes=104857600	# 请求套接字的缓冲区大小
num.partitions=1	# topic在当前broker的分区个数
num.recovery.threads.per.data.dir=1	#用来恢复和清理data下数据的线程数量 
log.retention=168	# segement文件保留的最长j
```



## P06_Kafka_命令行操作

# Kafka工作流程分析

## P07_Kafka_工作流程分析
## P08_Kafka_生产数据流程
## P09_Kafka_保存数据
## P10_Kafka_消费数据
## P11_Kafka_回顾

# Kafka API实战

## P12_Kafka_生产者API使用
## P13_Kafka_带回调函数的生产者
## P14_Kafka_自定义分区的生产者
## P15_Kafka_高级消费者
## P16_Kafka_低级消费者API思路梳理
## P17_Kafka_低级API参数设置
## P18_Kafka_低级API之获取分区leader
## P19_Kafka_低级API之获取分区数据
## P20_Kafka_低级API之测试
## P21_Kafka_扩展

# Kafka producer拦截器

## P22_Kafka_拦截器

# Kafka Streams

## P23_Kafka_KafkaStream
## P24_Kafka_与Flume对比及集成