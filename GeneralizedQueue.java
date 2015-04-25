
public class GeneralizedQueue<T>{
	private int size;
	private Node first, last;
	private class Node{
		Node next;
		Node prev;
		T item;
		Node(T item){
			this.item = item;
		}
	}
	public int size(){
		return size;
	}
	public boolean isEmpty(){
		return size == 0;
	}
	public void insert(T item){
		if(size == 0){
			first = new Node(item);
			last = first;
		}
		else{
			last.next = new Node(item);
			last.next.prev = last;
			last = last.next;
		}
		size++;
	}
	public T delete(int k){
		//if(k > size) return null;
		Node curNode = last;
		for(int i = 0; i < k && curNode.prev != null; ++i){
			curNode = curNode.prev;
		}
		if(size == 1){
			first = last = null;
		}
		else if(curNode == first){
			first = first.next;
			first.prev = null;
		}
		else if(curNode == last){
			last = last.prev;
			last.next = null;
		}
		else{
			curNode.prev.next = curNode.next;
			curNode.next.prev = curNode.prev;
		}
		size--;
		return curNode.item;
	}
}
