package com.edmProxy.util.mail.send;

import java.util.ArrayList;



//分组换ip账号发送邮件 //一次发送一条，以接收邮箱为主
//先调用mailListGroup()分组，在把分组的后的list传入mailGroupSend调用SendMail发送
public class ReceiveGroup {
public static void main(String[] args) {
//		GroupSendMail t=new GroupSendMail();
//		ArrayList<ArrayList> mailGroupList=new ArrayList();
//		ArrayList<SendHostMailInfo> sendHostMailInfoListT=new ArrayList<SendHostMailInfo>();
//		TestEntity1 testEntity1=new TestEntity1();
//		testEntity1.crateEntity();
//		ArrayList emailList=new ArrayList();
//		for(int i=0;i<50;i++){
//			emailList.add("huangliang@tarena.com.cn");
//		}
//		mailGroupList=t.mailListGroup(emailList,10,3);
//		
//		
//		sendHostMailInfoListT=testEntity1.sendHostMailInfoList;
//		//System.out.println("sendHostMailInfoListT->"+sendHostMailInfoListT.size());
//		
//		t.mailGroupSend(mailGroupList,sendHostMailInfoListT);
//		//System.out.println("-------------------------------------");
	}
	
	
	
	
	
	
	//分组
	//list 收信邮箱总数 whileSendCountChangeIPNum换IP发送数 分组,每一组一个IP    ip总数
	public  ArrayList<ArrayList> receiveGroup(ArrayList receiveList,int accountSendNum){
		ArrayList<ArrayList> receiveGroupList=new ArrayList(); //存放list，每个list为一组，里面放发送地址
		
		int count=0;//存放轮换IP次数，也是分组个数
		int receiveSum=receiveList.size();
		count=receiveSum/accountSendNum;
		//System.out.println("count-->"+count);
		
		//new 3个集合，赋值后存放sumCountList中
//		if(count<=sumAccount){//分组数<=ip数
			System.out.println(">---->分组提取数据结束，开始发送......");
			receiveGroupList=handReceiveGroupList(receiveList,accountSendNum);
//		}else{//分组数>ip数 根据ip地址个数自动分组
//			System.out.println("自动");
//			receiveGroupList=autoReceiveGroupList(receiveList,sumAccount);
//		}

		//测试
//		for (int i = 0; i < mailGroupList.size(); i++) {
//			System.out.println("第"+i+"个IP，-->"+mailGroupList.get(i).size());
//		}
		return receiveGroupList;
	}
	//根据条件（换IP发送数）分组 
	private ArrayList<ArrayList> handReceiveGroupList(ArrayList<String> receiveList,int accountSendNum){
		ArrayList<ArrayList> receiveGroupList=new ArrayList(); //存放list，每个list为一组，里面放发送地址
		int count=0;//存放轮换IP次数，也是分组个数
		int receiveSum=receiveList.size();
		int mode=receiveSum%accountSendNum;
		count=receiveSum/accountSendNum;
		for(int i=1;i<=count;i++){
			ArrayList list=new ArrayList();//组集合
			int n=(i-1)*accountSendNum;
			int m=i*accountSendNum;//-difference;
			int j=0;
			for(j=n;j<m;j++){
				list.add(receiveList.get(j));
				//把最后余数加到最后一组中	
				if(j==count*accountSendNum-1){
					//System.out.println("j->"+j+"==count*whileSendCountChangeIPNum->"+count*whileSendCountChangeIPNum);
					j=j+1; //因为集合的下标是从0开始，而0又不适合计算，所以这里j+1，
					for (int k = j; k < receiveList.size(); k++) {
						list.add(receiveList.get(k));
					}
				}
			}
			receiveGroupList.add(list);
		}
		return receiveGroupList;
	}
	
	//自动分组 分组数>ip数 根据ip地址个数自动分组
	private ArrayList<ArrayList> autoReceiveGroupList(ArrayList receiveList,int sumAccount){
		ArrayList<ArrayList> receiveGroupList=new ArrayList(); //存放list，每个list为一组，里面放发送地址
		int receiveSum=receiveList.size();
		int groupNum=0;//每组数据数,不包含计算所得余数，余数加到最后一组
		groupNum=receiveSum/sumAccount;
		int mode=receiveSum%sumAccount; 
//		System.out.println("groupNum-->"+groupNum);
//		System.out.println("mode-->"+mode);
		//count=mailSum/sumIp;
		for(int i=1;i<=sumAccount;i++){
			ArrayList list=new ArrayList();//组集合
			
			int n=(i-1)*groupNum;
			int m=i*groupNum;
			for(int j=n;j<m;j++){
				list.add(receiveList.get(j));
				//System.out.println("第"+i+"组,第"+j+"条数据");
				//System.out.println(j+":"+groupNum);
				if(j==groupNum*sumAccount-1){
					//System.out.println(j+":"+(groupNum*sumIp-1));
					j=j+1; //因为集合的下标是从0开始，而0又不适合计算，所以这里j+1，
					for (int k = j; k < receiveList.size(); k++) {
						//System.out.println("k->"+k);
						list.add(receiveList.get(k));
					}
				}
			}
			receiveGroupList.add(list);
		}
		//测试
//		for(int i=0;i<mailGroupList.size();i++){
//			System.out.println("mailGroupList.size()-->"+mailGroupList.get(i).size());
//		}
		return receiveGroupList;
	}

	
}
