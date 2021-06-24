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

from: 尚硅谷2020_k8s  https://www.bilibili.com/video/BV1GT4y1A756  黑马程序员 https://www.bilibili.com/video/BV1Qv41167ck

1. 所有服务器操作：

1.1 所有服务器初始化

```bash
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

1.2  安装Docker

```bash
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

1.3 所有节点安装kubeadm/kubelet

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

2. 部署master节点

   集群初始化

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

2.1 部署Kubernetes Master

```bash
kubeadm init  --apiserver-advertise-address=192.168.132.31  --kubernetes-version v1.17.4 --service-cidr=10.96.0.0/12 --pod-network-cidr=10.244.0.0/16
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
kubeadm token list  #查看token的失效时间

# 1、master上生成永久的token (最后一行是token)
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
   <img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/20210325101822.png" alt="img" style="zoom:80%;" />

2. node（工作节点）

```bash
	kubelet: master排到node节点代表，管理本机容器
	kube-proxy: 提供网络代理，负载均衡操作
```

   其他组件

```
   CoreDNS: 可以为集群中的SVC创建一个域名IP的对应关系解析
   Ingress Controller: 官司方只能实现4层代理。Ingress可以实现七层代理
   Prometheus: 提供一个k8s集群的监控能力
   Dashboard: 给k8s集群提供一个B/S结构访就只有体系
   Federation:	提供一个跨集群中心多k8s统一管理的功能 
   ELK: k8s集群日志统一分析介入平台
```

# 查看日志

```sh
ubuntu@node1:~/.kube$ journalctl -f -u kubelet
```

# ClusterIP ping不到

from: https://www.yisu.com/zixun/17436.html

# k8s命令对node调度 cordon，drain，delete 区别

from: https://blog.csdn.net/erhaiou2008/article/details/104986006

## cordon  暂停节点

影响最小，只会将node调为SchedulingDisabled， 之后再发创建pod，不会被调度到该节点。 旧有的pod不会受到影响，仍正常对外提供服务

**恢复调度** 

```sh
kubectl uncordon node_name
```

## drain 驱逐节点

首先，驱逐node上的pod，其他节点重新创建 。 接着，将节点调为** SchedulingDisabled**

 <font color='red'>对节点执行维护操作之前（例如：内核升级，硬件维护等），您可以使用 kubectl drain 安全驱逐节点上面所有的 pod。
 安全驱逐的方式将会允许 pod 里面的容器遵循指定的 PodDisruptionBudgets 执行优雅的中止。</font>
 注： 默认情况下，kubectl drain 会忽略那些不能杀死的系统类型的 pod，如果您想了解更多详细的内容，请参考kubectl drain

```sh
# 确定要排空的节点的名称
kubectl get nodes 
# 查看获取pod名字
kubectl get pod
# 命令node节点开始释放所有pod，并且不接收新的pod进程
kubectl drain [node-name] --force --ignore-daemonsets --delete-local-data 
# 这时候把需要做的事情做一下。比如上面说的更改docker文件daemon.json或者说node节点故障需要进行的处理操作 
# 然后恢复node，恢复接收新的pod进程
kubectl uncordon [node-name]
```

```sh
# drain的参数
--force					# 当一些pod不是经 ReplicationController, ReplicaSet, Job, DaemonSet 或者 StatefulSet 管理的时候就需要用--force来强制执行 (例如:kube-proxy)
--ignore-daemonsets		# 无视DaemonSet管理下的Pod
--delete-local-data		# 如果有mount local volumn的pod，会强制杀掉该pod并把料清除掉。另外如果跟本身的配置讯息有冲突时，drain就不会执行
```



## delete 删除节点

- 首先，驱逐node上的pod，其他节点重新创建。然后，从master节点删除该node，master对其不可见，失去对其控制，master不可对其恢复
- 恢复调度，需进入node节点，重启kubelet。基于node的自注册功能，节点重新恢复使用。systemctl restart kubelet
- delete是一个比较粗暴的命令，它会将被删node上的pod直接驱逐，由其他node创建（针对replicaset），然后将被删节点从master管理范围内移除，master对其失去管理控制，若想使node重归麾下，必须在node节点重启kubelet



# K8s部署prometheus监控k8s

from: https://blog.csdn.net/zeorg/article/details/112075071

prometheus监控使用（按照文档步骤就可以操作成功）。
 文档分别坐了使用k8s集群部署docker 跑prometheus监控。（后期如果有时间会做一个监控pod的文档）
 监控K8s细节文章链接
 https://blog.csdn.net/zeorg/article/details/112164465
 特别说明：此环境要提前部署好k8s。
 1、prometheus 作为监控k8s的最佳选择在这里做一个在k8s部署prometheus文档供大家参考。

\#私自转载请联系博主否则必定追究版权 下方有微信
 系统环境：

| IP              | 节点名称（不是主机名） |
| --------------- | ---------------------- |
| 192.168.182.150 | k8s-master             |
| 192.168.182.151 | k8s-node2              |
| 192.168.182.152 | k8s-node2              |
| 192.168.182.153 | docker仓库             |

1、在docker仓库里面先拉取prometheus images（直接使用k8s拉取因为网络问题可以说基本是失败的）。
 docker仓库执行

```bash
docker pull prom/prometheus:v2.2.1 
docker   tag   prom/prometheus:v2.2.1  192.168.182.153:5000/prom/prometheus:v2.2.1
###为prometheus:v2.2.1 images 打一个标签
docker   push  192.168.182.153:5000/prom/prometheus:v2.2.1
##上传打包后的prometheus:v2.2.1镜像
```

2、使用k8s创建prometheus 容器配置。
 k8s-master执行

```bash
创建文件prometheus-config.yml（如果是实验环境目录无所谓）
apiVersion: v1
kind: ConfigMap
metadata:
  name: prometheus-config
data:
  prometheus.yml: |
    global:
      scrape_interval:     15s
      evaluation_interval: 15s
    scrape_configs:
      - job_name: 'prometheus'
        static_configs:
        - targets: ['localhost:9090']
创建文件prometheus-deployment.yml（如果是实验环境目录无所谓）
apiVersion: v1
kind: "Service"
metadata:
  name: prometheus
  labels:
    name: prometheus
spec:
  ports:
  - name: prometheus
    protocol: TCP
    port: 9090
    targetPort: 9090
  selector:
    app: prometheus
  type: NodePort
---
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  labels:
    name: prometheus
  name: prometheus
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: prometheus
    spec:
      containers:
      - name: prometheus
        image: prom/prometheus:v2.2.1
        command:
        - "/bin/prometheus"
        args:
        - "--config.file=/etc/prometheus/prometheus.yml"
        ports:
        - containerPort: 9090
          protocol: TCP
        volumeMounts:
        - mountPath: "/etc/prometheus"
          name: prometheus-config
      volumes:
      - name: prometheus-config
        configMap:
          name: prometheus-config
```

3、使用k8s下发任务。
 k8s-master执行

```bash
kubectl create -f prometheus-config.yml
kubeckubectl create -f prometheus-deployment.yml
```

4、查看任务进度。
 k8s-master执行

```bash
[root@master prometheus]# kubectl get pods    ##如果下方创建失败可以根据NAME名称查看日志
NAME                          READY     STATUS    RESTARTS   AGE
nginx-3121059884-lx6qc        1/1       Running   0          1h
nginx-3121059884-n6bdl        1/1       Running   0          1h
nginx-3121059884-n9pxz        1/1       Running   1          4h
prometheus-3596598276-5wrkl   1/1       Running   0          1h
```

5、查看失败日志（我上面是成功后的所以状态是Running）大家可以看我的报错有多次镜像拉取失败。
 k8s-master执行

```bash
 kubectl describe pod prometheus-3596598276-5wrkl
Name:           prometheus-3596598276-5wrkl
Namespace:      default
Node:           node-1/192.168.182.151
Start Time:     Fri, 01 Jan 2021 21:39:02 +0800
Labels:         app=prometheus
                pod-template-hash=3596598276
Status:         Running
IP:             10.10.17.5
Controllers:    ReplicaSet/prometheus-3596598276
Containers:
  prometheus:
    Container ID:       docker://d2887dbb516b7415f0ddf1ee1c8fbf0b389db935e361c6108c44a9b52bb6ef29
    Image:              prom/prometheus:v2.2.1
    Image ID:           docker-pullable://192.168.182.153:5000/prom/prometheus@sha256:b0912ab008c270be88f6e81d3df6dfd24b7c1f9b4aacbffa70abe2a382152223
    Port:               9090/TCP
    Command:
      /bin/prometheus
    Args:
      --config.file=/etc/prometheus/prometheus.yml
    State:              Running
      Started:          Fri, 01 Jan 2021 22:24:25 +0800
    Ready:              True
    Restart Count:      0
    Volume Mounts:
      /etc/prometheus from prometheus-config (rw)
    Environment Variables:      <none>
Conditions:
  Type          Status
  Initialized   True 
  Ready         True 
  PodScheduled  True 
Volumes:
  prometheus-config:
    Type:       ConfigMap (a volume populated by a ConfigMap)
    Name:       prometheus-config
QoS Class:      BestEffort
Tolerations:    <none>
Events:
  FirstSeen     LastSeen        Count   From                    SubObjectPath                   Type            Reason                  Message
  ---------     --------        -----   ----                    -------------                   --------        ------                  -------
  1h            58m             9       {default-scheduler }                                    Warning         FailedScheduling        no nodes available to schedule pods
  57m           57m             6       {default-scheduler }                                    Warning         FailedScheduling        no nodes available to schedule pods
  56m           56m             1       {default-scheduler }                                    Normal          Scheduled               Successfully assigned prometheus-3596598276-5wrkl to node-1
  56m           44m             14      {kubelet node-1}                                        Warning         FailedMount             MountVolume.SetUp failed for volume "kubernetes.io/configmap/0a83b05e-4c34-11eb-ae3b-000c29fd225f-prometheus-config" (spec.Name: "prometheus-config") pod "0a83b05e-4c34-11eb-ae3b-000c29fd225f" (UID: "0a83b05e-4c34-11eb-ae3b-000c29fd225f") with: configmaps "prometheus-config" not found
  54m           43m             6       {kubelet node-1}                                        Warning         FailedMount             Unable to mount volumes for pod "prometheus-3596598276-5wrkl_default(0a83b05e-4c34-11eb-ae3b-000c29fd225f)": timeout expired waiting for volumes to attach/mount for pod "default"/"prometheus-3596598276-5wrkl". list of unattached/unmounted volumes=[prometheus-config]
  54m           43m             6       {kubelet node-1}                                        Warning         FailedSync              Error syncing pod, skipping: timeout expired waiting for volumes to attach/mount for pod "default"/"prometheus-3596598276-5wrkl". list of unattached/unmounted volumes=[prometheus-config]
  32m           32m             1       {kubelet node-1}        spec.containers{prometheus}     Normal          BackOff                 Back-off pulling image "prom/prometheus:v2.2.1"
  32m           32m             1       {kubelet node-1}                                        Warning         FailedSync              Error syncing pod, skipping: failed to "StartContainer" for "prometheus" with ImagePullBackOff: "Back-off pulling image \"prom/prometheus:v2.2.1\""

  32m   14m     2       {kubelet node-1}        spec.containers{prometheus}     Warning Failed          Failed to pull image "prom/prometheus:v2.2.1": net/http: request canceled
  32m   14m     2       {kubelet node-1}                                        Warning FailedSync      Error syncing pod, skipping: failed to "StartContainer" for "prometheus" with ErrImagePull: "net/http: request canceled"

  42m   14m     3       {kubelet node-1}        spec.containers{prometheus}     Normal  Pulling                 pulling image "prom/prometheus:v2.2.1"
  11m   11m     1       {kubelet node-1}        spec.containers{prometheus}     Normal  Pulled                  Successfully pulled image "prom/prometheus:v2.2.1"
  42m   11m     2       {kubelet node-1}                                        Warning MissingClusterDNS       kubelet does not have ClusterDNS IP configured and cannot create Pod using "ClusterFirst" policy. Falling back to DNSDefault policy.
  11m   11m     1       {kubelet node-1}        spec.containers{prometheus}     Normal  Created                 Created container with docker id d2887dbb516b; Security:[seccomp=unconfined]
  11m   11m     1       {kubelet node-1}        spec.containers{prometheus}     Normal  Started                 Started container with docker id d2887dbb516b
```

6、查看容器随机分配端口。
 k8s-master执行

```bash
[root@master prometheus]# kubectl get all
NAME                DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
deploy/nginx        3         3         3            3           4h
deploy/prometheus   1         1         1            1           1h

NAME             CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
svc/kubernetes   10.10.0.1       <none>        443/TCP          4h
svc/nginx        10.10.214.157   <nodes>       80:31882/TCP     4h
svc/prometheus   10.10.165.138   <nodes>       9090:32332/TCP   1h
```

6、在web浏览器打开。

# kubernetes不能拉取私有仓库image

from: https://blog.csdn.net/kozazyh/article/details/79427119

```sh
kubectl create secret docker-registry secret_name --namespace=default \
    --docker-server=registry.cn-shanghai.aliyuncs.com --docker-username=catherlove@163.com \
    --docker-password=xxxxxxxxx
    
# --docker-server: 仓库地址
# --docker-username: 仓库登陆账号
# --docker-password: 仓库登陆密码
# --docker-email: 邮件地址(选填)
```

在yaml上添加secret_name

```yaml
   spec:
      containers:
      - image: registry.cn-shanghai.aliyuncs.com/jstone01/javademo1:1.0.0
        name: javademo1
        resources: {}
      imagePullSecrets:
      - name: secret_name
```

# jar包制作docker镜像（dockerfile）

```dockerfile
FROM openjdk:8-jdk-alpine	
VOLUME /tmp
ADD ./target/demojenkins.jar demojenkins.jar
ENTRYPOINT ["java","-jar","/demojenkins.jar", "&"]
```

测试
```sh
# 在jar包路径下制作镜像
docker build -t java-demo-01:1.0 . 
# 检查image
docker images
# 运行docker
docker run -d -p 8111:8111 java-demo-01:1.0 -t
# 浏览器访问
ipaddress:8111/user
```

3. 上传到镜像服务器（阿里云）

```sh
# 1. 登录阿里云Docker Registry
$ sudo docker login --username=cather****@163.com registry.cn-shanghai.aliyuncs.com

# 用于登录的用户名为阿里云账号全名，密码为开通服务时设置的密码。

#您可以在访问凭证页面修改凭证密码。
#2. 从Registry中拉取镜像
$ sudo docker pull registry.cn-shanghai.aliyuncs.com/jstone01/javademo1:[镜像版本号]

#3. 将镜像推送到Registry
$ sudo docker login --username=cather****@163.com registry.cn-shanghai.aliyuncs.com
$ sudo docker tag [ImageId] registry.cn-shanghai.aliyuncs.com/jstone01/javademo1:[镜像版本号]
# 例
sudo docker tag [ImageId] registry.cn-shanghai.aliyuncs.com/jstone01/javademo1:1.0.1

$ sudo docker push registry.cn-shanghai.aliyuncs.com/jstone01/javademo1:[镜像版本号]
# 例
sudo docker push registry.cn-shanghai.aliyuncs.com/jstone01/javademo1:1.0.1

#请根据实际镜像信息替换示例中的[ImageId]和[镜像版本号]参数。
#4. 选择合适的镜像仓库地址
#从ECS推送镜像时，可以选择使用镜像仓库内网地址。推送速度将得到提升并且将不会损耗您的公网流量。

#如果您使用的机器位于VPC网络，请使用 registry-vpc.cn-shanghai.aliyuncs.com 作为Registry的域名登录。
#5. 示例
#使用"docker tag"命令重命名镜像，并将它通过专有网络地址推送至Registry。
$ sudo docker images
REPOSITORY                                                         TAG                 IMAGE ID            CREATED             VIRTUAL SIZE
registry.aliyuncs.com/acs/agent                                    0.7-dfb6816         37bb9c63c8b2        7 days ago          37.89 MB

$ sudo docker tag 37bb9c63c8b2 registry-vpc.cn-shanghai.aliyuncs.com/acs/agent:0.7-dfb6816

#使用 "docker push" 命令将该镜像推送至远程。
$ sudo docker push registry-vpc.cn-shanghai.aliyuncs.com/acs/agent:0.7-dfb6816
```

4. 部署

```sh
kubectl create deploy javademo01 --image=registry.cn-shanghai.aliyuncs.com/jstone01/javademo1:1.0.1 --dry-run -o yaml > javademo1.yaml
kubectl apply -f javademo1.yaml
```

5. 暴露应用

```sh
kubectl scale deploy javademo01 --replicas=3	#扩容
kubectl expose deploy javademo01 --port=8111 --target-port=8111 --type=NodePort # 暴露端口
kubectl get svc
NAME         TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
javademo01   NodePort    10.104.23.232   <none>        8111:31274/TCP   37s

```

# Grafana+Prometheus+node_exporter监控，Grafana无法显示数据的问题

from: 

https://blog.csdn.net/weixin_40391011/article/details/113177767

环境搭建：

被测linux机器上部署了Grafana，Prometheus，node_exporter,并成功启动了它们。

Grafana中已经创建了Prometheus数据源，并测试通过，并且导入了监控面板，将对被测机器的CPU，内存，网络和磁盘进行监控。

问题：

使用windows机器登录Grafana监控被测的Linux机器，右上角时间调整为最近的12个小时，无数据显示，显示为NA，如下图：

 

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/afef3f629e0f5ecec36e087196326a98.png)

分析原因：

Prometheus这个时序数据库对时间要求很严格，Linux服务器与Windows监控机的日期，时间，时区不一致导致Grafana中监控不到数据。

解决办法：

把linux服务器时间调整为与windows监控机一样即可，调整步骤如下：

方案一：临时修改centos时间（不推荐），重启后将恢复到原来的时间，参考链接：

https://jingyan.baidu.com/article/597a0643a082a9712a52435a.html?qq-pf-to=pcqq.c2c

方案二：永久修改centos时间

1.Centos上安装ntpdate：命令 yum install ntpdate -y

2.输入命令：ntpdate ntp1.aliyun.com

3.输入命令：hwclock --sysohc

4.输入命令：timedatectl

5.重新查看一下日期，输入命令：date

6.删除原来的Prometheus，重新安装配置一下

7.输入命令：reboot，重启centos

8.先检查一下日期：输入命令date，保证时间与windows监控机一致

9.关闭防火墙，重新启动grafana，prometheus，node_exporter

10.windows登录grafana，进入监控面板，已经可以监控到数据了，问题完美解决

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/4aa243e765b3570dd2f8338a9d53cd59.png)

# master去污点

```sh
kubectl taint node ma node-role.kubernetes.io/master-
```

# k8s和SpringCloud谁更胜一筹

from: https://cloud.tencent.com/developer/article/1776313
