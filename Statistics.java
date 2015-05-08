/**
 * Created by Paul on 5/3/2015.
 */
public class Statistics {
    public static double mean(int[] a){
        double sum = 0.0;
        for(int i = 0; i < a.length; ++i){
            sum += a[i];
        }
        return sum / a.length;
    }
    public static double mean(double[] a){
        double sum = 0.0;
        for(int i = 0; i < a.length; ++i){
            sum += a[i];
        }
        return sum / a.length;
    }
    public static double var(int[] a){
        double avg = mean(a);
        double sum = 0.0;
        for(int i = 0; i < a.length; ++i){
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (a.length - 1);
    }
    public static double var(double[] a){
        double avg = mean(a);
        double sum = 0.0;
        for(int i = 0; i < a.length; ++i){
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (a.length - 1);
    }
    public static double stddev(int[] a){
        return Math.sqrt(var(a));
    }
    public static double stddev(double[] a){
        return Math.sqrt(var(a));
    }
}
