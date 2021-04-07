package rip;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;;
/**
 * @author Zzy
 * @version 1.0
 * @date 2021/3/14 9:37
 */

public class Window extends JFrame implements ActionListener{
    private String lyq[]= {"路由器A","路由器B","路由器C","路由器D","路由器E","路由器F"};
    private String wl[]= {"网络1","网络2","网络3","网络4","网络5","网络6"};
    private String jb[]= {"保存","重置"};
    private JButton jbt[];//按钮
    private JCheckBox jcb[];//选择
    private JPanel JP1,JP2,jp,jp1,jp2,jp3,jp4,jp5,jp6;//面板
    private Container cp;//容器
    private JLabel jl1,jl2,jl3;//
    private JLabel jl[];//
    private NetWork net[];
    private Router rt[];
    private JComboBox<String> jcbb;
    private JScrollPane jsp1;//
    private JTextArea jta1;

    public void paint(Graphics g) {    //绘制连线
        super.paint(g);
        g.drawLine(110,150,260,100);//网络1

        g.drawLine(170,170,260,170);//网络2

        g.drawLine(120,195,170,235);//网络3

        g.drawLine(248,235,347,235);//网络4

        g.drawLine(430,235,480,195);//网络5

        g.drawLine(342,100,385,120);//网络6-1

        g.drawLine(342,160,385,145);//网络6-2

        g.drawLine(410,140,450,160);//网络6-3

    }

    public Window(NetWork[] net, Router[] rt) {
        super();
        this.net = net;
        this.rt = rt;
        this.setBounds(100, 200, 1400, 600);//设置窗口大小和位置
        cp=this.getContentPane();
        cp.setLayout(new GridLayout(1,2));
        JP1=new JPanel(new GridLayout(2,1));
        jp=new JPanel();
        jp.setLayout(null);

        //设置路由器按钮
        jbt=new JButton[8];
        jbt[0]=new JButton(lyq[0]);
        jbt[0].setBounds(80, 120, 80, 40);
        jbt[1]=new JButton(lyq[1]);
        jbt[1].setBounds(160, 180, 80, 40);
        jbt[2]=new JButton(lyq[2]);
        jbt[2].setBounds(340, 180, 80, 40);
        jbt[3]=new JButton(lyq[3]);
        jbt[3].setBounds(250, 120, 80, 40);
        jbt[4]=new JButton(lyq[4]);
        jbt[4].setBounds(250, 30, 80, 40);
        jbt[5]=new JButton(lyq[5]);
        jbt[5].setBounds(440, 120, 80, 40);

        //将按钮添加到面板上去
        jp.add(jbt[0]);
        jp.add(jbt[1]);
        jp.add(jbt[2]);
        jp.add(jbt[3]);
        jp.add(jbt[4]);
        jp.add(jbt[5]);

        jl=new JLabel[wl.length];//网络1的位置
        jl[0]=new JLabel();
        jl[0].setBounds(160, 40, 50, 50);
        jl[0].setText(wl[0]);
        jl[0].setForeground(Color.RED);

        jl[1]=new JLabel();//网络2的位置
        jl[1].setBounds(185, 105, 50, 50);
        jl[1].setText(wl[1]);
        jl[1].setForeground(Color.RED);

        jl[2]=new JLabel(); //网络3的位置
        jl[2].setBounds(80, 160, 50, 50);
        jl[2].setText(wl[2]);
        jl[2].setForeground(Color.RED);

        jl[3]=new JLabel();//网络4的位置
        jl[3].setBounds(270, 170, 50, 50);
        jl[3].setText(wl[3]);
        jl[3].setForeground(Color.RED);

        jl[4]=new JLabel(); //网络5的位置
        jl[4].setBounds(470, 155, 50, 50);
        jl[4].setText(wl[4]);
        jl[4].setForeground(Color.RED);


        jl[5]=new JLabel();//网络6的位置
        jl[5].setBounds(370, 75, 50, 50);
        jl[5].setText(wl[5]);
        jl[5].setForeground(Color.RED);

        jp.add(jl[0]);
        jp.add(jl[1]);
        jp.add(jl[2]);
        jp.add(jl[3]);
        jp.add(jl[4]);
        jp.add(jl[5]);


        jp.setBorder(new EtchedBorder(EtchedBorder.RAISED));//边界
        JP1.add(jp);

        jp1=new JPanel(new GridLayout(5,1));
        jp2=new JPanel(new FlowLayout());
        jl1=new JLabel("请勾选有故障的路由器：");
        jp2.add(jl1);
        jp1.add(jp2);

        jp3=new JPanel(new FlowLayout());
        jcb=new JCheckBox[lyq.length+wl.length];
        for(int i=0;i<lyq.length;i++)
        {
            jcb[i]=new JCheckBox(lyq[i]);
            jp3.add(jcb[i]);
        }
        jp3.setBorder(new EtchedBorder(EtchedBorder.RAISED));//边界
        jp1.add(jp3);

        jp4=new JPanel(new FlowLayout());
        jl2=new JLabel("请勾选有故障的网络：");
        jp4.add(jl2);
        jp1.add(jp4);

        jp5=new JPanel(new FlowLayout());
        for(int i=0;i<wl.length;i++)
        {
            jcb[i+lyq.length]=new JCheckBox(wl[i]);
            jp5.add(jcb[i+lyq.length]);
        }
        jp5.setBorder(new EtchedBorder(EtchedBorder.RAISED));//边界
        jp1.add(jp5);

        jp6=new JPanel(new FlowLayout());
        jbt[6]=new JButton(jb[0]);//保存按钮
        jbt[7]=new JButton(jb[1]);//重置按钮
        jp6.add(jbt[6]);
        jp6.add(jbt[7]);
        jp1.add(jp6);

        for(int i=0;i<8;i++)
        {
            jbt[i].addActionListener(this);
        }

        JP1.add(jp1);
        JP1.setBorder(new EtchedBorder(EtchedBorder.RAISED));//边界
        cp.add(JP1);
        JP2=new JPanel(new FlowLayout());
        jl3=new JLabel();
        jl3.setText("选择路由器查看路由表更新日志：");
        JP2.add(jl3);
        jcbb=new JComboBox<String>(lyq);
        jcbb.addActionListener(new ActionListen());
        JP2.add(jcbb);
        jta1 = new JTextArea(20, 35);
        jta1.setFont(new Font("微软雅黑", Font.BOLD, 18));
        JScrollPane jsp1 = new JScrollPane(jta1);
        jsp1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JP2.add(jsp1);
        cp.add(JP2);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // TODO Auto-generated method stub
        if((JButton)e.getSource()==jbt[6])//点击保存按钮
        {
            for(int i = 0; i < lyq.length; i++)
            {
                if(jcb[i].isSelected())
                {
                    if (rt[i + 1].isWork()) {
                        rt[i + 1].stop();//路由器线程暂停
                    }
                }
                else {
                    if (!rt[i + 1].isWork()) {
                        rt[i + 1].start();
                    }
                }
            }

            for(int i=0;i < wl.length;i++)
            {
                if(jcb[i + lyq.length].isSelected())
                {
                    if (net[i + 1].isWork()) {
                        net[i+1].stop();
                        jl[i].setForeground(Color.green);//网络线程关闭
                    }
                }
                else {
                    if (!net[i + 1].isWork()) {
                        net[i+1].start();
                        jl[i].setForeground(Color.red);
                    }
                }
            }
        }

        if((JButton)e.getSource() == jbt[7])//点击重置按钮
        {
            for(int i = 0; i < wl.length; i++)
            {
                jcb[i + lyq.length].setSelected(false);
                if (net[i + 1].isWork()) {
                    net[i + 1].stop();
                }
                net[i + 1].start();
                jl[i].setForeground(Color.red);
            }
            //路由器+网络线程全部设为工作状态
            for(int i = 0; i < lyq.length; i++)
            {
                jcb[i].setSelected(false);
                if (rt[i + 1].isWork()) {
                    rt[i + 1].stop();
                }
                rt[i + 1].start();
            }
        }

        if((JButton)e.getSource()==jbt[0])//点击路由器1
        {
            new SubWindow(rt[1].showRouteTable());
        }
        if((JButton)e.getSource()==jbt[1])//点击路由器2
        {
            new SubWindow(rt[2].showRouteTable());
        }
        if((JButton)e.getSource()==jbt[2])//点击路由器3
        {
            new SubWindow(rt[3].showRouteTable());
        }
        if((JButton)e.getSource()==jbt[3])//点击路由器4
        {
            new SubWindow(rt[4].showRouteTable());
        }
        if((JButton)e.getSource()==jbt[4])//点击路由器5
        {
            new SubWindow(rt[5].showRouteTable());
        }
        if((JButton)e.getSource()==jbt[5])//点击路由器6
        {
            new SubWindow(rt[6].showRouteTable());
        }
    }
    public class ActionListen implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if((String)jcbb.getSelectedItem()=="路由器A")//选择路由器1查看路由表更新日志
            {
                jta1.setText(rt[1].showLog());
            }
            if((String)jcbb.getSelectedItem()=="路由器B")//选择路由器2查看路由表更新日志
            {
                jta1.setText(rt[2].showLog());
            }
            if((String)jcbb.getSelectedItem()=="路由器C")//选择路由器3查看路由表更新日志
            {
                jta1.setText(rt[3].showLog());
            }
            if((String)jcbb.getSelectedItem()=="路由器D")//选择路由器4查看路由表更新日志
            {
                jta1.setText(rt[4].showLog());
            }
            if((String)jcbb.getSelectedItem()=="路由器E")//选择路由器5查看路由表更新日志
            {
                jta1.setText(rt[5].showLog());
            }
            if((String)jcbb.getSelectedItem()=="路由器F")//选择路由器6查看路由表更新日志
            {
                jta1.setText(rt[6].showLog());
            }
        }
    }
}


