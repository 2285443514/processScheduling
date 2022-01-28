package OS;

public class PCB {
    protected String name;              // 进程名
    protected int ArriveTime;           // 到达时间
    protected int RunTime;              // 运行时间
    protected int Priority;             // 优先级
    protected int isIndependent;        // 1代表独立进程，0为同步进程
    protected String last;              // 前驱
    protected String next;              // 后继
    protected String state;             // 状态
    public int a;                       // 记录原来的运行时间
    protected int space;                // 占用的内存空间
    protected int address;              // 记录内存分配起始地址

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

    public String getName() {  // 获得进程名
        return name;
    }

    public int getArriveTime() {  // 获得到达时间
        return ArriveTime;
    }

    public int getRunTime() {  // 获得运行时间
        return RunTime;
    }

    public int getPriority() {    // 获得优先级
        return Priority;
    }

    public String getPrecursor() {  // 获得需要的前驱资源
        return last;
    }

    public String getSuccessor() {// 后继信息
        return next;
    }
}
