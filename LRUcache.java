package com.company;

import java.util.Arrays;
import java.util.*;

/**
 * Created by Paul on 8/12/2015.
 */
public class Main {
    public static void main(String[] args){
        LRUcache<Integer> lru = new LRUcache<>();
        Random rnd = new Random();
        for(int i = 0; i < 1000; ++i){
            lru.insert(rnd.nextInt(20));
            if(Math.random() > 0.5){
                lru.remove();
            }
        }
        /*while(!lru.isEmpty()){
            lru.remove();
        }*/
        for(int i : lru){
            System.out.println(i);
        }
        System.out.println("------");
        lru.insert(18);
        for(int i : lru){
            System.out.println(i);
        }
    }
}

class LRUcache<K> implements Iterable<K>{
    private HashMap<K, Node> map;

    @Override
    public Iterator<K> iterator() {
        return new LRUiter();
    }

    private class LRUiter implements Iterator<K>{
        Node it = first;
        @Override
        public K next(){
            K key = it.data;
            it = it.next;
            return key;
        }
        @Override
        public boolean hasNext(){
            return it != null;
        }
    }

    private class Node{
        Node prev, next;
        K data;
        public Node(K data){
            this.data = data;
        }
    }
    private Node first, last;

    public LRUcache(){
        map = new HashMap<>();
    }

    public void insert(K key){
        if(!map.containsKey(key)) {
            Node node = new Node(key);
            if (isEmpty()) {
                first = last = node;
            } else {
                node.next = first;
                first.prev = node;
                first = node;
            }
            map.put(key, node);
        } else{
            Node node = map.get(key);
            if(node != first && node != last) {
                Node prev = node.prev;
                Node next = node.next;
                if (prev != null) {
                    prev.next = next;
                }
                if (next != null) {
                    next.prev = prev;
                }
                node.next = first;
                first.prev = node;
                first = node;
            } else if(node != first && node == last){
                last = last.prev;
                last.next = null;
                node.prev = null;
                node.next = first;
                first.prev = node;
                first=  node;
            }
        }
    }
    public K remove(){
        if(isEmpty()) return null;
        K ret;
        if(first == last){
            Node node = last;
            first = last = null;
            K key = node.data;
            map.remove(key);
            ret = key;
        } else{
            Node node = last;
            last = last.prev;
            last.next = null;
            node.prev = null;
            K key = node.data;
            map.remove(key);
            ret = key;
        }
        return ret;
    }
    public boolean isEmpty(){
        return map.isEmpty();
    }
    public int size(){
        return map.size();
    }
    public int countSize(){
        Node it = first;
        int count = 0;
        for(;it != null; it = it.next){
            System.out.println("Key: " + it.data);
            count++;
        }
        return count;
    }
}