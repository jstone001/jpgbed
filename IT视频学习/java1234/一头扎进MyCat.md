官网：

- http://www.mycat.io/
- http://mycat.org.cn/
- https://github.com/MyCATApache/Mycat-Server

# MyCat介绍，安装和配置


## 1、Mycat简介

http://blog.java1234.com/blog/articles/629.html

- Mycat 是一个开源的分布式数据库系统，但是由于真正的数据库需要存储引擎，而 Mycat 并没有存 储引擎，所以并不是完全意义的分布式数据库系统
- MyCat是目前最流行的**基于Java语言编写**的数据库中间件，也可以理解为是数据库代理。在架构体系中是位于数据库和应用层之间的一个组件，并且对于应用层是透明的，即数据库 感受不到mycat的存在，认为是直接连接的mysql数据库（实际上是连接的mycat,mycat实现了mysql的原生协议）
- MyCat是**基于阿里开源的Cobar产品**而研发，Cobar的稳定性、可靠性、优秀的架构和性能以及众多成熟的使用案例使得MyCat变得非常的强大。

mycat的三大功能：分库分表、读写分离、主从切换

## 2、Mycat安装（基于docker）

http://blog.java1234.com/blog/articles/630.html

mycatdockerfile

```sh
FROM centos
MAINTAINER caofeng<caofeng2012@126.com>
 
LABEL name="Java1234 myCat Image" \
    build-date="20200119"
    

ADD server-jre-8u151-linux-x64.tar.gz /home/
ADD Mycat-server-1.6.7.4-release-20200105164103-linux.tar.gz /home/
 
ENV WORKPATH /home/mycat/
WORKDIR $WORKPATH
 
ENV JAVA_HOME /home/jdk1.8.0_151
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH $PATH:$JAVA_HOME/bin:$CATALINA_HOME/lib:$CATALINA_HOME/bin
 
 
EXPOSE 8066
CMD /home/mycat/bin/mycat console
```

```sh
# Mycat启动方式有两种：
bin目录  ./mycat console # 控制台方式启动 （我们用这种，可以看到执行记录信息）
bin目录 ./mycat start # 后台启动

docker build -f mycatdockerfile -t java1234/mycat:1.0 .		#构建命令
docker run -p 8066:8066 -it 镜像id		# 启动容器

```

```sh
#我们先把conf配置和logs日志目录copy到宿主机，主要是我们启动的时候挂载宿主机，方便配置和查看日志；
docker cp 8fa48751b5cd:/home/mycat/conf/ /home/docker/mycat/
docker cp 8fa48751b5cd:/home/mycat/logs/ /home/docker/mycat/

docker run -p 8066:8066 -it  -v /home/docker/mycat/conf/:/home/mycat/conf/ -v /home/docker/mycat/logs/:/home/mycat/logs/ 镜像id
```



## 3、Mycat配置介绍及连接

http://blog.java1234.com/blog/articles/631.html

Mycat conf目录下有三个重要配置文件，分别是：

```sh
- schema.xml	#定义逻辑库，表，分片节点等内容；
- rule.xml		#定义分片规则；
- server.xml	#定义用户以及系统相关变量
```

# MySql 主从复制实现

http://blog.java1234.com/blog/articles/632.html

## 4、**mysql主从复制原理**

主从同步过程中主服务器有一个工作线程I/O dump thread，从服务器有两个工作线程I/O thread和SQL thread。

主库把外界接收的SQL请求记录到自己的binlog日志中，从库的I/O thread去请求主库的binlog日志，并将binlog日志写到中继日志中，然后从库重做中继日志的SQL语句。主库通过I/O dump  thread给从库I/O thread传送binlog日志。

![1095387-20181214172242259-1042240136.png](https://gitee.com/jstone001/booknote/raw/master/jpgBed/1582469390894064495.png)

![image-20220216144012902](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20220216144012902.png)

<font color='red'>relay-log  中继日志</font>

## 5、Docker下运行两个Mysql5.7容器

http://blog.java1234.com/blog/articles/633.html

```sh
# 先启动一个mysql
docker run -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 镜像id
```

```sh
#宿主机下 home目录下 新建 mysql目录 用于存放配置文件和日志目录；
docker cp cdcbca2ad794:/etc/mysql/mysql.conf.d/ /home/mysql/
docker cp cdcbca2ad794:/var/log /home/mysql/
```

```sh
#我们启动两个mysql容器：
 docker run -p 3306:3306 --name master   -d  -v /home/mysql/mysql.conf.d/:/etc/mysql/mysql.conf.d/ -v /home/mysql/log/:/var/log  -e MYSQL_ROOT_PASSWORD=123456  镜像ID

 docker run -p 3307:3306 --name slave   -d  -v /home/mysql2/mysql.conf.d/:/etc/mysql/mysql.conf.d/ -v /home/mysql2/log/:/var/log -e MYSQL_ROOT_PASSWORD=123456  镜像ID
```



## 6、<font color='red'>**docker自定义网络模式，实现容器固定ip地址**</font>

http://blog.java1234.com/blog/articles/634.html

我们在使用Docker容器时候，每次启动容器，容器分配到的虚拟IP经常变动，比如我们现在使用Mycat +  Mysql，我们是需要配置Mysql服务IP地址的，这个IP是Docker分配的虚拟IP，假如老是变，那我们还得老是修改配置，那就很麻烦了；所以我们需要固定手工分配容器IP；

Docker默认使用的是bridge 桥接网络模式

```sh
# 查看docker网络模式
docker network ls
NETWORK ID          NAME                DRIVER              SCOPE
dc1fcd2e7148        bridge              bridge              local
923358e1c0b4        host                host                local
387a08b6228f        none                null                local
```

```sh
# 我们创建自定义网络模式；
docker network create --subnet=172.20.0.0/16 extnetwork
NETWORK ID          NAME                DRIVER              SCOPE
dc1fcd2e7148        bridge              bridge              local
03c14f0e7c33        extnetwork          bridge              local
923358e1c0b4        host                host                local
387a08b6228f        none                null                local
```

```sh
# 通过--net extnetwork --ip 172.20.0.2 指定   
docker run -p 3306:3306 --name master   -d  -v /home/mysql/mysql.conf.d/:/etc/mysql/mysql.conf.d/ -v /home/mysql/log/:/var/log --net extnetwork --ip 172.20.0.2  -e MYSQL_ROOT_PASSWORD=123456  镜像ID

docker run -p 3307:3306 --name slave   -d  -v /home/mysql2/mysql.conf.d/:/etc/mysql/mysql.conf.d/ -v /home/mysql2/log/:/var/log --net extnetwork --ip 172.20.0.3 -e MYSQL_ROOT_PASSWORD=123456  镜像ID 

# 我们启动两个Mysql容器，并且固定分配IP  172.20.0.2和172.20.0.3
# （注意：这里必须用172.20.0.2开始分配，因为172.20.0.1是网关；）
```

```sh
# 扩展：
docker network rm extnetwork #删除网络
docker rm -f $(docker ps -qa) #删除所有容器
```

## 7、**mysql主从复制配置**

http://blog.java1234.com/blog/articles/635.html

### 主服务器配置

mysqld.cnf

```sh
[mysqld]
# 主服务器ID 必须唯一
server-id=2

# 开启及设置二进制日志文件名称
log_bin=mysql-bin

# 要同步的数据库
binlog-do-db =db_java1234

# 不需要同步的数据库
binlog-ignore-db=mysql    
binlog_ignore_db=information_schema
binlog_ignore_db=performation_schema
binlog_ignore_db=sys

# 设置logbin格式
binlog_format= MIXED # binlog日志格式，mysql默认采用statement，建议使用mixed
binlog_format= "STATEMENT"
```

```sql
#主服务器创建从机访问用户以及授权：
CREATE USER 'slave1'@'172.20.0.3' IDENTIFIED BY '123456';
GRANT REPLICATION SLAVE ON *.* TO 'slave1'@'172.20.0.3';
FLUSH PRIVILEGES;
```

```sql
# 查看主服务器状态
SHOW MASTER STATUS	
```

![image-20220217105412976](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20220217105412976.png)


### 从服务器配置

```sh
[mysqld]
server-id=3
relay-log=mysql-relay
```

```sql
# 从机创建连接主服务器的IP，用户，密码以及日志文件和位置；
CHANGE MASTER TO MASTER_HOST='172.20.0.2', MASTER_USER='slave1', MASTER_PASSWORD='123456', MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS=154;

#启动主从复制
START SLAVE;

#查看从机状态；
SHOW SLAVE STATUS
```

<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/1582469659441010556.jpg" alt=20200223223128.jpg" style="zoom:80%;" />



###  **binlog_format详解**

mysql复制主要有三种方式：基于SQL语句的复制(statement-based replication, SBR)，基于行的复制(row-based replication, RBR)，混合模式复制(mixed-based replication, MBR)。对应的，binlog的格式也有三种：STATEMENT，ROW，MIXED。

- ① STATEMENT模式（SBR）

> 每一条会修改数据的sql语句会记录到binlog中。优点是并不需要记录每一条sql语句和每一行的数据变化，减少了binlog日志量，节约IO，提高性能。缺点是在某些情况下会导致master-slave中的数据不一致(如sleep()函数， last_insert_id()，以及user-defined functions(udf)等会出现问题)
>

- ② ROW模式（RBR）

> 不记录每条sql语句的上下文信息，仅需记录哪条数据被修改了，修改成什么样了。而且不会出现某些特定情况下的存储过程、或function、或trigger的调用和触发无法被正确复制的问题。缺点是会产生大量的日志，尤其是alter table的时候会让日志暴涨。
>

- ③ MIXED模式（MBR）

> 以上两种模式的混合使用，一般的复制使用STATEMENT模式保存binlog，对于STATEMENT模式无法复制的操作使用ROW模式保存binlog，MySQL会根据执行的SQL语句选择日志保存方式。
>

## 8、mysql主从复制测试



## 9、主从同步常用命令

http://blog.java1234.com/blog/articles/637.html

```sql
#不带任何参数，表示同时启动I/O 线程和SQL线程。
#I/O线程从主库读取bin log，并存储到relay log中继日志文件中。
#SQL线程读取中继日志，解析后，在从库重放。
START SLAVE;

STOP SLAVE;  #完成停止I/O 线程和SQL线程的操作。

SHOW MASTER STATUS;

SHOW SLAVE STATUS;

RESET MASTER;   #删除所有index file 中记录的所有binlog 文件，将日志索引文件清空，创建一个新的日志文件，这个命令通常仅仅用于第一次用于搭建主从关系的时的主库



# 先STOP SLAVE
# 再清空MASTER, SLAVE的内容
# 再RESET MASTER
# 再CHANGE MASTER TO MASTER_HOST='172.20.0.2', MASTER_USER='slave1', MASTER_PASSWORD='123456', MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS=154;   修改LOG_POS位置
# 再START SLAVE
```

# MyCat读写分离实现

## 10、mycat读写分离介绍

基于前面讲的Mysql主从复制，我们通过Mycat，能够实现读写分离，即master主服务器实现写操作(insert,update,delete等)，salve从服务器实现读操作(select等)，

主服务器一旦有写入操作，从服务器通过读取binlog，来实现数据同步；Mycat也时时发送心跳包来检测mysql服务器是否可用；

![QQ鎴浘20200228082945.jpg](https://gitee.com/jstone001/booknote/raw/master/jpgBed/1582898335926032087.jpg)

## 11、mycat读写分离核心配置详解

http://blog.java1234.com/blog/articles/639.html

Mycat读写分离核心配置文件 schema.xml文件

![QQ鎴浘20200228084538.jpg](https://gitee.com/jstone001/booknote/raw/master/jpgBed/1582898373160012021.jpg)

一个逻辑库，对应多个逻辑表，每个逻辑表如上图，可以水平分片（我们后面会细讲），分成一个或者多个数据分片节点，每个数据分片节点对应一个dataHost（数据库主机），dataHost里可以通过writeHost和readHost配置写主机和读主机；

**schema标签属性介绍：**

**（定义逻辑数据库）**

| 属性           | 解释                                                         |
| -------------- | ------------------------------------------------------------ |
| name           | 配置逻辑库的名字（即数据库实例名）；                         |
| checkSQLschema | SQLschema检查 boolean类型。当前端执行【select *from USERDB.tf_user;】时（表名前指定了mycat逻辑库名），两种取值true：mycat会把语句转换为【select * from tf_user;】false：会报错 |
| sqlMaxLimit    | 最大查询每页记录数设置 相当于sql的结果集中，加上【limit N】。如果sql本身已经指定limit，则以sql指定的为准。 |
| dataNode       | 配置数据库节点：用于配置该逻辑库默认的分片。没有通过table标签配置的表，就会走到默认的分片上。这里注意没有配置在table标签的表，用工具查看是无法显示的，但是可以正常使用。如果没有配置dataNode属性，则没有配置在table标签的表，是无法使用的。 |

**dataNode标签属性：**

**（定义数据分片节点）**

| 属性         | 解释                                                         |
| ------------ | ------------------------------------------------------------ |
| name         | 数据分片节点名称                                             |
| **dataHost** | 定义该数据分片节点属于哪个数据库主机                         |
| **database** | 定义该数据分片节点属于哪个具体数据库实例上的具体库（即对应mysql中实际的DB） |

**dataHost标签属性：**

**（定义后端的数据库主机）**

| 属性           | 解释                                                         |
| -------------- | ------------------------------------------------------------ |
| name           | 指定dataHost的名字                                           |
| maxCon         | 指定每个读写实例连接池的最大连接。也就是说，标签内嵌套的writeHost、 readHost 标签都会使用这个属性的值来实例化出连接池的最大连接数。 |
| minCon         | 指定每个读写实例连接池的最小连接，初始化连接池的大小。       |
| balance        | 负载均衡类型：<br />（1）balance="0", 不开启读写分离机制，所有读操作都发送到当前可用的writeHost 上。<br />（2）<font color='red'>balance="1"，全部的 readHost 与 stand by writeHost 参与 select  语句的负载均衡，简单的说，当双主双从模式(M1->S1，M2->S2，并且 M1 与 M2 互为主备)，正常情况下，M2,S1,S2 都参与 select 语句的负载均衡。</font><br />（3）balance="2"，所有读操作都随机的在 writeHost、 readhost 上分发。<br />（4）<font color='red'>balance="3"，所有读请求随机的分发到 wiriterHost 对应的 readhost 执行，writerHost 不负担读压力，注意 balance=3 只在 1.4 及其以后版本有，1.3 没有。</font> |
| writeType      | （1）writeType="0", 所有写操作发送到配置的第一个 writeHost，第一个挂了切到还生存的第二个writeHost，重新启动后已切换后的为准，切换记录在配置文件中:dnindex.properties.<br />（2）writeType="1"，所有写操作都随机的发送到配置的 writeHost，1.5 以后废弃不推荐。 |
| dbType         | 指定后端连接的数据库类型，目前支持二进制的 mysql 协议，还有其他使用 JDBC 连接的数据库。例如：mongodb、 oracle、 spark 等。 |
| dbDriver       | 指定连接后端数据库使用的 Driver，目前可选的值有 native 和 JDBC。使用native 的话，因为这个值执行的是二进制的 mysql 协议，所以可以使用 mysql 和 maridb。其他类型的数据库则需要使用 JDBC 驱动来支持。 |
| switchType     | -1 表示不自动切换<br /> 1 默认值，自动切换 <br />2 基于 MySQL 主从同步的状态决定是否切换 心跳语句为 show slave status <br />3 基于 MySQL galary cluster 的切换机制（适合集群）（1.4.1） |
| slaveThreshold | 配置真实MySQL与MyCat的心跳                                   |

参考配置：

schema.xml

```xml
<?xml version="1.0"?>
<!DOCTYPE mycat:schema SYSTEM "schema.dtd">
<mycat:schema xmlns:mycat="http://io.mycat/">
  <schema name="TESTDB" checkSQLschema="true" sqlMaxLimit="100" dataNode="dn1">
  </schema>
  <dataNode name="dn1" dataHost="localhost1" database="db_java1234" />
  <dataHost name="localhost1" maxCon="1000" minCon="10" balance="3"
         writeType="0" dbType="mysql" dbDriver="native" switchType="2"  slaveThreshold="100">
     <heartbeat>select user()</heartbeat>
     <!-- can have multi write hosts -->
     <writeHost host="masterHost1" url="172.20.0.2:3306" user="root" password="123456">
             <readHost host="slaveHost1" url="172.20.0.3:3306" user="root" password="123456"></readHost>
     </writeHost>
  </dataHost>
</mycat:schema>
```



## 12、Navicat替代sqlyog连接mycat

```sh
docker run -p 8066:8066 -it  -v /home/docker/mycat/conf/:/home/mycat/conf/ -v /home/docker/mycat/logs/:/home/mycat/logs/ --net extnetwork  --ip 172.20.0.4 镜像id
```

## 13、Mycat读写分离测试

# Mycat+mysql双主双从读写分离实现

## **14、Mycat双主双从高可用读写分离介绍**

http://blog.java1234.com/blog/articles/642.html

![20190807153826962.png](https://gitee.com/jstone001/booknote/raw/master/jpgBed/1583503146266003187.png)

| 编号 | 角色    | IP地址     |
| ---- | ------- | ---------- |
| 1    | Master1 | 172.20.0.2 |
| 2    | Slave1  | 172.20.0.3 |
| 3    | Master2 | 172.20.0.5 |
| 4    | Slave2  | 172.20.0.6 |

M1 S1 主从复制 M2 S2 主从复制； M1,M2 互为主从复制 实现数据同步；无论哪个挂掉，Mycat可以自动切换，依然系统可用；

## 15、**2对mysql主从复制搭建**

```sh
# 前面我们搭建过一对，
# 用的是：
 docker run -p 3306:3306 --name master   -d  -v /home/mysql/mysql.conf.d/:/etc/mysql/mysql.conf.d/ -v /home/mysql/log/:/var/log --net extnetwork --ip 172.20.0.2  -e MYSQL_ROOT_PASSWORD=123456  镜像ID 

 docker run -p 3307:3306 --name slave   -d  -v /home/mysql2/mysql.conf.d/:/etc/mysql/mysql.conf.d/ -v /home/mysql2/log/:/var/log --net extnetwork --ip 172.20.0.3 -e MYSQL_ROOT_PASSWORD=123456  镜像ID

# 再启动两个
 docker run -p 3308:3306 --name master2   -d  -v /home/mysql3/mysql.conf.d/:/etc/mysql/mysql.conf.d/ -v /home/mysql3/log/:/var/log --net extnetwork --ip 172.20.0.5  -e MYSQL_ROOT_PASSWORD=123456  镜像ID 
 

 docker run -p 3309:3306 --name slave2   -d  -v /home/mysql4/mysql.conf.d/:/etc/mysql/mysql.conf.d/ -v /home/mysql4/log/:/var/log --net extnetwork --ip 172.20.0.6 -e MYSQL_ROOT_PASSWORD=123456  镜像ID 
```

```sh
# m1,m2我们要加配置：涉及到数据插入id不冲突，以及数据同步；
# 在作为从数据库的时候，有写入操作也要更新二进制日志文件
log-slave-updates

#表示自增长字段每次递增的量，指自增字段的起始值，其默认值是1，取值范围是1 .. 65535
auto-increment-increment=2 

# 表示自增长字段从哪个数开始，指字段一次递增多少，他的取值范围是1 .. 65535
auto-increment-offset=1
```

m2 的 server-id=5

s2的 server-id=6

```sql
# 配置OK后，我们重新搭界主从复制；
# m1 删除user里的原先slave用户，重新权；
CREATE USER 'slave'@'172.20.0.%' IDENTIFIED BY '123456';
GRANT REPLICATION SLAVE ON *.* TO 'slave'@'172.20.0.%';
FLUSH PRIVILEGES;
RESET MASTER

```

```sql
# s1 配置主从复制；
CHANGE MASTER TO MASTER_HOST='172.20.0.2', MASTER_USER='slave', MASTER_PASSWORD='123456', MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS=154;

START SLAVE

SHOW SLAVE STATUS #测试；
```

```sql
# m2 创建slave用户，授权
CREATE USER 'slave'@'172.20.0.%' IDENTIFIED BY '123456';
GRANT REPLICATION SLAVE ON *.* TO 'slave'@'172.20.0.%';
FLUSH PRIVILEGES;
RESET MASTER
```

```sql
# s2 配置主从复制；
CHANGE MASTER TO MASTER_HOST='172.20.0.5', MASTER_USER='slave', MASTER_PASSWORD='123456', MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS=154;
START SLAVE
SHOW SLAVE STATUS #测试；
```

## 16、**m1和m2配置互为主从复制**

m2复制m1配置

```sql
# m1先reset
RESET MASTER

#m2里配置：
CHANGE MASTER TO MASTER_HOST='172.20.0.2', MASTER_USER='slave', MASTER_PASSWORD='123456', MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS=154;
START SLAVE
SHOW SLAVE STATUS #测试；
```
m1复制m2配置

```sql
# m2先rest
RESET MASTER

#m1里配置：
CHANGE MASTER TO MASTER_HOST='172.20.0.5', MASTER_USER='slave', MASTER_PASSWORD='123456', MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS=154;
START SLAVE
SHOW SLAVE STATUS 测试；
```



# MyCat分库分表实现

## 垂直分库

### 20、mycat垂直分库介绍

- 单个数据库负载是有限的，在应对高并发时候，达到极限时候，容易崩掉，以及当某个表数据量很大的时候，执行速度缓慢，影响应用用户体验；
- 这时候，mycat提供了对数据库表垂直的库拆分，不同的业务模块的表，可以放不同的数据库，通过mycat，能屏蔽拆分细节，也就是连接mycat操作，我们依然可以看做是一个库；
- 同时了mycat支持了把单个数据量巨大的表依据某个字段规则拆分成多个表分散存到多个数据库当中去，来减少单表压力；

### 21、mycat垂直分库最佳实践

垂直分库如何根据表来分库呢？

这里一种情况，比如订单表，商品表。 订单表的一个商品外检关联商品表主键；多对一的关系；

如果我们把 订单表 商品表拆分到两个数据库中，那这里就会有一个很头疼的问题，这两个表Join关联查询的话，如何来搞呢。

**这里传统的话，有四种解决方案：**

- mycat新版本，提供了跨库2表join，但是假如数据量大的话，会有性能问题；
- 全局表，后面会讲到，每个数据库都有搞个商品表，有点浪费资源；
- 不join查询，业务代码里通过API方式另外再查询，看具情况使用；
- 采用冗余数据方式，订单表里面把商品id，商品名称，价格等数据库也搞进自己的表里，这样就不用join查询，也是有点浪费资源；

mycat官方就说明了，支持单库内任意Join，所以我们设计的时候，把同一个业务模块的表放同一个库，<font color='red'>依据业务模块来垂直分库；</font>

**所以我们的最终分库原则以下三点：**

- 根据业务模块来划分库，不搞跨库join操作，避坑；
- 公共表，比如数据字典表，系统属性表等，采用全局表；
- 有些核心表，比如用户表，部门表，权限表等，业务模块偶尔用到的时候，可以通过API方式查询，无需划分到具体业务模块里去；

<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/1584958536984083176.png" alt="20200323181527.png" style="zoom:80%;" />

### 22、mycat垂直分库案例实现

```sql
# 我们抽象电商系统四个表；
# 用户表，商品表，订单表，数据字典表

# 用户表t_user:
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `userName` varchar(20) DEFAULT NULL COMMENT '用户名',
  `password` varchar(20) DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

# 商品表t_product
CREATE TABLE `t_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `price` decimal(10,0) DEFAULT NULL COMMENT '价格',
  `description` text COMMENT '描述',
  `stock` int(11) DEFAULT NULL COMMENT '库存量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8; 

#  订单表t_order：
CREATE TABLE `t_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `pId` int(11) DEFAULT NULL COMMENT '商品编号',
  `num` int(11) DEFAULT NULL COMMENT '购买数量',
  `uId` int(11) DEFAULT NULL COMMENT '用户编号',
  `orderDate` datetime DEFAULT NULL COMMENT '下单时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

# 数据字典表t_datadic:
CREATE TABLE `t_datadic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(20) DEFAULT NULL COMMENT '数据字典名称',
  `value` varchar(20) DEFAULT NULL COMMENT '数据字典值',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

 
insert  into `t_datadic`(`id`,`name`,`value`,`remark`) values (1,'性别','男',NULL),(2,'性别','女',NULL),(3,'性别','男变女',NULL),(4,'性别','女变男',NULL),(5,'政治面貌','共产党',NULL),(6,'政治面貌','共青团员',NULL),(7,'政治面貌','无党派人士',NULL);

```

数据字典表作为全局表我们后面再说；

 我们分两个库，t_user一个库 t_product和t_order搞一个库，因为有join关联；

- t_user 放 db_mall_user库；
- t_product和t_order放db_mall_order库；

| 编号 | 角色              | 分配IP     |
| ---- | ----------------- | ---------- |
| 1    | db_mall_user库；  | 172.20.0.7 |
| 2    | db_mall_order库； | 172.20.0.8 |
| 3    | mycat             | 172.20.0.9 |

```sql
#启动两个mysql

 docker run -p 3306:3306 --name db_mall_user   -d  -v /home/mysql7/mysql.conf.d/:/etc/mysql/mysql.conf.d/ -v /home/mysql7/log/:/var/log --net extnetwork --ip 172.20.0.7  -e MYSQL_ROOT_PASSWORD=123456  镜像ID

docker run -p 3307:3306 --name db_mall_order   -d -v  /home/mysql8/mysql.conf.d/:/etc/mysql/mysql.conf.d/ -v /home/mysql8/log/:/var/log --net extnetwork --ip 172.20.0.8 -e MYSQL_ROOT_PASSWORD=123456  镜像ID
```

配置schema.xml

```xml
<?xml version="1.0"?>
<!DOCTYPE mycat:schema SYSTEM "schema.dtd">
<mycat:schema xmlns:mycat="http://io.mycat/">
  <schema name="TESTDB" checkSQLschema="true" sqlMaxLimit="100">
     <table name="t_user" primaryKey="id" dataNode="dn1" />
     <table name="t_order" primaryKey="id" dataNode="dn2" />
     <table name="t_product" primaryKey="id" dataNode="dn2" />
  </schema>
  <dataNode name="dn1" dataHost="host1" database="db_mall_user" />
  <dataNode name="dn2" dataHost="host2" database="db_mall_order" />

  <dataHost name="host1" maxCon="1000" minCon="10" balance="3"
         writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
     <heartbeat>select user()</heartbeat>
     <!-- can have multi write hosts -->
     <writeHost host="hostM1" url="172.20.0.7:3306" user="root"
             password="123456">
     </writeHost>
  </dataHost>
    
  <dataHost name="host2" maxCon="1000" minCon="10" balance="3"
         writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
     <heartbeat>select user()</heartbeat>
     <!-- can have multi write hosts -->
     <writeHost host="hostM2" url="172.20.0.8:3306" user="root"
             password="123456">
     </writeHost>
  </dataHost>
</mycat:schema>
```

```sh
# 启动mycat

docker run -p 8066:8066 -it  -v /home/docker/mycat1/conf/:/home/mycat/conf/ -v /home/docker/mycat1/logs/:/home/mycat/logs/ --net extnetwork --ip 172.20.0.9 镜像ID
```

```sql
# 插入数据；
insert  into `T_USER`(`id`,`userName`,`password`) values (1,'java1234','123456'),(2,'jack','123'),(3,'marry','456');

insert  into `T_PRODUCT`(`id`,`name`,`price`,`description`,`stock`) values (1,'耐克鞋2020款001A','998','dfs',50),(2,'阿迪达斯休闲衣服2020Add','388','dfs',20);

insert  into `T_ORDER`(`id`,`pId`,`num`,`uId`,`orderDate`) values (1,1,1,2,'2020-03-16 22:10:25'),(2,2,3,3,'2020-03-16 22:10:34');

select * from T_ORDER o left join T_PRODUCT p on o.pId=p.id
```

### 23、mycat垂直分库优缺点

http://blog.java1234.com/blog/articles/651.html

优点

- 拆分简单明了，拆分规则明确
- 应用程序模块清晰明确，整合容易
- 数据维护方便易行，容易定位
- 减轻了单个库的负载

缺点

- 部分表关联无法在数据库级别完成，需要在程序中完成
- 对于访问机器频繁且数据量超大的表任然存在性能瓶颈
- 切分达到一定程度之后，扩展性会遇到限制，单表性能依然存在瓶颈

## 水平分表

### 24、mycat水平分表介绍

### 25、mycat水平分表原则

### 26、mycat水平分表取模分片实现

## 全局表

 

