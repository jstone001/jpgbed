# ysxtqx测试及正式上的发布

## 1、ysxtqx 发布

### 1.1 更新maven

在myEclispe下，下载全部最新的ysxtqx项目。

1. 在项目名下，右键点击run as-> run configrations的Maven Build中配置强制更新

2. 新建一个Maven Build

3. Base directory: 项目在电脑中的位置

4. Goals: clean package -e -U （-e 显示错误， -U强制更新）

   <img src="http://note.youdao.com/yws/public/resource/1026851c45add8209b355e7fc7749b1d/xmlnote/48F0532ADB814439AB960932B6F51686/34212" style="zoom:120%;" />

   

### 1.2 ysxtqx发布文件配置

   1. 发布时配置applicationContext.xml 增加数据源。

   ```xml
    	<bean id="dataSource" class="org.springframework.jndi.JndiObjectFactoryBean">
   			<description>JNDI 数据源配置</description>
   			<property name="jndiName" value="YSQXCSKDB"/>   <!-- 测试 -->
     													<!-- YSQXZSKDB(正式) -->
   	</bean>
   ```

   2. redis.config.properties 中配置ip地址(本机的ip)如：

      redis.server.host=172.30.195.29。(测试内网)
      							 172.31.195.21(测试外网) 
      							  <font color=red>**172.31.195.40(正式)**</font>
      
3. 在wssuap.properties中第3行配置：authentic.encoder.type=plain。(开发环境)

   ​													    authentic.encoder.type=sha。	(测试环境、正式环境)

  <img  src="http://note.youdao.com/yws/public/resource/1026851c45add8209b355e7fc7749b1d/xmlnote/A50E2F3C5B074D52A58526AEC5598B31/34834" div align="center"/>
  4. 在生产环境时，logback.xml 第34行

```xml
<logger name="com.wondersgroup.shys.framework.dao" level="INFO" additivity="false">  <!-- INFO -->
```


​	配置level等级。

  5. 在web.xml目录下，增加weblogic.xml文件。

     正式环境上如有集群增加集群的配置，在weblogic.xml中添加：

     ```XML
     	<library-ref>
             <library-name>coherence-web-spi</library-name>
         </library-ref>
         <coherence-cluster-ref>
             <coherence-cluster-name>Coherence-0</coherence-cluster-name>
         </coherence-cluster-ref>	<!-- 测试环境上不用增加，先注释掉。 -->
     ```

### 1.3  发布
1. 先将之前运行的项目备份。(根据日期新建文件夹。如：d:\正式备份\20160227\ysxtqx)
2. 项目发布在172.31.195.40上的d:\project\。
3. 发布时先运行radis3.0中的redis-server.exe 。
4. 发布时选择所有的集群。
5. 第2步选择复制到项目路径。
6. 在项目启动时，查看	D:\Oracle\Middleware\wlserver_10.3\common\nodemanager\servers\server1\logs\server1.log 查看启动日志。

## 2、ysxtqx_jk文件发布

### 2.1 更新maven

1. 在myEclispe下，下载全部最新的ysxtqx、ysxtqx_jk、ysxtqx_ws项目。

2. 强制更新最新的maven包

   在项目名下，右键点击run as-> run configrations

   ​	新建一个Maven Build

   ​	Base directory: 项目在电脑中的位置

   ​	Goals: clean package -e -U （-e 显示错误， -U强制更新）

   ![d](http://note.youdao.com/yws/public/resource/1026851c45add8209b355e7fc7749b1d/xmlnote/04F89754CFB9471AA8DF5FA3B0FE9A93/34836)

3. 安装ysxt_jk-spec入本地maven 库

   1. 在ysxtqx_jk\ysxt_jk-spec目录下，按住shift键，点击鼠标右键，选择在此文件夹打开命令窗口，打开cmd界面。
   2. 运行mvn clean。
   3. 运行 mvn install -e -U。 强制更新。此时在电脑的maven仓库中的
      	repository\com\wondersgroup\shys\module\ysxtqx_jk-spec\1.0-SNAPSHOT目录下应该有最新的ysxtqx_jk-spec-1.0-SNAPSHOT.jar文件。
   

### 2.2  修改配置文件

1、 修改ysxtqx_jk\ysxtqx_jk-impl\src\main\resources下database.properties 中的数据库配置。
resources下共有4个目录其中：
```properties
#开发环境(development目录)：
jdbc.username=ysqx
jdbc.password=ysqx
jdbc.url=jdbc:oracle:thin:@10.1.25.61:1521:YBCP
 
#内网测试(text目录)：
jdbc.username=ysqx
jdbc.password=ysqx
jdbc.url=jdbc:oracle:thin:@172.30.195.26:1521:shybtest

#33外网测试(text目录)：
jdbc.username=ysqx
jdbc.password=ysqx
jdbc.url=jdbc:oracle:thin:@172.31.195.21:1521:orcl
```

2、修改upper-module-provider.xml文件
	根据发布环境要求在development、text、production目录下配置
		META-INF\spring\upper-module-provider.xml中第10行：
```xml
	开发环境： <dubbo:registry address="redis://127.0.0.1:6379"/> 
	内网测试： <dubbo:registry address="redis://172.30.195.29:6379"/> 
	外网测试： <dubbo:registry address="redis://172.31.195.21:6379"/> 
	外网正式： <dubbo:registry address="redis://172.31.195.40:6379"/> 
```
​	如果6379端口被占用，请更换端口号。

![](http://note.youdao.com/yws/public/resource/1026851c45add8209b355e7fc7749b1d/xmlnote/3B56299EB01C4E1CA2C5B55067B383C2/34838)

在测试和开发环境中，将第9行：timeout="60000"  //一分钟

如果在同一机器上发多个接口，则第21行增加port

```xml
<dubbo:protocol name=**"dubbo"** serialization=**"fastjson"** port=**"30118"**/>
```

3、redis.config.properties文件：

​	根据发布环境要求在development、text、production目录下配置

```properties
开发环境： redis.server.host=127.0.0.1
内网测试： redis.server.host=192.30.195.29
外网测试： redis.server.host=192.31.195.21
外网正式： redis.server.host=192.31.195.40 
如果6379端口被占用，请更换端口号。配置与之前upper-module-provider.xml一致。
```

4、 修改wssuap.properties文件

​	根据发布环境要求在development、text、production目录下配置

```properties
开发环境： authentic.encoder.type=plain
内网测试： authentic.encoder.type=sha
外网测试： authentic.encoder.type=sha
外网正式： authentic.encoder.type=sha
```

5、修改logback.xml文件

```properties
开发环境中：第34行
     <logger name="com.wondersgroup.shys.framework.dao" level="TRACE" additivity="false">
内网测试 第24行：        <level value="INFO"/>
外网测试 第24行：        <level value="INFO"/>
外网正式 第24行：        <level value="INFO"/>
```

### 2.3 发布

1. 在项目的E:\MyEclipse 8.51\ysxtqx_jk\ 按住shift键，点击鼠标右键，选择在此文件夹打开命令窗口，打开cmd界面。

2. 运行 mvn clean。

3. 根据不同的发布要求运行：

   ​	运行mvn clean package -Dmaven.test.skip=true。(开发环境中)

   ​	<font color=red>运行mvn clean package -Dmaven.test.skip=true -Ptest。(测试环境中)</font>

   ​	运行mvn clean package -Dmaven.test.skip=true -Pprod。(正式环境中)

   

4. 在target中找到ysxtqx-jk.jar 文件，与项目中的start.cmd 和start.sh放在同一个目录下。

5. 配置start.cmd/start.sh 文件。JAVA_HOME名称与系统环境一致。

   ![](http://note.youdao.com/yws/public/resource/1026851c45add8209b355e7fc7749b1d/xmlnote/36628586F74848519C39DD749AD9E776/34840)

6. 在环境下先运行redis。

7. 将之前的ysxtqx-jk.jar包按时间备份。如：d:\测试备份\备份20160228\。

8. 在系统环境运行start.cmd/start.sh。

## 3、ysxtqx_ws发布

### 3.1 修改配置文件

1、配置dsContext.xml文件

​	根据发布环境要求在development、text、production目录下配置

​	在开发环境中，需要配置databases.properties。

```properties
jdbc.username=ysqx
jdbc.password=ysqx
jdbc.url=jdbc:oracle:thin:@10.1.25.61:1521:YBCP
```

​	dsContext.xml配置

```xml
内网测试：<description>JNDI 数据源配置</description>
        <property name="jndiName" value="YSQXCSKDB"/>
外网测试： <description>JNDI 数据源配置</description>
        <property name="jndiName" value="YSQXCSKDB"/>
外网正式： <description>JNDI 数据源配置</description>
        <property name="jndiName" value="YSQXZSKDB"/>
```

2、 配置dubbo-consumer.xml

​	根据发布环境要求在development、text、production目录下配置

​	src\main\resources\META-INF\spring\dubbo-consumer.xml：

```xml
开发环境：<dubbo:registry address="redis://172.30.195.29:6379"/>
        		<dubbo:consumer retries="0" timeout="3600000"/> 
内网测试：<dubbo:registry address="redis://172.30.195.29:6379"/>
        		<dubbo:consumer retries="0" timeout="60000"/>   //一分钟
外网测试：<dubbo:registry address="redis://172.31.195.33:6379"/>
        		<dubbo:consumer retries="0" timeout="60000"/>   //一分钟
外网正式：<dubbo:registry address="redis://172.31.195.40:6379"/>
        		<dubbo:consumer retries="0" timeout="60000"/>   //一分钟
```

​	如果要使用不同的port在第20行增加:

```xml
<dubbo:protocol name="dubbo" serialization="fastjson" port="30118"/>
<dubbo:protocol name="dubbo" serialization="fastjson" port="30122"/>等
```

3、配置redis.config.properties文件

​	根据发布环境要求在development、text、production目录下配置

```properties
开发环境： redis.server.host=127.0.0.1
内网测试： redis.server.host=192.30.195.29
外网测试： redis.server.host=192.31.195.21
外网正式： redis.server.host=192.31.195.40 
```

4、配置wssuap.properties文件

```properties
#在开发环境中：
authentic.encoder.type=plain

wssuap.domain.code=YS
wssuap.app.code=YSQX
realtime.group.code=

#在内网测试环境中：
authentic.encoder.type=sha

wssuap.domain.code=YS
wssuap.app.code=YSQX
realtime.group.code=DLQYJKJY

#在外网测试环境中：
authentic.encoder.type=sha

wssuap.domain.code=YS
wssuap.app.code=YSQX
realtime.group.code=DLQYJKJY

#在外网正式环境中：
authentic.encoder.type=sha

wssuap.domain.code=YS
wssuap.app.code=YSQX
realtime.group.code=DLQYJKJY
```

5、修改logback.xml文件

```xml
开发环境中：第34行:
     <logger name="com.wondersgroup.shys.framework.dao" level="TRACE" additivity="false">
内网测试 第24行：        <level value="INFO"/>
外网测试 第24行：        <level value="INFO"/>
外网正式 第24行：        <level value="INFO"/>
```

### 3.2  发布

1. 在ysxtqx_ws的pom文件中，修改 <finalName>ysxtqx_ws</finalName> 中修改war包的名字。

2. 在E:\MyEclipse 8.51\ysxtqx_ws路径下， 按住shift键，点击鼠标右键，选择在此文件夹打开命令窗口，打开cmd界面。

   运行mvn clean package -Dmaven.test.skip=true。(开发环境中)

   <font color=red>运行mvn clean package -Dmaven.test.skip=true -Ptest。(测试环境中)</font>

   运行mvn clean package -Dmaven.test.skip=true -Pprod。(正式环境中)

3. 在target文件夹中将ysxtqx-ws.war复制到发布环境中。

4. 在发布环境中，将之前的war包备份。备份方式，如d:/发布目录/测试前备份/20160228/ (d:/发布目录/正式前备份/20160228)

5. 解压后，通过fileFIlla软件，发布到linux上，路径/oracle/application/

6. 在weblogic控制台上，更新项目。

7. 打开secureCRT软件，进入所在的服务器，进入/home/weblogic/logs/。

8. 使用tail -f server1.log命令，查看启动日志。

9. 在weblogic上确认更改，项目启动。

