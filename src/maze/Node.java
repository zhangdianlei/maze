package maze;

public  class Node{//Position类其中的节点
	public Position info;
	public Node next;
	
	public Node(Position o,Node n){//Node类构造方法
		info=o;
		next=n;			
	}
	public Node nextPosition(Node nd){		
		Node node = nd.next;
		return node;
	}
	
}