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

# 语言

## locale

```sh
locale 		# 显示目前所支持的语系
LANG=zh_CN.UTF-8		# 只负责输出的格式
export LC_ALL=en_US.utf8 # 所有的
```



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
lsof -i:8080	# 查看8080端口占用
lsof abc.txt	#显示开启文件abc.txt的进程
lsof -c abc	# 显示abc进程现在打开的文件
lsof -c -p 1234	# 列出进程号为1234的进程所打开的文件
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

#### 查看占cpu, 内存最大的10个应用

```sh
# linux下获取占用CPU资源最多的10个进程，可以使用如下命令组合：
ps aux|head -1;ps aux|grep -v PID|sort -rn -k +3|head  # 或者top （然后按下M，注意这里是大写）


#linux下获取占用内存资源最多的10个进程，可以使用如下命令组合：	
ps aux|head -1;ps aux|grep -v PID|sort -rn -k +4|head   # 或者top （然后按下P，注意这里是大写）


补充:内容解释

PID：进程的ID
USER：进程所有者
PR：进程的优先级别，越小越优先被执行
NInice：值
VIRT：进程占用的虚拟内存
RES：进程占用的物理内存
SHR：进程使用的共享内存
S：进程的状态。S表示休眠，R表示正在运行，Z表示僵死状态，N表示该进程优先值为负数
%CPU：进程占用CPU的使用率
%MEM：进程使用的物理内存和总内存的百分比
TIME+：该进程启动后占用的总的CPU时间，即占用CPU使用时间的累加值。
COMMAND：进程启动命令名称
```





# 网络

## netstat

### 生产环境

```sh
ss -tnulp | grep java
```



# 查看cpu型号

https://www.jianshu.com/p/a0ab0ccb8051

## 1.查看CPU详细信息

在Linux服务器上查看CPU详细信息：
 cat /proc/cpuinfo
 输出结果：

```sh
processor       : 0
vendor_id       : GenuineIntel
cpu family      : 6
model           : 62
model name      : Intel(R) Xeon(R) CPU E5-2650 v2 @ 2.60GHz
stepping        : 4
microcode       : 0x428
cpu MHz         : 1200.062
cache size      : 20480 KB
physical id     : 0
siblings        : 16
core id         : 0
cpu cores       : 8
apicid          : 0
initial apicid  : 0
fpu             : yes
fpu_exception   : yes
cpuid level     : 13
wp              : yes
flags           : fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe syscall nx pdpe1gb rdtscp lm constant_tsc arch_perfmon pebs bts rep_good nopl xtopology nonstop_tsc aperfmperf eagerfpu pni pclmulqdq dtes64 monitor ds_cpl vmx smx est tm2 ssse3 cx16 xtpr pdcm pcid dca sse4_1 sse4_2 x2apic popcnt tsc_deadline_timer aes xsave avx f16c rdrand lahf_lm ida arat epb pln pts dtherm tpr_shadow vnmi flexpriority ept vpid fsgsbase smep erms xsaveopt
bogomips        : 5200.34
clflush size    : 64
cache_alignment : 64
address sizes   : 46 bits physical, 48 bits virtual
power management:
```

上面只截取了一部分信息，
 完整的CPU信息请参考文末附录，
 这个命令输出了太多的冗余信息不方便查看，
 下面介绍的命令以该Linux输出的CPU信息为例,
 可以很方便的知道当前系统CPU的特定信息。

## 2.基本概念

请参考以下文章了解CPU的一些基本概念：
 [物理CPU，物理CPU内核，逻辑CPU概念详解](https://www.jianshu.com/p/6a53819fa89b)

## 3.查看物理CPU的个数

```sh
cat /proc/cpuinfo | grep "physical id" | sort | uniq | wc -l
 #输出结果：
 2
 #表示Linux服务器上面实际安装了2个物理CPU芯片>
```

## 4.查看物理CPU内核的个数

```sh
cat /proc/cpuinfo | grep "cpu cores" | uniq
输出结果：
 cpu cores : 8
 # 表示1个物理CPU里面有8个物理内核。
```

## 5.查看所有逻辑CPU的个数

```sh
cat /proc/cpuinfo | grep "processor" | wc -l
 # 输出结果：
 32
 # 表示Linux服务器一共有32个逻辑CPU。
```

## 6.查看每个物理CPU中逻辑CPU的个数

```sh
cat /proc/cpuinfo | grep 'siblings' | uniq
# 输出结果：
 siblings : 16
# 表示每个物理CPU中有16个逻辑CPU，
# 一共有2个物理CPU，
# 所以总共有32个逻辑CPU，
# 和第5步中查看的结果一致。
```

## 7.查询CPU是否启用超线程

```sh
cat /proc/cpuinfo | grep -e "cpu cores"  -e "siblings" | sort | uniq
# 输出结果：
 cpu cores : 8
 siblings  : 16
# 看到cpu cores数量是siblings数量一半，说明启动了超线程。
# 如果cpu cores数量和siblings数量一致，则没有启用超线程。
```

## 8.输出项的含义

cpuinfo输出了详细的信息，
 可以看到CPU具体型号等各种参数，
 下面说明各个输出项的含义：

| 输出项          | 含义                                                         |
| --------------- | ------------------------------------------------------------ |
| processor       | 系统中逻辑处理核的编号。对于单核处理器，则课认为是其CPU编号，对于多核处理器则可以是物理核、或者使用超线程技术虚拟的逻辑核 |
| vendor_id       | CPU制造商                                                    |
| cpu family      | CPU产品系列代号                                              |
| model           | CPU属于其系列中的哪一代的代号                                |
| model name      | CPU属于的名字及其编号、标称主频                              |
| stepping        | CPU属于制作更新版本                                          |
| cpu MHz         | CPU的实际使用主频                                            |
| cache size      | CPU二级缓存大小                                              |
| physical id     | 单个CPU的标号                                                |
| siblings        | 单个CPU逻辑物理核数                                          |
| core id         | 当前物理核在其所处CPU中的编号，这个编号不一定连续            |
| cpu cores       | 该逻辑核所处CPU的物理核数                                    |
| apicid          | 用来区分不同逻辑核的编号，系统中每个逻辑核的此编号必然不同，此编号不一定连续 |
| fpu             | 是否具有浮点运算单元（Floating Point Unit）                  |
| fpu_exception   | 是否支持浮点计算异常                                         |
| cpuid level     | 执行cpuid指令前，eax寄存器中的值，根据不同的值cpuid指令会返回不同的内容 |
| wp              | 表明当前CPU是否在内核态支持对用户空间的写保护（Write Protection） |
| flags           | 当前CPU支持的功能                                            |
| bogomips        | 在系统内核启动时粗略测算的CPU速度（Million Instructions Per Second） |
| clflush size    | 每次刷新缓存的大小单位                                       |
| cache_alignment | 缓存地址对齐单位                                             |
| address sizes   | 可访问地址空间位数                                           |

## 9.参考文章

[14、/proc/cpuinfo 文件(查看CPU信息)](https://links.jianshu.com/go?to=https%3A%2F%2Fwww.cnblogs.com%2Frenping%2Fp%2F7289473.html)
 [Linux CPU数量判断，通过/proc/cpuinfo.](https://links.jianshu.com/go?to=https%3A%2F%2Fwww.cnblogs.com%2Fzengkefu%2Fp%2F5579354.html)

# 查看内存 free

```sh
[shaoer@host-1707b51f8f4 ~]$ free
              total        used        free      shared  buff/cache   available
Mem:       32779656    22013572      746628     1671940    10019456     8691320
Swap:             0           0           0

第一列
# Mem 内存的使用信息
# Swap 交换空间的使用信息
第一行
# total 系统总的可用物理内存大小
# used 已被使用的物理内存大小
# free 还有多少物理内存可用
# shared 被共享使用的物理内存大小
# buff/cache 被 buffer 和 cache 使用的物理内存大小
# available 还可以被 应用程序 使用的物理内存大小

其中有两个概念需要注意
# free 与 available 的区别

# free 是真正尚未被使用的物理内存数量。
# available 是应用程序认为可用内存数量，available = free + buffer + cache (注：只是大概的计算方法)

Linux 为了提升读写性能，会消耗一部分内存资源缓存磁盘数据，对于内核来说，buffer 和 cache 其实都属于已经被使用的内存。但当应用程序申请内存时，如果 free 内存不够，内核就会回收 buffer 和 cache 的内存来满足应用程序的请求。这就是稍后要说明的 buffer 和 cache。
```

## buff 和 cache 的区别

from: https://www.cnblogs.com/M18-BlankBox/p/5326484.html

从字面上和语义来看，buffer名为缓冲，cache名为缓存。我们知道各种硬件存在制作工艺上的差别，所以当两种硬件需要交互的时候，肯定会存在速度上的差异，而且只有交互双方都完成才可以各自处理别的其他事务。假如现在有两个需要交互的设备A和B，A设备用来交互的接口速率为1000M/s，B设备用来交互的接口速率为500M/s，那他们彼此访问的时候都会出现以下两种情况：（以A来说）

一.A从B取一个1000M的文件结果需要2s，本来需要1s就可以完成的工作，却还需要额外等待1s,B设备把剩余的500M找出来，这等待B取出剩下500M的空闲时间内(1s)其他的事务还干不了

二.A给B一个1000M的文件结果也需要2s，本来需要也就1s就可以完成的工作，却由于B，1s内只能拿500M，剩下的500M还得等下一个1sB来取，这等待下1s的时间还做不了其他事务。

那有什么方法既可以让A在‘取’或‘给’B的时候既能完成目标任务又不浪费那1s空闲等待时间去处理其他事务呢？我们知道产生这种结果主要是因为B跟不上A的节奏，但即使这样A也得必须等B处理完本次事务才能干其他活（单核cpu来说），除非你有三头六臂。那有小伙伴可能会问了，能不能在A和B之间加一层区域比如说ab，让ab既能跟上A的频率也会照顾B的感受，没错我们确实可以这样设计来磨合接口速率上的差异，你可以这样想象，在区域ab提供了两个交互接口一个是a接口另一个是b接口，a接口的速率接近A，b接口的速率最少等于B，然后我们把ab的a和A相连，ab的b和B相连，ab就像一座桥把A和B链接起来，并告知A和B通过他都能转发给对方，文件可以暂时存储，最终拓扑大概如下：

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/917695-20160327123829839-630260780.png)

**示例**

现在我们再来看上述两种情况：

对于第一种情况A要B：当A从B取一个1000M的文件，他把需求告诉了ab，接下来ab通过b和B进行文件传送，由于B本身的速率，传送第一次ab并没有什么卵用，对A来说不仅浪费了时间还浪费了感情，ab这家伙很快感受到了A的不满，所以在第二次传送的时候，ab背着B偷偷缓存了一个一模一样的文件，而且只要从B取东西，ab都会缓存一个拷贝下来放在自己的大本营，如果下次A或者其他C来取B的东西，ab直接就给A或C一个货真价实的赝品，然后把它通过a接口给了A或C，由于a的速率相对接近A的接口速率，所以A觉得不错为他省了时间，最终和ab的a成了好基友，说白了此时的ab提供的就是一种缓存能力，即cache，绝对的走私！因为C取的是A执行的结果。所以在这种工作模式下，怎么取得的东西是最新的也是我们需要考虑的，一般就是清cache。例如cpu读取内存数据，硬盘一般都提供一个内存作为缓存来增加系统的读取性能

对于第二种情况A给B：当A发给B一个1000M的文件，因为A知道通过ab的a接口就可以转交给B，而且通过a接口要比通过B接口传送文件需要等待的时间更短，所以1000M通过a接口给了ab  ，站在A视图上他认为已经把1000M的文件给了B，但对于ab并不立即交给B，而是先缓存下来，除非B执行sync命令，即使B马上要，但由于b的接口速率最少大于B接口速率，所以也不会存在漏洞时间，但最终的结果是A节约了时间就可以干其他的事务，说白了就是推卸责任，哈哈而ab此时提供的就是一种缓冲的能力，即buffer，它存在的目的适用于当速度快的往速度慢的输出东西。例如内存的数据要写到磁盘，cpu寄存器里的数据写到内存。

看了上面这个例子，那我们现在看一下在计算机领域，在处理磁盘IO读写的时候，cpu，memory，disk基于这种模型给出的一个实例。我们先来一幅图：（我从别家当来的，我觉得，看N篇文档 不如瞄此一图）

![img](E:\JS\booknote\jpgBed\091523391861765.jpg)

**示例**

page  cache：文件系统层级的缓存，从磁盘里读取的内容是存储到这里，这样程序读取磁盘内容就会非常快，比如使用grep和find等命令查找内容和文件时，第一次会慢很多，再次执行就快好多倍，几乎是瞬间。但如上所说，如果对文件的更新不关心，就没必要清cache，否则如果要实施同步，必须要把内存空间中的cache clean下

buffer  cache：磁盘等块设备的缓冲，内存的这一部分是要写入到磁盘里的。这种情况需要注意，位于内存buffer中的数据不是即时写入磁盘，而是系统空闲或者buffer达到一定大小统一写到磁盘中，所以断电易失，为了防止数据丢失所以我们最好正常关机或者多执行几次sync命令，让位于buffer上的数据立刻写到磁盘里。

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

