# Docker简介

## 01 Docker是什么 

Docker官网： https://www.docker.com/

- Docker 是一个开源的应用容器引擎，基于 Go 语言 并遵从Apache2.0协议开源。
- Docker 可以让开发者打包他们的应用以及依赖包到一个轻量级、可移植的容器中，然后发布到任何流行的 Linux 机器上，也可以实现虚拟化。
- 容器是完全使用沙箱机制，相互之间不会有任何接口（类似 iPhone 的 app）,更重要的是容器性能开销极低。
- Docker 从 17.03 版本之后分为 CE（Community Edition: 社区版） 和 EE（Enterprise Edition: 企业版），我们用社区版就可以了。

## 02 Docker架构原理 

![image-20210519095155767](E:\JS\booknote\jpgBed\image-20210519095155767.png)

Docker三要素，镜像，容器，仓库

1. **镜像**

Docker 镜像（Image）就是一个只读的模板，它可以是一个可运行软件（tomcat，mysql），也可以是一个系统（centos）。镜像可以用来创建 Docker 容器，一个镜像可以创建很多容器。

2. **容器**

Docker 利用容器（Container）独立运行的一个或一组应用。容器是用镜像创建的运行实例。它可以被启动、开始、停止、删除。每个容器都是相互隔离的、保证安全的平台。可以把容器看做是一个简易版的 Linux 环境（包括root用户权限、进程空间、用户空间和网络空间等）和运行在其中的应用程序。容器的定义和镜像几乎一模一样，也是一堆层的统一视角，唯一区别在于容器的最上面那一层是可读可写的。

3. **仓库**

仓库（Repository）是集中存放镜像文件的场所，类似GitHub存放项目代码一样，只不过Docker Hub是由来存镜像（image）的。仓库(Repository)和仓库注册服务器（Registry）是有区别的。仓库注册服务器上往往存放着多个仓库，每个仓库中又包含了多个镜像，每个镜像有不同的标签（tag，类似版本号）。

仓库分为公开仓库（Public）和私有仓库（Private）两种形式。

最大的公开仓库是 Docker Hub(https://hub.docker.com/)，存放了数量庞大的镜像供用户下载。国内的公开仓库包括阿里云、网易云等。



**容器与镜像的关系类似于面向对象编程中的对象与类。**

| Docker | 面向对象 |
| ------ | :------- |
| 容器   | 对象     |
| 镜像   | 类       |

## 03 Docker有什么用 

1. 简化环境搭建，提高开发生命周期效率；
2. 大大简化运维工作量；
3. 微服务利器；

## 04 Docker容器与虚拟机区别 

Docker是一种轻量级的虚拟化技术，比传统的虚拟机性能更好。

### 虚拟机的体系结构

![img](E:\JS\booknote\jpgBed\B83BF69E-842F-4F07-8ADF-AB452BA58A35.png)

- server - 表示真实电脑。
- Host OS - 真实电脑的操作系统，例如：Windows，Linux
- Hypervisor - 虚拟机平台，模拟硬件，如VMWare，VirtualBox
- Guest OS - 虚拟机平台上安装的操作系统，例如CentOS Linux
- App - 虚拟机操作系统上的应用，例如nginx

### Docker的体系结构

![img](E:\JS\booknote\jpgBed\DC0DCC52-57E5-4374-800D-677C92269E90.png)

- server - 表示真实电脑。
- Host OS - 真实电脑的操作系统，例如：Windows，Linux
- Docker Engine - 新一代虚拟化技术，不需要包含单独的操作系统。
- App - 所有的应用程序现在都作为Docker容器运行。

这种体系结构的明显优势是，不需要为虚拟机操作系统提供硬件模拟。所有应用程序都作为Docker容器工作，性能更好。

# Docker安装

## 05 Docker版本介绍 

- Docker从1.13版本之后采用时间线的方式作为版本号，分为社区版CE和企业版EE。
- 社区版是免费提供给个人开发者和小型团体使用的，企业版会提供额外的收费服务，比如经过官方测试认证过的基础设施、容器、插件等。
- 社区版按照stable和edge两种方式发布，每个季度更新stable版本，如17.06，17.09；每个月份更新edge版本，如17.09，17.10。
- 我们平时用社区版就足够了。所以我们安装社区版；

## 06 Docker安装官方文档 

我们主要参考：https://docs.docker.com/install/linux/docker-ce/centos/ 来安装

## 07 工具准备 

- 前置课程：Centos课程 http://www.java1234.com/javaxuexiluxiantu.html
- 打包下载： http://pan.baidu.com/s/1i55jJAt
- 虚拟机 VMware
- centos7安装下虚拟机VM上；
- 连接工具 才用 FinalShell 官方地址：http://www.hostbuf.com/

## 08 Docker安装步骤 

```sh
# 切换到root用户
# Docker 要求 CentOS 系统的内核版本高于 3.10 ，查看本页面的前提条件来验证你的CentOS 版本是否支持 Docker 。
uname -r 	# 通过 uname -r 命令查看你当前的内核版本

# 使用 root 权限登录 Centos。确保 yum 包更新到最新。
 yum update
 
 # 卸载旧版本(如果安装过旧版本的话)
 yum remove docker  docker-common docker-selinux docker-engine
 
 #安装需要的软件包， yum-util 提供yum-config-manager功能，另外两个是devicemapper驱动依赖的
  yum install -y yum-utils device-mapper-persistent-data lvm2
  
 # 设置yum源
  yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
  
  # 安装最新版本的Docker
  yum install docker-ce docker-ce-cli containerd.io
  
 # 启动Docker并设置开机启动
 systemctl start docker
 systemctl enable docker
   
 # 验证Docker
 docker version
 Client:
 Version:           18.06.3-ce
 API version:       1.38
 Go version:        go1.10.3
 Git commit:        d7080c1
 Built:             Wed Feb 20 02:26:51 2019
 OS/Arch:           linux/amd64
 Experimental:      false
  
# Docker HelloWorld测试
docker run hello-world
latest: Pulling from library/hello-world
b8dfde127a29: Pull complete 
Digest: sha256:5122f6204b6a3596e048758cabba3c46b1c937a46b5be6225b835d091b90e46c
Status: Downloaded newer image for hello-world:latest

Hello from Docker!
This message shows that your installation appears to be working correctly.

```



## 09 HelloWorld运行原理解析 



![img](E:\JS\booknote\jpgBed\94936A0A-B9C5-44CF-BCE0-19F52F80EF3E.png)

运行 docker run hello-world

本地仓库未能找到该镜像，然后去远程仓库寻找以及下载该镜像；

然后我们再执行该命令：

![img](E:\JS\booknote\jpgBed\B3E35C50-23AF-4803-8F91-8BD97F3AD9E3.png)

出来了 Helloworld。我们具体来分析下 执行原理和过程；

![image-20210519103030190](E:\JS\booknote\jpgBed\image-20210519103030190.png)

从左到右 client客户端，Docker运行主机，远程仓库；

docker build ,pull，run分别是 构建，拉取，运行命令，后面再细讲；

中间Docker主机里有 Docker daemon主运行线程，以及Containers容器，容器里可以运行很多实例，（实例是从右侧Images镜像实例化出来的）Images是存储再本地的镜像文件，比如 Redis，Tomat这些镜像文件；

右侧是Registry镜像仓库，默认远程镜像仓库 https://hub.docker.com/ 不过是国外主机，下载很慢，不稳定，所以我们后面要配置成阿里云仓库镜像地址，稳定快捷；

执行 docker run hello-world的过程看如下图例：

![img](E:\JS\booknote\jpgBed\4A210FC2-C9CD-4433-95BA-E95134E72FA5.png)

## 10 Docker配置阿里云镜像仓库 

Docker默认远程仓库是 https://hub.docker.com/

比如我们下载一个大点的东西，龟速

![img](E:\JS\booknote\jpgBed\7BE93DBA-3361-46A9-99A3-14E9B934E886.png)

由于是国外主机，类似Maven仓库，慢得一腿，经常延迟，破损；

所以我们一般都是配置国内镜像，比如阿里云，网易云等；推荐阿里云，稳定点；

### **配置步骤如下**

1. 登录进入阿里云镜像服务中心，获取镜像地址： https://developer.com/mirror/

进入阿里云容器镜像服务地址：点这里快速进入

使用你的淘宝账号密码登录

![img](E:\JS\booknote\jpgBed\E1993895-C7A1-443D-AE17-0EA025FCB93E.png)

这里我们获取镜像地址；

2. 在/etc/docker目录下找到在daemon.json文件（没有就新建），将下面内容写入

```sh
{
 "registry-mirrors": ["https://xxxxxxx.mirror.aliyuncs.com"]
}
```

3. 重启daemon

```sh
systemctl daemon-reload
```

4. 重启docker服务
```sh
systemctl restart docker
```

5. 测试

```sh
docker info
Registry Mirrors:
 https://b9pmyelo.mirror.aliyuncs.com/
Live Restore Enabled: false

```

   ![img](E:\JS\booknote\jpgBed\4B14D1AB-9C6E-4071-8DE6-A34397E36A30.png)



# Docker命令

## 11 Docker基本命令 

```sh
systemctl start docker		#启动Docker
systemctl stop docker		#停止Docker
systemctl restart docker	#重启Docker
systemctl enable docker		#开机启动Docker
docker info					#查看Docker概要信息
docker --help				#查看Docker帮助文档
docker version				#查看Docker版本信息
```

### docker --help

```sh
#1、查看 docker help 帮助
docker help


#2、用法
docker [选项] 命令


#3、选项
-- 客户端配置文件的配置字符串位置(默认为“/root/.docker”)
-D,    --启用调试模式
-H,    --要连接的主机列表守护进程套接字
-l,     --设置日志级别的字符串
             (“调试”|“信息”|“警告”|“错误”|“致命”)(默认“信息”)
    --tls                #使用tls;暗示了--tlsverify
    --tlscacert string   #仅由此CA签名的信任证书(默认为“/root/.docker/ CA .pem”)
    --tlscert string     #TLS证书文件的路径(默认为“/root/.docker/cert.pem”)
    --tlskey string      #TLS密钥文件路径(默认为“/root/.docker/key.pem”)
    --tlsverify          #使用TLS并验证远程
-v, --version            #打印版本信息并退出


#4、管理命令
  builder     #管理构建
  config      #码头工人管理配置
  container   #管理容器
  engine      #管理docker引擎
  image       #管理图像
  network     #管理网络
  node        #管理群节点
  plugin      #管理插件
  secret      #管理码头工人的秘密
  service     #管理服务
  stack       #管理码头工人栈
  swarm       #管理群
  system      #管理码头工人
  trust       #管理Docker映像上的信任
  volume      #管理卷

# 5、命令
  attach      # 将本地标准输入、输出和错误流附加到正在运行的容器中
  build       # 从Dockerfile构建一个映像
  commit      # 从容器的更改中创建一个新映像
  cp          # 在容器和本地文件系统之间复制文件/文件夹
  create      # 创建一个新容器
  diff        # 检查容器文件系统上文件或目录的更改
  events      # 从服务器获取实时事件
  exec        # 在正在运行的容器中运行命令
  export      # 将容器的文件系统导出为tar存档文件
  history     # 显示图像的历史
  images      # 图片列表
  import      # 从tarball导入内容以创建文件系统映像
  info        # 显示整个系统的信息
  inspect     # 返回Docker对象的底层信息
  kill        # 杀死一个或多个正在运行的容器
  load        # 从tar存档或STDIN加载图像
  login       # 登录到Docker注册表
  logout      # 从Docker注册表注销
  logs        # 获取容器的日志
  pause       # 暂停一个或多个容器中的所有进程
  port        # 列出容器的端口映射或特定映射
  ps          # 列表容器
  pull        # 从注册表中提取映像或存储库
  push        # 将映像或存储库推入注册表
  rename      # 重命名一个容器
  restart     # 重新启动一个或多个容器
  rm          # 移除一个或多个容器
  rmi         # 删除一个或多个图像
  run         # 在新容器中运行命令
  save        # 将一个或多个图像保存到tar存档文件(默认情况下流到STDOUT)
  search      # 在Docker集线器中搜索图像
  start       # 启动一个或多个停止的容器
  stats       # 显示容器资源使用统计数据的实时流
  stop        # 停止一个或多个正在运行的容器
  tag         # 创建一个引用SOURCE_IMAGE的标记TARGET_IMAGE
  top         # 显示容器的运行进程
  unpause     # 在一个或多个容器中暂停所有进程
  update      # 更新一个或多个容器的配置
  version     # 显示Docker版本信息
  wait        # 阻塞，直到一个或多个容器停止，然后打印它们的退出代码


```



## 12 Docker镜像操作命令 

### 列出本机所有镜像

```sh
docker images # 列出本机所有镜像

REPOSITORY  #镜像的仓库源
TAG         #镜像的标签（版本）同一个仓库有多个TAG的镜像，多个版本；我们用REPOSITORY:TAG来定义不同的镜像；
IMAGE ID    # 镜像ID，镜像的唯一标识
CREATE      # 镜像创建时间
SIZE        # 镜像大小

# OPTIONS 可选参数：
-a          #显示所有镜像（包括中间层）
-q          #只显示镜像ID
-qa         #可以组合
--digests   #显示镜像的摘要信息
--no-trunc  #显示完整的镜像信息 
```

### 搜索镜像

```sh
docker search # 搜索镜像

#和 https://hub.docker.com/ 这里的搜索效果一样；
#OPTIONS可选参数：
--no-trunc			# 显示完整的镜像描述
-s					# 列出收藏数不小于指定值的镜像
--automated			# 只列出Docker Hub自动构建类型的镜像
```

### 下载镜像

```sh
docker pull # 下载镜像
docker pull 镜像名称:[TAG]
# 注意：不加TAG，默认下载最新版本latest
```

### 删除镜像

```sh
docker rmi 	# 删除镜像

1，删除单个：docker rmi 镜像名称:[TAG]
#如果不写TAG，默认删除最新版本latest
#有镜像生成的容器再运行时候，会报错，删除失败；
￼
# 我们需要加 -f 强制删除
2，删除多个：docker rmi -f 镜像名称1:[TAG] 镜像名称2:[TAG]
# 中间空格隔开

3，删除全部：docker rmi -f $(docker images -qa)
```

## 13 Docker容器操作基本命令 

### 创建并启动容器

```sh
docker run [OPTIONS] IMAGE [COMMAND] [ARG...]
--name="容器新名字"：为容器指定一个名称；
-i：以交互模式运行容器，通常与-t或者-d同时使用；
-t：为容器重新分配一个伪输入终端，通常与-i同时使用；
-d: 后台运行容器，并返回容器ID；
-P: 随机端口映射，容器内部端口随机映射到主机的端口
-p: 指定端口映射，格式为：主机(宿主)端口:容器端口

docker run -it mycentos002 0f3e07c0138f

docker run --name 别名 镜像ID  	#  启动普通容器 
docker run -it --name 别名 镜像ID   # 启动交互式容器：  来运行一个容器，取别名，交互模式运行，以及分配一个伪终端
docker run -di --name 别名 镜像ID	# 守护式方式创建并启动容器，（没有进入容器）

 docker run -it --name 别名 镜像ID  /bin/bash	# 启动容器，并执行/bin/bash命令；
 docker run -it -p 8888:8080 tomcat		# 端口映射
```

### 列出容器

```sh
docker ps [OPTIONS]
OPTIONS说明：
-a :显示所有的容器，包括未运行的。
-f :根据条件过滤显示的内容。
--format :指定返回值的模板文件。
-l :显示最近创建的容器。
-n :列出最近创建的n个容器。
--no-trunc :不截断输出。
-q :静默模式，只显示容器编号。
-s :显示总的文件大小。

docker ps #查看正在运行的容器
docker ps -a #查看所有容器
docker ps -n 2  #显示最近创建的2个容器
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                                       
3682a075f09c        70f311871ae1        "/coredns -conf /etc…"   2 minutes ago       Exited (0) 47 seconds ago                           
a6a12f5371b8        70f311871ae1        "/coredns -conf /etc…"   3 minutes ago       Exited (0) About a minute ago                       

docker ps -f status=exited #查看停止的容器
```

### 退出容器

```sh
exit或者Ctrl+P+Q
exit # 容器停止退出
ctrl+P+Q #容器不停止退出
```

### 进入容器

```sh
docker attach 容器ID or 容器名 
```

### 启动容器

```sh
docker start 容器ID or 容器名
```

### 重启容器

```sh
docker restart 容器ID or 容器名
```

### 停止容器

```sh
docker stop 容器ID or 容器名

# 暴力删除，直接杀掉进程 （不推荐）
docker kill 容器ID or 容器名
```

### 删除容器

```sh
docker rm 容器ID  
￼
#如果删除正在运行的容器，会报错，我们假如需要删除的话，需要强制删除；
#强制删除docker rm -f 容器ID

#删除多个容器 
docker rm -f 容器ID1  容器ID2 中间空格隔开
#删除所有容器
docker rm -f $(docker ps -qa)
```



## 14 Docker容器操作进阶命令 

### 守护式方式创建并启动容器

```sh
docker run -di --name 别名 镜像ID	# 守护式方式创建并启动容器，（没有进入容器）
```

### 进入容器执行命令

```sh
docker exec -it 容器名称 或者 容器ID 执行命令
#直接操作容器，执行完 回到 宿主主机终端；
#我们一般用于 启动容器里的应用 比如 tomcat nginx redis elasticsearch等等
# 例
docker exec -it f6fffc051d64 ls -l /sys		# 结束命令后，又退出了容器
```

### 查看容器的日志

```sh
$ docker logs [OPTIONS] CONTAINER
  Options:
        --details        显示更多的信息
    -f, --follow         跟踪实时日志
        --since string   显示自某个timestamp之后的日志，或相对时间，如42m（即42分钟）
        --tail string    从日志末尾显示多少行日志， 默认是all
    -t, --timestamps     显示时间戳
        --until string   显示自某个timestamp之前的日志，或相对时间，如42m（即42分钟）
（以上了解）

```

锋哥推荐，简单粗糙方式，直接去docker容器文件里找；

具体未知：/var/lib/docker/containers/

![img](E:\JS\booknote\jpgBed\_CopyPix_14.png)



每个容器对应一堆文件，然后有个log结尾的，就是日志文件；

我们打开；

很直观 假如时间长了 日志文件很大，直接自己操刀处理即可；

### 查看容器进程

```sh
docker top 容器ID

UID                 PID                 PPID                C                   STIME               TTY                 TIME                CMD
root                45110               115120              0                   May18               ?                   00:00:00            /bin/bash
root                91562               115120              0                   May18               ?                   00:00:00            /bin/bash
root                115140              115120              0                   May18               pts/0               00:00:00            nginx: master process nginx -g daemon off;
101                 115218              115140              0                   May18               pts/0               00:00:00            nginx: worker process

```

### 查看容器元信息  inspect

```sh
docker inspect 容器ID
```



### 宿主机和容器之间文件拷贝

```sh
# 1. 宿主机文件 copy to 容器内
docker cp 需要拷贝的文件或者目录   容器名称：容器目录
# 例
docker cp /home/dog.jpg f6fffc051d64:/tmp
docker cp /home/dog.jpg f6fffc051d64:/tmp/dogg.jpg

# 2.容器内 copy to 宿主机
docker cp 容器名称：容器目录    宿主机目录
docker cp f6fffc051d64:/tmp/dog.jpg /home/data
docker cp f6fffc051d64:/tmp/animal /home/data

```

## 15 Docker容器宿主机端口映射 

```sh
 docker run -it -p 8888:8080 tomcat		# 端口映射
```

# 与远程docker库交互

## 16 Docker commit提交运行时容器成为镜像 

```sh
docker commit -a='java1234' -m='no logo tomcat' 运行时的容器_id java1234/tomcat7:1.1  
# -a  作者
# -m  备注
# java1234/tomcat7:1.1  新镜像名称
```

## 17 推送镜像到hub服务器 

我们可以通过docker push命令 把自己本地定制的镜像推送到Hub服务器，方便全球开发者使用，包括自己；

上一讲，我们定制了一个镜像 java1234/tomcat7 tag是1.1

我们把这个镜像发布到hub服务器；

**步骤一：**

https://hub.docker.com/ 注册下 得到docker id和密码

**步骤二：**

我们用docker login登陆hub服务器

![img](E:\JS\booknote\jpgBed\4CB02AD0-543C-4421-B5E9-F801FA832FA5.png)



**步骤三：**

docker push推送

docker push java1234/tomcat7:1.1

![img](E:\JS\booknote\jpgBed\95082C76-B6DE-4854-A01C-37C3B6AB5A34.png)

推送成功：

登陆 https://hub.docker.com/  点击 Repositories 菜单

![img](E:\JS\booknote\jpgBed\62003531-876B-44A5-AFFF-7EC86C0C6B4E.png)

已经显示这个镜像；

点击：

![img](E:\JS\booknote\jpgBed\FCD87B32-BB7F-4206-AA34-07E0B2E56B80.png)

我们加简介和描述信息；

点Tags:

![img](E:\JS\booknote\jpgBed\C921E230-CC4D-439C-BEE3-76166C6E5154.png)

我们可以删除掉；

## 18 推送镜像到阿里云 

很多时候，中小公司为了方便搭建私有仓库方便，直接使用稳定的阿里云镜像仓库，方便公司内部业务系统直接拉取镜像；

**步骤一：**

进入：https://cr.console.aliyun.com 阿里云镜像控制台 需要注册 用户名就是你的淘宝或者支付宝 账号名称 ，镜像控制台密码单独设置；

**步骤二：**

进入控制台，我们先创建命名空间，再创建镜像；

![img](E:\JS\booknote\jpgBed\1E7313EE-BD92-4E83-85B8-7F80B63A7CC8.png)

然后我们可以根据阿里云官方提示说明来进行镜像远程登录，提交，以及拉取操作，简单易用；

<font color='red'>tag, push代码阿里云上有</font>

```sh
2. 从Registry中拉取镜像
sudo docker pull registry.cn-aliyuncs.com/java1234/tomcat:[镜像版本号]

3. 将镜像推送到Registry
sudo dockeer login --username=hi50030383@aliyun.com registry.cn-hangzhou.aliyun.com
sudo docker tag [ImageId] registry.cn-hangzhou.aliyuncs.com/java1234/tomcat:[镜像版本号]
sudo docker push registry.cn-hangzhou.aliyuncs.com/java1234/tomcat:[镜像版本号]
```

# 容器目录挂载 

## 19 Docker容器目录挂载 

### 简介

容器目录挂载：我们可以在创建容器的时候，将宿主机的目录与容器内的目录进行映射，这样我们就可以实现宿主机和容器目录的双向数据自动同步；

### 作用

前面学过cp命令来实现数据传递，这种方式比较麻烦；

我们通过容器目录挂载，能够轻松实现代码上传，配置修改，日志同步等需求；

### 实现

```sh
# 语法：
docker run -it -v  /宿主机目录:/容器目录 镜像名
# 多目录挂载
docker run -it -v /宿主机目录:/容器目录 -v /宿主机目录2:/容器目录2  镜像名

#注意：
#如果你同步的是多级目录，可能会出现权限不足的提示；
#这是因为Centos7中的安全模块selinux把权限禁掉了，我们需要添加  --privileged=true 来解决挂载的目录没有权限的问题；
```

### 挂载目录只读

```sh
docker run -it -v  /宿主机目录:/容器目录:ro 镜像名
```



# Docker常用软件安装

## 20 简单web测试项目准备 

- 我们搞一个连接数据库操作的小项目；
- 在docker搞一个tomcat容器，以及mysql容器，
- 我们把这个小项目运行出来，让大家体验下docker容器的魅力；

## 21 在docker上安装tomcat7 和配置 

第一步：运行容器

第二步：宿主机里home目录下新建tomcat目录，复制容器里conf,webapps到宿主机

```sh
docker cp 容器id:/usr/local/tomcat/conf /home/tomcat/
docker cp 容器id::/usr/local/tomcat/webapps /home/tomcat/
```

第三步：把容器里的tomcat里的webapp，logs，conf挂载到宿主机tomcat目录下，方便上传代码，同步持久化日志，以及方便配置tomcat；关掉容器，启动容器；

```sh
docker run -d --name 容器名称 -p 80:8080 -v /home/tomcat/conf/:/usr/local/tomcat/conf/ -v /home/tomcat/webapps/:/usr/local/tomcat/webapps/ -v /home/tomcat/logs/:/usr/local/tomcat/logs/  镜像名称
```

第四步：配置tomcat server.xml 以及 同步上传war包

## 22 在docker上安装mysql5.7和配置 

第一步：运行容器

 第二步：宿主机里home目录下新建mysql目录，复制容器里conf,webapps到宿主机

```sh
docker cp 容器id:/etc/mysql/conf.d /home/mysql/
docker cp 容器id:/var/log /home/mysql/
docker cp 容器id:/var/lib/mysql /home/mysql/ 
```

第三步：把容器里的tomcat里的webapp，logs，conf挂载到宿主机tomcat目录下，方便上传代码，同步持久化日志，以及方便配置tomcat；关掉容器，启动容器；

```sh
docker run -p 3306:3306 -d -v /etc/mysql/conf.d/:/home/mysql/conf/ -v /var/log:/home/mysql/log/ -v /var/lib/mysql/:/home/mysql/mysql/ -e MYSQL_ROOT_PASSWORD=123456 镜像ID
```

第四步：用sqlyog连接docker里的数据库，导入sql脚本 

<font color='red'>注意点：运行的项目里的数据库连接地址，要写docker里的mysql容器所在的虚拟IP地址；容器间通信IP；</font>

## 23 Docker迁移与备份 

### 概述

我们开发的时候，经常自定义镜像，然后commit提交成镜像到本地仓库，但是我们发布到客户服务器的时候，可以用前面讲得搞到hub官方，或者阿里云，但是有些机密性的项目，是禁止公网存储的，所以我们只能通过docker镜像备份和迁移实现；

### 实现

备份镜像：

```sh
docker save -o 备份镜像的名称 源镜像名称:tag版本
docker save -o mytomcat7.1.tar java1234/tomcat7:7.1
```

![img](E:\JS\booknote\jpgBed\A0332BBC-95A8-4FFC-B441-1D8F2F4FAD1B.png)



恢复镜像：

```sh
docker load -i 镜像文件
docker load -i mytomcat7.1.tar
```

![img](E:\JS\booknote\jpgBed\C8026D64-B6A8-4C17-9A33-16EC55C76549.png)

# DockerFile

## 24 DockerFile简介 

- Dockerfile是由一系列命令和参数构成的脚本，这些命令应用于操作系统(centos或者Ubuntu)基础镜像并最终创建的一个新镜像；
- 我们前面讲过的用手工的方式，修改配置文件，或者添加，删除文件目录的方式，来构建一种新镜像；这种手工方式麻烦，容易出错，而且不能复用；
- 我们这里讲Dockerfile，用脚本方式来构建自动化，可复用的，高效率的创建镜像方式，是企业级开发的首选方式；

再软件系统开发生命周期中，采用Dockerfile来构建镜像；

1. 对于开发人员:可以为开发团队提供一个完全一致的开发环境;
2. 对于测试人员:可以直接拿开发时所构建的镜像或者通过Dockerfile文件构建一个新的镜像开始工作；
3. 对于运维人员:在部署时，可以实现应用的无缝移植。

## 25 DockerFile常用指令 
## 26 DockerFile构建自定义centos 
## 27 DockerFile构建自定义Tomcat 
## 28 DockerFile通过VOLUME实现容器卷 
## 29 DockerFile之CMD, ENTRYPOINT 的区别和联系 
## 30 DockerFile之ONBUILD

# Docker私有仓库

## 31 Docker私有仓库

### 简介

Docker私有仓库主要是企业内部用来存放镜像的仓库，相对官方仓库以及阿里云仓库，具有更高的保密安全级别；

### 搭建

第一步：拉取私有仓库镜像 （私有仓库程序本身就是一个镜像）

```sh
docker pull registry
```

第二步：启动私有仓库容器

```sh
docker run -di --name=myRegistry -p 5000:5000 registry
```

第三步：测试

http://192.168.1.112:5000/v2/_catalog

![img](E:\JS\booknote\jpgBed\20FF3ED8-D264-4BAA-9045-F18034CED587.png)

看到这个 说明启动OK。因为仓库里还没有镜像，所以就是空的；

第四步：etc/docker 修改daemon.json，让docker信任私有仓库地址

"insecure-registries": ["192.168.1.112:5000"]

![img](E:\JS\booknote\jpgBed\0E57691A-0CC5-46AB-AC92-0E2BEF211034.png)

第五步：修改配置后重启docker；

```sh
 systemctl restart docker
```

### 测试

第一步：标记此镜像为私有仓库的镜像

```sh
docker tag tomcat:7 192.168.1.112:5000/mytomcat7
```

第二步：上传镜像到私有仓库

```sh
docker push 192.168.1.112:5000/mytomcat7
```

![img](E:\JS\booknote\jpgBed\867315BC-2C5F-44A5-8478-F2EA04CAE8B6.png)

此时私有仓库里已经有了这个镜像；

第三步：删除192.168.1.112:5000/mytomcat7本地仓库镜像

```sh
docker rmi -f 192.168.1.112:5000/mytomcat7
```

第四步：从私有仓库拉取192.168.1.112:5000/mytomcat7镜像，并运行；

```sh
docker run -it -p 8080:8080 192.168.1.112:5000/mytomcat7
```

第五步：浏览器运行 http://192.168.1.112:8080 测试

<img src="E:\JS\booknote\jpgBed\15211790-11DB-4666-A98F-A26E566DB951.png" alt="img" style="zoom: 80%;" />

