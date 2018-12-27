package maze;

public class Stack {
	public Node top;//�ڵ�ΪNode����
	public int size;//ջ�нڵ���
	
	public Stack(){//���췽��
		top=null;
		size=0;
	}
	
	public void push(Position o){
		top=new Node(o,top);
		size++;
	}
	public Position top(){
		if(isEmpty())return null;
		Position o=top.info;
		return o;
	}
	public void pop(){
		if(isEmpty()) return ;
		else{
			top=top.next;
			size--;
			return ;
		}
	}	
	public boolean isEmpty(){
		return size==0;
	}
	public Node nextNode(Node nd){
		Node node = nd.next;
		return node;
	}
}