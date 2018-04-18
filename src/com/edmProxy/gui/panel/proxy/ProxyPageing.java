package com.edmProxy.gui.panel.proxy;

import java.util.ArrayList;

import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.entity.ProxyEntity;

//分页 每页20条数据
public class ProxyPageing {
	private ProxyDAO proxyServerDAO;
	//计算总页数 pageSize：页面大小
	public Integer findTotalPage(Integer pageSize){
		proxyServerDAO=new ProxyDAO();
		Integer totalRow=getTotalRow(); //总数据
		Integer pages=0; //总页数
		//求模运算，如果总页数不是整数，则加1
		if(totalRow%pageSize==0){
			pages=totalRow/pageSize;
		}else{
			pages=totalRow/pageSize+1;
		}
		return pages;
	}
	
	//当前页总数据量,传入：page：当前页。pageSize：页面大小
	public Integer findPageTotalRow(Integer pageSize,Integer page){
		Integer pageTotalRow=(page-1)*pageSize;
		return pageTotalRow;
	}
	//得到总数据
	public Integer getTotalRow(){
		Integer totalRow=proxyServerDAO.queryCount(); //总数据
		return totalRow;
	}	
	
	//分页 传入pageSize:页面大小，pageNumber：当前页数  
	public ArrayList<ProxyEntity> pageingBypageNowAndpageSize(int pageNow,int pageSize){
		proxyServerDAO=new ProxyDAO();
		return proxyServerDAO.pageingBypageNowAndpageSize(pageNow, pageSize);
	}
	
	public static void main(String[] args) {
		ArrayList<ProxyEntity> list=new ArrayList<ProxyEntity>();
		ProxyPageing p=new ProxyPageing();
		list=p.pageingBypageNowAndpageSize(2, 20);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getId());
		}
		
		
	}
}
