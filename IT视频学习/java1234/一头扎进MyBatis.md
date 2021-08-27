## 《一头扎进MyBatis3》第一讲 HelloWorld

jdbc.properties

```properties
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://192.168.132.25:3306/db_mp
jdbc.username=root
jdbc.password=123

```

mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<properties resource="jdbc.properties"/>
	<!--<typeAliases>-->
		<!--<typeAlias alias="Student" type="com.java1234.model.Student"/>-->
	<!--</typeAliases>-->
	<environments default="development">
		<environment id="development">
			<transactionManager type="JDBC" />
			<dataSource type="POOLED">
				<property name="driver" value="${jdbc.driverClassName}" />
				<property name="url" value="${jdbc.url}" />
				<property name="username" value="${jdbc.username}" />
				<property name="password" value="${jdbc.password}" />
			</dataSource>
		</environment>
	</environments>
	<mappers>
		<mapper resource="mappers/DepartmentMapper.xml" />
	</mappers>
</configuration>

```

SqlSessionFactoryUtil.java

```java
package com.sw.util;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionFactoryUtil {

	private static SqlSessionFactory sqlSessionFactory;

	public static SqlSessionFactory getSqlSessionFactory(){
		if(sqlSessionFactory==null){
			InputStream inputStream=null;
			try{
				inputStream=Resources.getResourceAsStream("mybatis-config.xml");
				sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return sqlSessionFactory;
	}

	public static SqlSession openSession(){
		return getSqlSessionFactory().openSession();
	}
}
```



## 《一头扎进MyBatis3》第二讲 项目配置

mybatis-config.xml

```xml
<!-- 扫描包，取别名   -->	
<typeAliases>
    <package name="com.java1234.model"/>
</typeAliases>

<!-- 扫描mapper   -->
 
```





## 

