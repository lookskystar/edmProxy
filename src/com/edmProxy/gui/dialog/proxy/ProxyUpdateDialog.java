package com.edmProxy.gui.dialog.proxy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;

import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.gui.BaseFrame;
import com.edmProxy.gui.panel.proxy.ProxyButtonColumn;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProxyUpdateDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField proxyHostTextField;
	private JTextField proxyPortTextField;
	private JTextField proxyAccountTextField;
	private JTextField proxyPasswordTextField;
	private JRadioButton proxyHttpTypeRdb;
	private JRadioButton proxyScoketTypeRdb;
	private JRadioButton proxyValidRdb;
	private JRadioButton proxyNoValidRdb;
	private JRadioButton proxyNoStartRdb;
	private JRadioButton proxyStartRdb;
	private JTextPane remarkTextPane;
	
	private ProxyEntity proxyEntityTemp;
	private ProxyDAO  proxyServerDAO;
	
	private static ButtonGroup proxyTypeBtnBg = new ButtonGroup();
	private static ButtonGroup proxyValidBtnBg = new ButtonGroup();
	private static ButtonGroup proxyStartBtnBg = new ButtonGroup();
	
	private String idStr;

	public static void main(String[] args) {
		try {
			ProxyUpdateDialog dialog = new ProxyUpdateDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ProxyEntity proxyEntity;
	public ProxyUpdateDialog (ProxyEntity proxyEntity) {
		this.proxyEntity=proxyEntity;
		
		this.setTitle("EDM系统  v1.0 --修改代理服务器");
		setBounds(300, 240, 750, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel label = new JLabel("\u4EE3\u7406\u4E3B\u673A");
		label.setBounds(98, 40, 85, 15);
		contentPanel.add(label);
		
		proxyHostTextField = new JTextField();
		proxyHostTextField.setEnabled(false);
		proxyHostTextField.setColumns(10);
		proxyHostTextField.setBounds(186, 40, 229, 21);
		contentPanel.add(proxyHostTextField);
		
		proxyPortTextField = new JTextField();
		proxyPortTextField.setColumns(10);
		proxyPortTextField.setBounds(450, 40, 66, 21);
		contentPanel.add(proxyPortTextField);
		
		JLabel label_1 = new JLabel("\u7AEF\u53E3");
		label_1.setBounds(421, 40, 46, 15);
		contentPanel.add(label_1);
		
		JLabel label_2 = new JLabel("\u4EE3\u7406\u7C7B\u578B");
		label_2.setBounds(98, 73, 85, 15);
		contentPanel.add(label_2);
		
		proxyScoketTypeRdb = new JRadioButton("SCOKET");
		proxyScoketTypeRdb.setSelected(true);
		proxyScoketTypeRdb.setBounds(185, 73, 121, 23);
		contentPanel.add(proxyScoketTypeRdb);
		
		proxyHttpTypeRdb = new JRadioButton("HTTP");
		proxyHttpTypeRdb.setBounds(309, 73, 121, 23);
		contentPanel.add(proxyHttpTypeRdb);
		
		JLabel label_3 = new JLabel("\u8D26    \u53F7");
		label_3.setBounds(98, 103, 85, 15);
		contentPanel.add(label_3);
		
		proxyAccountTextField = new JTextField();
		proxyAccountTextField.setColumns(10);
		proxyAccountTextField.setBounds(186, 103, 229, 21);
		contentPanel.add(proxyAccountTextField);
		
		proxyPasswordTextField = new JTextField();
		proxyPasswordTextField.setColumns(10);
		proxyPasswordTextField.setBounds(186, 137, 229, 21);
		contentPanel.add(proxyPasswordTextField);
		
		JLabel label_4 = new JLabel("\u5BC6    \u7801");
		label_4.setBounds(98, 137, 54, 15);
		contentPanel.add(label_4);
		
		JLabel label_5 = new JLabel("\u662F\u5426\u6709\u6548");
		label_5.setBounds(98, 173, 54, 15);
		contentPanel.add(label_5);
		
		proxyValidRdb = new JRadioButton("\u6709\u6548");
		proxyValidRdb.setSelected(true);
		proxyValidRdb.setBounds(185, 173, 121, 23);
		contentPanel.add(proxyValidRdb);
		
		proxyNoValidRdb = new JRadioButton("\u65E0\u6548");
		proxyNoValidRdb.setBounds(309, 173, 121, 23);
		contentPanel.add(proxyNoValidRdb);
		
		proxyNoStartRdb = new JRadioButton("\u5173\u95ED");
		proxyNoStartRdb.setBounds(309, 213, 121, 23);
		contentPanel.add(proxyNoStartRdb);
		
		proxyStartRdb = new JRadioButton("\u5F00\u542F");
		proxyStartRdb.setSelected(true);
		proxyStartRdb.setBounds(185, 213, 121, 23);
		contentPanel.add(proxyStartRdb);
		
		JLabel label_6 = new JLabel("\u662F\u5426\u5F00\u542F");
		label_6.setBounds(98, 213, 54, 15);
		contentPanel.add(label_6);
		
		JLabel label_7 = new JLabel("\u5907    \u6CE8");
		label_7.setBounds(98, 255, 54, 15);
		contentPanel.add(label_7);
		
		remarkTextPane = new JTextPane();
		remarkTextPane.setBounds(186, 252, 514, 96);
		contentPanel.add(remarkTextPane);
		
		JButton updateButton = new JButton("\u4FEE\u6539");
		//修改
		updateButton.addActionListener(new ActionListener() {
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
				
				if("".equals(idStr)||"".equals(proxyHost)||"".equals(proxyPort)||"".equals(proxyAccount)||"".equals(proxyPassword)){
					JOptionPane.showMessageDialog(
							ProxyUpdateDialog.this,
							"数据不能为空", "系统提示",
							JOptionPane.INFORMATION_MESSAGE);
				}else{
					proxyEntityTemp=new ProxyEntity();
					proxyEntityTemp.setId(Integer.parseInt(idStr));
					proxyEntityTemp.setProxyHost(proxyHost);
					proxyEntityTemp.setProxyPort(proxyPort);
					proxyEntityTemp.setProxyType(proxyType);
					proxyEntityTemp.setProxyAccount(proxyAccount);
					proxyEntityTemp.setProxyPassword(proxyPassword);
					proxyEntityTemp.setValid(valid);
					proxyEntityTemp.setStart(start);
					proxyEntityTemp.setRemark(remark);
					
					proxyServerDAO=new ProxyDAO();
					int count=proxyServerDAO.update(proxyEntityTemp);
					if(count>0){
						JOptionPane.showMessageDialog(
								ProxyUpdateDialog.this,
								"修改成功,请更新TABLE查看！", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
					}else{
						JOptionPane.showMessageDialog(
								ProxyUpdateDialog.this,
								"修改失败！", "系统提示",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		updateButton.setBounds(608, 384, 95, 25);
		contentPanel.add(updateButton);
		
		
		proxyTypeBtnBg.add(proxyScoketTypeRdb); 
		proxyTypeBtnBg.add(proxyHttpTypeRdb); 
		proxyValidBtnBg.add(proxyValidRdb); 
		proxyValidBtnBg.add(proxyNoValidRdb); 
		proxyStartBtnBg.add(proxyStartRdb); 
		proxyStartBtnBg.add(proxyNoStartRdb); 
		
		toDialogValue();
	}
	
	
	public void toDialogValue(){
		idStr=proxyEntity.getId()+"";
		proxyHostTextField.setText(proxyEntity.getProxyHost());
		proxyPortTextField.setText(proxyEntity.getProxyPort());
		proxyAccountTextField.setText(proxyEntity.getProxyAccount());
		proxyPasswordTextField.setText(proxyEntity.getProxyPassword());
		if(proxyEntity.getProxyType()==1){
			proxyScoketTypeRdb.setSelected(true);
		}else{
			proxyHttpTypeRdb.setSelected(true);
		}
		if(proxyEntity.getValid()==1){
			proxyValidRdb.setSelected(true);
		}else{
			proxyNoValidRdb.setSelected(true);
		}
		if(proxyEntity.getStart()==1){
			proxyStartRdb.setSelected(true);
		}else{
			proxyNoStartRdb.setSelected(true);
		}
		remarkTextPane.setText(proxyEntity.getRemark());
	}
}
