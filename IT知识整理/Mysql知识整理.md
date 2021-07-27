## 修改主键自增值

```sql
SELECT AUTO_INCREMENT  FROM INFORMATION_SCHEMA.`TABLES` WHERE table_name='tb_episode';		#查一下当前值

ALTER TABLE tb_episode AUTO_INCREMENT= 249612;			#修改下一个值

SELECT AUTO_INCREMENT  FROM INFORMATION_SCHEMA.`TABLES` WHERE table_name='tb_episode';	#查询自增值
```

# 报错

## Mysql 错误代码： 1093, 对一个表的字段查询并修改

from:https://blog.csdn.net/qq_29672495/article/details/72668008

在MySQL中，写SQL语句的时候 ，可能会遇到You can't specify target table '表名' for update in FROM clause这样的错误，它的意思是说，不能先select出同一表中的某些值，再update这个表(在同一语句中)，即不能依据某字段值做判断再来更新某字段的值。

```sql
#做一个中间表

UPDATE `tb_gallery` SET companyid=6 WHERE galleryid IN  ( SELECT a.galleryid FROM tb_gallery a ,tb_actress_gallery b WHERE a.`GALLERYID`=b.`GALLERYID` AND b.`actressid`=6) ;

# 改成：
#双表
UPDATE `tb_gallery` SET companyid=6 WHERE galleryid IN (SELECT galleryid FROM ( SELECT a.galleryid FROM tb_gallery a,  tb_actress_gallery b WHERE a.`GALLERYID`=b.`GALLERYID` AND b.`actressid`=6) c);

#单表
UPDATE   `tb_htxt` SET  mygrade= '9' WHERE htxtId IN 
  (SELECT    htxtId   FROM   
  (SELECT       a.htxtid     FROM      tb_htxt a 
      WHERE a.`myGrade` = 'A') b) ;
```



## 解决Linux下MySQL登录ERROR 1045 (28000): Access denied for user 'root'@'localhost' (using passwor)问题

```sh
from: https://blog.csdn.net/vv19910825/article/details/82979563

文章转载自：https://www.cnblogs.com/gumuzi/p/5711495.html

一般这个错误是由密码错误引起，解决的办法自然就是重置密码。
假设我们使用的是root账户。
1.重置密码的第一步就是跳过MySQL的密码认证过程，方法如下：
#vim /etc/my.cnf(注：windows下修改的是my.ini)
在文档内搜索mysqld定位到[mysqld]文本段：
/mysqld(在vim编辑状态下直接输入该命令可搜索文本内容)
在[mysqld]后面任意一行添加“skip-grant-tables”用来跳过密码验证的过程，如下图所示：
p
保存文档并退出
 
2.接下来我们需要重启MySQL：
/etc/init.d/mysql restart(有些用户可能需要使用/etc/init.d/mysqld restart)
p
3.重启之后输入mysql即可进入mysql。
 
p
4.接下来就是用sql来修改root的密码
进入到终端当中，敲入 mysql -u root -p 命令然后回车，当需要输入密码时，直接按enter键，便可以不用密码登录到数据库当中
mysql> update user set password=password('你的新密码') where user='root';
mysql> flush privileges;
mysql> quit
注意：如果在执行该步骤的时候出现ERROR 1290 (HY000): The MySQL server is running with the --skip-grant-tables option so it cannot execute this statement 错误。则执行下 flush privileges 命令，再执行该命令即可。
到这里root账户就已经重置成新的密码了。
5.编辑my.cnf,去掉刚才添加的内容，然后重启MySQL。大功告成！
 网上有很多关于这个问题的解决说明，很多刚接触的朋友可能比较迷惑的是在自己的平台上找不到my.cnf或者my.ini文件，如果你是Linux,使用如下方式可以搜索到：
p
至于windows平台，去安装目录下找一下my.ini吧。
```

## mysql提示The server quit without updating PID file /usr/local/mysql/data/localhost.localdomain.pid

```sh
from: https://blog.csdn.net/zhou75771217/article/details/82893997
 
 
The server quit without updating PID file (/usr/local/mysql/var/xxx.pid). ... failed错误解决
错误信息详细描述：
root@MyServer:~# service mysql start
Starting MySQL
..The server quit without updating PID file (/usr/local/mysql/var/MyServer.pid). ... failed!
报错如下图：
p
错误解决排查思路：
1.可能是/usr/local/mysql/data/rekfan.pid文件没有写的权限
解决方法 ：给予权限，执行 “chown -R mysql:mysql /var/data” “chmod -R 755 /usr/local/mysql/data”  然后重新启动mysqld！
2.可能进程里已经存在mysql进程
解决方法：用命令“ps -ef|grep mysqld”查看是否有mysqld进程，如果有使用“kill -9  进程号”杀死，然后重新启动mysqld！
3.可能是第二次在机器上安装mysql，有残余数据影响了服务的启动。
解决方法：去mysql的数据目录/data看看，如果存在mysql-bin.index，就赶快把它删除掉吧，它就是罪魁祸首了。
4.mysql在启动时没有指定配置文件时会使用/etc/my.cnf配置文件，请打开这个文件查看在[mysqld]节下有没有指定数据目录(datadir)。
解决方法：请在[mysqld]下设置这一行：datadir = /usr/local/mysql/data
5.skip-federated字段问题
解决方法：检查一下/etc/my.cnf文件中有没有没被注释掉的skip-federated字段，如果有就立即注释掉吧。
6.错误日志目录不存在
解决方法：使用“chown” “chmod”命令赋予mysql所有者及权限
7.selinux惹的祸，如果是centos系统，默认会开启selinux
解决方法：关闭它，打开/etc/selinux/config，把SELINUX=enforcing改为SELINUX=disabled后存盘退出重启机器试试。
我遇到得是该错误使用的是第二种方法解决的，祝好运。
解决方式如下图
p
p

```

## MYsql：Plugin 'FEDERATED' is disabled.或1067错误 启动错误与“服务 mysql 意外停止”解决方法

```sh
from: https://blog.csdn.net/kang89/article/details/6404792

MYSQL启动报1067错误，系统日志中是“服务 mysql 意外停止” Mysql日志中则是：“Plugin 'FEDERATED' is disabled”
网我在网上找到解决方案：
1、在MY.INI文件中的 [mysqld] 中增加一行
tmpdir="D:/MySQL/data/"
修改后，还是启动不了或者能启动但关机后又出现同样问题，接着我做了第二步，重启正常。
2、删除DATA目录下除数据库文件夹外的其他文件，重启mysql，问题解决。 

主要是以ib开头的文件
-rw-rw---- 1 mysql mysql 134217728 Aug 17 11:38 ibdata1
-rw-rw---- 1 mysql mysql   4194304 Aug 17 11:38 ib_logfile0
-rw-rw---- 1 mysql mysql   4194304 Aug 17 11:38 ib_logfile1
-rw-rw---- 1 mysql mysql   4194304 Aug 17 11:38 ib_logfile2
```

## MySQL 1130错误，无法远程连接

from: https://www.cnblogs.com/devan/p/7055339.html

MySQL 1130错误，无法远程连接

 

![img](E:\JS\booknote\jpgBed\04-209941648.png)

 

```sql
错误：ERROR 1130: Host '192.168.1.3' is not allowed to connect to thisMySQL serve
错误1130：主机192.168.1.3”不允许连接到thismysql服务
原因：被连接的数据不允许使用 192.168.1.3访问，只允许是用 localhost;
  

解决办法：
可能是你的帐号不允许从远程登陆，只能在localhost。这个时候只要在localhost的那台电脑，登入mysql后，更改"mysql" 数据库里的 "user" 表里的 "host"项，从"localhost"改称"%"

 1、启用 cmd 
输入 ： mysql -u -root -p 
若是提示 ‘mysql’不是内部或外部命令，也不是可运行的程序.那需要配置环境变量 在配置 path 变量添加 “……\mysq\/MYSQL Server5.7\bin”
 

总体运行语句:
mysql -u root -p
mysql>use mysql;
mysql>select 'host' from user where user='root';
mysql>update user set host = '%' where user ='root';
mysql>flush privileges;
mysql>select 'host'  from user where user='root';

运行完毕后再次连接测试，若还不行重启mysql服务，或是直接重启电脑
```

## ERROR 1062 (23000): Duplicate entry '%-root' for key 'PRIMARY'

from: https://blog.csdn.net/lee_sire/article/details/46277057

查看了下数据库的host信息如下： 

host已经有了%这个值，所以直接运行命令：

# 数据库备份

## 生成备份的sql

```sh
mysqldump -uroot -p123 --databases db_txt > e:\mysql_backup\db_txt.sql
```

# Mysql安装

## mysql的Linux安装

```sh
https://www.cnblogs.com/zk-blog/p/11097514.html

一、清理老的mysql
（1）卸载已有的mysql
查找以前是否装有mysql命令：
1      rpm -qa|grep -i mysql
         停止mysql服务，卸载之前安装的mysql
1 rpm -ev 包名
如果卸载过程中报依赖错误，直接在卸载命名后面加参数 --nodeps
1 rpm -ev 包名 --nodeps  
（2）查找之前老版本mysql的文件并删除老版本mysql的文件 
1 find / -name mysql
2 # 出来一堆列表
3 # 一个一个删除就ok
二、安装前的准备
（1）安装mysql之前需要确保系统中有libaio依赖
1 yum search libaio 
2 yum install libaio 
（2）下载安装包（tar.gz的包，非rpm和yum） 
1 cd /usr/
2 mkdir database
3 cd database
4 wget https://cdn.mysql.com//Downloads/MySQL-5.7/mysql-5.7.22-el7-x86_64.tar.gz
（3）解压
1 tar -zxvf mysql-5.7.22-el7-x86_64.tar.gz 
2 mv mysql-5.7.22-el7-x86_64 mysql5.7 #重命名为mysql5.7 
三、安装
（1）添加用户和组
1 #添加用户组
2 groupadd mysql
3 #添加用户mysql 到用户组mysql(使用-r参数表示mysql用户是一个系统用户，不能登录)
4 useradd -r -g mysql mysql
5 #添加完用下面命令测试,能看到mysql用户的信息
6 id mysql
（2）手动创建MySQL data目录
1 cd /usr/database/mysql5.7/
2 mkdir data
（3）目录权限设置
1 将mysql及其下所有的目录所有者和组均设为mysql
2 chown -R mysql:mysql /usr/database/mysql5.7/
3 查看是否设置成功，执行下面命令，可以看到文件的所有者和组都变成了mysql
4 cd /usr/database/
5 ll
6 结果中出现：drwxr-xr-x 10 mysql mysql      4096 Jul  3 13:57 mysql5.7
 
（4）配置my.cnf文件
此文件非常重要，初始化之前要把此文件放到 /etc 目录下 
1 rm -rf /etc/my.cnf
2 vim /etc/my.cnf
3 #此文件内容如下(路径根据自己的实际情况):
4 [client]
5 port = 3306
6 socket = /tmp/mysql.sock
7
8 [mysqld]
9 init-connect='SET NAMES utf8'
10 basedir=/usr/database/mysql5.7       #根据自己的安装目录填写 
11 datadir=/usr/database/mysql5.7/data #根据自己的mysql数据目录填写
12 socket=/tmp/mysql.sock
13 max_connections=200 # 允许最大连接数
14 character-set-server=utf8 # 服务端使用的字符集默认为8比特编码的latin1字符集
15 default-storage-engine=INNODB # 创建新表时将使用的默认存储引擎
（5）初始化mysql
1 /usr/database/mysql5.7/bin/mysqld --initialize-insecure --user=mysql  --basedir=/usr/database/mysql5.7 --datadir=/usr/database/mysql5.7/data
2 #注意：mysqld --initialize-insecure初始化后的mysql是没有密码的
3 #重新修改下各个目录的权限
4 chown -R root:root /usr/database/mysql5.7/ #把安装目录的目录的权限所有者改为root
5 chown -R mysql:mysql /usr/database/mysql5.7/data/ #把data目录的权限所有者改为mysql
（6）启动mysql
1 /usr/database/mysql5.7/bin/mysqld_safe --user=mysql &
（7）修改密码
1 cd /usr/database/mysql5.7/bin/
2 ./mysql -u root -p # 默认没有密码,直接敲回车就可以
3 use mysql;
4 update user set authentication_string=password('这里填你设置的密码') where user='root';
5 flush privileges;
6 exit;
（8）测试登录
1 cd /usr/database/mysql5.7/bin/
2 ./mysql mysql -u root -p
3 输入密码后，应该就连接上了
4 show databases;
5 exit; #退出
（9）copy启动脚本并将其添加到服务且设置为开机启动
1 #mysql启动脚本为：/usr/database/mysql5.7/support-files/mysql.server
2 cp /usr/database/mysql5.7/support-files/mysql.server /etc/init.d/mysql
3 #添加服务
4 chkconfig --add mysql   
5 # 显示服务列表
6 chkconfig --list    
7 # 开机启动
8 chkconfig --level 345 mysql on
9 # 测试添加的服务是否能用
10 service mysql status #查看状态
11 service mysql start  #启动mysql服务
12 service mysql stop   #停止mysql服务
四、设置外网可以访问
1 在mysql的bin目录下执行：mysql -uroot -p密码 登陆到数据：
2 执行：use mysql;
3 执行：select host,user from user;
4 可以看到user为root，host为localhost的话，说明mysql只允许本机连接，那么外网，本地软件客户端就无法连接了。
5 调整方法：
6 执行：update user set host='%' where user ='root';
7 执行刷新:flush privileges;
8 OK!现在可以访问了！
9 如果还访问不了，那可能是防火墙问题,修改下防火墙就ok，修改方法这里就不提了，网上很多资料
五、相关说明
（1）mysql服务的启动和停止命令
1 service mysql status #查看状态
2 service mysql start  #启动mysql服务
3 service mysql stop   #停止mysql服务 
（2）怎么在Linux中登录mysql
1 #进入mysql安装目录的bin目录，然后输入以下命令
2 ./mysql -u root -p
3 #然后输入密码就登录成功
4 exit; #退出mysql
```

## 安装MariaDB

```sh
rpm -ivh xxxx.rpm
yum install -y mariadb mariadb-server
systemctl start mariadb
mysql -uroot -p
update user set password=password('123') where user='root';
flush privileges;

#sqlyog远程链接
GRANT ALL PRIVILEGES ON *.* TO 'user'@'%' IDENTIFIED BY 'password' WITH GRANT OPTION;   # %:表示从任何主机连接到mysql服务器
flush privileges;
```



# mysql优化

## 初步优化

```sql
#基本优化
select user,host from mysql.user;
delete from mysql.user where user='';
delete from mysql.user where host='www';
delete from mysql.user where host='::1';
drop database test;
show databases;

#或者额外授权管理员
grant all privileges on *.* to system@'localhost' identified by 'oldboy' with grant option;

quit

#linux下
/application/mysql//bin/mysqladmin -u root password 'new-password'
```



# 修改mysql时区

from:  https://blog.csdn.net/qq_41070393/article/details/105731300

```sql
# 查看当前时间
SELECT now();
# 查看时区信息
show variables like "%time_zone%";
# 修改mysql全局时区为北京时间
set global time_zone = '+8:00';
# 修改当前会话时区
set time_zone = '+8:00';
# 立即生效
flush privileges; 
```

# 字符集乱码问题

```sql
from: https://www.cnblogs.com/yanzi-meng/p/9184139.html

set names utf8 是用于设置编码，可以再在建数据库的时候设置，也可以在创建表的时候设置，或只是对部分字段进行设置，而且在设置编码的时候，这些地方最好是一致的，这样能最大程度上避免数据记录出现乱码。

执行SET NAMES utf8的效果等同于同时设定如下：
SET character_set_client='utf8';
SET character_set_connection='utf8';
SET character_set_results='utf8';

另外，如果数据出现乱码可以试着用以下办法解决：
一、避免创建数据库及表出现中文乱码和查看编码方法
1、创建数据库的时候：CREATE DATABASE `test`
CHARACTER SET 'utf8'
COLLATE 'utf8_general_ci';
2、建表的时候 CREATE TABLE `database_user` (
`ID` varchar(40) NOT NULL default '',
`UserID` varchar(40) NOT NULL default '',
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

这3个设置好了，基本就不会出问题了,即建库和建表时都使用相同的编码格式。
如果是已经建了库和表可以通过以下方式进行查询。
1.查看默认的编码格式:
mysql> show variables like "%char%";
+--------------------------+---------------+
| Variable_name | Value |
+--------------------------+---------------+
| character_set_client | gbk |
| character_set_connection | gbk |
| character_set_database | utf8 |
| character_set_filesystem | binary |
| character_set_results | gbk |
| character_set_server | utf8 |
| character_set_system | utf8 |
+--------------------------+-------------+
注：以前2个来确定,可以使用set names utf8,set names gbk设置默认的编码格式;
```

# insert后返回主键

```sql
from: https://www.cnblogs.com/panxuejun/p/6180506.html
mysql insert一条记录后怎样返回创建记录的主键id,last_insert_id(),selectkey	


mysql插入数据后返回自增ID的方法
 
mysql和oracle插入的时候有一个很大的区别是，oracle支持序列做id，mysql本身有一个列可以做自增长字段，mysql在插入一条数据后，如何能获得到这个自增id的值呢？
 
方法一：是使用last_insert_id
mysql> SELECT LAST_INSERT_ID();  #  重要
    产生的ID 每次连接后保存在服务器中。这意味着函数向一个给定客户端返回的值是该客户端产生对影响AUTO_INCREMENT列的最新语句第一个 AUTO_INCREMENT值的。这个值不能被其它客户端影响，即使它们产生它们自己的 AUTO_INCREMENT值。这个行为保证了你能够找回自己的 ID 而不用担心其它客户端的活动，而且不需要加锁或处理。 
 
    每次mysql_query操作在mysql服务器上可以理解为一次“原子”操作, 写操作常常需要锁表的， 是mysql应用服务器锁表不是我们的应用程序锁表。
 
    值得注意的是，如果你一次插入了多条记录，这个函数返回的是第一个记录的ID值。
    因为LAST_INSERT_ID是基于Connection的，只要每个线程都使用独立的Connection对象，LAST_INSERT_ID函数 将返回该Connection对AUTO_INCREMENT列最新的insert or update*作生成的第一个record的ID。这个值不能被其它客户端（Connection）影响，保证了你能够找回自己的 ID 而不用担心其它客户端的活动，而且不需要加锁。使用单INSERT语句插入多条记录,  LAST_INSERT_ID返回一个列表。
    LAST_INSERT_ID 是与table无关的，如果向表a插入数据后，再向表b插入数据，LAST_INSERT_ID会改变。
 
方法二：是使用max(id)
 
使用last_insert_id是基础连接的，如果换一个窗口的时候调用则会一直返回10
如果不是频繁的插入我们也可以使用这种方法来获取返回的id值
select max(id) from user;
这个方法的缺点是不适合高并发。如果同时插入的时候返回的值可能不准确。
 
方法三：是创建一个存储过程，在存储过程中调用先插入再获取最大值的操作
￼
￼
DELIMITER $$
DROP PROCEDURE IF EXISTS `test` $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `test`(in name varchar(100),out oid int)
BEGIN
  insert into user(loginname) values(name);
  select max(id) from user into oid;
  select oid;
END $$
DELIMITER ;
call test('gg',@id);
￼
￼
方法四:使用@@identity
select @@IDENTITY
    @@identity是表示的是最近一次向具有identity属性(即自增列)的表插入数据时对应的自增列的值，是系统定 义的全局变量。一般系统定义的全局变量都是以@@开头，用户自定义变量以@开头。比如有个表A，它的自增列是id，当向A表插入一行数据后，如果插入数据 后自增列的值自动增加至101，则通过select @@identity得到的值就是101。使用@@identity的前提是在进行insert操作后，执行select @@identity的时候连接没有关闭，否则得到的将是NULL值。
方法五:是使用getGeneratedKeys()
￼
￼
Connection conn = ;
Serializable ret = null;
PreparedStatement state = .;
ResultSet rs=null;
try {
    state.executeUpdate();
    rs = state.getGeneratedKeys();
    if (rs.next()) {
        ret = (Serializable) rs.getObject(1);
    }     
} catch (SQLException e) {
}
return ret;
￼
￼
总结一下，在mysql中做完插入之后获取id在高并发的时候是很容易出错的。另外last_insert_id虽然是基于session的但是不知道为什么没有测试成功。
     
方法6：selectkey:
其实在ibtias框架里使用selectkey这个节点，并设置insert返回值的类型为integer，就可以返回这个id值。
SelectKey在Mybatis中是为了解决Insert数据时不支持主键自动生成的问题，他可以很随意的设置生成主键的方式。
不管SelectKey有多好，尽量不要遇到这种情况吧，毕竟很麻烦。
```

