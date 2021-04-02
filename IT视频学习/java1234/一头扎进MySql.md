# 第17讲
## 第2节 创建存过和函数
```sql
DELIMITER $$

USE `db_jpxgyw`$$

DROP PROCEDURE IF EXISTS `pro_gallery`$$

CREATE DEFINER=`root`@`%` PROCEDURE `pro_gallery`(IN cId INT,OUT count_num INT)
    READS SQL DATA
BEGIN
		SELECT COUNT(*) FROM tb_gallery WHERE categoryId=cId;
	END$$

DELIMITER ;

CALL pro_gallery(2,@total); ##调用存过


###函数的创建

DELIMITER &&
CREATE FUNCTION func_gal (cId INT) RETURNS VARCHAR(20)
BEGIN
	RETURN (SELECT COUNT(1) FROM tb_gallery WHERE categoryId=cId) ;
END
&& DELIMITER;


SELECT func_gal(2)  ###调用函数

```

# 第18讲 存过和函数
## 2 创建存过和函数
### 2.3 变量的使用
```sql
--定义变量
DECLEAR a,b VARCHAR(20);

--变量的赋值
DROP PROCEDURE IF EXISTS `proc_t1`$$

CREATE DEFINER=`root`@`%` PROCEDURE `proc_t1`()
BEGIN
		DECLARE a,b VARCHAR(20);
		SET a='java1',b='java2';
		INSERT INTO tb_test VALUES(NULL,a,b);
	END$$

DELIMITER ;


--第二种方法
DELIMITER $$

USE `db_jpxgyw`$$

DROP PROCEDURE IF EXISTS `proc_t1`$$

CREATE DEFINER=`root`@`%` PROCEDURE `proc_t1`()
BEGIN
		DECLARE a,b VARCHAR(20);
		SET a='java1',b='java2';
		INSERT INTO tb_test VALUES(NULL,a,b);
	END$$

DELIMITER ;
```
### 2.4 游标
```mysql
DELIMITER &&
	CREATE PROCEDURE proc_t3()
	BEGIN 
		DECLARE a,b VARCHAR(20);       
		DECLARE cur_user CURSOR FOR SELECT n1,n2 FROM tb_test WHERE id=1;    ##声明
		OPEN cur_user;       ##打开
		FETCH cur_user INTO a,b;        ##使用
		INSERT INTO tb_test1 VALUES(NULL,a,b);
		CLOSE cur_user;     ##关闭
	END


&& DELIMITER ;
```

### 2.5 流程控制
IF语句
```mysql
DELIMITER &&
CREATE PROCEDURE proc_t4(IN cId INT)
	BEGIN
		SELECT COUNT(*) INTO @num FROM tb_test WHERE id=cid;
		IF @num>0 THEN UPDATE tb_test1 SET n1='java1234' WHERE id=cId;
		ELSE
			INSERT INTO tb_test1 VALUES(NULL,'123','123');
		END IF;
		
	END

&&
DELIMITER ;
```
2、CASE语句

3、LOOP,LEAVE语句
```mysql
DELIMITER
 &&
	CREATE PROCEDURE proc_t6 (IN totalNum INT)
	BEGIN
		aaa:LOOP
			SET totalNum=totalNum-1;
			IF totalNum=0 THEN LEAVE aaa;
			ELSE INSERT INTO tb_test1 VALUES(totalNum,'123','1234');
			END IF;

		END LOOP aaa;
	END
&& DELIMITER;
```

4、ITERATE语句
类似于java的continue

5、REPEAT语句

```mysql
DELIMITER &&
	CREATE PROCEDURE proc_t6(IN totalnum INT)
	BEGIN 
	REPEAT
		SET totalnum=totalnum-1;
		INSERT INTO tb_test1 VALUES(NULL,'123','1234');
		UNTIL totalnum=1
	END REPEAT;
	END


&& DELIMITER ;
```
6、WHILE语句

```
DELIMITER &&
	CREATE PROCEDURE proc_t7(IN totalnum INT)
	BEGIN 
	WHILE totalnum>0 DO 
		INSERT INTO tb_test1 VALUES(NULL,'12','123');
		SET totalnum=totalnum-1;
	END WHILE;
	END


&& DELIMITER ;
```
# 第19讲 存过和函数
## 第3节 调用存过和函数
```
call proc_t1();
```
## 第4节 查看
```mysql
##查看状态
SHOW PROCEDURE/FUNCTION LIKE 'proc_t1';
##查看定义
SHOW CREATE PROCEDURE/FUNCTION proc_t1;

在information_schema.routines里查看
```
## 第5节 修改
ALTER 一般直接删
```mysql
ALTER PROCEDURE proc_t1 COMMENT '我来测试一个表的COMMENT'
```
## 第6节 删除
```mysql
DROP PROCEDURE proc_t1;
```

# 第20讲 数据的备份和还原
## 第1节 备份
mysqldump
## 第2节 还原
```sh
MYSQL -U USERNAME -P < BACKUP.SQL
```