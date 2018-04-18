package com.edmProxy.gui.panel.sendTask;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.List;
import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JRadioButton;

import com.edmProxy.dao.AccountDAO;
import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.dao.ReceiveDAO;
import com.edmProxy.dao.SendTaskDAO;
import com.edmProxy.dao.StatisticsDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.ReceiveEntity;
import com.edmProxy.entity.SendTaskEntity;
import com.edmProxy.entity.TestEntiy;
import com.edmProxy.gui.dialog.sendTask.SendTaskEditEkitContentDialog;
import com.edmProxy.util.CheckParameterUtil;
import com.edmProxy.util.JFileChooserFileFilter;
import com.edmProxy.util.NumberLenghtLimitedDmt;
import com.edmProxy.util.ReadFile;
import com.edmProxy.util.proxy.check.ProxyCheck;
import com.hexidec.ekit.EkitCore;
import com.hexidec.ekit.EkitCoreSpell;

import javax.swing.JTextPane;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JToolBar;
import java.awt.FlowLayout;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

//添加任务
public class SendTaskInsertPanel extends JPanel {

	private ReceiveEntity receiveEntity;

	private ReceiveDAO receiveDAO;
	private AccountDAO accountDAO;
	private ProxyDAO proxyDAO;
	private SendTaskDAO sendTaskDAO;
	private StatisticsDAO statisticsDAO;
	private JFileChooser jFileChooser;
	private JTextField sendTaskTextField;
	private JTextField postTextField;
	private JTextField sendTaskReceivesPathTextField;
	private JTextField accessoryPathTextField;
	private JTextField accountStartLinksTextField;
	private JPanel panel;
	private JTextField sendIntervalTimeTextField;
	private JTextField accountSendNumTextField;
	private JTextField contentPathTextField;
	private SendTaskEditEkitContentDialog sendTaskEditEkitContentDialog;
	private static ButtonGroup proxyStartBtnBg = new ButtonGroup();
	private JFileChooserFileFilter filter;
	private int result;
	private ArrayList<String> accessoryList;
	private String accessoryStr;
	private String accessoryName;
	private String[] accessoryArr;
	private String accessoryNameTemp;
	private String[] accessoryArrTemp;
	private JPanel accessoryPanel;
	private Component[] cs;
	private ArrayList<JCheckBox> checkBoxs;
	
	private JRadioButton proxyStartRdb;
	private JRadioButton proxyNoStartRdb;	
	private String sendTask;
	private String contentPath;
	private String post;
	private String sendTaskReceivesPath;
	private int proxyStart;
	private String accountStartLinksStr;
	private String sendIntervalTimeStr;
	private String accountSendNumStr;
	private int accountStartLinksInt;
	private int sendIntervalTimeInt;
	private int accountSendNumInt;
	
	private SendTaskEntity sendTaskEntity;
	private SendTaskEntity sendTaskEntitytemp;
	private JTextField titleTextField;
	private String title;
	private String accessoryListToStr;
	private JTextField accountSumTextField;
	private JTextField proxySumTextField;
	private JTextField accountNeedNumTextField;
	private JTextField sendTimeNeedTextField;
	
	private int accountSum;//账号总数
	private int proxySum;//代理总数
	private int accountNeedNum;//必须账号数据
	private int sendTimeNeed;//必须发送时间
	private int proxyNeedNum;//必须代理数据
	private int receiveSumInt;
	private String receiveSumStr;
	private JTextField receiveSumTextField;
	private JTextPane msgTextPane;
	
	private int hour=60*60;
	
	private int count=0;
	
	private int objId;

	
	
	public SendTaskInsertPanel() {
		this(null, null, null, null, true, false, true, true, null, null,
				false, false, false, true, false);
	}

	public SendTaskInsertPanel(String sDocument, String sStyleSheet,
			String sRawDocument, URL urlStyleSheet, boolean includeToolBar,
			boolean showViewSource, boolean showMenuIcons,
			boolean editModeExclusive, String sLanguage, String sCountry,
			boolean base64, boolean debugMode, boolean useSpellChecker,
			boolean multiBar, boolean enterBreak) {
		setBorder(new LineBorder(SystemColor.desktop));
		setBackground(SystemColor.activeCaptionBorder);
		this.setBounds(0, 0, 880, 612);
		setLayout(null);

		panel = new JPanel();
		panel.setBorder(new LineBorder(SystemColor.desktop));
		panel.setBounds(10, 10, 860, 592);
		add(panel);
		panel.setLayout(null);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(SystemColor.desktop));
		panel_2.setBounds(-11, -11, 880, 612);
		panel.add(panel_2);
		panel_2.setLayout(null);

		JButton resButton = new JButton("\u91CD\u7F6E");
		//重置
		resButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reset();
			}
		});
		resButton.setBounds(672, 575, 95, 25);
		panel_2.add(resButton);

		JButton saveButton = new JButton("\u4FDD\u5B58");
		//保存
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(sendTaskEntity!=null){
					sendTaskDAO=new SendTaskDAO();
					statisticsDAO=new StatisticsDAO();
					sendTaskEntitytemp=new SendTaskEntity();
					count=sendTaskDAO.queryCount();
					sendTaskEntitytemp=sendTaskDAO.findBySendTask(sendTaskEntity.getSendTask().trim());
					System.out.println(sendTaskEntitytemp.getSendTask());
					if(sendTaskEntitytemp.getSendTask()!=null){
						JOptionPane.showMessageDialog(
								SendTaskInsertPanel.this,
								"该任务名称已经存在！", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					if(count>10){
						JOptionPane.showMessageDialog(
								SendTaskInsertPanel.this,
								"只能添加10个任务！", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					objId=sendTaskDAO.insert(sendTaskEntity);
					statisticsDAO.insert(objId);
					
					if(objId>0){
						reset();
						JOptionPane.showMessageDialog(
								SendTaskInsertPanel.this,
								"添加任务和任务统计成功！", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(
								SendTaskInsertPanel.this,
								"保存失败！", "系统提示",
								JOptionPane.WARNING_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(
							SendTaskInsertPanel.this,
							"请先测试！", "系统提示",
							JOptionPane.WARNING_MESSAGE);
				}
//				System.out.println(sendTaskEntity);
			}
		});
		saveButton.setBounds(773, 574, 95, 25);
		panel_2.add(saveButton);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(new Color(0, 191, 255)));
		panel_4.setBounds(17, 18, 851, 276);
		panel_2.add(panel_4);
		panel_4.setLayout(null);

		JLabel label = new JLabel("\u9644    \u4EF6");
		label.setBounds(12, 97, 60, 15);
		panel_4.add(label);

		JLabel lblNewLabel = new JLabel("\u4EFB\u52A1\u540D\u79F0");
		lblNewLabel.setBounds(11, 12, 48, 15);
		panel_4.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("\u90AE    \u5C40");
		lblNewLabel_1.setBounds(220, 13, 60, 15);
		panel_4.add(lblNewLabel_1);

		JLabel lblNewLabel_3 = new JLabel("\u63A5\u6536\u90AE\u7BB1");
		lblNewLabel_3.setBounds(12, 67, 48, 15);
		panel_4.add(lblNewLabel_3);

		sendTaskTextField = new JTextField();
		sendTaskTextField.setBounds(65, 12, 141, 21);
		panel_4.add(sendTaskTextField);
		sendTaskTextField.setColumns(10);

		postTextField = new JTextField();
		postTextField.setBounds(274, 12, 140, 21);
		panel_4.add(postTextField);
		postTextField.setColumns(10);

		sendTaskReceivesPathTextField = new JTextField();
		sendTaskReceivesPathTextField.setEnabled(false);
		sendTaskReceivesPathTextField.setBackground(new Color(102, 205, 170));
		sendTaskReceivesPathTextField.setBounds(66, 66, 660, 21);
		panel_4.add(sendTaskReceivesPathTextField);
		sendTaskReceivesPathTextField.setColumns(10);

		accessoryPathTextField = new JTextField();
		accessoryPathTextField.setEnabled(false);
		accessoryPathTextField.setBackground(new Color(32, 178, 170));
		accessoryPathTextField.setBounds(66, 93, 660, 21);
		panel_4.add(accessoryPathTextField);
		accessoryPathTextField.setColumns(10);

		JButton accessoryButton = new JButton(
				"\u9009\u62E9\u6DFB\u52A0\u9644\u4EF6");
		//添加附件
		accessoryList=new ArrayList<String>();
		accessoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jFileChooser = new JFileChooser();
				filter = new JFileChooserFileFilter();
				// jFileChooser.setFileFilter(filter);
				result = jFileChooser.showOpenDialog(SendTaskInsertPanel.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile();
					accessoryStr=file.getPath();
					accessoryPathTextField.setText(accessoryStr);
					accessoryArr=accessoryStr.split("\\\\");
					accessoryName=accessoryArr[accessoryArr.length-1];
					new Thread(new Runnable() {
			            public void run() {
			            	if(accessoryList.size()>-1){
			            		for (int i = 0; i < accessoryList.size(); i++) {
			            			accessoryArrTemp=accessoryList.get(i).split("\\\\");
									accessoryNameTemp=accessoryArrTemp[accessoryArrTemp.length-1];
									if(accessoryName.equals(accessoryNameTemp)){
										JOptionPane.showMessageDialog(
												SendTaskInsertPanel.this,
												"该附件名已经存在", "系统提示",
												JOptionPane.WARNING_MESSAGE);
										return;
									}
								}
			            		if(accessoryList.size()==5){
				            		JOptionPane.showMessageDialog(
											SendTaskInsertPanel.this,
											"附件不能超过5个", "系统提示",
											JOptionPane.WARNING_MESSAGE);
									return;
			            		}
			            	}
			            	//添加附件多选框和list
			            	addAccessoryCheckBox(accessoryStr);
			            	System.out.println(accessoryList.toString());
			            }
					}).start();
				}
				
			}
		});
		accessoryButton.setBackground(new Color(32, 178, 170));
		accessoryButton.setBounds(732, 93, 108, 25);
		panel_4.add(accessoryButton);

		JButton editContentButton_ = new JButton(
				"\u7F16\u8F91\u5185\u5BB9");
		editContentButton_.setBackground(new Color(250, 250, 210));
		// 编辑发送内容
		editContentButton_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendTaskEditEkitContentDialog = new SendTaskEditEkitContentDialog();
				sendTaskEditEkitContentDialog.setModal(true);
				sendTaskEditEkitContentDialog.setVisible(true);
			}
		});
		editContentButton_.setFont(new Font("SimSun", Font.BOLD, 12));
		editContentButton_.setForeground(new Color(0, 0, 255));
		editContentButton_.setBounds(731, 35, 109, 25);
		panel_4.add(editContentButton_);

		JButton sendTaskReceivesPathButton = new JButton(
				"\u9009\u62E9\u90AE\u7BB1");
		//选择接收邮箱
		sendTaskReceivesPathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jFileChooser = new JFileChooser();
				// 打开直接默认E盘
				JFileChooserFileFilter filter = new JFileChooserFileFilter();
				jFileChooser.setFileFilter(filter);
				int result = jFileChooser
						.showOpenDialog(SendTaskInsertPanel.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile();
					sendTaskReceivesPathTextField.setText(file.getPath());
				}
			}
		});
		sendTaskReceivesPathButton.setBackground(new Color(102, 205, 170));
		sendTaskReceivesPathButton.setBounds(732, 64, 108, 25);
		panel_4.add(sendTaskReceivesPathButton);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(12, 169, 194, 58);
		panel_4.add(panel_1);
		panel_1.setLayout(null);

		proxyStartRdb = new JRadioButton("\u662F");
		proxyStartRdb.setSelected(true);
		proxyStartRdb.setFont(new Font("SimSun", Font.BOLD, 12));
		proxyStartRdb.setBounds(14, 25, 60, 23);
		panel_1.add(proxyStartRdb);

		proxyNoStartRdb = new JRadioButton("\u5426");
		proxyNoStartRdb.setFont(new Font("SimSun", Font.BOLD, 12));
		proxyNoStartRdb.setBounds(120, 25, 60, 23);
		panel_1.add(proxyNoStartRdb);
		
		proxyStartBtnBg.add(proxyStartRdb);
		proxyStartBtnBg.add(proxyNoStartRdb);


		JLabel lblNewLabel_4 = new JLabel(
				"\u662F\u5426\u542F\u7528\u4EE3\u7406");
		lblNewLabel_4.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_4.setBounds(6, 4, 78, 15);
		panel_1.add(lblNewLabel_4);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.setBounds(222, 169, 479, 58);
		panel_4.add(panel_3);
		panel_3.setLayout(null);

		JLabel label_2 = new JLabel("\u8D26\u53F7\u53C2\u6570\u8BBE\u7F6E");
		label_2.setFont(new Font("SimSun", Font.BOLD, 12));
		label_2.setBounds(4, 2, 78, 15);
		panel_3.add(label_2);

		JLabel lblNewLabel_5 = new JLabel(
				"\u53D1\u9001\u95F4\u9694\u65F6\u95F4");
		lblNewLabel_5.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_5.setBounds(29, 29, 78, 15);
		panel_3.add(lblNewLabel_5);

		sendIntervalTimeTextField = new JTextField();
		sendIntervalTimeTextField.setText("5");
		sendIntervalTimeTextField.setDocument(new NumberLenghtLimitedDmt(2));
		sendIntervalTimeTextField.setBounds(110, 27, 41, 21);
		panel_3.add(sendIntervalTimeTextField);
		sendIntervalTimeTextField.setColumns(10);
		sendIntervalTimeTextField.setBackground(new Color(221, 160, 221));

		JLabel lblNewLabel_7 = new JLabel("\u79D2");
		lblNewLabel_7.setBounds(157, 31, 21, 15);
		panel_3.add(lblNewLabel_7);

		JLabel label_9 = new JLabel(
				"\u8D26\u53F7\u6700\u5927\u53D1\u9001\u6570");
		label_9.setFont(new Font("SimSun", Font.BOLD, 12));
		label_9.setBounds(307, 30, 101, 15);
		panel_3.add(label_9);

		accountSendNumTextField = new JTextField();
		accountSendNumTextField.setText("5");
		accountSendNumTextField.setDocument(new NumberLenghtLimitedDmt(2));
		accountSendNumTextField.setColumns(10);
		accountSendNumTextField.setBackground(new Color(221, 160, 221));
		accountSendNumTextField.setBounds(399, 29, 41, 21);
		panel_3.add(accountSendNumTextField);

		JLabel label_10 = new JLabel("\u4E2A");
		label_10.setBounds(443, 33, 28, 15);
		panel_3.add(label_10);

		JButton calculateSendParameterButton = new JButton(
				"\u6D4B\u7B97\u53D1\u9001\u53C2\u6570");
		//测算发送任务
		calculateSendParameterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendTask=sendTaskTextField.getText();
				title=titleTextField.getText();
				contentPath=contentPathTextField.getText();
				post=postTextField.getText();
				sendTaskReceivesPath=sendTaskReceivesPathTextField.getText();
				proxyStart=1;
				if(proxyNoStartRdb.isSelected()){
					proxyStart=0;
				}
				//accountStartLinksStr=accountStartLinksTextField.getText();
				sendIntervalTimeStr=sendIntervalTimeTextField.getText();
				accountSendNumStr=accountSendNumTextField.getText();
				
				if("".equals(sendTask)||"".equals(title)||"".equals(contentPath)||"".equals(post)||"".equals(sendTaskReceivesPath)||"".equals(sendIntervalTimeStr)||"".equals(accountSendNumStr)){
					JOptionPane.showMessageDialog(
							SendTaskInsertPanel.this,
							"发送参数不能为空！", "系统提示",
							JOptionPane.WARNING_MESSAGE);
				}else{
					
					sendTaskEntity=new SendTaskEntity();
					sendTaskEntity.setSendTask(sendTask);
					sendTaskEntity.setTitle(title);
					sendTaskEntity.setProxyStart(proxyStart);
					sendTaskEntity.setContentPath(contentPath);
					sendTaskEntity.setPost(post);
					sendTaskEntity.setSendTaskReceivesPath(sendTaskReceivesPath);
					sendTaskEntity.setProxyStart(proxyStart);
					sendIntervalTimeInt=Integer.parseInt(sendIntervalTimeStr);
					accountSendNumInt=Integer.parseInt(accountSendNumStr);
					sendTaskEntity.setAccountStartLinks(accountStartLinksInt);
					sendTaskEntity.setSendIntervalTime(sendIntervalTimeInt);
					sendTaskEntity.setAccountSendNum(accountSendNumInt);
					//sendTaskEntity.setRemark(remarkTextPane.getText());
					if(accessoryList.size()>-1){
						//有附件
						accessoryListToStr=accessoryList.toString();
						accessoryListToStr=accessoryListToStr.substring(1,accessoryListToStr.length()-1);
						accessoryListToStr.replace("\\", "\\\\");
						System.out.println(accessoryListToStr);
						sendTaskEntity.setAccessoryPath(accessoryListToStr);
					}
					getParameterByCalculate();
					sendTaskEntity.setAccountStartLinks(accountStartLinksInt);
				}
			}
		});
		calculateSendParameterButton.setFont(new Font("SimSun", Font.BOLD, 12));
		calculateSendParameterButton.setForeground(new Color(204, 51, 0));
		calculateSendParameterButton.setBounds(714, 169, 128, 58);
		panel_4.add(calculateSendParameterButton);

		JLabel label_12 = new JLabel("\u5185    \u5BB9");
		label_12.setBounds(12, 43, 60, 15);
		panel_4.add(label_12);

		contentPathTextField = new JTextField();
		contentPathTextField.setEnabled(false);
		contentPathTextField.setColumns(10);
		contentPathTextField.setBackground(new Color(250, 250, 210));
		contentPathTextField.setBounds(66, 39, 543, 21);
		panel_4.add(contentPathTextField);

		JButton contentButton = new JButton(
				"\u9009\u62E9\u53D1\u9001\u5185\u5BB9");
		// 选择发送内容
		contentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jFileChooser = new JFileChooser();
				filter = new JFileChooserFileFilter();
				// jFileChooser.setFileFilter(filter);
				result = jFileChooser.showOpenDialog(SendTaskInsertPanel.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile();
					contentPathTextField.setText(file.getPath());
				}
			}
		});
		contentButton.setBackground(new Color(250, 250, 210));
		contentButton.setBounds(617, 35, 109, 25);
		panel_4.add(contentButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setBounds(99, 120, 743, 43);
		panel_4.add(scrollPane);
		
		accessoryPanel = new JPanel();
		accessoryPanel.setBackground(new Color(32, 178, 170));
		scrollPane.setViewportView(accessoryPanel);
		accessoryPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		accessoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
		
		JButton deleteAccessoryButton = new JButton("\u5220\u9664\u9644\u4EF6");
		deleteAccessoryButton.setBackground(new Color(32, 178, 170));
		deleteAccessoryButton.setBounds(10, 122, 83, 39);
		panel_4.add(deleteAccessoryButton);
		
		JLabel lblNewLabel_2 = new JLabel("\u6807    \u9898");
		lblNewLabel_2.setBounds(426, 13, 54, 15);
		panel_4.add(lblNewLabel_2);
		
		titleTextField = new JTextField();
		titleTextField.setBounds(482, 10, 358, 21);
		panel_4.add(titleTextField);
		titleTextField.setColumns(10);
		//删除附件
		deleteAccessoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(accessoryList.size()>-1){
					cs = accessoryPanel.getComponents(); 
					checkBoxs = new ArrayList();
					for(Component c:cs){
						if(c instanceof JCheckBox){
							JCheckBox checkBox=((JCheckBox)c);
							checkBoxs.add(checkBox);
						}
					}
					
					for(JCheckBox box:checkBoxs){
						if(box.isSelected()){
							String boxTemp=box.getText();
							for (int i = 0; i < accessoryList.size(); i++) {
								accessoryStr=accessoryList.get(i);
								accessoryArr=accessoryStr.split("\\\\");
								accessoryName=accessoryArr[accessoryArr.length-1];
								if(boxTemp.equals(accessoryName)){
									accessoryList.remove(accessoryStr);
								}
							}
							accessoryPanel.remove(box);
							accessoryPanel.updateUI();
						}
					}
					//System.out.println(accessoryList.toString());
				}
				
			}
		});

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(new Color(0, 191, 255)));
		panel_5.setBounds(17, 325, 851, 244);
		panel_2.add(panel_5);
		panel_5.setLayout(null);

		JLabel label_4 = new JLabel("\u53D1\u9001\u6240\u9700\u8D26\u53F7\u6570(\u63A5\u6536\u90AE\u7BB1/\u8D26\u53F7\u6700\u5927\u53D1\u9001\u6570)\uFF1A");
		label_4.setBounds(313, 10, 335, 15);
		panel_5.add(label_4);

		JLabel label_5 = new JLabel("\u5B9E\u9645\u8D26\u53F7\u6570\uFF1A");
		label_5.setBounds(20, 12, 85, 15);
		panel_5.add(label_5);

		JLabel label_6 = new JLabel("\u53D1\u9001\u6240\u9700\u65F6\u95F4(\u63A5\u6536\u90AE\u7BB1*\u53D1\u9001\u95F4\u9694\u65F6\u95F4)\uFF1A");
		label_6.setBounds(313, 46, 231, 15);
		panel_5.add(label_6);

		JLabel lblNewLabel_9 = new JLabel("\u5B9E\u9645\u4EE3\u7406\u6570\uFF1A");
		lblNewLabel_9.setBounds(20, 80, 85, 15);
		panel_5.add(lblNewLabel_9);

		msgTextPane = new JTextPane();
		msgTextPane.setBounds(10, 193, 831, 41);
		panel_5.add(msgTextPane);

		JLabel lblNewLabel_11 = new JLabel("\u6D4B\u7B97\u7ED3\u679C");
		lblNewLabel_11.setBounds(20, 172, 54, 15);
		panel_5.add(lblNewLabel_11);
		
		accountSumTextField = new JTextField();
		accountSumTextField.setEnabled(false);
		accountSumTextField.setBounds(90, 10, 85, 21);
		panel_5.add(accountSumTextField);
		accountSumTextField.setColumns(10);
		
		proxySumTextField = new JTextField();
		proxySumTextField.setEnabled(false);
		proxySumTextField.setColumns(10);
		proxySumTextField.setBounds(90, 78, 85, 21);
		panel_5.add(proxySumTextField);
		
		accountNeedNumTextField = new JTextField();
		accountNeedNumTextField.setEnabled(false);
		accountNeedNumTextField.setColumns(10);
		accountNeedNumTextField.setBounds(654, 10, 85, 21);
		panel_5.add(accountNeedNumTextField);
		
		sendTimeNeedTextField = new JTextField();
		sendTimeNeedTextField.setEnabled(false);
		sendTimeNeedTextField.setColumns(10);
		sendTimeNeedTextField.setBounds(654, 43, 85, 21);
		panel_5.add(sendTimeNeedTextField);
		
		JLabel label_3 = new JLabel("\u6536\u4EF6\u90AE\u7BB1\u6570\uFF1A");
		label_3.setBounds(20, 130, 85, 15);
		panel_5.add(label_3);
		
		receiveSumTextField = new JTextField();
		receiveSumTextField.setColumns(10);
		receiveSumTextField.setBounds(90, 124, 85, 21);
		panel_5.add(receiveSumTextField);
		
				JLabel label_1 = new JLabel("\u8D26\u53F7\u542F\u52A8\u6570\uFF08\u6536\u4EF6\u90AE\u7BB1\u6570/\u8D26\u53F7\u6700\u5927\u53D1\u9001\u6570\uFF09\uFF1A");
				label_1.setBounds(315, 128, 255, 15);
				panel_5.add(label_1);
				label_1.setFont(new Font("SimSun", Font.PLAIN, 12));
				
						accountStartLinksTextField = new JTextField();
						accountStartLinksTextField.setEnabled(false);
						accountStartLinksTextField.setBounds(654, 127, 85, 21);
						panel_5.add(accountStartLinksTextField);
						accountStartLinksTextField.setText("5");
						accountStartLinksTextField.setDocument(new NumberLenghtLimitedDmt(1));
						accountStartLinksTextField.setBackground(new Color(221, 160, 221));
						accountStartLinksTextField.setColumns(10);
						
								JLabel lblNewLabel_6 = new JLabel("\u4E2A");
								lblNewLabel_6.setBounds(745, 130, 28, 15);
								panel_5.add(lblNewLabel_6);
								
								JLabel label_7 = new JLabel("\u79D2");
								label_7.setBounds(745, 46, 54, 15);
								panel_5.add(label_7);
								
								JLabel label_8 = new JLabel("\u4E2A");
								label_8.setBounds(745, 12, 54, 15);
								panel_5.add(label_8);
								
								JLabel label_11 = new JLabel("\u4E2A");
								label_11.setBounds(181, 13, 54, 15);
								panel_5.add(label_11);
								
								JLabel label_13 = new JLabel("\u4E2A");
								label_13.setBounds(181, 80, 54, 15);
								panel_5.add(label_13);
								
								JLabel label_14 = new JLabel("\u4E2A");
								label_14.setBounds(181, 130, 54, 15);
								panel_5.add(label_14);
	}
	
	//添加附件多选框和list
	public void addAccessoryCheckBox(String accessoryStr){
		accessoryList.add(accessoryStr);
    	accessoryPanel.removeAll();
    	for (int i = 0; i < accessoryList.size(); i++) {
    		accessoryList.remove(accessoryList);
    		//System.out.println(accessoryList.get(i));
    		accessoryArr=accessoryList.get(i).split("\\\\");
			accessoryName=accessoryArr[accessoryArr.length-1];
    		JCheckBox chckbx=new JCheckBox();
			chckbx.setText(accessoryName);
			chckbx.setBounds(1, 1, 103, 23);
			chckbx.setForeground(Color.green);
			chckbx.setBackground(new Color(0, 51, 102));
			accessoryPanel.add(chckbx);
			accessoryPanel.updateUI();
		}
	}
	
	//得到实际参数和计算
	public void getParameterByCalculate(){
		accountDAO=new AccountDAO();
		proxyDAO=new ProxyDAO();
		
		accountSum=accountDAO.queryCount();
		proxySum=proxyDAO.queryCount();
		
		accountSumTextField.setText(accountSum+"");
		proxySumTextField.setText(proxySum+"");
		
		receiveSumStr=receiveSumTextField.getText();
		if("".equals(receiveSumStr)){
			JOptionPane.showMessageDialog(
					SendTaskInsertPanel.this,
					"发送参数不能为空！", "系统提示",
					JOptionPane.WARNING_MESSAGE);
			return;
		}else{
			receiveSumInt=Integer.parseInt(receiveSumStr);
		}
		
		accountNeedNum=receiveSumInt/accountSendNumInt;
		sendTimeNeed=receiveSumInt*sendIntervalTimeInt;
		proxyNeedNum=sendTimeNeed/(60*60);
		accountStartLinksInt=receiveSumInt/accountSendNumInt;
		
		accountNeedNumTextField.setText(accountNeedNum+"");
		sendTimeNeedTextField.setText(sendTimeNeed+"");
		accountStartLinksTextField.setText(accountStartLinksInt+"");
		
		if(sendTimeNeed<hour&&accountSum>0){
			//System.out.println("可以发送");
			JOptionPane.showMessageDialog(
					SendTaskInsertPanel.this,
					"适合发送！", "系统提示",
					JOptionPane.INFORMATION_MESSAGE);
			msgTextPane.setText("适合发送");
		}else{
			//测算对比，是否合适发送
			if(accountNeedNum<accountSum&&proxyNeedNum<proxySum){
				//System.out.println("可以发送");
				JOptionPane.showMessageDialog(
						SendTaskInsertPanel.this,
						"适合发送！", "系统提示",
						JOptionPane.INFORMATION_MESSAGE);
				msgTextPane.setText("适合发送");
				
			}else{
				//System.out.println("不适合发送");
				JOptionPane.showMessageDialog(
						SendTaskInsertPanel.this,
						"不适合发送！", "系统提示",
						JOptionPane.WARNING_MESSAGE);
				msgTextPane.setText("不适合发送!");
			}
		}
		//System.out.println("accountNeedNum:"+accountNeedNum+"-"+"sendTimeNeed:"+sendTimeNeed+"-"+"proxyNeedNum:"+proxyNeedNum);
	}
	
	//重置
	public void reset(){
		sendTaskTextField.setText("");
		postTextField.setText("");
		titleTextField.setText("");
		contentPathTextField.setText("");
		sendTaskReceivesPathTextField.setText("");
		accessoryPathTextField.setText("");
		accountStartLinksTextField.setText("");
		sendIntervalTimeTextField.setText("");
		accountSendNumTextField.setText("");
		accountSumTextField.setText("");
		accountNeedNumTextField.setText("");
		proxySumTextField.setText("");
		accountStartLinksTextField.setText("");
		receiveSumTextField.setText("");
		msgTextPane.setText("");
		accessoryList.removeAll(accessoryList);
		accessoryPanel.removeAll();
	}
}
