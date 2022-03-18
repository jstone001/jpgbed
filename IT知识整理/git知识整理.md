# docker 安装 gogs

from: https://www.jianshu.com/p/2a7acb07b352

## 1、在宿主机创建目录

```sh
mkdir /opt/docker/gogs/
```



## 二. 运行容器

在此之前,先在mysql中创建gogs数据库. [注意,一定要先将mysql数据库的默认字符编码设置为utf8, 否则, gogs在自动创建表时, 会出现问题]

```sh
docker run -d -p 10022:22 -p 10080:3000 \ 
--name=gogs --net=backend \ 
-v /opt/docker/gogs/:/data \ 
gogs/gogs

# 参数说明:
# -d: 后台方式运行容器
# -p: 端口映射, 将容器的22端口映射到宿主机的10022端口, 将容器的3000端口映射到宿主机的10080端口
# --name: 指定容器名称
# --net: 将容器加入backend网络, 目的是为了能够连接backend网络中的mysql数据库
# -v: 数据卷挂载, 用于将容器和数据分离


```

## 三. 打开浏览器,进行gogs配置

`http://宿主机ip:10080`
 如: localhost:10080

![image-20220316123416946](E:\JS\booknote\jpgBed\image-20220316123416946.png)

![image-20220316123436705](E:\JS\booknote\jpgBed\image-20220316123436705.png)

**注意两个配置:**

1. `数据库主机` 值应该是: `mysql57:3306` 这个是docker中连接另一个容器中服务的方式(上一篇文章在启动mysql容器时,将mysql加入backend网络的目的就在于此).
2. `应用URL`, 这个url应该是`宿主机地址:10080`

点击确认, 之后会转到一个无法连接的页面, 是因为 刚才的`应用URL`指定的端口是3000, 但访问宿主机的3000并无意义, 必须访问10080才行, 为什么是10080, 因为容器启动时, 建立了`-p 10080:3000`的映射.

下一步我们将通过修改配置文件的方式修正这个问题

## 四.修改配置文件

配置文件路径: `/opt/docker/gogs/gogs/conf/app.ini`

```ini
APP_NAME = Gogs
RUN_USER = git
RUN_MODE = prod

[database]
DB_TYPE  = mysql
HOST     = mysql57:3306    ## 注意这里, 是docker中连接通网络,其它容器服务的方式
NAME     = gogs
USER     = root
PASSWD   = root
SSL_MODE = disable
PATH     = data/gogs.db

[repository]
ROOT = /data/git/gogs-repositories

[server]
DOMAIN           = localhost
HTTP_PORT        = 3000
ROOT_URL         = http://localhost:10080/   ## 这个就是图形界面的应用URL的值
DISABLE_SSH      = false
SSH_PORT         = 10022
START_SSH_SERVER = false
OFFLINE_MODE     = false

[mailer]
ENABLED = false

[service]
REGISTER_EMAIL_CONFIRM = false
ENABLE_NOTIFY_MAIL     = false
DISABLE_REGISTRATION   = false
ENABLE_CAPTCHA         = true
REQUIRE_SIGNIN_VIEW    = false

[picture]
DISABLE_GRAVATAR        = false
ENABLE_FEDERATED_AVATAR = true

[session]
PROVIDER = file

[log]
MODE      = file
LEVEL     = Info
ROOT_PATH = /app/gogs/log

[security]
INSTALL_LOCK = true
SECRET_KEY   = nV8DDKEN2IExVjr

```

修改完之后, 保存, 重启gogs容器,再访问
 [http://localhost:10080](https://link.jianshu.com?t=http%3A%2F%2Flocalhost%3A10080)
 即可看到登录, 界面了, 点击注册, 注册成功后的第一个用户, 会成为超管用户.

## 五. 测试

- 使用gogs的图形界面,创建一个项目. 然后在本地, pull下来, 修改之后再push上去. 然后看界面中是否有你提交的结果

- 使用如下命令, 停止并删除容器

  ```sh
   docker stop gogs
   docker rm gogs
  ```

  

- 再次启动容器

```sh
docker run -d -p 10022:22 -p 10080:3000 \ 
--name=gogs --net=backend \ 
-v /opt/docker/gogs/:/data \ 
gogs/gogs
```

- 访问 [http://localhost:10080](https://link.jianshu.com?t=http%3A%2F%2Flocalhost%3A10080), 登录查看之前创建是否依然存在, 如果存在, 则你已成功完成gogs部署了

