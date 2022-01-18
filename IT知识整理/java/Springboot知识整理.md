# Springboot开发console程序

Hztj1Application.java

```java
//引入CommandLineRunner
public class Hztj1Application implements CommandLineRunner {

    @Resource
    private HospitalThread hospitalThread;
    @Resource
    private CompanyThread companyThread;
    @Resource
    private DistrictThread districtThread;
    @Resource
    private QxSumInfoThread qxSumInfoThread;

    public static void main(String[] args) {
        SpringApplication.run(Hztj1Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        Thread thHospital = new Thread(hospitalThread);
        Thread thCompany = new Thread(companyThread);
        Thread thDistrict = new Thread(districtThread);
        Thread thQxSumInfoThread=new Thread(qxSumInfoThread);
        thHospital.start();
        thCompany.start();
        thDistrict.start();
        thQxSumInfoThread.start();

    }
```








# SpringBoot 热部署

```xml
        <!-- 修改后立即生效，热部署 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>springloaded</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
        </dependency>
```



# IDEA中设置Run Dashboard

workspace.xml

```xml
<component name="RunDashboard">
    <option name="configurationTypes">
      <set>
        <option value="SpringBootApplicationConfigurationType" />
      </set>
    </option>
    <option name="ruleStates">
      <list>
        <RuleState>
          <option name="name" value="ConfigurationTypeDashboardGroupingRule" />
        </RuleState>
        <RuleState>
          <option name="name" value="StatusDashboardGroupingRule" />
        </RuleState>
      </list>
    </option>
  </component>
————————————————
版权声明：本文为CSDN博主「chinoukin」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/chinoukin/article/details/80577890
```

# application.yml的配置

## yml设置项目起始页

```yaml
server:
  servlet:
    context-path: /index.html
```
## 自定义参数Configuration配置

```java
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MySql 属性配置文件
 * @author Admin
 */
@Component
@ConfigurationProperties(prefix = "mysql")      //important
public class MysqlProperties {

    private String jdbcName;
    private String dbUrl;
    private String userName;
    private String password;
```
## 集成jpa

pom.xml

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```



```yaml
  datasource:
    drive-class-name: com.mysql.jdbc.driver
    url: jdbc:mysql://192.168.132.25:3306/db_1024?characterEncoding=utf-8&useSSL=false
    username: root
    password: 123
  jpa:
    hibernate:
      ddl-auto: update

    show-sql: true
    database-platform: org.hibernate.dialect.MySQL5Dialect 
```



## 集成Sqlite

```yaml
spring:
  datasource:
    driver-class-name: org.sqlite.JDBC
    url: jdbc:sqlite:Demo.db
    #    username: root
    #    password: 123

    ### sqlite需要自己配置连接方式
  jpa:
    database-platform: com.sw.sql.SQLiteDialect
    hibernate:
      ddl-auto: update
      show-sql: true
```

## 集成双数据源

application.yml

```yaml
spring:  
  datasource:
    mysql1:
#      type: com.alibaba.druid.pool.DruidDataSource
      type: com.zaxxer.hikari.HikariDataSource
      driver-class-name: com.mysql.jdbc.Driver
      jdbc-url: jdbc:mysql://10.241.81.9:3306/db_ysxt?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
      username: root
      password: 123456

      hikari:
        maximum-pool-size: 20
        max-lifetime: 30000
        idle-timeout: 30000
        data-source-properties:
          prepStmtCacheSize: 250
          prepStmtCacheSqlLimit: 2048
          cachePrepStmts: true
          useServerPrepStmts: true

    mysql2: # 2.0 Datasource
#      type: com.alibaba.druid.pool.DruidDataSource
      type: com.zaxxer.hikari.HikariDataSource
      driver-class-name: com.mysql.jdbc.Driver
      jdbc-url: jdbc:mysql://10.241.81.9:3306/uap?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
      username: root
      password: 123456

      hikari:
        maximum-pool-size: 20
        max-lifetime: 30000
        idle-timeout: 30000
        data-source-properties:
          prepStmtCacheSize: 250
          prepStmtCacheSqlLimit: 2048
          cachePrepStmts: true
          useServerPrepStmts: true
```


DataSurceConfig.java


```java
package com.wondersgroup.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {



    @Primary
    @Bean(name = "mysql1")
    @ConfigurationProperties(prefix = "spring.datasource.mysql1")
    public DataSource mysql1DataSource(){

        return DataSourceBuilder.create().build();
    }


    @Bean(name = "jdbcTemplate1")
    public JdbcTemplate test1JdbcTemplate(@Qualifier("mysql1") DataSource dataSource){
        return  new JdbcTemplate(dataSource);
    }

    //    @Primary（主数据源配置）
    @Bean(name = "mysql2")
    @ConfigurationProperties(prefix = "spring.datasource.mysql2")
    public DataSource mysql2DataSource(){

        return DataSourceBuilder.create().build();
//
    }


    @Bean(name = "jdbcTemplate2")
    public JdbcTemplate test2JdbcTemplate(@Qualifier("mysql2") DataSource dataSource){
        return  new JdbcTemplate(dataSource);
    }
}

```



## SpringBoot集成freeMarker

```yaml
spring:
  freemarker:
    template-loader-path: classpath:/templates/
    cache: false
    charset: utf-8
    check-template-location: true
    content-type: text/html
    expose-request-attributes: false
    expose-session-attributes: false
    request-context-attribute: request
    suffix: .ftl
```

from: https://blog.csdn.net/whandgdh/article/details/95937078

## SpringBoot集成thymeleaf

```xml
<!-- 前端模板 thymeleaf 依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
```

```properties
# thymeleaf
spring.thymeleaf.mode=HTML5
spring.thymeleaf.suffix=.html
spring.thymeleaf.prefix=classpath:/templates/
```

————————————————
版权声明：本文为CSDN博主「水越帆」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/qq_41618510/article/details/86034046

# 一些注解

## jpa

```java
    @Query(value = "select * from tb_book order by rand() limit ?1", nativeQuery = true)
        //nativeQuery本地sql. 默认false为HQL
    List<Book> randomList(Integer n);
```



##  表单验证

| 限制                      | 说明                                                         |
| ------------------------- | ------------------------------------------------------------ |
| @Null                     | 限制只能为null                                               |
| @NotNull                  | 限制必须不为null                                             |
| @AssertFalse              | 限制必须为false                                              |
| @AssertTrue               | 限制必须为true                                               |
| @DecimalMax(value)        | 限制必须为一个不大于指定值的数字                             |
| @DecimalMin(value)        | 限制必须为一个不小于指定值的数字                             |
| @Digits(integer,fraction) | 限制必须为一个小数，且整数部分的位数不能超过integer，小数部分的位数不能超过fraction |
| @Future                   | 限制必须是一个将来的日期                                     |
| @Max(value)               | 限制必须为一个不大于指定值的数字                             |
| @Min(value)               | 限制必须为一个不小于指定值的数字                             |
| @Past                     | 限制必须是一个过去的日期                                     |
| @Pattern(value)           | 限制必须符合指定的正则表达式                                 |
| @Size(max,min)            | 限制字符长度必须在min到max之间                               |
| @Past                     | 验证注解的元素值（日期类型）比当前时间早                     |
| @NotEmpty                 | 验证注解的元素值不为null且不为空（字符串长度不为0、集合大小不为0） |
| @NotBlank                 | 验证注解的元素值不为空（不为null、去除首位空格后长度为0），不同于@NotEmpty，@NotBlank只应用于字符串且在比较时会去除字符串的空格 |
| @Email                    | 验证注解的元素值是Email，也可以通过正则表达式和flag指定自定义的email格式 |

例：

Student.java

```java
package com.sw.bo;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity	
@Table(name = "tb_student")
public class Student {


    @Id
    @GeneratedValue
    private Integer Id;

    @NotEmpty(message = "姓名不能为空")
    @Column(length = 50)
    private String name;

    @NotNull(message = "年龄不能为空")
    @Min(value = 18,message = "年龄必须大于18岁")
    private Integer age;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

```

StudentController.java

```java
package com.sw.Controller;

import com.sw.bo.Student;
import com.sw.service.StudentService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    @RequestMapping("/add")
    public String add(@Valid Student student, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return bindingResult.getFieldError().getDefaultMessage();	//关键语句
        }else{
            studentService.add(student);
            return "添加成功";
        }
    }
}
```



## 切面AOP

```java
@Aspect		//注解是切面注解类
@Pointcut	//切点定义
@Before		//是方法执行前调用
@After是		//方法执行后调用
@AfterReturning	//方法执行返回值调用
```

例：

RequestAspect.java

```java
package com.sw.aspect;


import com.sw.bo.Student;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Aspect
@Component
public class RequestAspect {

    private Logger logger = Logger.getLogger(RequestAspect.class);

    @Pointcut("execution(public * com.sw.controller.*.*(..))")	//任意出参的任意类的任意方法，任意入参
    public void log(){

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes sra= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=sra.getRequest();
        logger.info("url: " + request.getRequestURI());
        logger.info("ip: " + request.getRemoteHost());
        logger.info("method: " + request.getMethod());
        logger.info("class_method: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());		//.getDeclaringTypeName获取类名， getSignature获取方法名
        logger.info("args: "+joinPoint.getArgs());	//获取参数
        Student student= (Student) joinPoint.getArgs()[0];
        System.out.println(student);

        logger.info("方法执行前");

    }

    @After("log()")
    public void doAfter(JoinPoint joinPoint){
        logger.info("方法执行后");

    }

    @AfterReturning(returning = "object",pointcut = "log()")
    public void doAfterReturn(Object object){
        logger.info("方法执行后"+object);

    }

}
```



# 报错

## Cannot determine embedded database driver class for database type NONE

from: https://blog.csdn.net/Master_Shifu_/article/details/80420099

```xml
<dependency>
        <groupId>com.h2database</groupId> 
        <artifactId>h2</artifactId>
        <scope>runtime</scope> 
</dependency>
```
## 关于热部署Devtools出现同一个类型进行类型转换失败的问题

from： https://www.cnblogs.com/biaogejiushibiao/p/10135850.html

报错截图：

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/1399348-20181218103726680-1151961593-16424001299711.png)

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/1399348-20181218104204707-213913021-16424001477715.png)

https://www.cnblogs.com/biaogejiushibiao/p/10135850.html#_labelTop)

**解决方法：**

1.如果不是必须使用Devtools的热部署，可以将相关依赖去掉即可

2.如果必须使用热部署，可以将要强制类型转换的对象先转换成json格式在进行转换即可

![img](https://gitee.com/jstone001/booknote/raw/master/jpgBed/1399348-20181218104606058-264264867-16424001770949.png)

3.参考官方文档进行配置：

在resources目录下面创建META_INF文件夹，然后创建spring-devtools.properties文件，文件加上类似下面的配置：
restart.exclude.companycommonlibs=/mycorp-common-[\w-]+.jar
restart.include.projectcommon=/mycorp-myproj-[\w-]+.jar

**原因：**

为了实现热部署，Devtools原有自己的类加载器，进行更新，由于类加载器的不同导致类型转换失败

## application.yml文件不起作用

from: https://blog.csdn.net/czjlghndcy/article/details/109552528

```xml
pom.xml去掉 <packaging>pom</packaging>
```

