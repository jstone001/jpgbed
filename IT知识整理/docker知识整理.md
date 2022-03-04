

# 安装docker

```sh
# 1. 切换镜像源
$ wget https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -O /etc/yum.repos.d/docker-ce.repo

# 2. 查看当前镜像中支持的docker版本
yum list docker-ce --showduplicates

#3. 安装特定版本的docker-ce 
# 必须指定--setopt=obsoletes-0, 否则yum会自动安装更高版本
$ yum -y install --setopt=obsoletes-0 docker-ce-18.06.3.ce-3.el7

#4. 添加一个配置文件
# docker在默认情况下使用的Cgroup Drive为cgroupfs，而k8s推荐使用systemd来代替cgroups
mkdir /etc/docker
$ cat > /etc/docker/daemon.json << EOF
{
  "exec-opts": ["native.cgroupdriver=systemd"],
  "registry-mirrors": ["https://b9pmyelo.mirror.aliyuncs.com"],
  "graph": "/opt/upload/docker"
}
EOF

#启动docker
$ systemctl enable docker && systemctl start docker
$ docker --version
Docker version 18.06.3-ce, build e68fc7a


```

# daemon.json的配置

```json
{
  "graph": "/data/docker",        //docker的工作目录。会自动生成一些文件夹
  "storage-driver": "overlay2",       //存储驱动。使用overlay2
  "insecure-registries": ["registry.access.r示例edhat.com","quay示例.io"],        //不安全的registries。自己的私有仓库也填在里边
  "registry-mirrors": ["https://q2gr04ke.mirror.aliyuncs.com"],     //镜像加速源。这里是阿里的
  "bip": "172.7.22.1/24",       //docker的网络地址
  "exec-opts": ["native.cgroupdriver=systemd"],
  "live-restore": true
}
————————————————
版权声明：本文为CSDN博主「ultralinux」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/Flag2920/article/details/114377815
```



# 阿里云docker镜像仓库推送时报错:requested access to the resource is denied

from: https://www.cnblogs.com/subendong/p/13766184.html

docker login 的域名要和docker push的镜像一致

# docker tag

from: https://docs.docker.com/engine/reference/commandline/tag/

### Tag an image for a private repository

To push an image to a private registry and not the central Docker registry you must tag it with the registry hostname and port (if needed).

```sh
docker tag 0e5574283393 myregistryhost:5000/fedora/httpd:version1.0
```

# Docker容器中用户权限管理

from: https://www.cnblogs.com/zhouzhifei/p/11557118.html

# Docker内bash 没有vim

```sh
# 进入docker容器
docker exec -it c8e238686f85 /bin/bash

#更新安装源 
apt-get update 
#如果下载过程中卡在[waiting for headers] 删除/var/cache/apt/archives/下的所有文件 
#安装vim 
apt-get install vim
```



# Docker 报错

## ERROR: Failed to Setup IP tables: Unable to enable SKIP DNAT rule:  (iptables failed: iptables --wait -t nat -I DOCKER -i br-2add1a39bc5d -j RETURN: iptables: No chain/target/match by that name.

原因是关闭防火墙之后docker需要重启，执行以下命令重启docker即可：

```sh
service docker restart
```

————————————————
版权声明：本文为CSDN博主「tianshuhao521」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/tianshuhao521/article/details/84782309



## docker启动WARNING：IPv4 forwarding is disabled. Networking will not work. 报错解决办法

https://www.cnblogs.com/nulige/articles/9204841.html

centos 7 docker 启动了一个web服务 但是启动时 报

WARNING: IPv4 forwarding is disabled. Networking will not work.

**#需要做如下配置**

**解决办法：**

```sh
vi /etc/sysctl.conf
net.ipv4.ip_forward=1 #添加这段代码

#重启network服务
systemctl restart network && systemctl restart docker

#查看是否修改成功 （备注：返回1，就是成功）
[root@docker-node2 ~]# sysctl net.ipv4.ip_forward
net.ipv4.ip_forward = 1
```



