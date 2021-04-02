# 第1讲 简介
# 第2讲 
# 第3讲 jsp基本语法
## page指令
- Language : 用来定义要使用的脚本语言；
- contentType：定义 JSP 字符的编码和页面响应的 MIME 类型；
- pageEncoding：Jsp 页面的字符编码

## scriptlet标签
- 第一种：<%! %> 我们可以在里面定义全局变量、方法、类；
- 第二种：<% %> 我们可以在里面定义局部变量、编写语句；
- 第三种：<%= %> 我们可以在里面输出一个变量或一个具体内容

## jsp注释
- <!-- --> Html 注释 客户端可见
- <%-- --%> Jsp 注释 客户端不可见
- // java 单行注释
- /* */ java 多行注释

## jsp包含指令
- <%@ include file=”要包含的文件”%> 静态包含 先包含，后编译处理；
- <jsp:include page=”要包含的文件”> 动态包含 子页面先编译处理，后包含；
- 以后开发用动态包含    (防止变量重名等)

jsp页面跳转
    服务器内部跳转，网址不变
```xml
	<% request.setCharacterEncoding("utf-8"); %>      <!-- 带中文 -->
	<jsp:forward  page ="target.jsp">
		<jsp:param  value="张三" name="userName"/>
		<jsp:param name="password" value="111"/>
	</jsp:forward>
```

# 第4讲 jsp九大内置对象和4个作用域

## 作用域
- page
- request
- session
- application

```java
	// 获取头信息
	Enumeration enu=request.getHeaderNames();
	while(enu.hasMoreElements()){
		String headerName=(String)enu.nextElement();
		String headerValue=request.getHeader(headerName);
%>
	<h4><%=headerName %>&nbsp;: <%=headerValue %></h4>
<%
	}
%>
```

# 第5讲A 内置对象
## response对象
- 刷新页面
- 页面重定向  //客户端跳转,改变地址
- 操作cookie

```jsp
	<%
		//每秒刷新一次页面
		response.setHeader("refresh", "1");
		%>
```
```java
    //重定向
    response.setRedict("index.html");
```
```java
if("remember-me".equals(remember)){
    Cookie userNameAndPwd=new Cookie("userNameAndPwd",userName+"-"+pwd);
    userNameAndPwd.setMaxAge(1*60*60*24*7); // cookie记录一个星期
    response.addCookie(userNameAndPwd);  // 保存cookie
    System.out.println("设置Cookie成功");
}
```

## out对象
## config对象
```jsp
    config.getInitParameter("jdbcName");
```
## exception 对象
```jsp
<%@ page errorPage="error.jsp" %>
```
```jsp
<%@ page isErrorPage="true" %>
```
# 第5讲B 内置对象
# 第6讲A javaBean
## 第1节 javabean 组件引入
## 第2节 jsp:javabean创建

## 第3节 jsp: setProperty
```xml
	<jsp:useBean id="student" scope="page" class="com.java1234.model.Student" />
	<jsp:setProperty property="*" name="student"/>
```

# 第6讲B javabean
## 第4节 getProperty
## 第5节 javabean的范围
## 第6节 javabean的删除
```jsp
<% session.removeAttribute("student"); %>
```

# 第7讲A servlet
## 第1节 引入
## 第2节 实例
## 第3节 生命周期
servlet 类加载->初始化->服务->销毁

# 第7讲B servlet
## 第4节 客户端跳转vs 服务端跳转
```java
//客户端跳转：
	resp.sendRedirect("target.jsp");    //内部跳转 session application值能传
//服务器跳转：
	RequestDispatcher rd=req.getRequestDispatcher("target.jsp");
	rd.forward(req, resp);  //能传request的值
```
# 第8讲A
##  第5节 jsp&servlet 登录实例

# 第8讲B
## 第6节 servlet 过滤器
## 第7节 serlvet 监听器
    

```java
//监听application, session, request
@Override
public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
    System.out.println("添加的属性："+httpSessionBindingEvent.getName()+ "值："+httpSessionBindingEvent.getValue());
}

@Override
public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {
    System.out.println("移除的属性："+httpSessionBindingEvent.getName()+ "值："+httpSessionBindingEvent.getValue());
}

@Override
public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
    System.out.println("替换的属性："+httpSessionBindingEvent.getName()+ "值："+httpSessionBindingEvent.getValue());
}
```
```xml
  <listener>
  	<listener-class>com.java1234.sessionAttributeListener.SessionAttrListerner</listener-class>
  </listener>
```
# 第9讲 EL表达式
## 第1节 简介 expression language
## 第2节 内置对象
## 第3节 范围属性 
    page -> request -> session -> application

## 第4节 el表达式接收请求参数
    param:
    paramValues:
## 第5节 el表达式对象操作
```
    ${zhangsan.id}
```
## 第6节 el表达式集合操作
## 第7节 el表达式关系运算符操作
```
    ${empty a}
```

# 第10讲A jsp自定义标签
## 第3节 自定义有属性标签

# 第10讲B jsp自定义标签
## 第4节 自定义标签体

# 第11讲A jsp标准标签库
## 第1节 jstl引入   JSP Standard Tag Library
    c:out   内容输出
```jsp
<c:out value="你好" />
<c:out value="${dd}" default="world" ></c:out>
<c:set var="people" value="张三" scope="request"/>
<c:out value="${people }" />
<jsp:useBean id="people2" class="com.java1234.model.People"/>
<c:set property="id" target="${people2 }" value="007"></c:set>
<c:set property="name" target="${people2 }" value="王五"></c:set>
<c:set property="age" target="${people2 }" value="16"></c:set><br/>
<c:out value="${people }"></c:out><br/>
<c:remove var="people" scope="request"/>
<c:out value="${people }" default="没人啊"></c:out><br/>
<c:catch var="errMsg">
<%
int a=1/0;
%>
</c:catch>
异常信息：${errMsg }

<c:if test="${people2.name=='王五' }" var='r' scope="page">
yes
</c:if>
```

# 第11讲B jsp标准标签库
## 第3节 核心标签库
```jsp
	<c:choose>
		<c:when test="${people2.age<18 }">
			&lt;18
		</c:when>
		<c:when test="${people2.age==18 }">
			=18
		</c:when>
		<c:otherwise>
			&gt;18
		</c:otherwise>
	</c:choose>
```
- c:out 内容输出标签；
- c:set 用来设置 4 中属性范围值的标签；
- c:remove 用来删除指定范围中的属性；
- c:catch 用来处理程序中产生的异常；
- c:if 用来条件判断；
- c:choose、c:when、c:otherwise 用来多条件判断；
- c:forEach 用来遍历数组或者集合；
- c:fortokens 分隔输出；
- c:import 导入页面；
- c:url 生成一个 url 地址；
- c:redirect 客户端跳转

# 第12讲 
## 第4节 国际化标签库
## 第5节 sql标签库
## 第6节 xml标签库
## 第7节 国际化标签库