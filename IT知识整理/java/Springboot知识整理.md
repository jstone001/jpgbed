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



