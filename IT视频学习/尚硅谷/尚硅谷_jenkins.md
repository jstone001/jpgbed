from: https://www.bilibili.com/video/BV1GW411w7pn

# 介绍

## P01.持续集成、持续部署、持续交付的概念

- 持续部署
- 持续集成：模块整合
- 持续交付：用小版本不断进行快速迭代，不断收集用户反馈信息，用最快的速度改进优化。
  - 关注点：在于研发团队的最新代码能够尽快让最终用户体验到。

总体目标：

好处：

1. 降低风险
2. 减少重复过程
3. 任何时间，任何地点生成可部署的软件
4. 增强项目的可见性
5. 建立团队对开发产品的信心

## P02.Jenkins和Hudson

## P03.Web工程部署方式说明

1. 钩子程序
2. git/svn插件
3. maven插件
4. deploy to web container插件

# SVN

## P04.Subversion环境要求



## P05.应用服务器设置账号密码说明

因为是机器访问服务，所以要设置用户名和密码

tomcat/conf/tomcat-users.xml

```xml
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<role rolename="manager-jmx"/>
<role rolename="manager-status"/>
<user  username="tomcat_user"  password="123456" role="manager-gui,manager-script,manager-jmx,manager-status"/>
```

## P06.运行Jenkins主体程序并初始化

tomcat/conf/server.xml

```xml
<Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443"
           URIEncodeing="UTF-8" />	<!-- 配置下uncoding  -->
```



## P07.配置JDK和Maven并安装Deploy插件
## P08.创建用于测试的Maven工程

## P09.创建SVN版本库并提交Maven工程

修改svn的svnserve.conf

```sh
anon-access =none   # 匿名访问
auth-acces= write
password-db = passwd  # 保存用户名密码
authz-db =authz  # 授权
```

passwd

```sh
subman = 123123  # 新建账户
```

authz

```sh
[/]
subman =rw  # subman 有读写权限
* =
```

## P10.创建工程并配置源码管理
## P11.配置构建命令并手动执行一次构建
## P12.配置构建完成后部署到Tomcat上
## P13.配置远程触发构建的TOKEN值

构建触发器

## P14.curl命令触发构建

svn/repository/apple/hooks/post-commit.tmpl

```sh
curl -X post -v -u admin:12bc334fbe2833d3 http://192.168.70.131:8080/jenkins/job/apple/build?token=ATGUIGU_TOKEN
```



## P15.编辑SVN钩子程序

在svn/repository/apple/hooks/post-commit.tmpl 复制一个post-commit

复制该curl命令

```sh
chmod 755 post-commit
```

## P16.测试验证整个自动化持续集成流程

```sh
Repository URL=svn://192.168.70.130/Apple@HEAD	# 没有head的话，只更新上一个版本
```

# Git

## P17.整合GitHub的持续集成环境要点说明

和svn的区别：

- jenkins要部署到外网上。这一点可以通过租用阿里云等平台提供的云服务器。
- jenkins所在的主机上需要安装git，通过git程序从github上clone代码。
- 在jenkins内需要指定git程序位置，和指定jdk，maven程序位置非常类似。
- 在github上使用每个repository的webHook方式远程触发jenkins构建。
- 在jenkins内关闭“防止跨站点请求伪造”。

## P18.安装Git客户端
## P19.在Jenkins中指定Git客户端位置
## P20.在GitHub上创建WebHook



## P21.关闭防止跨站点请求伪造



## P22.总结