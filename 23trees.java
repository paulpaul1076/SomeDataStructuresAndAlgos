package com.company;

import java.util.*;

public class Main {
    static boolean checkOrder(List<Integer> l){
        for(int i = 1; i < l.size(); ++i){
            if(l.get(i) < l.get(i - 1)) return false;
        }
        return true;
    }
    public static void main(String[] args) {
        RedBlackTree<Integer, Integer> rb = new RedBlackTree<>();
        RandomInt rnd = new RandomInt();
        ArrayList<Integer> ints = new ArrayList<>();
        for(int i = 0; i <= 1000; ++i){
            ints.add(rnd.getNext(0, 100));
        }
        for(int i = 0; i <= 1000; ++i){
            rb.put(ints.get(i), 0);
        }
        boolean ok = true;
        while(!rb.isEmpty()){
            rb.delete(rnd.getNext(0, 100));
            ok &= rb.isBalanced() & rb.isRedBlack();
        }
        System.out.println("Is ok? : " + ok);
        System.out.println("Is empty? : " + rb.isEmpty());
    }
}

class RandomString{
    private final int MIN = Character.MIN_VALUE;
    private final int MAX = Character.MAX_VALUE;
    private RandomInt rnd = new RandomInt();
    public String getNext(int size){
        StringBuilder sb = new StringBuilder(size);
        for(int i = 0; i < size; ++i){
            sb.append(rnd.getNext(MIN, MAX));
        }
        return sb.toString();
    }
}

// returns an element from a (inclusive) to b (inclusive as well)
class RandomInt{
    private Random rnd = new Random(System.currentTimeMillis());
    public int getNext(int from, int to){
        return (int)(rnd.nextDouble() * (to - from + 1)) + from;
    }
}

class RedBlackTree<Key extends Comparable<Key>, Value>{
    private final boolean RED = true;
    private final boolean BLACK = false;
    private class Node{
        Key key;
        Value val;
        Node left, right;
        boolean color;
        int size;
        public Node(Key key, Value val, int size, boolean color){
            this.key = key;
            this.val = val;
            this.size = size;
            this.color = color;
        }
    }
    private Node root;
    public void put(Key key, Value val){
        root = put(root, key, val);
        root.color = BLACK;
    }
    private Node put(Node h, Key key, Value val){
        if(h == null) return new Node(key, val, 1, RED);

        int cmp = key.compareTo(h.key);
        if(cmp < 0) h.left = put(h.left, key, val);
        else if(cmp > 0) h.right = put(h.right, key, val);
        else h.val = val;

        //if( isRED(node.left) && isRED(node.left.left)) node = rotateRight(node);
        //if(!isRED(node.left) && isRED(node.right))     node = rotateLeft(node);
        //if( isRED(node.left) && isRED(node.right))     flipColors(node);

        if(isRED(h.right) && !isRED(h.left)) h = rotateLeft(h);
        if(isRED(h.left) && isRED(h.left.left)) h = rotateRight(h);
        if(isRED(h.left) && isRED(h.right)) flipColors(h);

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }
    private boolean isRED(Node node){
        if(node == null) return false;
        return node.color == RED;
    }
    private int size(Node node){
        if(node == null) return 0;
        return node.size;
    }
    private void flipColors(Node node){
        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;
    }
    private Node rotateLeft(Node node){
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = node.color;
        node.color = RED;
        x.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return x;
    }
    private Node rotateRight(Node node){
        Node x = node.left;
        node.left = x.right;
        x.right = node;
        x.color = node.color;
        node.color = RED;
        x.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;
        return x;
    }
    public void deleteMin(){
        root = deleteMin(root);
        if(isRED(root)) root.color = BLACK;
    }
    private boolean is4Node(Node h){
        return isRED(h.left) && isRED(h.right);
    }
    private boolean case1Min(Node h){
        if(     !isRED(h.left) &&
                h.right != null &&
                isRED(h.right.left) &&
                h.left != null &&
                !isRED(h.left.left)
                )
            return true;
        return false;
    }
    private boolean case2Min(Node h) {
        if (!isRED(h.left) &&
                h.left != null &&
                h.right != null &&
                !isRED(h.left.left)
                )
            return true;
        return false;
    }
    private boolean case1Max(Node h){
        if(     !isRED(h.right) &&
                h.left != null &&
                (isRED(h.left.left)) &&
                h.right != null &&
                !isRED(h.right.left))
            return true;
        return false;
    }
    private boolean case2Max(Node h){
        if(     !isRED(h.right) &&
                h.left != null &&
                h.right != null &&
                !isRED(h.right.left)
                )
            return true;
        return false;
    }
    private Node handleCase1Max(Node h){
        h = rotateRight(h);
        h.left.color = BLACK;
        h.right.color = BLACK;
        h.right.right.color = RED;
        return h;
    }
    private Node handleCase2Max(Node h){
        h.color = !(h.left.color = h.right.color = RED);
        return h;
    }
    private Node handleCase1Min(Node h){
        h.right = rotateRight(h.right);
        h = rotateLeft(h);
        h.right.color = BLACK; //TODO: remove if anything
        h.left.left.color = RED; //TODO:  remove if anything
        h.left.color = BLACK; // TODO: remove if anything
        return h;
    }
    private Node handleCase2Min(Node h){
        h.color = !(h.left.color = h.right.color = RED);
        return h;
    }
    private Node deleteMin(Node h){
        if(h == root){ // at root
            if(case1Min(h)){
                h = handleCase1Min(h);
            } else if(case2Min(h)){
                h = handleCase2Min(h);
            }
        } else {
            if(case1Min(h)){
                h = handleCase1Min(h);
            } else if(case2Min(h)){
                h = handleCase2Min(h);
            }
        }
        if(h.left == null) return null;
        h.left = deleteMin(h.left);
        return balance(h);
    }
    private Node balance(Node h){
        if(isRED(h.left) && isRED(h.left.left)){
            //System.out.println("Balance case 1, element : " + h.key);
            h = rotateRight(h);
        }
        if(!isRED(h.left) && isRED(h.right)){
            //System.out.println("Balance case 2, element : " + h.key);
            h = rotateLeft(h);
        }
        if(isRED(h.left) && isRED(h.right)){
            //System.out.println("Balance case 3, element : " + h.key);
            flipColors(h);
        }
        return h;
    }
    public void deleteMax(){
        root = deleteMax(root);
        if(isRED(root)) root.color = BLACK;
    }
    private Node deleteMax(Node h){
        if(isRED(h.left)){ // GOOD MOVE
            h = rotateRight(h);
        }
        if(case1Max(h)){
            //System.out.println("Case 1 ( " + h.key + " )");
            h = handleCase1Max(h);
        } else if(case2Max(h)) {
            //System.out.println("Case 2 ( " + h.key + " )");
            h = handleCase2Max(h);
        }
        if(h.right == null) return null;
        h.right = deleteMax(h.right);
        return balance(h);
    }
    public void delete(Key key){
        root = delete(root, key);
        if(isRED(root)) root.color = BLACK;
    }
    private Node delete(Node h, Key key){
        if(h == null) return null;
        if(key.compareTo(h.key) < 0){
            if(case1Min(h)){
                h = handleCase1Min(h);
            } else if(case2Min(h)){
                h = handleCase2Min(h);
            }
            h.left = delete(h.left, key);
        } else {
            if(isRED(h.left)){
                h = rotateRight(h);
            }
            if(case1Max(h)){
                h = handleCase1Max(h);
            } else if(case2Max(h)){
                h = handleCase2Max(h);
            }
            if(key.compareTo(h.key) == 0 && h.right == null) return null;
            if(key.compareTo(h.key) == 0) {
                Node temp = min(h.right);
                h.key = temp.key; h.val = temp.val;
                h.right = deleteMin(h.right);
            }
            if(key.compareTo(h.key) > 0){
                h.right = delete(h.right, key);
            }
        }
        return balance(h);
    }
    public boolean isEmpty(){
        return size(root) == 0;
    }
    public boolean isRedBlack(){
        return isRedBlack(root);
    }
    private Node min(Node h){
        if(h == null) return null;
        while(h.left != null){
            h = h.left;
        }
        return h;
    }
    private boolean isRedBlack(Node h){
        if(h == null) return true;
        if(isRED(h.right)) return false;
        if(h != root && isRED(h) && isRED(h.left)) return false;
        return isRedBlack(h.left) && isRedBlack(h.right);
    }
    public boolean isBalanced(){
        Node x = root;
        int black = 0;
        while(x != null){
            if(!isRED(x)) black++;
            x = x.left;
        }
        return isBalanced(root, black);
    }
    private boolean isBalanced(Node h, int black){
        if(h == null) return black == 0;
        if(!isRED(h)) black--;
        return isBalanced(h.left, black) && isBalanced(h.right, black);
    }
    public int size(){
        return size(root);
    }
    public Key min(){
        if(isEmpty()) throw new NoSuchElementException("Empty tree");
        Node x = root;
        while(x.left != null){
            x = x.left;
        }
        return x.key;
    }
    public Key max(){
        if(isEmpty()) throw new NoSuchElementException("Empty tree");
        Node x = root;
        while(x.right != null){
            x = x.right;
        }
        return x.key;
    }
    public Iterable<Key> keys(){
        throw new UnsupportedOperationException();
    }
    public void print(){
        print(root, 0);
    }
    private void print(Node x, int l){
        if(x == null) return;
        print(x.right, l + 1);
        if(l != 0) {
            for (int i = 0; i < l - 1; ++i) {
                System.out.print("|\t");
            }
            System.out.println("|---" + x.key + (isRED(x) ?"r":""));
        } else {
            System.out.println(x.key);
        }
        print(x.left, l + 1);
    }
}
