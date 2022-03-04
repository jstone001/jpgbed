from: https://www.bilibili.com/video/BV1Qv41167ck

```
http://yun.itheima.com/course/640.html
```


# 第1章 k8s介绍

# 第2章 集群环境搭建

## P5-环境搭建--环境规划

### 集群类型：一主多从，多主多从

### 安装方式：kubeadm, minikube, 二进制包

```
minikube: 一个用于快速搭建单节点k8s的工具
```


```
kubeadm: 一个用于快速搭建k8s集群的工具
```


```
二进制包: 官网下载每个组件的2进制包
```


### 主机规划


| 作用   | IP地址          | 操作系统  | 配置                  |
| -------- | ----------------- | ----------- | ----------------------- |
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
kubectl get nodes
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

（现在不用这样做了）
# 修改文件中quay.io仓库为quay-mirror.qiniu.com 
#sed -i 's#quay.io#quay-mirror.qiniu.com#g' kube-flannel.yml

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


|      类型      | 操作对象 | 适用环境 |      优点      |                  缺点                  |
| :--------------: | :--------: | :--------: | :--------------: | :--------------------------------------: |
| 命令式对象管理 |   对象   |   测试   |      简单      | 只能操作活动对象，<br />无法审计，跟踪 |
| 命令式对象配置 |   文件   |   开发   | 可以审计，跟踪 |  项目大时，<br />配置文件多，操作麻烦  |
| 声明式对象配置 |   目录   |   开发   |  支持目录操作  |           意外情况下难以调试           |

## P15-资源管理方式-1：命令式对象管理

### kubectl 命令的语法

```
kubectl [command] [type] [name] [flags]
```


#### 1、comand

```
指定要对资源执行的操作，例如 create、get、describe 和 delete
```


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

```
指定可选的参数。例如，可用-s 或者–server 参数指定 Kubernetes API server 的地址和端口
```


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

### 查看

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
> - 环境标签：“environment": "dev", "environment":"test"
> - 架构标签：”tier": "frontend", "tier": "backend"

#### label selector

```
label用于给某个资源对象定义标识
```


```
label selector用于查询和筛选拥有某引起标签的资源对象
```


##### 2种label selector

- ```
  基于等式的label selector
  ```


  ```
  name=slave: 选择所有包含label中key='name'且value="slave"的对象
  ```


  ```
  env!=production: 选择所有包括label中的key="env" 且value不等于"production"的对象
  ```
- ```
  基于集合的label selector
  ```


  ```
  name in (master, slave): 选择所有包含label中key="name"且value="master"或"slave"对象
  ```


  ```
  name not in(frontend): 选择所有包含label中的key="name" 且value不等于"frontend"的对象
  ```

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

# 第5章 pod详解

## P24-Pod详解-结构和定义

### 5.1 pod介绍

#### 5.1.1 pod结构

Pause容器

作用：

- ```
  可以以它为依据，评估整个pod的健康状态
  ```
- ```
  可以在根容器上设置ip地址，其它窗口都以此为ip，以实现pod内部的通信
  ```

> 这里是pod内部的通讯，pod之间的通讯采用虚拟2层风络技术来实现，我们当前用flannel

#### 5.1.2 pod定义

pod.yaml

```yaml
apiVersion: v1	#必选，版本号，例如v1
kind: Pod		#必选，资源类型，例如Pod
metadata: 		#必选，元数据
  name: string		#必选，pod名称
  namespace: string  #pod所属空间，默认为default
  labels: 
  - name: string
spec:		#必选，pod中容器的详细定义
  containers:  	#必选，pod中容器列表
  - name: string 	#必选，容器名称
    image: string  	#必选，容器的镜像名称
    imagePullPolicy: [Always|Never|IfNotPresent] 	#获取镜像的策略
    command: [string]  	#容器的启动命令列表，如不指定，使用打包时使用的启动命令
    args: [string]		#容器的启动命令参数列表
    workingDir: string	#容器的工作目录
    volumnMounts:		#挂载到容器内部的存储卷配置
    - name: string 		#引用pod定义的共享存储卷的名称，需用volumes[]部分定义的卷名
    mountPath: string	#存储卷在容器内mount的绝对路径，应少于512字符
    readOnly: boolean	#是否为只读模式
  ports: 		#需要暴露的端口库号列表
  - name: string		#端口的名称
  containerPort: int	#容器需要监听的端口号
  hostPort: int			#容器所在主机需要监听的端口号，默认与container相同
  protocal: string		#端口协议，支持TCP和UDP, 默认TCP
```

```sh
# 属性查询 
kubectl explain 资源类型	#查看某种资源配置的一级属性
kubectl explain 资源类型.属性		#查看属性的子属性
kubectl explain pod
kubectl explain pod.metadata
```

```sh
# 一级属性
apiVersion	<string>	# 版本。k8s 内部定义。 kubectl api-vesions查询所有的版本
kind	<string>		# 类型，k8s内部定义。 kubectl api-resources 查询 
metadata	<Object>	# 元数据，主要是资源标识和说明，常用有name, namespace, labels等
spec	<Object>		# 描述，这是配置中最重要的一部分，里面是对各种资源配置的详细描述
status	<Object>		# 状态信息，由k8s自动生成

#查看状态 
kubectl get pod nginx_name -n dev -o yaml
```

```sh
#spec  常见属性
containers		# 容器列表，用于定义容器的详细信息
nodeName		# 根据nodeName的值将pod调度到指定node节点上
nodeSelector	# 根据nodeSelector中定义的信息选择将该pod调度到包含这些label的node上
hostNetwork		# 是否使用主机网络模式，默认为false, 如果设置true, 表示使用宿主机网络
volumes			# 存储卷，用于定义pod上面挂在的存储信息
restartPolicy 	# 重启策略，表示pod在遇到故障的时候的处理策略
```

## P25-Pod详解-基本配置

### 5.2 pod配置

```sh
# pod.spec.containers 属性
kubectl explain pod.spec.containers
KIND:     Pod
VERSION:  v1

RESOURCE: containers <[]Object>		# 数组，代表可以有多个容器

FIELDS:
   args			# 启动参数列表
   command		# 启动命令列表
   env	<[]Object>	# 窗口环境变量的配置
   image			# 镜像地址
   imagePullPolicy	# 拉取策略
   lifecycle	<Object>
   name	<string> -required- 	# 容器名称
   ports	<[]Object>
   resources	<Object>	# 资源限制和次源请求的设置

```

#### 5.2.1 基本配置

创建pod-base.yaml

```yaml
apiVersion: v1	#必选，版本号，例如v1
kind: Pod		#必选，资源类型，例如Pod
metadata: 		#必选，元数据
  name: pod-base		#必选，pod名称
  namespace: dev  #pod所属空间，默认为default
  labels: 
    user: heima
spec:		#必选，pod中容器的详细定义
  containers:  	#必选，pod中容器列表
  - name: nginx 	#必选，容器名称
    image: nginx:1.17.1  	#必选，容器的镜像名称（docker配置的镜像源）
  - name: busybox	#busybox是一个小巧的Linux命令集合
    image: busybox:1.30
```

```sh
kubectl apply -f pod-base.yaml	#创建pod
# 查看pod状况
```

## P26-Pod详解-镜像拉取策略

#### 5.2.2 镜像拉取策略

```sh
# imagePullPolicy
## always: 总是从远程拉取
## IfNotPresent: 本地有就不拉，没有就拉
## Never: 只使用本地镜像，不拉取
```

<font color='red'>如果tag为具体版本号，默认策略是：IfNotPresent</font>

<font color='red'>如果镜像tag为：lastest，默认策略是always</font>

## P27-Pod详解-启动命令

#### 5.2.3 启动命令

busybox并不是一个程序，面是一个工具类的集合，k8s集群启动管理后，它会自动关闭，解决押一付一是让其一直运行，这就用到了command配置。

创建pod-command.yaml

```yaml
apiVersion: v1	#必选，版本号，例如v1
kind: Pod		#必选，资源类型，例如Pod
metadata: 		#必选，元数据
  name: pod-command		#必选，pod名称
  namespace: dev  #pod所属空间，默认为default
spec:		#必选，pod中容器的详细定义
  containers:  	#必选，pod中容器列表
  - name: nginx 	#必选，容器名称
    image: nginx:1.17.1  	#必选，容器的镜像名称（docker配置的镜像源）
  - name: busybox	#busybox是一个小巧的Linux命令集合
    image: busybox:1.30
    command: ["/bin/sh","-c","touch /tmp/hello.txt;while true;do /bin/echo $/(date +%T) >> /tmp/hello.txt; sleep 3; done;"]
```

```sh
# 进入pod的busybox容器
# kubectl exec pod_name -n namespace_name -it -c 容器名称 /bin/sh  在容器内部执行命令
kubectl exec pod-command -n dev -it -c busybox /bin/sh
tail -f /tmp/hello.txt
```

<font color='red'>特别说明：</font>

k8s中的command, args两荐其实是实现覆盖Dockerfile中的ENTRYPOINT的功能。

1. 如果command和args均没写，那么用Dockerfile的配置
2. 如果command写了，args没有写，那么Dockerfile默认的撇脂会被 忽略，执行输入的command
3. 如果command没写，但args写了，那么Dockerfile中配置的ENTRYPOINT的命令会被执行，使用当前的args参数
4. 如果command和args都写了，那么Dockerfile的配置被忽略，执行command并追加args参数

## P28Pod详解-环境变量

#### 5.2.4 环境配置

设置容器的环境变量（企业不太用）

以后会单独存储在配置文件中，

pod-env.yaml

```yaml
apiVersion: v1	#必选，版本号，例如v1
kind: Pod		#必选，资源类型，例如Pod
metadata: 		#必选，元数据
  name: pod-env		#必选，pod名称
  namespace: dev  #pod所属空间，默认为default
spec:		#必选，pod中容器的详细定义
  containers:  	#必选，pod中容器列表
  - name: busybox 	#必选，容器名称
    image: busybox:1.30  	#必选，容器的镜像名称（docker配置的镜像源）
    command: ["/bin/sh","-c","while true;do /bin/echo $(date +%T);sleep 60; done;"]
    env：
    - name: "usrname"
      value: "admin"
    - name: "password"
      value: "123456"
```

```sh
# 验证
kubectl exec pod-env -n dev -c busybox -it /bin/sh
echo $username
echo $password
```

## P29-Pod详解-端口设置

#### 5.2.5  端口设置

```sh
kubectl explain pod.spec.containers.ports
   containerPort	# 0~65536	(重要)
   hostIP			# 将外部端口绑定到主机IP(一般省略)
   hostPort			# 容器要在主机上公开的端口，如果设置，主机上只能运行容器的一个副本（一般省略）
   name	<string>	# 端口名称
   protocol	<string>	# UDP/TCP/SCTP。默认TCP
```

pod-ports.yaml

```yaml
apiVersion: v1	#必选，版本号，例如v1
kind: Pod		#必选，资源类型，例如Pod
metadata: 		#必选，元数据
  name: pod-ports		#必选，pod名称
  namespace: dev  #pod所属空间，默认为default
spec:		#必选，pod中容器的详细定义
  containers:  	#必选，pod中容器列表
  - name: nginx 	#必选，容器名称
    image: nginx:1.17.1  	#必选，容器的镜像名称（docker配置的镜像源）
    ports:
    - name: nginx-port
      containerPort: 80
      protocol: TCP
```

```sh
# 验证
kubectl get pod pod-ports -n dev -o yaml
```

## P30-Pod详解-资源配额

#### 5.2.6 resources配额

k8s对内存和cpu资源进行配额的机制，通过resources选项实现，他有2个子选项：

- limits: 用于限制运行时容器的最大占用资源，当窗口占用资源超过Limits时会被终止，并进行重启。
- requests: 用于设置容器需要的最小资源，如果环境资源不够，容器将无法启动。

pod-resources.yaml

```yaml
apiVersion: v1	#必选，版本号，例如v1
kind: Pod		#必选，资源类型，例如Pod
metadata: 		#必选，元数据
  name: pod-resources		#必选，pod名称
  namespace: dev  #pod所属空间，默认为default
spec:		#必选，pod中容器的详细定义
  containers:  	#必选，pod中容器列表
  - name: nginx 	#必选，容器名称
    image: nginx:1.17.1  	#必选，容器的镜像名称（docker配置的镜像源）
    resources:
      limits: 
        cpu: "2"
        memory: "18Gi"
      requests:
        cpu: "1"
        moemory: "10Mi"
```

> cpu: core数，可以为整数或小数
>
> memory: 内存大小，可以使用Gi，Mi，G，M等形式

## P31-Pod详解-生命周期-概述

### 5.3 生命周期

- pod创建过程
- 运行初始化容器(init container) 过程
- 运行主容器(main container) 过程

  - 容器启动后钩子(post start), 容器终止前钩子(pre stop)
  - 容器的存活性控沙漠风暴(liveness probe), 就绪性控测(readiness probe)
- pod终止过程

5种状态（相位）：

- 挂起 (pending)：apiserver已经 创建了pod资源对象，但它尚未被调度完成或者仍处于下载镜像的过程中
- 运行中(running): pod已经被调度至某节点，并且所有容器都已经被k8s创建
- 成功(succeeded):  pod中的所有容器都已经到哪里下图止并且不会被重启
- 失败(failed): 所有容器都已经终止，但至少有一个容器终止失败，那容器返回了非0值的退出状态
- 未知(unknown): apiserver无法正常获取到pod对象的状态信息，通常由网络通信失败所导致。

## P32-Pod详解-生命周期-创建和终止

#### 5.3.1 创建和终止

**创建过程：**

1. 用户通过k8s或其他api客户端提交需要创建的pod信息给apiServer
2. apiServer开始生成pod对象的信息，并将信息存入etcd, 然后返回确认信息至客户端
3. apiServer开始反映etcd中的pod对象的变化，其他组件使用watch机制来跟踪检查apiServer上的变动
4. scheduler发现有新的pod对象要创建，开始为pod分配主机并将结果信息更新至apiServer
5. node节点上的kubelet发现有pod调度过来，尝试调用docker启动容器，并将结果回送至apiServer
6. apiServer将接收到pod状态信息存入etcd中

**pod的终止过程：**

1. 用户向apiServer发送删除pod对象的命令
2. apiServer中的pod对象信息会随着时间的推移而更新，在宽限内（默认为30s），pod被视为dead
3. 将pod标记为erminating状态
4. kubelet在监控到pod对象转为terminating状态的同时启动pod关闭过程
5. 端点控制器到pod对象的关闭行为时将其从所有匹配到此端点的service资源的端点列表中移除
6. 如果当前pod对象定义了preStop钩子处理器，则在其标记为terminating后即会以同步的方式启动执行
7. pod对象中的容器进程上到停止信号
8. 宽限期结束后，若pod中还存在仍在运行的进程，那么pod对象会收到立即终止的信号
9. kubelet请求apiServer将止pod资源的宽限设置为0从而完成删除操作，此时pod对于用户已不可见

## P33-Pod详解-生命周期-初始化容器

#### 5.2.3 初始化容器

<font color='red'>主程序之前先搭建其他程序</font>

两大特征：

- 初始化容器必须运行完成直至结束，若某初始化容器运行朱败，那么k8s需要重启它直到成功完成
- 初始化容器必须按照定义的顺序执行，当且仅当前一个成功之后，后面的一个才能运行

应用场景：

- 提供主容器镜像中不具备的工具程序或自定义代码
- 初始化容器要先于应用容器串行启动并运行完成，因此可用于延后应用容器的启动直至基依赖的条件得到满足

pod-initcontainer.yaml

```yaml
apiVersion: v1	#必选，版本号，例如v1
kind: Pod		#必选，资源类型，例如Pod
metadata: 		#必选，元数据
  name: pod-initcontainer		#必选，pod名称
  namespace: dev  #pod所属空间，默认为default
spec:		#必选，pod中容器的详细定义
  containers:  	#必选，pod中容器列表
  - name: main-container 	#必选，容器名称
    image: nginx:1.17.1  	#必选，容器的镜像名称（docker配置的镜像源）
    ports:
    - name: nginx-port
      containerPort: 80
  initContainers:
  - name: test-mysql
    image: busybox:1.30
    command: ['sh','-c','until ping 192.168.109.201 -c 1 ; do echo waiting for mysql...;sleep 2; done;']
  - name: test-redis
    image: busybox:1.30
    command: ['sh','-c','until ping 192.168.109.202 -c 1 ; do echo waiting for redis...;sleep 2; done;']
```

```sh
#动态查看pod
kubectl get pods pod-initcontainer -n dev -w

# 接下来新开一个shell, 为当前服务器新增两个ip，观察pod的变化 
ifconfig ens33:1 192.168.109.201 netmask 255.255.255.0 up	#给网卡地址
ifconfig ens33:2 192.168.109.202 netmask 255.255.255.0 up
```

## P34-Pod详解-生命周期-钩子函数

#### 5.3.3 钩子函数

- post start: 容器创建之后执行，如果失败会重启容器
- pre stop: 容器终止之前执行，执行完成之后容器将成功终止，在其完成之前会阻塞删除容器的操作

钩子处理支持使用下面<font color='red'>3种方式定义动作：</font>

- Exec命令：在容器内执行一次命令（常用）

```yaml
lifecycle:
  postStart:
    exec:
      command:
      - cat
      - /tmp/healthy
```

- TCPSocket: 在当前容器尝试访问指定的socket

```yaml
lifecycle:
  postStart:
    tcpSocket:
      port: 8080
```

- HTTPGet: 在当前容器中向某url发起http请求

```yaml
lifecycle:
  postStart:
    httpGet:
      path: /  #url地址
      port: 80
      host: 192.168.109.100  #主机地址，（拼接后：192.168.109.100:80/ ）
      scheme: HTTP	#支持的协议，http或者https    
```

pod-hook-exec.yaml

```yaml
apiVersion: v1	#必选，版本号，例如v1
kind: Pod		#必选，资源类型，例如Pod
metadata: 		#必选，元数据
  name: pod-hook-exec		#必选，pod名称
  namespace: dev  #pod所属空间，默认为default
spec:		#必选，pod中容器的详细定义
  containers:  	#必选，pod中容器列表
  - name: main-container 	#必选，容器名称
    image: nginx:1.17.1  	#必选，容器的镜像名称（docker配置的镜像源）
    ports:
    - name: nginx-port
      containerPort: 80
    lifecycle:
      postStart:
        exec:  # 在容器启动的时候执行一个命令，修改掉nginx的默认首页内容
          command: ['/bin/sh','-c','echo postStart... > /usr/share/nginx/html/index.html']
      preStop:
        exec:  # 在容器停止之前停止nginx服务
          command: ['/usr/sbin/nginx','-s','quit']
```

```sh
# 查看
kubectl get pod pod-hook-exec.yaml

#访问pod
curl 10.244.2.48

#explain
kubectl explain pod.spec.containers.lifecycle.postStart
```

## P35-Pod详解-生命周期-容器探测

#### 5.3.4  容器探测

用于探测容器中的应用实例是否正常工作。

两种探针：

- liveness probes: 存活性探针，用于检测应用实例当前是否处于下常运行状态，如果不是，k8s会重启容器。
- readiness probes: 就绪性探针，用于检测应用实例当前是否可以接收请求，如果 不能，k8s不会转发流量

> livenessProbe 决定是否重启容器，readinessProbe决定是否将请求转发给容器。

- exec命令：在容器内执行一次命令，如果 命令执行的退出码为0，，则认为程序正常，否则不正常

```yaml
lifecycle:
  postStart:
    exec:
      command:
      - cat
      - /tmp/healthy
```

- TCPSocket: 将尝试访问一个用户容器的端口，如果能够建立这条连接，则认为程序正常，否则不正常

```yaml
lifecycle:
  postStart:
    tcpSocket:
      port: 8080
```

- HTTPGet: 调用容器内Web应用的URL，如果返回的状态码在200和399之间，则认为程序正常，否则不正常

```yaml
lifecycle:
  postStart:
    httpGet:
      path: /  #url地址
      port: 80
      host: 192.168.109.100  #主机地址，（拼接后：192.168.109.100:80/ ）
      scheme: HTTP	#支持的协议，http或者https 
```

以liveness probes为例：

**方式1：Exec**

pod-liveness-exec.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: pod-liveness-exec
  namespace: dev
spec:
  containers:
  - name: nginx
    image: nginx:1.17.1
    ports:
    - name: nginx-port
      containerPort: 80
    livenessProbe:
      exec:
        command: ["/bin/cat","/tmp/hello.txt"]	# 没有这个文件，所以会重启
```

```sh
# 观察
kubectl describe pod  pod-liveness-exec -n dev

Events:
  Type     Reason     Age                From               Message
  ----     ------     ----               ----               -------
  Normal   Scheduled  <unknown>          default-scheduler  Successfully assigned dev/pod-liveness-exec to n1
  Normal   Pulled     18s (x2 over 46s)  kubelet, n1        Container image "nginx:1.17.1" already present on machine
  Normal   Created    18s (x2 over 46s)  kubelet, n1        Created container nginx
  Normal   Started    18s (x2 over 46s)  kubelet, n1        Started container nginx
  Normal   Killing    18s                kubelet, n1        Container nginx failed liveness probe, will be restarted
  Warning  Unhealthy  8s (x4 over 38s)   kubelet, n1        Liveness probe failed: /bin/cat: /tmp/hello.txt: No such file or directory
```

## P36-Pod详解-生命周期-容器探测补充

```sh
kubectl explain pod.spec.containers.livenessProbe

   exec	<Object>
     One and only one of the following should be specified. Exec specifies the
     action to take.

   failureThreshold	<integer>	# 连续探测多少次被认定为失败。默认是3次，最小值为1
     Minimum consecutive failures for the probe to be considered failed after
     having succeeded. Defaults to 3. Minimum value is 1.

   httpGet	<Object>
     HTTPGet specifies the http request to perform.

   initialDelaySeconds	<integer>  # 容器启动后等待多少秒执行第1次探测
     Number of seconds after the container has started before liveness probes
     are initiated. More info:
     https://kubernetes.io/docs/concepts/workloads/pods/pod-lifecycle#container-probes

   periodSeconds	<integer>	# 执行控测的频率。默认是10秒，最小1秒
     How often (in seconds) to perform the probe. Default to 10 seconds. Minimum
     value is 1.

   successThreshold	<integer>  # 连续探测多少次被认定为成功。默认是1
     Minimum consecutive successes for the probe to be considered successful
     after having failed. Defaults to 1. Must be 1 for liveness and startup.
     Minimum value is 1.

   tcpSocket	<Object>
     TCPSocket specifies an action involving a TCP port. TCP hooks not yet
     supported

   timeoutSeconds	<integer>	# 探测超时时间。默认1秒，最小1秒
     Number of seconds after which the probe times out. Defaults to 1 second.
     Minimum value is 1. More info:
     https://kubernetes.io/docs/concepts/workloads/pods/pod-lifecycle#container-probes

```

pod-liveness-httpget.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: pod-liveness-exec
  namespace: dev
spec:
  containers:
  - name: nginx
    image: nginx:1.17.1
    ports:
    - name: nginx-port
      containerPort: 80
    livenessProbe:
        httpGet:
          scheme: HTTP
          path: /  #url地址
          port: 80
        initialDelaySeconds: 30  # 30秒后开始探测
        timeoutSeconds: 5	# 探测超时时间为5s
```

## P37-Pod详解-生命周期-重启策略

#### 5.3.5  重启策略

- Always: 容器朱效时，自动重启该容器，这也是default
- OnFailure: 容器终止运行且退出码不为0时重启
- Never: 不论状态为何，不重启

> 首次需要重启的容器，将在其需要时立即重启，随后再次需要重启的操作将由kubelet延迟一段时间后进行，且反复的重启操作的延迟时长以此为10s, 20s, 40s, 80s, 160s 和300s, 300s是最大延迟时长。

pod-restartpolicy.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: pod-restartpolcy
  namespace: dev
spec:
  containers:
  - name: nginx
    image: nginx:1.17.1
    ports:
    - name: nginx-port
      containerPort: 80
    livenessProbe:
        httpGet:
          scheme: HTTP
          path: /hello  #url地址
          port: 80
    restartPolicy: Never
```

## P38-Pod详解-调度-概述

### 5.4 pod调度

4大调度方式：

- 自动调度：运行在哪个节点上完全由Scheduler经过一系列的算法计算得出
- 定向调度：NodeName, NodeSelector
- 亲和性调度：NodeAffinity, PodAffinity, PodAntiAffinity
- 污点(容忍)调度：Taints, Toleraton

## P39-Pod详解-定向调度

### 5.4.1 定向调度

指用在pod上声明nodeName或者nodeSelector，以此将pod调度到期望的node节点上。注意，这里的调度是强制的，这意味着即使node不存在，也会向上面进行调度，只不过pod运行失败而已。

**NodeName**

用于强制约束将pod调度到指定的Name的node节点上。这种方式，其实是直接跳过Scheduler的调度逻辑，直接将pod调度到指定名称的节点。

pod-nodename.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: pod-restartpolcy
  namespace: dev
spec:
  containers:
  - name: nginx
    image: nginx:1.17.1
  nodeName: node1	# 指定调度到node1节点上
```

**NodeSelector**

用于将pod调度到添加指定标签的node节点上。它是通过k8s的label-selector机制实现的。在pod创建之前，会由scheduler使用natchNodeSelector调度策略进行label匹配，打出目标node，然后将pod调度到目标节点，该匹配规则是**强制约束**。

```sh
# 先为node添加label
kubectl label nodes node1 nodeenv=pro	# label是键值对
kebectl label nodes node2 nodeenv=test
```

pod-nodeseletor.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: pod-restartpolcy
  namespace: dev
spec:
  containers:
  - name: nginx
    image: nginx:1.17.1
  nodeSelector: 
    nodeenv: pro  # 指定调度到具有nodeenv=pro标签的节点
```

## P40-Pod详解-亲和性调度-概述

优先选择满足条件的node，如果没有，也可以调度到不满足条件的节点上，使调度更加灵活。

3类：

- nodeAffinity(node亲和性)：以node为目标，解决pod可以调度到哪些node的问题。
- podAffinity（pod亲和性): 以pod为目标，解决pod可以和哪些已存在的pod部署在同一个拓扑域中的问题。
- podAntiAffinity(pod反亲和性)：以pod为目标，解决pod不能和哪些已存在pod部署在同一个拓扑域中的问题。

> 关于亲和性（反亲和性）使用场景的说明：
>
> **亲和性：**如果两个应频繁交互，那就有必要利用亲和性让两个应用的尽可能的靠近，这样可以减少因网络通信而带来的性能损耗。
>
> **反亲和性：**当应用的采用多副本部署时，有必要采用反亲和性让各个应用实例打散分布在各个node上，这样可以提高服务的高可用性。

## P41-Pod详解-亲和性调度-nodeAffinity

### 5.4.2  亲和性高度

**NodeAffinty**

- requiredDuringSchedulingIgnoredDuringExecution  **硬限制**
- preferredDuringSchedulingIgnoredDuringExecution  **软限制**

```yaml
kubectl explain pod.spec.affinity.nodeAffinity

requiredDuringSchedulingIgnoredDuringExecution	<Object>	# node节点必须满足指定的所有规则才可以，相当于硬限制
  nodeSelectorTerms		#节点选择列表
    matchFields 		#按节点字段列出的节点选择器要求列表
    matchExpressions	#按节点标签列出的节点选择器要求列表（推荐）
      key 
      values
      operator		# 关系符。 支持Exists, DoesNotExist, In,, NotIn, Gt, Lt

preferredDuringSchedulingIgnoredDuringExecution	<[]Object>	# 优先调度到满足指定的规则的node，相当于软限制（倾向）
  preference  #一个节点选择器项，与相应的权重相关联
    matchFields 		#按节点字段列出的节点选择器要求列表
    matchExpressions	#按节点标签列出的节点选择器要求列表（推荐）
      key 
      values
      operator		# 关系符。 支持Exists, DoesNotExist, In,, NotIn, Gt, Lt
  weight	# 倾向权重，在1~100范围

```

```yaml
# 关系符的使用说明：
- matchExpressions:
  - key: nodeenv	# 匹配存在标签的key为nodeenv的节点
    operator: Exists
  - key: nodeenv	# 匹配标签的key为nodeenv, 且value是"xxx"或"yyy" 的节点
    operator: In
    values: ["xxx","yyy"]
  - key: nodeenv	# 匹配标签的key为nodeenv，且value大于"xxx"的节点
    operator: Gt
    value: "xxx"
```

pod-nodeaffinity-required.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: pod-nodeaffinity-required
  namespace: dev
spec:
  containers:
  - name: nginx
    image: nginx:1.17.1
  affinity:	# 亲和性设置
    nodeAffinity: 	#设置node亲和性
      requiredDuringSchedulingIgnoredDuringExecution:  #硬限制 
        nodeSelectorTerms:
        - matchExpressions: 	#匹配env的值在["xxx","yyy"]中的标签
          - key: nodeenv
            operator: In
            value: ["xxx","yyy"]
```

```sh
# 看node标签
kubectl get nodes --show-labels
```

pod-nodeaffinity-preferred.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: pod-nodeaffinity-preferred
  namespace: dev
spec:
  containers:
  - name: nginx
    image: nginx:1.17.1
  affinity:	# 亲和性设置
    nodeAffinity: 	#设置node亲和性
      preferredDuringSchedulingIgnoredDuringExecution:  #硬限制 
      - weight: 1
        preference:
          matchExpressioins:  #匹配env的值在["xxx","yyy"]中的标签(当前环境中没有)
          - key: nodeenv
            operator: In
            values: ["xxx","yyy"]
```

## P42-Pod详解-亲和性调度-podAffinity

podAffinity主要实现以运行的pod为参照，实现让新创建的Pod跟参照pod在一个区域的功能。

- requiredDuringSchedulingIgnoredDuringExecution  硬限制
- preferredDuringSchedulingIgnoredDuringExecution  软限制

```yaml
kubectl explain pod.spec.affinity.podAffinity

requiredDuringSchedulingIgnoredDuringExecution	<Object>	# 硬限制
  namespaces 	#指定参照pod的namespace
  topologyKey	# 指定调度作用域
  labelSelector	#标签选择器
    matchExpressions	#按节点标签列出节点选择器要求列表（推荐）
      key
      values
      operator	# 关系符
    matchLabels	# 指多个matchExpressions 映射的内容

preferredDuringSchedulingIgnoredDuringExecution	<[]Object>	# 软限制（倾向）
  podAffinityTerm 	#选项
    namespaces
    topologyKey
    labelSelector
      matchExpressions
        key
        values
        operator
      matchLabels
    weight  #权重
```

topologyKey用于指定调度时作用域，例如：

- 如果指定为kubernates.io/hostname，那就是以node节点为区分范围。
- 如果指定为beta.kubernates.io/os，则以node节点的操作系统类型来区分

硬限制 pod-podaffinity-targeet.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: pod-podaffinity-target
  namespace: dev
  labes:
    podenv: pro  # 设置标签
spec:
  containers:
  - name: nginx
    image: nginx:1.17.1
  nodeName: node1	# 将目标pod名确定到node1上
```

pod-podaffinity-required.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: pod-podaffinity-required
  namespace: dev
spec:
  containers:
  - name: nginx
    image: nginx:1.17.1
  affinity: 	# 亲和性
    podAffinity:	# 设置pod亲和性
      requiredDuringSchedulingIgnoredDuringExecution:  # 硬限制 
      - labelSelector:
          matchExpressions: # 匹配env的值在["xxx","yyy"]中的标签
          - key: podenv
            operator: In
            values: ["xxx","yyy"]
        topologyKey: kubernates.io/hostname
```

上面配置表达的意思是：新pod必须要与拥有标签nodeenv=xxx或者nodeenv=yyy的pod在同一个node上，显然现在没有这样的pod。

## P43-Pod详解-亲和性调度-podAntiAffinity

与affinity的配置是一样的

pod-podatiaffinity-required.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: pod-podantiaffinity-required
  namespace: dev
spec:
  containers:
  - name: nginx
    image: nginx:1.17.1
  affinity: 	# 亲和性
    podAntiAffinity:	# 设置pod亲和性
      requiredDuringSchedulingIgnoredDuringExecution:  # 硬限制 
      - labelSelector:
          matchExpressions: # 匹配env的值在["pro"]中的标签
          - key: podenv
            operator: In
            values: ["pro"]
        topologyKey: kubernates.io/hostname
```

新的pod必须要与拥有标签nodeenv=pro的pod在不同的node上。

## P44-Pod详解-调度-污点

### 5.4.3  污点和容忍

#### 污点(Taints)

node被设置上污点之后就和pod之间存在了一种相斥的关系，进而拒绝高度进来，甚至可以将已存在的pod驱逐出去。

<font color='red'>以node的角度来看</font>

污点的格式为：key=value:effect，key和value是污点的标签，effect描述污点的作用，支持如下3个选项：

- PreferNoSchedule: k8s将尽量避免把pod高度到具有该污点的node上，除非没有其他节点可高度。
- NoSchedule：k8s将不会把pod调度到具有该污点的node上，但不会影响当前node上已存在的pod。
- noExecute：k8s将不会把pod调度到具有该污点的node上，同时民会将node上已存在的pod驱离。

```sh
# 设置污点
kubectl taint nodes node1 key=value:effect

# 去除污点
kubectl taint nodes node1 key:effect-

# 去除所有污点
kubectl taint nodes node1 key
```

案例：

1. 准备node1（为了演示效果更加明显，暂时停止node2节点）
2. 为node1节点设置一个污点：tag=heima:PreferNoSchedule；然后创建pod1（可以）
3. 修改node1节点设置一个污点：tag=heima:NoSchedule；然后创建pod2（pod1正常，pod2失败）
4. 修改为node1节点设置一个污点：tag=heima:NoExecute；然后创建pod3

```sh
# 案例
# 为node1调置污点（PreferNoSchedule）
kubectl taint nodes node1 tag=heima:PreferNoSchedule

# 创建pod1
kubectl run taint1 --image=nginx:1.17.1 -n dev
kubectl get pods -n dev -o wide

# 为node1设置污点（取消PreferNoSchedule，设置NoSchedule）
kubectl taint nodes node1 tag:PreferNoSchedule-
kubectl taint nodes node1 tag=heima:NoSchedule

# 创建pod2
kubectl run taint2 --image=nginx:1.17.1 -n dev
kubectl get pods taint2 -n dev -o wide
NAME                      READY   STATUS    RESTARTS   AGE    IP             NODE     NOMINATED NODE   READINESS GATES
taint1-766c47bf55-592xq   1/1     Running   0          20m    10.244.3.130   n1       <none>           <none>
taint2-84946958cf-6pznh   0/1     Pending   0          4m9s   <none>         <none>   <none>           <none>

# 重制污点策略
kubectl taint nodes node1 tag:NoSchedule-
node/n1 untainted
kubectl taint nodes node1 tag=heima:NoExecute

# 创建pod3
kubectl run taint3 --image=nginx:1.17.1 -n dev
kubectl get pods -n dev -o wide
NAME                      READY   STATUS    RESTARTS   AGE   IP       NODE     NOMINATED NODE   READINESS GATES
taint1-766c47bf55-zkg9v   0/1     Pending   0          32s   <none>   <none>   <none>           <none>
taint2-84946958cf-s5bdn   0/1     Pending   0          32s   <none>   <none>   <none>           <none>
taint3-57d45f9d4c-dmsz5   0/1     Pending   0          4s    <none>   <none>   <none>           <none>

```

> 提示：使用kubeadm搭建集群，默认就会给master节点添加一个污点标记，所以pod就不会调度到master节点上。

## P45-Pod详解-调度-容忍

#### 容忍 Toleration

将pod调度到一个有污点的节点上。

> 污点就是拒绝，容忍就是忽略，Node通过污点拒绝pod调度上去，pod通过容忍忽略拒绝。

pod-toleration.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: pod-toleration
  namespace: dev
spec:
  containers:
  - name: nginx
    image: nginx:1.17.1
  tolerations:
  - key: "tag"
    operator: "Equal" 	# 操作符
    value: "heima"		# 容忍污点的value
    effect: "NoExecute" 	#添加容忍的规则，这里必须和标记的污点规则相同
```

详细配置

```sh
kubectl explain pod.spec.tolerations

FIELDS:
  key	# 对应要容忍的污点的键（空意味着匹配所有的键）
  value		# 对应要容忍的污点的值
  operator	# key-value的运算符。支持Equal和Exists（默认）。（exist存在该键，不用配value了）
  effect	# 对应污点的effect（空意味着匹配所有影响）
  tolerationSeconds	#容忍时间，当effect为NoExecute时生效，表示pod在node上停留的时间。
```

# 第6章 控制器详解

## P46-Pod控制器--概述

pod的创建方式可以分为2类：

- 自主式pod: k8s直接创建出来的pod，这种pod删除后就没有了，也不会重建。
- 控制器创建的pod: 通过控制器创建的pod, 这种pod删除了之后还会自动重建。

### 6.1 pod控制器介绍

**什么是pod控制器**：

pod控制器是管理pod的中间层，使用了pod控制器之后，我们只需要告诉pod控制器，想要多少个什么样的pod就可以了。它就会创建出满足条件的pod并确保每一个pod处于用户期望的状态，如果pod在运行中出现故障，控制器会指定策略重启动或者重建pod。

**pod控制器类型：**

- ReplicationController: 比较原始的pod控制器，已经被废弃，由ReplicaSet替代
- ReplicaSet: 保证指定数量的pod运行，并支持pod数量变量，镜像版本变更
- <font color='red'>Deployment</font>: 通过控制ReplcaSet来控制pod，并支持滚动升级、版本回退
- Horizontal Pod AutoScaler: 可以根据集群负载自动调整pod数量，实现削峰填谷
- DaemonSet: 在集群中的指定node上都运行一个副本，一般用于守护进程类的任务。
- Job: 它创建出来的pod只要完成任务就立即退出，用于执行一次性任务
- Cronjob: 它创建的pod会周期性的执行，用于执行周期性任务
- StatefulSet: 管理有状态应用

## P47-Pod控制器--ReplicaSet

### 6.2 ReplicaSet（RS）

主要作用是保证一定数量的pod能够正常运行，它会持续监听这些pod的运行状态，一旦pod发生帮障，就会重启或重建。同时它还支持对pod数量的扩缩容和版本镜像的升级。

```yaml
apiVersion: v1
kind: ReplicaSet
metadata: 
  name: 
  namespace: dev
  labels:
    controller: rs
spec:
  replicas: 3
  selector:	# 选择器，通过它指定该控制器管理哪些pod
    matchLabels:	#labels匹配规则 
      app: nginx-pod
    matchExpressions:	# Expressions匹配规则
    - {key:app, operator:In, values:[nginx-pod]}
  template:	# 模板，当副本数量不足时，会要据下面的模板创建pod副本
    metadata:
    labels:
      app: nginx-pod
    spec:
      containers:
      - name: nginx
        image: nginx:1.17.1
        ports:
        - containerPort: 80
```

#### 创建

pc-replicaset.yaml

```yaml
apiVersion: apps/v1
kind: ReplicaSet
metadata: 
  name: pc-replicaset
  namespace: dev
spec:
  replicas: 3
  selector:	# 选择器，通过它指定该控制器管理哪些pod
    matchLabels:	#labels匹配规则 
      app: nginx-pod
  template:	# 模板，当副本数量不足时，会要据下面的模板创建pod副本
    metadata:
      labels:
        app: nginx-pod
    spec:
      containers:
      - name: nginx
        image: nginx:1.17.1
```

```sh
kubectl get rs pc-replicaset -n dev -o wide
# DESIRED  希望数
# CURRENT  当前数
# READY    已经准备好提供服务的副本数量

# 查看当前控制器创建出来的pod
# 控制器创建出来的pod的名称是控制器名称后面拼接的 -xxxx随机码
```

#### 扩缩容

```sh
# 编辑rs的副本数量，修改spec:replicas: 6即可
kubectl edit rs pc-replicaset -n dev

# 查看pod
kubectl get pods -n dev

# 命令实现
kubectl scale rs pc-replicaset --replicas=2 -n dev
```

#### 镜像升级

```sh
# 编辑
kubectl edit rs pc-replicaset -n dev

# 命令
kubectl set image rs rs_name 容器=镜像版本 -n namespace
kubectl set image rs pc-replicaset nginx=nginx:1.17.1 -n dev
```

#### 删除ReplicaSet

```sh
# k8s删除rs前，会将replicasclear调整为0，等待所有pod被删除后，再执行rs对象删除
kubectl delete rs pc-replicaset -n dev

# 如果要删除ReplicaSet(保留pod)，可用kubectl delete 时加--cascade=false（不推荐）
kubectl delete rs pc-replicaset -n dev --cascade=false

#yaml 删除（推荐）
kubectl delete -f pc-replicaset.yaml
```

## P48-Pod控制器--Deployment-基础

```
为了更好的解决服务器编排的问题，k8s在v1.2开始，引入Deployment控制器。值得一提的是，这种控制器并不直接管理pod，而是通过管理ReplicaSet来间接管理pod。Deployment的功能更强大。
```


主要功能有：

- 支持ReplicaSet的所有功能
- 支持发布的停止、继续
- 支持版本滚动升级和版本回退

Deployment的资源清单文件：

```yaml
apiVersion: v1
kind: Deployment
metadata: 
  name: 
  namespace: dev
  labels:
    controller: deploy
spec:
  replicas: 3
  revisionHistoryLimit: 3	#保留历史版本，默认是10
  paused: false  # 暂停部署，默认是false
  progressDeadlineSeconds: 600  #部署时间(s)，默认是10
  strategy:
    type: RollingUpdate	# 滚动更新策略
    rollingUpdate:
      maxSurge: 30% 	# 最大额外可以存在的副本数，可以为百分比，也可以为整数 
      maxUnavailable: 30% # 最大不可用状态的pod的最大值，可以为百分比，也可以为整数 
  selector:	# 选择器
    matchLabels: # labels匹配规则 
      app: nginx-pod
    matchExpressions:	# Expressions匹配规则
    - {key:app, operator:In, values:[nginx-pod]}
  template:	# 模板，当副本数量不足时，会要据下面的模板创建pod副本
    metadata:
    labels:
      app: nginx-pod
    spec:
      containers:
      - name: nginx
        image: nginx:1.17.1
        ports:
        - containerPort: 80
```

#### 创建

pc-deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata: 
  name: pc-deployment
  namespace: dev
spec:
  replicas: 3
  selector:	# 选择器，通过它指定该控制器管理哪些pod
    matchLabels:	#labels匹配规则 
      app: nginx-pod
  template:	# 模板，当副本数量不足时，会要据下面的模板创建pod副本
    metadata:
      labels:
        app: nginx-pod
    spec:
      containers:
      - name: nginx
        image: nginx:1.17.1
```

```sh
# 创建
# --record=true  记录每次的版本变化 
kubectl create -f pc-deploymnet.yaml --record=true

# 查看deployment
# UP-TO-DATE 最新版本的pod数量 
# AVAILABLE 当前可用的pod数量 
kubectl get deploy -n dev -o wide

# 查看rs
kubectl get rs -n dev -o wide	# 有10位随机码

# 查看pod
kubectl get pod -n dev	# 有10位rs及5位的随机码
```

## P49-Pod控制器--Deployment-扩缩容

## P50-Pod控制器--Deployment-升级策略

#### 镜像更新

策略：**重建更新**、**滚动更新**（默认）。通过strategy进行配置

```yaml
strategy: 指定新的pod替换旧的pod的策略，支持2个属性：
  type: 指定策略类型
    Recreate: # 在创建出新的pod之前会先杀掉所有已存在的pod
    RollingUpdate: # 滚动更新，就是杀死一部分，就启动一部分，在更新过程中，存在两个版本pod
    # rollingUpdate: 当type为RollingUpdate时生效，有2个属性：
      maxUnavailable: #用来指定在升级过程中不可用pod的最大数量，默认为25%
      maxSurge: # 用来指定在升级过程中可以超过期望的pod的最大数量，默认为25% 
```

##### **重建更新**

pc-deployment.yaml

```yaml
spec:
  strategy: # 策略
    type: Recreate  # 重建更新策略
```

```sh
# 变更镜像
kubectl apply -f pc-deployment.yaml
kubectl set image deployment pc-deployment nginx=nginx:1.17.2 -n dev

# 观察升级过程
kubectl get pods -n dev -w
```

##### 滚动更新

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: pc-deployment
  namespace: dev
spec:
  strategy:
    type: RollingUpdate # 滚动更新，就是杀死一部分，就启动一部分，在更新过程中，存在两个版本pod
    rollingUpdate: # 当type为RollingUpdate时生效，有2个属性：
      maxUnavailable: 25% #用来指定在升级过程中不可用pod的最大数量，默认为25%
      maxSurge: 25%  # 用来指定在升级过程中可以超过期望的pod的最大数量，默认为25%
  replicas: 3
  selector:     # 选择器，通过它指定该控制器管理哪些pod
    matchLabels:        #labels匹配规则 
      app: nginx-pod
  template:     # 模板，当副本数量不足时，会要据下面的模板创建pod副本
    metadata:
      labels:
        app: nginx-pod
    spec:
      containers:
      - name: nginx
        image: nginx:1.17.1

```

```sh
# 变更镜像
kubectl apply -f pc-deployment.yaml
kubectl set image deployment pc-deployment nginx=nginx:1.17.2 -n dev

# 观察升级过程
kubectl get pods -n dev -w
kubectl get rs -n dev -w 	# 新的rs代替老的rs
```

## P51-Pod控制器--Deployment-版本回退

```sh
kubectl create -f pc-deployment.yaml --record
kubectl get rs -n dev -w 	# 新的rs代替老的rs

kubectl get rs -n dev
# 查看rs， 发现原来的rs依旧存在，只是pod数量变为0，而后又亲产生了一个rs，pod数量为3
# 其实这就是deployment能够进行版本回退的奥妙
```

#### 版本回退

deployment支持升级过程中的暂停、继续功能以及版本回退等诸多功能

kubectl rollout 支持下面选项：

- status  显示当前升级状态
- history  显示升级历史记录
- pause  暂停版本升级过程
- resume 继续已经暂停的版本升级过程
- restart  重启版本升级过程
- undo  回滚到上一级版本（可以使用--to-revision回滚到指定版本）

```sh
# 查看当前升级版本的状态
kubectl rollout status deploy pc-deployment -n dev

# 查看升级历史记录
kubectl rollout history deploy pc-deployment -n dev
REVISION  CHANGE-CAUSE
1         kubectl create --filename=pc-deployment.yaml --record=true	# create时必须带--record
2         kubectl create --filename=pc-deployment.yaml --record=true
3         kubectl create --filename=pc-deployment.yaml --record=true
# 版本回滚
# 这里直接使用--to-revision=1 回滚到1版本，如果省略这个选项，就是回退到上个版本，就是2版本
kubectl rollout undo deployment pc-deployment --to-revision=1 -n dev
```

## P52-Pod控制器--Deployment-金丝雀发布

#### 金丝雀发布

```
Deployment支持更新过程中的控制，如“暂停（pause)”或“继续(resume)"更新操作
```


```
比如有一批新的pod资源创建完成后立即暂停更新过程，此时，仅存在一部分新版本的应用，主体部分还是旧的版本。然后，再筛选一小部分的用户请求路由到新版本的pod应用，继续观察能否稳定地按期望的方式运行。确定没问题之后再继续完成余下的pod资源滚动更新，否则立即回滚更新操作。这就是金丝雀发布。
```


```sh
# 更新版本，并暂停
kubectl set image deploy pc-deployment nginx=nginx:1.17.3 -n dev && kubectl rollout pause deployment pc-deployment -n dev

# 观察更新的状态
kubectl rollout status deploy pc-deployment -n dev
Waiting for deployment "pc-deployment" rollout to finish: 1 out of 3 new replicas have been updated...

# 监控更新的过程，可以看到已经新增了一个资源，但是并未按照预期的状态去删除一个旧的资源，就是因为使用了pause暂停命令
kubectl get rs -n dev -o wide
kubectl get pod -n dev

# 确保更新的pod没有问题，继续更新
kubectl rollout resume deploy pc-deployment -n dev

# 查看最后的更新情况
kubectl get rs -n dev -o wide
pc-deployment-5d89bdfbf9   0         0         0       25m    nginx        nginx:1.17.2   app=nginx-pod,pod-template-hash=5d89bdfbf9
pc-deployment-675d469f8b   3         3         3       24m    nginx        nginx:1.17.3   app=nginx-pod,pod-template-hash=675d469f8b
```

#### 删除Deployment

```sh
kubectl delete -f pc-deployment.yaml
```

## P53-Pod控制器--HPA-上

### 5.4 HPA（Horizontal Pod Autoscaler）

```
k8s期望可以通过监测pod的使用情况，实现pod数量的自动调整，于是就产生了HPA控制器。
```


```
HPA可以获取每个pod利用率，然后和HPA中定义的指标进行对比，同时计算出需要伸缩的具体值，最后实现pod数量的调整。其实HPA与之前的Deployment一样，也属于一种k8s资源对象，它通过追踪分析目标pod负载变化情况，来确定是否需要针对性地调整目标pod副本数。
```


#### 1. 安装metrics-server

```sh
# 安装 git
yum install git -y
#获取metrics-server，注意使用的版本
git clone -b v0.3.6 https://github.com/kubenetes-incubator/metrics-server
#修改deployment, 注意修改的是镜像和初始化参数
cd /root/metrics-server/deploy/1.8+
vim metrics-server-deployment.yaml
```

```yaml
hostNetwork: true
image: registry.cn-hangzhou.aliyuncs.com/google_containers/metrics-server-amd64:v0.3.6
imagePullPolicy: Always
args:
- --kubelet-insecure-tls
- --kubelet-preferred-address-types=InternalIP,Hostname,InternalDNS,ExternalDNS,ExternalIP
```

metrics-server-deployment.yaml

```yaml

---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: metrics-server
  namespace: kube-system
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: metrics-server
  namespace: kube-system
  labels:
    k8s-app: metrics-server
spec:
  selector:
    matchLabels:
      k8s-app: metrics-server
  template:
    metadata:
      name: metrics-server
      labels:
        k8s-app: metrics-server
    spec:
      hostNetwork: true
      serviceAccountName: metrics-server
      volumes:
      # mount in tmp so we can safely use from-scratch images and/or read-only containers
      - name: tmp-dir
        emptyDir: {}
      containers:
      - name: metrics-server
        image: registry.cn-hangzhou.aliyuncs.com/google_containers/metrics-server-amd64:v0.3.6
        imagePullPolicy: Always
        args:
        - --kubelet-insecure-tls
        - --kubelet-preferred-address-types=InternalIP,Hostname,InternalDNS,ExternalDNS,ExternalIP
        volumeMounts:
        - name: tmp-dir
          mountPath: /tmp
```

```sh
#安装metrics-server
kubectl apply -f ./

#查看pod运行情况
kubectl get pod -n kube-system

#使用kubectl top node 想提资源使用情况
kubectl top node
NAME   CPU(cores)   CPU%   MEMORY(bytes)   MEMORY%   
m1     150m         3%     1056Mi          61%     
n1     29m          1%     924Mi           53%     
n2     29m          1%     809Mi           47% 

kubectl top pod -n kube-system
NAME                              CPU(cores)   MEMORY(bytes)   
coredns-6955765f44-6rzqx          0m           0Mi           
coredns-6955765f44-ztfz7          5m           5Mi           
etcd-m1                           19m          80Mi          
kube-apiserver-m1                 47m          323Mi         
kube-controller-manager-m1        29m          65Mi  
...
# 至此，metrics-server安装完成
```

#### 2 准备deployment和service

```sh
# 创建deployment
kubectl run nginx --image=nginx:1.17.1 --requests=cpu=100m -n dev

# 创建service
kubectl expose deployment nginx --type=NodePort --port=80 -n dev

#查看
kubectl get deployment , pod, svc -n dev
[root@m1 1.8+]# kubectl get deployment,pod,svc -n dev
NAME                    READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/nginx   1/1     1            1           4m18s

NAME                         READY   STATUS    RESTARTS   AGE
pod/nginx-778cb5fb7b-rbv7z   1/1     Running   0          4m18s

NAME            TYPE       CLUSTER-IP      EXTERNAL-IP   PORT(S)        AGE
service/nginx   NodePort   10.107.75.107   <none>        80:31716/TCP   34s
```

## P54-Pod控制器--HPA-下

#### 3. 部署HPA

pc-hpa.yaml

```yaml
apiVersion: autoscaling/v1
kind: HorizontalPodAutoscaler
metadata:
  name: pc-hpa
  namespace: dev
spec:
  minReplicas: 1  #最小pod数量 
  maxReplicas: 10 # 最大pod数量 
  targetCPUUtilizationPercentage: 3	# cpu使用率指标
  scaleTargetRef: 	# 指定要控制的nginx信息
    apiVersion: apps/v1
    kind: Deployment
    name: nginx		# 管理一个叫nginx的Deployment

```

```sh
# 创建hpa
kubectl create -f pc-hpa.yaml

# 查看hpa
kubectl get hpa -n dev
NAME     REFERENCE          TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
pc-hpa   Deployment/nginx   0%/3%     1         10        1          30s
```

#### 4. 压测

用postman测

```sh
# 观查pod的变化数
kubectl get hpa -n dev -w
NAME     REFERENCE          TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
pc-hpa   Deployment/nginx   0%/3%     1         10        1          10m
pc-hpa   Deployment/nginx   25%/3%    1         10        1          24m
pc-hpa   Deployment/nginx   25%/3%    1         10        4          24m
pc-hpa   Deployment/nginx   25%/3%    1         10        8          24m
pc-hpa   Deployment/nginx   25%/3%    1         10        9          24m
pc-hpa   Deployment/nginx   0%/3%     1         10        9          25m

kubectl get pod -n dev -w 
kubectl get deploy -n dev -w
```

## P55-Pod控制器--DaemonSet

### 5.5 DaemonSet（DS）

```
DaemonSet类型的控制器可以保证集群中的每一台（或指定）节点上都运行一个副本，一般适用于日志收集，节点监控等场景。也就是说，如果一个pod提供的功能是节点级别的（每个节点都需要且只需要一个），那么这类pod就适合使用DaemonSet类型的控制哭喊创建。
```


特点：

- 每当向集群中添加一个节点时，指定的pod副本也将添加到该节点上
- 当节点从集群中移除时，pod也就被垃圾回收了

资源清单

```yaml
spec:
  revisionHistoryLimit: 3  # 保留历史版本
  updateStrategy:	# 更新策略
    type: RollingUpdate #滚动更新策略
    rollingUpdate:
      maxUnavailable: 1	#最大不可用状态的pod的最大值，可以为百分比
```

pc-daemonset.yaml

```yaml
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: pc-daemonset
  namespace: dev
spec:
  selector:
    matchLabels:
      app: nginx-pod
  template:
    metadata:
      labels:
        app: nginx-pod
    spec:
      containers:
      - name: nginx
        image: nginx:1.17.1
```

```sh
# 创建daemonset
kubectl create -f pc-daemonset.yaml
# 查看daemonset
kubectl get ds -n dev -o wide
```

## P56-Pod控制器--Job

### 6.6 Job

```
主要负责
```

**批量处理**短暂的**一次性**任务。Job的特点：

- 当Job创建的Pod执行成功结束时，Job将记录成功结束的pod数量
- 当成功结束的pod达到指定的数量时，Job将完成执行

资源清单

```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: 
  namespace: 
spec:
  completions: 1	# 指定job需要成功支行pods的次数。默认为：1
  parallelism: 1	# 指定job在任一时刻应该并发运行pods的数量。默认值：1
  activeDeadlineSeconds: 30	# 指定job可运行的时间期限，超过时间还未结束，系统将会尝试进行终止。
  backoffLimit: 6	# 指定job失败后进行重试的次数。默认是6
  manualSelector: true	# 是否可以使用selector选择器选择pod, 默认是false
  selector: 	#选择器
    matchLabels:	# labels匹配规则 
      app: counter-pod
    matchExpressions: #Expressions匹配规则
      - {key: app, operator: In, values: [counter-pod]}
    template: # 模板，当副本数量不足时，会根据下面的模板创建pod副本
      metadata:
        labels:
          app: count-pod
      spec:
        restartPolicy: Never # 重启策略只能设置为Never或者OnFailure
        containers:
        - name: counter
          image: busybox:1.30
          command: ["bin/sh","-c","for i in 9 8 7 6 5 4 3 2 1; do echo $i;sleep 2; done"]
```

关于重启策略restartPolicy设置的说明：

- OnFailure, 则job会在pod出现故障时重启容器，而不是创建pod，failed次数不变
- Never，则job会在pod出现故障时创建新的pod，并且故障pod不会消失，也不会重启，failed次数加1
- Always，意味着一直重启，意味着job任务会重复去执行，当然不对，所以不能设置为always

创建pc-job.yaml

```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: pc-job
  namespace: dev
spec:
  manualSelector: true	# 是否可以使用selector选择器选择pod, 默认是false
  completions: 6	# 指定job需要成功支行pods的次数。默认为：1
  parallelism: 3	# 指定job在任一时刻应该并发运行pods的数量。默认值：1
  selector: 	#选择器
    matchLabels:	# labels匹配规则 
      app: counter-pod
  template:
    metadata:
      labels:
        app: counter-pod
    spec:
      restartPolicy: Never
      containers:
      - name: counter
        image: busybox:1.30
        command: ["bin/sh","-c","for i in 9 8 7 6 5 4 3 2 1; do echo $i;sleep 2; done"]    
```

## P57-Pod控制--CronJob

### 6.7 CronJob（CJ）

```
CronJob可以类似于Linux操作系统的周期性任务。
```


资源清单文件

```yaml
apiVersion: batch/v1beta1
kind: CronJob
metadata:
  name: 
  namespace: 
  labels: 
    controller: cronjob
spec:
  schedule: # cron格式的作业调试运行时间点，用于控制任务在什么时间执行
  concurrencyPolicy: # 并发执行策略，用于定义前一次作业执行尚未完成时是否以及如何运行后一次的作业
  failedJobHistoryLimit:	# 为失败的任务执行保留的历史记录数，默认为1
  successfulJobHistoryLimit:	# 为成功的任务执行保留的历史记录数，默认为3
  startingDeadlineSeconds: 	# 启动作业错误的超时时长
  jobTemplate: 	# job控制器模板，用于为cronjob控制器生成job对象；下面其实就是job的定义
    metadata:
    spec:
      completions: 1	# 指定job需要成功支行pods的次数。默认为：1
      parallelism: 1	# 指定job在任一时刻应该并发运行pods的数量。默认值：1
      activeDeadlineSeconds: 30	# 指定job可运行的时间期限，超过时间还未结束，系统将会尝试进行终止。
      backoffLimit: 6	# 指定job失败后进行重试的次数。默认是6
      manualSelector: true	# 是否可以使用selector选择器选择pod, 默认是false
      selector: 	#选择器
        matchLabels:	# labels匹配规则 
          app: counter-pod
        matchExpressions: #Expressions匹配规则
          - {key: app, operator: In, values: [counter-pod]}
        template: # 模板，当副本数量不足时，会根据下面的模板创建pod副本
          metadata:
            labels:
              app: count-pod
  
```

```sh
# schedule: cron表达式，用于指定伤务的执行时间
  */1		*		*		*		*
  <分钟>	<小时>	<日>    <月份>	   <星期>
  
 concurrencyPolicy:
   Allow: 允许jobs并发运行（默认）
   Forbid: 禁止并发运行，如果上一次运行尚未完成，则跳过下一次运行
   Replace: 替换，取消当前正在运行的作业并用新作业替换它
```

pc-cronjob.yaml

```yaml
apiVersion: batch/v1beta1
kind: CronJob
metadata:
  name: pc-cronjob
  namespace: dev
  labels: 
    controller: cronjob
spec:
  schedule: "*/1 * * * *"
  jobTemplate: 	# job控制器模板，用于为cronjob控制器生成job对象；下面其实就是job的定义
    metadata:
    spec:
      template: 
        spec:
          restartPolicy: Never
          containers:
          - name: counter
            image: busybox:1.30
            command: ["bin/sh","-c","for i in 9 8 7 6 5 4 3 2 1; do echo $i;sleep 2; done"]
```

```sh
#创建cronjob
kubectl create -f pc-cronjob.yaml

#查看cronjob
kubectl get cj -n dev

# 查看job
kubectl get jobs -n dev
```

# 第7章 服务详解

流量负载组件：Service和Ingress

- Service：4层路由的负载
- Ingress：7层路由的负载

## P58-Service--概述

### 7.1 Service介绍

```
pod的IP地址不是固定的。所以k8s提供Service资源。Service对同一个服务的多个pod进行聚合，并且提供一个统一的入口地址。通过访问service的入口地址就访问后面的pod服务。
```


```
Serice在很多情况下只是一个概念，真正起作用的是kube-proxy服务进程，每个node节点上都运行着一个kube-proxy服务进程。当创建Service的时候会通过api-server向etcd写入创建的service的信息，而kube-proxy会基于监听的机制发现这种Service的变动，然后
```

**它会将最新的service信息转换成对应的访问规则**。

```sh
ipvsadm -Ln
# kube-proxy会基于（轮询）的策略，将请求分发到其中一个pod上去

```

3种工作模式：

- **userspace模式**

  ```
  该模式下，kube-proxy会为每一个service创建一个监听端口，发向cluster IP的请求被Iptables规则重定向到kube-proxy监听的端口上，kube-proxy根据LB算法选择一个提供服务的pod并和其建立链接，以将请求转发到pod上。
  ```


  ```
  该模式下，kube-proxy充当一个四层负责均衡器的角色。由于Kube-proxy运行在userspace中，在进行转发处理时会增加内核和用户之间的数据拷贝，虽然比较稳定，但是效率比较低。
  ```


  ![image-20210420140411460](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210420140411460.png)
- **iptables模式**

  ```
  iptables模式下，kube-proxy为service
  ```

  **后端的每个pod创建对应的iptables规则**，直接将发向cluster IP的请求重定向到一个Pod IP。

  ```
  该模式下Kube-proxy不承担四层负责均衡器的角色，只负责创建iptables规则。该模式的优点是较userspace模式效率更高，但不能提供灵活的LB策略，当后端pod不可用时也无法进重重试。（LB：负载均衡）
  ```


  ![image-20210420140505365](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210420140505365.png)
- <font color='red'>**ipvs模式**</font>

  ```
  ipvs模式和iptables类似，kube-prox监控pod的变化并创建相应的ipvs规则。ipvs相对iptables转发效率更高。除此以外，ipvs支持更多的LB算法。
  ```


  ![image-20210420141118449](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210420141118449.png)

  ```sh
  # 此模式必须安装ipvs内核模块，否则会降级为iptables
  # 开启ipvs
  kubectl edit cm kube-proxy -n kube-system	# 编辑cm，将mode改成ipvs
  kubectl delete pod -l k8s-app=kube-proxy -n kube-system
  ipvsadm -Ln	# 新建一批规则
  IP Virtual Server version 1.2.1 (size=4096)
  Prot LocalAddress:Port Scheduler Flags
    -> RemoteAddress:Port           Forward Weight ActiveConn InActConn
  TCP  127.0.0.1:31274 rr		# rr 轮询
    -> 10.244.3.138:8111            Masq    1      0          0       
    -> 10.244.4.23:8111             Masq    1      0          0       
    -> 10.244.4.24:8111             Masq    1      0          0       
  TCP  127.0.0.1:32434 rr
  TCP  172.17.0.1:31274 rr
    -> 10.244.3.138:8111            Masq    1      0          0       
    -> 10.244.4.23:8111             Masq    1      0          0       
    -> 10.244.4.24:8111             Masq    1      0          0  
    ...
  ```

## P59-Service--资源清单文件介绍

### 7.2 Service类型

service资源清单

```yaml
kind: Service
apiVersion: v1
metadata: 
  name: service
  namespace: dev
spec:
  selector: 	# 标签选择器，用于确定当前service代理哪些pod
    app: nginx
  type: #service类型，指定service的访问方式
  clusterIP:  #虚拟服务的ip地址
  sessioinAffinity:	#session亲和性，支持ClientIP, None两个选项
  ports: 
  - protocol: TCP
    port: 3017	# service端口
    targtPort: 5003	# pod端口
    nodePort: 31122	# 主机端口
```

service类型：

- ClusterIP：默认值，它是k8s系统自动分配的虚拟IP，只能在集群内部访问
- NodePort：将service通过指定的node上的端口暴露给外部，通过此方法，就可以在集群外部访问服务
- LoadBalancer：使用外接负载均衡器完成到服务的负载分发，注意此模式需要外部云环境支持。
- ExternalName：把集群外部的服务引入集群内部，直接使用。

## P60-Service--实验--环境准备

### 7.3 Service使用

#### 7.3.1 实验环境准备

deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: pc-deployment
  namespace: dev
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx-pod
  template:
    metadata:
      labels:
        app: nginx-pod
    spec:
      containers:
      - name: nginx
        image: nginx:1.17.1
        ports:
        - containerPort: 80
```

```sh
kubectl create -f deployment.yaml

# 查看pod详情
kubectl get pods -n dev -o wide --show-labels
NAME                             READY   STATUS    RESTARTS   AGE   IP             NODE   NOMINATED NODE   READINESS GATES   LABELS
pc-deployment-6696798b78-5j4md   1/1     Running   0          11s   10.244.4.66    n2     <none>           <none>            app=nginx-pod,pod-template-hash=6696798b78
pc-deployment-6696798b78-dpvkg   1/1     Running   0          11s   10.244.4.67    n2     <none>           <none>            app=nginx-pod,pod-template-hash=6696798b78
pc-deployment-6696798b78-kmg46   1/1     Running   0          11s   10.244.3.160   n1     <none>           <none>            app=nginx-pod,pod-template-hash=6696798b78

# 为了方便后面的测试，修改下三台nginx的index.html页面（三台修改的IP地址不一致）
kubectl exec -it pc-deployment-66cb59984-8p84h -n dev /bin/sh
echo '10.244.1.40' > /usr/share/nginx/html/index.html

# 修改之后，访问测试
```

## P61-Service--实验--ClusterIP类型

#### 7.3.2 ClusterIP类型的Service

service-clusterip.yaml

```yaml
apiVersion: v1
kind: Service
metadata:
  name: service-clusterip
  namespace: dev
spec:
  selector: 
    app: nginx-pod
  clusterIP: 10.97.97.97	# service的ip地址，如果不写，默认会生成一个
  type: ClusterIP
  ports:
  - port: 80	# Service 端口
    targetPort: 80 	#pod端口
```

```sh
kubectl create -f service-clusterip.yaml

# 查看service
kubectl get svc -n dev -o wide
NAME                TYPE        CLUSTER-IP    EXTERNAL-IP   PORT(S)   AGE   SELECTOR
service-clusterip   ClusterIP   10.97.97.97   <none>        80/TCP    11s   app=nginx-pod

# 查看service的详细信息
kubectl describe svc service-clusterip -n dev
```

**Endpoint**

```
Endpoint是K8s中的一个资源对象，存储在etcd中，用来记录一个service对应的所有pod的访问地址，它是根据service三围文件中selector爬藤榕产生的。
```


```
一个service由一组pod组成，这些pod通过Endpoints暴露出来，Endpoints是实现实际服务的
```

**端点集合**。换句话说，service和pod之间的联系是通过Endpoints实现的。

```sh
kubectl get endpoints -n dev
ipvsadm -Ln  # 查看映射规则（rr 轮询）

# 循环访问测试
while true;do curl 10.97.97.97:80; sleep 5; done;
# 会轮询到各个服务器
```

**负载分发策略**

```
对service的访问被 分发到了后端的pod上，目前k8s提供了两种负载分发策略：
```


- 如果不定义，默认使用kube-proxy的策略，比较随机、轮询
- 基于客户端地址的会话保持模式，即来自同一个客户端发起的所有请求都会转发到固定的一个pod上

  些塔式可以在spec中添加 sesssionAffinity: ClientIP选项

修改发分策略

```sh
# 修改发分策略 ----sessionAffinity:ClientIP
vim service-clusterip.yaml
# 查看ipvs规则（persistent 代表持久）
ipvsadm -Ln

curl 10.97.97.97:80
```

## P62-Service--实验--HeadLiness类型

#### 7.3.3  HeadLiness 类型的Service

```
在某些场景中，开发人员可能不想使用Service提供的负载均衡功能，而希望自已来控制负载均衡策略，针对这种情况，k8s提供了HeadLiness，这类service不会分配ClusterIP，如果想要访问service，只能通过service的域名进行查询。
```


service-headliness.yaml

```yaml
apiVersion: v1
kind: Service
metadata: 
  name: service-headliness
  namespace: dev
spec:
  selector:
    app: nginx-pod
  clusterIP: None	# 将clusterIP设置为None，即可创建headliness Service
  type: ClusterIP
  ports:
  - port: 80
    targetPort: 80
```

```sh
kubectl get svc -n dev
kubectl get pod -n dev

# 进入pod
kubectl exec -it pc-deployment-6696798b78-5j4md -n dev /bin/bash
cat /etc/resolv.conf
nameserver 10.96.0.10
search dev.svc.cluster.local svc.cluster.local cluster.local
options ndots:5

# 退出
exit

dig @10.96.0.10 service-headliness.dev.svc.cluster.local   #dig @10.96.0.10 service_name.dev.svc.cluster.local
```

## P63-Service--实验--NodePort类型

#### NodePort类型

```
如果希望将service暴露给集群外部使用，就要使用Nodeport类型。NodePort的工作原理其实就是将service的端口
```

**映射到Node的一个端口上**，然后就可以通过NodeIp:NodePort来访问serivce了。

创建service-nodeport.yaml

```yaml
apiVersion: v1
kind: Service
metadata: 
  name: service-nodeport
  namespace: dev
spec:
  selector:
    app: nginx-pod
  type: NodePort	# service类型
  ports:
  - port: 80
    nodePort: 30002	#绑定的node端口（默认取值为：30000~32767），如果不指定，会默认分配
    targetPort: 80
```

## P64-Service--实验--LoadBalancer类型

#### 6.3.5 LoadBalancer类型

```
与NodePort的区别在于，LoadBalancer会在集群的外部再来一个负载均衡设备，而这个设备需要外部环境支持的，外部服务发送到这个设备上的请求，会被设备负载之后转发到集群中。
```


## P65-Service--实验--ExternalName类型

#### 6.3.6 ExternalName类型的Service

```
ExternalName类型的Service用于引 入集群外部的服务，它通过externalName属性指定外部一个服务的地址，然后在集群内部访问此service就可以访问到外部的服务了。
```


<font color='red'>**外部服务引入到pod**</font>

service-externalname.yaml

```yaml
apiVersion: v1
kind: Service
metadata:
  name: service-externalname
  namespace: dev
spec: 
  type: ExternalName
  externalName: www.baidu.com	#改成ip地址也可以
```

```sh
kubectl create -f service-externalname.yaml

# 域名解析
dig @10.96.0.10 service-externalname.dev.svc.cluster.local
```

## P66-Ingress介绍

### 7.4 Ingress介绍

Service对集群之外暴露的主要方式有2种：NodePort和LoadBalancer，但这2种方式，都有一定的缺点：

- NodePort方式的缺点是会占用很多集群机器的端口，那么当集群服务变动很多的时候，这个缺点愈发明显。
- LB方式的缺点是每个service需要一个LB，浪费，麻烦，并且需要k8s之外设备的支持。

基于这种现状，k8s提供了Ingress资源对象，Ingress只需要一个nodePortt或者一个LB就可以满足暴露多个service的需求。

![image-20210422110458684](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210422110458684.png)

```
实际上，Ingress相当于一个7层负载均衡器，是以k8s对反向代理的一个抽象。它的工作原理类似于Nginx，可以理解成在
```

**Ingress里建立诸多映射规则，Ingress Controller通过监听这些配置规则并转化成Nginx的配置，然后对外部提供服务**。在这里有2个核心概念：

- Ingress: k8s中的一个对象，作用是定义请求如何转发到service的规则
- Ingress controller: 具体实现反向代理及负载均衡的程序，对Ingress定义的规则进行解析，根据配置的规则来实现请求转发，实现方式有很多，比如Nginx, Contour, Haproxy等。

Ingress（以Nginx为例）的工作原理如下：

1. 用户编写Ingress规则，说明哪个域名对应k8s集群中的哪个service
2. Ingress控制器动态感知Ingress服务规则的变化，然后生居一段对应的Nginx配置。
3. Ingress控制器会将生成的Nginx配置写入到一个运行着的Nginx服务中，并动态更新
4. 到此为止，其实真正在工作的就是一个Nginx了，内部配置了用户定义的请求转发规则

![image-20210422113902373](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210422113902373.png)

## P67-Ingress案例--环境准备

### 7.5  Ingress使用

#### 7.5.1 环境准备

**搭建Ingress环境**

```sh
# 创建文件夹
mkdir ingress-controller
cd ingress-controller

# 获取ingress-nginx, 本次使用0.30版本
wget https://github.com/kubernetes/ingress-nginx/tree/nginx-0.30.0/deploy/static/mandatory.yaml

wget https://github.com/kubernetes/ingress-nginx/blob/nginx-0.30.0/deploy/static/provider/baremetal/service-nodeport.yaml

# 修改mandatory.yaml文件中的仓库
# 修改quay.io/kuberntes-ingress-controller/nginx-ingress-controller:0.30.0
# 为registry.aliyuncs.com/google_containers/nginx-ingress-controller:0.30.0
# 创建ingress-nginx
kubectl apply -f ./

# 查看ingress-nginx
kubectl get pod -n ingress-nginx
NAME                                        READY   STATUS    RESTARTS   AGE
nginx-ingress-controller-7b86f6f9fc-8nkhr   1/1     Running   0          87s

# 查看service
kubectl get svc -n ingress-nginx
NAME            TYPE       CLUSTER-IP       EXTERNAL-IP   PORT(S)                      AGE
ingress-nginx   NodePort   10.101.158.154   <none>        80:31256/TCP,443:32175/TCP   108s
# 80是http对应端口，443是https对应端口
```

**准备service和pod**

tomcat-nginx.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
  namespace: dev
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx-pod
  template:
    metadata:
      labels:
        app: nginx-pod
    spec:
      containers:
      - name: nginx
        image: nginx:1.17.1
        ports:
        - containerPort: 80
      
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: tomcat-deployment
  namespace: dev
spec:
  replicas: 3
  selector:
    matchLabels:
      app: tomcat-pod
  template:
    metadata:
      labels:
        app: tomcat-pod
    spec:
      containers:
      - name: tomcat
        image: tomcat:8.5-jre10-slim
        ports:
        - containerPort: 8080
      
---

apiVersion: v1
kind: Service
metadata:
  name: nginx-service
  namespace: dev
spec:
  selector:
    app: nginx-pod
  clusterIP: None
  type: ClusterIP
  ports:
  - port: 80
    targetPort: 80
  
---

apiVersion: v1
kind: Service
metadata:
  name: tomcat-service
  namespace: dev
spec:
  selector:
    app: tomcat-pod
  clusterIP: None
  type: ClusterIP
  ports:
  - port: 8080
    targetPort: 8080
 
```

```sh
# 先重建下dev namespace
kubectl delete ns dev
kubectl create ns dev

# 创建
kubectl create -f tomcat-nginx.yaml
```

## P68-Ingress案例--http代理

#### 7.5.2 http代理

ingress-http.yaml

```yaml
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: ingress-http
  namespace: dev
spec:
  rules:
  - host: nginx.itheima.com
    http:
      paths:
      - path: /
        backend:
          serviceName: nginx-service
          servicePort: 80
  - host: tomcat.itheima.com
    http:
      paths:
      - path: /
        backend:
          serviceName: tomcat-service
          servicePort: 8080
  
```

```sh
# 创建
kubectl create -f ingress-http.yaml
# 查看
kubetl gt ing ingress-http -n dev
NAME           HOSTS                                  ADDRESS          PORTS   AGE
ingress-http   nginx.itheima.com,tomcat.itheima.com   10.101.158.154   80      8s

# 查看详情
kubectl describe ing ingress-http -n dev
Rules:
  Host                Path  Backends
  ----                ----  --------
  nginx.itheima.com   
                      /   nginx-service:80 (10.244.3.164:80,10.244.4.75:80,10.244.4.78:80)
  tomcat.itheima.com  
                      /   tomcat-service:8080 (10.244.3.163:8080,10.244.4.76:8080,10.244.4.77:8080)

# 修改主机的host规则
C:\Windows\System32\drivers\etc\hosts
192.168.132.31   nginx.itheima.com
192.168.132.31   tomcat.itheima.com

# 查看ingress服务对应的端口
[root@m1 ingress-controller]# kubectl get svc -n ingress-nginx
NAME            TYPE       CLUSTER-IP       EXTERNAL-IP   PORT(S)                      AGE
ingress-nginx   NodePort   10.101.158.154   <none>        80:31256/TCP,443:32175/TCP   63m

# 浏览器输入
http://nginx.itheima.com:31256
```

## P69-Ingress案例--https代理

创建证书

```sh
# 生成证书
openssl req -x509 -sha256 -nodes -days 365 -newkey rsa:2048 -keyout tls.key -out tls.crt -subj "/C=CN/ST=BJ/L=BJ/O=nginx/CN=itheima.com"

# 创建密钥
kubectl create secret tls tls-secret --key tls.key --cert tls.crt
```

创建ingress-https.yaml

```yaml
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: ingress-https
  namespace: dev
spec:
  tls:
    - hosts:
      - nginx.itheima.com
      - tomcat.ithima.com
      secretName: tls-secret	# 指定密钥
  rules:
  - host: nginx.itheima.com
    http:
      paths:
      - path: /
        backend:
          serviceName: nginx-service
          servicePort: 80
  - host: tomcat.itheima.com
    http:
      paths:
      - path: /
        backend:
          serviceName: tomcat-service
          servicePort: 8080
```

```sh
kubectl create -f ingress-https.yaml

# 查看
kubectl get ing ingress-https -n dev
NAME            HOSTS                                  ADDRESS   PORTS     AGE
ingress-https   nginx.itheima.com,tomcat.itheima.com             80, 443   20s

kubectl describe ing ingress-https -n dev
```

# 第8章 数据存储

## P70-数据存储介绍

为了持久化保存容器的数据，k8s引入了Volume的概念

Volume是pod中能够被多个容器文章的共享目录，它被定义在pod上，然后被一个pod里的多个容器挂载到具休的文件目录下，k8s通过Volume实现同一个pod中不同容器之间的数据共享以及数据的持久化。Volume的生命容器不与pod中单个容器的生命周期相关，当容器终止或者重启时，Volume不会丢失。

Volume 支持多种哦打，常见有：

- 简单存储：EmptyDir, HostPath, NFS
- 高级存储：PV， PVC
- 配置存储：ConfigMap, Secret

## P71-基本存储--EmptyDir

### 8.1 基本存储

#### 8.1.1 EmptyDir

```
EmptyDir是最基础的Volume类型，一个EmptyDir就是Host上的一个空目录。
```


```
EmptyDir是在Pod被分配到Node时创建的，它的初始内容为空，并且无须指定宿主机上对应的目录文件，因为k8s会自动分配一个目录，当pod销毁时，EmptyDir中的数据也会被永久删除。EmptyDir用途如下：
```


- 临时空间，例如用于某些应用程序运行时所需的临时目录，且无须永久保留。
- 一个窗口需要从另一个窗口中获取数据的目录（多窗口共享目录）

案例：

```
在一个pod中准备两个容器nginx和busybox，然后声明一个Volume分别挂在到两个容器的目录中，然后nginx窗口负责向Volume中写日志，busybox中通过命令将日志内容读到控制台。
```


volume-emptydir.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: volume-emptydir
  namespace: dev
spec: 
  containers:
  - name: nginx
    image: nginx:1.17.1
    ports:
    - containerPort: 80
    volumeMounts:	# 将logs-volume挂在到nginx容器中，对应的目录为/var/log/nginx
    - name: logs-volume
      mountPath: /var/log/nginx
  - name: busybox
    image: busybox:1.30
    command: ["/bin/sh","-c","tail -f /logs/access.log"] # 初始命令，动态读取指定文件中内容
    volumeMounts:	# 将logs-volume挂在到busybox容器中，对应的目录为 /logs
    - name: logs-volume
      mountPath: /logs
  volumes: 	# 声明volumme, name为logs-volume, 类型为emptyDir
  - name: logs-volume
    emptyDir: {}
```

```sh
kubectl create -f volume-emptydir.yaml

#查看pod
kubectl  get pods volume-emptydir -n dev -o wide

#访问nginx
curl 10.244.1.100

# 通过kubectl logs命令查看指定容器的标准输出 
kubectl logs -f volume-emptydir -n dev -c busybox
```

## P72-基本存储--HostPath

### 8.1.2 HostPath

```
EmptyDir中数据不会被持久化， 它会随着Pod的结束而销毁，如果想简单的将数据持久化到主机中，可以选择HostPath。
```


```
HostPath就是将Node主机中一个实际目录挂在到Pod中，以供容器使用，这样的设计就要以保证Pod销毁了，但是数据依然可以存在于nodey主机上。
```


volume-hostpath.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: volume-hostpath
  namespace: dev
spec: 
  containers:
  - name: nginx
    image: nginx:1.17.1
    ports:
    - containerPort: 80
    volumeMounts:	# 将logs-volume挂在到nginx容器中，对应的目录为/var/log/nginx
    - name: logs-volume
      mountPath: /var/log/nginx
  - name: busybox
    image: busybox:1.30
    command: ["/bin/sh","-c","tail -f /logs/access.log"] # 初始命令，动态读取指定文件中内容
    volumeMounts:	# 将logs-volume挂在到busybox容器中，对应的目录为 /logs
    - name: logs-volume
      mountPath: /logs
  volumes: 	# 声明volumme, name为logs-volume, 类型为emptyDir
  - name: logs-volume
    hostPath:
      path: /root/logs
      type: DirectoryOrCreate	# 目录存在就使用，不存在就先创建后使用
```

关于type值的说明

- DirectoryOrCreate  目录存在就使用，不存在就先创建后使用
- Directory  目录必须存在
- FireOrCreate  文件存在就使用，不存在就先创建后使用
- File    文件必须存在
- Socket     unix套接字必须存在
- CharDevice   字符设备必须存在
- BlockDevice    块设备必须存在

<font color='red'>pod部署在哪个node上，就在哪个node上创建hostpath</font>

## P73-基本存储--NFS

```
HostPath可以解决数据持久化的问题，但是一旦Node节点的故障了，pod如果转移到了别的节点，又会出现问题了，此时需要准备单独的网络存储系统，比较常用的有NFS, CIFS。
```


1、准备nfs服务器

```sh
# 在master上安装nfs服务
yum install nfs-utils -y

# 准备一个共享目录
mkdir /root/data/nfs -pv

# 将共享目录以读写权限暴露给192.168.109.0/24网段中的所有主机
vim /etc/exports
more /etc/exports
/root/data/nfs		192.168.132.0/24(rw,no_root_squash)	# 可读可写，不用root权限

# 启动nfs服务
systemctl start nfs
```

2、每个node节点都安装下nfs, 这样的目的是为了node节点可以驱动nfs设备

```sh
# 在node上安装nfs服务，注意不需要启动
yum install nfs-utils -y
```

3、volume-nfs.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: volume-nfs
  namespace: dev
spec: 
  containers:
  - name: nginx
    image: nginx:1.17.1
    ports:
    - containerPort: 80
      nodePort: 80
    volumeMounts:	# 将logs-volume挂在到nginx容器中，对应的目录为/var/log/nginx
    - name: logs-volume
      mountPath: /var/log/nginx
  - name: busybox
    image: busybox:1.30
    command: ["/bin/sh","-c","tail -f /logs/access.log"] # 初始命令，动态读取指定文件中内容
    volumeMounts:	# 将logs-volume挂在到busybox容器中，对应的目录为 /logs
    - name: logs-volume
      mountPath: /logs
  volumes: 	# 声明volumme, name为logs-volume, 类型为emptyDir
  - name: logs-volume
    nfs:
      server: 192.168.132.31	# nfs服务器地址
      path: /root/data/nfs
```

```sh
# 创建pod
kubectl create -f volume-nfs.yaml

# 查看pod
kubectl get pod -n dev -o wide

# 查看访问日志
tail -f /root/data/nfs/access.log
10.244.3.1 - - [27/Apr/2021:06:18:19 +0000] "GET / HTTP/1.1" 200 612 "-" "curl/7.29.0" "-"
```

## P74-高级存储--pv和pvc的介绍

![image-20210428104907303](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210428104907303.png)

### 8.2.1  PV和PVC

```
NFS提供存储，此时就要求用户会搭建NFS系统，并且会在yaml配置nfs。由于k8s支持的存储系统很多，要求客户全都要掌握，显然不现实。为了能够屏蔽底层存储实现的细节，方便用户使用，k8s引入PV和PVC两种资源对象。
```


- PV（Persistent Volume）是持久化卷的意思。是对底层的共享在座的一种抽象。一般情况下PV由k8s管理员进行 创建和配置，它与底层具体的共享存储技术有关，并通过插件完成与共享存储的对接。
- PVC（Persistent Volume Claim）是持久卷声明的意思，是用户对于存储需求的一种声明。换句话说，PVC其实就是用户向k8s系统发出的一种资源需求申请。

使用了PV和PVC之后，工作可以进一步细分：

- 存储：存储工程师维护
- PV：k8s管理员维护
- PVC：k8s用户维护

## P75-高级存储--pv

### 8.2.2 PV

<font color='red'>PV是跨nacespce的</font>

资源清单

```yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv2
spec:
  nfs: 	#存储类型，与底层真正存储对应
  capacity: # 存储能力，目前只支持存储空间的设置
    storage: 2Gi
  accessModes: #  访问模式
  storageClassName:  #存储类别 
  persistentVolumeReclaimPolicy:  #回收策略
```

PV的关键参数说明：

- 存储类型：底层实际存储的类型，k8s支持多种存储类型，每种存储类型的配置都有所差异。
- 存储能力（capacity）：目前只支持存储空间的设置（storage=1Gi）。不过未来可能会加入IOSPS，吞吐量等指标的配置。
- 访问模式（accessModes）：用于描述用户应用对存储资源的访问权限，访问权限包括下面几种方式：

  - ReadWriteOnce（RWO）：读写权限，但是只能被单个节点挂载。
  - ReadOnlyMany(ROX)：只读权限，可以被多个节点挂载。
  - ReadWriteMany（RWX）：读写权限，可以被多个节点挂载

  <font color='red'>需要注意的是是，底层不同的存储类型可能支持的访问模式不同</font>
- 回收策略（persistentVolumeReclaimPolicy）：当PV不再被使用了之后，对其的处理方式。目前支持3种策略：

  - Retain（保留）：保留数据，需要管理员手工清理数据
  - Recycle（回收）：清除PV中的数据，效果相当于执行rm -rf /thevolume/*
  - Delete（删除）：与PV相连后端存储完成volume的删除操作，当我这常见于云服务商的存储服务

  <font color='red'>需要注意的是是，底层不同的存储类型可能支持的访问模式不同</font>
- 存储类别：PV通过storageClassName参数指定一个存储类别（比较少用）

  - 具有特定类别的PV只能与请求了该类别的PVC进行绑定。
  - 未这对定类别的PV则只能与不请求任保类别的PVC进行绑定
- 状态（status）：一个PV的生命周期中，可能会处于不同的阶段：

  - Available（可用）：表示可用状态，还未被任保PVC绑定
  - Bound（已绑定）：表示PV已经被PVC绑定
  - Released（已释放）：表示PVC被删除，但是资源还未被集群重新声明
  - Failed（失败）：表示该PV的自动回收失败

**实验**：使用NFS准备NFS作为存储，来演示PV的使用，创建3个PV，对应NFS中的3个暴露的路径。

1、准备NFS环境

```sh
# 创建目录
mkdir /root/data/{pv1,pv2,pv3} -pv

# 暴露服务
more /etc/exports
/root/data/pv1    192.168.132.0/24(rw,no_root_squash)
/root/data/pv2    192.168.132.0/24(rw,no_root_squash)
/root/data/pv3    192.168.132.0/24(rw,no_root_squash)

# 重启服务
systemctl restart nfs
```

2、pv.yaml

```yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv1
spec:
  capacity:
    storage: 1Gi
  accessModes:
  - ReadWriteMany
  persistentVolumeReclaimPolicy: Retain
  nfs:
    path: /root/data/pv1
    server: 192.168.132.31
  
---

apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv2
spec:
  capacity:
    storage: 2Gi
  accessModes:
  - ReadWriteMany
  persistentVolumeReclaimPolicy: Retain
  nfs:
    path: /root/data/pv2
    server: 192.168.132.31
  
---

apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv3
spec:
  capacity:
    storage: 3Gi
  accessModes:
  - ReadWriteMany
  persistentVolumeReclaimPolicy: Retain
  nfs:
    path: /root/data/pv3
    server: 192.168.132.31

```

```sh
# 创建pv
kubectl create -f pv.yaml

# 查看
kubectl get pv -o wide
NAME   CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS      CLAIM   STORAGECLASS   REASON   AGE     VOLUMEMODE
pv1    1Gi        RWX            Retain           Available                                   2m44s   Filesystem
pv2    2Gi        RWX            Retain           Available                                   2m44s   Filesystem
pv3    3Gi        RWX            Retain           Available                                   2m44s   Filesystem
```

## P76-高级存储--pvc

PVC是资源的是申请，用来声明对存储空间、访问模式、存储类别需求信息。资源清单文件：

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: pvc
  namespace: dev
spec:
  accessModes: # 访问模式
  selector: # 采用标签对PV选择
  storageClassName: 	#存储类别 
  resources:	# 请求空间
    requests: 
      storage: 5Gi
```

PVC关键配置参数说明：

- 访问模式（accessModes）：用于描述用户对存储资源的访问权限
- 选择条件（selector）：通过Label Selector的设置，可使用PVC对系统中已存在的PV进行筛选
- 存储类别（storageClassName）：PVC在定义时可以设定老百姓要的后端存储的类别，只有设置了该class的pvc才能被系统选出
- 资源请求（Resours）：描述对存储 资源的请求

pvc.yaml

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: pvc1
  namespace: dev
spec:
  accessModes:
  - ReadWriteMany
  resources:
    requests:
      storage: 1Gi
  
---

apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: pvc2
  namespace: dev
spec:
  accessModes:
  - ReadWriteMany
  resources:
    requests:
      storage: 1Gi
  
---

apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: pv3
  namespace: dev
spec:
  accessModes:
  - ReadWriteMany
  resources:
    requests:
      storage: 5Gi
```

```sh
kubectl create -f pvc.yaml

# 查看
kubectl get pvc -n dev
kubectl get pv
```

创建pvc-pods.yaml

```yaml
apiVersion: v1
kind: Pod
metadata: 
  name: pod1
  namespace: dev
spec: 
  containers:
  - name: busybox
    image: busybox:1.30
    command: ["/bin/sh","-c","while true;do echo pod1 >> /root/out.txt; sleep 10; done;"] 
    volumeMounts:
    - name: volume
      mountPath: /root/		#绑定容器内部的路径
  volumes: 
  - name: volume
    persistentVolumeClaim:
      claimName: pvc1
      readOnly: false
    
---

apiVersion: v1
kind: Pod
metadata: 
  name: pod2
  namespace: dev
spec: 
  containers:
  - name: busybox
    image: busybox:1.30
    command: ["/bin/sh","-c","while true;do echo pod2 >> /root/out.txt; sleep 10; done;"] 
    volumeMounts:
    - name: volume
      mountPath: /root/
  volumes: 
  - name: volume
    persistentVolumeClaim:
      claimName: pvc2
      readOnly: false
```

```sh
kubectl create -f pvc-pod.yaml

# 查看master下/root/data/pv1/out.txt 和/root/data/pv2/out.txt
tail -f /root/data/pv1/out.txt
tail -f /root/data/pv2/out.txt
```

## P77-高级存储--pv和pvc的生命周期

#### 8.2.4 生命周期

PVC和PV是一一对应的，PV和PVC之间的相互作用遵循以下生命周期：

- **资源供应**：管理员手动创建底层存储和PV
- 资源绑定：用户创建PVC, k8s负责根据PVC的声明去寻找PV，并绑定在用户定义好的PVC之后，系统将根据PVC对存储资源的请求在已存在的PV中选择一个满足条件的

  - 一旦找到，就将该PV与用户定义的PVC进行绑定，用户的应用就可以使用这个PVC了
  - 如果找不到，PVC则会无限期处于Pending状态，直到等到系统管理员创建一个符合其要求的PV

  <font color='red'>PV一旦绑定到某个PVC上，就会被这个PVC独占，不能再与其他PVC进行绑定了</font>
- **资源使用**：用户可在pod中像volume一样使用PVC

  Pod使用Volume的定义，将PVC挂载到容器内的某个路径进行使用。
- **资源释放**：用户删除pvc来释放pv

  当存储资源使用完毕后，用户可以删除pvc，与该PVC绑定的PV将会被标记为“released"，但还不能立刻与其他pvc进行绑定，通过之前PVC写入的数据可能还被留在存储设备上，只有在清除之后该pv才能再次使用。
- 资源回收：k8s根据pv设置的回收策略进行资源的回收

  对于pv，管理员可以设定回收策略，用于设置与之绑定的PVC释放资源之后如何处理遗留数据的问题。只有PV的存储空间完成回收，才能供新的PVC绑定和使用。

  ![image-20210428144917804](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210428144917804.png)

## P78-配置存储--configmap

#### 8.3.1 ConfigMap

configmap.yaml

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: configmap
  namespace: dev
data:
  info: 
    username:admin
    password:123456
```

```sh
# 创建configmap
kubectl create -f configmap.yaml

#查看configmmap详情
kubectl describe cm configmap -n dev
Data
====
info:
----
username:admin password:123456
Events:  <none>
```

创建pod pod-configmap.yaml

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: pod-configmap
  namespace: dev
spec:
  containers:
  - name: nginx
    image: nginx:1.17.1
    volumeMounts: 	# 将configmap挂载到目录
    - name: config
      mountPath: /configmap/config
  volumes:	# 引用configmap
  - name: config
    configMap:
      name: configmap
```

```sh
# 创建pod
kubectl create -f pod-configmap.yaml

# 查看pod
kubectl get pod-configmap -n dev

# 进入容器
kubectl exec -it pod-configmap -n dev /bin/bash
# cd /configmap/config
# ls
more info

# 可以看到映射成功，每个configmap都映射成了一个目录
# key --->文件    value--->文件中的内容
# 此例中info是文件名， username, password都是内容
# 此时如果更新configmap的内容，容器中的值也会动态更新（要等一会，大概1分钟）
```

## P79-配置存储--secret

#### 8.3.2 Secret

```
它主要用于存储敏感信息，例如密码，密钥，证书等
```


1、对base64对数据进行编码

```sh
echo -n 'admin' | base64  # 准备username
YWRtaW4=
echo -n '123456' | base64
MTIzNDU2

```

2、secret.yaml

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: secret
  namespace: dev
type: Opaque
data:
  username: YWRtaW4=
  password: MTIzNDU2
```

```sh
# 创建secret
kubectl create -f secret.yaml
# 查看secret详情
kubectl describe secret  secret -n dev
Name:         secret
Namespace:    dev
Labels:       <none>
Annotations:  <none>

Type:  Opaque

Data
====
password:  6 bytes	# 加密了
username:  5 bytes  # 加密了
```

创建pod-secret.yaml

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: pod-secret
  namespace: dev
spec:
  containers:
  - name: nginx
    image: nginx:1.17.1
    volumeMounts: 	# 将secret挂载到目录
    - name: config
      mountPath: /secret/config
  volumes:
  - name: config
    secret:
      secretName: secret
```

```sh
#创建pod
kubectl create -f pod-secret.yaml

# 进入容器，查看secret信息，发现已经自动解码了
kubectl exec -it pod-secret /bin/bash -n dev
# ls /secret/config/
# more /secret/config/username
admin
# more /secret/config/password
123456
```

# 第9章 安全认证

## P80-安全认证--概述

### 9.1  访问控制概述

```
k8s作为一个分布式集群的管理工具，保证集群的安全性是其一个重要的任务。所谓安全性其实就是保证对k8s的各种
```

**客户端**进行**认证和鉴权**操作

客户端：

- User Account：一般是独立于k8s之外的其他服务管理的用户账号。
- Service Account：k8s管理的账号，用于为pod中的服务进程在k8s时提供身份标识。

认证，授权与准入控制

- Authentication（认证）：身份鉴别，只有正确的的账号才能通过认证。
- Authorization（授权）：判断用户是否有权限对访问的资源执行特定的动作。
- Admission Control（准入控制）：用于补充授权机制以实现更加精细的访问控制功能。

![image-20210430102222304](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210430102222304.png)

## P81-安全认证--认证方式

### 9.2 认证管理

```
k8s集群安全的最关键点在于如何识别并认证客户端身份，它提供了3种客户端身份认证方式：
```


- **http Base认证**：通过用户名+密码的方式认证

  ```
  这种认证方式是把“用户名：密码”用BASE6算法进行编码后的字符串放在http请求中的Header Authorization域里发送给服务端。服务端收到后进行解码，获取用户名及密码，然后进行用户身份认证的进程。
  ```
- **http token认证**：通过一个token来识别合法用户

  ```
  这种认证方式是用一个很长的难以被模仿的字符串——token来表明客户身份的一种方式。每个token对应一个用户名，当客户端发起API调用请求时，需要在http Header里放入token，API Server接到token后会跟服务器中保存的tokken进行比对，然后进行用户身份认证的过程。
  ```
- **https证书认证**：基于CA根证书签名的双向数字证书认证方式

  ```
  这种认证方式是安全性最高的一种方式，但是同时也是操作起来最麻烦的一种方式。
  ```


  ![image-20210430103703990](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210430103703990.png)

http认证大体分为3个过程：

1. 证书申请和下发

   ```
   https通过双方的服务器向ca机构申请证书，ca机构下发根证书、服务端证书及私钥给申请者
   ```
2. 客户端和服务端的双向认证

   ```md
   1. 客户端向服务器端发起请求，服务端下发自己的证书给客户端，
      客户端接收到证书后，通过私钥解密证书，在证书中获得服务端的公钥，
      客户端利用服务器端的公钥认证证书中的信息，如果一致，则认可这个服务器
   2. 客户端发送自己的证书给服务器端，服务端接收到证书后，通过私钥解密证书，
      在证书中获得客户端的公钥，并用该公钥认证证书信息，确认客户端是否合法。
   ```
3. 服务器端和客户端进行通信

   ```
   服务端和客户端协商好加密方案后，客户端会产生一个随机的秘钥并加密，然后发送到服务端。
   ```


   ```
   服务端接收这个秘钥后，双方接下来通信的所有内容都通过该随机秘钥加密
   ```


   PS：**k8s允许同时配置多种认证方式，只要其中任意一个方式认证通过即可**。

## P82-安全认证--授权管理

### 9.3  授权管理

```
授权发生在认证成功之后，通过认证就可以态度同一个求用 户是谁，然后k8s会根据事先定义的授权策略来决定用户是否有权限访问，这个过程就称为授权。
```


```
每个发送到ApiServer的请求都带上了用户和资源的信息：比如发送请求的用户，请求的路径，请求的动作等，授权就是根据这些信息和授权策略进行比较，如果符合策略，则认为授权通过，否则会返回错误。
```


API Server目前支持以下几种授权策略：

- AlwaysDeny：表示拒绝所有请求，一般用于测试
- AlwaysAllow：允许接收所有请求，相当于集群不需要授权流程（k8s默认的策略）
- ABAC：基于属性的访问控制，表示使用用户配置的授权规则对用䚮请求进行匹配和控制。
- Webhook：通过调用外部REST服务对用户进行授权。
- Node：是一各专用模式，用于对kubelet发出的请求进行访问控制。
- **RBAC**：基于角色的访问控制（kubeadm安装方式下的默认选项）

  RBAC（Role-Base Access Control）基于角色的访问控制，主要是在描述一件事情：**给哪些对象授予哪些权限**

  **基中涉及到了下面几个概念**：

  - 对象：user, groups, ServiceAccount
  - 角色：代表着一组定义在资源上的要操作动作的集合
  - 绑定：将定义好的角色跟用䚮绑定在一起

  RBAC引入4个顶级资源对象：

  - Role，ClusterRole：角色，用于指定一组权限
  - RoleBinding, ClusterRoleBinnding：角色权限，用于将角色（权限）赋予给对象

  **Role, ClusterRole**

  一个角色就是一组权限的集合，这时的权限都是许可形式的（白名单）。

```yaml
# Role只能对命名空间内的资源进行授权，需要指定namespace
kind: Role
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  namespace: dev
  name: authorization-role
rules:
- apigroups: [""]  # 支持的API组列表，""空字符串，表示核心API群
  resources: ["pods"] # 支持的资源对象列表
  verbs: ["get" ,"watch", "list"] # 允许的对资源对象的你叫什么方法列表
```

```yaml
# ClusterRole可以对集群范围内资源，跨namespaces的范围资源，非资源类型进行授权
kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: authorization-clusterrole
rules:
- apigroups: [""] 
  resources: ["pods"] 
  verbs: ["get" ,"watch", "list"] 
```

需要说细说明的是，rules的参数：

- apiGroups: 支持的API组列表

```sh
"","apps","autoscaling", "batch"
```

- resources: 支持的资源对象列表

```sh
"services", "endpoints", "pods","secrets", "configmaps", "crontabs", "deployments", "jobs", "nodes", "rolebindings", "clusterroles", "daemonsets", "replicasets","statefulsets", "hozizontalpodautoscalers", "replicatioincontrollers", "cronjobs"
```

- verbs：对资源对象的操作方法列表

```sh
"get", "list", "watch", "create", "update", "patch", "delete", "exec"
```

**RoleBinding， ClusterRoleBinding**

角色绑定用来把一个角色绑定到一个目标对象上，绑定目标可以是User，Group或者ServiceAccount

```yaml
# RoleBinding可以将同一namespace上的subject绑定到某个Role下，则此subject即具有该role定义的权限
kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: authorization-role-binding
  namespace: dev
subjects:
- kind: User
  name: heima
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: Role
  name: authorization-role	# 将authorization-role的角色赋予heima的用户
  apiGroup: rbac.authorization.k8s.io
```

```yaml
# ClusterRoleBinding在整个集群级别和所有namespaces将特定的subject与ClusterRole绑定，授予权限
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: authorization-clusterrole-binding
subjects:
- kind: User
  name: heima
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: ClusterRole
  name: authorization-role
  apiGroup: rbac.authorization.k8s.io
```

**RoleBinding 引用ClusterRole进行授权**

RoleBinding可以引用ClusterRole，对属于同一命名空间内ClusterRole定义的资源主体进行授权。

```sh
#  一种很常用的做法是，集群管理员为集群范围预定义好一组角色（ClusterRole），然后在多个命名空间中重复使用这些ClusterRole。这可以大幅度提高授权管理工作效率，也使得各个命名空间下的基础性授权规则与使用体验保持一致。
```

```yaml
# 虽然authorization-clusterrole是一个集群角色 ，但是因为使用了RoleBinding
# 所以heima只能读取dev命名空间中的资源
kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: authorization-role-binding-ns
  namespace: dev
subjects:
- kind: User
  name: heima
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: ClusterRole
  name: authorization-clusterrole	# 将authorization-role的角色赋予heima的用户
  apiGroup: rbac.authorization.k8s.io
```

**实战：创建一个只能管理dev空间下pods资源的账号**

1、创建账号

```sh
# 1. 创建证书
cd /etc/kubernetes/pki/

(umask 077;openssl genrsa -out devman.key 2048)

# 2. 用apiserver的证书签署
# 2.1 签名申请，申请的用户是devman，组是devgroup
openssl req -new -key devman.key -out devman.csr -subj "/CN=devman/O=devgroup"

# 2.2 签署证书
openssl x509 -req -in devman.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out devman.crt -days 3650

# 3. 设置集群，用户，上下文信息
kubectl config set-cluster kubernetes --embed-certs=true --certificate-authority=/etc/kubernetes/pki/ca.crt --server=https://192.168.132.31:6443

kubectl config set-credentials devman --embed-certs=true --client-certificate=/etc/kubernetes/pki/devman.crt --client-key=/etc/kubernetes/pki/devman.key

kubectl config set-context devman@kubernetes --cluster=kubernetes --user=devman

# 切换到devman
kubectl config use-context devman@kubernetes

# 查看dev下权限，发现没有权限
kubectl get pods -n dev
Error from server (Forbidden): pods is forbidden: User "devman" cannot list resource "pods" in API group "" in the namespace "dev"

# 切换到admin账户
kubectl config use-context kubernetes-admin@kubernetes
```

2、创建Role和RoleBinding，为devman用户授权

dev-role.yaml

```yaml
kind: Role
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  namespace: dev
  name: dev-role
rules:
- apiGroups: [""]  
  resources: ["pods"] 
  verbs: ["get" ,"watch", "list"] 
  
---

kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  namespace: dev
  name: authorization-role-binding
subjects:
- kind: User
  name: devman
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: Role
  name: dev-role
  apiGroup: rbac.authorization.k8s.io

```

```sh
kubectl create -f dev-role.yaml
```

3、切换账户，再次验证

```sh
# 切换账户到devman
kubectl config use-context devman@kubernetes
Switched to context "devman@kubernetes".


# 再次检查
kubectl get pods -n dev

# 为了不影响后面的学习，切回到admin账户
kubectl config use-context kubernetes-admin@kubernetes
Switched to context "kubernetes-admin@kubernetes".

```

## P83-安全认证-- 准入控制

通过了前面的认证和授权之后，还需要经过准入控制处理通过之后，apiserver才会处理这个请求。

准入控制是一个可配置的控制器列表，可以通过在api-server上通过命令行设置选择执行哪些准入控制器：

```sh
--admission-control=NamespaceLifecycle, LimmitRanger, ServiceAccount, PersistentVolumeLabel, DefaultStorageClass, ResourceQuota, DefaultTolerationSeconds
```

只有当所有的准入控制器都检查通过之后，apiserver才执行该请求，否则返回拒绝。

当前可配置的Admission Control准入控制如下：

- AlwaysAdmit：允许接收所有请求
- AlwaysDeny：禁止所有请求，一般用于测试
- AlwaysPullImages：在启动容器之前总去下载镜像
- DenyExecOnPrivileged：会拦截所有想在Privileged Container上执行命令的请求
- ImagePolicyWebhook：这个插件将允许后端的一个webhook程序来完成admission controller的功能
- Service Account：实现serviceAccount自动化
- SecurityContextDeny：将使用SecurityContext的pod中的定义全部失效
- ResourceQuota：用于资源配额管理目的，观察所有请求，确保在namespace上的配额不会超标
- LimitRanger：用于资源限制管理，作用于namespace上，确保对pod进行资源限制
- InitialResources：为未设置资源请求与限制的pod，根据其镜像的历史资源的使用情况进行设置
- NamespaceLifecycle：如果尝试在一个不存在的namespace中创建资源对象，则该创建请求将被拒绝。当删除一个namespace时，系统将会删除该namespace中所有对象。
- DefaultStorageClass：为了实现共享存储的动态供应，为未指定StorageClass或PV的PVC尝试匹配默认的StorageClass，尽可能减少用户在申请 PVC时所需了解的后端在座细节
- DefaultTolerationSeconds：为那些没有设置forgiveness toleratioins并具有notreay:NoExecute和unreachable:NoExecute两种taints的pod设置默认的”容忍“时间，为5min
- PodSecurityPolicy：用于在创建或修改pod时决定是否根据pod的security context和可用的podSecurityPolicy对pod的安全策略进行控制。

# 第10章 DashBoard

k8s的web用户界面。用户可以使用Dashboard部署容器化的应用，还可以监控应用状态，执行排查以及管理k8s各种资源。

## P84-DashBoard-部署

### 10.1 部署DashBoard

1) 下载yaml ，并运行Dashboard

```sh
# 下载 yaml
wget https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0/aio/deploy/recommended.yaml
```

```yaml
# 修改k8s-dashboard的service类型
kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kubernetes-dashboard
spec:
  type: NodePort	#新增
  ports:
  - port: 443
    targePort: 8443
    nodePort: 30009	#新增
  selector:
    k8s-app: kubernetes-dashboard
```

```sh
# 部署
kubectl create -f recommand.yaml

# 查看namespace下的k8s-dashboard下的资源
kubectl get pod,svc -n kubernetes-dashboard
NAME                                            READY   STATUS              RESTARTS   AGE
pod/dashboard-metrics-scraper-c79c65bb7-dbp6x   0/1     ContainerCreating   0          86s
pod/kubernetes-dashboard-56484d4c5-fll8n        0/1     ContainerCreating   0          86s

NAME                                TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)         AGE
service/dashboard-metrics-scraper   ClusterIP   10.102.63.120    <none>        8000/TCP        86s
service/kubernetes-dashboard        NodePort    10.106.135.193   <none>        443:30009/TCP   86s

```

2) 创建访问账户，获取token

```sh
# 创建账号
kubectl create serviceaccount dashboard-admin -n kubernetes-dashboard
serviceaccount/dashboard-admin created
# 授权
kubectl create clusterrolebinding dashboard-admin-rb --clusterrole=cluster-admin --serviceaccount=kubernetes-dashboard:dashboard-admin
clusterrolebinding.rbac.authorization.k8s.io/dashboard-admin-rb created

# 获取账号token
kubectl get secrets -n kubernetes-dashboard | grep dashboard-admin
kubectl describe secrets dashboard-admin-token-j4svz -n kubernetes-dashboard
```

## P85-DashBoard-使用演示

ip: https://192.168.132.32:30009
