

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
  "registry-mirrors": ["https://b9pmyelo.mirror.aliyuncs.com"]
}
EOF

#启动docker
$ systemctl enable docker && systemctl start docker
$ docker --version
Docker version 18.06.3-ce, build e68fc7a


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
