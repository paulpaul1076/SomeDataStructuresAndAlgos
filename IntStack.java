/**
 * Created by Paul on 5/1/2015.
 */
public class IntStack {
    private int[] data = new int[2];
    private int size;
    public int getSize(){
        return size;
    }
    private void doubleSize(){
        int[] newData = new int[data.length * 2];
        for(int i = 0; i < size; ++i){
            newData[i] = data[i];
        }
        data = newData;
    }
    private void halfSize(){
        int[] newData = new int[data.length / 2];
        for(int i = 0; i < size; ++i){
            newData[i] = data[i];
        }
        data = newData;
    }
    public void push(int item){
        if(size == data.length) doubleSize();
        data[size++] = item;
    }
    public int pop(){
        if(size == data.length / 4) halfSize();
        if(size == 0) throw new IllegalStateException("trying to pop from and empty stack!");
        return data[--size];
    }
}
