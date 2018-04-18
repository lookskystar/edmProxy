package com.edmProxy.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WriteFile {
	//得到数据写入文件
    public static void  getDataWriteFile(String pathFile,String data){
    	 BufferedWriter out = null ;  
         try  {  
             out = new BufferedWriter(new OutputStreamWriter(  
                     new FileOutputStream(pathFile, true )));  
             out.write(data);  
             out.newLine();//换行
         } catch  (Exception e) {  
             e.printStackTrace();  
         } finally  {  
             try  {  
                 out.close();  
             } catch  (IOException e) {  
                 e.printStackTrace();  
             }  
         }  
    }
    //创建文件
    public static boolean createFile(String fileNamePath){ 
    	//System.out.println(filePath);
       	 File file= new File(fileNamePath);
       	 boolean flag=true;
       	 try {
			 if(file.createNewFile()){//文件创建成功
				flag= true;
			 }else if(file.exists()){
				 flag= false; 
			 }
		} catch (IOException e) {
			e.printStackTrace();
		}
         return flag;   
    }
    
   
    
    
    public static void main(String[] args) throws IOException {
    	double t=((double)2/3)*100;
    	int tt=(int)t;
    	System.out.println("\\"+tt);
    	//WriteFile.createFile("D:/test/d.txt");
	}
}
