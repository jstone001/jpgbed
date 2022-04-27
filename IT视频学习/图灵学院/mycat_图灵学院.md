https://vip.tulingxueyuan.cn/detail/p_603396fbe4b035d3cdba1905/6

https://vip.tulingxueyuan.cn/detail/v_6033b751e4b0f176aed37f57/3?from=p_603396fbe4b035d3cdba1905&type=6&parent_pro_id=

（讲得特别差，非运维，机子又差）

# 141 mycat分库分表实战

server.xml

```xml
<user name="taibai" defaultAccout="true">
	<property name="password" >123456</property>
    <property name="schemas">vip_admin,vip_order,vip_product</property>	<!-- 不同的数据库用，隔开。区分大小写-->
    <property name="readOnly">true</property>
    
    <!-- 表级DML 权限设置 -->
    <privileges check="false">
    	<schema name="vip_admin" dml="0110">	<!-- 4位指增删改查 -->
        	<table name="tb01" dml="0000"></table>
        	<table name="tb02" dml="1111"></table>
        </schema>
    </privileges>
</user>
```

wrapper.conf

```sh
# 修改Mycat运行内存
wrapper.java.additional.9=-Xmx4G		# 运行的内存，云服务器可能没有那么大内存  1G
wrapper.java.additional.10=-Xms1G		# 可改成512M
```

```sh
# bin 目录下
./mycat start
```

balance参数

  负载均衡类型：

1. balance="0", 不开启读写分离机制，所有读操作都发送到当前可用的writeHost 上。
2. <font color='red'>balance="1"，全部的 readHost 与 stand by writeHost 参与 select  语句的负载均衡，简单的说，当双主双从模式(M1->S1，M2->S2，并且 M1 与 M2 互为主备)，正常情况下，M2,S1,S2 都参与 select 语句的负载均衡。</font>
3. balance="2"，所有读操作都随机的在 writeHost、 readhost 上分发。
4. <font color='red'>balance="3"，所有读请求随机的分发到 wiriterHost 对应的 readhost 执行，writerHost 不负担读压力，注意 balance=3 只在 1.4 及其以后版本有，1.3 没有。</font>

# 142 通用集群高可用实战

自增id

- 写在配置文件中
- 写在数据库中

## 通用集群方案

### haproxy：做负载均衡

### keepalived：ip漂移

### 安装  zookeeper 做集群

下载：https://zookeeper.apache.org/releases.html

#### 将配置导入zk

##### zookeeper 导入mycat配置文件

​	将所有自己改的配置文件复制到zkconf文件夹中

##### 修改mycat/conf/myid.properties

```sh
loadZk=true
zkURL=111.231.227.24:2181   # 如果是集群，则用,号分割
clusterSize=1	# 单机
```

##### 运行bin/init_zk_data.sh

##### 另一台也只要修改myid.properties

### 安装 haproxy

```sh
yum -y install haproxy
```

修改/etc/haproxy/aproxy.cfg

```sh
global
	log 127.0.0.1 local0
	#log 1270.0.0.1 local1 notice
	#log loghost local0 info
	maxconn 4096	# 最大链接数
	pidfile /usr/data/haproxy/haproxy.pid
	uid 99
	gid 99
	daemon
	#debug
	#quiet
defaults
	log global
	mode tcp
	option abortonclose
	option redispatch
	retries 3
	maxconn 2000
	timeout connect 5000
	timeout client 50000
	timeout server 50000
listen proxy_status	
	bind :48066		# 端口
	mode tcp
	balance roundrobin
	server mycat_1 192.168.204.201:8066 check inter 10s
	server mycat_2 192.168.204.200:8066 check inter 10s
frontend admin_stats	# 监控
	bind :7777
		mode http
		stats enable
		option httplog
		maxconn 10
		stats refresh 30s
		stats uri /adimn	# 用户名
		stats auth admin:123123	# 密码
		stats hide-version
		stats admin if TRUE
```



```sh
touch /usr/data/haproxy/haproxy.pid  # 创建pid文件
```

```sh
haproxy -f ./haproxy.cfg  #启动haproxy
```

登录 http://192.168.204.200:7777/admin

### 安装 keepalived

必须安装在2台haproxy的电脑上，一个为master，一个为backup

```sh
tar -zxvf keepalived-1.4.2.tar.gz -C /usr/local/src
yum install -y gcc openssl-devel popt-devel  # 安装依赖 
```

```sh
cd /usr/local/src/keepalived-1.4.2
./configure --prefix=/usr/local/keepalived

make && make install

cp /usr/local/src/keepalived-1.4.2/keepalived/etc/init.d/keepalived /etc/init.d/
mkdir /etc/keepalived
cp /usr/local/keepalived/etc/keepalived/keepalived.conf /etc/keepalived
cp /usr/local/src/keepalived-1.4.2/keepalived/etc/sysconfig/keepalived /etc/sysconfig/
cp /usr/local/keepalived/sbin/keepalived /usr/bin
```

修改  /etc/keepalived/keepalived.conf

```sh
！ Configuration File for keepalive
global defs {
	notification_email{
		1723423@qq.com	 #邮件报警地址
	}
	notification_email_from 12332334@qq.com 	# 指定发件人
	smtp_server 127.0.0.1 		# 指定smtp服务器地址
	smtp_connect_timeout 30 	# 指定smtp连接超时时间
	router_id LVS_DEVEL			# 负载均衡标识，在局域网内应该是唯一的。
	vrrp_skip_check_adv_addr	
	vrrp_garp_interval 0
	vrrp_gna_interval 0
}
vrrp_instance VI_1{
	state MASTER			# 指定该keepalived节点的初始状态  # BACKUP
	interface ens33			# 网卡名称
	virtual_router_id 51	# 指定VRRP实例ID, 范围0~255
	priority 100			# 指定优先级，优先级高的将成为MASTER
	advert_int 100			# 指定发送VRRP通告的间隔，单位是秒。
	authentication{
		auth_type PASS		# 指定认证方式。PASS简单密码认证（推荐），AH:IPSEC认证（不推荐）。
		auth_pass 1111		# 指定认证所使用的密码。最多8位
	}
	virtual_ipaddress{
		192.168.204.177		# 指定VIP地址。虚拟IP
	}
}
```

```sh
service keepalived start 	# 启动
```



### 腾讯云申请高可用虚拟IP

