# 第0章  计算机概论

## 0.1 计算机：辅助人脑的好工具

### 0.0.1 计算机的5大单元

- 输入

- 输出

- cpu控制单元

- 计算逻辑单元

- 主存储器
### 0.1.2 CPU架构

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

ctrl+u    #光标所在处向前删除
ctrl+k    #光村所在处向后删除
ctrl+a    #到行首
ctrl+e    #到行尾
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

    

