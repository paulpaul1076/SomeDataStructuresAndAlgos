class StackArray<T>{
		T[] data = (T[])new Object[2];
		int N; // count;
		
		private void doubleArray(){
			T[] newData = (T[]) new Object[data.length*2];
			for(int i = 0; i < N; ++i){
				newData[i] = data[i];
			}
			data = newData;
		}
		private void halfArray(){
			T[] newData = (T[]) new Object[data.length/2];
			for(int i = 0; i < N; ++i){
				newData[i] = data[i];
			}
			data = newData;
		}
		T pop(){
			if(N == data.length/4) halfArray();
			T ret = data[--N];
			data[N] = null;
			return ret;
		}
		void push(T item){
			if(N == data.length) doubleArray();
			data[N] = item;
			N++;
		}
		T[] getArray(){ return data; }
		boolean isEmpty(){return N==0;}
		T peek(){
			return data[N-1];
		}
		int getLength(){
			return N;
		}
	}