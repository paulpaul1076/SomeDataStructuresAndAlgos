/**
 * Created by Paul on 5/3/2015.
 */
import java.util.Random;
import java.util.Scanner;
public class ErdosRenyi {
    public static int count(int N) {
        int edges = 0;
        UF uf = new UF(N);
        //Random rnd = new Random(System.currentTimeMillis());
        while(uf.count() > 1){
            int p = Algorithms.getRandomInt(0, N);
            int q = Algorithms.getRandomInt(0, N);
            uf.QUunion(p, q);
            edges++;
            System.out.println("count = " + uf.count() + ", edge # = " + edges);
        }
        return edges;
    }
    public static void main(){
        try(Scanner sc = new Scanner(System.in)){
            int N = sc.nextInt();
            int T = sc.nextInt();
            int[] edges = new int[T];
            System.out.println("Calculating...");
            for(int i = 0; i < T; ++i){
                edges[i] = count(N);
            }

            System.out.println("expected val = " + 0.5 * N * Math.log(N));
            System.out.println("mean         = " + Statistics.mean(edges));
            System.out.println("stddev       = " + Statistics.stddev(edges));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private static int countQU(int N){
        int edges = 0;
        UF uf = new UF(N);
        while(uf.count() > 1){
            int p = Algorithms.getRandomInt(0, N);
            int q = Algorithms.getRandomInt(0, N);
            uf.QUunion(p, q);
            edges++;
        }
        return edges;
    }
    private static int countQF(int N){
        int edges = 0;
        UF uf = new UF(N);
        while(uf.count() > 1){
            int p = Algorithms.getRandomInt(0, N);
            int q = Algorithms.getRandomInt(0, N);
            uf.QFunion(p, q);
            edges++;
        }
        return edges;
    }
    private static int countWQU(int N){
        int edges = 0;
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(N);
        while(uf.count() > 1){
            int p = Algorithms.getRandomInt(0, N);
            int q = Algorithms.getRandomInt(0, N);
            uf.union(p, q);
            edges++;
        }
        return edges;
    }
    private static int countWQUWPC(int N){
        int edges = 0;
        WQUFWPC uf = new WQUFWPC(N);
        while(uf.count() > 1){
            int p = Algorithms.getRandomInt(0, N);
            int q = Algorithms.getRandomInt(0, N);
            uf.union(p, q);
            edges++;
        }
        return edges;
    }
    public static void doublingTest(int N, int T){
        int[] QUedges = new int[T];
        int[] QFedges = new int[T];
        int[] WQUedges = new int[T];
        int[] WQUWPCedges = new int[T];

        long QUprevTime = 1;
        long QFprevTime = 1;
        long WQUprevTime = 1;
        long WQUWPCprevTime = 1;

        long elapsed = 0;

        for(int i = N, j = 0; j < T; i *= 2, ++j){
            System.out.println("N = " + i);

            //quick union : should be quadratic
            //StopWatch quSW = new StopWatch();
            //QUedges[j] = countQU(i);
            //elapsed = quSW.getElapsedMillis();
            //System.out.println("Ratio for quick union = " + elapsed / QUprevTime + " , and " + QUedges[j] + " processed");
            //QUprevTime = elapsed == 0 ? 1 : elapsed;

            //quick find : should be quadratic
            //StopWatch qfSW = new StopWatch();
            //QFedges[j] = countQF(i);
            //elapsed = qfSW.getElapsedMillis();
            //System.out.println("Ratio for quick find = " + elapsed / QFprevTime + " , and " + QFedges[j] + " processed");
            //QFprevTime = elapsed == 0 ? 1 : elapsed;

            //weighted quick union : should be almost linear
            long start = System.currentTimeMillis();
            WQUedges[j] = countWQU(i);
            elapsed = System.currentTimeMillis();
            System.out.println("Ratio for weighted quick union = " + Math.ceil(((double)(elapsed - start)) / WQUprevTime) + " , and " + WQUedges[j] + " processed");
            System.out.println("Previous time = " + WQUprevTime + ", current time = " + (elapsed - start));
            WQUprevTime = elapsed - start == 0 ? 1 : elapsed - start;

            //weighted quick union with path compression : ???
            start = System.currentTimeMillis();
            WQUWPCedges[j] = countWQUWPC(i);
            elapsed = System.currentTimeMillis();
            System.out.println("Ratio for weighted quick union wpc = " + Math.ceil(((double)(elapsed - start)) / WQUWPCprevTime) + " , and " + WQUWPCedges[j] + " processed");
            System.out.println("Previous time = " + WQUWPCprevTime + ", current time = " + (elapsed - start));
            WQUWPCprevTime = elapsed - start == 0 ? 1 : elapsed - start;
        }
    }
}
