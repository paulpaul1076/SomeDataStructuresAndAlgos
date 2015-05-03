
public class RingBuffer<T>{
	private int size;
	private int end;
	private T[] data;
	public RingBuffer(int size){
		data = (T[]) new Object[size];
	}
	public void deposit(T item){
		data[(end++) % data.length] = item;
		size++;
	}
}
