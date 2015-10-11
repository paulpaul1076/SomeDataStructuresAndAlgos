/**
 * Created by paulp on 10/11/2015.
 */
public class Grid {
    private double[][] weights;
    private int width, height;

    public Grid(double[][] weights, int width, int height) {
        this.weights = weights;
        this.width = width;
        this.height = height;
    }

    public double getWeight(int i, int j) {
        return weights[i][j];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
