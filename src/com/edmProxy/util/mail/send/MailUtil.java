package com.edmProxy.util.mail.send;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 *发送邮件工具类
 *1、读取接收邮件地址
 */
public class MailUtil {
	
	//读取接收邮件地址（读取txt文件） 把Txt中的地址读取后放入集合返回
	//这是测试，放到集合中，其实是要放到数据库中的
	public static ArrayList readTxt() throws IOException
	{
		//把str里面的数据按,分组放到集合中，并计算出用户邮箱地址个数
	    ArrayList receiveMailList=new ArrayList();
	    //建立可容纳10000000个字符的数组 
	    char data[]=new char[10000000]; 
	    FileInputStream fileInputStream = new FileInputStream("C:\\2011-10-31x1\\mailAddress.txt");//读取模块文件中华英才简历.txt
	    InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);
	    // 建立对象bufferedReader 
	    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	    String tempStr=""; //存放读取出来的邮件地址数据
	    while ((tempStr = bufferedReader.readLine()) != null)
	    {
	    	//加入邮件地址到集合    
	    	receiveMailList.add(tempStr);
	    }
	    //关闭数据流和读取流
	    fileInputStream.close();
	    bufferedReader.close(); 
	    
	    return receiveMailList;
	}
	//读取模板，根据路径
	 public static String readTemplateMail(String filePath) {
		 String str = "";
         //long beginDate = (new Date()).getTime();
         try {
                 String tempStr = "";
                 FileInputStream is = new FileInputStream(filePath);//读取模块文件
                 BufferedReader br = new BufferedReader(new InputStreamReader(is));
                 while ((tempStr = br.readLine()) != null)
                 str = str + tempStr ;
                 is.close();
         } catch (IOException e) {
                 e.printStackTrace();
                 return str;
         }
         return str;
         
         //以下程序暂时没有用到，得到所有的title，content和editer，开始和结束的时间
         //替换模板中的标题，内容和作者
 /*      try {
          
   str = str.replaceAll("###title###",
       title);
   str = str.replaceAll("###content###",
       context);
   str = str.replaceAll("###author###",
       editer);//替换掉模块中相应的地方
   
               //File f = new File(HtmlFile);
               //BufferedWriter o = new BufferedWriter(new FileWriter(f));
               //o.write(str);
               //o.close();
               System.out.println("共用时：" + ((new Date()).getTime() - beginDate) + "ms");
       } catch (IOException e) {
               e.printStackTrace();
               return false;
       }
	 }
	 */
	 }
}
