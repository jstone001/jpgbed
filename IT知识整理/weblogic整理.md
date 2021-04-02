# weblogic部署时候遇到的Reached EOF

from:https://blog.csdn.net/ourserver/article/details/50546251

1、在部署的时候，点击activate报错

```sh
java.io.IOException: Reached EOF
at weblogic.deploy.service.datatransferhandlers.MultipartParser.readFile(MultipartParser.java:166)
at weblogic.deploy.service.datatransferhandlers.MultipartParser.readFiles(MultipartParser.java:129)
at weblogic.deploy.service.datatransferhandlers.MultipartParser.parseResponse(MultipartParser.java:121)
at weblogic.deploy.service.datatransferhandlers.MultipartParser.(MultipartParser.java:82)
at weblogic.deploy.service.datatransferhandlers.MultipartParser.
```

**解决办法是：**检查工程文件的时间和当前服务器系统的时间是否一致，如果系统时间比工程文件时间超前（有时候系统时间被改或者是其他原因恢复到之前的时间），可能就会出错。<font color='red'>解决办法就是将系统时间改成真实时间即可（当然肯定是要比工程文件的时间晚了）</font>

 2、在部署的时候，点activate报错

 ```sh
java.lang.Exception:[DeploymentService:290049]Deploy failed for id '1,378,285,527,510' since no targets are reachable
 ```

<font color='red'>**解决办法：**检查是否有受管端口没有启动</font>



 

 

​	