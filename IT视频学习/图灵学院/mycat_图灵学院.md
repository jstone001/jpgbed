https://vip.tulingxueyuan.cn/detail/p_603396fbe4b035d3cdba1905/6

https://vip.tulingxueyuan.cn/detail/v_6033b751e4b0f176aed37f57/3?from=p_603396fbe4b035d3cdba1905&type=6&parent_pro_id=

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
