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
import com.edmProxy.dao.StatisticsDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ReceiveEntity;
import com.edmProxy.entity.SendTaskStatisticsObj;
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
import com.edmProxy.gui.panel.statistics.StatisticsAbstractTableModel;
import com.edmProxy.gui.panel.statistics.StatisticsButtonColumn;
import com.edmProxy.gui.panel.statistics.StatisticsPageing;
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

public class StatisticsManagePanel extends JPanel {
	private JTable table;
	private JScrollPane scrollPane = null;
	private JCheckBox jCheckBox = null;
	private JPanel tablePanel = null;
	private JPanel tableMainPanel;
	private JFileChooser jFileChooser = null;
	private JTextField jumpPageText;
	private StatisticsDAO statisticsDAO;

	private StatisticsPageing statisticsPageing;
	private ArrayList<SendTaskStatisticsObj> list;
	private JButton selectAllButton;
	private JButton delSelectButton;
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
	private StatisticsAbstractTableModel abstractTableModel;
	private StatisticsButtonColumn updateButtonColumn;
	private TableColumn tableColumn;

	private JButton refreshButton;
	private int condition=0;
	
	private String sqlStr="";
	private String conditionSqlStr="";
	private String query="";

	public StatisticsManagePanel(final MainFrame source) {
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

		JLabel lblNewLabel_1 = new JLabel("\u5171\u6709");
		lblNewLabel_1.setFont(new Font("SimSun", Font.BOLD, 12));
		lblNewLabel_1.setBounds(455, 5, 34, 15);
		panel.add(lblNewLabel_1);

		delSelectButton = new JButton("\u5220\u9664");
		// 删除
		delSelectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				statisticsDAO = new StatisticsDAO();
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
					int count = statisticsDAO.deleteBatch(ids);
					
					statisticsPageing = new StatisticsPageing();
					list = new ArrayList<SendTaskStatisticsObj>();
					list = statisticsPageing.pageingBypageNowAndpageSizeObjByCondition(
							countPage, numPageInt,"");
					createTable(list);

//					if (count > 0) {
						JOptionPane.showMessageDialog(StatisticsManagePanel.this,
								"删除成功！", "系统提示",
								JOptionPane.INFORMATION_MESSAGE);
//					} else {
//						JOptionPane.showMessageDialog(StatisticsManagePanel.this,
//								"删除失败！", "系统提示",
//								JOptionPane.WARNING_MESSAGE);
//					}

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
				table.setValueAt(new Boolean(true), 1, 5);
				// System.out.println(table.getValueAt(1,
				// 13)+"--"+table.getRowCount());
				for (int i = 0; i < table.getRowCount(); i++) {
					String temp = "[" + table.getValueAt(i, 0) + "],";
					table.setValueAt(new Boolean(true), i, 5);
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
				statisticsPageing  = new StatisticsPageing ();
				list = new ArrayList<SendTaskStatisticsObj>();
				list = statisticsPageing.pageingBypageNowAndpageSizeObjByCondition(countPage,
						numPageInt,conditionSqlStr);
				createTable(list);
			}
		});
		refreshButton.setBounds(109, 7, 94, 24);
		add(refreshButton);
		// 显示数据
		showDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String numPageStr = numPageTextField.getText();

				if (!"".equals(numPageStr)) {
					numPageInt = Integer.parseInt(numPageStr);
				}
				statisticsPageing = new StatisticsPageing();

				sumPage = statisticsPageing.findTotalPageByCondition(numPageInt,conditionSqlStr);
				sumNumPage = statisticsPageing.getTotalRowByCondition(conditionSqlStr);
				countPageLabel.setText(1 + "");
				sumPageLabel.setText(sumPage + "");
				sumNumPageLabel.setText(sumNumPage + "");
				
				list = new ArrayList<SendTaskStatisticsObj>();
				list = statisticsPageing
						.pageingBypageNowAndpageSizeObjByCondition(1, numPageInt,conditionSqlStr);
				createTable(list);
				
				selectAllButton.setEnabled(true);
				delSelectButton.setEnabled(true);
				refreshButton.setEnabled(true);

				numPageTextField.setEnabled(false);
			}
		});
	}

	public void createTable(ArrayList<SendTaskStatisticsObj> list) {
		table = new JTable();
		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// 不知道什么原因必须这样做，才能显示正常
		tableMainPanel.removeAll();
		tableMainPanel.add(scrollPane);

		abstractTableModel = new StatisticsAbstractTableModel(list);
		abstractTableModel.dataToTable();
		tableMainPanel.setLayout(null);
		table.setModel(abstractTableModel);
		//updateButtonColumn = new ReceiveButtonColumn(table, 6);
		// delButtonColumn=new ProxyServerButtonColumn(table, 12);
		// 获得表格的表格列类
		tableColumn = table.getColumnModel().getColumn(5);
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
