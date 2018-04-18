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
import com.edmProxy.dao.ReceiveDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ReceiveEntity;
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
import com.edmProxy.util.GuiUtil;
import com.edmProxy.util.JFileChooserFileFilter;
import com.edmProxy.util.NumberLenghtLimitedDmt;
import com.edmProxy.util.WriteFile;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.swing.border.LineBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class ReceiveManagePanel extends JPanel {
	private JTable table;
	private JScrollPane scrollPane = null;
	private JCheckBox jCheckBox = null;
	private JPanel tablePanel = null;
	private JPanel tableMainPanel;
	private JFileChooser jFileChooser = null;
	private JTextField jumpPageText;
	private JTextField jumpPageTextField;
	private ReceiveDAO receiveDAO;

	private ReceivePageing receivePageing;
	private ArrayList<ReceiveEntity> list;
	private JButton selectAllButton;
	private JButton delSelectButton;
	private JButton fistPageButton;
	private JButton nextPageButton;
	private JButton upPageButton;
	private JButton lastPageButton;
	private JButton jumpPageButton;
	private JButton showDataButton;
	private int numPageInt = 0;

	private String selectIdValues = "";
	private JTextField numPageTextField;
	private JLabel countPageLabel;
	private JLabel sumPageLabel;
	private JLabel sumNumPageLabel;

	private int fistPage = 1;
	private int countPage = 1;// 当前页
	private int sumPage;// 总页数
	private int sumNumPage;// 总数据

	private String[] arr;

	private MainFrame source;
	private ReceiveAbstractTableModel abstractTableModel;
	private ReceiveButtonColumn updateButtonColumn;
	private TableColumn tableColumn;

	private JButton refreshButton;
	private JButton clearButton;
	private JTextField queryTextField;
	private JComboBox conditionComboBox;
	private int condition=0;
	
	private String sqlStr="";
	private String conditionSqlStr="";
	private String query="";

	public ReceiveManagePanel(final MainFrame source) {
		this.source = source;

		setBorder(null);
		setBackground(SystemColor.activeCaptionBorder);
		this.setBounds(0, 0, 880, 612);
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(SystemColor.desktop));
		panel.setBounds(0, 568, 881, 44);
		add(panel);
		panel.setLayout(null);

		fistPageButton = new JButton("\u9996\u9875");
		// 首页
		fistPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				receivePageing = new ReceivePageing();
				list = new ArrayList<ReceiveEntity>();
				list = receivePageing
						.pageingBypageNowAndpageSizeByCondition(1, numPageInt,"");
				createTable(list);
				countPageLabel.setText(1 + "");
				countPage = 1;
			}
		});
		fistPageButton.setEnabled(false);
		fistPageButton.setBounds(10, 11, 70, 25);
		panel.add(fistPageButton);

		nextPageButton = new JButton("\u4E0B\u9875");
		// 下一页
		nextPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println("countPage-->" + countPage);
				if (countPage < sumPage) {
					countPage += 1;
				}
				receivePageing = new ReceivePageing();
				list = new ArrayList<ReceiveEntity>();
				list = receivePageing.pageingBypageNowAndpageSizeByCondition(countPage,
						numPageInt,conditionSqlStr);
				createTable(list);

				if (countPage == sumPage) {
					countPage = sumPage;
					JOptionPane.showMessageDialog(ReceiveManagePanel.this,
							"已到达末页！", "系统提示", JOptionPane.INFORMATION_MESSAGE);
					// return;
				}
				countPageLabel.setText(countPage + "");
			}
		});
		nextPageButton.setEnabled(false);
		nextPageButton.setBounds(86, 11, 70, 25);
		panel.add(nextPageButton);

		upPageButton = new JButton("\u4E0A\u9875");
		// 上页
		upPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (countPage > 1) {
					//System.out.println("countPage-->" + countPage);
					countPage -= 1;
					//System.out.println("countPage-->" + countPage);
				}
				receivePageing = new ReceivePageing();
				list = new ArrayList<ReceiveEntity>();
				list = receivePageing.pageingBypageNowAndpageSizeByCondition(countPage,
						numPageInt,conditionSqlStr);
				createTable(list);
				if (countPage < 1) {
					countPage = 1;
				}
				if (countPage == 1) {
					JOptionPane.showMessageDialog(ReceiveManagePanel.this,
							"已到达首页！", "系统提示", JOptionPane.INFORMATION_MESSAGE);
					// return;
				}
				// System.out.println("countPage---->"+countPage);
				countPageLabel.setText(countPage + "");

			}
		});
		upPageButton.setEnabled(false);
		upPageButton.setBounds(162, 11, 70, 25);
		panel.add(upPageButton);

		lastPageButton = new JButton("\u672B\u9875");
		// 末页
		lastPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				receivePageing = new ReceivePageing();
				list = new ArrayList<ReceiveEntity>();
				list = receivePageing.pageingBypageNowAndpageSizeByCondition(sumPage,
						numPageInt,conditionSqlStr);
				createTable(list);
				countPage = sumPage;
				countPageLabel.setText(sumPage + "");
			}
		});
		lastPageButton.setEnabled(false);
		lastPageButton.setBounds(238, 11, 70, 25);
		panel.add(lastPageButton);

		jumpPageButton = new JButton("\u8DF3\u8F6C");
		// 跳转到
		jumpPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String jumpPageStr = jumpPageTextField.getText();
				if (!"".equals(jumpPageStr)) {
					countPage = Integer.parseInt(jumpPageStr);
				}

				receivePageing = new ReceivePageing();
				list = new ArrayList<ReceiveEntity>();
				list = receivePageing.pageingBypageNowAndpageSizeByCondition(countPage,
						numPageInt,conditionSqlStr);
				createTable(list);

				countPageLabel.setText(countPage + "");
			}
		});
		jumpPageButton.setEnabled(false);
		jumpPageButton.setBounds(314, 11, 70, 25);
		panel.add(jumpPageButton);

		jumpPageTextField = new JTextField();
		jumpPageTextField.setDocument(new NumberLenghtLimitedDmt(10));
		jumpPageTextField.setBounds(390, 12, 34, 25);
		panel.add(jumpPageTextField);
		jumpPageTextField.setColumns(10);

		JLabel lblNewLabel = new JLabel("\u9875");
		lblNewLabel.setBounds(430, 18, 28, 15);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("\u5171\u6709");
		lblNewLabel_1.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_1.setBounds(455, 5, 34, 15);
		panel.add(lblNewLabel_1);

		delSelectButton = new JButton("\u5220\u9664");
		// 删除
		delSelectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				receiveDAO = new ReceiveDAO();
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
					int count = receiveDAO.deleteBatch(ids);
					
					receivePageing = new ReceivePageing();
					list = new ArrayList<ReceiveEntity>();
					list = receivePageing.pageingBypageNowAndpageSizeByCondition(
							countPage, numPageInt,"");
					createTable(list);

					if (count > 0) {
						JOptionPane.showMessageDialog(ReceiveManagePanel.this,
								"删除成功！", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(ReceiveManagePanel.this,
								"删除失败！", "系统提示",
								JOptionPane.WARNING_MESSAGE);
					}

				}
			}
		});
		delSelectButton.setEnabled(false);
		delSelectButton.setBounds(786, 11, 88, 25);
		panel.add(delSelectButton);

		sumNumPageLabel = new JLabel("0");
		sumNumPageLabel.setForeground(Color.RED);
		sumNumPageLabel.setBounds(491, 6, 56, 15);
		panel.add(sumNumPageLabel);

		JLabel lblNewLabel_2 = new JLabel("\u5206");
		lblNewLabel_2.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_2.setBounds(456, 24, 28, 15);
		panel.add(lblNewLabel_2);

		sumPageLabel = new JLabel("0");
		sumPageLabel.setForeground(Color.RED);
		sumPageLabel.setBounds(491, 24, 35, 15);
		panel.add(sumPageLabel);

		JLabel lblNewLabel_3 = new JLabel("\u73B0\u5728\u7B2C");
		lblNewLabel_3.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_3.setBounds(615, 6, 48, 15);
		panel.add(lblNewLabel_3);

		countPageLabel = new JLabel("0");
		countPageLabel.setForeground(Color.RED);
		countPageLabel.setBounds(665, 7, 38, 15);
		panel.add(countPageLabel);

		JLabel lblNewLabel_5 = new JLabel("\u6BCF\u9875");
		lblNewLabel_5.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_5.setBounds(615, 23, 40, 15);
		panel.add(lblNewLabel_5);

		JLabel label = new JLabel("\u6761\u6570\u636E");
		label.setFont(new Font("SimSun", Font.BOLD, 12));
		label.setBounds(553, 5, 54, 15);
		panel.add(label);

		JLabel lblNewLabel_6 = new JLabel("\u9875");
		lblNewLabel_6.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_6.setBounds(553, 21, 54, 15);
		panel.add(lblNewLabel_6);

		JLabel label_1 = new JLabel("\u9875");
		label_1.setFont(new Font("SimSun", Font.BOLD, 12));
		label_1.setBounds(709, 5, 54, 15);
		panel.add(label_1);

		JLabel lblNewLabel_7 = new JLabel("\u6761\u6570\u636E");
		lblNewLabel_7.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_7.setBounds(708, 24, 54, 15);
		panel.add(lblNewLabel_7);

		numPageTextField = new JTextField();
		numPageTextField.setDocument(new NumberLenghtLimitedDmt(10));
		numPageTextField.setForeground(Color.RED);
		numPageTextField.setText("32");
		numPageTextField.setBounds(643, 23, 56, 18);
		panel.add(numPageTextField);
		numPageTextField.setColumns(10);

		tableMainPanel = new JPanel();
		tableMainPanel.setBounds(1, 37, 880, 530);
		add(tableMainPanel);

		showDataButton = new JButton("\u663E\u793A\u6570\u636E");
		showDataButton.setBounds(8, 6, 95, 25);
		add(showDataButton);

		selectAllButton = new JButton("\u5168\u9009");
		// 全选
		selectAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// table.getValueAt(1, 13);
				table.setValueAt(new Boolean(true), 1, 6);
				// System.out.println(table.getValueAt(1,
				// 13)+"--"+table.getRowCount());
				for (int i = 0; i < table.getRowCount(); i++) {
					String temp = "[" + table.getValueAt(i, 0) + "],";
					table.setValueAt(new Boolean(true), i, 6);
					if (selectIdValues.indexOf(temp) > -1) {

					} else {
						selectIdValues += temp;
					}
				}
				// selectIdValues=selectIdValues.substring(0,selectIdValues.length()-1);
				System.out.println(selectIdValues);
			}
		});
		selectAllButton.setBounds(782, 6, 90, 25);
		add(selectAllButton);
		selectAllButton.setEnabled(false);

		refreshButton = new JButton("\u66F4\u65B0TABLE");
		refreshButton.setEnabled(false);
		// 刷新数据
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				receivePageing = new ReceivePageing();
				list = new ArrayList<ReceiveEntity>();
				list = receivePageing.pageingBypageNowAndpageSizeByCondition(countPage,
						numPageInt,conditionSqlStr);
				createTable(list);
			}
		});
		refreshButton.setBounds(109, 7, 94, 24);
		add(refreshButton);

		clearButton = new JButton("\u6E05\u9664\u6240\u6709\u6570\u636E");
		clearButton.setEnabled(false);
		// 清除所有数据
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int flag = JOptionPane.showConfirmDialog(null, "你真的要清空所有数据吗?",
						"系统警告", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (JOptionPane.YES_OPTION == flag) {
					// 删除所有数据
					receiveDAO = new ReceiveDAO();
					int count = receiveDAO.deleteAll();
					if (count > 0) {
						receivePageing = new ReceivePageing();
						list = new ArrayList<ReceiveEntity>();
						list = receivePageing.pageingBypageNowAndpageSizeByCondition(1,
								numPageInt,"");
						createTable(list);

						countPage = 1;// 当前页
						sumPage = 1;// 总页数
						sumNumPage = 0;// 总数据

						sumNumPageLabel.setText(sumNumPage + "");
						sumPageLabel.setText(sumPage + "");
						countPageLabel.setText(countPage + "");
						JOptionPane.showMessageDialog(ReceiveManagePanel.this,
								"清除所有数据成功！", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(ReceiveManagePanel.this,
								"清除所有数据失败！", "系统提示",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		clearButton.setForeground(new Color(204, 0, 0));
		clearButton.setFont(new Font("SimSun", Font.BOLD, 12));
		clearButton.setBounds(209, 7, 133, 24);
		add(clearButton);
		
		JButton queryButton = new JButton("\u67E5\u8BE2");
		queryButton.setBackground(new Color(176, 224, 230));
		//查询
		queryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				query = queryTextField.getText();
				int selectInt=conditionComboBox.getSelectedIndex();
				System.out.println("query------"+query);
				if (!"".equals(query)&&selectInt>0) {
					System.out.println("selectInt---->"+selectInt);
					if(selectInt==1||selectInt==2){
						System.out.println("12else if query------"+query);
						query="'%"+query+"%'";
					}else if(selectInt==3){
						System.out.println("3else if query------"+query);
					}else if(selectInt==4||selectInt==5){
						System.out.println("45else if query------"+query);
						query="'"+query+"'";
					}
					
					conditionSqlStr=sqlStr+query;
					
					receivePageing = new ReceivePageing();
					numPageInt=20000;
					countPage=1;

					sumPage = receivePageing.findTotalPageByCondition(numPageInt,conditionSqlStr);
					sumNumPage = receivePageing.getTotalRowByCondition(conditionSqlStr);
					countPageLabel.setText(1 + "");
					sumPageLabel.setText(sumPage + "");
					sumNumPageLabel.setText(sumNumPage + "");//总数
					numPageTextField.setText(numPageInt+"");

					list = new ArrayList<ReceiveEntity>();
					list = receivePageing
							.pageingBypageNowAndpageSizeByCondition(countPage, numPageInt,conditionSqlStr);
					createTable(list);
					
					
					selectAllButton.setEnabled(true);
					delSelectButton.setEnabled(true);
					fistPageButton.setEnabled(true);
					nextPageButton.setEnabled(true);
					upPageButton.setEnabled(true);
					lastPageButton.setEnabled(true);
					jumpPageButton.setEnabled(true);
					refreshButton.setEnabled(true);

					numPageTextField.setEnabled(false);
					clearButton.setEnabled(true);
					
					queryTextField.setText("");
//
//					selectAllButton.setEnabled(false);
//					delSelectButton.setEnabled(false);
//					fistPageButton.setEnabled(false);
//					nextPageButton.setEnabled(false);
//					upPageButton.setEnabled(false);
//					lastPageButton.setEnabled(false);
//					jumpPageButton.setEnabled(false);
//					refreshButton.setEnabled(false);
//					clearButton.setEnabled(false);
//					numPageTextField.setEnabled(false);
				}else{
					JOptionPane.showMessageDialog(ReceiveManagePanel.this,
							"请选择查询条件或查询不能为空！", "系统提示",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		queryButton.setBounds(698, 7, 78, 24);
		add(queryButton);
		
		queryTextField = new JTextField();
		queryTextField.setBackground(new Color(176, 224, 230));
		queryTextField.setToolTipText("\u6839\u636E\u8D26\u53F7\u5B57\u6BB5\u6A21\u7CCA\u67E5\u8BE2");
		queryTextField.setColumns(10);
		queryTextField.setBounds(538, 7, 154, 24);
		add(queryTextField);
		
		conditionComboBox = new JComboBox();
		//改变事件
		conditionComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				condition=conditionComboBox.getSelectedIndex();
				switch (condition) {
				case 1:
					sqlStr=" where receive like ";
					break;
				case 2:
					sqlStr=" where post like ";
					break;
				case 3:
					sqlStr=" where sendCount <= ";
					break;
				case 4:
					sqlStr=" where date_format(createDate,'%Y-%m-%d')=";
					break;
				case 5:
					sqlStr=" where date_format(lastSendDate,'%Y-%m-%d')=";
					break;
				default:
					sqlStr=" where receive like ";
					break;
				}
				System.out.println("switch----->"+sqlStr);
			}
		});
		conditionComboBox.setModel(new DefaultComboBoxModel(new String[] {"\u8BF7\u9009\u62E9\u67E5\u8BE2\u6761\u4EF6", "\u63A5\u6536\u5730\u5740", "\u90AE\u5C40", ">\u53D1\u9001\u6B21\u6570", "=\u521B\u5EFA\u65F6\u95F4", "=\u6700\u540E\u53D1\u9001\u65F6\u95F4"}));
		conditionComboBox.setBackground(new Color(176, 224, 230));
		conditionComboBox.setBounds(421, 8, 111, 23);
		add(conditionComboBox);
		
		JButton exportButton = new JButton("\u5BFC\u51FA");
		//导出
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				for (int i = 0; i < list.size(); i++) {
//					WriteFile.getDataWriteFile("f://aa.txt",list.get(i).getReceive());
//				}
				if(list!=null&&list.size()>-1){
					ReceiveExportDialog receiveExportDialog=new ReceiveExportDialog(list);
					receiveExportDialog.setModal(true);
					receiveExportDialog.setVisible(true);
				}
				
			}
		});
		exportButton.setBackground(new Color(176, 224, 230));
		exportButton.setBounds(348, 6, 67, 25);
		add(exportButton);
		// 显示数据
		showDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String numPageStr = numPageTextField.getText();

				if (!"".equals(numPageStr)) {
					numPageInt = Integer.parseInt(numPageStr);
				}
				receivePageing = new ReceivePageing();

				sumPage = receivePageing.findTotalPageByCondition(numPageInt,conditionSqlStr);
				sumNumPage = receivePageing.getTotalRowByCondition(conditionSqlStr);
				countPageLabel.setText(1 + "");
				sumPageLabel.setText(sumPage + "");
				sumNumPageLabel.setText(sumNumPage + "");

				list = new ArrayList<ReceiveEntity>();
				list = receivePageing
						.pageingBypageNowAndpageSizeByCondition(1, numPageInt,conditionSqlStr);
				createTable(list);
				selectAllButton.setEnabled(true);
				delSelectButton.setEnabled(true);
				fistPageButton.setEnabled(true);
				nextPageButton.setEnabled(true);
				upPageButton.setEnabled(true);
				lastPageButton.setEnabled(true);
				jumpPageButton.setEnabled(true);
				refreshButton.setEnabled(true);

				numPageTextField.setEnabled(false);
				clearButton.setEnabled(true);
			}
		});
	}

	public void createTable(ArrayList<ReceiveEntity> list) {
		table = new JTable();
		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// 不知道什么原因必须这样做，才能显示正常
		tableMainPanel.removeAll();
		tableMainPanel.add(scrollPane);

		abstractTableModel = new ReceiveAbstractTableModel(list);
		abstractTableModel.dataToTable();
		tableMainPanel.setLayout(null);
		table.setModel(abstractTableModel);
		//updateButtonColumn = new ReceiveButtonColumn(table, 6);
		// delButtonColumn=new ProxyServerButtonColumn(table, 12);
		// 获得表格的表格列类
		tableColumn = table.getColumnModel().getColumn(6);
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
}
