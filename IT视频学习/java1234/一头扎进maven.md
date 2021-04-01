# 第1讲 记事本写maven

```xml
  <modelVersion>4.0.0</modelVersion>        <!-- 固定版本 -->
  <groupId>com.java1234.HelloWorld</groupId>
  <artifactId>HelloWorld</artifactId>
  <version>0.0.1-SNAPSHOT</version>        <!-- 坐标 -->
```

mvn compile
mvn clean
mvn test
mvn package    

mvn 下载路径
setting.xml
```xml
 <localRepository>d:\j2ee_dev\repository</localRepository>
```

# 第2讲：    在eclipse 写maven
maven 插件下载：
    m2e 
     http://download.eclipse.org/technology/m2e/releases

## 第2节 在eclipse中写hello world

本地库配置

# 第3讲 maven核心技术

maven-model-builder-3.2.5.jar 中pom.xml
# 第4讲
    maven web配置
# 第5讲
    maven 聚合

# 第6讲 依赖范围
    scope
        Compile
        Test
        Provided: 已提供依赖范围    (测试有效，运行无效)
        System: 系统依赖范围(了解即可)
        Import: 导入依赖范围(了解即可)