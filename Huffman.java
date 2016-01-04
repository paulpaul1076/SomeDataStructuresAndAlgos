/**
 * Created by paulp on 02.01.2016.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Huffman {

    private Huffman() {
    }

    private static class Node {
        public Node left, right;
        public char ch;
        public int mark;
        public int freq;

        public Node(Node left, Node right, char ch, int mark, int freq) {
            this.left = left;
            this.right = right;
            this.ch = ch;
            this.mark = mark;
            this.freq = freq;
        }
    }

    private static Node readTrie(boolean isRight) {
        boolean bit = BinaryStdIn.readBoolean();
        Node newNode = new Node(null, null, '\0', isRight ? 1 : 0, 0);
        if (bit) {
            newNode.ch = BinaryStdIn.readChar();
            System.err.println("char = " + newNode.ch);
        } else {
            newNode.left = readTrie(false);
            newNode.right = readTrie(true);
        }
        return newNode;
    }

    private static void writeTrie(Node x) {
        if (x.left == null && x.right == null) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch);
        } else {
            BinaryStdOut.write(false);
            writeTrie(x.left);
            writeTrie(x.right);
        }
    }

    // handle empty input
    public static void compress() {
        HashMap<Character, Integer> freqTable = new HashMap<>();
        ArrayList<Character> message = new ArrayList<>();
        //System.err.println("Is empty ? " + BinaryStdIn.isEmpty());
        //do {
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            message.add(ch);
            if (!freqTable.containsKey(ch)) freqTable.put(ch, 1);
            else freqTable.put(ch, freqTable.get(ch) + 1);
            //System.err.println("Numerical value of char = " + ((int) ch));
            //System.err.println(ch);
            //System.err.println("ITER");
        }
        //} while (!BinaryStdIn.isEmpty());
        PriorityQueue<Node> minpq = new PriorityQueue<>((x, y) -> (x.freq - y.freq));
        for (char c : freqTable.keySet()) {
            //System.err.printf("%c{%d}\n", c, freqTable.get(c));
            minpq.add(new Node(null, null, c, 3, freqTable.get(c)));
        }
        while (minpq.size() > 1) {
            Node x = minpq.poll();
            Node y = minpq.poll();
            //System.err.println("x = " + x.ch);
            //System.err.println("y = " + y.ch);
            //System.err.println("");
            x.mark = 0;
            y.mark = 1;
            Node newNode = new Node(x, y, '\0', 3, x.freq + y.freq);
            minpq.add(newNode);
        }
        Node root = null;
        if (minpq.size() != 0)
            root = minpq.poll();
        String[] codes = new String[256];
        if (root != null)
            buildTable(codes, "", root);
        //System.err.println("codes:");
        /*for (int i = 0; i < codes.length; i++) {
            if (codes[i] != null)
            //    System.err.println((char) i + " > " + codes[i]);
        }*/
        //System.err.println("N = " + message.size());
        BinaryStdOut.write(message.size());
        if (root != null)
            writeTrie(root);
        for (char c : message) {
            String code = codes[c];
            for (char c1 : code.toCharArray()) {
                BinaryStdOut.write(c1 == '1');
            }
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    private static void buildTable(String[] codes, String collected, Node x) {
        if (x.left == null && x.right == null) {
            codes[x.ch] = collected;
            return;
        }
        buildTable(codes, collected + "0", x.left);
        buildTable(codes, collected + "1", x.right);
    }

    public static void decompress() {
        Node root = null;
        int N = BinaryStdIn.readInt();
        //System.err.println("N = " + N);
        if (N == 0) return;
        root = readTrie(false);
        String[] codes = new String[256];
        buildTable(codes, "", root);
        //System.err.println("codes:");
        /*for (int i = 0; i < codes.length; i++) {
            if (codes[i] != null)
                System.err.println((char) i + " > " + codes[i]);
        }*/
        for (int i = 0; i < N; i++) {
            Node x = root;
            while (x.left != null && x.right != null) {
                boolean bit = BinaryStdIn.readBoolean();
                if (bit) x = x.right;
                else x = x.left;
            }
            //System.err.println(x.ch);
            BinaryStdOut.write(x.ch);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        assert args.length == 1;
        String option = args[0];
        switch (option) {
            case "+":
                compress();
                //System.err.println("successfully compressed");
                break;
            case "-":
                decompress();
                //System.err.println("successfully decompressed");
                break;
            default:
                //System.err.println("unknown option");
        }
    }
}
