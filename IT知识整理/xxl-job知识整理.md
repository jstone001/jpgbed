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

