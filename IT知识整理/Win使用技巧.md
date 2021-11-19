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



# Win10

## bug

### win10 运行VMware会蓝屏

from: https://daniuwo.com/t/23155.html

关于运行VMware等虚拟机导致win10蓝屏死机（终止代码：SYSTEM_SERVICE_EXCEPTION） 

之前可以正常打开虚拟机的，但今天突然运行虚拟机系统时就死机了，通过重装软件，恢复系统备份等方式都无效，最后查出来是因为更新了Windows的相关补丁导致的，Hyper-V被默认开启

以下方法同样适用于win10家庭中文版
升级高版本的VMware16似乎能解决该冲突的问题
win10家庭中文版因为在（控制面板–>程序–>程序和功能–>启用或关闭windows功能）中没有 Hyper-V选项，因此需要在新建的记事本中输入：

```cmd
pushd "%~dp0"

dir /b %SystemRoot%\servicing\Packages\*Hyper-V*.mum >hyper-v.txt

for /f %%i in ('findstr /i . hyper-v.txt 2^>nul') do dism /online /norestart /add-package:"%SystemRoot%\servicing\Packages\%%i"

del hyper-v.txt

Dism /online /enable-feature /featurename:Microsoft-Hyper-V-All /LimitAccess /ALL
```

然后将文件名改成Hyper-V.bat，运行该文件，等待配置成功后按提示输入y，重启电脑后，就可以在（启用或关闭windows功能）中看到Hyper-V选项了，然后将该选项取消即可



### 完美解决无Internet但能正常上网的问题

from: https://www.bilibili.com/read/cv5292887/

（转载）

最近也遇到同样的问题，试过网上的几乎所有方法，例如禁用复用网卡、网络重置、禁用复用服务，也用了用修改注册表下\HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\NlaSvc\Parameters\Internet下的EnableActiveProbing项的办法，均不见效，这种情况下可以试试此方法。如果大家也出现以上状况，尤其是win10 18362（即1903）版以后的新版本，则取消上网验证通常不能解决问题。

原因分析：经注册表选项比对，发现问题出在连网返回值功能有了变化。原理是每当连网后，系统会自动向微软的dns.msftncsi.com发出请求，然后返回一个NCSI.txt的值，这一值如果正确时，则确认为已连网。而Win10最近的版本中，这个返回服务器和值有了新变化并反映为6个注册表项中。这些变化因国内部分运行商的网络重定向，不能得到正确的反馈，因此出现实际能上网却显示为无internet的问题。下图中第一张图是出错的注册表项：

![img](E:\JS\booknote\jpgBed\12d88c845430c3413a73137c4ae1cccd60efee48.png@900w_497h_progressive.png)

下图中第二张是18362版之前正确的注册表项：

![img](E:\JS\booknote\jpgBed\e0d51e3c1e827abe1cb4211cc4d06c42b13aba9d.png@900w_455h_progressive.png)

其中被选中标成蓝色的项，改回为图中所显示的值，EnableActiveProbing仍恢复为1，重启系统后，网络即能恢复正常，此方法已在多台电脑上测试有效，希望能顺利解决大家的问题！

注册表项具体在：\HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\NlaSvc\Parameters\Internet 之下，请对照图中成值修改！！！

 作者：觸碰bu菿de薀暖 https://www.bilibili.com/read/cv5292887/ 出处：bilibili

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

## Win10 家庭版没有gpedit.msc

建一个cmd文件

```cmd
@echo off

pushd "%~dp0"

dir /b C:\Windows\servicing\Packages\Microsoft-Windows-GroupPolicy-ClientExtensions-Package~3*.mum >List.txt

dir /b C:\Windows\servicing\Packages\Microsoft-Windows-GroupPolicy-ClientTools-Package~3*.mum >>List.txt

for /f %%i in ('findstr /i . List.txt 2^>nul') do dism /online /norestart /add-package:"C:\Windows\servicing\Packages\%%i"

pause
```



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

