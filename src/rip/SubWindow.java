package rip;

import java.awt.Container;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 * @author Zzy
 * @version 1.0
 * @date 2021/3/14 9:38
 */

public class SubWindow extends JFrame{

    private JScrollPane jsp;//
    private Container con;


    public SubWindow(String s)
    {
        super();
        this.setBounds(470, 300, 400, 400);//设置窗口大小和位置
        con=new Container();
        con=this.getContentPane();
        JTextArea jta = new JTextArea(300, 300);
        jta.setFont(new Font("微软雅黑", Font.BOLD, 18));
        jta.setText(s);
        JScrollPane jsp = new JScrollPane(jta);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        con.add(jsp);
        this.setTitle("路由表");
        this.setVisible(true);
    }
}

