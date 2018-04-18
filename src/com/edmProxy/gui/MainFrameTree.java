package com.edmProxy.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.edmProxy.constant.Constants;
import com.edmProxy.entity.TreeUserObj;
import com.edmProxy.gui.panel.AccountManagePanel;
import com.edmProxy.gui.panel.ProxyManagePanel;
import com.edmProxy.gui.panel.ReceiveManagePanel;
import com.edmProxy.gui.panel.SendTaskManagePanel;
import com.edmProxy.gui.panel.StatisticsManagePanel;
import com.edmProxy.gui.panel.account.AccountCheckPanel;
import com.edmProxy.gui.panel.account.AccountInsertPanel;
import com.edmProxy.gui.panel.proxy.ProxyCheckPanel;
import com.edmProxy.gui.panel.proxy.ProxyInsertPanel;
import com.edmProxy.gui.panel.receive.ReceiveInsertPanel;
import com.edmProxy.gui.panel.sendTask.SendTaskInsertPanel;
import com.edmProxy.gui.panel.statistics.StatisticsReportPanel;
import com.edmProxy.util.GuiUtil;
import com.incors.plaf.alloy.AlloyLookAndFeel;
import com.incors.plaf.alloy.AlloyTheme;
import com.incors.plaf.alloy.themes.bedouin.BedouinTheme;

public class MainFrameTree implements TreeSelectionListener {
	private JTree tree;
	private ProxyManagePanel proxyHostManagePanel;
	private ProxyCheckPanel proxyServerCheckPanel;
	private ProxyInsertPanel proxyServerInsertPanel;
	private AccountManagePanel accountManagePanel;
	private AccountInsertPanel accountInsertPanel;
	private AccountCheckPanel accountCheckPanel;
	private ReceiveManagePanel receiveManagePanel;
	private ReceiveInsertPanel receiveInsertPanel;
	private SendTaskManagePanel sendTaskManagePanel;
	private SendTaskInsertPanel sendTaskInsertPanel;
	private StatisticsManagePanel statisticsManagePanel;
	private StatisticsReportPanel statisticsReportPanel;

	public MainFrameTree() {
		
	}

	public JTree createTree(final MainFrame source) {
		
		Object[] objManage = { "代理服务管理", "发送账号管理", "收件邮箱管理", "发送任务管理", "统计管理"};
		Object[] objProxyManage = { "代理检测", "添加"};
		Object[] objAccountManage = { "账号检测", "添加"};
		Object[] objReceiveManage = {"添加"};
		Object[] objSendTaskManage = {"添加"};
		Object[] objStatisticsManage = {"图形报表"};
		
		// 创建默认树
		tree = new JTree();
		tree.setLocation(1, 1);
		tree.setSize(135, 610);
		// 首先,创建一个根节点:
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new TreeUserObj(-1,-1+"","EMD系统"));
		// 树下有5个组:
		for (int i =0; i <objManage.length; i++) {
			DefaultMutableTreeNode teamNode = new DefaultMutableTreeNode();
			//System.out.println(""+i);
			TreeUserObj tuoMg=new TreeUserObj(i,""+i,objManage[i].toString());
			teamNode.setUserObject(tuoMg);
			// 将组节点加到根节点上:
			rootNode.add(teamNode);
			TreeUserObj object = (TreeUserObj)teamNode.getUserObject();
			if(i==0){
				for (int j = 0; j < objProxyManage.length; j++) {
					DefaultMutableTreeNode proxyCheckChildNode = new DefaultMutableTreeNode();
					//System.out.println(i+"_"+j);
					TreeUserObj tuoPy=new TreeUserObj(j,i+"_"+j,objProxyManage[j].toString());
					proxyCheckChildNode.setUserObject(tuoPy);
					teamNode.add(proxyCheckChildNode);
				}
			}
			if(i==1){
				for (int j = 0; j < objAccountManage.length; j++) {
					DefaultMutableTreeNode accountCheckChildNode = new DefaultMutableTreeNode();
					//System.out.println(i+"_"+j);
					TreeUserObj tuoPy=new TreeUserObj(j,i+"_"+j,objAccountManage[j].toString());
					accountCheckChildNode.setUserObject(tuoPy);
					teamNode.add(accountCheckChildNode);
				}
			}
			if(i==2){
				for (int j = 0; j < objReceiveManage.length; j++) {
					DefaultMutableTreeNode receiveCheckChildNode = new DefaultMutableTreeNode();
					//System.out.println(i+"_"+j);
					TreeUserObj tuoPy=new TreeUserObj(j,i+"_"+j,objReceiveManage[j].toString());
					receiveCheckChildNode.setUserObject(tuoPy);
					teamNode.add(receiveCheckChildNode);
				}
			}
			if(i==3){
				for (int j = 0; j < objSendTaskManage.length; j++) {
					DefaultMutableTreeNode sendTaskChildNode = new DefaultMutableTreeNode();
					//System.out.println(i+"_"+j);
					TreeUserObj tuoPy=new TreeUserObj(j,i+"_"+j,objSendTaskManage[j].toString());
					sendTaskChildNode.setUserObject(tuoPy);
					teamNode.add(sendTaskChildNode);
				}
			}
			if(i==4){
				for (int j = 0; j < objStatisticsManage.length; j++) {
					DefaultMutableTreeNode statisticsChildNode = new DefaultMutableTreeNode();
					//System.out.println(i+"_"+j);
					TreeUserObj tuoPy=new TreeUserObj(j,i+"_"+j,objStatisticsManage[j].toString());
					statisticsChildNode.setUserObject(tuoPy);
					teamNode.add(statisticsChildNode);
				}
			}
			
			DefaultTreeModel dm = new DefaultTreeModel(rootNode);
			// 将模型设给树,树上显示的将上前面所加载的节点
			tree.setModel(dm);
			// 设定树上的图标
//			 ImageIcon leafIcon = Constants.treeNodeIcon;
//			 DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
//			 renderer.setLeafIcon(leafIcon);
//			 tree.setCellRenderer(renderer);
			 
		}

		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//System.out.println(tree.getLastSelectedPathComponent());
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				DefaultMutableTreeNode selectionNode =
			            (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//得到选中的节点
				
				//System.out.println(">>>"+selectionNode);
				
				//TreePath selPath = tree.getLastSelectedPathComponent();
				//System.out.println(selRow+":"+selPath);
				if(selectionNode!=null){
					TreeUserObj object = (TreeUserObj)selectionNode.getUserObject();
					if (selRow!=-1) {
						//System.out.println(object.getKey());
						String key=object.getKey();
						if("0".equals(key)){
							proxyHostManagePanel = new ProxyManagePanel(source);
							source.getMiddlePanel().removeAll();
							source.getMiddlePanel().add(proxyHostManagePanel);
						}else if("0_0".equals(key)){
							proxyServerCheckPanel=new ProxyCheckPanel();
							source.getMiddlePanel().removeAll();
							source.getMiddlePanel().add(proxyServerCheckPanel);
						}else if("0_1".equals(key)){
							proxyServerInsertPanel = new ProxyInsertPanel();
							source.getMiddlePanel().removeAll();
							source.getMiddlePanel().add(proxyServerInsertPanel);
						}else if("1".equals(key)){
							accountManagePanel = new AccountManagePanel(source);
							source.getMiddlePanel().removeAll();
							source.getMiddlePanel().add(accountManagePanel);
						}else if("1_0".equals(key)){
							accountCheckPanel=new AccountCheckPanel();
							source.getMiddlePanel().removeAll();
							source.getMiddlePanel().add(accountCheckPanel);
						}else if("1_1".equals(key)){
							accountInsertPanel = new AccountInsertPanel();
							source.getMiddlePanel().removeAll();
							source.getMiddlePanel().add(accountInsertPanel);
						}else if("2".equals(key)){
							receiveManagePanel=new ReceiveManagePanel(source); 
							source.getMiddlePanel().removeAll();
							source.getMiddlePanel().add(receiveManagePanel);
						}else if("2_0".equals(key)){
							receiveInsertPanel=new ReceiveInsertPanel();
							source.getMiddlePanel().removeAll();
							source.getMiddlePanel().add(receiveInsertPanel);
						}else if("3".equals(key)){
							sendTaskManagePanel=new SendTaskManagePanel(source);
							source.getMiddlePanel().removeAll();
							source.getMiddlePanel().add(sendTaskManagePanel);
						}else if("3_0".equals(key)){
							sendTaskInsertPanel=new SendTaskInsertPanel(null, null, null, null, false, false, false, false, null, null, false, false, false, false, false);
							source.getMiddlePanel().removeAll();
							source.getMiddlePanel().add(sendTaskInsertPanel);
						}else if("4".equals(key)){
							statisticsManagePanel=new StatisticsManagePanel(source);
							source.getMiddlePanel().removeAll();
							source.getMiddlePanel().add(statisticsManagePanel);
						}else if("4_0".equals(key)){
							statisticsReportPanel=new StatisticsReportPanel();
							source.getMiddlePanel().removeAll();
							source.getMiddlePanel().add(statisticsReportPanel);
						}
						source.refresh(source);
					}
				}
			}
		};
		tree.addMouseListener(ml);
		source.refresh(source);
		return tree;
	}

	public void treeNodesChanged(TreeModelEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("treeNodesChanged");

	}

	public void treeNodesInserted(TreeModelEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("TreeModelEvent");

	}

	public void treeNodesRemoved(TreeModelEvent arg0) {
		System.out.println("treeNodesChanged");
		// TODO Auto-generated method stub

	}

	public void treeStructureChanged(TreeModelEvent arg0) {
		System.out.println("treeNodesChanged");
		// TODO Auto-generated method stub

	}

	public void valueChanged(TreeSelectionEvent arg0) {
		System.out.println("treeNodesChanged"+arg0.getPaths());
		// TODO Auto-generated method stub
		
	}

}
