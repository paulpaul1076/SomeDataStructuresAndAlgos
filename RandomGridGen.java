import java.util.Iterator;

/**
 * Created by Paul on 5/3/2015.
 */
public class RandomGridGen {
    private static RandomBag<Connection_> generate(int N){
        RandomBag<Connection_> connections = new RandomBag<>();
        for(int i = 0; i < N; ++i){
            int p = Algorithms.getRandomInt(0, N);
            int q = Algorithms.getRandomInt(0, N);
            connections.add(new Connection_(p, q));
        }
        return connections;
    }
    public static void main(int N){
        RandomBag<Connection_> connections = generate(N);
        for(Iterator<Connection_> it = connections.iterator(); it.hasNext();){
            System.out.println(it.next());
        }
    }
    private static class Connection_{
        int p;
        int q;

        public Connection_(int p, int q){
            this.p = p; this.q = q;
        }

        @Override
        public String toString() {
            return p + " " + q;
        }
    }
    private static int count(int N){
        int edges = 0;
        UF uf = new UF(N);
        RandomBag<Connection_> rb = generate(N);
        Iterator<Connection_> it = rb.iterator();
        while(uf.count() > 1){
            if(!it.hasNext()){
                rb = generate(N);
                it = rb.iterator();
            }
            Connection_ c = it.next();
            uf.QFunion(c.p, c.q);
            edges++;
        }
        System.out.println(uf.count() + " connected components.");
        return edges;
    }
    public static void doublingTest(int N, int T){
        assert T <= 10;
        double prev = 1.0;
        int[] edges = new int[T];
        for(int i = N, j = 0; j < T; i *= 2, ++j){
            System.out.println("N = " + i);
            long start = System.currentTimeMillis();
            edges[j] = count(i);
            long now = System.currentTimeMillis();
            double diff = (now - start);
            System.out.println("ratio = " + Math.round(diff / prev));
            prev = diff;
        }
    }
}
