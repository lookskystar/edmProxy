package com.edmProxy.gui.panel.statistics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ChartPanel;

import com.edmProxy.dao.StatisticsDAO;
import com.edmProxy.entity.SendTaskStatisticsObj;
import com.edmProxy.entity.StatisticsEntity;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class StatisticsReportPanel extends JPanel {

	private JPanel reportPanel;
	private StandardChartTheme standardChartTheme;
	private DefaultCategoryDataset dataSet;
	private JFreeChart jFreeChart;
	private ChartPanel chartPanel;
	private ArrayList<SendTaskStatisticsObj> sendTaskStatisticsObjList;
	private StatisticsDAO statisticsDAO;
	

	public StatisticsReportPanel() {
		setBorder(new LineBorder(SystemColor.desktop));
		setBackground(SystemColor.activeCaptionBorder);
		this.setBounds(0, 0, 880, 612);
		setLayout(null);
		
		reportPanel = new JPanel();
	    reportPanel.setBorder(new LineBorder(new Color(100, 149, 237)));
	    reportPanel.setBackground(Color.WHITE);
	    reportPanel.setBounds(10, 10, 860, 524);
	    add(reportPanel);
		
		
		JPanel panel = new JPanel();
	    panel.setBounds(10, 540, 860, 62);
	    add(panel);
	    panel.setLayout(null);
	       
	    JButton showDataButton = new JButton("\u663E\u793A\u6570\u636E");
	    showDataButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		reportPanel.removeAll();
	       		createReport();
	        }
	     });
	       showDataButton.setBounds(10, 10, 95, 25);
	       panel.add(showDataButton);
	}
	
	public void createReport(){
		
		//创建主题样式  
	    standardChartTheme=new StandardChartTheme("CN");  
		//设置标题字体  
		standardChartTheme.setExtraLargeFont(new Font("隶书",Font.BOLD,20));  
		//设置图例的字体  
		standardChartTheme.setRegularFont(new Font("宋书",Font.BOLD,15));  
		//设置轴向的字体  
		standardChartTheme.setLargeFont(new Font("宋书",Font.BOLD,15));  
		//应用主题样式  
		ChartFactory.setChartTheme(standardChartTheme); 
		
		sendTaskStatisticsObjList=new ArrayList<SendTaskStatisticsObj>();
		statisticsDAO=new StatisticsDAO();
		sendTaskStatisticsObjList=statisticsDAO.pageingBypageNowAndpageSizeObjByCondition(1, 10, "");
		dataSet= new DefaultCategoryDataset();//构造DataSet
		for (int i = 0; i <sendTaskStatisticsObjList.size(); i++) {
			dataSet.addValue(sendTaskStatisticsObjList.get(i).getStatisticsEntity().getOpenCount(),sendTaskStatisticsObjList.get(i).getStatisticsEntity().getId()+"", sendTaskStatisticsObjList.get(i).getSendTaskEntity().getSendTask());
		}
		 //创建柱形图
        jFreeChart = ChartFactory.createBarChart3D("发送任务统计图形报表",
                "发送任务", "邮件打开数", dataSet, PlotOrientation.HORIZONTAL, 
                false, false, false);
	 
      //用来放置图表
       chartPanel = new ChartPanel(jFreeChart);
       chartPanel.setPreferredSize(new Dimension(850, 500)); 
       
       
    
       reportPanel.add(chartPanel, BorderLayout.CENTER);
       reportPanel.updateUI();
	}
}
