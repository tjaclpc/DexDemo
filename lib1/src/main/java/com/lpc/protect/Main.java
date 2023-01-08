package com.lpc.protect;

import com.lpc.protect.core.ApkCheck;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {

    static JLabel selectedFile;
    static JTextArea textArea;
    static int screenSizeWidth = 0;
    static int screenSizeHeight = 0;
    static JFrame frame;
    static JPanel actionPanel, processPanel;
    static String[] actionStr = new String[]{"文件选择", "开始加固", "取消加固"};
    static JButton[] actionBtn = new JButton[actionStr.length];
    static String[] progressStep = new String[]{
            "1.检查apk的完整性",
            "2.分析是否已加固",
            "3.检查安全漏洞",
            "4.资源文件保护（assets加密、h5加密）",
            "5.so保护（混淆、防脱壳、字符串加密）",
            "6.dex保护（dex抽取，核心函数vmp保护、防dump、防注入）",
            "7.重新生成新的apk",
            "8.检查包完整性",
            "9.发布到远程仓库供用户下载",
    };
    static ExecutorService taskRunner;
    static File curSelectFile;

    public static void main(String[] args) {


        // Setting the width and height of frame
        initData();
        initFrame();
        /*
         * add component to panel
         */
        fillActionPanel();
        fillProgressPanel();
        // set panel is visible
        frame.setVisible(true);
    }

    private static void initFrame() {
        // create JFrame instance
        frame = new JFrame("加固");
        frame.setBounds(screenSizeWidth / 6, screenSizeHeight / 6, screenSizeWidth * 2 / 3, screenSizeHeight * 2 / 3);
//        frame.setSize(screenSizeWidth * 2 / 3, screenSizeHeight * 2 / 3);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* create panel ,like HTML  div tag
         * we can create more panel and in JFrame set position
         * we can set text ,button  in panel
         */
        actionPanel = new JPanel();
        actionPanel.setBounds(0, 0, frame.getWidth(), 40);
//        actionPanel.setBackground(Color.BLUE);
        processPanel = new JPanel();
        processPanel.setBounds(0, actionPanel.getHeight(), frame.getWidth(), frame.getHeight() - actionPanel.getHeight());
//        processPanel.setBackground(Color.GREEN);
        // add panel
        frame.add(actionPanel);
        frame.add(processPanel);
    }

    private static void initData() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSizeWidth = (int) screenSize.getWidth();//
        screenSizeHeight = (int) screenSize.getHeight();//
        taskRunner = Executors.newSingleThreadExecutor();
    }

    private static void fillActionPanel() {
        actionPanel.setLayout(new FlowLayout());
        for (int i = 0; i < actionStr.length; i++) {
            addActionButton(i);
        }

    }

    private static void addActionButton(int i) {
        actionBtn[i] = new JButton(actionStr[i]);

        actionBtn[i].setBackground(Color.white);
        actionBtn[i].setForeground(Color.blue);

        actionBtn[i].setBounds(10, 10, 140, 30);

        if (i == 0) {
            actionBtn[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    showFileChooseView(frame);
                }
            });
        } else if (i == 1) {
            actionBtn[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    execProtect();
                }
            });
        } else if (i == 2) {
            actionBtn[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    cancelProtect();
                }
            });
        }
        actionPanel.add(actionBtn[i]);
    }

    private static void fillProgressPanel() {

        /*
         * set layout null
         */
        processPanel.setLayout(null);
        addLabelSelectedFile();
        addProgressView();
    }

    private static void addLabelSelectedFile() {
        selectedFile = new JLabel("已选中文件：");

        selectedFile.setBackground(Color.white);
        selectedFile.setForeground(Color.blue);

        selectedFile.setBounds(10, processPanel.getY() + 10, frame.getWidth() - 20, 30);


        processPanel.add(selectedFile);
    }

    private static void addProgressView() {
        textArea = new JTextArea("");
//        textArea.setBackground(Color.orange);
        textArea.setForeground(Color.red);

        textArea.setBounds(10, processPanel.getY() + selectedFile.getHeight() + 10, frame.getWidth() - 40, 300);


        processPanel.add(textArea);
    }

    private static void showFileChooseView(JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setBounds(10, 20, frame.getWidth() - 20, 320);

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.setFileFilter(new FileNameExtensionFilter("zip(*.apk)", "apk"));//
//        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("image(*.jpg,*.png,.gif)","jpg","png","gif"));
        int state = fileChooser.showOpenDialog(null);
        if (state == fileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println(file.getAbsolutePath());
            fileChooser.cancelSelection();
            curSelectFile = file;
            selectedFile.setText("已选中文件：" + file.getAbsolutePath());
        }

    }

    private static void execProtect() {
        if (curSelectFile == null) {
            showInfoHint("请先选择文件");
            return;
        }
//        if (!taskRunner.isTerminated()) {
//            showInfoHint("正在加固中，请稍后再试");
//            return;
//        }
        taskRunner.submit(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        textArea.append("开始加固");
                    }
                });

                for (int i = 0; i < progressStep.length; i++) {

                    final  int pos = i;
                    switch (pos){
                        case 0:
                            ApkCheck.getInstance().getUninstallApkInfo( curSelectFile.getAbsolutePath());
                            break;
                        default:
                            try {
                                int execTime = new Random().nextInt(5) * 1000;
                                Thread.sleep(execTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                    }

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            textArea.append("\n");
                            textArea.append(progressStep[pos]);
                            if (pos==progressStep.length){
                                textArea.append("加固完成");
                            }
                        }
                    });

                }
            }
        });

    }

    private static void cancelProtect() {
        if (curSelectFile == null) {
            showInfoHint("请先选择文件");
            return;
        }
        try {
            if (!taskRunner.isTerminated())
                taskRunner.shutdown();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    static void showInfoHint(String title) {
        new MyDialog1(frame,title).setVisible(true);
//        JDialog jDialog = new JDialog();
//        jDialog.setTitle("警告");
//        jDialog.setBounds(frame.getX() + frame.getWidth() / 2 - 150, frame.getY() + frame.getHeight() / 2 - 100, 300, 200);
//        Container container = jDialog.getContentPane();
//        container.setLayout(null);
////        container.setBackground(Color.yellow);
//        JLabel hintLabel = new JLabel(title);
//        hintLabel.setText(title);
//        hintLabel.setForeground(Color.red);
//        hintLabel.setBackground(Color.GREEN);
////        hintLabel.setBounds(container.getX(),container.getY(),container.getWidth(),container.getHeight());
//        container.add(hintLabel);
//
//
//        jDialog.show();
    }

    static class MyDialog1 extends JDialog {
        /**
         *
         */
        private static final long serialVersiOnUID = 1L;

        public MyDialog1(JFrame frame,String content) {
            super(frame, "出错了");
            Container conn = getContentPane();
            conn.add(new JLabel(content));
            setBounds(frame.getX() + frame.getWidth() / 2 - 150, frame.getY() + frame.getHeight() / 2 - 100, 300, 200);

        }

    }
}
