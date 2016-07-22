package tests;

import java.util.ArrayList;
import java.util.List;

public class ListSubtractionTest {
	public static void main(String[] args) {
		List<B> prevList = new ArrayList<B>();
		List<B> nowList = new ArrayList<B>();
		
		B b1 = new B("Angela");
		B b2 = new B("Barbara");
		B b3 = new B("Celia");
		B b4 = new B("Davina");
		
		prevList.add(b1);
		prevList.add(b2);
		prevList.add(b3);
		
		nowList.add(b2);
		nowList.add(b3);
		nowList.add(b4);
		
		System.out.println(prevList);
		System.out.println(nowList);
		
		List<B> removed = new ArrayList<B>(prevList);
		removed.removeAll(nowList);
		System.out.println("removed" + removed);
		
		List<B> newItems = new ArrayList<B>(nowList);
		newItems.removeAll(prevList);
		System.out.println("new: " + newItems);
	}
}

class B {
	String name;
	
	public B (String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
