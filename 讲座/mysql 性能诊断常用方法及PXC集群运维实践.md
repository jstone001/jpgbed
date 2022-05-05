# mysql 性能诊断常用方法及PXC集群运维实践



# 一、性能诊断常用方法

## **1.1 mysql特点**

cpu核心与内存比：1:4。 cpu4, 内存16G

###  **内存利用特点**：

- 类似ORACLE的SGA、PGA模式，注意PGA不宜分配过大
- 内存管理简单、有效。在高TPS、高并发环境下，可增加物理内存以减少物理IO，提高并发性能 
- 官方分支锁并发竞争比较严重，MariaDB、Percona进行优化 
- 有类似ORACLE library cache的query cache，但效果不佳，建议关闭 
- 执行计划没有缓存（类似ORACLE的library cache） 
- 通常内存建议按热点数据总量的15%-20%来规划，专用单实例则可以分配物理内存的50~70%左右 
- 类似K-V简单数据，采用memcached、Redis等NOSQL来缓

### **磁盘的利用特点**

- binlog、redo log、undo log主要顺序IO 
- datafile是随机IO和顺序IO都有 
- OLTP业务以随机IO为主，建议加大内存，尽量合并随机IO为顺序IO
- OLAP业务以顺序IO为主，极大内存的同时增加硬盘数量提高顺序IO性能 
- MyISAM是堆组织表（HOT），InnoDB是索引组织表（IOT） 
- InnoDB相比MyISAM更消耗磁盘空间 



## **1.2 性能诊断工具**

### 系统性能诊断工具

- top
- vmstat

```sh
vmstat 2 10
```

- sar
- iotop
- osstat

### mysql性能查看工具

```sql
slow log 
show global status 
show processlist 
show engine innodb status 
pt-ioprofile 
```

### 这里介绍两个工具：

1.mysql一个简单的性能监控工具

https://github.com/dblucyne/dodba_tools

![image-20220427172042920](E:\JS\booknote\jpgBed\image-20220427172042920.png)

2.慢查询分析工具

https://www.percona.com/doc/percona-toolkit/3.0/index.html （PT工具集）

pt-query-digest

![image-20220427172104472](E:\JS\booknote\jpgBed\image-20220427172104472.png)

# **二、cpu,IO和memory**

## **2.1 谁在消耗cpu？**

***用户+系统+IO等待+软硬中断+空闲=cpu\***

![image-20220427172129773](E:\JS\booknote\jpgBed\image-20220427172129773.png)

## **2.2 如何减少CPU消耗？**

**减少等待**

![image-20220427172156211](E:\JS\booknote\jpgBed\image-20220427172156211.png)

**减少计算**

![image-20220427172210380](E:\JS\booknote\jpgBed\image-20220427172210380.png)

![image-20220427172218518](E:\JS\booknote\jpgBed\image-20220427172218518.png)

## **2.3 IO**

![image-20220427172243940](E:\JS\booknote\jpgBed\image-20220427172243940.png)

然后登录mysql执行如下查询：

select * from performance_schema.threads where thread_os_id=37012\G

耗资源的SQL语句立刻就呈现在你眼前，就是如此高效。

以上定位的问题也比较的简单，还有一些复杂的IO问题，比如：binlog写入过大、binlog扫描过多、同步线程阻塞、临时表造成的IO过大，等等问题。

## **3.3 memory**

### **3.3.1mysql内部内存是如何分配的？**

![image-20220427172319432](E:\JS\booknote\jpgBed\image-20220427172319432.png)

### **3.3.2 mysql和swap**

 相关设置： vm.swappiness  不高于5-10 

# **三、索引相关**

## **3.1  Schema设计**

- 基数（ Cardinality ）很低的字段不创建索引（MySQL还不支持 bitmap 索引） 
- 采用第三方系统实现text/blob全文检索 
- 常用排序（ORDER BY）、分组（GROUP BY）、取唯一（DISTINCT）字段上创建索引 
- 索引数量不要太多，有负作用 
- 多使用联合索引，少用单独索引
- 字符型列需要索引时，创建前缀索引

## **3.2  无法使用索引的场景**

- 通过索引扫描的记录数超过30%，变成全表扫描

- 联合索引中，第一个索引列使用范围查询 

- 联合索引中，第一个查询条件不是最左索引列 

- 模糊查询条件列最左以通配符 % 开始 

- 内存表(HEAP 表)使用HASH索引时，使用范围检索或者ORDER BY 

- 两个独立索引，其中一个用于检索，一个用于排序 

- 表关联字段类型不一样（也包括长度不一样）

- 索引字段条件上使用函数

## **3.3  常见杀手级SQL 1**

```sql
SELECT * vs SELECT col1, col2 

ORDER BY RAND() 

LIMIT huge_num, offset 

SELECT COUNT(*) on InnoDB table

WHERE func(key_col) = ? 

WHERE key_part2 =? AND key_part3 =?

WHERE key_part1 > ? AND key_part2 =? 

SELECT … WHERE key_col + ? = ? 
```

## **3.4  常见杀手级SQL 2**

```sql
SELECT a.x ... FROM a ORDER BY a.y LIMIT 11910298, 20; 
# 采用子查询进行优化 => 

SELECT a.x ... FROM a WHERE a.pkid > (SELECT pkid FROM a WHERE pkid >= 11910298 ORDER BY a.y) LIMIT 20;
```

# **四、PXC运维实践**

## **4.1 PXC特性**

1. 高可用

2. 多主写入

3. 数据一致性

4. 节点间实时同步复制

5. 与MySQL兼容

## **4.2 PXC应该监控什么？**

-  wsrep_cluster_size 显示了集群中节点的个数 

-  wsrep_cluster_status  显示集群里节点的主状态。标准返回primary。如返回non-Primary或其他值说明是多个节点改变导致的节点丢失或者脑裂。如果所有节点都返回 不是Primary，则要重设quorum。 

-  wsrep_local_state 显示4为正常。

-  wsrep_local_recv_queue_avg——平均请求队列长度。当返回值大于0时，说明apply write-sets比收write-set慢，有等待。 

-  wsrep_flow_control_paused 显示了自从上次查询之后，节点由于flow control而暂停的时间占整个查询间隔时间比。总体反映节点落后集群的状况。如果返回值为1， 说明自上次查询之后，节点一直在暂停状态。

## **4.3 PXC节点故障该怎么处理？**

![image-20220427172944666](E:\JS\booknote\jpgBed\image-20220427172944666.png)

![image-20220427172950238](E:\JS\booknote\jpgBed\image-20220427172950238.png)

## **4.4 PXC使用和维护该注意什么？**

1. 默认工作在InnoDB引擎表上，因此对其他引擎的表支持的很差，所以不要考虑在PXC上使用MyISAM或者其他的存储引擎 。

2. 所有的表都必须要有主键 。

3. 不支持的操作： LOCK/UNLOCK TABLES、 lock functions (GET_LOCK(), RELEASE_LOCK()... ) 。

4. 由于集群是基于乐观的并发控制（ optimistic concurrency control ），事务冲突的情况可能会在commit阶段发生 。

5. 不支持XA事务，因为XA事务有可能在commit的时候出现异常发生 rollback。

6. 整个集群的吞吐量/性能取决于最慢的那个节点（成本）。

7. 最小建议的集群节点数为3，否则很容易产生脑裂（成本）。<font color='red'>(选举机制）</font>
8. 加入新节点，开销大，有多少个节点就有多少重复的数据 。
9. 不能有效的解决写缩放问题，所有的写操作都将发生在所有节点上 。

# **五、案例**

**做了什么能够支撑2500万的全市大筛查？**

1. 分库(按业务分)

2. 缓存

3. 优化sql慢查询

4. 升级服务器配置（大内存,SSD)

5. 减少数据量（归档）



<font color='red'>My Question：</font>

- mysql_mgr
- DBLE中间件
- TiDB
- XtraBackup
- 15921984996  江贻龙

