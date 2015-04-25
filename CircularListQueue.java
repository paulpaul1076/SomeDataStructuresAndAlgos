public class CircularListQueue<T>{
	private Node last;
	private int N;
	private class Node{
		T item;
		Node next;
	}
	public void enqueue(T item){
		if(N == 0){
			last = new Node();
			last.item = item;
			last.next = last;
		}
		else{
			Node insNode = new Node();
			insNode.item = item;
			insNode.next = last.next;
			last.next = insNode;
			last = insNode;
		}
		N++;
	}
	public T dequeue(){
		if(N == 0) throw new IllegalStateException();
		T retVal;
		if(N == 1){
			retVal = last.item;
			last = null;
		}
		else{
			retVal = last.next.item;
			last.next = last.next.next;
		}
		N--;
		return retVal;
	}
	public int getSize(){
		return N;
	}
}