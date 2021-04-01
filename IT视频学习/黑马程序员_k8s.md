from: https://www.bilibili.com/video/BV1Qv41167ck

​		http://yun.itheima.com/course/640.html

# 第1章 k8s介绍

# 第2章 集群环境搭建

## P5-环境搭建--环境规划

### 集群类型：一主多从，多主多从

### 安装方式：kubeadm, minikube, 二进制包

​	minikube: 一个用于快速搭建单节点k8s的工具

​	kubeadm: 一个用于快速搭建k8s集群的工具

​	二进制包: 官网下载每个组件的2进制包

### 主机规划

| 作用   | IP地址          | 操作系统  | 配置                  |
| ------ | --------------- | --------- | --------------------- |
| master | 192.168.109.101 | centos7.5 | 2颗CPU 2g内存 50g硬盘 |
| node1  | 192.168.109.102 | centos7.5 | 2颗CPU 2g内存 50g硬盘 |
| node2  | 192.168.109.103 | centos7.5 | 2颗CPU 2g内存 50g硬盘 |

## P6-环境搭建--主机安装

docker: 18.06.3

kubeadm,kubelet, kubectl: 1.17.4

## P7-环境搭建--环境初始化

必须CentOS7.5 以上

```sh
# 1. 查看主机版本
cat /etc/redhat-release 

#2. 主机名解析 /etc/hosts
192.168.132.31  m1
192.168.132.32  n1
192.168.132.33  n2

#3. 时间同步
#3.1 启动chronyd服务
systemctl start chronyd
#3.2 设置chronyd服务开机自启
systemctl enable chronyd
#3.3 chronyd服务启动稍等几秒钟，就可以使用date了
date

#4. 禁用iptables和firewalld服务
#关闭firewalld 服务
systemctl stop firewalld
systemctl disable firewalld

#关闭iptables服务
systemctl stop iptables
systemctl disable iptables

#5. 禁用selinux
# 编辑/etc/selinux/config
SELINUX=disabled

setenforce 0  #临时
getenforce  #查看状态

# 6. 禁用swap
# 编辑分区配置文件/etc/fstab, 注释到swap分区一行
# 注释修改完之后要重启linux
swapoff -a  # 临时
sed -ri 's/.*swap.*/#&/' /etc/fstab    # 永久
free -m  #查询

# 7.将桥接的IPv4流量传递到iptables的链
cat > /etc/sysctl.d/k8s.conf << EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
sysctl -p  # 重新加载模块
#sysctl --system  # 生效
# 加载网桥过滤模块
modprobe br_netfilter
# 查看网桥过滤模块是否加载成功
lsmod | grep br_netfilter
br_netfilter           22256  0 
bridge                151336  1 br_netfilter

# 8. 配置ipvs功能
# 8.1 安装ipset和ipvsadm
yum install ipset ipvsadm -y

#8.2 添加加载模块的脚本文件
cat >> /etc/sysconfig/modules/ipvs.modules << EOF
#!/bin/bash
modprobe -- ip_vs
modprobe -- ip_vs_rr
modprobe -- ip_vs_wrr
modprobe -- ip_vs_sh
modprobe -- nf_conntrack_ipv4
EOF

#8.3 为脚本文件添加执行权限
chmod +x /etc/sysconfig/modules/ipvs.modules
#8.4 执行脚本文件
/bin/bash /etc/sysconfig/modules/ipvs.modules
#8.5 查看对应的模块是否加载成功
lsmod | grep -e ip_vs -e nf_conntrack_ipv4

# 重启linux
reboot
```



## P8-环境搭建--集群所需组件安装

安装docker: 18.06.3， kubeadm,kubelet, kubectl: 1.17.4

### 安装docker

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

### 安装kubernetes组件

```bash
# 1. 切换阿里云镜像
$ cat > /etc/yum.repos.d/kubernetes.repo << EOF
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF


# 2. 安装Kubenet组件
$ yum install -y --setopt=obsoletes-0 kubelet-1.17.4-0 kubeadm-1.17.4-0 kubectl-1.17.4-0

# 3. 配置kubelet的cgroup
# 编辑/etc/sysconfig/kubelet, 添加下面的配置
cat >> /etc/sysconfig/kubelet <<EOF
KUBELET_CGROUP_ARGS="--cgroup-driver=systemd"
KUBE_PROXY_MODE="ipvs"
EOF

# 4. 开机启动
$ systemctl enable kubelet    
```

## P9-环境搭建--集群安装

### 集群初始化

```bash
# 准备集群镜像
images=(
    kube-apiserver:v1.17.4
    kube-controller-manager:v1.17.4
    kube-scheduler:v1.17.4
    kube-proxy:v1.17.4
    pause:3.1
    etcd:3.4.3-0
    coredns:1.6.5
)

for imageName in ${images[@]} ; do
	docker pull registry.cn-hangzhou.aliyuncs.com/google_containers/$imageName
	docker tag registry.cn-hangzhou.aliyuncs.com/google_containers/$imageName k8s.gcr.io/$imageName
	docker rmi registry.cn-hangzhou.aliyuncs.com/google_containers/$imageName
done

# 查看docker images
docker images

```

### master下

```sh
# master下创建集群
kubeadm init  --apiserver-advertise-address=192.168.132.31  --kubernetes-version v1.17.4 --service-cidr=10.96.0.0/12 --pod-network-cidr=10.244.0.0/16
# 由于默认拉取镜像地址 k8s.gcr.io 国内无法访问，这里指定阿里云镜像仓库地址。
# 会生成token值

# 创建必要文件
mkdir -p $HOME/.kube 
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config 
sudo chown $(id -u):$(id -g) $HOME/.kube/config 
$ kubectl get nodes
```

### node下

```sh
kubeadm join 192.168.132.31:6443 --token vxthmk.y58aankfjbuflzi4 \
    --discovery-token-ca-cert-hash sha256:e176a6c8f39fd190f515617c4be04e653b2ff15b70d7f1dd82d9645adea13fa4
```



## P10-环境搭建--网络插件安装

### master下

```sh
# 获取fannel的配置文件
wget https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml

# 修改文件中quay.io仓库为quay-mirror.qiniu.com （现在不用这样做了）
sed -i 's#quay.io#quay-mirror.qiniu.com#g' kube-flannel.yml

# 使用配置文件启动fannel
kubectl apply -f kube-flannel.yml

# 稍等片刻，再次查看集群节点的状态
kubectl get nodes
NAME   STATUS   ROLES    AGE   VERSION
m1     Ready    master   23m   v1.17.4
n1     Ready    <none>   16m   v1.17.4
n2     Ready    <none>   16m   v1.17.4

# 至此，k8s的集群环境搭建完成
```

## P11-环境搭建--环境测试

```sh
# 部署nginx
kubectl create deployment nginx --image=nginx:1.14-alpine

#暴露端口
kubectl expose deployment nginx --port=80 --type=NodePort

#查看服务状态
kubectl get pod,svc
NAME                         READY   STATUS    RESTARTS   AGE
pod/nginx-6867cdf567-8p624   1/1     Running   0          28s

NAME                 TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)        AGE
service/kubernetes   ClusterIP   10.96.0.1       <none>        443/TCP        27m
service/nginx        NodePort    10.105.205.24   <none>        80:32434/TCP   13s

#最后在电脑上访问部署的nginx服务
http://192.168.132.32:32434/	# 是否有nginx主页
```



# 第3章  资源管理

## P12-资源管理介绍

## P13-yaml语言介绍

#### 1、语法

- 大小写敏感

- 不允许使用tab, 只允许空格

- 给进的空格不重要，只要相同层级的元素左对齐即可

- #表示注释

#### 2、数据类型：纯量、对象

纯量：

- ~ 表示null
- 日期类型：2012-12-11（必须是yyyy-mm-dd）
- 时间类型：2021-01-02T17:03:33+08:00
- 字符串： 字符串过多的情况可以拆成多行，每一行会被转化在一个空格

对象：键值对集合 mapping/hash/dictionary

  对象：

```yaml
形式1：
heima:
	age:15
	address: Beijing
形式2：
heima: {age: 15, address: Beijing}
```

  数组：

```yml
形式1：
address: 
    - 顺义
    - 昌平

形式2：
address: [顺义, 昌平]
```

#### 3、提示

- 书写yaml切记：后面要加一个空格
- 如果需要将多段yaml配置放在一个文件中，中章要使用---分隔
- https://www.json2yaml.com/convert-yaml-to-json 校验
## P14-资源管理方式-介绍

1. 命令式对象管理：直接使用命令去操作k8s资源
```bash
kubectl run nginx-pod --image-nginx:1.17.1 --port=80
```
2. 命令式对象配置：通过命令配置和配置文件去操作k8s资源 
```bash
kubectl create/patch -f nginx-pod.yaml
```
3. 声明式对象配置：通过apply命令和配置文件去操作k8s资源
```bash
kubectl apply -f nginx-pod.yaml  #只用于创建和更新资源
```

优缺点：

| 类型 | 操作对象 | 适用环境 | 优点 | 缺点 |
| :--: | :--:| :--: |:--: |:--:|
| 命令式对象管理 | 对象 | 测试 | 简单 | 只能操作活动对象，  <br />无法审计，跟踪 |
| 命令式对象配置 | 文件 | 开发 | 可以审计，跟踪 | 项目大时，<br />配置文件多，操作麻烦 |
| 声明式对象配置 | 目录 | 开发 | 支持目录操作 | 意外情况下难以调试 |



## P15-资源管理方式-1：命令式对象管理

### kubectl 命令的语法

​	kubectl [command] [type] [name] [flags]

#### 1、comand

​	指定要对资源执行的操作，例如 create、get、describe 和 delete

- 基本命令

<table border="1" style="width:70%">
    <tr align="center">
        <th ></th>   
        <th >命令</th>
        <th>中文</th>  
        <th >说明</th>  
    </tr>
    <tr   align="center">
        <td style="vertical-align:middle" rowspan="7">基本命令</td>
        <td>create</td>
        <td>创建</td>
        <td>创建一个资源</td>
    </tr>
    <tr  align="center">
        <td>edit</td>
        <td>编辑</td>
        <td>编辑一个资源</td>
    </tr>
     <tr align="center">
        <td>get</td>
        <td>获取</td>
        <td>获取一个资源</td>
    </tr>
     <tr align="center">
        <td>get</td>
        <td>获取</td>
        <td>获取一个资源</td>
    </tr>
     <tr align="center">
        <td>patch</td>
        <td>更新</td>
        <td>更新一个资源</td>
    </tr>
     <tr align="center">
        <td>delete</td>
        <td>删除</td>
        <td>删除一个资源</td>
    </tr>
     <tr align="center">
        <td>explain</td>
        <td>解释</td>
        <td>解释一个资源</td>
    </tr>
</table>




- 运行和调试

- 高级命令

- 其他命令

#### 2、type

  指定资源类型，资源类型是大小写敏感的，开发者能够以单数、复数和缩略的 形式

#### 3、name

  指定资源的名称，名称也大小写敏感的。如果省略名称，则会显示所有的资源

#### 4、flags

​	指定可选的参数。例如，可用-s 或者–server 参数指定 Kubernetes API server 的地址和端口

### 例

```bash
# 创建一个namespace
kubectl create namespace dev

# 获取namespace
kubectl get ns

# 在此namespace下创建并运行一个nginnx的pod
kubectl run pod_name --image=nginx -n dev

#查看新创建的pod
kubectl get pod -n dev

#删除指定的pod
kubectl delete pod pod_name

#删除指定的namespace
kubectl delete ns dev
```



## P16-资源管理方式-2：命令式对象配置

nginxpod.yaml

```yml
apiVersion: v1
kind: Namespace
metadata:
    name: dev
---
apiVersion: v1
kind: Pod
metadata:
    name: nginxpod
    namespace: dev
spec:
  containers:
  - name: nginx-containers
    image: nginx:1.17.1

```

```bash
# 创建
kubectl create -f nginxpod.yaml

# 获得信息
kubectl get -f nginxpod.yaml

# 删除资源
kubectl delete -f nginxpod.yaml
```

总结：命令式对象配置的方式操作资源，可以简单的认为：命令+yaml配置文件（里面是命令需要的各种参数）

## P17-资源管理方式-3：声明式对象配置

声明式对象配置跟命令式对象配置很相似，但是它只有一个命令apply

```bash
# 首先执行一次kubectl apply -f yaml文件，发现创建了资源
kubectl apply -f nginxpod.yaml
namespace/dev created
pod/nginxpod created

# 再执行一次kubectl apply -f yaml文件，发现说资源没有变动
kubectl apply -f nginxpod.yaml
```



## P18-资源管理方式-小结

kubectl的运行是需要配置文件的，文件是$HOME/.kube，如果相要在node上运行些命令，需要将master上的.kube复制到node上

```bash
scp -r HOME/.kube node1: HOME/
scp -r ~/.kube node1:~/
```

推荐方案：

```bash
kubectl apply -f xxx.yaml		   #创建/更新资源
kubectl delete -f xxx.yaml		   #删除资源
kubectl get(describe) 资源名称		#查询资源
```

# 第4章 实战入门

## P19-实战入门-Namespace

namespace: 它的主要作用是用来实现**多套环境的资源隔离**或者**多租户的资源隔离**

- 空间隔离

- 还可以结合k8s的资源配额机制，限定不同租户能占用的资源，例如cpu使用量，内存使用量等，来实现租户可用资源的管理。

```bash
[root@m1 ~]# kubectl get ns
NAME              STATUS   AGE
default           Active   13d	# 所有未指定namespace的对象都会被分配在default中
dev               Active   18h	
kube-node-lease   Active   13d	# 集群节点之间的心跳维护，v1.13开始引入	
kube-public       Active   13d	# 此命名空间下的资源可以被所有人访问（包括未认证用户）
kube-system       Active   13d	# 所有由k8s系统创建的资源都处于这个命名空间
  
```

###   查看

```bash
# 1、查看所有的ns
kubectl get ns

# 2、查看指定的ns  kubectl get ns ns_name
kubectl get ns default
NAME      STATUS   AGE
default   Active   13d

# 3、查看输出格式： kubectl get ns ns_name -o 参数
# k8s支持的格式很多，常见有：wide, json, yaml
kubectl get ns default -o yaml
apiVersion: v1
kind: Namespace
metadata:
  creationTimestamp: "2021-03-11T08:41:53Z"
  managedFields:
  - apiVersion: v1
    fieldsType: FieldsV1
    fieldsV1:
      f:status:
        f:phase: {}
    manager: kube-apiserver
    operation: Update
    time: "2021-03-11T08:41:53Z"
  name: default
  resourceVersion: "190"
  uid: 9b84f7e7-e28c-4660-83b5-f514d5002645
spec:
  finalizers:
  - kubernetes
status:
  phase: Active

# 查看ns详情
kubectl describe ns default
Name:         default
Labels:       <none>
Annotations:  <none>
Status:       Active	#active 命名空间正在使用中，Terminating 正在删除命名空间

No resource quota.		#针对ns做的资源限制
No LimitRange resource.	#针对ns中的每个组件做的资源限制

```

### 创建

```bash
kubectl create ns dev
namespace/dev created
```

### 删除

```bash
kubectl delete ns dev
namespace "dev" deleted
```

配置方式：

首先准备一个yaml文件：ns-dev.yaml

```yaml
apiVersion: v1
kind: Namespace
metadata:
	name: dev
```

然后就可以执行创建和删除命令了

```bash
kubectl create -f ns-dev.yaml
kubectl delete -f ns-dev.yaml
```

## P20-实战入门-Pod

### kubectl get -n kube-system 详解

```bash
[root@m1 ~]# kubectl get pod -n kube-system
NAME                         READY   STATUS    RESTARTS   AGE
coredns-7f89b7bc75-4s6lr     1/1     Running   711        13d
coredns-7f89b7bc75-m5mnl     1/1     Running   710        13d
etcd-m1                      1/1     Running   0          13d	#存放信息
kube-apiserver-m1            1/1     Running   0          13d	#入口
kube-controller-manager-m1   1/1     Running   0          13d	#具体执行
kube-flannel-ds-4jm45        1/1     Running   0          13d	#网络组件
kube-flannel-ds-7nq5n        1/1     Running   0          13d
kube-proxy-2xhzd             1/1     Running   0          13d
kube-proxy-rf6lf             1/1     Running   0          13d
kube-scheduler-m1            1/1     Running   0          13d	#调度
```



### 命令行

#### 创建并运行pod

k8s没有提供单独运行pod的命令，都是通过pod控制器来实现的

```bash
# 命令格式：kubectl run pod_name [参数]
# --image 指定pod镜像
# --port  指定端口
# --namespace 指定namespace
[root@master ~]# kubectl run nginx1 --image=nginx:1.17.1 --port=80 --namespace dev
deployment apps/nginx created
```

#### 查看pod

```bash
# 查看pod基本信息
kubectl get pods -n dev

# 查看pod与详细信息
kubectl describe pod nginx-5ff9343-fg2db -n dev
```

#### 访问pod

```bash
# 获取PodIP
kubectl get pods -n dev -o wide

# 访问pod
curl http://10.244.1.23:80
```

#### 删除指定pod

```bash
kubectl delete pod nginx-5ff2343423-fg2db -n dev

# 此时，显示pod删除成功，但再查询，发现又新产生一个
# 这是因为pod是由pod控制器创建，控制器会监控pod状况，一旦发现pod死亡，会立即重建
# 此时要想删除pod，必须删除pod控制器

# 先查询一下当前namespace下的pod控制器
kubectl get deploy -n dev

# 删除此pod控制器
kubectl delete deploy nginx -n dev

# 稍等下，再查询pod，发现pod 被删除了
kubectl get pods -n dev
```

### 基于yaml

pod-nginx.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: nginx
  namespace: dev
spec:
  containers:
  - image: nginx:1.17.1
    imagePullPolicy: IfNotPresent
    name: pod
    ports:
    - name: nginx-port
      containerPort: 80
      protocol: TCP
```

```bash
kubectl create -f pod-nginx.yaml  #创建
kubectl delete -f pod-nginx.yaml  #删除
```



## P21-实战入门-Label

#### label

概念：label的作用就是在资源上添加标识，用来对它们进行区分和选择。

特点：

- 一个label会以key/value键值对的开式附加到各种对象上，如 node, pod, service等等。
- 一个资源对象可以定义任意数量的label, 同一个label 也可以被添加到量的资源对够用上去。
- label通常在资源对象定义时确定，当然也可以在对象创建后动态添加或者删除。

> 一些常用的label示例如下：
>
> - 版本标签："version":"release", "version":"stable"
>
> - 环境标签：“environment": "dev", "environment":"test"
>
> - 架构标签：”tier": "frontend", "tier": "backend"
>
>   

#### label selector

​	label用于给某个资源对象定义标识

​	label selector用于查询和筛选拥有某引起标签的资源对象

##### 2种label selector

- ​	基于等式的label selector

  ​	name=slave: 选择所有包含label中key='name'且value="slave"的对象

  ​	env!=production: 选择所有包括label中的key="env" 且value不等于"production"的对象

- ​	基于集合的label selector

  ​	name in (master, slave): 选择所有包含label中key="name"且value="master"或"slave"对象

  ​	name not in(frontend): 选择所有包含label中的key="name" 且value不等于"frontend"的对象

  
#### 命令方式
```bash
# 为pod资源打标签
kubectl label pod pod_name version=1.0 -n dev

#为pod资源更新标签
kubectl label pod pod_name version=2.0 -n dev --overwrite

# 查看标签
kubectl get pod pod_name -n dev --show-labels

# 筛选标签
kubectl get pod -n dev -l version=2.0 --show-labels
kubectl get pod -n dev -l version!=2.0 --show-labels

# 删除标签
kubectl label pod pod_name version- -n dev
```
#### 基于yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: nginx
  namespace: dev
  labels:
    version: "3.0"
    env: "test"
spec:
  containers:
  - image: nginx:1.17.1
    imagePullPolicy: IfNotPresent
    name: pod
    ports:
    - name: nginx-port
      containerPort: 80
      protocol: TCP
```



## P22-实战入门-Deployment

pod控制器用于pod的管理，确保pod资源符合预期的状态。

deployment是pod控制器的一种

### 命令操作

```bash
# 命令格式：kubectl run deployment_name [参数]
# --image 指定pod的镜像
# --port 指定端口
# --replicas  指定创建pod数量
# --namespace 指定namespace
kubectl run nginx2 --image=nginx:1.17.1 --port=80 --replicas=3 -n dev

# 查看pod
kubectl get pod -n dev

# 查看deployment的信息
kubectl get deploy -n dev

# UP-TO-DATE: 成功升级的副本数量
# AVAILABLE: 可用副本的数量
kubectl get deploy -n dev -o wide

# 查看deployment的详细信息
kubectl describe deploy nginx -n dev

#删除
kubectl delete deploy nginx -n dev
```

### 配置yaml操作

deploy-nginx.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx
  namespace: dev
spec:
  replicas: 3
  selector:
    matchLabels:
      run: nginx
  ###### pod 模板
  template:   
    metadata:
      labels:
        run: nginx
    spec:
      containers:
      - image: nginx:1.17.1
        name: nginx
        ports:
        - containerPort: 80
          protocol: TCP
```

```bash
kubectl create -f deploy-nginx.yaml  #创建
kubectl delete -f deploy-nginx.yaml  #删除
```

## P23-实战入门-Service

虽然每个pod都会分配一个单独的ip, 但却存在2个问题：

- pod ip  会随着pod的重建产生变化
- pod ip  仅仅是集群内可见的虚拟ip, 外部无法访问

这对于访问这个服务带来了难度，因此，k8s设计了service来解决这个问题。

service 可以看作是一组同类pod**对外的访问接口**借助。service, 应用可以方便地实现服务发现和负载均衡。

### 创建及查看

```sh
# 操作一：集群内部访问
# 暴露service
kubectl expose deploy nginx --name=svc-nginx1 --type=ClusterIP --port=80 --target-port=80 -n dev

#查看service
kubectl get svc svc-nginx1 -n dev -o wide
NAME         TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)   AGE   SELECTOR
svc-nginx1   ClusterIP   10.103.170.48   <none>        80/TCP    20s   run=nginx

#这里产生了一个CLSTER-IP，这就是service的IP, 在service生命周期中，这个地址是不会变动的
#可以通过这个IP访问当前service对应的pod
curl 10.103.170.48:80
```

```sh
# 操作二：集群外部访问
# 修改type为NodePort
kubectl expose deploy nginx --name=svc-nginx2 --type=NodePort --port=80 --target-port=80 -n dev

# 查看时会发现出现了NodePort类型的service，而且有一对port
$ kubectl get svc -n dev
NAME         TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)        AGE
svc-nginx1   ClusterIP   10.103.170.48   <none>        80/TCP         13m
svc-nginx2   NodePort    10.96.1.50      <none>        80:30496/TCP   44s

# 接下来可以通过集群外主机访问
http://192.168.132.31:30496/
http://192.168.132.32:30496/
http://192.168.132.33:30496/
```

### 删除service

```sh
kubectl delete svc svc-nginx1 -n dev
```

### 配置yaml方式

svc-nginx.yaml

```yml
apiVersion: v1
kind: Service
metadata:
  name: svc-nginx
  namespace: dev
spec: 
  clusterIP: 10.109.179.231
  ports: 
  - port: 80
    protocol: TCP
    targetPort: 80
  selector:
    run: nginx
  type: ClusterIP
```

```sh
kubectl create -f svc-nginx.yaml
kubectl delete -f svc-nginx.yaml
```

# pod详解

## P23-实战入门-Service
## P24-Pod详解-结构和定义
## P25-Pod详解-基本配置
## P26-Pod详解-镜像拉取策略
## P27-Pod详解-启动命令
## P28Pod详解-环境变量
## P29Pod详解-端口设置
## P30Pod详解-资源配额
## P31-Pod详解-生命周期-概述
## P32-Pod详解-生命周期-创建和终止
## P33-Pod详解-生命周期-初始化容器
## P34-Pod详解-生命周期-钩子函数
## P35-Pod详解-生命周期-容器探测
## P36-Pod详解-生命周期-容器探测补充
## P37-Pod详解-生命周期-重启策略
## P38-Pod详解-调度-概述
## P39-Pod详解-定向调度
## P40-Pod详解-亲和性调度-概述
## P41-Pod详解-亲和性调度-nodeAffinity
## P42-Pod详解-亲和性调度-podAffinity
## P43-Pod详解-亲和性调度-podAntiAffinity
## P44-Pod详解-调度-污点
## P45-Pod详解-调度-容忍