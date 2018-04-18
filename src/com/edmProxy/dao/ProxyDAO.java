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

import com.edmProxy.entity.ProxyEntity;


public class ProxyDAO {

	private Connection conn = null; // 保存数据库连接
	private PreparedStatement pstmt = null; // 用于执行SQL语句

	private Statement stmt = null;

	private ResultSet rs = null; // 用户保存查询结果集
	private DBHelp dbHelp = new DBHelp();
	private String dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.format(Calendar.getInstance().getTime());	
	public ProxyDAO(){
	}
	

	/** 增加原始数据 */
	public int insert(ProxyEntity obj) {
		int count = 0; // 接受返回值
		// String serialnumber = ""; // 保存随机产生的Number
		// SQL语句
		String preparedSql = "insert into proxy_tab" //+ TABLENAME
				+ "(proxyHost,proxyPort,proxyType,"
				+ " proxyAccount,proxyPassword,"
				+ "valid,start,createDate,"
				+ "proxyCount,lastProxyDate,remark) "
				+ "values(?,?,?,?" +",?,?,?,?," +"?,?,?)";
		// 占位符号数组
		Object[] param = {obj.getProxyHost(),obj.getProxyPort(),
						  (Integer)obj.getProxyType(),obj.getProxyAccount()	, 
						  obj.getProxyPassword(),(Integer)obj.getValid(),
						  (Integer)obj.getStart(),
						  dateAndTime,0,dateAndTime,obj.getRemark() };

		count = dbHelp.executeSQL(preparedSql, param);
		//System.out.println("->"+obj.getProxyType()+"："+obj.getValid()+":"+obj.getStart());
		return count;
	}
	
	// 批量插入传入list
	public String insertBatch(ArrayList<ProxyEntity> list) throws SQLException,
			ClassNotFoundException {
		Connection con = null;
		PreparedStatement ps = null;
		int i = 1;
		String msg = "";
		try {
			con = dbHelp.getConn();
			con.setAutoCommit(false);
			String sql = "insert into " + "proxy_tab"
					+ "(proxyHost,proxyPort,proxyType,proxyAccount," +
					"proxyPassword,valid,start,createDate,proxyCount," +
					"lastProxyDate,remark"+
					") " + "values(?,?,?,?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);

			for (i = 0; i < list.size(); i++) {
				ps.setString(1, list.get(i).getProxyHost());
				ps.setString(2, list.get(i).getProxyPort());
				ps.setInt(3, list.get(i).getProxyType());
				ps.setString(4,list.get(i).getProxyAccount());
				ps.setString(5,list.get(i).getProxyPassword());
				ps.setInt(6,list.get(i).getValid());
				ps.setInt(7,list.get(i).getStart());
				ps.setString(8, dateAndTime);
				ps.setInt(9,0);
				ps.setString(10,dateAndTime);
				ps.setString(11,"");
				ps.addBatch();

				// System.out.println("-------i的值："+i);
				if (i % 10000 == 0) {
					ps.executeBatch();
				}
				ps.executeBatch();
				
				
			}
			
			// System.out.println(dateAndTime);
			con.commit();
			// con.setAutoCommit(autoCommit);
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
	public String InsertBatchNotRepetitionByProxyHost(ArrayList<ProxyEntity> list) throws SQLException,
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
			String sql = "insert into  proxy_tab" +
					"(proxyHost,proxyPort,proxyType,proxyAccount," +
					"proxyPassword,valid,start,createDate,proxyCount," +
					"lastProxyDate,remark)" +
					"select " +
					"?,?,?,?,?,?,?,?,?,?,? " +
					"from dual " +
					"WHERE NOT EXISTS" +
					"(select proxyHost " +
					"from proxy_tab " +
					"where proxyHost=?);";
			ps = con.prepareStatement(sql);
			for (i = 0; i < list.size(); i++) {
				ps.setString(1, list.get(i).getProxyHost());
				ps.setString(2, list.get(i).getProxyPort());
				ps.setInt(3, 1);
				ps.setString(4,list.get(i).getProxyAccount());
				ps.setString(5,list.get(i).getProxyPassword());
				ps.setInt(6,1);
				ps.setInt(7,1);
				ps.setString(8, dateAndTime);
				ps.setInt(9,0);
				ps.setString(10,dateAndTime);
				ps.setString(11,"");
				ps.setString(12,list.get(i).getProxyHost());
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
	
	public ArrayList<ProxyEntity> pageingBypageNowAndpageSize(int pageNow,int pageSize) {
		ArrayList<ProxyEntity> list = new ArrayList(); // 用来保存对象列表
		
		//rowNew当前页，数据总量
		int rowNewPage=(pageNow-1)*pageSize;
		
		StringBuffer sbSql = new StringBuffer();
		try {
			conn = dbHelp.getConn(); // 得到数据库连接
			// 以下方法分页，在hql是不行的，hql不支持top,要使用sql
			sbSql.append("select * from proxy_tab limit ");
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
				ProxyEntity obj = new ProxyEntity(); // 回复对象
				obj.setId(rs.getInt("id"));
				obj.setProxyHost(rs.getString("proxyHost"));
				obj.setProxyPort(rs.getString("proxyPort"));
				obj.setProxyType(rs.getInt("proxyType"));
				obj.setProxyAccount(rs.getString("proxyAccount"));
				obj.setProxyPassword(rs.getString("proxyPassword"));
				obj.setValid(rs.getInt("valid"));
				obj.setStart(rs.getInt("start"));
				obj.setCreateDate(rs.getDate("createDate"));
				obj.setProxyCount(rs.getInt("proxyCount"));
				obj.setLastProxyDate(rs.getDate("lastProxyDate"));
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
					+ "proxy_tab";
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
		
		String preparedSql = "delete from proxy_tab where id in (?)";
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
		public ProxyEntity findById(String id) {
		ProxyEntity obj = new ProxyEntity(); // 回复对象
		try {
			conn = dbHelp.getConn(); // 得到数据库连接
			// SQL语句
			String preparedSql = "select * from " + "proxy_tab where id="+id;
			pstmt = conn.prepareStatement(preparedSql); // 执行sql取得结果集
			rs = pstmt.executeQuery(); // 执行sql取得结果集
			
			//循环将回复信息封装成List
			while (rs.next()) {
				obj.setId(rs.getInt("id"));
				obj.setProxyHost(rs.getString("proxyHost"));
				obj.setProxyPort(rs.getString("proxyPort"));
				obj.setProxyType(rs.getInt("proxyType"));
				obj.setProxyAccount(rs.getString("proxyAccount"));
				obj.setProxyPassword(rs.getString("proxyPassword"));
				obj.setValid(rs.getInt("valid"));
				obj.setStart(rs.getInt("start"));
				obj.setCreateDate(rs.getDate("createDate"));
				obj.setProxyCount(rs.getInt("proxyCount"));
				obj.setLastProxyDate(rs.getDate("lastProxyDate"));
				obj.setRemark(rs.getString("remark"));
			}
		} catch (Exception e) {
			e.printStackTrace(); // 处理异常
		} finally {
			dbHelp.closeAll(conn, pstmt, rs);
		}
		return obj;
	}
		//通过主机查询
		public ProxyEntity findByProxyHost(String proxyHost) {
			ProxyEntity obj = new ProxyEntity(); // 回复对象
			try {
				conn = dbHelp.getConn(); // 得到数据库连接
				// SQL语句
				String preparedSql = "select * from " + "proxy_tab where proxyHost='"+proxyHost+"'";
				pstmt = conn.prepareStatement(preparedSql); // 执行sql取得结果集
				rs = pstmt.executeQuery(); // 执行sql取得结果集
				
				//循环将回复信息封装成List
				while (rs.next()) {
					obj.setId(rs.getInt("id"));
					obj.setProxyHost(rs.getString("proxyHost"));
					obj.setProxyPort(rs.getString("proxyPort"));
					obj.setProxyType(rs.getInt("proxyType"));
					obj.setProxyAccount(rs.getString("proxyAccount"));
					obj.setProxyPassword(rs.getString("proxyPassword"));
					obj.setValid(rs.getInt("valid"));
					obj.setStart(rs.getInt("start"));
					obj.setCreateDate(rs.getDate("createDate"));
					obj.setProxyCount(rs.getInt("proxyCount"));
					obj.setLastProxyDate(rs.getDate("lastProxyDate"));
					obj.setRemark(rs.getString("remark"));
				}
			} catch (Exception e) {
				e.printStackTrace(); // 处理异常
			} finally {
				dbHelp.closeAll(conn, pstmt, rs);
			}
			return obj;
		}
		
		//通过主机查询
		public ProxyEntity findFistByValid(int valid) {
			ProxyEntity obj = new ProxyEntity(); // 回复对象
			try {
				conn = dbHelp.getConn(); // 得到数据库连接
				// SQL语句
				String preparedSql = "select * from " + "proxy_tab where valid="+valid+" limit 0,1";
				System.out.println(preparedSql);
				pstmt = conn.prepareStatement(preparedSql); // 执行sql取得结果集
				rs = pstmt.executeQuery(); // 执行sql取得结果集
				
				//循环将回复信息封装成List
				while (rs.next()) {
					obj.setId(rs.getInt("id"));
					obj.setProxyHost(rs.getString("proxyHost"));
					obj.setProxyPort(rs.getString("proxyPort"));
					obj.setProxyType(rs.getInt("proxyType"));
					obj.setProxyAccount(rs.getString("proxyAccount"));
					obj.setProxyPassword(rs.getString("proxyPassword"));
					obj.setValid(rs.getInt("valid"));
					obj.setStart(rs.getInt("start"));
					obj.setCreateDate(rs.getDate("createDate"));
					obj.setProxyCount(rs.getInt("proxyCount"));
					obj.setLastProxyDate(rs.getDate("lastProxyDate"));
					obj.setRemark(rs.getString("remark"));
				}
			} catch (Exception e) {
				e.printStackTrace(); // 处理异常
			} finally {
				dbHelp.closeAll(conn, pstmt, rs);
			}
			return obj;
		}
		
		
		
	//模糊查询
		public ArrayList<ProxyEntity> findBy(String proxyHost) {
			ArrayList<ProxyEntity> list = new ArrayList(); 
			
			try {
				conn = dbHelp.getConn(); // 得到数据库连接
				// SQL语句
				String preparedSql = "select * from " + "proxy_tab where proxyHost like "+"'%"+proxyHost+"%'";
				//System.out.println(preparedSql);
				pstmt = conn.prepareStatement(preparedSql); // 执行sql取得结果集
				rs = pstmt.executeQuery(); // 执行sql取得结果集
				
				//循环将回复信息封装成List
				while (rs.next()) {
					ProxyEntity obj = new ProxyEntity(); // 回复对象
					obj.setId(rs.getInt("id"));
					obj.setProxyHost(rs.getString("proxyHost"));
					obj.setProxyPort(rs.getString("proxyPort"));
					obj.setProxyType(rs.getInt("proxyType"));
					obj.setProxyAccount(rs.getString("proxyAccount"));
					obj.setProxyPassword(rs.getString("proxyPassword"));
					obj.setValid(rs.getInt("valid"));
					obj.setStart(rs.getInt("start"));
					obj.setCreateDate(rs.getDate("createDate"));
					obj.setProxyCount(rs.getInt("proxyCount"));
					obj.setLastProxyDate(rs.getDate("lastProxyDate"));
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
	
	
	//修改
	public int update(ProxyEntity proxyEntiy) {
		int count = 0; // 接受返回值
		// SQL语句
		String preparedSql = "update " + "proxy_tab"
				+ " set proxyHost=?,proxyPort=?,proxyType=?," +
						"proxyAccount=?,proxyPassword=?," +
						"valid=?,start=?,remark=?  where id=? ";
		Object[] param = {proxyEntiy.getProxyHost(),proxyEntiy.getProxyPort(),
				proxyEntiy.getProxyType(),proxyEntiy.getProxyAccount(),
				proxyEntiy.getProxyPassword(),proxyEntiy.getValid(),
				proxyEntiy.getStart(),proxyEntiy.getRemark(),proxyEntiy.getId()};
		count = dbHelp.executeSQL(preparedSql, param);
		return count;	
	}	
	
	
	//修改 通过代理主机修改valid
	public int updateValidByProxyHost(String proxyHost,int valid) {
		int count = 0; // 接受返回值
		// SQL语句
		String preparedSql = "update " + "proxy_tab"
				+ " set valid=? where proxyHost=? ";
		Object[] param = {valid,proxyHost};
		count = dbHelp.executeSQL(preparedSql, param);
		return count;	
	}	
	
	//删除所有数据
	public int deleteAll() {
		int count = 0; // 接受返回值		
		String preparedSql = "delete from proxy_tab";
		Object[] param = {};
		count = dbHelp.executeSQL(preparedSql,param);
		dbHelp.closeAll(conn, pstmt, rs);
		return count;
	}
	

	/**
	 * 查询
	 */
//	public List findListLottery() {
//		List list = new ArrayList(); // 用来保存对象列表
//		try {
//			conn = dbHelp.getConn(); // 得到数据库连接
//			// SQL语句
//			String preparedSql = "select * from " + TABLENAME;
//			stmt = conn.createStatement(
//					SQLServerResultSet.TYPE_SS_SERVER_CURSOR_FORWARD_ONLY,
//					ResultSet.CONCUR_READ_ONLY);
//			// stmt = conn.prepareStatement(preparedSql); //
//			// 得到PreparedStatement对象
//			rs = stmt.executeQuery(preparedSql); // 执行sql取得结果集
//			/* 循环将回复信息封装成List */
//			while (rs.next()) {
//				Lottery lottery = new Lottery(); // 回复对象
//				lottery.setId(rs.getInt("id"));
//				lottery.setSerialnumber(rs.getString("serialnumber"));
//				lottery.setHundreds(rs.getString("hundreds"));
//				lottery.setDecade(rs.getString("decade"));
//				lottery.setSingledigit(rs.getString("singledigit"));
//
//				lottery.setOmit1record(rs.getString("omit1record"));
//				lottery.setOmit1(rs.getInt("omit1"));
//				lottery.setOmit1state(rs.getInt("omit1state"));
//
//				lottery.setOmit2record(rs.getString("omit2record"));
//				lottery.setOmit2(rs.getInt("omit2"));
//				lottery.setOmit2state(rs.getInt("omit2state"));
//
//				lottery.setOmit3record(rs.getString("omit3record"));
//				lottery.setOmit3(rs.getInt("omit3"));
//				lottery.setOmit3state(rs.getInt("omit3state"));
//
//				lottery.setOmitnewrecord(rs.getString("omitnewrecord"));
//				lottery.setOmitnew(rs.getInt("omitnew"));
//				lottery.setOmitnewstate(rs.getInt("omitnewstate"));
//
//				lottery.setOmitsum(rs.getInt("omitsum"));
//				lottery.setComparecount(rs.getInt("comparecount"));
//				lottery.setDateAndTime(rs.getDate("dateAndTime"));
//				list.add(lottery);
//			}
//		} catch (Exception e) {
//			e.printStackTrace(); // 处理异常
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return list;
//	}

	/** 通过id删除 */
//	public int deleteLottery(int id) {
//		int count = 0; // 接受返回值
//		// SQL语句
//		String preparedSql = "delete from " + TABLENAME + " where id=? ";
//		// 占位符号数组
//		Object[] param = { id };
//		count = dbHelp.executeSQL(preparedSql, param);
//		return count;
//	}


	/** 通过id修改omitsum */
//	public int updateOmitsum(int omitsum, int id) {
//		int count = 0; // 接受返回值
//		// SQL语句
//		String preparedSql = "update " + TABLENAME
//				+ " set omitsum=?  where id=? ";
//		Object[] param = { omitsum, id };
//		count = dbHelp.executeSQL(preparedSql, param);
//		return count;
//	}

	/** 通过id修改comparecount */
//	public int updateComparecount(int comparecount, int id) {
//		int count = 0; // 接受返回值
//		// SQL语句
//		String preparedSql = "update " + TABLENAME
//				+ " set comparecount=?  where id=? ";
//		Object[] param = { comparecount, id };
//		count = dbHelp.executeSQL(preparedSql, param);
//		return count;
//	}

	/**
	 * 清空，一次遗漏记录null：一次遗漏-1：一次遗漏状态1，上次遗漏记录null：上次遗漏-1：上次遗漏状态1，
	 * 本次遗漏记录null：本次遗漏-1：本次遗漏状态1，新遗漏记录null：新遗漏-1：新遗漏状态1, 遗漏统计0，比较次数0
	 * 
	 */
//	public int updateLotteryOmit1Omit2Omit3OmitnewOther() {
//		int count = 0; // 接受返回值
//		// SQL语句
//		// omit1record,omit1,omit1state,omit2record,omit2,omit2state,omit3record,omit3,omit3state,
//		// omitnewrecord,omitnew,omitnewstate,omitsum,comparecount,dateAndTime
//		String preparedSql = "update " + TABLENAME
//				+ " set omit1record=? ,omit1=?,omit1state=?,"
//				+ "omit2record=?,omit2=?,omit2state=?,"
//				+ "omit3record=?,omit3=?,omit3state=?,"
//				+ "omitnewrecord=?,omitnew=?,omitnewstate=?,"
//				+ "omitsum=?,comparecount=?";
//		Object[] param = { "", -1, 1, "", -1, 1, "", -1, 1, "", -1, 1, 0, 0 };
//		count = dbHelp.executeSQL(preparedSql, param);
//		return count;
//	}
	
	
	//条件和全部查询------------------------------------------------------------分页--------开始
	// 查询数据数据总数,输入状态，百位，十位，遗漏统计，根据state的值，判断用那种统计（1：根据百位，十位查询；2：根据遗漏统计查询；3：无条件查询）
//	public int queryCount(int state,String hundredsNumberStr, String decadeNumberStr,String singledigitStr,int num) {
//		
//		int count = 0;
//		String preparedSql="";
//		try {
//			conn = dbHelp.getConn(); // 得到数据库连接
//			// SQL语句
//			if(state==1){
//				preparedSql = "select count(*) totalCount  from "+ TABLENAME+" where hundreds like '%"+hundredsNumberStr+"%' and decade like '%"+decadeNumberStr+"%' and singledigit like '%"+singledigitStr+"%'";
//				//pstmt.setString(1, "%" + hundredsNumberStr + "%");
//				//pstmt.setString(2, "%" + decadeNumberStr + "%");
//			}else if(state==2){
//				preparedSql = "select count(*) totalCount  from "+ TABLENAME+" where omitsum>="+num;
//				//pstmt.setInt(1, num);
//			}else if(state==3){
//				preparedSql = "select count(*) totalCount  from "+ TABLENAME;
//			}
//			
//			pstmt = conn.prepareStatement(preparedSql); // 得到PreparedStatement对象
//			rs = pstmt.executeQuery(); // 执行sql取得结果集
//
//			while (rs.next()) {
//				count = rs.getInt("totalCount");
//			}
//		} catch (Exception e) {
//			e.printStackTrace(); // 处理异常
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return count;
//	}
	
	
	/*
	 * 分页 需要数据：1、数据总数。2、每页显示多少条数据。3、数据总数/每页显示多少条数据=页数。4、PageSize：每页显示多少条数据。
	 * TotalRow:数据总数。pageTotalRow：当前页总数据量。 page:当前页。
	 */

	/*
	 * 分页 输入：pageTotalRow：当前页总数据量，PageSize:每页显示多少条数据。
	 */
//	public List<Lottery> findPageByPageTotalRowAndPage(int state,String hundredsNumberStr, String decadeNumberStr,String singledigitNumberStr,int num,int pageTotalRow,int pageSize) {
//		List list = new ArrayList(); // 用来保存对象列表
//		StringBuffer sbSql = new StringBuffer();
//		try {
//			conn = dbHelp.getConn(); // 得到数据库连接
//			// 以下方法分页，在hql是不行的，hql不支持top,要使用sql
//			if(state==1){  //根据百位，十位,个位 查询
//				sbSql.append("select top" + " " +pageSize + " " + "*");
//				sbSql.append(" " + "from " +TABLENAME);
//				sbSql.append(" " + "where id not in");
//				sbSql.append("(select top " +pageTotalRow);
//				sbSql.append(" " + "id from " +TABLENAME);
//				sbSql.append(" " + "where hundreds like '%"+hundredsNumberStr+"%' and decade like '%"+decadeNumberStr+"%' and singledigit like '%"+singledigitNumberStr+"%'");
//				sbSql.append(" " + "order by id asc)");
//				sbSql.append(" " + "and hundreds like '%"+hundredsNumberStr+"%' and decade like '%"+decadeNumberStr+"%' and singledigit like '%"+singledigitNumberStr+"%'");
//				sbSql.append(" " + "order by id asc");
//				//pstmt = conn.prepareStatement(sbSql.toString()); 
////				pstmt.setString(1, "%" + hundredsNumberStr + "%");
////				pstmt.setString(2, "%" + decadeNumberStr + "%");
//			}else if(state==2){  //根据遗漏统计查询
//				sbSql.append("select top" + " " +pageSize + " " + "*");
//				sbSql.append(" " + "from " +TABLENAME);
//				sbSql.append(" " + "where id not in");
//				sbSql.append("(select top " +pageTotalRow);
//				sbSql.append(" " + "id from " +TABLENAME);
//				sbSql.append(" " + "where omitsum>="+num);
//				sbSql.append(" " + "order by id asc)");
//				sbSql.append(" " + "and omitsum>="+num);
//				sbSql.append(" " + "order by id asc");
//				//pstmt = conn.prepareStatement(sbSql.toString()); 
////				pstmt.setInt(1, num);
//			}else if(state==3){  //无条件查询
//				sbSql.append("select top" + " " +pageSize + " " + "*");
//				sbSql.append(" " + "from " +TABLENAME);
//				sbSql.append(" " + "where id not in");
//				sbSql.append("(select top " +pageTotalRow);
//				sbSql.append(" " + "id from " +TABLENAME);
//				sbSql.append(" " + "order by id asc) order by id asc");
//				//pstmt = conn.prepareStatement(sbSql.toString()); 
//			}
//			stmt = conn.prepareStatement(sbSql.toString()); 
//
//			//conn = dbHelp.getConn();
//			stmt = conn.createStatement(
//					SQLServerResultSet.TYPE_SS_SERVER_CURSOR_FORWARD_ONLY,
//					ResultSet.CONCUR_READ_ONLY);
//			rs = stmt.executeQuery(sbSql.toString()); // 执行sql取得结果集
//
//			/* 循环将回复信息封装成List */
//			while (rs.next()) {
//				Lottery lottery = new Lottery(); // 回复对象
//				lottery.setId(rs.getInt("id"));
//				lottery.setSerialnumber(rs.getString("serialnumber"));
//				lottery.setHundreds(rs.getString("hundreds"));
//				lottery.setDecade(rs.getString("decade"));
//				lottery.setSingledigit(rs.getString("singledigit"));
//
//				lottery.setOmit1record(rs.getString("omit1record"));
//				lottery.setOmit1(rs.getInt("omit1"));
//				lottery.setOmit1state(rs.getInt("omit1state"));
//
//				lottery.setOmit2record(rs.getString("omit2record"));
//				lottery.setOmit2(rs.getInt("omit2"));
//				lottery.setOmit2state(rs.getInt("omit2state"));
//
//				lottery.setOmit3record(rs.getString("omit3record"));
//				lottery.setOmit3(rs.getInt("omit3"));
//				lottery.setOmit3state(rs.getInt("omit3state"));
//
//				lottery.setOmitnewrecord(rs.getString("omitnewrecord"));
//				lottery.setOmitnew(rs.getInt("omitnew"));
//				lottery.setOmitnewstate(rs.getInt("omitnewstate"));
//
//				lottery.setOmitsum(rs.getInt("omitsum"));
//				lottery.setComparecount(rs.getInt("comparecount"));
//				lottery.setDateAndTime(rs.getDate("dateAndTime"));
//				list.add(lottery);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace(); // 处理异常
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return list;
//	}
    //-------------------------------------------------------------------------------------分页结束
	//条件和全部查询------------------------------------------------------------分页--------开结束

	

	// 通过输入的统计数，查出大于该统计数的结果
//	public List queryByOmitsum(int num) {
//		List list = new ArrayList(); // 用来保存对象列表
//		list.clear();
//		try {
//			conn = dbHelp.getConn(); // 得到数据库连接
//			// SQL语句
//			String preparedSql = "select * from " + TABLENAME
//					+ " where omitsum>=?";
//			pstmt = conn.prepareStatement(preparedSql); // 得到PreparedStatement对象
//			pstmt.setInt(1, num);
//
//			rs = pstmt.executeQuery(); // 执行sql取得结果集
//
//			/* 循环将回复信息封装成List */
//			while (rs.next()) {
//				Lottery lottery = new Lottery(); // 回复对象
//				lottery.setId(rs.getInt("id"));
//				lottery.setSerialnumber(rs.getString("serialnumber"));
//				lottery.setHundreds(rs.getString("hundreds"));
//				lottery.setDecade(rs.getString("decade"));
//				lottery.setSingledigit(rs.getString("singledigit"));
//
//				lottery.setOmit1record(rs.getString("omit1record"));
//				lottery.setOmit1(rs.getInt("omit1"));
//				lottery.setOmit1state(rs.getInt("omit1state"));
//
//				lottery.setOmit2record(rs.getString("omit2record"));
//				lottery.setOmit2(rs.getInt("omit2"));
//				lottery.setOmit2state(rs.getInt("omit2state"));
//
//				lottery.setOmit3record(rs.getString("omit3record"));
//				lottery.setOmit3(rs.getInt("omit3"));
//				lottery.setOmit3state(rs.getInt("omit3state"));
//
//				lottery.setOmitnewrecord(rs.getString("omitnewrecord"));
//				lottery.setOmitnew(rs.getInt("omitnew"));
//				lottery.setOmitnewstate(rs.getInt("omitnewstate"));
//
//				lottery.setOmitsum(rs.getInt("omitsum"));
//				lottery.setComparecount(rs.getInt("comparecount"));
//				lottery.setDateAndTime(rs.getDate("dateAndTime"));
//				list.add(lottery);
//			}
//		} catch (Exception e) {
//			e.printStackTrace(); // 处理异常
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return list;
//	}

	// 显示合计统计sum值
//	public int sumShow(int id) {
//
//		int sumOmit123 = 0;
//		List<Lottery> list = new ArrayList(); // 用来保存对象列表
//		try {
//			conn = dbHelp.getConn(); // 得到数据库连接
//			// SQL语句
//			String preparedSql = "select * from " + TABLENAME + " where id=?";
//			pstmt = conn.prepareStatement(preparedSql); // 得到PreparedStatement对象
//			pstmt.setInt(1, id);
//
//			rs = pstmt.executeQuery(); // 执行sql取得结果集
//
//			/* 循环将回复信息封装成List */
//			while (rs.next()) {
//				Lottery lottery = new Lottery(); // 回复对象
//				lottery.setId(rs.getInt("id"));
//				lottery.setSerialnumber(rs.getString("serialnumber"));
//				lottery.setHundreds(rs.getString("hundreds"));
//				lottery.setDecade(rs.getString("decade"));
//				lottery.setSingledigit(rs.getString("singledigit"));
//
//				lottery.setOmit1record(rs.getString("omit1record"));
//				lottery.setOmit1(rs.getInt("omit1"));
//				lottery.setOmit1state(rs.getInt("omit1state"));
//
//				lottery.setOmit2record(rs.getString("omit2record"));
//				lottery.setOmit2(rs.getInt("omit2"));
//				lottery.setOmit2state(rs.getInt("omit2state"));
//
//				lottery.setOmit3record(rs.getString("omit3record"));
//				lottery.setOmit3(rs.getInt("omit3"));
//				lottery.setOmit3state(rs.getInt("omit3state"));
//
//				lottery.setOmitnewrecord(rs.getString("omitnewrecord"));
//				lottery.setOmitnew(rs.getInt("omitnew"));
//				lottery.setOmitnewstate(rs.getInt("omitnewstate"));
//
//				lottery.setOmitsum(rs.getInt("omitsum"));
//				lottery.setComparecount(rs.getInt("comparecount"));
//				lottery.setDateAndTime(rs.getDate("dateAndTime"));
//				list.add(lottery);
//			}
//			int omit1Int = list.get(0).getOmit1();
//			int omit2Int = list.get(0).getOmit2();
//			int omit3Int = list.get(0).getOmit3();
//
//			if (omit1Int == -1) {
//				omit1Int = 0;
//			}
//			if (omit2Int == -1) {
//				omit2Int = 0;
//			}
//			if (omit3Int == -1) {
//				omit3Int = 0;
//			}
//
//			sumOmit123 = omit1Int + omit2Int + omit3Int;
//		} catch (Exception e) {
//			e.printStackTrace(); // 处理异常
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return sumOmit123;
//
//	}

	// 清除所有的数据
//	public int deleteAll() {
//		int count = 0; // 接受返回值
//		// SQL语句
//		String preparedSql = "delete from " + TABLENAME;
//
//		Object[] param = {};
//
//		count = dbHelp.executeSQL(preparedSql, param);
//		return count;
//	}

	//---------------------------------------------------------------------分页开始
//	// 查询数据数据总数
//	public int queryCountTest() {
//		int count = 0;
//		try {
//			conn = dbHelp.getConn(); // 得到数据库连接
//			// SQL语句
//			String preparedSql = "select count(*) totalCount  from "
//					+ TABLENAME;
//			pstmt = conn.prepareStatement(preparedSql); // 得到PreparedStatement对象
//			// pstmt.setInt();
//
//			rs = pstmt.executeQuery(); // 执行sql取得结果集
//
//			while (rs.next()) {
//				count = rs.getInt("totalCount");
//			}
//		} catch (Exception e) {
//			e.printStackTrace(); // 处理异常
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return count;
//	}

	/*
	 * 分页 需要数据：1、数据总数。2、每页显示多少条数据。3、数据总数/每页显示多少条数据=页数。4、PageSize：每页显示多少条数据。
	 * TotalRow:数据总数。pageTotalRow：当前页总数据量。 page:当前页。
	 */

//	/*
//	 * 分页 输入：pageTotalRow：当前页总数据量，PageSize:每页显示多少条数据。
//	 */
//	public List<Lottery> findPageByPageTotalRowAndPageTest(Integer pageTotalRow,Integer pageSize) {
//		List list = new ArrayList(); // 用来保存对象列表
//		try {
//			conn = dbHelp.getConn();
//			// 以下方法分页，在hql是不行的，hql不支持top,要使用sql
//			StringBuffer sbSql = new StringBuffer();
//			sbSql.append("select top" + " " +pageSize + " " + "*");
//			sbSql.append(" " + "from " +TABLENAME);
//			sbSql.append(" " + "where id not in");
//			sbSql.append("(select top " +pageTotalRow);
//			sbSql.append(" " + "id from " +TABLENAME);
//			sbSql.append(" " + "order by id asc) order by id asc");
//			
//			stmt = conn.createStatement(
//					SQLServerResultSet.TYPE_SS_SERVER_CURSOR_FORWARD_ONLY,
//					ResultSet.CONCUR_READ_ONLY);
//			rs = stmt.executeQuery(sbSql.toString()); // 执行sql取得结果集
//
//			/* 循环将回复信息封装成List */
//			while (rs.next()) {
//				Lottery lottery = new Lottery(); // 回复对象
//				lottery.setId(rs.getInt("id"));
//				lottery.setSerialnumber(rs.getString("serialnumber"));
//				lottery.setHundreds(rs.getString("hundreds"));
//				lottery.setDecade(rs.getString("decade"));
//				lottery.setSingledigit(rs.getString("singledigit"));
//
//				lottery.setOmit1record(rs.getString("omit1record"));
//				lottery.setOmit1(rs.getInt("omit1"));
//				lottery.setOmit1state(rs.getInt("omit1state"));
//
//				lottery.setOmit2record(rs.getString("omit2record"));
//				lottery.setOmit2(rs.getInt("omit2"));
//				lottery.setOmit2state(rs.getInt("omit2state"));
//
//				lottery.setOmit3record(rs.getString("omit3record"));
//				lottery.setOmit3(rs.getInt("omit3"));
//				lottery.setOmit3state(rs.getInt("omit3state"));
//
//				lottery.setOmitnewrecord(rs.getString("omitnewrecord"));
//				lottery.setOmitnew(rs.getInt("omitnew"));
//				lottery.setOmitnewstate(rs.getInt("omitnewstate"));
//
//				lottery.setOmitsum(rs.getInt("omitsum"));
//				lottery.setComparecount(rs.getInt("comparecount"));
//				lottery.setDateAndTime(rs.getDate("dateAndTime"));
//				list.add(lottery);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace(); // 处理异常
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return list;
//	}
    //-------------------------------------------------------------------------------------分页结束
	
	// 批量更新传入list
//	public String udapteBatch(List<Lottery> list) throws SQLException,
//			ClassNotFoundException {
//		Connection con = null;
//		PreparedStatement ps = null;
//		int i = 1;
//		String msg = "";
//		try {
//			con = dbHelp.getConn();
//			// 保存当前自动提交模式
//			// boolean autoCommit=con.getAutoCommit();
//			con.setAutoCommit(false);
//			String sql = "update " + TABLENAME + " set "
//					+ "omit1record=?,omit1=?,omit1state=?,"
//					+ "omit2record=?,omit2=?,omit2state=?,"
//					+ "omit3record=?,omit3=?,omit3state=?,"
//					+ "omitnewrecord=?,omitnew=?,omitnewstate=?,"
//					+ "omitsum=? " + " where id=?";
//			ps = con.prepareStatement(sql);
//			// System.out.println(dateAndTime);
//
//			for (i = 0; i < list.size(); i++) {
//				ps.setString(1, list.get(i).getOmit1record());
//				ps.setInt(2, list.get(i).getOmit1());
//				ps.setInt(3, list.get(i).getOmit1state());
//				ps.setString(4, list.get(i).getOmit2record());
//				ps.setInt(5, list.get(i).getOmit2());
//				ps.setInt(6, list.get(i).getOmit2state());
//				ps.setString(7, list.get(i).getOmit3record());
//				ps.setInt(8, list.get(i).getOmit3());
//				ps.setInt(9, list.get(i).getOmit3state());
//				ps.setString(10, list.get(i).getOmitnewrecord());
//				ps.setInt(11, list.get(i).getOmitnew());
//				ps.setInt(12, list.get(i).getOmitnewstate());
//				ps.setInt(13, list.get(i).getOmitsum());
//				ps.setInt(14, list.get(i).getId());
//
//				ps.addBatch();
//				msg = "一共有-" + i + "-条数据对比完成! \n";
//
//				if (i % 20000 == 0) {
//					ps.executeBatch();
//				}
//				ps.executeBatch();
//			}
//			// System.out.println(dateAndTime);
//			con.commit();
//			// con.setAutoCommit(autoCommit);
//			return msg;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			conn.rollback();
//			return null;
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//	}

	
	/*
	 * 释放资源 @param conn 数据库连接 @param pstmt PreparedStatement对象 @param rs 结果集
	 */
//	public void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {
//		// 关闭资源应该从后望前关
//		/* 如果rs不空，关闭rs */
//		if (rs != null) {
//			try {
//				rs.close();
//				// System.err.println("关闭rs");
//			} catch (SQLException e) {
//				// 输出异常
//				e.printStackTrace();
//			}
//			/* 如果pstmt不空，关闭pstmt */
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (Exception e) {
//					// 输出异常
//					e.printStackTrace();
//				}
//			}
//			/* 如果conn不空，关闭conn */
//			if (conn != null) {
//				try {
//					conn.close();
//				} catch (Exception e) {
//					// 输出异常
//					e.printStackTrace();
//				}
//			}
//
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}

	// 根据ID查找数据
	public static void main(String[] args) {
		//ProxyServerDAO lotteryDAO = new ProxyServerDAO();
		// System.out.println("---------------------->:"+lotteryDAO.queryCount());
	}


	


	

}
