# 第2A讲 XMLHttpRequest

http://www.java1234.com/a/yuanchuang/ajax/2014/0325/1851.html

ie5 ie6 ActiveXObject
```js
var xmlHttp;
if(window.XMLHttpRequest){
	xmlHttp=new XMLHttpRequest;
}else{
	xmlHttp=new ActiveObject("Microsoft.XMLHTTP");      //IE5.0  , IE6.0
}

open(method,url,async)
xmlHttp.open("get","getAjaxName",true)
```
ajax返回
```java
response.setContentType("text/html;charset=utf-8");
PrintWriter pw=response.getWriter();
out.println("ajax返回值");
out.flush;
out.close;
```

xmlHttp setRequestHeader方式 表单
```js
xmlHttp.open("post","AjaxServlet",true);        //必须post
xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");  
xmlHttp.send("name=jack&age=12");
```

# 第2B讲 XMLHttpRequest对象响应服务器
http://www.java1234.com/a/yuanchuang/ajax/2014/0329/1877.html

- 0: 请求未初始化
- 1: 服务器连接已建立
- 2: 请求已接收
- 3: 请求处理中
- 4: 请求已完成，且响应已就绪

1. **responseText**  //获取文本
1. responseXML      //获取xml

```js
xmlHttp.onreadystatechange=function(){
    // alert("readyState:"+xmlHttp.readyState+" Status: "+xmlHttp.status);
    if(xmlHttp.readyState==4&&(xmlHttp.status==200)){
        // console.log(xmlHttp.responseText);
        document.getElementById("name").value=xmlHttp.responseText;
    }
};
```
# 第3A讲 JSON
```js
if("4"==xmlHttp.readyState&&"200"==xmlHttp.status){

    var json=xmlHttp.responseText
    var data=eval("("+json+")");

    var newTr;
    var newTd0;
    var newTd1;

    for(var i=0;i<data.students.length;i++){
        var student=data.students[i];
        newTr=st.insertRow();
        newTd0=newTr.insertCell();
        newTd1=newTr.insertCell();
        newTd0.innerHTML=student.name;
        newTd1.innerHTML=student.age;
    }
```
dataObj=eval("("+data+")")  //转换为对象


JSON数组
```java
JSONObject json = new JSONObject();

JSONObject stu1 = new JSONObject();
stu1.put("name", "张三");
stu1.put("age", 21);
JSONObject stu2 = new JSONObject();
stu2.put("name", "李四");
stu2.put("age", 22);		
JSONObject stu3 = new JSONObject();
stu3.put("name", "王五");
stu3.put("age", 24);

JSONArray array1=new JSONArray();
array1.add(stu1);
array1.add(stu2);
array1.add(stu3);
json.put("students", array1);
```
```xml
<!-- https://mvnrepository.com/artifact/net.sf.json-lib/json-lib -->
<dependency>
    <groupId>net.sf.json-lib</groupId>
    <artifactId>json-lib</artifactId>
    <version>2.4</version> 
    <classifier>jdk15</classifier>
</dependency>
```
# 第4A讲 交互实例
## 第1节 用户名验证
## 第2节 2级联动
```js
xmlHttp.onreadystatechange=function(){
    if("4"==xmlHttp.readyState&&"200"==xmlHttp.status){

        //   alert(xmlHttp.responseText);
        var jsonObj=eval('('+xmlHttp.responseText+')');
        if(jsonObj.exist){
            document.getElementById('tip').innerHTML="<img src='no.png'/>&nbsp;&nbsp;注册名已存在";
        }else{
            document.getElementById('tip').innerHTML="<img src='ok.png'/>&nbsp;&nbsp;可以注册";
        }
        // 	 document.getElementById("name").value= xmlHttp.responseText;
    }
};

xmlHttp.open("get", "checkUserName?userName="+userName, true);
xmlHttp.send();
```


# 第4讲B ajax 二级联动
 ```js
var shengId=document.getElementById("sheng").value;
// var shi=document.getElementById("shi");
shi.options.length=0;	//删除所有值
var xmlHttp;
if(window.XMLHttpRequest){
    xmlHttp=new XMLHttpRequest;
}else{
    xmlHttp=new ActiveObject("Microsoft.XMLHTTP");      //IE5.0  , IE6.0
}

xmlHttp.onreadystatechange=function(){
    if(xmlHttp.readyState==4&&(xmlHttp.status==200)){
        var obj=eval("("+xmlHttp.responseText+")");
        for(var i=0;i<obj.rows.length;i++){
            var o=obj.rows[i];
            alert
            shi.options.add(new Option(o.text,o.id));
        }
    }

};

xmlHttp.open("get","loadInfo?shengId="+shengId,true);
xmlHttp.send();
 ```