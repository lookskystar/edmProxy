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
import com.edmProxy.entity.ReceiveEntity;


public class ReceiveDAO {

	private Connection conn = null; // 保存数据库连接
	private PreparedStatement pstmt = null; // 用于执行SQL语句

	private Statement stmt = null;

	private ResultSet rs = null; // 用户保存查询结果集
	private DBHelp dbHelp = new DBHelp();
	private String dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.format(Calendar.getInstance().getTime());

//	private static String TABLENAME = "";
	
	public ReceiveDAO(){
//		ReadPrpperties readPrpperties=new ReadPrpperties();
//		TABLENAME=readPrpperties.getTABLENAME();
	}
	

	/** 增加原始数据 */
	public int insert(ReceiveEntity obj) {
		int count = 0; // 接受返回值
		String preparedSql = "insert into receive_tab"
				+ "(receive,post,"
				+ "sendCount,createDate,"
				+ "lastSendDate,remark) "
				+ "values(?,?,?" +",?,?,?)";
		// 占位符号数组
		Object[] param = {obj.getReceive(),obj.getPost(),
						  0,dateAndTime,
						  dateAndTime,obj.getRemark() };

		count = dbHelp.executeSQL(preparedSql, param);
		return count;
	}
	
	//批量 传入list 插入 判断是否有该数据，如果没有则插入
	public String InsertBatchNotRepetitionByAccount(ArrayList<ReceiveEntity> list) throws SQLException,
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
			
			String sql = "insert into receive_tab" +
					"(receive,post," +
					"sendCount,createDate," +
					"lastSendDate,remark)values(?,?,?,?,?,?)";
//			String sql = "insert into receive_tab" +
//					"(receive,post," +
//					"sendCount,createDate," +
//					"lastSendDate,remark)" +
//					"select " +
//					"?,?,?,?,?,? " +
//					"from dual " +
//					"WHERE NOT EXISTS" +
//					"(select receive " +
//					"from receive_tab " +
//					"where receive=?);";
			ps = con.prepareStatement(sql);
			for (i = 0; i < list.size(); i++) {
				ps.setString(1, list.get(i).getReceive());
				ps.setString(2,list.get(i).getPost());
				ps.setInt(3, 0);
				ps.setString(4, dateAndTime);
				ps.setString(5, dateAndTime);
				ps.setString(6,"");
				//ps.setString(7,list.get(i).getReceive());
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
	
	public ArrayList<ReceiveEntity> pageingBypageNowAndpageSize(int pageNow,int pageSize) {
		ArrayList<ReceiveEntity> list = new ArrayList(); // 用来保存对象列表
		
		//rowNew当前页，数据总量
		int rowNewPage=(pageNow-1)*pageSize;
		
		StringBuffer sbSql = new StringBuffer();
		try {
			conn = dbHelp.getConn(); // 得到数据库连接
			// 以下方法分页，在hql是不行的，hql不支持top,要使用sql
			sbSql.append("select * from receive_tab limit ");
			sbSql.append(rowNewPage+","+pageSize);
				//pstmt = conn.prepareStatement(sbSql.toString()); 
			stmt = conn.prepareStatement(sbSql.toString()); 

			//conn = dbHelp.getConn();
//			stmt = conn.createStatement(
//					SQLServerResultSet.TYPE_SS_SERVER_CURSOR_FORWARD_ONLY,
//					ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sbSql.toString()); // 执行sql取得结果集

			/* 循环将回复信息封装成List */
			while (rs.next()) {
				ReceiveEntity obj = new ReceiveEntity(); // 回复对象
				obj.setId(rs.getInt("id"));
				obj.setReceive(rs.getString("receive"));
				obj.setPost(rs.getString("post"));
				obj.setSendCount(rs.getInt("sendCount"));
				obj.setCreateDate(rs.getDate("createDate"));
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
					+ "receive_tab";
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
	
	// 查询数据数据总数 根据条件
	public int queryCountByCondition(String condition) {
		int count = 0;
		try {
			conn = dbHelp.getConn(); // 得到数据库连接
			// SQL语句
			String preparedSql = "select count(*) totalCount  from "
					+ "receive_tab "+condition;
			System.out.println(preparedSql);
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
	
	//分页，根据条件
	public ArrayList<ReceiveEntity> pageingBypageNowAndpageSizeByCondition(int pageNow,int pageSize,String condition) {
		ArrayList<ReceiveEntity> list = new ArrayList(); // 用来保存对象列表
		
		//rowNew当前页，数据总量
//		int rowNewPage=1;
//		if(pageNow>1){
//			rowNewPage=(pageNow-1)*pageSize;
//		}
		int rowNewPage=(pageNow-1)*pageSize;
		
		
		StringBuffer sbSql = new StringBuffer();
		try {
			conn = dbHelp.getConn(); // 得到数据库连接
			sbSql.append("select * from receive_tab ");
			sbSql.append(condition);
			sbSql.append(" limit "+rowNewPage+","+pageSize);
			
				//pstmt = conn.prepareStatement(sbSql.toString()); 
			stmt = conn.prepareStatement(sbSql.toString()); 
			
			//conn = dbHelp.getConn();
//			stmt = conn.createStatement(
//					SQLServerResultSet.TYPE_SS_SERVER_CURSOR_FORWARD_ONLY,
//					ResultSet.CONCUR_READ_ONLY);
			System.out.println(sbSql.toString());
			rs = stmt.executeQuery(sbSql.toString()); // 执行sql取得结果集

			/* 循环将回复信息封装成List */
			while (rs.next()) {
				ReceiveEntity obj = new ReceiveEntity(); // 回复对象
				obj.setId(rs.getInt("id"));
				obj.setReceive(rs.getString("receive"));
				obj.setPost(rs.getString("post"));
				obj.setSendCount(rs.getInt("sendCount"));
				obj.setCreateDate(rs.getDate("createDate"));
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
	
	//
	
    //-------------------------------------------------------------------------------------分页结束
	//条件和全部查询------------------------------------------------------------分页--------开结束


	/// 通过id批量删除 
	public int deleteBatch(String ids) {
		int count = 0; // 接受返回值
		// SQL语句
		String preparedSql = "delete from receive_tab where id in (?)";
		// 占位符号数组
		String[] arr=ids.split(",");
		for (int i = 0; i < arr.length; i++) {
			Object[] param = { arr[i] };
			count = dbHelp.executeSQL(preparedSql,param);
		}
		//System.out.println("count-->"+count);
		dbHelp.closeAll(conn, pstmt, rs);
		return count;
	}
	//通过id查询
	public ReceiveEntity findById(String id) {
		    ReceiveEntity obj = new ReceiveEntity(); // 回复对象
			try {
				conn = dbHelp.getConn(); // 得到数据库连接
				// SQL语句
				String preparedSql = "select * from " + "receive_tab where id="+id;
				pstmt = conn.prepareStatement(preparedSql); // 执行sql取得结果集
				rs = pstmt.executeQuery(); // 执行sql取得结果集
				
				//循环将回复信息封装成List
				while (rs.next()) {
					obj.setId(rs.getInt("id"));
					obj.setReceive(rs.getString("receive"));
					obj.setPost(rs.getString("post"));
					obj.setSendCount(rs.getInt("sendCount"));
					obj.setCreateDate(rs.getDate("createDate"));
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
	public ReceiveEntity findByReceive(String receive) {
		ReceiveEntity obj = new ReceiveEntity(); // 回复对象
			try {
				conn = dbHelp.getConn(); // 得到数据库连接
				// SQL语句
				String preparedSql = "select * from " + "receive_tab where receive="+"'"+receive+"'";
				pstmt = conn.prepareStatement(preparedSql); // 执行sql取得结果集
				rs = pstmt.executeQuery(); // 执行sql取得结果集
				
				//循环将回复信息封装成List
				while (rs.next()) {
					obj.setId(rs.getInt("id"));
					obj.setReceive(rs.getString("receive"));
					obj.setPost(rs.getString("post"));
					obj.setSendCount(rs.getInt("sendCount"));
					obj.setCreateDate(rs.getDate("createDate"));
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
	public int update(ReceiveEntity obj) {
		int count = 0; // 接受返回值
		// SQL语句
		String preparedSql = "update " + "receive_tab"
				+ " set receive=?,post=?," +
						"remark=?  where id=? ";
		Object[] param = {obj.getReceive(),obj.getPost(),obj.getRemark(),obj.getId()};
		count = dbHelp.executeSQL(preparedSql, param);
		return count;	
	}	
	
	//删除所有数据
	public int deleteAll() {
		int count = 0; // 接受返回值		
		String preparedSql = "delete from receive_tab";
		Object[] param = {};
		count = dbHelp.executeSQL(preparedSql,param);
		dbHelp.closeAll(conn, pstmt, rs);
		return count;
	}
	//查询
	public ArrayList<ReceiveEntity> findBy(String value,int condition) {
		ArrayList<ReceiveEntity> list = new ArrayList(); 
		
		try {
			conn = dbHelp.getConn(); // 得到数据库连接
			// SQL语句
			String preparedSql = "";
			
			switch (condition) {
			case 0:
				preparedSql = "select * from " + "receive_tab where receive like "+"'%"+value+"%'";
				break;
			case 1:
				preparedSql = "select * from " + "receive_tab where post like "+"'%"+value+"%'";
				break;
			case 2:
				preparedSql = "select * from " + "receive_tab where sendCount <= "+value;
				break;
			case 3:
				preparedSql = "select * from " + "receive_tab where date_format(createDate,'%Y-%m-%d')='"+value+"'";
				break;
			case 4:
				preparedSql = "select * from " + "receive_tab where date_format(lastSendDate,'%Y-%m-%d')='"+value+"'";
				break;
			default:
				preparedSql = "select * from " + "receive_tab where receive like "+"'%"+value+"%'";
				break;
			}
			
			
			//System.out.println(preparedSql);
			pstmt = conn.prepareStatement(preparedSql); // 执行sql取得结果集
			rs = pstmt.executeQuery(); // 执行sql取得结果集
			
			//循环将回复信息封装成List
			while (rs.next()) {
				ReceiveEntity obj = new ReceiveEntity(); // 回复对象
				obj.setId(rs.getInt("id"));
				obj.setReceive(rs.getString("Receive"));
				obj.setPost(rs.getString("post"));
				obj.setSendCount(rs.getInt("sendCount"));
				obj.setCreateDate(rs.getDate("createDate"));
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
