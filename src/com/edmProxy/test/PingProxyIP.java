package com.edmProxy.test;

	import java.awt.Toolkit;
	import java.io.InputStream;
	import java.net.HttpURLConnection;
	import java.net.URL;
	/**
	 * @Description:本类开启一个线程检测网络是否连通,只能测试网站，不能测试IP是否通
	 */
	public class PingProxyIP implements Runnable {
	    private int htmlCodeSize;
	    private int sleepMillisecond;
	    private int sleepMillisecondWhenNetWorkUnLinked;
	    private boolean isSpontaneousNotice;
	    private static boolean networkIsLinked;
	    private Thread thread = new Thread(this);
	    private Toolkit toolkit;
	    private String[] urls;

	    public PingProxyIP() {
	        this.urls = new String[]{"http://www.baidu.com", "http://www.google.cn"};
	        this.htmlCodeSize = 50;
	        this.sleepMillisecond = 5000;
	        this.sleepMillisecondWhenNetWorkUnLinked = 10000;
	        this.toolkit = Toolkit.getDefaultToolkit();
	        thread.start();
	    }
	    public void setURLs(String[] urls) {
	        if (urls != null && urls.length > 0) {
	            this.urls = urls;
	        }
	    }
	    public void setHtmlCodeSize(int htmlCodeSize) {
	        if (htmlCodeSize > 0) {
	            this.htmlCodeSize = htmlCodeSize;
	        }
	    }
	    public void isSpontaneousNotice(boolean isSpontaneousNotice) {
	        this.isSpontaneousNotice = isSpontaneousNotice;
	    }
	    public void setSleepMillisecont(int sleepMillisecont) {
	        if (sleepMillisecont > 100) {
	            this.sleepMillisecond = sleepMillisecont;
	        }
	    }
	    public void setSleepMillisecondWhenNetWorkUnLinked(int sleepMillisecont) {
	        if (sleepMillisecont > 100) {
	            this.sleepMillisecondWhenNetWorkUnLinked = sleepMillisecont;
	        }
	    }
	    public static boolean IsNetWordLinking() {
	        return PingProxyIP.networkIsLinked;
	    }

	    public void run() {
	        while (true) {
	            try {
	                this.isNetWorkLinked();
	                if (!PingProxyIP.networkIsLinked) {
	                    this.isPrintMessage(this.isSpontaneousNotice);
	                    Thread.sleep(this.sleepMillisecondWhenNetWorkUnLinked);
	                }
	                System.out.println(PingProxyIP.IsNetWordLinking());
	                Thread.sleep(this.sleepMillisecond);
	            } catch (Exception e) {
	            }
	        }
	    }

	    private boolean canGetHtmlCode(String httpUrl) {
	        String htmlCode = "";
	        try {
	            InputStream in;
	            URL url = new java.net.URL(httpUrl);
	            HttpURLConnection connection = (HttpURLConnection) url
	                    .openConnection();
	            connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestProperty("User-Agent", "Mozilla/4.0");
	            connection.connect();
	            in = connection.getInputStream();
	            byte[] buffer = new byte[this.htmlCodeSize];
	            in.read(buffer);
	            htmlCode = new String(buffer);
	        } catch (Exception e) {
	        }
	        if (htmlCode == null || htmlCode.equals("")) {
	            return false;
	        }
	        return true;
	    }

	    private void isNetWorkLinked() {
	        boolean tempIsNetWorkLinked = false;
	        for (int urlsCount = 0; urlsCount < this.urls.length; urlsCount++) {
	            if (this.canGetHtmlCode(this.urls[urlsCount])) {
	                tempIsNetWorkLinked = true;
	                break;
	            }
	        }
	        PingProxyIP.networkIsLinked = tempIsNetWorkLinked;
	    }
	    private void isPrintMessage(boolean isPrint) {
	        if (isPrint) {
	            toolkit.beep();
	            StringBuffer message = new StringBuffer();
	            message.append("------------->");
	            message.append("网络中断, ");
	            message.append(this.sleepMillisecondWhenNetWorkUnLinked);
	            message.append(" 毫秒后再次检测!<-------------");
	            System.out.println(message.toString());
	        }

	    }
	    public static void main(String[] args) {
	    	PingProxyIP n = new PingProxyIP();
	        n.isSpontaneousNotice(true);
	    }
	}
