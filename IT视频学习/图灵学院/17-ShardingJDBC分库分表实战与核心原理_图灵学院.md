from: https://vip.tulingxueyuan.cn/detail/v_62414b77e4b09dda1253246c/3

# ShardingSphere产品介绍

shardingProxy： 自己是一个服务，伪装成一个mysql

# ShardingSphere生态定位

![image-20220517180634276](E:\JS\booknote\jpgBed\image-20220517180634276.png)

# ShardingSphere实战

## ShardingJDBC

### sql

```sql
# course.sql
CREATE TABLE course_1 (
	cid BIGINT(20) PRIMARY KEY,
	cname VARCHAR(50) NOT NULL,
	user_id BIGINT(20) NOT NULL,
	cstatus varchar(10) NOT NULL
);

CREATE TABLE course_2 (
	cid BIGINT(20) PRIMARY KEY,
	cname VARCHAR(50) NOT NULL,
	user_id BIGINT(20) NOT NULL,
	cstatus varchar(10) NOT NULL
);

insert into course(cname,user_id,cstatus) values ('shardingProxy',100,'1');

```

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.sw</groupId>
    <artifactId>shardingDemo3</artifactId>
    <version>1.0</version>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <version>2.4.5</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>2.4.5</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.23</version>
        </dependency>

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.3.2</version>
        </dependency>

        <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
            <version>4.1.1</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
            <version>2.4.5</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

application.properties

```properties
#垂直分表策略
# 配置真实数据源
spring.shardingsphere.datasource.names=m1

# 配置第 1 个数据源
spring.shardingsphere.datasource.m1.type=com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.m1.driver-class-name=com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.m1.url=jdbc:mysql://localhost:3306/db_test?serverTimezone=UTC
spring.shardingsphere.datasource.m1.username=root
spring.shardingsphere.datasource.m1.password=123


# 指定表的分布情况 配置表在哪个数据库里，表名是什么。水平分表，分两个表：m1.course_1,m1.course_2
spring.shardingsphere.sharding.tables.course.actual-data-nodes=m1.course_$->{1..2}

# 指定表的主键生成策略
spring.shardingsphere.sharding.tables.course.key-generator.column=cid
spring.shardingsphere.sharding.tables.course.key-generator.type=SNOWFLAKE

#雪花算法的一个可选参数
spring.shardingsphere.sharding.tables.course.key-generator.props.worker.id=1

#使用自定义的主键生成策略
#spring.shardingsphere.sharding.tables.course.key-generator.type=MYKEY
#spring.shardingsphere.sharding.tables.course.key-generator.props.mykey-offset=88

# 指定分片策略 约定cid值为偶数添加到course_1表。如果是奇数添加到course_2表。
# 选定计算的字段ff。 分片键
spring.shardingsphere.sharding.tables.course.table-strategy.inline.sharding-column= cid     
# 根据计算的字段算出对应的表名。（分片策略）
spring.shardingsphere.sharding.tables.course.table-strategy.inline.algorithm-expression=course_$->{cid%2+1}

# 打开sql日志输出。
spring.shardingsphere.props.sql.show=true

spring.main.allow-bean-definition-overriding=true
```

ShardingTest.java

```java
package com.sw;

import com.sw.entity.Course;
import com.sw.mapper.CourseMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShardingTest {

    @Resource
    private CourseMapper courseMapper;

    @Test
    public void addCourse(){
        for(int i=0;i<10;i++) {
            Course course = new Course();
            course.setCname("java test");
            course.setUserid(2L);
            course.setCstatus("1");

            courseMapper.insert(course);
        }
    }
}
```



# 分库分表与多数据源的切换