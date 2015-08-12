package com.company;

public class Main{
    public static void main(String[] args){
        double[][] mat1 = {{3,7,5},{0,5,4},{3,2,5}};
        double[][] mat2 = {{1,3,3},{0,0,0},{1,2,5}};
        SparseMatrix m1 = new SparseMatrix(mat1, 3,3);
        SparseMatrix m2 = new SparseMatrix(mat2, 3,3);

        SparseMatrix m3 = m1.add(m2);

        System.out.println(m3);

    }
}

class SparseMatrix{
    private LinearProbingST<Integer, Double>[] matrix;
    private int height, width;
    public SparseMatrix(double[][] m, int h, int w){
        matrix = (LinearProbingST<Integer, Double>[])new LinearProbingST[h];
        for(int i = 0; i < h; ++i){
            matrix[i] = new LinearProbingST<Integer, Double>();
        }
        for(int i = 0; i < h; ++i){
            for(int j = 0; j < w; ++j){
                if(m[i][j] != 0.0){
                    matrix[i].put(j, m[i][j]);
                }
            }
        }
        height = h;
        width = w;
    }
    public SparseMatrix(int h, int w){
        matrix = (LinearProbingST<Integer,Double>[])new LinearProbingST[h];
        for(int i = 0; i < h; ++i){
            matrix[i] = new LinearProbingST<Integer, Double>();
        }
        height = h;
        width = w;
    }
    public int getHeight(){
        return height;
    }
    public int getWidth(){
        return width;
    }
    public double get(int row, int col){
        if(matrix[row].contains(col)) return matrix[row].get(col);
        return 0.0;
    }
    public void put(int row, int col, double val){
        matrix[row].put(col, val);
    }
    public SparseMatrix multiply(SparseMatrix other){
        assert this.width == other.height;

        SparseMatrix newMatrix = new SparseMatrix(this.height, other.width);

        for(int i = 0; i < this.height; ++i){
            for(int j = 0; j < other.width; ++j){
                for(int k = 0; k < other.height; ++k){
                    if((int)(1000 * this.get(i, k)) != 0 && (int)(1000 * other.get(k , j)) != 0) {
                        double sum = newMatrix.get(i, j) + this.get(i, k) * other.get(k, j);
                        if ((int) (sum * 1000) != 0)
                            newMatrix.put(i, j, sum);
                    }
                }
            }
        }

        return newMatrix;
    }

    public SparseMatrix add(SparseMatrix other){
        assert this.height == other.height && this.width == other.width;
        SparseMatrix newMatrix = new SparseMatrix(height, width);

        for(int i = 0; i < height; ++i){
            for(int j = 0; j < width; ++j){
                double sum = this.get(i, j) + other.get(i, j);
                if((int)(sum * 1000) != 0)
                    newMatrix.put(i, j, sum);
            }
        }
        return newMatrix;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < height; ++i){
            for(int j = 0; j < width; ++j){
                sb.append(String.format("%f, ", get(i, j)));
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}

class Matrix{
    private double[][] matrix;
    private int height, width;
    public Matrix(int h, int w){
        matrix = new double[h][w];
        height = h;
        width = w;
    }
    public Matrix(double[][] matrix, int h, int w){
        this.matrix = matrix;
        height = h;
        width = w;
    }
    public double get(int row, int col){
        return matrix[row][col];
    }
    public void put(int row, int col, double val){
        matrix[row][col] = val;
    }
    public int getHeight(){
        return height;
    }
    public int getWidth(){
        return width;
    }
    public Matrix multiply(Matrix other){
        assert this.width == other.height;

        double[][] matrix2 = other.matrix;

        int newHeight = this.height;
        int newWidth = other.width;
        double[][] newMatrix = new double[newHeight][newWidth];

        //multiply
        for(int i = 0; i < this.height; ++i){
            for(int j = 0; j < other.width; ++j){
                for(int k = 0; k < other.height; ++k){
                    newMatrix[i][j] += matrix[i][k] * matrix2[k][j];
                }
            }
        }

        return new Matrix(newMatrix, newHeight, newWidth);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < height; ++i){
            for(int j = 0; j < width; ++j){
                sb.append(String.format("%f, ", matrix[i][j]));
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}

interface ST<Key, Value>{
    void put(Key key, Value val);
    void remove(Key key);
    Value get(Key key);
    int size();
    boolean isEmpty();
    boolean contains(Key key);
}

class LinearProbingST<Key, Value> implements ST<Key, Value>{
    private int N;
    private int M;
    private Key[] keys;
    private Value[] values;

    public void printNM(){
        System.out.println("N = " + N + ", M = " + M);
    }

    public LinearProbingST(){
        keys = (Key[]) new Object[13];
        values = (Value[]) new Object[13];
        M = 13;
    }
    public LinearProbingST(int cap){
        keys = (Key[]) new Object[cap];
        values = (Value[]) new Object[cap];
        M = cap;
    }
    @Override
    public void put(Key key, Value val) {
        if(N >= M / 2) resize(M * 2);
        int i;
        for(i = hash(key); keys[i] != null; i = (i + 1) % M){
            if(key.equals(keys[i])){
                values[i] = val; return;
            }
        }
        keys[i] = key;
        values[i] = val;
        N++;
    }

    @Override
    public void remove(Key key) {
        if(!contains(key)) return;
        int i = hash(key);
        while(!keys[i].equals(key)) i = (i + 1) % M;
        keys[i] = null; values[i] = null;
        i = (i + 1) % M;
        while(keys[i] != null){
            Key tempKey = keys[i];
            Value tempVal = values[i];
            keys[i] = null;
            values[i] = null;
            i = (i + 1) % M;
            N--;
            put(tempKey, tempVal);
            //System.out.println("Index : " + i);
        }
        N--;
        if(N > 0 && N <= M / 8) resize(M / 2);
    }

    @Override
    public Value get(Key key) {
        for(int i = hash(key); keys[i] != null; i = (i + 1) % M){
            if(key.equals(keys[i])) return values[i];
        }
        return null;
    }

    @Override
    public int size() {
        return N;
    }

    @Override
    public boolean isEmpty() {
        return N == 0;
    }

    @Override
    public boolean contains(Key key) {
        for(int i = hash(key); keys[i] != null; i = (i + 1) % M){
            if(key.equals(keys[i])) return true;
        }
        return false;
    }

    private void resize(int size){
        LinearProbingST<Key, Value> temp = new LinearProbingST<>(size);
        for(int i = 0; i < M; ++i){
            if(keys[i] != null) temp.put(keys[i], values[i]);
        }
        keys = temp.keys;
        values = temp.values;
        M = temp.M;
    }

    private int hash(Key key){
        return (key.hashCode() & 0x7fffffff) % M;
    }


}