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
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.gui.BaseFrame;
import com.edmProxy.gui.MainFrame; //import com.edmProxy.gui.panel.account.ProxyServerAbstractTableModel;
//import com.edmProxy.gui.panel.account.ProxyServerButtonColumn;
import com.edmProxy.gui.panel.account.AccountAbstractTableModel;
import com.edmProxy.gui.panel.account.AccountButtonColumn;
import com.edmProxy.gui.panel.account.AccountPageing;
import com.edmProxy.gui.panel.proxy.ProxyPageing;
import com.edmProxy.util.GuiUtil;
import com.edmProxy.util.JFileChooserFileFilter;
import com.edmProxy.util.NumberLenghtLimitedDmt;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.swing.border.LineBorder;

public class AccountManagePanel extends JPanel {
	private JTable table;
	private JScrollPane scrollPane = null;
	private JCheckBox jCheckBox = null;
	private JPanel tablePanel = null;
	private JPanel tableMainPanel;
	private JFileChooser jFileChooser = null;
	private JTextField jumpPageText;
	private JTextField jumpPageTextField;
	private AccountDAO accountDAO;

	private AccountPageing accountPageing;
	private ArrayList<AccountEntity> list;
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
	private AccountAbstractTableModel accountAbstractTableModel;
	private AccountButtonColumn updateButtonColumn;
	private TableColumn tableColumn;

	private JButton refreshButton;
	private JButton clearButton;
	private JTextField queryTextField;

	public AccountManagePanel(final MainFrame source) {
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

		fistPageButton = new JButton("\u9996\u9875",Constants.fistIcon);
		// 首页
		fistPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				accountPageing = new AccountPageing();
				list = new ArrayList<AccountEntity>();
				list = accountPageing
						.pageingBypageNowAndpageSize(1, numPageInt);
				createTable(list);
				countPageLabel.setText(1 + "");
				countPage = 1;
			}
		});
		fistPageButton.setEnabled(false);
		fistPageButton.setBounds(10, 11, 75, 25);
		panel.add(fistPageButton);

		nextPageButton = new JButton("\u4E0B\u9875",Constants.nextIcon);
		// 下一页
		nextPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("countPage-->" + countPage);
				if (countPage < sumPage) {
					countPage += 1;
				}
				accountPageing = new AccountPageing();
				list = new ArrayList<AccountEntity>();
				list = accountPageing.pageingBypageNowAndpageSize(countPage,
						numPageInt);
				createTable(list);

				if (countPage == sumPage) {
					countPage = sumPage;
					JOptionPane.showMessageDialog(AccountManagePanel.this,
							"已到达末页！", "系统提示", JOptionPane.INFORMATION_MESSAGE);
					// return;
				}
				countPageLabel.setText(countPage + "");
			}
		});
		nextPageButton.setEnabled(false);
		nextPageButton.setBounds(89, 11, 85, 25);
		panel.add(nextPageButton);

		upPageButton = new JButton("\u4E0A\u9875",Constants.upIcon);
		// 上页
		upPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (countPage > 1) {
					//System.out.println("countPage-->" + countPage);
					countPage -= 1;
					//System.out.println("countPage-->" + countPage);
				}
				accountPageing = new AccountPageing();
				list = new ArrayList<AccountEntity>();
				list = accountPageing.pageingBypageNowAndpageSize(countPage,
						numPageInt);
				createTable(list);
				if (countPage < 1) {
					countPage = 1;
				}
				if (countPage == 1) {
					JOptionPane.showMessageDialog(AccountManagePanel.this,
							"已到达首页！", "系统提示", JOptionPane.INFORMATION_MESSAGE);
					// return;
				}
				// System.out.println("countPage---->"+countPage);
				countPageLabel.setText(countPage + "");

			}
		});
		upPageButton.setEnabled(false);
		upPageButton.setBounds(179, 11, 85, 25);
		panel.add(upPageButton);

		lastPageButton = new JButton("\u672B\u9875",Constants.lastIcon);
		// 末页
		lastPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				accountPageing = new AccountPageing();
				list = new ArrayList<AccountEntity>();
				list = accountPageing.pageingBypageNowAndpageSize(sumPage,
						numPageInt);
				createTable(list);
				countPage = sumPage;
				countPageLabel.setText(sumPage + "");
			}
		});
		lastPageButton.setEnabled(false);
		lastPageButton.setBounds(269, 11, 75, 25);
		panel.add(lastPageButton);

		jumpPageButton = new JButton("\u8DF3\u8F6C");
		// 跳转到
		jumpPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String jumpPageStr = jumpPageTextField.getText();
				if (!"".equals(jumpPageStr)) {
					countPage = Integer.parseInt(jumpPageStr);
				}

				accountPageing = new AccountPageing();
				list = new ArrayList<AccountEntity>();
				list = accountPageing.pageingBypageNowAndpageSize(countPage,
						numPageInt);
				createTable(list);

				countPageLabel.setText(countPage + "");
			}
		});
		jumpPageButton.setEnabled(false);
		jumpPageButton.setBounds(348, 11, 70, 25);
		panel.add(jumpPageButton);

		jumpPageTextField = new JTextField();
		jumpPageTextField.setDocument(new NumberLenghtLimitedDmt(10));
		jumpPageTextField.setBounds(422, 12, 34, 25);
		panel.add(jumpPageTextField);
		jumpPageTextField.setColumns(10);

		JLabel lblNewLabel = new JLabel("\u9875");
		lblNewLabel.setBounds(461, 18, 28, 15);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("\u5171\u6709");
		lblNewLabel_1.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_1.setBounds(481, 5, 34, 15);
		panel.add(lblNewLabel_1);

		delSelectButton = new JButton("\u5220\u9664",Constants.deleteIcon);
		// 删除
		delSelectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				accountDAO = new AccountDAO();
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
					int count = accountDAO.deleteBatch(ids);
					
					accountPageing = new AccountPageing();
					list = new ArrayList<AccountEntity>();
					list = accountPageing.pageingBypageNowAndpageSize(
							countPage, numPageInt);
					createTable(list);

					if (count > 0) {
						JOptionPane.showMessageDialog(AccountManagePanel.this,
								"删除成功！", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(AccountManagePanel.this,
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
		sumNumPageLabel.setBounds(517, 6, 53, 15);
		panel.add(sumNumPageLabel);

		JLabel lblNewLabel_2 = new JLabel("\u5206");
		lblNewLabel_2.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_2.setBounds(482, 24, 28, 15);
		panel.add(lblNewLabel_2);

		sumPageLabel = new JLabel("0");
		sumPageLabel.setForeground(Color.RED);
		sumPageLabel.setBounds(517, 24, 35, 15);
		panel.add(sumPageLabel);

		JLabel lblNewLabel_3 = new JLabel("\u73B0\u5728\u7B2C");
		lblNewLabel_3.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_3.setBounds(639, 5, 48, 15);
		panel.add(lblNewLabel_3);

		countPageLabel = new JLabel("0");
		countPageLabel.setForeground(Color.RED);
		countPageLabel.setBounds(687, 5, 28, 15);
		panel.add(countPageLabel);

		JLabel lblNewLabel_5 = new JLabel("\u6BCF\u9875");
		lblNewLabel_5.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_5.setBounds(638, 23, 40, 15);
		panel.add(lblNewLabel_5);

		JLabel label = new JLabel("\u6761\u6570\u636E");
		label.setFont(new Font("SimSun", Font.BOLD, 12));
		label.setBounds(576, 5, 54, 15);
		panel.add(label);

		JLabel lblNewLabel_6 = new JLabel("\u9875");
		lblNewLabel_6.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_6.setBounds(576, 21, 54, 15);
		panel.add(lblNewLabel_6);

		JLabel label_1 = new JLabel("\u9875");
		label_1.setFont(new Font("SimSun", Font.BOLD, 12));
		label_1.setBounds(732, 5, 54, 15);
		panel.add(label_1);

		JLabel lblNewLabel_7 = new JLabel("\u6761\u6570\u636E");
		lblNewLabel_7.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_7.setBounds(731, 24, 54, 15);
		panel.add(lblNewLabel_7);

		numPageTextField = new JTextField();
		numPageTextField.setDocument(new NumberLenghtLimitedDmt(10));
		numPageTextField.setForeground(Color.RED);
		numPageTextField.setText("32");
		numPageTextField.setBounds(685, 23, 37, 18);
		panel.add(numPageTextField);
		numPageTextField.setColumns(10);

		tableMainPanel = new JPanel();
		tableMainPanel.setBounds(1, 37, 880, 530);
		add(tableMainPanel);

		showDataButton = new JButton("\u663E\u793A\u6570\u636E",Constants.showDataIcon);
		showDataButton.setBounds(8, 6, 120, 25);
		add(showDataButton);

		selectAllButton = new JButton("\u5168\u9009",Constants.selectIcon);
		// 全选
		selectAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// table.getValueAt(1, 13);
				table.setValueAt(new Boolean(true), 1, 10);
				// System.out.println(table.getValueAt(1,
				// 13)+"--"+table.getRowCount());
				for (int i = 0; i < table.getRowCount(); i++) {
					String temp = "[" + table.getValueAt(i, 0) + "],";
					table.setValueAt(new Boolean(true), i, 10);
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

		refreshButton = new JButton("\u66F4\u65B0TABLE",Constants.updateIcon);
		refreshButton.setEnabled(false);
		// 刷新数据
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				accountPageing = new AccountPageing();
				list = new ArrayList<AccountEntity>();
				list = accountPageing.pageingBypageNowAndpageSize(countPage,
						numPageInt);
				createTable(list);
			}
		});
		refreshButton.setBounds(134, 7, 120, 24);
		add(refreshButton);

		clearButton = new JButton("\u6E05\u9664\u6240\u6709\u6570\u636E",Constants.clearIcon);
		clearButton.setEnabled(false);
		// 清除所有数据
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int flag = JOptionPane.showConfirmDialog(null, "你真的要清空所有数据吗?",
						"系统警告", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (JOptionPane.YES_OPTION == flag) {
					// 删除所有数据
					accountDAO = new AccountDAO();
					int count = accountDAO.deleteAll();
//					if (count > 0) {
						accountPageing = new AccountPageing();
						list = new ArrayList<AccountEntity>();
						list = accountPageing.pageingBypageNowAndpageSize(1,
								numPageInt);
						createTable(list);

						countPage = 1;// 当前页
						sumPage = 1;// 总页数
						sumNumPage = 0;// 总数据

						sumNumPageLabel.setText(sumNumPage + "");
						sumPageLabel.setText(sumPage + "");
						countPageLabel.setText(countPage + "");
						JOptionPane.showMessageDialog(AccountManagePanel.this,
								"清除所有数据成功！", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
//					} else {
//						JOptionPane.showMessageDialog(AccountManagePanel.this,
//								"清除所有数据失败！", "系统提示",
//								JOptionPane.WARNING_MESSAGE);
//					}
				}
			}
		});
		clearButton.setForeground(new Color(204, 0, 0));
		clearButton.setFont(new Font("SimSun", Font.BOLD, 12));
		clearButton.setBounds(260, 7, 133, 24);
		add(clearButton);
		
		JButton queryButton = new JButton("\u67E5\u8BE2",Constants.checkBtnIcon);
		queryButton.setBackground(new Color(176, 224, 230));
		queryButton.setForeground(new Color(0, 0, 0));
		//查询
		queryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String query = queryTextField.getText();
				if (!"".equals(query)) {
					list = new ArrayList<AccountEntity>();
					accountDAO = new AccountDAO();
					list = accountDAO.findBy(query);
					createTable(list);

					selectAllButton.setEnabled(false);
					delSelectButton.setEnabled(false);
					fistPageButton.setEnabled(false);
					nextPageButton.setEnabled(false);
					upPageButton.setEnabled(false);
					lastPageButton.setEnabled(false);
					jumpPageButton.setEnabled(false);
					refreshButton.setEnabled(false);
					clearButton.setEnabled(false);
					numPageTextField.setEnabled(false);
				}
			}
		});
		queryButton.setBounds(656, 7, 120, 24);
		add(queryButton);
		
		queryTextField = new JTextField();
		queryTextField.setBackground(new Color(176, 224, 230));
		queryTextField.setForeground(Color.BLUE);
		queryTextField.setToolTipText("\u6839\u636E\u8D26\u53F7\u5B57\u6BB5\u6A21\u7CCA\u67E5\u8BE2");
		queryTextField.setColumns(10);
		queryTextField.setBounds(496, 7, 154, 24);
		add(queryTextField);
		// 显示数据
		showDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String numPageStr = numPageTextField.getText();

				if (!"".equals(numPageStr)) {
					numPageInt = Integer.parseInt(numPageStr);
				}
				accountPageing = new AccountPageing();

				sumPage = accountPageing.findTotalPage(numPageInt);
				sumNumPage = accountPageing.getTotalRow();
				countPageLabel.setText(1 + "");
				sumPageLabel.setText(sumPage + "");
				sumNumPageLabel.setText(sumNumPage + "");

				list = new ArrayList<AccountEntity>();
				list = accountPageing
						.pageingBypageNowAndpageSize(1, numPageInt);
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

	public void createTable(ArrayList<AccountEntity> list) {
		table = new JTable();
		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// 不知道什么原因必须这样做，才能显示正常
		tableMainPanel.removeAll();
		tableMainPanel.add(scrollPane);

		accountAbstractTableModel = new AccountAbstractTableModel(list);
		accountAbstractTableModel.dataToTable();
		tableMainPanel.setLayout(null);
		table.setModel(accountAbstractTableModel);
		updateButtonColumn = new AccountButtonColumn(table, 9);
		// delButtonColumn=new ProxyServerButtonColumn(table, 12);
		// 获得表格的表格列类
		tableColumn = table.getColumnModel().getColumn(10);
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
