public class Steque<T>{
	private int N;
	DNode first, last;
	private class DNode{
		DNode next, prev;
		T item;
		DNode(T item){
			this.item = item;
		}
	}
	public void push(T item){
		if(N == 0){
			first = new DNode(item);
			last = first;
		}
		else{
			DNode newLast = new DNode(item);
			last.next = newLast;
			newLast.prev = last;
			last = newLast;
		}
		N++;
	}
	public T pop(){
		T ret;
		if(N == 1){
			ret = last.item;
			first = last = null;
			N--;
		}
		else if(N > 1){
			ret = last.item;
			last = last.prev;
			last.next = null;
			N--;
		}
		else{
			throw new IllegalStateException();
		}
		return ret;
	}
	public void enqueue(T item){
		if(N == 0){
			first = new DNode(item);
			last = first;
		}
		else{
			DNode newFirst = new DNode(item);
			newFirst.next = first;
			first.prev = newFirst;
			first = newFirst;
		}
		N++;
	}
}