# 第1讲
CSS：Cascading Style Sheets

# 第2讲
## 第2节 css基本选择器
标记选择器
类选择器

```html
<style type="text/css">
	 .r{	
		font-size:12px} 
	.b{
		color:blue;
	}
</style>
<body bgcolor="grey">
	<p>段落一</p>
	<p>段落二</p>
	<p class='b'>段落三</p>
```
id选择器
id只针对1个

## 第3节 html中引入css
1.行内样式 style=""
2.内嵌式  p{}
3.链接式
4.导入样式(了解)    一下子样式全导进来
```html
	<style type="text/css">
		@import url(demo1.css);
	</style>
```
5.优先级比较
    行内样式>链接式>内嵌式>导入式(链接式在后面)
        行内样式>内嵌式>导入式>链接式(链接式在前面)
        
    行内样式>内嵌式=链接式(无导入式的情况)

##### 网页制作小实验
# 第4讲 css高级特性
## 第1节 复合选择器
"交集"选择器
```html
    p.c{
        p.c{text-decoration:underline;}
    }
    
    <p class='c'>hello</p>
```
"并集"选择器
```css
h2,p,#p1{background-color:gray}
```
"后代"选择器
```htl
    p span {color:red}
```
"子"选择器
```html
    div>span {color:pink}
```

## 第2节 css的继承特性
## 第3节 css的层叠特性
    行内>id>类别>标记选择器

##### 用css设置图像效果
###### 设置图片边框
###### 设置图片大小及缩放
###### 图文混排
###### 图片及文字的对齐方式
  vertical-align:top;
  vertical-align:20px;

# 第7讲 css设置背景色和背景图像
###### 背景图片
```html
    style="background-image:url(../grid.jpg)"
```
```html
    background-repeat:repeat-x;
    background-repeat:no-repeat;
```
###### 设置图片位置固定
```
    background-attachment:fixed;
```

# 第8讲 css盒模型
## 第5节 网页布局和盒模型
1. 标准文档流
2. 块级元素和行内元素

## 第6节 盒子在标准流的定位
span: margin-right+ margin-left

div: margin-bottom margin-top :以大的为主

# 第9讲 盒子的浮动和定位
## 第1节 float
## 第2节 clear
```css
clear:left  不允许左边有悬浮
clear:both  不允许有悬浮
```
## 第3节 position
    relative: 相对于原来的位置
    absolute：以父包含框为基准
    fixed：以浏览器定位
## 第4节 z-index
```
    {z-index:-1;}
```
## 第5节 display
```css
{display: inline;}  //变为span
{display: block;}   //变为div
```

# 第10章 css表格样式
## 第1节 表格边框
```css
border-spacing:: 0px;
border-collapse: collapse;
padding:    //内容和边框之间的距离
```
## 第2节 设置表格的宽度
```css
table-layout: atuo; (默认,随内容变化)
table-layout: fixed;
```

## 第3节 设置隔行换色
```css
tbody tr.even{ background-color: gray;}
th+td {align:center;}       //连接选择器
th+td+td+td {align:center;}
```

## 第4节 鼠标经过时变色
```css
tbody td: hover{background-color: aqua;}
```

# 第11讲 用CSS设置超链接样式
## 第1节 使用CSS伪类别来设置超链接样式
```css
a:link
a:visited
a:hover:
a:active: 链接被点击时的样式
```
## 第2节 创建按钮式超链接
```css
	a{
		font-family: Arial;
		margin: 5px;
	}
	
	a:LINK,a:VISITED {
		color:#A62020;
		padding:4px 10px 4px 10px;
		background-color:#DDD;
		text-decoration: none;
		border-top: 1px solid #EEEEEE;
		border-left: 1px solid #EEEEEE;
		border-bottom: 1px solid #717171;
		border-right: 1px solid #717171;
	}
	
	a:HOVER {
		color: #821818;
		padding: 5px 8px 3px 12px;
		background-color: #CCC;
		border-top: 1px solid #717171;
		border-left: 1px solid #717171;
		border-bottom: 1px solid #EEEEEE;
		border-right: 1px solid #EEEEEE;
	}
```
# 第12讲 css设置列表样式
## 第1节 设置列表符号
```css
ul{ list-style-type: square}
```
## 第2节 设置列表图片符号
```css
ul {list-style-image: url("../image.png");}
```
## 第3节 创建简单导航菜单

# 第13讲 css布局
## 第1节 布局
## 第2节 绝对定位法
## 第3节 浮动法
```css
#foot{ clear: both;}
```

# 第14讲 变宽度布局
```css
随浏览器，宽度同时变
```
