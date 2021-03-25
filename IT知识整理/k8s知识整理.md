# 禁用swap

from: https://blog.csdn.net/weixin_43224440/article/details/111556962

centos关闭swap分区
 第一步 关闭swap分区:
 ```bash
 swapoff -a
 ```

第二步修改配置文件 - /etc/fstab
 删除swap相关行 /mnt/swap swap swap defaults 0 0 这一行或者注释掉这一行

第三步确认swap已经关闭
```bash
 free -m
```

​	![在这里插入图片描述](https://gitee.com/jstone001/booknote/raw/master/jpgBed/20201222152208274.png)

若swap行都显示 0 则表示关闭成功

第四步调整 swappiness 参数
```bash
 echo 0 > /proc/sys/vm/swappiness # 临时生效
```

vim /etc/sysctl.conf # 永久生效
 \#修改 vm.swappiness 的修改为 0
 vm.swappiness=0
 sysctl -p # 使配置生效

（2）永久关闭swap分区 

​	<font color=red>sed -ri 's/.*swap.*/#&/' /etc/fstab</font>



# kubectl 子命令使用分类

​    基础命令

```bash
Basic Commands (Beginner):
  create        Create a resource from a file or from stdin.
  expose        Take a replication controller, service, deployment or pod and expose it as a new Kubernetes Service
  run           Run a particular image on the cluster
  set           Set specific features on objects

Basic Commands (Intermediate):
  explain       Documentation of resources	#重要
  get           Display one or many resources
  edit          Edit a resource on the server
  delete        Delete resources by filenames, stdin, resources and names, or by resources and label selector
```

​    部署和集群管理命令

```bash
Deploy Commands:
  rollout       Manage the rollout of a resource
  scale         Set a new size for a Deployment, ReplicaSet or Replication Controller
  autoscale     Auto-scale a Deployment, ReplicaSet, or ReplicationController

Cluster Management Commands:
  certificate   Modify certificate resources.
  cluster-info  Display cluster info
  top           Display Resource (CPU/Memory/Storage) usage.
  cordon        Mark node as unschedulable
  uncordon      Mark node as schedulable
  drain         Drain node in preparation for maintenance
  taint         Update the taints on one or more nodes
```

​    故障和调试命令
```bash
Troubleshooting and Debugging Commands:
  describe      Show details of a specific resource or group of resources  #重要
  logs          Print the logs for a container in a pod  #重要
  attach        Attach to a running container
  exec          Execute a command in a container  #重要
  port-forward  Forward one or more local ports to a pod
  proxy         Run a proxy to the Kubernetes API server
  cp            Copy files and directories to and from containers.
  auth          Inspect authorization
  debug         Create debugging sessions for troubleshooting workloads and nodes

```


​    其他命令

```bash
Advanced Commands:
  diff          Diff live version against would-be applied version
  apply         Apply a configuration to a resource by filename or stdin
  patch         Update field(s) of a resource
  replace       Replace a resource by filename or stdin
  wait          Experimental: Wait for a specific condition on one or many resources.
  kustomize     Build a kustomization target from a directory or a remote url.

Settings Commands:
  label         Update the labels on a resource
  annotate      Update the annotations on a resource
  completion    Output shell completion code for the specified shell (bash or zsh)

Other Commands:
  api-resources Print the supported API resources on the server	# 常用
  api-versions  Print the supported API versions on the server, in the form of "group/version"
  config        Modify kubeconfig files
  plugin        Provides utilities for interacting with plugins.
  version       Print the client and server version information
```



# kubeadm(快速部署)

from: 尚硅谷2020_k8s  https://www.bilibili.com/video/BV1GT4y1A756

1. 所有服务器操作：

1.1 所有服务器初始化
```bash
# 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld

# 关闭selinux
sed -i 's/enforcing/disabled/' /etc/selinux/config  # 永久
setenforce 0  # 临时

# 关闭swap
swapoff -a  # 临时
sed -ri 's/.*swap.*/#&/' /etc/fstab    # 永久

# 根据规划设置主机名
hostnamectl set-hostname <hostname>

# 在master添加hosts
cat >> /etc/hosts << EOF
192.168.44.146 k8smaster
192.168.44.145 k8snode1
192.168.44.144 k8snode2
EOF

# 将桥接的IPv4流量传递到iptables的链
cat > /etc/sysctl.d/k8s.conf << EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
sysctl --system  # 生效

# 时间同步（如果虚拟机同步主机，则不需要）
yum install ntpdate -y
ntpdate time.windows.com
```

1.2  安装Docker

```bash
# 安装docker
$ wget https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -O /etc/yum.repos.d/docker-ce.repo
$ yum -y install docker-ce-18.06.1.ce-3.el7
$ systemctl enable docker && systemctl start docker
$ docker --version
Docker version 18.06.1-ce, build e68fc7a

# 切换docker库源
$ cat > /etc/docker/daemon.json << EOF
{
  "registry-mirrors": ["https://b9pmyelo.mirror.aliyuncs.com"]
}
EOF

# 重启docker
$ systemctl restart docker
```

1.3 所有节点安装kubeadm/kubelet

```bash
# 添加阿里云镜像
$ cat > /etc/yum.repos.d/kubernetes.repo << EOF
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF


# 安装Kubenet组件
$ yum install -y kubelet-1.18.0 kubeadm-1.18.0 kubectl-1.18.0
$ systemctl enable kubelet    #开机启动
```

2. 部署master节点

2.1 部署Kubernetes Master

```bash
kubeadm init  --apiserver-advertise-address=192.168.132.31 --image-repository registry.aliyuncs.com/google_containers --kubernetes-version v1.18.0 --service-cidr=10.96.0.0/12 --pod-network-cidr=10.244.0.0/16
# 由于默认拉取镜像地址 k8s.gcr.io 国内无法访问，这里指定阿里云镜像仓库地址。
# 会生成token值
```
2.2 使用 kubectl 工具
```bash
mkdir -p $HOME/.kube 
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config 
sudo chown $(id -u):$(id -g) $HOME/.kube/config 
$ kubectl get nodes
```
2.3 部署CNI网络插件
```bash
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml

kubectl get pods -n kube-system

NAME                          READY   STATUS    RESTARTS   AGE
kube-flannel-ds-amd64-2pc95   1/1     Running   0          72s
```

3. 添加node节点

```bash
$ kubeadm join 192.168.1.11:6443 --token esce21.q6hetwm8si29qxwn \
       --discovery-token-ca-cert-hash sha256:00603a05805807501d7181c3d60b478788408cfe6cedefedb1f97569708be9c5
  
```

4. 测试(在master的节点上)
```bash
$ kubectl create deployment nginx --image=nginx     #拉取nginx
$ kubectl expose deployment nginx --port=80 --type=NodePort     #对外暴露端口
$ kubectl get pod,svc       #查看状态
```

# 生成永不过期的token

https://www.cnblogs.com/xiaoyongyang/p/11953660.html
https://blog.csdn.net/qianghaohao/article/details/82624920

```bash
# 1、master上生成永久的token
kubeadm token create --ttl 0

# 2、master上生成sha256
openssl x509 -pubkey -in /etc/kubernetes/pki/ca.crt | openssl rsa -pubin -outform der 2>/dev/null | openssl dgst -sha256 -hex | sed 's/^.* //'

# 3、master删除旧的节点
kubectl delete node n1

# 4、node上reset
kubeadm reset

#5、重新join
kubeadm join --token 8dv0fj.3bh9267krsx0j5j6 192.168.132.31:6443 --discovery-token-ca-cert-hash sha256:15ef8f90a05e69382f9ea47c23ff4ba45adfd13fad0d04ccfe149f6d32f3a32f
```

# 组件说明

1. master（主控节点）
```
       API Server: 集群统一入口，以restful方式，交给etcd
       scheduler: 节点调度
       controller-manager: 处理集群中常规后台任务
       etcd: 存储系统，保存集群相关数据
       	组件:
       		httpserver
           	raft
           	WAL
           	Entry
           	Snapshot
           	Store
```
   ![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/20210325101822.png)

2. node（工作节点）

```
	kubelet: master排到node节点代表，管理本机容器
	kube-proxy: 提供网络代理，负载均衡操作
```

   

3. 其他组件

```
   CoreDNS: 可以为集群中的SVC创建一个域名IP的对应关系解析
   Ingress Controller: 官司方只能实现4层代理。Ingress可以实现七层代理
   Prometheus: 提供一个k8s集群的监控能力
   Dashboard: 给k8s集群提供一个B/S结构访就只有体系
   Federation:	提供一个跨集群中心多k8s统一管理的功能 
   ELK: k8s集群日志统一分析介入平台
```


