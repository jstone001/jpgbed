# 文件操作

## touch

用于修改文件或者目录的时间属性，包括存取时间和更改时间。若文件不存在，系统会建立一个新的文件。

###   参数

```sh
# -a 只改变访问时间
# -m 只改变修改时间
# -c 没有文件时，不创建
```

## cp

#### 生产环境实例

##### 复制文件夹内所有文件到另一个文件夹

```sh
cp -r dir1 dir2   # dir2 不存在
cp -r dir1/* dir2 	# dir2 存在
```



## mv

## rm 

## ln

## stat

（display file or file system status）查看文件具体信息

## file

（determine file type）辨识文件类型

# 文件夹操作

## ls

显示目录内容

### 参数

```sh
# -l  显示详细信息
# -a  显示包括隐藏文件的所有内容
# -t  将文件依建立时间之先后次序列出
# -h  以G，M，K查看文件大小
# -r  将文件以相反次序显示(原定依英文字母次序)
# -R  递归查看
```

### 生产环境实例

##### 根据时间倒序排列，能看到最新生成的文件

```sh
ll -rt  
```

##### 查看文件夹有多少个文件

```sh
ls |wc -w 
```



## tree

以树状图列出目录的内容

### 参数

```sh
# -L  level 限制目录显示层级。
# -d  显示目录而不是文件
```

### 生产环境实例

#### 显示当前目录下，第1层级的目录

```sh
tree -Ld 1  # 显示当前目录下，第1层级的目录
```

## cd

### 参数

```sh
# cd ~    跳转到家目录
# cd .    当前目录
# cd ..    到上级目录
# cd -    到上一个目录
```



## mkdir

## du

### 生产环境实例

```sh
du -ah --max-depth=1   # 该目录下第1级文件到夹的大小
```



## rmdir

# 进程命令

## lsof

lsof(list open files)是一个列出当前系统打开文件的工具。

```sh
# lsof -i:8000
COMMAND   PID USER   FD   TYPE   DEVICE SIZE/OFF NODE NAME
nodejs  26993 root   10u  IPv4 37999514      0t0  TCP *:8000 (LISTEN)
```

### 参数

```sh
lsof -i:8080：	# 查看8080端口占用
lsof abc.txt：	#显示开启文件abc.txt的进程
lsof -c abc：	# 显示abc进程现在打开的文件
lsof -c -p 1234：	# 列出进程号为1234的进程所打开的文件
lsof -g gid：	#显示归属gid的进程情况
lsof +d /usr/local/：	# 显示目录下被进程开启的文件
lsof +D /usr/local/：	#同上，但是会搜索目录下的目录，时间较长
lsof -d 4：				# 显示使用fd为4的进程
lsof -i -U：			# 显示所有打开的端口和UNIX domain文件
```

### 生产环境实例

#### lsof 命令来查看端口是否开放

```sh
lsof -i:1025 #如果有显示说明已经开放了，如果没有显示说明没有开放
```



## ps

### 生产环境实例

## kill

### 生产环境实例

#### 批量Kill多个进程

```sh
ps -ef|grep php|grep -v grep|cut -c 9-15|xargs kill -9  #批量Kill多个进程

#几个命令：
#"ps - ef"是linux 里查看所有进程的命令。这时检索出的进程将作为下一条命令"grep mcfcm_st"的输入。
#"grep mcfcm_st"的输出结果是，所有含有关键字"mcfcm_st"的进程，这是Oracle数据库中远程连接进程的共同特点。
#"grep -v grep"是在列出的进程中去除含有关键字"grep"的进程。
#"cut -c 9-15"是截取输入行的第9个字符到第15个字符，而这正好是进程号PID。
#"xargs kill -9"中的xargs命令是用来把前面命令的输出结果（PID）作为"kill -9"命令的参数，并执行该令。

kill -9 `ps -ef | grep xxx|awk '{print $2}' ` 	# 翻译过来就是 列出进程，找到包含xxx的行，并删除，输出pid的列。awk的作用是输出某一列，{print $2}就是输出第二列，因为第二列是pid的列
ps -ef | grep xxx | grep -v root | awk '{print $2}' | xargs kill -9   # grep -v这个参数的作用是排除某个字符。所以这里排除了root执行的命令。
from: https://blog.csdn.net/weixin_36453829/article/details/116614988
```





# 网络

## netstat

### 生产环境

```sh
ss -tnulp | grep java
```



# 过滤3剑客grep, sed, awk

## grep

### 参数

```sh
# -c  或 --count : 计算符合样式的列数
# -r  或 --recursive : 递归
# -i  忽略大小写的不同，所以大小写视为相同
# -v  反向选择，亦即显示出没有 '搜寻字符串' 内容的那一行！
grep -v ^# /etc/ssh/sshd_config    #去掉已#开头的行
grep -v '^$'  /etc/ssh/sshd_config    #去掉空行
 
# -E  将样式为延伸的正则表达式来使用。 =egrep
grep -vE "crond|sshd|network|rsyslog"   排除4个服务

# -n  打印匹配行号
# -e  匹配多个
grep -e "bin" -e "sh" ans_yn_2.sh
```



### 生产环境实例

```sh
grep -r CREATE  backup/ -l  #现在要查找某个关键字在某文件夹下的文件, 并只返回文件名
grep CREATE -R	# 匹配当前文件夹下的所有的文件
grep -10 'patten'  xxx   #打印匹配的前后10行
	tail -50 /usr/local/mysql/data/sql-slow.log |grep -3 '192.168.0.10'   # 和tail配合使用
```

## sed

## awk

# linux 修改时区

from: https://blog.csdn.net/robertsong2004/article/details/42268701

```sh
cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
```

# linux 修改主机名

```sh
vim /etc/hosts

127.0.0.1  test1

ping test1
PING test1 (192.168.132.28) 56(84) bytes of data.
64 bytes from test2 (192.168.132.28): icmp_seq=1 ttl=64 time=0.508 ms
```



# yum查看已安装的程序 

from: https://blog.csdn.net/rentian1/article/details/93768557

```sh
yum list installed 
rpm ng
```

# 防火墙开放某个端口

```sh
# 查看开放的端口
firewall-cmd --list-all

# 设置开放的端口
firewall-cmd --add-service=http --permanent
sudo firewall-cmd --add-port=80/tcp --permanent

# 重启服务墙
firewall-cmd --reload
```



# linux操作远程文件

from: http://www.ruanyifeng.com/blog/2020/08/rsync.html

## rsync

### 参数

```sh
-a 		#这是归档模式，表示以递归方式传输文件，并保持所有属性，它等同于-r、-l、-p、-t、-g、-o、-D 选项。-a 选项后面可以跟一个 --no-OPTION，表示关闭 -r、-l、-p、-t、-g、-o、-D 中的某一个，比如-a --no-l 等同于 -r、-p、-t、-g、-o、-D 选项。
-r 		#表示以递归模式处理子目录，它主要是针对目录来说的，如果单独传一个文件不需要加 -r 选项，但是传输目录时必须加。
-v 		#表示打印一些信息，比如文件列表、文件数量等。
-l 		#表示保留软连接。

--link-dest	#参数用来指定同步时的基准目录
```



### 生产环境实例

```sh
rsync -avl /Software/ root@192.168.132.43:/Software
```

