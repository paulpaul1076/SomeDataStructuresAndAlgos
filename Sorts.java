import java.util.ArrayList;
import java.util.List;
/**
 * Created by Paul on 5/6/2015.
 */
public class Sorts {
    // helper methods and fields
    private static Comparable[] aux;
    private static void merge(Comparable[] a, int lo, int mid, int hi){
        int i = lo, j = mid + 1;
        for(int k = lo; k <= hi; ++k){
            aux[k] = a[k];
        }
        for(int k = lo; k <= hi; ++k){
                 if(i > mid)              a[k] = aux[j++];
            else if(j > hi)               a[k] = aux[i++];
            else if(less(aux[j], aux[i])) a[k] = aux[j++];
            else                          a[k] = aux[i++];
        }
    }
    private static void mergeSortHelper(Comparable[] a, int lo, int hi){
        if(hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        mergeSortHelper(a, lo, mid);
        mergeSortHelper(a, mid + 1, hi);
        merge(a, lo, mid, hi);
    }
    private static void exch(Comparable[] a, int i, int j){
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    private static void exch(int[] a, int i, int j){
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    private static boolean less(Comparable a, Comparable b){
        return a.compareTo(b) < 0;
    }
    public static boolean isSorted(Comparable[] a){
        for(int i = 1; i < a.length; ++i){
            if(less(a[i], a[i - 1])) return false;
        }
        return true;
    }
    public static void show(Comparable[] a){
        for(int i = 0; i < a.length; ++i){
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }
    // helper methods end
    public static void insertionSort(Comparable[] a){
        for(int i = 1; i < a.length; ++i){
            for(int j = i; j > 0 && less(a[j], a[j - 1]); --j){
                exch(a, j, j - 1);
            }
        }
    }
    public static void selectionSort(Comparable[] a){
        for(int i = 0; i < a.length - 1; ++i){
            int min = i;
            for(int j = i + 1; j < a.length; ++j){
                if(less(a[j], a[min])) min = j;
            }
            exch(a, i, min);
        }
    }
    public static void bubbleSort(Comparable[] a){
        for(int i = a.length; i > 0; --i){
            for(int j = 1; j < i; ++j){
                if(less(a[j], a[j - 1])) exch(a, j, j - 1);
            }
        }
    }
    public static void mergeSort(Comparable[] a){
        aux = new Comparable[a.length];
        mergeSortHelper(a, 0, a.length - 1);
    }
    public static void quickSort(Comparable[] a){

    }
    public static void shellSort(Comparable[] a){
        int h = 1;
        while(h < a.length / 3) h = 3 * h + 1;
        while(h >= 1){
            for(int i = h; i < a.length; ++i){
                for(int j = i; j >= h && less(a[j], a[j - h]); j -= h)
                    exch(a, j, j - 1);
            }
            h /= 3;
        }
    }
    public static void shellSortMemoized(Comparable[] a){
        List<Integer> list = new ArrayList<>();
        for(int h = 1; h < a.length / 3; h = h * 3 + 1){
            list.add(h);
        }
        for(int i = list.size() - 1; i >= 0; --i){
            int h = list.get(i);
            for(int j = h; j < a.length; ++j){
                for(int k = j; k >= h && less(a[k], a[k - h]);  k -= h){
                    exch(a, k, k - h);
                }
            }
        }
    }
    public static void insertionSortPrimitive(int[] a){
        for(int i = 1; i < a.length; ++i){
            for(int j = i; j > 0 && a[j] < a[j - 1]; --j){
                exch(a, j, j - 1);
            }
        }
    }
    public static void shellSortDiffInc(Integer[] a){
        List<Integer> list = new ArrayList<>();
        int k9 = 0;
        int k4 = 2;
        int elem = 0;
        for(int k = 0; elem < a.length / 3; ++k){
            if(k % 2 == 0){
                elem = (int)(9 * Math.pow(4, k9) - 9 * Math.pow(2, k9) + 1);
                list.add(elem);
                k9++;
            }else{
                elem = (int)(Math.pow(4, k4) - 3 * Math.pow(2, k4) + 1);
                list.add(elem);
                k4++;
            }
        }
        //System.out.println("Increment sequence: ");
        for(int i = 0; i < list.size(); ++i){
            System.out.print(list.get(i) + " ");
        }
        System.out.println();
        for(int i = list.size() - 1; i >= 0; --i){
            int h = list.get(i);
            for(int k = h; k < a.length; ++k) {
                for (int j = k; j >= h && less(a[j], a[j - h]); j -= h) {
                    exch(a, j, j - h);
                }
            }
        }
    }
}