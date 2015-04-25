import java.util.Iterator;

public class RandomBag<T> implements Iterable<T>{
	private int size;
	private T[] data = (T[]) new Object[2];
	private void doubleArray(){
		T[] newData = (T[]) new Object[data.length * 2];
		for(int i = 0 ; i < size; ++i){
			newData[i] = data[i];
		}
		data = newData;
	}
	public boolean isEmpty(){
		return size == 0;
	}
	public int size(){
		return size;
	}
	void add(T item){
		if(size == data.length) doubleArray();
		data[size++] = item;
	}
	public Iterator<T> iterator() {
		return new RBIterator();
	}
	private class RBIterator implements Iterator<T>{
		private int curId;
		RBIterator(){
			Algorithms.shufftArray(data, 0, size);
		}
		public boolean hasNext() {
			return curId < size;
		}
		public T next() {
			return data[curId++];
		}
	}
}
