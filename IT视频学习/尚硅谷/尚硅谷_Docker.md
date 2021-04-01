https://www.bilibili.com/video/av27122140?from=search&seid=1368435345787026596

# 01_尚硅谷_Docker_前提知识要求和课程简介 
Docker  
- 语言：go
- 框架：swarm/compose/machine/mesos/k8s/ ci/cd

# 02_尚硅谷_Docker_为什么会出现 
环境打包

# 03_尚硅谷_Docker_理念 
docker   
- build, ship and run any app and anywhere
- www.docker-cn.com

# 04_尚硅谷_Docker_是什么 
3要素
- 仓库
- 镜像
- 容器

# 05_尚硅谷_Docker_能干什么
Dev/Ops(开发自运维)

- www.docker-cn.com
- https://hub.docker.com

# 06_尚硅谷_Docker_三要素 
内核> 2.6.32-431

- 镜像(image)： (java中的类)
- 容器(container)： (java中的实例，可以把容器看成一个简易版的linux环境)
- 仓库(Repository)：是存放镜像文件的场所。
    - 仓库注册服务器(Registry)：Registry存放多个仓库。每个仓库又存放多个镜像。每个镜像有不同的标签tag。


> 仓库有公开库public和私有库Private，最大的公开库https://hub.docker.com

# 07_尚硅谷_Docker_CentOS6安装Docker 

1. 安装运行库:：yum install -y epel-release         
2. 安装docker：yum install -y docker-io           
3. 配置文件：/etc/sysconfig/docker               
4. 启动服务：service docker start                
5. 验证：docker version


# 08_尚硅谷_Docker_CentOS7安装Docker简介（补全请看34集）
https://docs.docker.com/install/linux/docker-ce/centos/

# 09_尚硅谷_Docker_阿里云镜像加速器配置 
阿里云服务：https://promotion.aliyun.com/ntms/act/kubernetes.html  

CENTOS6: /etc/sysconfig/docker文件
```bash
other_args="--registry-mirror=https://1ii98hvz.mirror.aliyuncs.com"

 ps -ef|grep docker  #查看是否生效
root      24740      1  0 20:45 pts/3    00:00:00 /usr/bin/docker -d --registry-mirror=https://1ii98hvz.mirror.aliyuncs.com
root      24811  24135  0 20:46 pts/3    00:00:00 grep docker

```

# 10_尚硅谷_Docker_helloworld镜像 
```bash
docker run hello-world  #先在本地找，没有去阿里云上找
```
# 11_尚硅谷_Docker_运行底层原理 
docker为什么比vm快？
- docker有更少的抽象层。它不需要Hypervisor实现硬件资源虚拟化。
- 不需要客户端的操作系统 

# 12_尚硅谷_Docker_帮助命令 
```bash
docker version
docker info
docker --help
```

# 13_尚硅谷_Docker_镜像命令 
1. 查询本地镜像
```bash
docker images   #列出本地镜像
docker images -a    #a表示all（含中间镜像层）
docker images -q    #只显示id，可以和a一起用
docker images --digests     #显示摘要
docker images --digests --no-trunc  #显示镜像完整信息
```
2. 搜索hub.docker.com镜像
```bash
docker search tomcat                        #从https://hub.docker.com 找镜像
docker search -s 30 tomcat                  #star数超过30
docker search -s 30 --no-trunc tomcat       #有详细说明
                    --automated             #只列出自动构建的镜像
```
3. 拉镜像和删除镜像
```
docker pull tomcat                  #等价于docker pull tomcat:lastest
docker rmi hello-world              #删除某个镜像
docker rmi -f hello-world           #强删
docker rmi -f hello-world nginx     #删除多个(本例中：hello-world, nginx)
docker rmi -f $(docker images -qa)  #删除全部
```

# 14_尚硅谷_Docker_容器命令(上) 
## 1. 运行容器
```bash
docker pull centos      #拉镜像
docker run -it centos
    -i                  #以交互方式运行容器，
    -t                  #为容器分配一个伪输入终端
    --name=mycentos       #指定一个名字，不然会自己分配一个名字
例：docker run -it --name mycentos001 centos    #容器起个名字
```
## 2. 查看运行的容器
```bash
docker ps   #查看运行的所有容器

在容器内部时:
exit        #容器停止退出容器
ctrl+P+Q    #容器不停止退出

docker ps -l                            #上一个容器
docker ps -a                            #all, 以前和当前的所有
docker ps -n 3                          #上3个运行的
docker ps -ql                           #q只显示ID
docker ps --no-trunc                    #不截断显示


```
## 3. 启动，停止，推出，删除容器

```bash
例：docker start mycentos001                    #启动容器+容器I或容器名
例：docker restart mycentos001                  #重启容器
例：docker stop mycentos001                     #停止容器
例：docker kill mycentos001                     #强制停止容器
例：docker rm mycentos001                       #删除容器log, -f强制删除
docker rm -f $(docker ps -a -q)             #一次性删除多个容器  =docker ps -a -q |xargs docker rm
```

# 15_尚硅谷_Docker_容器命令(下) 
## 4. 启动守护式容器，后台式
```bash
docker run -d centos                        #启动守护式容器，后台式  (docker ps没有，因为马上关掉了)
```
- <font color='red'>docker容器后台运行，必须要有个前台进程</font>。
- 如果无top,tail等挂起命令，是会自动退出的。

## 5. 查看日志

```
docker logs                     #查看日志
    -f                          #-f 不停的追加
    -t                          #-t 加时间戳
    --tail                      #容器ID--tail 尾部显示
    
    docker run -d centos /bin/sh -c "while true; do echo hello zzyy; sleep 2; done"     #前台每2秒打印
    
例：docker logs -tf c1d210841f2e                #有时间戳
例：docker logs -tf --tail 3 c1d210841f2e       #只看最后3行
```
##  6. 查看容器内的进程
```
docker top c1d210841f2e         #查看容器内的进程  容器ID
```
##  7. 查看容器内部的细节
```bash
docker inspect c1d210841f2e           #docker inspect 容器ID

```
##  8. 进入正在运行的容器，并以命令行交互
```
docker attach c1d210841f2e                  #进入运行中的容器
docker exec -t 7f9df9f1439b ls -l /tmp      #显示结果，但不进入容器
docker exec -t 7f9df9f1439b /bin/bash       #=docker attach 7f9df9f1439b
```
##  9. 从容器中拷贝文件到主机
```bash
docker cp 7f9df9f1439b:/tmp/yum.log /root       #docker cp 容器ID:文件 主机路径
```

# 16_尚硅谷_Docker_镜像原理 
## 1. 镜像是什么？

 轻量级的独立软件包。用来打包<font color='red'>运行环境</font>和基于环境开发的<font color='red'>软件</font>。

- UnionFS(联合文件系统)
    - <font color='red'>对文件系统的修改作为一次提交来一层层的叠加 </font>
- Docker镜像原理
    - bootfs: bootloader和kernel
    - rootfs: 包含最基本的命令。kernel用宿主机的。所以可以很小200多M。
```
tomcat 为什么这么大？
包含了kernel->centos->jdk8-> tomcat
```
- 分层的镜像
- 为什么Docker采用这种分层结构：<font color='red'>最大的好处是共享资源</font>

## 2. 特点
- docker镜像都是只读的。
- 当容器启动时，一个新的可写层被加载到镜像的顶部。这一层通常被称为“容器层”，“容器层”这下都叫做“镜像层”


# 17_尚硅谷_Docker_镜像commit 
## 3. docker commit操作补充
- docker commit提交容器副本使之成为一个新的镜像。
- docker commit -m="提交的描述信息"  -a="作者" 容器ID要创建的目标镜像名[标签名]
```bash
1. 从hub上下载tomcat并运行
    docker run -it -p 8080:8080 tomcat      #-p 主机端口:docker容器端口
                                            #-P 随机分配端口
                                            #i  交互
                                            #t: 终端
    
2. 故意删除上一步镜像生产tomcat的容器文档
    docker exec -it 8d0fe5c2acca /bin/bash
    rm -rf /webapps/docs
    
3. 当前运行的tomcat是一个没有文档内容的容器。以它为模板commit一个没有doc的tomcat新镜像atguigu/tomcat2
    docker commit -a="zzyy" -m="tomcat without docs" 8d0fe5c2acca atguigu/tomcat01:1.2
    
    docker run -it -p 7777:8080 atguigu/tomcat01:1.2  

4.启动新的镜像并和原来的对比
```
补充
```
docker run -d -p 6666:8080 tomcat
```

# 18_尚硅谷_Docker_容器数据卷介绍