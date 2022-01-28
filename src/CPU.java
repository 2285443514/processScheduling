package OS;

import javax.swing.*;

public class CPU {
    static int Time = 0;
    static boolean seized = false;
    static String cname;
    static String bname;
    static final int SPACE_MAX = 2000;
    static final int READY_MAX = 6;
    static PCB[] reserveQueue = new PCB[20]; // �󱸶���
    static PCB[] readyQueue = new PCB[20];   // ��������
    static PCB[] blockQueue = new PCB[20];  // ��������
    static PCB[] CPUQueue = new PCB[2];   // ���ж���
    static PCB[] finishQueue = new PCB[30];    // ��ɶ���
    static PCB[] suspendQueue = new PCB[30];    // �������
    public RAMBlock RAMBlock = new RAMBlock();
    public RAMBlock head;

    public CPU() {
        head = RAMBlock;
    }

    public static class RAMBlock {
        public int start;    // ������ʼλ��
        public int space;    // �����ڴ��С
        public String pid;   // �洢�ڴ˵Ľ���
        public boolean empty;  // ��־�ռ��Ƿ�ɱ����ã�true��ʾδ����
        RAMBlock next;

        public RAMBlock() {
            space = SPACE_MAX;
            empty = true;
            next = null;
            pid = null;
        }

        public RAMBlock(int st, int sp, boolean a, String name) {
            // �����ڴ����ʼλ�á��ռ䣬�Ƿ���У�ռ�ô˿ռ�ĳ�������
            start = st;
            space = sp;
            empty = a;
            next = null;
            pid = name;
        }
    }

    public boolean enterRAM(PCB p) {  // �жϳ����ܷ�����ڴ�
        RAMBlock h = head;
        while (h != null) {
            if (h.empty) {// �ҵ����ڴ��
                if (p.space < h.space) {// ����ڴ����ڴ�ռ������ڳ�������Ҫ�Ŀռ�
                    p.address = h.start;//���ڴ��ֲ���ʼ��ַ���ڳ�����ʼλ��
                    RAMBlock r = new RAMBlock(h.start + p.space, h.space - p.space, true, null);
                    // �ڴ�����Ŀռ��Ϊһ���µ�С�ڴ�飬�������µ���Ϣ
                    r.next = h.next;
                    h.empty = false;//h�鱻����ռ��
                    h.pid = p.getName();
                    h.next = r;
                    h.space = p.space;
                    return true;
                } else if (p.space == h.space) {// ����ڴ��Ŀռ������ռ����
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

    public boolean recycleSpace(PCB p) { // �����ڴ�ռ�
        RAMBlock r1, r2;
        RAMBlock h = head;
        r1 = h;
        while (h != null) {
            if (h.start == p.address) { // ȷ����ʼλ��
                if (r1.empty) { // Ϊ�գ����пռ����
                    r1.space = r1.space + h.space;
                    r1.next = h.next;
                    r1.pid = null;
                    h.next = null;
                    h = r1;
                }
                r2 = h.next;
                if (r2 == null) {// ������r2
                    h.empty = true;
                    h.pid = null;
                } else if (r2.empty) {// r2Ϊ�գ��ϲ��ڴ��Ŀռ�Ϊһ��
                    h.space = h.space + r2.space;
                    h.empty = true;
                    h.pid = null;
                    h.next = r2.next;
                    r2.next = null; // ȥ��r2
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

    public static int getReadySize() {  // ���ؾ������е�ʵ�ʳ���
        int i = 0;
        for (; i < readyQueue.length; i++) {
            if (readyQueue[i] == null)
                break;
        }
        return i;
    }

    public static int getBlockSize() {  // ���ض������е�ʵ�ʳ���
        int i = 0;
        for (; i < blockQueue.length; i++) {
            if (blockQueue[i] == null)
                break;
        }
        return i;
    }

    public static int getSuspendSize() {  // ���ع�����е�ʵ�ʳ���
        int i = 0;
        for (; i < suspendQueue.length; i++) {
            if (suspendQueue[i] == null)
                break;
        }
        return i;
    }


    public static int getFinishSize() {  // ������ɶ��е�ʵ�ʳ���
        int i = 0;
        for (; i < finishQueue.length; i++) {
            if (finishQueue[i] == null)
                break;
        }
        return i;
    }

    public static int getReserveSize() {  // ���غ󱸶��е�ʵ�ʳ���
        int i = 0;
        for (; i < reserveQueue.length; i++) {
            if (reserveQueue[i] == null)
                break;
        }
        return i;
    }

    public static void updateReserve() {  // ���º󱸶���
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
        // �ھ��������е��������̳���������л������������Ժ���¶���
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
        // ���������еĽ��̽���������У��Զ����������н��и���
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
        // �ж��Ƿ�����������У�false��ʾ��������
        int i = 0;
        if (p.isIndependent == 1) return true; // �������̲����������״̬
        if (p.getPrecursor().equals("��")) return true;// ��ǰ��
        while (i < getFinishSize()) {// ����ɶ����в���
            if (p.getPrecursor().equals(finishQueue[i].getName())) {
                // �ҵ�ƥ���ǰ���˳�
                break;
            }
            i++;
        }
        // δ����ɶ������ҵ�ǰ�����������
        return i < getFinishSize();
    }

    public static void reserveSort() {
        // ���̶�������
        int i = 0;
        int j;
        PCB p1;
        while (reserveQueue[i] != null) {
            j = i + 1;
            while (reserveQueue[j] != null) {
                if (reserveQueue[i].getArriveTime() > reserveQueue[j].getArriveTime()) {
                    p1 = reserveQueue[i];// ������ʱ������
                    reserveQueue[i] = reserveQueue[j];
                    reserveQueue[j] = p1;
                }
                j++;
            }
            i++;
        }
        if (reserveQueue[i] != null) {
            while (reserveQueue[i].getArriveTime() <= Time) {
                // ������Ȩ����
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

    public void readySort() {  // �������а�����Ȩ����
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

    public void setProcess() {    // �Զ�����һ�����
        reserveQueue[0] = new PCB("1��", 1, 3, 5, "��", "2��", "��", 0, 300);
        reserveQueue[1] = new PCB("2��", 3, 8, 1, "1��", "��", "��", 0, 400);
        reserveQueue[2] = new PCB("3��", 4, 5, 3, "��", "��", "��", 1, 220);
        reserveQueue[3] = new PCB("4��", 2, 6, 1, "��", "��", "��", 0, 150);
        reserveQueue[4] = new PCB("5��", 1, 5, 5, "2��", "7��", "��", 0, 440);
        reserveQueue[5] = new PCB("6��", 6, 1, 3, "��", "4��", "��", 0, 330);
        reserveQueue[6] = new PCB("7��", 3, 3, 6, "��", "��", "��", 1, 240);
        reserveQueue[7] = new PCB("8��", 6, 7, 2, "5��", "��", "��", 0, 400);
        reserveQueue[8] = new PCB("9��", 9, 3, 3, "��", "��", "��", 1, 630);
        reserveSort();
    }


    public void reserveToReady() {    //�󱸶��е����������жϽ����������
        while (reserveQueue[0] != null) {
            if (reserveQueue[0].getArriveTime() <= Time) {
                if (readyQueue[READY_MAX - 1] == null && enterRAM(reserveQueue[0])) //��������δ���Ϳռ��ڴ滹δ��
                {
                    reserveQueue[0].state = "����";
                    readyQueue[getReadySize()] = reserveQueue[0];// ����
                    updateReserve();
                } else
                    break;
            } else {
                break;
            }
        }
        readySort();
    }

    public int judgePriorityCPU() {   // ����cpu�����ȼ���С���Ǹ����
        if (CPUQueue[0].getPriority() > CPUQueue[1].getPriority())
            return 1;
        else return 0;
    }

    public static String Information(int i) {//  ������������е���Ϣ
        return (" " + CPUQueue[i].getName() + "\t" + CPUQueue[i].getPriority() + "\t��������" + "\t" + CPUQueue[i].getRunTime() + "\t" + (i + 1) + "\t" + CPUQueue[i].address + "\t" + CPUQueue[i].space + "\n");
    }

    public void RunCPU() {  //CPU����
        if (CPUQueue[0] != null) {
            CPUQueue[0].RunTime--;// ����Ҫ������ʱ��-1
            if (CPUQueue[0].Priority > 1) {
                CPUQueue[0].Priority--;
            }// ����Ȩ-1����СΪ1��
        }
        if (CPUQueue[1] != null) {
            CPUQueue[1].RunTime--;
            if (CPUQueue[1].Priority > 1) {
                CPUQueue[1].Priority--;
            }
        }
    }

    public boolean enterCPU() {   // ���̽���CPU
        if (CPUQueue[0] == null) {// �ж��ܷ����CPU0
            CPUQueue[0] = readyQueue[0];
            return true;
        } else if (CPUQueue[1] == null) {// �ж��ܷ����CPU1
            CPUQueue[1] = readyQueue[0];
            return true;
        } else return false;

    }

    public boolean seizeCPU() {    //������ռCPU���Ⱥ�CPU�����ȼ�С�ıȽ�
        if (readyQueue[0].getPriority() > CPUQueue[judgePriorityCPU()].getPriority()) {
            CPUQueue[judgePriorityCPU()].ArriveTime = Time;
            bname = CPUQueue[judgePriorityCPU()].getName();
            // ������ռ��Ϣ����
            seized = true;
            PCB p1 = readyQueue[0];
            cname = p1.getName();
            readyQueue[0] = CPUQueue[judgePriorityCPU()];
            CPUQueue[judgePriorityCPU()] = p1;
            readySort();
            return true;
        } else
            return false;// ��ռʧ��
    }

    public void RunOrBlock() {   //�жϾ������н������ж��л�����������
        while (readyQueue[0] != null) {
            if (enterBlock(readyQueue[0])) {
                if (enterCPU()) {// �Ƿ��ܽ���CPU������ռCPU
                    updateReady();
                } else if (seizeCPU()) {
                } else
                    break;
            } else {// ����CPU״̬��������������Ϣ
                readyQueue[0].state = "����";
                blockQueue[getBlockSize()] = readyQueue[0];
                updateReady();
            }
        }
        readySort();
    }

    public void BlockToReady() {   //�������н����������
        int i = 0;
        while (blockQueue[i] != null) {
            for (int j = 0; j < getFinishSize(); j++) {
                if (blockQueue[i].getPrecursor().equals(finishQueue[j].getName())) {// ����ɶ������ҵ�ǰ��
                    if (readyQueue[READY_MAX - 1] == null) {
                        blockQueue[i].state = "����";
                        readyQueue[getReadySize()] = blockQueue[i];// ���������н��и���
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
            if (CPUQueue[0].getRunTime() == 0) { // �ж��Ƿ����н���������������
                finishQueue[getFinishSize()] = CPUQueue[0];
                if (recycleSpace(CPUQueue[0])) System.out.println("�ڴ���ճɹ�");
                CPUQueue[0] = null;
            }
        }
        if (CPUQueue[1] != null) {
            if (CPUQueue[1].getRunTime() == 0) {
                finishQueue[getFinishSize()] = CPUQueue[1];
                if (recycleSpace(CPUQueue[1])) System.out.println("�ڴ���ճɹ�");
                CPUQueue[1] = null;
            }
        }
    }

    public static boolean isCPUEmpty() {//�ж�CPU�Ƿ񶼿���
        return CPUQueue[0] != null || CPUQueue[1] != null;
    }

    public void running() {

        CPU.Time++;
        RunCPU();// CPU����
        showCPU();// CPU���̽���
        BlockToReady();// �������н����������
        reserveToReady();// �󱸶��н����������
        RunOrBlock();// �����������л�������

    }


    public void Suspend() {// ���ù���
        String sname = JOptionPane.showInputDialog(null, "��Ҫ�����CPU�ţ�", "����", JOptionPane.PLAIN_MESSAGE);
        if (CPUQueue[0] != null && sname.equals("1")) {
            CPUQueue[0].state = "����";
            recycleSpace(CPUQueue[0]);
            suspendQueue[getSuspendSize()] = CPUQueue[0];
            CPUQueue[0] = null;
        }
        if (CPUQueue[1] != null && sname.equals("2")) {
            CPUQueue[1].state = "����";
            recycleSpace(CPUQueue[1]);
            suspendQueue[getSuspendSize()] = CPUQueue[1];
            CPUQueue[1] = null;
        }
    }

    public boolean Un_suspend() {// ���
        int i = 0, j;
        String pname = JOptionPane.showInputDialog(null, "��Ҫ��ҵĽ��̵Ľ�������", "���", JOptionPane.PLAIN_MESSAGE);
        while (suspendQueue[i] != null) {
            if (suspendQueue[i].name.equals(pname))// Ѱ�ҽ�����
            {
                if (getReadySize() <= READY_MAX && enterRAM(suspendQueue[i])) {
                    suspendQueue[i].state = "����";
                    readyQueue[getReadySize()] = suspendQueue[i];
                    j = i;
                    while (suspendQueue[j] != null) {//���������̱����𣬽�����Ϣ����
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
	
	

