

# 简介

## 01. Nginx简介

![](http://nginx.org/nginx.png)

- Nginx是lgor Sysoev为俄罗斯访问量第二的rambler.ru站点设计开发的。从2004年发布至今，凭借开源的力量，已经接近成熟与完善。
- Nginx功能丰富，可作为HTTP服务器，也可作为反向代理服务器，邮件服务器。支持FastCGI、SSL、Virtual Host、URL Rewrite、Gzip等功能。并且支持很多第三方的模块扩展。
- Nginx的稳定性、功能集、示例配置文件和低系统资源的消耗让他后来居上，在全球活跃的网站中有12.18%的使用比率，大约为2220万个网站。
- 我们课程主要是基于Docker讲解Nginx，讲解内容有Nginx原理以及配置文件，反向代理，负载均衡，
- 动静分离，虚拟主机，以及Nginx+Keepalived实现高可用集群，以及springboot+nginx+tomcat+redis实现session共享

官方站点：http://nginx.org/

# 安装及运行

## 02. Nginx基于Docker安装以及运行

```sh
docker pull nginx		# 下载nginx镜像
docker run --name mynginx -id -p 80:80 image_id  # 运行nginx容器
docker restart 容器id	# 重启nginx容器
docker stop 容器id	# 关闭nginx容器
docker exec -it 容器id /bin/bash 	#进入nginx容器
```

# Nginx原理及配置文件讲解

## 03. nginx安装目录

我们如何来找nginx的安装目录呢，有两种方法；

- 第一种：通过dockerfile的定义来看；
- 第二种，简单粗暴，直接搜索 find / -name nginx

```sh
root@729a9f3d9c48:/# find / -name nginx
/etc/default/nginx
/etc/init.d/nginx
/etc/logrotate.d/nginx
/etc/nginx
find: '/proc/1/map_files': Operation not permitted
find: '/proc/8/map_files': Operation not permitted
find: '/proc/9/map_files': Operation not permitted
find: '/proc/15/map_files': Operation not permitted
/usr/lib/nginx
/usr/sbin/nginx
/usr/share/doc/nginx
/usr/share/nginx
/var/cache/nginx
/var/log/nginx

/etc/nginx/nginx.conf	# nginx配置文件
/var/log/nginx	# 日志目录
/usr/share/nginx/html	# 静态文件目录
```

## 04. 挂载容器目录启动nginx容器

为了方便我们修改配置文件，我们启动容器的时候，需要挂载容器目录，这样可以在宿主机中修改配置，来实现同步容器里的文件。

```sh
#  首先第一步：我们需要copy下原始数据
docker cp container_id:/etc/nginx /home/data/nginx

# 启动容器，挂载目录
docker run -it --name=myNginx1 -v /root/nginx/data/nginx:/etc/nginx  -p 80:80 nginx:1.17.1	# 注意主机下要再加个nginx文件夹
```



## 05. nginx配置文件基本配置

```sh
user  nginx;  # 配置worker进程运行用户
worker_processes  1;  # 配置工程进程数目，根据硬件配置，一般是和CPU数量一致，或者CPU数量的2倍，能达到最佳性能(一般和cpu数量一致即可)
error_log  /var/log/nginx/error.log warn;  # 配置全局错误日志文件以及配置级别 [ debug | info | notice | warn | error | crit ] 
pid        /var/run/nginx.pid;  #配置进程pid文件（了解即可）


#关于日志级别：
#在配置nginx.conf 的时候，有一项是指定错误日志的，默认情况下你不指定也没有关系，因为nginx很少有错误日志记录的。但有时出现问题时，是有必要记录一下错误日志的，方便我们排查问题。
#error_log 级别分为 debug, info, notice, warn, error, crit  默认为crit, 该级别在日志名后边定义格式如下：
#error_log  /your/path/error.log crit;  
#crit 记录的日志最少，而debug记录的日志最多。如果你的nginx遇到一些问题，比如502比较频繁出现，但是看默认的error_log并没有看到有意义的信息，那么就可以调一下错误日志的级别，当你调成error级别时，错误日志记录的内容会更加丰富。
```

## 06. nginx配置文件events配置

```sh
events 是配置工作模式和连接数
 
events {
    worker_connections  1024;  # 配置每个worker进程连接数上限
}
 
# 说明：nginx支持得总连接数=worker_processes * worker_connections
```

![image-20210510101547524](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210510101547524.png)

## 07. nginx配置文件http配置之基本配置

```sh
# 配置http服务器
http {
    include       /etc/nginx/mime.types;  # 配置nginx支持哪些多媒体类型
    default_type  application/octet-stream; # 默认文件类型
   
    # 配置日志格式
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';


    access_log  /var/log/nginx/access.log  main;  #配置访问日志  ，并使用上面的格式


    sendfile        on; # 开启高效文件传输模式
    #tcp_nopush     on; #开启防止网络阻塞模式


    keepalive_timeout  65; #长连接超时时间，单位秒

    #gzip  on;  #开启gzip压缩输出

    include /etc/nginx/conf.d/*.conf;	# 包含其他conf文件
```



## 08. nginx配置文件http配置之server配置

/root/nginx/data/nginx/conf.d/default.conf

```sh
# 配置server服务器；可以多个；
 
server {
    listen       80;  #监听端口 
    server_name  localhost;  # 配置服务名
 
    #charset koi8-r;  #配置字符集
    #access_log  /var/log/nginx/host.access.log  main;  #配置本虚拟主机访问日志
 
   # 匹配/请求 ，/是根路径请求，会被该location匹配到并且处理
    location / {
        root   /usr/share/nginx/html;  #root是配置服务器的默认网关根目录位置
        index  index.html index.htm; #配置首页文件的名称
    }
 
    #error_page  404              /404.html; #配置404页面
 
    # redirect server error pages to the static page /50x.html
    # 
    error_page   500 502 503 504  /50x.html;  #配置50x错误页面
    location = /50x.html {
        root   /usr/share/nginx/html;
    }
 
    # proxy the PHP scripts to Apache listening on 127.0.0.1:80
    #
    #location ~ \.php$ {
    #    proxy_pass   http://127.0.0.1;
    #}
 
    # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
    #
    #location ~ \.php$ {
    #    root           html;
    #    fastcgi_pass   127.0.0.1:9000;
    #    fastcgi_index  index.php;
    #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
    #    include        fastcgi_params;
    #}
 
    # deny access to .htaccess files, if Apache's document root
    # concurs with nginx's one
    #
    #location ~ /\.ht {
    #    deny  all;
    #}
}
 
```

# Nginx实现反向代理

## 09. 什么是反向代理？

**什么是代理服务器？**

代理服务器，客户机在发送请求时，不会直接发送给目的主机，而是先发送给代理服务器，代理服务接受客户机请求之后，再向主机发出，并接收目的主机返回的数据，存放在代理服务器的硬盘中，再发送给客户机。

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/_CopyPix_6_3.png)



**为什么要使用代理服务器？**

1. 提高访问速度

由于目标主机返回的数据会存放在代理服务器的硬盘中，因此下一次客户再访问相同的站点数据时，会直接从代理服务器的硬盘中读取，起到了缓存的作用，尤其对于热门站点能明显提高请求速度。

2. 防火墙作用

由于所有的客户机请求都必须通过代理服务器访问远程站点，因此可在代理服务器上设限，过滤某些不安全信息。

3. 通过代理服务器访问不能访问的目标站点

互联网上有许多开放的代理服务器，客户机在访问受限时，可通过不受限的代理服务器访问目标站点，通俗说，我们使用的翻墙浏览器就是利用了代理服务器，虽然不能出国，但也可直接访问外网。



**什么是正向代理？**

正向代理，架设在客户机与目标主机之间，只用于代理内部网络对 Internet 的连接请求，客户机必须指定代理服务器,并将本来要直接发送到 Web 服务器上的 Http 请求发送到代理服务器中。

一般在客户机

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/_CopyPix_7_3.png)

**什么是反向代理？**

反向代理服务器架设在服务器端，通过缓冲经常被请求的页面来缓解服务器的工作量，将客户机请求转发给内部网络上的目标服务器；并将从服务器上得到的结果返回给 Internet 上请求连接的客户端，此时代理服务器与目标主机一起对外表现为一个服务器。



![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/_CopyPix_8_3.png)

## 10. Nginx反向代理实例

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/_CopyPix_9.png)

 我们要实现下访问Nginx的时候，代理请求tomcat服务器；

```sh
#启动nginx
docker run -it --name=myNginx -v /home/data/nginx:/etc/nginx  -p 80:80 nginx

#启动tomcat
docker run -d  -p 8080:8080 tomcat_image_id   # webapps.dist 取代webapps目录

docker inspect container_id		# 查看容器的ip
```



nginx配置：

```sh

server {
    listen       80;  # 监听端口 
    server_name   172.17.0.2;  # server_name改成nginx内网IP或者改成域名
 
    #charset koi8-r;  #配置字符集
    #access_log  /var/log/nginx/host.access.log  main;  #配置本虚拟主机访问日志
 
   # 匹配/请求 ，/是根路径请求，会被该location匹配到并且处理
    location / {
        proxy_pass	http://172.17.0.3:8080;
        index  index.html index.htm
    }
 
}
```



server_name改成nginx内网IP或者改成域名

location里加个 proxy_pass 配置 值改成tomxat地址；

 这样可以实现Nginx反向代理 ，我们访问Nginx的时候，代理请求 tomcat服务器；

default.conf

```properties
server_name =www.feng.com
```



```sh
本机修改hosts可以搞个域名映射本机
C:\Windows\System32\drivers\etc
192.168.1.112 www.feng.com
```



![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/_CopyPix_11.png)

# Nginx实现负载均衡

## 11. 什么是负载均衡？

一个网站 创建初期 没多少流量的时候，网站能正常访问；

- 但是这个网站等流量上升，并发量大的时候，网站会出现访问延迟，甚至访问失败的问题；

- 这时候，就需要用到负载均衡，即我们以前部署的是单一服务器，现在部署多个服务器集群，把流量分发的集群中的不同的机器上，这个就是负载均衡技术实现要做的事；

- 使用了负载均衡后，每个节点机器上处理的请求相对会少很少，所以能够解决访问延迟卡顿和访问失败问题，提高网站用户体验；

![image-20210510140317631](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210510140317631.png)

## 12. Nginx实现负载均衡

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/_CopyPix_13-1620626704434.png)



我们实现下通过Nginx实现负载均衡，来把客户端的请求分发达到两个tomcat；

*准备2个tomcat*

```sh
#启动tomcat
#运行容器
#宿主机里home目录下新建tomcat1和tomcat2目录，复制容器里conf,webapps到宿主机
docker cp 容器id:/usr/local/tomcat/conf /home/tomcat/
docker cp 容器id:/usr/local/tomcat/webapps /home/tomcat/

# 修改两个tomcat的配置，端口分别是8080和9090
server.xml
# 以及两个tomcat默认jsp内容改成8080和9090
/root/tomcat1/webapps/ROOT/index.jsp

# 启动两个tomcat测试
docker run -d --name tomcat1 -p 8080:8080 -v /home/tomcat1/conf/:/usr/local/tomcat/conf/ -v /home/tomcat1/webapps/:/usr/local/tomcat/webapps/  be38ac28efcd

docker run -d --name tomcat2 -p 9090:9090 -v /home/tomcat2/conf/:/usr/local/tomcat/conf/ -v /home/tomcat2/webapps/:/usr/local/tomcat/webapps/  be38ac28efcd
```

  运行没问题就OK；

 

配置nginx.conf；

```sh
# 在http模块下配置下加

upstream www.feng.com{

	server 172.17.0.2:8080 weight=5;
	server 172.17.0.3:9090 weight=10;

  }
  
# upstream是配置nginx与后端服务器负载均衡非常重要的一个模块，并且它还能对后端的服务器的健康状态进行检查，若后端服务器中的一台发生故障，则前端的请求不会转发到该故障的机器；

# weight是权重配置 权重越高 分配到的概率越高；
```
conf.d/default.conf

```sh
# 在http下的server模块
location / {

	proxy_pass  http://www.feng.com;  # 这里修改下
	index index.html index.htm;

  }
  
# proxy_pass配置upstream对象即可；
```

这样就可以实现负载均衡 我们请求nginx服务器地址，不断刷新请求，会出现9090或者8080内容页面；

## 13. Nginx负载均衡策略（常用）

### 轮询（默认）

```sh
#每个请求轮流分配到不同的后端服务器，如果后端服务器挂掉，则自动被剔除；
#参考实例：

upstream www.feng.com{
         server 172.17.0.2:8080 ;
         server 172.17.0.3:9090 ;
}
```

### weight权重

```sh
#根据weight权重，请求会根据权重比例分发给不同后端服务器，weight权重越高，分配的比例越大；
#实际分配，根据服务器硬件配置高低，来具体分配weight权重，硬件配置高的，weight就配置高点；
#参考实例：
upstream www.feng.com{
         server 172.17.0.2:8080 weight=5;
         server 172.17.0.3:9090 weight=10;
    }
```

### ip_hash

```sh
# ip_hash策略是根据用户客户端的IP的hash值来分配具体服务器，这样每个访问客户端都会固定访问某一个服务器，这样可以解决session丢失问题，很多网站都采用这种策略来搞负载均衡，主要考虑到session问题；
# 参考配置：
 upstream www.feng.com{
         ip_hash;
         server 172.17.0.2:8080 ;
         server 172.17.0.3:9090 ;
}
```

### least_conn最少连接

```sh
#web请求会被分发到连接数最少的服务器上；
# 参考实例：
 upstream www.feng.com{
         least_conn;
         server 172.17.0.2:8080 ;
         server 172.17.0.3:9090 ;
}
```

## 14. Nginx负载均衡备份和宕机

备份 backup配置：

```sh

# 其他非backup机器挂掉后，才会请求backup机器；
# 案例：
 upstream www.feng.com{
         server 172.17.0.2:8080 ;
         server 172.17.0.3:9090 backup ;
}
```

<font color='red'>宕机down配置：（应用版本更新用）</font>

```sh
# 配置down的服务器不参与负载均衡；
upstream www.feng.com{
         server 172.17.0.2:8080 ;
         server 172.17.0.3:9090 down ;		#测试某台机子时用
}

这两个配置很多时候用于运维，维护某个机器的时候用；
```

# Nginx实现动静分离

## 15. 什么是动静分离？

动静分离是指在web服务器架构中，将静态页面与动态页面或者静态内容接口和动态内容接口分开不同系统访问的架构设计方法，进而提升整个服务访问性能和可维护性。

我们可以静态html页面，css样式，js文件，以及图片文件这些静态放Nginx服务器中，然后把动态请求显示的文件放类似Tomcat这样的容器服务器；

这样方便维护，也可以提高系统性能；

## 16. Nginx实现动静分离

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/_CopyPix_15.png)

我们通过Nginx可以分发请求，实现动态请求转发到Tomcat，静态请求转发到Nginx服务器，来实现动静分离；

 基于前面案例，我们再搞一个Nginx，作为静态资源服务器；原先的Nginx是处理负载均衡和动静分离；

 前面我们tomcat搞过了 就不重复搞了。

我们先搞一个静态资源处理的nginx服务器；

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/_CopyPix_16.png)

server里配置下即可；

 ```sh
 listen    1010;
 server_name img.feng.com;
   
   location / {
 	root /home/nginx ;
 	index index.html index.htm;
   }
 ```

```sh
docker run -it --name=myNginx2 -v /home/data/nginx2:/etc/nginx -v /home/data/nginx2_data:/home/nginx   -p 1010:1010 nginx
```

我们搞个 静态网页以及图片css js文件； 放到 /home/data/nginx2data 然后同步到/home/nginx 上去；

 最终运行：http://192.168.1.112:1010/



<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/_CopyPix_17.png" alt="img" style="zoom:60%;" /> 

有效果的话，说明搭建静态Nginx服务器OK； 

接下来，我们配置动静分离；

```sh
#启动两个tomcat测试

docker run -d --name tomcat1 -p 8080:8080 -v /home/tomcat1/conf/:/usr/local/tomcat/conf/ -v /home/tomcat1/webapps/:/usr/local/tomcat/webapps/  be38ac28efcd

docker run -d --name tomcat2 -p 9090:9090 -v /home/tomcat2/conf/:/usr/local/tomcat/conf/ -v /home/tomcat2/webapps/:/usr/local/tomcat/webapps/  be38ac28efcd
```

重点我们配置Nginx负载均衡和静态分离；

nginx.conf

```sh
# 加一个静态Nginx的 服务地址；
upstream www.feng.com{
	server 172.17.0.4:8080 weight=5;
	server 172.17.0.5:9090 weight=10;
  }

upstream static.feng.com{
	server 172.17.0.3:1010;
  }
```

default.conf

```sh
# server下 加个 ：
location ~ .*\.(gif|jpg|jpeg|png|bmp|swf)$ {
	proxy_pass http://static.feng.com;
  }


location ~ .*\.(js|css)?$ {
	proxy_pass http://static.feng.com;
  }

location ~ .*\.(html|htm)?$ {
	proxy_pass http://static.feng.com;
  }
```

匹配后缀 然后代理转发；

windows下hosts配置：

 ```sh
 192.168.1.112 www.feng.com
 192.168.1.112 static.feng.com
 ```

配置后 保存：

```sh
# 启动Nginx；
docker run -it --name=myNginx -v /home/data/nginx:/etc/nginx  -p 80:80 nginx
```

然后我们可以测试；

http://static.feng.com/ 我们能访问到静态资源；

http://www.feng.com/ 我们可以访问到动态资源；

## 17. 什么是虚拟主机？

虚拟主机概念比较广，可以是虚拟硬件来实现多网站，多应用运行，也可以是通过一些代理服务器来实现单机多网站运行；

例如：我们一个服务器上可以配置三个网站，通过三个域名访问；

Nginx可以通过反向代理来实现虚拟主机；

## 18. Nginx实现虚拟主机

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/_CopyPix_18.png)

我们实现三个域名 www.feng.com blog.feng.com download.feng.com 通过Nginx服务器，反向代理 来实现动态转发到 指定Tomcat服务器；

```sh
# 启动三个tomcat

docker run -d --name tomcat1 -p 8080:8080 -v /home/tomcat1/conf/:/usr/local/tomcat/conf/ -v /home/tomcat1/webapps/:/usr/local/tomcat/webapps/  175550c415d1

docker run -d --name tomcat2 -p 9090:9090 -v /home/tomcat2/conf/:/usr/local/tomcat/conf/ -v /home/tomcat2/webapps/:/usr/local/tomcat/webapps/  175550c415d1

docker run -d --name tomcat3 -p 7070:7070 -v /home/tomcat3/conf/:/usr/local/tomcat/conf/ -v /home/tomcat3/webapps/:/usr/local/tomcat/webapps/  175550c415d1
```

我们分别改下tomcat的server.xml配置的端口，以及webapps下的ROOT下的index.jsp内容，方便查看；

以及通过docker inspect 容器Id 查看下docker内分配的IP

```sh
tomcat1 172.17.0.2 8080
tomcat2 172.17.0.3 9090
tomcat3 172.17.0.4 7070
```

我们配置下Nginx

nginx.conf 加上upstream 

```sh
 upstream www.feng.com{
    server 172.17.0.2:8080;
  }

upstream blog.feng.com{
	server 172.17.0.3:9090;
}

upstream download.feng.com{
		server 172.17.0.4:7070;
}
```

default.conf 加下三个server

```sh
server {
  listen    80;
  server_name www.feng.com;

   location / {
      proxy_pass  http://www.feng.com;
      index index.html index.htm;
    }
}


server {
  listen    80;
  server_name blog.feng.com;

  location / {
    proxy_pass  http://blog.feng.com;
    index index.html index.htm;
  }
}


server {
  listen    80;
  server_name download.feng.com;

  location / {
    proxy_pass  http://download.feng.com;
    index index.html index.htm;
  }
}
```

本机hosts改下

```yaml
192.168.1.112   www.feng.com
192.168.1.112   blog.feng.com
192.168.1.112   download.feng.com
```

保存配置，然后启动nginx;

```sh
docker run -it --name=myNginx -v /home/data/nginx:/etc/nginx  -p 80:80 nginx
```

我们可以通过域名测试 能访问到指定的tomcat;

# Keepalived+Nginx实现高可用集群

## 19. Keepalived简介

keepalived是集群管理中保证集群高可用的一个服务软件，通过keepalived，我们可以实现Nginx集群，实现高可用。

keepalived是以VRRP协议为实现基础的，VRRP全称Virtual Router Redundancy Protocol，即虚拟路由冗余协议。

虚拟路由冗余协议，可以认为是实现路由器高可用的协议，即将N台提供相同功能的路由器组成一个路由器组，这个组里面有一个master和多个backup，master上面有一个对外提供服务的vip（该路由器所在局域网内其他机器的默认路由为该vip）(<font color='red'>VIP  虚拟IP </font>)，master会发组播，当backup收不到vrrp包时就认为master宕掉了，这时就需要根据VRRP的优先级来选举一个backup当master。这样的话就可以保证路由器的高可用了。

keepalived主要有三个模块，分别是core、check和vrrp。core模块为keepalived的核心，负责主进程的启动、维护以及全局配置文件的加载和解析。check负责健康检查，包括常见的各种检查方式。vrrp模块是来实现VRRP协议的

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/_CopyPix_19.png)

## 20. nginx yum命令安装

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

# 输入ip地址有nignx主d
```



## 21. Keepalived安装

```sh
# 安装yum命令
yum install keepalived
 
# keepalived常用命令
systemctl start keepalived.service
systemctl stop keepalived.service
systemctl restart keepalived.service


# keepalived查看日志
tail -f  /var/log/messages
```

```sh
# 目录结构
find / -name keepalived
/etc/sysconfig/keepalived
/etc/selinux/targeted/active/modules/100/keepalived
/etc/keepalived/keepalived.conf		# 配置文件
/usr/sbin/keepalived
/usr/libexec/keepalived

```



## 22. keepalived+Nginx高可用集群配置
## 23. keepalived+Nginx高可用集群测试
## 24. 通过shell脚本优化keepalived+Nginx高可用集群
## 25. keepalived+Nginx+tomcat负载均衡 高可用测试

# Springboot+nginx+tomcat+redis实现session共享

## 26. springboot+nginx+tomcat +redis实现session共享 原理
## 27. docker上安装redis
## 28. springboot2实现redis session存储
## 29. 编写dockerfile实现项目自动运行
## 30. springboot+nginx+tomcat +redis实现session共享 配置和测试