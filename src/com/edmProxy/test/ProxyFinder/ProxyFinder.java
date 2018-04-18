
/**
 * @author 李晓宇， hmily_yu@hotmail.com
 * ProxyFinder类为程序GUI
 */
package com.edmProxy.test.ProxyFinder;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Timer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;

public class ProxyFinder extends JFrame implements Logable {
    private Vector<ProxyDesc>  proxyList     = new Vector<ProxyDesc>();
    private Vector<Thread>     threadList    = new Vector<Thread>();
    private int                totalProxyNum = 0;
    private boolean            isFinding     = false;
    private JButton            buttonStart;
    private JLabel             labelStatus;
    private JSpinner           spinner;
    private SpinnerNumberModel spinnerNumberModel;
    private SpringLayout       springLayout;
    private JTextArea          textAreaLog;
    private JTextArea          textAreaResult;
    private JTextField         textFieldUrl;
    private Timer              timer;

    /**
     * Create the frame
     */
    public ProxyFinder() {
        super();
        setTitle("cnproxy代理测试器（作者：hmily_yu@hotmail.com）");

        final BorderLayout borderLayout = new BorderLayout();

        borderLayout.setVgap(10);
        getContentPane().setLayout(borderLayout);
        setBounds(100, 100, 500, 438);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();

        springLayout = new SpringLayout();
        panel.setLayout(springLayout);
        getContentPane().add(panel);

        final JLabel label = new JLabel();

        label.setText("测试网站：");
        panel.add(label);
        springLayout.putConstraint(SpringLayout.NORTH, label, 30, SpringLayout.NORTH, panel);
        springLayout.putConstraint(SpringLayout.WEST, label, 15, SpringLayout.WEST, panel);
        textFieldUrl = new JTextField();
        textFieldUrl.setText("http://");
        panel.add(textFieldUrl);
        springLayout.putConstraint(SpringLayout.SOUTH, textFieldUrl, 50, SpringLayout.NORTH, panel);
        springLayout.putConstraint(SpringLayout.NORTH, textFieldUrl, 28, SpringLayout.NORTH, panel);
        springLayout.putConstraint(SpringLayout.EAST, textFieldUrl, 455, SpringLayout.WEST, panel);
        springLayout.putConstraint(SpringLayout.WEST, textFieldUrl, 100, SpringLayout.WEST, panel);
        buttonStart = new JButton();
        buttonStart.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                if (textFieldUrl.getText().length() > 0) {
                    if (isFinding == false) {
                        isFinding = true;
                        new Thread() {
                            public void run() {
                                findProxy();
                            }
                        }.start();
                        buttonStart.setText("停止");
                        buttonStart.setEnabled(false);
                    } else {
                        isFinding = false;
                        new Thread() {
                            public void run() {
                                stopFinding();
                            }
                        }.start();
                        buttonStart.setText("开始");
                    }
                }
            }
        });
        buttonStart.setText("开始");
        panel.add(buttonStart);
        springLayout.putConstraint(SpringLayout.SOUTH, buttonStart, 88, SpringLayout.NORTH, panel);
        springLayout.putConstraint(SpringLayout.NORTH, buttonStart, 60, SpringLayout.NORTH, panel);

        final JLabel label_1 = new JLabel();

        label_1.setText("测试记录：");
        panel.add(label_1);
        springLayout.putConstraint(SpringLayout.SOUTH, label_1, 128, SpringLayout.NORTH, panel);

        final JScrollPane scrollPane = new JScrollPane();

        panel.add(scrollPane);
        springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, textFieldUrl);
        springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, label_1);
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 210, SpringLayout.NORTH, panel);
        springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 140, SpringLayout.NORTH, panel);
        textAreaLog = new JTextArea();
        textAreaLog.setEditable(false);
        scrollPane.setViewportView(textAreaLog);

        final JLabel label_2 = new JLabel();

        label_2.setText("可用代理：");
        panel.add(label_2);
        springLayout.putConstraint(SpringLayout.EAST, label_2, 65, SpringLayout.WEST, scrollPane);
        springLayout.putConstraint(SpringLayout.WEST, label_2, 0, SpringLayout.WEST, scrollPane);
        springLayout.putConstraint(SpringLayout.NORTH, label_2, 230, SpringLayout.NORTH, panel);

        final JScrollPane scrollPane_1 = new JScrollPane();

        panel.add(scrollPane_1);
        springLayout.putConstraint(SpringLayout.EAST, scrollPane_1, 0, SpringLayout.EAST, scrollPane);
        springLayout.putConstraint(SpringLayout.WEST, scrollPane_1, 0, SpringLayout.WEST, label_2);
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPane_1, 383, SpringLayout.NORTH, panel);
        springLayout.putConstraint(SpringLayout.NORTH, scrollPane_1, 260, SpringLayout.NORTH, panel);
        textAreaResult = new JTextArea();
        textAreaResult.setEditable(false);
        scrollPane_1.setViewportView(textAreaResult);

        final JLabel label_3 = new JLabel();

        label_3.setText("并发线程：");
        panel.add(label_3);
        springLayout.putConstraint(SpringLayout.EAST, label_1, 65, SpringLayout.WEST, label_3);
        springLayout.putConstraint(SpringLayout.WEST, label_1, 0, SpringLayout.WEST, label_3);
        springLayout.putConstraint(SpringLayout.NORTH, label_3, 65, SpringLayout.NORTH, panel);
        springLayout.putConstraint(SpringLayout.WEST, label_3, 0, SpringLayout.WEST, label);
        spinner            = new JSpinner();
        spinnerNumberModel = new SpinnerNumberModel();
        spinnerNumberModel.setStepSize(new Integer(1));
        spinnerNumberModel.setMaximum(new Integer(20));
        spinnerNumberModel.setMinimum(new Integer(1));
        spinnerNumberModel.setValue(new Integer(5));
        spinner.setModel(spinnerNumberModel);
        panel.add(spinner);
        springLayout.putConstraint(SpringLayout.SOUTH, spinner, 85, SpringLayout.NORTH, panel);
        springLayout.putConstraint(SpringLayout.NORTH, spinner, 63, SpringLayout.NORTH, panel);
        springLayout.putConstraint(SpringLayout.EAST, spinner, 75, SpringLayout.WEST, textFieldUrl);
        springLayout.putConstraint(SpringLayout.WEST, spinner, 0, SpringLayout.WEST, textFieldUrl);
        labelStatus = new JLabel();
        panel.add(labelStatus);
        springLayout.putConstraint(SpringLayout.EAST, buttonStart, 0, SpringLayout.EAST, labelStatus);
        springLayout.putConstraint(SpringLayout.WEST, buttonStart, -60, SpringLayout.EAST, labelStatus);
        springLayout.putConstraint(SpringLayout.EAST, labelStatus, 355, SpringLayout.WEST, spinner);
        springLayout.putConstraint(SpringLayout.WEST, labelStatus, 0, SpringLayout.WEST, spinner);
        springLayout.putConstraint(SpringLayout.SOUTH, labelStatus, 128, SpringLayout.NORTH, panel);
        springLayout.putConstraint(SpringLayout.NORTH, labelStatus, 110, SpringLayout.NORTH, panel);

        final JLabel label_4 = new JLabel();

        label_4.setText("数据来源：www.cnproxy.com");
        panel.add(label_4);
        springLayout.putConstraint(SpringLayout.SOUTH, label_4, 83, SpringLayout.NORTH, panel);
        springLayout.putConstraint(SpringLayout.NORTH, label_4, 65, SpringLayout.NORTH, panel);
        springLayout.putConstraint(SpringLayout.WEST, label_4, 190, SpringLayout.WEST, panel);

        final JPanel panel_2 = new JPanel();

        getContentPane().add(panel_2, BorderLayout.EAST);

        final JPanel panel_3 = new JPanel();

        getContentPane().add(panel_3, BorderLayout.WEST);

        final JPanel panel_1 = new JPanel();

        getContentPane().add(panel_1, BorderLayout.SOUTH);

        //
    }

    /**
     * Launch the application
     * @param args
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ProxyFinder frame = new ProxyFinder();

                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void findProxy() {
        buildProxyList();
        watchStatus();
        testProxyList();
    }

    private void stopFinding() {

        // textAreaLog.setText(new String());
        proxyList.clear();
        threadList.clear();
        timer.cancel();
        addResult("用户中断操作。\n_______________________________________________\n\n");

        /*
         * for(int i=0; i<threadList.size(); i++){
         *       Thread thread=threadList.elementAt(i);
         *       try {
         *               thread.sleep(50);
         *               thread.interrupt();
         *       } catch (InterruptedException e) {
         *               // TODO Auto-generated catch block
         *               e.printStackTrace();
         *       }
         * }
         */
        labelStatus.setText(new String());
    }

    private void testProxyList() {
        int    num = spinnerNumberModel.getNumber().intValue();
        String log = "开始测试代理，共" + num + "个线程并行处理\n";

        addLog(log);

        Thread thread;
        String url = textFieldUrl.getText();

        threadList.clear();

        for (int i = 0; i < num; i++) {
            ProxyTestThread t = new ProxyTestThread(url, proxyList, this);

            thread = new Thread(t);
            thread.setName("线程" + (i + 1));
            threadList.add(thread);
            thread.start();
        }
    }

    private void watchStatus() {
        timer = new Timer();
        timer.schedule(new DisplayTask(this), 1000, 1000);
    }

    private void buildProxyList() {
        String            urlPrefix  = "http://www.cnproxy.com/proxy";
        String            urlPostfix = ".html";
        String            url;
        CnProxyPageParser parser;
        String            log;

        log = "\n_____________________\n开始生成备选代理列表：\n";
        textAreaLog.append(log);
        log = "\n__________________________________\n对" + textFieldUrl.getText() + "可能有效的代理服务器：\n";
        textAreaResult.append(log);
        proxyList = new Vector<ProxyDesc>();

        for (int i = 1; i <= 10; i++) {
            url    = urlPrefix + i + urlPostfix;
            parser = new CnProxyPageParser(url);

            Vector<ProxyDesc> list = parser.getProxyList();

            proxyList.addAll(list);
            log = "加入" + list.size() + "个备选代理，共" + proxyList.size() + "个备选代理。\n";
            textAreaLog.append(log);
        }

        totalProxyNum = proxyList.size();
        log           = "备选代理列表生成完成，共" + proxyList.size() + "个代理。\n\n";
        textAreaLog.append(log);
        textAreaLog.append("开始检测代理服务器……\n");
        buttonStart.setEnabled(true);
    }

    synchronized public void addLog(String log) {
        Thread thread = Thread.currentThread();

        if (threadList.contains(thread)) {
            textAreaLog.append(log);
        }
    }

    synchronized public void addResult(String log) {
        Thread thread = Thread.currentThread();

        if (threadList.contains(thread)) {
            textAreaResult.append(log);
        }
    }

    public void setStatus() {
        int activeThreadNum = 0;

        for (int i = 0; i < threadList.size(); i++) {
            Thread thread = threadList.elementAt(i);

            if (thread.isAlive()) {
                activeThreadNum++;
            }
        }

        labelStatus.setText("已测试" + (totalProxyNum - proxyList.size()) + "/" + totalProxyNum + "个代理服务器。"
                            + activeThreadNum + "个线程活跃中。");
    }

    public boolean getFlag() {
        return isFinding;
    }
}
