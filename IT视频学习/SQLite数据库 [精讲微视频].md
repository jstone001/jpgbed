https://ke.qq.com/course/271369
# 第1章   SQLite概念、数据存储方案、优点
SQLite相关概念: 

- 1: 它是一种嵌入式数据库(Oracle、Mysql、DB2等数据库不同,例如Oracle它是与程序分离) 嵌入式数据库时内嵌在程序中,是程序的一个组成部分

- 2: 与程序一起编译,不需要独立维护,简约而不简单. 

- 3：Html5、Android、IOS 都内嵌支持SQLite


# 第2章  SQLite数据类型
> SQLite可以给每个字段定义类型,但是也可以不制定,如果字段没有类型则与Javascript一样字段属于动态类型. 


- Integer：有符号的整数类型
- Real: 浮点型
- Text: 字符串(编码取决于DB的编码)
- BLOB: 存储图片、等二进制信息.
- Boolean数据类型: sqlite默认没有boolean类型, 逻辑值为0则代表false,1代表true.
- 日期与时间类型: 没有独立存储类型用于日期与时间, 一般采用Text来取代. 

## SQL查询语句(上)
```sql
/* 如果存在表,则先删除表 */
drop table if exists person;
/* 创建person表结构 */
create table if not exists person (_id integer primary key autoincrement,name text,salary real);
/* 查询表结构,sqlite_master此系统表,用来存储视图、索引、表等信息 */
select * from sqlite_master where type = 'table';
/* 查询person表的数据*/
select * from person;
/* 增加数据:测试数据类型的自动转化过程 */
insert into person (name,salary) values ('小强',6000.00),('旺财','6000.00'),('实习生',NULL),('小李',5000),('主管',10000.00);
/* 模糊查询 + 分页 + 排序 */
select * from person where name like '%%' and salary > 4000 order by salary asc limit 0,3;
/* 了解常见的聚合函数 */
select min (salary) '最低工资',max (salary) '最高工资',avg(salary) '平均工资',count(*) '总人数' from person;
/* 查询出来工资相同人的信息  SQL语句的执行顺序,先分组在聚合 */
select * from person where salary = ( select salary from person group by salary having count(*)>1);

/* 
    关于sqlite数据类型处理:
      1: 在数据默认转化不成功,也不会抛出异常(只有SQL语法错误才会抛出异常), 数据的合法性应该交给程序解决
      2: 如果表中的字段不指定类型,那么就是动态类型,按照存入的数据为主(灵活、简洁)
*/ 
update person set salary = 0 where salary is null;
insert into person (name,salary) values ('脏数据','hello');
select * from person where salary = 0;

/* 如果存在表,则先删除表 */
drop table if exists person;
/* 创建person表结构 */
create table if not exists person (_id integer primary key autoincrement,name,salary);
/* 查询表结构,sqlite_master此系统表,用来存储视图、索引、表等信息 */
select * from sqlite_master where type = 'table';
insert into person (name,salary) values ('老王','hello');

/* sqlite的视图创建: 视图就是一张虚表,可能是多张表的部分列的集合 */
drop view if exists st;
create view st as select _id,name from person;
select * from st;
select * from sqlite_master;

```
## SQL查询语句(下)