# 文件操作

## touch

用于修改文件或者目录的时间属性，包括存取时间和更改时间。若文件不存在，系统会建立一个新的文件。

###   参数

```sh
# -a 只改变访问时间
# -m 只改变修改时间
# -c 没有文件时，不创建
```

## cp

## mv

## rm 

## ln

## stat

（display file or file system status）查看文件具体信息

## file

（determine file type）辨识文件类型

# 文件夹操作

## ls

显示目录内容

### 参数

```sh
# -l  显示详细信息
# -a  显示包括隐藏文件的所有内容
# -t  将文件依建立时间之先后次序列出
# -h  以G，M，K查看文件大小
# -r  将文件以相反次序显示(原定依英文字母次序)
# -R  递归查看
```

### 生产环境实例

```sh
ll -rt  # 根据时间倒序排列，能看到最新生成的文件
```

## tree

以树状图列出目录的内容

## 参数

```sh
# -L  level 限制目录显示层级。
# -d  显示目录而不是文件
```

### 生产环境实例

```sh
tree -Ld 1  # 显示当前目录下，第1层级的目录
```



## pwd

## cd

### 参数

```sh
# cd ~    跳转到家目录
# cd .    当前目录
# cd ..    到上级目录
# cd -    到上一个目录
```



## mkdir

## du

## rmdir

# 过滤3剑客grep, sed, awk

## grep

### 参数

```sh
# -c  或 --count : 计算符合样式的列数
# -r  或 --recursive : 递归
# -i  忽略大小写的不同，所以大小写视为相同
# -v  反向选择，亦即显示出没有 '搜寻字符串' 内容的那一行！
grep -v ^# /etc/ssh/sshd_config    #去掉已#开头的行
grep -v '^$'  /etc/ssh/sshd_config    #去掉空行
 
# -E  将样式为延伸的正则表达式来使用。 =egrep
grep -vE "crond|sshd|network|rsyslog"   排除4个服务

# -n  打印匹配行号
# -e  匹配多个
grep -e "bin" -e "sh" ans_yn_2.sh
```



### 生产环境实例

```sh
grep -r CREATE  backup/ -l  #现在要查找某个关键字在某文件夹下的文件, 并只返回文件名
grep CREATE -R	# 匹配当前文件夹下的所有的文件
grep -10 'patten'  xxx   #打印匹配的前后10行
	tail -50 /usr/local/mysql/data/sql-slow.log |grep -3 '192.168.0.10'   # 和tail配合使用
```

## sed

## awk