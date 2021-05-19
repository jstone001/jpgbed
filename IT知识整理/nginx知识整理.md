# nignx在linux上的安装 

```sh
#默认情况Centos7中没有Nginx的源，Nginx官方提供了源，所以执行如下命令添加源
rpm -Uvh http://nginx.org/packages/centos/7/noarch/RPMS/nginx-release-centos-7-0.el7.ngx.noarch.rpm
 
#安装Nginx
yum install -y nginx
 
#启动Nginx并设置开机自动运行
systemctl start nginx.service
systemctl enable nginx.service
 
#Nginx关闭命令
systemctl stop nginx.service
#Nginx重启命令
systemctl restart nginx.service
 
#关闭防火墙
systemctl stop firewalld #临时关闭
systemctl disable firewalld #然后reboot 永久关闭
systemctl status  firewalld #查看防火墙状态

# 输入ip地址有nignx主页即可
```

# 反向代理

要先关闭selinux

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

# 负载均衡策略

## **轮询（默认）**

```sh
#每个请求轮流分配到不同的后端服务器，如果后端服务器挂掉，则自动被剔除；
#参考实例：

upstream www.feng.com{
         server 172.17.0.2:8080 ;
         server 172.17.0.3:9090 ;
}
```

## weight权重

```sh
#根据weight权重，请求会根据权重比例分发给不同后端服务器，weight权重越高，分配的比例越大；
#实际分配，根据服务器硬件配置高低，来具体分配weight权重，硬件配置高的，weight就配置高点；
#参考实例：
upstream www.feng.com{
         server 172.17.0.2:8080 weight=5;
         server 172.17.0.3:9090 weight=10;
    }
```

## ip_hash

```sh
# ip_hash策略是根据用户客户端的IP的hash值来分配具体服务器，这样每个访问客户端都会固定访问某一个服务器，这样可以解决session丢失问题，很多网站都采用这种策略来搞负载均衡，主要考虑到session问题；
# 参考配置：
 upstream www.feng.com{
         ip_hash;
         server 172.17.0.2:8080 ;
         server 172.17.0.3:9090 ;
}
```

## least_conn最少连接

```sh
#web请求会被分发到连接数最少的服务器上；
# 参考实例：
 upstream www.feng.com{
         least_conn;
         server 172.17.0.2:8080 ;
         server 172.17.0.3:9090 ;
}
```

## fair（第3方）：按后端服务器的响应时间来分配请求，响应时间短的优先分配。

```sh
 upstream www.feng.com{
         fair;
         server 172.17.0.2:8080 ;
         server 172.17.0.3:9090 ;
}
```

# 动静分离的定义

 定义：

- 一种是纯粹把静态文件独立成单独的域名，放在独立的服务器上，也是目前主流推崇的方案；

- 一种是动态跟静态文件混合在一起发布，通过nginx来分开。

  ​	通过location指定不同的后缀名实现不同的请求转发。通过expires参数设置，可以使浏览器缓存过期时间，减少与服务器之前的请求和流量。具体expires定义；是给一个资源设定一个过期时间，也就是说无需去服务端验证，直接通过浏览器自身确认是否过期即可，所以不会产生额外的流量。此种方法非常适合不经常变动的资源。（如果经常更新模板的文件，不建议使用expires来缓存），这里设置3d，表示在这3天之内访问这个url，发送一个请求，比对服务器该文件最后更新时间没有变化，则不会从服务器抓取，返回状态码304，如果有修改，则直接从服务器重新下载，返回状态码200。