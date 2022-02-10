# 简介和HelloWorld

## 01_mybatis-plus介绍

官网：https://mp.baomidou.com

**特性**

- 无侵入：只做增强不做改变，引入它不会对现有工程产生影响，如丝般顺滑
- 损耗小：启动即会自动注入基本 CURD，性能基本无损耗，直接面向对象操作
- 强大的 CRUD 操作：内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作，更有强大的条件构造器，满足各类使用需求
- 支持 Lambda 形式调用：通过 Lambda 表达式，方便的编写各类查询条件，无需再担心字段写错
- 支持主键自动生成：支持多达 4 种主键策略（内含分布式唯一 ID 生成器 - Sequence），可自由配置，完美解决主键问题
- 支持 ActiveRecord 模式：支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可进行强大的 CRUD 操作
- 支持自定义全局通用操作：支持全局通用方法注入（ Write once, use anywhere ）
- 内置代码生成器：采用代码或者 Maven 插件可快速生成 Mapper 、 Model 、 Service 、 Controller 层代码，支持模板引擎，更有超多自定义配置等您来使用
- 内置分页插件：基于 MyBatis 物理分页，开发者无需关心具体操作，配置好插件之后，写分页等同于普通 List 查询
- 分页插件支持多种数据库：支持 MySQL、MariaDB、Oracle、DB2、H2、HSQL、SQLite、Postgre、SQLServer 等多种数据库
- 内置性能分析插件：可输出 Sql 语句以及其执行时间，建议开发测试时启用该功能，能快速揪出慢查询
- 内置全局拦截插件：提供全表 delete 、 update 操作智能分析阻断，也可自定义拦截规则，预防误操作

**框架结构**

![image-20210708110328386](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210708110328386.png)



## 02_mybatis-plus HelloWorld实现

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.2.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <groupId>com.sw</groupId>
    <artifactId>mp-demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    
    <dependencies>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.3</version>
        </dependency>

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.3.2</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.10</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

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

实体：Department.java

```java
package com.sw.bo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 部门实体
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2020-08-09 23:36
 */
@TableName("t_department")
@Data
public class Department {

    private Integer id; // 编号

    private String name; // 名称

    private String remark; // 备注
}
```

DepartmentMapper.java

```java
package com.sw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sw.bo.Department;

/**
 * 部门mapper接口
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2020-08-09 23:38
 */
public interface DepartmentMapper extends BaseMapper<Department> {

}
```

<font color='red'>测试类DepartmentTest.java</font>

```java
package com.sw;

import com.sw.bo.Department;
import com.sw.mapper.DepartmentMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentTest {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Test
    public void select(){
        List<Department> departmentList = departmentMapper.selectList(null);
        for (Department department : departmentList){
            System.out.println(department);
        }
    }
}

```

application.yml

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.132.25:3306/db_mp?serverTimezone=GMT
    username: root
    password: 123
```

# 通用CRUD

## 03_mybatis-plus通用Mapper CRUD之insert

http://blog.java1234.com/blog/articles/747.html

department.java

自增策略

```java
    /*@TableId(type = IdType.AUTO)*/
    private Integer id; // 编号
```

全局主键配置

application.yml

```yaml
mybatis-plus:
  global-config:
    db-config:
      id-type: 0
      table-prefix: t_ 	# 自动映射t_的数据库表
```

## 04_mybatis-plus驼峰式命名规则配置

mybatis-plus对于驼峰式命名的处理：

对于驼峰式命名的实体名和字段名，mybatis-plus默认处理是通过下划线分隔。

比如：SysUser实体名默认映射sys_user表；

比如：UserName字段名默认映射user_name字段。

application.yml

```yaml
mybatis-plus:
  global-config:
    db-config:
      id-type: 0
      table-prefix: t_
      table-underline: false  # 去掉下划线
  configuration:
    map-underscore-to-camel-case: false		# 表字段也去掉下划线
```

<font color='ff3300'>但2个去掉下划线，会对性能有损耗</font> 

## 05_mybatis-plus之@TableField字段

有时候我们有这种情况出现，设计师设计的表字段名和我们实体设计的属性名不一致，我们可以通过@TableField的value来映射；

以及有些属性字段不需要映射到数据库，仅仅系统里临时用，或者记录等功能的时候，我们有可以通过@TableField的exist属性来配置；

比如数据库字段名是name，实体属性名就是userName，我们可以用@TableField(value="name")来映射

假如state字段不需要映射数据库，可以用@TableField(exist = false)

SysUser.java

```java
@Data
@TableName("t_sysUser")
public class SysUser {

    private Integer id;

    @TableField(value="name")
    private String userName;

    private String passWord;

    @TableField(exist = false)	//不去映射
    private String state;
}
```

## 06_mybatis-plus通用Mapper CRUD之update

```java
    @Test
    public void updateById(){
        Department department=new Department();
        department.setId(7);
        department.setName("总经理办公室");
        department.setRemark("光棍~~");
        departmentMapper.updateById(department);
        System.out.println(department.getId());
    }
```



## 07_mybatis-plus springboot配置显示执行SQL

```yaml
  configuration:
    map-underscore-to-camel-case: false
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl   # 显示sql
```

## 08_mybatis-plus通用Mapper CRUD之delete

根据条件删除

```java
@Test
public void deleteByMap(){
    Map<String, Object> map = new HashMap<>();
    map.put("name", "java部1");
    map.put("remark", "光棍~~");		// 2个条件，相当于 where ... and ...
    int row=departmentMapper.deleteByMap(map);
    if(row>0){
        System.out.println("删除了"+row+"条");
    }else {
        System.out.println("没有删除");
    }
}
```

根据多个id删除

```java 
    @Test
    public void deleteBatchId(){
        List<Integer> idList = new LinkedList<>();
        idList.add(10);
        idList.add(14);
        int deleteRow=departmentMapper.deleteBatchIds(idList);
        if(deleteRow>0){
            System.out.println("删除了"+deleteRow+"条");
        }else {
            System.out.println("没有删除");
        }
    }
```



## 09_mybatis-plus通用Mapper CRUD之select

### selectById

```java
Department department = departmentMapper.selectById(2);
```



### selectBatchIds

```java
List<Integer> idList = new LinkedList<>();
idList.add(2);
idList.add(3);
idList.add(4);
List<Department> departmentList = departmentMapper.selectBatchIds(idList);
System.out.println(departmentList);
```

### selectByMap

```java
Map<String, Object> map = new HashMap<>();
map.put("name", "总经理办公室");
map.put("remark","光棍~~");
List<Department> departmentList = departmentMapper.selectByMap(map);
System.out.println(departmentList);
```

# 条件构造器

## 10_mybatis-plus条件构造器_简介

mybatis-plus提供了AbstractWrapper抽象类，提供了很多sql语法支持的方法，比如模糊查询，比较，区间，分组查询，排序，判断空，子查询等等，方便我们用面向对象的方式去实现sql语句；

AbstractWrapper有两个常用的实现类，分别是QueryWrapper和UpdateWrapper，我们以后就用这两个实现类；

QueryWrapper

```java
    /**
     * 查找薪水大于3500 名字里有“小”的 员工
     * sql： select * from t_employee where salary>3500 and name like '%小%'
     */

    @Test
    public void selectByQueryWrapper(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper();
        // QueryWrapper<Employee> queryWrapper2=Wrappers.<Employee>query();  //另一种方法
        queryWrapper.gt("salary",3500).like("name","小");
        List<Employee> employeeList = employeeMapper.selectList(queryWrapper);
        System.out.println(employeeList);
    }
```

复杂查询

```java
    /**
     * 查找薪水范围【3500，5000】，名字里有“小”以及email不为空 的员工
     * sql： select * from t_employee where between 3500 and 5000 and name like '%小%' and email is not null
     */
    @Test
    public void selectByQueryWrapper2(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper();
        // QueryWrapper<Employee> queryWrapper2=Wrappers.<Employee>query();
        queryWrapper.like("name","小").between("salary",3500,5000).isNotNull("email");
        List<Employee> employeeList = employeeMapper.selectList(queryWrapper);
        System.out.println(employeeList);
    }
```

## 11_mybatis-plus条件构造器_QueryWrapper实例

### 日期查询

```java
   /**
     * 查询姓李的，并且出生日期范围是1993-02-09到1994-04-09的员工
     * sql：SELECT * FROM t_employee WHERE DATE_FORMAT(birthday,'%Y-%m-%d')>='1993-02-09' AND DATE_FORMAT(birthday,'%Y-%m-%d')<='1994-04-09' AND NAME LIKE '李%'
     */
    @Test
    public void selectByQueryWrapper3(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper();
        // QueryWrapper<Employee> queryWrapper2=Wrappers.<Employee>query();
        queryWrapper.apply("DATE_FORMAT(birthday,'%Y-%m-%d')>={0} and DATE_FORMAT(birthday,'%Y-%m-%d')<={1}","1993-02-09","1994-04-09").likeRight("name","李");
        List<Employee> employeeList = employeeMapper.selectList(queryWrapper);
        System.out.println(employeeList);
    }
```

### or

```java
    /**
     * 查询姓李的或者邮箱不为空并且是女性的员工
     * sql：SELECT * FROM t_employee WHERE NAME LIKE '李%' OR (email IS NOT NULL AND gender ='女')
     */
    @Test
    public void selectByQueryWrapper4(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper();
        // QueryWrapper<Employee> queryWrapper2=Wrappers.<Employee>query();
        queryWrapper.likeRight("name","李").or(wq->wq.isNotNull("email").eq("gender","女"));
        List<Employee> employeeList = employeeMapper.selectList(queryWrapper);

    }
```

### and

```java
    /**
     * 查询姓李的并且邮箱不为空或者是女性的员工
     * sql：SELECT * FROM t_employee WHERE NAME LIKE '李%' AND (email IS NOT NULL OR gender ='女')
     */
    @Test
    public void selectByQueryWrapper5(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper();
        // QueryWrapper<Employee> queryWrapper2=Wrappers.<Employee>query();
        queryWrapper.likeRight("name","李").and(wq->wq.isNotNull("email").or().eq("gender","女"));
        List<Employee> employeeList = employeeMapper.selectList(queryWrapper);
        System.out.println(employeeList);
    }
```

### in和order by

```java
    /**
     * 查询属于编号1,2,3部门的并且薪水小于等于3500的员工 根据年龄从大到小排序显示
     * sql：SELECT * FROM t_employee WHERE salary<=3500 AND departmentId IN (1,2,3) ORDER BY birthday ASC
     */
    @Test
    public void selectByQueryWrapper6() {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper();
        // QueryWrapper<Employee> queryWrapper2=Wrappers.<Employee>query();
        queryWrapper.le("salary", 3500).in("department_id", "1,2,3").orderByAsc("birthday");
        List<Employee> employeeList = employeeMapper.selectList(queryWrapper);
        System.out.println(employeeList);
    }
```



## 12_mybatis-plus条件构造器_UpdateWrappe实例

### 普通更新

```java
   /**
     * 更新指定员工的邮箱和联系电话
     * sql实现：
     * UPDATE t_employee SET email="123456@qq.com", phoneNumber="12345678" WHERE id=6
     */
    @Test
    public void updateByUpdateWrapper(){
        UpdateWrapper<Employee> updateWrapper=new UpdateWrapper<>();
        //UpdateWrapper<Employee> updateWrapper2 = Wrappers.<Employee>update();
        Employee employee=new Employee();
        employee.setEmail("1234@qq.com");
        employee.setPhoneNumber("1234567");
        updateWrapper.eq("id",6);
        int affectRows=employeeMapper.update(employee,updateWrapper);
        if(affectRows>0){
            System.out.println("更新成功");
        }else{
            System.out.println("更新失败");
        }
```

### 删除市场部老员工

```java
    /**
     * 删除市场部老员工
     * sql实现：
     * DELETE FROM t_employee WHERE DATE_FORMAT(birthday,'%Y-%m-%d')<="1990-01-01" AND departmentId=2
     * mp实现：
     */
    @Test
    public void deleteByUpdateWrapper2() {
        UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
        //UpdateWrapper<Employee> updateWrapper2 = Wrappers.<Employee>update();
        updateWrapper.apply("DATE_FORMAT(birthday,'%Y-%m-%d')<={0}", "1990-01-01").eq("departmentId", 2);
        int affectRows = employeeMapper.delete(updateWrapper);
        if (affectRows > 0) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }
    }
```



## 13_mybatis-plus条件构造器_QueryWrapper条件构造器select方法-返回指定字段

- 根据业务需求，有时候只需要返回特定的几个数据表字段，我们通过条件构造器的select方法可以指定；
- 还有一种情况，假如数据库字段很多的时候，我们要排除某几个字段，其他字段返回的时候，select方法也支持排除某些字段，查询其他的；
- 最后还有一种情况，我们搞分组聚合函数的时候，可以使用select方法，返回聚合函数执行后的数据字段；

### 只显示编号和姓名

```java
@Test
public void selectByQueryWrapper7(){
    QueryWrapper<Employee> queryWrapper=new QueryWrapper();
    // QueryWrapper<Employee> queryWrapper2=Wrappers.<Employee>query();
    queryWrapper.select("id","name").gt("salary",3500).like("name","小");	//select
    List<Employee> employeeList = employeeMapper.selectList(queryWrapper);
    System.out.println(employeeList);
}
```

### 排除出生日期和性别

```java
    /**
     * 返回的birthday 和gender 字段为null
     */    
	@Test
    public void selectByQueryWrapper8(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper();
        // QueryWrapper<Employee> queryWrapper2=Wrappers.<Employee>query();
        queryWrapper
                .select(Employee.class,fieldInfo->!fieldInfo.getColumn().equals("birthday")&&!fieldInfo.getColumn().equals("gender"))
                .gt("salary",3500)
                .like("name","小");
        List<Employee> employeeList = employeeMapper.selectList(queryWrapper);
        System.out.println(employeeList);
    }
```

### 分组查询：查询每个部门的平均薪资

Employee.java 增加

```java 
    @TableField(exist = false)
    private Integer departmentId;
    
    @TableField(exist = false)
    private Integer avgSalary;
```

```java
    /**
     * sql实现：
     * SELECT departmentId,AVG(salary) AS avg_salary FROM t_employee GROUP BY department_id;
     */
    @Test
    public void selectByQueryWrapper9(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper();
        // QueryWrapper<Employee> queryWrapper2=Wrappers.<Employee>query();
        queryWrapper
                .select("department_id","AVG(salary) AS avg_salary")
                .groupBy("department_id");
        List<Employee> employeeList = employeeMapper.selectList(queryWrapper);
        System.out.println(employeeList);
    }
```



## 14_mybatis-plus条件构造器_condition动态判断条件

mp框架，在条件构造方法里，都会重载一个condition参数；

这个参数的作用是动态判断条件，假如condition是true，则拼接加条件，false的话，则不拼接加条件；

我们前台传来的动态条件，以前是通过代码判断拼接，现在我们可以直接条件构造方法里写，大大简化代码量；

```java
/**
     * 动态判断条件 根据性别和名字查询
     */
    @Test
    public void selectByQueryWrapper10(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper<>();
        String s_gender="男";
        String s_name="小";
        /*if(StringUtil.isNotEmpty(s_gender)){
            queryWrapper.eq("gender",s_gender);
        }
        if(StringUtil.isNotEmpty(s_name)){
            queryWrapper.like("name",s_name);
        }*/
        queryWrapper
                .eq(StringUtil.isNotEmpty(s_gender),"gender",s_gender)
                .like(StringUtil.isNotEmpty(s_name),"name",s_name);
        List<Employee> employeeList = employeeMapper.selectList(queryWrapper);
        System.out.println(employeeList);
    }
```

StringUtil.java

```java
package com.sw.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 字符串工具类
 * @author
 *
 */
public class StringUtil {

	/**
	 * 判断是否是空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str==null||"".equals(str.trim())){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 判断是否不是空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		if((str!=null)&&!"".equals(str.trim())){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 格式化模糊查询
	 * @param str
	 * @return
	 */
	public static String formatLike(String str){
		if(isNotEmpty(str)){
			return "%"+str+"%";
		}else{
			return null;
		}
	}

	/**
	 * 过滤掉集合里的空格
	 * @param list
	 * @return
	 */
	public static List<String> filterWhite(List<String> list){
		List<String> resultList=new ArrayList<String>();
		for(String l:list){
			if(isNotEmpty(l)){
				resultList.add(l);
			}
		}
		return resultList;
	}

	/**
	 * 去除html标签
	 */
	public static String stripHtml(String content) {
	    // <p>段落替换为换行
	    content = content.replaceAll("<p .*?>", "\r\n");
	    // <br><br/>替换为换行
	    content = content.replaceAll("<br\\s*/?>", "\r\n");
	    // 去掉其它的<>之间的东西
	    content = content.replaceAll("\\<.*?>", "");
	    // 去掉空格
	    content = content.replaceAll(" ", "");
	    return content;
	}

	/**
	 * 生成六位随机数
	 * @return
	 */
	public static String genSixRandomNum(){
		Random random = new Random();
		String result="";
		for (int i=0;i<6;i++)
		{
			result+=random.nextInt(10);
		}
		return result;
	}
	
}
```

## 15_mybatis-plus_返回Map类型数据

我们前面的案例都是返回的集合List<T>；

集合List的弊端是会把所有的列属性都封装返回，但是我们有时候，只需要返回几个字段，然后再返回到用户端；

所以mp框架给我们提供了List<Map<String, Object>>返回类型，String是列名，Object是值，只返回select的字段；

```java
    @Test
    public void selectByQueryWrapper11() {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper();
        // QueryWrapper<Employee> queryWrapper2=Wrappers.<Employee>query();
        queryWrapper
                .select("department_id", "AVG(salary) AS avg_salary")
                .groupBy("department_id");
        List<Map<String,Object>> maps = employeeMapper.selectMaps(queryWrapper);
        System.out.println(maps);
    }
//返回
//[{department_id=1, avg_salary=3250.0000}, {department_id=2, avg_salary=3700.0000}, {department_id=3, avg_salary=4000.0000}, {department_id=4, avg_salary=4900.0000}]
```



## 16_mybatis-plus_返回查询总记录数

```java
    /**
     * mp框架提供了selectCount方法，来查询总记录数；
     * 需求：查找薪水大于3500 名字里有“小”的 员工的个数
     * sql实现：select count(*) from t_employee where salary>3500 and name like '%小%'
     */
    @Test
    public void selectCountByQueryWrapper11() {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper();
        queryWrapper.gt("salary", 3500).like("name", "小");
        Integer count = employeeMapper.selectCount(queryWrapper);
        System.out.println(count);
    }
```



## 17_mybatis-plus_lambda条件构造器

# 分页

## 18_mybatis-plus_物理分页插件

MybatisPlusConfig.java

```java
package com.java1234.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatisplus配置类
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2020-09-07 16:51
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}

```

EmployeeQueryWrapperWithPageTest.java

```java
   /**
     * 查找薪水大于3500 名字里有“小”的 员工 带分页
     * sql：select * from t_employee where salary>3500 and name like '%小%'
     */
    @Test
    public void selectByQueryWrapper(){
        QueryWrapper<Employee> queryWrapper=new QueryWrapper<>();
        // QueryWrapper<Employee> queryWrapper2= Wrappers.<Employee>query();
        queryWrapper.gt("salary",3500).like("name","小");

        Page<Employee> page = new Page<>(1,3,false);       //false 不查询总记录数

        IPage<Employee> employeePage = employeeMapper.selectPage(page, queryWrapper);
        System.out.println("总记录数："+employeePage.getTotal());
        System.out.println("总页数："+employeePage.getPages());
        System.out.println("当前页数据："+employeePage.getRecords());
    }
```





## 19_mybatis-plus_ActiveRecord模式
## 20_mybatis-plus_通用Service
## 21_mybatis-plus_代码生成器

pom.xml

```xml
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.4.1</version>
        </dependency>

        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity</artifactId>
            <version>1.7</version>
        </dependency>
```

Mptest2ApplicationTests.java

```java
package com.sw;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class Mptest2ApplicationTests {

    @Test
    void contextLoads() {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("java1234_小锋");
        gc.setOpen(false);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.132.11:3306/db_mp?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=GMT");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        // pc.setModuleName(scanner("模块名"));
        pc.setParent("com.java1234");
        pc.setEntity("bo");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");
        mpg.setPackageInfo(pc);



        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        ///strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        //strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        // strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        // strategy.setSuperEntityColumns("id");
        strategy.setInclude("t_department");
        // strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix("t_");
        mpg.setStrategy(strategy);
        // mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}

```

