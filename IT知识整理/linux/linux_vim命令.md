# 命令行状态

## 清屏

```sh
$ sudo vim test.txt
# 进入文件编辑模式
gg
# 进入行首
dG
# 文件内容将被全部清空
:wq
# 保存退出
```

## 翻页

```sh
gg	# 到第1行
G	#到最后一行
ctrl+d	# 向下翻页
ctrl+f  # 向下翻页（保留3行）
ctrl+u	# 向上翻页
ctrl+b	# 向上翻页（保留3行）

ctrl+e	# 向下一行
ctrl+y  # 向上一行
```

## 保存

```sh
shift+ZZ
:wq
```

## 到行尾行首

```sh
0 , shift+6, home   # 行首
shift+4, end # 行尾
shift+a   # 行尾并转到编辑mo'shi
```



# 末行状态

## **显示行号**

```sh
:set nu			# 显示行号 ★
:set number		
```

## **查询**

```sh
/ 	# 查询  n 下一个  N 上一个
?   #从下往上搜
```

## **到某行**

```sh
:150	# 到150行
```

