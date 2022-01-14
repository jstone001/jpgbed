# 寻找导入jar包的实际位置

```java
System.out.println("!!!!!!!!!"+HSSFWorkbook.class.getProtectionDomain().getCodeSource().getLocation());
```

# java导出海量excel数据

https://mp.weixin.qq.com/s?__biz=MzIxNTAwNjA4OQ==&mid=2247523190&idx=2&sn=a7821bb4633d063e942318add58b13fc&chksm=979c3590a0ebbc86a1b40a758ab37693bec74a8dfe7b59352d47c34f2d67c093bfb68c0117ec&mpshare=1&scene=23&srcid=0823lJ14i0jnC5t2YJfFuezg&sharer_sharetime=1629713927761&sharer_shareid=81b31165b2e86b730d896e28d5acfa50#rd

# java List去重

https://blog.csdn.net/jiaobuchong/article/details/54412094

```java
//根据Person对象的id去重，那该怎么做呢？
public static List<Person> removeDupliById(List<Person> persons) {
        Set<Person> personSet = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
        personSet.addAll(persons);

        return new ArrayList<>(personSet);
    }

// 另一个
        Set<Person> personSet = new TreeSet<>(Comparator.comparing(Person::getId));
        personSet.addAll(persons);
        return new ArrayList<>(personSet);

//google 写法
// 根据id去重
     List<Person> unique = persons.stream().collect(
                collectingAndThen(
                        toCollection(() -> new TreeSet<>(comparingLong(Person::getId))), ArrayList::new)
        );

```

# jar命令加入文件到jar包中

```sh
jar -uvf0 mftcc-platform-factor-0.0.1-SNAPSHOT.jar BOOT-INF/   
# 将需要打包的文件放到建好的文件夹内，将文件夹拷贝到跟jarb包同一个目录下使用如下命令
# 0 是不压缩
```



# 算法实现

## 雪花算法

from:https://www.jianshu.com/p/2a27fbd9e71a

```java
public class IdWorker{

    //下面两个每个5位，加起来就是10位的工作机器id
    private long workerId;    //工作id
    private long datacenterId;   //数据id
    //12位的序列号
    private long sequence;

    public IdWorker(long workerId, long datacenterId, long sequence){
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0",maxDatacenterId));
        }
        System.out.printf("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerid %d",
                timestampLeftShift, datacenterIdBits, workerIdBits, sequenceBits, workerId);

        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
    }

    //初始时间戳
    private long twepoch = 1288834974657L;

    //长度为5位
    private long workerIdBits = 5L;
    private long datacenterIdBits = 5L;
    //最大值
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    //序列号id长度
    private long sequenceBits = 12L;
    //序列号最大值
    private long sequenceMask = -1L ^ (-1L << sequenceBits);
    
    //工作id需要左移的位数，12位
    private long workerIdShift = sequenceBits;
   //数据id需要左移位数 12+5=17位
    private long datacenterIdShift = sequenceBits + workerIdBits;
    //时间戳需要左移位数 12+5+5=22位
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    
    //上次时间戳，初始值为负数
    private long lastTimestamp = -1L;

    public long getWorkerId(){
        return workerId;
    }

    public long getDatacenterId(){
        return datacenterId;
    }

    public long getTimestamp(){
        return System.currentTimeMillis();
    }

     //下一个ID生成算法
    public synchronized long nextId() {
        long timestamp = timeGen();

        //获取当前时间戳如果小于上次时间戳，则表示时间戳获取出现异常
        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        //获取当前时间戳如果等于上次时间戳（同一毫秒内），则在序列号加一；否则序列号赋值为0，从0开始。
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        
        //将上次时间戳值刷新
        lastTimestamp = timestamp;

        /**
          * 返回结果：
          * (timestamp - twepoch) << timestampLeftShift) 表示将时间戳减去初始时间戳，再左移相应位数
          * (datacenterId << datacenterIdShift) 表示将数据id左移相应位数
          * (workerId << workerIdShift) 表示将工作id左移相应位数
          * | 是按位或运算符，例如：x | y，只有当x，y都为0的时候结果才为0，其它情况结果都为1。
          * 因为个部分只有相应位上的值有意义，其它位上都是0，所以将各部分的值进行 | 运算就能得到最终拼接好的id
        */
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    //获取时间戳，并与上次时间戳比较
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    //获取系统时间戳
    private long timeGen(){
        return System.currentTimeMillis();
    }

    //---------------测试---------------
    public static void main(String[] args) {
        IdWorker worker = new IdWorker(1,1,1);
        for (int i = 0; i < 30; i++) {
            System.out.println(worker.nextId());
        }
    }

}
```

# 报错

## 关于热部署Devtools出现同一个类型进行类型转换失败的问题

from： https://www.cnblogs.com/biaogejiushibiao/p/10135850.html

报错截图：

![img](E:\JS\booknote\jpgBed\1399348-20181218103726680-1151961593.png)

![img](E:\JS\booknote\jpgBed\1399348-20181218104204707-213913021.png)

https://www.cnblogs.com/biaogejiushibiao/p/10135850.html#_labelTop)

**解决方法：**

1.如果不是必须使用Devtools的热部署，可以将相关依赖去掉即可

2.如果必须使用热部署，可以将要强制类型转换的对象先转换成json格式在进行转换即可

![img](E:\JS\booknote\jpgBed\1399348-20181218104606058-264264867.png)

3.参考官方文档进行配置：

在resources目录下面创建META_INF文件夹，然后创建spring-devtools.properties文件，文件加上类似下面的配置：
restart.exclude.companycommonlibs=/mycorp-common-[\w-]+.jar
restart.include.projectcommon=/mycorp-myproj-[\w-]+.jar

**原因：**

为了实现热部署，Devtools原有自己的类加载器，进行更新，由于类加载器的不同导致类型转换失败

