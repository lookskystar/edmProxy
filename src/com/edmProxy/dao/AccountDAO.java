package com.edmProxy.dao;

import java.sql.Statement;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;


public class AccountDAO {

	private Connection conn = null; // 保存数据库连接
	private PreparedStatement pstmt = null; // 用于执行SQL语句

	private Statement stmt = null;

	private ResultSet rs = null; // 用户保存查询结果集
	private DBHelp dbHelp = new DBHelp();
	private String dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.format(Calendar.getInstance().getTime());

//	private static String TABLENAME = "";
	
	public AccountDAO(){
//		ReadPrpperties readPrpperties=new ReadPrpperties();
//		TABLENAME=readPrpperties.getTABLENAME();
	}
	

	/** 增加原始数据 */
	public int insert(AccountEntity obj) {
		int count = 0; // 接受返回值
		String preparedSql = "insert into account_tab"
				+ "(account,password,post,"
				+ " valid,start,createDate,"
				+ "sendCount,lastSendDate,remark) "
				+ "values(?,?,?" +",?,?,?" +",?,?,?)";
		// 占位符号数组
		Object[] param = {obj.getAccount(),obj.getPassword(),
						  obj.getPost(),obj.getValid()	, 
						  obj.getStart(),dateAndTime,0,
						  dateAndTime,obj.getRemark() };

		count = dbHelp.executeSQL(preparedSql, param);
		return count;
	}
	
	// 批量插入传入list
	public String insertBatch(ArrayList<AccountEntity> list) throws SQLException,
			ClassNotFoundException {
		Connection con = null;
		PreparedStatement ps = null;
		int i = 1;
		String msg = "";
		try {
			con = dbHelp.getConn();
			con.setAutoCommit(false);
			String sql = "insert into account_tab"
				+ "(account,password,post,"
				+ " valid,start,createDate,"
				+ "sendCount,lastSendDate,remark) "
				+ "values(?,?,?" +",?,?,?," +"?,?,?)";
			ps = con.prepareStatement(sql);
			int[] count;
			for (i = 0; i < list.size(); i++) {
				ps.setString(1, list.get(i).getAccount());
				ps.setString(2, list.get(i).getPassword());
				ps.setString(3, list.get(i).getPost());
				ps.setInt(4,list.get(i).getValid());
				ps.setInt(5,list.get(i).getStart());
				ps.setString(6, dateAndTime);
				ps.setInt(7,0);
				ps.setString(8,dateAndTime);
				ps.setString(9,"");
				ps.addBatch();
			}
			if (i % 10000 == 0) {
				count=ps.executeBatch();
			}
			count=ps.executeBatch();
			for(int j=0;j<count.length;j++){
				System.out.println(count[j]);
			}
			con.commit();
			// con.setAutoCommit(autoCommit);
			msg = "\n一共有-" + i + "-条数据插入完成! \n";
			return msg;
		} catch (SQLException e) {
			e.printStackTrace();
			// conn.rollback();
			return null;
		} finally {
			//closeAll(conn, pstmt, rs);
			dbHelp.closeAll(conn, pstmt, rs);
		}
	}
	
	
	//批量 传入list 插入 判断是否有该数据，如果没有则插入
	public String InsertBatchNotRepetitionByAccount(ArrayList<AccountEntity> list) throws SQLException,
			ClassNotFoundException {
		Connection con = null;
		PreparedStatement ps = null;
		int i = 0; //总执行数
		int k=0;//成功
		int j=0;//失败数（重复，未执行）
		String msg = "";
		int[] count;
		try {
			con = dbHelp.getConn();
			con.setAutoCommit(false);
			String sql = "insert into account_tab" +
					"(account,password,post,valid,start," +
					"createDate,sendCount," +
					"lastSendDate,remark)" +
					"select " +
					"?,?,?,?,?,?,?,?,? " +
					"from dual " +
					"WHERE NOT EXISTS" +
					"(select account " +
					"from account_tab " +
					"where account=?);";
			ps = con.prepareStatement(sql);
			for (i = 0; i < list.size(); i++) {
				ps.setString(1, list.get(i).getAccount());
				ps.setString(2, list.get(i).getPassword());
				ps.setString(3,list.get(i).getPost());
				ps.setInt(4, 1);
				ps.setInt(5, 1);
				ps.setString(6, dateAndTime);
				ps.setInt(7,0);
				ps.setString(8, dateAndTime);
				ps.setString(9,"");
				ps.setString(10,list.get(i).getAccount());
				ps.addBatch();
				if(i>0){
					if (i % 100000 == 0) {
						count=ps.executeBatch();
						if(count.length>0){
							if(count[0]==1){
								k++;
							}else if(count[0]==0){
								j++;
							}
						}
					}	
				}
				count=ps.executeBatch();
				if(count.length>0){
					if(count[0]==1){
						k++;
					}else if(count[0]==0){
						j++;
					}
				}
			}
			
			con.commit();
			// con.setAutoCommit(autoCommit);
			msg="批量执行：共有"+i+"条数据，其中"+j+"条重复未执行，成功执行"+k+"条数据";
			return msg;
		} catch (SQLException e) {
			e.printStackTrace();
			// conn.rollback();
			return null;
		} finally {
			//closeAll(conn, pstmt, rs);
			dbHelp.closeAll(conn, pstmt, rs);
		}
	}
	
	

	 // 分页 需要数据：1、数据总数。2、每页显示多少条数据。3、数据总数/每页显示多少条数据=页数。4、PageSize：每页显示多少条数据。
	 //TotalRow:数据总数。pageTotalRow：当前页总数据量。 page:当前页。
	 //分页 输入：pageNow：当前页，PageSize:每页显示多少条数据。
	
	public ArrayList<AccountEntity> pageingBypageNowAndpageSize(int pageNow,int pageSize) {
		ArrayList<AccountEntity> list = new ArrayList(); // 用来保存对象列表
		
		//rowNew当前页，数据总量
		int rowNewPage=(pageNow-1)*pageSize;
		
		StringBuffer sbSql = new StringBuffer();
		try {
			conn = dbHelp.getConn(); // 得到数据库连接
			// 以下方法分页，在hql是不行的，hql不支持top,要使用sql
			sbSql.append("select * from account_tab limit ");
			sbSql.append(rowNewPage+","+pageSize);
				//pstmt = conn.prepareStatement(sbSql.toString()); 
			stmt = conn.prepareStatement(sbSql.toString()); 

			//System.out.println(sbSql);
			rs = stmt.executeQuery(sbSql.toString()); // 执行sql取得结果集

			/* 循环将回复信息封装成List */
			while (rs.next()) {
				AccountEntity obj = new AccountEntity(); // 回复对象
				obj.setId(rs.getInt("id"));
				obj.setAccount(rs.getString("account"));
				obj.setPassword(rs.getString("password"));
				obj.setPost(rs.getString("post"));
				obj.setValid(rs.getInt("valid"));
				obj.setStart(rs.getInt("start"));
				obj.setCreateDate(rs.getDate("createDate"));
				obj.setSendCount(rs.getInt("sendCount"));
				obj.setLastSendDate(rs.getDate("lastSendDate"));
				obj.setRemark(rs.getString("remark"));
				list.add(obj);
			}

		} catch (Exception e) {
			e.printStackTrace(); // 处理异常
		} finally {
	//		closeAll(conn, pstmt, rs);
			dbHelp.closeAll(conn, pstmt, rs);
		}
		return list;
	}

	// 查询数据数据总数
	public int queryCount() {
		int count = 0;
		try {
			conn = dbHelp.getConn(); // 得到数据库连接
			// SQL语句
			String preparedSql = "select count(*) totalCount  from "
					+ "account_tab";
			pstmt = conn.prepareStatement(preparedSql); // 得到PreparedStatement对象
			// pstmt.setInt();

			rs = pstmt.executeQuery(); // 执行sql取得结果集

			while (rs.next()) {
				count = rs.getInt("totalCount");
			}
		} catch (Exception e) {
			e.printStackTrace(); // 处理异常
		} finally {
			//closeAll(conn, pstmt, rs);
			dbHelp.closeAll(conn, pstmt, rs);
		}
		return count;
	}
	//
	
    //-------------------------------------------------------------------------------------分页结束
	//条件和全部查询------------------------------------------------------------分页--------开结束


	/// 通过id批量删除 
	public int deleteBatch(String ids) {
		int count = 0; // 接受返回值
		// SQL语句
		
		String preparedSql = "delete from account_tab where id in (?)";
		// 占位符号数组
		String[] arr=ids.split(",");
		for (int i = 0; i < arr.length; i++) {
			Object[] param = { arr[i] };
			count = dbHelp.executeSQL(preparedSql,param);
		}
		System.out.println("count-->"+count);
		dbHelp.closeAll(conn, pstmt, rs);
		return count;
	}
	//通过id查询
	public AccountEntity findById(String id) {
			AccountEntity obj = new AccountEntity(); // 回复对象
			try {
				conn = dbHelp.getConn(); // 得到数据库连接
				// SQL语句
				String preparedSql = "select * from " + "account_tab where id="+id;
				pstmt = conn.prepareStatement(preparedSql); // 执行sql取得结果集
				rs = pstmt.executeQuery(); // 执行sql取得结果集
				
				//循环将回复信息封装成List
				while (rs.next()) {
					obj.setId(rs.getInt("id"));
					obj.setAccount(rs.getString("account"));
					obj.setPassword(rs.getString("password"));
					obj.setPost(rs.getString("post"));
					obj.setValid(rs.getInt("valid"));
					obj.setStart(rs.getInt("start"));
					obj.setCreateDate(rs.getDate("createDate"));
					obj.setSendCount(rs.getInt("sendCount"));
					obj.setLastSendDate(rs.getDate("lastSendDate"));
					obj.setRemark(rs.getString("remark"));
				}
			} catch (Exception e) {
				e.printStackTrace(); // 处理异常
			} finally {
				dbHelp.closeAll(conn, pstmt, rs);
			}
			return obj;
	}
	//通过邮箱查询
	public AccountEntity findByAccount(String account) {
			AccountEntity obj = new AccountEntity(); // 回复对象
			try {
				conn = dbHelp.getConn(); // 得到数据库连接
				// SQL语句
				String preparedSql = "select * from " + "account_tab where account="+"'"+account+"'";
				pstmt = conn.prepareStatement(preparedSql); // 执行sql取得结果集
				rs = pstmt.executeQuery(); // 执行sql取得结果集
				
				//循环将回复信息封装成List
				while (rs.next()) {
					obj.setId(rs.getInt("id"));
					obj.setAccount(rs.getString("account"));
					obj.setPassword(rs.getString("password"));
					obj.setPost(rs.getString("post"));
					obj.setValid(rs.getInt("valid"));
					obj.setStart(rs.getInt("start"));
					obj.setCreateDate(rs.getDate("createDate"));
					obj.setSendCount(rs.getInt("sendCount"));
					obj.setLastSendDate(rs.getDate("lastSendDate"));
					obj.setRemark(rs.getString("remark"));
				}
			} catch (Exception e) {
				e.printStackTrace(); // 处理异常
			} finally {
				dbHelp.closeAll(conn, pstmt, rs);
			}
			return obj;
	}
	
	//修改
	public int update(AccountEntity obj) {
		int count = 0; // 接受返回值
		// SQL语句
		String preparedSql = "update " + "account_tab"
				+ " set account=?,password=?,post=?," +
						"valid=?,start=?," +
						"remark=?  where id=? ";
		Object[] param = {obj.getAccount(),obj.getPassword(),
				obj.getPost(),obj.getValid(),
				obj.getStart(),obj.getRemark(),obj.getId()};
		count = dbHelp.executeSQL(preparedSql, param);
		return count;	
	}	
	
	//修改
	public int updateValidByAccount(String account,int valid) {
		int count = 0; // 接受返回值
		// SQL语句
		String preparedSql = "update " + "account_tab"
				+ " set valid=? where account=? ";
		Object[] param = {valid,account};
		count = dbHelp.executeSQL(preparedSql, param);
		return count;	
	}	
	
	
	
	//删除所有数据
	public int deleteAll() {
		int count = 0; // 接受返回值		
		String preparedSql = "delete from account_tab";
		Object[] param = {};
		count = dbHelp.executeSQL(preparedSql,param);
		dbHelp.closeAll(conn, pstmt, rs);
		return count;
	}
	//查询
	public ArrayList<AccountEntity> findBy(String account) {
		ArrayList<AccountEntity> list = new ArrayList(); 
		
		try {
			conn = dbHelp.getConn(); // 得到数据库连接
			// SQL语句
			String preparedSql = "select * from " + "account_tab where account like "+"'%"+account+"%'";
			//System.out.println(preparedSql);
			pstmt = conn.prepareStatement(preparedSql); // 执行sql取得结果集
			rs = pstmt.executeQuery(); // 执行sql取得结果集
			
			//循环将回复信息封装成List
			while (rs.next()) {
				AccountEntity obj = new AccountEntity(); // 回复对象
				obj.setId(rs.getInt("id"));
				obj.setAccount(rs.getString("account"));
				obj.setPassword(rs.getString("password"));
				obj.setPost(rs.getString("post"));
				obj.setValid(rs.getInt("valid"));
				obj.setStart(rs.getInt("start"));
				obj.setCreateDate(rs.getDate("createDate"));
				obj.setSendCount(rs.getInt("sendCount"));
				obj.setLastSendDate(rs.getDate("lastSendDate"));
				obj.setRemark(rs.getString("remark"));
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace(); // 处理异常
		} finally {
			dbHelp.closeAll(conn, pstmt, rs);
		}
		return list;
	}
	

	// 根据ID查找数据
	public static void main(String[] args) {
		//ProxyServerDAO lotteryDAO = new ProxyServerDAO();
		// System.out.println("---------------------->:"+lotteryDAO.queryCount());
	}


	


	


	

}
