package com.edmProxy.gui.panel.sendTask;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.List;
import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JRadioButton;

import com.edmProxy.dao.AccountDAO;
import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.dao.ReceiveDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.ReceiveEntity;
import com.edmProxy.entity.TestEntiy;
import com.edmProxy.util.CheckParameterUtil;
import com.edmProxy.util.JFileChooserFileFilter;
import com.edmProxy.util.ReadFile;
import com.edmProxy.util.proxy.check.ProxyCheck;
import com.hexidec.ekit.EkitCore;
import com.hexidec.ekit.EkitCoreSpell;


import javax.swing.JTextPane;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JToolBar;
import java.awt.FlowLayout;
import javax.swing.border.TitledBorder;
//添加任务
public class CopyOfSendTaskInsertPanel extends JPanel implements WindowListener {

	private ReceiveEntity receiveEntity;
	
	private ReceiveDAO  receiveDAO;
	private JFileChooser jFileChooser;
	private ArrayList<String> receiveStrList;
	private ArrayList<ReceiveEntity> receiveList;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	
	private EkitCore ekitCore;
	private File currentFile = (File)null;
	private JPanel panel;
	private JPanel ekitMenuBarPanel;
	
	public CopyOfSendTaskInsertPanel(){
		this(null, null, null, null, true, false, true, true, null, null, false, false, false, true, false);
	}
	public CopyOfSendTaskInsertPanel(String sDocument, String sStyleSheet, String sRawDocument, URL urlStyleSheet, boolean includeToolBar, boolean showViewSource, boolean showMenuIcons, boolean editModeExclusive, String sLanguage, String sCountry, boolean base64, boolean debugMode, boolean useSpellChecker, boolean multiBar, boolean enterBreak) {
		setBorder(new LineBorder(SystemColor.desktop));
		setBackground(SystemColor.activeCaptionBorder);
		this.setBounds(0, 0, 880, 612);
		setLayout(null);
		
		panel = new JPanel();
		panel.setBorder(new LineBorder(SystemColor.desktop));
		panel.setBounds(10, 10, 860, 592);
		add(panel);
		panel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(SystemColor.desktop));
		panel_2.setBounds(-11, -11, 880, 612);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JButton btnNewButton = new JButton("\u91CD\u7F6E");
		btnNewButton.setBounds(672, 575, 95, 25);
		panel_2.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("\u4FDD\u5B58");
		btnNewButton_1.setBounds(773, 574, 95, 25);
		panel_2.add(btnNewButton_1);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(10, 6, 860, 52);
		panel_2.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel label = new JLabel("\u9644      \u4EF6");
		label.setBounds(558, 5, 60, 15);
		panel_4.add(label);
		
		JLabel lblNewLabel = new JLabel("\u4EFB\u52A1\u540D\u79F0");
		lblNewLabel.setBounds(10, 5, 48, 15);
		panel_4.add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("\u53D1\u9001\u8D26\u53F7");
		lblNewLabel_2.setBounds(10, 27, 48, 15);
		panel_4.add(lblNewLabel_2);
		
		JLabel label_1 = new JLabel("\u8D26\u53F7\u542F\u52A8\u6570");
		label_1.setBounds(211, 5, 60, 15);
		panel_4.add(label_1);
		
		JLabel lblNewLabel_1 = new JLabel("\u90AE    \u5C40");
		lblNewLabel_1.setBounds(336, 5, 48, 15);
		panel_4.add(lblNewLabel_1);
		
		JLabel lblNewLabel_3 = new JLabel("\u63A5\u6536\u90AE\u7BB1");
		lblNewLabel_3.setBounds(336, 27, 48, 15);
		panel_4.add(lblNewLabel_3);
		
		textField = new JTextField();
		textField.setBounds(64, 5, 141, 21);
		panel_4.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBackground(new Color(135, 206, 250));
		textField_1.setBounds(64, 27, 141, 21);
		panel_4.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton_2 = new JButton("\u9009\u62E9\u5BFC\u5165");
		btnNewButton_2.setBackground(new Color(135, 206, 235));
		btnNewButton_2.setBounds(211, 24, 95, 25);
		panel_4.add(btnNewButton_2);
		
		textField_2 = new JTextField();
		textField_2.setBounds(390, 4, 140, 21);
		panel_4.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(390, 27, 140, 21);
		panel_4.add(textField_3);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBackground(new Color(152, 251, 152));
		textField_4.setBounds(624, 5, 206, 21);
		panel_4.add(textField_4);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setBackground(new Color(135, 206, 235));
		textField_5.setBounds(277, 5, 29, 19);
		panel_4.add(textField_5);
		textField_5.setColumns(10);
		
		JButton btnNewButton_3 = new JButton("\u9009\u62E9\u6DFB\u52A0\u9644\u4EF6");
		btnNewButton_3.setBackground(new Color(152, 251, 152));
		btnNewButton_3.setBounds(723, 27, 107, 25);
		panel_4.add(btnNewButton_3);
		
		JPanel ekitPanel = new JPanel();
		ekitPanel.setBounds(10, 94, 860, 470);
		panel_2.add(ekitPanel);
		
		ekitMenuBarPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) ekitMenuBarPanel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		ekitMenuBarPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		ekitMenuBarPanel.setBounds(10, 61, 860, 32);
		panel_2.add(ekitMenuBarPanel);
		
		//-----------------------------开始，在线文本编辑器
		if(useSpellChecker)
		{
			ekitCore = new EkitCoreSpell(false, sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, true, multiBar, (multiBar ? EkitCore.TOOLBAR_DEFAULT_MULTI : EkitCore.TOOLBAR_DEFAULT_SINGLE), enterBreak);
		}
		else
		{
			ekitCore = new EkitCore(false, sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, false, multiBar, (multiBar ? EkitCore.TOOLBAR_DEFAULT_MULTI : EkitCore.TOOLBAR_DEFAULT_SINGLE), enterBreak);
		}
		/* Add the components to the app */
		if(includeToolBar)
		{
			if(multiBar)
			{
				//this.getContentPane().setLayout(new GridBagLayout());
				ekitPanel.setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.fill       = GridBagConstraints.HORIZONTAL;
				gbc.anchor     = GridBagConstraints.NORTH;
				gbc.gridheight = 1;
				gbc.gridwidth  = 1;
				gbc.weightx    = 1.0;
				gbc.weighty    = 0.0;
				gbc.gridx      = 1;

				gbc.gridy      = 1;
				ekitPanel.add(ekitCore.getToolBarMain(includeToolBar), gbc);

				gbc.gridy      = 2;
				ekitPanel.add(ekitCore.getToolBarFormat(includeToolBar), gbc);

				gbc.gridy      = 3;
				ekitPanel.add(ekitCore.getToolBarStyles(includeToolBar), gbc);

				gbc.anchor     = GridBagConstraints.SOUTH;
				gbc.fill       = GridBagConstraints.BOTH;
				gbc.weighty    = 1.0;
				gbc.gridy      = 4;
				ekitPanel.add(ekitCore, gbc);
			}
			else
			{
				ekitPanel.setLayout(new BorderLayout());
				ekitPanel.add(ekitCore, BorderLayout.CENTER);
				ekitPanel.add(ekitCore.getToolBar(includeToolBar), BorderLayout.NORTH);
			}
		}
		else
		{
			ekitPanel.setLayout(new BorderLayout());
			ekitPanel.add(ekitCore, BorderLayout.CENTER);
		}
		
//		JToolBar toolBar = new JToolBar();
//		toolBar.setBounds(10, 62, 315, 23);
//		panel_2.add(toolBar);
		
//		this.setJMenuBar(ekitCore.getMenuBar());
		ekitMenuBarPanel.add(ekitCore.getMenuBar());
		
		
		this.addWindowListener(this);
	}
	private void addWindowListener(CopyOfSendTaskInsertPanel sendTaskInsertPanel) {
		// TODO Auto-generated method stub
		
	}
	private void updateTitle()
	{
		//this.setTitle(ekitCore.getAppName() + (currentFile == null ? "" : " - " + currentFile.getName()));
		
	}
	public void reset(){

	}
	public void windowClosing(WindowEvent arg0) {
		//this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	
	public static void usage()
	{
		System.out.println("usage: com.hexidec.ekit.Ekit [-t|t+|T] [-s|S] [-m|M] [-x|X] [-b|B] [-v|V] [-p|P] [-fFILE] [-cCSS] [-rRAW] [-uURL] [-lLANG] [-d|D] [-h|H|?]");
		System.out.println("       Each option contained in [] brackets is optional,");
		System.out.println("       and can be one of the values separated be the | pipe.");
		System.out.println("       Each option must be proceeded by a - hyphen.");
		System.out.println("       The options are:");
		System.out.println("         -t|t+|T : -t = single toolbar, -t+ = multiple toolbars, -T = no toolbar");
		System.out.println("         -s|S    : -s = show source window on startup, -S = hide source window");
		System.out.println("         -m|M    : -m = show icons on menus, -M = no menu icons");
		System.out.println("         -x|X    : -x = exclusive document/source windows, -X = use split window");
		System.out.println("         -b|B    : -b = use Base64 document encoding, -B = use regular encoding");
		System.out.println("         -v|V    : -v = include spell checker, -V = omit spell checker");
		System.out.println("         -p|P    : -p = ENTER key inserts paragraph, -P = inserts break");
		System.out.println("         -fFILE  : load HTML document on startup (replace FILE with file name)");
		System.out.println("         -cCSS   : load CSS stylesheet on startup (replace CSS with file name)");
		System.out.println("         -rRAW   : load raw document on startup (replace RAW with file name)");
		System.out.println("         -uURL   : load document at URL on startup (replace URL with file URL)");
		System.out.println("         -lLANG  : specify the starting language (defaults to your locale)");
		System.out.println("                    replace LANG with xx_XX format (e.g., US English is en_US)");
		System.out.println("         -d|D    : -d = DEBUG mode on, -D = DEBUG mode off (developers only)");
		System.out.println("         -h|H|?  : print out this help information");
		System.out.println("         ");
		System.out.println("The defaults settings are equivalent to: -t+ -S -m -x -B -V -p -D");
		System.out.println("         ");
		System.out.println("For further information, read the README file.");
	}
	
	public static void ekitRun(String[] args){
		String sDocument = null;
		String sStyleSheet = null;
		String sRawDocument = null;
		URL urlStyleSheet = null;
		boolean includeToolBar = true;
		boolean multibar = true;
		boolean includeViewSource = false;
		boolean includeMenuIcons = true;
		boolean modeExclusive = true;
		String sLang = null;
		String sCtry = null;
		boolean base64 = false;
		boolean debugOn = false;
		boolean spellCheck = false;
		boolean enterBreak = false;
		for(int i = 0; i < args.length; i++)
		{
			if     (args[i].equals("-h") ||
					args[i].equals("-H") ||
					args[i].equals("-?"))     { usage(); }
			else if(args[i].equals("-t"))     { includeToolBar = true; multibar = false; }
			else if(args[i].equals("-t+"))    { includeToolBar = true; multibar = true; }
			else if(args[i].equals("-T"))     { includeToolBar = false; multibar = false; }
			else if(args[i].equals("-s"))     { includeViewSource = true; }
			else if(args[i].equals("-S"))     { includeViewSource = false; }
			else if(args[i].equals("-m"))     { includeMenuIcons = true; }
			else if(args[i].equals("-M"))     { includeMenuIcons = false; }
			else if(args[i].equals("-x"))     { modeExclusive = true; }
			else if(args[i].equals("-X"))     { modeExclusive = false; }
			else if(args[i].equals("-b"))     { base64 = true; }
			else if(args[i].equals("-B"))     { base64 = false; }
			else if(args[i].startsWith("-f")) { sDocument = args[i].substring(2, args[i].length()); }
			else if(args[i].startsWith("-c")) { sStyleSheet = args[i].substring(2, args[i].length()); }
			else if(args[i].startsWith("-r")) { sRawDocument = args[i].substring(2, args[i].length()); }
			else if(args[i].equals("-v"))     { spellCheck = true; }
			else if(args[i].equals("-V"))     { spellCheck = false; }
			else if(args[i].equals("-p"))     { enterBreak = false; }
			else if(args[i].equals("-P"))     { enterBreak = true; }
			else if(args[i].startsWith("-u"))
			{
				try
				{
					urlStyleSheet = new URL(args[i].substring(2, args[i].length()));
				}
				catch(MalformedURLException murle)
				{
					murle.printStackTrace(System.err);
				}
			}
			else if(args[i].startsWith("-l"))
			{
				if(args[i].indexOf('_') == 4 && args[i].length() >= 7)
				{
					sLang = args[i].substring(2, args[i].indexOf('_'));
					sCtry = args[i].substring(args[i].indexOf('_') + 1, args[i].length());
				}
			}
			else if(args[i].equals("-d"))     { debugOn = true; }
			else if(args[i].equals("-D"))     { debugOn = false; }
		}
	}
	public void windowOpened(WindowEvent we)      { ; }
	public void windowClosed(WindowEvent we)      { ; }
	public void windowActivated(WindowEvent we)   { ; }
	public void windowDeactivated(WindowEvent we) { ; }
	public void windowIconified(WindowEvent we)   { ; }
	public void windowDeiconified(WindowEvent we) { ; }
}
