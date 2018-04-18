package com.edmProxy.gui.panel.proxy;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JRadioButton;

import com.edmProxy.constant.Constants;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.TestEntiy;
import com.edmProxy.util.GetHtmlUtil;
import com.edmProxy.util.JFileChooserFileFilter;
import com.edmProxy.util.ReadFile;
import com.edmProxy.util.WriteFile;
import com.edmProxy.util.proxy.check.ProxyCheck;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;

//代理检测
public class ProxyCheckPanel extends JPanel {
	private JTextField filePathTextField;
	private JTextField testNetAddressTextField;
	private JTextField checkTimeTextField;
	private JTextField proxyHostTextField;
	private JTextField outNetAddressTextField;
	private JTextField proxyAccountTextField;
	private JTextField proxyPasswordTextField;
	private JLabel proxySumLabel;
	private ProxyCheck proxyCheck;
	private ProxyEntity proxyEntiy;
	
	private JLabel proxyFailureLabel;
	private JButton proxyBatchCheckButton;
	private JButton outNetButton;
	private JButton proxyServerSingleCheckButton;
	private JLabel proxyStateLabel;

	private JRadioButton proxyHttpTypeRdb;
	private JRadioButton proxyScoketTypeRdb;
	private static ButtonGroup proxyTypeBtnBg = new ButtonGroup();
	private JTextField proxyPortTextField;
	private JTextArea msgTextArea;
	private String[] getHtmlArry;
	private JFileChooser jFileChooser;
	private JScrollPane scrollPane;
	private ArrayList<String> proxyPortStrList;

	private String proxyHost;
	private String proxyPort;
	private String proxyAccount;
	private String proxyPassword;
	private String testNetAddress;
	private String checkTime;
	private String outNetAddress;
	private String proxyPortStr;
	private int proxyType;
	private JLabel proxySucceeLabel;
	private int proxySucceeCount;
	private int proxyFailureCount;
	
	private String fileName;
	private String filePath;
	private String trueNewFile;
	private String falseNewFile;
	
	private String newFile[];
	
	private String dateAndTime = new SimpleDateFormat("yyyyMMddHHmmss")
	.format(Calendar.getInstance().getTime());


	public ProxyCheckPanel() {
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
		panel_2.setBounds(10, 10, 405, 126);
		panel.add(panel_2);
		panel_2.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 403, 126);
		panel_2.add(tabbedPane);

		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("\u6279\u91CF\u68C0\u6D4B", Constants.batchCheckIcon, panel_4, null);
		panel_4.setLayout(null);

		JLabel label = new JLabel("\u8BF7\u9009\u62E9\u6587\u4EF6");
		label.setBounds(10, 10, 75, 15);
		panel_4.add(label);

		filePathTextField = new JTextField();
		filePathTextField.setBounds(72, 10, 182, 21);
		panel_4.add(filePathTextField);
		filePathTextField.setColumns(10);

		JButton selectButton = new JButton("\u9009\u62E9",Constants.selectIcon);
		// 选择
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jFileChooser = new JFileChooser();
				JFileChooserFileFilter filter = new JFileChooserFileFilter();
				jFileChooser.setFileFilter(filter);
				int result = jFileChooser
						.showOpenDialog(ProxyCheckPanel.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile();
					filePathTextField.setText(file.getPath());
				}
			}
		});
		selectButton.setBounds(260, 6, 95, 25);
		panel_4.add(selectButton);

		proxyBatchCheckButton = new JButton("\u68C0\u6D4B",Constants.checkBtnIcon);
		// 批量检测
		proxyBatchCheckButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
		            public void run() {
		            	proxyStateLabel.setText(Constants.runing);
		            	proxyBatchCheckButton.setEnabled(false);
						outNetButton.setEnabled(false);
						proxyServerSingleCheckButton.setEnabled(false);
		            	
		            	proxyCheck = new ProxyCheck();
						testNetAddress = testNetAddressTextField.getText();
						checkTime = checkTimeTextField.getText();
						outNetAddress = outNetAddressTextField.getText();
		            	
						// 读取指定文件地址
						String filePathStr = filePathTextField.getText();
						//System.out.println(testNetAddress+":"+checkTime+":"+outNetAddress+":"+filePathStr);
						if ("".equals(filePathStr) || "".equals(testNetAddress)
								|| "".equals(checkTime) || "".equals(outNetAddress)) {
							{
								JOptionPane.showMessageDialog(ProxyCheckPanel.this,
										"测试参数不能为空！", "系统提示", JOptionPane.WARNING_MESSAGE);
							}
						} else {
							try {
								proxyPortStrList = new ArrayList<String>();
								proxyPortStrList = ReadFile.ReadTxt(filePathStr);
								if (proxyPortStrList.size() > -1) {
									
									 fileName = filePathStr.substring(filePathStr.lastIndexOf("\\")+1);
							    	 filePath=filePathStr.substring(0,filePathStr.lastIndexOf("\\")+1);
							    	 //filePath=filePath.replace("/", "\\\\");
							    	 
							    	 trueNewFile=filePath+"true-proxy-"+dateAndTime+fileName;
									 //falseNewFile=filePath+"false-proxy"+dateAndTime+fileName;
									 
									 //System.out.println("1--"+filePath);
									 //System.out.println("--"+falseNewFile);
									 
									 WriteFile.createFile(trueNewFile);
									 //WriteFile.createFile(falseNewFile);
									 
									 newFile=new String[2];
									 newFile[0]=trueNewFile;
									 newFile[1]=falseNewFile;

									int proxyPortStrListSize = proxyPortStrList.size();
									proxySumLabel.setText(proxyPortStrListSize + "");
									// 把字符拆成5个//1-173.234.50.215-1080-user001-123456
									for (int i = 0; i < proxyPortStrListSize; i++) {
										proxyEntiy = new ProxyEntity();
										proxyPortStr = proxyPortStrList.get(i);
										String proxy[] = proxyPortStr.split("-");
										if (proxy.length > -1) {
											proxyEntiy.setProxyType(Integer.parseInt(proxy[0]));
											proxyEntiy.setProxyHost(proxy[1]);
											proxyEntiy.setProxyPort(proxy[2]);
											proxyEntiy.setProxyAccount(proxy[3]);
											proxyEntiy.setProxyPassword(proxy[4]);
											getHtmlArry = proxyCheck.start(proxyEntiy, outNetAddress,checkTime, testNetAddress,newFile);
											if (getHtmlArry.length < 0) {
												JOptionPane.showMessageDialog(
														ProxyCheckPanel.this, "测试参数错误或网络不通！",
														"系统提示", JOptionPane.WARNING_MESSAGE);
											} else {
												msgTextArea.append(getHtmlArry[1]);
												msgTextArea.setCaretPosition(msgTextArea.getText().length() - 1);
												if("true".equals(getHtmlArry[0])){
													proxySucceeCount++;
													proxySucceeLabel.setText(proxySucceeCount+"");
												}else{
													proxyFailureCount++;
													proxyFailureLabel.setText(proxyFailureCount+"");
												}
											}
										}
									}
									filePathStr="";
								}
							} catch (IOException e) {
								JOptionPane.showMessageDialog(
										ProxyCheckPanel.this, "读取文件错误！",
										"系统提示", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}
						}
						proxyStateLabel.setText(Constants.stop);
		            }
		        }).start();
			}
		});
		proxyBatchCheckButton.setBounds(260, 37, 95, 25);
		panel_4.add(proxyBatchCheckButton);

		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("\u5355\u4E2A\u68C0\u6D4B", Constants.checkIcon, panel_5, null);
		panel_5.setLayout(null);

		JLabel label_7 = new JLabel("\u6D4B\u8BD5\u4EE3\u7406");
		label_7.setBounds(10, 10, 54, 15);
		panel_5.add(label_7);

		proxyHostTextField = new JTextField();
		proxyHostTextField.setText("173.234.50.210");
		proxyHostTextField.setBounds(66, 10, 123, 21);
		panel_5.add(proxyHostTextField);
		proxyHostTextField.setColumns(10);

		proxyServerSingleCheckButton = new JButton("\u68C0\u6D4B",Constants.checkBtnIcon);
		// 单个检测
		proxyServerSingleCheckButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
		            public void run() {
		            	proxyStateLabel.setText(Constants.runing);
		            	proxyServerSingleCheckButton.setEnabled(false);
						outNetButton.setEnabled(false);
						proxyBatchCheckButton.setEnabled(false);
						
						proxyCheck = new ProxyCheck();
						proxyEntiy = new ProxyEntity();

						// 得到测试参数
						proxyHost = proxyHostTextField.getText();
						proxyPort = proxyPortTextField.getText();
						proxyAccount = proxyAccountTextField.getText();
						proxyPassword = proxyPasswordTextField.getText();

						proxyType = 1;
						if (proxyHttpTypeRdb.isSelected()) {
							proxyType = 0;
						}
						testNetAddress = testNetAddressTextField.getText();
						checkTime = checkTimeTextField.getText();
						outNetAddress = outNetAddressTextField.getText();
						if ("".equals(proxyHost) || "".equals(proxyPort)
								|| "".equals(proxyAccount) || "".equals(proxyPassword)
								|| "".equals(testNetAddress) || "".equals(checkTime)
								|| "".equals(outNetAddress)) {
							JOptionPane.showMessageDialog(ProxyCheckPanel.this,
									"检测参数不能为空！", "系统提示",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							proxyEntiy.setProxyHost(proxyHost);
							proxyEntiy.setProxyPort(proxyPort);
							proxyEntiy.setProxyAccount(proxyAccount);
							proxyEntiy.setProxyPassword(proxyPassword);
							proxyEntiy.setProxyType(proxyType);

							// System.out.println(proxyEntiy.getProxyHost()+":"+proxyEntiy.getProxyPort()+":"+proxyEntiy.getProxyAccount()+":"+proxyEntiy.getProxyPassword());
							try {
								getHtmlArry = proxyCheck.start(proxyEntiy, outNetAddress,
										checkTime, testNetAddress,null);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (getHtmlArry.length < 0) {
								JOptionPane.showMessageDialog(
										ProxyCheckPanel.this, "测试参数错误或网络不通！",
										"系统提示", JOptionPane.INFORMATION_MESSAGE);
							} else {
								msgTextArea.setText(getHtmlArry[1]);
							}
						}
						proxyStateLabel.setText(Constants.stop);
		            }
				 }).start();
				
				
				// TestEntiy testEntiy = new TestEntiy();
				// testEntiy.create();

				// ArrayList<ProxyEntiy> list = new ArrayList<ProxyEntiy>();
				// list = testEntiy.list;
				// ProxyCheck proxyCheck = new ProxyCheck();
				// for (int i = 0; i < list.size(); i++) {

				// }
			}
		});
		proxyServerSingleCheckButton.setBounds(293, 65, 95, 25);
		panel_5.add(proxyServerSingleCheckButton);

		JLabel lblNewLabel_1 = new JLabel("\u8D26\u53F7");
		lblNewLabel_1.setBounds(10, 44, 54, 15);
		panel_5.add(lblNewLabel_1);

		proxyAccountTextField = new JTextField();
		proxyAccountTextField.setText("user001");
		proxyAccountTextField.setBounds(66, 37, 123, 21);
		panel_5.add(proxyAccountTextField);
		proxyAccountTextField.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("\u5BC6\u7801");
		lblNewLabel_2.setBounds(205, 44, 54, 15);
		panel_5.add(lblNewLabel_2);

		proxyPasswordTextField = new JTextField();
		proxyPasswordTextField.setText("123456");
		proxyPasswordTextField.setColumns(10);
		proxyPasswordTextField.setBounds(265, 38, 123, 21);
		panel_5.add(proxyPasswordTextField);

		proxyHttpTypeRdb = new JRadioButton("HTTP");
		proxyHttpTypeRdb.setEnabled(false);
		proxyHttpTypeRdb.setBounds(113, 65, 76, 23);
		panel_5.add(proxyHttpTypeRdb);

		proxyScoketTypeRdb = new JRadioButton("SCOKET");
		proxyScoketTypeRdb.setSelected(true);
		proxyScoketTypeRdb.setBounds(10, 65, 76, 23);
		panel_5.add(proxyScoketTypeRdb);

		proxyTypeBtnBg.add(proxyHttpTypeRdb);
		proxyTypeBtnBg.add(proxyScoketTypeRdb);

		JLabel label_9 = new JLabel("\u7AEF\u53E3");
		label_9.setBounds(205, 16, 54, 15);
		panel_5.add(label_9);

		proxyPortTextField = new JTextField();
		proxyPortTextField.setText("1080");
		proxyPortTextField.setBounds(265, 10, 123, 21);
		panel_5.add(proxyPortTextField);
		proxyPortTextField.setColumns(10);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(SystemColor.activeCaption));
		panel_3.setBounds(421, 10, 280, 126);
		panel.add(panel_3);
		panel_3.setLayout(null);

		JLabel label_1 = new JLabel("\u68C0\u6D4B\u53C2\u6570\u8BBE\u7F6E");
		label_1.setFont(new Font("SimSun", Font.BOLD, 12));
		label_1.setBounds(10, 3, 87, 15);
		panel_3.add(label_1);

		JLabel label_2 = new JLabel("\u6D4B\u8BD5\u7F51\u5740");
		label_2.setBounds(10, 24, 97, 15);
		panel_3.add(label_2);

		testNetAddressTextField = new JTextField();
		testNetAddressTextField.setText("http://iframe.ip138.com/ic.asp");
		testNetAddressTextField.setBounds(60, 24, 210, 21);
		panel_3.add(testNetAddressTextField);
		testNetAddressTextField.setColumns(10);

		JLabel label_4 = new JLabel("\u95F4\u9694\u65F6\u95F4");
		label_4.setBounds(10, 45, 54, 15);
		panel_3.add(label_4);

		checkTimeTextField = new JTextField();
		checkTimeTextField.setText("5");
		checkTimeTextField.setBounds(60, 45, 210, 21);
		panel_3.add(checkTimeTextField);
		checkTimeTextField.setColumns(10);

		outNetAddressTextField = new JTextField();
		outNetAddressTextField.setText("58.20.53.243");
		outNetAddressTextField.setBounds(60, 66, 110, 25);
		panel_3.add(outNetAddressTextField);
		outNetAddressTextField.setColumns(10);

		JLabel label_8 = new JLabel("\u5916\u7F51\u5730\u5740");
		label_8.setBounds(10, 66, 54, 15);
		panel_3.add(label_8);

		outNetButton = new JButton("\u5916\u7F51\u5730\u5740");
		// 外网地址
		outNetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String testNetAddress = testNetAddressTextField.getText();
				String getHtmlStr = GetHtmlUtil.getHtml(testNetAddress);
				if (getHtmlStr == null) {
					JOptionPane.showMessageDialog(ProxyCheckPanel.this,
							"测试地址错误或网络不通！", "系统提示",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					msgTextArea.setText(getHtmlStr);
				}
			}
		});
		outNetButton.setBounds(176, 68, 95, 25);
		panel_3.add(outNetButton);
		
		JButton EnabledButton = new JButton("\u542F\u7528\u68C0\u6D4B");
		EnabledButton.setFont(new Font("SimSun", Font.BOLD, 12));
		EnabledButton.setForeground(new Color(255, 0, 0));
		EnabledButton.setBounds(10, 97, 95, 25);
		panel_3.add(EnabledButton);
		//启用检测
		EnabledButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				proxyBatchCheckButton.setEnabled(true);
				outNetButton.setEnabled(true);
				proxyServerSingleCheckButton.setEnabled(true);
				proxySumLabel.setText("0");
				proxySucceeLabel.setText("0");
				proxyFailureLabel.setText("0");
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

		proxySumLabel = new JLabel("0");
		proxySumLabel.setForeground(Color.RED);
		proxySumLabel.setFont(new Font("SimSun", Font.BOLD, 12));
		proxySumLabel.setBounds(70, 34, 54, 15);
		panel_6.add(proxySumLabel);
		
		proxySucceeLabel = new JLabel("0");
		proxySucceeLabel.setForeground(Color.RED);
		proxySucceeLabel.setFont(new Font("SimSun", Font.BOLD, 12));
		proxySucceeLabel.setBounds(70, 55, 54, 15);
		panel_6.add(proxySucceeLabel);
		
		proxyFailureLabel = new JLabel("0");
		proxyFailureLabel.setForeground(Color.RED);
		proxyFailureLabel.setFont(new Font("SimSun", Font.BOLD, 12));
		proxyFailureLabel.setBounds(70, 76, 54, 15);
		panel_6.add(proxyFailureLabel);
		
		proxyStateLabel = new JLabel("\u505C\u6B62");
		proxyStateLabel.setForeground(Color.RED);
		proxyStateLabel.setFont(new Font("SimSun", Font.BOLD, 12));
		proxyStateLabel.setBounds(70, 97, 54, 15);
		panel_6.add(proxyStateLabel);

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
