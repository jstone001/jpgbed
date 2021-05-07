# 第1讲 简介

# 第2讲 项目属性配置

## 第1节 项目内置配置
application.properties下
## 第2节 系统内置
```yml
#server.context-path=/He
server.port=8080
```
## 第3节 自定义配置
```java
helloworld=springboot大爷你好

    @Value("${helloworld}")
    private String helloworld;
```


```java
    @Value("${helloworld}")
    private String helloWorld;
    
    
```
```java
mysql.jdbcName=com.mysql.jdbc.Driver
mysql.dbUrl=jdbc:mysql://localhost:3306/db_root
mysql.userName=root
mysql.password=123

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
BookDao
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
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String add(Book book){
        bookDao.save(book);
        return "forward:/book/list";
    }

    /**
     * 根据id查找book
     * @param id
     * @return
     */
    @RequestMapping("/preUpdate/{id}")
    public ModelAndView preUpdate(@PathVariable("id") Integer id){
        ModelAndView mav = new ModelAndView();
        Book book = bookDao.getOne(id);
        mav.addObject("book", book);
        mav.setViewName("/bookUpdate");
        return mav;
    }
    
    @PostMapping(value = "/update")
    public String update(Book book){
        bookDao.save(book);
        return "forward:/book/list";
    }
    


    @GetMapping("/delete")
    public String delete(Integer id){
        bookDao.delete(id);
        return "forward:/book/list";

    }
```
BookList.ftl
```html
    <table>
        <tr>
            <th>id</th><th>书名</th><th>作者</th><th>操作</th>
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
```
# 第5讲 对Spring Data Jpa的支持 
## 自定义查询@Query
BookDao.java
```java
public interface BookDao extends JpaRepository<Book, Integer>{

    @Query("select b from Book b where b.name like %?1%")
    List<Book> findByName(String name);

    @Query(value="select * from tb_book order by rand() limit ?1",nativeQuery = true)   //nativeQuery本地sql. 默认false为HQL
    List<Book> randomList(Integer n);
}
```
## 动态查询Specification使用
 BookDao
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
> 第5章 SpringBoot事务管理

 ```java
     @Override
    @Transactional
    public void transferAccount(int fromUserId, int toUserId, float money) {

        Account fromAccount = accountDao.getOne(fromUserId);
        fromAccount.setBlance(fromAccount.getBlance()-money);
        accountDao.save(fromAccount);

//        int z=1/0;
        Account toAccount = accountDao.getOne(toUserId);
        toAccount.setBlance(toAccount.getBlance() + money);
        accountDao.save(toAccount);
    }
 ```

# 第7讲 Springboot表单验证
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

# 第8讲 Springboot切面AOP
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

    private Logger logger = Logger.getLogger(RequestAspect.class);

    @Pointcut("execution(public * com.sw.controller.*.*(..))")
    public void log(){

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes sra= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=sra.getRequest();
        logger.info("url: " + request.getRequestURI());
        logger.info("ip: " + request.getRemoteHost());
        logger.info("method: " + request.getMethod());
        logger.info("class_method: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("args: "+joinPoint.getArgs());
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



 