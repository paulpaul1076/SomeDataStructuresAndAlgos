import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.PriorityQueue;

public class BidirectionalDijkstra {
    private double[] distToF, distToR;
    private boolean[] markedF, markedR;
    private boolean found;
    private int[] parentF, parentR;
    private PriorityQueue<Integer> pqF, pqR;
    private double lastSP = Double.POSITIVE_INFINITY;
    private int end;
    private boolean finished;

    private class VertexComparerF implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (distToF[o1] < distToF[o2]) return -1;
            return 1;
        }
    }

    private class VertexComparerR implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (distToR[o1] < distToR[o2]) return -1;
            return 1;
        }
    }

    public BidirectionalDijkstra(WeightedDigraph Gf, int source, int sink) {

        if (source == sink) {
            end = sink;
            found = true;
        }

        distToF = new double[Gf.V()];
        distToR = new double[Gf.V()];
        markedF = new boolean[Gf.V()];
        markedR = new boolean[Gf.V()];
        parentF = new int[Gf.V()];
        parentR = new int[Gf.V()];

        for (int i = 0; i < Gf.V(); ++i) {
            distToF[i] = Double.POSITIVE_INFINITY;
            distToR[i] = Double.POSITIVE_INFINITY;
        }

        WeightedDigraph Gr = Gf.reverse();
        pqF = new PriorityQueue<>(new VertexComparerF());
        pqR = new PriorityQueue<>(new VertexComparerR());

        distToF[source] = 0.0;
        distToR[sink] = 0.0;

        parentF[source] = source;
        parentR[sink] = sink;

        pqF.add(source);
        pqR.add(sink);


        while (!pqF.isEmpty() && !pqR.isEmpty() && (distToF[pqF.peek()] + distToR[pqR.peek()] < lastSP)) {
            int topF = pqF.poll();
            if (!markedF[topF])
                relaxF(Gf, topF);
            int topR = pqR.poll();
            if (!markedR[topR])
                relaxR(Gr, topR);
        }
    }

    private void relaxF(WeightedDigraph G, int v) {
        markedF[v] = true;
        for (DirectedWeightedEdge e : G.adj(v)) {
            if (distToF[e.to()] > distToF[v] + e.weight()) {
                distToF[e.to()] = distToF[v] + e.weight();
                parentF[e.to()] = v;
                if (!pqF.contains(e.to())) {
                    pqF.add(e.to());
                } else {
                    pqF.remove(e.to());
                    pqF.add(e.to());
                }
                if (markedR[e.to()]) {
                    if (lastSP > distToR[e.to()] + e.weight() + distToF[v]) {
                        lastSP = distToR[e.to()] + e.weight() + distToF[v];
                        end = v;
                        found = true;
                    }
                }
            }
        }
    }

    private void relaxR(WeightedDigraph G, int v) {
        markedR[v] = true;
        for (DirectedWeightedEdge e : G.adj(v)) {
            if (distToR[e.to()] > distToR[v] + e.weight()) {
                distToR[e.to()] = distToR[v] + e.weight();
                parentR[e.to()] = v;
                if (!pqR.contains(e.to())) {
                    pqR.add(e.to());
                } else {
                    pqR.remove(e.to());
                    pqR.add(e.to());
                }
                if (markedF[e.to()]) {
                    if (lastSP > distToF[e.to()] + e.weight() + distToR[v]) {
                        lastSP = distToF[e.to()] + e.weight() + distToR[v];
                        end = v;
                        found = true;
                    }
                }
            }
        }
    }

    public boolean hasPath() {
        return found;
    }

    public Iterable<Integer> path() {
        assert hasPath();
        Deque<Integer> deque = new ArrayDeque<>();
        //PUSH forward
        int ptr = end;
        for (; ptr != parentF[ptr]; ptr = parentF[ptr]) {
            deque.push(ptr);
        }
        deque.push(ptr);
        //ADD reverse
        ptr = parentR[end];
        for (; ptr != parentR[ptr]; ptr = parentR[ptr]) {
            deque.add(ptr);
        }
        if (deque.getLast() != ptr)
            deque.add(ptr);
        return deque;
    }

    public double distance() {
        return distToF[end] + distToR[end];
    }
}