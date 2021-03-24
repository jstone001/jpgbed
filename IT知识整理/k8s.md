## 禁用swap

from: https://blog.csdn.net/weixin_43224440/article/details/111556962

centos关闭swap分区
第一步 关闭swap分区:
swapoff -a

第二步修改配置文件 - /etc/fstab
删除swap相关行 /mnt/swap swap swap defaults 0 0 这一行或者注释掉这一行

第三步确认swap已经关闭
free -m

![t](https://img-blog.csdnimg.cn/20201222152208274.png)

若swap行都显示 0 则表示关闭成功

第四步调整 swappiness 参数
echo 0 > /proc/sys/vm/swappiness # 临时生效

vim /etc/sysctl.conf # 永久生效
#修改 vm.swappiness 的修改为 0
vm.swappiness=0
sysctl -p # 使配置生效
————————————————
版权声明：本文为CSDN博主「啊呜～」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/weixin_43224440/article/details/111556962