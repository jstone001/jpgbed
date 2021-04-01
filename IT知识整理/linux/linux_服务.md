# CentOS7

## 防火墙

```bash
from: https://www.jianshu.com/p/04e23d24e853
# 1、firewalld和iptables的区别

在centos7下默认使用的是firewalld，但是在工作中，大多是时间用到的是iptables，所以推荐使用iptables
iptables默认每个服务都是开启的，需要拒绝才能限制；firewalld默认每个服务都是拒绝的，需要设置才能开放
iptables 服务在 /etc/sysconfig/iptables 中储存配置；而 FirewallD 将配置储存在 /usr/lib/firewalld/ 和 /etc/firewalld/ 中的各种 XML 文件里
使用 iptables 的时候每一个单独更改意味着清除所有旧有的规则和从 /etc/sysconfig/iptables 里读取所有新的规则；使用 firewalld 却不会再创建任何新的规则；仅仅运行规则中的不同。因此 FirewallD 可以在运行时改变设置而不丢失现行配置。

# 2、firewalld运用
firewall-cmd --list-all        #查看firewalld所有开放的规则
默认开放的服务只有ssh和ipv6

firewall-cmd --list-services    #查看已经开放的服务
firewall-cmd --list-port    #查看已经开放的端口
firewall-cmd --reload    #配置生效
firewall-cmd --add-port=8080/tcp --permanent    #开放8080端口（--permanent 永久生效，需要reload）
firewall-cmd --add-service=http --permanent    #开放http服务
firewall-cmd --remove-port=8080/tcp --permanent    #关闭8080端口
firewall-cmd --remove-service=http --permanent    #移除http服务
firewall-cmd --add-forward-port=port=80:proto=tcp:toport=8080 --permanent    #将80端口的流量转发到8080
firewall-cmd --zone=public--add-forward-port=port=80:proto=tcp:toport=8080:toaddr=192.168.217.128 --permanent     #将本地的80端口，转发到192.168.217.128机器的8080端口

systemctl stop firewalld.service    #关闭firewall
systemctl start firewalld.service    #开启firewall
systemctl status firewalld.service    #查看firewall状态

# 3、iptables安装
#前言：使用iptables时，需要关闭firewalld并卸载
systemctl stop firewalld.service    #停止firewall
systemctl disable firewalld.service    #禁止firewall开机启动
yum -y install iptables-services    #安装iptables
systemctl restart iptables.service    #重启iptables

# 4、iptables配置规则
iptables -L -n    #查看详细策略
iptables -F    #清除预设表中的所有规则链的规则
iptables -X    #清除预设表中使用者自定链中的规则

# iptables下可以通过以下两种方式来修改防火墙规则
#4.1、直接修改/etc/sysconfig/iptables文件（注意：修改iptables文件后，需要重启iptables才能永久生效）
#4.2、使用命令直接修改（注意：需要save生效）
iptables -I INPUT -p tcp -m state --state NEW -m tcp --dport 8080 -j ACCEPT    #开放8080端口
service iptables save    #使配置生效

# 5、iptables配置小结
iptables -I INPUT -p tcp -m multiport --dport 22,80 -j ACCEPT    #同时开放22和80端口
iptables -I INPUT -p tcp --dport 5000:6000 -j ACCEPT    #开放5000-6000端口
iptables -I INPUT -p all -s 0.0.0.0/0 -j ACCEPT    #允许某个网段的ip访问
iptables -I INPUT -s 0.0.0.0 -p tcp --dport 8080 -j ACCEPT    #允许某个ip的8080端口访问
iptables -I INPUT -p tcp -s 0.0.0.0 -j DROP    #禁止某台主机访问
iptables -D INPUT 7    #移除第七条规则
（注：放行使用ACCEPT，禁止使用DROP）

#6、端口准发
iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-ports 8080    #将本机的80端口流量转发到8080端口
iptables -t nat -A OUTPUT -d localhost -p tcp --dport 80 -j REDIRECT --to-ports 8080
iptables -t nat -A PREROUTING -p tcp -m tcp --dport 8282 -j DNAT --to-destination 192.168.217.129:8181
```

