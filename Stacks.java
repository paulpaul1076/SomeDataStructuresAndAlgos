import java.util.Iterator;
import java.util.Scanner;
public class Stacks {
    static public void main(String[] args){
    	DequeArray<Integer> d = new DequeArray<Integer>();
    	for(int i = 9; i >= 0; --i){
    		d.pushLeft(i);
    	}
    	for(int i = 10; i < 20; ++i){
    		d.pushRight(i);
    	}
    	System.out.println("The size is " + d.size());
    	for(int i = 0; i < 20; ++i){
    		System.out.println(d.popRight());
    	}
    	//System.out.println(d.size());
    }
}