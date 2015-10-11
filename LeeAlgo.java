import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by paulp on 10/11/2015.
 */
public class LeeAlgo { // bfs for grid
    private int[][] paths;
    private int width;
    private int height;

    public LeeAlgo(char[][] grid, int height, int width, Pair s) {
        if (grid[s.x][s.y] == '-') return;
        this.width = width;
        this.height = height;
        paths = new int[height][width];
        paths[s.x][s.y] = 1;

        Deque<Pair> queue = new ArrayDeque<>();
        queue.add(s);
        while (!queue.isEmpty()) {
            Pair v = queue.poll();
            for (int i = v.x - 1; i <= v.x + 1; ++i) {
                for (int j = v.y - 1; j <= v.y + 1; ++j) {
                    if (i < 0 || j < 0 || i >= height || j >= width || (i == v.x && j == v.y) || grid[i][j] == '-' || paths[i][j] > 0) {
                        continue;
                    }
                    paths[i][j] = paths[v.x][v.y] + 1;
                    queue.add(new Pair(i, j));
                }
            }
        }
    }

    public Iterable<Pair> pathTo(Pair p) {
        assert hasPathTo(p);

        int x = p.x;
        int y = p.y;

        int state = paths[x][y];
        Deque<Pair> stack = new ArrayDeque<>();
        stack.push(new Pair(x, y));
        while (state != 1) {
            brk : for (int i = x - 1; i <= x + 1; ++i) {
                for (int j = y - 1; j <= y + 1; ++j) {
                    if (i < 0 || j < 0 || i >= height || j >= width || (i == x && j == y) || (paths[i][j] == 0) || paths[i][j] >= state) {
                        continue;
                    }
                    x = i;
                    y = j;
                    state = paths[i][j];
                    stack.push(new Pair(x, y));
                    break brk;
                }
            }
        }
        return stack;
    }

    public boolean hasPathTo(Pair p) {
        return paths != null && paths[p.x][p.y] != 0;
    }

}
