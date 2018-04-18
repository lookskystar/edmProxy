package com.edmProxy.test;

import java.io.BufferedReader;     
import java.io.IOException;   
import java.io.InputStreamReader;     
import org.apache.http.HttpEntity;     
import org.apache.http.HttpHost;     
import org.apache.http.HttpResponse;     
import org.apache.http.HttpStatus;   
import org.apache.http.auth.AuthScope;   
import org.apache.http.auth.UsernamePasswordCredentials;   
import org.apache.http.client.ClientProtocolException;   
import org.apache.http.client.CredentialsProvider;   
import org.apache.http.client.HttpClient;   
import org.apache.http.client.methods.HttpGet;     
import org.apache.http.conn.params.ConnRoutePNames;     
import org.apache.http.impl.client.BasicCredentialsProvider;   
import org.apache.http.impl.client.DefaultHttpClient;    
import org.apache.http.util.EntityUtils; 

public class HttpClientTest1 {
    
	  
	    /**  
	     * @param args  
	     * @throws IOException   
	     * @throws ClientProtocolException   
	     */  
	    public static void main(String[] args) throws ClientProtocolException, IOException {   
	        //实例化一个HttpClient   
	        HttpClient httpClient = new DefaultHttpClient();     
	        //设定目标站点   
	        HttpHost httpHost = new HttpHost("www.baidu.com");     
	        //设置代理对象 ip/代理名称,端口   
	        HttpHost proxy = new HttpHost("110.4.12.170", 83);   
	        //实例化验证   
	        CredentialsProvider credsProvider = new BasicCredentialsProvider();   
	        //设定验证内容   
	        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("user001", "123456");   
	        //创建验证   
	        credsProvider.setCredentials(   
	            new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),    
	            creds);   
	        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);   
	        ((DefaultHttpClient)httpClient).setCredentialsProvider(credsProvider);   
	              
	        // 目标地址     
	        HttpGet httpget = new HttpGet("/"); 
	        
	        // 执行     
	        HttpResponse response = httpClient.execute(httpHost, httpget);
	        System.out.println(HttpStatus.SC_OK+":"+response.getStatusLine().getStatusCode());
	        if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){   
	            //请求成功   
	            //取得请求内容   
	            HttpEntity entity = response.getEntity();   
	            //显示内容   
	            if (entity != null) {   
	                // 显示结果   
	                   
	                System.out.println(EntityUtils.toString(entity,"utf-8"));   
	                   
	            }   
	            if (entity != null) {   
	                entity.consumeContent();   
	            }   
	        }   
	    }   
	}  

