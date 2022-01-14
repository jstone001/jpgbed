

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