## 修改主键自增值

```sql
SELECT AUTO_INCREMENT  FROM INFORMATION_SCHEMA.`TABLES` WHERE table_name='tb_episode';		#查一下当前值

ALTER TABLE tb_episode AUTO_INCREMENT= 249612;			#修改下一个值

SELECT AUTO_INCREMENT  FROM INFORMATION_SCHEMA.`TABLES` WHERE table_name='tb_episode';	#查询自增值
```

# 报错

## mysql查询出现In aggregated query without GROUP BY, expression #1 of SELECT list contains nonaggregate...

from: https://my.oschina.net/u/4341223/blog/3767910

出现原因：

在MySQL5.7.5后，默认开启了ONLY_FULL_GROUP_BY，所以导致了之前的一些SQL无法正常执行，其实，是我们的SQL不规范造成的，因为group by 之后，返回的一些数据是不确定的，所以才会出现这个错误。

解决方案，两种：

方案一：修改SQL，因为出现这个问题，基本都是因为这个问题造成的，不确定返回字段可以使用ANY_VALUE(column_name)。

方案二：关闭ONLY_FULL_GROUP_BY，我的是Linux环境，我就说一下Linux的解决步骤：

①、登录进入MySQL，linux登录的：mysql -u username -p ，然后输入密码，输入SQL：show variables like '%sql_mode';

编辑my.cnf文件，文件地址一般在：/etc/my.cnf，<font color='red'>/etc/mysql/my.cnf</font>，找到sql-mode的位置，去掉ONLY_FULL_GROUP_BY，然后重启MySQL；有的my.cnf中没有sql-mode，需要加入：

my.cnf

```properties
[mysqld]

sql-mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION
```

sql-mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION，注意要加入到[mysqld]下面，我就是加入到其他地方，重启后也不生效，具体的如下图：

修改成功后重启MySQL服务，service mysql restart，重启好后，再登录mysql，*输入SQL：show variables like '%sql_mode'; 如果没有ONLY_FULL_GROUP_BY，就说明已经成功了。*

<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20211117171647113.png" alt="image-20211117171647113" style="zoom:80%;" />



## 1093, 对一个表的字段查询并修改

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

 

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/04-209941648.png)

 

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

## mysql的win安装 

from: https://www.cnblogs.com/zhangkanghui/p/9613844.html

官网下载win的zip包

**配置环境变量**：

```bat
%MySql_HOME%
```

**生成data文件**

```shell
# 以管理员身份运行cmd
mysqld --initialize-insecure --user=mysql  在E:\mysql\mysql-8.0.12-winx64目录下生成data目录

mysqld -install  # 安装mysql
net start MySQL  # 启动服务
```

**登录MySQL**

```sh
E:\python\mysql\mysql-8.0.12-winx64\bin>mysql -u root -p

```

**查询用户密码**

```sql
mysql> select host,user,authentication_string from mysql.user;
mysql> use mysql;
mysql> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
mysql> flush privileges;  
mysql> quit;

```

**关于修改密码再次登录出现ERROR的解决方案：**

```sh
net stop mysql
打开mysql的安装目录，找到data文件夹，将其删除！
回到cmd命令窗口，输入mysqld -remove
```

```sql
接下来按照上面教程，从第二步生成data文件开始执行，一定要注意修改密码那里：
mysql> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
mysql> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
mysql> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
```



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

##  MySQL 索引优化方案

https://mp.weixin.qq.com/s?__biz=MzIxNTAwNjA4OQ==&mid=2247535635&idx=2&sn=9c21e8322a1a8ccd17541415c1e63e87&chksm=979ce2f5a0eb6be31b9cfad94d33227392d4c72c8bc5773ece0d94bcb9e5ccfb3e8f7516a372&mpshare=1&scene=23&srcid=0306FIPFAh7yOGw5FJlY5HUT&sharer_sharetime=1646572997414&sharer_shareid=81b31165b2e86b730d896e28d5acfa50#rd

### 分页查询优化

```sql
select * from employees limit 90000,5;

#优化成
select * from employees limit 90000,5; 	# 主键不连续，不能优化
```

### 根据非主键字段排序的分页查询

```sql
select * from employees ORDER BY name limit 90000,5; # 没走索引
# 扫描整个索引并查找到没索引的行(可能要遍历多个索引树)的成本比扫描全表的成本更高，所以优化器放弃使用索引。
# 优化成：
select * from employees e inner join (select id from employees order by name limit 90000,5) ed on e.id = ed.id;
```

### Join关联查询优化

```sql
# 创建2个存过
DELIMITER $$

USE `db_java`$$

DROP PROCEDURE IF EXISTS `insert_t1`$$

CREATE DEFINER=`root`@`%` PROCEDURE `insert_t1`()
BEGIN
 DECLARE i INT;
 SET i=1;
 WHILE (i<=10000) DO
 INSERT INTO t1(a,b) VALUES(i,i);
 SET i=i+1;
 END WHILE;
 END$$

DELIMITER ;

```

```sql
DELIMITER $$

USE `db_java`$$

DROP PROCEDURE IF EXISTS `insert_t2`$$

CREATE DEFINER=`root`@`%` PROCEDURE `insert_t2`()
BEGIN
 DECLARE i INT;
 SET i=1;
 WHILE (i<=100) DO
 INSERT INTO t2(a,b) VALUES(i,i);
 SET i=i+1;
 END WHILE;
 END$$

DELIMITER ;
```

**mysql的表关联常见有两种算法**

- Nested-Loop Join 算法
- Block Nested-Loop Join 算法

##### 1、 嵌套循环连接 Nested-Loop Join(NLJ) 算法

一次一行循环地从第一张表（称为驱动表）中读取行，在这行数据中取到关联字段，根据关联字段在另一张表（被驱动表）里取出满足条件的行，然后取出两张表的结果合集。

```sql
EXPLAIN select * from t1 inner join t2 on t1.a= t2.a;  -- // a字段有索引
```

![image-20220309100000039](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20220309100000039.png)

从执行计划中可以看到这些信息：

- 驱动表是 t2，被驱动表是 t1。先执行的就是驱动表(执行计划结果的id如果一样则按从上到下顺序执行sql)；优化器一般会优先选择小表做驱动表。所以使用 `inner join` 时，排在前面的表并不一定就是驱动表。
- 当使用left join时，左表是驱动表，右表是被驱动表，当使用`right join`时，右表是驱动表，左表是被驱动表，当使用join时，mysql会选择数据量比较小的表作为驱动表，大表作为被驱动表。
- 使用了 NLJ算法。一般 join 语句中，如果执行计划 Extra 中未出现 `Using join buffer` 则表示使用的 join 算法是 NLJ。

上面sql的大致流程如下：

- 从表 t2 中读取一行数据（如果t2表有查询过滤条件的，会从过滤结果里取出一行数据）；
- 从第 1 步的数据中，取出关联字段 a，到表 t1 中查找；
- 取出表 t1 中满足条件的行，跟 t2 中获取到的结果合并，作为结果返回给客户端；
- 重复上面 3 步。

整个过程会读取 t2 表的所有数据(扫描100行)，然后遍历这每行数据中字段 a 的值，根据 t2 表中 a 的值索引扫描 t1 表中的对应行(扫描100次 t1 表的索引，1次扫描可以认为最终只扫描 t1 表一行完整数据，也就是总共 t1 表也扫描了100行)。因此整个过程扫描了 200 行。

##### 2、 基于块的嵌套循环连接 Block Nested-Loop Join(BNL)算法

把驱动表的数据读入到 <font color='red'>`join_buffer` </font>中，然后扫描被驱动表，把被驱动表每一行取出来跟 `join_buffer` 中的数据做对比。

```sql
EXPLAIN select * from t1 inner join t2 on t1.b= t2.b; -- // b字段没有索引
```

![345](https://gitee.com/jstone001/booknote/raw/master/jpgBed/640.png)

Extra 中 的`Using join buffer (Block Nested Loop)`说明该关联查询使用的是 BNL 算法。

**上面sql的大致流程如下：**

- 把 t2 的所有数据放入到 `join_buffer` 中
- 把表 t1 中每一行取出来，跟 `join_buffer` 中的数据做对比
- 返回满足 join 条件的数据

整个过程对表 t1 和 t2 都做了一次全表扫描，因此扫描的总行数为10000(表 t1 的数据总量) + 100(表 t2 的数据总量) =10100。并且 `join_buffer` 里的数据是无序的，因此对表 t1 中的每一行，都要做 100 次判断，所以内存中的判断次数是`100 * 10000= 100` 万次。

**这个例子里表 t2 才 100 行，要是表 t2 是一个大表，join_buffer 放不下怎么办呢？**

join_buffer 的大小是由参数 `join_buffer_size` 设定的，默认值是 256k。如果放不下表 t2 的所有数据话，策略很简单，就是分段放。

比如 t2 表有1000行记录， `join_buffer` 一次只能放800行数据，那么执行过程就是先往 `join_buffer` 里放800行记录，然后从 t1 表里取数据跟 `join_buffer` 中数据对比得到部分结果，然后清空 `join_buffer` ，再放入 t2 表剩余200行记录，再次从 t1 表里取数据跟 `join_buffer` 中数据对比。所以就多扫了一次 t1 表。

**被驱动表的关联字段没索引为什么要选择使用 BNL 算法而不使用 `Nested-Loop Join` 呢？**

如果上面第二条sql使用 `Nested-Loop Join`，那么扫描行数为 `100 * 10000 = 100`万次，这个是磁盘扫描。

很显然，用BNL磁盘扫描次数少很多，相比于磁盘扫描，BNL的内存计算会快得多。因此MySQL对于被驱动表的关联字段没索引的关联查询，一般都会使用 BNL 算法。如果有索引一般选择 NLJ 算法，有索引的情况下 NLJ 算法比 BNL算法性能更高.

### 对于关联sql的优化

- 关联字段加索引，让mysql做join操作时尽量选择NLJ算法
- 小表驱动大表，写多表连接sql时如果明确知道哪张表是小表可以用`straight_join`写法固定连接驱动方式，省去mysql优化器自己判断的时间

`straight_join`解释：`straight_join`功能同join类似，但能让左边的表来驱动右边的表，能改表优化器对于联表查询的执行顺序。

比如：`select * from t2 straight_join t1 on t2.a = t1.a; `代表指定mysql选着 t2 表作为驱动表。

- `straight_join`只适用于`inner join`，并不适用于`left join`，right join。（因为`left join`，`right join`已经代表指定了表的执行顺序）
- 尽可能让优化器去判断，因为大部分情况下mysql优化器是比人要聪明的。使用`straight_join`一定要慎重，因为部分情况下人为指定的执行顺序并不一定会比优化引擎要靠谱。

**对于小表定义的明确：**

在决定哪个表做驱动表的时候，应该是两个表按照各自的条件过滤，过滤完成之后，计算参与 join 的各个字段的总数据量，数据量小的那个表，就是“小表”，应该作为驱动表。

### in和exsits优化

**原则：小表驱动大表，即小的数据集驱动大的数据集。**

```sql
# In是In后的表先执行（适用于B表小于A表）：
select * from A where id in ( select id from B)

# Exists是Exists前面的表先执行（适用于A表小于B表）:
select * from A where id in ( select id from B)
```
```sql
# in：当B表的数据集小于A表的数据集时，in优于exist
select * from A where id in (select id from B)

// #等价于：
 for(select id from B){
 select * from A where A.id = B.id
 }
```

```sql
# exists：当A表的数据集小于B表的数据集时，exists优于in
# 将主查询A的数据，放到子查询B中做条件验证，根据验证结果（true或false）来决定主查询的数据是否保留.
select * from A where exists (select 1 from B where B.id = A.id)

 // # 等价于:
 for(select * from A){
 select * from B where B.id = A.id
 }

 // # A表与B表的ID字段应建立索引
```

总结：

1、`EXISTS (subquery)`只返回TRUE或FALSE,因此子查询中的`SELECT * `也可以用SELECT 1替换,官方说法是实际执行时会忽略SELECT清单,因此没有区别

2、`EXISTS`子查询的实际执行过程可能经过了优化而不是我们理解上的逐条对比

3、`EXISTS`子查询往往也可以用JOIN来代替，何种最优需要具体问题具体分析

### count(*)查询优化

```sql
 -- 临时关闭mysql查询缓存，为了查看sql多次执行的真实时间
 set global query_cache_size=0;
 set global query_cache_type=0;

 EXPLAIN select count(1) from employees;
 EXPLAIN select count(id) from employees;
 EXPLAIN select count(name) from employees;
 EXPLAIN select count(*) from employees;
 -- 注意：以上4条sql只有根据某个字段count不会统计字段
```

![image-20220309100027195](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20220309100027195.png)

经过测试发现：四个sql的执行计划一样，说明这四个sql执行效率应该差不多

**1、字段有索引：** `count(*)`≈`count(1)`>`count(字段)`>`count(主键 id)`

字段有索引，`count(字段)`统计走二级索引，二级索引存储数据比主键索引少，所以`count(字段)`>`count(主键 id)`

**2、字段无索引:** `count(*)`≈`count(1)`>`count(主键 id)`>`count(字段)`

字段没有索引`count(字段)`统计走不了索引，count(主键 id)还可以走主键索引，所以`count(主键 id)`>`count(字段)`

`count(1)` 跟 `count(字段)` 执行过程类似，不过`count(1)`不需要取出字段统计，就用常量1做统计，`count(字段)`还需要取出字段，所以理论上`count(1)`比`count(字段)`会快一点。

`count(*)` 是例外，mysql并不会把全部字段取出来，而是专门做了优化(5.7版本)，不取值，按行累加，效率很高，所以不需要用`count(列名)`或`count(常量)`来替代 `count(*)`。

为什么对于`count(id)`，mysql最终选择辅助索引而不是主键聚集索引？因为二级索引相对主键索引存储数据更少，检索性能应该更高，mysql内部做了点优化(应该是在5.7版本才优化)。

**常见优化方法**

当表中数据量非常大的时候，count这种通过计算统计的都会很慢，所以需要一些优化手段。

#### 1、查询mysql自己维护的总行数

对于myisam存储引擎的表做不带where条件的count查询性能是很高的，因为myisam存储引擎的表的总行数会被mysql存储在磁盘上，查询不需要计算.

![image-20220309100133752](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20220309100133752.png)

对于innodb存储引擎的表mysql不会存储表的总记录行数(因为有MVCC机制，后面会讲)，查询count需要实时计算.

#### 2、show table status

如果只需要知道表总行数的估计值可以用如下sql查询，性能很高

<img src="https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20220309100156129.png" alt="image-20220309100156129" style="zoom:80%;" />

#### 3、将总数维护到Redis里

插入或删除表数据行的时候同时维护redis里的表总行数key的计数值(用incr或decr命令)，但是这种方式可能不准，很难保证表操作和redis操作的事务一致性.

#### 4、增加数据库计数表

插入或删除表数据行的时候同时维护计数表，让他们在同一个事务里操作

# MySQL中interactive_timeout和wait_timeout的区别

from:　https://www.cnblogs.com/ivictor/p/5979731.html

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

# 字符集

##  更改MySQL数据库的编码为utf8mb4

from:https://www.cnblogs.com/shihaiming/p/5855616.html

utf-8编码可能2个字节、3个字节、4个字节的字符，但是[MySQL](http://lib.csdn.net/base/14)的utf8编码只支持3字节的数据，而移动端的表情数据是4个字节的字符。如果直接往采用utf-8编码的[数据库](http://lib.csdn.net/base/14)中插入表情数据，[Java](http://lib.csdn.net/base/17)程序中将报SQL异常：

> java.sql.SQLException: **Incorrect string value**: ‘\xF0\x9F\x92\x94’ for column ‘name’ at row 1
> at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:1073) 
> at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:3593) 
> at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:3525) 
> at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:1986) 
> at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:2140) 
> at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2620) 
> at com.mysql.jdbc.StatementImpl.executeUpdate(StatementImpl.java:1662) 
> at com.mysql.jdbc.StatementImpl.executeUpdate(StatementImpl.java:1581)

可以对4字节的字符进行编码存储，然后取出来的时候，再进行解码。但是这样做会使得任何使用该字符的地方都要进行编码与解码。

utf8mb4编码是utf8编码的超集，兼容utf8，并且能存储4字节的表情字符。 
采用utf8mb4编码的好处是：存储与获取数据的时候，不用再考虑表情字符的编码与解码问题。

**更改数据库的编码为utf8mb4:**

**1. MySQL的版本**

utf8mb4的最低mysql版本支持版本为5.5.3+，若不是，请升级到较新版本。

**2. MySQL驱动**

5.1.34可用,最低不能低于5.1.13

**3.修改MySQL配置文件**

```properties
# 修改mysql配置文件my.cnf（windows为my.ini） 
# my.cnf一般在etc/mysql/my.cnf位置。找到后请在以下三部分里添加如下内容： 
[client] 
default-character-set = utf8mb4 
[mysql] 
default-character-set = utf8mb4 
[mysqld] 
character-set-client-handshake = FALSE 
character-set-server = utf8mb4 
collation-server = utf8mb4_unicode_ci 
init_connect='SET NAMES utf8mb4'
```

**4. 重启数据库，检查变量**

```sql
SHOW VARIABLES WHERE Variable_name LIKE 'character_set_%' OR Variable_name LIKE 'collation%';
```

| Variable_name            | Value              |
| ------------------------ | ------------------ |
| character_set_client     | utf8mb4            |
| character_set_connection | utf8mb4            |
| character_set_database   | utf8mb4            |
| character_set_filesystem | binary             |
| character_set_results    | utf8mb4            |
| character_set_server     | utf8mb4            |
| character_set_system     | utf8               |
| collation_connection     | utf8mb4_unicode_ci |
| collation_database       | utf8mb4_unicode_ci |
| collation_server         | utf8mb4_unicode_ci |

collation_connection 、collation_database 、collation_server是什么没关系。

但必须保证

| 系统变量                 | 描述                         |
| ------------------------ | ---------------------------- |
| character_set_client     | (客户端来源数据使用的字符集) |
| character_set_connection | (连接层字符集)               |
| character_set_database   | (当前选中数据库的默认字符集) |
| character_set_results    | (查询结果字符集)             |
| character_set_server     | (默认的内部操作字符集)       |

这几个变量必须是utf8mb4。

**5. 数据库连接的配置**

数据库连接参数中: 
characterEncoding=utf8会被自动识别为utf8mb4，也可以不加这个参数，会自动检测。 
而autoReconnect=true是必须加上的。

**6. 将数据库和已经建好的表也转换成utf8mb4**

更改数据库编码：ALTER DATABASE caitu99 CHARACTER SET `utf8mb4` COLLATE `utf8mb4_general_ci`;

更改表编码：ALTER TABLE `TABLE_NAME` CONVERT TO CHARACTER SET `utf8mb4` COLLATE`utf8mb4_general_ci`; 
如有必要，还可以更改列的编码

## 表字符集乱码问题

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
#方法四:使用@@identity
select @@IDENTITY
    @@identity是表示的是最近一次向具有identity属性(即自增列)的表插入数据时对应的自增列的值，是系统定 义的全局变量。一般系统定义的全局变量都是以@@开头，用户自定义变量以@开头。比如有个表A，它的自增列是id，当向A表插入一行数据后，如果插入数据 后自增列的值自动增加至101，则通过select @@identity得到的值就是101。使用@@identity的前提是在进行insert操作后，执行select @@identity的时候连接没有关闭，否则得到的将是NULL值。
    
# 方法五:是使用getGeneratedKeys()
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

# Docker 部署 MySql 并修改为大小写不敏感

编辑/etc/mysql/mysql.conf.d/mysqld.cnf 文件

```bash
#[mysqld]后添加 
lower_case_table_names=1
```

重启应用

```bash
#容器中执行
service mysql restart

#或者退出容器直接重启mysql容器
docker restart mysql
```

# 生产环境启动mysql

```sh
docker run --name mysql1 --restart=always  -p 3306:3306  -v /etc/mysql/conf.d/:/home/mysql/conf.d/ -v /var/log:/home/mysql/log/  -e MYSQL_ROOT_PASSWORD=123456 -e TZ=Asia/Shanghai -d 938b57d64674 --lower-case-table-names=1
```

