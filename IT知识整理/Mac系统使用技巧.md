# 清除Mac上Dock中的最近历史记录

from:https://wenku.baidu.com/view/b3d91f4c1ed9ad51f11df21f.html

清除Mac上Dock中的最近历史记录

```
1. 打开Finder
2. 前往 > 个人
3. 打开隐藏选项，显示所有文件和文件夹
4. 打开文件夹： 资源库 > Preferences
5. 找到指定应用的plist文件并删除之
```

******

Dock中停放的程序为方便起见右键会显示最近访问文件的历史记录，不过有些即使在程序中清空了最近访问记录，依旧会“坚守”在Dock程序图标的右键列表中。要清除这些，除了使用专门的系统设置优化软件以外，也可以手动访问文件进行删除。

大部分的程序，如果要清空访问历史记录，只需要在菜单中找到类似的［最近使用的项目］，然后从Dock上退出一次程序再打开就没有了。

但是遇到QuickTime Player这样的顽固份子，情况就有差别了，明明已经将最近打开的记录清空。

但是在Dock上程序图标的右键中还是存在着播放列表，幸亏没有啥不良观影记录，否则别人借用电脑的时候就要尴尬了。

除了QuickTime Player、文本编辑程序、Textwrangler也有同样的情况。要清除这些记录，需要用Finder找到保存记录的文件位置。打开Finder，按住Option点击 前往 ，打开 资源库（~/Library/）。在搜索框中搜索：lsshare，并将搜索结果定位到“资源库”，

例如，我这里可以直接删除其中的 com.apple.QuickTimePlayerX.LSSharedFileList.plist文件，也可以全选删除所有这些。退出Dock上的程序再打开看看，呵呵，世界清静了。

# MAC在Finder栏显示所浏览文件夹路径的方法

from:http://www.xitongzhijia.net/xtjc/20150129/36428.html

我们在使用MAC时，Finder栏默认只显示当前浏览的文件夹名称，而没有显示访问路径，这个问题该怎么解决呢？

![52-1501291II6142.jpg](https://gitee.com/jstone001/booknote/raw/master/jpgBed/52-1501291ii6142.jpg)

**操作步骤：**

　　打开“终端”（应用程序-》实用工具），输入以下两条命令：

```sh
defaults write com.apple.finder _FXShowPosixPathInTitle -bool TRUE;killall Finder
```

你看完整的路径地址出来了吧。

**如何恢复默认状态呢？**

打开“终端”（应用程序-》实用工具），输入以下两条命令：

```sh
defaults delete com.apple.finder _FXShowPosixPathInTitle;killall Finder
```

这就是如何在Finder栏上显示当前浏览文件的访问路径的方法，有需要或是有兴趣的用户，可以尝试下这种方法。