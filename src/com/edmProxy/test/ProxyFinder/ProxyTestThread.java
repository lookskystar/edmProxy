
/**
 * @author 李晓宇， hmily_yu@hotmail.com
 * ProxyTestThread类为测试代理服务器的线程对象
 */
package com.edmProxy.test.ProxyFinder;

//~--- JDK imports ------------------------------------------------------------

import java.util.Vector;

public class ProxyTestThread implements Runnable {
    private Logable           m_logger;
    private Vector<ProxyDesc> m_proxyList;
    private String            m_url;

    public ProxyTestThread(String url, Vector<ProxyDesc> proxyList, Logable logger) {
        m_url       = url;
        m_proxyList = proxyList;
        m_logger    = logger;
    }

    public void run() {

        // TODO Auto-generated method stub
        ProxyDesc   proxy;
        ProxyTester tester;
        String      name = Thread.currentThread().getName();

        while (m_proxyList.size() > 0) {
            if (m_logger.getFlag() == false) {
                break;
            }

            synchronized (m_proxyList) {
                proxy = m_proxyList.remove(0);
            }

            tester = new ProxyTester(m_url, proxy);

            boolean res = tester.test();

            if (res == true) {
                m_logger.addResult(proxy.ip + ":" + proxy.port + "\t" + proxy.type + "\t" + proxy.speed + "\t"
                                   + proxy.area + "\n");
            }

            m_logger.addLog("<" + name + ">  " + proxy.ip + ":" + proxy.port + "\t[" + res + "]\n");
        }
    }
}
