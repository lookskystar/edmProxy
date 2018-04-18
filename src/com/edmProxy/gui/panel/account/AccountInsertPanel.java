package com.edmProxy.gui.panel.account;

import java.awt.List;
import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JRadioButton;

import com.edmProxy.dao.AccountDAO;
import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.TestEntiy;
import com.edmProxy.util.CheckParameterUtil;
import com.edmProxy.util.JFileChooserFileFilter;
import com.edmProxy.util.ReadFile;
import com.edmProxy.util.proxy.check.ProxyCheck;


import javax.swing.JTextPane;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
//代理添加
public class AccountInsertPanel extends JPanel {
	private JTextField filePathTextField;

	private AccountEntity accountEntity;
	private JTextField accountTextField;
	private JTextField passwordTextField;
	
	private static ButtonGroup validBtnBg = new ButtonGroup();
	private static ButtonGroup startBtnBg = new ButtonGroup();
	private JRadioButton noStartRdb;
	private JRadioButton startRdb;
	private JRadioButton validRdb;
	private JRadioButton noValidRdb;
	
	private AccountDAO  accountDAO;
	private JTextPane remarkTextPane;
	private JFileChooser jFileChooser;
	private ArrayList<String> accoutStrList;
	private ArrayList<AccountEntity> accoutList;
	private JTextField postTextField;
	private JButton saveButton;

	public AccountInsertPanel() {
		setBorder(new LineBorder(SystemColor.desktop));
		setBackground(SystemColor.activeCaptionBorder);
		this.setBounds(0, 0, 880, 612);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(SystemColor.desktop));
		panel.setBounds(10, 10, 860, 592);
		add(panel);
		panel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(SystemColor.desktop));
		panel_2.setBounds(10, 10, 840, 572);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 842, 571);
		panel_2.add(tabbedPane);
		
		JPanel batchPanel = new JPanel();
		tabbedPane.addTab("\u6279\u91CF\u6DFB\u52A0", null, batchPanel, null);
		batchPanel.setLayout(null);
		
		JLabel label = new JLabel("\u8BF7\u9009\u62E9\u6587\u4EF6");
		label.setBounds(10, 10, 75, 15);
		batchPanel.add(label);
		
		filePathTextField = new JTextField();
		filePathTextField.setBounds(72, 10, 182, 21);
		batchPanel.add(filePathTextField);
		filePathTextField.setColumns(10);
		
		JButton selectButton = new JButton("\u9009\u62E9");
		//选择
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jFileChooser = new JFileChooser();
				// jFileChooser.setCurrentDirectory(new File("e://"));//设置默认目录
				// 打开直接默认E盘
				JFileChooserFileFilter filter = new JFileChooserFileFilter();
				jFileChooser.setFileFilter(filter);
				int result = jFileChooser
						.showOpenDialog(AccountInsertPanel.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile();
					filePathTextField.setText(file.getPath());
				}
			}
		});
		selectButton.setBounds(260, 6, 95, 25);
		batchPanel.add(selectButton);
		
		JButton insertBatchButton = new JButton("\u6279\u91CF\u5BFC\u5165");
		//批量导入
		insertBatchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String filePath=filePathTextField.getText();
				if("".equals(filePath)){
					JOptionPane.showMessageDialog(AccountInsertPanel.this,
							"选择不能为空！", "系统提示", JOptionPane.INFORMATION_MESSAGE);
				}else{
					try {
						accoutStrList=new ArrayList<String>();
						accoutList=new ArrayList<AccountEntity>();
						accoutStrList=ReadFile.ReadTxt(filePath);
						//dtnilans@sina.com-sayler8390
						//System.out.println("accoutStrList="+accoutStrList.size());
						int j=0;
						String msg="";
						if(accoutStrList.size()>-1){
							for (int i = 0; i < accoutStrList.size(); i++) {
								String arr[]=accoutStrList.get(i).split("-");
								AccountEntity accountEntity=new AccountEntity();
								if(arr.length>-1){
									if(CheckParameterUtil.checkEmail(arr[0])){
										accountEntity.setAccount(arr[0]);
										System.out.println(arr[0]);
										accountEntity.setPassword(arr[1]);
										accountEntity.setPost(arr[0].substring(arr[0].indexOf("@")+1,arr[0].length()));
										accoutList.add(accountEntity);
									}else{
										j++;
									}
								}
							}
							accountDAO=new AccountDAO();
							try {
								msg=accountDAO.InsertBatchNotRepetitionByAccount(accoutList);
								msg+="，入库前，服务器地址错误"+j+"个";
							} catch (SQLException e) {
								JOptionPane.showMessageDialog(
										AccountInsertPanel.this,
										"批量插入错误SQL", "系统提示",
										JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								JOptionPane.showMessageDialog(
										AccountInsertPanel.this,
										"批量插入错误ClassNotFound", "系统提示",
										JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}
							JOptionPane.showMessageDialog(
									AccountInsertPanel.this,
									msg, "系统提示",
									JOptionPane.INFORMATION_MESSAGE);
							reset();
							return;
						}
					} catch (IOException e) {
						JOptionPane.showMessageDialog(
								AccountInsertPanel.this, "读取文件错误！",
								"系统提示", JOptionPane.INFORMATION_MESSAGE);
						e.printStackTrace();
					}
				}
			}
		});
		insertBatchButton.setBounds(260, 37, 95, 25);
		batchPanel.add(insertBatchButton);
		
		JPanel SinglePanel = new JPanel();
		tabbedPane.addTab("\u5355\u4E2A\u6DFB\u52A0", null, SinglePanel, null);
		SinglePanel.setLayout(null);
		
		JLabel label_3 = new JLabel("\u8D26    \u53F7");
		label_3.setBounds(94, 98, 85, 15);
		SinglePanel.add(label_3);
		
		JLabel label_4 = new JLabel("\u5BC6    \u7801");
		label_4.setBounds(93, 132, 54, 15);
		SinglePanel.add(label_4);
		
		JLabel label_5 = new JLabel("\u662F\u5426\u6709\u6548");
		label_5.setBounds(93, 168, 54, 15);
		SinglePanel.add(label_5);
		
		JLabel label_6 = new JLabel("\u662F\u5426\u5F00\u542F");
		label_6.setBounds(93, 208, 54, 15);
		SinglePanel.add(label_6);
		
		JLabel lblNewLabel = new JLabel("\u5907    \u6CE8");
		lblNewLabel.setBounds(93, 297, 54, 15);
		SinglePanel.add(lblNewLabel);
		
		accountTextField = new JTextField();
		//当失去焦点，自动输入邮局事件
		accountTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String account=accountTextField.getText().trim();
				
				if(CheckParameterUtil.checkEmail(account)){
					accountDAO=new AccountDAO();
					accountEntity=new AccountEntity();
					accountEntity=accountDAO.findByAccount(account);
					if(accountEntity.getAccount()==null){
						String post=account.substring(account.indexOf("@")+1,account.length());
						postTextField.setText(post);
						saveButton.setEnabled(true);
					}else{
						JOptionPane.showMessageDialog(
								AccountInsertPanel.this,
								"账号存在", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(
							AccountInsertPanel.this,
							"账号格式不正确", "系统提示",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		accountTextField.setBounds(181, 98, 229, 21);
		SinglePanel.add(accountTextField);
		accountTextField.setColumns(10);
		
		passwordTextField = new JTextField();
		passwordTextField.setBounds(181, 132, 229, 21);
		SinglePanel.add(passwordTextField);
		passwordTextField.setColumns(10);
		
		startRdb = new JRadioButton("\u5F00\u542F");
		startRdb.setSelected(true);
		startRdb.setBounds(180, 208, 121, 23);
		SinglePanel.add(startRdb);
		
		noStartRdb = new JRadioButton("\u5173\u95ED");
		noStartRdb.setBounds(304, 208, 121, 23);
		SinglePanel.add(noStartRdb);
		startBtnBg.add(startRdb);
		startBtnBg.add(noStartRdb);
		
		validRdb = new JRadioButton("\u6709\u6548");
		validRdb.setSelected(true);
		validRdb.setBounds(180, 168, 121, 23);
		SinglePanel.add(validRdb);
		
		noValidRdb = new JRadioButton("\u65E0\u6548");
		noValidRdb.setBounds(304, 168, 121, 23);
		SinglePanel.add(noValidRdb);
		
		validBtnBg.add(validRdb);
		validBtnBg.add(noValidRdb);
		
		remarkTextPane = new JTextPane();
		remarkTextPane.setBounds(181, 297, 514, 96);
		SinglePanel.add(remarkTextPane);
		
		JButton resButton = new JButton("\u91CD\u7F6E");
		//重置
		resButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reset();
			}
		});
		resButton.setBounds(605, 510, 95, 25);
		SinglePanel.add(resButton);
		
		saveButton = new JButton("\u4FDD\u5B58");
		saveButton.setEnabled(false);
		//单个保存
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String account=accountTextField.getText();
				String password=passwordTextField.getText();
				String post=postTextField.getText();
				String remark=remarkTextPane.getText();
				int valid=1;
				int start=1;
				
				if(noValidRdb.isSelected()){
					valid=0;
				}
				if(noStartRdb.isSelected()){
					start=0;
				}
				
				if("".equals(account)||"".equals(password)||"".equals(post)){
					JOptionPane.showMessageDialog(
							AccountInsertPanel.this,
							"数据不能为空", "系统提示",
							JOptionPane.INFORMATION_MESSAGE);
				}else{
					accountEntity=new AccountEntity();
					accountEntity.setAccount(account);
					accountEntity.setPassword(password);
					accountEntity.setPost(post);
					accountEntity.setValid(valid);
					accountEntity.setStart(start);
					accountEntity.setRemark(remark);
					
					accountDAO=new AccountDAO();
					int count=accountDAO.insert(accountEntity);
					if(count>0){
						JOptionPane.showMessageDialog(
								AccountInsertPanel.this,
								"添加成功", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
						reset();
					}else{
						JOptionPane.showMessageDialog(
								AccountInsertPanel.this,
								"添加失败", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
						//reset();
					}
					return;
				}
			}
		});
		saveButton.setBounds(732, 510, 95, 25);
		SinglePanel.add(saveButton);
		
		JLabel label_1 = new JLabel("\u90AE\u5C40");
		label_1.setBounds(435, 104, 85, 15);
		SinglePanel.add(label_1);
		
		postTextField = new JTextField();
		postTextField.setEnabled(false);
		postTextField.setBounds(473, 99, 121, 21);
		SinglePanel.add(postTextField);
		postTextField.setColumns(10);
	}
	
	public void reset(){
		this.filePathTextField.setText("");
		this.postTextField.setText("");
		this.accountTextField.setText("");
		this.passwordTextField.setText("");
		this.remarkTextPane.setText("");
		this.startRdb.setSelected(true);
		this.validRdb.setSelected(true);
	}
}
