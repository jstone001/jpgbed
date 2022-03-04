# 第1讲 简介

http://blog.java1234.com/blog/articles/329.html

```
1. 创建独立的Spring应用程序
2. 嵌入的Tomcat，无需部署WAR文件
3. 简化Maven配置
4. 自动配置Spring
5. 提供生产就绪型功能，如指标，健康检查和外部配置
6. 绝对没有代码生成和对XML没有要求配置
```

```java
@ResponseBody
@RestController
```

# 第2讲 项目属性配置

## 第1节 项目内置配置
application.properties下
## 第2节 系统内置
```properties
server.context-path=/HelloWorld		#开始页
server.port=8080

helloworld=spring root
helloworld=springboot大爷你好
```
## 第3节 自定义配置
```java
@Value("${helloworld}")
private String helloworld;
```


```java
@Value("${helloworld}")
private String helloWorld;
        
```
```properties
mysql.jdbcName=com.mysql.jdbc.Driver
mysql.dbUrl=jdbc:mysql://localhost:3306/db_root
mysql.userName=root
mysql.password=123
```
```java
//程序内的调用
    @Value("${mysql.jdbcName}")
    private String jdbcName;
    @Value("${mysql.dbUrl}")
    private String dbUrl;
    @Value("${mysql.userName}")
    private String userName;
    @Value("${mysql.password}")
    private String password;
    
        @RequestMapping("/showJdbc")
    public String showJdbc(){
        return "jdbcName:"+jdbcName+"<br/>"
                +"dbUrl:"+dbUrl+"<br/>";
    }
```
## 第4节 Configuration配置
MysqlProperties.java
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
调用
```java
@Resource
private MysqlProperties mysqlProperties;
```

# 第3讲 springboot的MVC支持

## 第1节 @RequestMapping对url的支持

## 第2节 @Controller对http请求

```java
    @RequestMapping("/say")
    public ModelAndView say(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", "Springboot 大爷你好");
        mav.setViewName("helloworld");
        return mav;
    }
```



## 第3节 @RestController处理ajax
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>

    <script src="http://www.java1234.com/jquery-easyui-1.3.3/jquery.min.js"></script>
</head>
<body>
    <button onclick="show()">你好</button> 
</body>
<script language="JavaScript">

    function show() {
        $.post("/ajax/hello",{},function (result) {
            alert(result);
        });
    }
</script>
</html>
```
HelloAjaxController.java
```java
package com.sw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ajax")
public class HelloAjaxController {

    @RequestMapping("/hello")
    public String say(){

        return "{'message1':'小明你好','message2':'小红你好'}";
    }
}
```
index.html
```java
    function show() {
        $.post("/ajax/hello",{},function (result) {
            alert(result);
            var obj=eval("("+result+")");       //解析json(important)
            $("#tt").html(obj.message1);

        });
    }
```

## 第4节 @PathVariable获取url参数
```java
@RequestMapping("/{id}")
public ModelAndView show(@PathVariable("id") Integer id){

ModelAndView modelAndView = new ModelAndView();
modelAndView.addObject("id",id);
modelAndView.setViewName("blog");
return modelAndView;
}
```

## 第5节 @RequestParam获取请求参数
```java
@RequestMapping("/query")
public ModelAndView query(@RequestParam(value = "q",required = false) String q ){

ModelAndView modelAndView = new ModelAndView();
modelAndView.addObject("q",q);
modelAndView.setViewName("qq");
return modelAndView;
}
```

# 第4讲 对Spring Data Jpa的支持 
## 第1节 简介
## 第2节 基本CRUD实现

application.yml

```yaml
server:
  servlet:
    context-path: /index.html

  port: 8888
 
spring:
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



Book
```java
package com.sw.bo;

import javax.persistence.*;

@Entity
@Table(name="tb_book")
public class Book {


    @Id
    @GeneratedValue
    private Integer id;
    @Column(length = 100)
    private String name;
    @Column(length = 100)
    private String author;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

```
BookDao.java
```java
package com.sw.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sw.bo.Book;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookDao extends JpaRepository<Book, Integer>{

    @Query("select b from Book b where b.name like %?1%")
    public List<Book> findByName(String name);

}


```
BookController.java
```java
package com.ajs.springbootdemo4.controller;


import com.ajs.springbootdemo4.bo.Book;
import com.ajs.springbootdemo4.dao.BookDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookDao bookDao;

    /**
     * 查询所有图书
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("bookList", bookDao.findAll());
        mav.setViewName("bookList");
        return mav;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String add(Book book){
        bookDao.save(book);
        return "forward:/book/list";
    }

    @RequestMapping("/preUpdate/{id}")
    public ModelAndView preUpdate(@PathVariable("id") Integer id){
        ModelAndView mav = new ModelAndView();
        mav.addObject("book", bookDao.getOne(id));
        mav.setViewName("bookUpdate");
        return mav;

    }

    /**
     * update
     * @param book
     * @return
     */
    @PostMapping(value = "/update")
    public String update(Book book){
        bookDao.save(book);
        return "forward:/book/list";
    }

    /**
     * delete
     */
    @GetMapping("/delete")
    public String delete(Integer id){
        bookDao.delete(id);
        return "forward:/book/list";

    }
}

```
BookList.ftl
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>图书</title>
</head>
<body>
<a href="/bookAdd.html">新增</a>
    <table>
        <tr>
            <th>编号</th>
            <th>书名</th>
            <th>作者</th>
            <th>操作</th>
        </tr>
        <#list bookList as book>
            <tr>
              <td>${book.id}</td>
              <td>${book.name}</td>
              <td>${book.author}</td>
              <td>
                  <a href="/book/preUpdate/${book.id}">修改</a>
                  <a href="/book/delete?id=${book.id}">删除</a>
              </td>
            </tr>
        </#list>

    </table>
</body>
</html>
```
bookUpdate.ftl

```html
<body>
<form action="/book/update" method="post">
    <input type="hidden" value="${book.id}" name="id" />
    书名：<input name="name" type="text" value="${book.name}"/> <br/>
    作者：<input name="author" type="text" value="${book.author}" /> <br/>
    <input type="submit" value="提交" /> <input type="reset" value="重置" />
</form>
</body>
```

# 第5讲 对Spring Data Jpa的支持 

## 第3节 自定义查询@Query
BookDao.java
```java
public interface BookDao extends JpaRepository<Book, Integer>{  

    @Query("select b from Book b where b.name like %?1%")
    List<Book> findByName(String name);

    @Query(value="select * from tb_book order by rand() limit ?1",nativeQuery = true)   //nativeQuery本地sql. 默认false为HQL
    List<Book> randomList(Integer n);
}
```
## 第4节 动态查询Specification使用
 BookDao.java
 ```java
 public interface BookDao extends JpaRepository<Book, Integer>, JpaSpecificationExecutor {      //继承JpaSpecificationExecutor 

}
 ```
 BookController
 ```java
    /**
     * 根据动态条件查询
     * @return
     */
    @RequestMapping("/list2")
    public ModelAndView list2(Book book){

        ModelAndView mav = new ModelAndView();
        List<Book> bookList=bookDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {

                Predicate predicate= criteriaBuilder.conjunction();
                if(book!=null){
                    if(book.getName()!=null&&book.getName()!=""){
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + book.getName() + "%"));
                    }
                    if(book.getAuthor()!=null&&book.getAuthor()!=""){
                        predicate.getExpressions().add(criteriaBuilder.like(root.get("author"), "%" + book.getAuthor() + "%"));
                    }
                }
                return predicate;
            }
        });
        mav.addObject("bookList", bookList);
        mav.setViewName("bookList");
        return mav;
    }
    
 ```
# 第6讲 Springboot事务管理
第5章 SpringBoot事务管理

AccountServiceImpl.java

 ```java
package com.ajs.springbootdemo4.service.impl;

import com.ajs.springbootdemo4.bo.Account;
import com.ajs.springbootdemo4.dao.AccountDao;
import com.ajs.springbootdemo4.service.AccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountDao accountDao;

    @Transactional
    @Override
    public void transferAccounts(int fromUserId, int toUserId, float money) {
        Account fromAccount = accountDao.getOne(fromUserId);
        fromAccount.setBlance(fromAccount.getBlance() - money);
        accountDao.save(fromAccount);

        int z=1/0;
        Account toAccount = accountDao.getOne(toUserId);
        toAccount.setBlance(toAccount.getBlance() + money);
        accountDao.save(toAccount);
    }
}
 ```

# 第7讲 Springboot表单验证

http://blog.java1234.com/blog/articles/336.html

StudentAdd.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<script src="jquery-1.11.2.min.js"></script>
<script type="text/javascript">

    function submitData() {
        $.post("/student/add",{name:$("#name").val(),age:$("#age").val()},
            function(result){
                alert(result);
        });
    }

</script>
<body>

<button onclick="show()">你好</button>
<br/>
<span id="tt"></span>

<hr/>
    姓名：<input type="text" name="name" id="name"/><br/>
    年龄：<input type="text" name="age" id="age"/><br/>
    <input type="button" value="提交" onclick="submitData()"/>
</body>
</html>
```



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
            return bindingResult.getFieldError().getDefaultMessage();
        }else{
            studentService.add(student);
            return "添加成功";
        }
    }
}

```

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

# 第8讲 Springboot切面AOP

http://blog.java1234.com/blog/articles/337.html

```java
@Aspect注解是切面注解类
@Pointcut切点定义
@Before是方法执行前调用
@After是方法执行后调用
@AfterReturning方法执行返回值调用
```

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

    private static final Logger logger = LoggerFactory.getLogger(RequestAspect.class);

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

StudentController.java

```java
package com.java1234.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java1234.entity.Student;
import com.java1234.service.StudentService;

/**
 * 学生信息controller层
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/student")
public class StudentController {

	@Resource
	private StudentService studentService;
	
	/**
	 * 添加学生信息
	 * @param student
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping("/add")
	public String add(@Valid Student student,BindingResult bindingResult){
		if(bindingResult.hasErrors()){
			return bindingResult.getFieldError().getDefaultMessage();
		}else{
			studentService.add(student);
			return "添加成功";
		}
	}
}

```



 