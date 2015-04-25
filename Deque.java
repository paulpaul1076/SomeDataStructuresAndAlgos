public class Deque<T>{
	private int N;
	private DNode first, last;
	private class DNode{
		T item;
		DNode prev, next;
		DNode(T item){
			this.item = item;
		}
	}
	public boolean isEmpty(){
		return N == 0;
	}
	public int size(){
		return N;
	}
	public void pushLeft(T item){
		if(N == 0){
			first = new DNode(item);
			last = first;
		}
		else{
			DNode newFirst = new DNode(item);
			first.prev = newFirst;
			newFirst.next = first;
			first = newFirst;
		}
		N++;
	}
	public void pushRight(T item){
		if(N == 0){
			last = new DNode(item);
			first = last;
		}
		else{
			DNode newLast = new DNode(item);
			last.next = newLast;
			newLast.prev = last;
			last = newLast;
		}
		N++;
	}
	public T popLeft(){
		T ret = null;
		if(N == 1){
			ret = first.item;
			first = last = null;
		}
		else if (N > 1){
			ret = first.item;
			first = first.next;
			first.prev = null;
		}
		else{
			throw new IllegalStateException();
		}
		N--;
		return ret;
	}
	public T popRight(){
		T ret = null;
		if(N == 1){
			ret = last.item;
			first = last = null;
		}
		else if(N > 1){
			ret = last.item;
			last = last.prev;
			last.next = null;
		}
		else{
			throw new IllegalStateException();
		}
		N--;
		return ret;
	}
}
