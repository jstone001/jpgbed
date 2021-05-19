from: https://www.bilibili.com/video/BV1zJ411w7SV/

## P01-nginx课程介绍

1. 基本概念

   - 反射代理
   - 负载均衡
   - 动静分离

2. 安装、常用命令和配置文件

   - linux下安装nginx
   - 常用命令
   - 配置文件

3. 配置实例1：反向代理

4. 配置实例2：负载均衡

5. 配置实例3：动静分离

6. nginx高可用集群 

7. nginx原理

## P02-nginx的简介

https://lnmp.org/nginx.html

有报告表明能支持高达50000   个并发连接数

Nginx具有很高的稳定性。其它HTTP服务器，当遇到访问的峰值，或者有人恶意发起慢速连接时，也很可能会导致服务器物理内存耗尽频繁交换，失去响应，只能重启服务器。例如当前apache一旦上到200个以上进程，web响应速度就明显非常缓慢了。而Nginx采取了分阶段资源分配技术，使得它的CPU与内存占用率非常低。nginx官方表示保持10,000个没有活动的连接，它只占2.5M内存，所以类似DOS这样的攻击对nginx来说基本上是毫无用处的。就稳定性而言,nginx比lighthttpd更胜一筹。

## P03-nginx相关概念（正向和反向代理）

## P04-nginx相关概念（负载均衡和动静分离）

负载均衡： 

动静分离：

## P05-nginx在linux系统安装

 ```sh
 # 安装依赖
 pcre-8.37.tar.gz
 openssl-1.0.1t.tar.gz
 zlib-1.2.8.tar.gz
 
 # 安装pcre
 下载pcre包
 ./configure
 make && make install
 pcre-config  --version
 
 # yum
 yum -y install make zlib zlib-devel gcc-c++libtool openssl openssl-devel
 
 # 安装nginx
 官网下载ngix包
 ./configure
 make && make install
 在/usr/local/nginx/sbin/nginx   #启动脚本
 
 
 # 查看开放的端口
 firewall-cmd --list-all
 
 # 设置开放的端口
 firewall-cmd --add-service=http --permanent
 sudo firewall-cmd --add-port=80/tcp --permanent
 
 # 重启服务墙
 firewall-cmd --reload
 ```

##  P06-nginx常用的命令

```sh
# 前提条件，进入目录
cd /usr/local/nginx/nginx/sbin/

./nginx -v 	# 查看nginx版本号
./nginx		# 启动
./nginx -s stop		# 关闭nginx
./nginx -s reload	# 重新加载
```

## P07-nginx的配置文件

nginx.conf

```sh
# 第1部分 全局块
worker_processes  1;

# 第2部分 events块
events{
	worker_connections 1024;
}

# 第3部分 http块
# 代理，缓存，日志等绝大多数功能和第3分模块的配置都在这里
# 又包含http全局块  server块
# 1. http全局块：包括文件引入、MIME-TYPE定义、日志自定义、连接超时时间、单链接请求数上限等
# serv1er块：
## 2.1 全局server块：最常见的配置是本虚拟机主机的监听配置和本虚拟机的名称或IP配置
## 2.2 location块：只要作用是基于Nginx服务接收到的请求字符串，对虚拟主机名称之外的字符串进行匹配，对特定的请求进行处理。地址定向，数据缓存和应答控制等功能。
http{
	include mime.types;
	default_type application/octet-stream;
}
```

# 反向代理

## P08-nginx配置实例（反向代理准备工作）

```sh
#安装tomcat，直接进入tomcat/bin
./startup.sh
```

## P09-nginx配置实例（反向代理实例一）

```sh
# 需关闭selinux
getforce	# 查询 Enforcing
setenforce 0	# 临时关闭
getforce 	# Permissive
sed -i 's/enforcing/disabled/' /etc/selinux/config  # 永久	

```



/etc/nginx/conf.d/default.conf

```sh
server {
	server_name 192.168.132.42
	
	location /{
		proxy_pass	http://127.0.0.1:8080;
		index index.html index.htm  index.jsp;
	}
}
```

## P10-nginx配置实例（反向代理实例二）

访问 http://127.0.0.1:9001/edu/  直接跳转到127.0.0.1:8080

访问 http://127.0.0.1:9001/vod/  直接跳转到127.0.0.1:8081

```sh
# 修改端口
在server.xml下的8080,8005修改一下
```

```sh
server{
	listen 9001;
	server_name localhost;
	
	location ~ /edu/{
		proxy_pass http://localhost:8080;
	}
	location ~ /vod/{
		proxy_pass http://localhost:8081;
	}
}
```

```sh
# 在8080端口的tomcat的webapps/下添加edu/a.html 内容8080
# 在8081端口的tomcat的webapps/下添加edu/a.html 内容8081

```

```sh
# 最终测试
# 在浏览器中输入http://192.168.132.42:9001/vod/a.html和http://192.168.132.42:9001/edu/a.html
```

**location的配置**：

- =：用于不含正则表达式的uri前，要求请求字符串与uri 严格匹配，如果匹配成功，就停止继续向下搜索并立即处理该请求。
- ~：用于表示uri包含正则表达式，关且区分大小写。
- ~*：用于表示uri包含正则表达式，并且不区分大小写。
- ^~：用于不含正则表达式的uri前，要求Nginx服务器找到标识uri和请求字符串匹配度最高的location后，立即使用此location处理请求，而不再使用location块中的正则uri和请求字符串做匹配。

​        注意：如果uri包含正则表达式，则必须要有~或者 ~*标识。

# 负载均衡

## P11-nginx配置实例（负载均衡）

<font color='red'>不要用chrome, 会有缓存</font>

```sh
http{
	upstream myserver{
		ip_hash;
		server 192.168.132.42:8080  weight=1;
		server 192.168.132.42:8081  weight=1;
	}
	
	server{
		location / {
			......
			proxy_pass http://myserver;
			proxy_connect_timeout 10;
		}
	}

}
```

### 负载均衡策略：

- 轮询：
- weight：
- ip_hash：
- fair（第3方）：按后端服务器的响应时间来分配请求，响应时间短的优先分配。

```sh
 upstream www.feng.com{
         fair;
         server 172.17.0.2:8080 ;
         server 172.17.0.3:9090 ;
}
```

# 动静分离

 定义：

- 一种是纯粹把静态文件独立成单独的域名，放在独立的服务器上，也是目前主流推崇的方案；

- 一种是动态跟静态文件混合在一起发布，通过nginx来分开。

  ​	通过location指定不同的后缀名实现不同的请求转发。通过expires参数设置，可以使浏览器缓存过期时间，减少与服务器之前的请求和流量。具体expires定义；是给一个资源设定一个过期时间，也就是说无需去服务端验证，直接通过浏览器自身确认是否过期即可，所以不会产生额外的流量。此种方法非常适合不经常变动的资源。（如果经常更新模板的文件，不建议使用expires来缓存），这里设置3d，表示在这3天之内访问这个url，发送一个请求，比对服务器该文件最后更新时间没有变化，则不会从服务器抓取，返回状态码304，如果有修改，则直接从服务器重新下载，返回状态码200。

## P12-nginx配置实例（动静分离准备工作）

## P13-nginx配置实例（动静分离）

```sh
location /www/ {
	root /data/;
	index index.html index.htm;
}

location /image/ {
 	root /data/;
 	autoindex on;
}
```

# 高可用 

## P14-nginx配置实例（高可用准备工作）

准备2台服务器，都装上nginx, keepalived

安装 keepalived

```sh
yum install -y keepalived
```

## P15-nginx配置实例（高可用主备模式）

keepalived.conf

```sh
global_defs{	# 关于全局
	notification_email{
		accessen@firewall.loc
		failover@firewall.loc
		sysadmin@firewall.loc
	}
	notification_email_from Alexandre.Cassen@firewall.loc
	smtp_server 192.168.17.129
	smtp_connect_timeout 30
	router_id LVS_DEVEL		# 访问到主机 LVS_DEVEL：服务器名
}

vrrp_script chk_http_port{	# 关于脚本
	script "/usr/local/src/nginx_check.sh"  # 检测脚本
	internal 2	# 检测脚本执行的间隔
	weight 2	# 当前服务器权重
}

vrrp_instance VI_1 {	# 虚拟ip的配置
	state BACKUP	# 主服务器MASTER 备份服务器BACKUP
	interface ens33		# 网卡
	virtual_rooter_id  51 # 主、备机的virtual_router_id必须相同
	priority 100	# 主、备机取不同的优先级，主机值较大，备份机较小
	advert_int  1	# 心跳时间
	authentication {
		auth_type PASS	# 权限认证方式
		auth_pass 1111	# 密码
	}
	virtual_ipaddress {
	 192.168.17.50	# VRRP H虚拟地址（可多个）
	}
}
```

nginx_check.sh

```sh
#!/bin/bash
A=`ps -C nginx -no-header | wc -l`
if [ $A -eq 0 ];then
    /usr/local/nginx/sbin/nginx
    sleep 2
    if [ `ps -C nginx --no-header | wc -l` -eq 0 ];then
        killall keepalived
    fi
fi
```



## P16-nginx配置实例（高可用配置文件详解）

见P15

# 原理

## P17-nginx的原理解析

![image-20210518150431898](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210518150431898.png)

worker 是如何工作的

![image-20210518152922653](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210518152922653.png)

**master-workers的机制的好处**

- 可以使用nginx -s reload 热部署，利于nginx进行热部署。

- 首先，对于每个worker进程来说，独立的进程，不需要加锁，所以省掉了锁带来的开销，同时在编程以及问题查找时，也会方便很多。其次，采用独立的进程，可以让互相之间不会影响，一个进程退出后，其它进程还在工作，服务不会中断，master进程则很快启动新的worker进程。当然，worker进程的异常退出，肯定是程序有bug了，异常退出，会导致当前worker上的所有请求失败，不过不会影响到所有请求，所以降低了风险。
- 需要设置多少个worker？Nginx同redis类似都采用了<font color='red'>io多路复用</font>机制，每个worker都是一个独立的进程，但每个进程里只有一个主线程，通过<font color='red'>异步非阻塞</font>的方式来处理请求，即使是千万个请求也不在话下。每个worker的线程可以把一个cpu的性能发挥到极致。所以worker数和服务器的cpu数相等是最为适宜的。设少了会浪费cpu，多了会造成cpu频繁切换上下文带来的损耗。

```sh
# 设置worker数量
work_processes 4
# 绑定 cpu（4 work绑定 4cpu）
worker_cpu_affinity 0001 0010 0100 1000

# work绑定cpu（4 work绑定8cpu中的4个）
work_cpu_affinity 00000001 00000010 00000100 00000101

# 连接数worker_connection
这个值是表示每个worker进程所能建立连接的最大值。所以，一个nginx能建立的最大连接数，应该是worker_connections*worker_processes。当然，这里说的是最大连接数，对于http请求本地资源来说，能够支持的最大并发数是worker_connections*worker_processes，如果是支持http1.1的浏览器每次访问要占2个连接，所以普通的静态最大并发数是worker_connections*worker_processes/2，而如果是http作为反向代理来说，最大并发数量应该是worker_connections*worker_processes/4。因为反向代理服务器，每个并发会建立与客户端和与后端服务的连接，会用上2个连接。
```

