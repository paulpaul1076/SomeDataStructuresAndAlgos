public class QueueArray<T>{
	private int N, begin = -1, end = -1;
	private T[] data = (T[]) new Object[2];
	
	public void enqueue(T x){
		if(N == data.length) doubleSize();
		data[(++end) % data.length] = x;
		N++;
	}
	public T dequeue(){
		if(N == 0) throw new IllegalStateException();
		T retVal;
		if(N == data.length / 4) halfSize();
		retVal = data[(++begin) % data.length];
		N--;
		return retVal;
	}
	private void doubleSize(){
		T[] newData = (T[]) new Object[data.length * 2];
		for(int i = (begin + 1) % data.length, j = 0; j < N; i = (i + 1) % data.length, j++){
			newData[j] = data[i];
		}
		begin = -1; end = N-1;
		data = newData;
	}
	private void halfSize(){
		T[] newData = (T[]) new Object[data.length / 2];
		for(int i = (begin + 1) % data.length, j = 0; j < N; i = (i + 1) % data.length, j++){
			newData[j] = data[i];
		}
		begin = -1; end = N-1;
		data = newData;
	}
	public int getSize(){
		return N;
	}
}