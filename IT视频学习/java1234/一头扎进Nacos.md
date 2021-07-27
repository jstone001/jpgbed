## 1_Nacos简介

Nacos一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。

官网：https://nacos.io/zh-cn/

### **功能：**

**1，动态配置服务**

动态配置服务让您能够以中心化、外部化和动态化的方式管理所有环境的配置。动态配置消除了配置变更时重新部署应用和服务的需要。配置中心化管理让实现无状态服务更简单，也让按需弹性扩展服务更容易。

![image-20210701102736666](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210701102736666.png)

**2，服务发现及管理**

动态服务发现对以服务为中心的（例如微服务和云原生）应用架构方式非常关键。Nacos支持DNS-Based和RPC-Based（Dubbo、gRPC）模式的服务发现。Nacos也提供实时健康检查，以防止将请求发往不健康的主机或服务实例。借助Nacos，您可以更容易地为您的服务实现断路器。

![image-20210701102800730](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210701102800730.png)

 

**3，动态DNS服务**

通过支持权重路由，动态DNS服务能让您轻松实现中间层负载均衡、更灵活的路由策略、流量控制以及简单数据中心内网的简单DNS解析服务。动态DNS服务还能让您更容易地实现以DNS协议为基础的服务发现，以消除耦合到厂商私有服务发现API上的风险。

##  2_Nacos Server安装

startup.sh改成单例模式

```sh
26 export MODE="standalone"
```

修改数据库链接 conf/application.properties

```properties
### If use MySQL as datasource:
 spring.datasource.platform=mysql

### Count of DB:
 db.num=1

### Connect URL of DB:
 db.url.0=jdbc:mysql://192.168.132.25:3306/db_nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
 db.user.0=root
 db.password.0=123

```

## 3_Nacos配置中心HelloWorld入门实例

https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E

springcloudalibaba springcloud springboot版本

### 组件版本关系

| Spring Cloud Alibaba Version                              | Sentinel Version | Nacos Version | RocketMQ Version | Dubbo Version | Seata Version |
| --------------------------------------------------------- | ---------------- | ------------- | ---------------- | ------------- | ------------- |
| 2021.1 or 2.2.5.RELEASE or 2.1.4.RELEASE or 2.0.4.RELEASE | 1.8.0            | 1.4.1         | 4.4.0            | 2.7.8         | 1.3.0         |
| 2.2.3.RELEASE or 2.1.3.RELEASE or 2.0.3.RELEASE           | 1.8.0            | 1.3.3         | 4.4.0            | 2.7.8         | 1.3.0         |
| 2.2.1.RELEASE or 2.1.2.RELEASE or 2.0.2.RELEASE           | 1.7.1            | 1.2.1         | 4.4.0            | 2.7.6         | 1.2.0         |
| 2.2.0.RELEASE                                             | 1.7.1            | 1.1.4         | 4.4.0            | 2.7.4.1       | 1.0.0         |
| 2.1.1.RELEASE or 2.0.1.RELEASE or 1.5.1.RELEASE           | 1.7.0            | 1.1.4         | 4.4.0            | 2.7.3         | 0.9.0         |
| 2.1.0.RELEASE or 2.0.0.RELEASE or 1.5.0.RELEASE           | 1.6.3            | 1.1.1         | 4.4.0            | 2.7.3         | 0.7.1         |

### 毕业版本依赖关系(推荐使用)

| Spring Cloud Version        | Spring Cloud Alibaba Version      | Spring Boot Version |
| --------------------------- | --------------------------------- | ------------------- |
| Spring Cloud 2020.0.0       | 2021.1                            | 2.4.2               |
| Spring Cloud Hoxton.SR8     | 2.2.5.RELEASE                     | 2.3.2.RELEASE       |
| Spring Cloud Greenwich.SR6  | 2.1.4.RELEASE                     | 2.1.13.RELEASE      |
| Spring Cloud Hoxton.SR3     | 2.2.1.RELEASE                     | 2.2.5.RELEASE       |
| Spring Cloud Hoxton.RELEASE | 2.2.0.RELEASE                     | 2.2.X.RELEASE       |
| Spring Cloud Greenwich      | 2.1.2.RELEASE                     | 2.1.X.RELEASE       |
| Spring Cloud Finchley       | 2.0.4.RELEASE(停止维护，建议升级) | 2.0.X.RELEASE       |
| Spring Cloud Edgware        | 1.5.1.RELEASE(停止维护，建议升级) | 1.5.X.RELEASE       |

我们使用

| Spring Cloud Version        | Spring Cloud Alibaba Version      | Spring Boot Version |
| ----------------------- | ------------- | ------------- |
| Spring Cloud Hoxton.SR8 | 2.2.5.RELEASE | 2.3.2.RELEASE |

主pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.java1234</groupId>
    <artifactId>nacos_demo</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>nacos_config_test</module>
    </modules>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <spring-cloud.version>Hoxton.SR8</spring-cloud.version>
        <springboot.version>2.3.2.RELEASE</springboot.version>
        <springcloudalibaba.version>2.2.5.RELEASE</springcloudalibaba.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${springboot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${springcloudalibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

        </dependencies>
    </dependencyManagement>


    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

module->nacos_config_test/pom.xml

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 配置中心 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>
    </dependencies>
```

nacos_cinfog_test/application.yml

```yaml
server:
  port: 8080
  servlet:
    context-path: /
```

bootstrap.properties

```properties
spring.application.name=nacos_config_test
spring.cloud.nacos.config.server-addr=192.168.132.111:8848
spring.cloud.nacos.config.group=DEFAULT_GROUP
spring.cloud.nacos.config.name=nacos_config.properties
```

NacosConfigController.java

```java
package com.sw.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nacos")
@RefreshScope   //实时刷新
public class NacosConfigController {

    @Value("${java1234.name}")
    private String name;

    @Value("${java1234.age}")
    private String age;

    @GetMapping("/getConfigInfo")
    public String getConfigInfo(){
        return name+":"+age;
    }
}

```

# Nacos配置中心

## 4_配置中心-数据模型

Nacos 数据模型 Key 由三元组唯一确定, Namespace默认是空串，公共命名空间（public），分组默认是 DEFAULT_GROUP。

![image-20210702094751613](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210702094751613.png)

```properties
spring.application.name=nacos_config_test
spring.cloud.nacos.config.server-addr=192.168.132.111:8848
spring.cloud.nacos.config.group=DEFAULT_GROUP
spring.cloud.nacos.config.name=nacos_config.properties
spring.cloud.nacos.config.namespace=dev	 # namespace
```

## 5_配置中心-加载多配置集

有时候为了多模块项目的配置共享，我们需要进行配置拆分以及加载多配置集；

比如我们有10个业务模块项目；mysql，redis，reabbitmq等配置都是一样的，为了统一管理，我们在nacos配置中心需要单独的搞成一个通用配置，然后供主项目引入；

bootstrap.properties  获取nacos中不同的配置

```properties
spring.application.name=nacos_config_test
spring.cloud.nacos.config.server-addr=192.168.132.111:8848
# dev的id
spring.cloud.nacos.config.namespace=a432743b-9171-46c4-8a08-9128a7a9aa0c	
#spring.cloud.nacos.config.group=DEFAULT_GROUP
#spring.cloud.nacos.config.name=nacos_config.properties

spring.cloud.nacos.config.extension-configs[0].data-id=mysql_common.properties
spring.cloud.nacos.config.extension-configs[0].group=DEFAULT_GROUP
spring.cloud.nacos.config.extension-configs[0].refresh=true

spring.cloud.nacos.config.extension-configs[1].data-id=redis_common.properties
spring.cloud.nacos.config.extension-configs[1].group=DEFAULT_GROUP
spring.cloud.nacos.config.extension-configs[1].refresh=true

spring.cloud.nacos.config.extension-configs[2].data-id=crm.properties
spring.cloud.nacos.config.extension-configs[2].group=CRM_GROUP
spring.cloud.nacos.config.extension-configs[2].refresh=true

spring.cloud.nacos.config.extension-configs[3].data-id=oa.properties
spring.cloud.nacos.config.extension-configs[3].group=OA_GROUP
spring.cloud.nacos.config.extension-configs[3].refresh=true
```

## 6_配置中心-其他功能

### 1. 配置的导入和导出

### 2. 克隆

### 3. 历史版本

### 4. 监听

# 服务注册与发现

## 7_服务注册与发现简介

1. 大的分布式微服务项目会根据业务，把项目拆分成多个业务模块项目，然后互相调用；
2. 如何互相调用呢？
3. 这里我们每个项目模块都需要去nacos服务注册中心注册下，登记下每个项目自身的地址和端口，然后其他的项目模块就可以通过Nacos找到需要调用的其他模块项目的地址了；
4. 这样就可以实现服务的发现和调用

![image-20210702112541818](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210702112541818.png)

## 8_服务注册实现

### 新建模块：nacos-order

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>nacos_demo</artifactId>
        <groupId>com.sw</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>nacos-order</artifactId>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 配置中心 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>

        <!-- 服务注册/发现-->	 <!-- 添加这个   -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
    </dependencies>

</project>

```

application.yml

```yaml
server:
  port: 8082
  servlet:
    context-path: /

# 配置服务发现
spring:
  application:
    name: nacos-order
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.132.111:8848
```

NacosOrderApplication.java

```java
package com.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // 让别人发现服务
public class NacosOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosOrderApplication.class, args);
    }
}
```

### 新建模块：nacos-stock

application.yml

```yaml
server:
  port: 8083
  servlet:
    context-path: /

# 配置服务发现
spring:
  application:
    name: nacos-stock
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.132.111:8848
```

NacosStockApplication.java

```java
package com.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // 让别人发现服务
public class NacosStockApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosStockApplication.class, args);
    }
}
```

在nacos的页面中，能看到这2个服务名

## 9_openfeign服务调用实现

### nacos-order加文件

在nacos-order/pom.xml 引入依赖

```xml
<!-- openfeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

nacos-order/StockFeignService接口

```java
package com.sw.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("nacos-stock")
public interface StockFeignService {

    @RequestMapping("/stock/test")
    String test(@RequestParam ("info") String info);
}
```

nacos-order/OrderController.java

```java
package com.sw.controller;

import com.sw.feign.StockFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private StockFeignService stockFeignService;

    @RequestMapping("/test")
    public String test(){
        return stockFeignService.test("牛逼");
    }
}
```

nacos-order/NacosOrderApplication 加上feign客户端支持

```java
package com.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient  // 让别人发现服务
@EnableFeignClients(basePackages = "com.sw.feign")  //开启Feign客户端支持
public class NacosOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosOrderApplication.class, args);
    }
}
```

### nacos-stock加文件

nacos-stock/StockController.java

```java
package com.sw.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stock")
public class StockController {

    @RequestMapping("/test")
    public String test(String info) {

        return "库存模块收到的信息：" + info;
    }
}
```



## 10_多实例服务负载均衡

新建nacos-stock2 module. 端口不一样，其他都一样，即可

## 11_服务领域模型

![image-20210705141125822](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210705141125822.png)

application.yml

```yaml
server:
  port: 8084
  servlet:
    context-path: /

# 配置服务发现
spring:
  application:
    name: nacos-stock
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.132.111:8848
        namespace: a432743b-9171-46c4-8a08-9128a7a9aa0c	# 定义namespace
        cluster-name: java1234	# 集群名称
```

# Nacos高可用集群

## 12_Nacos高可用集群

为了防止一个nacos崩掉，我们可以搞个3个或者3个以上的nacos集群部署，实现高可用；

  **startup.bat Model改成cluster集群模式**

```sh
55 export MODE="standalone"
```

修改 **application.properties** 端口改下； nacos程序文件复制三分，端口分别搞成 8848，8849 ， 8850；

```properties
server.port=8848
```

**cluster.conf.example改成cluster.conf**

```properties
# 以及配置上ip列表：
127.0.0.1:8848
127.0.0.1:8849
127.0.0.1:8850
```

项目的bootstrap.properties

```properties
spring.cloud.nacos.config.server-addr=192.168.132.111:8848,192.168.132.111:8849,192.168.132.111:885
```







 
