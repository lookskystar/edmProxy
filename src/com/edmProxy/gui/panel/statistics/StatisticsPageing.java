package com.edmProxy.gui.panel.statistics;

import java.util.ArrayList;

import com.edmProxy.dao.AccountDAO;
import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.dao.ReceiveDAO;
import com.edmProxy.dao.StatisticsDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.ReceiveEntity;
import com.edmProxy.entity.SendTaskStatisticsObj;
import com.edmProxy.entity.StatisticsEntity;

//分页 每页20条数据
public class StatisticsPageing {
	private StatisticsDAO statisticsDAO;
	//计算总页数 pageSize：页面大小
	public Integer findTotalPageByCondition(Integer pageSize,String condition){
		System.out.println(condition);
		Integer totalRow=getTotalRowByCondition(condition); //总数据
		Integer pages=0; //总页数
		//求模运算，如果总页数不是整数，则加1
		if(totalRow!=0){
			if(totalRow%pageSize==0){
				pages=totalRow/pageSize;
			}else{
				pages=totalRow/pageSize+1;
			}
		}
		return pages;
	}
	
	//当前页总数据量,传入：page：当前页。pageSize：页面大小
	public Integer findPageTotalRow(Integer pageSize,Integer page){
		Integer pageTotalRow=(page-1)*pageSize;
		return pageTotalRow;
	}
	//得到总数据
	public Integer getTotalRowByCondition(String condition){
		statisticsDAO=new StatisticsDAO();
		Integer totalRow=statisticsDAO.queryCountByCondition(condition); //总数据
		return totalRow;
	}	
	
	//分页 传入pageSize:页面大小，pageNumber：当前页数
	public ArrayList<StatisticsEntity> pageingBypageNowAndpageSizeByCondition(int pageNow,int pageSize,String condition){
		statisticsDAO=new StatisticsDAO();
		return statisticsDAO.pageingBypageNowAndpageSizeByCondition(pageNow, pageSize,condition);
	}
	
	
	//分页 传入pageSize:页面大小，pageNumber：当前页数 得到统计和发送任务实体类封装类SendTaskStatisticsObj
	public ArrayList<SendTaskStatisticsObj> pageingBypageNowAndpageSizeObjByCondition(int pageNow,int pageSize,String condition){
		statisticsDAO=new StatisticsDAO();
		return statisticsDAO.pageingBypageNowAndpageSizeObjByCondition(pageNow, pageSize,condition);
	}
	
	public static void main(String[] args) {
		ArrayList<StatisticsEntity> list=new ArrayList<StatisticsEntity>();
		StatisticsPageing p=new StatisticsPageing();
		list=p.pageingBypageNowAndpageSizeByCondition(2, 20,"");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getId());
		}
		
		
	}
}
