

# 解决sublime text 3使用Install Package时出现There are no packages available for installa

from: https://www.cnblogs.com/jellify/p/9522477.html

1.在使用sublime下载扩展包的过程中，通过ctrl+shift+p打开包管理菜单界面，输入install 选中Install Package并回车，出现There are no packages available for installation的提示，导致安装插件出现问题

 ![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/5-1825333686.png)

  

2.分析原因发现，在利用sublime进行插件下载时，sublime会调用channel_v3.json文件，点击Preferences->Package Setting->Package Control ->Setting Default，可以看到该文件是放置在网络中进行读取的，而由于GFW的原因，导致无法读取该文件（但是竟然可以直接访问？？），这也就是导致插件无法下载的原因

 ![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/57-290150804.png)

  

3.我们在Preferences->Package Setting->Package Control ->Setting User 中，可以进行用户设置，我们可以将文件下载后，进行本地访问。首先访问[https://packagecontrol.io/channel_v3.json ](https://packagecontrol.io/channel_v3.json)，将源代码复制后，新建文件为channel_v3.json（也可以从github中获取源文件），然后在Setting User设置中，添加代码，至此，就可以正常使用install package下载插件。

{        "channels": [        "D:/channel_v3.json"    ] }


 ![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/7-2070942516.png)



# sublime_REPL使用及安装教程(解决Sublime无交互问题)

from： https://www.cnblogs.com/lyy135146/p/11557779.html

pycharm？IDLE？

Sublime Text具有漂亮的用户界面和强大的功能，例如代码缩略图，Python的插件，代码段等，还可自定义键绑定，菜单和工具栏。

Sublime Text 的主要功能包括：拼写检查，书签，完整的 Python API ， Goto 功能，即时项目切换，多选择，多窗口等等。

它还是一个跨平台的编辑器，同时支持Windows、Linux、Mac OS X等操作系统。

简而言之，Sublime Text你值得拥有。

我今天要说的便是sublime的一款组件SublimeREPL

首先来给大家做个示范

| 12   | a = 15print(a) |
| ---- | -------------- |
|      |                |

简单粗暴的代码，一目了然的结果。

 <img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/98-646773122.png" alt="img" style="zoom:67%;" />

那么我们稍微升一下级；

| 12   | a = input("请输入：")print("a=",a) |
| ---- | ---------------------------------- |
|      |                                    |

 依旧是简单粗暴的代码，但是结果却出人意料。

 <img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/49-320480670.png" alt="img" style="zoom:67%;" />

 

 

 sublime本身是没有交互功能的，重复一遍sublime本身是没有交互功能的！！

那如何实现交互功能呢？猜想大家已经想到了，那就是加入SublimeREPL。

接下来我将为大家带来如何安装SublimeREPL

<font color='red'>1.打开Sublime_text，按键Ctrl+shift+P打开命令面板</font>

 <img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/88-157478392.png" alt="img" style="zoom:67%;" />

<font color='red'>2.输入install，选择Package Control：Install Package（程序包控件，安装程序包）</font>

 

 <img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/94-741111360.png" alt="img" style="zoom:67%;" />

<font color='red'>3.选中后按键后敲击enter进入，在弹出的界面中搜索SublimeREPL点击下便可自动下载安装</font>

<font color='red'>4.完成安装后，打开工具，在最下方可以看到SublimeREPL即为成功安装（如下图）</font>

 <img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/4-1613257995.png" alt="img" style="zoom:67%;" />

 

 

 那么接下来我们来实验一下刚刚的代码，首先输入代码

 <img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/6-1519002345.png" alt="img" style="zoom:67%;" />

 

 

 点击工具——>SublimeREPL

由于我们的代码为python语句，所以选择python语言中的Python-RUN current file

 <img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/1-1436202159.png" alt="img" style="zoom:67%;" />

 

 运行结果如下图所示，可喜可贺、幸不辱命

 <img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/4-1206348627.png" alt="img" style="zoom:67%;" />

 

那么问题来了，每一次都要这样“麻烦”的调试程序么？

简单的方法来了，sublime可以自动设置快捷键！

简单的方法来了，sublime可以自动设置快捷键！

简单的方法来了，sublime可以自动设置快捷键！

重要的事情说三遍！！！

网上有很多改快捷键的教程，选定了一个最为简单的方法，亲测有效

<span style="color:red">首先点击首选项</span>

<span style="color:red">其次打开快捷键设置</span>

<span style="color:red">然后输入神秘代码</span>

<span style="color:red">最后关闭保存</span>

<span style="color:red">其次打开快捷键设置</span>

<span style="color:red">然后输入神秘代码</span>

<span style="color:red">最后关闭保存</span>

好了，改完键了

 是不是很简单??

最后送上神秘代码↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

| 12   | { "keys": ["f7"], "command": "toggle_setting", "args": {"setting": "word_wrap"} },{ "keys":["f5"], "caption": "SublimeREPL: Python - RUN current file", "command": "run_existing_window_command", "args":{"id": "repl_python_run","file": "config/Python/Main.sublime-menu"}} |
| ---- | ------------------------------------------------------------ |
|      |                                                              |

 

 <img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/42-677250026.png" alt="img" style="zoom:40%;" />

# sublime 加到鼠标右键

**方法一（推荐）、**

把以下代码，复制到SublimeText3的安装目录，然后重命名为：sublime_addright.inf，然后右击安装就可以了。

PS：重命名文件之前，需要先在工具--文件夹选项，查看中，把隐藏已知文件类型的扩展名前边的复选框不勾选。

```properties
[Version]
Signature="$Windows NT$"

[DefaultInstall]
AddReg=SublimeText3

[SublimeText3]
hkcr,"*\\shell\\SublimeText3",,,"用 SublimeText3 打开"
hkcr,"*\\shell\\SublimeText3\\command",,,"""%1%\sublime_text.exe"" ""%%1"" %%*"
hkcr,"Directory\shell\SublimeText3",,,"用 SublimeText3 打开"
hkcr,"*\\shell\\SublimeText3","Icon",0x20000,"%1%\sublime_text.exe, 0"
hkcr,"Directory\shell\SublimeText3\command",,,"""%1%\sublime_text.exe"" ""%%1"""
```

**方法二、**

把以下代码，复制到SublimeText3的安装目录，然后重命名为：sublime_addright.reg，然后双击就可以了。

PS:**需要把里边的Sublime的安装目录，替换成实际的Sublime安装目录。**

```properties
Windows Registry Editor Version 5.00
[HKEY_CLASSES_ROOT\*\shell\SublimeText3]
@="用 SublimeText3 打开"
"Icon"="D:\\Program Files\\Sublime Text 3\\sublime_text.exe,0"

[HKEY_CLASSES_ROOT\*\shell\SublimeText3\command]
@="D:\\Program Files\\Sublime Text 3\\sublime_text.exe %1"


[HKEY_CLASSES_ROOT\Directory\shell\SublimeText3]
@="用 SublimeText3 打开"
"Icon"="D:\\Program Files\\Sublime Text 3\\sublime_text.exe,0"

[HKEY_CLASSES_ROOT\Directory\shell\SublimeText3\command]
@="D:\\Program Files\\Sublime Text 3\\sublime_text.exe %1"
```

最后，附一个删除右键菜单的脚本吧。

把以下代码，复制到SublimeText3的安装目录，然后重命名为：sublime_delright.reg，然后双击就可以了。

```properties
Windows Registry Editor Version 5.00
[-HKEY_CLASSES_ROOT\*\shell\SublimeText3]
[-HKEY_CLASSES_ROOT\Directory\shell\SublimeText3]
```

# Sublime Text 3 build 3175 Linux 注册方法 （亲测可用）

from: https://blog.csdn.net/omnispace/article/details/81284265

```sh
> * Added these lines into  /etc/hosts
127.0.0.1       www.sublimetext.com
127.0.0.1       license.sublimehq.com
> * Used the license key
----- BEGIN LICENSE -----
sgbteam
Single User License
EA7E-1153259
8891CBB9 F1513E4F 1A3405C1 A865D53F
115F202E 7B91AB2D 0D2A40ED 352B269B
76E84F0B CD69BFC7 59F2DFEF E267328F
215652A3 E88F9D8F 4C38E3BA 5B2DAAE4
969624E7 DC9CD4D5 717FB40C 1B9738CF
20B3C4F1 E917B5B3 87C38D9C ACCE7DD8
5F7EF854 86B9743C FADC04AA FB0DA5C0
F913BE58 42FEA319 F954EFDD AE881E0B
------ END LICENSE ------
```



