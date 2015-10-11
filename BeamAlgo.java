import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by paulp on 10/11/2015.
 */
public class BeamAlgo { // dfs for grid
    private int[][] paths;
    private int height;
    private int width;

    public BeamAlgo(char[][] grid, int height, int width, Pair s) {
        if (grid[s.x][s.y] == '-') return;
        this.height = height;
        this.width = width;
        paths = new int[height][width];
        go(grid, s, 1);

        System.out.println("GRID: ");
        for(int i = 0; i < height; ++i){
            for(int j = 0; j < width; ++j){
                System.out.print(paths[i][j]);
            }
            System.out.println();
        }

        System.out.println("END");
    }

    private void go(char[][] grid, Pair v, int count) {
        paths[v.x][v.y] = count;
        for (int i = v.x - 1; i <= v.x + 1; ++i) {
            for (int j = v.y - 1; j <= v.y + 1; ++j) {
                if (i < 0 || j < 0 || i >= height || j >= width || (v.x == i && v.y == j) || grid[i][j] == '-' || paths[i][j] > 0){
                    //System.out.println("-");
                    continue;
                }
                go(grid, new Pair(i, j), count + 1);
            }
        }
    }

    public Iterable<Pair> pathTo(Pair p) {
        Deque<Pair> stack = new ArrayDeque<>();
        stack.push(p);
        int x = p.x;
        int y = p.y;
        int state = paths[x][y];
        while (state != 1) {
            System.out.println("State : " + state);
            brk : for (int i = x - 1; i <= x + 1; ++i) {
                for (int j = y - 1; j <= y + 1; ++j) {
                    if (i < 0 || j < 0 || i >= height || j >= width || (i == x && j == y) || paths[i][j] != state - 1)
                        continue;
                    state--;
                    x = i;
                    y = j;
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
