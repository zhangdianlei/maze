package maze;

import java.awt.*;
import java.io.*;
import java.net.URL;

import javax.swing.*;

public class Rollback{
	Position curPos;//��ǰλ��
	Stack memory;//·����ջ
	Maze maze;
	MazeGUI mt;
	TextArea area;
	URL url = Dialog.class.getResource("/icon/1.gif");
	Image person=new ImageIcon(url).getImage();
	int lastsize = 0;//��¼ǰһ��ջ��size
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
	public int isPass(Position p){//�ж���Χ��ͨ����Щû�߹����ң��£����ϣ�
		int j=p.x;
		int i=p.y;
		if(maze.mazeMap[i][j+1]==0){
			System.out.println("����������");
			area.append("�������ң��£����ϵ�˳���ԣ����������ߣ�");
			return 0;
		}
		if(maze.mazeMap[i+1][j]==0){
			System.out.println("����������");
			area.append("�������ң��£����ϵ�˳���ԣ����������ߣ�");
			return 1;
		}
		if(maze.mazeMap[i][j-1]==0){
			System.out.println("����������");
			area.append("�������ң��£����ϵ�˳���ԣ���֪�����ߣ�");
			return 2;
		}
		if(maze.mazeMap[i-1][j]==0){
			System.out.println("����������");
			area.append("�������ң��£����ϵ�˳���ԣ����������ߣ�");
			return 3;
		}
		System.out.println("�Բ�����·����");
		area.append("�����ԣ���·���ߣ�ֻ�÷���");
		return -1; //��Χ���޷�ͨ��
	}
	
	public boolean forward(){//ǰ��
		this.maze.mark(this.curPos,2);//���Ϊ���߹�
		int direction = this.isPass(this.curPos);
		if(direction == -1){
			return false;//��Χ���޷�ͨ��ʱ����false
		}
		int x = this.curPos.x +(1 - direction) % 2;
		int y = this.curPos.y +(2 - direction) % 2;
		this.curPos.x = x;
		this.curPos.y = y;
		remember(this.curPos);
		return true;
	}

	public void back(){//forward()����falseʱִ�и÷���
		this.maze.mark(this.curPos,3);//ȡ����ӡ
		topx=memory.top().x;
		topy=memory.top().y;
		this.memory.pop();//��ջ
		
		Position p = (Position) this.memory.top();//��ȡǰһ��λ��
		if(p != null){
			this.curPos.x = p.x;//��ǰһ��λ����Ϊ��ǰλ��
			this.curPos.y = p.y;
		}
		else return ;
	}

	public boolean isOver(){//�ж��Ƿ����,�����ҵ��յ��û�ҵ��յ��������
		if(this.curPos.x==maze.end.x&&this.curPos.y==maze.end.y){
			
		//	area.append("Ѱ·�ɹ���\n");
			Object[] options1 = {"ȡ��","���� "};
			int response1=JOptionPane.showOptionDialog(null, "Congratulations��\n���뱣��Ѱ·������������(����C�� �ĵ�)","ʤ������",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options1, options1[0]);
			if(response1==0)
			{
				
			}
			else if(response1==1)
			{ 
				byte [] a = area.getText().getBytes();
				try{
					FileOutputStream out = new FileOutputStream("C:\\Users\\Public\\Documents\\�ɹ�--�Թ�Ѱ·����.txt");
					out.write(a);
					out.close();
				}catch(IOException e){
					JOptionPane.showMessageDialog(null, "�ļ�����ʧ�ܣ�");
				}		
			}
			return true;
		}
		else if(this.memory.isEmpty()){//ջ��Ϊ�գ��Ҳ�����·
		//	JOptionPane.showMessageDialog(null, "�Բ����������Ǳ���Χ��!\nʲô�������ף������޸��Թ���");
			area.append("\n�������Թ��ӡ���\n\n");
			Object[] options2 = {"����","���� ","��������"};
			int response2=JOptionPane.showOptionDialog(null, "�Բ����������Ǳ���Χ��!\n�������ɣ�\nʲô�������ף������޸��Թ���\n���뱣��Ѱ·������������(����C�� �ĵ�)\n", "�����ѡ��",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options2, options2[0]);
			if(response2==0)
			{
				
			}else if(response2==1)
			{ 
				byte [] a = area.getText().getBytes();
				/*int state = fileDialog.showSaveDialog(null);
				File dir = new File("C:\\Users\\Ʈ��\\Desktop");
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
					FileOutputStream out = new FileOutputStream("C:\\Users\\Public\\Documents\\ʧ��--�Թ�Ѱ·����.txt");
					out.write(a);
					out.close();
				}catch(IOException e){
					JOptionPane.showMessageDialog(null, "�ļ�����ʧ�ܣ�");
				}				
			}else if(response2==2){
				
			}
			return true;
		}
		return false;
	}

	public void remember(Position p){//���浱ǰ·��
		this.memory.push(new Position(p.x, p.y));
	}

	public void draw(Graphics g){//���������λ��,�Լ�ջ�ı仯
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
					area.append(Pushtop+" ��ջ;\n");
				}
				if((lastsize-c==1)&&(lastsize!=2&&c!=1)){
					area.append(","+Poptop+" ��ջ;\n");
				}
				/*if(c<lastsize){
					area.append(","+Poptop+" ��ջ;\n");
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
				g.setFont(new Font("����", 0, 15));
				//g.drawString("wahahahaha");
				g.drawString("��ջ�仯", x+3 , y+50);
				g.setFont(new Font("Serif",0,14));
				node = memory.nextNode(node);						
			}			
			
		}
			if(curPos!=null){
			g.drawImage(person,this.curPos.x*50,24+this.curPos.y*50,50,50,null);
		}			
	}
	
	public void flush(){//ˢ�£����ջ������sprite�е�curPos����ָ�������λ��
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