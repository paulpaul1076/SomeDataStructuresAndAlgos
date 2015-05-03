import java.util.Iterator;

/**
 * Created by Paul on 5/3/2015.
 */
public class RandomGridGen {
    private static RandomBag<Connection_> generate(int N){
        RandomBag<Connection_> connections = new RandomBag<>();
        for(int i = 0; i < N; ++i){
            int p = Algorithms.getRandomInt(0, N - 1);
            int q = Algorithms.getRandomInt(0, N - 1);
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
}
