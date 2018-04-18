
/**
 * @author 李晓宇， hmily_yu@hotmail.com
 * CnProxyPageParser类对www.cnproxy.com的HTML页面进行解析并提取相应信息
 */
package com.edmProxy.test.ProxyFinder;

//~--- non-JDK imports --------------------------------------------------------

import org.htmlparser.nodes.TextNode;

//~--- JDK imports ------------------------------------------------------------

import java.util.Hashtable;
import java.util.Vector;

public class CnProxyPageParser {
    private Hashtable<String, String> cryptMap  = new Hashtable<String, String>(10, 0.7f);
    private Vector<ProxyDesc>         proxyList = new Vector<ProxyDesc>();
    private String                    m_pageUrl;    // 页面URL
    private PageParser                m_parser;

    public CnProxyPageParser(String url) {
        m_pageUrl = url;
        m_parser  = new PageParser(m_pageUrl);
    }

    public Vector<ProxyDesc> getProxyList() {
        try {
            buildCryptArray();
            buildProxyList();
        } catch (PageStructureChangeException psce) {
            System.out.println("page structure may changed.");
        }

        return proxyList;
    }

    private void buildCryptArray() throws PageStructureChangeException {
        String   path     = "Html(1)->HeadTag(1)->ScriptTag(1)->TextNode(1)";
        TextNode node     = (TextNode) m_parser.getNode(path);
        String   cryptStr = node.getText();

        for (int i = 0; i < 10; i++) {
            String postfix = "=\"" + i + "\";";
            int    idx     = cryptStr.indexOf(postfix);

            if (idx <= 0) {
                System.out.println("Crypt info parser error!");

                throw new PageStructureChangeException();
            }

            String ch = cryptStr.substring(idx - 1, idx);

            cryptMap.put(ch, i + "");
        }
    }

    private void buildProxyList() throws PageStructureChangeException {
        String pathPrefix       = "Html(1)->BodyTag(1)->Div(2)->Div(2)->Div(1)->Div(3)->TableTag(3)->TableRow(";
        String ipPathPostfix    = ")->TableColumn(1)->TextNode(1)";
        String portPathPostfix  = ")->TableColumn(1)->ScriptTag(1)->TextNode(1)";
        String typePathPostfix  = ")->TableColumn(2)->TextNode(1)";
        String speedPathPostfix = ")->TableColumn(3)->TextNode(1)";
        String areaPathPostfix  = ")->TableColumn(4)->TextNode(1)";

        for (int i = 2; i <= 101; i++) {
            ProxyDesc desc = new ProxyDesc();

            desc.ip    = getIp(pathPrefix + i + ipPathPostfix);
            desc.port  = getPort(pathPrefix + i + portPathPostfix);
            desc.type  = getType(pathPrefix + i + typePathPostfix);
            desc.speed = getSpeed(pathPrefix + i + speedPathPostfix);
            desc.area  = getArea(pathPrefix + i + areaPathPostfix);
            proxyList.add(desc);
        }
    }

    private String getIp(String path) throws PageStructureChangeException {
        TextNode node = (TextNode) m_parser.getNode(path);

        if (node != null) {
            return node.getText();
        } else {
            throw new PageStructureChangeException();
        }
    }

    private int getPort(String path) throws PageStructureChangeException {
        TextNode node = (TextNode) m_parser.getNode(path);

        if (node != null) {
            String str     = node.getText();
            String portStr = new String();
            int    offset  = 0;
            int    idx;

            while ((idx = str.indexOf('+', offset)) >= 0) {
                String ch  = str.substring(idx + 1, idx + 2);
                String num = cryptMap.get(ch);

                portStr += num;
                offset  = idx + 2;
            }

            return Integer.valueOf(portStr);
        } else {
            throw new PageStructureChangeException();
        }
    }

    private String getType(String path) throws PageStructureChangeException {
        TextNode node = (TextNode) m_parser.getNode(path);

        if (node != null) {
            return node.getText();
        } else {
            throw new PageStructureChangeException();
        }
    }

    private String getSpeed(String path) throws PageStructureChangeException {
        TextNode node = (TextNode) m_parser.getNode(path);

        if (node != null) {
            return node.getText();
        } else {
            throw new PageStructureChangeException();
        }
    }

    private String getArea(String path) throws PageStructureChangeException {
        TextNode node = (TextNode) m_parser.getNode(path);

        if (node != null) {
            return node.getText();
        } else {
            return new String();
        }
    }

    public static void main(String[] args) {
        String            url    = "http://www.cnproxy.com/proxy10.html";
        CnProxyPageParser parser = new CnProxyPageParser(url);
        Vector<ProxyDesc> proxys = parser.getProxyList();

        for (int i = 0; i < proxys.size(); i++) {
            ProxyDesc proxy = proxys.elementAt(i);

            System.out.println(proxy.ip + ":" + proxy.port + "\t" + proxy.type + "\t" + proxy.speed + "\t"
                               + proxy.area);
        }
    }
}
