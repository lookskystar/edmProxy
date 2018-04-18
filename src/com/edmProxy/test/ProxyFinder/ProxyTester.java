
/**
 * @author 李晓宇， hmily_yu@hotmail.com
 * ProxyTester检测代理服务器是否可以连通指定网页
 */
package com.edmProxy.test.ProxyFinder;

//~--- non-JDK imports --------------------------------------------------------

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.EncodingChangeException;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

//~--- JDK imports ------------------------------------------------------------

import java.net.URL;
import java.net.URLConnection;

public class ProxyTester {
    private String   ip;
    private NodeList m_list;
    private Parser   m_parser;
    private String   m_url;
    private int      port;

    public ProxyTester(String url, ProxyDesc proxy) {
        m_url = url;
        ip    = proxy.ip;
        port  = proxy.port;
    }

    public boolean test() {

         System.getProperties().put("proxySet","true");
         System.getProperties().put("proxyHost",ip);
         System.getProperties().put("proxyPort",port+"");

        /*
         * ConnectionManager manager=new ConnectionManager();
         * manager.setProxyHost(ip);
         * manager.setProxyPort(port);
         *
         * m_parser=new Parser();
         * m_parser.setConnectionManager(manager);
         */
        URLConnection conn = null;

        try {
            URL siteUrl = new URL("http", ip, port, m_url);

            conn = siteUrl.openConnection();
        } catch (Exception ee) {
            System.out.println("url or proxy not right.");

            return false;
        }

        try {
            m_parser = new Parser(conn);

            Lexer  lex  = m_parser.getLexer();
            String type = lex.getPage().getContentType();

            if ((type != null) &&!type.startsWith("text")) {
                return false;
            }

            m_list = new NodeList();

            Node currNode;

            try {
                for (NodeIterator ni = m_parser.elements(); ni.hasMoreNodes(); ) {
                    currNode = ni.nextNode();
                    m_list.add(currNode);
                }
            } catch (EncodingChangeException ece) {
                m_parser.reset();
                m_list = new NodeList();

                for (NodeIterator ni = m_parser.elements(); ni.hasMoreNodes(); ) {
                    currNode = ni.nextNode();
                    m_list.add(currNode);
                }
            }
        } catch (ParserException pe) {
            return false;
        } catch (Exception ee) {
            return false;
        }

        if (m_list.size() > 0) {
            Node rootNode = m_list.elementAt(0);

            // System.out.println(rootNode.toHtml());
            // System.out.println(rootNode.toPlainTextString());
            String plainText = rootNode.toPlainTextString();

            if (plainText.length() > 0) {
                if (plainText.indexOf("404 Not Found") < 0) {

                    // System.out.println(rootNode.toHtml());
                    return true;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        ProxyDesc proxy = new ProxyDesc();

        proxy.ip   = "183.234.16.37";
        proxy.port = 80;

        ProxyTester tester = new ProxyTester("http://baidu.com", proxy);

        System.out.println("url test:" + tester.test());
    }
}
