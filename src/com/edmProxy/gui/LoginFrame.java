package com.edmProxy.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;



import javax.swing.ImageIcon;
import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import javax.swing.SpringLayout;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.CardLayout;
import javax.swing.JLabel;
import java.awt.SystemColor;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginFrame extends BaseFrame {
	private JTextField usernameText;
	private JPasswordField passwordText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		this.setTitle("EDMÏµÍ³  v1.0 --µÇÂ¼");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 200);
        getContentPane().setLayout(new CardLayout(0, 0));   
        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel, "name_20041572777712");
        mainPanel.setLayout(null);
        
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.YELLOW);
        logoPanel.setBounds(0, 0, 292, 61);
        mainPanel.add(logoPanel);
        
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(UIManager.getColor("RadioButton.light"));
        loginPanel.setBounds(0, 62, 292, 111);
        mainPanel.add(loginPanel);
        loginPanel.setLayout(null);
        
        JLabel usernameLabel = new JLabel("\u7528\u6237\u540D");
        usernameLabel.setBounds(31, 12, 42, 15);
        loginPanel.add(usernameLabel);
        
        JLabel passwordLabel = new JLabel("\u5BC6  \u7801");
        passwordLabel.setBounds(31, 46, 42, 15);
        loginPanel.add(passwordLabel);
        
        usernameText = new JTextField();
        usernameText.setBounds(79, 10, 144, 21);
        loginPanel.add(usernameText);
        usernameText.setColumns(10);
        
        passwordText = new JPasswordField();
        passwordText.setBounds(79, 40, 144, 25);
        loginPanel.add(passwordText);
        
        JButton loginButton = new JButton("\u767B\u5F55");
        loginButton.setBounds(34, 76, 95, 25);
        loginPanel.add(loginButton);
        
        JButton closeButton = new JButton("\u5173\u95ED");
        closeButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		System.exit(0);
        	}
        });
        closeButton.setBounds(165, 76, 95, 25);
        loginPanel.add(closeButton);
        
	}
}
