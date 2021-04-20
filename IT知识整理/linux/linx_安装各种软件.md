# 安装jdk

```sh
#1.
tar -zxvf jdk-8u144-linux-x64.tar.gz
#2. 
vim /etc/profile

#在文本的最后一行粘贴如下：
#注意JAVA_HOME=/usr/java/jdk1.8.0_144  就是你自己的目录
#java environment
export JAVA_HOME=/usr/java/jdk1.8.0_144
export CLASSPATH=.:${JAVA_HOME}/jre/lib/rt.jar:${JAVA_HOME}/lib/dt.jar:${JAVA_HOME}/lib/tools.jar
export PATH=$PATH:${JAVA_HOME}/bin 

#3.
source /etc/profile或 . /etc/profile
#4. 
java -version
```

# 安装maven

```sh
# 官网下载mvn
http://maven.apache.org/download.cgi

tar zxvf apache-maven-3.6.1-bin.tar.gz -C /opt/

vim /etc/profile

export M2_HOME=/opt/apache-maven-3.6.1
export PATH=$PATH:$M2_HOME/bin

source /etc/profile

mvn -version
Apache Maven 3.8.1 (05c21c65bdfed0f71a2f2ada8b84da59348c4c5d)
Maven home: /software/apache-maven-3.8.1
Java version: 1.8.0_251, vendor: Oracle Corporation, runtime: /software/jdk1.8.0_251/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "3.10.0-1062.el7.x86_64", arch: "amd64", family: "unix"
```

