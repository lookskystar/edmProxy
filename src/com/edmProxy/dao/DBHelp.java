package com.edmProxy.dao;

/*
 * 连接数据类
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

import com.edmProxy.util.ReadDBPrpperties;

//import com.lottery.util.ReadPrpperties;



public class DBHelp {
	//定义常量，把数据库驱动定义成一个字符串，服务器名和数据库名定义成一个字符串，数据库用户名定义成一个字符串，数据库密码定义成一个字符串
    private  static String DRIVER="";   //数据库驱动
    private  static String URL="";       //
    private  static String USER="";    //数据库用户名
    private  static String PASSWORD=""; //数据库密码
    
    
    
    public ResultSet rs=null;            //定义结果集
    
    public Connection conn=null;
    
    
    public PreparedStatement pstmt=null;
    
    public ReadDBPrpperties readPrpperties=new ReadDBPrpperties();
    
    /*
     * 得到数据库连接
     * @throws classNotFoundException
     * @throws SQLException
     * @return 数据库连接
     */
    public Connection getConn() throws ClassNotFoundException, SQLException{
    	try {
    		/*
    		//项目路径
            String pathString=DBHelp.class.getClassLoader().getResource("").toString();
	    	//pathString=pathString.substring(6, pathString.length());//打包发布读取配置文件信息
	    	pathString=pathString.substring(6, pathString.length()-4);//测试读取配置文件信息
	    	pathString=pathString+"resource/db.properties";     
	    	
	    	//System.out.println("---------"+pathString);
    		
			Properties props=new Properties();
			
			//从类路径下加载属性文件
			File file=new File(pathString);
			InputStreamReader in=new InputStreamReader(new FileInputStream(file),"gbk");
			*/

			DRIVER=readPrpperties.getDRIVER();
			URL=readPrpperties.getURL();
			USER=readPrpperties.getUSER();
			PASSWORD=readPrpperties.getPASSWORD();
			Class.forName(DRIVER);
			
			//Connection conn=DriverManager.getConnection(URL,USER,PASSWORD);
			conn=DriverManager.getConnection(URL,USER,PASSWORD);
    		//System.out.println("连接数据库成功.................");
    		return conn;    //返回连接	
			
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    		
    }
    
    
    
    /*
     * 释放资源
     * @param conn 数据库连接
     * @param pstmt PreparedStatement对象
     * @param rs 结果集
     */
    public static void closeAll(Connection conn,PreparedStatement pstmt,ResultSet rs){
    	//关闭资源应该从后望前关
    	/*如果rs不空，关闭rs*/
    	if(rs!=null){
    		try{
    			rs.close();
    		}
    		catch(SQLException e){
    				//输出异常
    				e.printStackTrace();
    			}
    		/*如果pstmt不空，关闭pstmt*/
    		if(pstmt!=null){
    			try {
					pstmt.close();
				} catch (Exception e) {
					//输出异常
					e.printStackTrace();
				}
    		}
    		/*如果conn不空，关闭conn*/
    		if(conn!=null){
    			try {
					conn.close();
				} catch (Exception e) {
					//输出异常
					e.printStackTrace();
				}
    		}
    		}
    	}
	
    /**
     * 执行SQL语句，可以进行增，删，改的操作，不能进行查询
     * @param sql 预编译的sql语句
     * @param param 预编译的sql语句中的‘？’，参数的字符串数组
     * @return 影响的条数
     */
    public int executeSQL(String preparedSql,Object[] param){
    	conn=null;
    	PreparedStatement pstmt=null;
    	ResultSet rs=null;
    	int num=0;
    	/*处理SQL,执行SQL*/
    	try {
			conn=getConn();       //得到数据库连接
			pstmt=conn.prepareStatement(preparedSql);
			if(pstmt!=null){
				for(int i=0;i<param.length;i++){
					pstmt.setObject(i+1, param[i]);  //设置参数   设置Object是因为如果是int类型就不出现类型不同的错误
				}
			}
			pstmt.executeUpdate();     //执行SQL语句
			rs=pstmt.getGeneratedKeys(); //得到刚插入的数据id
			if(rs.next()){
				num=rs.getInt(1);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();   //处理ClassNotFoundException异常
		} catch (SQLException e){
			e.printStackTrace();    //处理SQLException异常
		}finally{
			closeAll(conn, pstmt, rs);      //释放资源
		}
		return num;
    }
    
    /**
     * 执行SQL语句，进行查询
     * @param sql 预编译的sql语句
     * @param param 预编译的sql语句中的‘？’，参数的字符串数组
     * @return ResultSet结果集
     */
    public ResultSet executeQuerySQL(String preparedSql,Object[] param){
    	Connection conn=null;
    	pstmt=null;
    	/*处理SQL,执行SQL*/
    	try {
			conn=getConn();       //得到数据库连接
			pstmt=conn.prepareStatement(preparedSql);
			if(pstmt!=null){
				for(int i=0;i<param.length;i++){
					pstmt.setObject(i+1, param[i]);  //设置参数   设置Object是因为如果是int类型就不出现类型不同的错误	
				}
			}
			rs=pstmt.executeQuery();     //执行SQL语句
		} catch (ClassNotFoundException e) {
			e.printStackTrace();   //处理ClassNotFoundException异常
		} catch (SQLException e){
			e.printStackTrace();    //处理SQLException异常
		}finally{
			closeAll(conn, pstmt, rs);      //释放资源
		}
		return rs;
    }
    
    
    
    
    //测试代码
    public static void main(String[] args) throws Exception {
    	DBHelp dBHelp=new DBHelp();
    	System.out.println(dBHelp.getConn());
	}
}