package maze;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class MazeGUI extends JFrame implements KeyListener,ActionListener{//迷宫GUI
	//final Object obj=new Object();//锁对象
	int row=0,col=0;
	int speed = 1000;
	Maze maze;//构建迷宫参数
	Rollback rollback;
	TextArea area = new TextArea(6,20);
	Thread t1 = new Thread(new SolveThread(),"SolveThread");//寻路进程，在初始化Thread类时，把目标对象传递给这个线程实例
	public void paint(Graphics g){//调用绘制地图的draw()方法和绘制人物位置的draw()方法
		maze.draw(g);
		rollback.draw(g);
	}
	
	public MazeGUI(int row,int col){//初始化窗体
		this.setTitle("~~迷你迷宫~~");
		this.setLayout(new BorderLayout());
		this.setSize((col+1)*50+85, (row+1)*50+100);
		
		this.row=row;//构建迷宫参数
		this.col=col;
		maze = new Maze(row,col);
	    rollback = new Rollback(maze, this,area);
		this.setVisible(true);
		
		JPanel p1 = new JPanel();//设置文本显示区
		Font x = new Font("Serif",0,15);
		area.setFont(x);
		area.setFocusable(false);
		area.setText("这是堆栈的变化过程：\n");
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
		
		//添加速度调节条
		  final JSlider speedSlider = new JSlider ();
		  speedSlider.setOrientation (JSlider.VERTICAL);//设置方向
		  speedSlider.setMinimum (100);//设置最小值
		  speedSlider.setMaximum (2000);//设置最大值
		  speedSlider.setMajorTickSpacing (200);//设置主标号间隔
		  speedSlider.setMinorTickSpacing (50);//设置辅标号间隔
		  speedSlider.setPaintLabels (true);//Default:false显示标签
		  speedSlider.setPaintTicks (true);//Default:false显示标号
		  speedSlider.setPaintTrack (true);//Determines whether the track is painted on the slider
		  speedSlider.setValue (500);//设置初始值
		  speedSlider.setFocusable(false);
		  this.add(speedSlider,BorderLayout.EAST);
		  speedSlider.addChangeListener(
		            new ChangeListener(){
		                public void stateChanged(ChangeEvent event){
		                    speed=speedSlider.getValue();
		                }
		            }
		        );
		  
		  
		//添加基本的操作按钮
		MenuBar menuBar=new MenuBar();
		Menu helpMenu=new Menu("帮助");
		Menu settingMenu=new Menu("操作");
		
		MenuItem exitItem=new MenuItem("退出");
		MenuItem mapItem=new MenuItem("刷新地图");
		MenuItem helpItem=new MenuItem("按键提示");
		MenuItem startItem = new MenuItem("开始");
		MenuItem aboutItem = new MenuItem("关于");
	
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
	
	private class SolveThread implements Runnable{//提供一个实现借口Runnable接口的类的实例，作为一个线程的目标对象
		public void run(){
			while(!rollback.isOver()){//寻路结束后，线程结束		
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
		
		if(e.getKeyCode()==KeyEvent.VK_K){//负责删除已设置的墙
			maze.mark(maze.drawPos,0);
		}
		else if(e.getKeyCode()==KeyEvent.VK_J){//设置墙
			maze.mark(maze.drawPos,1);
		}
		else if(e.getKeyCode()==KeyEvent.VK_Y){//演示回溯法走迷宫
			if(maze.begin==null){
				JOptionPane.showMessageDialog(null, "起点没有设置");
			}else if(maze.end==null){
				JOptionPane.showMessageDialog(null, "终点没有设置");
			}else if(t1.getState().equals(Thread.State.NEW)){//当没有线程处于运行态时
				area.setText("这是堆栈的变化过程：\n");
				area.append("( "+maze.begin.x+","+maze.begin.y+" ) "+"入栈;\n");
				rollback.flush();			
				t1.start();//启动寻路线程
			}else if(t1.getState().equals(Thread.State.TERMINATED)){//当该线程处于终止态时，又点击了Y，即重新演示
				t1=new Thread(new SolveThread(),"SolveThread");//新建线程
				rollback.flush();//刷新
				maze.resume();//恢复迷宫到初始状态
				t1.start();
			}
			else{//在寻路过程中，如果点击了“Y”按键
				System.out.print("repeat");
				area.append("( "+maze.begin.x+","+maze.begin.y+" ) "+"入栈;\n");
				rollback.flush();//清空栈，并把sprite中的curPos对象恢复到初试位置
				maze.resume();//恢复迷宫到初始状态
				repaint();
			 }
		    }
		else if(e.getKeyCode()==KeyEvent.VK_S){//向下
			if(maze.drawPos.y+1>0&&maze.drawPos.y+1<row-1)//禁止定位方框移动到最外围的“墙”上
			maze.drawPos.y=maze.drawPos.y+1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_W){//向上
			if(maze.drawPos.y-1>0&&maze.drawPos.y-1<row-1)
			maze.drawPos.y=maze.drawPos.y-1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_D){//向右
			if(maze.drawPos.x+1>0&&maze.drawPos.x+1<col-1)
			maze.drawPos.x=maze.drawPos.x+1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_A){//向左
			if(maze.drawPos.x-1>0&&maze.drawPos.x-1<col-1)
				maze.drawPos.x=maze.drawPos.x-1;
		}
		else if(e.getKeyCode()==KeyEvent.VK_G){//设置出口
			maze.setEnd(maze.drawPos.x, maze.drawPos.y);
			
		}
		else if(e.getKeyCode()==KeyEvent.VK_H){//设置入口
			maze.setBegin(maze.drawPos.x, maze.drawPos.y);//起点坐标
			//area.append("起点"+"("+maze.drawPos.x+maze.drawPos.y+")"+"入栈");
	
			rollback.setCurPos(maze.drawPos.x, maze.drawPos.y);//设置回溯算法中的当前点
			//rollback.remember(rollback.curPos);	
		}
		repaint();
	}
	public void actionPerformed(ActionEvent e){
		String es=e.getActionCommand();
		if(es.equals("退出")){
			System.exit(0);
		}else if(es.equals("刷新地图")){
			maze=new Maze(row,col);
			rollback=new Rollback(maze,this,area);
			area.setText("这是堆栈的变化过程：\n");
			repaint();
		}else if(es.equals("按键提示")){
			JOptionPane.showMessageDialog(null, "W：向上移动\nS：向下移动\nA：向左移动\nD：向右移动\nH：设置起点\nG：设置终点\nJ：设置墙\nK：删除墙\nY：演示\n");
		}else if(es.equals("关于")){
			JOptionPane.showMessageDialog(null,"Version 1.0\nby：张佃磊");
		}
		else if(es.equals("开始")){
			if(maze.begin==null){
				JOptionPane.showMessageDialog(null, "起点没有设置");
			}else if(maze.end==null){
				JOptionPane.showMessageDialog(null, "终点没有设置");
			}else if(t1.getState().equals(Thread.State.NEW)){//当没有线程处于运行态时
				area.setText("这是堆栈的变化过程：\n");
				area.append("( "+maze.begin.x+","+maze.begin.y+" ) "+"入栈;\n");
				rollback.flush();			
				t1.start();//启动寻路线程
			}else if(t1.getState().equals(Thread.State.TERMINATED)){//当该线程处于终止态时，又点击了Y，即重新演示
				t1=new Thread(new SolveThread(),"SolveThread");//新建线程
				rollback.flush();//刷新
				maze.resume();//恢复迷宫到初始状态
				t1.start();
			}
			else{//在寻路过程中，如果点击了“Y”按键
				System.out.print("repeat");
				area.append("( "+maze.begin.x+","+maze.begin.y+" ) "+"入栈;\n");
				rollback.flush();//清空栈，并把sprite中的curPos对象恢复到初试位置
				maze.resume();//恢复迷宫到初始状态
				repaint();
			 }}
	}
}