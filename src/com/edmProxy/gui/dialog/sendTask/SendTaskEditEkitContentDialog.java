package com.edmProxy.gui.dialog.sendTask;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.hexidec.ekit.EkitCore;
import com.hexidec.ekit.EkitCoreSpell;

public class SendTaskEditEkitContentDialog extends JDialog implements WindowListener{
	private JPanel contentPane;
	private EkitCore ekitCore;
	private File currentFile = (File)null;


	public static void main(String[] args) {
		try {
			SendTaskEditEkitContentDialog dialog = new SendTaskEditEkitContentDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ekitRun(args);
	}

	public SendTaskEditEkitContentDialog(){
		this(null, null, null, null, true, false, true, true, null, null, false, false, false, true, false);
		setResizable(false);
	}
	
	public SendTaskEditEkitContentDialog(String sDocument, String sStyleSheet, String sRawDocument, URL urlStyleSheet, boolean includeToolBar, boolean showViewSource, boolean showMenuIcons, boolean editModeExclusive, String sLanguage, String sCountry, boolean base64, boolean debugMode, boolean useSpellChecker, boolean multiBar, boolean enterBreak) {
		this.setTitle("EDM系统  v1.0 --编辑邮件内容");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 690);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 990, 643);
		contentPane.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		
		////
		if(useSpellChecker)
		{
			ekitCore = new EkitCoreSpell(false, sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, true, multiBar, (multiBar ? EkitCore.TOOLBAR_DEFAULT_MULTI : EkitCore.TOOLBAR_DEFAULT_SINGLE), enterBreak);
		}
		else
		{
			ekitCore = new EkitCore(false, sDocument, sStyleSheet, sRawDocument, null, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, false, multiBar, (multiBar ? EkitCore.TOOLBAR_DEFAULT_MULTI : EkitCore.TOOLBAR_DEFAULT_SINGLE), enterBreak);
		}
		//ekitCore.setFrame(this);
		
		/* Add the components to the app */
		if(includeToolBar)
		{
			if(multiBar)
			{
				//this.getContentPane().setLayout(new GridBagLayout());
				panel.setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.fill       = GridBagConstraints.HORIZONTAL;
				gbc.anchor     = GridBagConstraints.NORTH;
				gbc.gridheight = 1;
				gbc.gridwidth  = 1;
				gbc.weightx    = 1.0;
				gbc.weighty    = 0.0;
				gbc.gridx      = 1;

				gbc.gridy      = 1;
				panel.add(ekitCore.getToolBarMain(includeToolBar), gbc);

				gbc.gridy      = 2;
				panel.add(ekitCore.getToolBarFormat(includeToolBar), gbc);

				gbc.gridy      = 3;
				panel.add(ekitCore.getToolBarStyles(includeToolBar), gbc);

				gbc.anchor     = GridBagConstraints.SOUTH;
				gbc.fill       = GridBagConstraints.BOTH;
				gbc.weighty    = 1.0;
				gbc.gridy      = 4;
			    panel.add(ekitCore, gbc);//文本框
			}
			else
			{
				panel.setLayout(new BorderLayout());
				panel.add(ekitCore, BorderLayout.CENTER);
				panel.add(ekitCore.getToolBar(includeToolBar), BorderLayout.NORTH);
			}
		}
		else
		{
			panel.setLayout(new BorderLayout());
			panel.add(ekitCore, BorderLayout.CENTER);
		}
		
		this.setJMenuBar(ekitCore.getMenuBar());
		this.addWindowListener(this);

		//this.updateTitle();
		//this.pack();
		//this.setVisible(true);
	}
	
	private void updateTitle()
	{
		this.setTitle(ekitCore.getAppName() + (currentFile == null ? "" : " - " + currentFile.getName()));
		
	}
	public void windowClosing(WindowEvent we)
	{
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
