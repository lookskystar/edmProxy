package com.edmProxy.gui.panel.proxy;

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

import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.TestEntiy;
import com.edmProxy.util.CheckParameterUtil;
import com.edmProxy.util.JFileChooserFileFilter;
import com.edmProxy.util.ReadFile;
import com.edmProxy.util.proxy.check.ProxyCheck;


import javax.swing.JTextPane;
//代理添加
public class ProxyInsertPanel extends JPanel {
	private JTextField filePathTextField;
	private ProxyCheck proxyCheck;
	private ProxyEntity proxyEntiy;
	private JTextField proxyHostTextField;
	private JTextField proxyAccountTextField;
	private JTextField proxyPasswordTextField;
	
	private static ButtonGroup proxyTypeBtnBg = new ButtonGroup();
	private static ButtonGroup proxyValidBtnBg = new ButtonGroup();
	private static ButtonGroup proxyStartBtnBg = new ButtonGroup();
	
	private JTextField proxyPortTextField;
	private JRadioButton proxyScoketTypeRdb;
	private JRadioButton proxyHttpTypeRdb;
	private JRadioButton proxyNoStartRdb;
	private JRadioButton proxyStartRdb;
	private JRadioButton proxyValidRdb;
	private JRadioButton proxyNoValidRdb;
	
	private ProxyDAO  proxyServerDAO;
	private JTextPane remarkTextPane;
	private JFileChooser jFileChooser;
	private ArrayList<String> proxyStrList;
	private ArrayList<ProxyEntity> proxyList;
	
	private int result;
	private int j;

	public ProxyInsertPanel() {
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
				result = jFileChooser
						.showOpenDialog(ProxyInsertPanel.this);
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
					JOptionPane.showMessageDialog(ProxyInsertPanel.this,
							"选择不能为空！", "系统提示", JOptionPane.WARNING_MESSAGE);
				}else{
					try {
						proxyStrList=new ArrayList<String>();
						proxyList=new ArrayList<ProxyEntity>();
						proxyStrList=ReadFile.ReadTxt(filePath);
						//1-173.234.50.210-1080-user001-123456
						j=0;
						String msg="";
						if(proxyStrList.size()>0){
							for (int i = 0; i < proxyStrList.size(); i++) {
								String arr[]=proxyStrList.get(i).split("-");
								ProxyEntity proxyEntiy=new ProxyEntity();
								if(arr.length>-1){
									if(CheckParameterUtil.checkIP(arr[1])){
										proxyEntiy.setProxyType(Integer.parseInt(arr[0]));
										proxyEntiy.setProxyHost(arr[1]);
										proxyEntiy.setProxyPort(arr[2]);
										proxyEntiy.setProxyAccount(arr[3]);
										proxyEntiy.setProxyPassword(arr[4]);
										proxyList.add(proxyEntiy);
									}else{
										j++;
									}
								}
							}
							proxyServerDAO=new ProxyDAO();
							try {
								msg=proxyServerDAO.InsertBatchNotRepetitionByProxyHost(proxyList);
								msg+="，入库前，服务器地址错误"+j+"个";
								//System.out.println(msg);
							} catch (SQLException e) {
								JOptionPane.showMessageDialog(
										ProxyInsertPanel.this,
										"批量插入错误SQL", "系统提示",
										JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}catch (ClassNotFoundException e) {
								JOptionPane.showMessageDialog(
										ProxyInsertPanel.this,
										"批量插入错误ClassNotFound", "系统提示",
										JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}
							JOptionPane.showMessageDialog(
									ProxyInsertPanel.this,
									msg, "系统提示",
									JOptionPane.INFORMATION_MESSAGE);
							reset();
						}else{
							JOptionPane.showMessageDialog(
									ProxyInsertPanel.this, "文件格式不正确或没有数据！",
									"系统提示", JOptionPane.ERROR_MESSAGE);
						}
					} catch (IOException e) {
						JOptionPane.showMessageDialog(
								ProxyInsertPanel.this, "读取文件错误！",
								"系统提示", JOptionPane.ERROR_MESSAGE);
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
		
		JLabel label_1 = new JLabel("\u4EE3\u7406\u4E3B\u673A");
		label_1.setBounds(93, 35, 85, 15);
		SinglePanel.add(label_1);
		
		JLabel label_2 = new JLabel("\u4EE3\u7406\u7C7B\u578B");
		label_2.setBounds(93, 68, 85, 15);
		SinglePanel.add(label_2);
		
		JLabel label_3 = new JLabel("\u8D26    \u53F7");
		label_3.setBounds(93, 98, 85, 15);
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
		
		proxyHostTextField = new JTextField();
		proxyHostTextField.setBounds(181, 35, 229, 21);
		SinglePanel.add(proxyHostTextField);
		proxyHostTextField.setColumns(10);
		
		proxyScoketTypeRdb = new JRadioButton("SCOKET");
		proxyScoketTypeRdb.setSelected(true);
		proxyScoketTypeRdb.setBounds(180, 68, 121, 23);
		SinglePanel.add(proxyScoketTypeRdb);
		
		proxyHttpTypeRdb = new JRadioButton("HTTP");
		proxyHttpTypeRdb.setEnabled(false);
		proxyHttpTypeRdb.setBounds(304, 68, 121, 23);
		SinglePanel.add(proxyHttpTypeRdb);
		proxyTypeBtnBg.add(proxyScoketTypeRdb);
		proxyTypeBtnBg.add(proxyHttpTypeRdb);
		
		proxyAccountTextField = new JTextField();
		proxyAccountTextField.setBounds(181, 98, 229, 21);
		SinglePanel.add(proxyAccountTextField);
		proxyAccountTextField.setColumns(10);
		
		proxyPasswordTextField = new JTextField();
		proxyPasswordTextField.setBounds(181, 132, 229, 21);
		SinglePanel.add(proxyPasswordTextField);
		proxyPasswordTextField.setColumns(10);
		
		proxyStartRdb = new JRadioButton("\u5F00\u542F");
		proxyStartRdb.setSelected(true);
		proxyStartRdb.setBounds(180, 208, 121, 23);
		SinglePanel.add(proxyStartRdb);
		
		proxyNoStartRdb = new JRadioButton("\u5173\u95ED");
		proxyNoStartRdb.setBounds(304, 208, 121, 23);
		SinglePanel.add(proxyNoStartRdb);
		proxyStartBtnBg.add(proxyStartRdb);
		proxyStartBtnBg.add(proxyNoStartRdb);
		
		proxyValidRdb = new JRadioButton("\u6709\u6548");
		proxyValidRdb.setSelected(true);
		proxyValidRdb.setBounds(180, 168, 121, 23);
		SinglePanel.add(proxyValidRdb);
		
		proxyNoValidRdb = new JRadioButton("\u65E0\u6548");
		proxyNoValidRdb.setBounds(304, 168, 121, 23);
		SinglePanel.add(proxyNoValidRdb);
		
		proxyValidBtnBg.add(proxyValidRdb);
		proxyValidBtnBg.add(proxyNoValidRdb);
		
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
		
		JButton saveButton = new JButton("\u4FDD\u5B58");
		//单个保存
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String proxyHost=proxyHostTextField.getText();
				String proxyPort=proxyPortTextField.getText();
				String proxyAccount=proxyAccountTextField.getText();
				String proxyPassword=proxyPasswordTextField.getText();
				int proxyType=1;
				int valid=1;
				int start=1;
				if(proxyHttpTypeRdb.isSelected()){
					proxyType=0;
				}
				if(proxyNoValidRdb.isSelected()){
					valid=0;
				}
				if(proxyNoStartRdb.isSelected()){
					start=0;
				}
				String remark=remarkTextPane.getText();
				
				if("".equals(proxyHost)||"".equals(proxyPort)||"".equals(proxyAccount)||"".equals(proxyPassword)){
					JOptionPane.showMessageDialog(
							ProxyInsertPanel.this,
							"数据不能为空", "系统提示",
							JOptionPane.WARNING_MESSAGE);
				}else{
					proxyEntiy=new ProxyEntity();
					proxyServerDAO=new ProxyDAO();
					proxyEntiy=proxyServerDAO.findByProxyHost(proxyHost);
					if(proxyEntiy!=null){
						JOptionPane.showMessageDialog(
								ProxyInsertPanel.this,
								"服务器已存在，请添加新的服务器！", "系统提示",
								JOptionPane.WARNING_MESSAGE);
						reset();
					}else{
						proxyEntiy.setProxyHost(proxyHost);
						proxyEntiy.setProxyPort(proxyPort);
						proxyEntiy.setProxyType(proxyType);
						proxyEntiy.setProxyAccount(proxyAccount);
						proxyEntiy.setProxyPassword(proxyPassword);
						proxyEntiy.setValid(valid);
						proxyEntiy.setStart(start);
						proxyEntiy.setRemark(remark);
						
						int count=proxyServerDAO.insert(proxyEntiy);
						if(count>0){
							JOptionPane.showMessageDialog(
									ProxyInsertPanel.this,
									"添加成功！", "系统提示",
									JOptionPane.INFORMATION_MESSAGE);
							reset();
						}else{
							JOptionPane.showMessageDialog(
									ProxyInsertPanel.this,
									"添加失败！", "系统提示",
									JOptionPane.WARNING_MESSAGE);
						}
						
					}
				}
			}
		});
		saveButton.setBounds(732, 510, 95, 25);
		SinglePanel.add(saveButton);
		
		JLabel lblNewLabel_1 = new JLabel("\u7AEF\u53E3");
		lblNewLabel_1.setBounds(416, 35, 46, 15);
		SinglePanel.add(lblNewLabel_1);
		
		proxyPortTextField = new JTextField();
		proxyPortTextField.setBounds(445, 35, 66, 21);
		SinglePanel.add(proxyPortTextField);
		proxyPortTextField.setColumns(10);
	}
	
	public void reset(){
		this.filePathTextField.setText("");
		this.proxyHostTextField.setText("");
		this.proxyPortTextField.setText("");
		this.proxyAccountTextField.setText("");
		this.proxyPasswordTextField.setText("");
		this.remarkTextPane.setText("");
		this.proxyScoketTypeRdb.setSelected(true);
		this.proxyStartRdb.setSelected(true);
		this.proxyValidRdb.setSelected(true);
		
	}
}
