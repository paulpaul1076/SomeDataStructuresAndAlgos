import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.PriorityQueue;

/**
 * Created by paulp on 10/15/2015.
 */
public class Dijkstra {
    private double[] distTo;
    private int[] parent;
    private boolean[] marked;
    private PriorityQueue<Integer> pq;

    private class VertexComp implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (distTo[o1] < distTo[o2]) return -1;
            return 1;
        }
    }

    public Dijkstra(WeightedDigraph G, int source) {
        distTo = new double[G.V()];
        parent = new int[G.V()];
        marked = new boolean[G.V()];
        for (int i = 0; i < G.V(); ++i) {
            distTo[i] = Double.POSITIVE_INFINITY;
            parent[i] = -1;
        }

        parent[source] = source;
        distTo[source] = 0.0;

        pq = new PriorityQueue<>(new VertexComp());

        pq.add(source);

        while (!pq.isEmpty()) {
            int top = pq.poll();
            if (!marked[top])
                relax(G, top);
        }
    }

    private void relax(WeightedDigraph G, int v) {
        marked[v] = true;
        for (DirectedWeightedEdge e : G.adj(v)) {
            if (distTo[e.to()] > distTo[v] + e.weight()) {
                distTo[e.to()] = distTo[v] + e.weight();
                parent[e.to()] = v;
                if (!pq.contains(e.to())) pq.add(e.to());
                else{
                    pq.remove(e.to());
                    pq.add(e.to());
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return parent[v] != -1;
    }

    public Iterable<Integer> pathTo(int v) {
        Deque<Integer> stack = new ArrayDeque<>();
        int ptr = v;
        for (; ptr != parent[ptr]; ptr = parent[ptr]) {
            stack.push(ptr);
        }
        stack.push(ptr);
        return stack;
    }

    public double distanceTo(int v){
        return distTo[v];
    }
}
