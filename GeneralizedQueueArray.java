
public class GeneralizedQueueArray<T>{
	private int size;
	private T[] data = (T[]) new Object[2];
	public boolean isEmpty(){
		return size == 0;
	}
	private void doubleArray(){
		T[] newData = (T[]) new Object[data.length * 2];
		for(int i = 0; i < size; ++i){
			newData[i] = data[i];
		}
		data = newData;
	}
	private void halfArray(){
		T[] newData = (T[]) new Object[data.length / 2];
		for(int i = 0; i < size; ++i){
			newData[i] = data[i];
		}
		data = newData;
	}
	private void move(int nullPos){
		for(int i = nullPos; i + 1 < size; ++i){
			data[i] = data[i + 1];
		}
		data[size - 1] = null;
	}
	public void insert(T item){
		if(size == data.length) doubleArray();
		data[size++] = item;
	}
	public T delete(int k){
		T ret = null;
		ret = data[size - k - 1];
		move(size - k - 1);
		if(size == data.length / 4) halfArray();
		size--;
		return ret;
	}
	public int size(){
		return size;
	}
}