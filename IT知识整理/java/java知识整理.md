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

