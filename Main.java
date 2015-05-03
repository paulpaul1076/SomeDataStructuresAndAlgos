import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    static void doublingTest(int N, boolean intOrInteger){
        if(intOrInteger){
            Integer[] testSub = new Integer[N];
            for(int i = 0; i < N; ++i){
                testSub[i] = Algorithms.getRandomInt(0, N);
            }
        }
        else{
            int[] testSub = new int[N];
            for(int i = 0; i < N; ++i){
                testSub[i] = Algorithms.getRandomInt(0, N);
            }
        }
    }
    static void doublingTestStack(int N, boolean flag){
        if(flag){
            StackArray<Integer> st = new StackArray<Integer>();
            for(int i = 0; i < N; ++i){
                st.push(Algorithms.getRandomInt(0, N));
            }
            for(int i = 0; i < N; ++i){
                st.pop();
            }
        }
        else{
            IntStack st = new IntStack();
            for(int i = 0; i < N; ++i){
                st.push(Algorithms.getRandomInt(0, N));
            }
            for(int i = 0; i < N; ++i){
                st.pop();
            }
        }
    }
    public static void main(String[] args) {
        RandomGridGen.main(10);
    }
}