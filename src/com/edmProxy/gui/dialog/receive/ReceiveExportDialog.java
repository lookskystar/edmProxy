package com.edmProxy.gui.dialog.receive;

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
import javax.swing.UIManager;

import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.ReceiveEntity;
import com.edmProxy.gui.BaseFrame;
import com.edmProxy.gui.panel.proxy.ProxyButtonColumn;
import com.edmProxy.util.JFileChooserFileFilter;
import com.edmProxy.util.WriteFile;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.Timer;

import javax.swing.JProgressBar;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.Font;

public class ReceiveExportDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private JFileChooser jFileChooser;
	private JLabel percentageLabel;

	private double percentage;
	private JProgressBar progressBar;
	private Timer timer;
	private int i=0;
	
	public static void main(String[] args) {
		try {
			ReceiveExportDialog dialog = new ReceiveExportDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JTextField fileNameTextField;
	private String fileName;
	private String fileNamePath;
	private JFileChooserFileFilter filter;
	private int result;
	private File file;
	private boolean flag;
	private boolean threadFlag=false;
	private int value=0;

	public ReceiveExportDialog(final ArrayList<ReceiveEntity> list) {

		this.setTitle("EDM系统  v1.0 --导出收件邮箱");
		setBounds(300, 240, 380, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JButton selectButton = new JButton("\u9009\u62E9\u8DEF\u5F84-\u5BFC\u51FA\u672C\u9875\u6587\u4EF6");
		selectButton.setForeground(new Color(0, 0, 255));
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				new Thread(new Runnable() {
		            public void run() {
		            	
		        		fileName = fileNameTextField.getText().trim();
						if (!"".equals(fileName)) {
							jFileChooser = new JFileChooser();
							jFileChooser
									.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 只能打开文件夹
							filter = new JFileChooserFileFilter();
							jFileChooser.setFileFilter(filter);
							jFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);// 设置保存对话框
							result = jFileChooser.showDialog(ReceiveExportDialog.this,
									"保存");

							if (result == JFileChooser.APPROVE_OPTION) {
								file = jFileChooser.getSelectedFile();
								fileNamePath=file.getPath()+ "/"+fileName+"-"+list.size()+".txt";
								fileNamePath=fileNamePath.replaceAll("\\\\","/");
								flag = WriteFile.createFile(fileNamePath);
								if (flag) {
//									JOptionPane.showMessageDialog(
//											ReceiveExportDialog.this, "创建完成，请查看是否存在", "系统提示",
//											JOptionPane.INFORMATION_MESSAGE);
									progressBar.setMaximum(100);
									timer=new Timer(100, null);
									
									for (i = 0; i < list.size(); i++) {
										WriteFile.getDataWriteFile(fileNamePath,list.get(i).getReceive());
								        threadFlag=true;
										percentage=((double)i/list.size())*100;
										value=(int)percentage;
								        if (value < 100){
								        	  progressBar.setValue(++value);
								        	  percentageLabel.setText((value++)+"%");
								        }else {
								             timer.stop();
								        }
									}
									progressBar.setValue(100);
									percentageLabel.setText(100+"%");
									if(threadFlag){
										JOptionPane.showMessageDialog(
												ReceiveExportDialog.this, "导出完成，请查看是否存在！", "系统提示",
												JOptionPane.INFORMATION_MESSAGE);
										dispose();
									}
									
								} else {
									JOptionPane.showMessageDialog(
											ReceiveExportDialog.this, "创建失败", "系统提示",
											JOptionPane.WARNING_MESSAGE);
								}
							}
						}else{
							JOptionPane.showMessageDialog(
									ReceiveExportDialog.this, "请输入文件名", "系统提示",
									JOptionPane.WARNING_MESSAGE);
						}
		            }
				}).start();
				
				
			}
		});
		selectButton.setBounds(152, 76, 168, 25);
		contentPanel.add(selectButton);

		fileNameTextField = new JTextField();
		fileNameTextField.setBounds(57, 49, 264, 21);
		contentPanel.add(fileNameTextField);
		fileNameTextField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("\u6587\u4EF6\u540D");
		lblNewLabel.setBounds(10, 50, 54, 15);
		contentPanel.add(lblNewLabel);
		
		progressBar = new JProgressBar();
		progressBar.setForeground(new Color(255, 0, 0));
		progressBar.setBackground(new Color(135, 206, 250));
		progressBar.setBounds(57, 20, 264, 21);
		contentPanel.add(progressBar);
		
		JLabel lblNewLabel_1 = new JLabel("\u8FDB  \u5EA6");
		lblNewLabel_1.setBounds(10, 20, 54, 15);
		contentPanel.add(lblNewLabel_1);
		
		percentageLabel = new JLabel("");
		percentageLabel.setFont(new Font("SimSun", Font.BOLD, 12));
		percentageLabel.setForeground(new Color(255, 0, 0));
		percentageLabel.setBounds(327, 25, 40, 15);
		contentPanel.add(percentageLabel);
		
		JLabel lblNewLabel_2 = new JLabel("\u63D0\u793A\uFF1A\u5BFC\u51FA\u6570\u636E\u65F6\u52FF\u8FDB\u884C\u64CD\u4F5C");
		lblNewLabel_2.setForeground(new Color(255, 0, 0));
		lblNewLabel_2.setToolTipText("\u63D0\u793A\uFF1A\u5BFC\u51FA\u6570\u636E\u65F6\u52FF\u8FDB\u884C\u64CD\u4F5C");
		lblNewLabel_2.setBounds(10, 148, 214, 15);
		contentPanel.add(lblNewLabel_2);

	}
}
