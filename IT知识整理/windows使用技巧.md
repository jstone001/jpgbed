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

# 2018最新win10激活密匙 win10各版本永久激活序列号 win10正式版激活码分享

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

# Win10此电脑视频、图片等7个文件夹怎么去除

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

