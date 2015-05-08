import java.util.Iterator;

public class RandomQueue<T> implements Iterable<T>{
	private T[] data = (T[]) new Object[2];
	private int size;
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
	public void enqueue(T item){ // add an item
		if(size == data.length) doubleArray();
		data[size++] = item;
	}
	public T dequeue(){ // return a random item and remove it
		if(size == data.length / 4) halfArray();
		int rand = Algorithms.getRandomInt(0, size);
		T temp = data[rand];
		data[rand] = data[size - 1];
		data[size--] = null;
		return temp;
	}
	public T sample(){ // return a random item
		int rand = Algorithms.getRandomInt(0, size);
		return data[rand];
	}
	public Iterator<T> iterator() {
		return new RQIterator();
	}
	private class RQIterator implements Iterator<T>{
		private int curId;
		RQIterator(){
			Algorithms.shuffleArray(data, 0, size);
		}
		public boolean hasNext() {
			return curId < size;
		}
		public T next() {
			return data[curId++];
		}
	}
}
