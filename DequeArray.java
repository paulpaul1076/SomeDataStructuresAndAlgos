public class DequeArray<T>{
	private T[] rightArray = (T[]) new Object[2];
	private T[] leftArray = (T[]) new Object[2];
	private int rightSize, leftSize;
	
	private int rightBegin = -1, rightEnd = -1;
	private int leftBegin = -1, leftEnd = -1;
	
	public boolean isEmpty(){
		return rightSize == 0 && leftSize == 0;
	}
	public int size(){
		return rightSize + leftSize;
	}
	private void doubleLeftArray(){
		T[] newLeftArray = (T[]) new Object[leftArray.length * 2];
		
		for(int i = leftBegin + 1, j = 0; j < leftSize; i = (i + 1) % newLeftArray.length, ++j){
			newLeftArray[j] = leftArray[i];
		}
		leftArray = newLeftArray;
		leftBegin = -1;
		leftEnd = leftSize - 1;
	}
	private void doubleRightArray(){
		T[] newRightArray = (T[]) new Object[rightArray.length * 2];
		
		for(int i = rightBegin + 1, j = 0; j < rightSize; i = (i + 1) % newRightArray.length, ++j){
			newRightArray[j] = rightArray[i];
		}
		rightArray = newRightArray;
		rightBegin = -1;
		rightEnd = rightSize - 1;
	}
	private void halfLeftArray(){
		T[] newLeftArray = (T[]) new Object[leftArray.length / 2];
		
		for(int i = leftBegin + 1, j = 0; j < leftSize; i = (i + 1) % newLeftArray.length, ++j){
			newLeftArray[j] = leftArray[i];
		}
		leftArray = newLeftArray;
		leftBegin = -1;
		leftEnd = leftSize - 1;
	}
	private void halfRightArray(){
		T[] newRightArray = (T[]) new Object[rightArray.length / 2];
		
		for(int i = rightBegin + 1, j = 0; j < rightSize; i = (i + 1) % newRightArray.length, ++j){
			newRightArray[j] = rightArray[i];
		}
		rightArray = newRightArray;
		rightBegin = -1;
		rightEnd = rightSize - 1;
	}
	public void pushLeft(T item){
		if(leftSize == leftArray.length) doubleLeftArray();
		leftArray[(++leftEnd) % leftArray.length] = item;
		leftSize++;
	}
	public void pushRight(T item){
		if(rightSize == rightArray.length) doubleRightArray();
		rightArray[(++rightEnd) % rightArray.length] = item;
		rightSize++;
	}
	public T popLeft(){
		if(leftSize == 0) {
			T ret = rightArray[(++rightBegin) % rightArray.length];
			rightArray[rightBegin % rightArray.length] = null;
			return ret;
		}
		if(leftSize == leftArray.length / 4) halfLeftArray();
		T ret = leftArray[leftEnd % leftArray.length];
		leftArray[leftEnd-- % leftArray.length] = null;
		leftSize--;
		return ret;
	}
	public T popRight(){
		if(rightSize == 0) {
			T ret = leftArray[(++leftBegin) % leftArray.length];
			leftArray[leftBegin % leftArray.length] = null;
			return ret;
		}
		if(rightSize == rightArray.length / 4) halfRightArray();
		T ret = rightArray[rightEnd % rightArray.length];
		rightArray[rightEnd-- % rightArray.length] = null;
		rightSize--;
		return ret;
	}
}