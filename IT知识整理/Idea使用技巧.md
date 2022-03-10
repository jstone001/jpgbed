

# 编译时内存不够

java.lang.OutOfMemoryError: WrappedJavaFileObject[org.jetbrains.jps.javac.InputFileObject

场景：

项目启动编译时报错

原因：

编译过程内存溢出

解决：

设置更大的编译内存，默认700M，可以依据实际情况逐渐增大

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxMzU4MTUx,size_16,color_FFFFFF,t_70.png)

 其他方案比如改JVM，觉得没必要因为这是编译中出现的问题。这样改影响最小

# idea导入eclipse项目的问题

## 配置JSTL 解决错误：org.apache.jasper.JasperException

from：https://blog.csdn.net/weixin_39663138/article/details/88148254?utm_medium=distribute.pc_relevant.none-task-blog-baidujs-9

```sh
org.apache.jasper.JasperException: This absolute uri (http://java.sun.com/jsf/core) cannot be resolved in either web.xml or the jar files deployed with this application

```

这个错误也是以前遇到过的，没有记录，再遇到又花了一些时间，还是记录以下：

解决方法：
 把 jstl jar 包下META_INF中的这几个文件复制一下，放到项目的WEB-INF目录下即可，如下图：

![在这里插入图片描述](https://gitee.com/jstone001/booknote/raw/master/jpgBed/20190305001341196-16456774606742.png)

复制其中这几个，如图：
 ![在这里插入图片描述](https://gitee.com/jstone001/booknote/raw/master/jpgBed/20190305001433535.png)

放到项目的WEB-INF目录下：
 ![在这里插入图片描述](https://gitee.com/jstone001/booknote/raw/master/jpgBed/20190305001632820.png)
 再运行！解决！~~

## IDEA启动Tomcat报错 java.lang.ClassNotFoundException: org.apache.jsp.index_jsp

 IDEA启动Tomcat报错 java.lang.ClassNotFoundException:  org.apache.jsp.index_jsp的解决办法 **  在idea里不能直接往项目里导入jstl.jar和standard.jar,应该在WEB-INF目录下创建一个lib文件夹将jar包导入lib文件夹中再引用就不会出现异常了。 这个问题困惑了我半天，希望可以帮助到你们！...

