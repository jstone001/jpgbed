# 介绍

## 00_系统介绍以及功能演示

1. springcloud分布式电商秒杀系统简介：
2. 采用前后端分离架构，前端vue vue-element，后端springboot springcloud springcloudlalibaba；
3. 项目开发工具idea，webstorm；
4. 基于Docker来管理redis，rabbitmq，mysql等；
5. 压测工具Jmeter使用讲解；
6. 自定义分布式session token会话，存redis；
7. 使用Redis作为缓存，提高QPS;
8. 消息队列RabbitMQ，实现异步下单；
9. 图形验证码，提高QPS；
10. 使用Nacos作为服务注册中心和配置中心；
11. 使用OpenFeign进行微服务之间的http调用；
12. 使用Gateway作为分布式微服务网关，用Gateway来进行网关限流；

![image-20210705162143893](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210705162143893.png)

# 环境准备

## 01_后端架构搭建

pom.xml

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- 实体类注解  -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.20</version>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.3.2</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.10</version>
        </dependency>

        <!-- spring boot redis 缓存引入 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <!-- lettuce pool 缓存连接池 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.35</version>
        </dependency>

        <dependency>
            <groupId>commons-codec</groupId>
            <artifactId>commons-codec</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.6</version>
        </dependency>

        <!-- rabbmitmq -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>



    </dependencies>
```



## 02_前端vue开发环境搭建

# 登录功能实现

## 03_elementui组件登录页面实现

https://element.eleme.cn/#/zh-CN

## 04_axios引入

```sh
npm install --save axios	# 前后端交互
```

login.vue

```vue
<template>
  <div class="login-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>分布式秒杀系统-用户登录</span>
      </div>

      <el-form ref="form" label-width="80px">
        <el-form-item  label="用户名：">
          <el-input v-model="username" placeholder="请输入用户名" style="width:250px"></el-input>
        </el-form-item>
        <el-form-item label="密码：">
          <el-input v-model="password" type="password" placeholder="请输入密码" style="width:250px"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width:250px" @click="submit">登录</el-button>
          <p class="errorInfo">{{errorInfo}}</p>
        </el-form-item>
      </el-form>
      <div>
        版权所有：<a href="www.java1234.vip">www.java1234.vip</a>
      </div>
    </el-card>
  </div>
</template>

<script>
  import axios from "axios"
  export default {
    name: 'Login',
    data(){
      return{
        username:'',
        password:'',
        errorInfo:""
      }
    },
    methods:{
      submit(){
        if(this.username.trim()==""){
          this.errorInfo="用户名不能为空！";
          return;
        }
        if(this.password.trim()==""){
          this.errorInfo="密码不能为空！";
          return;
        }
        axios.post("http://localhost:80/login",{"username":this.username, "password": this.password})
          .then(response=>{
            console.log(response.data);
          }).catch(error=>{
            this.errorInfo=error;
        })
      }
    }
  }

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .login-container {
    display: flex;
    justify-content: center;
    margin-top: 150px;
  }
  .errorInfo{
    text-align:center;
    font-weight: bold;
    color: red;
  }
</style>

```

## 05_跨域问题解决

WebAppConfiguurer.java

```java
package com.sw.miaosha.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2021-01-25 21:54
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE","OPTIONS")
                .maxAge(3600);
    }
}

```

## 06_登录功能基本实现

返回的封闭 R.java

```java
package com.sw.miaosha.bo;

import java.util.HashMap;
import java.util.Map;

/**
 * 页面响应entity
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2019-08-13 上午 10:00
 */
public class R extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 0);
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}

```

LoginController.java

```java
package com.sw.miaosha.controller;

import com.sw.miaosha.bo.R;
import com.sw.miaosha.bo.User;
import com.sw.miaosha.service.IUserService;
import com.sw.miaosha.util.StringUtil;
import com.sw.miaosha.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录控制器
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2021-01-23 21:41
 */
@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private IUserService userService;

    /**
     * 用户登录
     *
     * @return
     */
    @RequestMapping("/login")
    public R login(@RequestBody UserVo userVo) {
        if(userVo==null){
            return R.error();
        }
        if(StringUtil.isEmpty(userVo.getUsername())){
            return R.error("用户名不能为空！");
        }
        if(StringUtil.isEmpty(userVo.getPassword())){
            return R.error("密码不能为空！");
        }
        User resultUser = userService.findByUserName(userVo.getUsername());
        if(resultUser==null){
            return R.error("用户名不存在！");
        }
        if(!resultUser.getPassword().trim().equals(userVo.getPassword().trim())){
            return R.error("用户名或者密码错误！");
        }
        return R.ok("登录成功！");
    }
}
```



## 07_用户登录密码前后端MD5二次加密

Md5Util.java

```java
package com.sw.miaosha.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * md5加密工具类
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2020-11-23 18:54
 */
public class Md5Util {

    private static  final String FRONT_SALT="3dfsty"; // 前端加密盐

    private static final String BACK_SALT_BEFORE="3qx2dfdx3"; // 后端加密前置盐

    private static final String BACK_SALT_AFTER="ds2f3dsf5"; // 后端加密后置盐

    /**
     * md5加密
     * @param data
     * @return
     */
    public static String md5(String data){
        return DigestUtils.md5Hex(data);
    }

    /**
     * 前端加盐后md5加密
     * @param frontData
     * @return
     */
    public static String frontMd5(String frontData){
        return md5(FRONT_SALT+frontData);
    }

    /**
     * 后端加盐后md5加密
     * @param backData
     * @return
     */
    public static String backMd5(String backData){
        return md5(BACK_SALT_BEFORE+backData+BACK_SALT_AFTER);
    }

    public static void main(String[] args) {
        System.out.println("前端md5加密："+Md5Util.frontMd5("123456"));

        System.out.println("后端md5加密验证："+backMd5(frontMd5("123456")));
    }


}
```

md5.js

```js
var hexcase = 0;
var b64pad  = "";
var chrsz   = 8;

function hex_md5(s){ return binl2hex(core_md5(str2binl(s), s.length * chrsz));}
function b64_md5(s){ return binl2b64(core_md5(str2binl(s), s.length * chrsz));}
function hex_hmac_md5(key, data) { return binl2hex(core_hmac_md5(key, data)); }
function b64_hmac_md5(key, data) { return binl2b64(core_hmac_md5(key, data)); }
function calcMD5(s){ return binl2hex(core_md5(str2binl(s), s.length * chrsz));}

function md5_vm_test()
{
  return hex_md5("abc") == "900150983cd24fb0d6963f7d28e17f72";
}

function core_md5(x, len)
{

  x[len >> 5] |= 0x80 << ((len) % 32);
  x[(((len + 64) >>> 9) << 4) + 14] = len;
  var a =  1732584193;
  var b = -271733879;
  var c = -1732584194;
  var d =  271733878;
  for(var i = 0; i < x.length; i += 16)
  {
    var olda = a;
    var oldb = b;
    var oldc = c;
    var oldd = d;

    a = md5_ff(a, b, c, d, x[i+ 0], 7 , -680876936);
    d = md5_ff(d, a, b, c, x[i+ 1], 12, -389564586);
    c = md5_ff(c, d, a, b, x[i+ 2], 17,  606105819);
    b = md5_ff(b, c, d, a, x[i+ 3], 22, -1044525330);
    a = md5_ff(a, b, c, d, x[i+ 4], 7 , -176418897);
    d = md5_ff(d, a, b, c, x[i+ 5], 12,  1200080426);
    c = md5_ff(c, d, a, b, x[i+ 6], 17, -1473231341);
    b = md5_ff(b, c, d, a, x[i+ 7], 22, -45705983);
    a = md5_ff(a, b, c, d, x[i+ 8], 7 ,  1770035416);
    d = md5_ff(d, a, b, c, x[i+ 9], 12, -1958414417);
    c = md5_ff(c, d, a, b, x[i+10], 17, -42063);
    b = md5_ff(b, c, d, a, x[i+11], 22, -1990404162);
    a = md5_ff(a, b, c, d, x[i+12], 7 ,  1804603682);
    d = md5_ff(d, a, b, c, x[i+13], 12, -40341101);
    c = md5_ff(c, d, a, b, x[i+14], 17, -1502002290);
    b = md5_ff(b, c, d, a, x[i+15], 22,  1236535329);
    a = md5_gg(a, b, c, d, x[i+ 1], 5 , -165796510);
    d = md5_gg(d, a, b, c, x[i+ 6], 9 , -1069501632);
    c = md5_gg(c, d, a, b, x[i+11], 14,  643717713);
    b = md5_gg(b, c, d, a, x[i+ 0], 20, -373897302);
    a = md5_gg(a, b, c, d, x[i+ 5], 5 , -701558691);
    d = md5_gg(d, a, b, c, x[i+10], 9 ,  38016083);
    c = md5_gg(c, d, a, b, x[i+15], 14, -660478335);
    b = md5_gg(b, c, d, a, x[i+ 4], 20, -405537848);
    a = md5_gg(a, b, c, d, x[i+ 9], 5 ,  568446438);
    d = md5_gg(d, a, b, c, x[i+14], 9 , -1019803690);
    c = md5_gg(c, d, a, b, x[i+ 3], 14, -187363961);
    b = md5_gg(b, c, d, a, x[i+ 8], 20,  1163531501);
    a = md5_gg(a, b, c, d, x[i+13], 5 , -1444681467);
    d = md5_gg(d, a, b, c, x[i+ 2], 9 , -51403784);
    c = md5_gg(c, d, a, b, x[i+ 7], 14,  1735328473);
    b = md5_gg(b, c, d, a, x[i+12], 20, -1926607734);
    a = md5_hh(a, b, c, d, x[i+ 5], 4 , -378558);
    d = md5_hh(d, a, b, c, x[i+ 8], 11, -2022574463);
    c = md5_hh(c, d, a, b, x[i+11], 16,  1839030562);
    b = md5_hh(b, c, d, a, x[i+14], 23, -35309556);
    a = md5_hh(a, b, c, d, x[i+ 1], 4 , -1530992060);
    d = md5_hh(d, a, b, c, x[i+ 4], 11,  1272893353);
    c = md5_hh(c, d, a, b, x[i+ 7], 16, -155497632);
    b = md5_hh(b, c, d, a, x[i+10], 23, -1094730640);
    a = md5_hh(a, b, c, d, x[i+13], 4 ,  681279174);
    d = md5_hh(d, a, b, c, x[i+ 0], 11, -358537222);
    c = md5_hh(c, d, a, b, x[i+ 3], 16, -722521979);
    b = md5_hh(b, c, d, a, x[i+ 6], 23,  76029189);
    a = md5_hh(a, b, c, d, x[i+ 9], 4 , -640364487);
    d = md5_hh(d, a, b, c, x[i+12], 11, -421815835);
    c = md5_hh(c, d, a, b, x[i+15], 16,  530742520);
    b = md5_hh(b, c, d, a, x[i+ 2], 23, -995338651);
    a = md5_ii(a, b, c, d, x[i+ 0], 6 , -198630844);
    d = md5_ii(d, a, b, c, x[i+ 7], 10,  1126891415);
    c = md5_ii(c, d, a, b, x[i+14], 15, -1416354905);
    b = md5_ii(b, c, d, a, x[i+ 5], 21, -57434055);
    a = md5_ii(a, b, c, d, x[i+12], 6 ,  1700485571);
    d = md5_ii(d, a, b, c, x[i+ 3], 10, -1894986606);
    c = md5_ii(c, d, a, b, x[i+10], 15, -1051523);
    b = md5_ii(b, c, d, a, x[i+ 1], 21, -2054922799);
    a = md5_ii(a, b, c, d, x[i+ 8], 6 ,  1873313359);
    d = md5_ii(d, a, b, c, x[i+15], 10, -30611744);
    c = md5_ii(c, d, a, b, x[i+ 6], 15, -1560198380);
    b = md5_ii(b, c, d, a, x[i+13], 21,  1309151649);
    a = md5_ii(a, b, c, d, x[i+ 4], 6 , -145523070);
    d = md5_ii(d, a, b, c, x[i+11], 10, -1120210379);
    c = md5_ii(c, d, a, b, x[i+ 2], 15,  718787259);
    b = md5_ii(b, c, d, a, x[i+ 9], 21, -343485551);

    a = safe_add(a, olda);
    b = safe_add(b, oldb);
    c = safe_add(c, oldc);
    d = safe_add(d, oldd);
  }
  return Array(a, b, c, d);

}

function md5_cmn(q, a, b, x, s, t)
{
  return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s),b);
}
function md5_ff(a, b, c, d, x, s, t)
{
  return md5_cmn((b & c) | ((~b) & d), a, b, x, s, t);
}
function md5_gg(a, b, c, d, x, s, t)
{
  return md5_cmn((b & d) | (c & (~d)), a, b, x, s, t);
}
function md5_hh(a, b, c, d, x, s, t)
{
  return md5_cmn(b ^ c ^ d, a, b, x, s, t);
}
function md5_ii(a, b, c, d, x, s, t)
{
  return md5_cmn(c ^ (b | (~d)), a, b, x, s, t);
}

function core_hmac_md5(key, data)
{
  var bkey = str2binl(key);
  if(bkey.length > 16) bkey = core_md5(bkey, key.length * chrsz);

  var ipad = Array(16), opad = Array(16);
  for(var i = 0; i < 16; i++)
  {
    ipad[i] = bkey[i] ^ 0x36363636;
    opad[i] = bkey[i] ^ 0x5C5C5C5C;
  }

  var hash = core_md5(ipad.concat(str2binl(data)), 512 + data.length * chrsz);
  return core_md5(opad.concat(hash), 512 + 128);
}

function safe_add(x, y)
{
  var lsw = (x & 0xFFFF) + (y & 0xFFFF);
  var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
  return (msw << 16) | (lsw & 0xFFFF);
}

function bit_rol(num, cnt)
{
  return (num << cnt) | (num >>> (32 - cnt));
}

function str2binl(str)
{
  var bin = Array();
  var mask = (1 << chrsz) - 1;
  for(var i = 0; i < str.length * chrsz; i += chrsz)
    bin[i>>5] |= (str.charCodeAt(i / chrsz) & mask) << (i%32);
  return bin;
}

function binl2hex(binarray)
{
  var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
  var str = "";
  for(var i = 0; i < binarray.length * 4; i++)
  {
    str += hex_tab.charAt((binarray[i>>2] >> ((i%4)*8+4)) & 0xF) +
      hex_tab.charAt((binarray[i>>2] >> ((i%4)*8  )) & 0xF);
  }
  return str;
}

function binl2b64(binarray)
{
  var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
  var str = "";
  for(var i = 0; i < binarray.length * 4; i += 3)
  {
    var triplet = (((binarray[i   >> 2] >> 8 * ( i   %4)) & 0xFF) << 16)
      | (((binarray[i+1 >> 2] >> 8 * ((i+1)%4)) & 0xFF) << 8 )
      |  ((binarray[i+2 >> 2] >> 8 * ((i+2)%4)) & 0xFF);
    for(var j = 0; j < 4; j++)
    {
      if(i * 8 + j * 6 > binarray.length * 32) str += b64pad;
      else str += tab.charAt((triplet >> 6*(3-j)) & 0x3F);
    }
  }
  return str;
}

export {
  hex_md5
}

```

## 08_分布式会话redis实现

```sh
docker pull redis:6.0.8
docker run -it -p 6379:6379 redis:6.0.8
```

application.yml

```yaml
server:
  port: 80
  servlet:
    context-path: /

spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.132.25:3306/db_miaosha?serverTimezone=Asia/Shanghai
    username: root
    password: 123

  redis:  # redis配置
    host: 192.168.132.111 # IP
    port: 6379  # 端口
    password:  # 密码
    connect-timeout: 10s  # 连接超时时间
    lettuce: # lettuce redis客户端配置
      pool:  # 连接池配置
        max-active: 8  # 连接池最大连接数（使用负值表示没有限制） 默认 8
        max-wait: 200s  # 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
        max-idle: 8 # 连接池中的最大空闲连接 默认 8
        min-idle: 0 # 连接池中的最小空闲连接 默认 0

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true  # 开启驼峰功能  userName  - >  user_name
    auto-mapping-behavior: full  # 自动mapping映射
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath:mybatis/mapper/*.xml


```

RedisConfig.java

```java
package com.sw.miaosha.config;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;

/**
 * Redis配置类
 *
 * @author Administrator
 *
 */
@Configuration
public class RedisConfig {


	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private Integer port;
	@Value("${spring.redis.password}")
	private String password;
	@Value("${spring.redis.lettuce.pool.max-idle}")
	private Integer maxIdle;
	@Value("${spring.redis.lettuce.pool.min-idle}")
	private Integer minIdle;
	@Value("${spring.redis.lettuce.pool.max-active}")
	private Integer maxTotal;
	@Value("${spring.redis.lettuce.pool.max-wait}")
	private Duration maxWaitMillis;


	@Bean
	LettuceConnectionFactory lettuceConnectionFactory() {
		// 连接池配置
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxIdle(maxIdle == null ? 8 : maxIdle);
		poolConfig.setMinIdle(minIdle == null ? 1 : minIdle);
		poolConfig.setMaxTotal(maxTotal == null ? 8 : maxTotal);
		poolConfig.setMaxWaitMillis(maxWaitMillis == null ? 5000L : maxWaitMillis.toMillis());
		LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.builder()
				.poolConfig(poolConfig)
				.build();
		// 单机redis
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
		redisConfig.setHostName(host);
		redisConfig.setPort(port);
		if (password != null && !"".equals(password)) {
			redisConfig.setPassword(password);
		}

		// redisConfig.setPassword(password);

		return new LettuceConnectionFactory(redisConfig, lettucePoolingClientConfiguration);
	}


	@Bean(name="redisTemplate1")
	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(lettuceConnectionFactory);
		//序列化类
		MyRedisSerializer myRedisSerializer = new MyRedisSerializer();
		//key序列化方式
		template.setKeySerializer(new StringRedisSerializer());
		//value序列化
		template.setValueSerializer(myRedisSerializer);
		//value hashmap序列化
		template.setHashValueSerializer(myRedisSerializer);
		return template;
	}

	@Bean(name="redisTemplate2")
	public RedisTemplate<String, Object> redisTemplate2(LettuceConnectionFactory lettuceConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(lettuceConnectionFactory);
		//序列化类
		MyRedisSerializer myRedisSerializer = new MyRedisSerializer();
		//key序列化方式
		template.setKeySerializer(new StringRedisSerializer());
		//value序列化
		template.setValueSerializer(new StringRedisSerializer());
		return template;
	}

	static class MyRedisSerializer implements RedisSerializer<Object> {

		@Override
		public byte[] serialize(Object o) throws SerializationException {
			return serializeObj(o);
		}

		@Override
		public Object deserialize(byte[] bytes) throws SerializationException {
			return deserializeObj(bytes);
		}

		/**
		 * 序列化
		 * @param object
		 * @return
		 */
		private static byte[] serializeObj(Object object) {
			ObjectOutputStream oos = null;
			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				byte[] bytes = baos.toByteArray();
				return bytes;
			} catch (Exception e) {
				throw new RuntimeException("序列化失败!", e);
			}
		}

		/**
		 * 反序列化
		 * @param bytes
		 * @return
		 */
		private static Object deserializeObj(byte[] bytes) {
			if (bytes == null){
				return null;
			}
			ByteArrayInputStream bais = null;
			try {
				bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);
				return ois.readObject();
			} catch (Exception e) {
				throw new RuntimeException("反序列化失败!", e);
			}
		}

	}
}

```

LoginController.java

```java
    /**
     * 用户登录
     *
     * @return
     */
    @RequestMapping("/login")
    public R login(@RequestBody UserVo userVo) {
        if(userVo==null){
            return R.error();
        }
        if(StringUtil.isEmpty(userVo.getUsername())){
            return R.error("用户名不能为空！");
        }
        if(StringUtil.isEmpty(userVo.getPassword())){
            return R.error("密码不能为空！");
        }
        User resultUser = userService.findByUserName(userVo.getUsername());
        if(resultUser==null){
            return R.error("用户名不存在！");
        }
        if(!resultUser.getPassword().trim().equals(Md5Util.backMd5(userVo.getPassword().trim()))){
            return R.error("用户名或者密码错误！");
        }
        String token= UUIDUtil.genUuid();   //生成tokem
        redisUtil.set(Constant.REDIS_TOKEN_PREFIX, token, resultUser,Constant.REDIS_TOKEN_EXPIRE);
        return R.ok(token);
    }
```

前端

Login.vue

```vue
<template>
  <div class="login-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>分布式秒杀系统-用户登录</span>
      </div>

      <el-form ref="form" label-width="80px">
        <el-form-item  label="用户名：">
          <el-input v-model="username" placeholder="请输入用户名" style="width:250px"></el-input>
        </el-form-item>
        <el-form-item label="密码：">
          <el-input v-model="password" type="password" placeholder="请输入密码" style="width:250px"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width:250px" @click="submit">登录</el-button>
          <p class="errorInfo">{{errorInfo}}</p>
        </el-form-item>
      </el-form>
      <div>
        版权所有：<a href="www.java1234.vip">www.java1234.vip</a>
      </div>
    </el-card>
  </div>
</template>

<script>
  import axios from "axios"
  import {hex_md5} from "@/util/md5";
  import {getServerUrl} from "@/config/sys";

  export default {
    name: 'Login',
    data(){
      return{
        username:'',
        password:'',
        errorInfo:""
      }
    },
    methods:{
      submit(){
        if(this.username.trim()==""){
          this.errorInfo="用户名不能为空！";
          return;
        }
        if(this.password.trim()==""){
          this.errorInfo="密码不能为空！";
          return;
        }
        let salt="3dfsty";
        let url = getServerUrl("login");
        axios.post(url,{"username":this.username, "password": hex_md5(salt+this.password)})
          .then(response=>{
            console.log(response.data);
            let data=response.data;
            if (data.code==500){
              this.errorInfo=data.msg;
            }else if(data.code==0){
              console.log(data.msg);
              window.sessionStorage.setItem("token", data.msg);	//存到sessionStorage
            }
          }).catch(error=>{
            this.errorInfo=error;
        })
      }
    }
  }

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .login-container {
    display: flex;
    justify-content: center;
    margin-top: 150px;
  }
  .errorInfo{
    text-align:center;
    font-weight: bold;
    color: red;
  }
</style>

```



## 09_前端定周期性续期token

TokenController.java

```java
package com.sw.miaosha.controller;

import com.sw.miaosha.bo.R;
import com.sw.miaosha.constant.Constant;
import com.sw.miaosha.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * token操作控制器
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2021-01-28 13:08
 */
@RestController
@RequestMapping("/")
public class TokenController {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * token续期
     * @param request
     * @return
     */
    @GetMapping(value = "/refreshToken")
    public R refreshToken(HttpServletRequest request){
        String token=request.getHeader("token");
        System.out.println("a有效期："+redisUtil.getExpire(Constant.REDIS_TOKEN_PREFIX+token));
        redisUtil.expire(Constant.REDIS_TOKEN_PREFIX,token,Constant.REDIS_TOKEN_EXPIRE);
        System.out.println("b有效期："+redisUtil.getExpire(Constant.REDIS_TOKEN_PREFIX+token));
        return R.ok();
    }
}

```

Header.vue

```vue
<template>
  <div>
    <img src="~@/assets/logo.png" height="120px" width="300px" alt="分布式电商秒杀系统"/>
  </div>

</template>

<script>
  import axios from 'axios'
  import {getServerUrl} from "@/config/sys";

  export default {
    name: "Header",
    methods:{
      refreshToken(){
        let url=getServerUrl("refreshToken");
        let token=window.sessionStorage.getItem("token");
        axios.defaults.headers.common['token']=token;
        axios.get(url,{})
          .then(response=>{
            console.log(response.data);
            if(response.data.code==0){
              console.log('token刷新成功');
            }
          }).catch(error=>{
          alert(error+"-请联系管理员");
        })

      }
    },
    mounted() {
      setInterval(this.refreshToken,1000*60*10); // 10分钟刷新一次token
      // setInterval(this.refreshToken,1000); // 10分钟刷新一次token
    }
  }
</script>

<style scoped>

</style>
```

## 10_拦截器实现鉴权

SysInterceptor.java

```java
package com.sw.miaosha.interceptor;

import com.sw.miaosha.constant.Constant;
import com.sw.miaosha.util.RedisUtil;
import com.sw.miaosha.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 鉴权拦截器
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2021-01-29 14:11
 */
public class SysInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path=request.getRequestURI();	//请求的url
        System.out.println(path);
        if(handler instanceof HandlerMethod){
            String token = request.getHeader("token");
            System.out.println("token:"+token);
            if(StringUtil.isEmpty(token)){
                System.out.println("token为空！");
                throw new RuntimeException("签名验证不存在");
            }else{
                Object o=redisUtil.get(Constant.REDIS_TOKEN_PREFIX,token);
                if(o!=null){
                    System.out.println("验证成功");
                    return true;
                }else{
                    System.out.println("验证失败");
                    throw new RuntimeException("签名失败");
                }
            }
        }else{
            return true;
        }
    }
}
```

## 11_全局统一异常处理

GlobalExceptionHandler.java

```java
package com.sw.miaosha.expection;

import com.sw.miaosha.bo.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2021-01-29 15:16
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHanlder {


    @ExceptionHandler(value = Exception.class)
    public R exceptionHandler(HttpServletRequest request, Exception e){
        System.out.println("全局异常捕获");
        return R.error("服务端异常，请联系管理员"+"<br/>"+e.getMessage()+"<br/>"+e.getStackTrace());
    }
}

```

# 秒杀接口

## 12_获取所有秒杀商品数据接口实现



## 13_秒杀商品查询接口性能优化

### 引入redis

MiaoShaGoodsController.java

```java
    /**
     * 查询所有秒杀商品
     * @return
     */
    @RequestMapping("/findAll")
    public R findAll(){
        List<MiaoShaGoodsVo> miaoShaGoodsList=null;
        Object o=redisUtil.get(Constant.REDIS_MIAOSHA_GOODS);
        if(o==null){
            System.out.println("从数据库里面查询");
            miaoShaGoodsList = miaoShaGoodsService.listAll();
            redisUtil.set(Constant.REDIS_MIAOSHA_GOODS,miaoShaGoodsList,Constant.REDIS_MIAOSHA_GOODS_EXPIRE);
        }else{
            System.out.println("从redis中取值");
            miaoShaGoodsList= (List<MiaoShaGoodsVo>) o;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("data",miaoShaGoodsList);
        return R.ok(map);
    }
```



## 14_前端秒杀商品列表显示

main.vue

```vue
<template>
  <div>
    <el-container>
      <el-header height="120px">
        <miao-sha-header></miao-sha-header>
      </el-header>
      <el-main>
        <el-table
          :data="tableData"
          style="width: 100%">
          <el-table-column
            prop="goods.name"
            label="商品名称"
            width="180">
          </el-table-column>
          <el-table-column
            prop="goods.image"
            label="商品图片"
            width="180">
          </el-table-column>
          <el-table-column
            prop="goods.price"
            label="商品原价（元）"
            width="180">
          </el-table-column>
          <el-table-column
            prop="price"
            label="秒杀价（元）"
            width="180">
          </el-table-column>
          <el-table-column
            prop="stock"
            label="库存数量"
            width="180">
          </el-table-column>
          <el-table-column
            label="操作"
            >
            <template slot-scope="scope">
              <el-button type="text" size="small">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer>Footer</el-footer>
    </el-container>
  </div>
</template>

<script>
  import MiaoShaHeader from "./common/Header"
  import {getServerUrl} from "@/config/sys";
  import axios from 'axios'

  export default {
    name: "Main",
    data(){
      return{
        tableData:[]
      }
    },
    components: {
      MiaoShaHeader
    },
    methods:{
      getMiaoShaGoods(){
        let url=getServerUrl("miaoShaGoods/findAll");
        let token=window.sessionStorage.getItem("token");
        axios.defaults.headers.common['token']=token;
        axios.get(url,{})
          .then(response=>{
            console.log(response.data.data);
            this.tableData=response.data.data;
           }).catch(error=>{
          alert(error+"-请联系管理员");
        })
      }
    },
    mounted(){
      this.getMiaoShaGoods();
    }
  }
</script>

<style scoped>

</style>

```



## 15_虚拟路径映射显示图片

```java
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**").addResourceLocations("file:E:\\miaoshaimg\\");		//file:E:\\miaoshaimg\\
    }	
```



## 16_Footer模块实现
## 17_秒杀商品详情后台接口开发
## 18_前端秒杀商品详情显示实现
## 19_秒杀倒计时等三种状态实现

```VUE
<template>
  <div>
    <el-container>
      <el-header height="120px">
        <miao-sha-header></miao-sha-header>
      </el-header>
      <el-main>
        <p class="goods_head">秒杀商品详情</p>
        <el-form label-position="left" label-width="120px">
          <el-form-item label="商品名称：">
            {{this.miaoShaGoods.goods.name}}
          </el-form-item>
          <el-form-item label="商品图片：">
            <img :src="getSrcUrl(this.miaoShaGoods.goods.image)"/>
          </el-form-item>
          <el-form-item label="商品原价：">
            {{this.miaoShaGoods.goods.price+'元'}}
          </el-form-item>
          <el-form-item label="秒杀价：">
            {{this.miaoShaGoods.price+'元'}}
          </el-form-item>
          <el-form-item label="库存数量：">
            {{this.miaoShaGoods.stock}}
          </el-form-item>
          <el-form-item label="秒杀开始时间：">
            {{this.miaoShaGoods.startTime}}
            <span v-show="miaoShaGoods.miaoShaStatus==0">秒杀倒计时：{{miaoShaGoods.remainBeginSecond}}秒</span>
            <span v-show="miaoShaGoods.miaoShaStatus==1">秒杀进行中</span>
            <span v-show="miaoShaGoods.miaoShaStatus==2">秒杀结束</span>
          </el-form-item>
          <el-form-item label="秒杀结束时间：">
            {{this.miaoShaGoods.endTime}}
          </el-form-item>
          <el-form-item>
            <el-button v-show="miaoShaGoods.miaoShaStatus==1" type="primary" size="small">立即秒杀</el-button>
          </el-form-item>
        </el-form>
      </el-main>
      <el-footer>
        <miao-sha-footer></miao-sha-footer>
      </el-footer>
    </el-container>
  </div>
</template>

<script>

  import MiaoShaHeader from "./common/Header"
  import MiaoShaFooter from "./common/Footer"
  import axios from "axios";
  import {getServerUrl} from "@/config/sys";

  export default {
    name: "Detail",
    data(){
      return{
        miaoShaGoods:{
          goods:{
            name:'',
            image:'default.jpg',
            price:0
          }
        }
      }
    },
    components:{
      MiaoShaHeader,
      MiaoShaFooter
    },
    methods:{
      getSrcUrl(t){
        return getServerUrl('image/'+t);
      },
      getInfo(){
        let url=getServerUrl("miaoShaGoods/detail");
        let token=window.sessionStorage.getItem("token");
        axios.defaults.headers.common['token']=token;
        axios.get(url,{
          params:{
            id:this.$route.params.id
          }
        }).then(response=>{
          console.log(response.data.data);
          this.miaoShaGoods=response.data.data;
          this.countDown();
          if(this.miaoShaGoods.remainEndSecond>0){ // 秒杀还没结束
            setTimeout(()=>{
              this.miaoShaGoods.miaoShaStatus=2;
            },this.miaoShaGoods.remainEndSecond*1000)
          }
        }).catch(error=>{
          alert(error+"-请联系管理员");
        })
      },
      countDown(){
        let timeout;
        let rs=this.miaoShaGoods.remainBeginSecond;
        if(rs>0){  // 秒杀还没开始，倒计时
          timeout=setTimeout(()=>{
            this.miaoShaGoods.remainBeginSecond=this.miaoShaGoods.remainBeginSecond-1;
            this.countDown();
          },1000);
        }else if(rs==0){  // 秒杀进行中
          this.miaoShaGoods.miaoShaStatus=1;
          if(timeout){
            clearTimeout(timeout);
          }
        }else{  // 秒杀结束
          this.miaoShaGoods.miaoShaStatus=2;
        }
      }
    },
    mounted() {
      this.getInfo();
    }
  }
</script>

<style scoped>

  .goods_head{
    font-weight: bold;
    font-size: 20px;
  }
</style>

```



## 20_后端秒杀接口实现

- 第一步：根据token得到用户user对象
- 第二步：判断库存是否足够
- 第三步：判断用户是否重复秒杀
- 第四步：减库存，下订单，必须同一个事务

## 21_订单接口实现
## 22_秒杀库存减1操作实现
## 23_生成秒杀订单
## 24_事务一致性测试
## 25_判断用户重复秒杀

# Jemter

## 26_Jemter快速上手

### TPS, QPS, 并发数，响应时间概念

#### 响应时间(RT)

　　响应时间是指系统对请求作出响应的时间。直观上看，这个指标与人对软件性能的主观感受是非常一致的，因为它完整地记录了整个计算机系统处理请求的时间。由于一个系统通常会提供许多功能，而不同功能的处理逻辑也千差万别，因而不同功能的响应时间也不尽相同，甚至同一功能在不同输入数据的情况下响应时间也不相同。所以，在讨论一个系统的响应时间时，人们通常是指该系统所有功能的平均时间或者所有功能的最大响应时间。当然，往往也需要对每个或每组功能讨论其平均响应时间和最大响应时间。

　　对于单机的没有并发操作的应用系统而言，人们普遍认为响应时间是一个合理且准确的性能指标。需要指出的是，响应时间的绝对值并不能直接反映软件的性能的高低，软件性能的高低实际上取决于用户对该响应时间的接受程度。对于一个游戏软件来说，响应时间小于100毫秒应该是不错的，响应时间在1秒左右可能属于勉强可以接受，如果响应时间达到3秒就完全难以接受了。而对于编译系统来说，完整编译一个较大规模软件的源代码可能需要几十分钟甚至更长时间，但这些响应时间对于用户来说都是可以接受的。

#### 吞吐量(Throughput)

​        吞吐量是指系统在单位时间内处理请求的数量。对于无并发的应用系统而言，吞吐量与响应时间成严格的反比关系，实际上此时吞吐量就是响应时间的倒数。前面已经说过，对于单用户的系统，响应时间（或者系统响应时间和应用延迟时间）可以很好地度量系统的性能，但对于并发系统，通常需要用吞吐量作为性能指标。

　　对于一个多用户的系统，如果只有一个用户使用时系统的平均响应时间是t，当有你n个用户使用时，每个用户看到的响应时间通常并不是n×t，而往往比n×t小很多（当然，在某些特殊情况下也可能比n×t大，甚至大很多）。这是因为处理每个请求需要用到很多资源，由于每个请求的处理过程中有许多不走难以并发执行，这导致在具体的一个时间点，所占资源往往并不多。也就是说在处理单个请求时，在每个时间点都可能有许多资源被闲置，当处理多个请求时，如果资源配置合理，每个用户看到的平均响应时间并不随用户数的增加而线性增加。实际上，不同系统的平均响应时间随用户数增加而增长的速度也不大相同，这也是采用吞吐量来度量并发系统的性能的主要原因。一般而言，吞吐量是一个比较通用的指标，两个具有不同用户数和用户使用模式的系统，如果其最大吞吐量基本一致，则可以判断两个系统的处理能力基本一致。

#### 并发用户数

　　并发用户数是指系统可以同时承载的正常使用系统功能的用户的数量。与吞吐量相比，并发用户数是一个更直观但也更笼统的性能指标。实际上，并发用户数是一个非常不准确的指标，因为用户不同的使用模式会导致不同用户在单位时间发出不同数量的请求。一网站系统为例，假设用户只有注册后才能使用，但注册用户并不是每时每刻都在使用该网站，因此具体一个时刻只有部分注册用户同时在线，在线用户就在浏览网站时会花很多时间阅读网站上的信息，因而具体一个时刻只有部分在线用户同时向系统发出请求。这样，对于网站系统我们会有三个关于用户数的统计数字：注册用户数、在线用户数和同时发请求用户数。由于注册用户可能长时间不登陆网站，使用注册用户数作为性能指标会造成很大的误差。而在线用户数和同事发请求用户数都可以作为性能指标。相比而言，以在线用户作为性能指标更直观些，而以同时发请求用户数作为性能指标更准确些。

#### QPS每秒查询率(Query Per Second)

　　每秒查询率QPS是对一个特定的查询服务器在规定时间内所处理流量多少的衡量标准，在因特网上，作为域名系统服务器的机器的性能经常用每秒查询率来衡量。对应fetches/sec，即每秒的响应请求数，也即是最大吞吐能力。 （看来是类似于TPS，只是应用于特定场景的吞吐量）

#### QPS计算方式

原理：每天80%的访问集中在20%的时间里，这20%时间叫做峰值时间。
公式：( 总PV数 * 80% ) / ( 每天秒数 * 20% ) = 峰值时间每秒请求数(QPS) 。
机器：峰值时间每秒QPS / 单台机器的QPS = 需要的机器 。
每天300w PV 的在单台机器上，这台机器需要多少QPS？
( 3000000 * 0.8 ) / (86400 * 0.2 ) = 139 (QPS)。
一般需要达到139QPS，因为是峰值。

> QPS 
> 每秒查询率QPS是对一个特定的查询服务器在规定时间内所处理流量多少的衡量标准。
>
> 每秒查询率
> 因特网上，经常用每秒查询率来衡量域名系统服务器的机器的性能，其即为QPS。
> 对应fetches/sec，即每秒的响应请求数，也即是最大吞吐能力。
>
> 计算机语言
> 一种计算机编程语言。用于数据分析和报表产出。运作的平台是MRDCL。支持的数据文件包括ASC格式和CSI格式。
> 其中CSI格式为QPS独有数据格式。是极其专业的用于数据分析、数据清理和报表产出的语言，目前应用最广的是市场调研行业。中国国内运用的相对比较少。

### Jmeter介绍

Apache JMeter是Apache组织开发的基于Java的压力测试工具。

官网：https://jmeter.apache.org/

我们用JMeter工具对秒杀接口进行压测，看看秒杀接口最多能承受多少并发

## 27_Jmeter单用户模拟调用秒杀接口 中文乱码解决

Jmeter 中文乱码解决：

```java
搞一个BeanShell后置处理程序
加下:
prev.setDataEncoding("utf-8");
```

## 28_Jmeter自定义用户变量模拟多用户



## 29_代码生成海量用户模拟高并发秒杀
## 30_解决高并发下的超卖问题

```sql
# 加个判断即可:
update t_goods_miaosha set stock=stock-1 where id=#{id} and stock>0
```

# 验证码实现

## 31_秒杀验证码实现

# RabbitMQ

## 32_引入RabbitMQ秒杀接口异步化



## 33_秒杀结果接口开发
## 34_前端秒杀结果轮询实现
## 35_秒杀商品信息放redis中判断

StartupRunner.java

```java
package com.java1234.miaosha.run;

import com.java1234.miaosha.constant.Constant;
import com.java1234.miaosha.service.IMiaoShaGoodsService;
import com.java1234.miaosha.util.RedisUtil;
import com.java1234.miaosha.vo.MiaoShaGoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 启动的时候加载秒杀商品库存信息和是否秒杀完标识
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2021-03-01 9:39
 */
@Component("startupRunner")
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private IMiaoShaGoodsService miaoShaGoodsService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run(String... args) throws Exception {
        List<MiaoShaGoodsVo> miaoShaGoodsList = miaoShaGoodsService.listAll();
        System.out.println("启动加载秒杀库存信息");
        for(MiaoShaGoodsVo miaoShaGoodsVo:miaoShaGoodsList){
            System.out.println(miaoShaGoodsVo.getId()+":"+miaoShaGoodsVo.getStock());
            redisUtil.set2(Constant.REDIS_STOCK_PREFIX,miaoShaGoodsVo.getId()+"",miaoShaGoodsVo.getStock()+"");
            redisUtil.set(Constant.REDIS_GOODS_MIAOSHA_OVER_PREFIX,miaoShaGoodsVo.getId()+"",false);
        }
    }
}

```



## 36_vue秒杀订单页面实现

OrderInfo.vue

```vue
<template>
  <div>
    <el-container>
      <el-header height="120px">
        <miao-sha-header></miao-sha-header>
      </el-header>
      <el-main>
        <p class="order_head">订单详情</p>
        <el-form label-position="left" label-width="120px">
          <el-form-item label="订单号：">
            {{this.order.id}}
          </el-form-item>
          <el-form-item label="商品名称：">
            {{this.order.miaoShaGoods.goods.name}}
          </el-form-item>
          <el-form-item label="商品图片：">
            <img :src="getSrcUrl(this.order.miaoShaGoods.goods.image)"/>
          </el-form-item>
          <el-form-item label="订单价：">
            {{this.order.totalPrice+'元'}}
          </el-form-item>
          <el-form-item label="订单状态：">
            <span v-show="this.order.status==0">待支付</span>
            <span v-show="this.order.status==1">已支付</span>
            <span v-show="this.order.status==2">已发货</span>
            <span v-show="this.order.status==3">已收货</span>
          </el-form-item>
          <el-form-item label="收货人：">
            {{this.order.user.name}}
          </el-form-item>
          <el-form-item label="联系电话：">
            {{this.order.user.phoneNumber}}
          </el-form-item>
          <el-form-item label="收货地址：">
            {{this.order.user.address}}
          </el-form-item>
        </el-form>
        <el-button type="primary" size="small">立即支付</el-button>
      </el-main>
      <el-footer>
        <miao-sha-footer></miao-sha-footer>
      </el-footer>
    </el-container>
  </div>
</template>

<script>

import MiaoShaHeader from "./common/Header"
import MiaoShaFooter from "./common/Footer"
import axios from "axios";
import {getServerUrl} from "@/config/sys";

export default {
  name: "OrderInfo",
  data(){
    return{
      order:{
        miaoShaGoods:{
          goods:{
            name:'',
            image:'default.jpg'
          }
        },
        user:{
          name:'',
          phoneNumber:'',
          address:''
        }
      }

    }
  },
  components:{
    MiaoShaHeader,
    MiaoShaFooter
  },
  methods:{
    getSrcUrl(t){
      return getServerUrl('image/'+t);
    },
    getInfo(){
      let url=getServerUrl("order/detail");
      let token=window.sessionStorage.getItem("token");
      axios.defaults.headers.common['token']=token;
      axios.get(url,{
        params:{
          id:this.$route.params.id
        }
      }).then(response=>{
          this.order=response.data.data;
          console.log(this.order)
        }).catch(error=>{
        alert(error+"-请联系管理员");
      })
    }
  },
  mounted() {
    this.getInfo();
  }
}
</script>

<style scoped>

  .order_head{
    font-weight: bold;
    font-size: 20px;
  }
</style>

```

# 分布式

## 37_项目分布式拆分

![image-20210723110018439](https://gitee.com/jstone001/booknote/raw/master/jpgBed/image-20210723110018439.png)

端口分配：

-  miaosha-gateway === 88
-  miaosha-goods === 8081 
-  miaosha-miaosha === 8083
-  miaosha-order === 8082
-  miaosha-order-2 === 8089
-  miaosha-user === 80

组件：

- 使用Nacos作为服务注册中心和配置中心；
- 使用OpenFeign进行微服务之间的http调用；
- 使用Gateway作为分布式微服务网关，用Gateway来进行网关限流；

## 38_Nacos服务注册与发现

application.yml

```yaml
server:
  port: 8081
  servlet:
    context-path: /

spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.132.25:3306/db_miaosha?serverTimezone=Asia/Shanghai
    username: root
    password: 123
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.132.111:8848
  application:
    name: miaosha-goods

  redis:  # redis配置
    host: 192.168.132.111 # IP
    port: 6379  # 端口
    password:  # 密码
    connect-timeout: 10s  # 连接超时时间
    lettuce: # lettuce redis客户端配置
      pool:  # 连接池配置
        max-active: 8  # 连接池最大连接数（使用负值表示没有限制） 默认 8
        max-wait: 200s  # 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
        max-idle: 8 # 连接池中的最大空闲连接 默认 8
        min-idle: 0 # 连接池中的最小空闲连接 默认 0

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true  # 开启驼峰功能  userName  - >  user_name
    auto-mapping-behavior: full  # 自动mapping映射
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath:mybatis/mapper/*.xml

```



## 39_Nacos配置中心实现

rabbitmq.yml

```yaml
spring: 
  rabbitmq: 
    host: 192.168.132.111
    port: 5672
    username: admin
    password: admin
```

mybatis-plus.yml

```yaml
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true  # 开启驼峰功能  userName  - >  user_name
    auto-mapping-behavior: full  # 自动mapping映射
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath:mybatis/mapper/*.xml
```

miaosha-user/sys.yml

```yaml
spring: 
  jackson: 
    time-zone: Asia/Shanghai
  cloud: 
    nacos: 
      discovery: 
        server-addr: 192.168.132.111:8848
  application: 
    name: miaosha-user
```

redis.yml

```yaml
spring:
  redis:  # redis配置
    host: 192.168.132.111  # IP
    port: 6379   # 端口
    password:  # 密码
    connect-timeout: 10s  # 连接超时时间
    lettuce: # lettuce redis客户端配置
      pool:  # 连接池配置
        max-active: 8  # 连接池最大连接数（使用负值表示没有限制） 默认 8
        max-wait: 200s  # 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
        max-idle: 8 # 连接池中的最大空闲连接 默认 8
        min-idle: 0 # 连接池中的最小空闲连接 默认 0
```



## 40_openfeign远程调用

### 1 将service写下在miaosha-order中，并在controller下暴露http地址

### 2 在miaosha-miaosha下

pom.xml 加上

```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
```

添加feign包

OrderFeignService.java

```java
package com.java1234.miaosha.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("miaosha-order")
public interface OrderFeignService {


    @RequestMapping("/order/getMiaoShaResult")
    String getMiaoShaResult(@RequestParam("userId") Integer useId, @RequestParam("miaoShaGoodsId") Integer miaoShaGoodsId);
}
```

在MiaoshaMiaoshaApplication.java启动类加上@EnableFeignClients(basePackages = "com.java1234.miaosha.feign") 

```java
package com.java1234.miaosha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients(basePackages = "com.java1234.miaosha.feign")        //开启Feign客户端
@SpringBootApplication
@MapperScan("com.java1234.miaosha.mapper")
@EnableDiscoveryClient
public class MiaoshaMiaoshaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaoshaMiaoshaApplication.class, args);
    }

}

```

原来的MiaoShaController修改下

```java
    /**
     * 秒杀结果查询
     * @param request
     * @param miaoShaGoodsId
     * @return  >0 返回orderId 订单ID  -1 秒杀失败  0 排队中
     */
    @RequestMapping("/result")
    public R result(HttpServletRequest request,Integer miaoShaGoodsId){
        String token = request.getHeader("token");
        System.out.println("token:"+token);
        User user=(User)redisUtil.get(Constant.REDIS_TOKEN_PREFIX,token);
        System.out.println(user);
        String result=orderFeignService.getMiaoShaResult(user.getId(),miaoShaGoodsId);
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("result",result);
        return R.ok(resultMap);
    }
```

### 3 建权

WebAppConfigurer.java  

```java
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] patterns=new String[]{"/login","/order/getMiaoShaResult","verifyCode/get"};
        registry.addInterceptor(sysInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(patterns);
    }
```



## 41_多实例集群
## 42_gateway网关限流

注意点：

- org.springframework.http.codec.ServerCodecConfigurer‘ that could not be found 报错；

> 出现该错误是因为Spring Cloud Gateway依赖了spring-boot-starter-web包
>
>  因为spring cloud gateway是基于webflux的，如果非要web支持的话需要导入spring-boot-starter-webflux而不是spring-boot-start-web。
>
>  我们这里的话 ，gateway模块单独引入依赖，不引入common模块；
>
> 新建miaosha-gateway模块

- gateway网关里面我们配置了跨域，所以项目里不用重复再配置了；

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>miaoshasys33</artifactId>
        <groupId>com.java1234</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>miaosha-gateway</artifactId>
    <dependencies>
        <!-- 服务注册/发现-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <!-- 配置中心 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>

        <!-- spring boot redis 缓存引入 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <!-- lettuce pool 缓存连接池 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
    </dependencies>
</project>

```

application.yml

```yaml
server:
  port: 88
  servlet:
      context-path: "/"

spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.132.25:3306/db_miaosha?serverTimezone=Asia/Shanghai
    username: root
    password: 123
  redis:  # redis配置
    host: 192.168.132.111 # IP
    port: 6379  # 端口
    password:  # 密码
    connect-timeout: 10s  # 连接超时时间
    lettuce: # lettuce redis客户端配置
      pool:  # 连接池配置
        max-active: 8  # 连接池最大连接数（使用负值表示没有限制） 默认 8
        max-wait: 200s  # 连接池最大阻塞等待时间（使用负值表示没有限制） 默认 -1
        max-idle: 8 # 连接池中的最大空闲连接 默认 8
        min-idle: 0 # 连接池中的最小空闲连接 默认 0
  jackson:
    time-zone: Asia/Shanghai
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.132.111:8848
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
              allowCredentials: true
              allowedHeaders: "*"
              allowedOrigins: "*"
              allowedMethods: "*"
      routes:
        - id: user_router
          uri: lb://miaosha-user
          predicates:
            - Path=/user/**

        - id: goods_router
          uri: lb://miaosha-goods
          predicates:
            - Path=/goods/**

        - id: miaoShaGoods_router
          uri: lb://miaosha-goods
          predicates:
            - Path=/miaoShaGoods/**

        - id: miaosha_router
          uri: lb://miaosha-miaosha
          predicates:
            - Path=/miaoSha/**, /verifyCode/**
          filters:
            - name: RequestRateLimiter  # 限流过滤器
              args:
                redis-rate-limiter.replenishRate: 1   # 令牌桶每秒填充速率
                redis-rate-limiter.burstCapacity: 2   # 令牌桶总容量
                redis-rate-limiter.requestedTokens: 1  # 一个请求需要消费的令牌数
                key-resolver: "#{@pathKeyResolver}"

        - id: order_router
          uri: lb://miaosha-order
          predicates:
            - Path=/order/**


  application:
    name: miaosha-gateway

```

MiaoshaGatewayApplication.java

```java
package com.java1234.miaosha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MiaoshaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaoshaGatewayApplication.class, args);
    }

}
```

KeyResolverConfiguration.java

```java
package com.java1234.miaosha.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 限流规则配置类
 * @author java1234_小锋
 * @site www.java1234.com
 * @company Java知识分享网
 * @create 2021-01-23 11:16
 */
@Configuration
public class KeyResolverConfiguration {

    @Bean
    public KeyResolver pathKeyResolver(){

        /*return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                return Mono.just(exchange.getRequest().getURI().getPath());
            }
        };*/


        return exchange -> Mono.just(exchange.getRequest().getURI().getPath());
    }
}

```

