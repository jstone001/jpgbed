

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
worker_processes  1;  #配置工程进程数目，根据硬件配置，一般是和CPU数量一致，或者CPU数量的2倍，能达到最佳性能


error_log  /var/log/nginx/error.log warn;  # 配置全局错误日志文件以及配置级别 [ debug | info | notice | warn | error | crit ] 
pid        /var/run/nginx.pid;  #配置进程pid文件


#关于日志级别：
#在配置nginx.conf 的时候，有一项是指定错误日志的，默认情况下你不指定也没有关系，因为nginx很少有错误日志记录的。但有时出现问题时，是有必要记录一下错误日志的，方便我们排查问题。
#error_log 级别分为 debug, info, notice, warn, error, crit  默认为crit, 该级别在日志名后边定义格式如下：
error_log  /your/path/error.log crit;  
#crit 记录的日志最少，而debug记录的日志最多。如果你的nginx遇到一些问题，比如502比较频繁出现，但是看默认的error_log并没有看到有意义的信息，那么就可以调一下错误日志的级别，当你调成error级别时，错误日志记录的内容会更加丰富。
```

## 06. nginx配置文件events配置

## 07. nginx配置文件http配置之基本配置
## 08. nginx配置文件http配置之server配置

# Nginx实现反向代理

## 09. 什么是反向代理？
## 10. Nginx反向代理实例

# Nginx实现负载均衡

## 11. 什么是负载均衡？
## 12. Nginx实现负载均衡
## 13. Nginx负载均衡策略（常用）
## 14. Nginx负载均衡备份和宕机

# Nginx实现动静分离

## 15. 什么是动静分离？
## 16. Nginx实现动静分离
## 17. 什么是虚拟主机？
## 18. Nginx实现虚拟主机

# Keepalived+Nginx实现高可用集群

## 19. Keepalived简介
## 20. nginx yum命令安装
## 21. Keepalived安装
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