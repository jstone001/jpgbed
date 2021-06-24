# SpringCloud 简介和安装

## 01 SpringCloud简介

### 第1节 简介
- https://projects.spring.io/spring-cloud/    springcloud项目官方主页
- https://springcloud.cc/    springcloud中文网 有很详细的翻译文档 
- http://springcloud.cn/    springcloud中文论坛 

## 02 父项目搭建
### 第2节 父项目MicroService

pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.java1234.springcloud</groupId>
  <artifactId>microservice</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>pom</packaging>	<!-- pom -->
   
  <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <druid.version>1.1.10</druid.version>
  </properties>
   
  <dependencyManagement>
      <dependencies>
          <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>Edgware.SR4</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>1.5.13.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <!-- 连接池 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>${druid.version}</version>
        </dependency>
      </dependencies>
  </dependencyManagement>
</project>
```

## 03 公共模块项目搭建

### 第3节 公共模块microservice-common搭建

http://blog.java1234.com/blog/articles/408.html

## 04 SpringCloud视频教程_服务提供者项目搭建

### 第4节  服务提供者项目microservice-provide-1001 建立

pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.java1234.springcloud</groupId>
    <artifactId>microservice</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  <artifactId>microservice-student-provider-1001</artifactId>
   
  <dependencies>
      <dependency>
          <groupId>com.java1234.springcloud</groupId>
          <artifactId>microservice-common</artifactId>
         <version>${project.version}</version>
      </dependency>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
    </dependency>
    <!-- 修改后立即生效，热部署 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>springloaded</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
    </dependency>
  </dependencies>
</project>
```

StudentDao

```java
package com.sw.dao;

import com.sw.bo.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentDao extends JpaRepository<Student, Integer>, JpaSpecificationExecutor<Student> {

}
```

StudentController.java

```java
package com.sw.controller;


import com.sw.bo.Student;
import com.sw.service.StudentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务提供者-学生信息控制器
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    private StudentService studentService;

    /**
     * 添加或者修改学生信息
     * @param student
     * @return
     */
    @PostMapping(value="/save")
    public boolean save(Student student){
        try{
            studentService.save(student);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * 查询学生信息
     * @return
     */
    @GetMapping(value="/list")
    public List<Student> list(){
        return studentService.list();
    }

    /**
     * 根据id查询学生信息
     * @return
     */
    @GetMapping(value="/get/{id}")
    public Student get(@PathVariable("id") Integer id){
        return studentService.findById(id);
    }

    /**
     * 根据id删除学生信息
     * @return
     */
    @GetMapping(value="/delete/{id}")
    public boolean delete(@PathVariable("id") Integer id){
        try{
            studentService.delete(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}

```

## 05 服务消费者项目搭建StudentConsumer-80

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>microservice</artifactId>
        <groupId>com.sw.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>microservice-student-consumer-80</artifactId>


    <dependencies>
        <dependency>
            <groupId>com.sw.springcloud</groupId>
            <artifactId>microservice-common</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- 修改后立即生效，热部署 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>springloaded</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
        </dependency>
    </dependencies>
</project>

```



SpringCloudConfig.java

```java
package com.java1234.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
 
/**
 * SpringCloud相关配置
 * @author Administrator
 *
 */
@Configuration
public class SpringCloudConfig {
 
    /**
     * 调用服务模版
     * @return
     */
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```

StudentConsumeApplication_80.java

```java
package com.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class StudentConsumerApplication_80 {

    public static void main(String[] args) {
        SpringApplication.run(StudentConsumerApplication_80.class,args);
    }
}

```

StudentProviderController.java

```java
    /**
     * 添加或者修改学生信息
     * @param student
     * @return
     */
    @PostMapping(value="/save")
    public boolean save(@RequestBody  Student student){		//必须加RequestBody
        try{
            studentService.save(student);
            return true;
        }catch(Exception e){
            return false;
        }
    }
```

# Eureka

## 06 服务治理组件Eureka简介

http://blog.java1234.com/blog/articles/411.html

Eureka github 地址： https://github.com/Netflix/eureka

## 07 搭建Eureka服务注册中心

http://blog.java1234.com/blog/articles/412.html

```yml
server:
  port: 2001
  context-path: /

eureka:
  instance:
    hostname: localhost #eureka注册中心实例名称
  client:
    register-with-eureka: false     #false 由于该应用为注册中心，所以设置为false,代表不向注册中心注册自己。
    fetch-registry: false     #false 由于注册中心的职责就是维护服务实例，它并不需要去检索服务，所以也设置为false
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/       #设置与Eureka注册中心交互的地址，查询服务和注册服务用到
```

microservie-eureka-server-2001.java

```java
package com.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer		// 作为服务器
public class EurekaApplication_2001 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication_2001.class, args);
    }
}
```

## 08 注册服务提供者到Eureka注册中心

http://blog.java1234.com/blog/articles/413.html

pom.xml

```xml
<!-- 加上eureka客户端依赖  -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<!-- actuator监控引入 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

application.yml

```yaml
eureka:
  instance:
    hostname: localhost  # eureka客户端主机实例名称
    appname: microservice-student  # 客户端服务名
    instance-id: microservice-student:1001 # 客户端实例名称
    prefer-ip-address: true # 显示IP
  client: 
    service-url: 
      defaultZone: http://localhost:2001/eureka   # 把服务注册到eureka注册中心
```

再父项目pom.xml里加上构建插件配置，主要是为了再构建的时候扫描子项目配置文件，解析配置用的。

```xml
<!-- 构建的时候 解析 src/main/resources 下的配置文件  其实就是application.yml 解析以$开头和结尾的信息 -->
<build>
    <finalName>microservice</finalName>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <filtering>true</filtering>
        </resource>
    </resources>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-resources-plugin</artifactId>
            <configuration>
                <delimiters>
                    <delimit>$</delimit>
                </delimiters>
            </configuration>
        </plugin>
    </plugins>
</build>
```

provider的application.yml添加

```yaml
info:
  groupId: $project.groupId$
  artifactId: $project.artifactId$
  version: $project.version$
  负责人: 张三
  联系电话: 110
```

## 09 Eureka注册中心高可用集群配置

### 修改本机hosts

```xml
127.0.0.1 eureka2001.sw.com
127.0.0.1 eureka2002.sw.com
127.0.0.1 eureka2003.sw.com
```

### 修改eureka 3个服务器的yml

eureka2001.yml

```yaml
defaultZone: http://eureka2002.java1234.com:2002/eureka/,http://eureka2003.java1234.com:2003/eureka/ # 集群
```

eureka2002.yml

```yaml
defaultZone: http://eureka2001.java1234.com:2001/eureka/,http://eureka2003.java1234.com:2003/eureka/ # 集群
```

eureka2003.yml

```yaml
defaultZone: http://eureka2002.java1234.com:2002/eureka/,http://eureka2001.java1234.com:2001/eureka/ # 集群
```

修改provider 的yml

```yaml
client:
  service-url:
    #defaultZone: http://localhost:2001/eureka   # 把服务注册到eureka注册中心
    defaultZone: http://eureka2001.java1234.com:2001/eureka/,http://eureka2002.java1234.com:2002/eureka/,http://eureka2003.java1234.com:2003/eureka/ # 集群
```

## 10 Eureka自我保护机制

http://blog.java1234.com/blog/articles/415.html

- 默认情况，如果服务注册中心再一段时间内没有接收到某个微服务实例的心跳，服务注册中心会注销该实例（默认90秒）。
- 由于正式环境，经常会有网络故障，网络延迟问题发生，服务和注册中心无法正常通信，此时服务是正常的，不应该注销该服务，Eureka这时候，就通过“自我保护模式”来解决问题，当短时间和服务失去通信时，保留服务信息，当恢复网络和通信时候，退出“自我保护模式”；

# Ribbon

## 11 Ribbon简介

http://blog.java1234.com/blog/articles/429.html

## 12 Ribbon初步应用

http://blog.java1234.com/blog/articles/430.html

pom.xm加上

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-ribbon</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

application.yml 加上

```yaml
eureka:
  client:
    register-with-eureka: false #false 由于注册中心的职责就是维护服务实例，它并不需要去检索服务，所以也设置为false
    service-url: 
      defaultZone: http://eureka2001.java1234.com:2001/eureka/,http://eureka2002.java1234.com:2002/eureka/,http://eureka2003.java1234.com:2003/eureka/
```

SpringCloudConfig.java 也改成 要加个负载均衡配置 @LoadBalanced

```java
/**
     * 调用服务模版对象
     * @return
     */
    @Bean
    @LoadBalanced  // 引入ribbon负载均衡
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
```

 StudentConsumerApplication_80.java 加上@EnableEurekaClient

```java
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableEurekaClient		//加上
public class StudentConsumerApplication_80 {

    public static void main(String[] args) {
        SpringApplication.run(StudentConsumerApplication_80.class,args);
    }
}
```

这里还有一个，要修改下StudentConsumerController的<font color='red'>PRE_HOST</font>，改成指定的微服务应用名称，这里是 http://MICROSERVICE-STUDENT 即可；

```java
private final static String PRE_HOST="http://MICROSERVICE-STUDENT";
```



当然这里要先在服务提供者microservice-student-provider-1001的application.yml加下配置，指定下应用名称：

```yaml
spring:
  application:
    name: microservice-student		# 新增name
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://192.168.132.25:3306/db_1024
    username: root
    password: 123
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

上面配置好后，我们可以测试下；

先启动三个eureka，然后再启动服务提供者，再启动服务消费者；

执行 http://localhost/student/list

```
[{"id":1,"name":"张三","grade":"大一"},{"id":2,"name":"李四","grade":"大二"},{"id":3,"name":"王五","grade":"大三"},{"id":4,"name":"赵六","grade":"大四"}]
```

## 13 Ribbon负载均衡

http://blog.java1234.com/blog/articles/431.html

新建项目microservice-student-provider-1002，microservice-student-provider-1003

修改端口

再Controller控制器的方法里搞个打印语句；

```java
    /**
     * 查询学生信息
     * @return
     */
    @GetMapping(value="/list")
    public List<Student> list(){
        System.out.println("list----provider1");	//添加
        return studentService.list();
    }
```

先测试服务提供者：

- http://localhost:1001/student/list
- [http://localhost:1002/student/list](http://localhost:1001/student/list)
- http://localhost:1003/student/list

看看是否有结果；

再测试下 eureka：

- http://eureka2001.java1234.com:2001/
- [http://eureka2002.java1234.com:2002/](http://eureka2001.java1234.com:2001/)
- http://eureka2003.java1234.com:2003/



然后再启动服务消费者：

http://localhost/student/list 多刷新几次 看控制台，我们看到 有默认的轮询策略，访问对应的服务提供者；



| 策略名                    | 策略声明                                                     | 策略描述                                                     | 实现说明                                                     |
| ------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| BestAvailableRule         | public class BestAvailableRule extends ClientConfigEnabledRoundRobinRule | 选择一个最小的并发请求的server                               | 逐个考察Server，如果Server被tripped了，则忽略，在选择其中ActiveRequestsCount最小的server |
| AvailabilityFilteringRule | public class AvailabilityFilteringRule extends PredicateBasedRule | 过滤掉那些因为一直连接失败的被标记为circuit tripped的后端server，并过滤掉那些高并发的的后端server（active connections 超过配置的阈值） | 使用一个AvailabilityPredicate来包含过滤server的逻辑，其实就就是检查status里记录的各个server的运行状态 |
| WeightedResponseTimeRule  | public class WeightedResponseTimeRule extends RoundRobinRule | 根据响应时间分配一个weight，响应时间越长，weight越小，被选中的可能性越低。 | 一个后台线程定期的从status里面读取评价响应时间，为每个server计算一个weight。Weight的计算也比较简单responsetime  减去每个server自己平均的responsetime是server的权重。当刚开始运行，没有形成status时，使用roubine策略选择server。 |
| RetryRule                 | public class RetryRule extends AbstractLoadBalancerRule      | 对选定的负载均衡策略机上重试机制。                           | 在一个配置时间段内当选择server不成功，则一直尝试使用subRule的方式选择一个可用的server |
| RoundRobinRule            | public class RoundRobinRule extends AbstractLoadBalancerRule | roundRobin方式轮询选择server                                 | 轮询index，选择index对应位置的server                         |
| RandomRule                | public class RandomRule extends AbstractLoadBalancerRule     | 随机选择一个server                                           | 在index上随机，选择index对应位置的server                     |
| ZoneAvoidanceRule         | public class ZoneAvoidanceRule extends PredicateBasedRule    | 复合判断server所在区域的性能和server的可用性选择server       | 使用ZoneAvoidancePredicate和AvailabilityPredicate来判断是否选择某个server，前一个判断判定一个zone的运行性能是否可用，剔除不可用的zone（的所有server），AvailabilityPredicate用于过滤掉连接数过多的Server。 |

SpringCloudConfig.java 添加策略

```java
    /**
     * 自定义轮询算法
     * @return
     */
    @Bean
    public IRule myRule(){
        return new AvailabilityFilteringRule();
    }
```

# Feign

## 14 Feign介绍

http://blog.java1234.com/blog/articles/433.html

## 15 Feign应用

http://blog.java1234.com/blog/articles/434.html

### 1. microservice-common 的pom.xml加入依赖

```xml
<dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-feign</artifactId>
    </dependency>
```

### 2. 创建StudentClientService接口

StudenClientService.java

```java
package com.java1234.service;
 
import java.util.List;
 
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
 
import com.java1234.entity.Student;
 
/**
 * Student Feign接口客户端
 * @author Administrator
 *
 */
@FeignClient(value="MICROSERVICE-STUDENT")		//指定了调用的服务名称MICROSERVICE-STUDENT
public interface StudentClientService {
 
    /**
     * 根据id查询学生信息
     * @param id
     * @return
     */
    @GetMapping(value="/student/get/{id}")
    public Student get(@PathVariable("id") Integer id);
     
    /**
     * 查询学生信息
     * @return
     */
    @GetMapping(value="/student/list")
    public List<Student> list();
     
    /**
     * 添加或者修改学生信息
     * @param student
     * @return
     */
    @PostMapping(value="/student/save")
    public boolean save(Student student);
     
    /**
     * 根据id删除学生信息
     * @return
     */
    @GetMapping(value="/student/delete/{id}")
    public boolean delete(@PathVariable("id") Integer id);
}
```

**common项目修改后，maven clean下 然后install下**

### 3. 新建一个Feign消费者项目

- 参考microservice-student-consumer-80建一个microservice-student-consumer-feign-80
- 代码都复制一份，包括pom.xml
- pom.xml里加下 feign依赖，和第一步一样；

### 4. 修改启动类名称，和加注解

启动类名称改下，改成StudentConsumerFeignApplication_80，同时加个注解@EnableFeignClients

### 5. 修改Contrller

StudentConsumerController.java

```java
package com.java1234.controller;
 
import java.util.List;
 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.java1234.entity.Student;
import com.java1234.service.StudentClientService;
 
/**
 * 服务消费者-学生信息控制器
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/student")
public class StudentConsumerFeignController {
 
    @Autowired
    private StudentClientService studentClientService;
     
    /**
     * 添加或者修改学生信息
     * @param student
     * @return
     */
    @PostMapping(value="/save")
    public boolean save(Student student){
        return studentClientService.save(student);
    }
      
    /**
     * 查询学生信息
     * @return
     */
    @GetMapping(value="/list")
    public List<Student> list(){
        return studentClientService.list();
    }
      
    /**
     * 根据id查询学生信息
     * @return
     */
    @GetMapping(value="/get/{id}")
    public Student get(@PathVariable("id") Integer id){
        return studentClientService.get(id);
    }
      
    /**
     * 根据id删除学生信息
     * @return
     */
    @GetMapping(value="/delete/{id}")
    public boolean delete(@PathVariable("id") Integer id){
        return studentClientService.delete(id);
    }    
}
```

因为现在用Fiegn，所以把restTemplate去掉，改成注入service，调用service方法来实现服务的调用；

### 6. 测试负载均衡

SpringCloudConfig类的myRule，大伙自行修改测试即可；

# Hystrix

## 16 Hystrix断路器简介

http://blog.java1234.com/blog/articles/435.html

在一个分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，如何能够保证在一个依赖出问题的情况下，不会导致整体服务失败，这个就是Hystrix需要做的事情。Hystrix提供了熔断、隔离、Fallback、cache、监控等功能，能够在一个、或多个依赖同时出现问题时保证系统依然可用。

## 17 服务雪崩效应

http://blog.java1234.com/blog/articles/436.html

## 18 Hystrix服务熔断服务降级

http://blog.java1234.com/blog/articles/437.html

### 1. 新建的带服务熔断的服务提供者项目 microservice-student-provider-hystrix-1004

### 2. pom.xml 增加依赖

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-hystrix</artifactId>
</dependency>
```

### 3. application.yml修改下端口和实例名称

```yaml
port: 1004
eureka:
  instance:
    instance-id: microservice-student-hystrix:1004 #客户端实例名称
```

### 4. 启动类名称改成StudentProviderHystrixApplication_1004

​	以及加下注解支持 @EnableCircuitBreaker

### 5. 我们新增方法getInfo

StudentController.java

```java
/**
     * 获取信息
     * @return
     * @throws InterruptedException
     */
@ResponseBody
@GetMapping(value="/getInfo")
@HystrixCommand(fallbackMethod="getInfoFallback")
public Map<String,Object> getInfo() throws InterruptedException{
    Thread.sleep(100);
    Map<String,Object> map=new HashMap<String,Object>();
    map.put("code", 200);
    map.put("info", "业务数据xxxxx");
    return map;
}

public Map<String,Object> getInfoFallback() throws InterruptedException{
    Map<String,Object> map=new HashMap<String,Object>();
    map.put("code", 500);
    map.put("info", "系统出错，稍后重试");
    return map;
}
```

### 6. microservice-student-consumer-80项目也要对应的加个方法

StudentConsumerControler.java

```java
@GetMapping(value="/getInfo")
@ResponseBody
public Map<String,Object> getInfo(){
    return restTemplate.getForObject(PRE_HOST+"/student/getInfo/", Map.class);
}
```

### 7. 测试http://localhost/student/getInfo

因为 Hystrix默认1算超时，所有 sleep了2秒 所以进入自定义fallback方法，防止服务雪崩；

我们这里改sleep修改成100毫秒；

## 19 Hystrix默认超时时间设置

http://blog.java1234.com/blog/articles/438.html

通过hystrix源码看到，

找到 hystrix-core.jar com.netflix.hystrix包下的HystrixCommandProperties类

default_executionTimeoutInMilliseconds属性局势默认的超时时间

**application.yml配置文件加上** 

```yaml
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 3000
```

## 20 Hystrix服务监控Dashboard

http://blog.java1234.com/blog/articles/441.html

### 1. 新建项目：microservice-student-consumer-hystrix-dashboard-90

pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-hystrix</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```

applilcation.yml

```yaml
server:
  port: 90
  context-path: /

```

StudentConsumerDashBoardApplication_90.java

```java
package com.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableHystrixDashboard
public class StudentConsumerDashBoardApplication_90 {

    public static void main(String[] args) {
        SpringApplication.run(StudentConsumerDashBoardApplication_90.class, args);
    }
}
```

### 2. 然后浏览器输入：http://localhost:90/hystrix

![image-20210618141646784](E:\JS\booknote\jpgBed\image-20210618141646784.png)

### 3. 我们启动三个eureka，然后再启动microservice-student-provider-hystrix-1004

直接请求http://localhost:1004/student/getInfo

监控的话，http://localhost:1004/hystrix.stream 这个路径即可；

图形化：

![image-20210618141722276](E:\JS\booknote\jpgBed\image-20210618141722276.png)

指标含义：

![image-20210618141757717](E:\JS\booknote\jpgBed\image-20210618141757717.png)

各种情况：

![image-20210618141830693](E:\JS\booknote\jpgBed\image-20210618141830693.png)

## 21 Hystrix集群监控turbine

http://blog.java1234.com/blog/articles/442.html

### 1. 建一个新项目microservice-student-provider-hystrix-1005

修改yml文件

```yaml
server:
  port: 1005

instance-id: microservice-student-hystrix:1005 #客户端实例名称 
```

启动类改成StudentProviderHystrixApplication_1005

### 2. 新建项目microservice-student-consumer-hystrix-turbine-91

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>microservice</artifactId>
        <groupId>com.sw.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>microservice-student-consumer-hystrix-turbine-91</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-turbine</artifactId>
        </dependency>
    </dependencies>

</project>
```

新建启动类：StudentConsumerTurbineApplication_91.java

```java
package com.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableTurbine
public class StudentConsumerTurbineApplication_91 {

    public static void main(String[] args) {
        SpringApplication.run(StudentConsumerTurbineApplication_91.class, args);
    }
}
```

### 3. 测试

- 先启动三个eureka，然后把1004 1005 带hystrix的服务都启动；
- microservice-student-consumer-80这个也启动，方便测试；
- dashboard，turbine启动；

这样的话 http://localhost/student/getInfo 就能调用服务集群；

http://localhost:90/hystrix  面板中输入

[http://localhost:91/turbine.stream](http://localhost:91/turbine.stream可以监控数据) 可以监控数据，实时ping 返回data

## 22 Feign Hystrix整合&服务熔断服务降级彻底解耦

http://blog.java1234.com/blog/articles/439.html

### 1 microservice-student-provider-hystrix-1004项目修改

StudentService.java加新的接口方法：

```java
/**
 * 获取信息
 * @return
 */
public Map<String,Object> getInfo();
```

StudentServiceImpl.java。原getInfoFallback 不用了放在microservice-common里

```java
@Override
public Map<String, Object> getInfo() {
    Map<String,Object> map=new HashMap<String,Object>();
    map.put("code", 200);
    map.put("info", "业务数据xxxxx");
    return map;
}
```

StudentProviderController.java 正常调用service方法：

```java
/**
 * 获取信息
 * @return
 * @throws InterruptedException 
 */
@ResponseBody
@GetMapping(value="/getInfo")
public Map<String,Object> getInfo() throws InterruptedException{
    Thread.sleep(900);
    return studentService.getInfo();
}
```

### 2. microservice-common项目新建FallbackFactory类，解耦服务熔断服务降级

前面的代码，用@HystrixCommand fallbackMethod是很不好的，因为和业务代码耦合度太高，不利于维护，所以需要解耦，这我们讲下Feign Hystrix整合。

### 3. microservice-student-provider-hystrix-1004项目修改

StudentService.java加新的接口方法：

```java
/**
 * 获取信息
 * @return
 */
public Map<String,Object> getInfo();
```

StudentServiceImpl.java

```java
@Override
public Map<String, Object> getInfo() {
    Map<String,Object> map=new HashMap<String,Object>();
    map.put("code", 200);
    map.put("info", "业务数据xxxxx1004");
    return map;
}
```

StudentProviderController.java正常调用service方法：

```java
/**
 * 获取信息
 * @return
 * @throws InterruptedException 
 */
@ResponseBody
@GetMapping(value="/getInfo")
public Map<String,Object> getInfo() throws InterruptedException{
    Thread.sleep(900);
    return studentService.getInfo();
}
```

### 4.  microservice-student-provider-hystrix-1005 一样改一下

### 5. microservice-common项目新建FallbackFactory类，解耦服务熔断服务降级

StudentClientService.java接口，新增getInfo方法

```java
/**
 * 获取信息
 * @return
 */
@GetMapping(value="/student/getInfo")
public Map<String,Object> getInfo();
```

新建 StudentClientFallbackFactory.java 类，实现FallbackFactory<StudentClientService>接口；

```java
package com.java1234.service;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.springframework.stereotype.Component;
 
import com.java1234.entity.Student;
 
import feign.hystrix.FallbackFactory;
 
@Component
public class StudentClientFallbackFactory implements FallbackFactory<StudentClientService>{
 
    @Override
    public StudentClientService create(Throwable cause) {
        // TODO Auto-generated method stub
        return new StudentClientService() {
             
            @Override
            public boolean save(Student student) {
                // TODO Auto-generated method stub
                return false;
            }
             
            @Override
            public List<Student> list() {
                // TODO Auto-generated method stub
                return null;
            }
             
            @Override
            public Map<String, Object> getInfo() {
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("code", 500);
                map.put("info", "系统出错，稍后重试");
                return map;
            }
             
            @Override
            public Student get(Integer id) {
                // TODO Auto-generated method stub
                return null;
            }
             
            @Override
            public boolean delete(Integer id) {
                // TODO Auto-generated method stub
                return false;
            }
        };
    }
 
}
```

StudentClientService接口的@FeignClient注解加下 fallbackFactory属性 

```java
@FeignClient(value="MICROSERVICE-STUDENT",fallbackFactory=StudentClientFallbackFactory.class)
```

### 6. microservice-student-consumer-feign-80修改 支持Hystrix

StudentConsumerFeignController.java新增方法调用

```java
/**
 * 根据id删除学生信息
 * @return
 */
@GetMapping(value="/getInfo")
@ResponseBody
public Map<String,Object> getInfo(){
    return studentClientService.getInfo();
}
```

application.yml 加上hystrix支持

```yaml
feign: 
  hystrix: 
    enabled: true
```

## 23 Feign Hystrix整合之超时时间配置

http://blog.java1234.com/blog/articles/440.html

microservice-student-consumer-feign-80 项目的application.yml

```yaml
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 3000

ribbon:
  ReadTimeout: 10000
  ConnectTimeout: 9000
```

原provider端的已无用

```yaml
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 3000	# 无用了
```

# Zuul

## 24 Zuul API路由网关服务简介

http://blog.java1234.com/blog/articles/449.html

## 25 Zuul 路由配置

http://blog.java1234.com/blog/articles/450.html

### 1. 新建module microservice-zuul-3001

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>microservice</artifactId>
        <groupId>com.sw.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>microservice-zuul-3001</artifactId>

    <dependencies>
        <dependency>
            <groupId>com.sw.springcloud</groupId>
            <artifactId>microservice-common</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- zuul路由网关 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zuul</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>

        <!-- actuator监控 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- hystrix容错 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-hystrix</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>

        <!-- 修改后立即生效，热部署 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>springloaded</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
        </dependency>
    </dependencies>
</project>

```

application.yml

```yaml
server:
  port: 3001
  context-path: /

spring:
  application:
    name: microservice-zuul

eureka:
  instance:
    instance-id: microservice-zuul:3001 #客户端实例名称
    prefer-ip-address: true #显示IP
  client:
    service-url:
      defaultZone: http://eureka2001.java1234.com:2001/eureka/,http://eureka2002.java1234.com:2002/eureka/,http://eureka2003.java1234.com:2003/eureka/ # 集群

info:
  groupId: $project.groupId$
  artifactId: $project.artifactId$
  version: $project.version$
  负责人: 王五
  联系电话: 110
```

启动类ZuulApplication_3001.java

```java
package com.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableZuulProxy
public class ZuulApplication_3001 {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication_3001.class, args);
    }

}

```

### 2. 测试

启动三个eureka 然后再启动下一个1001服务，以及 zuul网关服务

这里有两个服务；

- 我们直接请求：http://localhost:1001/student/list 能获取到数据；
- 我们用 http://zuul.java1234.com:3001/microservice-student/student/list 域名+端口+服务名称+请求地址 也能请求到数据；

说明我们的路由基本配置OK

## 26 路由映射规则配置

http://blog.java1234.com/blog/articles/451.html

前面的默认路由请求地址，很容易暴露接口细节；

所以我们这里需要配置下映射规则，提高服务的安全性

### 1.  增加映射配置

```yaml
zuul:
  routes:
    studentServer.serviceId: microservice-student
    studentServer.path: /studentServer/**
```

我们把 microservice-student的服务名称 映射成 /studentServer/**

这样就把 服务地址影藏了；

我们可以用 http://zuul.java1234.com:3001/studentServer/student/list 来访问服务请求；

### 2. 屏蔽原来的请求地址

但是我们的原来的 用服务名称访问 http://zuul.java1234.com:3001/microservice-student/student/list 

也没问题；我们要屏蔽，忽略这种请求方式的话，加个配置：ignored-services: "microservice-student"

假如要忽略所有的服务名称的话，ignored-services: "*"

```yaml
zuul:
  ignored-services: "*"
  routes:
    studentServer.serviceId: microservice-student
    studentServer.path: /studentServer/**
```

http://zuul.java1234.com:3001/microservice-student/student/list 就被屏蔽了

### 3. 增加公司前缀

有时候要加上公司域名前缀，我们加下配置 prefix: /java1234

```yaml
zuul:
  ignored-services: "*"
  prefix: /java1234		#公司前缀
  routes:
    studentServer.serviceId: microservice-student
    studentServer.path: /studentServer/**
```

这样的话 比如 http://zuul.java1234.com:3001/java1234/studentServer/student/list 请求方式，否则是404

## 27 Zuul 请求过滤配置

http://blog.java1234.com/blog/articles/452.html

### 1. 新建com.sw.filter.AccessFilter.java

```java
package com.sw.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;


public class AccessFilter extends ZuulFilter {

    Logger logger=Logger.getLogger(AccessFilter.class);

    /**
     * 判断该过滤器是否要被执行
     */
    @Override
    public boolean shouldFilter() {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * 过滤器的具体执行逻辑
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String parameter = request.getParameter("accessToken");
        logger.info(request.getRequestURL().toString()+" 请求访问");
        if(parameter==null){
            logger.error("accessToken为空！");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody("{\"result\":\"accessToken is empty!\"}");
            return null;
        }
        //  token判断逻辑
        logger.info(request.getRequestURL().toString()+" 请求成功");
        return null;
    }

    /**
     * 过滤器的类型 这里用pre，代表会再请求被路由之前执行
     */
    @Override
    public String filterType() {
        // TODO Auto-generated method stub
        return "pre";
    }

    /**
     * 过滤器的执行顺序
     */
    @Override
    public int filterOrder() {
        // TODO Auto-generated method stub
        return 0;
    }
}

```

### 2. 新建com.sw.config.ZuulConfig.java

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import com.java1234.filter.AccessFilter;
 
/**
 * Zuul配置
 * @author Administrator
 *
 */
@Configuration
public class ZuulConfig {
 
    @Bean
    public AccessFilter accessFilter(){
        return new AccessFilter();
    }
}

```

配置完后，我们 访问：http://zuul.java1234.com:3001/java1234/studentServer/student/list 

发现歇逼了：

```json
{"result":"accessToken is empty!"}
```

我们带个 accessToken http://zuul.java1234.com:3001/java1234/studentServer/student/list?accessToken=3232 

就返回了结果；

当然 token的生成和验证 这里我们不细讲了,具体要根据设计来

# Config

## 28 Config介绍

http://blog.java1234.com/blog/articles/502.html

SpringCloud Config简介

​        Spring Cloud Config 是 Spring  Cloud  团队创建的一个全新项目，用来为分布式系统中的基础设施和微服务应用提供集中化的外部配置支持，它分为服务端与客户端两个部分。其中服务端也称为分布式配置中心，它是一个独立的微服务应用，用来连接配置仓库并为客户端提供获取配置信息、加密 /  解密信息等访问接口；而客户端则是微服务架构中的各个微服务应用或基础设施，它们通过指定的配置中心来管理应用资源与业务相关的配置内容，并在启动的时候从配置中心获取和加载配置信息。Spring Cloud Config 实现了对服务端和客户端中环境变量和属性配置的抽象映射，所以它除了适用于 Spring  构建的应用程序之外，也可以在任何其他语言运行的应用程序中使用。由于 Spring Cloud Config 实现的配置中心默认采用 Git  来存储配置信息，所以使用 Spring Cloud Config 构建的配置服务器，天然就支持对微服务应用配置信息的版本管理，并且可以通过  Git 客户端工具来方便的管理和访问配置内容。当然它也提供了对其他存储方式的支持，比如：GIT仓库、SVN 仓库、本地化文件系统。

![image-20210624094446657](E:\JS\booknote\jpgBed\image-20210624094446657.png)

SpringCloud Bus

![image-20210624094522825](E:\JS\booknote\jpgBed\image-20210624094522825.png)

## 29 Config Server基本使用



## 30 Config Client基本使用

## 31 Config整合Eureka

## 32 Config配置搜索路径
