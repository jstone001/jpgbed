from: https://www.bilibili.com/video/BV1R7411F7JV

# Python基础

## 简介及安装

### P1千锋Python教程：1.1.1-Python简介
### P2千锋Python教程：1.1.2-Python语言的特点
### P3千锋Python教程：1.1.3-Python环境安装与配置
### P4千锋Python教程：1.1.4-Python的包管理工具pip
### P5千锋Python教程：1.1.5-Python安装问题总结
### P6千锋Python教程：1.1.6-第一个Python程序
### P7千锋Python教程：1.1.7-Python程序中的命名规则
### P8千锋Python教程：1.1.8-变量的概念
### P9千锋Python教程：1.1.9-变量命名规则
### P10千锋Python教程：1.2.0-知识点回顾
### P11千锋Python教程：1.2.1-print的使用
### P12千锋Python教程：1.2.2-转义字符
### P13千锋Python教程：1.2.3-字符串和字面常量
### P14千锋Python教程：1.2.4-格式化输出-1
### P15千锋Python教程：1.2.4-格式化输出-2
### P16千锋Python教程：1.2.4-格式化输出-3
### P17千锋Python教程：1.2.5-input的使用
### P18千锋Python教程：1.2.6-练习
### P19千锋Python教程：1.2.7-赋值运算符及内存分析
### P20千锋Python教程：1.2.8-算术运算符
### P21千锋Python教程：1.3.0-知识点回顾
### P22千锋Python教程：1.3.1-关系运算符
### P23千锋Python教程：1.3.2-逻辑运算符
### P24千锋Python教程：1.3.3-进制转换
### P25千锋Python教程：1.3.4-位运算符-1
### P26千锋Python教程：1.3.4-位运算符-2
### P27千锋Python教程：1.3.5-三元运算符和运算符优先级

## 条件语句

## 循环语句

## 字符串

### P41千锋Python教程：1.5.1-字符串运算符
### P42千锋Python教程：1.5.2-字符串逆序和练习
### P43千锋Python教程：1.5.3-字符串内置方法之大小写
### P44千锋Python教程：1.5.4-字符串内置方法之查找
### P45千锋Python教程：1.5.5-字符串内置方法之编码和判断开头结尾
### P46千锋Python教程：1.5.6-字符串内置方法之判断是否是数字
### P47千锋Python教程：1.5.7-字符串内置方法之合并拆分
### P48千锋Python教程：1.6.0-作业-1
### P49千锋Python教程：1.6.0-作业-2
### P50千锋Python教程：1.6.0-作业-3

## 类和对象

### P135千锋Python教程：1.14.5-面向对象简介
### P136千锋Python教程：1.14.6-对象属性
### P137千锋Python教程：1.15.0-知识点回顾
### P138千锋Python教程：1.15.1-方法-写在类里面的函数
### P139千锋Python教程：1.15.2-构造器和__init__方法

  <font color='red'>方法的self指的是调用对象的内存地址</font>
  创建对象时的方法

### P140千锋Python教程：1.15.3-对象方法

\_\_init\_\_  传参

### P141千锋Python教程：1.15.4-类方法

```python
类方法特点：
1. 定义需要依赖装饰器
2. 类方法中参数不是一个对象，而是类
 		print(cls)      #<class '__main__.Phone'>
3. 类方法中只可以有类属性
4. 类方法中不能调用对象方法
 
作用：
在创建对象前，完成一些功能
 
    @classmethod    
    def test(cls):      # 对象也能调类方法
        print(cls)      #<class '__main__.Phone'>
```

### P142千锋Python教程：1.15.5-静态方法

\_\_age   # 类的私有属性用\_\_

```python
特点：
1. 需要装饰器@staticmethod
2. 静态方法无需传递参数(cls,self)
3. 也只能访问类属性和类方法
4. 加载时机同类方法

    @staticmethod  
    #静态方法
    def test():
    	print("静态方法")
```

### P143千锋Python教程：1.15.6-面向对象编程案例-1
### P144千锋Python教程：1.15.6-面向对象编程案例-2

### P145千锋Python教程：1.15.7-魔术方法-1
```python
__init__  #构造方法
__new__
```
### P146千锋Python教程：1.15.7-魔术方法-2
```python
__call__
__del__   #析构
    del p2  
```
### P147千锋Python教程：1.15.7-魔术方法-3
    __str__

### P148千锋Python教程：1.16.1-私有化

```python
class Student:
    def __init__(self, name, age):
        self.__age = 59  # 私有化成员

    def setAge(self, age):
        self.__age = age

    def getAge(self):
        return self.__age
```

### P149千锋Python教程：1.16.2-@property装饰器

getter/setter

```python
    @property
    def money(self):
        return self.__money
 
    @money.setter
    def money(self, value):
        if isinstance(value, int):
            self.__money = value
        else:
            print("error：输入类型与预设类型不一致")
```

### P150千锋Python教程：1.16.3-关联关系-1
```python
has a    #类调用其他对象

is a     # 类继承

class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age


class Student(Person):
    def __init__(self, name, age, class1):
        super().__init__(name, age)
        self.class1 = class1


class Employee(Person):
    def __init__(self, name, age, employer, salary):
        # 如何调用父类__init__
        super().__init__(name, age)
        self.employer = employer
        self.salary = salary
```
### P151千锋Python教程：1.16.3-关联关系-2
### P152千锋Python教程：1.16.4-继承关系-1
### P153千锋Python教程：1.16.4-继承关系-2
### P154千锋Python教程：1.16.4-继承关系-3
### P155千锋Python教程：1.16.4-继承练习-4

### P156千锋Python教程：1.16.5-多重继承与MRO-9

```python
class A:
    def test(self):
        print("----AAAA")


class B:
    def test1(self):
        print("----BBBB")


class C(A, B):
    pass

c=C()
c.test()
```

- getmro(cls)   =C.\_\_mro\_\_

```python
class A:
    def test(self):
        print("----AAAA")


class B:
    def test1(self):
        print("----BBBB")


class C(A, B):
    pass


import inspect

# 显示继承关系
print(inspect.getmro(C))  # (<class '__main__.C'>, <class '__main__.A'>, <class '__main__.B'>, <class 'object'>)

```

- 经典类和新式类

  ```
  经典类    深度优先        python2
  新式类    广度优先        python3
  ```

### P157千锋Python教程：1.16.6-多态
不同的类，调相同的方法

### P158千锋Python教程：1.17.0-知识点回顾

### P159千锋Python教程：1.17.1-单例模式

#### 单例模式

```python
class Single:
    # 私有化
    __instance = None

    def __new__(cls):

        if cls.__instance is None:
            print("------------1")
            cls.__instance = object.__new__(cls)
            return cls.__instance
        else:
            print("------------2")
            return cls.__instance

s=Single()
s1=Single()

print(s)
print(s1)
```

#### 工厂模式

## 模块

### P160千锋Python教程：1.17.2-模块的导入和使用-1
### P161千锋Python教程：1.17.2-模块的导入和使用-2
### P162千锋Python教程：1.17.3-包的导入-1
### P163千锋Python教程：1.17.3-包的导入-2
### P164千锋Python教程：1.17.4-包的__init__文件
### P165千锋Python教程：1.17.5-模块的循环导入问题
### P166千锋Python教程：1.17.6-sys模块
### P167千锋Python教程：1.18.0-作业评讲
### P168千锋Python教程：1.18.1-time模块
### P169千锋Python教程：1.18.2-datetime模块
### P170千锋Python教程：1.18.3-random模块
### P171千锋Python教程：1.18.4-hashlib模块

### P172千锋Python教程：1.18.5-第三方模块

```python
pip install requests

setting->Project Interpreter ->"+"
```

## 正则

总结

```python
re的方法：
re.match(pattern,str)			#开头匹配，只匹配1次
re.search(pattern,str)			#只匹配1次
re.findall(pattern,str)
re.sub(pattern,'new content',str)	#正则的替换
re.split(pattern,str)	#正则的切割
r      表示已经转义，前面不加r的话，要写成\\b

基础：
.		#匹配\n的任意字符
^       #开头
$       #结尾，匹配到结尾
\w     	#等同于[a-zA-Z0-9_]     word
\d     	#等同于0-9              digit
\s     	#匹配空白字符		space
\S		#not space
|		#或
[]      #单个字母范围	[abc]		a或b或c
()		#1、整个单词	(word1|word2|word3)	 word1或word2或word3 2、表示组
\b      #单词边界
\B      #非单词边界

量词：
*       #>=0
+       #>=0
?       #0 or 1
{m}     #固定m
{m,}    #>=m
{m,n}   #m个到n个

分组：
	group()	必须匹配，不然会报错

	1、number
		(\w+)(\d*)	->group(1)group(2)
		引用：
		(\1)(\2)
	2、起名
		(?P<name1>\w+)  (?P=name1)	 引用
```



### P173千锋Python教程：1.18.6-正则表达式介绍

```python
import re

msg='aabbccddee'

result1=re.match('bb',msg)      #match都是从头匹配
print(result1)                  # none

result2=re.search('bb',msg)     #search全文查找匹配
print(result2)                  # <re.Match object; span=(2, 4), match='bb'>
print(type(result2))            # <class 're.Match'>
print(result2.span())           #(2, 4)
print(result2.span()[0])        #2
print(result2.group())          #bb 匹配的数据
```



### P175千锋Python教程：1.18.8-正则表达式相关函数-1（视频顺序反了）

```python
import re

msg='a7aopa88akigka7878a'

# 匹配2边是1个字母，中间是数字
result=re.findall('[a-z][0-9]+[a-z]',msg)   #findall找多个
print(result)       #['a7a', 'a88a', 'a7878a']

# qq号验证，第1位不为0，长度为5~10的数字
qq='1494468992'
result1=re.search('^[1-9][0-9]{4,9}',qq)
result2=re.match('[1-9][0-9]{4,9}',qq)
print(result1)
print(result2)

# 用户名可以是字母和数字，不能是数字开头，用户名长度必须6位以上
username='admin001#*'
result=re.match('[a-zA-Z][0-9a-zA-Z]{5,}$',username)    # $匹配到结尾
print(result)       #None

# 匹配py文件
msg='aa*py ab.txt bb.py kk.png uu.py'
result=re.findall(r'\w\.py\b',msg)      #r 表示已经转义，前面不加r的话，要写成\\b
print(result)       #['b.py', 'u.py']

#手机号验证
phone='12398120332'
re.match('1[135789]\d{9}$',phone)       #别忘了$
```



### P174千锋Python教程：1.18.7-正则表达式的使用和分组操作（或者|）

```python
import re

# 匹配1~100
s = '9'

result = re.match(r'[1-9]?\d?$|100$', s)
print(result)

# 只验证163 126 qq的邮箱
mail = 'frr21123@163.com'
result = re.match(r'\w*@(163|126|qq)\.(com|cn)$', mail)  # ()表示整体的或
print(result)
```



### P177千锋Python教程：1.18.8-正则表达式相关函数-3（分组匹配）

```python

# -*- coding:utf-8 -*-
import re

#不是以4、7结尾的手机号码（11位）
phone='15901018866'
result=re.match(r'1[135789]\d{8}[0-35689]$',phone)
print(result)
print(result.group())	#提取匹配结果


#分别提取
phone='010-12345678'
result=re.match(r'(\d{3,4})-(\d{8})',phone)
print(result.group())		#提取整体
print(result.group(1))		#第1个小括号的内容
print(result.group(2))		#第2个小括号的内容

#标签匹配
msg='<html>hello</html>'
msg1='<h1>abc</h1>'
result1=re.match(r'<[0-9a-zA-Z]+>(.+)</[0-9a-zA-Z]+>$',msg)
result2=re.match(r'<[0-9a-zA-Z]+>(.+)</[0-9a-zA-Z]+>$',msg1)
print(result1.group(1))		# 不能是group()
print(result2.group(1))		#abc

#成对匹配 number
msg='<html>hello</html>'
result=re.match(r'<([0-9a-zA-Z]+)>(.+)</\1>$',msg)	#\1是引用
print(result)
print(result.group(1))	#html
print(result.group(2))	#hello

#多标签取值
msg='<html><h1>hello</h1></html>'
result=re.match(r'<([0-9a-zA-Z]+)><([0-9a-zA-Z]+)>(.+)</\2></\1>$',msg)
print(result)
print(result.group(3))	#取到hello
```



### P176千锋Python教程：1.18.8-正则表达式相关函数-2（视频顺序反了）

#### 分组2

```python
# -*- coding:utf-8 -*-
import re

#起名的方式  (?P<名字>正则) （?P=名字)
msg='<html><h1>abc</h1></html>'
result=re.match(r'<(?P<name1>\w+)><(?P<name2>\w+)>(.+)</(?P=name2)></(?P=name1)>',msg)	#
print(result)
print(result.group(1))	#html
print(result.group(3))	#abc

'''
分组：
	不需要引用分组的内容：
		result1=re.match(r'<[0-9a-zA-Z]+>(.+)</[0-9a-zA-Z]+>$',msg)
		print(result)
		print(result.group(1))

	引用分组匹配：
		1、number \number引用第number组的数据
		result=re.match(r'<([0-9a-zA-Z]+)><([0-9a-zA-Z]+)>(.+)</\2></\1>$',msg)
		print(result)

		2、?P<名字>
		result=re.match(r'<(?P<name1>\w+)><(?P<name2>\w+)>(.+)</(?P=name2)></(?P=name1)>',msg)	#
'''
```

#### re.sub

```python
# -*- coding:utf-8 -*-
import re

result=re.sub(r'\d+','90','java:99, python:100')
print(result)	#java:90, python:90


#sub中添加函数
def func(temp):
	num=temp.group()
	return str(int(num)+1)

result=re.sub(r'\d+',func,'java:99, python:100')
print(result)
```

#### re.split

```python
result=re.split(r'[,:]','java:99, python:100')	#遇到,: 分割
print(result)	#['java', '99', ' python', '100']
```

### P178千锋Python教程：1.19.1-贪婪匹配和惰性匹配

```python
# -*- coding:utf-8 -*-
import re

# 有量词默认是贪婪的
msg='abc123abc'
result=re.match('abc\d+',msg)
print(result)

#非贪婪，尽可能匹配少的字符
result=re.match('abc\d+?',msg)
print(result)		#abc1
```

```python
# 爬虫图片
import requests
image_path='http://tiebapic.baidu.com/forum/pic/item/7bec54e736d12f2ea3cac92158c2d562843568a7.jpg'
response=requests.get(image_path)


with open('aa.jpg','wb') as wstream:
	wstream.write(response.content)
```



## 进程

# 阿里云配置

# 数据库

## MySql

## redis

# 网站开发

## flask

## tornado

## django

### P374千锋Python教程：week11_day01_01Django的入门说明与第一个项目

```python
https://www.djangoproject.com/  #官网    
pip install django==2.0.1 -i https://mirrors.aliyun.com/pypi/simple		#源    
django-admin help startproject		#帮助

#创建项目
    django-admin startproject helloDjango  #创建项目结构
    django-admin startapp xxx  #创建app应用(模块)：
```



### P375千锋Python教程：week11_day01_02Django的路由与模板配置
### P376千锋Python教程：week11_day01_03初步使用ORM
### P377千锋Python教程：week11_day01_04模型的简单CRUD操作
### P378千锋Python教程：week11_day01_05实现模型的更新和删除
### P379千锋Python教程：week11_day01_06admin管理模型类
### P380千锋Python教程：week11_day01_07综合案例模型分析
### P381千锋Python教程：week11_day02_01模型的字段类型与约束
### P382千锋Python教程：week11_day02_02优化模型类和ImageField的使用
### P383千锋Python教程：week11_day02_03分析模型的元类和objects的来源
### P384千锋Python教程：week11_day02_04使用QuerySet的filter进行数据筛选
### P385千锋Python教程：week11_day02_05QuerySet的对象方法及聚合操作
### P386千锋Python教程：week11_day03_01Q和F的用法
### P387千锋Python教程：week11_day03_02原生SQL查询的方式-1
### P388千锋Python教程：week11_day03_03原生SQL查询的方式-2
### P389千锋Python教程：week11_day03_04设计订单模型类
### P390千锋Python教程：week11_day03_05一对一的模型关系
### P391千锋Python教程：week11_day03_06一对多模型关系
### P392千锋Python教程：week11_day04_01多对多关系应用
### P393千锋Python教程：week11_day04_02django_migrations表和模板的入门
### P394千锋Python教程：week11_day04_03采购记录与商品模型关系分析说明
### P395千锋Python教程：week11_day04_04模板标签和指令
### P396千锋Python教程：week11_day04_05模板过滤器及自定义过滤器
### P397千锋Python教程：week11_day05_01CSRF的原理和防范
### P398千锋Python教程：week11_day05_02check_password和session及取消csrf方式
### P399千锋Python教程：week11_day05_03模块内部模板文件
### P400千锋Python教程：week11_day05_04URL路由参数与正则
### P401千锋Python教程：week11_day05_05反向解析和错误的视图
### P402千锋Python教程：week11_day05_06扩展validators及批量创建和请求对象中的参数
### P403千锋Python教程：week12_day01_01MySQL的配置
### P404千锋Python教程：week12_day01_02request对象的基本属性和fetch()ajax请求
### P405千锋Python教程：week12_day01_03POST和PUT请求
### P406千锋Python教程：week12_day01_04PUT上传json数据和META及文件上传
### P407千锋Python教程：week12_day01_05Response响应对象及属性
### P408千锋Python教程：week12_day01_06Cookie与Session加Token
### P409千锋Python教程：week12_day02_01生成PIL图片验证码
### P410千锋Python教程：week12_day02_02Paginator分页器的使用
### P411千锋Python教程：week12_day02_03Django的CBV应用-1
### P412千锋Python教程：week12_day02_04Django的CBV应用-2
### P413千锋Python教程：week12_day02_05中间件的应用
### P414千锋Python教程：week12_day02_06Django日志的使用
### P415千锋Python教程：week12_day02_06Django日志的使用
### P416千锋Python教程：week12_day02_07Django的文件缓存
### P417千锋Python教程：week12_day02_07Django的文件缓存
### P418千锋Python教程：week12_day03_01复习知识及页面缓存

# 爬虫

## linux

### curl

#### P450千锋Python教程：01-curl的使用1

```bash
# 爬虫库和工具
    python: 
        requests
        urllib
        pycurl
    linux : 
        wget
        httpie
```

#### P451千锋Python教程：02-curl的使用2

安装curl

```bash
apt install openssl openssl-dev curl #安装curl
```

使用

```bash
#-A  设置user-agent
curl -A "Chrome" http://www.baidu.com

#-X 用指定方法请求
curl -X POST http://httpbin.org/post

#-I 只返回header信息
curl -I https://baidu.com
curl -I http://http://httpbin.org/status/[400-404]

HTTP/1.1 302 Moved Temporarily		#跳转
Server: bfe/1.0.8.18
Date: Fri, 13 Nov 2020 08:04:25 GMT
Content-Type: text/html
Content-Length: 161
Connection: keep-alive
Location: http://www.baidu.com/

#-d 以post方法请求url，并发送相应的参数
curl -d test=123 http://httpbin.org/post
curl -d "a=1&b=2&c=3" http://httpbin.org/post

#文件post.data
curl -d @/tmp/post.data http://httpbin.org/post

#-O 下载文件(大o)
curl -O http://httpbin.org/image/jpeg

#-o  小写o。自定义下载文件名
curl -o fox.jpeg http://httpbin.org/image/jpeg

#-L  跟随跳转
curl -L https://baidu.com
curl -IL https://baidu.com  #跟踪跳转，可看到2个请求

# -H  设置头信息
curl -o image.webp -H "accept:image/webp" http://httpbin.org/image 
curl -o image.png -H "accept:image/png" http://httpbin.org/image

#-k  允许发起不安全的ssl请求
curl -k https://www.12306.cn

#-b  发起一个带coookie的请求
curl -b a=test http://httpbin.org/cookies

#-s  slient  不显示其他无关信息
curl http://httpbin.org/get| grep "origin"
#-v 显示连接时的所有信息
```

### wget

#### P452千锋Python教程：03-wget的使用1

```bash
curl -s http://httpbin.org/get |grep "origin"| cut -d "\"" -f4  # 获取本地外网访问的IP
```

#### P453千锋Python教程：04-wget的使用2

- wget

```bash
#-O 以指定文件名下载文件
wget -O test.png http://httpbin.org/image/png

# --limit-rate=20k  限速下载
wget --limit-rate=20k https://qiniu.dfdalsjdfsdf.mp4

#-c  断点续传

#-b 在后台下载

#-U 指定agent
wget -U "Windows IE 10.0" -bc https://qiniu.dfdalsjdfsdf.mp4

#--mirror 镜像下载
#-p 下载链接资源文件
wget --mirror -U "Mozilla" -p http://docs.python-requests.org

#-r  递归下载所有的链接

# --convert-links 下载后，转换成本地的链接 
```

- httpie    brew install httpie
- postman
- charles    抓包软件
- fiddler

## urllib

## requests

## scrapy



# 算法