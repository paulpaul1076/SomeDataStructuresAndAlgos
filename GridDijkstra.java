import java.util.*;

/**
 * Created by paulp on 10/11/2015.
 */
public class GridDijkstra { // must override hashcode for Pair, else distTo(X)/distTo.get(X) will fail.

    private PriorityQueue<WeightedPair> pq = new PriorityQueue<>();
    private HashMap<Pair, Double> distTo = new HashMap<>();
    private HashMap<Pair, Pair> parent = new HashMap<>();
    private HashSet<Pair> marked = new HashSet<>();

    private final int NO = -1;

    private class WeightedPair implements Comparable<WeightedPair> {
        public int x, y;
        public Pair p;

        public WeightedPair(Pair p) {
            x = p.x;
            y = p.y;
            this.p = p;
        }

        public int compareTo(WeightedPair other) {
            if (distTo(p) < distTo(other.p)) return -1;
            return 1;
        }
    }

    public GridDijkstra(Grid grid, Pair s) {
        for (int i = 0; i < grid.getHeight(); ++i) {
            for (int j = 0; j < grid.getWidth(); ++j) {
                distTo.put(new Pair(i, j), Double.POSITIVE_INFINITY);
            }
        }

        if ((int) grid.getWeight(s.x, s.y) == NO) return;

        distTo.put(s, grid.getWeight(s.x, s.y));
        parent.put(s, s);

        relax(grid, s);

        while (!pq.isEmpty()) {
            WeightedPair p = pq.poll();
            if (marked.contains(p)) continue;
            relax(grid, p.p);
        }
    }

    private void relax(Grid grid, Pair p) {
        marked.add(p);
        // get neighbors
        List<Pair> neighbors = new ArrayList<>();
        for (int i = p.x - 1; i <= p.x + 1; ++i) {
            for (int j = p.y - 1; j <= p.y + 1; ++j) {
                if (i < 0 || j < 0 || j >= grid.getWidth() || i >= grid.getWidth() || (i == p.x && j == p.y) || (int) grid.getWeight(i, j) == NO)
                    continue;
                neighbors.add(new Pair(i, j));
            }
        }

        // check neighbors

        for (Pair X : neighbors) {
            if (distTo(X) > distTo(p) + grid.getWeight(X.x, X.y)) {
                parent.put(X, p);
                distTo.put(X, distTo(p) + grid.getWeight(X.x, X.y));
                pq.add(new WeightedPair(X));
            }
        }
    }

    public Iterable<Pair> pathTo(Pair p) {
        assert hasPathTo(p);
        Deque<Pair> path = new ArrayDeque<>();
        Pair ptr = p;
        for (; ptr != parent.get(ptr); ptr = parent.get(ptr)) {
            path.push(ptr);
        }
        path.push(ptr);
        return path;
    }

    public boolean hasPathTo(Pair p) {
        return distTo(p) < Double.POSITIVE_INFINITY;
    }

    public double distTo(Pair p) {
        if (distTo.containsKey(p)) return distTo.get(p);
        return Double.POSITIVE_INFINITY;
    }
}
