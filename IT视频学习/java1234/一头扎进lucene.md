# 第1讲 HelloWorld
```sh
#jar包:
    lucene-core
    lucene-queryparser
    lucene-analysers-common
```

# 第2讲 构建索引
## 第1节 添加文档

## 第2节 删除文档
testDeleteBeforeMerge   //在合并前
```java
	writer.deleteDocuments(new Term("id","1"));
	writer.commit();
```
testDeleteAfterMerge   //在合并后
```java
	writer.deleteDocuments(new Term("id","1"));
	writer.forceMergeDeletes(); 	//强制删除
	writer.commit();

```
## 第3节 修改文档
```java
IndexWriter writer=getWriter();
Document doc=new Document();
doc.add(new StringField("id","1",Field.Store.YES));
doc.add(new StringField("city","qingdao",Field.Store.YES));
doc.add(new TextField("desc","dsss is a city",Field.Store.NO));
writer.updateDocument(new Term("id","1"), doc);
writer.close();
```
# 第3讲
## 第4节 文档域加权
```java
filed.setBoost(1.5);
```

# 第4讲 搜索功能
## 第1节 对特定项搜索

## 第2节 查询表达式 QueryParser

```java
String q="java AND request" //并搜索
String q="particula~"   //模糊查询
```

### 第3节 分页实现 

### 第4节 其他方式查询
TermRangeQuery      中文不用
NumericRangeQuery   //指定数字范围
PerfixQuery     //字符串开头搜索
BooleanQuery    //组合查询

```java
BooleanQuery.Builder booleanQuery=new BooleanQuery.Builder();
booleanQuery.add(query1.BooleanClause.Occur.MUST);
TopDocs hits=is.search(booleanQuery.builder(),10);
```

# 第6讲 
中文分词和高亮显示
## 第1节 中文分词 smartcn
```java
SmartChineseAnalyzer parser=new SmartChineseAnalyzer();
```
StringField不进行分词

## 第2节 结果高亮显示
lucene-highlighter
```java
QueryScorer scorer=new QueryScorer(query);
Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);
SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);
highlighter.setTextFragmenter(fragmenter);
for(ScoreDoc scoreDoc:hits.scoreDocs){
    Document doc=is.doc(scoreDoc.doc);
    System.out.println(doc.get("city"));
    System.out.println(doc.get("desc"));
    String desc=doc.get("desc");
    if(desc!=null){
        TokenStream tokenStream=analyzer.tokenStream("desc", new StringReader(desc));
        System.out.println(highlighter.getBestFragment(tokenStream, desc));
    }
}
```