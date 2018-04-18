package com.edmProxy.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

import com.edmProxy.util.GuiUtil;





public class BaseFrame extends JFrame {
	
	public BaseFrame(){
		GuiUtil.alloy(BaseFrame.this);
	}
	@Override
	  public void setVisible(boolean b) {
		this.centerWindow(this);//窗体居中
	    //this.screenWindow(this);//窗体满屏
		//this.close(this);//窗体X按钮提示关闭
		//窗体图标
        this.setIconImage(new ImageIcon("img/pc.gif").getImage());
	    super.setVisible(b);
	  }
	
	
	/** 窗体居中 */
	public static void centerWindow(Container win) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scr = toolkit.getScreenSize();
		int x = (scr.width - win.getWidth()) / 2;
		int y = (scr.height - win.getHeight()) / 2;
		win.setLocation(x, y);
	}

	/** 设置窗体大小等于屏幕 */
	public static void screenWindow(Container win) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scr = toolkit.getScreenSize();
		win.setSize((int) scr.getWidth(), (int) scr.getHeight() - 25); // 当前窗口大小
	}

	/**
	 * 此方法是一静态方法是将接收到的JTAble按照奇偶行分别设置成表色和银蓝色
	 * 
	 * @param table
	 *            JTable
	 */
	public static void makeFace(JTable table) {
		try {
			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table,
						Object value, boolean isSelected, boolean hasFocus,
						int row, int column) {
					if (row % 2 == 0)
						setBackground(Color.white); // 设置奇数行底色
					else if (row % 2 == 1)
						setBackground(new Color(206, 231, 255)); // 设置偶数行底色
					return super.getTableCellRendererComponent(table, value,
							isSelected, hasFocus, row, column);
				}
			};
			for (int i = 0; i < table.getColumnCount(); i++) {
				table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	//窗体X按钮提示
	public static void close(JFrame source){
		source.setDefaultCloseOperation(source.DO_NOTHING_ON_CLOSE);
		source.addWindowListener(new WindowAdapter() {   
            public void windowClosing(WindowEvent e) {   
                int flag = JOptionPane.showConfirmDialog(null, "你真的要离开系统吗?",   
                        "系统提示", JOptionPane.YES_NO_OPTION,   
                        JOptionPane.INFORMATION_MESSAGE);   
                if(JOptionPane.YES_OPTION == flag){   
                    System.exit(0);   
                }else{   
                    return;   
                }   
            }   
        });  
	}
	
	//刷新窗体
	public static void refresh(JFrame source){
		source.repaint();
		source.invalidate();
		source.validate();
	}
	

	
	//离开系统提示
	public static void exit(JFrame source){
		   int val = JOptionPane.showConfirmDialog( source, "确认离开福彩3D统计系统吗？");
		   if(val==JOptionPane.YES_OPTION){
		     System.exit(0);
		   }
			   source.setVisible(true);
	}

}
