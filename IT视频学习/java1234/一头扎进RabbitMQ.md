

# 1. RabbitMQ概述和基本实现

## 01_RabbitMQ 简介

http://blog.java1234.com/blog/articles/775.html

官网：http://rabbitmq.com

- MQ全称为Message Queue, [消息队列](https://baike.baidu.com/item/消息队列/4751675)（MQ）是一种应用程序对应用程序的通信方法。应用程序通过读写出入队列的消息（针对应用程序的数据）来通信，而无需专用连接来链接它们。消息传递指的是程序之间通过在消息中发送数据进行通信，而不是通过直接调用彼此来通信，直接调用通常是用于诸如远程过程调用的技术。排队指的是应用程序通过 队列来通信。队列的使用除去了接收和发送应用程序同时执行的要求。
- RabbitMQ是使用Erlang语言开发的开源消息队列系统，基于AMQP协议来实现。AMQP的主要特征是面向消息、队列、路由(包括点对点和发布/订阅)、可靠性、 安全。AMQP协议更多用在企业系统内，对数据一致性、稳定性和可靠性要求很高的场景，对性能和吞吐量的要求还在其次。

### **Kafka、RabbitMQ、RocketMQ等消息中间件的对比**

http://blog.java1234.com/blog/articles/774.html

## 02_RabbitMQ使用场景

http://blog.java1234.com/blog/articles/775.html

优点：解耦，异步，削峰

## 03_RabbitMQ安装

docker

```sh
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
docker run -it --rm --name rabbitmq2 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

5672是rabbitmq 默认TCP监听商品

15672是rabbitMQ的web 管理界面端口

## 04_RabbitMQ运作原理

http://blog.java1234.com/blog/articles/776.html

![image-20210629094521509](E:\JS\booknote\jpgBed\image-20210629094521509.png)

1. Server：Broker，接受client连接，实现AMQP实体服务　　

2. Connection：应用程序和Broker的网络连接　　

3. Channel：网络信道，读写都是在Channel中进行（NIO的概念），包括对MQ进行的一些操作（例如clear queue等）都是在Channel中进行，客户端可建立多个Channel，每个Channel代表一个会话任务　　

4. Message：由properties（有消息优先级、延迟等特性）和Body（消息内容）组成　　

5. Virtual host：用于消息隔离（类似Redis 16个db这种概念），最上层的消息路由，一个包含若干Exchange和Queue，同一个里面Exchange和Queue的名称不能存在相同的。　　

6. Exchange：Routing and Filter　　

7. Binding：把Exchange和Queue进行Binding　　

8. Routing key：路由规则　　

9. Queue：物理上存储消息

## 05_基本测试环境搭建

### 1.  新建项目rabbitmqtest

rabbitmq-parent/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.sw</groupId>
    <artifactId>rabbitmqtest</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>rabbitmq-producer</module>
        <module>rabbitmq-consumer</module>
        <module>rabbitmq-common</module>
    </modules>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <springboot.version>2.3.2.RELEASE</springboot.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${springboot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>

```

### 2. 新建module，rabbitmq-common, rabbitmq-porducer, rabbitmq-consumer

rabbitmq-common/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <artifactId>rabbitmqtest</artifactId>
        <groupId>com.sw</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <groupId>com.sw</groupId>
    <artifactId>rabbitmq-common</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <!-- rabbmitmq -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>

    </dependencies>
</project>

```

rabbitmq-porducer/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <artifactId>rabbitmqtest</artifactId>
        <groupId>com.sw</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <groupId>com.sw</groupId>
    <artifactId>rabbitmq-producer</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>com.sw</groupId>
            <artifactId>rabbitmq-common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>

</project>
```

rabbit-consumer/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <artifactId>rabbitmqtest</artifactId>
        <groupId>com.sw</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <groupId>com.sw</groupId>
    <artifactId>rabbitmq-consumer</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>com.sw</groupId>
            <artifactId>rabbitmq-common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>

</project>

```

rabbitmq-producer/ProducerApplication.java

```java
package com.sw.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }
}
```

rabbitmq-consumer/ConsumerApplication.java

```java
package com.sw.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
```

rabbitmq-producer/application.yml

```yaml
server:
  port: 80
```

rabbitmq-consumer/application.yml

```yaml
server:
  port: 81
```

## 06_RabbitMQ生产者Producer实现

application.yml

```yaml
server:
  port: 80

spring:
  rabbitmq:
    host: 192.168.132.111
    port: 5672
    username: admin
    password: admin
    virtual-host: /
```

ProducerApplication.java

```java
package com.sw.producer;

import com.sw.producer.service.RabbitMqService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        ApplicationContext ac=SpringApplication.run(ProducerApplication.class, args);
        RabbitMqService rabbitMqService= (RabbitMqService) ac.getBean("rabbitMqService");
        rabbitMqService.sendMessage("rabbitMQ大爷你好！");
    }
}

```

RabbitMQConfig.java

```java
package com.sw.producer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String  DIRECT_EXCHAGE="directExchage";
    public static final String  DIRECT_QUEUE="directQueue";
    /**
     * 路由规则
     * direct路由key
     */

    public static final String  DIRECT_ROUTINGKEY="directRoutingKey";

    /**
     * 定义一个direct交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHAGE);
    }

    /**
     * 定义一个direct队列
     * @return
     */
    @Bean
    public Queue directQueue() {
        return new Queue(DIRECT_QUEUE);
    }

    /**
     * 定义一个队列和交换机的绑定规则
     * @return
     */
    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with(DIRECT_ROUTINGKEY);
    }
}

```

RabbitMqServiceImpl.java

```java
package com.sw.producer.service.impl;

import com.sw.producer.config.RabbitMQConfig;
import com.sw.producer.service.RabbitMqService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("rabbitMqService")
public class RabbitMqServiceImpl implements RabbitMqService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * String exchange
     * String routingKey
     * Object object
     * @param message
     */
    @Override
    public void sendMessage(String message) {
        amqpTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHAGE,RabbitMQConfig.DIRECT_ROUTINGKEY,message);
    }
}
```

Queue参数

- durable：是否持久化，true 交换机或者队列会存到本地文件数据库，当mq重启，依然在，false的话，重启或者断电，就没了；默认true
- autoDelete：是否自动删除，true当没有Consumer消费者时候，自动删除掉；默认false
- exclusive:是否独占：true的话只有一个消费者监听这个队列。默认false

## 07_RabbitMQ消费者者Consumer实现

rabbitMqConfig.java 放到rabbitmq-common中

rabbitmq-consumer/RabbitMqServiceImpl.java

```java
package com.sw.consumer.service.impl;

import com.sw.config.RabbitMQConfig;
import com.sw.consumer.service.RabbitMqService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("rabbitMqService")
public class RabbitMqServiceImpl implements RabbitMqService {

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Override
    public void receiveMessage() {
        String message= (String) amqpTemplate.receiveAndConvert(RabbitMQConfig.DIRECT_QUEUE);
        System.out.println("接收到MQ消息："+message);
    }

    @Override
    @RabbitListener(queues={RabbitMQConfig.DIRECT_QUEUE})	//监听消费队列 
    public void receiveMessage2(String message) {
        System.out.println("接收到MQ消息："+message);
    }
}
```

ConsumerApplication.java

```java
package com.sw.producer;

import com.sw.producer.service.RabbitMqService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        ApplicationContext ac=SpringApplication.run(ProducerApplication.class, args);
        RabbitMqService rabbitMqService= (RabbitMqService) ac.getBean("rabbitMqService");
        rabbitMqService.sendMessage("rabbitMQ大爷你好！");
    }
}
```



# 2. RabbitMQ生产方式

## 08_Work queues工作模式

![image-20210629141615629](E:\JS\booknote\jpgBed\image-20210629141615629.png)

- 工作模式是一个或者多个消费者共同消费一个队列中的消息；
- 队列中的每一个消息只可能被其中一个消费者消费；
- 简单比如，多个人吃一个蛋糕；

应用场景：对于消息任务很多的情况，可以使用工作队列提高任务处理的速度；

原理就是集群处理大量的消息；

rabbitmq-producer/ProducerApplication.java

```java
package com.sw.producer;

import com.sw.producer.service.RabbitMqService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        ApplicationContext ac=SpringApplication.run(ProducerApplication.class, args);
        RabbitMqService rabbitMqService= (RabbitMqService) ac.getBean("rabbitMqService");
        for (int i=0;i<10;i++){
            rabbitMqService.sendMessage("rabbitMQ大爷你好！"+i);	//发送10条消息
        }
    }
}
```

rabbitmq-consumer/RabbitMqServiceImpl.java

```java
package com.sw.consumer.service.impl;

import com.sw.config.RabbitMQConfig;
import com.sw.consumer.service.RabbitMqService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("rabbitMqService")
public class RabbitMqServiceImpl implements RabbitMqService {

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Override
    public void receiveMessage() {
        String message= (String) amqpTemplate.receiveAndConvert(RabbitMQConfig.DIRECT_QUEUE);
        System.out.println("接收到MQ消息："+message);
    }

    @Override
    @RabbitListener(queues={RabbitMQConfig.DIRECT_QUEUE})
    public void receiveMessage2(String message) {		//2个消费者
        System.out.println("消费者1：接收到MQ消息："+message);
    }

    @Override
    @RabbitListener(queues={RabbitMQConfig.DIRECT_QUEUE})
    public void receiveMessage3(String message) {		//2个消费者
        System.out.println("消费者2：接收到MQ消息："+message);
    }
}
```

## 09_订阅发布工作模式利用fanout交换机

![image-20210629143807337](E:\JS\booknote\jpgBed\image-20210629143807337.png)

Pub/Sub订阅模式：

- 生产者生产的消息，所有订阅过的消费者都能够接收到消息；
- Exchange采用Fanout类型，即广播方式。
-  Fanout类型的交换机会把消息发送到所有绑定到该交换机的队列；

适合场景：手机短信，APP,邮件消息推送。

