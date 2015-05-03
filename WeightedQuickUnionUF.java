/**
 * Created by Paul on 5/2/2015.
 */
import java.util.Scanner;
public class WeightedQuickUnionUF {
    private int id[];
    private int sz[];
    private int count;

    public WeightedQuickUnionUF(int N){
        count = N;
        id = new int[N];
        for(int i = 0; i < N; ++i) id[i] = i;
        sz = new int[N];
        for(int i = 0; i < N; ++i) sz[i] = 1;
    }
    public void union(int p, int q){
        int i = find(p);
        int j = find(q);
        if(i == j) return;

        if(sz[i] < sz[j]) { id[i] = j; sz[j] += sz[i]; }
        else { id[j] = i; sz[i] += sz[j]; }
        count--;
    }
    public int find(int p){
        while(p != id[p]) p = id[p];
        return p;
    }
    //public int newSite(){

   //}
    public int count(){
        return count;
    }
    public boolean connected(int p, int q){
        return find(p) == find(q);
    }
    public static void main(){
        try(Scanner sc = new Scanner(System.in)){
            int N = sc.nextInt();
            WeightedQuickUnionUF uf = new WeightedQuickUnionUF(N);
            while(sc.hasNextInt()){
                int p = sc.nextInt();
                int q = sc.nextInt();
                if(uf.connected(p, q)) continue;
                uf.union(p, q);
                System.out.println(p + " " + q);
            }
            System.out.println(uf.count() + " components.");
            for(int i = 0; i < N; ++i){
                System.out.print(i + " ");
            }
            System.out.println();
            for(int i = 0; i < N; ++i){
                System.out.print(uf.id[i] + " ");
            }
            System.out.println();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
