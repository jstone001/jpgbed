# tomcat7中cookie写入中文引发的异常问题及解决

from: https://www.aliyun.com/jiaocheng/859738.html

摘要：java.lang.IllegalArgumentException:Controlcharacterincookievalueorattribute问题:Cookiecookie=newCookie("cookieName","Cookie值");resp.addCookie(cookie);目标URL访问此方法时,会抛出异常:java.lang.IllegalArgumentException:Controlcharacterinc

java.lang.IllegalArgumentException: Control character in cookie value or attribute

问题:

```java
Cookie cookie = new Cookie("cookieName", "Cookie值");
resp.addCookie(cookie);

//目标URL访问此方法时,会抛出异常:
java.lang.IllegalArgumentException: Control character in cookie value or attribute
//描述:
//环境:tomcat7,Java7
//具体问题:cookie写入中文会引发异常。所以这样看来Cookie默认支持的编码方式是ASCII码。不能对中文进行编码和解码。

```

<font color='red'>解决方式:</font>

```java
//创建Cookie的时候:
Cookie cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));

//读取Cookie的时候:
String value = URLDecoder.decode(cookie.getValue(), "UTF-8");
```

# tomcat 中文乱码

server.xml 中

```xml
<Connector port="8080" maxThreads="150" minSpareThreads="25" maxSpareThreads="75"
enableLookups="false" redirectPort="8443" acceptCount="100"
connectionTimeout="20000" disableUploadTimeout="true" URIEncoding="utf-8" />
```

# tomcat 配数据源

在context.xml 的<Context></Context>之间

```xml
 <Resource name="jdbc/db"
            auth="Container"
            type="javax.sql.DataSource"                      
            driverClassName="oracle.jdbc.driver.OracleDriver"  
            url="jdbc:oracle:thin:@10.1.1.99:1521:YBCP"
            username="wxkfyh"
            password="wxkfyh"
            maxActive="20"    
            maxIdle="10"
            maxWait="10000"/>
    <!-- Default set of monitored resources -->
    <WatchedResource>WEB-INF/web.xml</WatchedResource>
```

# tomcat 内存设置

 最近做项目碰到了让我纠结的问题，tomcat服务器运行一段时间，总是会自动报异常：java.lang.OutOfmemoryError:

PermGen Space 的错误，导致项目无法正常运行。

 出现这个错误的原因，总结一下：

PermGen Space指的是内存的永久保存区，该块内存主要是被JVM存放class和mete信息的，当class被加载loader的时候

就会被存储到该内存区中，与存放类的实例的heap区不同，java中的垃圾回收器GC不会在主程序运行期对PermGen space

进行清理，所以当我们的应用中有很多的class时，很可能就会出现PermGen space的错误。

```sh
# 解决方法：
# 1. 手动设置MaxPermSize的大小
修改 TOMCAT_HOME/bin/catalina.bat(Linux上为catalina.sh)文件，在echo "using CATALINA_BASE：$CATALINA_BASE"上面加入这一行内容：
set JAVA_OPTS=%JAVA_OPTS% -server -XX:PermSize=128m -XX:MaxPermSize=512m
catalina.sh修改如下：
JAVA_OPTS="$JAVA_OPTS" -server -XX:PermSize=128m -XX:MaxSize=512m

# 2. 修改 TOMCAT_HOME/bin/catalina.bat文件的内容：在 %_EXECJAVA% %JAVA_OPTS% 后面添加 -Xms=256m -Xmx512m 注意哦，前后后有空格的
例如： %_EXECJAVA% %JAVA_OPTS% -Xms=256m -Xmx512m(空格)后面的内容不变

# 3. 可以考虑将相同的第三方jar文件拷贝到tomcat/shared/lib 目录下，这样可以减少jar文件重复占用内存的
```

# 修改servlet无需重启tomcat 

```xml
试了很多方法只有这一种有效
编辑Tomcat目录下conf目录中
<!-- 第一步：找到<Host name="localhost" appBase="webapps"  -->

<!-- 第二步：在其后加上这样一句话：-->
<Context path="/myapp" docBase="myapp" debug="99" reloadable="true" />
"myapp" 为要部署的应用程序，通常在webapps目录下，docBase则是你的项目所在的路径
比如你有一个项目在webapps下面，名为apps
那就加上这样一句话就行了：
<Context path="/apps" docBase="apps" debug="99" reloadable="true" />
在这里debug可以不写，如果项目位于webapps下面的话，docBase也可以省略，但是path参数和reloadable="true"一定得写，像这样：
<Context path="/apps"   reloadable="true" />
不然以后修改了java文件编译后还得重启tomcat

<!-- 第三步：重启tomcat让修改生效 -->
这样，以后再修改servlet后就不用重启tomcat了，只要重新编译java文件，tomcat会检测到并重新导入servlet，如果使用的是dos窗口启动tomcat，重新编译servlet后tomcat会出现以下提示：
org.apache.catalina.core.StandardContext reload
信息: Reloading this Context has started
```

# 严重_ IOException while loading persisted sessions_ java.io.EOFException

<font color='red'>严重: IOException while loading persisted sessions: java.io.EOFException 严重: Exception loading sessions from persistent storage</font>

原因是tomcat对硬盘的session读取失败，彻底解决办法一下：将work下面的文件清空，主要是*.ser文件，或者只是删除掉session.ser即可以解决。

分析：EOFException表示输入过程中意外地到达文件尾或流尾的信号,导致从session中获取数据失败。异常是tomcat本身的问题，由于tomcat上次非正常关闭时有一些活动session被持久化（表现为一些临时文件），在重启时，tomcat尝试去恢复这些session的持久化数据但又读取失败造成的。此异常不影响系统的使用。

​    解决办法:将tomcat6.0\work\Catalina\localhost\peam\SESSIONS.ser删除。如果正常关闭服务端，该文件是自动删除的。

​    我参照上面方法解决的问题，用的是MyEclipse6.0,tomcat6.0。通常情况下，会认为是tomcat的缓存，会直接把整个localhost文件夹删除。但是上面的方法也是可取的，在localhost文件夹下，找到部署的工程名，在该工程名下有<font color='red'>SESSIONS.ser文件</font>，直接删除。重启tomcat，问题解决。

# PermGen space错误解决方法

from: http://www.cnblogs.com/xwdreamer/archive/2011/11/21/2296930.html

在看下文之前，首先要确认意见事情，就是你是如何启动tomcat的，我们在平时的开发环境当中，都是通过startup.bat方式启动tomcat的，那么你按照下面的方式，去修改/bin/catalina.bat是没有问题的。但是如果你是生产环境下，我们一般都希望使用windows服务方式去启动tomcat，此时之前修改的配置文件是没有用的。因为windows服务启动tomcat不再去加载catalina.bat当中的参数了，而是去加载注册表中的参数，所以我们需要修改注册表。

HKEY_LOCAL_MACHINE/SOFTWARE/Apache Software Foundation/Procrun 2.0/Tomcat_APPNAME/Parameters/Java，修改JvmMs和JvmMx的值，当前我都将其设定为1024，也就是1个G的容量。具体性能再后面继续观察。 之前还真不知道windows服务启动和startup.bat启动的区别。 windows服务器启动是在注册表中加载参数，startup.bat启动是在catalina.bat加载参数。

 ![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/232321393198.png)

**1.参考：**

http://blog.csdn.net/fox009/article/details/5633007

http://hi.baidu.com/like_dark/blog/item/19c1948b3292b0799f2fb468.html

http://anyeeye.iteye.com/blog/444624

[Tomcat6性能调优 出现java.lang.OutOfMemoryError: PermGen space](http://hi.baidu.com/chssheng2007/blog/item/22e7b1d37d7f480b3af3cf18.html)

http://www.mkyong.com/tomcat/tomcat-javalangoutofmemoryerror-permgen-space/

**2.报错：**

```
Exception in thread "DispatcherThread" java.lang.OutOfMemoryError: PermGen space
Exception in thread "ContainerBackgroundProcessor[StandardEngine[Catalina]]" java.lang.OutOfMemoryError: PermGen space
Exception in thread "State Saver" java.lang.OutOfMemoryError: PermGen space
Exception in thread "AWT-Windows" java.lang.OutOfMemoryError: OutOfMemoryError
```

**3.原因：**

PermGen space的全称是Permanent Generation space,是指内存的永久保存区域,这块内存主要是被JVM存放Class和Meta信息的,Class在被Loader时就会被放到PermGen space中,它和存放类实例(Instance)的Heap区域不同,GC(Garbage Collection)不会在主程序运行期对PermGen space进行清理，所以如果你的应用中有很多CLASS的话,就很可能出现PermGen space错误,这种错误常见在web服务器对JSP进行pre compile的时候。如果你的WEB APP下都用了大量的第三方jar, 其大小超过了jvm默认的大小(4M)那么就会产生此错误信息了。

**4.解决方法1：** 

手动设置MaxPermSize大小，如果是linux系统，修改TOMCAT_HOME/bin/catalina.sh，如果是windows系统，修改TOMCAT_HOME/bin/catalina.bat，

在“echo "Using CATALINA_BASE: $CATALINA_BASE"”上面加入以下行：

JAVA_OPTS="-server -XX:PermSize=64M -XX:MaxPermSize=128m

<font color='red'>**建议：将相同的第三方jar文件移置到tomcat/shared/lib目录下，这样可以达到减少jar 文档重复占用内存的目的。**</font>

**5.解决方法2**

修改eclipse.ini文件，修改如下：

```ini
-vmargs
-Dosgi.requiredJavaVersion=1.5
-Xms128m
-Xmx512m
-XX:PermSize=64M 
-XX:MaxPermSize=128M
```

如果还报错，可以考虑如下修改

```ini
-vmargs
-Dosgi.requiredJavaVersion=1.5
-Xms512m
-Xmx1024m
-XX:PermSize=256M 
-XX:MaxPermSize=512M
```

报错：

```
011-11-21 21:10:46 org.apache.catalina.loader.WebappClassLoader clearReferencesJdbc
严重: The web application [/Application] registered the JDBC driver [com.mysql.jdbc.Driver] but failed to unregister it when the web application was stopped. To prevent a memory leak, the JDBC Driver has been forcibly unregistered.
2011-11-21 21:10:46 org.apache.catalina.loader.WebappClassLoader clearReferencesThreads
严重: The web application [/Application] appears to have started a thread named [MySQL Statement Cancellation Timer] but has failed to stop it. This is very likely to create a memory leak.
2011-11-21 21:10:46 org.apache.catalina.loader.WebappClassLoader clearReferencesThreads
严重: The web application [/Application] appears to have started a thread named [AWT-Windows] but has failed to stop it. This is very likely to create a memory leak.
2011-11-21 21:10:46 org.apache.catalina.loader.WebappClassLoader clearReferencesThreads
严重: The web application [/Application] appears to have started a thread named [Thread-14] but has failed to stop it. This is very likely to create a memory leak.
2011-11-21 21:10:46 org.apache.catalina.loader.WebappClassLoader clearThreadLocalMap
严重: The web application [/Application] created a ThreadLocal with key of type [net.sf.json.AbstractJSON$1] (value [net.sf.json.AbstractJSON$1@3661eeb]) and a value of type [java.util.HashSet] (value [[]]) but failed to remove it when the web application was stopped. This is very likely to create a memory leak.
2011-11-21 21:10:50 org.apache.catalina.core.ApplicationContext log
信息: Initializing Spring FrameworkServlet 'Dispatcher'
```

修改catalina.bat

添加

```ini
JAVA_OPTS="-Djava.awt.headless=true -Dfile.encoding=UTF-8 
-server -Xms1536m -Xmx1536m
-XX:NewSize=256m -XX:MaxNewSize=256m -XX:PermSize=256m 
-XX:MaxPermSize=256m -XX:+DisableExplicitGC"
```

```cmd
#   JSSE_HOME       (Optional) May point at your Java Secure Sockets Extension
#                   (JSSE) installation, whose JAR files will be added to the
#                   system class path used to start Tomcat.
#
#   CATALINA_PID    (Optional) Path of the file which should contains the pid
#                   of catalina startup java process, when start (fork) is used
#
# $Id: catalina.sh 609438 2008-01-06 22:14:28Z markt $
# -----------------------------------------------------------------------------
 
JAVA_OPTS="-Djava.awt.headless=true -Dfile.encoding=UTF-8 -server -Xms1536m 
-Xmx1536m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:PermSize=256m 
-XX:MaxPermSize=256m -XX:+DisableExplicitGC"
```

```cmd
# OS specific support.  $var _must_ be set to either true or false.
cygwin=false
os400=false
darwin=false
case "`uname`" in
CYGWIN*) cygwin=true;;
OS400*) os400=true;;
Darwin*) darwin=true;;
esac
 
# resolve links - $0 may be a softlink
PRG="$0"
```

```cmd
# 具体参数根据自己机器情况而定
JAVA_OPTS="-Djava.awt.headless=true -Dfile.encoding=UTF-8 -server -Xms512m 
-Xmx512m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:PermSize=256m 
-XX:MaxPermSize=256m -XX:+DisableExplicitGC"
```

