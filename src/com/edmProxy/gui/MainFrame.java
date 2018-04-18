package com.edmProxy.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.SystemColor;
import javax.swing.JToolBar;
import javax.swing.JToggleButton;
import javax.swing.JButton;
import javax.swing.SwingConstants;


import com.edmProxy.constant.Constants;
import com.edmProxy.gui.panel.AccountManagePanel;
import com.edmProxy.gui.panel.ProxyManagePanel;
import com.edmProxy.gui.panel.ReceiveManagePanel;




import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JTree;
import javax.swing.JScrollPane;

public class MainFrame extends BaseFrame {
	
	private static final long serialVersionUID = 1L;

	private JPanel contentPanel;
	private JPanel mainPanel;
	private JMenuItem helpMenuItem;
	//private JTree tree;
	
	private JPanel middlePanel;
	private JPanel treePanel;
	private MainFrameTree createTree;
	
	private ProxyManagePanel proxyHostManagePanel;
	private AccountManagePanel accountManagePanel;
	private ReceiveManagePanel receiveManagePanel; 
	
	
	public ProxyManagePanel getProxyHostManagePanel() {
		return proxyHostManagePanel;
	}
	public void setProxyHostManagePanel(ProxyManagePanel proxyHostManagePanel) {
		this.proxyHostManagePanel = proxyHostManagePanel;
	}
	public AccountManagePanel getAccountManagePanel() {
		return accountManagePanel;
	}
	public void setAccountManagePanel(AccountManagePanel accountManagePanel) {
		this.accountManagePanel = accountManagePanel;
	}
	public JPanel getMiddlePanel() {
		return middlePanel;
	}
	public void setMiddlePanel(JPanel middlePanel) {
		this.middlePanel = middlePanel;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public MainFrame() {
		this.setTitle("EDM系统  v1.0 --主窗体");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 1024, 690);
		contentPanel = new JPanel();
		contentPanel.setBorder(null);
		setContentPane(contentPanel);
		contentPanel.setLayout(null);
		
		mainPanel = new JPanel();
		mainPanel.setBorder(null);
		mainPanel.setBounds(0, 0, 1016, 663);
		contentPanel.add(mainPanel);
		//初始化
		init();
		close(this);//关闭窗体提示
	}
	//菜单
	public void init(){
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setBounds(0, 0, 1016, 25);
		menuBar.setFont(new Font("SimSun", Font.BOLD, 12));
		menuBar.setBackground(new Color(0, 153, 255));
		//创建菜单选项
		JMenu toolMenu = new JMenu("工具（T）");
		toolMenu.setBackground(SystemColor.inactiveCaptionBorder);
		JMenu setingMenu = new JMenu("设置（C）");
		JMenu helpMenu = new JMenu("帮助（H）");
		//设置热键  
		toolMenu.setMnemonic(KeyEvent.VK_T);
		setingMenu.setMnemonic(KeyEvent.VK_C);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		//把menu加入menuBar
		menuBar.add(toolMenu);  
		menuBar.add(setingMenu);
		menuBar.add(helpMenu);
		//工具下拉
//		JMenuItem proxyCheckTidyToolMenuItem = new JMenuItem("代理服务器检测（P）", KeyEvent.VK_P);
//		JMenuItem accountCheckTidyToolMenuItem = new JMenuItem("发送账号检测（A）", KeyEvent.VK_A);
//		JMenuItem emailTidyToolMenuItem = new JMenuItem("收件邮箱整理（F）", KeyEvent.VK_F);
		JMenuItem backupsDataToolMenuItem = new JMenuItem("数据备份（D）", KeyEvent.VK_D);
		//设置下拉
		JMenuItem updatePwdSetMenuItem = new JMenuItem("修改用户密码（U）", KeyEvent.VK_U);
		JMenuItem traySetMenuItem = new JMenuItem("最小到托盘（M）", KeyEvent.VK_M);
		//帮助下拉
		helpMenuItem = new JMenuItem("帮助文件（H）", KeyEvent.VK_H);
		JMenuItem aboutHelpMenuItem = new JMenuItem("关于软件（A）", KeyEvent.VK_A);
		//把MenuItem加入Menu
//		toolMenu.add(proxyCheckTidyToolMenuItem);
//		toolMenu.add(accountCheckTidyToolMenuItem);
//		toolMenu.add(emailTidyToolMenuItem);
		toolMenu.add(backupsDataToolMenuItem);
		setingMenu.add(updatePwdSetMenuItem);
		setingMenu.add(traySetMenuItem);
		helpMenu.add(helpMenuItem);
		helpMenu.add(aboutHelpMenuItem);
		
		//菜单动作
		//邮件地址整理
//		emailTidyToolMenuItem.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				
//			}
//		});
		mainPanel.setLayout(null);
		mainPanel.add(menuBar);
		
		middlePanel= new JPanel();
//		{
//			public void paintComponent(Graphics g){
//				g.drawImage(new ImageIcon("img/bg.jpg").getImage(),0,0,null);
//			}		
//		};
		middlePanel.setBorder(new LineBorder(SystemColor.desktop));
		middlePanel.setBackground(SystemColor.activeCaptionBorder);
		middlePanel.setBounds(137, 26, 880, 612);
		middlePanel.setLayout(null);
		//设置发件信箱
//		setSendMailPanel.setBounds(0, 0, 852, 529);
//		contentPanel.add(setSendMailPanel);
//		setSendMailPanel.setLayout(null);
		
	    //设置邮件内容
		/*
		setMailConetentPanel.setBounds(0, 0, 852, 529);
		contentPanel.add(setMailConetentPanel);
		setMailConetentPanel.setLayout(null);
		*/
		//设置收信邮件
		/*
		setReceiveMailPanel.setBounds(0, 0, 852, 529);
		contentPanel.add(setReceiveMailPanel);
		setReceiveMailPanel.setLayout(null);
		*/
	
		mainPanel.add(middlePanel);
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(null);
		bottomPanel.setBackground(new Color(51, 153, 255));
		bottomPanel.setBounds(0, 638, 1016, 25);
		mainPanel.add(bottomPanel);
		
		treePanel = new JPanel();
		treePanel.setBorder(new LineBorder(SystemColor.desktop));
		treePanel.setBounds(0, 26, 137, 612);
		mainPanel.add(treePanel);
		
		createTree=new MainFrameTree();
		JTree tree = createTree.createTree(MainFrame.this);
		//treePanel.add(tree);
		treePanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(1, 1, 135, 610);
		treePanel.add(scrollPane);
		scrollPane.setViewportView(tree);
	}
}
