package com.edmProxy.gui.panel.account;

import java.awt.SystemColor;

import javax.mail.MessagingException;
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
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.swing.JRadioButton;

import com.edmProxy.constant.Constants;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.TestEntiy;
import com.edmProxy.util.GetHtmlUtil;
import com.edmProxy.util.JFileChooserFileFilter;
import com.edmProxy.util.ReadFile;
import com.edmProxy.util.WriteFile;
import com.edmProxy.util.mail.receive.AcceptMail;
import com.edmProxy.util.mail.send.SendMail;
import com.edmProxy.util.proxy.check.ProxyCheck;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

//代理检测
public class AccountCheckPanel extends JPanel {
	private JTextField filePathTextField;
	private JTextField reveiceEmailTextField;
	private JTextField reveicePasswordTextField;
	private JTextField reveicePostTextField;
	private JLabel accountSumLabel;

	
	private JLabel accountFailureLabel;
	private JButton accountBatchCheckButton;
	private JButton SendTestButton;
	private JLabel accountStateLabel;
	private JTextArea msgTextArea;
	private JFileChooser jFileChooser;
	private JScrollPane scrollPane;



	private JLabel accountSucceeLabel;
	private int accountSumCount;
	private int accountSucceeCount;
	private int accountFailureCount;
	
	private String fileName;
	private String filePath;
	private String trueNewFile;
	private String falseNewFile;
	
	private String newFile[];
	
	private String dateAndTime = new SimpleDateFormat("yyyyMMddHHmmss")
	.format(Calendar.getInstance().getTime());
	private JTextField sendAccountTextField;
	private JTextField accuntPasswordEmailTextField;
	private JTextField accountPostTextField;
	private SendMail sendMail;
	private AcceptMail acceptMail;
	
	
	private String sendAccount;
	private String accuntPassword;
	private String accountPost;
	
	private String reveiceEmail;
	private String reveicePassword;
	private String reveicePost;
	


	private ArrayList<String> accountStrList;
	private AccountEntity accountEntity;
	
	private String accountStr;
	private String reveiceStr;
	
	private String filePathStr;

	public AccountCheckPanel() {
		setBorder(new LineBorder(SystemColor.desktop));
		setBackground(SystemColor.activeCaptionBorder);
		this.setBounds(0, 0, 880, 612);
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(SystemColor.desktop));
		panel.setBounds(10, 10, 860, 146);
		add(panel);
		panel.setLayout(null);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(SystemColor.desktop));
		panel_2.setBounds(10, 10, 246, 126);
		panel.add(panel_2);
		panel_2.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 237, 126);
		panel_2.add(tabbedPane);

		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("\u6279\u91CF\u68C0\u6D4B", Constants.batchCheckIcon, panel_4, null);
		panel_4.setLayout(null);

		JLabel label = new JLabel("\u8BF7\u9009\u62E9\u6587\u4EF6");
		label.setBounds(10, 10, 75, 15);
		panel_4.add(label);

		filePathTextField = new JTextField();
		filePathTextField.setBounds(72, 10, 152, 21);
		panel_4.add(filePathTextField);
		filePathTextField.setColumns(10);

		JButton selectButton = new JButton("\u9009\u62E9",Constants.selectIcon);
		// 选择
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jFileChooser = new JFileChooser();
				// jFileChooser.setCurrentDirectory(new File("e://"));//设置默认目录
				// 打开直接默认E盘
				JFileChooserFileFilter filter = new JFileChooserFileFilter();
				jFileChooser.setFileFilter(filter);
				int result = jFileChooser
						.showOpenDialog(AccountCheckPanel.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile();
					filePathTextField.setText(file.getPath());
				}
			}
		});
		selectButton.setBounds(129, 37, 95, 25);
		panel_4.add(selectButton);

		accountBatchCheckButton = new JButton("\u68C0\u6D4B",Constants.checkBtnIcon);
		// 批量检测
		accountBatchCheckButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
		            public void run() {
		            	accountStateLabel.setText(Constants.runing);
		            	accountBatchCheckButton.setEnabled(false);
						SendTestButton.setEnabled(false);
						
						reveiceEmail=reveiceEmailTextField.getText();
						reveicePassword=reveicePasswordTextField.getText();
						reveicePost=reveicePostTextField.getText();

						// 读取指定文件地址
						filePathStr = filePathTextField.getText();
						//System.out.println(testNetAddress+":"+checkTime+":"+outNetAddress+":"+filePathStr);
						if ("".equals(filePathStr) || "".equals(sendAccount)|| 
								"".equals(accuntPassword)||"".equals(accountPost)||
								"".equals(reveiceEmail)||"".equals(reveicePassword)||
								"".equals(reveicePost)) {
							{
								JOptionPane.showMessageDialog(AccountCheckPanel.this,
										"测试参数不能为空！", "系统提示", JOptionPane.WARNING_MESSAGE);
							}
						} else {
							try {
								accountStrList = new ArrayList<String>();
								accountStrList = ReadFile.ReadTxt(filePathStr);
								accountSumCount=accountStrList.size();
								if (accountStrList.size() > -1) {
									
//									 fileName = filePathStr.substring(filePathStr.lastIndexOf("\\")+1);
//							    	 filePath=filePathStr.substring(0,filePathStr.lastIndexOf("\\")+1);
//							    	 //filePath=filePath.replace("/", "\\\\");
//							    	 
//							    	 trueNewFile=filePath+"true"+dateAndTime+fileName;
//									 falseNewFile=filePath+"false"+dateAndTime+fileName;
//									 
//									 //System.out.println("1--"+filePath);
//									 //System.out.println("--"+falseNewFile);
//									 
//									 WriteFile.createFile(trueNewFile);
//									 WriteFile.createFile(falseNewFile);
//									 
//									 newFile=new String[2];
//									 newFile[0]=trueNewFile;
//									 newFile[1]=falseNewFile;

									int accountStrListSize = accountStrList
											.size();
									accountSumLabel
											.setText(accountStrListSize + "");
									// 把字符拆成5个//1-173.234.50.215-1080-user001-123456
									for (int i = 0; i < accountStrListSize; i++) {
										accountEntity = new AccountEntity();
										accountStr = accountStrList.get(i);
										String account[] = accountStr.split("-");
										if (account.length > -1) {
											String temp=account[0];
											accountEntity.setAccount(temp);
											accountEntity.setPassword(account[1]);
											accountEntity.setPost(temp.substring(temp.indexOf("@")+1, temp.length()));
											
											ArrayList<String> receiveList=new ArrayList<String>();
											receiveList.add(reveiceEmail);	
											String titleMail="test";
											String contentMail="test";
											int state=0;
											try {
												//发送
												double t=(double)Math.random()*10;
												if(t<5)
													t+=3;
												int tt=(int)t*1000;
												//Thread.sleep(tt);
												Thread.sleep(10000*6);
												sendMail=new SendMail();
												sendMail.sendMailTrue(accountEntity, receiveList, null, titleMail+tt, contentMail+tt, 0,null,0);
												//接收
												Thread.sleep(3000);
												acceptMail=new AcceptMail();
												
												fileName = filePathStr.substring(filePathStr.lastIndexOf("\\")+1);
										    	filePath=filePathStr.substring(0,filePathStr.lastIndexOf("\\")+1);
										    	trueNewFile=filePath+"true-account-"+dateAndTime+fileName;
										    	WriteFile.createFile(trueNewFile);
										    	
//										    	newFile=new String[2];
//												newFile[0]=trueNewFile;
//												newFile[1]=falseNewFile;
												
												String msg=acceptMail.mailReceive(reveiceEmail, reveicePassword, reveicePost, accountEntity, titleMail+tt);
												
												String[] msgArr=msg.split("-");
												if("true".equals(msgArr[0])){
													WriteFile.getDataWriteFile(trueNewFile,msgArr[1]+"-"+msgArr[2]);
													msgTextArea.append(msg);
													accountSucceeCount++;
													accountSucceeLabel.setText(accountSucceeCount+"");
												}
												
											} 
											catch (UnsupportedEncodingException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (MessagingException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} 
											catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
								}
							} catch (IOException e) {
								JOptionPane.showMessageDialog(
										AccountCheckPanel.this, "读取文件错误！",
										"系统提示", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}
						}
						accountFailureCount=accountSumCount-accountSucceeCount;
						accountFailureLabel.setText(accountFailureCount+"");
						filePathStr="";
						filePathTextField.setText("");
						accountStateLabel.setText(Constants.stop);
		            }
		        }).start();
				
			}
		});
		accountBatchCheckButton.setBounds(129, 65, 95, 25);
		panel_4.add(accountBatchCheckButton);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(SystemColor.activeCaption));
		panel_3.setBounds(262, 10, 439, 126);
		panel.add(panel_3);
		panel_3.setLayout(null);

		JLabel label_1 = new JLabel("\u68C0\u6D4B\u53C2\u6570\u8BBE\u7F6E");
		label_1.setFont(new Font("SimSun", Font.BOLD, 12));
		label_1.setBounds(10, 3, 87, 15);
		panel_3.add(label_1);

		JLabel label_2 = new JLabel("\u6536\u4EF6\u90AE\u7BB1");
		label_2.setBounds(10, 24, 97, 15);
		panel_3.add(label_2);

		reveiceEmailTextField = new JTextField();
		//失去焦点，接收
		reveiceEmailTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				reveiceStr=reveiceEmailTextField.getText();
				if(!"".equals(reveiceStr)){
					reveicePost=reveiceStr.substring(reveiceStr.indexOf("@")+1,reveiceStr.length());
					reveicePostTextField.setText(reveicePost);
				}
			}
		});
		reveiceEmailTextField.setText("lookTestAndy@163.com");
		reveiceEmailTextField.setBounds(60, 24, 135, 21);
		panel_3.add(reveiceEmailTextField);
		reveiceEmailTextField.setColumns(10);

		JLabel label_4 = new JLabel("\u5BC6    \u7801");
		label_4.setBounds(10, 46, 54, 15);
		panel_3.add(label_4);

		reveicePasswordTextField = new JTextField();
		reveicePasswordTextField.setText("andy123456");
		reveicePasswordTextField.setBounds(60, 45, 135, 21);
		panel_3.add(reveicePasswordTextField);
		reveicePasswordTextField.setColumns(10);

		reveicePostTextField = new JTextField();
		reveicePostTextField.setToolTipText("\u6536\u4EF6\u90AE\u7BB1\u534F\u8BAE\u5FC5\u987B\u652F\u6301imap\u534F\u8BAE");
		reveicePostTextField.setText("163.com");
		reveicePostTextField.setBounds(60, 66, 135, 25);
		panel_3.add(reveicePostTextField);
		reveicePostTextField.setColumns(10);

		JLabel label_8 = new JLabel("\u90AE    \u5C40");
		label_8.setBounds(10, 68, 54, 15);
		panel_3.add(label_8);

		SendTestButton = new JButton("\u53D1\u9001\u6D4B\u8BD5\u4FE1");
		SendTestButton.setForeground(new Color(0, 51, 255));
		SendTestButton.setFont(new Font("SimSun", Font.BOLD, 12));
		// 发送测试信
		SendTestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendAccount=sendAccountTextField.getText();
				accuntPassword=accuntPasswordEmailTextField.getText();
				accountPost=accountPostTextField.getText();
				
				reveiceEmail=reveiceEmailTextField.getText();
				reveicePassword=reveicePasswordTextField.getText();
				reveicePost=reveicePostTextField.getText();
				
				//发邮件
				AccountEntity accountEntity=new AccountEntity();
				accountEntity.setAccount(sendAccount);
				accountEntity.setPassword(accuntPassword);
				accountEntity.setPost(accountPost);
				ArrayList<String> receiveList=new ArrayList<String>();
				receiveList.add(reveiceEmail);	
				String titleMail="test";
				String contentMail="test";
				int state=0;
				
				try {
					sendMail=new SendMail();
					sendMail.sendMailTrue(accountEntity, receiveList, null, titleMail, contentMail,0,null,0);
					Thread.sleep(2000);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				acceptMail=new AcceptMail();
				String msg=acceptMail.mailReceive(reveiceEmail, reveicePassword, reveicePost, accountEntity, titleMail);
				msgTextArea.append(msg);
			}
		});
		SendTestButton.setBounds(319, 97, 110, 25);
		panel_3.add(SendTestButton);
		
		JButton enabledButton = new JButton("\u542F\u7528\u68C0\u6D4B");
		enabledButton.setFont(new Font("SimSun", Font.BOLD, 12));
		enabledButton.setForeground(new Color(255, 0, 0));
		enabledButton.setBounds(10, 97, 95, 25);
		panel_3.add(enabledButton);
		
		sendAccountTextField = new JTextField();
		//失去焦点 发送
		sendAccountTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				accountStr=sendAccountTextField.getText();
				if(!"".equals(accountStr)){
					accountPost=accountStr.substring(accountStr.indexOf("@")+1,accountStr.length());
					accountPostTextField.setText(accountPost);
				}
			}
		});
		sendAccountTextField.setText("2775363605@qq.com");
		sendAccountTextField.setColumns(10);
		sendAccountTextField.setBounds(269, 24, 160, 21);
		panel_3.add(sendAccountTextField);
		
		accuntPasswordEmailTextField = new JTextField();
		accuntPasswordEmailTextField.setText("andy123456");
		accuntPasswordEmailTextField.setColumns(10);
		accuntPasswordEmailTextField.setBounds(269, 45, 160, 21);
		panel_3.add(accuntPasswordEmailTextField);
		
		accountPostTextField = new JTextField();
		//失去焦点
		accountPostTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		accountPostTextField.setText("qq.com");
		accountPostTextField.setColumns(10);
		accountPostTextField.setBounds(269, 66, 160, 25);
		panel_3.add(accountPostTextField);
		
		JLabel label_9 = new JLabel("\u53D1\u4EF6\u90AE\u7BB1");
		label_9.setBounds(215, 24, 97, 15);
		panel_3.add(label_9);
		
		JLabel label_11 = new JLabel("\u5BC6    \u7801");
		label_11.setBounds(215, 46, 54, 15);
		panel_3.add(label_11);
		
		JLabel label_12 = new JLabel("\u90AE    \u5C40");
		label_12.setBounds(215, 68, 54, 15);
		panel_3.add(label_12);
		//启用检测
		enabledButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				accountBatchCheckButton.setEnabled(true);
				SendTestButton.setEnabled(true);
				accountSumLabel.setText("0");
				accountSucceeLabel.setText("0");
				accountFailureLabel.setText("0");
			}
		});

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(new Color(255, 0, 0)));
		panel_6.setBounds(707, 10, 143, 126);
		panel.add(panel_6);
		panel_6.setLayout(null);

		JLabel label_3 = new JLabel("\u68C0\u6D4B\u603B\u6570");
		label_3.setBounds(10, 34, 54, 15);
		panel_6.add(label_3);

		JLabel lblNewLabel = new JLabel("\u6210\u529F");
		lblNewLabel.setBounds(10, 55, 54, 15);
		panel_6.add(lblNewLabel);

		JLabel label_5 = new JLabel("\u5931\u8D25");
		label_5.setBounds(10, 76, 54, 15);
		panel_6.add(label_5);

		JLabel label_6 = new JLabel("\u72B6\u6001");
		label_6.setBounds(10, 97, 54, 15);
		panel_6.add(label_6);

		JLabel label_10 = new JLabel("\u6279\u91CF\u68C0\u6D4B\u4FE1\u606F");
		label_10.setFont(new Font("SimSun", Font.BOLD, 12));
		label_10.setBounds(9, 6, 87, 15);
		panel_6.add(label_10);

		accountSumLabel = new JLabel("0");
		accountSumLabel.setForeground(Color.RED);
		accountSumLabel.setFont(new Font("SimSun", Font.BOLD, 12));
		accountSumLabel.setBounds(70, 34, 54, 15);
		panel_6.add(accountSumLabel);
		
		accountSucceeLabel = new JLabel("0");
		accountSucceeLabel.setForeground(Color.RED);
		accountSucceeLabel.setFont(new Font("SimSun", Font.BOLD, 12));
		accountSucceeLabel.setBounds(70, 55, 54, 15);
		panel_6.add(accountSucceeLabel);
		
		accountFailureLabel = new JLabel("0");
		accountFailureLabel.setForeground(Color.RED);
		accountFailureLabel.setFont(new Font("SimSun", Font.BOLD, 12));
		accountFailureLabel.setBounds(70, 76, 54, 15);
		panel_6.add(accountFailureLabel);
		
		accountStateLabel = new JLabel("\u505C\u6B62");
		accountStateLabel.setForeground(Color.RED);
		accountStateLabel.setFont(new Font("SimSun", Font.BOLD, 12));
		accountStateLabel.setBounds(70, 97, 54, 15);
		panel_6.add(accountStateLabel);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 153, 255)));
		panel_1.setBounds(10, 162, 860, 440);
		add(panel_1);
		panel_1.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 860, 440);
		panel_1.add(scrollPane);
		
				msgTextArea = new JTextArea();
				msgTextArea.setForeground(SystemColor.windowText);
				msgTextArea.setBackground(SystemColor.window);
				msgTextArea.setFont(new Font("Dialog", Font.BOLD, 16));
				scrollPane.setViewportView(msgTextArea);
	}
}
