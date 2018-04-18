/**
 * @author 李晓宇， hmily_yu@hotmail.com
 * PageParser类通用的HTML页面解析工具类
 */
package com.edmProxy.test.ProxyFinder;

//~--- non-JDK imports --------------------------------------------------------

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.EncodingChangeException;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

//~--- JDK imports ------------------------------------------------------------

import java.util.Vector;

public class PageParser {
    private NodeList m_list;
    private Parser   m_parser;
    private String   m_url;

    public PageParser(String url) {
        this(url, "gb2312");
    }

    public PageParser(String url, String encode) {
        Node currNode;

        m_url    = url;
        m_parser = new Parser();

        try {
            m_parser.setURL(m_url);
            m_parser.setEncoding(encode);

            try {
                m_list = new NodeList();

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
        } catch (ParserException pe) {}
    }

    public Vector<Node> getNodeChildrenByType(Node node, String type) {
        String       currFullName, currName;
        int          idx;
        Node         currNode;
        NodeList     list     = node.getChildren();
        Vector<Node> children = new Vector<Node>();

        for (int i = 0; i < list.size(); i++) {
            currNode     = list.elementAt(i);
            currFullName = currNode.getClass().getName();
            idx          = currFullName.lastIndexOf(".");
            currName     = currFullName.substring(idx + 1);

            if (currName.equals(type)) {
                children.add(currNode);
            }
        }

        return children;
    }

    public NodeList getNodeList() {
        return m_list;
    }

    // 根据绝对路径来取得节点
    // 路径形式为类似"Html(1)->HeadTag(1)->TitleTag(1)->TextNode(1)"
    // 若后面数字为负数，则代表倒数第几个
    public Node getNode(String pathDesc) {
        Node         currNode = null;
        int          offset   = 0;
        int          idxA, idxB;
        NodeList     currList = m_list;
        String       name, currName, currFullName;
        int          count, currCount, idx;
        Vector<Node> nodeVector;

        do {
            idxA       = pathDesc.indexOf("(", offset);
            idxB       = pathDesc.indexOf(")", offset);
            name       = pathDesc.substring(offset, idxA);
            count      = Integer.valueOf(pathDesc.substring(idxA + 1, idxB));
            offset     = idxB + 3;
            currCount  = 0;
            nodeVector = new Vector<Node>();

            if (count >= 0) {
                for (int i = 0; i < currList.size(); i++) {
                    currNode     = currList.elementAt(i);
                    currFullName = currNode.getClass().getName();
                    idx          = currFullName.lastIndexOf(".");
                    currName     = currFullName.substring(idx + 1);

                    if (currName.equals(name)) {
                        currCount++;
                        nodeVector.add(currNode);

                        if (currCount == count) {
                            currList = currNode.getChildren();

                            break;
                        }
                    }
                }

                if (count > nodeVector.size()) {    // 即路径参数有问题，大于存在的该类型节点个数
                    return null;
                }
            } else {
                for (int i = 0; i < currList.size(); i++) {
                    currNode     = currList.elementAt(i);
                    currFullName = currNode.getClass().getName();
                    idx          = currFullName.lastIndexOf(".");
                    currName     = currFullName.substring(idx + 1);

                    if (currName.equals(name)) {
                        currCount++;
                        nodeVector.add(currNode);
                    }
                }

                int nth = nodeVector.size() + count;

                if (nth < 0) {
                    return null;                    // 路径参数有问题，倒数第nth个该类型节点不存在
                }

                currNode = nodeVector.get(nth);
                currList = currNode.getChildren();
            }
        } while (offset < pathDesc.length());

        return currNode;
    }

    // 根据起始节点和相对路径取得节点
    public Node getNode(Node startNode, String relPathDesc) {
        Node         currNode = null;
        int          offset   = 0;
        int          idxA, idxB;
        NodeList     currList = startNode.getChildren();
        String       name, currName, currFullName;
        int          count, currCount, idx;
        Vector<Node> nodeVector;

        do {
            idxA       = relPathDesc.indexOf("(", offset);
            idxB       = relPathDesc.indexOf(")", offset);
            name       = relPathDesc.substring(offset, idxA);
            count      = Integer.valueOf(relPathDesc.substring(idxA + 1, idxB));
            offset     = idxB + 3;
            currCount  = 0;
            nodeVector = new Vector<Node>();

            if (count >= 0) {
                for (int i = 0; i < currList.size(); i++) {
                    currNode     = currList.elementAt(i);
                    currFullName = currNode.getClass().getName();
                    idx          = currFullName.lastIndexOf(".");
                    currName     = currFullName.substring(idx + 1);

                    if (currName.equals(name)) {
                        currCount++;
                        nodeVector.add(currNode);

                        if (currCount == count) {
                            currList = currNode.getChildren();

                            break;
                        }
                    }
                }

                if (count > nodeVector.size()) {    // 即路径参数有问题，大于存在的该类型节点个数
                    return null;
                }
            } else {
                for (int i = 0; i < currList.size(); i++) {
                    currNode     = currList.elementAt(i);
                    currFullName = currNode.getClass().getName();
                    idx          = currFullName.lastIndexOf(".");
                    currName     = currFullName.substring(idx + 1);

                    if (currName.equals(name)) {
                        currCount++;
                        nodeVector.add(currNode);
                    }
                }

                int nth = nodeVector.size() + count;

                if (nth < 0) {
                    return null;                    // 路径参数有问题，倒数第nth个该类型节点不存在
                }

                currNode = nodeVector.get(nth);
                currList = currNode.getChildren();
            }
        } while (offset < relPathDesc.length());

        return currNode;
    }
}
