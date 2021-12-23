# VDI管理员应该掌握的四种虚拟磁盘格式

from: https://blog.csdn.net/u013641333/article/details/52325168

原文链接地址：http://virtual.51cto.com/art/201303/383246.htm

虚拟桌面项目依赖磁盘镜像文件来存储数据，市场上存在多种不同的虚拟磁盘镜像文件格式，它们的差异很难区分。

磁盘镜像文件存储硬盘内的所有内容。它们用于各种不同的虚拟化软件，格式也各不相同，而且跟各自的原开发厂商兼容性最好。例如，VMDK虚拟磁盘格式与VMware vSphere和View集成。

这也就是说，各种不同虚拟化产品的虚拟磁盘镜像模式及其发展方向各有不同。因此，我们需要了解它们的主要区别。下面是最常见的几种类型的纲要、其相关的软件和需要了解的限制。

**VMware VMDK**

VMware创建了VMDK格式，作为其服务器和桌面虚拟化产品的主流磁盘镜像文件格式。最初就是作为开放文件格式发布的，允许其它软件使用VMDK的处理程序。例如Oracle的VirtualBox就可以读取VMDK卷。

VMDK卷支持镜像、精简配置(指的是文件可以根据需求而自动扩展，直到创建时指定的最大卷容量)以及高级功能，例如支持64K数据块。这意味着如果某个大扇区驱动器，如4K一个扇区的，在VMDK的文件系统内设置为非零时，该文件只会占用一个64K的数据块来代表这个簇。(我接下来将提到的VirtualBox VDI簇，使用1MB大小的页面，所以4K的簇将会占用整个1MB的空间来表示。)

所以，VMDK在空间的使用上效率更高。对于虚拟桌面管理员，这意味着VMDK文件可以为某些客户端提供精简卷，比其它的虚拟磁盘镜像文件格式避免了浪费。

VMDK规格现在已经到第五代。VMware以“技术备忘录”的方式和其它的开发者文档一起发布了其规格，或者也可以在线发送规格请求。由于供应商在业界的广泛接受度，其它的虚拟化产品识别VMDK磁盘镜像文件变得很普遍，甚至有部分产品也是基于该格式工作。

**Oracle VirtualBox VDI**

VDI(Virtual Disk Image)是Oracle VM VirtualBox的默认虚拟磁盘格式。该格式支持很多在其它虚拟化产品的磁盘镜像文件中具备的功能，例如快照和精简卷。

您也可以在VirtualBox内使用别的磁盘文件格式，但是在对其进行维护之前需要先转化为VDI格式——我曾经见过转过过程失败的例子。最佳的办法是创建VirtualBox VM，同时挂载需要转化的镜像文件和一个全新的VDI格式镜像，然后通过磁盘拷贝工具把内容全拷过来。

很多虚拟磁盘镜像文件格式可以转化为VDI格式。最好不要使用它来为桌面客户端准备镜像，除非也使用VirtualBox作为客户端或者您已经验证过客户端可以无缝地使用VDI磁盘镜像格式。

虽然这种格式在技术上是公开的，但很难找到其规格的详细文档，只有VirtualBox的源代码是公开的。在VirtualBox社区论坛公告内有一个非常详细的技术细节描述，但是从2008年之后就没有更新过。

**微软VHD**

Connectix创建了VHD格式用于其虚拟PC产品，之后被微软认可并广泛用于其Virtual Server和Hyper-V虚拟平台中。VHD格式的规格也是公开的，任何人都可以部署。和其它的虚拟磁盘格式类似，VHD是位于宿主机文件系统内的一个文件。该磁盘镜像文件支持的功能包括固定和动态的磁盘大小，差分复制和快照。

在使用Windows OS时，VHD也有一些独有的优势。其中之一是较新的Windows版本的引导程序都可以配置为直接从VHD文件启动。这也就是说指定的系统可以启动多个独立存储的VHD Windows OS，相应的，可以通过其它的Windows实例来进行操作。

VHD还可以直接在Windows中挂载并通过微软的磁盘管理器工具直接指定驱动器盘符，不需要其它工具支持就能访问VHD磁盘镜像文件内的内容。这也就是说桌面虚拟化管理员可以部署VHD作为最新的Windows OS版本(Windows 7及其最新结果)，并使用VHD的各种功能，而不仅仅是作为虚机。

记住一点，VHD不能大于2TB，这对于现在3TB及更大的磁盘已经在最终用户端使用而言，是个问题。微软在Windows Server 2012中发布的VHDX格式把该限值提升到64TB。

**Parallels HDD**

Parallel软件的HDD格式，是Mac OS X软件hypervisor使用的，主要用于支持Mac用户在自己的系统内运行Windows。

和VDI虚拟磁盘镜像文件格式类似，HDD主要是Parallel使用，很少有其它程序。VirtualBox无需转化，直接挂载和使用HDD磁盘镜像文件，但仅限它们是“简单格式”HDD文件(Parallel V2或更早版本)情况。在VirtualBox中使用最新的版本需要先进行转换。

# VMware虚拟机nat模式连不上网

from: https://blog.csdn.net/main_h_/article/details/56291977

我的虚拟机总是各种连不上网，每次都要折腾一番。现在我把虚拟机连不上网的原因总体排查一下，按照流程一步步来，基本上可以解决大部分人的问题。

首先，在VMware的编辑->虚拟网络编辑器重新建立ＮＡＴ网络**（之前的要删掉，新建的同样选择ＶＭｎｅｔ８就可以）。**

 ![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/221184054745.png)

如果还不能上网，在windows的服务里面，看一下

 ![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/221184304065.png)

确保这三项已经打开，如果服务处于停止状态重新开启并且设置为自动运行。（**注：使用360加速球优化可能会把VMware DHCP服务关闭。如果优化被360关闭吧VMware服务添加360白名单）**

做完这一些需要重启虚拟机，如果还是不能连接网络的话进去windows的网络和共享中心*（控制面板）。把vmnat8的iPV4地址设置为自动获取，dns也自动获取。

 ![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/221184901749.png)

重启虚拟机。

如果还是无法连接到网络，在虚拟机里面的系统重新新建一个以太网连接，地址设置为自动获取，使用新配置的连接去联网。

到此，虚拟机一般都可以成功连接到网络了，除非你的主系统没网。

# Vmware fusion 8 Pro(虚拟机软件) Mac 破解版(附注册机和序列号)

from:https://www.jb51.net/softs/377756.html

**Fusion 8序列号：**

FY75A-06W1M-H85PZ-0XP7T-MZ8E8

ZY7TK-A3D4N-08EUZ-TQN5E-XG2TF

FG1MA-25Y1J-H857P-6MZZE-YZAZ6

# 虚拟机安装xp出现：pxe-mof exiting intel pxe rom operating system not found

**装虚拟机时vmware启动一个GhostVistaSp1总显示PXE-MOF:Exiting Intel PXE ROM. 。然后是Operating System not found ……安装盘是……**

在分区进没有设置为<font color='red'>作用分区</font>，即活动分区。

 主要分区跟活动分区是不同的.不设置活动分区,如果你是用GHOST来安装系统的话就会导致无法引导系统.除非你是一步步地安装系统,那样它就会自动帮你把系统盘设置成活动盘.活动分区只有主分区才能设置,逻辑分区无法设置.选择一个主分区,右键在菜单最下面一项右拉会看到设置为活动盘或是作用盘,如果你的是PQ是中文版就会这样显示,最后一项是进阶到>>设置作用盘.如果是英文版,则是set active这个是活动的英文

# VMWare 16 关闭443端口

from: https://linzyjx.com/archives/65.html

升级到VMware16以后，首选项菜单里的共享虚拟机被砍了，但是它的功能没有被禁用。甚至于如果15禁用过这个功能的，升级到16又自动开启了。但是开启以后GUI里都没地方关了啊摔，我本地服务器跑都跑不起来了啊。坑爹呢这是！

要禁用就不能指望GUI了，可以修改配置文件解决。管理员权限打开`C:\ProgramData\VMware\hostd\proxy.xml`文件，并将`httpsPort`字段设置为`-1`即可禁用。然后在Windows服务里重启`VMware Workstation Server`服务即可。

![配置文件修改示意](https://gitee.com/jstone001/booknote/raw/master/jpgBed/2667336790.png)

鉴于中文互联网目前没看到相关解决办法，写一下以备查询。

VMware这波操作也是醉了，要砍掉的东西为什么还会继续存在，配置还在启用。