package com.edmProxy.gui.panel;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.edmProxy.constant.Constants;
import com.edmProxy.dao.AccountDAO; //import com.edmProxy.dao.ProxyServerDAO;
import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.dao.ReceiveDAO;
import com.edmProxy.dao.SendTaskDAO;
import com.edmProxy.dao.StatisticsDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.ReceiveEntity;
import com.edmProxy.entity.SendTaskEntity;
import com.edmProxy.gui.BaseFrame;
import com.edmProxy.gui.MainFrame; //import com.edmProxy.gui.panel.account.ProxyServerAbstractTableModel;
//import com.edmProxy.gui.panel.account.ProxyServerButtonColumn;
import com.edmProxy.gui.dialog.receive.ReceiveExportDialog;
import com.edmProxy.gui.panel.account.AccountAbstractTableModel;
import com.edmProxy.gui.panel.account.AccountButtonColumn;
import com.edmProxy.gui.panel.account.AccountPageing;
import com.edmProxy.gui.panel.proxy.ProxyPageing;
import com.edmProxy.gui.panel.receive.ReceiveAbstractTableModel;
import com.edmProxy.gui.panel.receive.ReceiveButtonColumn;
import com.edmProxy.gui.panel.receive.ReceivePageing;
import com.edmProxy.gui.panel.sendTask.SendTaskAbstractTableModel;
import com.edmProxy.gui.panel.sendTask.SendTaskButtonColumn;
import com.edmProxy.util.GuiUtil;
import com.edmProxy.util.JFileChooserFileFilter;
import com.edmProxy.util.NumberLenghtLimitedDmt;
import com.edmProxy.util.WriteFile;
import com.edmProxy.util.ConsoleToUI.ConsoleTextArea;
import com.edmProxy.util.mail.send.SendMail;
import com.edmProxy.util.mail.send.SendThread;
import com.edmProxy.util.mail.send.TimerChange;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.border.LineBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JTextArea;

public class SendTaskManagePanel extends JPanel {
	private JTable table;
	private JScrollPane scrollPane = null;
	private JCheckBox jCheckBox = null;
	private JPanel tablePanel = null;
	private JPanel tableMainPanel;
	private JFileChooser jFileChooser = null;
	private JTextField jumpPageText;
	private AccountDAO accountDAO;
	private SendTaskDAO sendTaskDAO;
	private StatisticsDAO statisticsDAO;

	private AccountPageing accountPageing;
	private ArrayList<SendTaskEntity> list;
	private JButton delSelectButton;
	private JButton lastPageButton;
	private JButton jumpPageButton;
	private JButton showDataButton;
	private int numPageInt = 0;

	private String selectIdValues = "";

	private int fistPage = 1;
	private int countPage = 1;// 当前页
	private int sumPage;// 总页数
	private int sumNumPage;// 总数据

	private String[] arr;

	private MainFrame source;
	private SendTaskAbstractTableModel sendTaskAbstractTableModel;
	private SendTaskButtonColumn lookButtonColumn;
	private TableColumn tableColumn;
	private JButton sendButton;

	private SendThread sendThread;
	private ArrayList<SendTaskEntity> sendTaskList;

	private SendMail sendMail;
	private ProxyEntity proxyEntity;
	private ProxyDAO proxyDAO;

	private TimerChange timerChange;
	private JScrollPane scrollPane_1;
	

	// private ArrayList<SendTaskEntity> sendTaskList;

	

	public SendTaskManagePanel(final MainFrame source) {
		this.source = source;

		setBorder(null);
		setBackground(SystemColor.activeCaptionBorder);
		this.setBounds(0, 0, 880, 612);
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 568, 881, 44);
		panel.setBorder(new LineBorder(SystemColor.desktop));
		add(panel);
		panel.setLayout(null);

		sendButton = new JButton("\u53D1\u9001");
		// 发送
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					public void run() {
						if ("".equals(selectIdValues)) {
							JOptionPane.showMessageDialog(
									SendTaskManagePanel.this, "请选择任务！", "系统提示",
									JOptionPane.WARNING_MESSAGE);
						} else {
							// 根据数组，查找对应的发送任务，返回list
							sendTaskDAO = new SendTaskDAO();
							selectIdValues = selectIdValues.replace("[", "");
							selectIdValues = selectIdValues.replace("]", "");
							selectIdValues = selectIdValues.substring(0,
									selectIdValues.length() - 1);

							// System.out.println("selectIdValues--->"+selectIdValues);

							list = new ArrayList<SendTaskEntity>();
							list = sendTaskDAO.getListByIds(selectIdValues);
							// 清空选择和UI选择
							selectIdValues = "";
							for (int i = 0; i < list.size(); i++) {
								table.setValueAt(new Boolean(false), i, 13);
							}

							// 得到第一个代理地址
							proxyEntity = new ProxyEntity();
							proxyDAO = new ProxyDAO();
							proxyEntity = proxyDAO.findFistByValid(1);
							
							
						///////第三方类，把控制台信息输出到窗体中
							//JFrame f = new JFrame("邮件发送信息（关闭该窗体则停止发送）");
					        ConsoleTextArea consoleTextArea = null;
					        try {
					            consoleTextArea = new ConsoleTextArea();
					        }
					        catch(IOException e) {
					        	JOptionPane.showMessageDialog(
										SendTaskManagePanel.this, "不能创建监控IO流，程序将退出！",
										"系统提示", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
					            System.err.println(
					                "不能创建LoopedStreams：" + e);
					            System.exit(1);
					        }
					        consoleTextArea.setFont(java.awt.Font.decode("monospaced"));
					        consoleTextArea.setBackground(Color.BLACK);
					        consoleTextArea.setForeground(Color.GREEN);
					        scrollPane_1 = new JScrollPane(consoleTextArea);
					        consoleTextArea.setBounds(0, 0, 880, 235);
							scrollPane_1.setBounds(0, 325, 881, 237);
							add(scrollPane_1);
					        
					        // System.out和System.err输出
					        ///////第三方类，把控制台信息输出到窗体中
							// 根据选择建立线程
							sendThread = new SendThread();
							try {
								sendThread.startThread(list, proxyEntity,
										"http://iframe.ip138.com/ic.asp",
										"58.20.53.243",consoleTextArea);// 启动多线程发送

							} catch (IOException e) {
								JOptionPane.showMessageDialog(
										SendTaskManagePanel.this, "线程启动失败！",
										"系统提示", JOptionPane.WARNING_MESSAGE);
								e.printStackTrace();
							}
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						// 定时读取线程值
//						timerChange = new TimerChange();
//						timerChange.timer(SendTaskManagePanel.this);
//						msgTextArea.updateUI();
					}
				}).start();
				
				System.out.println(">---->发送结束");
			}
		});
		sendButton.setBounds(391, 10, 95, 25);
		panel.add(sendButton);
		
				delSelectButton = new JButton("\u5220\u9664");
				delSelectButton.setBounds(783, 10, 88, 25);
				panel.add(delSelectButton);
				// 删除
				delSelectButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						sendTaskDAO = new SendTaskDAO();
						statisticsDAO=new StatisticsDAO();
						if (!"".equals(selectIdValues)) {
							String temp = "";
							String ids = "";
							arr = selectIdValues.split(",");
							for (int i = 0; i < arr.length; i++) {
								temp = arr[i];
								temp = temp.substring(1, temp.indexOf("]")) + ",";
								ids += temp;
							}
							ids = ids.substring(0, ids.length() - 1);

							int count = sendTaskDAO.deleteBatch(ids);
							
							statisticsDAO.deleteBatchBySendTaskIds(ids);

							list = new ArrayList<SendTaskEntity>();
							list = sendTaskDAO.pageingBypageNowAndpageSize(1, 20);
							createTable(list);
							
//					if (count > 0) {
							JOptionPane.showMessageDialog(SendTaskManagePanel.this,
										"删除任务和统计成功！", "系统提示",
										JOptionPane.INFORMATION_MESSAGE);
//					} else {
//						JOptionPane.showMessageDialog(SendTaskManagePanel.this,
//								"删除失败！", "系统提示", JOptionPane.WARNING_MESSAGE);
//					}

						}
					}
				});
				delSelectButton.setEnabled(false);

		tableMainPanel = new JPanel();
		tableMainPanel.setBounds(1, 37, 880, 282);
		add(tableMainPanel);

		showDataButton = new JButton("\u663E\u793A\u6570\u636E");
		showDataButton.setBounds(8, 6, 95, 25);
		add(showDataButton);
		
				JLabel lblNewLabel = new JLabel("\u53D1\u9001\u4FE1\u606F");
				lblNewLabel.setBounds(816, 325, 54, 15);
				add(lblNewLabel);

		
		// 显示数据
		showDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				list = new ArrayList<SendTaskEntity>();
				sendTaskDAO = new SendTaskDAO();
				list = sendTaskDAO.pageingBypageNowAndpageSize(1, 10);
				createTable(list);

				delSelectButton.setEnabled(true);
			}
		});
	}

	public void createTable(ArrayList<SendTaskEntity> list) {
		table = new JTable();
		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// 不知道什么原因必须这样做，才能显示正常
		tableMainPanel.removeAll();
		tableMainPanel.add(scrollPane);

		sendTaskAbstractTableModel = new SendTaskAbstractTableModel(list);
		sendTaskAbstractTableModel.dataToTable();
		tableMainPanel.setLayout(null);
		table.setModel(sendTaskAbstractTableModel);
		// lookButtonColumn = new SendTaskButtonColumn(table, 13);
		// 获得表格的表格列类
		tableColumn = table.getColumnModel().getColumn(13);
		// 实例化JCheckBox
		jCheckBox = new JCheckBox();
		tableColumn.setCellEditor(new DefaultCellEditor(jCheckBox));
		// 鼠标点击获得表格单元格值
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Object o = e.getSource();
				if (o instanceof JTable) {
					JTable t = (JTable) o;
					// t.setValueAt("13", t.getSelectedRow(),
					// t.getSelectedColumn());
					if (t.isCellEditable(t.getSelectedRow(), t
							.getSelectedColumn())) {
						// System.out.println(jCheckBox.isSelected()+"");
						String id = t.getValueAt(t.getSelectedRow(), 0) + "";
						String temp = "[" + id + "]";
						if (!jCheckBox.isSelected()) {
							// System.out.println("选择
							// -得到id-->"+t.getValueAt(t.getSelectedRow(), 0));
							// System.out.println("<-1-->"+selectIdValues.indexOf(temp));
							if (selectIdValues.indexOf(temp) == -1) {
								selectIdValues = selectIdValues + temp + ",";
							} else {
								// selectIdValues=id;
							}
						} else {
							// System.out.println("不选择
							// -删除id-->"+t.getValueAt(t.getSelectedRow(), 0));
							if (selectIdValues.indexOf(temp) > -1) {
								// System.out.println(">-1-->"+selectIdValues.indexOf(id));
								// System.out.println(id+",");
								selectIdValues = selectIdValues.replace(temp
										+ ",", "");
							} else {
								// selectIdValues=id;
							}
						}
						System.out.println(selectIdValues);
					}
				}
			}
		});

		scrollPane.setBounds(0, 0, 880, 566);
		table.setBounds(1, 1, 879, 556);
		tableMainPanel.add(scrollPane);
		scrollPane.setViewportView(table);
		// scrollPane.updateUI();

	}

	// //测试当线程发送
	// private void send(ArrayList<SendTaskEntity> list) {
	// sendMail=new SendMail();
	//		
	// for (int i = 0; i < list.size(); i++) {
	// sendMail.
	// }
	//		
	// }

}
