/**
 * Created by Paul on 5/1/2015.
 */
public class StopWatch {
    private long start = System.currentTimeMillis();
    public long getElapsed(){
        long now = System.currentTimeMillis();
        return (now - start) / 1000;
    }
    public long getElapsedMillis(){
        long now = System.currentTimeMillis();
        return now - start;
    }
}
