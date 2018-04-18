package com.edmProxy.gui.panel.receive;

import java.util.ArrayList;

import com.edmProxy.dao.AccountDAO;
import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.dao.ReceiveDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.ReceiveEntity;

//分页 每页20条数据
public class ReceivePageing {
	private ReceiveDAO receiveDAO;
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
		receiveDAO=new ReceiveDAO();
		Integer totalRow=receiveDAO.queryCountByCondition(condition); //总数据
		return totalRow;
	}	
	
	//分页 传入pageSize:页面大小，pageNumber：当前页数
	public ArrayList<ReceiveEntity> pageingBypageNowAndpageSizeByCondition(int pageNow,int pageSize,String condition){
		receiveDAO=new ReceiveDAO();
		return receiveDAO.pageingBypageNowAndpageSizeByCondition(pageNow, pageSize,condition);
	}
	
	public static void main(String[] args) {
		ArrayList<ReceiveEntity> list=new ArrayList<ReceiveEntity>();
		ReceivePageing p=new ReceivePageing();
		list=p.pageingBypageNowAndpageSizeByCondition(2, 20,"");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getId());
		}
		
		
	}
}
