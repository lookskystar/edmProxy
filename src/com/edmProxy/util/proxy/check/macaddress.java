package com.edmProxy.util.proxy.check;
import   java.io.InputStreamReader;  
import   java.io.BufferedReader;

public   class   macaddress   {  
    public   static   void   main(String[]   args)   {  
        macaddress   mdd   =   new   macaddress();  
        String   str=mdd.getMacOnWindow();  
        System.out.println("---"+str);  
    }  
    private   static   String   getMacOnWindow()   {  
                String   s   =   "";  
                try   {  
                        String   s1   =   "ipconfig /all";  
                        Process   process   =   Runtime.getRuntime().exec(s1);  
                        BufferedReader   bufferedreader   =   new   BufferedReader(  
                                        new   InputStreamReader(process.getInputStream()));  
                        String   nextLine;  
                        for   (String   line   =   bufferedreader.readLine();  
                        		line   !=   null;   line   =   nextLine)   {  
                        	System.out.println(line);
                                nextLine   =   bufferedreader.readLine();  
                                if   (line.indexOf("Physical Address")   <=   0)   {  
                                        continue;  
                                }  
                                int   i   =   line.indexOf("Physical Address")   +   36;  
                                s   =   line.substring(i);  
                                break;  
                        }  
 
                        bufferedreader.close();  
                        process.waitFor();  
                }   catch   (Exception   e)   {  
                        s   =   "";  
                        e.printStackTrace();
                }  
                return   s.trim();  
        }  
 
}
