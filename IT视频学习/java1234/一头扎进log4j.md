# 第1章 简介
## 第1节 简介
等级可分为OFF、FATAL、ERROR、WARN、INFO、DEBUG、ALL

## 第2节 HelloWorld
```java
logger.error("error", new IllegalArgumentException("illgal错误"));
```

# 第2章 配置详解
## 第1节 rootLogger
log4j.rootLogger = [ level ] , appenderName, appenderName, …

## 第2节 日志等级
## 第3节 appender输出类型配置

```properties
org.apache.log4j.ConsoleAppender（控制台），  
org.apache.log4j.FileAppender（文件），  
org.apache.log4j.DailyRollingFileAppender（每天产生一个日志文件），  
org.apache.log4j.RollingFileAppender（文件大小到达指定尺寸的时候产生一个新的文件），  
org.apache.log4j.WriterAppender（将日志信息以流格式发送到任意指定的地方）

假如日志数据量不是很大，我们可以用DailyRollingFileAppender 每天产生一个日志，方便查看；
假如日志数据量很大，我们一般用RollingFileAppender，固定尺寸的日志，假如超过了 就产生一个新的文件；
```

## 第4节 log4j layout日志信息格式

http://blog.java1234.com/blog/articles/271.html

```properties
有个ConversionPattern属性，灵活配置输出属性：
%m 输出代码中指定的消息；
%M 输出打印该条日志的方法名；
%p 输出优先级，即DEBUG，INFO，WARN，ERROR，FATAL；
%r 输出自应用启动到输出该log信息耗费的毫秒数；
%c 输出所属的类目，通常就是所在类的全名；
%t 输出产生该日志事件的线程名；
%n 输出一个回车换行符，Windows平台为"rn”，Unix平台为"n”；
%d 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyyy-MM-dd HH:mm:ss,SSS}，输出类似：2002-10-18 22:10:28,921；
%l 输出日志事件的发生位置，及在代码中的行数；
```

## 第5节 Log4j Threshold属性指定输出等级
http://blog.java1234.com/blog/articles/272.html

```properties
log4j.appender.EFILE.Threshold = ERROR
```

## 第6节 Log4j Append属性指定是否追加内容
http://blog.java1234.com/blog/articles/273.html
```properties
log4j.appender.FIEL.Append = false  --覆盖
```