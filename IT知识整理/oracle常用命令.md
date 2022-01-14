# 存过

## 如何执行存过

```sql
-- sql 窗口
call addbook('java好东西1',2);
call upete_tb_book();
-- command 窗口
exec addbook('java好东西',1)
```



## 存过生产实例

### 使用游标的存过：

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



### 存过1:

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

### 存过2

```sql
CREATE OR REPLACE PROCEDURE PRO_UPDATE_TB_DRUGSEARCH IS

  BEGIN_TIME VARCHAR2(50);--过程执行开始时间
  END_TIME VARCHAR2(50);--过程执行结束时间
  XH_TIME VARCHAR2(50);     	--消耗时间
  n_step varchar2(1);

BEGIN
 
  n_step:='1';
  SELECT TO_CHAR(SYSDATE,'yyyy-mm-dd hh24:mi:ss') INTO BEGIN_TIME FROM DUAL;--执行开始时间
  EXECUTE IMMEDIATE 'truncate table TB_DRUGSERACH1';

  n_step:='2';
  INSERT INTO TB_DRUGSERACH1(
          YPTBDM,
          YPSPM,
          TYMMC,                               --药品通用名名称
          YPCPM,                               --药品产品名名称
          SCQYMC,                              --生产企业名称
          BSJX,                                --表述剂型
          JXMC,                                --剂型名称
          GGBZ,                                --规格包装
          jylbz,--甲乙类标识
          lsj,        --零售价
          cgj,        --采购价
          cgje,  --采购金额(近3月)
          sfdlcgyp,         --是否大量采购药品 1 是 0 不是
          cgyys ,  --采购医院数(近3月)
          cgyys1,  --采购医院数(近1月)
          zxybz,                                --中西药标志
          gjybz,             --高价药标志
          xzsyz                                --限制适应症
       )
   select
       a.YPTBDM,                              --药品统编代码
       decode(a.YPSPM,'','-',a.YPSPM) YPSPM,  --药品商品名
       a.TYMMC,                               --药品通用名名称
       a.YPCPM,                               --药品产品名名称
       a.SCQYMC,                              --生产企业名称
       a.BSJX,                                --表述剂型
       a.JXMC,                                --剂型名称
       a.GGBZ,                                --规格包装
       (select aaa103 from aa10 where AAA100 = 'JYLBZ' and aaa102 = h.jylbz) jylbz,--甲乙类标识
       cast(b.lsj as number(9,2)) lsj,        --零售价
       cast(e.cgj as number(9,2)) cgj,        --采购价
       cast(d.je/10000 as number(9,2)) cgje,  --采购金额(近3月)
   --     nvl(d.cgyys,0) cgyys,                  --采购医院数(近3月)
   --    nvl(g.cgyys,0) cgyys1,                  --采购医院数(近1月)
       a.sfdlcgyp,         --是否大量采购药品 1 是 0 不是
       nvl(decode(a.sfdlcgyp,'1',f.cgyys3,d.cgyys3),0) cgyys ,  --采购医院数(近3月)
       nvl(decode(a.sfdlcgyp,'1',f.cgyys1,d.cgyys1),0) cgyys1,  --采购医院数(近1月)
       a.zxybz,                                --中西药标志
       substr(a.yptsbz,5,1) gjybz,             --高价药标志
       f.xzsyz                                --限制适应症
  from (select * from (select c.*, decode(d.zxspbm, null, '0', '1') sfdlcgyp
               from v_yppg_fbk c
               left join (select b.zxspbm
                           from tb_dlcgxx a, tb_dlcgmx b
                          where a.dlcgid = b.dlcgid
                            and zt = '20'
                            and yxbz = '1'
                            and bq in ('04', '05')) d
                 on c.yptbdm = d.zxspbm) r
    where  (substr(r.yptsbz, 1, 1) = '1' or substr(r.yptsbz, 9, 1) = '1' or
       sfdlcgyp = '1'))  a
  left outer join (select b.yptbdm, max(gzcsz) as lsj
                     from tb_ypwjgz_bdkmx b inner join tb_ypwjgz_fbk j
                     on b.ypwjbdid = j.YPWJBDID and GZLX = '00' group by b.yptbdm ) b
    on a.YPTBDM = b.yptbdm
  left outer join (select b.yptbdm, max(gzcsz) as cgj
                     from tb_ypwjgz_bdkmx b inner join tb_ypwjgz_fbk j
                     on b.ypwjbdid = j.YPWJBDID and GZLX = '10' inner join tb_ypcggz_fbk g on b.YPTBDM = g.yptbdm and

  g.YPCGLX in('1','5','6') group by b.yptbdm
                       ) e
    on a.YPTBDM = e.yptbdm
  left outer join (select p.yptbdm, sum(pjcgj * cgzsl) je,count(distinct p.yybm) cgyys3,
                        count(case when tjny=to_char(add_months(sysdate,-1),'YYYYMM') then p.yybm else null end) cgyys1
                     from TB_YPCG_YHZ p
                    where p.tjny between to_char(add_months(sysdate,-3),'YYYYMM') and to_char(sysdate,'YYYYMM')
                    group by p.yptbdm) d
    on a.YPTBDM = d.YPTBDM
    left outer join (select yptbdm,count(distinct yybm) cgyys3,
                       count(case when jsny=to_char(add_months(sysdate,-1),'YYYYMM') then yybm else null end) cgyys1
                      from tb_dlcgdzd
                      where jsny between to_char(add_months(sysdate,-3),'YYYYMM') and to_char(sysdate,'YYYYMM')
                    group by yptbdm) f
    on a.yptbdm=f.yptbdm
  left outer join (select f.yptbdm, wm_concat(f.XYTJWZSM) xzsyz from tb_ypybgz_fbk f group by f.yptbdm) f
    on a.YPTBDM = f.YPTBDM
  left outer join tb_ypybgz_fbk h
    on a.YPTBDM = h.YPTBDM;
    
    SELECT TO_CHAR(SYSDATE,'yyyy-mm-dd hh24:mi:ss') INTO END_TIME FROM DUAL;--执行结束时间
    select round(to_number(TO_DATE(END_TIME,'yyyy-mm-dd hh24:mi:ss')-TO_DATE(BEGIN_TIME,'yyyy-mm-dd hh24:mi:ss'))*1440*60)
    INTO XH_TIME FROM DUAL;
 --insert into MBINMSGS_TEST select * from MBINMSGS;
    n_step:='3';
    DBMS_OUTPUT.PUT_LINE('消耗的时间为：'||xh_time||'秒');
    COMMIT;
  EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('step'||to_char(n_step)||' ,Exception happened,error='||sqlerrm);
    ROLLBACK;
END PRO_UPDATE_TB_DRUGSEARCH;

```

### 存过3

```sql
  /****************************************************/
  --功能描述: 实现物价规则审核确认通过
  --传入参数:
  --sWhsqid  物价规则维护申请ID
  --sJbrbs   经办人标识
  --sJbjg    经办机构
  --传出参数：
  --sCode   0 成功 -1 失败
  --sMsg    错误信息 成功时为空
  /****************************************************/
  Procedure ExecWjgzshqrtg(sWhsqid in varchar2,
                           sJbrbs  in varchar2,
                           sJbjg   in varchar2,
                           sYwrzid in varchar2,
                           sCode   out number,
                           sMsg    out varchar2) is
    r_ypwjgz_whsq TB_YPWJGZ_WHSQ%ROWTYPE;
    r_ypwhlc TB_YPWHLC%ROWTYPE;
    n_count NUMBER;         --计数器
    v_tbdm VARCHAR2(20);    --药品统编代码
    v_ypwjbdid VARCHAR2(20);--药品物价本地库ID
    v_ypcggqwhsqid VARCHAR2(20);--药品采购规则维护申请ID
    v_qyrq VARCHAR2(8);     --启用日期
    s_tbdm    VARCHAR2(20);    --药品统编代码
  Begin
    --初始化数据
    SP_NAME := 'ExecWjgzshqrtg';
    sCode   := ERROR;
    --获取维护申请数据
    iStep := 1;
    SELECT *
      INTO r_ypwjgz_whsq
      FROM TB_YPWJGZ_WHSQ
     WHERE YPWJWHSQID = sWhsqid
       AND zt = '32';

    --取药品维护流程
    SELECT *
      INTO r_ypwhlc
      FROM TB_YPWHLC
     WHERE WHLCID = r_ypwjgz_whsq.whlcid;

    --校验是否可以进行审核操作
    IF r_ypwhlc.lclx = '10' THEN
      --校验是否存在待发布或者待启用的物价规则数据，若存在则不允许进行审核确认操作
      iStep := 2;
      SELECT COUNT(1)
        INTO n_count
        FROM TB_YPWJGZ_BDK
       WHERE YPTBDM = r_ypwjgz_whsq.yptbdm
         AND yxbz = '1'
         AND fbbz IN ('0', '1', '2')
         AND QYBZ = '0';
      IF n_count <> 0 THEN
        sMsg := '该统编代码已存在待启用的药品物价信息，不能再次进行审核确认';
        RAISE MY_EXCEPTION;
      END IF;
      --判断是否存在正处于流程处理中的数据，若存在则返回错误提示
      iStep := 3;
      SELECT COUNT(1)
        INTO n_count
        FROM TB_YPWHLC
       WHERE YPTBDM = r_ypwjgz_whsq.yptbdm
         AND WHLCID <> r_ypwjgz_whsq.whlcid
         AND LCBZ NOT IN ('01', '02', '03', '04', '05', '60')
         AND decode(substrb(LCBZ, 1, 1), '0', '1', substrb(LCBZ, 1, 1))
             <> decode(substrb(LCQD, 1, 1), '0', '1', substrb(LCQD, 1, 1));
      IF n_count <> 0 THEN
        sMsg := '该统编代码已处于维护流程中，不能再次进行审核确认';
        RAISE MY_EXCEPTION;
      END IF;
    END IF;
    --更新物价维护申请状态为41
    iStep := 4;
    UPDATE TB_YPWJGZ_WHSQ
       SET zt = '41',
           SQSHRQ = to_char(SYSDATE, 'yyyymmdd')
     WHERE YPWJWHSQID = sWhsqid;
    --记录物价维护申请事件表
    iStep := 5;
    INSERT INTO TB_YPWJGZ_WHSQSJ(YPWJWHSQSJID,  --药品物价维护申请事件ID
                                 YPWJWHSQID  ,  --药品物价维护申请ID
                                 WHLCID      ,  --维护流程ID
                                 WHSQLX      ,  --维护申请类型
                                 YPBDKID     ,  --药品本地库ID
                                 YPTBDM      ,  --药品统编代码
                                 YPLSDM      ,  --药品临时代码
                                 SQLRRQ      ,  --申请录入日期
                                 SQTJRQ      ,  --申请提交日期
                                 SQSHRQ      ,  --申请审核日期
                                 SHYJ        ,  --审核意见
                                 ZT          ,  --状态
                                 JBRBS       ,  --经办人标识
                                 JBJG        ,  --经办机构
                                 JBSJ        ,  --经办时间
                                 YWLX        ,  --业务类型
                                 YWRZID      )  --业务日志ID
    SELECT SEQ_YPWJGZ_WHSQSJ_ID.NEXTVAL,  --药品物价维护申请事件ID
           YPWJWHSQID  ,  --药品物价维护申请ID
           WHLCID      ,  --维护流程ID
           WHSQLX      ,  --维护申请类型
           YPBDKID     ,  --药品本地库ID
           YPTBDM      ,  --药品统编代码
           YPLSDM      ,  --药品临时代码
           SQLRRQ      ,  --申请录入日期
           SQTJRQ      ,  --申请提交日期
           SQSHRQ      ,  --申请审核日期
           SHYJ        ,  --审核意见
           ZT          ,  --状态
           sJbrbs      ,  --经办人标识
           sJbjg       ,  --经办机构
           SYSDATE     ,  --经办时间
           '04'        ,  --业务类型
           sYwrzid           --业务日志ID
      FROM TB_YPWJGZ_WHSQ
     WHERE YPWJWHSQID = sWhsqid;

    --判断是否新增的药品品规，若是，则需要生成统编代码
    iStep := 6;
    IF r_ypwhlc.lclx <> '10' THEN
      --获取中西药标志和启用日期
      SELECT decode(ZXYBZ, '1', 'XN', 'ZN'), qyrq
        INTO v_tbdm, v_qyrq
        FROM TB_YPPG_BDK
       WHERE YPBDKID = r_ypwjgz_whsq.ypbdkid;
      --生成统编代码
      SELECT v_tbdm || lpad(SEQ_YPPG_YPTBDM.Nextval, 13, '0')
        INTO v_tbdm
        FROM dual;
    ELSE
      v_tbdm := r_ypwjgz_whsq.yptbdm;
    END IF;
    --更新流程标志为41
    iStep := 7;
    UPDATE TB_YPWHLC
       SET LCBZ = '41',YPTBDM = v_tbdm
     WHERE WHLCID = r_ypwjgz_whsq.whlcid;

    --若是新增的药品品规，则更新药品本地库的药品统编代码
    iStep := 8;
    IF r_ypwhlc.lclx <> '10' THEN
      UPDATE TB_YPPG_BDK
         SET yptbdm = v_tbdm,
             ypystbdm = CASE WHEN ypystbdm IS NULL THEN v_tbdm ELSE ypystbdm END
       WHERE YPBDKID= r_ypwjgz_whsq.ypbdkid;
    END IF;
    --循环物价规则维护规则明细，逐条处理发生变更的规则数据
    FOR c_wjgz IN (SELECT *
                     FROM TB_YPWJGZ_WHGZMX
                    WHERE YPWJWHSQID = sWhsqid
                      AND yxbz = '1') LOOP
      --获取物价本地库ID
      iStep := 9;
      SELECT SEQ_YPWJGZ_BDK_ID.NEXTVAL
        INTO v_ypwjbdid
        FROM dual;
      IF c_wjgz.tqbz = '0' THEN
        --若停启标志为停用，说明是停用已有的本地库规则
        --保存停用的本地库数据
        iStep := 10;
        INSERT INTO TB_YPWJGZ_BDK(YPWJBDID  ,  --药品物价本地ID
                                  YPWJWHSQID,  --药品物价维护申请ID
                                  YPFBSJID  ,  --药品发布事件ID
                                  YPLSDM    ,  --药品临时代码
                                  YPTBDM    ,  --药品统编代码
                                  DJYBZ     ,  --低价药标志
                                  YPWJYSXXID,  --药品物价原始信息ID
                                  FBBZ      ,  --判断是否可以发布的标志
                                  FBRQ      ,  --发布日期
                                  QYRQ      ,  --启用日期
                                  TYRQ      ,  --停用日期
                                  TQBZ      ,  --停启标志
                                  QYBZ      ,  --启用标志
                                  YXBZ      ,  --有效标志
                                  BZ        ,  --备注
                                  GLYPWJBDID)  --关联本地库ID
        VALUES(v_ypwjbdid,  --药品物价本地ID
               c_wjgz.ypwjwhsqid,  --药品物价维护申请ID
               NULL      ,  --药品发布事件ID
               c_wjgz.YPLSDM    ,  --药品临时代码
               v_tbdm    ,  --药品统编代码
               c_wjgz.DJYBZ     ,  --低价药标志
               c_wjgz.YPWJYSXXID,  --药品物价原始信息ID
               --decode(r_ypwjgz_whsq.whsqlx, '00', '0', '1'),  --判断是否可以发布的标志
               '0',
               NULL      ,  --发布日期
               c_wjgz.QYRQ      ,  --启用日期
               NULL      ,  --停用日期
               '0'       ,  --停启标志
               '0'       ,  --启用标志
               '1'       ,  --有效标志
               c_wjgz.bz ,  --备注
               c_wjgz.YPWJBDID);  --关联本地库ID
        --保存规则明细本地库数据
        iStep := 11;
        INSERT INTO TB_YPWJGZ_BDKMX(YPWJBDMXID,  --药品物价规则明细ID
                                    YPWJBDID  ,  --药品物价本地ID
                                    YPLSDM    ,  --药品临时代码
                                    YPTBDM    ,  --药品统编代码
                                    GZLX      ,  --规则类型
                                    JJDW      ,  --计价单位
                                    GZCSZ     ,  --规则参数值
                                    JSGS      ,  --计算公式
                                    GZCC      ,  --规则出处
                                    BZ        )  --备注
        SELECT SEQ_YPWJGZ_BDKMX_ID.Nextval,--药品物价规则明细ID
               v_ypwjbdid  ,  --药品物价本地ID
               c_wjgz.YPLSDM    ,  --药品临时代码
               v_tbdm    ,  --药品统编代码
               GZLX      ,  --规则类型
               JJDW      ,  --计价单位
               GZCSZ     ,  --规则参数值
               JSGS      ,  --计算公式
               GZCC      ,  --规则出处
               BZ           --备注
          FROM TB_YPWJGZ_BDKWHSQMX
         WHERE YPWJWHSQGZMXID = c_wjgz.ypwjwhsqgzmxid
           AND yxbz = '1';
        --更新原本地库数据的停用日期
        iStep := 12;
        UPDATE TB_YPWJGZ_BDK
           SET tyrq = to_char(to_date(c_wjgz.qyrq, 'yyyymmdd') - 1, 'yyyymmdd')
         WHERE YPWJBDID = c_wjgz.ypwjbdid;
      ELSIF c_wjgz.tqbz = '1' THEN
        --若停启标志为启用，说明是新增的物价规则
        iStep := 13;
        INSERT INTO TB_YPWJGZ_BDK(YPWJBDID  ,  --药品物价本地ID
                                  YPWJWHSQID,  --药品物价维护申请ID
                                  YPFBSJID  ,  --药品发布事件ID
                                  YPLSDM    ,  --药品临时代码
                                  YPTBDM    ,  --药品统编代码
                                  DJYBZ     ,  --低价药标志
                                  YPWJYSXXID,  --药品物价原始信息ID
                                  FBBZ      ,  --判断是否可以发布的标志
                                  FBRQ      ,  --发布日期
                                  QYRQ      ,  --启用日期
                                  TYRQ      ,  --停用日期
                                  TQBZ      ,  --停启标志
                                  QYBZ      ,  --启用标志
                                  YXBZ      ,  --有效标志
                                  BZ        ,  --备注
                                  GLYPWJBDID)  --关联本地库ID
        VALUES(v_ypwjbdid,  --药品物价本地ID
               c_wjgz.ypwjwhsqid,  --药品物价维护申请ID
               NULL      ,  --药品发布事件ID
               c_wjgz.YPLSDM    ,  --药品临时代码
               v_tbdm    ,  --药品统编代码
               c_wjgz.DJYBZ     ,  --低价药标志
               c_wjgz.YPWJYSXXID,  --药品物价原始信息ID
               --decode(r_ypwjgz_whsq.whsqlx, '00', '0', '1'),  --判断是否可以发布的标志
               '0',
               NULL      ,  --发布日期
               c_wjgz.QYRQ      ,  --启用日期
               NULL      ,  --停用日期
               '1'       ,  --停启标志
               '0'       ,  --启用标志
               '1'       ,  --有效标志
               c_wjgz.bz ,  --备注
               NULL     );  --关联本地库ID
        --保存规则明细本地库数据
        iStep := 13;
        INSERT INTO TB_YPWJGZ_BDKMX(YPWJBDMXID,  --药品物价规则明细ID
                                    YPWJBDID  ,  --药品物价本地ID
                                    YPLSDM    ,  --药品临时代码
                                    YPTBDM    ,  --药品统编代码
                                    GZLX      ,  --规则类型
                                    JJDW      ,  --计价单位
                                    GZCSZ     ,  --规则参数值
                                    JSGS      ,  --计算公式
                                    GZCC      ,  --规则出处
                                    BZ        )  --备注
        SELECT SEQ_YPWJGZ_BDKMX_ID.Nextval,--药品物价规则明细ID
               v_ypwjbdid  ,  --药品物价本地ID
               c_wjgz.YPLSDM    ,  --药品临时代码
               v_tbdm    ,  --药品统编代码
               GZLX      ,  --规则类型
               JJDW      ,  --计价单位
               GZCSZ     ,  --规则参数值
               JSGS      ,  --计算公式
               GZCC      ,  --规则出处
               BZ           --备注
          FROM TB_YPWJGZ_BDKWHSQMX
         WHERE YPWJWHSQGZMXID = c_wjgz.ypwjwhsqgzmxid
           AND yxbz = '1';
      ELSE
        sMsg := '未知的停启标志，请检查';
        RAISE MY_EXCEPTION;
      END IF;
    END LOOP;

    --生成药事采购规则维护申请
    --获取药事采购规则维护申请ID
    iStep := 14;
    SELECT SEQ_YPCGGZ_WHSQ_ID.NEXTVAL
      INTO v_ypcggqwhsqid
      FROM dual;
    --生成药事采购规则维护申请
    iStep := 15;
    INSERT INTO TB_YPCGGZ_WHSQ(YPCGLXWHSQID,  --药品采购类型维护申请ID
                               WHLCID      ,  --维护流程ID
                               YPBDKID     ,  --药品本地库ID
                               YPLSDM      ,  --药品临时代码
                               YPTBDM      ,  --药品统编代码
                               WHSQLX      ,  --维护申请类型
                               SQLRRQ      ,  --申请录入日期
                               SQTJRQ      ,  --申请提交日期
                               SQSHRQ      ,  --申请审核日期
                               SHYJ        ,  --审核意见
                               ZT          )  --状态
    VALUES(v_ypcggqwhsqid,
           r_ypwjgz_whsq.whlcid,
           r_ypwjgz_whsq.ypbdkid,
           r_ypwjgz_whsq.yplsdm,
           v_tbdm,
           r_ypwjgz_whsq.whsqlx,
           to_char(SYSDATE, 'YYYYMMDD'),
           NULL,
           NULL,
           NULL,
           '41');
    --记录维护申请新增事件表
    iStep := 16;
    INSERT INTO TB_YPCGGZ_WHSQSJ(YPCGLXWHSQSJID,  --药品采购类型维护申请事件ID
                                 YPCGLXWHSQID  ,  --药品采购类型维护申请ID
                                 YPBDKID       ,  --药品本地库ID
                                 YPLSDM        ,  --药品临时代码
                                 YPTBDM        ,  --药品统编代码
                                 WHSQLX        ,  --维护申请类型
                                 SQLRRQ        ,  --申请录入日期
                                 SQTJRQ        ,  --申请提交日期
                                 SQSHRQ        ,  --申请审核日期
                                 SHYJ          ,  --审核意见
                                 ZT            ,  --状态
                                 JBRBS         ,  --经办人标识
                                 JBJG          ,  --经办机构
                                 JBSJ          ,  --经办时间
                                 YWLX          ,  --业务类型
                                 YWRZID        )  --业务日志ID
    SELECT SEQ_YPCGGZ_WHSQSJ_ID.NEXTVAL,  --药品采购类型维护申请事件ID
           YPCGLXWHSQID  ,  --药品采购类型维护申请ID
           YPBDKID       ,  --药品本地库ID
           YPLSDM        ,  --药品临时代码
           v_tbdm        ,  --药品统编代码
           WHSQLX        ,  --维护申请类型
           SQLRRQ        ,  --申请录入日期
           SQTJRQ        ,  --申请提交日期
           SQSHRQ        ,  --申请审核日期
           SHYJ          ,  --审核意见
           ZT            ,  --状态
           sJbrbs        ,  --经办人标识
           sJbjg         ,  --经办机构
           SYSDATE       ,  --经办时间
           '01'          ,  --业务类型
           sYwrzid             --业务日志ID
      FROM TB_YPCGGZ_WHSQ
     WHERE YPCGLXWHSQID = v_ypcggqwhsqid;
    --为流程明细表添加一条采购规则维护申请的记录
    iStep := 17;
    INSERT INTO TB_YPWHLCMX(WHLCMXID,  --维护流程明细ID
                            WHLCID  ,  --维护流程ID
                            WHSQID  ,  --维护申请ID
                            SQLCLX  )  --申请流程类型
    VALUES(SEQ_YPWHLCMX_ID.NEXTVAL,
           r_ypwjgz_whsq.whlcid,
           v_ypcggqwhsqid,
           '20');
    --若为新增的药品品规，则需要添加一条默认的采购规则明细
    IF r_ypwhlc.lclx = '00' THEN
      /*--获取药品品规本地库启用日期
      iStep := 18;
      SELECT qyrq
        INTO v_qyrq
        FROM TB_YPPG_BDK
       WHERE YPBDKID = r_ypwjgz_whsq.ypbdkid;*/
      --保存维护申请明细记录
      iStep := 19;
      INSERT INTO TB_YPCGGZ_WHSQMX(YPCGLXWHSQMXID,  --药品采购规则维护申请明细ID
                                   YPCGLXWHSQID  ,  --药品采购类型维护申请ID
                                   WHLCID        ,  --维护流程ID
                                   YPCLGXBDID    ,  --药品采购类型本地ID
                                   YPLSDM        ,  --药品临时代码
                                   YPTBDM        ,  --药品统编代码
                                   YPCGLX        ,  --药品采购类型
                                   PSXYSJ        ,  --配送响应时间
                                   JSZQ          ,  --结算周期
                                   CGZFLX        ,  --采购支付类型
                                   JCZBZT        ,  --集采招标状态
                                   QYRQ          ,  --启用日期
                                   TQBZ          ,  --停启标志
                                   YXBZ          )  --有效标志
      VALUES(SEQ_YPCGGZ_WHSQMX_ID.NEXTVAL,  --药品采购规则维护申请明细ID
             v_ypcggqwhsqid  ,  --药品采购类型维护申请ID
             r_ypwjgz_whsq.whlcid,  --维护流程ID
             NULL          ,  --药品采购类型本地ID
             r_ypwjgz_whsq.yplsdm,  --药品临时代码
             v_tbdm        ,  --药品统编代码
             '5'           ,  --药品采购类型
             10            ,  --配送响应时间
             10            ,  --结算周期
             '1'           ,  --采购支付类型
             '1'           ,  --集采招标状态(无限制)
             v_qyrq        ,  --启用日期
             '1'           ,  --停启标志
             '1'          );  --有效标志
    ELSIF r_ypwhlc.lclx = '11' THEN
      --查询是由那个统编代码继承过来的
      SELECT YPTBDM
        INTO s_tbdm
        FROM TB_YPPG_BDK
       WHERE YPBDKID = (SELECT YPBDKID
                          FROM TB_YPPG_WHSQ
                         WHERE WHLCID = r_ypwhlc.whlcid);
      --如果是修改并新增统编需要将老规则带过来
       --获取药品品规本地库启用日期
       iStep := 20;
       SELECT qyrq
          INTO v_qyrq
          FROM TB_YPPG_BDK
         WHERE YPBDKID = r_ypwjgz_whsq.ypbdkid;

       iStep := 21;
       INSERT INTO TB_YPCGGZ_WHSQMX (YPCGLXWHSQMXID,  --药品采购规则维护申请明细ID
                                     YPCGLXWHSQID  ,  --药品采购类型维护申请ID
                                     WHLCID        ,  --维护流程ID
                                     YPCLGXBDID    ,  --药品采购类型本地ID
                                     YPLSDM        ,  --药品临时代码
                                     YPTBDM        ,  --药品统编代码
                                     YPCGLX        ,  --药品采购类型
                                     PSXYSJ        ,  --配送响应时间
                                     JSZQ          ,  --结算周期
                                     CGZFLX        ,  --采购支付类型
                                     JCZBZT        ,  --集采招标状态
                                     SYYYFW        ,  --适用医院范围
                                     CGFW          ,  --采购范围
                                     GZYJ          ,  --规则依据
                                     QYRQ          ,  --启用日期
                                     TQBZ          ,  --停启标志
                                     YXBZ          )  --有效标志
       SELECT SEQ_YPCGGZ_WHSQMX_ID.NEXTVAL,  --药品采购规则维护申请明细ID
              v_ypcggqwhsqid  ,  --药品采购类型维护申请ID
              r_ypwjgz_whsq.whlcid,  --维护流程ID
              NULL          ,  --药品采购类型本地ID
              r_ypwjgz_whsq.yplsdm,  --药品临时代码
              v_tbdm        ,  --药品统编代码
              YPCGLX        ,  --药品采购类型
              PSXYSJ        ,  --配送响应时间
              JSZQ          ,  --结算周期
              CGZFLX        ,  --采购支付类型
              JCZBZT        ,  --集采招标状态
              SYYYFW        ,  --适用医院范围
              CGFW          ,  --采购范围
              GZYJ          ,  --规则依据
              v_qyrq        ,  --启用日期
              '1'           ,  --停启标志
              '1'            --有效标志
     FROM TB_YPCGGZ_FBK
    WHERE YPTBDM = s_tbdm;

    END IF;
    --审核完成
    sCode := OK;
  EXCEPTION
    WHEN MY_EXCEPTION THEN
      ROLLBACK;
      sCode := ERROR;
    When Others THEN
      ROLLBACK;
      sCode := ERROR;
      sMsg  := '在' || SP_NAME || '第' || to_char(iStep) || '步处理出现错误,错误内容为:' ||
               SQLERRM;
  End;
```

### 存过4

```sql
create or replace procedure SP_TBCWJL(PI_TBLX VARCHAR2,     --同步类型
                                      PI_TBSJB VARCHAR2,    --同步数据表
                                      PI_TBXXID VARCHAR2,   --同步信息ID
                                      PI_SJZJID VARCHAR2,   --数据主键ID
                                      PI_CWMS VARCHAR2) IS  --错误描述
/******************************************************************
  * 程序名：SP_TBCWJL
  * 程序功能：同步数据错误记录
  * 　　输入：TBLX   --同步类型
              TBSJB  --同步数据表
              TBXXID --同步信息ID
              SJZJID --数据主键ID
              CWMS   --错误描述
  * 版本日期：2015-4-14
  * 作者：zhujiajie
  * 执行类型：系统调用
******************************************************************/
  PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
  --将错误信息记录到同步错误记录表
  INSERT INTO tb_sjtb_cwjl(CWJLID,  --错误记录ID
                           TBLX  ,  --同步类型
                           TBSJB ,  --同步数据表
                           TBXXID,  --同步信息ID
                           SJZJID,  --数据主键ID
                           CWMS  ,  --错误描述
                           CWJLSJ,  --错误记录时间
                           CWCLSM,  --错误处理说明
                           CWCLSJ,  --错误处理时间
                           CLBZ  )  --处理标志
  SELECT SEQ_SJTB_CWJL_ID.NEXTVAL,  --错误记录ID
         PI_TBLX  ,  --同步类型
         PI_TBSJB ,  --同步数据表
         PI_TBXXID,  --同步信息ID
         PI_SJZJID,  --数据主键ID
         PI_CWMS  ,  --错误描述
         SYSDATE,  --错误记录时间
         NULL,  --错误处理说明
         NULL,  --错误处理时间
         '00'     --处理标志
    FROM dual;
  COMMIT;
EXCEPTION
  WHEN OTHERS THEN
    --错误记录，不做异常记录
    NULL;
end ;

```

### 查看存过的执行时间

```sql
CREATE OR REPLACE PROCEDURE PRO_UPDATE_TB_DRUGSEARCH IS

  BEGIN_TIME VARCHAR2(50);	  	--过程执行开始时间
  END_TIME VARCHAR2(50);		--过程执行结束时间
  XH_TIME VARCHAR2(50);     		--消耗时间
  n_step varchar2(1);

BEGIN
 
  n_step:='1';
  SELECT TO_CHAR(SYSDATE,'yyyy-mm-dd hh24:mi:ss') INTO BEGIN_TIME FROM DUAL;  --执行开始时间
  EXECUTE IMMEDIATE 'truncate table TB_DRUGSERACH';

  n_step:='2';
  INSERT INTO TB_DRUGSERACH(
          YPTBDM,
          YPSPM,
          TYMMC,                               --药品通用名名称
          YPCPM,                               --药品产品名名称
          SCQYMC,                              --生产企业名称
          BSJX,                                --表述剂型
          JXMC,                                --剂型名称
          GGBZ,                                --规格包装
          jylbz,--甲乙类标识
          lsj,        --零售价
          cgj,        --采购价
          cgje,  --采购金额(近3月)
          sfdlcgyp,         --是否大量采购药品 1 是 0 不是
          cgyys ,  --采购医院数(近3月)
          cgyys1,  --采购医院数(近1月)
          zxybz,                                --中西药标志
          gjybz,             --高价药标志
          xzsyz                                --限制适应症
       )
   select
       a.YPTBDM,                              --药品统编代码
       decode(a.YPSPM,'','-',a.YPSPM) YPSPM,  --药品商品名
       a.TYMMC,                               --药品通用名名称
       a.YPCPM,                               --药品产品名名称
       a.SCQYMC,                              --生产企业名称
       a.BSJX,                                --表述剂型
       a.JXMC,                                --剂型名称
       a.GGBZ,                                --规格包装
       (select aaa103 from aa10 where AAA100 = 'JYLBZ' and aaa102 = h.jylbz) jylbz,--甲乙类标识
       cast(b.lsj as number(9,2)) lsj,        --零售价
       cast(e.cgj as number(9,2)) cgj,        --采购价
       cast(d.je/10000 as number(9,2)) cgje,  --采购金额(近3月)
   --     nvl(d.cgyys,0) cgyys,                  --采购医院数(近3月)
   --    nvl(g.cgyys,0) cgyys1,                  --采购医院数(近1月)
       a.sfdlcgyp,         --是否大量采购药品 1 是 0 不是
       nvl(decode(a.sfdlcgyp,'1',f.cgyys3,d.cgyys3),0) cgyys ,  --采购医院数(近3月)
       nvl(decode(a.sfdlcgyp,'1',f.cgyys1,d.cgyys1),0) cgyys1,  --采购医院数(近1月)
       a.zxybz,                                --中西药标志
       substr(a.yptsbz,5,1) gjybz,             --高价药标志
       f.xzsyz                                --限制适应症
  from (select * from (select c.*, decode(d.zxspbm, null, '0', '1') sfdlcgyp
               from v_yppg_fbk c
               left join (select b.zxspbm
                           from tb_dlcgxx a, tb_dlcgmx b
                          where a.dlcgid = b.dlcgid
                            and zt = '20'
                            and yxbz = '1'
                            and bq in ('04', '05')) d
                 on c.yptbdm = d.zxspbm) r
    where  (substr(r.yptsbz, 1, 1) = '1' or substr(r.yptsbz, 9, 1) = '1' or
       sfdlcgyp = '1'))  a
  left outer join (select b.yptbdm, max(gzcsz) as lsj
                     from tb_ypwjgz_bdkmx b inner join tb_ypwjgz_fbk j
                     on b.ypwjbdid = j.YPWJBDID and GZLX = '00' group by b.yptbdm ) b
    on a.YPTBDM = b.yptbdm
  left outer join (select b.yptbdm, max(gzcsz) as cgj
                     from tb_ypwjgz_bdkmx b inner join tb_ypwjgz_fbk j
                     on b.ypwjbdid = j.YPWJBDID and GZLX = '10' inner join tb_ypcggz_fbk g on b.YPTBDM = g.yptbdm and

  g.YPCGLX in('1','5','6') group by b.yptbdm
                       ) e
    on a.YPTBDM = e.yptbdm
  left outer join (select p.yptbdm, sum(pjcgj * cgzsl) je,count(distinct p.yybm) cgyys3,
                        count(case when tjny=to_char(add_months(sysdate,-1),'YYYYMM') then p.yybm else null end) cgyys1
                     from TB_YPCG_YHZ p
                    where p.tjny between to_char(add_months(sysdate,-3),'YYYYMM') and to_char(sysdate,'YYYYMM')
                    group by p.yptbdm) d
    on a.YPTBDM = d.YPTBDM
    left outer join (select yptbdm,count(distinct yybm) cgyys3,
                       count(case when jsny=to_char(add_months(sysdate,-1),'YYYYMM') then yybm else null end) cgyys1
                      from tb_dlcgdzd
                      where jsny between to_char(add_months(sysdate,-3),'YYYYMM') and to_char(sysdate,'YYYYMM')
                    group by yptbdm) f
    on a.yptbdm=f.yptbdm
  left outer join (select f.yptbdm, wm_concat(f.XYTJWZSM) xzsyz from tb_ypybgz_fbk f group by f.yptbdm) f
    on a.YPTBDM = f.YPTBDM
  left outer join tb_ypybgz_fbk h
    on a.YPTBDM = h.YPTBDM;
    
    SELECT TO_CHAR(SYSDATE,'yyyy-mm-dd hh24:mi:ss') INTO END_TIME FROM DUAL;--执行结束时间
    select round(to_number(TO_DATE(END_TIME,'yyyy-mm-dd hh24:mi:ss')-TO_DATE(BEGIN_TIME,'yyyy-mm-dd hh24:mi:ss'))*1440*60)
    INTO XH_TIME FROM DUAL;			--消耗时间 秒
 --insert into MBINMSGS_TEST select * from MBINMSGS;
    n_step:='3';
    DBMS_OUTPUT.PUT_LINE('消耗的时间为：'||xh_time||'秒');
    COMMIT;
  EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('step'||to_char(n_step)||' ,Exception happened,error='||sqlerrm);		--sql内部报错信息
    ROLLBACK;
END PRO_UPDATE_TB_DRUGSEARCH;

```



# 导入导出表

```cmd
C:\Users\Administrator> exp smpaweb/ysww2015@172.31.194.11/orcl file=d:\daochude
tail.dmp tables=(TB_DRUGSEARCHDETAIL)

imp smpaweb/smpaweb@10.1.25.61:1521/ybcp file=e:\daochudetail.dmp full=y

必须先建个空表
imp dp3dev/dp3dev@EXG00T/orcl file=/tmp/dprdev20100419.dmp  tables=(t_calendar) ignore=y	
重要：（如果出现xxx表空间不存在，则临时修改要导入的表空间名）
# alter tablespace TEST  rename to YWDBS;

imp smpaweb/smpaweb@localhost:1521/XE file=e:\smpaweb.dmp
imp wcms/wcms@localhost:1521/XE file=e:\wcms.dmp tables=(CMS_PAGE) ignore=y
imp smpaweb/smpaweb@localhost:1521/XE file=e:\smpa.dmp tables=(UAP_BLOB) ignore=y
imp smpaweb/smpaweb@localhost:1521/XE file=e:\smpa.dmp tables=(UAP_CLOB) ignore=y

imp ybjc/ybjc@192.168.17.237:1521/YBCP file=e:\dmsb_error.dmp full=y；
exp ysreader/ysreader@172.30.196.26/ycnw  file=d:\temp.dmp  tables=(tb_temp_yppzm,temp_invoice_rtj,temp_order_rtj,temp_invoice_rtj_dt,temp_order_rtj_dt)
imp ysxt/ysxt@192.168.17.237:1521/ybcp file=e:\temp.dmp full=y
imp ysreader/ysreader@172.31.196.25:1521/ycww  file=f:\temp.dmp full=y

exp smpaweb/smpaweb@192.168.132.130/orcl file=e:\smpaweb.dmp 
exp wcms/wcms@192.168.132.130/orcl file=e:\wcms.dmp 
exp wcms/wcms@192.168.132.130/orcl file=e:\wcms.dmp tables=(cms_schedule)
imp smpaweb1/smpaweb1@192.168.132.130:1521/orcl  file=e:\smpaweb.dmp  fromuser=smpaweb touser=smpaweb1 ignore=y 
impdp smpaweb1/smpaweb1 dumpfile=smpaweb.dmp directory=e:\ remap_schema=smpaweb:smpaweb1 remap_tablespace=system:SMPA_1_DATA,users:SMPA_1_DATA
imp wcms1/wcms1@192.168.132.130:1521/orcl  file=e:\wcms.dmp full=y 

exp smpaweb1/smpaweb1@192.168.132.130/orcl file=e:\smpaweb_dev.dmp
exp wcms1/wcms1@192.168.132.130/orcl file=e:\wcms_dev.dmp
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

<span style="color:red">最常用：</span>

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

# Job

dbms.job

## job实例

```sql
begin
  sys.dbms_job.submit(job => :job,
                      what => 'PRO_SYNC_DOCUMENT;',
                      next_date => to_date('12-05-2020 05:42:16', 'dd-mm-yyyy hh24:mi:ss'),
                      interval => 'SYSDATE + 1');
  commit;
end;
/
```

## 生产实例dbms_scheduler.create_job

```sql
begin
  /**   每天更新TB_DRUGSERACH表     **/
  sys.dbms_scheduler.create_job(job_name            => 'SMPAWEB.UPDATE_DRUGSEARCH',
                                job_type            => 'STORED_PROCEDURE',
                                job_action          => 'PRO_UPDATE_TB_DRUGSEARCH',
                                start_date          => to_date('2020-05-13 01:00:00', 'yyyy-mm-dd hh24:mi:ss'),
                                repeat_interval     => 'Freq=Daily;Interval=1',
                                end_date            => to_date(null),
                                job_class           => 'DEFAULT_JOB_CLASS',
                                enabled             => true,
                                auto_drop           => false,
                                comments            => '');
end;
/

begin
  /**   每天更新TB_DRUGSERACHDETAIL表     **/
  sys.dbms_scheduler.create_job(job_name            => 'SMPAWEB.UPDATE_DRUGSEARCHDETAIL',
                                job_type            => 'STORED_PROCEDURE',
                                job_action          => 'PRO_UPDATE_TB_DRUGSEARCHDETAIL',
                                start_date          => to_date('2020-05-13 02:00:00', 'yyyy-mm-dd hh24:mi:ss'),
                                repeat_interval     => 'Freq=Daily;Interval=1',
                                end_date            => to_date(null),
                                job_class           => 'DEFAULT_JOB_CLASS',
                                enabled             => true,
                                auto_drop           => false,
                                comments            => '每天更新TB_DRUGSERACHDETAIL表');
end;
/
```

# sequence

## 修改sequence值

```sql
select seq_book_id.nextval from dual;
alter sequence seq_book_id increment by -10	--现在的值减去需要的max值 ，再减1
select seq_book_id.nextval from dual;
alter sequence seq_book_id increment by 1
```

# Index

## 重建index

```sql
ALTER INDEX IDX_PIC_PICURL REBUILD;
ALTER INDEX IDX_PIC_PICURL REBUILD online;

select segment_name ,sum(bytes)/1024/1024 from user_segments where segment_type ='INDEX' group by segment_name;
```

## 查看索引所占空间

```sql
select segment_name ,sum(bytes)/1024/1024 from user_segments where segment_type ='INDEX' group by segment_name;
```

# 用户

## 删除用户及其所有对象

```sql
select * from all_users;	--sys查看
--查找工作空间的路径
select * from dba_data_files; 

--删除表空间
drop tablespace 表空间名称 including contents and datafiles cascade constraint;
例如：删除用户名成为LYK，表空间名称为LYK
--删除用户，及级联关系也删除掉
drop user LYK cascade;
--如果报Oracle ORA-01940 无法删除当前已连接用户:
select  username,sid,serial# from v$session;
alter system kill  session '19,33180';

--删除表空间，及对应的表空间文件也删除掉
drop tablespace LYK including contents and datafiles cascade constraint;
```

# 角色权限

## 查看角色权限

```sql
select * from Dba_Sys_Privs where grantee='DBA';
```

## 查看角色(只能查看登陆用户拥有的角色)所包含的权限

```sql
select * from role_sys_privs;
```

## 查看用户对象权限

```sql
select * from dba_tab_privs;
select * from all_tab_privs;
select * from user_tab_privs;
```

## 查看所有角色

```sql
select * from dba_roles;
```

## 查看哪些用户有sysdba或sysoper系统权限(查询时需要相应权限)

```sql
select * from V$PWFILE_USERS;
```



# 查看数据库

## 查看数据库库对象 

```sql
SELECT owner, object_type, status, COUNT(*) count# 
FROM all_objects 
GROUP BY owner, object_type, status; 
```

## 查看数据库的版本

```sql
SELECT version 
FROM product_component_version 
WHERE substr(product, 1, 6) = 'Oracle'; 
```

### 查看数据库的创建日期和归档方式

```sql
SELECT created, log_mode, log_mode FROM v$database
```

# oracle连接数

## 查询oracle的连接数

```sh
select count(*) from v$session;
```

## 查询oracle的并发连接数

```sql
select count(*) from v$session where status='ACTIVE';
```

## 查看不同用户的连接数

```sql
select username,count(username) from v$session where username is not null group by username;
```

## 修改最大连接数

```sql
alter system set processes = 300 scope = spfile;
--重启数据库:
shutdown immediate;
startup;

--查看当前有哪些用户正在使用数据
SELECT osuser, a.username,cpu_time/executions/1000000||'s', sql_fulltext,machine
from v$session a, v$sqlarea b
where a.sql_address =b.address order by cpu_time/executions desc;


select count(*) from v$session --连接数
select count(*) from v$session where status='ACTIVE'　--并发连接数
show parameter processes --最大连接
select count(*) from v$process --当前的连接数
select value from v$parameter where name = 'processes' --数据库允许的最大连接
alter system set processes = value scope = spfile; --重启数据库 --修改连接
```



# 查找数据库中的topsql语句

## 查找使用资源最多的SQL语句

```sql
--（较高的磁盘读取（disk_reads消耗I/O）和较高的逻辑读取（buffer_gets消耗CPU）被用作衡量标准）

select sql_text from
(select sql_text,executions,buffer_gets,disk_reads
from v$sql
where buffer_gets > 100000
or disk_reads > 100000
order by buffer_gets + 100*disk_reads DESC)
where rownum <= 5;
```

## 查找使用CPU最多的SQL语句

```sql
-- （较高的逻辑读取（buffer_gets消耗CPU）被用作衡量标准）
select sql_text from
(select sql_text,executions,buffer_gets,disk_reads
from v$sql
where buffer_gets > 100000
order by buffer_gets desc)
where rownum <= 5;
或者
（直接使用v$sql里的cpu_time）
select sql_text,
round(cpu_time/1000000, 2) cpu_seconds from
(select * from v$sql order by cpu_time desc)
where rownum <= 5;
```

## 查找使用磁盘I/O最多的SQL语句

```sql
-- （较高的磁盘读取（disk_reads消耗I/O）被用作衡量标准）

select sql_text from
(select sql_text,executions,buffer_gets,disk_reads
from v$sql
where disk_reads > 100000
order by disk_reads desc)
where rownum <= 5;
```

## 查找占用数据库时间最多的SQL语句

```sql
select sql_text,round(elapsed_time/1000000, 2) elapsed_seconds,executions from
(select * from v$sql order by elapsed_time desc)
where rownum <= 5;
```

## 查找执行次数（executions）最多的SQL语句

```sh
select sql_text, executions from
(select * from v$sql
where executions > 1000
order by executions desc)
where rownum <= 5;
```

## 查找解析调用最多的SQL语句

```sql
select sql_text,parse_calls from
(select * from v$sql
where parse_calls > 1000
order by parse_calls desc)
where rownum <= 5;
```

## 查找使用共享内存最多的SQL语句

```sql
-- （使用共享内存大于1048576（bytes）的SQL语句会显示）
select sql_text,sharable_mem from
(select * from v$sql
where sharable_mem > 1048576
order by sharable_mem desc)
where rownum <= 5;
```



# 表空间

## 查看表空间的名称及大小

```sql
SELECT t.tablespace_name, round(SUM(bytes / (1024 * 1024)), 0) ts_size 
FROM dba_tablespaces t, dba_data_files d 
WHERE t.tablespace_name = d.tablespace_name 
GROUP BY t.tablespace_name; 
```

## 查看表空间物理文件的名称及大小 

```sql
SELECT tablespace_name, 
file_id, 
file_name, 
round(bytes / (1024 * 1024), 0) total_space 
FROM dba_data_files 
ORDER BY tablespace_name; 
```

## 查看表空间的使用情况

```sh
SELECT SUM(bytes) / (1024 * 1024) AS free_space, tablespace_name 
FROM dba_free_space 
GROUP BY tablespace_name; 
SELECT a.tablespace_name, 
a.bytes total, 
b.bytes used, 
c.bytes free, 
(b.bytes * 100) / a.bytes "% USED ", 
(c.bytes * 100) / a.bytes "% FREE " 
FROM sys.sm$ts_avail a, sys.sm$ts_used b, sys.sm$ts_free c 
WHERE a.tablespace_name = b.tablespace_name 
AND a.tablespace_name = c.tablespace_name; 

```

## 查看表空间物理位置

```sql
select tablespace_name, file_id,file_name, round(bytes/(1024*1024),0)||'M' total_space from dba_data_files order by tablespace_name;
```



## <font color="red">创建表空间</font>

```sql
from: https://blog.csdn.net/u010035907/article/details/50299453

-----------------------(重要) 案例一：创建表空间及用户 -----------------
/*第1步：创建临时表空间 */   
create temporary tablespace IVMS86X0_TEMP    ----测试成功！  注记：表空间名字不能重复，即便存储的位置不一致, 但是dbf文件可以一致
tempfile 'I:\oracle\oradata\oracle11g\IVMS86X0_TEMP.dbf' 
size 50m   ---50m为表空间的大小，对大数据量建议用20G，甚至32G
autoextend on 
next 50m maxsize 20480m 
extent management local;
 
/*第2步：创建数据表空间 */
create tablespace IVMS86X0_DATA    ----测试成功！
logging 
datafile 'I:\oracle\oradata\oracle11g\IVMS86X0_DATA.dbf' 
size 50m 
autoextend on 
next 50m maxsize 20480m 
extent management local;
/*第3步：创建用户并指定表空间 */
create user IVMS86X0_WJ identified by IVMS86X0_WJ  ---测试成功！
default tablespace IVMS86X0_DATA 
temporary tablespace IVMS86X0_TEMP;
/*第4步：给用户授予权限 */   
--grant connect,resource to IVMS86X0_WJ;    ---测试成功！
grant connect,resource,dba to IVMS86X0_WJ;  -----创建权限后，才可以连接  测试成功！


/*删除表空间与用户*/
drop user IVMS86X0_WJ cascade;  ---删除用户的所有对象在删除用户   测试成功！
--drop tablespace yuanmin_data including contents;
--drop tablespace yuanmin_temp including contents;

drop tablespace IVMS86X0_DATA including contents and datafiles    -----删除表空间，包括文件  测试成功！
drop tablespace IVMS86X0_TEMP including contents and datafiles    -----删除表空间，包括文件  测试成功！
-----删除用户表空间与删除用户，没有先后之分 测试成功！
----经验总结！！！
----存在一种情况
--在执行 drop tablespace IVMS86X0_DATA including contents and datafiles时，有用户在使用，导致drop user IVMS86X0_WJ cascade 无法
--执行,而且此时对应的表空间文件也无法删除，此时断开IVMS86X0_WJ用户，便可手动删除表空间文件
 


----  案例二：为表索引指定表空间
--step0: 创建表空间 P201507
create tablespace P201507    ----测试成功！
logging 
datafile 'I:\oracle\oradata\oracle11g\P201507.dbf' 
size 50m 
autoextend on 
next 50m maxsize 20480m 
extent management local;
--step2：创建索引，并指定表空间
CREATE INDEX idx_vehiclepass_com3 
ON traffic_vehicle_pass(plate_no, pass_time, crossing_id) TABLESPACE P201507;  --为索引指定表空间
 
 
----- 案例三：oracle表空间（数据文件）满了后，修改表空间的大小
--1、扩展表空间
alter database datafile 'D:\ORACLE\PRODUCT\ORADATA\TEST\USERS01.DBF' resize 50m;
--2、自动增长
alter database datafile 'D:\ORACLE\PRODUCT\ORADATA\TEST\USERS01.DBF' autoextend on next 50m maxsize 500m;
--3、增加数据文件
alter tablespace yourtablespacename add datafile 'd:\newtablespacefile.dbf' size 5m;


```

## 删除表空间

```sql
from: https://www.cnblogs.com/Alanf/p/9485550.html

--删除空的表空间，但是不包含物理文件
drop tablespace tablespace_name;
--删除非空表空间，但是不包含物理文件
drop tablespace tablespace_name including contents;
--删除空表空间，包含物理文件
drop tablespace tablespace_name including datafiles;
--删除非空表空间，包含物理文件
drop tablespace tablespace_name including contents and datafiles;
--如果其他表空间中的表有外键等约束关联到了本表空间中的表的字段，就要加上CASCADE CONSTRAINTS
drop tablespace tablespace_name including contents and datafiles CASCADE CONSTRAINTS;
  


以system用户登录，查找需要删除的用户：
--查找用户
select *　from dba_users;
--查找工作空间的路径
select * from dba_data_files;
--删除用户
drop user 用户名称 cascade;
--删除表空间
drop tablespace 表空间名称 including contents and datafiles cascade constraint;
例如：删除用户名成为ABC，表空间名称为ABC
--删除用户，及级联关系也删除掉
drop user ABC cascade;
--删除表空间，及对应的表空间文件也删除掉
drop tablespace ABC including contents and datafiles cascade constraint;
 
-- 删除无任何数据对象的表空间：
-- 首先使用PL/SQL界面化工具，或者使用oracle自带的SQL PLUS工具，连接需要删除的表空间的oracle数据局库。
-- 确认当前用户是否有删除表空间的权限，如果没有 drop tablespace,请先用更高级的用户(如sys)给予授权或者直接用更高级的用户。
-- 用drop tablespace xxx ，删除需要删除的表空间。
-- 删除有任何数据对象的表空间
-- 使用drop tablespace xxx including contents and datafiles;来删除表空间。
-- 注意事项：
-- 如果drop tablespace语句中含有datafiles,那datafiles之前必须有contents关键字,不然会提示ora-01911错误
-- 备注：随笔中内容来源于网上资料整理，仅供参考。
```



## system表空间满

```sql
--查看表空间使用情况，及物理位置
select
b.file_name 物理文件名,
b.tablespace_name 表空间,
b.bytes/1024/1024 大小M,
(b.bytes-sum(nvl(a.bytes,0)))/1024/1024 已使用M,
substr((b.bytes-sum(nvl(a.bytes,0)))/(b.bytes)*100,1,5) 利用率
from dba_free_space a,dba_data_files b
where a.file_id=b.file_id
group by b.tablespace_name,b.file_name,b.bytes
order by b.tablespace_name ;

--查看用户使用的表空间
select username,default_tablespace from dba_users;

--查看最占空间的表
select t.segment_name, t.segment_type, sum(t.bytes / 1024 / 1024)||'MB' "占用空间(M)" ,OWNER 
from dba_segments t
where t.segment_type='TABLE'
group by OWNER, t.segment_name, t.segment_type, t.bytes  order by t.bytes desc ;

--SYSTEM表中最占空间的10个表

SELECT *
FROM (SELECT BYTES/1024/1024||'MB', segment_name, segment_type, owner
FROM dba_segments
WHERE tablespace_name = 'SYSTEM'
ORDER BY BYTES DESC)
WHERE ROWNUM < 10;

--修改表空间大小
alter database datafile 'E:\APP\ADMINISTRATOR\ORADATA\ORCL\SYSTEM01.DBF' autoextend on;  
alter database datafile 'E:\APP\ADMINISTRATOR\ORADATA\ORCL\SYSTEM01.DBF' resize 1024M;
```

## <font color='red'>改变表的表空间</font>

```sql
SELECT 'alter table '||TABLE_NAME||' move tablespace SMPA_1_DATA;' FROM USER_TABLES WHERE TABLESPACE_NAME = 'USERS';


alter table PLAN_TABLE move tablespace SMPA_1_DATA;
alter table MV_YPYBGZ_FBK move tablespace SMPA_1_DATA;
alter table MV_YPPG_FBK move tablespace SMPA_1_DATA;
alter table AA11 move tablespace SMPA_1_DATA;
alter table AA10 move tablespace SMPA_1_DATA;
alter table TB_ZDMB move tablespace SMPA_1_DATA;
alter table TB_YBYPTJLOG move tablespace SMPA_1_DATA;
alter table TB_YBYP move tablespace SMPA_1_DATA;
alter table TB_YBGZ_SYGZ_BDK move tablespace SMPA_1_DATA;
alter table TB_DRUGSERACH move tablespace SMPA_1_DATA;
alter table TB_DRUGSEARCHDETAIL move tablespace SMPA_1_DATA;
```



# 日志文件

## 查看回滚段名称及大小

```sql
SELECT segment_name, 
tablespace_name, 
r.status, 
(initial_extent / 1024) initialextent, 
(next_extent / 1024) nextextent, 
max_extents, 
v.curext curextent 
FROM dba_rollback_segs r, v$rollstat v 
WHERE r.segment_id = v.usn(+) 
ORDER BY segment_name; 
```

## 查看控制文件

```sql
SELECT NAME FROM v$controlfile; 
```

## 查看日志文件

```sql
SELECT MEMBER FROM v$logfile
```



# Table

## 表实际占用空间

```sql
select a.table_name,  num_rows * avg_row_len /1024/1024 实际占用M from user_tables a
```

## 表分配的空间

```sql
   Select Segment_Name,Sum(bytes)/1024/1024 分配的空间M From User_Extents Group By Segment_Name
```

## 改表名

```sql
ALTER TABLE table_name RENAME TO new_table_name；
-- 另一种是直接使用RENAME语句，语法如下：
RENAME table_name TO new_table_name;
```

## 删除表时释放空间

```sql
drop table <table_name> purge;   -- 若要彻底删除表，则使用语句
  清除回收站里的信息
purge table <table_name>;	-- 清除指定表
purge recyclebin;	-- 清除当前用户的回收站
purge dba_recyclebin;	-- 清除所有用户的回收站
drop table xx purge;	-- 不放入回收站，直接删除则是
```

## 一次插入多条数据 

```sql
insert all 
 into jack_20170206_aa values('4014033')
 into jack_20170206_aa values('4065304')
 into jack_20170206_aa values('4088136')
 into jack_20170206_aa values('4092405')
select 1 from dual;
```

# 内置函数

## wm_concat

```sql
select wm_concat(categoryname) from tb_category where categoryid in(1,2,3)	-- 合并字符串
```

## nvl

```sql
SELECT           	   T.D_FDATE,
                       T.VC_ZHCODE,
                       NVL(SUM(T.F_FZQSZ), 0) f_price_b,		--sum为null的话，则取0代替
                       NVL(SUM(T.F_FZQCB), 0) f_cost_b,
                       NVL(SUM(T.F_FGZ_ZZ), 0) f_gz_b,
                       NVL(SUM(T.F_FYZQSZ), 0) f_price_Y,
                       NVL(SUM(T.F_FYZQCB), 0) f_cost_Y,
                       NVL(SUM(T.F_FYGZ_ZZ), 0) f_gz_Y,
                       T.VC_SOURCE,
                       SYSDATE d_updatetime
                  FROM GZ_FUND_GZB T
```

# 物化视图

## 每日更新

```sql
create materialized view TB_DRUGSEARCHDETAIL
refresh complete on demand
start with to_date('09-05-2020 01:33:29', 'dd-mm-yyyy hh24:mi:ss') next SYSDATE + 1 
as
select * from v_drugsearchdetail;

```

## 生成物化视图的耗时

```sql
SELECT
mview_name,
last_refresh_date "START_TIME",			--开始时间
CASE
WHEN fullrefreshtim <> 0 THEN
LAST_REFRESH_DATE + fullrefreshtim/60/60/24
WHEN increfreshtim <> 0 THEN
LAST_REFRESH_DATE + increfreshtim/60/60/24
ELSE
LAST_REFRESH_DATE						--结束时间
END "END_TIME",
fullrefreshtim,			--完全刷新（Complete）时间
increfreshtim			--可快速刷新（Fast）的时间
FROM all_mview_analysis where mview_name='TB_DRUGSERACH';
```

# 游标

## 显示游标

### 例1

```sql
declare cursor cu_emp is select  empno, ename, sal from emp;

e_no number;			--或  e_no emp.empno%type	;
e_name varchar2(20);
e_sal number;

begin
	open cu_emp;
	fetch cu_emp into e_no,e_name,e_sal ;		--从游标中取数据

	while cu_emp%found loop						--循环
		dbms_output.put_line('编号：'||e_no||' '||'姓名：'||e_name||' 薪水：'||e_sal);
		fetch cu_emp into e_no,e_name,e_sal ;		--再取一次
	end loop;
	close cu_emp;
end;
```

### 例2(游标指定行)

```sql
declare cursor cu_emp is select * from emp where sal>=2000 and sal<=3000;
e emp%rowtype;			--指定行
begin
	open cu_emp;		
	fetch cu_emp into e ;

	while cu_emp%found loop
		dbms_output.put_line('编号：'||e.empno||' '||'姓名：'||e.ename||' 薪水：'||e.sal);
		fetch cu_emp into e ;
	end loop;
	close cu_emp;
end;
```

## 隐式游标

### sql%found

```sql
SQL%FOUND和SQL%NOTFOUND在执行任何DML语句前SQL%FOUND和SQL%NOTFOUND的值都是NULL
```

```sql
--TRUE :INSERT

--TRUE :DELETE和UPDATE，至少有一行被DELETE或UPDATE.
set serverout on;
begin
  update emp set ename='sb3' where empno=123;
  if sql%found then
    dbms_output.put_line('更新成功');
  else
    dbms_output.put_line('更新未成功');
  end if;
  commit;
end;
--TRUE :SELECT INTO至少返回一行
```

### sql%notfound

### sql%isopen

```sql
-- 对于隐式游标而言SQL%ISOPEN总是FALSE，这是因为隐式游标在DML语句执行时打开，结束时就立即关闭
set serverout on;

begin
  if sql%isopen then
    dbms_output.put_line('sql游标已打开');
  else
    dbms_output.put_line('sql游标未打开');
  end if;
end;
```

### sql%rowcount

DML语句成功执行的行数

```sql
--例1
set serverout on;
declare e_count number;
begin
  select count(1) into e_count from t_book;		--DML语句，必须有into
  dbms_output.put_line('执行的行数是：'||sql%rowcount);
end;

-- 例2
set serverout on;

begin
  update emp set ename='sb3' where empno=123;
  if sql%rowcount = 1 then
    dbms_output.put_line('更新成功');
  else
    dbms_output.put_line('更新未成功');
  end if;
end;
```

## 动态游标

### 强类型

```sql
declare type emptype is ref cursor return emp%rowtype;
cu_emp emptype;
e_count number;
e emp%rowtype;
begin
  select count(*) into e_count from emp where job='PRESIDENT';

  if e_count=0 then
    open cu_emp for select * from emp;
  else
    open cu_emp for select * from emp where job='PRESIDENT';
  end if;

  fetch cu_emp into e;
    while cu_emp%found loop
    dbms_output.put_line('编号：'||e.empno||' '||'姓名：'||e.ename||' 薪水：'||e.sal);
  fetch cu_emp into e ;
  end loop;
  close cu_emp;
end;
```

### <font color='red'>弱类型</font>

可以指定任意表的合集

```sql
declare type customType is ref cursor;     --等于是一个泛型的ResultSet
e_count number;
e emp%rowtype;      --emp表的rowtype类型
s salgrade%rowtype;
cType customType;
begin
    select count(*) into e_count from emp where job='PRESIDENT';
    if e_count=0 then 
      open cType for select * from salgrade;
      fetch cType into s;
      while cType%found loop
        DBMS_OUTPUT.PUT_LINE('等级：'||s.grade||',最低薪资：'||s.losal);
        fetch cType into s;
      loop;
      close cType;
    else
      open cType for select * from emp where job='PRESIDENT';
      fetch cType into e;
      while cType%found loop
        DBMS_OUTPUT.PUT_LINE('编号：'||e.empno||',姓名：'||e.name);
      loop;
    end if;
end;
```

## <font color='red'>正式环境实例</font>

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

# DML, DDL, DCL

## DML

```sql
DML（Data Manipulation Language，数据操作语言）：用于检索或者修改数据。
  	DML包括： SELECT：用于检索数据；
    INSERT：用于增加数据到数据库；
    UPDATE：用于从数据库中修改现存的数据 
    DELETE：用于从数据库中删除数据
```



## DDL

```sql
  DDL（Data Definition Language，数据定义语言）： 用于定义数据的结构，比如 创建、修改或者删除数据库对象。
  DDL包括：DDL语句可以用于创建用户和重建数据库对象。下面是DDL命令：
    CREATE TABLE：# 创建表
    ALTER TABLE
    DROP TABLE：	 # 删除表
    CREATE INDEX
    DROP INDEX
```

## DCL

```sql
 DCL（Data Control Language，数据控制语言）：用于定义数据库用户的权限。
  DCL包括：
    ALTER PASSWORD 
    GRANT 
    REVOKE 
    CREATE SYNONYM
```

# 报错

## ORA-00911

sql未尾;

## ORA-01119

```bash
ORA-01119: error in creating database file '/u01/app/datafiles/gaodata1.dbf'                                
ORA-27040: file create error, unable to create file                                
Linux-x86_64 Error: 2: No such file or directory   

这是因为，目录尚未建立：
```

## ORA-01940 

提示 “无法删除当前已连接用户”

```sql
Oracle删除用户的提示无法删除当前已连接用户两种解决方法如下： 1、先锁定用户、然后查询进程号，最后删除对应的进程、在删除对应的用户 SQLalter user XXX account lock; SQLSELECT * FROM V$SESSION WHERE USERNAME='LGDB'；
Oracle删除用户的提示无法删除当前已连接用户两种解决方法如下：
1、先锁定用户、然后查询进程号，最后删除对应的进程、在删除对应的用户
SQL>alter user XXX account lock;
SQL>SELECT * FROM V$SESSION WHERE USERNAME='LGDB'；
SQL>alter system kill session 'xx,xx'
SQL>drop user xx cascade
2.shutdown掉数据库，再重启
具体查询进程号，最后删除对应的进程步骤如下
1) 查看用户的连接状况
select username,sid,serial# from v$session
如下结果：
username         sid        serial#
NETBNEW         513         22974
NETBNEW         514         18183
NETBNEW         516         21573
NETBNEW         531         9
ts             532         4562
(2)找到要删除用户的sid,和serial，并删除
如：你要删除用户'ts',可以这样做：
alter system kill session'532,4562'
(3)删除用户
drop user ts cascade
(**)如果在drop 后还提示ORA-01940:无法删除当前已链接的用户，说明还有连接的session，可以通过查看session的状态来确定该session是否被kill 了，用如下语句查看：
select saddr,sid,serial#,paddr,username,status from v$session where username is not null
结果如下(以我的库为例)：
saddr         sid       serial#       paddr       username       status
564A1E28       513       22974       569638F4       NETBNEW       ACTIVE
564A30DC       514       18183       569688CC       NETBNEW       INACTIVE
564A5644       516       21573       56963340       NETBNEW       INACTIVE
564B6ED0       531       9         56962D8C       NETBNEW       INACTIVE
564B8184       532       4562       56A1075C       WUZHQ       KILLED
status 为要删除用户的session状态，如果还为inactive，说明没有被kill掉，如果状态为killed，说明已kill。
由此可见，ts这个用户的session已经被杀死。此时可以安全删除用户。
```

# 数据恢复

## <font color='red'>误delete</font>

```sql
--7天内数据快照
SELECT * FROM TABLE_NAME AS OF TIMESTAMP to_timestamp('2013-08-07 14:45:00', 'yyyy-mm-dd hh24:mi:ss');
```

误drop

```sql
select * from user_recyclebin;		--查看回收站

flashback table A to before drop;		--恢复误删除的表
```

```sql
from: https://www.cnblogs.com/xiaoyu1994/p/8304969.html

--查看回收站中表
select object_name,original_name,partition_name,type,ts_name,createtime,droptime from recyclebin;
--恢复表
SQL>flashback table test_drop to before drop;或
SQL>flashback table "BIN$b+XkkO1RS5K10uKo9BfmuA==$0" to before drop;
注：必须9i或10g以上版本支持，flashback无法恢复全文索引
以下为参考资料
 使用 Oracle Database 10g 中的闪回表特性，可以毫不费力地恢复被意外删除的表
以下是一个不该发生却经常发生的情况：用户删除了一个非常重要的表 ― 当然是意外地删除 ― 并需要尽快地恢复。（在某些时候，这个不幸的用户可能就是 DBA！）
Oracle9i Database 推出了闪回查询选项的概念，以便检索过去某个时间点的数据，但它不能闪回 DDL 操作，如删除表的操作。唯一的恢复方法是在另一个数据库中使用表空间的时间点恢复，然后使用导出/导入或其他方法，在当前数据库中重新创建表。这一过程需要 DBA 进行大量工作并且耗费宝贵的时间，更不用说还要使用另一个数据库进行克隆。
请使用 Oracle Database 10g 中的闪回表特性，它使得被删除表的恢复过程如同执行几条语句一样简单。让我们来看该特性是如何工作的。
删除那个表！
首先，让我们查看当前模式中的表。
SQL> select * from tab;
TNAME
TABTYPE
CLUSTERID
---------------------------------------
RECYCLETEST
TABLE
现在，我们意外地删除了该表：
SQL> drop table recycletest;
Table dropped.
现在让我们来查看该表的状态。
SQL> select * from tab;
TNAME
TABTYPE
CLUSTERID
--------------------------- - -- -- --- ------
BIN$04LhcpndanfgMAAAAAANPw==$0 TABLE
表 RECYCLETEST 已不存在，但是请注意出现新表 BIN$04LhcpndanfgMAAAAAANPw==$0。这就是所发生的事情：被删除的表 RECYCLETEST 并没有完全消失，而是重命名为一个由系统定义的名称。它存在于同一个表空间中，具有与原始表相同的结构。如果在该表上定义了索引或触发器，则它们也被重命名，使用与表相同的命名规则。任何相关源（如过程）都失效；原始表的触发器和索引被改为放置在重命名的表 BIN$04LhcpndanfgMAAAAAANPw==$0 上，保持被删除表的完整对象结构。
表及其相关对象被放置在一个称为"回收站"的逻辑容器中，它类似于您 PC 机中的回收站。但是，对象并没有从它们原先所在的表空间中删除；它们仍然占用那里的空间。回收站只是一个列出被删除对象目录的逻辑结构。在 SQL*Plus 提示符处使用以下命令来查看其内容（您需要使用 SQL*Plus 10.1 来进行此操作）：
SQL> show recyclebin
ORIGINAL NAME
RECYCLEBIN NAME
OBJECT TYPE
DROP TIME
------------- - -- ----------------------- - -- ----- - -- --------------
RECYCLETEST
BIN$04LhcpndanfgMAAAAAANPw==$0 TABLE
2004-02-16:21:13:31
结果显示了表的原始名称 RECYCLETEST，并显示了回收站中的新名称，该名称与我们看到的删除后所创建的新表名称相同。（注意：确切的名称可能因平台不同而不同。）为恢复该表，您所需要做的就是使用 FLASHBACK TABLE 命令：
SQL> FLASHBACK TABLE RECYCLETEST TO BEFORE DROP;
FLASHBACK COMPLETE.
SQL> SELECT * FROM TAB;
TNAME
TABTYPE
CLUSTERID
--------------------------- - -- -- --- ------
RECYCLETEST
TABLE
瞧！表毫不费力地恢复了。如果现在查看回收站，它将是空的。
记住，将表放在回收站里并不在原始表空间中释放空间。要释放空间，您需要使用以下命令清空回收站：
PURGE RECYCLEBIN;
但是如果您希望完全删除该表而不需要使用闪回特性，该怎么办？在这种情况下，可以使用以下命令永久删除该表：
DROP TABLE RECYCLETEST PURGE;
此命令不会将表重命名为回收站中的名称，而是永久删除该表，就象 10g 之前的版本一样。
管理回收站
如果在该过程中没有实际删除表 ― 因而没有释放表空间 ― 那么当被删除的对象占用了所有空间时，会发生什么事？
答案很简单：这种情况根本不会出现。当表空间被回收站数据完全占满，以至于必须扩展数据文件来容纳更多数据时，可以说表空间处于"空间压力"情况下。此时，对象以先进先出的方式从回收站中自动清除。在删除表之前，相关对象（如索引）被删除。
同样，空间压力可能由特定表空间定义的用户限额而引起。表空间可能有足够的空余空间，但用户可能将其在该表空间中所分配的部分用完了。在这种情况下，Oracle 自动清除该表空间中属于该用户的对象。
此外，有几种方法可以手动控制回收站。如果在删除名为 TEST 的特定表之后需要从回收站中清除它，可以执行
PURGE TABLE TEST;
或者使用其回收站中的名称：
PURGE TABLE "BIN$04LhcpndanfgMAAAAAANPw==$0";
此命令将从回收站中删除表 TEST 及所有相关对象，如索引、约束等，从而节省了空间。但是，如果要从回收站中永久删除索引，则可以使用以下命令来完成工作：
purge index in_test1_01;
此命令将仅仅删除索引，而将表的拷贝留在回收站中。
有时在更高级别上进行清除可能会有用。例如，您可能希望清除表空间 USERS 的回收站中的所有对象。可以执行：
PURGE TABLESPACE USERS;
您也许希望只为该表空间中特定用户清空回收站。在数据仓库类型的环境中，用户创建和删除许多临时表，此时这种方法可能会有用。您可以更改上述命令，限定只清除特定的用户：
PURGE TABLESPACE USERS USER SCOTT;
诸如 SCOTT 等用户可以使用以下命令来清空自己的回收站
PURGE RECYCLEBIN;
DBA 可以使用以下命令清除任何表空间中的所有对象
PURGE DBA_RECYCLEBIN;
可以看到，可以通过多种不同方法来管理回收站，以满足特定的需要。
表版本和闪回功能
用户可能会经常多次创建和删除同一个表，如：
CREATE TABLE TEST (COL1 NUMBER);
INSERT INTO TEST VALUES (1);
commit;
DROP TABLE TEST;
CREATE TABLE TEST (COL1 NUMBER);
INSERT INTO TEST VALUES (2);
commit;
DROP TABLE TEST;
CREATE TABLE TEST (COL1 NUMBER);
INSERT INTO TEST VALUES (3);
commit;
DROP TABLE TEST;
此时，如果您要对表 TEST 执行闪回操作，那么列 COL1 的值应该是什么？常规想法可能认为从回收站取回表的第一个版本，列 COL1 的值是 1。实际上，取回的是表的第三个版本，而不是第一个。因此列 COL1 的值为 3，而不是 1。
此时您还可以取回被删除表的其他版本。但是，表 TEST 的存在不允许出现这种情况。您有两种选择：
使用重命名选项：
FLASHBACK TABLE TEST TO BEFORE DROP RENAME TO TEST2;
FLASHBACK TABLE TEST TO BEFORE DROP RENAME TO TEST1;
这些语句将表的第一个版本恢复到 TEST1，将第二个版本恢复到 TEST2。 TEST1 和 TEST2 中的列 COL1 的值将分别是 1 和 2。或者，
使用表的特定回收站名称进行恢复。为此，首先要识别表的回收站名称，然后执行：
FLASHBACK TABLE "BIN$04LhcpnoanfgMAAAAAANPw==$0" TO BEFORE DROP RENAME TO TEST2;
FLASHBACK TABLE "BIN$04LhcpnqanfgMAAAAAANPw==$0" TO BEFORE DROP RENAME TO TEST1;
这些语句将恢复被删除表的两个版本。
警告......
取消删除特性使表恢复其原始名称，但是索引和触发器等相关对象并没有恢复原始名称，它们仍然使用回收站的名称。在表上定义的源（如视图和过程）没有重新编译，仍然保持无效状态。必须手动得到这些原有名称并应用到闪回表。
信息保留在名为 USER_RECYCLEBIN 的视图中。在对表进行闪回操作前，请使用以下查询来检索原有名称。
SELECT OBJECT_NAME, ORIGINAL_NAME, TYPE
FROM USER_RECYCLEBIN
WHERE BASE_OBJECT = (SELECT BASE_OBJECT FROM USER_RECYCLEBIN
WHERE ORIGINAL_NAME = 'RECYCLETEST')
AND ORIGINAL_NAME != 'RECYCLETEST';
OBJECT_NAME
ORIGINAL_N TYPE
--------------------------- - -- --- - -- ----
BIN$04LhcpnianfgMAAAAAANPw==$0 IN_RT_01
INDEX
BIN$04LhcpnganfgMAAAAAANPw==$0 TR_RT
TRIGGER
在表进行闪回操作后，表 RECYCLETEST 上的索引和触发器将按照 OBJECT_NAME 列中所示进行命名。根据以上查询，可以使用原始名称重新命名对象，如下所示：
ALTER INDEX "BIN$04LhcpnianfgMAAAAAANPw==$0" RENAME TO IN_RT_01;
ALTER TRIGGER "BIN$04LhcpnganfgMAAAAAANPw==$0" RENAME TO TR_RT;
一个值得注意的例外情况是位图索引。当删除位图索引时，它们并不放置在回收站中 ― 因此无法检索它们。约束名称也无法从视图中检索。必须从其他来源对它们进行重命名。
闪回表的其他用途
闪回删除表功能不仅限于恢复表的删除操作。与闪回查询类似，您还可以使用它将表恢复到不同的时间点，形如flashback table tmm2076 TO TIMESTAMP to_timestamp('2007-05-22
12:00:00','yyyy-mm-dd hh24:mi:ss')
弹出ORA-08189错误，需要执行以下命令先：
alter table tmm2076 enable row movement这个命令的作用是，允许oracle修改分配给行的rowid。
然后再flashback，数据被恢复完毕。 
```

# win上将oracle

```sql
from: https://blog.csdn.net/wangzhaopeng0316/article/details/8480451

解决Oracle数据库客户端随机端口连接的问题

要使Oracle客户端能正常连接到设置有防火墙的安装在windows上的Oracle服务器，单开放一个1521或自定义的监听端口是不够的。

我们有的时候需要映射端口远程去访问Oracle 数据库，这里有个防火墙的问题，在unix 上没有问题，但是在win 平台上却无法正确访问,下面的可以解决这个问题,从网上找到有如下资料：

资料一、

近来由于工作需要，在Windows XP平台上安装了Oracle9i数据库作为测试之用,一切正常。但当客户机连接服务器时却总是超时，我首先想到了防火墙，当我打开1521端口时，连接操作仍然失败。我又怀疑网络有问题，用telnet server_ip:1521尝试，连接被接受，说明1521端口已经被打开。

没有办法，查询Oracle资料后才明白，network listener 只起一个中介作用，当客户连接它时，它根据配置寻找到相应的数据库实例进程，然后spawned一个新的数据库连接，这个连接端口由network listener传递给客户机，此后客户机就不再和打交道了，即使listener停止了工作。这个新的连接端口是不可预知的，因而会被防火墙阻止。

Windows Socket2 规范有一个新的特性，就是Shared Socket， 所谓共享套接字是指一个进程共享另一个进程的套接字(详见MSDN相关参考)。如果让network listener与数据库服务进程共享套接字，那么连接端口就不会变化。

如何设置Shared Socket?

在 注册表：HKEY_LOCAL_MACHINE\SOFTWARE\ORACLE\HOME0上新建一个字符串 值：USE_SHARED_SOCKET=true。如果安装了多个目录，则每个类似的目录都要设 置：HKEY_LOCAL_MACHINE\SOFTWARE\ORACLE\HOMEx (x目录编号）

设置后要求重新启动实例（只重启listener发现没有效果）

资料二、

Oracle客户端连接服务器，首先去找1521监听端口，服务器的1521监听端口再向server process进程发出请求，并返回一个随机端口，返回给客户端，客户端再来连接这个端口。 这样就给服务器上的防火墙设置带来了麻烦，这个端口是随机的，如何开放?

windows 平台上的这个问题成了一大难题，很多论坛都 有人问，但很少有人能解决。 unix平台不用担心，系统自动会解决这个问题. Matalink上提供了三种解决办法，实际上USE_SHARED_SOCKET 是最有效最方便的。但经过无数次实现，仍然没有成功，最后终于发现是Oracle 8.1.7的bug 需要打补丁，升级到Oracle 8.1.7.1.2

需要在MTS模式下（共享模式） Oracle默认是专用模式。

经 试验发现，如果不在init文件中设参数的话，Oracle仍然会要求一个随机端口和1521端口来共同通讯，只是这个随机端口，并不随客户端会话和登录 的变化而变化，在没有重启服务器时，是固定的。 （试验发现，在专用模式下，每次连接，oracle服务器会按+1方式，提供一个非1521的端口。） 所以，还需要在init.ora文件的最后加上一条参数：
mts_dispatchers="(address=(protocol=tcp)(host=myoradb)(port=1521))(dispatchers=1)"
设置后要求重新启动实例（只重启listener发现没有效果）
这样才真正实现只用一个端口，穿过防火墙。
```

