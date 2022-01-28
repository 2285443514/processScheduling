package OS;

import javax.swing.*;

public class CPU {
    static int Time = 0;
    static boolean seized = false;
    static String cname;
    static String bname;
    static final int SPACE_MAX = 2000;
    static final int READY_MAX = 6;
    static PCB[] reserveQueue = new PCB[20]; // 后备队列
    static PCB[] readyQueue = new PCB[20];   // 就绪队列
    static PCB[] blockQueue = new PCB[20];  // 阻塞队列
    static PCB[] CPUQueue = new PCB[2];   // 运行队列
    static PCB[] finishQueue = new PCB[30];    // 完成队列
    static PCB[] suspendQueue = new PCB[30];    // 挂起队列
    public RAMBlock RAMBlock = new RAMBlock();
    public RAMBlock head;

    public CPU() {
        head = RAMBlock;
    }

    public static class RAMBlock {
        public int start;    // 定义起始位置
        public int space;    // 定义内存大小
        public String pid;   // 存储在此的进程
        public boolean empty;  // 标志空间是否可被利用，true表示未分配
        RAMBlock next;

        public RAMBlock() {
            space = SPACE_MAX;
            empty = true;
            next = null;
            pid = null;
        }

        public RAMBlock(int st, int sp, boolean a, String name) {
            // 定义内存块起始位置、空间，是否空闲，占用此空间的程序名字
            start = st;
            space = sp;
            empty = a;
            next = null;
            pid = name;
        }
    }

    public boolean enterRAM(PCB p) {  // 判断程序能否进入内存
        RAMBlock h = head;
        while (h != null) {
            if (h.empty) {// 找到空内存块
                if (p.space < h.space) {// 如果内存块的内存空间量大于程序所需要的空间
                    p.address = h.start;//该内存块分布起始地址等于程序起始位置
                    RAMBlock r = new RAMBlock(h.start + p.space, h.space - p.space, true, null);
                    // 内存块多余的空间成为一个新的小内存块，并存入新的信息
                    r.next = h.next;
                    h.empty = false;//h块被程序占用
                    h.pid = p.getName();
                    h.next = r;
                    h.space = p.space;
                    return true;
                } else if (p.space == h.space) {// 如果内存块的空间与程序空间相等
                    p.address = h.start;
                    h.pid = p.getName();
                    h.empty = false;
                    return true;
                }
            }
            h = h.next;
        }
        return false;
    }

    public boolean recycleSpace(PCB p) { // 回收内存空间
        RAMBlock r1, r2;
        RAMBlock h = head;
        r1 = h;
        while (h != null) {
            if (h.start == p.address) { // 确认起始位置
                if (r1.empty) { // 为空，进行空间回收
                    r1.space = r1.space + h.space;
                    r1.next = h.next;
                    r1.pid = null;
                    h.next = null;
                    h = r1;
                }
                r2 = h.next;
                if (r2 == null) {// 不存在r2
                    h.empty = true;
                    h.pid = null;
                } else if (r2.empty) {// r2为空，合并内存块的空间为一个
                    h.space = h.space + r2.space;
                    h.empty = true;
                    h.pid = null;
                    h.next = r2.next;
                    r2.next = null; // 去掉r2
                } else {
                    h.empty = true;
                    h.pid = null;
                }
                return true;
            }
            r1 = h;
            h = h.next;
        }
        return false;
    }

    public static int getReadySize() {  // 返回就绪队列的实际长度
        int i = 0;
        for (; i < readyQueue.length; i++) {
            if (readyQueue[i] == null)
                break;
        }
        return i;
    }

    public static int getBlockSize() {  // 返回堵塞队列的实际长度
        int i = 0;
        for (; i < blockQueue.length; i++) {
            if (blockQueue[i] == null)
                break;
        }
        return i;
    }

    public static int getSuspendSize() {  // 返回挂起队列的实际长度
        int i = 0;
        for (; i < suspendQueue.length; i++) {
            if (suspendQueue[i] == null)
                break;
        }
        return i;
    }


    public static int getFinishSize() {  // 返回完成队列的实际长度
        int i = 0;
        for (; i < finishQueue.length; i++) {
            if (finishQueue[i] == null)
                break;
        }
        return i;
    }

    public static int getReserveSize() {  // 返回后备队列的实际长度
        int i = 0;
        for (; i < reserveQueue.length; i++) {
            if (reserveQueue[i] == null)
                break;
        }
        return i;
    }

    public static void updateReserve() {  // 更新后备队列
        int i = 0;
        if (reserveQueue[1] != null) {
            while (reserveQueue[i] != null) {
                reserveQueue[i] = reserveQueue[i + 1];
                i++;
            }
        } else
            reserveQueue[0] = null;
    }

    public void updateReady() {
        // 在就绪队列中的首条进程程序进入运行或是阻塞队列以后更新队列
        int i = 0;
        if (readyQueue[1] != null) {
            while (readyQueue[i] != null) {
                readyQueue[i] = readyQueue[i + 1];
                i++;
            }
            readyQueue[i] = null;
        } else
            readyQueue[0] = null;
    }

    public void updateBlock(int e) {
        // 堵塞队列中的进程进入就绪队列，对堵塞阻塞队列进行更新
        if (getBlockSize() > 1) {
            int i = e;
            while (blockQueue[i] != null) {
                blockQueue[i] = blockQueue[i + 1];
                i++;
            }
        } else
            blockQueue[e] = null;
    }

    public boolean enterBlock(PCB p) {
        // 判断是否进入阻塞队列（false表示会阻塞）
        int i = 0;
        if (p.isIndependent == 1) return true; // 独立进程不会进入阻塞状态
        if (p.getPrecursor().equals("无")) return true;// 无前驱
        while (i < getFinishSize()) {// 在完成队列中查找
            if (p.getPrecursor().equals(finishQueue[i].getName())) {
                // 找到匹配的前驱退出
                break;
            }
            i++;
        }
        // 未在完成队列中找到前驱则进入阻塞
        return i < getFinishSize();
    }

    public static void reserveSort() {
        // 进程队列排序
        int i = 0;
        int j;
        PCB p1;
        while (reserveQueue[i] != null) {
            j = i + 1;
            while (reserveQueue[j] != null) {
                if (reserveQueue[i].getArriveTime() > reserveQueue[j].getArriveTime()) {
                    p1 = reserveQueue[i];// 按到达时间排序
                    reserveQueue[i] = reserveQueue[j];
                    reserveQueue[j] = p1;
                }
                j++;
            }
            i++;
        }
        if (reserveQueue[i] != null) {
            while (reserveQueue[i].getArriveTime() <= Time) {
                // 按优先权排序
                i = 0;
                j = i + 1;
                while (reserveQueue[j].getArriveTime() <= Time) {
                    if (reserveQueue[i].getPriority() > reserveQueue[j].getPriority()) {
                        p1 = reserveQueue[i];
                        reserveQueue[i] = reserveQueue[j];
                        reserveQueue[j] = p1;
                    }
                    j++;
                }
                i++;
            }
        }
    }

    public void readySort() {  // 就绪队列按优先权排序
        int i = 0;
        int j;
        if (readyQueue[0] == null) return;
        PCB p1;
        while (readyQueue[i + 1] != null) {
            j = i + 1;
            while (readyQueue[j] != null) {
                if (readyQueue[i].getPriority() < readyQueue[j].getPriority()) {
                    p1 = readyQueue[i];
                    readyQueue[i] = readyQueue[j];
                    readyQueue[j] = p1;
                }
                j++;
            }
            i++;
        }
    }

    public void setProcess() {    // 自动生成一组进程
        reserveQueue[0] = new PCB("1号", 1, 3, 5, "无", "2号", "后备", 0, 300);
        reserveQueue[1] = new PCB("2号", 3, 8, 1, "1号", "无", "后备", 0, 400);
        reserveQueue[2] = new PCB("3号", 4, 5, 3, "无", "无", "后备", 1, 220);
        reserveQueue[3] = new PCB("4号", 2, 6, 1, "无", "无", "后备", 0, 150);
        reserveQueue[4] = new PCB("5号", 1, 5, 5, "2号", "7号", "后备", 0, 440);
        reserveQueue[5] = new PCB("6号", 6, 1, 3, "无", "4号", "后备", 0, 330);
        reserveQueue[6] = new PCB("7号", 3, 3, 6, "无", "无", "后备", 1, 240);
        reserveQueue[7] = new PCB("8号", 6, 7, 2, "5号", "无", "后备", 0, 400);
        reserveQueue[8] = new PCB("9号", 9, 3, 3, "无", "无", "后备", 1, 630);
        reserveSort();
    }


    public void reserveToReady() {    //后备队列的首条进程判断进入就绪队列
        while (reserveQueue[0] != null) {
            if (reserveQueue[0].getArriveTime() <= Time) {
                if (readyQueue[READY_MAX - 1] == null && enterRAM(reserveQueue[0])) //就绪队列未满和空间内存还未满
                {
                    reserveQueue[0].state = "就绪";
                    readyQueue[getReadySize()] = reserveQueue[0];// 存入
                    updateReserve();
                } else
                    break;
            } else {
                break;
            }
        }
        readySort();
    }

    public int judgePriorityCPU() {   // 返回cpu中优先级较小的那个序号
        if (CPUQueue[0].getPriority() > CPUQueue[1].getPriority())
            return 1;
        else return 0;
    }

    public static String Information(int i) {//  设置运行情况中的信息
        return (" " + CPUQueue[i].getName() + "\t" + CPUQueue[i].getPriority() + "\t正常运行" + "\t" + CPUQueue[i].getRunTime() + "\t" + (i + 1) + "\t" + CPUQueue[i].address + "\t" + CPUQueue[i].space + "\n");
    }

    public void RunCPU() {  //CPU运行
        if (CPUQueue[0] != null) {
            CPUQueue[0].RunTime--;// 还需要的运行时间-1
            if (CPUQueue[0].Priority > 1) {
                CPUQueue[0].Priority--;
            }// 优先权-1（最小为1）
        }
        if (CPUQueue[1] != null) {
            CPUQueue[1].RunTime--;
            if (CPUQueue[1].Priority > 1) {
                CPUQueue[1].Priority--;
            }
        }
    }

    public boolean enterCPU() {   // 进程进入CPU
        if (CPUQueue[0] == null) {// 判断能否进入CPU0
            CPUQueue[0] = readyQueue[0];
            return true;
        } else if (CPUQueue[1] == null) {// 判断能否进入CPU1
            CPUQueue[1] = readyQueue[0];
            return true;
        } else return false;

    }

    public boolean seizeCPU() {    //进程抢占CPU，先和CPU中优先级小的比较
        if (readyQueue[0].getPriority() > CPUQueue[judgePriorityCPU()].getPriority()) {
            CPUQueue[judgePriorityCPU()].ArriveTime = Time;
            bname = CPUQueue[judgePriorityCPU()].getName();
            // 进行抢占信息更改
            seized = true;
            PCB p1 = readyQueue[0];
            cname = p1.getName();
            readyQueue[0] = CPUQueue[judgePriorityCPU()];
            CPUQueue[judgePriorityCPU()] = p1;
            readySort();
            return true;
        } else
            return false;// 抢占失败
    }

    public void RunOrBlock() {   //判断就绪队列进入运行队列还是阻塞队列
        while (readyQueue[0] != null) {
            if (enterBlock(readyQueue[0])) {
                if (enterCPU()) {// 是否能进入CPU或者抢占CPU
                    updateReady();
                } else if (seizeCPU()) {
                } else
                    break;
            } else {// 更新CPU状态，及阻塞队列信息
                readyQueue[0].state = "阻塞";
                blockQueue[getBlockSize()] = readyQueue[0];
                updateReady();
            }
        }
        readySort();
    }

    public void BlockToReady() {   //阻塞队列进入就绪队列
        int i = 0;
        while (blockQueue[i] != null) {
            for (int j = 0; j < getFinishSize(); j++) {
                if (blockQueue[i].getPrecursor().equals(finishQueue[j].getName())) {// 在完成队列中找到前驱
                    if (readyQueue[READY_MAX - 1] == null) {
                        blockQueue[i].state = "就绪";
                        readyQueue[getReadySize()] = blockQueue[i];// 对阻塞队列进行更新
                        readySort();
                        updateBlock(i);
                        i--;
                        break;
                    }
                }
            }
            i++;
        }
        readySort();
    }


    public void showCPU() {
        if (CPUQueue[0] != null) {
            if (CPUQueue[0].getRunTime() == 0) { // 判断是否运行结束，是则撤销进程
                finishQueue[getFinishSize()] = CPUQueue[0];
                if (recycleSpace(CPUQueue[0])) System.out.println("内存回收成功");
                CPUQueue[0] = null;
            }
        }
        if (CPUQueue[1] != null) {
            if (CPUQueue[1].getRunTime() == 0) {
                finishQueue[getFinishSize()] = CPUQueue[1];
                if (recycleSpace(CPUQueue[1])) System.out.println("内存回收成功");
                CPUQueue[1] = null;
            }
        }
    }

    public static boolean isCPUEmpty() {//判断CPU是否都空闲
        return CPUQueue[0] != null || CPUQueue[1] != null;
    }

    public void running() {

        CPU.Time++;
        RunCPU();// CPU运行
        showCPU();// CPU进程结束
        BlockToReady();// 阻塞队列进入就绪队列
        reserveToReady();// 后备队列进入就绪队列
        RunOrBlock();// 就绪队列运行还是阻塞

    }


    public void Suspend() {// 设置挂起
        String sname = JOptionPane.showInputDialog(null, "需要挂起的CPU号：", "挂起", JOptionPane.PLAIN_MESSAGE);
        if (CPUQueue[0] != null && sname.equals("1")) {
            CPUQueue[0].state = "挂起";
            recycleSpace(CPUQueue[0]);
            suspendQueue[getSuspendSize()] = CPUQueue[0];
            CPUQueue[0] = null;
        }
        if (CPUQueue[1] != null && sname.equals("2")) {
            CPUQueue[1].state = "挂起";
            recycleSpace(CPUQueue[1]);
            suspendQueue[getSuspendSize()] = CPUQueue[1];
            CPUQueue[1] = null;
        }
    }

    public boolean Un_suspend() {// 解挂
        int i = 0, j;
        String pname = JOptionPane.showInputDialog(null, "需要解挂的进程的进程名：", "解挂", JOptionPane.PLAIN_MESSAGE);
        while (suspendQueue[i] != null) {
            if (suspendQueue[i].name.equals(pname))// 寻找进程名
            {
                if (getReadySize() <= READY_MAX && enterRAM(suspendQueue[i])) {
                    suspendQueue[i].state = "就绪";
                    readyQueue[getReadySize()] = suspendQueue[i];
                    j = i;
                    while (suspendQueue[j] != null) {//如果多个进程被挂起，进行信息更新
                        suspendQueue[j] = suspendQueue[j + 1];
                        j++;
                    }
                    return true;
                } else return false;
            }
            i++;
        }
        return false;
    }
}
	
	

