# 寻找导入jar包的实际位置

```java
System.out.println("!!!!!!!!!"+HSSFWorkbook.class.getProtectionDomain().getCodeSource().getLocation());
```

