package maze;

public  class Node{//Position�����еĽڵ�
	public Position info;
	public Node next;
	
	public Node(Position o,Node n){//Node�๹�췽��
		info=o;
		next=n;			
	}
	public Node nextPosition(Node nd){		
		Node node = nd.next;
		return node;
	}
	
}