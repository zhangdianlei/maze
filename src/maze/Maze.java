package maze;

import java.awt.*;
import java.net.URL;

import javax.swing.*;

public class Maze  {
	int[][] mazeMap;//��ͼ����
	Position begin;//��ʼ����
	Position end;//��������
	Position drawPos=new Position(1,1);//�����
	URL urlWall = Maze.class.getResource("/icon/wall.jpg");
	URL urlRoad = Maze.class.getResource("/icon/road.jpg");
	URL urlGo = Maze.class.getResource("/icon/go.jpg");
	URL urlEnd = Maze.class.getResource("/icon/end.jpg");
	URL urlBegin = Maze.class.getResource("/icon/begin.jpg");
	URL urlStack = Maze.class.getResource("/icon/stack.jpg");
	
	
	Image wallPic=new ImageIcon(urlWall).getImage();
	Image roadPic=new ImageIcon(urlRoad).getImage();
	Image goPic=new ImageIcon(urlGo).getImage();
	Image endPic=new ImageIcon(urlEnd).getImage();
	Image beginPic=new ImageIcon(urlBegin).getImage();
	Image stackPic = new ImageIcon(urlStack).getImage();
	int row=0,col=0;
	public Maze(int row,int col){
		this.row=row;
		this.col=col;
		mazeMap=new int[row][col+1];
		for(int i=0;i<row;i++){
			for(int j=0;j<col+1;j++){
				if((i==0||j==0||j==col-1||i==row-1)&&(j!=col)){//�Թ��������Ϊǽ
					this.mazeMap[i][j]=1;
				}else if(j==col){                              
					this.mazeMap[i][j]=-2;//-2���ɿհ�
				}else if(i==0&&j==col||i==row-1&&j==col){
					this.mazeMap[i][j]=-2;//-2���ɿհ�
				}else{
					this.mazeMap[i][j]=0;
				}
			}
		}
	}
	public void mark(Position p,int m){//���λ�����߹�
		this.mazeMap[p.y][p.x]=m;
	}
	
	public void draw(Graphics g){//���Ƶ�ͼͼƬ
		for(int i=0;i<row;i++){
			for(int j=0;j<col+1;j++){
				if(begin!=null&&i==begin.y&&j==begin.x){//���
					g.drawImage(beginPic,j*50, 24+i*50, 50, 50,null);
				}else if(end!=null&&i==end.y&&j==end.x){//�յ�
					g.drawImage(endPic,j*50, 24+i*50, 50, 50,null);
				}else if(this.mazeMap[i][j] == 1){//ǽ��
					g.drawImage(wallPic,j*50, 24+i*50, 50, 50,null);
				}else if(this.mazeMap[i][j] == 2){//���߹���·��
					g.drawImage(goPic,j*50, 24+i*50, 50, 50,null);
				}else if(this.mazeMap[i][j] == 3){//���߹���·����������ӡ
					g.drawImage(roadPic,j*50, 24+i*50, 50, 50,null);
				}else if(this.mazeMap[i][j]==-2){
					g.drawImage(stackPic,j*50,24+i*50,80,50,null);
				}		
				else{//ͨ��
					g.drawImage(roadPic,j*50, 24+i*50, 50, 50,null);
				}
			}
		}
		g.drawRect(drawPos.x*50, 24+drawPos.y*50, 50, 50);
	}

	public void resume(){//�ָ��Թ�
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				if(this.mazeMap[i][j]==2||this.mazeMap[i][j]==3){
					this.mazeMap[i][j]=0;
				}
			}
		}
	}
	public void setEnd(int x,int y){
		end=new Position(x,y);
	}
	public void setBegin(int x,int y){
		begin=new Position(x,y);
	}
}