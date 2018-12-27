package maze;

import java.awt.*;
import java.io.*;
import java.net.URL;

import javax.swing.*;

public class Rollback{
	Position curPos;//当前位置
	Stack memory;//路径堆栈
	Maze maze;
	MazeGUI mt;
	TextArea area;
	URL url = Dialog.class.getResource("/icon/1.gif");
	Image person=new ImageIcon(url).getImage();
	int lastsize = 0;//记录前一次栈的size
	int topx,topy;
	JFileChooser fileDialog = new JFileChooser();
	BufferedReader in;
	FileReader fileReader;
	BufferedWriter out;
	FileWriter fileWriter;
	
	
	public Rollback(Maze m, MazeGUI mt,TextArea ta){
		this.maze = m;
		this.mt = mt;
		this.memory = new Stack();
		area = ta;
	}
	public int isPass(Position p){//判断周围的通道那些没走过（右，下，左，上）
		int j=p.x;
		int i=p.y;
		if(maze.mazeMap[i][j+1]==0){
			System.out.println("可以向右走");
			area.append("按照先右，下，左，上的顺序尝试，可以向右走，");
			return 0;
		}
		if(maze.mazeMap[i+1][j]==0){
			System.out.println("可以向下走");
			area.append("按照先右，下，左，上的顺序尝试，可以向下走，");
			return 1;
		}
		if(maze.mazeMap[i][j-1]==0){
			System.out.println("可以向左走");
			area.append("按照先右，下，左，上的顺序尝试，可知向左走，");
			return 2;
		}
		if(maze.mazeMap[i-1][j]==0){
			System.out.println("可以向上走");
			area.append("按照先右，下，左，上的顺序尝试，可以向上走，");
			return 3;
		}
		System.out.println("对不起，无路可走");
		area.append("经尝试，无路可走，只好返回");
		return -1; //周围都无法通过
	}
	
	public boolean forward(){//前进
		this.maze.mark(this.curPos,2);//标记为已走过
		int direction = this.isPass(this.curPos);
		if(direction == -1){
			return false;//周围都无法通过时返回false
		}
		int x = this.curPos.x +(1 - direction) % 2;
		int y = this.curPos.y +(2 - direction) % 2;
		this.curPos.x = x;
		this.curPos.y = y;
		remember(this.curPos);
		return true;
	}

	public void back(){//forward()返回false时执行该方法
		this.maze.mark(this.curPos,3);//取消脚印
		topx=memory.top().x;
		topy=memory.top().y;
		this.memory.pop();//出栈
		
		Position p = (Position) this.memory.top();//获取前一个位置
		if(p != null){
			this.curPos.x = p.x;//将前一个位置设为当前位置
			this.curPos.y = p.y;
		}
		else return ;
	}

	public boolean isOver(){//判断是否结束,包括找到终点和没找到终点两种情况
		if(this.curPos.x==maze.end.x&&this.curPos.y==maze.end.y){
			
		//	area.append("寻路成功！\n");
			Object[] options1 = {"取消","保存 "};
			int response1=JOptionPane.showOptionDialog(null, "Congratulations！\n若想保存寻路过程请点击保存(存于C盘 文档)","胜利啦！",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options1, options1[0]);
			if(response1==0)
			{
				
			}
			else if(response1==1)
			{ 
				byte [] a = area.getText().getBytes();
				try{
					FileOutputStream out = new FileOutputStream("C:\\Users\\Public\\Documents\\成功--迷宫寻路过程.txt");
					out.write(a);
					out.close();
				}catch(IOException e){
					JOptionPane.showMessageDialog(null, "文件保存失败！");
				}		
			}
			return true;
		}
		else if(this.memory.isEmpty()){//栈已为空，找不到出路
		//	JOptionPane.showMessageDialog(null, "对不起啦，我们被包围了!\n什么！想作弊，重新修改迷宫？");
			area.append("\n被困于迷宫矣……\n\n");
			Object[] options2 = {"作弊","保存 ","就这样吧"};
			int response2=JOptionPane.showOptionDialog(null, "对不起啦，我们被包围了!\n就这样吧！\n什么！想作弊，重新修改迷宫？\n若想保存寻路过程请点击保存(存于C盘 文档)\n", "纠结的选择",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options2, options2[0]);
			if(response2==0)
			{
				
			}else if(response2==1)
			{ 
				byte [] a = area.getText().getBytes();
				/*int state = fileDialog.showSaveDialog(null);
				File dir = new File("C:\\Users\\飘雷\\Desktop");
				String name = "baocun";
				File file = new File(dir,name);
				if(state == JFileChooser.APPROVE_OPTION){
					try {
						fileWriter = new FileWriter(file);
						out.write(area.getText());
						out.close();
						fileWriter.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}*/
				
				
				
			try{
					FileOutputStream out = new FileOutputStream("C:\\Users\\Public\\Documents\\失败--迷宫寻路过程.txt");
					out.write(a);
					out.close();
				}catch(IOException e){
					JOptionPane.showMessageDialog(null, "文件保存失败！");
				}				
			}else if(response2==2){
				
			}
			return true;
		}
		return false;
	}

	public void remember(Position p){//保存当前路径
		this.memory.push(new Position(p.x, p.y));
	}

	public void draw(Graphics g){//绘制人物的位置,以及栈的变化
		int c = memory.size;
		int x= (maze.col)*50, y =(maze.row-1)*50;
		
		if(!memory.isEmpty()){
			Position stacktop = memory.top();
			int a=stacktop.x;
			int b=stacktop.y;
			String Pushtop =  "( "+b+","+a+" )";
			String Poptop =  "( "+topy+","+topx+" )";
			if(c>=1){
				if(c>lastsize){
					area.append(Pushtop+" 入栈;\n");
				}
				if((lastsize-c==1)&&(lastsize!=2&&c!=1)){
					area.append(","+Poptop+" 出栈;\n");
				}
				/*if(c<lastsize){
					area.append(","+Poptop+" 出栈;\n");
				}*/
				lastsize = c;
			}
		}
			
		Node node = memory.top;
		if(c>=1){
			for(int i=c;i>0;i--){
				if(i==c){
					g.setColor(Color.RED);
					g.drawRect(x, y-i*20+25, 75, 20);
				}else{
					g.setColor(Color.blue);
					g.drawRect(x, y-i*20+25, 75, 20);	
				}
			
				//Position p = mey.top();
				Position p = node.info;
				int m=p.x;
				int n=p.y;
				String stack = "( "+n+","+m+" )";
				//area.append(stack+"; ");
				g.setFont(new Font("Serif",0,14));
				g.drawString(stack,x+15,y-i*20+40);
				g.setColor(Color.BLACK);
				g.setFont(new Font("楷书", 0, 15));
				//g.drawString("wahahahaha");
				g.drawString("堆栈变化", x+3 , y+50);
				g.setFont(new Font("Serif",0,14));
				node = memory.nextNode(node);						
			}			
			
		}
			if(curPos!=null){
			g.drawImage(person,this.curPos.x*50,24+this.curPos.y*50,50,50,null);
		}			
	}
	
	public void flush(){//刷新，清空栈，并把sprite中的curPos对象恢复到初试位置
		while(!this.memory.isEmpty()){
			this.memory.pop();
		}
		this.curPos.x=maze.begin.x;
		this.curPos.y=maze.begin.y;
		remember(this.curPos);
	}
	public void setCurPos(int x,int y){
		this.curPos=new Position(x,y);
	}
}