package com.company;

import java.util.*;

public class Main {
    static <T extends Comparable<T>> boolean isSorted(List<T> list){
        for(int i = 1; i < list.size(); ++i){
            int cmp = list.get(i - 1).compareTo(list.get(i));
            if(cmp > 0) return false;
        }
        return true;
    }
    public static void main(String[] args) {

    }
}
// only keys
// keys are ordered
// TODO: consider using arrays of linked lists
class SkipList<T extends Comparable<T>> implements Iterable<T>{
    // members
    private List<Node> linkedLists;
    private List<Integer> sizes;
    private int count;

    @Override
    public Iterator<T> iterator() {
        return new SkipListIterator();
    }

    private class SkipListIterator implements Iterator<T>{

        Node it = linkedLists.get(0).right;

        @Override
        public boolean hasNext() {
            return it != null;
        }

        @Override
        public T next() {
            T element = it.element;
            it = it.right;
            return element;
        }
    }

    // auxiliary class
    private class Node implements Comparable<Node>{
        T element;
        Node bottom, left, right;
        public Node(T element){
            this(element, null, null, null);
        }
        public Node(T element, Node bottom, Node left, Node right){
            this.element = element;
            this.bottom  = bottom;
            this.left    = left;
            this.right   = right;
        }

        @Override
        public int compareTo(Node other) {
            return element.compareTo(other.element);
        }
        public int compareTo(T x){
            if(element == null) return -1;
            return element.compareTo(x);
        }
    }

    private static class Flip{
        static Random rnd = new Random(System.currentTimeMillis());
        final static int heads = 0, tails = 1;
        public static int flip(){
            return (int)rnd.nextInt(2);
        }
    }

    // constructors


    public SkipList() {
        linkedLists = new ArrayList<>();
        linkedLists.add(new Node(null));
        sizes = new ArrayList<>();
        sizes.add(1);
    }
    public void insert(T element){
        /*if(isEmpty()){
            linkedLists.add(new Node(null));
            sizes.add(1);
        }*/

        List<Node> levelNodes = floor(element);

        if(levelNodes.get(0).compareTo(element) != 0){
            // must be inserted into the first level
            Node prev = levelNodes.get(0);
            Node next = prev.right;
            Node newNode = new Node(element, null, prev, next);

            prev.right = newNode;
            if(next != null) next.left = newNode;

            int curLevel = 0;

            sizes.set(curLevel, sizes.get(curLevel) + 1);
            curLevel++;
            //flip coins
            while(Flip.flip() == Flip.heads && curLevel < linkedLists.size()){
                System.out.println("linked list size is : " + linkedLists.size());
                prev = levelNodes.get(curLevel);
                next = prev.right;
                newNode = new Node(element, levelNodes.get(curLevel - 1).right, prev, next);

                prev.right = newNode;
                if(next != null) next.left = newNode;

                sizes.set(curLevel, sizes.get(curLevel) + 1);

                curLevel++;
            }
            if(Flip.flip() == Flip.heads && curLevel == linkedLists.size()){
                linkedLists.add(new Node(null, linkedLists.get(curLevel - 1), null, null));
                prev = linkedLists.get(linkedLists.size() - 1);

                //prev = levelNodes.get(curLevel);
                //next = prev.right;
                newNode = new Node(element, levelNodes.get(curLevel - 1).right, prev, null);

                prev.right = newNode;
                //if(next != null) next.left = newNode;

                //sizes.set(curLevel, sizes.get(curLevel) + 1);
                sizes.add(2);
            }
            count++;
        }
    }
    public void remove(T element){
        // TODO: maintain the invariant that empty lists gotta be removed, to avoid curNode != null check in insert
        List<Node> levelNodes = floor(element);
        Node x = levelNodes.get(0);
        if(x.compareTo(element) == 0){ // the element is in the list
            //TODO: remove redundancy
            for(int i = levelNodes.size() - 1; i >= 0; --i){
                Node temp = levelNodes.get(i);
                if(temp.compareTo(element) == 0){
                    if(sizes.get(i) == 2 && i != 0){
                        sizes.remove(i);
                        linkedLists.remove(i);
                    } else{
                        Node prev = temp.left;
                        Node next = temp.right;
                        prev.right = next;
                        //TODO: check wtf is wrong
                        if(next != null)
                            next.left = prev;
                        sizes.set(i, sizes.get(i) - 1);
                    }
                }
            }
            count--;
        }
    }
    public boolean contains(T element){
        List<Node> levelNodes = floor(element);
        Node temp = levelNodes.get(0);
        if(temp.compareTo(element) != 0) return false;
        return true;
    }
    public boolean isEmpty(){
        return count == 0;
    }
    public int size(){
        return count;
    }

    // helper methods

    private List<Node> floor(T element){
        List<Node> list = new ArrayList<>();

        Node curNode = linkedLists.get(linkedLists.size() - 1);
        while(curNode != null){
            while(curNode.right != null && curNode.right.compareTo(element) <= 0){
                curNode = curNode.right;
            }
            list.add(curNode);
            curNode = curNode.bottom;
        }

        Collections.reverse(list);
        return list;
    }
}