package rip;
import java.util.*;
/**
 * @author Zzy
 * @version 1.0
 * @date 2021/3/14 9:36
 */

public class NetWork {  //定义网络结构，一个网络结构包含的属性有名称，路由器，消息日志
    private boolean isWork; // 工作状态
    private String name; // 名称
    private List<Router> routers; // 连接的路由器
    private Queue<Pair<String, List<Message>>> mq; // 消息队列
    private NetSendRouteTable sendThread; // 发送线程

    public NetWork(String name) { // 构造函数
        this.name = name;
        this.routers = new ArrayList<Router>();
        this.mq = new LinkedList<Pair<String, List<Message>>>();
    }

    public String getNetName() { // 获取网络名称
        return name;
    }

    public boolean isWork() { // 返回工作状态
        return isWork;
    }

    public void start() { // 开始工作
        isWork = true;
        sendThread = new NetSendRouteTable(this);
        sendThread.start();
    }

    public void stop() { // 停止工作
        isWork = false;
        sendThread.interrupt(); //线程中断
        synchronized(mq) {
            mq.clear();
        }
        selfBroken();
    }

    public void selfBroken() { // 设置为自身不工作
        for (int i = 0; i < routers.size(); i++) {
            Router rt = routers.get(i);
            rt.setNetBroken(name);
        }
    }

    public void connectRouter(Router rt) { // 连接路由器
        routers.add(rt);
    }

    public void sendMessage() { // 向外发送一个路由表报文
        synchronized(mq) {
            if (!mq.isEmpty()) {
                Pair<String, List<Message>> msg = mq.poll();
                for (int i = 0; i < routers.size(); i++) {
                    Router rt = routers.get(i);
                    if (rt.getRtName() != msg.getFirst()) {
                        rt.receiveMessage(msg);
                    }
                }
            }
        }
    }

    public void receiveMessage(String sourceRouter, List<Message> routeTable) { // 接收路由表
        if (!isWork) {
            return;
        }
        synchronized(mq) {
            mq.add(new Pair<String, List<Message>>(sourceRouter, routeTable));
        }
    }
}

class NetSendRouteTable extends Thread { // 网络发送线程类
    private NetWork netWork;

    public NetSendRouteTable(NetWork netWork) {
        this.netWork = netWork;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                break;
            }
            netWork.sendMessage();
        }
    }
}

