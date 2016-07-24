package tests;

import java.util.ArrayList;
import java.util.List;

public class ListSubtractionTest {
	public static void main(String[] args) {
		List<Y> prevList = new ArrayList<Y>();
		List<Y> nowList = new ArrayList<Y>();
		
		Y b1 = new Y("Angela");
		Y b2 = new Y("Barbara");
		Y b3 = new Y("Celia");
		Y b4 = new Y("Davina");
		
		prevList.add(b1);
		prevList.add(b2);
		prevList.add(b3);
		
		nowList.add(b2);
		nowList.add(b3);
		nowList.add(b4);
		
		System.out.println(prevList);
		System.out.println(nowList);
		
		List<Y> removed = new ArrayList<Y>(prevList);
		removed.removeAll(nowList);
		System.out.println("removed" + removed);
		
		List<Y> newItems = new ArrayList<Y>(nowList);
		newItems.removeAll(prevList);
		System.out.println("new: " + newItems);
	}
}

class Y {
	String name;
	
	public Y (String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
