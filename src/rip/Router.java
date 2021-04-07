package rip;

import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @author Zzy
 * @version 1.0
 * @date 2021/3/14 9:37
 */

public class Router {

    private boolean work; // 工作状态
    private long delay = 3000; // delay 毫秒未接收到设为不可达
    private String name; // 名称
    private List<NetWork> netWorks; // 连接的网络
    private List<Router> linkRt; // 相邻路由表
    private List<Message> routeTable; // 路由表
    private List<UpdateInfo> updateLog; // 更新日志
    private Queue<List<Message>> mq; // 缓存队列
    private SendRouteTable sendThread; // 发送线程
    private UpdateRouteTable updateThread; // 更新线程
    private List<ReceiveClock> clock; // 相邻路由器的发送时钟

    public Router(String name) { // 构造函数
        this.name = name;
        this.netWorks = new ArrayList<NetWork>();
        this.routeTable = new ArrayList<Message>();
        this.linkRt = new ArrayList<Router>();
        this.updateLog = new ArrayList<UpdateInfo>();
        this.mq = new LinkedList<List<Message>>();
        this.sendThread = new SendRouteTable(this);
        this.updateThread = new UpdateRouteTable(this);
        this.clock = new ArrayList<ReceiveClock>();
    }

    public boolean isWork() {
        return work;
    }

    public void start() { // 开始工作
        work = true;
        connectNetWork(); // 初始化路由表
        sendThread = new SendRouteTable(this);
        updateThread = new UpdateRouteTable(this);
        sendThread.start();
        updateThread.start();
    }

    public void stop() { // 停止工作
        work = false;
        sendThread.interrupt();
        updateThread.interrupt();
        clear();
    }

    public void clear() { // 清空相关缓存
        mq.clear();
        routeTable.clear();
        updateLog.clear();
    }

    public void connectRouter(Router rt) { // 连接路由器
        linkRt.add(rt);
        clock.add(new ReceiveClock(rt.name, System.currentTimeMillis() + delay));
    }

    public void connectNetWork(NetWork net) { // 与某网络连接
        netWorks.add(net);
    }

    public void connectNetWork() { // 与相邻网络连接，初始化路由表
        for (NetWork net: netWorks) {
            routeTable.add(new Message(net.getNetName(), 1, "-"));
        }
    }

    public void setNetBroken(String net) { // 网络不可用
        for (int i = 0; i < routeTable.size(); i++) {
            Message m = routeTable.get(i);
            if (m.getNetName() == net) {
                m.setDist(16);
            }
        }
    }

    public void setRouterBroken(String rt) { // 路由器不可用
        for (int i = 0; i < routeTable.size(); i++) {
            Message m = routeTable.get(i);
            if (m.getNextRouter() == rt) {
                m.setDist(16);
            }
        }
    }

    public String getRtName() { // 获取路由器名称
        return name;
    }

    public void setRtName(String name) { // 设置路由器名称
        this.name = name;
    }

    public List<Message> copyTable(List<Message> routeTable) { // 复制路由表
        List<Message> rt = new ArrayList<Message>();
        for (int i = 0; i < routeTable.size(); i++) {
            rt.add(new Message(routeTable.get(i)));
        }
        return rt;
    }

    public void sendMessage() { // 发出路由表
        synchronized(routeTable) {
            for (int i = 0; i < netWorks.size(); i++) {
                netWorks.get(i).receiveMessage(name, copyTable(routeTable));
            }
        }
    }

    public void receiveMessage(Pair<String, List<Message>> msg) { // 接收路由表
        if (!work) {
            return;
        }
        String sourceRoute = msg.getFirst(); // 源路由器
        List<Message> msg1 = copyTable(msg.getSecond()); // 路由表
        changeRouteStatus(sourceRoute); // 修改发送时钟
        for (int i = 0; i < msg1.size(); i++) { // 对报文中所有项目进行更改
            Message m = msg1.get(i);
            m.setDist(m.getDist() + 1);
            m.setNextRouter(sourceRoute);
        }
        synchronized (mq) {
            mq.add(msg1);
        }
    }

    public void insertIntoLog(Message m1, Message m2) { // 将更新记录插入更新日志
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss:SSS");
        Message mm1 = new Message(m1);
        Message mm2 = new Message(m2);
        String date = tf.format(new Date());
        updateLog.add(new UpdateInfo(mm1, mm2, date));
    }

    public void changeRouteStatus(String rt) { // 收到新消息后，修改接收时钟
        for (int i = 0; i < clock.size(); i++) {
            ReceiveClock rc = clock.get(i);
            if (rc.getRouter() == rt) {
                rc.setTimes(System.currentTimeMillis() + delay);
                break;
            }
        }
    }

    public void checkRouteStatus() { // 检查是否在规定时间内收到相邻路由器的信息，并修改状态
        for (int i = 0; i < clock.size(); i++) {
            ReceiveClock rc = clock.get(i);
            if (System.currentTimeMillis() > rc.getTimes()) {
                setRouterBroken(rc.getRouter());
            }
        }
    }

    public void updateRoute() { // 更新路由表
        checkRouteStatus(); // 检查状态
        synchronized(routeTable) {
            synchronized(mq) {
                if (!mq.isEmpty()) {
                    List<Message> list = mq.poll();
                    for (Message msg: list) {
                        boolean flag = false;
                        for (int i = 0; i < routeTable.size(); i++) {
                            Message m = routeTable.get(i);
                            if (m.getNetName() == msg.getNetName()) { // 若有到N的路由
                                flag = true;
                                if (msg.getDist() >= 16) {
                                    msg.setDist(16);
                                }
                                if (m.getNextRouter() == msg.getNextRouter()) { // 若下一跳为 X
                                    insertIntoLog(new Message(m), new Message(msg)); // 更新日志
                                    m.setDist(msg.getDist());
                                } else if (m.getDist() > msg.getDist()) { // 否则比较距离再更新
                                    insertIntoLog(new Message(m), new Message(msg)); // 更新日志
                                    m.setDist(msg.getDist());
                                    m.setNextRouter(msg.getNextRouter());
                                }
                            }
                        }
                        if (!flag) { // 若无到网络 N 的路由，则添加
                            routeTable.add(new Message(msg));
                            insertIntoLog(new Message(), new Message(msg));
                        }
                    }
                }
            }
        }
    }

    public String showRouteTable() { //输出路由表
        String s = "";
        s += name + "\t" + "路由表" + "\r\n";
        s += "网络 \t 距离 \t 下一跳" + "\r\n";
        for (Message m: routeTable) {
            s += m.toLocalString() + "\r\n";
        }
        return s;
    }

    public String showLog() { // 展示路由日志
        String s = "\t+--------------------------+" + "\r\n";
        s += "\t | " + name + "\t " + "路由日志" + " |" + "\r\n";
        s += "\t+--------------------------+" + "\r\n";
        s += "\r\n";
        s += "原信息项 \t\t 新信息项 \t\t 时间" + "\r\n";
        s += "--------------------------------------------------";
        s += "--------------------------------------------------" + "\r\n";
        for (int i = 0; i < updateLog.size(); i++) {
            Message m1 = updateLog.get(i).getM1();
            Message m2 = updateLog.get(i).getM2();
            String times = updateLog.get(i).getDate();
            s += m1 + " \t " + m2 + " \t " + times + "\r\n";
        }
        return s;
    }

}

class ReceiveClock { // 接收消息时钟
    private String router;
    private long times;

    public ReceiveClock() {
        router = null;
        times = 0;
    }

    public ReceiveClock(String router, long times) {
        this.router = router;
        this.times = times;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }
}

class UpdateInfo { // 更新日志信息项
    private Message m1;
    private Message m2;
    private String date;

    public UpdateInfo(Message m1, Message m2, String date) {
        this.m1 = m1;
        this.m2 = m2;
        this.date = date;
    }

    public Message getM1() {
        return m1;
    }

    public Message getM2() {
        return m2;
    }

    public String getDate() {
        return date;
    }
}

class UpdateRouteTable extends Thread { // 更新线程
    private Router router;

    public UpdateRouteTable(Router router) {
        this.router = router;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(100); //每0.1秒更新一次
                router.updateRoute();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

class SendRouteTable extends Thread { // 发送线程
    private Router router;

    public SendRouteTable(Router router) {
        this.router = router;
    }

    public void run() { // 0.1钟发一次路由表
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
            router.sendMessage();
        }
    }
}

