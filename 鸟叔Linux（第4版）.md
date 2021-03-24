# 第0章 计算机概论

## 0.1 计算机：辅助人脑的好工具

### 0.0.1 计算机的5大单元

- 输入

- 输出

- cpu控制单元

- 计算逻辑单元

- 主存储器
### 0.1.2 CPU架构

# 第十二章、学习 Shell Scripts

## 12.1 什么是 Shell scripts

systemd(CentOS7)

### 12.1.1 干嘛学习 shell scripts

- 自动化管理的重要依据

- 追踪与管理系统的重要工作

- 简单入侵检测功能

- 连续指令单一化

- 简易的数据处理

- 跨平台支持与学习历程较短

  
### 12.1.2 第一支 script 的撰写与执行

```bash
#!/bin/bash
#Program:
#This is show.....
#History:
#2015/07/16
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH
echo -e "Hello World! \a \n"
exit 0
```



cal.sh

```sh
#!/bin/bash
# Program:
# User input a scale number to calculate pi number.
# History:
# 2015/07/16 VBird First release

PATH=/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin:/usr/local/sbin:~/bin
export PATH
echo -e "This program will calculate pi value. \n"
echo -e "You should input a float number to calculate pi value.\n"
read -p "The scale number (10~10000) ? " checking
num=${checking:-"10"}	 # 开始判断有否有输入数值
echo -e "Starting calcuate pi value. Be patient."
time echo "scale=${num}; 4*a(1)" | bc -lq		# 4*a(1) 是 bc 主动提供的一个计算 pi 的函数，至于 scale 就是要 bc 计算几个小数点下位数的意思.
```



