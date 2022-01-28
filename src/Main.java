package OS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    static int key;
    static boolean RUN = true;
    static boolean BREAK = false;
    JButton Set;
    JButton defaultProcess;
    JButton breakOff;
    JButton BreakSus;
    JButton Sus;
    JButton addButton;
    JTextField CPU1;
    JTextField CPU2;
    JTextField time1;
    JTextArea run0;
    JProgressBar progressBar;
    JTable mainTable, readyTable, blockTable, suspendTable, finishTable, spaceTable;
    JScrollPane js;     //滚动面板
    JLabel l1, l2, l3, l4, l5, l6;     //固定标签
    JTextField t1, t2, t3, t4, t5, t6;  //标签对应的文本框
    CPU c = new CPU();

    public Main() {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setTitle("操作系统实验");
        this.setLayout(null);
        this.setLocation(300,200);
        this.setSize(1300, 700);
        this.setBackground(new Color(240,240,240));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        progressBar=new JProgressBar(0,100);
        progressBar.setBounds(705,85,500,25);
        this.add(progressBar);

        l1 = new JLabel("进程名");
        l1.setFont(new Font("微软雅黑", Font.BOLD, 16));
        l1.setBounds(50, 50, 130, 30);
        this.add(l1);

        l2 = new JLabel("运行时间");
        l2.setFont(new Font("微软雅黑", Font.BOLD, 16));
        l2.setBounds(150, 50, 130, 30);
        this.add(l2);

        l3 = new JLabel("优先级");
        l3.setFont(new Font("微软雅黑", Font.BOLD, 16));
        l3.setBounds(250, 50, 130, 30);
        this.add(l3);

        l4 = new JLabel("前驱");
        l4.setFont(new Font("微软雅黑", Font.BOLD, 16));
        l4.setBounds(340, 50, 130, 30);
        this.add(l4);

        l5 = new JLabel("后继");
        l5.setFont(new Font("微软雅黑", Font.BOLD, 16));
        l5.setBounds(420, 50, 130, 30);
        this.add(l5);

        l6 = new JLabel("所占空间");
        l6.setFont(new Font("微软雅黑", Font.BOLD, 16));
        l6.setBounds(490, 50, 100, 30);
        this.add(l6);

        t1 = new JTextField();
        t1.setBounds(50, 77, 55, 28);
        t1.setBackground(new Color(205, 222, 221));
        this.add(t1);

        t2 = new JTextField();
        t2.setBounds(151, 77, 65, 28);
        t2.setBackground(new Color(205, 222, 221));
        this.add(t2);

        t3 = new JTextField();
        t3.setBounds(245, 77, 60, 28);
        t3.setBackground(new Color(205, 222, 221));
        this.add(t3);

        t4 = new JTextField();
        t4.setBounds(330, 77, 60, 28);
        t4.setBackground(new Color(205, 222, 221));
        this.add(t4);

        t5 = new JTextField();
        t5.setBounds(410, 77, 60, 28);
        t5.setBackground(new Color(205, 222, 221));
        this.add(t5);

        t6 = new JTextField();
        t6.setBounds(490, 77, 65, 28);
        t6.setBackground(new Color(205, 222, 221));
        this.add(t6);

        JLabel addLabel1 = new JLabel("CPU1");
        addLabel1.setFont(new Font("微软雅黑", Font.BOLD, 16));
        addLabel1.setBounds(820, 10, 150, 30);
        this.add(addLabel1);

        CPU1 = new JTextField("");
        CPU1.setFont(new Font("微软雅黑", Font.BOLD, 16));
        CPU1.setHorizontalAlignment(JTextField.CENTER);
        CPU1.setBounds(780, 40, 120, 40);
        CPU1.setBackground(new Color(205, 222, 221));
        this.add(CPU1);

        JLabel addLabel2 = new JLabel("CPU2");
        addLabel2.setFont(new Font("微软雅黑", Font.BOLD, 16));
        addLabel2.setBounds(1060, 10, 150, 30);
        this.add(addLabel2);

        CPU2 = new JTextField("");
        CPU2.setFont(new Font("微软雅黑", Font.BOLD, 16));
        CPU2.setHorizontalAlignment(JTextField.CENTER);
        CPU2.setBounds(1020, 40, 120, 40);
        CPU2.setBackground(new Color(205, 222, 221));
        this.add(CPU2);

        JLabel addLabel3 = new JLabel("Time");
        addLabel3.setFont(new Font("微软雅黑", Font.BOLD, 16));
        addLabel3.setBounds(940, 10, 60, 30);
        this.add(addLabel3);

        time1 = new JTextField("");
        time1.setFont(new Font("微软雅黑", Font.BOLD, 16));
        time1.setHorizontalAlignment(JTextField.CENTER);
        time1.setBounds(920, 40, 80, 40);
        time1.setBackground(new Color(205, 222, 221));
        this.add(time1);

        Set = new JButton("开始");
        Set.setFont(new Font("微软雅黑", Font.BOLD, 16));
        Set.setBounds(60, 20, 150, 30);
        this.add(Set);

        defaultProcess = new JButton("默认进程");
        defaultProcess.setFont(new Font("微软雅黑", Font.BOLD, 16));
        defaultProcess.setBounds(400, 20, 150, 30);
        this.add(defaultProcess);

        addButton = new JButton("新建进程");
        addButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        addButton.setBounds(230, 20, 150, 30);
        this.add(addButton);

        breakOff = new JButton("中断 ");
        breakOff.setFont(new Font("微软雅黑", Font.BOLD, 16));
        breakOff.setBounds(680, 400, 150, 40);
        this.add(breakOff);

        Sus = new JButton("挂起");
        Sus.setFont(new Font("微软雅黑", Font.BOLD, 16));
        Sus.setBounds(680, 460, 150, 40);
        this.add(Sus);

        BreakSus = new JButton("解挂");
        BreakSus.setFont(new Font("微软雅黑", Font.BOLD, 16));
        BreakSus.setBounds(680, 520, 150, 40);
        this.add(BreakSus);

        JLabel runLabel = new JLabel("运行情况");
        runLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        runLabel.setBounds(850, 110, 150, 30);
        this.add(runLabel);

        run0 = new JTextArea();
        run0.setBackground(new Color(230, 230, 230));
        run0.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(run0);
        scrollPane.setBounds(660, 140, 610, 200);
        this.add(scrollPane);

        Set.addActionListener(this);
        defaultProcess.addActionListener(this);
        breakOff.addActionListener(this);
        Sus.addActionListener(this);
        addButton.addActionListener(this);
        BreakSus.addActionListener(this);

        JLabel set = new JLabel("后备队列");
        set.setFont(new Font("微软雅黑", Font.BOLD, 16));
        set.setBounds(280, 110, 150, 30);
        this.add(set);

        Object[] columnTitle = new Object[]{"进程", "状态", "运行时间", "优先级", "前驱", "后继"};
        Object[][] tableData = new Object[15][6];
        mainTable = new JTable(tableData, columnTitle);
        js = new JScrollPane(mainTable);
        this.add(js);

        js.setBounds(50, 140, 600, 110);

        JLabel pQ = new JLabel("就绪队列");
        pQ.setFont(new Font("微软雅黑", Font.BOLD, 16));
        pQ.setBounds(280, 250, 150, 30);
        this.add(pQ);

        Object[] pQTitle = new Object[]{"进程", "状态", "运行时间", "优先级", "前驱", "后继", "起始位置", "占用空间"};
        Object[][] pQData = new Object[15][8];
        readyTable = new JTable(pQData, pQTitle);
        Component js1 = new JScrollPane(readyTable);
        this.add(js1);

        js1.setBounds(50, 280, 600, 110);

        JLabel jam = new JLabel("阻塞队列");
        jam.setFont(new Font("微软雅黑", Font.BOLD, 16));
        jam.setBounds(280, 390, 150, 30);
        this.add(jam);

        Object[] jamTitle = new Object[]{"进程", "状态", "运行时间", "优先级", "前驱", "后继", "起始位置", "占用空间"};
        Object[][] jamData = new Object[10][8];
        blockTable = new JTable(jamData, jamTitle);
        Component js2 = new JScrollPane(blockTable);
        this.add(js2);

        js2.setBounds(50, 420, 600, 90);

        JLabel sus = new JLabel("挂起队列");
        sus.setFont(new Font("微软雅黑", Font.BOLD, 16));
        sus.setBounds(280, 510, 150, 30);
        this.add(sus);

        Object[] susTitle = new Object[]{"进程", "状态", "运行时间", "优先级", "前驱", "后继"};
        Object[][] susData = new Object[10][6];
        suspendTable = new JTable(susData, susTitle);
        Component js3 = new JScrollPane(suspendTable);
        this.add(js3);

        js3.setBounds(50, 540, 600, 90);

        JLabel com = new JLabel("已完成队列");
        com.setFont(new Font("微软雅黑", Font.BOLD, 16));
        com.setBounds(860, 490, 150, 30);
        this.add(com);

        Object[] comTitle = new Object[]{"进程", "运行时间", "状态"};
        Object[][] comData = new Object[30][3];
        finishTable = new JTable(comData, comTitle);
        Component js4 = new JScrollPane(finishTable);
        this.add(js4);

        js4.setBounds(850, 520, 350, 120);

        JLabel ram = new JLabel("内存分配状态表");
        ram.setFont(new Font("微软雅黑", Font.BOLD, 16));
        ram.setBounds(860, 340, 150, 30);
        this.add(ram);

        Object[] ramTitle = new Object[]{"分区号", "分区大小", "分区始址", "状态", "占用程序"};
        Object[][] ramData = new Object[20][5];
        spaceTable = new JTable(ramData, ramTitle);
        Component js5 = new JScrollPane(spaceTable);
        this.add(js5);

        js5.setBounds(850, 370, 400, 120);

        setVisible(true);

    }


    public void printSpace() {//。打印存储空间
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 5; j++)
                spaceTable.getModel().setValueAt("", i, j);
        CPU.RAMBlock traver = c.head;
        int k = 0;
        while (traver != null) {
            String status = "未分配";
            if (!traver.empty) status = "已分配";
            spaceTable.getModel().setValueAt(k + 1, k, 0);
            spaceTable.getModel().setValueAt(traver.space, k, 1);
            spaceTable.getModel().setValueAt(traver.start, k, 2);
            spaceTable.getModel().setValueAt(status, k, 3);
            spaceTable.getModel().setValueAt(traver.pid, k, 4);
            k++;
            traver = traver.next;
        }
    }

    public void printReserve() { //打印等待队列
        for (int i = 0; i < 15; i++)//。行数
            for (int j = 0; j < 6; j++)//。列数
                mainTable.getModel().setValueAt("", i, j);
        for (int i = 0; i < CPU.getReserveSize(); i++) {
            mainTable.getModel().setValueAt(CPU.reserveQueue[i].getName(), i, 0);
            mainTable.getModel().setValueAt(CPU.reserveQueue[i].state, i, 1);
            mainTable.getModel().setValueAt(CPU.reserveQueue[i].getRunTime(), i, 2);
            mainTable.getModel().setValueAt(CPU.reserveQueue[i].getPriority(), i, 3);
            mainTable.getModel().setValueAt(CPU.reserveQueue[i].getPrecursor(), i, 4);
            mainTable.getModel().setValueAt(CPU.reserveQueue[i].getSuccessor(), i, 5);
        }
    }

    public void printReady() {  //打印就绪队列
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 8; j++)
                readyTable.getModel().setValueAt("", i, j);
        for (int i = 0; i < CPU.getReadySize(); i++) {
            readyTable.getModel().setValueAt(CPU.readyQueue[i].getName(), i, 0);
            readyTable.getModel().setValueAt(CPU.readyQueue[i].state, i, 1);
            readyTable.getModel().setValueAt(CPU.readyQueue[i].getRunTime(), i, 2);
            readyTable.getModel().setValueAt(CPU.readyQueue[i].getPriority(), i, 3);
            readyTable.getModel().setValueAt(CPU.readyQueue[i].getPrecursor(), i, 4);
            readyTable.getModel().setValueAt(CPU.readyQueue[i].getSuccessor(), i, 5);
            readyTable.getModel().setValueAt(CPU.readyQueue[i].address, i, 6);
            readyTable.getModel().setValueAt(CPU.readyQueue[i].space, i, 7);
        }
    }

    public void printBlock() {  //打印堵塞队列
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 8; j++)
                blockTable.getModel().setValueAt("", i, j);
        for (int i = 0; i < CPU.getBlockSize(); i++) {
            blockTable.getModel().setValueAt(CPU.blockQueue[i].getName(), i, 0);
            blockTable.getModel().setValueAt(CPU.blockQueue[i].state, i, 1);
            blockTable.getModel().setValueAt(CPU.blockQueue[i].getRunTime(), i, 2);
            blockTable.getModel().setValueAt(CPU.blockQueue[i].getPriority(), i, 3);
            blockTable.getModel().setValueAt(CPU.blockQueue[i].getPrecursor(), i, 4);
            blockTable.getModel().setValueAt(CPU.blockQueue[i].getSuccessor(), i, 5);
            blockTable.getModel().setValueAt(CPU.blockQueue[i].address, i, 6);
            blockTable.getModel().setValueAt(CPU.blockQueue[i].space, i, 7);
        }
    }

    public void printSuspend() {  //打印挂起队列
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 6; j++)
                suspendTable.getModel().setValueAt("", i, j);
        for (int i = 0; i < CPU.getSuspendSize(); i++) {
            suspendTable.getModel().setValueAt(CPU.suspendQueue[i].getName(), i, 0);
            suspendTable.getModel().setValueAt(CPU.suspendQueue[i].state, i, 1);
            suspendTable.getModel().setValueAt(CPU.suspendQueue[i].getRunTime(), i, 2);
            suspendTable.getModel().setValueAt(CPU.suspendQueue[i].getPriority(), i, 3);
            suspendTable.getModel().setValueAt(CPU.suspendQueue[i].getPrecursor(), i, 4);
            suspendTable.getModel().setValueAt(CPU.suspendQueue[i].getSuccessor(), i, 5);
        }
    }

    public void printFinish() {    //显示已经完成的进程
        for (int i = 0; CPU.finishQueue[i] != null && i < 30; i++) {
            finishTable.getModel().setValueAt(CPU.finishQueue[i].getName(), i, 0);
            finishTable.getModel().setValueAt(CPU.finishQueue[i].a, i, 1);
            finishTable.getModel().setValueAt("已完成", i, 2);
        }
    }

    public void printCPU(int i) {   //显示CPU状态
        if (i == 0) {
            if (CPU.CPUQueue[i] != null) {
                CPU1.setText(CPU.CPUQueue[i].getName() + "     " + CPU.CPUQueue[i].getPriority() + "     " + CPU.CPUQueue[i].getRunTime());
            } else
                CPU1.setText("空闲");
        } else if (i == 1) {
            if (CPU.CPUQueue[i] != null) {
                CPU2.setText(CPU.CPUQueue[i].getName() + "     " + CPU.CPUQueue[i].getPriority() + "     " + CPU.CPUQueue[i].getRunTime());
            } else
                CPU2.setText("空闲");
        }

        time1.setText("" + CPU.Time);
    }

    public void putProcess() {   //手动输入进程
        if (!t1.getText().isEmpty() && !t2.getText().isEmpty() && !t3.getText().isEmpty() && !t4.getText().isEmpty() && !t5.getText().isEmpty()) {
            int i = Integer.parseInt(t2.getText());
            int j = Integer.parseInt(t3.getText());
            int k = Integer.parseInt(t6.getText());
            String na1, na2;
            int indep = 0;
            na1 = t4.getText();
            na2 = t5.getText();
            if (na1.equals("无") && na2.equals("无")) indep = 1;
            PCB p = new PCB(t1.getText(), CPU.Time, i, j, t4.getText(), t5.getText(), "后备", indep, k);
            CPU.reserveQueue[CPU.getReserveSize()] = p;
            CPU.reserveSort();
        }

    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Set) {// 只有k=1，才能开始运行
            key = 1;
            RUN = true;
            BREAK = false;
            Set.setEnabled(false);
        } else if (e.getSource() == defaultProcess) {
            key = 2;
            c.setProcess();
            Set.setEnabled(true);
            JOptionPane.showMessageDialog(null, "添加默认进程成功。", null, JOptionPane.PLAIN_MESSAGE);
        } else if (e.getSource() == breakOff) {
            key = 3;
            RUN = false;
            Set.setEnabled(true);
        } else if (e.getSource() == addButton) {
            key = 4;
            Set.setEnabled(true);
            putProcess();
        } else if (e.getSource() == Sus) {
            key = 5;
            c.Suspend();
        } else if (e.getSource() == BreakSus) {
            RUN = false;
            if (!c.Un_suspend()) {
                JOptionPane.showMessageDialog(null, "就绪队列已满或内存空间不足，解挂失败！", null, JOptionPane.PLAIN_MESSAGE);
            } else {
                printSuspend();
            }
            key = 1;
            RUN = true;
        } else {
            key = -1;
        }

        if (e.getSource() == Set || e.getSource() == defaultProcess || e.getSource() == breakOff || e.getSource() == addButton || e.getSource() == Sus || e.getSource() == BreakSus) {
            printReserve();
            printReady();
            switch (key) {
                case 1:
                    printSpace();
                    c.BlockToReady();
                    c.reserveToReady();
                    c.RunOrBlock();
                    new Thread() {
                        public void run() {
                            while (CPU.getReserveSize() > 0 || CPU.getReadySize() > 0 || CPU.getBlockSize() > 0 || CPU.isCPUEmpty()) {
                                if (RUN) {
                                    progressBar.setValue(0);
                                    while (progressBar.getValue() != progressBar.getMaximum()) {
                                        if(RUN){
                                            try {// 延时
                                                Thread.sleep(10);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            progressBar.setValue(progressBar.getValue() + 1);
                                        }
                                        else break;
                                    }
                                    progressBar.setValue(0);
                                    if(!BREAK){
                                        c.running();//CPU运行
                                        if (CPU.seized) {
                                            run0.append("发生抢占：进程" + CPU.cname + "抢占进程" + CPU.bname + "\n");
                                            CPU.seized = false;
                                        }
                                        printCPU(0);
                                        printCPU(1);
                                        printReserve();
                                        printReady();
                                        printBlock();
                                        printFinish();
                                        printSuspend();
                                        printSpace();
                                        if (CPU.CPUQueue[0] != null || CPU.CPUQueue[1] != null) {
                                            run0.append("\n\n 进程名\t优先级\t状态\t剩余运行时间\tCPU\t内存起始地址\t占用空间\n");
                                            for (int i = 0; i < 2; i++) {
                                                if (CPU.CPUQueue[i] != null) {
                                                    run0.append(CPU.Information(i));
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    run0.append("\n中断处理中.....\n");
                                    break;
                                }
                            }
                            if (RUN) {
                                CPU1.setText("空闲");
                                CPU2.setText("空闲");
                                run0.append("\n现有进程已经运行完成");
                            }
                        }
                    }.start();
                    break;
                case 3:
                    BREAK=true;
                    break;
            }
        }
    }

    public static void main(String[] args) {
            new Main();
        }
}