public class Stack<T>{
	class Node{
		T item;
		Node next;
	}
	Node first, last;
	int N; // count;
	T peek(){
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
	int getLength(){
		return N;
	}
}