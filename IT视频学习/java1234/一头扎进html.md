# 第1讲
# 第2讲
```html
<center></center>   //文字居中，不用
<blockquote>    //文字段落缩进
```
## 4.3 文字的特殊样式
## 4.4 font 标签
## 4.5 特殊符号

```htnl
&lt;
&gt;
&copy;
<sub></sub>
<sup></sup>
```

# 第3讲 网页中使用图像
title
# 第4讲 超链接
图片链接
```html
<a href='www.baidu.com'><img src='http://www.baidu.com/img/bd_logo1.png' height='127px' width='200px' alt='百度'/></a>
```
邮件链接
```html
<a href='mailto:catherlove@163.com' >联系我的邮箱</a>
```
框架里的链接
```html
<html>
	<frameset cols='20%,*'>
			<frame src='htmltxt.html' >
			<frame  name='right'>
	
	</frameset>
</html>
```

嵌入式链接
```html
	<iframe src='http://www.baidu.com' height='100%' width='100%' />
```
# 第5讲 创建表格
表的基本结构
合并单元格
边距
```html
cellpadding     //内容距离格线的距离
cellspacing     //相邻单元格2边线之间的距离
border-collapse:collapse;   //collapse：如果可能，边框会合并为一个单一的边框。会忽略border-spacing 和 empty-cells 属性。
//如果没有规定 !DOCTYPE，border-collapse 属性可能会引起意想不到的错误
```
# 第6讲 表单
文本：text, password, textarea
单选框：
```html
性别： <input type='radio' name='sex'/>男<input type='radio' name='sex' checked='checked'/>女
```
复选框
```html
		<input type='checkbox' name='hobby' />唱歌
		<input type='checkbox' name='hobby' />跳舞
		<input type='checkbox' name='hobby' checked='checked'/>游泳
```
下拉框
```html
		<select id='grade' name='grade'>
			<option value='1'>年级1</option>
			<option value='2'>年级2</option>
			<option value='3'>年级3</option>
		</select>
```
按钮
```html
		<input type='submit' value='登录' />
		<input type='button' value='重置' />
```
文件上传
```html
    <input type='file' id='f' name='f'/>
```

# 第7讲 div 和span