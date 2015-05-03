import java.util.Scanner;

/**
 * Created by Paul on 5/3/2015.
 */
public class WQUFWPC {
    private int count;
    private int sz[];
    private int id[];

    public WQUFWPC(int N){
        count = N;
        sz = new int[N];
        for(int i = 0; i < N; ++i) sz[i] = 1;
        id = new int[N];
        for(int i = 0; i < N; ++i) id[i] = i;
    }

    public int find(int p){ // 2lgN
        int root = p;
        while(root != id[root]) root = id[root]; // lgN
        while(p != root) { // lgN
            id[p] = root;
            p = id[p];
        }
        return root;
    }
    public void union(int p, int q){
        int i = find(p);
        int j = find(q);

        if(i == j) return;

        if(sz[i] < sz[j]) {
            id[i] = j;
            sz[j] += sz[i];
        }
        else{
            id[j] = i;
            sz[i] += sz[j];
        }
        count--;
    }
    public int count(){
        return count;
    }
    boolean connected(int p, int q){
        return find(p) == find(q);
    }
    public static void main(){
        try(Scanner sc = new Scanner(System.in)){
            int N = sc.nextInt();
            WQUFWPC uf = new WQUFWPC(N);
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
