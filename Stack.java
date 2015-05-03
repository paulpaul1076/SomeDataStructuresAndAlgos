import java.util.Iterator;

public class Stack<T> implements Iterable<T>{
	class Node{
		T item;
		Node next;
	}
	private Node first, last;
	private int N = 0; // count;
	
	public Stack(){}
	
	public Stack(Stack<T> that){
		for(Iterator<T> it = that.iterator(); it.hasNext();){
			this.push(it.next());
		}
	}
	
	T peek(){
		if(last == null) throw new IllegalStateException();
		return last.item;
	}
	void push(T item){
		if(N == 0){
			first = new Node();
			first.item = item;
			last = first;
		}
		else if(N == 1){
			last = new Node();
			last.item = item;
			first.next = last;
		}
		else{
			last.next = new Node();
			last.next.item = item;
			last = last.next;
		}
		N++;
	}
	T pop(){
		T ret;
		if(N == 0) throw new NullPointerException();
		else if(N == 1){
			ret = first.item;
			first = last = null;
		}
		else if(N == 2){
			ret = last.item;
			last = first;
		}
		else{
			ret = last.item;
			Node current = first;
			while(current.next != last){
				current = current.next;
			}
			last = current;
		}
		N--;
		
		return ret;
	}
	int getSize(){
		return N;
	}
	public Iterator<T> iterator() {
		return new StackIterator();
	}
	private class StackIterator implements Iterator<T>{
		Node curNode = first;
		public boolean hasNext() {
			return curNode != null;
		}
		public T next() {
			T ret = curNode.item;
			curNode = curNode.next;
			return ret;
		}
	}
}