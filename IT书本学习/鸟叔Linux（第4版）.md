# 第0章  计算机概论

## 0.1 计算机：辅助人脑的好工具

### 0.0.1 计算机的5大单元

- 输入

- 输出

- cpu控制单元

- 计算逻辑单元

- 主存储器
### 0.1.2 CPU架构

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

    

