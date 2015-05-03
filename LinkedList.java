import java.util.Iterator;

public class LinkedList<T extends Comparable<T>> implements Iterable<T>{
	private Node first, last;
	private int size;
	private class Node{
		Node prev, next;
		T item;
		Node(T item){
			this.item = item;
		}
	}
	public int size(){
		return size;
	}
	public void moveToFront(T item){
		if(size == 0){
			first = new Node(item);
			last = first;
			size++;
			return;
		}
		Node foundNode = find(item);
		if(foundNode != null && foundNode != first){
			if(foundNode == last){
				last.prev.next = null;
				last = last.prev;
				foundNode.next = first;
				first.prev = foundNode;
				foundNode.prev = null;
				first = foundNode;
			}
			else{
				foundNode.prev.next = foundNode.next;
				foundNode.next.prev = foundNode.prev;
				foundNode.prev = null;
				foundNode.next = first;
				first.prev = foundNode;
				first = foundNode;
			}
			size++;
		}
		else if(foundNode == null){
			Node newFirst = new Node(item);
			newFirst.next = first;
			first.prev = newFirst;
			first = newFirst;
			size++;
		}
	}
	private Node find(T item){
		Node curNode = first;
		while(curNode != null){
			if(curNode.item.compareTo(item) == 0){
				return curNode;
			}
			curNode = curNode.next;
		}
		return null;
	}
	public Iterator<T> iterator() {
		return new LLIterator();
	}
	private class LLIterator implements Iterator<T>{
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
