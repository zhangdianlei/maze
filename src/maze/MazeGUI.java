package maze;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class MazeGUI extends JFrame implements KeyListener,ActionListener{//�Թ�GUI
	//final Object obj=new Object();//������
	int row=0,col=0;
	int speed = 1000;
	Maze maze;//�����Թ�����
	Rollback rollback;
	TextArea area = new TextArea(6,20);
	Thread t1 = new Thread(new SolveThread(),"SolveThread");//Ѱ·���̣��ڳ�ʼ��Thread��ʱ����Ŀ����󴫵ݸ�����߳�ʵ��
	public void paint(Graphics g){//���û��Ƶ�ͼ��draw()�����ͻ�������λ�õ�draw()����
		maze.draw(g);
		rollback.draw(g);
	}
	
	public MazeGUI(int row,int col){//��ʼ������
		this.setTitle("~~�����Թ�~~");
		this.setLayout(new BorderLayout());
		this.setSize((col+1)*50+85, (row+1)*50+100);
		
		this.row=row;//�����Թ�����
		this.col=col;
		maze = new Maze(row,col);
	    rollback = new Rollback(maze, this,area);
		this.setVisible(true);
		
		JPanel p1 = new JPanel();//�����ı���ʾ��
		Font x = new Font("Serif",0,15);
		area.setFont(x);
		area.setFocusable(false);
		area.setText("���Ƕ�ջ�ı仯���̣�\n");
		area.setBackground(new Color(220,240,255));
		p1.setBackground(new Color(220,240,255));
		p1.add(area);		
		this.add(area,BorderLayout.SOUTH);
		this.setLocation(20, 10);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		//����ٶȵ�����
		  final JSlider speedSlider = new JSlider ();
		  speedSlider.setOrientation (JSlider.VERTICAL);//���÷���
		  speedSlider.setMinimum (100);//������Сֵ
		  speedSlider.setMaximum (2000);//�������ֵ
		  speedSlider.setMajorTickSpacing (200);//��������ż��
		  speedSlider.setMinorTickSpacing (50);//���ø���ż��
		  speedSlider.setPaintLabels (true);//Default:false��ʾ��ǩ
		  speedSlider.setPaintTicks (true);//Default:false��ʾ���
		  speedSlider.setPaintTrack (true);//Determines whether the track is painted on the slider
		  speedSlider.setValue (500);//���ó�ʼֵ
		  speedSlider.setFocusable(false);
		  this.add(speedSlider,BorderLayout.EAST);
		  speedSlider.addChangeListener(
		            new ChangeListener(){
		                public void stateChanged(ChangeEvent event){
		                    speed=speedSlider.getValue();
		                }
		            }
		        );
		  
		  
		//��ӻ����Ĳ�����ť
		MenuBar menuBar=new MenuBar();
		Menu helpMenu=new Menu("����");
		Menu settingMenu=new Menu("����");
		
		MenuItem exitItem=new MenuItem("�˳�");
		MenuItem mapItem=new MenuItem("ˢ�µ�ͼ");
		MenuItem helpItem=new MenuItem("������ʾ");
		MenuItem startItem = new MenuItem("��ʼ");
		MenuItem aboutItem = new MenuItem("����");
	
		helpMenu.add(helpItem);
		helpMenu.add(aboutItem);
		helpMenu.add(exitItem);
		settingMenu.add(mapItem);
		settingMenu.add(startItem);
		menuBar.add(helpMenu);
		menuBar.add(settingMenu);
		
		setMenuBar(menuBar);
		addKeyListener(this);
		exitItem.addActionListener(this);
		mapItem.addActionListener(this);
		helpItem.addActionListener(this);
		startItem.addActionListener(this);	
		aboutItem.addActionListener(this);
	}
	
	private class SolveThread implements Runnable{//�ṩһ��ʵ�ֽ��Runnable�ӿڵ����ʵ������Ϊһ���̵߳�Ŀ�����
		public void run(){
			while(!rollback.isOver()){//Ѱ·�������߳̽���		
					try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(!rollback.forward()){
						rollback.back();
					}
					repaint();
				}
			}
		}
	
	
	public void keyTyped(KeyEvent e){	
	}
	public void keyReleased(KeyEvent e){	
	}
	public void keyPressed(KeyEvent e){
		
		if(e.getKeyCode()==KeyEvent.VK_K){//����ɾ�������õ�ǽ
			maze.mark(maze.drawPos,0);
		}
		else if(e.getKeyCode()==KeyEvent.VK_J){//����ǽ
			maze.mark(maze.drawPos,1);
		}
		else if(e.getKeyCode()==KeyEvent.VK_Y){//��ʾ���ݷ����Թ�
			if(maze.begin==null){
				JOptionPane.showMessageDialog(null, "���û������");
			}else if(maze.end==null){
				JOptionPane.showMessageDialog(null, "�յ�û������");
			}else if(t1.getState().equals(Thread.State.NEW)){//��û���̴߳�������̬ʱ
				area.setText("���Ƕ�ջ�ı仯���̣�\n");
				area.append("( "+maze.begin.x+","+maze.begin.y+" ) "+"��ջ;\n");
				rollback.flush();			
				t1.start();//����Ѱ·�߳�
			}else if(t1.getState().equals(Thread.State.TERMINATED)){//�����̴߳�����ֹ̬ʱ���ֵ����Y����������ʾ
				t1=new Thread(new SolveThread(),"SolveThread");//�½��߳�
				rollback.flush();//ˢ��
				maze.resume();//�ָ��Թ�����ʼ״̬
				t1.start();
			}
			else{//��Ѱ·�����У��������ˡ�Y������
				System.out.print("repeat");
				area.append("( "+maze.begin.x+","+maze.begin.y+" ) "+"��ջ;\n");
				rollback.flush();//���ջ������sprite�е�curPos����ָ�������λ��
				maze.resume();//�ָ��Թ�����ʼ״̬
				repaint();
			 }
		    }
		else if(e.getKeyCode()==KeyEvent.VK_S){//����
			if(maze.drawPos.y+1>0&&maze.drawPos.y+1<row-1)//��ֹ��λ�����ƶ�������Χ�ġ�ǽ����
			maze.drawPos.y=maze.drawPos.y+1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_W){//����
			if(maze.drawPos.y-1>0&&maze.drawPos.y-1<row-1)
			maze.drawPos.y=maze.drawPos.y-1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_D){//����
			if(maze.drawPos.x+1>0&&maze.drawPos.x+1<col-1)
			maze.drawPos.x=maze.drawPos.x+1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_A){//����
			if(maze.drawPos.x-1>0&&maze.drawPos.x-1<col-1)
				maze.drawPos.x=maze.drawPos.x-1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_G){//���ó���
			maze.setEnd(maze.drawPos.x, maze.drawPos.y);
			
		}
		else if(e.getKeyCode()==KeyEvent.VK_H){//�������
			maze.setBegin(maze.drawPos.x, maze.drawPos.y);//�������
			//area.append("���"+"("+maze.drawPos.x+maze.drawPos.y+")"+"��ջ");
	
			rollback.setCurPos(maze.drawPos.x, maze.drawPos.y);//���û����㷨�еĵ�ǰ��
			//rollback.remember(rollback.curPos);	
		}
		repaint();
	}
	public void actionPerformed(ActionEvent e){
		String es=e.getActionCommand();
		if(es.equals("�˳�")){
			System.exit(0);
		}else if(es.equals("ˢ�µ�ͼ")){
			maze=new Maze(row,col);
			rollback=new Rollback(maze,this,area);
			area.setText("���Ƕ�ջ�ı仯���̣�\n");
			repaint();
		}else if(es.equals("������ʾ")){
			JOptionPane.showMessageDialog(null, "W�������ƶ�\nS�������ƶ�\nA�������ƶ�\nD�������ƶ�\nH���������\nG�������յ�\nJ������ǽ\nK��ɾ��ǽ\nY����ʾ\n");
		}else if(es.equals("����")){
			JOptionPane.showMessageDialog(null,"Version 1.0\nby���ŵ���");
		}
		else if(es.equals("��ʼ")){
			if(maze.begin==null){
				JOptionPane.showMessageDialog(null, "���û������");
			}else if(maze.end==null){
				JOptionPane.showMessageDialog(null, "�յ�û������");
			}else if(t1.getState().equals(Thread.State.NEW)){//��û���̴߳�������̬ʱ
				area.setText("���Ƕ�ջ�ı仯���̣�\n");
				area.append("( "+maze.begin.x+","+maze.begin.y+" ) "+"��ջ;\n");
				rollback.flush();			
				t1.start();//����Ѱ·�߳�
			}else if(t1.getState().equals(Thread.State.TERMINATED)){//�����̴߳�����ֹ̬ʱ���ֵ����Y����������ʾ
				t1=new Thread(new SolveThread(),"SolveThread");//�½��߳�
				rollback.flush();//ˢ��
				maze.resume();//�ָ��Թ�����ʼ״̬
				t1.start();
			}
			else{//��Ѱ·�����У��������ˡ�Y������
				System.out.print("repeat");
				area.append("( "+maze.begin.x+","+maze.begin.y+" ) "+"��ջ;\n");
				rollback.flush();//���ջ������sprite�е�curPos����ָ�������λ��
				maze.resume();//�ָ��Թ�����ʼ״̬
				repaint();
			 }}
	}
}