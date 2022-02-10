# JdbcTemplate

# SpringMVC

## SpringMVC @ResponseBoby 中文返回乱码

springMVC-*.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
    xmlns:tx="http://www.springframework.org/schema/tx" xmlns:context="http://www.springframework.org/schema/context"
    xmlns:mvc="http://www.springframework.org/schema/mvc"
    xsi:schemaLocation="
       http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd">
    <!-- utf-8编码 -->
    <mvc:annotation-driven>
        <mvc:message-converters register-defaults="true">
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <constructor-arg value="UTF-8" />
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>
</beans>

```

如果可以看org.springframework.http.converter.StringHttpMessageConverter这个类的源码的话就会发现其默认的编码方式为  "ISO-8859-1"，

这个应该是造成我们中文乱码的主要原因。


在这里不能不吐槽一下，好多老外写的jar包都会出现中文乱码问题，究其主要原因就是人家不用中文。。。故肯定会选择内存占用小的"ISO-8859-1",

啥时候才能大家都统一使用utf-8呀。。
————————————————
版权声明：本文为CSDN博主「生活不只*眼前的苟且」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/u011734144/article/details/53817064

# Maven

## 解决maven modules打包的项目名称是parent的项目名称问题

parent的pom.xml下去掉

```xml
<build>
    <finalName>microservice</finalName>		<!-- 去掉  -->
</build>
```

## dependencyManagement作用说明

from: https://blog.csdn.net/vtopqx/article/details/79034835

在父模块中：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.44</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

那么在子模块中只需要<groupId>和<artifactId>即可，如：

```html
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
</dependencies>
```

**说明：**

使用dependencyManagement可以统一管理项目的版本号，确保应用的各个项目的依赖和版本一致，不用每个模块项目都弄一个版本号，不利于管理，当需要变更版本号的时候只需要在父类容器里更新，不需要任何一个子项目的修改；如果某个子项目需要另外一个特殊的版本号时，只需要在自己的模块dependencies中声明一个版本号即可。子类就会使用子类声明的版本号，不继承于父类版本号。

**与dependencies区别：**

1)Dependencies相对于dependencyManagement，所有生命在dependencies里的依赖都会自动引入，并默认被所有的子项目继承。
2)dependencyManagement里只是声明依赖，并不自动实现引入，因此子项目需要显示的声明需要用的依赖。如果不在子项目中声明依赖，是不会从父项目中继承下来的；只有在子项目中写了该依赖项，**并且没有指定具体版本**，才会从父项目中继承该项，并且**version**和**scope**都读取自父pom；另外如果子项目中指定了版本号，那么会使用子项目中指定的jar版本。

## maven引入oracle

```sh
mvn install:install-file -Dfile=ojdbc6.jar -Dpackaging=jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.4.0　　

# 命令解释：mvn install:install-file -Dfile="jar包的绝对路径" -Dpackaging="文件打包方式" -DgroupId=groupid名 -DartifactId=artifactId名 -Dversion=jar版本 （artifactId名对应之后maven配置的依赖名）。

安装成功后会出现BUILD SUCCESS。
————————————————
版权声明：本文为CSDN博主「DY_HM」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/qq_21359547/article/details/79731665
```

