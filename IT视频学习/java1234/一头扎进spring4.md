# 第1讲
<font color='red'>ioc 控制反转</font>

<font color='red'>aop 面向切面</font>

# 第2讲
ioc  inverse of control
把控制权放在bean中

# 第3讲
依赖注入 property
- 属性注入: <property name="name" value="张三"></property>
- 构造方法注入：
```xml
<constructor-arg type="int" value="12" />       <!-- 类型  -->  
<constructor-arg index="1" value="12" />        <!-- 索引  -->  
```
- 工厂方法注入：    
```xml
<factory-bean factory-method>       <!-- 非静态  -->
<bean factory-method   >            <!-- 直接类名，静态 -->
```
- 泛型依赖注入：

# 第4讲
## 第5节 注入参数
bean 注入
```xml
    <property name="dog" ref="dog1"></property>
```
内部bean
null注入
```xml
	<bean id="people4" class="com.java1234.entity.People">
		<property name="id" value="2"></property>
		<property name="name" value="赵四"></property>
		<property name="age" value="11"></property>
		<property name="dog" >
			<null></null>
		</property>
	</bean>
```
    级联注入
```xml
    <property name="dog.name" value="Tom"></property>
```
集合注入：     list set map  properties 
```xml
		<property name="address">
			<props>
				<prop key="add1">aaa</prop>
				<prop key="add2">bbb</prop>
			</props>
		</property>
```
# 第5讲
## 第6节 自动装配(少用)
```
    default-autowire="byName"
    byName
    byType
    default-autowire="constructor"      //通过构造方法自动装配
```

## 第7节 方法注入
```xml
    scope="prototype"   <!-- 多态 -->
    
    <!-- 方法注入(很少用) -->
    <lookup-method name="getDog" bean="dog"/>    <!-- 每个方法不一样 -->
```
## 第8节 方法替换(极少用)   

```xml
<replaced-method name="getDog" replacer="people2"></replaced-method>
ac.getBean("dog")   <!-- 每次获得新的bean -->
```
# 第6讲 第2章 IOC详解

## 第9节 bean之间的关系

继承 (抽象bean)
```xml
	<bean id="abstractPeople" class="com.java1234.entity.People" abstract="true">
		<property name="className" value="高三5班"></property>
		<property name="age" value="19"></property>
	</bean>
	
	<bean id="zhangsan" parent="abstractPeople" ></bean>
```
依赖
```xml
	<bean id="zhangsan" parent="abstractPeople" depends-on="authority">
```
引用
```xml
    <property name="dog" ref="dog"></property>
```

## 第10节 bean的作用范围

- singleton   单例
- prototype   多例
- request
- session
- global session
- application


# 第7讲 AOP详解
    aspect oriented programming
    主要作用：对业务逻辑进行分离，解耦

# 第8讲 
## 第2节 AOP实例

前置通知
```xml
	public void doBefore(JoinPoint jp){
		System.out.println("类名"+jp.getTarget().getClass().getName());
		System.out.println("方法名"+ jp.getSignature().getName());
		System.out.println("开始添加学生");
	}
	
--------------------------------------------------
	<aop:config>
		<aop:aspect id="studentServiceAspect" ref="studentServiceAspect">
			<aop:pointcut expression="execution(* com.java1234.service.*.*(..))" id="businessService"/>
			<aop:before method="doBefore" pointcut-ref="businessService"/>
			<aop:after method="doAfter" pointcut-ref="businessService"/>
		</aop:aspect>
	</aop:config>
```
    后置通知
    环绕通知
```xml
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable{
		System.out.println("添加学生前");
		Object retVal=pjp.proceed();
		System.out.println(retVal);
		System.out.println("添加学生后");
		return retVal;
	}
	
---------------------------------------------------------
	<aop:config>
		<aop:aspect id="studentServiceAspect" ref="studentServiceAspect">
			<aop:pointcut expression="execution(* com.java1234.service.*.*(..))" id="businessService"/>
			<aop:before method="doBefore" pointcut-ref="businessService"/>
			<aop:after method="doAfter" pointcut-ref="businessService"/>
			<aop:around method="doAround" pointcut-ref="businessService"/>
			<aop:after-returning method="doAfterReturning" pointcut-ref="businessService"/>
			<aop:after-throwing method="doAfterThrowing" pointcut-ref="businessService" throwing="ex"/>
		</aop:aspect> 
	</aop:config>
```

返回通知

异常通知
```xml
	public void doAfterThrowing(JoinPoint jp,Throwable ex){
		System.out.println("异常通知");
		System.out.println("异常信息："+ex.getMessage());
	}
	
------------------------------------------------------
	<aop:after-throwing method="doAfterThrowing" pointcut-ref="businessService" throwing="ex"/>
```

# 第9讲 第4章 Spring对dao的支持
## 第1节 Spring对jdbc的支持
dbcp: common-dbcp.jar  common-pool.jar


JdbcTemplate
```xml
	<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
		<property name="dataSource" ref="dataSource"></property>
	</bean>
```
dao层要引入JdbcTemplate
```properties
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/db_spring
jdbc.username=root
jdbc.password=123
```

# 第10讲 
JdbcDaoSupport ★
dao层 extendsJdbcDaoSupport


NamedParameterJdbcTemplate

# 第11讲 第5章 spring对事务的支持
## 第1节 事务简介
- 原子性
- 一致性
- 隔离性
- 持久性

## 第2节 编程式事务管理

```xml
	<bean id="transactionManager"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource" />
	</bean>
	
		<bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
		<property name="transactionManager" ref="transactionManager"></property>
	</bean>
```

## 第3节 声明式事务管理
1. 以xml配置声明式事务 ★
```xml
    <!-- 配置事务通知 -->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
    	<tx:attributes>  
    	    <tx:method name="*"/>
    	 </tx:attributes>
    </tx:advice>
    
    <!-- 配置事务切面 -->
    <aop:config>
    	<!-- 配置切点 -->
    	<aop:pointcut id="serviceMethod" expression="execution(* com.java1234.service.*.*(..))" />
    	<!-- 配置事务通知 -->
    	<aop:advisor advice-ref="txAdvice" pointcut-ref="serviceMethod"/>
    </aop:config>
```
2. 注解式配置声明事务

## 第4节 事务的传播行为
```xml
    	<tx:attributes>  
            <tx:method name="insert*" propagation="REQUIRED" />  
            <tx:method name="update*" propagation="REQUIRED" />  
            <tx:method name="edit*" propagation="REQUIRED" />  
            <tx:method name="save*" propagation="REQUIRED" />  
            <tx:method name="add*" propagation="REQUIRED" />  
            <tx:method name="new*" propagation="REQUIRED" />  
            <tx:method name="set*" propagation="REQUIRED" />  
            <tx:method name="remove*" propagation="REQUIRED" />  
            <tx:method name="delete*" propagation="REQUIRED" />  
            <tx:method name="change*" propagation="REQUIRED" />  
            <tx:method name="get*" propagation="REQUIRED" read-only="true" />  
            <tx:method name="find*" propagation="REQUIRED" read-only="true" />  
            <tx:method name="load*" propagation="REQUIRED" read-only="true" />  
            <tx:method name="*" propagation="REQUIRED" read-only="true" />  
        </tx:attributes>  
```
# 第13讲 spring4整合hibernate, struts
web.xml
```xml
<!-- 对Spring的支持 -->
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener> 

```
```java
@Service("userService") //自动生成bean id=userService
public class UserServiceImpl implements UserService{
}
	@Resource      //自动注入 =set方法
	private BaseDao<User> baseDao;
```
```java
    @Repository //dao层
    @Controller     //控制层
```


# 第14讲 spring4整合

@Resource 代替 setter 方法
```xml
	<!-- 自动加载构建bean -->
	<context:component-scan base-package="com.sw" />
```
使用 component-scan 必须在Class标注 @Controller @Service @Repository @Component