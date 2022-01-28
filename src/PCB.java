package OS;

public class PCB {
    protected String name;              // ������
    protected int ArriveTime;           // ����ʱ��
    protected int RunTime;              // ����ʱ��
    protected int Priority;             // ���ȼ�
    protected int isIndependent;        // 1����������̣�0Ϊͬ������
    protected String last;              // ǰ��
    protected String next;              // ���
    protected String state;             // ״̬
    public int a;                       // ��¼ԭ��������ʱ��
    protected int space;                // ռ�õ��ڴ�ռ�
    protected int address;              // ��¼�ڴ������ʼ��ַ

    PCB(String name, int ArriveTime, int RunTime, int Priority, String last, String next, String state, int isIndependent, int space) {
        this.name = name;
        this.ArriveTime = ArriveTime;
        this.RunTime = RunTime;
        this.Priority = Priority;
        this.last = last;
        this.next = next;
        this.a = RunTime;
        this.state = state;
        this.isIndependent = isIndependent;
        this.space = space;
        this.address = 0;
    }

    public String getName() {  // ��ý�����
        return name;
    }

    public int getArriveTime() {  // ��õ���ʱ��
        return ArriveTime;
    }

    public int getRunTime() {  // �������ʱ��
        return RunTime;
    }

    public int getPriority() {    // ������ȼ�
        return Priority;
    }

    public String getPrecursor() {  // �����Ҫ��ǰ����Դ
        return last;
    }

    public String getSuccessor() {// �����Ϣ
        return next;
    }
}
