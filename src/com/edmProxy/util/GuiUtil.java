package com.edmProxy.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javax.swing.table.DefaultTableCellRenderer;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.border.StandardBorderPainter;
import org.jvnet.substance.button.ClassicButtonShaper;
import org.jvnet.substance.painter.StandardGradientPainter;
import org.jvnet.substance.skin.EmeraldDuskSkin;
import org.jvnet.substance.theme.SubstanceTerracottaTheme;
import org.jvnet.substance.watermark.SubstanceBubblesWatermark;

import com.incors.plaf.alloy.AlloyLookAndFeel;
import com.incors.plaf.alloy.AlloyTheme;
import com.incors.plaf.alloy.themes.bedouin.BedouinTheme;
import com.incors.plaf.alloy.themes.glass.GlassTheme;

public class GuiUtil {

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

	// 皮肤
	public static void alloy(Object obj) {
		try {
			 AlloyLookAndFeel.setProperty("alloy.theme","default");
			 AlloyLookAndFeel.setProperty("alloy.isLookAndFeelFrameDecoration",
			 "true");
//			 AlloyTheme theme = new GlassTheme();// 设置界面的外观，手册中共有5种样式
//			 LookAndFeel alloyLnF = new AlloyLookAndFeel(theme);
//			 UIManager.setLookAndFeel(alloyLnF);
			 UIManager.setLookAndFeel("com.incors.plaf.alloy.AlloyLookAndFeel");
			
			 
			 
			 
//			UIManager.setLookAndFeel(new SubstanceLookAndFeel());
//			JFrame.setDefaultLookAndFeelDecorated(true);
//			JDialog.setDefaultLookAndFeelDecorated(true);
//			SubstanceLookAndFeel
//					.setCurrentTheme(new SubstanceTerracottaTheme());
//			SubstanceLookAndFeel.setSkin(new EmeraldDuskSkin());
//			SubstanceLookAndFeel
//					.setCurrentButtonShaper(new ClassicButtonShaper());
//			SubstanceLookAndFeel
//					.setCurrentWatermark(new SubstanceBubblesWatermark());
//			SubstanceLookAndFeel
//					.setCurrentBorderPainter(new StandardBorderPainter());
//			SubstanceLookAndFeel
//					.setCurrentGradientPainter(new StandardGradientPainter());
		} catch (UnsupportedLookAndFeelException ex) {
			System.err.println("Something went wrong!");
			// You may handle the exception here
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// this line needs to be implemented in order to make JWS work properly
		UIManager.getLookAndFeelDefaults().put("ClassLoader",
				obj.getClass().getClassLoader());
	}
}
