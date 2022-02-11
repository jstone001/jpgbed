

# Springboot 一般开发

## Springboot 使用jdk1.6，并打成war包

- springboot 版本1.5.3.RELEASE
- maven 最高maven3.2.5
- tomcat 最高tomcat7
  不然jdk1.6不支持

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>SpringBoot</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>war</packaging>

    <name>springbootWarDemo</name>
    <description>Demo project for Spring Boot</description>


    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.3.RELEASE</version>
        <relativePath /> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>

        <!-- TO Support JDK 1.6 start -->
        <java.version>1.6</java.version>
        <tomcat.version>7.0.59</tomcat.version>
        <!-- TO Support JDK 1.6 end -->
        <maven.compiler.source>6</maven.compiler.source>
        <maven.compiler.target>6</maven.compiler.target>
    </properties>

    <dependencies>

        <dependency>
            <groupId>com.oracle</groupId>
            <artifactId>ojdbc6</artifactId>
            <version>11.2.0.1.0</version>
        </dependency>


        <!-- https://mvnrepository.com/artifact/org.apache.commons/commons-lang3 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.7</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- TO Support JDK 1.6 start -->
        <dependency>
            <groupId>org.apache.tomcat</groupId>
            <artifactId>tomcat-juli</artifactId>
            <version>${tomcat.version}</version>
        </dependency>
        <!-- TO Support JDK 1.6 end -->

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>commons-lang</groupId>
            <artifactId>commons-lang</artifactId>
            <version>2.3</version>
        </dependency>
        <dependency>
            <groupId>commons-beanutils</groupId>
            <artifactId>commons-beanutils</artifactId>
            <version>1.7.0</version>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.14</version>
            <optional>true</optional>
            <scope>runtime</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.79</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/net.sf.json-lib/json-lib -->
        <dependency>
            <groupId>net.sf.json-lib</groupId>
            <artifactId>json-lib</artifactId>
            <version>2.4</version>
            <classifier>jdk15</classifier>
        </dependency>
    </dependencies>

    <!-- Package as an executable jar -->
<!--    <build>-->
<!--        <plugins>-->
<!--            <plugin>-->
<!--                <groupId>org.springframework.boot</groupId>-->
<!--                <artifactId>spring-boot-maven-plugin</artifactId>-->
<!--                <configuration>-->
<!--                    <source>1.6</source>-->
<!--                    <target>1.6</target>-->
<!--                </configuration>-->
<!--            </plugin>-->
<!--        </plugins>-->
<!--    </build>-->

</project>

```

SpringBootWarDemoApplication.java

```java
package com.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringbootWarDemoApplication extends SpringBootServletInitializer {

	private static ConfigurableApplicationContext context;

	@Override protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SpringbootWarDemoApplication.class);
	}

	public static void main(String[] args) {

		context = SpringApplication.run(SpringbootWarDemoApplication.class, args);

	}
}
```




## 包扫描要注意的问题

```java
// 如果扫描了不是Application下的包，注意要复写下，自己本包下的路径
@SpringBootApplication(scanBasePackages ={"com.wondersgroup.yss.common", "com.wondersgroup.yss.xxljob"})  //xxljob 本包下的路径
```



## SpringBoot 的Test类

```java
@RunWith(SpringRunner.class)
//@SpringBootTest(classes = GeneratorApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(classes = GeneratorApp.class)
public class UserTest {
}
————————————————
版权声明：本文为CSDN博主「灰太狼_cxh」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/weixin_39220472/article/details/87714756
```



##  @responseBody 中文乱码解决方法

from: https://blog.csdn.net/syslbjjly/article/details/88952269

## 显示Spring Boot加载的所有bean

from: https://blog.csdn.net/cyan20115/article/details/106549716

1. CommandLineRunner作为界面

```java
package com.mkyong;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
 
import java.util.Arrays;
 
@SpringBootApplication
public class Application implements CommandLineRunner {
 
    @Autowired
    private ApplicationContext appContext;
 
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
 
    @Override
    public void run(String... args) throws Exception {
 
        String[] beans = appContext.getBeanDefinitionNames();
        Arrays.sort(beans);
        for (String bean : beans) {
            System.out.println(bean);
        }
 
    }
 
}
```

2. CommandLineRunner作为Bean 

```java
    package com.mkyong;
     
    import org.springframework.boot.CommandLineRunner;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.annotation.Bean;
     
    import java.util.Arrays;
     
    @SpringBootApplication
    public class Application {
     
        public static void main(String[] args) throws Exception {
            SpringApplication.run(Application.class, args);
        }
     
        @Bean
        public CommandLineRunner run(ApplicationContext appContext) {
            return args -> {
     
                String[] beans = appContext.getBeanDefinitionNames();
                Arrays.stream(beans).sorted().forEach(System.out::println);
     
            };
        }
     
    }
```



## Springboot开发console程序

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












## IDEA中设置Run Dashboard

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



# application.yml 的常用配置

## yml设置项目名称

```yaml
server:
  servlet:
    context-path: /ysxt-ws	# 地址上配项目名
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
## SpringBoot 热部署

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

# Spring集成其他组件

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

查询

```java
    @Query(value = "select * from tb_book order by rand() limit ?1", nativeQuery = true)
        //nativeQuery本地sql. 默认false为HQL
    List<Book> randomList(Integer n);
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

## 集成logback

logback.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 日志级别从低到高分为TRACE < DEBUG < INFO < WARN < ERROR < FATAL，如果设置为WARN，则低于WARN的信息都不会输出 -->
<!-- scan:当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true -->
<!-- scanPeriod:设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒。当scan为true时，此属性生效。默认的时间间隔为1分钟。 -->
<!-- debug:当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。 -->
<configuration scan="true" scanPeriod="10 seconds">
    <!--<include resource="org/springframework/boot/logging/logback/base.xml"
        /> -->
    <contextName>Logback For CxfDemo</contextName>
    <!-- name的值是变量的名称，value的值时变量定义的值。通过定义的值会被插入到logger上下文中。定义变量后，可以使“${}”来使用变量。 -->
    <!--    <property name="log.path" value="D:/Test/log/logback-test/log"/>-->
    <!-- 定义日志文件 输入位置 -->
    <!--    <property name="logDir" value="D:/Test/log/logback-test/log"/>-->
    <!-- 日志最大的历史 30天 -->
    <property name="maxHistory" value="30"/>


    <!-- 控制台输出日志 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS, Asia/Shanghai} [%thread] %-5level %logger : %msg%n</pattern>
            <charset class="java.nio.charset.Charset">UTF-8</charset>
        </encoder>
    </appender>


    <!--&lt;!&ndash; ERROR级别日志 &ndash;&gt;
    <appender name="ERROR"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy
                class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logDir}\%d{yyyyMMdd}\error.log</fileNamePattern>
            <maxHistory>${maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger-%msg%n</pattern>
            <charset class="java.nio.charset.Charset">UTF-8</charset>
        </encoder>
        <append>false</append>
        <prudent>false</prudent>
    </appender>

    &lt;!&ndash; WARN级别日志 &ndash;&gt;
    <appender name="WARN"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>WARN</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy
                class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logDir}\%d{yyyyMMdd}\warn.log</fileNamePattern>
            <maxHistory>${maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger-%msg%n</pattern>
            <charset class="java.nio.charset.Charset">UTF-8</charset>
        </encoder>
        <append>false</append>
        <prudent>false</prudent>
    </appender>

    &lt;!&ndash; INFO级别日志 &ndash;&gt;
    <appender name="INFO"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy
                class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logDir}\%d{yyyyMMdd}\info.log</fileNamePattern>
            <maxHistory>${maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger-%msg%n</pattern>
            <charset class="java.nio.charset.Charset">UTF-8</charset>
        </encoder>
        <append>false</append>
        <prudent>false</prudent>
    </appender>

    &lt;!&ndash; DEBUG级别日志 &ndash;&gt;
    <appender name="DEBUG"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>DEBUG</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy
                class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${logDir}\%d{yyyyMMdd}\debug.log</fileNamePattern>
            <maxHistory>${maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger-%msg%n</pattern>
            <charset class="java.nio.charset.Charset">UTF-8</charset>
        </encoder>
        <append>false</append>
        <prudent>false</prudent>
    </appender>-->

    <appender name="LOG"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy
                class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>log/%d{yyyyMMdd}\%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>${maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS, Asia/Shanghai} [%thread] %-5level %logger : %msg%n</pattern>
            <charset class="java.nio.charset.Charset">UTF-8</charset>
        </encoder>
        <append>false</append>
        <prudent>false</prudent>
    </appender>

    <!--文件日志， 按照每天生成日志文件 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>log/%d{yyyyMMdd}/boss.%d{yyyy-MM-dd}.log</FileNamePattern>
            <!--日志文件保留天数-->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS, Asia/Shanghai} [%thread] %-5level %logger{50} : %msg%n</pattern>
        </encoder>
        <!--日志文件最大的大小-->
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>10MB</MaxFileSize>
        </triggeringPolicy>
    </appender>

    <!-- 异步输出 -->
    <appender name="dayLogAsyncAppender" class="ch.qos.logback.classic.AsyncAppender">
        <includeCallerData>true</includeCallerData>
        <!-- 不丢失日志.默认的,如果队列的80%已满,则会丢弃TRACT、DEBUG、INFO级别的日志 -->
        <discardingThreshold>0</discardingThreshold>
        <!-- 更改默认的队列的深度,该值会影响性能.默认值为256 -->
        <queueSize>512</queueSize>
        <appender-ref ref="FILE"/>
    </appender>

    <!--专为 spring 定制
     -->
    <logger name="org.springframework" level="info"/>
    <!-- show parameters for hibernate sql 专为 Hibernate 定制 -->
    <logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE"/>
    <logger name="org.hibernate.type.descriptor.sql.BasicExtractor" level="DEBUG"/>
    <logger name="org.hibernate.SQL" level="DEBUG"/>
    <logger name="org.hibernate.engine.QueryParameters" level="DEBUG"/>
    <logger name="org.hibernate.engine.query.HQLQueryPlan" level="DEBUG"/>

    <!--myibatis log configure-->
    <!--
    <logger name="com.apache.ibatis" level="TRACE"/>
    <logger name="java.sql.Connection" level="DEBUG"/>
    <logger name="java.sql.Statement" level="DEBUG"/>
    <logger name="java.sql.PreparedStatement" level="DEBUG"/>
     -->
    <!-- root级别 INFO -->
    <root level="INFO">
        <!-- 控制台输出 -->
        <appender-ref ref="STDOUT"/>
        <!-- 文件输出 -->
        <appender-ref ref="LOG"/>
    </root>
</configuration>
```

## 集成redis

```xml
        <!-- redis依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
```

### list序列化成String

from: java1234

RedisConfig.java

```java
package com.wondersgroup.yss.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Redis配置类
 *
 * @author Administrator
 */
@Configuration
public class RedisConfig {

    @Resource
    private RedisConnectionFactory factory;


    @Bean(name = "redisTemplate1")
    public RedisTemplate<String, Object> redisTemplate1() {
        // 连接池配置
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);

        //切换库d
        JedisConnectionFactory connectionFactory = (JedisConnectionFactory) template.getConnectionFactory();
        connectionFactory.setDatabase(3);

        //序列化类
        MyRedisSerializer myRedisSerializer = new MyRedisSerializer();		//新写序列化，关键代码
        //key序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        //value序列化
        template.setValueSerializer(myRedisSerializer);
        //value hashmap序列化
        template.setHashValueSerializer(myRedisSerializer);
        return template;

    }


    @Bean(name = "redisTemplate2")
    public RedisTemplate<String, Object> redisTemplate2() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        //切换库d
        JedisConnectionFactory connectionFactory = (JedisConnectionFactory) template.getConnectionFactory();
        connectionFactory.setDatabase(3);

        //序列化类
        MyRedisSerializer myRedisSerializer = new MyRedisSerializer();
        //key序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        //value序列化
        template.setValueSerializer(new StringRedisSerializer());

        /*hash序列化方法*/
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        return template;
    }

    static class MyRedisSerializer implements RedisSerializer<Object> {

        @Override
        public byte[] serialize(Object o) throws SerializationException {
            return serializeObj(o);
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            return deserializeObj(bytes);
        }

        /**
         * 序列化
         *
         * @param object
         * @return
         */
        private static byte[] serializeObj(Object object) {
            ObjectOutputStream oos = null;
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                byte[] bytes = baos.toByteArray();
                return bytes;
            } catch (Exception e) {
                throw new RuntimeException("序列化失败!", e);
            }
        }

        /**
         * 反序列化
         *
         * @param bytes
         * @return
         */
        private static Object deserializeObj(byte[] bytes) {
            if (bytes == null) {
                return null;
            }
            ByteArrayInputStream bais = null;
            try {
                bais = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (Exception e) {
                throw new RuntimeException("反序列化失败!", e);
            }
        }

    }
}
```

redisUtil.java

```java
package com.wondersgroup.yss.common.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * @author Administrator
 *
 */
@Component
public class RedisUtil {

	@Resource(name="redisTemplate1")
	private RedisTemplate<String, Object> redisTemplate;

	@Resource(name="redisTemplate2")
	private RedisTemplate<String, Object> redisTemplate2;


	//=============================common============================
	/**
	 * 指定缓存失效时间
	 * @param key 键
	 * @param time 时间(秒)
	 * @return
	 */
	public boolean expire(String key,long time){
		try {
			if(time>0){
				redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean expire2(String key,long time){
		try {
			if(time>0){
				redisTemplate2.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 指定缓存失效时间
	 * @param prefix 前缀
	 * @param key 键
	 * @param time 时间(秒)
	 * @return
	 */
	public boolean expire(String prefix,String key,long time){
		try {
			if(time>0){
				redisTemplate.expire(prefix+key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据key 获取过期时间
	 * @param key 键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public long getExpire(String key){
		return redisTemplate.getExpire(key,TimeUnit.SECONDS);
	}

	/**
	 * 判断key是否存在
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public boolean hasKey(String key){
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除缓存
	 * @param key 可以传一个值 或多个
	 */
	@SuppressWarnings("unchecked")
	public void del(String ... key){
		if(key!=null&&key.length>0){
			if(key.length==1){
				redisTemplate.delete(key[0]);
			}else{
				redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
			}
		}
	}

	//============================String=============================
	/**
	 * 普通缓存获取
	 * @param key 键
	 * @return 值
	 */
	public Object get(String key){
		return key==null?null:redisTemplate.opsForValue().get(key);
	}

	/**
	 * 普通缓存获取
	 * @param prefix 前缀
	 * @param key 键
	 * @return 值
	 */
	public Object get(String prefix,String key){
		return key==null?null:redisTemplate.opsForValue().get(prefix+key);
	}

	/**
	 * 普通缓存放入
	 * @param key 键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public boolean set(String key,Object value) {
		try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 普通缓存放入(带前缀)
	 * @param prefix 前缀
	 * @param key 键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public boolean set(String prefix,String key,Object value) {
		try {
			redisTemplate.opsForValue().set(prefix+key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}


	/**
	 * 普通缓存放入(带前缀)
	 * @param prefix 前缀
	 * @param key 键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public boolean set2(String prefix,String key,Object value) {
		try {
			redisTemplate2.opsForValue().set(prefix+key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 普通缓存放入并设置时间
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public boolean set(String key,Object value,long time){
		try {
			if(time>0){
				redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			}else{
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 普通缓存放入并设置时间
	 * @param prefix 前缀
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public boolean set(String prefix,String key,Object value,long time){
		try {
			if(time>0){
				redisTemplate.opsForValue().set(prefix+key, value, time, TimeUnit.SECONDS);
			}else{
				set(prefix+key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 递增
	 * @param key 键
	 * @param  delta (要增加几大于0)
	 * @return
	 */
	public long incr(String key, long delta){
		if(delta<0){
			throw new RuntimeException("递增因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 递减
	 * @param key 键
	 * @param delta  (要减少几小于0)
	 * @return
	 */
	public long decr2(String key, long delta){
		if(delta<0){
			throw new RuntimeException("递减因子必须大于0");
		}
		return redisTemplate2.opsForValue().increment(key, -delta);
	}

	/**
	 * 递减
	 * @param key 键
	 * @param  delta (要减少几小于0)
	 * @return
	 */
	public long decr2(String prefix,String key, long delta){
		if(delta<0){
			throw new RuntimeException("递减因子必须大于0");
		}
		return redisTemplate2.opsForValue().increment(prefix+key, -delta);
	}

	//================================Map=================================
	/**
	 * HashGet
	 * @param key 键 不能为null
	 * @param item 项 不能为null
	 * @return 值
	 */
	public Object hget2(String key,String item){
		return redisTemplate2.opsForHash().get(key, item);
	}

	/**
	 * 获取hashKey对应的所有键值
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public Map<Object,Object> hmget2(String key){
		return redisTemplate2.opsForHash().entries(key);
	}

	/**
	 * HashSet
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 */
	public boolean hmset2(String key, Map<String,Object> map){
		try {
			redisTemplate2.opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * HashSet 并设置时间
	 * @param key 键
	 * @param map 对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 */
	public boolean hmset2(String key, Map<String,Object> map, long time){
		try {
			redisTemplate2.opsForHash().putAll(key, map);
			if(time>0){
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 * @return true 成功 false失败
	 */
	public boolean hset2(String key,String item,Object value) {
		try {
			redisTemplate2.opsForHash().put(key, item, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 */
	public boolean hset2(String key,String item,Object value,long time) {
		try {
			redisTemplate2.opsForHash().put(key, item, value);
			if(time>0){
				expire2(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除hash表中的值
	 * @param key 键 不能为null
	 * @param item 项 可以使多个 不能为null
	 */
	public void hdel2(String key, Object... item){
		redisTemplate2.opsForHash().delete(key,item);
	}

	/**
	 * 判断hash表中是否有该项的值
	 * @param key 键 不能为null
	 * @param item 项 不能为null
	 * @return true 存在 false不存在
	 */
	public boolean hHasKey2(String key, String item){
		return redisTemplate2.opsForHash().hasKey(key, item);
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 * @param key 键
	 * @param item 项
	 * @param by 要增加几(大于0)
	 * @return
	 */
	public double hincr2(String key, String item,double by){
		return redisTemplate2.opsForHash().increment(key, item, by);
	}

	public long hincr2(String key, String item,long by){
		return redisTemplate2.opsForHash().increment(key, item, by);
	}

	/**
	 * hash递减
	 * @param key 键
	 * @param item 项
	 * @param by 要减少记(小于0)
	 * @return
	 */
	public double hdecr2(String key, String item,double by){
		return redisTemplate2.opsForHash().increment(key, item,-by);
	}

	public long hdecr2(String key, String item,long by){
		return redisTemplate2.opsForHash().increment(key, item,-by);
	}

	//============================set=============================
	/**
	 * 根据key获取Set中的所有值
	 * @param key 键
	 * @return
	 */
	public Set<Object> sGet(String key){
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 * @param key 键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public boolean sHasKey(String key,Object value){
		try {
			return redisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将数据放入set缓存
	 * @param key 键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSet(String key, Object...values) {
		try {
			return redisTemplate.opsForSet().add(key, values);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 将set数据放入缓存
	 * @param key 键
	 * @param time 时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSetAndTime(String key,long time,Object...values) {
		try {
			Long count = redisTemplate.opsForSet().add(key, values);
			if(time>0) {
				expire(key, time);
			}
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取set缓存的长度
	 * @param key 键
	 * @return
	 */
	public long sGetSetSize(String key){
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 移除值为value的
	 * @param key 键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public long setRemove(String key, Object ...values) {
		try {
			Long count = redisTemplate.opsForSet().remove(key, values);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	//===============================list=================================

	/**
	 * 获取list缓存的内容
	 * @param key 键
	 * @param start 开始
	 * @param end 结束  0 到 -1代表所有值
	 * @return
	 */
	public List<?> lGet(String key,long start, long end){
		try {
			return redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取list缓存的长度
	 * @param key 键
	 * @return
	 */
	public long lGetListSize(String key){
		try {
			return redisTemplate.opsForList().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 通过索引 获取list中的值
	 * @param key 键
	 * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return
	 */
	public Object lGetIndex(String key,long index){
		try {
			return redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public boolean lSet(String key, Object value) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return
	 */
	public boolean lSet(String key, Object value, long time) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public boolean lSet(String key, List<Object> value) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return
	 */
	public boolean lSet(String key, List<Object> value, long time) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据索引修改list中的某条数据
	 * @param key 键
	 * @param index 索引
	 * @param value 值
	 * @return
	 */
	public boolean lUpdateIndex(String key, long index,Object value) {
		try {
			redisTemplate.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 移除N个值为value
	 * @param key 键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 */
	public long lRemove(String key,long count,Object value) {
		try {
			Long remove = redisTemplate.opsForList().remove(key, count, value);
			return remove;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public boolean lSetList(String key, List<?> value, long time) {
		try {
			redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
			redisTemplate.opsForList().rightPushAll(key, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
```



### 与RedisCacheManager整合

from: https://rstyro.github.io/blog/2019/04/16/SpringBoot%E4%B8%8ERedisCacheManager%E6%95%B4%E5%90%88/

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-cache</artifactId>
</dependency>

<!-- redis -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-pool2</artifactId>
</dependency>
```

RedisConfig.java

```java
package com.example.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;

/**
 *
 * @author rstyro
 * @time 2018-07-31
 *
 */
@Configuration
@EnableCaching // 开启缓存支持
public class RedisConfig extends CachingConfigurerSupport {
    @Resource
    private LettuceConnectionFactory lettuceConnectionFactory;


    /**
     * 配置CacheManager
     * @return
     */
    @Override
    @Bean
    public CacheManager cacheManager() {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ZERO)
                .entryTtl(Duration.ofSeconds(20))   //设置缓存失效时间
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();

        RedisCacheManager cacheManager = RedisCacheManager.builder(lettuceConnectionFactory)
                .cacheDefaults(config)
                .build();
        return cacheManager;
    }


    /**
     * RedisTemplate配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        // 设置序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);// key序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);// value序列化
        redisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);// Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
```

注解解析

```java
//指定缓存组件的名字
@AliasFor("cacheNames")
String[] value() default {};

//指定缓存组件的名字
@AliasFor("value")
String[] cacheNames() default {};

// 缓存数据使用的Key,
String key() default "";

// key的生成器，可以自己指定key的生成组件id; 
// key 与 keyGenerator 两个只能选一个，不能同时指定
String keyGenerator() default "";

// 指定缓存管理器，
String cacheManager() default "";

//指定获得解析器，cacheManager、cacheResolver 两者取其一
String cacheResolver() default "";

// 符合condition条件,才缓存
String condition() default "";

// 和 codition 条件相反才成立
String unless() default "";

// 是否使用异步模式
boolean sync() default false;

// 只有@CacheEvict 有这个属性
// 清空所有缓存信息
boolean allEntries() default false;

// 只有@CacheEvict 有这个属性
// 缓存的清除是否在方法之前执行
// beforeInvocation=false  默认是在方法之后执行
boolean beforeInvocation() default false;


    @Cacheable
    //先查询缓存中是否存在，存在则返回缓存内容，反正执行方法后返回并把返回结果缓存起来
    @CacheEvict
    //删除指定缓存
    @CachePut
    //更新并刷新缓存,先执行方法内容，然后更新缓存
    @EnableCaching
    //这个是一个复合注解，可以拥有同时配置上面3个注解的功能

```

实例：

```java
@CacheConfig(cacheNames = "act")  //这个注解表示类中共同放入到act 模块中
@Service
public class ActicleService implements IActicleService {

    @Autowired
    private ActicleMapper acticleMapper;

    /**
     * @Cacheable
     * 1、先查缓存，
     * 2、若没有缓存，就执行方法
     * 3、若有缓存。则返回，不执行方法
     *
     * 所以@Cacheable 不能使用result
     *
     * @return
     * @throws Exception
     */
    @Cacheable(key = "#root.methodName")
    public List<Acticle> list() throws Exception {
        return acticleMapper.getActicleList();
    }

    /**
     * @CachePut 更新并刷新缓存
     * 1、先调用目标方法
     * 2、把结果缓存
     * @param acticle
     * @return
     * @throws Exception
     */
    @CachePut(key = "#result.id" ,unless = "#result.id == null" )
    public Acticle save(Acticle acticle) throws Exception {
        acticle.setCreateBy(1l);
        acticle.setCreateTime(LocalDateTime.now());
        acticle.setModifyBy(1l);
        acticle.setModifyTime(LocalDateTime.now());
        acticleMapper.save(acticle);
        System.out.println("acticle="+acticle);
        return acticle;
    }

    /**
     * 删除指定key 的缓存
     * beforeInvocation=false  缓存的清除是否在方法之前执行
     * 默认是在方法之后执行
     * @param id
     * @return
     * @throws Exception
     */
    @CacheEvict(key = "#id",beforeInvocation = true)
    public int del(Long id) throws Exception {
        int isDel = 0;
        isDel = acticleMapper.del(id);
        return isDel;
    }

    /**
     * 删除所有缓存
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public int delAll() throws Exception {
        return 1;
    }

    @CachePut(key = "#result.id" ,unless = "#result.id == null" )
    public Acticle update(Acticle acticle) throws Exception {
        acticle.setModifyBy(1l);
        acticle.setModifyTime(LocalDateTime.now());
        return acticleMapper.update(acticle);
    }

    @Cacheable(key = "#id",condition = "#id > 0")
    public Acticle queryById(Long id) throws Exception {
        return acticleMapper.queryById(id);
    }

    /**
     * @Caching复杂组合缓存注解
     *
     * @param title
     * @return
     * @throws Exception
     */
    @Caching(cacheable = { @Cacheable(key = "#title")},
            put = {@CachePut(key = "#result.id"),
//            @CachePut(key = "T(String).valueOf(#page).concat('-').concat(#pageSize)")
            @CachePut(key = "T(String).valueOf('tag').concat('-').concat(#result.tagId)")
    })
    public Acticle queryByTitle(String title) throws Exception {
        return acticleMapper.queryByTitle(title);
    }

    @Cacheable(key = "T(String).valueOf('tag').concat('-').concat(#tagId)")
    public Acticle queryByTag(Long tagId) throws Exception {
        return null;
    }
}
```

缓存工作原理：

```java
当我们引入缓存的时候，SpringBoot的缓存自动配置CacheAutoConfiguration就会生效
    在CacheAutoConfiguration 中的CacheConfigurationImportSelector会导入很多缓存组件配置类
    通过debug看源码,知道imports 的内容如下

    static class CacheConfigurationImportSelector implements ImportSelector {
    	CacheConfigurationImportSelector() {
    	}

    	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
    		CacheType[] types = CacheType.values();
    		String[] imports = new String[types.length];

    		for(int i = 0; i < types.length; ++i) {
    			imports[i] = CacheConfigurations.getConfigurationClass(types[i]);
    		}

    		return imports;
    	}
    }

    /**
    imports 如下

    0 = "org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration"
    1 = "org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration"
    2 = "org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration"
    3 = "org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration"
    4 = "org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration"
    5 = "org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration"
    6 = "org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration"
    7 = "org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration"
    8 = "org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration"		
    9 = "org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration"
    */

    如果没有引Redis， 则SimpleCacheConfiguration这个就是默认的配置类
    在SimpleCacheConfiguration注入了一个ConcurrentMapCacheManager
    ConcurrentMapCacheManager 实现了CacheManager 接口
    ConcurrentMapCacheManager 通过 ConcurrentHashMap 把数据缓存起来
    在 CacheManager 有一个Cache getCache(String var1) 方法换取缓存
    流程大概就这里，感兴趣的同学可以打断点阅读源码

```



# 一些注解

## @ControllerAdvice

from: https://blog.csdn.net/m0_37607679/article/details/103949069

- 结合方法型注解@ExceptionHandler，用于捕获Controller中抛出的指定类型的异常，从而达到不同类型的异常区别处理的目的；

- 结合方法型注解@InitBinder，用于request中自定义参数解析方式进行注册，从而达到自定义指定格式参数的目的；

- 结合方法型注解@ModelAttribute，表示其标注的方法将会在目标Controller方法执行之前执行。

### @ExceptionHandler

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionHandler {
    // 指定需要捕获的异常的Class类型
	Class<? extends Throwable>[] value() default {};
}


   如下是我们使用@ExceptionHandler捕获RuntimeException异常的例子：

@ControllerAdvice(basePackages = "mvc")
public class SpringControllerAdvice {
  @ExceptionHandler(RuntimeException.class)
  public ModelAndView runtimeException(RuntimeException e) {
    e.printStackTrace();
    return new ModelAndView("error");
  }
}


   这里我们模拟一个访问user/detail的接口，在该接口中抛出了RuntimeException，那么，这里的异常捕获器就会捕获该异常，然后返回默认的error试图。如下是UserController的代码：

@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @RequestMapping(value = "/detail", method = RequestMethod.GET)
  public ModelAndView detail(@RequestParam("id") long id) {
    ModelAndView view = new ModelAndView("user");
    User user = userService.detail(id);
    view.addObject("user", user);
    throw new RuntimeException("mock user detail exception.");
  }
}


   启动上述服务，在浏览器中访问http://localhost:8080/user/detail?id=1之后，可以看到页面展示的是我们定义的异常视图。
————————————————
版权声明：本文为CSDN博主「Java晋升」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/m0_37607679/article/details/103949069
```

### @InitBinder

### @ModelAttribute


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

