# 查看历史版本文件

```bash
svn log "https://10.1.8.191/svn/shyycg2_src" -r 6333:6337 -v     #显示历史版本文件
```

# svn导入导出

```bash
svnrdump dump https://10.1.8.191/svn/shyycg_src/YSXT/trunk/ >ysxt.dump

svnrdump dump https://10.1.8.191/svn/shyycg2_src/trunk/ >ysxtqx.dump

svnrdump dump "https://10.1.8.191/svn/shyycgwx_src" >shyycgwx.dump

svnrdump load "D:\SVN_Repositories\TestRespository" <"C:\Program Files\VisualSVN Server\bin\ysxtqx.dump"

svnrdump load "D:\SVN_Repositories\TestRespository" <"C:\Program Files\VisualSVN Server\bin\shyycgwx.dump"

svnrdump load "https://SC-201607051105/svn/TestRepository/" <"C:\Program Files\VisualSVN Server\bin\shyycgwx.dump"

svnrdump load "https://10.1.94.73/svn/医疗保障事业部/上海项目部/上海医药采购/" <"C:\Program Files\VisualSVN Server\bin\ysxtqx.dump"


svnrdump dump -q "https://10.1.8.191/svn/shyycg2_doc/" |  svnrdump load "https://10.1.94.73/svn/医疗保障事业部/上海项目部/上海医药采购/shyycg2_doc/"

svnrdump dump -q "https://10.1.8.191/svn/shyycgwx_src" |  svnrdump load "https://10.1.94.73/svn/医疗保障事业部/上海项目部/上海医药采购/shyycgwx_src/"

svn switch --relocate "https://10.1.8.191/svn/shyycg2_doc/" "https://10.1.94.73/svn/医疗保障事业部/上海项目部/上海医药采购/shyycg2_doc/"

svnadmin dump "https://10.1.8.191/svn/shyycg2_doc/" > "https://10.1.94.73/svn/医疗保障事业部/上海项目部/上海医药采购/shyycg2_doc/"

svn propdel svn:sync-lock --revprop -r 0 "https://SC-201607051105/svn/TestRepository/"

svn propdel svn:sync-lock --revprop -r 0 "https://SC-201607051105/svn/TestRepository/"
svn propdel svn:sync-lock --revprop -r 0 "file:///D:\SVN_Repositories\TestRespository"
svn propdel svn:sync-lock --revprop -r 0 "http://localhost/svn/医疗保障事业部/上海项目部/上海医药采购/"

svnrdump dump -r 1:2000 "https://10.1.8.191/svn/shyycg_src" >ysxt1.dump

svnrdump dump  "https://10.1.8.191/svn/shyycg2_src" >ysxtqx.dump

svnrdump dump  "https://10.1.8.191/svn/shyycg2_doc" >ysxtqx_doc.dump

svn log "https://10.1.8.191/svn/shyycg_src"

svn propdel svn:sync-lock --revprop -r 0 file:///var/svn/project/


svn log "https://10.1.94.73/svn/医疗保障事业部/上海项目部/上海医药采购/shyycg_src/YSXT/trunk/ysxt/src/local/com/wondersgroup/yss/service/DaxxwhService.java"
```

# svn常用命令

```bash
from:https://blog.csdn.net/atsoar/article/details/80460421

首先推荐大家一个应该是国内外最好的SVN仓库，不限私有，不限成员：http://svnbucket.com

# checkout代码
svn co svn://svnbucket.com/xxx/xxx
# 更新代码
svn up
# 添加新文件到版本库
svn add filename
 
# 添加当前目录下所有php文件
svn add *.php
 
# 递归添加当前目录下的所有新文件
svn add . --no-ignore --force
# 提交代码
svn commit -m "提交描述"
# 查看指定文件的所有log
svn log test.php
 
# 查看指定版本号的log
svn log -r 100
# 撤销本地文件的修改（还没提交的）
svn revert test.php
svn revert -r 目录名
 
# 撤销目录下所有本地修改
svn revert --recursive 目录名
# 查看当前工作区的所有改动
svn diff
 
# 查看当前工作区test.php文件与最新版本的差异
svn diff test.php  
 
# 指定版本号比较差异
svn diff -r 200:201 test.php
 
# 查看当前工作区和版本301中bin目录的差异
svn diff -r 301 bin
# 查看当前工作区的状态
svn status
# 查看svn信息
svn info
# 查看文件列表，可以指定-r查看，查看指定版本号的文件列表
svn ls 
svn ls -r 100
# 显示文件的每一行最后是谁修改的（出了BUG，经常用来查这段代码是谁改的）
svn blame filename.php
# 查看指定版本的文件内容，不加版本号就是查看最新版本的
svn cat test.py -r 2
# 清理
svn cleanup
# 若想创建了一个文件夹，并且把它加入版本控制，但忽略文件夹中的所有文件的内容
$ svn mkdir spool 
$ svn propset svn:ignore '*' spool 
$ svn ci -m 'Adding "spool" and ignoring its contents.'
# 若想创建一个文件夹，但不加入版本控制，即忽略这个文件夹
$ mkdir spool 
$ svn propset svn:ignore 'spool' . 
$ svn ci -m 'Ignoring a directory called "spool".'
# 切换当前项目到指定分支。服务器上更新新版本我们经常就用这个命令来把当前代码切换到新的分支
svn switch svn://svnbucket.com/test/branches/online1.0
 
# 重定向仓库地址到新地址
svn switch --relocate 原svn地址 新svn地址
# 创建分支，从主干创建一个分支保存到branches/online1.0
svn cp -m "描述内容" http://svnbucket.com/repos/trunk http://svnbucket.com/repos/branches/online1.0
 
# 合并主干上的最新代码到分支上
cd branches/online1.0
svn merge http://svnbucket.com/repos/trunk 
 
# 分支合并到主干
svn merge --reintegrate http://svnbucket.com/repos/branches/online1.0
 
# 删除分支
svn rm http://svnbucket.com/repos/branches/online1.0
# 查看SVN帮助
svn help
 
# 查看指定命令的帮助信息
svn help commit
```

# svnsync报错Failed to get lock on destination repos解决办法

```
FROM:https://blog.csdn.net/b421001644/article/details/79641888

svn propdel svn:sync-lock --revprop -r 0 https://SC-201607051105/svn/TestRepository/
在使用svnsync备份svn的时候遇见报错

Failed to get lock on destination repos, currently held by 'localhost.localdomain:d18d89e9-a247-4402-8e69-721d0dfca60c'

使用svn propdel svn:sync-lock --revprop -r 0 http://ip:port/svn/project 解决
```

# SVN Repository has not been enabled to accept revision propchanges

from:https://blog.csdn.net/flying881114/article/details/7388695

1. Repository has not been enabled to accept revision propchanges

   解决方法：
   Linux:
   在hooks下新建pre-revprop-change文件，内容是

```bash
#!/bin/sh
exit 0;
```
Windows:
在hooks下新建pre-revprop-change.bat文件， 内容为空即可

2. Revprop change blocked by pre-revprop-change hook (exit code 255) with no output
解决方法：
Linux:
```bash
chmod a+x pre-revprop-change
```

