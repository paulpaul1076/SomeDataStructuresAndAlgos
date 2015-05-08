import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    static double sortTestInsertionSort(Integer[] a, int T){
        long total = 0;
        //for(int i = 0; i < T; ++i){
            //Integer[] a = new Integer[N];
            //Algorithms.randomFillInteger(a, 0, N);
            StopWatch sw = new StopWatch();
            Sorts.insertionSort(a);
            total += sw.getElapsedMillis();
        //}
        return ((double)total);// / T;
    }
    static double sortTestInsertionSortPrimitive(int[] a, int T){
        long total = 0;
        for(int i = 0; i < T; ++i){
            //int[] a = new int[N];
            //Algorithms.randomFillInt(a, 0, N);
            StopWatch sw = new StopWatch();
            Sorts.insertionSortPrimitive(a);
            total += sw.getElapsedMillis();
        }
        return ((double)total) / T;
    }
    static double sortTestSelectionSort(Integer[] a, int T){
        long total = 0;
       // for(int i = 0; i < T; ++i){
            //Integer[] a = new Integer[N];
            //Algorithms.randomFillInteger(a, 0, N);
            StopWatch sw = new StopWatch();
            Sorts.selectionSort(a);
            total += sw.getElapsedMillis();
        //}
        return ((double)total);// / T;
    }
    static double sortTestBubbleSort(int N, int T){
        long total = 0;
        for(int i = 0; i < T; ++i){
            Integer[] a = new Integer[N];
            Algorithms.randomFillInteger(a, 0, N);
            StopWatch sw = new StopWatch();
            Sorts.bubbleSort(a);
            total += sw.getElapsedMillis();
        }
        return ((double)total) / T;
    }static double sortTestShellSort(int N, int T){
        long total = 0;
        for(int i = 0; i < T; ++i){
            Integer[] a = new Integer[N];
            Algorithms.randomFillInteger(a, 0, N);
            StopWatch sw = new StopWatch();
            Sorts.shellSort(a);
            total += sw.getElapsedMillis();
        }
        return ((double)total) / T;
    }
    public static void main(String[] args) {

        Integer[] a = new Integer[1000];
        Integer[] b = new Integer[1000];
        for(int i = 0; i < 1000; ++i){
            int r = Algorithms.getRandomInt(0, 2);
            a[i] = r;
            b[i] = r;
        }
        Sorts.show(a);
        Sorts.mergeSort(a);
        Sorts.show(a);
        System.out.println(Sorts.isSorted(a));
    }
}