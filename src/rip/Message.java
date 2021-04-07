package rip;

/**
 * @author Zzy
 * @version 1.0
 * @date 2021/3/14 9:35
 */

public class Message { //定义路由表的数据结构
    private String net; // 目的网络
    private int dist; // 距离
    private String nextRouter; // 下一跳路由器

    public Message() {    //初始化构造方法
        net = "————";
        dist = 0;
        nextRouter = "————";
    }

    public Message(String net, int dist, String nextRouter) {
        this.net = net;
        this.dist = dist;
        this.nextRouter = nextRouter;
    }

    public Message(Message msg) {
        this.net = msg.net;
        this.dist = msg.dist;
        this.nextRouter = msg.nextRouter;
    }

    public String getNetName() {
        return net;
    }

    public void setNetName(String net) {
        this.net = net;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public String getNextRouter() {
        return nextRouter;
    }

    public void setNextRouter(String nextRouter) {
        this.nextRouter = nextRouter;
    }

    public String toLocalString() {
        return net + "\t" + dist + "\t" +  nextRouter;
    }

    @Override
    public String toString() {
        return net + ", " + dist + ", " + nextRouter;
    }
}
