# SpringCloud连接远程nacos报错，一直提示连接本地的localhost:8848

在resources下创建:bootstrap.properties

```properties
#nacos config
spring.cloud.nacos.config.server-addr=xxx.xxx.xxx.xxx:8848
spring.application.name=服务名