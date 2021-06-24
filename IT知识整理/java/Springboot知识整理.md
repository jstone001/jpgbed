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

# yml设置项目起始页

```yaml
server:
  servlet:
    context-path: /index.html
```

# yml设置Sqlite

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

# Cannot determine embedded database driver class for database type NONE

from: https://blog.csdn.net/Master_Shifu_/article/details/80420099

```xml
<dependency>
        <groupId>com.h2database</groupId> 
        <artifactId>h2</artifactId>
        <scope>runtime</scope> 
</dependency>
```

# application.yml文件不起作用

from: https://blog.csdn.net/czjlghndcy/article/details/109552528

```xml
pom.xml去掉 <packaging>pom</packaging>
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







