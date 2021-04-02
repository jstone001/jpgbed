# 第1讲
jquery 2.0 不支持ie 6,7,8
```html
<body onload="init()">  //文档加载完毕，执行init()
```

```js
	$(document).ready(function(){
		
		$("#button01").click(function(){
			
			alert($('#content').val());	
		});
	});
```
```js
	function init(){
		document.getElementById("actionButton").onclick=function(){
			var content=document.getElementById("content").value;
			alert(content);
		}
	}
```

# 第2讲 选择器
```js
// 基本选择器
$("#a1").css("background-color","red");  // 根据id
$(".c1").css("background-color","red"); //  根据class类别
$("a").css("background-color","red");  // 根据标签元素

// 属性选择器
$("[href]").css("background-color","red");
$("[href='#']").css("background-color","red");   //属性等于某个值
$("[href$='com']").css("background-color","red");

// 其他选择器
$("p.c1").css("background-color","red");
$("ul li:first").css("background-color","red");
$("ul li:last").css("background-color","red");   //没有 second	
```

# 第3讲A Jquery 操作Dom
## 第1节 jquery 操作dom节点
- 查找节点 
```js
    var li2=$("ul li:eq(1)");
    var li2_text=li2.text();
```
- 创建节点 append()
```js
		var li1=$("<li title='马化腾'>马化腾</li>");
		$("ul").append(li1);
		
		li2.insertAfter(li1);
```
- 删除节点 remove()
```js
    $("ul li:eq(1)").remove();
```
## 第2节 jquery 操作dom节点属性
- 查找属性
```js
    li2.attr("title");
```
- 修改属性
```json
    $("ul li:eq(1)").attr("title","你好");
```
- 删除属性
```js
    $("ul li:eq(1)").removeAttr("title");
```
## 第3节 jquery 操作dom节点css样式

```js
//获取样式
var li1_class=$("ul li:eq(1)").attr("class");
//修改样式
$("ul li:eq(1)").attr("class","lc2");
//追加样式
$("ul li:eq(1)").addClass("lc3");
//移除样式
$("ul li:eq(1)").removeClass("lc3");
```



## 第4节 设置和获取html，文本和值

```js
//获取html
 $("ul li:eq(0)").html();
//设置html
 $("ul li:eq(0)").html("<font color=red>哈哈</font>");
//获取文本
$("ul li:eq(0)").text();
//设置文本
$("ul li:eq(0)").text("你好");
```



# 第3讲B
子节点  
```js
	function setUserName(){

		$("#name").val("你好"); //val important
	}
```
```js
    var u=$("ul").children();
	for(var i=0;i<u.length;i++){
		console.log(u[i].innerText);    //u[i] 原生的节点，html()不能用
	}
	
	console.info(b[i].innerHTML);
	console.info($(b[i]).html());   //原生节点要包一层
```
## 第5节 遍历children() next()  prev()

## 第6节 jquery获取dom节点的css
```js
var c=$("#jq").css("color");
$("#jq").css("color","blue");
```

# 第四讲 jquery事件
```js
//文档加载事件
	$(document).ready(function(){

	});
	
//单击事件
	$("#zs").click(function(){

	});
	
//双击事件
	$("#zs").dblclick(function(){

	});

//得到焦点
	$("#t1").focus(function(){

	});
//失去焦点
	$("#t1").blur(function(){
		$("#t1").val("失去焦点");
	});	
//鼠标移入移出
	$(document).ready(function(){
		$("#d").mouseover(function(){
			$("#d").html("鼠标移入");
		});

		$("#d").mouseout(function(){
			$("#d").html("鼠标移出");
		});
	});
```

# 第5讲 jquery 动画效果

```js
//显示和隐藏dom
$("#p1").show();
$("#p1").hide();

//淡入和淡出dom
$("#d1").fadeOut();
$("#d1").fadeIn();
$("#d1").fadeToggle();      //淡入淡出开关
$("#d1").fadeToggle("fast");    //$("#d1").fadeToggle("slow");
$("#d1").fadeToggle(5000);
$("#d1").fadeTo("show",0.1);	//透明度

//滑动dom
$("#d5").slideUp("show");

//动画
$("#d6").animate({left:'500px'},"slow");
$("#d6").animate({left:'500px',opacity:0.5,height:'150px',width:'150px'},"slow");

//callBack
$("#b11").click(function(){
	$("#p2").show(function (){
		alert("回调");
	});
});

//暂停动画
$("#d5").stop();
```

 

# 第6讲 Jquery调ajax (very important)

## 第1节 简介
跨浏览器
## 第2节 Jquery Ajax Load方法
load(url,[data],[Callback]);
```js
<div id="d1"></div>

$(document).ready(function(){

    $("#b1").click(function(){
        $("#d1").load("${pageContext.request.contextPath}/ajax?action=load",{name:'张三',age:15},function(){
            alert('执行完成');      
        });

    });

});
```
## 第3节 Jquery Ajax get/post
```js
//b2按钮方法
$("#b2").click(function(){
    $.post("${pageContext.request.contextPath}/ajax?action=post",{id:1},function(data,textStatus){
        // alert(data);
        // alert(textStatus);
        if("success"==textStatus){
            var obj=eval("("+data+")"); //important
            $("#name").val(obj.name);
            $("#age").val(obj.age);
        }
    });
});
```