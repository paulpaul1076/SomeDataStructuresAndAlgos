package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Cuckoo<Integer, String> ht = new Cuckoo<>();
        Random rnd = new Random();
        RandomStr rndStr = new RandomStr();

        for(int i = 0; i < 100; ++i){
            int next = rnd.nextInt(100);
            System.out.println("\n\ninserting : " + next);
            ht.put(next, rndStr.next(20));
            ht.print();
        }

        System.out.println("initial size: " + ht.size());

        while(!ht.isEmpty()){
            int next = rnd.nextInt(100);
            System.out.println("removing: " + next);
            ht.remove(next);
            System.out.println("size is " + ht.size());
        }

    }
}

class RandomStr{
    private Random rnd;
    public RandomStr(long seed){
        rnd = new Random(seed);
    }
    public RandomStr(){
        rnd = new Random();
    }
    public String next(int bound){
        StringBuilder sb = new StringBuilder(bound);
        for(int i = 0; i < bound; ++i){
            sb.append(rnd.nextInt(Character.MAX_VALUE + 1));
        }
        return sb.toString();
    }
}

class Cuckoo<Key, Value> implements ST<Key, Value>{

    private Key[] keysOne;
    private Value[] valuesOne;

    private Key[] keysTwo;
    private Value[] valuesTwo;

    private int N;
    private int M;

    private final int maxLoop = 10;
    private int hasher = 0;
    private int timesRehashed = 0;

    private Random rnd = new Random(System.currentTimeMillis());

    public Cuckoo(){
        M = 11;
        keysOne = (Key[]) new Object[M];
        keysTwo = (Key[]) new Object[M];
        valuesOne = (Value[]) new Object[M];
        valuesTwo = (Value[]) new Object[M];
    }

    @Override
    public void put(Key key, Value val){
        if(containsOne(key)) valuesOne[hashOne(key)] = val;
        else if(containsTwo(key)) valuesTwo[hashTwo(key)] = val;
        else{
            int index = hashOne(key);
            for(int i = 0; i < maxLoop; ++i){
                if(keysOne[index] == null){
                    keysOne[index] = key;
                    valuesOne[index] = val;
                    N++;
                    return;
                } else{
                    Key oldKey = keysOne[index];
                    Value oldVal = valuesOne[index];
                    keysOne[index] = key;
                    valuesOne[index] = val;

                    key = oldKey;
                    val = oldVal;

                    int tempIndex = hashTwo(key);
                    if(keysTwo[tempIndex] == null){
                        keysTwo[tempIndex] = key;
                        valuesTwo[tempIndex] = val;
                        N++;
                        return;
                    } else{
                        oldKey = keysTwo[tempIndex];
                        oldVal = valuesTwo[tempIndex];
                        keysTwo[tempIndex] = key;
                        valuesTwo[tempIndex] = val;
                        key = oldKey;
                        val = oldVal;
                    }
                }
            }
            rehash(); put(key, val);
        }
    }

    public void print(){
        System.out.println("\nOne:\n");
        for(int i = 0; i < keysOne.length; ++i){
            if(keysOne[i] != null){
                System.out.print(keysOne[i] + ",");
            }
        }
        System.out.println("\nTwo:\n");
        for(int i = 0; i < keysTwo.length; ++i) {
            if (keysTwo[i] != null){
                System.out.print(keysTwo[i] + ", ");
            }
        }
        System.out.println("\nTimes rehashed: " + timesRehashed);
    }

    @Override
    public void remove(Key key) {
        if(containsOne(key)){
            keysOne[hashOne(key)] = null;
            valuesOne[hashOne(key)] = null;
        } else if(containsTwo(key)){
            keysTwo[hashTwo(key)] = null;
            valuesTwo[hashTwo(key)] = null;
        } else return;

        N--;
        if(N <= M / 4) resize(M / 2);
    }

    private void resize(int size){
        Key[] oldKeysOne = keysOne;
        Key[] oldKeysTwo = keysTwo;
        Value[] oldValsOne = valuesOne;
        Value[] oldValsTwo = valuesTwo;

        M = size;

        keysOne = (Key[]) new Object[size];
        valuesOne = (Value[]) new Object[size];
        keysTwo = (Key[]) new Object[size];
        valuesTwo = (Value[]) new Object[size];

        for(int i = 0; i < oldKeysOne.length; ++i){
            if(oldKeysOne[i] != null){
                N--;
                put(oldKeysOne[i], oldValsOne[i]);
            }
        }
        for(int i = 0; i < oldKeysTwo.length; ++i){
            if(oldKeysTwo[i] != null){
                N--;
                put(oldKeysTwo[i], oldValsTwo[i]);
            }
        }

    }

    @Override
    public Value get(Key key) {
        int index1 = hashOne(key);
        if(key.equals(keysOne[index1])) return valuesOne[index1];
        int index2 = hashTwo(key);
        if(key.equals(keysTwo[index2])) return valuesTwo[index2];
        return null;
    }

    @Override
    public int size() {
        return N;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Key key) {
        if(containsOne(key) || containsTwo(key)) return true;
        return false;
    }

    private int hashOne(Key key){
        return ((31 + hasher) * (key.hashCode() & 0x7fffffff)) % M;
    }

    private int hashTwo(Key key){
        return ((13 + hasher) * (key.hashCode() & 0x7fffffff)) % M;
    }

    private boolean containsOne(Key key){
        int index = hashOne(key);
        if(key.equals(keysOne[index])) return true;
        return false;
    }

    private boolean containsTwo(Key key){
        int index = hashTwo(key);
        if(key.equals(keysTwo[index])) return true;
        return false;
    }

    private void rehash(){
        hasher = (hasher + 13) % 1000;
        resize(2 * M);
        timesRehashed++;
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
class SeparateChainingST<Key, Value> implements ST<Key, Value>{
    private LinkedList<Key, Value>[] table;
    private int N;
    private int M;

    public SeparateChainingST(int cap){
        M = cap;
        table = (LinkedList<Key, Value>[]) new LinkedList[M];
        for(int i = 0; i < M; ++i){
            table[i] = new LinkedList<>();
        }
    }

    public SeparateChainingST(){
        M = 13;
        table = (LinkedList<Key, Value>[]) new LinkedList[M];
        for(int i = 0; i < M; ++i){
            table[i] = new LinkedList<>();
        }
    }

    @Override
    public void put(Key key, Value val) {
        if(N >= M/2) resize(M*2);
        int index = hash(key);
        int oldSize = table[index].size();
        table[index].put(key, val);
        int newSize = table[index].size();
        if(oldSize != newSize) N++;
    }

    @Override
    public void remove(Key key) {
        int index = hash(key);
        int oldSize = table[index].size();
        table[index].remove(key);
        int newSize = table[index].size();
        if(oldSize != newSize) N--;
        if(N > 0 && N == M/8) resize(M/2);
    }

    @Override
    public Value get(Key key) {
        int index = hash(key);
        Value val = table[index].get(key);
        if(val != null) return val;
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
        int index = hash(key);
        if(table[index].contains(key)) return true;
        return false;
    }

    private int hash(Key key){
        return (key.hashCode() & 0x7fffffff) % M;
    }

    private void resize(int cap) {
        SeparateChainingST<Key, Value> temp = new SeparateChainingST<>(cap);
        for(int i = 0; i < M; ++i){
            if(!table[i].isEmpty()){
                for(LinkedList<Key, Value>.Entry e : table[i]){
                    temp.put(e.key, e.val);
                }
            }
        }
        M = cap;
        table = temp.table;
    }

}
class LinkedList<Key, Value> implements ST<Key, Value>, Iterable<LinkedList<Key, Value>.Entry>{
    @Override
    public Iterator<LinkedList<Key, Value>.Entry> iterator() {
        return new LLIterator();
    }

    private class LLIterator implements Iterator<LinkedList<Key, Value>.Entry>{
        private Node it = first;
        @Override
        public boolean hasNext() {
            return it != null;
        }

        @Override
        public Entry next() {
            Node oldNode = it;
            it = it.next;
            return new Entry(oldNode.key, oldNode.val);
        }
    }

    private class Node{
        Node next, prev;
        Key key;
        Value val;
        public Node(Key key, Value val, Node next, Node prev){
            this.key = key; this.val = val; this.next = next; this.prev = prev;
        }
    }
    public class Entry{
        Key key;
        Value val;
        public Entry(Key key, Value val){
            this.key = key;
            this.val = val;
        }
    }
    private Node first, last;
    private int count;
    public LinkedList(){

    }
    public void put(Key key, Value val){
        if(count == 0){
            first = new Node(key, val, null, null);
            last = first;
            count++;
        } else{
            Node x = getNode(key);
            if(x != null){
                x.val = val;
            } else{
                Node oldLast = last;
                last = new Node(key, val, null, oldLast);
                oldLast.next = last;
                count++;
            }
        }
    }

    public void remove(Key key) {
        Node x = getNode(key);
        if(x != null){

            if(count == 1){
                first = last = null;
            } else if(x == first){
                first = first.next;
                first.prev = null;
            } else if(x == last){
                last = last.prev;
                last.next = null;
            } else{
                Node prev = x.prev;
                Node next = x.next;
                prev.next = next;
                next.prev = prev;
            }
            count--;
        }
    }
    private Node getNode(Key key){
        for(Node it = first; it != null; it = it.next){
            if(it.key.equals(key)) return it;
        }
        return null;
    }
    public Value get(Key key){
        Node x = getNode(key);
        if(x != null) return x.val;
        return null;
    }
    public boolean isEmpty(){
        return count == 0;
    }

    @Override
    public boolean contains(Key key) {
        Node x = getNode(key);
        if(x != null) return true;
        return false;
    }

    public int size(){
        return count;
    }
}