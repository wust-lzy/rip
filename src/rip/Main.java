package rip;

/**
 * @author Zzy
 * @version 1.0
 * @date 2021/3/14 9:39
 */

public class Main {
    public static Router[] rt;
    public static NetWork[] net;

    public static void addRouter(int n) {
        rt = new Router[n + 1];
        for (int i = 1; i <= n; i++) {
            rt[i] = new Router("路由器" + i);
        }
    }

    public static void addNetWork(int n) {
        net = new NetWork[n + 1];
        for (int i = 1; i <= n; i++) {
            net[i] = new NetWork("网络" + i);
        }
    }

    public static void initConnection() {
        rt[1].connectNetWork(net[1]);
        rt[1].connectNetWork(net[2]);
        rt[1].connectNetWork(net[3]);
        rt[1].connectRouter(rt[5]);
        rt[1].connectRouter(rt[4]);
        rt[1].connectRouter(rt[2]);

        rt[2].connectNetWork(net[3]);
        rt[2].connectNetWork(net[4]);
        rt[2].connectRouter(rt[1]);
        rt[2].connectRouter(rt[3]);

        rt[3].connectNetWork(net[4]);
        rt[3].connectNetWork(net[5]);
        rt[3].connectRouter(rt[2]);
        rt[3].connectRouter(rt[6]);

        rt[4].connectNetWork(net[2]);
        rt[4].connectNetWork(net[6]);
        rt[4].connectRouter(rt[1]);
        rt[4].connectRouter(rt[5]);
        rt[4].connectRouter(rt[6]);

        rt[5].connectNetWork(net[1]);
        rt[5].connectNetWork(net[6]);
        rt[5].connectRouter(rt[1]);
        rt[5].connectRouter(rt[4]);
        rt[5].connectRouter(rt[6]);

        net[1].connectRouter(rt[1]);
        net[1].connectRouter(rt[5]);

        net[2].connectRouter(rt[1]);
        net[2].connectRouter(rt[4]);

        net[3].connectRouter(rt[1]);
        net[3].connectRouter(rt[2]);

        net[4].connectRouter(rt[2]);
        net[4].connectRouter(rt[3]);

        net[5].connectRouter(rt[3]);
        net[5].connectRouter(rt[6]);

        net[6].connectRouter(rt[4]);
        net[6].connectRouter(rt[5]);
        net[6].connectRouter(rt[6]);
    }

    public static void startRouter() {
        for (int i = 1; i <= 6; i++) {
            rt[i].start();
        }
    }

    public static void startNetWork() {
        for (int i = 1; i <= 6; i++) {
            net[i].start();
        }
    }

    public static void showRouteTable() {
        for (int i = 1; i <= 6; i++) {
            rt[i].showRouteTable();
            rt[i].showLog();
        }
    }

    public static void main(String[] args) {
        addRouter(6);    //添加路由器界面和网络
        addNetWork(6);
        initConnection();   //初始化拓扑结构
        startRouter();     //路由器和网络开始工作
        startNetWork();
        new Window(net, rt);  //生成图形界面
    }
}
//test
