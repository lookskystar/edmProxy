package com.edmProxy.gui.panel.receive;

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
import com.edmProxy.dao.ReceiveDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.ReceiveEntity;
import com.edmProxy.entity.TestEntiy;
import com.edmProxy.util.CheckParameterUtil;
import com.edmProxy.util.JFileChooserFileFilter;
import com.edmProxy.util.ReadFile;
import com.edmProxy.util.proxy.check.ProxyCheck;


import javax.swing.JTextPane;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
//代理添加
public class ReceiveInsertPanel extends JPanel {
	private JTextField filePathTextField;

	private ReceiveEntity receiveEntity;
	private JTextField receiveTextField;
	
	private ReceiveDAO  receiveDAO;
	private JTextPane remarkTextPane;
	private JFileChooser jFileChooser;
	private ArrayList<String> receiveStrList;
	private ArrayList<ReceiveEntity> receiveList;
	private JTextField postTextField;
	private JButton saveButton;

	public ReceiveInsertPanel() {
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
						.showOpenDialog(ReceiveInsertPanel.this);
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
					JOptionPane.showMessageDialog(ReceiveInsertPanel.this,
							"选择不能为空！", "系统提示", JOptionPane.INFORMATION_MESSAGE);
				}else{
					try {
						receiveStrList=new ArrayList<String>();
						receiveList=new ArrayList<ReceiveEntity>();
						receiveStrList=ReadFile.ReadTxt(filePath);
						//dtnilans@sina.com-sayler8390
						//System.out.println("accoutStrList="+receiveStrList.size());
						int j=0;
						String msg="";
						if(receiveStrList.size()>-1){
							for (int i = 0; i < receiveStrList.size(); i++) {
								String arr[]=receiveStrList.get(i).split("-");
								ReceiveEntity receiveEntity=new ReceiveEntity();
								if(arr.length>-1){
									if(CheckParameterUtil.checkEmail(arr[0])){
										receiveEntity.setReceive(arr[0]);
										receiveEntity.setPost(arr[0].substring(arr[0].indexOf("@")+1,arr[0].length()));
										receiveList.add(receiveEntity);
									}else{
										j++;
									}
								}
							}
							receiveDAO=new ReceiveDAO();
							try {
								msg=receiveDAO.InsertBatchNotRepetitionByAccount(receiveList);
								msg+="，入库前，收件地址错误"+j+"个";
							} catch (SQLException e) {
								JOptionPane.showMessageDialog(
										ReceiveInsertPanel.this,
										"批量插入错误SQL", "系统提示",
										JOptionPane.INFORMATION_MESSAGE);
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								JOptionPane.showMessageDialog(
										ReceiveInsertPanel.this,
										"批量插入错误ClassNotFound", "系统提示",
										JOptionPane.INFORMATION_MESSAGE);
								e.printStackTrace();
							}
							JOptionPane.showMessageDialog(
									ReceiveInsertPanel.this,
									msg, "系统提示",
									JOptionPane.INFORMATION_MESSAGE);
							reset();
						}
					} catch (IOException e) {
						JOptionPane.showMessageDialog(
								ReceiveInsertPanel.this, "读取文件错误！",
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
		
		JLabel label_3 = new JLabel("\u6536\u4EF6\u90AE\u7BB1");
		label_3.setBounds(93, 98, 85, 15);
		SinglePanel.add(label_3);
		
		JLabel lblNewLabel = new JLabel("\u5907    \u6CE8");
		lblNewLabel.setBounds(93, 148, 54, 15);
		SinglePanel.add(lblNewLabel);
		
		receiveTextField = new JTextField();
		//当失去焦点，自动输入邮局事件
		receiveTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String receive=receiveTextField.getText().trim();
				
				if(CheckParameterUtil.checkEmail(receive)){
					receiveDAO=new ReceiveDAO();
					receiveEntity=new ReceiveEntity();
					receiveEntity=receiveDAO.findByReceive(receive);
					if(receiveEntity.getReceive()==null){
						String post=receive.substring(receive.indexOf("@")+1,receive.length());
						postTextField.setText(post);
						saveButton.setEnabled(true);
					}else{
						JOptionPane.showMessageDialog(
								ReceiveInsertPanel.this,
								"账号存在", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(
							ReceiveInsertPanel.this,
							"账号格式不正确", "系统提示",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		receiveTextField.setBounds(181, 98, 229, 21);
		SinglePanel.add(receiveTextField);
		receiveTextField.setColumns(10);
		
		remarkTextPane = new JTextPane();
		remarkTextPane.setBounds(181, 148, 514, 96);
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
				String receive=receiveTextField.getText();

				String post=postTextField.getText();
				String remark=remarkTextPane.getText();
			
				
				
				
				if("".equals(receive)||"".equals(post)){
					JOptionPane.showMessageDialog(
							ReceiveInsertPanel.this,
							"数据不能为空", "系统提示",
							JOptionPane.INFORMATION_MESSAGE);
				}else{
					receiveEntity=new ReceiveEntity();
					receiveEntity.setReceive(receive);
					receiveEntity.setPost(post);

					receiveEntity.setRemark(remark);
					
					receiveDAO=new ReceiveDAO();
					int count=receiveDAO.insert(receiveEntity);
					if(count>0){
						JOptionPane.showMessageDialog(
								ReceiveInsertPanel.this,
								"添加成功", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
						reset();
					}else{
						JOptionPane.showMessageDialog(
								ReceiveInsertPanel.this,
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
		this.receiveTextField.setText("");
		this.remarkTextPane.setText("");
	}
}
