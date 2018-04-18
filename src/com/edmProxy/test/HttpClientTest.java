package com.edmProxy.test;

import java.io.IOException;  
import org.apache.commons.httpclient.*;  
import org.apache.commons.httpclient.methods.GetMethod;  
import org.apache.commons.httpclient.params.HttpMethodParams;  
public class HttpClientTest{  
   public static void main(String[] args) {  
   //HttpClient 
   HttpClient httpClient = new HttpClient();  
   //GET
    GetMethod getMethod = new GetMethod("http://iframe.ip138.com/ic.asp");  
   //  
    getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,  
     new DefaultHttpMethodRetryHandler());  
   try {  
    //getMethod  
    //int statusCode = httpClient.executeMethod(getMethod);  
    //proxy ip¡¡¡Á¡Á¡Á.¡Á¡Á¡Á.¡Á¡Á¡Á.¡Á¡Á¡Á
    httpClient.getHostConfiguration().setProxy("23.104.72.2", 808);
    int statusCode = httpClient.executeMethod(getMethod);
    
    if (statusCode != HttpStatus.SC_OK) {  
      System.err.println("Method failed: "  
        + getMethod.getStatusLine());  
     }  
    //  InputStream 
    //InputStream responseBody = getMethod.getResponseBodyAsStream();
    //System.out.println(responseBody);  
    //byte
    //byte[] responseBody = getMethod.getResponseBody();
    //System.out.println(new String(responseBody));
    //string
    byte[] responseBody = getMethod.getResponseBody(); 
    String newresponseBody = new String(responseBody, "GBK");
    URI urll= getMethod.getURI();
    System.out.println(newresponseBody);
    System.out.println(urll);
    } catch (HttpException e) {  
    //
     System.out.println("Please check your provided http address!");  
     e.printStackTrace();  
    } catch (IOException e) {  
    //
     e.printStackTrace();  
    } finally {  
    //
     getMethod.releaseConnection();  
    }  
 }  
 }
