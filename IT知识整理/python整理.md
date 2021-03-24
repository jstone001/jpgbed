# Linux安装python3

一、安装依赖环境

输入命令：yum -y install zlib-devel bzip2-devel openssl-devel ncurses-devel sqlite-devel readline-devel tk-devel gdbm-devel db4-devel libpcap-devel xz-devel

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718164218911-1472763573.png)

 

二、下载Python3

1.进入opt文件目录下，cd opt/

2.下载python3  （可以到官方先看最新版本多少）

输入命令 wget https://www.python.org/ftp/python/3.7.1/Python-3.7.1.tgz

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718164656813-980412356.png)

如果出现 找不到wget命令，输入yum -y install wget，安装其依赖将会被安装

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718164745369-1073580691.png)

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718164804895-361704220.png)

 

 3.安装Python3

安装在/usr/local/python3（具体安装位置看个人喜好）

（1）创建目录：  mkdir -p /usr/local/python3

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718164845176-62828714.png)

（2）解压下载好的Python-3.x.x.tgz包(具体包名因你下载的Python具体版本不不同⽽而不不同，如：我下载的是Python3.7.1.那我这里就是Python-3.7.1.tgz)

输入命令 tar -zxvf Python-3.7.1.tgz

解压后出现python的文件夹

 ![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718164922599-603148947.png)

 

4.进入解压后的目录，编译安装。（编译安装前需要安装编译器yum install gcc）

（1）安装gcc   

输入命令 yum install gcc，确认下载安装输入“y”

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718165009734-1010614431.png)

（2）3.7版本之后需要一个新的包libffi-devel

安装即可：yum install libffi-devel -y

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718165050740-445632205.png)

（3）进入python文件夹，生成编译脚本(指定安装目录)：

 cd Python-3.7.1

./configure --prefix=/usr/local/python3 

\#/usr/local/python3为上面步骤创建的目录

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718165130728-737128762.png)

（4）编译：make

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718165154546-809408157.png)

（5）编译成功后，编译安装：make install

安装成功：

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718165214131-1197898470.png)

（6）检查python3.7的编译器：/usr/local/python3/bin/python3.7

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718165339253-191557922.png)

 

5.建立Python3和pip3的软链:

ln -s /usr/local/python3/bin/python3 /usr/bin/python3

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718165411662-1545177138.png)

ln -s /usr/local/python3/bin/pip3 /usr/bin/pip3

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718165424054-1674629624.png)

 

6.并将/usr/local/python3/bin加入PATH

（1）vim /etc/profile

（2）按“I”，然后贴上下面内容：

\# vim ~/.bash_profile

\# .bash_profile

\# Get the aliases and functions

if [ -f ~/.bashrc ]; then

. ~/.bashrc

fi

\# User specific environment and startup programs

PATH=$PATH:$HOME/bin:/usr/local/python3/bin

export PATH

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718165447405-1790588528.png)

（3）按ESC，输入:wq回车退出。

（4）修改完记得执行行下面的命令，让上一步的修改生效：

source ~/.bash_profile

![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718165505008-1801604010.png)

 

7.检查Python3及pip3是否正常可用：

python3 -V

pip3 -V

 ![img](https://img2018.cnblogs.com/blog/1730174/201907/1730174-20190718165523975-1547139881.png)

 