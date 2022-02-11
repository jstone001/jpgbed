from: https://www.bilibili.com/video/BV1PW411r7iP

# 第1章：zookeeper入门

## P01_课程介绍

## P02_概述

开源，Apache项目

zookeeper人设计模式角度来理解：是一个基于观察者模式设计的分布式服务管理框架，它负责存储和管理大家都产心的数据，然后<font color='red'>接受观察者的注册</font>，一旦这些数据的状态发生变化，zookeep就将负责能和已经在zookper上注册的那些观察者做出相应的反应。

<font color='red'>Zookeeper=文件系统+通知机制</font>

## P03_特点

1. zookeeper：一个Leader，多个Follower组成的集群。
2. <font color='red'>集群中只要有半数以上节点存活，zookeeper集群就能正常服务。</font>
3. 全局数据一致：每个server保存一份相同的数据副本，Client无论连接到哪个server，数据都是一致的。
4. 更新请求顺序进行，来自同一个client的更新模板请求按其发送顺序依次执行。
5. <font color='red'>数据更新原子性：一次数据更新要么成功，要么失败。</font>
6. 实时性，在一定时间范围内，client能读到最新数据。

## P04_数据结构

zookeeper数据模型的结构与unix文件系统很类似，整体上可以看作是一棵树，每个节点称做一个ZNode。每一个ZNode默认能够存储<font color='red'>1mb</font>的数据，每个ZNode都可以通过其路径唯一标识。

## P05_应用场景

### 统一命名服务

<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210521103806497.png" alt="image-20210521103806497" style="zoom:80%;" />

### 统一配置管理

- 配置文件同步

  1. 一般要求一个集群中，所有节点的配置信息是一致的，比如Kafka集群。
  2. 对配置文件修改后，希望能够快速同步到各个节点上。

- 配置管理由zookeeper实现

  1. 可将配置信息写入zookeeper上的一个Znode。
  2. 各个客户端服务器监听这个znode。
  3. 一旦znode中的数据被 修改，zookeeper将能通知各个客户端服务器。

  ![image-20210521104222958](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210521104222958.png)

### 统一集群管理 

- 可根据节点实时状态做出一些调整。

- 可将节点从息写入zookeeper上的一个znode。

- 监听这个znode可获取它的实时状态变化。

  ![image-20210521104611764](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210521104611764.png)

### 服务器节点动态上下线

### 软负载均衡

<font color='red'>在zookeeper中记录每台服务器的访问数，让访问数最少的服务器去处理最新的客户端请求。</font>

![image-20210521105038496](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210521105038496.png)

# 第2章：zookeep安装

## P06_下载地址

https://zookeeper.apache.org

## P07_本地模式安装

1. 下载安装zookeeper
2. 配置文件修改

```sh
# 1. 将zookeeper-3.4.10/conf这个路径下的zoo_sample.cfg修改为zoo.cfg

# 2. 打开zoo.cfg文件，修改dataDir路径
dataDir=/opt/module/zookeeper-3.4.10/zkData

# 3. 创建zkDaa文件夹
mkdir /opt/module/zookeeper-3.4.10/zkData
```

 	3. 操作zookeeper

```sh
# 启动zookeeper
# bin 下面
./zkServer.sh start

# 查看进程是否启动
jps
90805 QuorumPeerMain
90825 Jps

# 查看状态
./zkServer.sh status
Using config: /Software/zookeeper-3.4.13/bin/../conf/zoo.cfg
Mode: standalone

# 启动客户端
./zkCli.sh
[zk: localhost:2181(CONNECTED) 0] ls /
[zookeeper]

# 退出客户端
quit
 # 停止zookeeper
 ./zkServer.sh stop
```



## P08_配置参数解读

```sh
# The number of milliseconds of each tick
tickTime=2000	# 心跳
# The number of ticks that the initial 
# synchronization phase can take
initLimit=10	# 10个2秒（初始连接时leader和follower通信的时间）
# The number of ticks that can pass between 
# sending a request and getting an acknowledgement
syncLimit=5	# 5*2=10秒  （启动后leader和follower的通信时间，加入超过10秒，leader认为follower死了，从服务器列表中删除follower。
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just 
# example sakes.
dataDir=/Software/zookeeper-3.4.13/zkData
# the port at which the clients will connect
clientPort=2181

```

# 第３章：zookeeper内部原理

## P09_选举机制（面试重点）

1. <font color='red'>半数机制</font>：集群中半数以上机器存活，集群可用。所以zookeeper适合安装奇数台服务器。
2. zookeeper虽然在配置文件中并没有指定master和slave。但是，zookeeper工作时，是有一个节点为leader，其他则为follower，leader是通过内部的选举机制临时产生的。

流程：

1. 服务器1启动，此时只有它一台服务器启动了，它发出去的报文没有任何响应，所以它的选举状态一直是looking状态。
2. 服务器2启动，它与最开始启动的server1进行通信，互相交换自己的选举结果，由于两者都没有历史数据，所以id值较大的服务器2胜出，但是由于没有达到超过半数以上。所以还是looking状态。
3. server3启动，先投票给自己，然后serer1, server2 再投票给server3，超过半数，选定leader。一旦leader选定就不会改变。

## P10_节点类型

### 持久（Persistent）：客户端和服务器断开连接后，创建的节点不删除。

- 普通（Persistent）

- 持久化顺序编号目录节点（Persistent_sequential）：客户端与zookeeper断开连接后，该节点依旧在，只是zookeeper给该节点名称进行顺序编号 

   <font color='red'>注意：在分布式系统中，顺序号可以被 用于为所有的事件进行全局排序，这样客户端可以通过顺序号推断事件的顺序</font>

### 短暂（Ephemeral）：客户端和服务器端断开后，创建的节点自己删除。

- 一般
- 临时顺序编号目录节点：客户端与zookeeper断开连接后，该节点被删除，只是zookeeper给该节点名称进行顺序编号 

## P11_分布式安装

```sh
# 复制zookeeper到其他服务器
xsync zookeeper-3.4.10/

# 配置服务器编号 
echo 1 >/Software/zookeeper-3.4.13/zkData/myid

# 配置zoo.cfg
###########cluster##############
server.2=test1:2888:3888
server.3=test2:2888:3888
server.4=test3:2888:3888
# server.A=B:C:D
# A 是myid的值
# B 是这穿上服务器的ip
# C 是这个服务器与集群中的Leader服务器交换信息的端口
# D 选举信息

# 启动各个zookeeper
../bin/zkServer.sh start

# 查看状态(先关防火墙)
[root@localhost zookeeper-3.4.13]# ./bin/zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /Software/zookeeper-3.4.13/bin/../conf/zoo.cfg
Mode: follower	# 重要

[root@localhost bin]# ./zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /Software/zookeeper-3.4.13/bin/../conf/zoo.cfg
Mode: leader

```

## P12_客户端命令操作

```sh
help

# 查看当前znode中所包含的内容
ls /

# 当前节点详细数据 
ls2 /

# 创建2个普通节点
create /sanguo "jinlian"
create /sanguo/shuguo "liubei"
ls /sanguo		# 查看下一级节点

# 获取节点值
get /sanguo/shuguo

# 创建短暂节点
create -e /sanguo/wehuo "zhouyu" 
ls /sanguo	# 退出之后，没有这个节点

# 创建带有序号的节点
create -s /sanguo/weiguo "caocao"
ls /sanguo

# 修改节点的值
set /sanguo/shuguo "diaochan"

# 节点的值变化监听
get /sanguo watch	# 只能监听一次
WatchedEvent state:SyncConnected type:NodeDataChanged path:/sanguo

# 监听子节点的变化
ls /sanguo  watch

# 删除节点
delete /sanguo/weiguo
# 递归删除
rmr /sanguo

# 查看节点状态
stat /sanguo
```

## P13_Stat结构体

1. czxid：创建节点的事务zxid
   - 每次修改zooKeeper状态都会收到一个zxid形式的时间戳，也就是zookeeper事务ID。
   - 事务ID是zookeeper中所有修改总的次序。每个修改都有唯一的zxid，如果zxid1小事务ID是zookeeper中所有修改总的次序。每个修改都有一个唯一的zxid，如果zxid1小于zxid2，那么zxid1在zxid2之前发生。
2. ctime：znode被创建的毫秒数（1970年开始）
3. mzxid：znode最后更新的事务zxid
4. mtime：znode最后修改的毫秒数（1970年开始）
5. pZxid：znode最后更新的子节点zxid。
6. cversion：znode子节点变化号，znode子节点修改次数。
7. dataversion：znode数据变化号。
8. aclVersion：znode访问控制列表的变化号。
9. ephemeralOwner：  如果是临时节点，这个是znode拥有者的session id。如果不是临时节点则是0。
10. <font color='red'>dataLength：znode 的数据长度。</font>
11. <font color='red'>numChildren：znode子节点数量。</font>

## P14_监听器原理（面试重点）

原理：

1. 首先要有一个main()线程
2. 在main线程中创建zookeeper客户端，这时就会创建两个线程，一个负责网络连接（connect），一个负责监听（listener）。
3. 通过connect线程将注册的监听事件发送给zookeeper。
4. 在zookeeper的注册监听器列表中将注册的监听事件添加到列表中。
5. zookeeper监听到有数据或路径变化，就会将这个消息发送给listener线程。

常见的监听：

1. 监听节点数据的变化 

   get path [watch]

2. 监听子节点增减的变化

   ls path [watch]

## P15_写数据流程

1. client向zookeeper的server1上写数据，发送一个写请求.
2. 如果server1不是leader，那么server1会把接受到的请求进一步转发给leader，因为每个zookeeper的server里面有一个是leader。这个leader会将写请求广播给各个server，比如server1和server2，各个server写成功后就会通知leader。
3. 当leader收到大多数server数据写成功了，那么就说明数据写成功了。如果这里3个节点的话，只要有2个节眯数据写成功了，那么就认为数据写成功了。写成功之后，leader会告诉server1数据写成功了。

# 第４章：zookeeper实战

## P16_创建ZooKeeper客户端
## P17_创建一个节点
## P18_获取子节点并监听节点变化
## P19_判断节点是否存在
## P20_服务器节点动态上下线案例分析
## P21_服务器节点动态上下线案例注册代码
## P22_服务器节点动态上下线案例全部代码实现

# 第５章：面试题

## P23_企业面试真题

### 请简述zookeeper的选举机制

### zookeeper的监听原理

### zookeeper的部署方式有哪几种

### zookeeeper的常用命令