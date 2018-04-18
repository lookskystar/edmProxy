package com.edmProxy.gui.panel.account;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.edmProxy.constant.Constants;
import com.edmProxy.dao.AccountDAO;
import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.gui.dialog.account.AccountUpdateDialog;
import com.edmProxy.gui.dialog.proxy.ProxyUpdateDialog;

public class AccountButtonColumn extends AbstractCellEditor
implements TableCellRenderer, TableCellEditor, ActionListener
{
    JTable table;
    JButton renderButton;
    JButton editButton;
    String text;
    
    private AccountDAO accountDAO;
    private AccountEntity accountEntity;

    public AccountButtonColumn(JTable table, int column)
    {
        super();
        this.table = table;
        renderButton = new JButton(Constants.updateIcon);

        editButton = new JButton();
        editButton.setFocusPainted( false );
        editButton.addActionListener( this );

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer( this );
        columnModel.getColumn(column).setCellEditor( this );
    }

    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (hasFocus)
        {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        }
        else if (isSelected)
        {
            renderButton.setForeground(table.getSelectionForeground());
             renderButton.setBackground(table.getSelectionBackground());
        }
        else
        {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        }

        renderButton.setText( (value == null) ? "" : value.toString() );
        return renderButton;
    }

    public Component getTableCellEditorComponent(
        JTable table, Object value, boolean isSelected, int row, int column)
    {
        text = (value == null) ? "" : value.toString();
        editButton.setText( text );
        return editButton;
    }

    public Object getCellEditorValue()
    {
        return text;
    }

    public void actionPerformed(ActionEvent e)
    {
        fireEditingStopped();
        accountDAO=new AccountDAO();
        accountEntity=new AccountEntity();
        //System.out.println( e.getActionCommand() + " : " + table.getSelectedRow());
        String flag=e.getActionCommand();
        String id=table.getValueAt(table.getSelectedRow(), 0).toString();
        accountEntity=accountDAO.findById(id);
        if("UPDATE".equals(flag)){
        	//System.out.println("ÐÞ¸Ä-->"+value);
        	AccountUpdateDialog accountUpdateDialog=new AccountUpdateDialog(accountEntity);
        	accountUpdateDialog.setModal(true);
        	accountUpdateDialog.setVisible(true);
        }
    }
}
