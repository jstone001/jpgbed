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

```sh
ll -rt  # 根据时间倒序排列，能看到最新生成的文件
```

## tree

以树状图列出目录的内容

## 参数

```sh
# -L  level 限制目录显示层级。
# -d  显示目录而不是文件
```

### 生产环境实例

```sh
tree -Ld 1  # 显示当前目录下，第1层级的目录
```



## pwd

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

