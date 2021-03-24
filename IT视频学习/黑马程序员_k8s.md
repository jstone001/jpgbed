# 第3章  资源管理

## P1212-资源管理介绍

## P1313-yaml语言介绍

#### 1、语法

- 大小写敏感

- 不允许使用tab, 只允许空格

- 给进的空格不重要，只要相同层级的元素左对齐即可

- #表示注释

#### 2、数据类型：纯量、对象

纯量：

- ~ 表示null
- 日期类型：2012-12-11（必须是yyyy-mm-dd）
- 时间类型：2021-01-02T17:03:33+08:00
- 字符串： 字符串过多的情况可以拆成多行，每一行会被转化在一个空格

对象：键值对集合 mapping/hash/dictionary

  对象：

```yaml
形式1：
heima:
	age:15
	address: Beijing
形式2：
heima: {age: 15, address: Beijing}
```

  数组：

```yml
形式1：
address: 
    - 顺义
    - 昌平

形式2：
address: [顺义, 昌平]
```

#### 3、提示

- 书写yaml切记：后面要加一个空格
- 如果需要将多段yaml配置放在一个文件中，中章要使用---分隔
- https://www.json2yaml.com/convert-yaml-to-json 校验
## P1414-资源管理方式-介绍

### 分类
1. 命令式对象管理：直接使用命令去操作k8s资源
```bash
kubectl run nginx-pod --image-nginx:1.17.1 --port=80
```
2. 命令式对象配置：通过命令配置和配置文件去操作k8s资源 
```bash
kubectl create/patch -f nginx-pod.yaml
```
3. 声明式对象配置：通过apply命令和配置文件去操作k8s资源
```bash
kubectl apply -f nginx-pod.yaml  #只用于创建和更新资源
```

优缺点：

| 类型 | 操作对象 | 适用环境 | 优点 | 缺点 |
| :--: | :--:| :--: |:--: |:--:|
| 命令式对象管理 | 对象 | 测试 | 简单 | 只能操作活动对象，  <br />无法审计，跟踪 |
| 命令式对象配置 | 文件 | 开发 | 可以审计，跟踪 | 项目大时，<br />配置文件多，操作麻烦 |
| 声明式对象配置 | 目录 | 开发 | 支持目录操作 | 意外情况下难以调试 |



## P1515-资源管理方式-1：命令式对象管理

### kubectl 命令的语法：

​	kubectl [command] [type] [name] [flags]

#### 1、comand：

​	指定要对资源执行的操作，例如 create、get、describe 和 delete

- 基本命令

<table border="1" style="width:70%">
    <tr align="center">
        <th ></th>   
        <th >命令</th>
        <th>中文</th>  
        <th >说明</th>  
    </tr>
    <tr   align="center">
        <td style="vertical-align:middle" rowspan="7">基本命令</td>
        <td>create</td>
        <td>创建</td>
        <td>创建一个资源</td>
    </tr>
    <tr  align="center">
        <td>edit</td>
        <td>编辑</td>
        <td>编辑一个资源</td>
    </tr>
     <tr align="center">
        <td>get</td>
        <td>获取</td>
        <td>获取一个资源</td>
    </tr>
     <tr align="center">
        <td>get</td>
        <td>获取</td>
        <td>获取一个资源</td>
    </tr>
     <tr align="center">
        <td>patch</td>
        <td>更新</td>
        <td>更新一个资源</td>
    </tr>
     <tr align="center">
        <td>delete</td>
        <td>删除</td>
        <td>删除一个资源</td>
    </tr>
     <tr align="center">
        <td>explain</td>
        <td>解释</td>
        <td>解释一个资源</td>
    </tr>
</table>




- 运行和调试

- 高级命令

- 其他命令

#### 2、type：

  指定资源类型，资源类型是大小写敏感的，开发者能够以单数、复数和缩略的 形式

#### 3、name：

  指定资源的名称，名称也大小写敏感的。如果省略名称，则会显示所有的资源

#### 4、flags：

​	指定可选的参数。例如，可用-s 或者–server 参数指定 Kubernetes API server 的地址和端口

### 例：

```bash
# 创建一个namespace
kubectl create namespace dev

# 获取namespace
kubectl get ns

# 在此namespace下创建并运行一个nginnx的pod
kubectl run pod_name --image=nginx -n dev

#查看新创建的pod
kubectl get pod -n dev

#删除指定的pod
kubectl delete pod pod_name

#删除指定的namespace
kubectl delete ns dev
```



## P1616-资源管理方式-2：命令式对象配置

nginxpod.yaml

```yml
nginxpod.yaml

apiVersion: v1
kind: Namespace
metadata:
    name: dev
---
apiVersion: v1
kind: Pod
metadata:
    name: nginxpod
    namespace: dev
spec:
  containers:
  - name: nginx-containers
    image: nginx:1.17.1

```

```bash
# 创建
kubectl create -f nginxpod.yaml

# 获得信息
kubectl get -f nginxpod.yaml

# 删除资源
kubectl delete -f nginxpod.yaml
```

总结：命令式对象配置的方式操作资源，可以简单的认为：命令+yaml配置文件（里面是命令需要的各种参数）

## P1717-资源管理方式-3：声明式对象配置

声明式对象配置跟命令式对象配置很相似，但是它只有一个命令apply

## P1818-资源管理方式-小结