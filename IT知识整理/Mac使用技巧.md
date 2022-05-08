# 使用技巧

## 进入root

```sh
sudo su -   # 密码是登录密码
```

## 清除Mac上Dock中的最近历史记录

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

## MAC在Finder栏显示所浏览文件夹路径的方法

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

## missing xcrun at: /Library/Developer/CommandLineTools/usr/bin/xcrun 的解决方案
1，解决方法

```bash
xcode-select --install
```

2，问题原因

更新了Mac系统。

# 安装软件

## 安装git

```sh
brew install git
git -C /usr/local/Homebrew/Library/Taps/homebrew/homebrew-core fetch --unshallow
git -C /usr/local/Homebrew/Library/Taps/homebrew/homebrew-cask fetch --unshallow
sudo chown -R $(whoami) /usr/local/share/man/man8
chmod u+w /usr/local/share/man/man8
```

## Install JD-GUI on Mac OSX

from: http://macappstore.org/jd-gui/

About the App
App name: JD-GUI
App description: jd-gui (App: JD-GUI.app)
App website: http://jd.benow.ca/
Install the App

Press <font color='red'>Command+Space</font> and type Terminal and press enter/return key.

```sh
#Run in Terminal app:
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)" < /dev/null 2> /dev/null ; brew install caskroom/cask/brew-cask 2> /dev/null
#and press enter/return key. Wait for the command to finish.
#Run:
brew cask install jd-gui
#Done! You can now use JD-GUI.
```

# mac bug

## 2015款Macbook pro 聚焦待机耗电

[2015款Macbook pro待机耗电问题解决方案 - 简书 (jianshu.com)](https://www.jianshu.com/p/08416b9a53b6)

```sh
2015款macbook pro（2015年初的，13寸）在待机时候，一晚上消耗30%的电量。这真是太可怕了。想起官网上说的待机30天呢。虽然官网的待机时间很可能是个美好的梦想，但至少待机七八天吧。按一晚上30%耗电量，估计一天就没电了。于是我询问了懂行的朋友，以及自己搜索资料，最终找到了解决问题的方法（应该是），总结如下：

第一，时间机器那里自动备份会耗电，但是我没有勾选自动备份，所以应该不是这个问题；

第二，来自于网络资料，说的挺详细的还有配图，我就不赘述了；

第三，祭出终极杀手锏，那就是访达--应用程序--实用工具--活动监视器。选择能耗。你就可以看到到底啥是高耗电的罪魁祸首了。比如我就发现聚焦这玩意儿对能耗的影响达到13左右，然后我就使用sudo mdutil -a -i off(需要管理员密码)把聚焦关闭之后，我发现两个半小时的合盖待机基本没有电量损耗。

希望能对遇到类似问题的小伙伴有所帮助。

```

