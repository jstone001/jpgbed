# 安装Redis

1. 官网下载redis

2. make && make install

3. 升级gcc

```bash
https://www.cnblogs.com/yjt1993/p/13496132.html

问题1：在make的时候，提示如下错误：
server.c:5170:39: error: ‘struct redisServer’ has no member named ‘maxmemory’
if (server.maxmemory > 0 && server.maxmemory < 1024*1024) {
                                  ^
server.c:5171:176: error: ‘struct redisServer’ has no member named ‘maxmemory’
    serverLog(LL_WARNING,"WARNING: You specified a maxmemory value that is less than 1MB (current value is %llu bytes). Are you sure this is what you really want?", server.maxmemory);
                                ^
server.c:5174:31: error: ‘struct redisServer’ has no member named ‘server_cpulist’
redisSetCpuAffinity(server.server_cpulist);

解决办法：
出现这种错误是由于gcc版本太低，升级gcc
# 查看gcc版本是否在5.3以上，centos7.6默认安装4.8.5
gcc -v
# 升级gcc到5.3及以上,如下：
升级到gcc 9.3：
yum -y install centos-release-scl
yum -y install devtoolset-9-gcc devtoolset-9-gcc-c++ devtoolset-9-binutils
scl enable devtoolset-9 bash
需要注意的是scl命令启用只是临时的，退出shell或重启就会恢复原系统gcc版本。
如果要长期使用gcc 9.3的话：
 
echo "source /opt/rh/devtoolset-9/enable" >>/etc/profile
这样退出shell重新打开就是新版的gcc了
以下其他版本同理，修改devtoolset版本号即可

```

4. redis-server &    后台运行

# 文档

```
redis.io  官网
redisdoc.com  在线文档
```

# 启动方式

```bash
redis-server --port 1234 >redis.log &
redis-cli -h 120.33.xxx.xx -p 1234
redis-benchmark    #redis基准测试
reids-server redis-5.0.4/redis.conf &    #根据conf起redis
```

# 常用命令

```bash
set username xxxx ex 30  #30秒后过期
ttl username   #key存活时间
info  #看服务器信息
type
select   #换库
flushdb  #清空当前数据库
dbsize
auth xxxx    #输入密码验证
config set requirepass 123    #cli下设置密码
```

# 变量类型

```bash
String
    mset   一次加多个KV值
哈希表
    hset
    hmset
    hmget
    hvals
    hgetall
列表 list
    lpush
    lrang key 0 -1  # 查看整个列表
集合
    sadd key 
    smembers
    scard
    sismember
    sinter	# 交集
    sunion	# 并集
    sdiff 	# 差集
    spop	# 随机取元素
有序集合
    zadd
    zrange	# 从小到大排
    zrevrange	# 从大到小排
地理信息
    geoadd key 
    geodist
```

# 主从

```bash
cli> slaveof host port
masterauth xxx    #主人的密码
info replication    #看主从情况
redis-server --slaveof xxx.xx.xxx.xx 6379 --masterauth xxx >redis.log &
slaveof no one   #不当奴隶了
redis.conf  replicaof
```

# 报错

## python连redis  DENIED Redis is running in protected mode

redis-server --bind xxx.xx.xxx.xx(内网地址)

## broken pipe

```bash
from:https://www.cnblogs.com/tangyin/p/9951181.html

redis报错：java.net.SocketException: Broken pipe (Write failed); nested exception is redis.clients.jedis.exceptions.JedisConnectionException: java.net.SocketException: Broken pipe (Write failed)
　　　最近写了一个服务通过springboot构建，里面使用了redis作为缓存，发布到服务器运行成功，但是有时候会报redis的错误：org.springframework.data.redis.RedisConnectionFailureException: java.net.SocketException: Broken pipe (Write failed); nested exception is redis.clients.jedis.exceptions.JedisConnectionException: java.net.SocketException: Broken pipe (Write failed)   
　　在网上查了一下原因是因为redis的客户端超时时间超时导致写入失败，然后我把配置文件的 timeout 参数设置为0 同时配置testOnReturn，testWhileIdle，testOnBorrow为true 这样就没有提示这样的错误了
￼
#客户端超时时间
redis.timeout=0
#是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
redis.testOnBorrow=true  
#在空闲时检查有效性, 默认false
redis.testWhileIdle=true  
#是否进行有效性检查
redis.testOnReturn=true
```

# Redis修复持久化文件的工具

## redis-check-rdb

## redis-check-aof

```bash
    写入所有命令的存储方式
    redis-server --requirepass xxx --appendonly yes &        #aof方式启动
    redis-check-aof --fix appendonly.aof   #修复appendonly.aof文件
```



