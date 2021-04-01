

# 解决sublime text 3使用Install Package时出现There are no packages available for installa

from: https://www.cnblogs.com/jellify/p/9522477.html

1.在使用sublime下载扩展包的过程中，通过ctrl+shift+p打开包管理菜单界面，输入install 选中Install Package并回车，出现There are no packages available for installation的提示，导致安装插件出现问题

 ![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/5-1825333686.png)

  

2.分析原因发现，在利用sublime进行插件下载时，sublime会调用channel_v3.json文件，点击Preferences->Package Setting->Package Control ->Setting Default，可以看到该文件是放置在网络中进行读取的，而由于GFW的原因，导致无法读取该文件（但是竟然可以直接访问？？），这也就是导致插件无法下载的原因

 ![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/57-290150804.png)

  

3.我们在Preferences->Package Setting->Package Control ->Setting User 中，可以进行用户设置，我们可以将文件下载后，进行本地访问。首先访问[https://packagecontrol.io/channel_v3.json ](https://packagecontrol.io/channel_v3.json)，将源代码复制后，新建文件为channel_v3.json（也可以从github中获取源文件），然后在Setting User设置中，添加代码，至此，就可以正常使用install package下载插件。

{        "channels": [        "D:/channel_v3.json"    ] }


 ![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/7-2070942516.png)