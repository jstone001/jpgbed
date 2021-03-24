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

![在这里插入图片描述](https://gitee.com/jstone001/booknote/raw/master/jpgBed/20201222152208274.png)

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



