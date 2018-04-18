package com.edmProxy.gui.dialog.account;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;


import com.edmProxy.dao.AccountDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.gui.BaseFrame;
import com.edmProxy.util.CheckParameterUtil;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class AccountUpdateDialog extends JDialog {

	private JPanel contentPanel = new JPanel();
	
	

	
	private JTextField accountTextField;
	private JTextField passwordTextField;
	
	private static ButtonGroup validBtnBg = new ButtonGroup();
	private static ButtonGroup startBtnBg = new ButtonGroup();

	
	private AccountDAO  accountDAO;
	private ArrayList<String> accoutStrList;
	private ArrayList<AccountEntity> accoutList;
	private JTextField postTextField;
	private JRadioButton validRdb;
	private JRadioButton noValidRdb;
	private JRadioButton startRdb;
	private JRadioButton noStartRdb;
	private JTextPane remarkTextPane;
	
	private String idStr;

	public static void main(String[] args) {
		try {
			AccountUpdateDialog dialog = new AccountUpdateDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private AccountEntity accountEntity;
	private AccountEntity accountEntityTemp;
	public AccountUpdateDialog (AccountEntity accountEntity) {
	this.accountEntity=accountEntity;
		
		this.setTitle("EDM系统  v1.0 --修改账号");
		setBounds(300, 240, 750, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel label = new JLabel("\u8D26    \u53F7");
		label.setBounds(48, 41, 85, 15);
		contentPanel.add(label);
		
		accountTextField = new JTextField();
		accountTextField.setEnabled(false);
		//失去焦点
		accountTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String account=accountTextField.getText().trim();
				
				if(CheckParameterUtil.checkEmail(account)){
					accountDAO=new AccountDAO();
//					//accountEntity=new AccountEntity();
//					accountEntity=accountDAO.findByAccount(account);
//					if(accountEntity.getAccount()==null){
						String post=account.substring(account.indexOf("@")+1,account.length());
						postTextField.setText(post);
//						saveButton.setEnabled(true);
//					}else{
//						JOptionPane.showMessageDialog(
//								AccountUpdateDialog.this,
//								"账号存在", "系统提示",
//								JOptionPane.INFORMATION_MESSAGE);
//					}
				}else{
					JOptionPane.showMessageDialog(
							AccountUpdateDialog.this,
							"账号格式不正确", "系统提示",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		accountTextField.setColumns(10);
		accountTextField.setBounds(135, 41, 229, 21);
		contentPanel.add(accountTextField);
		
		JLabel label_1 = new JLabel("\u90AE\u5C40");
		label_1.setBounds(389, 47, 85, 15);
		contentPanel.add(label_1);
		
		postTextField = new JTextField();
		postTextField.setEnabled(false);
		postTextField.setColumns(10);
		postTextField.setBounds(427, 42, 121, 21);
		contentPanel.add(postTextField);
		
		JLabel label_2 = new JLabel("\u5BC6    \u7801");
		label_2.setBounds(47, 75, 54, 15);
		contentPanel.add(label_2);
		
		passwordTextField = new JTextField();
		passwordTextField.setColumns(10);
		passwordTextField.setBounds(135, 75, 229, 21);
		contentPanel.add(passwordTextField);
		
		JLabel label_3 = new JLabel("\u662F\u5426\u6709\u6548");
		label_3.setBounds(47, 111, 54, 15);
		contentPanel.add(label_3);
		
		JLabel label_4 = new JLabel("\u662F\u5426\u5F00\u542F");
		label_4.setBounds(47, 151, 54, 15);
		contentPanel.add(label_4);
		
		JLabel label_5 = new JLabel("\u5907    \u6CE8");
		label_5.setBounds(47, 209, 54, 15);
		contentPanel.add(label_5);
		
		validRdb = new JRadioButton("\u6709\u6548");
		validRdb.setBounds(135, 107, 121, 23);
		contentPanel.add(validRdb);
		
		noValidRdb = new JRadioButton("\u65E0\u6548");
		noValidRdb.setBounds(259, 107, 121, 23);
		contentPanel.add(noValidRdb);
		validBtnBg.add(validRdb);
		validBtnBg.add(noValidRdb);
		
		startRdb = new JRadioButton("\u5F00\u542F");
		startRdb.setBounds(135, 143, 121, 23);
		contentPanel.add(startRdb);
		
		noStartRdb = new JRadioButton("\u5173\u95ED");
		noStartRdb.setBounds(259, 143, 121, 23);
		contentPanel.add(noStartRdb);
		startBtnBg.add(startRdb);
		startBtnBg.add(noStartRdb);
		
		
		remarkTextPane = new JTextPane();
		remarkTextPane.setBounds(135, 209, 544, 135);
		contentPanel.add(remarkTextPane);
		
		JButton udapteButton = new JButton("\u4FEE\u6539");
		//修改
		udapteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String account=accountTextField.getText();
				String post=postTextField.getText();
				String password=passwordTextField.getText();
				int valid=1;
				int start=1;
				if(noValidRdb.isSelected()){
					valid=0;
				}
				if(noStartRdb.isSelected()){
					start=0;
				}
				String remark=remarkTextPane.getText();
				
				if("".equals(idStr)||"".equals(account)||"".equals(post)||"".equals(password)){
					JOptionPane.showMessageDialog(
							AccountUpdateDialog.this,
							"数据不能为空", "系统提示",
							JOptionPane.INFORMATION_MESSAGE);
				}else{
					accountEntityTemp=new AccountEntity();
					accountEntityTemp.setId(Integer.parseInt(idStr));
					accountEntityTemp.setAccount(account);
					accountEntityTemp.setPassword(password);
					accountEntityTemp.setPost(post);
					accountEntityTemp.setValid(valid);
					accountEntityTemp.setStart(start);
					accountEntityTemp.setRemark(remark);
					
					accountDAO=new AccountDAO();
					int count=accountDAO.update(accountEntityTemp);
					if(count>0){
						JOptionPane.showMessageDialog(
								AccountUpdateDialog.this,
								"修改成功,请更新TABLE查看！", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
					}else{
						JOptionPane.showMessageDialog(
								AccountUpdateDialog.this,
								"修改失败！", "系统提示",
								JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		});
		udapteButton.setBounds(637, 388, 95, 25);
		contentPanel.add(udapteButton);
		
		toDialogValue();
	}
	
	
	public void toDialogValue(){
		idStr=accountEntity.getId()+"";
		accountTextField.setText(accountEntity.getAccount());
		postTextField.setText(accountEntity.getPost());
		passwordTextField.setText(accountEntity.getPassword());
		if(accountEntity.getValid()==1){
			validRdb.setSelected(true);
		}else{
			noValidRdb.setSelected(true);
		}
		if(accountEntity.getStart()==1){
			startRdb.setSelected(true);
		}else{
			noStartRdb.setSelected(true);
		}
		remarkTextPane.setText(accountEntity.getRemark());
	}
}
