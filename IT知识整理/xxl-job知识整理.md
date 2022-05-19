# xxl-job 看不到执行日志 Console，只有[Rolling Log Finish]

from: https://blog.csdn.net/duanna0824/article/details/108643448

```sh
# 手动建执行器的日志输出文件夹
mkdir /data/  

# 手动创建admin的输出文件夹和日志文件
sudo mkdir -p jobhandler/gluesource
sudo touch xxl-job-admin.log
sudo chmod 666 xxl-job-admin.log 
```

# 运行sh文件

```sh
# 选择commandJobHandler
@XxlJob("commandJobHandler")

# 参数为
f:\a.txt
```

# 运行glue(powershell)

```powershell
# 因为在此系统上禁止运行脚本。有关详细信息，请参阅 https:/go.microsoft.com/fwlink/?LinkID=135170 中的 about_Execution_Policies。

用管理员身份运行 PowerShell，然后输入: set-executionpolicy remotesigned
```

```powershell
# 在GLUE IDE中直接输入
f:/a.bat  #即可
```

