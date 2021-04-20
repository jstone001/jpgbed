# 更新yum源（老男孩_linux）

```sh
cat /etc/yum.repos.d/CentOS-Base.repo      #查看软件包下载源
 
cd /etc/yum.repos.d/
cp CentOS-Base.repo CentOS-Base.repo.ori
wget http://mirrors.163.com/.help/CentOS6-Base-163.repo
cp CentOS6-Base-163.repo CentOS-Base.repo
yum makecache
```

# 关闭selinux（老男孩_linux）

```sh
cat /etc/selinux/config
 
sed -i s#SELINUX=enforcing#SELINUX=disabled#g /etc/selinux/config         
grep "disabled" /etc/selinux/config  
reboot # 并重启

getenforce  		# 查看selinux状态
setenforce 0        #临时改成permissive
```

# 设置runlevel为3（老男孩_linux）

```sh
 cat /etc/inittab   #linux启动模式查看
 runlevel
 vi /etc/inittab        #修改运行级别
 
 init       # (临时）切换运行级别
 init 0     #关机
 init 3     # 命令行模式
 init 5     #桌面模式
```

# CentOS6 精简linux启动服务项（老男孩_linux）

```sh
chkconfig --list| grep "3:on"       #在3级别上启动的服务
LANG=en     #调整字符集
for oldboy in `chkconfig --list| grep "3:on"| awk '{print $1}'` do chkconfig $oldboy off;done        // 关闭全部服务
for oldboy in crond network sshd rsyslog; do chkconfig $oldboy on;done  #打开需要的服务

for oldboy in `chkconfig --list| grep "3:on"|awk '{print $1}'| grep -vE "crond|network|sshd|rsyslog"` do chkconfig $oldboy off;done     #保留4个基本服务
```

# 加大服务器文件描述符（老男孩_linux）

- 在Linux系统中一切皆可以看成是文件，文件又可分为：普通文件、目录文件、链接文件和设备文件。
- 文件描述符（file descriptor）是内核为了高效管理已被打开的文件所创建的索引，其是一个非负整数（通常是小整数），用于指代被打开的文件，所有执行I/O操作的系统调用都通过文件描述符。程序刚刚启动的时候，0是标准输入，1是标准输出，2是标准错误。

```sh
ulimit -n  	# 查看系统描述符
ulimit -HSn 65535   # 临时增加文件描述符
echo '*         - nofile        65535'>> /etc/security/limits.conf     #退出，永久长效
```

# 定时清理clietmqueue目录垃圾文件（老男孩_linux）

```sh
find /var/spool/clientmqueue/ -type -f |xargs rm -f
```

# 隐藏登录时显示的系统版本号（老男孩_linux）

```sh
cat /etc/issue  	# 登录时系统的版本，可以清空
> /dev/issue   或  cat /dev/null >/etc/issue
```

# 锁定关键的系统文件（老男孩_linux）

```sh
chattr +i /etc/password /etc/shadow /etc/group /etc/gshadow /etc/inittab  #锁定关键文件
chattr -i               #解锁
lsattr /etc/passwd      #查看是否加锁
```

# 内核的调优 /etc/sysctl.conf（老男孩_linux）

```sh
https://blog.csdn.net/libaineu2004/article/details/79195449
http://blog.51cto.com/oldboy/1336488

net.ipv4.tcp_syn_retries = 1
net.ipv4.tcp_synack_retries = 1
net.ipv4.tcp_keepalive_time = 600
net.ipv4.tcp_keepalive_probes = 3
net.ipv4.tcp_keepalive_intvl =15
net.ipv4.tcp_retries2 = 5
net.ipv4.tcp_fin_timeout = 2
net.ipv4.tcp_max_tw_buckets = 36000
net.ipv4.tcp_tw_recycle = 1
net.ipv4.tcp_tw_reuse = 1
net.ipv4.tcp_max_orphans = 32768
net.ipv4.tcp_syncookies = 1
net.ipv4.tcp_max_syn_backlog = 16384
net.ipv4.tcp_wmem = 8192 131072 16777216
net.ipv4.tcp_rmem = 32768 131072 16777216
net.ipv4.tcp_mem = 786432 1048576 1572864
net.ipv4.ip_local_port_range = 1024 65000
net.ipv4.ip_conntrack_max = 65536
net.ipv4.netfilter.ip_conntrack_max=65536
net.ipv4.netfilter.ip_conntrack_tcp_timeout_established=180
net.core.somaxconn = 16384
net.core.netdev_max_backlog = 16384


防火墙的优化，在5.8上是
net.ipv4.ip_conntrack_max = 25000000
net.ipv4.netfilter.ip_conntrack_max = 25000000
net.ipv4.netfilter.ip_conntrack_tcp_timeout_established = 180
net.ipv4.netfilter.ip_conntrack_tcp_timeout_time_wait = 120
net.ipv4.netfilter.ip_conntrack_tcp_timeout_close_wait = 60
net.ipv4.netfilter.ip_conntrack_tcp_timeout_fin_wait = 120

在6.4上是
net.nf_conntrack_max = 25000000
net.netfilter.nf_conntrack_max = 25000000
net.netfilter.nf_conntrack_tcp_timeout_established = 180
net.netfilter.nf_conntrack_tcp_timeout_time_wait = 120
net.netfilter.nf_conntrack_tcp_timeout_close_wait = 60
net.netfilter.nf_conntrack_tcp_timeout_fin_wait = 120

sysctl -p #生效
```

- [详解linux netstat输出的网络连接状态信息](http://oldboy.blog.51cto.com/2561410/1184139)
- [庖丁解牛获取连接状态数的awk数组命令](http://oldboy.blog.51cto.com/2561410/1184165)
- [awk数组命令经典生产实战应用拓展](http://oldboy.blog.51cto.com/2561410/1184177)
- [老男孩培训第八节课前awk考试题案例(门户面试题解答](http://oldboy.blog.51cto.com/2561410/1184206)
- [linux生产服务器有关网络状态的优化措施](http://oldboy.blog.51cto.com/2561410/1184228)
- [linux内核参数注释与优化](https://blog.51cto.com/yangrong/1321594)