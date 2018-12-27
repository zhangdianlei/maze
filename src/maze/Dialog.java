package maze;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class Dialog extends JDialog {
	private JTextField tf1=new JTextField("8",10);
	private JTextField tf2=new JTextField("8",10);
	private JButton b=new JButton("生成迷宫");
	public static void main(String args[]){
		///////////////////////////////////////////////////////////////设置界面风格是我win7风格
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		new Dialog();
	}
	public Dialog(){
		setTitle("~~迷你迷宫~~");
		setLayout(new BorderLayout());
		JPanel p=new JPanel(new GridLayout(4,1));
		FlowLayout flowLayout=new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setHgap(10);
		FlowLayout flowLayout1=new FlowLayout();
		JPanel p1=new JPanel(flowLayout);
		JPanel p2=new JPanel(flowLayout);
		JPanel p3=new JPanel();
		JPanel p4=new JPanel();
		JPanel p5=new JPanel(flowLayout1);
		
		p1.add(new JLabel("         行数(包括墙面，大于3)：  "));
		p1.add(tf1);
		p2.add(new JLabel("         列数(包括墙面，大于3)：  "));
		p2.add(tf2);
		p1.setBackground(new Color(220,240,255));
		p2.setBackground(new Color(220,240,255));
		p3.setBackground(new Color(220,240,255));
		p4.setBackground(new Color(220,240,255));
		
		p.add(p3);
		p.add(p1);
		p.add(p2);
		p.add(p4);
		
		p5.add(b);
		p5.setBackground(new Color(196,100,150));
		
		URL url = Dialog.class.getResource("/icon/迷你迷宫.jpg");
		add(new JLabel(new ImageIcon(url)),BorderLayout.NORTH);
		add(p,BorderLayout.CENTER);
		add(p5,BorderLayout.SOUTH);
		setSize(345, 250);
		setLocation(400, 200);
		setVisible(true);
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				try{
					if(Integer.parseInt(tf1.getText())<=3||Integer.parseInt(tf2.getText())<=3){
						JOptionPane.showMessageDialog(null,"输入数据有误，请输入大于3的整数");
					}else{
						new MazeGUI(Integer.parseInt(tf1.getText()), Integer.parseInt(tf2.getText()));
						setVisible(false);
					}
								
				}catch(Exception eee){
					JOptionPane.showMessageDialog(null,"输入数据错误，请输入大于3的整数");
				}
			}
		});
		
	}
}