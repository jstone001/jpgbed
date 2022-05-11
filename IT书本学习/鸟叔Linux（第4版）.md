# 第0章  计算机概论

## 0.1 计算机：辅助人脑的好工具

### 0.0.1 计算机的5大单元

- 输入

- 输出

- cpu控制单元

- 计算逻辑单元

- 主存储器
### 0.1.2 CPU架构

- 精简指令集

- 复杂指令集

   多媒体微指令集：MMX, SSE, SSE2, SSE3, SSE4, AMD-3DNow!

   虚拟化微指令集：Intel-VT, AMD-SVM

   省电功能：Intel-SpeedStep, AMD-PowerNow!

   64/32 位兼容技术：AMD-AMD64, Intel-EM64T

### 0.1.3 其他单元设备

### 0.1.4 运作流程

### 0.1.5 **计算机用途的分类**

### **0.1.6** 计算机上面常用的计算单位

1 Byte = 8 bits

> 一般来说，文件容量使用的是二进制的方式，所以 1 GBytes 的文件大小实际上为：1024x1024x1024 
>
> Bytes 这么大！ 速度单位则常使用十进制，例如 1GHz 就是 1000x1000x1000 Hz 的意思。

网络传输：20M 单位是bit。 除以8是bytes，2.5M Bytes。

## **0.2** **个人计算机架构与相关设备组件**

### **0.2.1** **执行脑袋运算与判断的** **CPU**

### 0.2.2 内存

### **0.2.4** **硬盘与储存设备**

#### 传输接口

- sata

- Sas: 服务器硬盘

  

| 版本  | **带宽 (Gbit/s)** | 速度 (Mbyte/s) |
| ----- | ----------------- | -------------- |
| SAS 1 | 3                 | 300            |
| SAS 2 | 6                 | 600            |
| SAS 3 | 12                | 1200           |

- USB: 

| 版本    | 带宽 (Mbit/s) | 速度 (Mbyte/s) |
| ------- | ------------- | -------------- |
| USB 1.0 | 12            | 1.5            |
| USB 2.0 | 480           | 60             |
| USB 3.0 | 5G            | 500            |
| USB 3.1 | 10G           | 1000           |

- SSD（固态硬盘）

### **0.2.6** **主板**

## **0.3** 数据表示方式

### **0.3.2** 文字编码系统

- ASCII ：1byte
- Big5: 2bytes
- uft-8: 3bytes

## 0.4 软件程序运作



# 第1章、Linux是什么与如何学习

## 1.1 Linux 是什么

## 1.2 Torvalds的Linux发展

## 1.3 Linux当前应用的角色

## 1.4 Linux 该如何学习

### **1.4.1** **从头学习** **Linux** 基础

- 计算机基础 (http://www.study-area.org/compu/compu.htm)
- 网络基础 (http://www.study-area.org/network/network.htm)

## 1.5 重点回顾

## 1.6 本章习题

## 1.7 参考数据与延伸阅读

# 第二章、主机规划与磁盘分区

## **2.1 Linux** 与硬件的搭配

### **2.1.3** **各硬件装置在** **Linux** 中的文件名

| 装置                 | 装置在 Linux 内的文件名                              |
| -------------------- | ---------------------------------------------------- |
| SCSI/SATA/USB 硬盘机 | <font color='red'>/dev/sd[a-p]</font>                |
| USB 快闪碟           | /dev/sd[a-p] (与 SATA 相同)                          |
| VirtI/O 界面         | /dev/vd[a-p] (用于虚拟机内)                          |
| 软盘驱动器           | /dev/fd[0-7]                                         |
| 打印机               | /dev/lp[0-2] (25 针打印机)                           |
|                      | /dev/usb/lp[0-15] (USB 界面)                         |
| 鼠标                 | /dev/input/mouse[0-15] (通用)                        |
|                      | /dev/psaux (PS/2 界面)                               |
|                      | /dev/mouse (当前鼠标)                                |
| CDROM/DVDROM         | /dev/scd[0-1] (通用)                                 |
|                      | /dev/sr[0-1] (通用，CentOS 较常见)                   |
|                      | /dev/cdrom (当前 CDROM)                              |
| 磁带机               | /dev/ht0 (IDE 界面)                                  |
|                      | /dev/st0 (SATA/SCSI 界面)                            |
|                      | /dev/tape (当前磁带)                                 |
| IDE 硬盘机           | <font color='red'>/dev/hd[a-d] (旧式系统才有)</font> |

> 时至今日，由于 IDE 界面的磁盘驱动器几乎已经被淘汰，太少见了！因此现在连 IDE 界面的磁盘文件名也都被仿真成 /dev/sd[a-p] 了！此外， 如果你的机器使用的是跟因特网供货商 (ISP) 申请使用的云端机器，这时可能会得到的是虚拟机。为了加速，虚拟机内的磁盘是使用仿真器产生， 该仿真器产生的磁盘文件名为 /dev/vd[a-p] 系列的文件名喔！要注意！要注意！

### **2.1.4** 使用虚拟机学习

## **2.2** 磁盘分区

### **2.2.1** 磁盘连接的方式与装置文件名的关系

- MBR (Master Boot Record) 格式
- GPT (GUID partition table)   （更大的硬盘）

### 2.2.2 MSDOS(MBR) **与** **GPT** 磁盘分区表(partition table)

#### **MSDOS (MBR)** **分区表格式与限制**

- <font color='red'>主要启动记录区(Master Boot Record, MBR)</font>：可以安装开机管理程序的地方，有 446 bytes 
- 老系统1个扇区512kb。
- 分区表(partition table)：记录整颗硬盘分区的状态，有 64 bytes

446+4*16=510

- 446kb是开机管理程序
- 64是分区表

<font color='red'>分区类型：</font>

- P: primary 主分区 
- E: Extended 延伸分区
- L:  Logic 逻辑分区 

sd1~sd4 是给主分区和延伸分区

- <font color='red'>怎么装置文件名没有/dev/sda3 与/dev/sda4 呢？因为前面四个号码都是保留给 Primary 或Extended 用的嘛</font>
- 逻辑分区从5开始
- 主要分区与延伸分区最多可以有<font color='red'>四笔(硬盘的限制)  3P+1E</font>
- **<font color='red'>延伸分区最多只能有一个(操作系统的限制)</font>**
- 逻辑分区是由延伸分区持续切割出来的分区槽
- 能够被格式化后，作为数据存取的分区槽为主要分区与逻辑分区。<font color='red'>延伸分区无法格式化</font>
- 逻辑分区的数量依操作系统而不同，在 Linux 系统中 SATA 硬盘已经可以突破 63 个以上的分区限制

MBR存在的问题：

- 操作系统无法抓取到 2.2T 以上的磁盘容量！
- MBR 仅有一个区块，若被破坏后，经常无法或很难救援。
- MBR 内的存放开机管理程序的区块仅 446bytes，无法容纳较多的程序代码。

#### **GPT** 

​        因为过去一个扇区大小就是 512bytes 而已，不过目前已经有 4K 的扇区设计出现！为了兼容于所有的磁盘，因此在扇区的定义上面， 大多会使用所谓的逻辑区块地址(Logical Block Address, LBA)来处理。GPT 将磁盘所有区块以此 LBA(预设为 512bytes 喔！) 来规划，而第一个 LBA 称为 LBA0 (从0 开始编号)。 
​        与 MBR 仅使用第一个 512bytes 区块来纪录不同， GPT 使用了 34 个 LBA 区块来纪录分区信息！同时与过去 MBR 仅有一的区块，被干掉就死光光的情况不同， GPT 除了前面 34 个 LBA 之外，整个磁盘的最后 33 个 LBA 也拿来作为另一个备份！这样或许会比较安全些吧！详细的结构有点像底下的模样

- LBA0 (MBR 相容区块)
- LBA1 (GPT 表头纪录)
- LBA2-33 (实际纪录分区信息处)
  	<font color='red'>最大8ZB  （1ZB=2的30次TB)</font>

### 2.2.3 开机流程中的BIOS与UEFI开机检测程序

#### **BIOS**

- CMOS是主板上的储存器（硬件）
- BIOS是软件

开机流程：

1. BIOS：开机主动执行的韧体，会认识第一个可开机的装置；
2. MBR：第一个可开机装置的第一个扇区内的主要启动记录区块，内含开机管理程序；
3. 开机管理程序(boot loader)：一支可读取核心文件来执行的软件；
4. 核心文件：开始操作系统的功能...

boot loader 的主要任务有：

- 提供选单：用户可以选择不同的开机项目，这也是多重引导的重要功能！
- 载入核心文件：直接指向可开机的程序区段来开始操作系统；
- 转交其他 loader：将开机管理功能转交给其他 loader 负责。

#### **UEFI BIOS搭配GPT开机的流程**

| 比较项目               | 传统 BIOS             | UEFI               |
| ---------------------- | --------------------- | ------------------ |
| 使用程序语言           | 汇编语言              | C 语言             |
| 硬件资源控制           | 使用中断 (IRQ) 管理   | 使用驱动程序与协议 |
|                        | 不可变的内存存取      |                    |
|                        | 不可变得输入/输出存取 |                    |
| 处理器运作环境         | 16 位                 | CPU 保护模式       |
| 扩充方式               | 透过 IRQ 连结         | 直接加载驱动程序   |
| 第三方厂商支持         | 较差                  | 较佳且可支持多平台 |
| 图形化能力             | 较差                  | 较佳               |
| 内建简化操作系统前环境 | 不支援                | 支援               |

### 2.2.4  Linux 安装模式下，磁盘分区的选择(极重要)

#### 目录树结构 (directory tree)

#### 文件系统与目录树的关系(挂载)

####  distributions 安装时，挂载点与磁盘分区的规划

## 2.3 安装 Linux 前的规划

### 2.3.1 选择适当的 distribution

### 2.3.2 主机的服务规划与硬件的关系

### 2.3.3 主机硬盘的主要规划

### 2.3.4 鸟哥的两个实际案例

## 2.4 重点回顾

# 第3章 安装 CentOS7.x

## 3.1 本练习机的规划--尤其是分区参数

| 所需目录/装置 | 磁盘容量 | 文件系统   | 分区格式 |
| ------------- | -------- | ---------- | -------- |
| BIOS boot     | 2MB      | 系统自定义 | 主分区   |
| /boot         | 1GB      | xfs        | 主分区   |
| /             | 10GB     | xfs        | LVM方式  |
| /home         | 5GB      | xfs        | LVM方式  |
| swap          | 1GB      | swap       | LVM方式  |

## 3.2 开始安装 CentOS 7

# 第4章 首次登入与在线求助

## 4.1 首次登入系统

### 4.1.3 X window 与文本模式的切换

- [Ctrl] + [Alt] + [F2] ~ [F6] ：文字接口登入 tty2 ~ tty6 终端机；
- [Ctrl] + [Alt] + [F1] ：图形接口桌面。

> 从这一版 CentOS 7 开始，已经取消了使用多年的 SystemV 的服务管理方式，也就是说，从这一版开始，已经没有所谓的『执行等级 (run level) 』的概念了！ 新的管理方法使用的是 systemd 的模式，这个模式将很多的服务进行相依性管理。以文字与图形界面为例，就是要不要加入图形软件的服务启动而已～ 对于熟悉之前 CentOS 6.x 版本的老家伙们，要重新摸一摸 systemd 这个方式喔！因为不再有 /etc/inittab 啰！注意注意！

### 4.1.4 在终端界面登入 linux

## 4.2 文本模式下指令的下达

### 4.2.1 开始下达指令

```sh
locale   # 显示目前所支持的语系
LANG=en_US.utf8
export LC_ALL=en_US.utf8   # LANG 只与输出讯息有关，若需要更改其他不同的信息，要同步更新 LC_ALL 才行！
```

### 4.2.2 基础指令的操作

#### date

```sh
[root@localhost ~]# date +%Y/%m/%d
2021/04/12
[root@localhost ~]# date +%H:%M
02:33
```

#### cal

```sh
cal 2015
cal 10 2015
```

#### bc

```sh
scale=3		#小数点3位
1/3
.333
340/2349
.144
quit
```

### 4.2.3 重要的几个热键[Tab], [ctrl]-c, [ctrl]-d

#### [Tab]按键

#### [Ctrl]-c 按键

#### [Ctrl]-d 按键  = EOF   (End of File)

#### [shift]+{[PageUP]|[Page Down]}按键

## **4.3 Linux** **系统的在线求助** **man page** **与** **info page**

## **4.3.1** **指令的** **--help** **求助说明**

## 4.3.2 man 

```sh
man date
```



| 按键      | 进行工作                                                     |
| --------- | ------------------------------------------------------------ |
| /string   | 向『下』搜寻 string 这个字符串，如果要搜寻 vbird 的话，就输入 /vbird |
| ?string   | 向『上』搜寻 string 这个字符串                               |
| n, N      | 利用 / 或 ? 来搜寻字符串时，可以用 n 来继续下一个搜寻 (不论是 / 或 ?) ，可以利用 N 来进利用 / 或 ? 来搜寻字符串时，可以用 n 来继续下一个搜寻 (不论是 / 或 ?) ，可以利用 N 来进行『反向』搜寻。举例来说，我以 /vbird 搜寻 vbird 字符串， 那么可以 n 继续往下查询，用 N 往上查询。若以 ?vbird 向上查询 vbird 字符串， 那我可以用 n 继续『向上』查询，用 N 反向查 |
| 空格 or f | 下一页                                                       |
| p         | 第1页                                                        |
| b         | 上一页                                                       |
| q         | 退出man                                                      |

DATE(1)

1的含义：

| 代号 | 代表内容                                                     |
| ---- | ------------------------------------------------------------ |
| 1    | 用户在 shell 环境中可以操作的指令或可执行文件                |
| 2    | 系统核心可呼叫的函数与工具等                                 |
| 3    | 一些常用的函数(function)与函式库(library)，大部分为 C 的函式库(libc) |
| 4    | 装置文件的说明，通常在/dev 下的文件                          |
| 5    | 配置文件或者是某些文件的格式                                 |
| 6    | 游戏(games)                                                  |
| 7    | 惯例与协议等，例如 Linux 文件系统、网络协议、ASCII code 等等的说明 |
| 8    | 系统管理员可用的管理指令                                     |
| 9    | 跟 kernel 有关的文件                                         |

```sh
 man -f date
date (1)             - print or set the system date and time
date (1p)            - write the date and time

 man 1 date # <==这里是用 date(1) 的文件数据
```

```sh
[dmtsai@study ~]$ whatis [指令或者是数据] # <==相当于 man -f [指令或者是数据]
[dmtsai@study ~]$ apropos [指令或者是数据] # <==相当于 man -k [指令或者是数据]
```

## **4.3.3 info page**

- File：代表这个 info page 的资料是来自 info.info 文件所提供的；
- Node：代表目前的这个页面是属于 Top 节点。 意思是 info.info 内含有很多信息，而 Top 仅是 info.info 文件内的一个节点内容而已；
- Next：下一个节点的名称为 Getting Started，你也可以按『N』到下个节点去；
- Up：回到上一层的节点总揽画面，你也可以按下『U』回到上一层；
- Prev：前一个节点。但由于 Top 是 info.info 的第一个节点，所以上面没有前一个节点的信息。
- Space: 下一页
- del: 上一页
- h: 帮助 
- x: 关闭帮助
- tab：快速移动到下个节点
- Enter：进入这个节点 

![image-20220425104300130](/Users/anne/Downloads/js/jpgBed/image-20220425104300130.png)



| 按键                           | 进行工作                                            |
| ------------------------------ | --------------------------------------------------- |
| 空格键                         | 向下翻一页                                          |
| [Page Down]                    | 向下翻一页                                          |
| [Page Up]                      | 向上翻一页                                          |
| <font color='red'>[tab]</font> | 在 node 之间移动，有 node 的地方，通常会以 * 显示。 |
| [Enter]                        | 当光标在 node 上面时，按下 Enter 可以进入该 node 。 |
| b                              | 移动光标到该 info 画面当中的第一个 node 处          |
| e                              | 移动光标到该 info 画面当中的最后一个 node 处        |
| n                              | 前往下一个 node 处                                  |
| p                              | 前往上一个 node 处                                  |
| u                              | 向上移动一层                                        |
| s(/)                           | 在 info page 当中进行搜寻                           |
| h, ?                           | 显示求助选单                                        |
| q                              | 结束这次的 info page                                |



### **4.3.4** **其他有用的文件****(documents)**

/usr/share/doc 

## **4.4** **超简单文书编辑器：** **nano**

## **4.5** **正确的关机方法**

### **数据同步写入磁盘：** **sync**

### **惯用的关机指令：** **shutdown**

单纯执行 shutdown 之后， 系统默认会在 1 分钟后进行『关机』的动作喔！

```sh
shutdown -h now
init 0
systemctl reboot
systemctl poweroff
```



# 第5章、Linux **的文件权限与目录配置**

## **5.1** **使用者与群组**

```sh
/etc/passwd		# 系统所有用户
/etc/group		# 系统所有组
```

## **5.2 Linux** **文件权限概念**

### **5.2.1 Linux** **文件属性**

```sh
ls -l --full-time  # 显示完整日期
```

### **5.2.2** **如何改变文件属性与权限**

#### **改变所属群组****, chgrp**

```sh
chgrp users 12.txt
```

#### **改变文件拥有者****, chown**

```sh 
-R	# 对文件夹改用户
```



#### **改变权限****, chmod**

### **5.2.3** **目录与文件之权限意义

**权限对目录的重要性**

**x (access directory)**：<font color='red'>目录的 x 代表的是用户能否进入该目录</font>

**<font color='red'>放在你目录下的文件，即使不能读写，但除能删除</font>**

### **5.2.4 Linux** **文件种类与扩展名**

#### **设备与装置文件****(device)**：

- b  block 字段：以提供系统随机存取的接口设备，举例来说，硬盘与软盘等就是啦
- c character：亦即是一些串行端口的接口设备， 例如键盘、鼠标等等！这些设备的特色就是『一次性读取』的，不能够截断输出

#### **资料接口文件****(sockets)**

s  ：这种类型的文件通常被用在网络上的数据承接了，我们可以启动一个程序来监听客户端的要求， 而客户端就可以透过这个 socket 来进行数据的沟通了。最常在/run 或/tmp 这些个目录中看到这种文件类型了。

#### **数据输送文件****(FIFO, pipe)**：

p ：FIFO 也是一种特殊的文件类型，他主要的目的在解决多个程序同时存取一个文件所造成的错误问题。FIFO是 first-in-first-out 的缩写。

## **5.3 Linux** **目录配置**

### **5.3.1 Linux** **目录配置的依据****--FHS**

FHS: Filesystem Hierarchy Standard 



|                    | 可分享的(shareable)        | 不可分享的(unshareable) |
| ------------------ | -------------------------- | ----------------------- |
| 不变的(static)     | /usr (软件放置处)          | /etc (配置文件)         |
|                    | /opt (第三方协力软件)      | /boot (开机与核心档)    |
| 可变动的(variable) | /var/mail (使用者邮件信箱) | /var/run (程序相关)     |
|                    | /var/spool/news (新闻组)   | /var/lock (程序相关)    |

- 可分享的：可以分享给其他系统挂载使用的目录，所以包括执行文件与用户的邮件等数据， 是能够分享给网络上其他主机挂载用的目录；
- 不可分享的：自己机器上面运作的装置文件或者是与程序有关的 socket 文件等， 由于仅与自身机器有关，所以当然就不适合分享给其他主机了。
- 不变的：有些数据是不会经常变动的，跟随着 distribution 而不变动。 例如函式库、文件说明文件、系统管理员所管理的主机服务配置文件等等；
- 可变动的：经常改变的数据，例如登录文件、一般用户可自行收受的新闻组等。



FHS 针对目录树架构仅定义出三层目录底下应该放置什么数据而已，分别是底下这三个目录的定义：

- / (root, 根目录)：与开机系统有关；
- /usr (unix software resource)：与软件安装/执行有关；
- /var (variable)：与系统运作过程有关。

#### /  的意义和内容

![image-20220429124453657](/Users/anne/Downloads/js/jpgBed/image-20220429124453657.png)

![image-20220429124611342](/Users/anne/Downloads/js/jpgBed/image-20220429124611342.png)

![image-20220429124640335](/Users/anne/Downloads/js/jpgBed/image-20220429124640335.png)

<font color='red'>放在/sbin 底下的为开机过程中所需要的</font>

![image-20220429124917537](/Users/anne/Downloads/js/jpgBed/image-20220429124917537.png)

​	

![image-20220429125206590](/Users/anne/Downloads/js/jpgBed/image-20220429125206590.png)

/etc /bin /dev  /lib  /sbin   重要



#### /usr

usr: Unix Software Resource Unix 操作系统软件资源

就像 c;/program files/

#### /var 

主要针对常态性变动的文件，包括快取(cache)、登录档(log file)以及某些软件运作所产生的文件

# 第9章 vim程序编辑器

## 9.1 vi 与 vim

### 9.1.1 为何要学 vim

## 9.2 vi 的使用

- 一般指令模式 (command mode)

- 编辑模式 (insert mode)

- 指令列命令模式 (command-line mode)

  ​	在一般模式当中，输入『 : / ? 』三个中的任何一个按钮，就可以将光标移动到最底下那一列。

### 9.2.1 简易执行范例

### 9.2.2 按键说明

```sh
h, j, k ,l		# 方向键
ctrl+f	# pagedown
ctrl+b	# page Up
ctrl+d	# 向下移半页
ctrl+u 	# 向上移半页
```





# 第10章  认识与学习 BASH

## 10.1 认识 BASH 这个 Shell

### 10.1.1 硬件、核心与 Shell

### 10.1.2 为何要学文字接口的 shell？

### 10.1.3 系统的合法 shell 与 /etc/shells 功能

### 10.1.4 Bash shell 的功能

```sh
#命令与文件补全功能： ([tab] 按键的好处)
~/.bash_history 记录的是前一次登入以前所执行过的指令
#命令别名设定功能： (alias)
    alias lm='ls -al'
#工作控制、前景背景控制： (job control, foreground, background)
#程序化脚本： (shell scripts)
#通配符： (Wildcard)
```

### 10.1.5 查询指令是否为 Bash shell 的内建命令： type

man bash

```sh
# 无参：显示是否是内部or外部命令
# -t
    file ：表示为外部指令；
    alias ：表示该指令为命令别名所设定的名称；
    builtin ：表示该指令为 bash 内建的指令功能；
# -p 果后面接的 name 为外部指令时，才会显示完整文件名；
# -a  会由 PATH 变量定义的路径中，将所有含 name 的指令都列出来，包含 alias
```

### 10.1.6 指令的下达与快速编辑按钮（快捷键）

```sh
#反斜杠 (\)
范例：如果指令串太长的话，如何使用两行来输出？
[dmtsai@study ~]$ cp /var/spool/mail/root /etc/crontab \
> /etc/fstab /root

ctrl+u    # 光标所在处向前删除
ctrl+k    # 光村所在处向后删除
ctrl+a    # 到行首
ctrl+e    # 到行尾
```

## 10.2 Shell 的变量功能

## 10.2.1 什么是变量？

## 10.2.2 变量的取用与设定：echo, 变量设定规则, unset

#### 变数的取用: echo

变量的设定规则：

```sh
#1、变量与变量内容以一个等号『=』来连结
#2、等号两边不能直接接空格符
	如下所示为错误：
	『myname = VBird』或『myname=VBird Tsai』

#3、变量名称只能是英文字母与数字，但是开头字符不能是数字
#4、变量内容若有空格符可使用双引号『"』或单引号『'』将变量内容结合起来 【重要】
变量内容若有空格符可使用双引号『"』或单引号『'』将变量内容结合起来，但
o 双引号内的特殊字符如 $ 等，可以保有原本的特性，如下所示：
『var="lang is $LANG"』则『echo $var』可得『lang is zh_TW.UTF-8』 o 单引号内的特殊字符则仅为一般字符 (纯文本)，如下所示：
『var='lang is $LANG'』则『echo $var』可得『lang is $LANG』


#5、可用跳脱字符『 \ 』将特殊符号(如 [Enter], $, \, 空格符, '等)变成一般字符
#6、在一串指令的执行中，还需要藉由其他额外的指令所提供的信息时，可以使用反单引号『`指令`』或 『$(指 令)』。
#7、若该变量为扩增变量内容时，则可用 "$变量名称" 或 ${变量} 累加内容
#8、若该变量需要在其他子程序执行，则需要以 export 来使变量变成环境变量
#9、通常大写字符为系统默认变量，自行设定变量可以使用小写字符，方便判断
#10、取消变量的方法为使用 unset
```



# 第11章  正规表示法与文件格式化处理

## 11.1 开始之前：什么是正规表示法

## 11.2 基础正规表示法

## 11.3 延伸正规表示法

## 11.4 文件的格式化与相关处理

### 11.4.1 格式化打印： printf

### 11.4.2 awk：好用的数据处理工具

### 11.4.3 文件比对工具

### 11.4.4 文件打印准备： pr

## 11.5 重点回顾

# 第12章  学习 Shell Scripts

## 12.1 什么是 Shell scripts

systemd(CentOS7)

### 12.1.1 干嘛学习 shell scripts

- 自动化管理的重要依据

- 追踪与管理系统的重要工作

- 简单入侵检测功能

- 连续指令单一化

- 简易的数据处理

- 跨平台支持与学习历程较短

  
### 12.1.2 第一支 script 的撰写与执行

```bash
#!/bin/bash
#Program:
#This is show.....
#History:
#2015/07/16
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH
echo -e "Hello World! \a \n"
exit 0
```

cal.sh

```sh
#!/bin/bash
# Program:
# User input a scale number to calculate pi number.
# History:
# 2015/07/16 VBird First release

PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH
echo -e "This program will calculate pi value. \n"
echo -e "You should input a float number to calculate pi value.\n"
read -p "The scale number (10~10000) ? " checking
num=${checking:-"10"}	 # 开始判断有否有输入数值
echo -e "Starting calcuate pi value. Be patient."
time echo "scale=${num}; 4*a(1)" | bc -lq		# 4*a(1) 是 bc 主动提供的一个计算 pi 的函数，至于 scale 就是要 bc 计算几个小数点下位数的意思.
```

- 1、第一行 #!/bin/bash
- 2、程序内容的说明：

```sh
第1行宣告  #!/bin/bash
sh说明：
    1、内容和功能
    2、版本信息
    3、作者联络方式
    4、建档日期
    5、历史记录

例：
#Function:  This scripts function is ....
#Version:   1.1  
#Author:    Create by oldboy
#Date:      16:20 2012-03-30
#Mail:      43043894@qq.com
```

- 3、主要环境变量的宣告

```sh
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH
```

- 4、主要程序部分
- 5、执行成果告知 (定义回传值)

### 12.1.3 撰写 shell script 的良好习惯建立

```sh
script 的功能；
script 的版本信息；
script 的作者与联络方式；
script 的版权宣告方式；
script 的 History (历史纪录)；
script 内较特殊的指令，使用『绝对路径』的方式来下达；
script 运作时需要的环境变量预先宣告与设定。
```

## 12.2 简单的 shell script 练习

### 12.2.1 简单范例

showname.sh

```sh
#!/bin/bash
#Function:	show full name.
#History:	2015-09-09
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

read -p "Please input your first name:" firstname
read -p "Please input your last name:" lastname
echo -e "\nYour full name is: ${firstname} ${lastname}" 	#output	

```

create_3_files.sh

```sh
#!/bin/bash
# Program:
# Program creates three files, which named by user's input and date command.
# History:
# 2015/07/16 VBird First release

PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

# 1. 让使用者输入文件名，并取得 fileuser 这个变量；
echo -e "I will use 'touch' command to create 3 files." # 纯粹显示信息
read -p "Please input your filename: " fileuser # 提示使用者输入

# 2. 为了避免使用者随意按 Enter ，利用变量功能分析档名是否有设定？
filename=${fileuser:-"filename"} # 开始判断有否配置文件名

# 3. 开始利用 date 指令来取得所需要的档名了；
date1=$(date --date='2 days ago' +%Y%m%d) # 前两天的日期
date2=$(date --date='1 days ago' +%Y%m%d) # 前一天的日期
date3=$(date +%Y%m%d) # 今天的日期
file1=${filename}${date1} # 底下三行在配置文件名
file2=${filename}${date2}
file3=${filename}${date3}

# 4. 将档名建立吧！
touch "${file1}" # 底下三行在建立文件
touch "${file2}"
touch "${file3}"
```

multiplying.sh

```sh
#!/bin/bash
# Program:
# User inputs 2 integer numbers; program will cross these two numbers.
# History:
# 2015/07/16 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH
echo -e "You SHOULD input 2 numbers, I will multiplying them! \n"
read -p "first number: " firstnum
read -p "second number: " secnum
total=$((${firstnum}*${secnum}))
echo -e "\nThe result of ${firstnum} x ${secnum} is ==> ${total}"
```

```sh
var=$((运算内容)) #只能用于整数
echo "12.1*2"|bc
```

cal_pi.h

```sh
#!/bin/bash
# Program:
# User input a scale number to calculate pi number.
# History:
# 2015/07/16 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH
echo -e "This program will calculate pi value. \n"
echo -e "You should input a float number to calculate pi value.\n"
read -p "The scale number (10~10000) ? " checking
num=${checking:-"10"}	 # 开始判断有否有输入数值
echo -e "Starting calcuate pi value. Be patient."
time echo "scale=${num}; 4*a(1)" | bc -lq		#4*a(1) 是 bc 主动提供的一个计算 pi 的函数，至于 scale 就是要 bc 计算几个小数点下位数的意思.
```

### 12.2.2 script 的执行方式差异 (source, sh script, ./script)

局部的

```sh
sh 执行，是另开一个bash进程执行，不能保留环境变量
./    #如同sh
```

全局的

```sh
source在父环境执行，可以保留环境变量
.    #. 空格如同source
```

## 12.3 善用判断式

### 12.3.1 利用 test 指令的测试功能

```sh
test -e /dmtsai     #判断是否有该文件夹
test -e /dmtsai && echo "exist" || echo "Not exist"

#test的参数
#1. 关于某个档名的『文件类型』判断，如 test -e filename 表示存在否
-e 该『档名』是否存在？(常用)
-f 该『档名』是否存在且为文件(file)？(常用) 
-d 该『文件名』是否存在且为目录(directory)？(常用)

#2. 关于文件的权限侦测，如 test -r filename 表示可读否 (但 root 权限常有例外)
-r 侦测该档名是否存在且具有『可读』的权限？
-w 侦测该档名是否存在且具有『可写』的权限？
-x 侦测该档名是否存在且具有『可执行』的权限？

#5. 判定字符串的数据
test -z string  判定字符串是否为 0 ？若 string 为空字符串，则为 true
test -n string  判定字符串是否非为 0 ？若 string 为空字符串，则为 false。注： -n 亦可省略

#6. 多重条件判定，例如： test -r filename -a -x filename
-a  (and)两状况同时成立！例如 test -r file -a -x file，则 file 同时具有 r 与 x 权限时，才回传 true。 
-o  (or)两状况任何一个成立！例如 test -r file -o -x file，则 file 具有 r 或 x 权限时，就可回传 true。 
! 反相状态，如 test ! -x file ，当 file 不具有 x 时，回传 true
```

fileIsExist.sh    判断文件或文件夹是否存在

```sh
#!/bin/bash
#Function:  User input a filename, program will check the flowing:
#           1.) exist? 2.) file/directory? 3.) file permissions
#Author:
#Date:

PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

# 1. 让使用者输入档名，并且判断使用者是否真的有输入字符串？
echo -e "Please input a filename, I will check the filename's type and permission. \n\n"
read -p "Input a filename : " filename
test -z ${filename} && echo "You MUST input a filename." && exit 0

# 2. 判断文件是否存在？若不存在则显示讯息并结束脚本
test ! -e ${filename} && echo "The filename '${filename}' DO NOT exist" && exit 0

# 3. 开始判断文件类型与属性
test -f ${filename} && filetype="regulare file"
test -d ${filename} && filetype="directory"
test -r ${filename} && perm="readable"
test -w ${filename} && perm="${perm} writable"
test -x ${filename} && perm="${perm} executable"

# 4. 开始输出信息！
echo "The filename: ${filename} is a ${filetype}"
echo "And the permissions for you are : ${perm}"
```

### 12.3.2 利用判断符号 [ ]

<font color='red'>中括号的两端需要有空格符来分隔</font>

```
在中括号 [] 内的每个组件都需要有空格键来分隔；
在中括号内的变数，最好都以双引号括号起来；
在中括号内的常数，最好都以单或双引号括号起来。
```
ans_yn.sh

```sh
#!/bin/bash
# Program:
# This program shows the user's choice
# History:
# 2015/07/16 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

read -p "Please input(Y/N): " yn
[ "${yn}" == "Y" -o "${yn}" == "y" ] && echo "OK,continue" && exit 0
[ "${yn}" == "N" -o "${yn}" == "n" ] && echo "Oh, interrupt!" && exit 0
echo "I don't know what your choice is" && exit 0
```

### 12.3.3 <font color='red'>Shell script 的默认变数($0, $1...)</font>

```sh
$0 ：表示shell本身文件名
$# ：代表后接的参数『个数』，以上表为例这里显示为『 4 』；
$@ ：代表『 "$1" "$2" "$3" "$4" 』之意，每个变量是独立的(用双引号括起来)；  #常用
$* ：代表『 "$1c$2c$3c$4" 』，其中 c 为分隔字符，默认为空格键， 所以本例中代表『 "$1 $2 $3 $4" 』
之意。
```

how_paras.sh      shift：造成参数变量号码偏移

```sh
#!/bin/bash
# Program:
# Program shows the script name, parameters...
# History:
# 2015/07/16 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

echo "The script name is ==> ${0}"
echo "Total parameter number is ==> $#"
[ "$#" -lt 2 ] && echo "The number of parameter is less than 2. Stop here." && exit 0
echo "Your whole parameter is ==> '$@'"
echo "The 1st parameter ==> ${1}"
echo "The 2nd parameter ==> ${2}"
```

## 12.4 条件判断式

### <font color='red'>12.4.1 利用 if .... then</font>

ans_yn.sh

```sh
#!/bin/bash
# Program:
# This program shows the user's choice
# History:
# 2015/07/16 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

read -p "Please input(Y/N): " yn
if [ "${yn}" == "Y" ]||[ "${yn}" == "y" ]; then
        echo "OK, continue"
        exit 0
fi
# 判断N的情况
if [ "${yn}" == "N" ]||[ "${yn}" == "n" ]; then
        echo "Oh, intrrupt!"
        exit 0
fi
echo "I don't know what your choice is" && exit 0
```

#### 多重、复杂条件判断式

```sh
# 多个条件判断 (if ... elif ... elif ... else) 分多种不同情况执行
if [ 条件判断式一 ]; then
    当条件判断式一成立时，可以进行的指令工作内容；
elif [ 条件判断式二 ]; then
    当条件判断式二成立时，可以进行的指令工作内容；
else
    当条件判断式一与二均不成立时，可以进行的指令工作内容；
fi
```

ans_yn_3.sh

```sh
#!/bin/bash
# Program:
# This program shows the user's choice
# History:
# 2015/07/16 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

read -p "Please input(Y/N): " yn
if [ "${yn}" == "Y" ]||[ "${yn}" == "y" ]; then
    echo "OK, continue"
# 判断N的情况
elif [ "${yn}" == "N" ]||[ "${yn}" == "n" ]; then
    echo "Oh, intrrupt!"
else
    echo "I don't know what your choice is" && exit 0
fi
```

hello_2.sh

```sh
#!/bin/bash
# Program:
# Check $1 is equal to "hello"
# History:
# 2015/07/16 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

if [ "${1}" == "hello" ]; then
    echo "Hello, how are you?"
elif [ "${1}" == "" ]; then 
    echo "you MUST input parameters, ex> {${0} someword}"
else
    echo "The only parameters is 'hello'. ,ex> {${0} hello}"
fi
```
#### netstat

```sh
#netstat中localhost
若为 127.0.0.1 则是仅针对本机开放，若是0.0.0.0 或 ::: 则代表对整个 Internet 开放(更多信息请参考服务器架设篇的介绍)。

80: WWW
22: ssh
21: ftp
25: mail
111: RPC(远程过程调用)  631: CUPS(打印服务功能)
```

netstat.sh

```sh
#!/bin/bash
# Program:
# Using netstat and grep to detect WWW,SSH,FTP and Mail services.
# History:
# 2015/07/16 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

# 1. 先作一些告知的动作而已～
echo "Now, I will detect your Linux server's services!"
echo -e "The www, ftp, ssh, and mail(smtp) will be detect! \n"

# 2. 开始进行一些测试的工作，并且也输出一些信息啰！
testfile=/dev/shm/netstat_checking.txt
netstat -tuln > ${testfile} # 先转存数据到内存当中！不用一直执行 netstat
testing=$(grep ":80 " ${testfile}) # 侦测看 port 80 在否？
if [ "${testing}" != "" ]; then
    echo "WWW is running in your system."
fi
testing=$(grep ":22 " ${testfile}) # 侦测看 port 22 在否？
if [ "${testing}" != "" ]; then
    echo "SSH is running in your system."
fi
testing=$(grep ":21 " ${testfile}) # 侦测看 port 21 在否？
if [ "${testing}" != "" ]; then
    echo "FTP is running in your system."
fi
testing=$(grep ":25 " ${testfile}) # 侦测看 port 25 在否？
if [ "${testing}" != "" ]; then
    echo "Mail is running in your system."
fi
```

cal_retired.sh

```sh
#!/bin/bash
# Program:
# You input your demobilization date, I calculate how many days before you demobilize.
# History:
# 2015/07/16 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

# 1. 告知用户这支程序的用途，并且告知应该如何输入日期格式？
echo "This program will try to calculate :"
echo "How many days before your demobilization date..."
read -p "Please input your demobilization date (YYYYMMDD ex>20150716): " date2

# 2. 测试一下，这个输入的内容是否正确？利用正规表示法啰～
date_d=$(echo ${date2} |grep '[0-9]\{8\}') # 看看是否有八个数字
if [ "${date_d}" == "" ]; then
    echo "You input the wrong date format...."
    exit 1
fi

# 3. 开始计算日期啰～
declare -i date_dem=$(date --date="${date2}" +%s) # 退伍日期秒数
declare -i date_now=$(date +%s) # 现在日期秒数
declare -i date_total_s=$((${date_dem}-${date_now})) # 剩余秒数统计
declare -i date_d=$((${date_total_s}/60/60/24)) # 转为日数

if [ "${date_total_s}" -lt "0" ]; then # 判断是否已退伍
    echo "You had been demobilization before: " $((-1*${date_d})) " ago"
else
    declare -i date_h=$(($((${date_total_s}-${date_d}*60*60*24))/60/60))
    echo "You will demobilize after ${date_d} days and ${date_h} hours."
fi
```

### 12.4.2 利用 case ..... esac 判断

```sh
case $变量名称 in <==关键词为 case ，还有变数前有钱字号
 "第一个变量内容") <==每个变量内容建议用双引号括起来，关键词则为小括号 )
程序段
;; <==每个类别结尾使用两个连续的分号来处理！
 "第二个变量内容")
程序段
;;
 *) <==最后一个变量内容都会用 * 来代表所有其他值
不包含第一个变量内容与第二个变量内容的其他程序执行段
exit 1
;;
esac <==最终的 case 结尾！『反过来写』思考一下
```

hello_3.sh     可参考/etc/init.d/netconsole

```sh
#!/bin/bash
# Program:
# Show "Hello" from $1.... by using case .... esac
# History:
# 2015/07/16 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH
case ${1} in
"hello")
    echo "Hello, how are you ?"
    ;;
"")
    echo "You MUST input parameters, ex> {${0} someword}"
    ;;
*) # 其实就相当于通配符，0~无穷多个任意字符之意！
    echo "Usage ${0} {hello}"
    ;;
esac
```

show123.sh

```sh
#!/bin/bash
# Program:
# This script only accepts the flowing parameter: one, two or three.
# History:
# 2015/07/17 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

echo "This program will print your selection !"
# read -p "Input your choice: " choice # 暂时取消，可以替换！
# case ${choice} in # 暂时取消，可以替换！
case ${1} in # 现在使用，可以用上面两行替换！
"one")
    echo "Your choice is ONE"
;;
"two")
    echo "Your choice is TWO"
;;
"three")
    echo "Your choice is THREE"
;;
*)
    echo "Usage ${0} {one|two|three}"
;;
esac
```

### 12.4.3 利用 function 功能

要点：<font color='red'>function 的设定一定要在程序的最前面</font>

show123_2.sh

```sh
#!/bin/bash
# Program:
# Use function to repeat information.
# History:
# 2015/07/17 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

#打印函数
function printit(){
    echo -n "Your choice is " # 加上 -n 可以不断行继续在同一行显示
}

echo "This program will print your selection !"
case ${1} in
"one")
    printit; echo ${1} | tr 'a-z' 'A-Z' # 将参数做大小写转换！
;;
"two")
    printit; echo ${1} | tr 'a-z' 'A-Z'
;;
"three")
    printit; echo ${1} | tr 'a-z' 'A-Z'
;;
*)
    echo "Usage ${0} {one|two|three}"
;;
esac
```

show123_3.sh

```sh
#!/bin/bash
# Program:
# Use function to repeat information.
# History:
# 2015/07/17 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

function printit(){
echo "Your choice is ${1}" # 这个 $1 必须要参考底下指令的下达 }
echo "This program will print your selection !"
case ${1} in
 "one")
printit 1 # 请注意， printit 指令后面还有接参数！
;;
 "two")
printit 2
;;
 "three")
printit 3
;;
 *)
echo "Usage ${0} {one|two|three}"
;;
esac
```

## 12.5 循环 (loop)

### 12.5.1 while do done, until do done (不定循环)

#### while

```sh
#当 condition 条件成立时，就进行循环，直到condition的条件不成立才停止
while [ condition ] <==中括号内的状态就是判断式 
do <==do 是循环的开始！
    程序段落
done <==done 是循环的结束
```

cal_1_100.sh

```sh
#!/bin/bash
# Program:
# Use loop to calculate "1+2+3+...+100" result.
# History:
# 2015/07/17 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

s=0 # 这是加总的数值变数
i=0 # 这是累计的数值，亦即是 1, 2, 3....
while [ "${i}" != "100" ]
do
    i=$(($i+1)) # 每次 i 都会增加 1
    s=$(($s+$i)) # 每次都会加总一次！
done
echo "The result of '1+2+3+...+100' is ==> $s"
```

yes_to_stop.sh

```sh
#!/bin/bash
# Program:
# Repeat question until user input correct answer.
# History:
# 2015/07/17 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

while [ "${yn}" != "yes" -a "${yn}" != "YES" ]
do
    read -p "Please input yes/YES to stop this program: " yn
done
echo "OK! you input the correct answer."
```

#### until

```sh
#当 condition 条件成立时，就终止循环， 否则就持续进行循环的程序段。
condition 的条件不成立才停止
until [ condition ] 
do
    程序段落
done
```

yes_to_stop2.sh

```sh
#!/bin/bash
# Program:
# Repeat question until user input correct answer.
# History:
# 2015/07/17 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

until [ "${yn}" == "yes" -o "${yn}" == "YES" ]
do
    read -p "Please input yes/YES to stop this program: " yn
done
echo "OK! you input the correct answer." 
```



### 12.5.2 for...do...done (固定循环)

```sh
for var in con1 con2 con3 ...
do
    程序段
done
```

show_animal.sh

```sh
#!/bin/bash
# Program:
# Using for .... loop to print 3 animals
# History:
# 2015/07/17 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

for animal in dog cat elephant
do
    echo "There are ${animal}s.... "
done
```

userid.sh

```sh
#!/bin/bash
# Program
# Use id, finger command to check system account's information.
# History
# 2015/07/17 VBird first release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

users=$(cut -d ':' -f1 /etc/passwd)     # 撷取账号名称
for username in ${users}    # 开始循环进行！
do
    id ${username}
done
```

pingip.sh

```sh
#!/bin/bash
# Program
# Use ping command to check the network's PC state.
# History
# 2015/07/17 VBird first release

PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH
network="192.168.1" # 先定义一个网域的前面部分！
for sitenu in $(seq 1 100) # seq 为 sequence(连续) 的缩写之意
do
    # 底下的程序在取得 ping的回传值是正确的还是失败的！
    ping -c 1 -w 1 ${network}.${sitenu} &> /dev/null && result=0 || result=1
    # 开始显示结果是正确的启动 (UP)还是错误的没有连通 (DOWN)
    if [ "${result}" == 0 ]; then
        echo "Server ${network}.${sitenu} is UP."
    else
        echo "Server ${network}.${sitenu} is DOWN."
    fi
done
```



### <font color='red'>12.5.3 for...do...done 的数值处理</font>

```sh
for (( 初始值; 限制值; 执行步阶 )) 
do
    程序段
done
```

cal_1_100_2.sh

```sh
#!/bin/bash
# Program:
# Try do calculate 1+2+....+${your_input}
# History:
# 2015/07/17 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

read -p "Please input a number, I will count for 1+2+...+your_input: " nu
s=0
for (( i=1; i<=${nu}; i=i+1 ))
do
    s=$((${s}+${i}))
done
echo "The result of '1+2+3+...+${nu}' is ==> ${s}"
```



### 12.5.4 搭配随机数与数组的实验

what_to_eat.sh

```sh
#!/bin/bash
# Program:
# Try do tell you what you may eat.
# History:
# 2015/07/17 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

eat[1]="卖当当汉堡" # 写下你所收集到的店家！
eat[2]="肯爷爷炸鸡"
eat[3]="彩虹日式便当"
eat[4]="越油越好吃大雅"
eat[5]="想不出吃啥学餐"
eat[6]="太师父便当"
eat[7]="池上便当"
eat[8]="怀念火车便当"
eat[9]="一起吃泡面"
eatnum=9 # 需要输入有几个可用的餐厅数！

check=$(( ${RANDOM} * ${eatnum} / 32767 + 1 ))      #取随机数
echo "your may eat ${eat[${check}]}"
```

what_to_eat2.sh

```sh
#!/bin/bash
# Program:
# Try do tell you what you may eat.
# History:
# 2015/07/17 VBird First release
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH

eat[1]="卖当当汉堡"
eat[2]="肯爷爷炸鸡"
eat[3]="彩虹日式便当"
eat[4]="越油越好吃大雅"
eat[5]="想不出吃啥学餐"
eat[6]="太师父便当"
eat[7]="池上便当"
eat[8]="怀念火车便当"
eat[9]="一起吃泡面"
eatnum=9
eated=0
while [ "${eated}" -lt 3 ]; do
    check=$(( ${RANDOM} * ${eatnum} / 32767 + 1 ))
    mycheck=0
    if [ "${eated}" -ge 1 ]; then
        for i in $(seq 1 ${eated} )
        do
            if [ ${eatedcon[$i]} == $check ]; then
                mycheck=1
            fi
        done
    fi
    if [ ${mycheck} == 0 ]; then
        echo "your may eat ${eat[${check}]}"
        eated=$(( ${eated} + 1 ))
        eatedcon[${eated}]=${check}
    fi
done
```

## 12.6 shell script 的追踪与 debug

```bash
-n   # 不要执行 script，仅查询语法的问题； 
-v   # 再执行 sccript 前，先将 scripts 的内容输出到屏幕上；
-x   # 将使用到的 script 内容显示到屏幕上，这是很有用的参数！
```


## 12.7 重点回顾

1. shell script 是利用 shell 的功能所写的一个『程序 (program)』，这个程序是使用纯文本文件，将一些 shell 的语法与指令(含外部指令)写在里面，搭配正规表示法、管线命令与数据流重导向等功能，以达到我们所想要的处理目的

2. shell script 用在系统管理上面是很好的一项工具，但是用在处理大量数值运算上， 就不够好了，因为 Shell scripts 的速度较慢，且使用的 CPU 资源较多，造成主机资源的分配不良。

3. 在 Shell script的文件中，指令的执行是从上而下、从左而右的分析与执行；

4. shell script 的执行，至少需要有 r 的权限，若需要直接指令下达，则需要拥有 r 与 x 的权限；

5. 良好的程序撰写习惯中，第一行要宣告 shell (#!/bin/bash) ，第二行以后则宣告程序用途、版本、作者等

6. 对谈式脚本可用 read 指令达成；

7. 要建立每次执行脚本都有不同结果的数据，可使用 date 指令利用日期达成；

8. script 的执行若以 source 来执行时，代表在父程序的 bash 内执行之意！

9. 若需要进行判断式，可使用 test 或中括号 ( [] ) 来处理；

10. 在 script 内，$0, $1, $2..., $@ 是有特殊意义的！

11. 条件判断式可使用 if...then 来判断，若是固定变量内容的情况下，可使用 case $var in ... esac 来处理

12. 循环主要分为不定循环 (while, until) 以及固定循环 (for) ，配合 do, done 来达成所需任务！

13. 我们可使用 sh -x script.sh 来进行程序的 debug

    

