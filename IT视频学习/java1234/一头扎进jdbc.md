# 第1讲

# 第2讲

eclipse快捷键：

```
快速找错误：Ctrl+1 
导入包:Ctrl+Shift+o
```

引入驱动

```java
private static String jdbcName="com.mysql.jdbc.Driver";
	public static void main(String[] args) {
		
		try {
			Class.forName(jdbcName);
			System.out.println("load驱动 success");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("加载驱动失败");
		}
		
	}
```

连接数据库demo1.java: 

```java
package com.java1234.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Demo1 {

	private static String jdbcName="com.mysql.jdbc.Driver";
	private static String dbUrl="jdbc:mysql://localhost:3306/db_book";
	private static String dbUser="root";
	private static String dbPwd="123";
	
	public static void main(String[] args) {
		
		try {
			Class.forName(jdbcName);
			System.out.println("load驱动 success");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("加载驱动失败");
		}
		Connection con=null;
		try {
			con=DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			System.out.println("获取数据库连接成功");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("连接失败");
		}
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
```

# 第3讲 statement 增删改

eclipse快捷键：

```java
ctrl+shift+/ //多行注释
ctrl+shift+f //格式化
```
DbUtil.java

```java
package com.java1234.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbUtil {
	private static String jdbcName="com.mysql.jdbc.Driver";
	private static String dbUrl="jdbc:mysql://localhost:3306/db_book";
	private static String dbUser="root";
	private static String dbPwd="123";
	
	/**
	 * 获取连接
	 * @return
	 * @throws Exception
	 */
	public Connection getCon() throws Exception{
		Class.forName(jdbcName);
		Connection con=DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		return con;
	}
	
	/**
	 * 关闭数据库连接
	 */
	public void close(Statement stmt,Connection con) throws Exception{
		if(stmt!=null){
			stmt.close();	
			if(con!=null){
				con.close();
			}
		}

	}
}
```

# 第4讲 Preparestatement 增删改

```java
private static int updateBook(Book book) throws Exception{
		String sql="update  t_book set bookName=? where id=?";
		Connection con=dbUtil.getCon();
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, book.getBookName());
		pstmt.setFloat(2, book.getId());
		int result=pstmt.executeUpdate();
		return result;
	}
```

# 第5讲 resultSet结果集

```java
private static List<Journal> listJournal2() throws Exception{
		
		List<Journal> journalList=new ArrayList<Journal>();
		Connection con = dbUtil.getCon();

		String sql = "select a.startTime,a.endTime,a.event,b.addressAbbrName address,c.categoryName, d.gradeName,a.remark "
				+ "from tb_journal a,tb_address b, "
				+ "tb_category c,tb_grade d "
				+ "where a.address=b.addressId and a.category=c.categoryId and a.grade=d.gradeId order by a.startTime desc ";
		PreparedStatement pstmt = con.prepareStatement(sql);
		java.sql.ResultSet rs = pstmt.executeQuery();
		
		while (rs.next()) {
			String startTime = rs.getString("startTime");
			String endTime = rs.getString("endTime");
			String event = rs.getString("event");
			String categoryName = rs.getString("categoryName");
			String addressName = rs.getString("address");
			String gradeName = rs.getString("gradeName");
			String remark = rs.getString("remark");
			Journal journal=new Journal(startTime, endTime, event, addressName, categoryName, gradeName, remark);
			journalList.add(journal);
		}
		dbUtil.close(pstmt, con);
		return journalList;
	}
```

处理大数据对象：blob，clob