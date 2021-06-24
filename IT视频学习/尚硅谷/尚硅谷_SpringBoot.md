from: https://www.bilibili.com/video/BV1iK411A7JX



# Springboot 简介和安装

## P01 尚硅谷_SpringBoot_入门-课程简介

## P02 尚硅谷_SpringBoot_入门-Spring Boot简介
## P03 尚硅谷_SpringBoot_入门-微服务简介
## P04 尚硅谷_SpringBoot_入门-环境准备
## P05 尚硅谷_SpringBoot_入门-springboot-helloworld
## P06 尚硅谷_SpringBoot_入门-HelloWorld细节-场景启动器（starter）
## P07 尚硅谷_SpringBoot_入门-HelloWorld细节-自动配置
## P08 尚硅谷_SpringBoot_入门-使用向导快速创建Spring Boot应用

# Yaml

## P09 尚硅谷_SpringBoot_配置-yaml简介
## P10 尚硅谷_SpringBoot_配置-yaml语法
## P11 尚硅谷_SpringBoot_配置-yaml配置文件值获取
## P12 尚硅谷_SpringBoot_配置-properties配置文件编码问题
## P13 尚硅谷_SpringBoot_配置-@ConfigurationProperties与@Value区别
## P14 尚硅谷_SpringBoot_配置-@PropertySource @ImportResource @Bean
## P15 尚硅谷_SpringBoot_配置-配置文件占位符
## P16 尚硅谷_SpringBoot_配置-Profile多环境支持
## P17 尚硅谷_SpringBoot_配置-配置文件的加载位置
## P18 尚硅谷_SpringBoot_配置-外部配置加载顺序
## P19 尚硅谷_SpringBoot_配置-自动配置原理
## P20 尚硅谷_SpringBoot_配置-@Conditional&自动配置报告
## P21 尚硅谷_SpringBoot_日志-日志框架分类和选择
## P22 尚硅谷_SpringBoot_日志-slf4j使用原理
## P23 尚硅谷_SpringBoot_日志-其他日志框架统一转换为slf4j
## P24 尚硅谷_SpringBoot_日志-SpringBoot日志关系
## P25 尚硅谷_SpringBoot_日志-SpringBoot默认配置
## P26 尚硅谷_SpringBoot_日志-指定日志文件和日志Profile功能
## P27 尚硅谷_SpringBoot_日志-切换日志框架
## P28 尚硅谷_SpringBoot_web开发-简介
## P29 尚硅谷_SpringBoot_web开发-webjars&静态资源映射规则

# Thymeleaf

## P30 尚硅谷_SpringBoot_web开发-引入thymeleaf

### 模板引擎：

JSP，Velocity，Freemarker，Thymeleaf

### Thymeleaf：（语法更简单，功能更强大）

#### 引入thymeleaf

```xml
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<properties>
    <thymeleaf.version>3.0.2.RELEASE</thymeleaf.version>
    <!-- 布局功能的支持程序   thymeleaf3主程序   layout2以上版本  -->
    <!-- thymeleaf2主程序   layout1以上版本  -->
    <thymeleaf-layout-dialect.version>2.1.1</thymeleaf-layout-dialect.version>
</properties>
```

## P31 尚硅谷_SpringBoot_web开发-thymeleaf语法

```java
@ConfigurationProperties(prefix ="spring.thymeleaf")
// 只要把html页面放在classpath:/templates/, thymeleaf就能自动渲染 
public class ThymeleafProperties{
	private static final Charset DEFAULT_ENCODING=Charset.forName("UTF-8");
	private static final MimeType DEFAULT_CONTENT_TYPE=MimeType.valueOf("text/html");
	private static final DEFAULT_PREFX ="classpath:/templates/";
	private static final DEFAULT_SUFFIX =".html";
}
```

www.thymeleaf.org

###  语法规则

导入thymeleaf的名称空间

```html
<html lang='en' xmlns:th="http://www.thymeleaf.org">
```

#### 1. th语法

10 Atribute Precedence

```html
<div th:text="${hello}"></div>  <!-- 获取文本值 -->
< th:"${divId}" th:class="${myDiv}" >
```

th:text    #改变当前元素 

![image-20210609100934709](E:\JS\booknote\jpgBed\image-20210609100934709.png)

2. #### 5种表达式

4 Expression Syntax

```yaml
Simple expressions:（表达式语法）
    Variable Expressions: ${...}：获取变量值；OGNL；
    		1）、获取对象的属性、调用方法
    		2）、使用内置的基本对象：
    		   #ctx : the context object.
    		   #vars: the context variables.
                #locale : the context locale.
                #request : (only in Web Contexts) the HttpServletRequest object.
                #response : (only in Web Contexts) the HttpServletResponse object.
                #session : (only in Web Contexts) the HttpSession object.
                #servletContext : (only in Web Contexts) the ServletContext object.
                
                ${session.foo}
            3）、内置的一些工具对象：
            #execInfo : information about the template being processed.
            #messages : methods for obtaining externalized messages inside variables expressions, in the same way as they would be obtained using #{…} syntax.
            #uris : methods for escaping parts of URLs/URIs
            #conversions : methods for executing the configured conversion service (if any).
            #dates : methods for java.util.Date objects: formatting, component extraction, etc.
            #calendars : analogous to #dates , but for java.util.Calendar objects.
            #numbers : methods for formatting numeric objects.
            #strings : methods for String objects: contains, startsWith, prepending/appending, etc.
            #objects : methods for objects in general.
            #bools : methods for boolean evaluation.
            #arrays : methods for arrays.
            #lists : methods for lists.
            #sets : methods for sets.
            #maps : methods for maps.
            #aggregates : methods for creating aggregates on arrays or collections.
            #ids : methods for dealing with id attributes that might be repeated (for example, as a result of an iteration).

    Selection Variable Expressions: *{...}：选择表达式：和${}在功能上是一样；
    	补充：配合 th:object="${session.user}：
    Message Expressions: #{...}：获取国际化内容
    Link URL Expressions: @{...}： # 定义URL；
    		@{/order/process(execId=${execId},execType='FAST')}
    Fragment Expressions: ~{...}：  # 片段引用表达式
    		<div th:insert="~{commons :: main}">...</div>
    		
Literals（字面量）
      Text literals: 'one text' , 'Another one!' ,…
      Number literals: 0 , 34 , 3.0 , 12.3 ,…
      Boolean literals: true , false
      Null literal: null
      Literal tokens: one , sometext , main ,…
Text operations:（文本操作）
    String concatenation: +
    Literal substitutions: |The name is ${name}|
Arithmetic operations:（数学运算）
    Binary operators: + , - , * , / , %
    Minus sign (unary operator): -
Boolean operations:（布尔运算）
    Binary operators: and , or
    Boolean negation (unary operator): ! , not
Comparisons and equality:（比较运算）
    Comparators: > , < , >= , <= ( gt , lt , ge , le )
    Equality operators: == , != ( eq , ne )
Conditional operators:条件运算（三元运算符）
    If-then: (if) ? (then)
    If-then-else: (if) ? (then) : (else)
    Default: (value) ?: (defaultvalue)
Special tokens:
    No-Operation: _ 
```



## P32 尚硅谷_SpringBoot_web开发-SpringMVC自动配置原理

## P33 尚硅谷_SpringBoot_web开发-扩展与全面接管SpringMVC
## P34 尚硅谷_SpringBoot_web开发-【实验】-引入资源
## P35 尚硅谷_SpringBoot_web开发-【实验】-国际化
## P36 尚硅谷_SpringBoot_web开发-【实验】-登陆&拦截器
## P37 尚硅谷_SpringBoot_web开发-【实验】-Restful实验要求
## P38 尚硅谷_SpringBoot_web开发-【实验】-员工列表-公共页抽取
## P39 尚硅谷_SpringBoot_web开发-【实验】-员工列表-链接高亮&列表完成
## P40 尚硅谷_SpringBoot_web开发-【实验】-员工添加-来到添加页面
## P41 尚硅谷_SpringBoot_web开发-【实验】-员工添加-添加完成
## P42 尚硅谷_SpringBoot_web开发-【实验】-员工修改-重用页面&修改完成
## P43 尚硅谷_SpringBoot_web开发-【实验】-员工删除-删除完成
## P44 尚硅谷_SpringBoot_web开发-错误处理原理&定制错误页面
## P45 尚硅谷_SpringBoot_web开发-定制错误数据
## P46 尚硅谷_SpringBoot_web开发-嵌入式Servlet容器配置修改
## P47 尚硅谷_SpringBoot_web开发-注册servlet三大组件
## P48 尚硅谷_SpringBoot_web开发-切换其他嵌入式Servlet容器
## P49 尚硅谷_SpringBoot_web开发-嵌入式Servlet容器自动配置原理
## P50 尚硅谷_SpringBoot_web开发-嵌入式Servlet容器启动原理
## P51 尚硅谷_SpringBoot_web开发-使用外部Servlet容器&JSP支持
## P52 尚硅谷_SpringBoot_web开发-外部Servlet容器启动SpringBoot应用原理
## P53 尚硅谷_SpringBoot_Docker-简介
## P54 尚硅谷_SpringBoot_Docker-核心概念
## P55 尚硅谷_SpringBoot_Docker-linux环境准备
## P56 尚硅谷_SpringBoot_Docker-docker安装&启动&停止
## P57 尚硅谷_SpringBoot_Docker-docker镜像操作常用命令
## P58 尚硅谷_SpringBoot_Docker-docker容器操作常用命令
## P59 尚硅谷_SpringBoot_Docker-docker安装MySQL
## P60 尚硅谷_SpringBoot_数据访问-简介
## P61 尚硅谷_SpringBoot_数据访问-JDBC&自动配置原理
## P62 尚硅谷_SpringBoot_数据访问-整合Druid&配置数据源监控
## P63 尚硅谷_SpringBoot_数据访问-整合MyBatis（一）-基础环境搭建
## P64 尚硅谷_SpringBoot_数据访问-整合MyBatis（二）-注解版MyBatis
## P65 尚硅谷_SpringBoot_数据访问-整合MyBatis（二）-配置版MyBatis
## P66 尚硅谷_SpringBoot_数据访问-SpringData JPA简介
## P67 尚硅谷_SpringBoot_数据访问-整合JPA
## P68 尚硅谷_SpringBoot_原理-第一步：创建SpringApplication
## P69 尚硅谷_SpringBoot_原理-第二步：启动应用
## P70 尚硅谷_SpringBoot_原理-事件监听机制相关测试
## P71 尚硅谷_SpringBoot_原理-自定义starter
## P72 尚硅谷_SpringBoot_结束语
## P73 缓存-JSR107简介
## P74 缓存-Spring缓存抽象简介
## P75 缓存-基本环境搭建
## P76 缓存-@Cacheable初体验
## P77 缓存-缓存工作原理&@Cacheable运行流程
## P78 缓存-@Cacheable其他属性
## P79 缓存-@CachePut
## P80 缓存-@CacheEvict
## P81 缓存-@Caching&@CacheConfig
## P82 缓存-搭建redis环境&测试
## P83 缓存-RedisTemplate&序列化机制
## P84 缓存-自定义CacheManager
## P85 消息-JMS&AMQP简介
## P86 消息-RabbitMQ基本概念简介
## P87 消息-RabbitMQ运行机制
## P88 消息-RabbitMQ安装测试
## P89 消息-RabbitTemplate发送接受消息&序列化机制
## P90 消息-@RabbitListener&@EnableRabbit
## P91 消息-AmqpAdmin管理组件的使用
## P92 检索-Elasticsearch简介&安装
## P93 检索-Elasticsearch快速入门
## P94 检索-SpringBoot整合Jest操作ES
## P95 检索-整合SpringDataElasticsearch
## P96 任务-异步任务
## P97 任务-定时任务
## P98 任务-邮件任务
## P99 安全-测试环境搭建
## P100 安全-登录&认证&授权
## P101 安全-权限控制&注销
## P102 安全-记住我&定制登陆页
## P103 分布式-dubbo简介
## P104 分布式-docker安装zookeeper
## P105 分布式-SpringBoot Dubbo Zookeeper整合
## P106 分布式-SpringCloud-Eureka注册中心
## P107 分布式-服务注册
## P108 分布式-服务发现&消费
## P109 热部署-devtools开发热部署
## P110 监管-监管端点测试
## P111 监管-定制端点
## P112 监管-自定义HealthIndicator