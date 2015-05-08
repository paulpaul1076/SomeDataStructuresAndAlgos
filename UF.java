/**
 * Created by Paul on 5/2/2015.
 */
import java.util.Scanner;

public class UF {
    //fields
    private int[] id;
    private int count;
    /*initialize N sites with integer names (0 to N-1)*/
    public UF(int N){
        count = N;
        id = new int[N];
        for(int i = 0; i < N; ++i){
            id[i] = i;
        }
    }
    /*add connection between p and q*/
    public void QFunion(int p, int q){
        int pID = QFfind(p);
        int qID = QFfind(q);

        if(qID == pID) return;

        for(int i = 0; i < id.length; ++i){
            if(id[i] == pID) id[i] = qID;
        }
        count--;
    }
    public void QUunion(int p, int q){
        int i = QUfind(p);
        int j = QUfind(q);
        if(i == j) return;

        id[i] = j;

        count--;
    }
    /*component identifier for p (0 to N-1)*/
    public int QFfind(int p){
        return id[p];
    }
    public int QUfind(int p){
        while(p != id[p]) p = id[p];
        return p;
    }
    /*return true if p and q are in the same component*/
    public boolean QFconncted(int p, int q){
        return QFfind(p) == QFfind(q);
    }
    public boolean QUconnected(int p, int q) {
        return QUfind(p) == QUfind(q);
    }
    /*number of components*/
    public int count(){
        return count;
    }
    //the main client
    public static void main(){
        try(Scanner sc = new Scanner(System.in)) {
            int N = sc.nextInt();
            UF uf = new UF(N);
            while (sc.hasNextInt()) {
                int p = sc.nextInt();
                int q = sc.nextInt();
                if(uf.QFconncted(p, q)) continue;
                uf.QFunion(p, q);
                System.out.println(p + " " + q);
            }
            System.out.println(uf.count() + " components.");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
