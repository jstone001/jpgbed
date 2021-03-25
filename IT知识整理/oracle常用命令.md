# 存过

## 用游标的存过：

```sql
CREATE OR REPLACE PROCEDURE PRO_SYNC_DOCUMENT AS

/**      每日同步将公示公告,政策法规->上海，国家最近的文章同步到TB_SYNC_DOCUMENT   **/

    CURSOR cur_doc IS
    SELECT a.docid    docid,
           a.title    title,
           a.subtitle subtitle,
           a.realtime pubtime,
           b.content  content
      FROM cms_document a, cms_page b, cms_channel c
     WHERE a.docid = b.docid
       and a.channelid = c.channelid
       --and a.title like '%器械%'
       AND c.channelid in (14,22,23)

   
   and  a.DOCID > (SELECT NVL(MAX(docid), 0)  FROM TB_SYNC_DOCUMENT);

BEGIN

  FOR c_doc IN cur_doc LOOP

      INSERT INTO TB_SYNC_DOCUMENT(
      DOCID,
      TITLE,
      SUBTITLE,
      PUBTIME,
      CONTENT,
      YXBZ
    )
      values(
        c_doc.docid,
        c_doc.title,
        c_doc.subtitle,
        c_doc.pubtime,
        c_doc.content,
        1

    );


  END LOOP;

COMMIT;
 --insert into MBINMSGS_TEST select * from MBINMSGS;

EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Exception happened,data was rollback');
    ROLLBACK;
END;

```



## 存过1:

```sql
create or replace procedure SP_YY_PSYS(PI_YQBM IN VARCHAR2,   --药企编码
                                       PI_PSMXBH IN VARCHAR2, --配送明细编号
                                       PI_ZXSPBM IN VARCHAR2, --商品统编代码
                                       PI_PSL IN NUMBER,      --配送数量
                                       PI_YSJG IN VARCHAR2,   --验收结果（1：验收通过，2：验收不通过）
                                       PI_YSJGBZ IN VARCHAR2, --验收结果备注
                                       PI_JBRBS  IN VARCHAR2,  --经办人
                                       PI_JBRJG  IN VARCHAR2,  --经办机构
                                       PI_YWRZ   IN NUMBER,    --业务日志ID
                                       PO_RETCODE OUT NUMBER, --返回代码
                                       PO_ERRMSG  OUT VARCHAR2) IS--错误信息
/**********************************************************************
* 程序名：SP_YY_PSYS
* 程序功能：医院配送明细验收
* 　　输入：PI_YQBM IN VARCHAR2   --药企编码
*           PI_PSMXBH IN VARCHAR2 --配送明细编号
*           PI_ZXSPBM IN VARCHAR2 --商品统编代码
*           PI_PSL IN NUMBER      --配送数量
*           PI_YSJG IN VARCHAR2   --验收结果
*           PI_YSJGBZ IN VARCHAR2 --验收结果备注
*           PI_JBRBS  IN VARCHAR2 --经办人
*           PI_JBRJG  IN VARCHAR2 --经办机构
*           PI_YWRZ   IN NUMBER   --业务日志ID
* 　　输出：PO_RETCODE OUT NUMBER --返回代码
*           PO_ERRMSG  OUT VARCHAR2--错误信息
* 版本日期：2013-08-14
* 作者：zhujiajie
* 执行类型：用户手工执行
************************************************************************/
  SP_NAME CONSTANT VARCHAR2(20) := 'SP_YY_PSYS';
  OK CONSTANT NUMBER := 0;    --执行成功返回结果
  ERROR CONSTANT NUMBER := -1;--执行失败返回结果
  MY_EXCEPTION EXCEPTION;     --自定义异常
  n_step NUMBER; --步骤
  n_count NUMBER;
  v_yqbm VARCHAR2(20);
  v_zxspbm VARCHAR2(20);
  v_ddmxbh VARCHAR2(20);
  n_pssl NUMBER(16,4);
  v_psmxzt VARCHAR2(2);
  v_psdid VARCHAR2(20);
  v_ddmxzt VARCHAR2(2);
  v_dlcgid VARCHAR2(20);
  N_RETCODE NUMBER;
  V_ERRMSG VARCHAR2(2000);
BEGIN
  --变量初始化
  v_yqbm := '';
  v_zxspbm := '';
  n_pssl := 0;

  n_step := 1;
  --根据配送明细编号，查找配送明细记录
  SELECT b.yqbm,
         a.zxspbm,
         a.ddmxbh,
         a.pssl,
         a.psmxzt,
         a.psdid
    INTO v_yqbm,
         v_zxspbm,
         v_ddmxbh,
         n_pssl,
         v_psmxzt,
         v_psdid
    FROM TB_PSMX a, TB_PSDXX b
   WHERE a.psdid = b.psdid
     AND b.psdzt = '00'
     AND psmxid = PI_PSMXBH;

  --校验传入数据的正确性
  n_step := 2;
  --药企编码一致性的校验
  IF PI_YQBM <> v_yqbm THEN
    PO_RETCODE := 1;
    PO_ERRMSG := '传入的药企编码与该配送明细的药企编码不一致，请检查';
    RAISE MY_EXCEPTION;
  END IF;
  --中心商品编码的校验
  IF PI_ZXSPBM <> v_zxspbm THEN
    PO_RETCODE := 1;
    PO_ERRMSG := '传入的中心商品编码与该配送明细的中心商品编码不一致，请检查';
    RAISE MY_EXCEPTION;
  END IF;
  --配送数量的校验
  IF PI_PSL <> n_pssl THEN
    PO_RETCODE := 1;
    PO_ERRMSG := '传入的配送数量与该配送明细的配送数量不一致，请检查';
    RAISE MY_EXCEPTION;
  END IF;
  --配送明细状态的校验
  IF v_psmxzt <> '00' THEN
    PO_RETCODE := 1;
    PO_ERRMSG := '该配送明细已验收，不能重复验收';
    RAISE MY_EXCEPTION;
  END IF;
  --验收结果的校验
  IF PI_YSJG NOT IN ('1', '2') THEN
    PO_RETCODE := 1;
    PO_ERRMSG := '传入的验收结果有误，请检查';
    RAISE MY_EXCEPTION;
  END IF;

  --更新配送明细状态
  n_step := 3;
  UPDATE tb_psmx
     SET psmxzt = decode(PI_YSJG, '1', '20', '2', '11'),
         yssj = SYSDATE,
         YSQRSJ = SYSDATE,
         yssm = PI_YSJGBZ
   WHERE PSMXID = PI_PSMXBH;

  --记录配送明细验收事件
  IF v_psmxzt = '00' THEN
    INSERT INTO TB_PSMXYSSJ(PSMXYSSJID,
                            PSMXID    ,
                            SPLX      ,
                            ZXMLID    ,
                            ZXSPBM    ,
                            SCPH      ,
                            SCRQ      ,
                            YXRQ      ,
                            PSSL      ,
                            PSMXZT    ,
                            DDMXBH    ,
                            YSZDBS    ,
                            JBRBS     ,
                            JBJG      ,
                            JBSJ      ,
                            YWLX      ,
                            YWRZID    )
         SELECT SEQ_PSMXYSSJ_ID.Nextval,
                PI_PSMXBH,
                splx,
                zxmlid,
                zxspbm,
                scph,
                scrq,
                yxrq,
                pssl,
                decode(PI_YSJG, '1', '10', '2', '11'),
                ddmxbh,
                '',
                PI_JBRBS ,
                PI_JBRJG ,
                SYSDATE,
                '12',
                PI_YWRZ
           FROM TB_PSMX
          WHERE psmxid = PI_PSMXBH;
  END IF;
  IF PI_YSJG = '1' THEN
    INSERT INTO TB_PSMXYSSJ(PSMXYSSJID,
                            PSMXID    ,
                            SPLX      ,
                            ZXMLID    ,
                            ZXSPBM    ,
                            SCPH      ,
                            SCRQ      ,
                            YXRQ      ,
                            PSSL      ,
                            PSMXZT    ,
                            DDMXBH    ,
                            YSZDBS    ,
                            JBRBS     ,
                            JBJG      ,
                            JBSJ      ,
                            YWLX      ,
                            YWRZID    )
         SELECT SEQ_PSMXYSSJ_ID.Nextval,
                PI_PSMXBH,
                splx,
                zxmlid,
                zxspbm,
                scph,
                scrq,
                yxrq,
                pssl,
                '20',
                ddmxbh,
                '',
                PI_JBRBS ,
                PI_JBRJG ,
                SYSDATE,
                '13',
                PI_YWRZ
           FROM TB_PSMX
          WHERE psmxid = PI_PSMXBH;
  END IF;

  --查看是否存在未验收的配送明细
  n_step := 4;
  SELECT COUNT(1)
    INTO n_count
    FROM tb_psmx
   WHERE psdid = v_psdid
     AND psmxzt = '00';
  --若返回查询记录数为0，说明该配送单已全部完成配送，此时需要更新配送单信息
  n_step := 5;
  IF n_count = 0 THEN
    UPDATE TB_PSDXX
       SET yswcsj = SYSDATE,
           psdzt = '10'
     WHERE psdid = v_psdid;
    --记录配送单维护事件
    n_step := 6;
    INSERT INTO tb_psdwhsj(PSDWHSJID, --配送单维护事件ID
                           PSDID    , --配送单ID
                           PSDH     , --配送单号
                           --PSDTMH   , --配送单条码号
                           YQBM     , --药企编码
                           YYBM     , --医院编码
                           PSDBM    , --配送点编码
                           PSXS     , --配送箱数
                           PSDSCSJ  , --配送单生成时间
                           PSDSBSJ  , --配送单上报时间
                           YSWCSJ   , --验收完成时间
                           PSDZT    , --配送单状态
                           JBRBS    , --经办人标识
                           JBJG     , --经办机构
                           JBSJ     , --经办时间
                           YWLX     , --业务类型
                           YWRZID   ,
                           TJLX) --业务日志ID
    SELECT seq_pswhsj_id.nextval,
           PSDID    , --配送单ID
           PSDH     , --配送单号
           --PSDTMH   , --配送单条码号
           YQBM     , --药企编码
           YYBM     , --医院编码
           PSDBM    , --配送点编码
           PSXS     , --配送箱数
           PSDSCSJ  , --配送单生成时间
           PSDSBSJ  , --配送单上报时间
           YSWCSJ   , --验收完成时间
           PSDZT    , --配送单状态
           PI_JBRBS ,
           PI_JBRJG ,
           SYSDATE,
           '05',
           PI_YWRZ,
           '1'
      FROM TB_PSDXX
     WHERE psdid = v_psdid;
  END IF;

  --更新订单明细
  v_ddmxzt := NULL;
  v_dlcgid := NULL;
  n_step := 7;
  UPDATE tb_ddmx
     SET yysl = yysl + decode(PI_YSJG, '2', 0, PI_PSL),
         YSBGSL = nvl(YSBGSL, 0) + decode(PI_YSJG, '1', 0, PI_PSL),
         ddmxzt = CASE WHEN PI_YSJG = '1' AND yysl + PI_PSL = cgsl THEN '31' ELSE ddmxzt END
   WHERE ddmxbh = v_ddmxbh
   RETURNING ddmxzt, zbid INTO v_ddmxzt, v_dlcgid;

  --处理带量采购信息表
  n_step := 8;
  IF v_dlcgid IS NOT NULL THEN
    --若返回的带量采购ID不为空，则说明该订单是带量采购订单，需要更新带量带量采购的情况
    PKG_DLCGKCCL.SP_KCCL(v_dlcgid, --带量采购招标ID
                         '0',         --虚库存处理类型  0：不处理，1：加库存，2：减库存
                         0, --虚库存处理数量
                         CASE WHEN PI_YSJG = '1' THEN '2' ELSE '0' END,  --实库存处理类型  0：不处理，1：加库存，2：减库存
                         PI_PSL,           --实库存处理数量
                         N_RETCODE,   --返回代码
                         V_ERRMSG);   --错误信息
    IF N_RETCODE <> PKG_MACRO.RET_SUCCE THEN
      PO_RETCODE          := -1;
      PO_ERRMSG            := '系统错误，调用PKG_DLCGKCCL.SP_KCCL出错：' || V_ERRMSG;
      RAISE MY_EXCEPTION;
    END IF;
  END IF;

  PO_RETCODE := OK;
EXCEPTION
  WHEN MY_EXCEPTION THEN
    IF PO_RETCODE <> 1 THEN
      PO_RETCODE := ERROR;
    END IF;
  WHEN OTHERS THEN
    PO_RETCODE := ERROR;
    PO_ERRMSG  := '在' || SP_NAME || '第' || to_char(n_step) || '步处理出现错误,错误内容为:' || SQLERRM;
END SP_YY_PSYS;


```

# 导入导出

```bash
C:\Users\Administrator> exp smpaweb/ysww2015@172.31.194.11/orcl file=d:\daochude
tail.dmp tables=(TB_DRUGSEARCHDETAIL)

imp smpaweb/smpaweb@10.1.25.61:1521/ybcp file=e:\daochudetail.dmp full=y

必须先建个空表
imp dp3dev/dp3dev@EXG00T/orcl file=/tmp/dprdev20100419.dmp  tables=(t_calendar) ignore=y	
# 重要：（如果出现xxx表空间不存在，则临时修改要导入的表空间名）
# alter tablespace TEST  rename to YWDBS;

imp smpaweb/smpaweb@localhost:1521/XE file=e:\smpaweb.dmp
imp wcms/wcms@localhost:1521/XE file=e:\wcms.dmp tables=(CMS_PAGE) ignore=y
imp smpaweb/smpaweb@localhost:1521/XE file=e:\smpa.dmp tables=(UAP_BLOB) ignore=y
imp smpaweb/smpaweb@localhost:1521/XE file=e:\smpa.dmp tables=(UAP_CLOB) ignore=y
```

# 分页

## row_number()排序分页

```sql
 select * from (select row_number() over (order by a.uploadTime desc) as rownumber,a.filesize,
  a.uploadfilename,a.uploadSaveName,a.uploadtime,a.provinceid,a.yxbz,b.provincename
 from tb_uploadFile a,tb_province b where a.provinceid=b.provinceid )
 where rownumber >0 and rownumber<=10
 
 
 select *  from (select  * from ( select rownum rn,row_number()  over (partition by picurl order by a.picid ) as rownumber,a.*
 from tb_pic   a  where a.picsize='0KB' order by picid
)
 where rownumber=1) 
 
  where rn >0 and rn<=500   --去重，排序，分页
```

## 普通分页

最常用：

```sql
SELECT *
  FROM (SELECT a.*, ROWNUM rn
          FROM (SELECT *
                  FROM table_name) a
         WHERE ROWNUM <= 40)
 WHERE rn >= 21
```



from: http://blog.sina.com.cn/s/blog_8604ca230100vro9.html

```sql
--1:无ORDER BY排序的写法。(效率最高)
--(经过测试，此方法成本最低，只嵌套一层，速度最快！即使查询的数据量再大，也几乎不受影响，速度依然！)
SELECT *
  FROM (SELECT ROWNUM AS rowno, t.*
          FROM emp t
         WHERE hire_date BETWEEN TO_DATE ('20060501', 'yyyymmdd')
                             AND TO_DATE ('20060731', 'yyyymmdd')
           AND ROWNUM <= 20) table_alias
 WHERE table_alias.rowno >= 10;


--2:有ORDER BY排序的写法。(效率最高)
--(经过测试，此方法随着查询范围的扩大，速度也会越来越慢哦！)
SELECT *
  FROM (SELECT tt.*, ROWNUM AS rowno
          FROM (  SELECT t.*
                    FROM emp t
                   WHERE hire_date BETWEEN TO_DATE ('20060501', 'yyyymmdd')
                                       AND TO_DATE ('20060731', 'yyyymmdd')
                ORDER BY create_time DESC, emp_no) tt
         WHERE ROWNUM <= 20) table_alias
 WHERE table_alias.rowno >= 10;
 
 
=================================================================================
=======================垃圾但又似乎很常用的分页写法==========================
=================================================================================
--3:无ORDER BY排序的写法。(建议使用方法1代替)
--(此方法随着查询数据量的扩张，速度会越来越慢哦！)
SELECT *
  FROM (SELECT ROWNUM AS rowno, t.*
          FROM k_task t
         WHERE flight_date BETWEEN TO_DATE ('20060501', 'yyyymmdd')
                               AND TO_DATE ('20060731', 'yyyymmdd')) table_alias
 WHERE table_alias.rowno <= 20 AND table_alias.rowno >= 10;
--TABLE_ALIAS.ROWNO  between 10 and 100;


--4:有ORDER BY排序的写法.(建议使用方法2代替)
--(此方法随着查询范围的扩大，速度会越来越慢哦！)
SELECT *
  FROM (SELECT tt.*, ROWNUM AS rowno
          FROM (  SELECT *
                    FROM k_task t
                   WHERE flight_date BETWEEN TO_DATE ('20060501', 'yyyymmdd')
                                         AND TO_DATE ('20060531', 'yyyymmdd')
                ORDER BY fact_up_time, flight_no) tt) table_alias
 WHERE table_alias.rowno BETWEEN 10 AND 20;


--5另类语法。(有ORDER BY写法）
--(语法风格与传统的SQL语法不同，不方便阅读与理解，为规范与统一标准，不推荐使用。)
WITH partdata AS
     (
        SELECT ROWNUM AS rowno, tt.*
          FROM (  SELECT *
                    FROM k_task t
                   WHERE flight_date BETWEEN TO_DATE ('20060501', 'yyyymmdd')
                                         AND TO_DATE ('20060531', 'yyyymmdd')
                ORDER BY fact_up_time, flight_no) tt
         WHERE ROWNUM <= 20)
SELECT *
  FROM partdata
 WHERE rowno >= 10;
 
--6另类语法 。(无ORDER BY写法）
WITH partdata AS
     (
        SELECT ROWNUM AS rowno, t.*
          FROM k_task t
         WHERE flight_date BETWEEN TO_DATE ('20060501', 'yyyymmdd')
                               AND TO_DATE ('20060531', 'yyyymmdd')
           AND ROWNUM <= 20)
SELECT *
  FROM partdata
 WHERE rowno >= 10;
 
 
 
yangtingkun分析：
  --- from ：http://yangtingkun.itpub.net/post/468/100278
Oracle的分页查询语句基本上可以按照本文给出的格式来进行套用。
分页查询格式：
SELECT *
  FROM (SELECT a.*, ROWNUM rn
          FROM (SELECT *
                  FROM table_name) a
         WHERE ROWNUM <= 40)
 WHERE rn >= 21
其中最内层的查询SELECT * FROM TABLE_NAME表示不进行翻页的原始查询语句。ROWNUM <= 40和RN >= 21控制分页查询的每页的范围。
上面给出的这个分页查询语句，在大多数情况拥有较高的效率。分页的目的就是控制输出结果集大小，将结果尽快的返回。在上面的分页查询语句中，这种考虑主要体现在WHERE ROWNUM <= 40这句上。
选择第21到40条记录存在两种方法，一种是上面例子中展示的在查询的第二层通过ROWNUM <= 40来控制最大值，在查询的最外层控制最小值。而另一种方式是去掉查询第二层的WHERE ROWNUM <= 40语句，在查询的最外层控制分页的最小值和最大值。这是，查询语句如下：
SELECT *
  FROM (SELECT a.*, ROWNUM rn
          FROM (SELECT *
                  FROM table_name) a)
 WHERE rn BETWEEN 21 AND 40
 
对比这两种写法，绝大多数的情况下，第一个查询的效率比第二个高得多。
这是由于CBO优化模式下，Oracle可以将外层的查询条件推到内层查询中，以提高内层查询的执行效率。对于第一个查询语句，第二层的查询条件WHERE ROWNUM <= 40就可以被Oracle推入到内层查询中，这样Oracle查询的结果一旦超过了ROWNUM限制条件，就终止查询将结果返回了。
而第二个查询语句，由于查询条件BETWEEN 21 AND 40是存在于查询的第三层，而Oracle无法将第三层的查询条件推到最内层（即使推到最内层也没有意义，因为最内层查询不知道RN代表什么）。因此，对于第二个查询语句，Oracle最内层返回给中间层的是所有满足条件的数据，而中间层返回给最外层的也是所有数据。数据的过滤在最外层完成，显然这个效率要比第一个查询低得多。
上面分析的查询不仅仅是针对单表的简单查询，对于最内层查询是复杂的多表联合查询或最内层查询包含排序的情况一样有效。
这里就不对包含排序的查询进行说明了，下一篇文章会通过例子来详细说明。
 
下面简单讨论一下多表联合的情况。
对于最常见的等值表连接查询，CBO一般可能会采用两种连接方式NESTED LOOP和HASH JOIN（MERGE JOIN效率比HASH JOIN效率低，一般CBO不会考虑）。在这里，由于使用了分页，因此指定了一个返回的最大记录数，NESTED LOOP在返回记录数超过最大值时可以马上停止并将结果返回给中间层，而HASH JOIN必须处理完所有结果集（MERGE JOIN也是）。那么在大部分的情况下，对于分页查询选择NESTED LOOP作为查询的连接方法具有较高的效率（分页查询的时候绝大部分的情况是查询前几页的数据，越靠后面的页数访问几率越小）。
因此，如果不介意在系统中使用HINT的话，可以将分页的查询语句改写为：
SELECT *
  FROM (SELECT a.*, ROWNUM rn
          FROM (SELECT *
                  FROM table_name) a
         WHERE ROWNUM <= 40)
 WHERE rn >= 21
```

