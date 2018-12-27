package maze;

public class Position{
	public int x, y;

	public Position(int x,int y){
		this.x = x;
		this.y = y;
	}
	public void setPosition(int x1, int y1){
		this.x = x1;
		this.y = y1;
	}
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		Position p =(Position) o;
		return this.x == p.x && this.y == p.y;
	}
}