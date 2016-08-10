package tests;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Maps {
	public static void main(String[] args) {
		Map<Integer, Y> m = new HashMap<Integer, Y>();
		
		m.put(0, new Y("Antelope"));
		m.put(3, new Y("Boy"));
		m.put(4, new Y("Caterpillar"));
		
		System.out.println(m);
		
		m.put(3, new Y("Dancer"));
		
		System.out.println(m);
		
		System.out.println(m.get(8));
		
		Map<Integer, Integer> myFunction = new HashMap<Integer, Integer>();
		myFunction.put(1, 1);
		myFunction.put(2, 4);
		myFunction.put(3, 9);
		
		System.out.println(myFunction);
		Integer b = myFunction.get(6);
		System.out.println(myFunction.get(6));
		System.out.println(b);
		Set<Entry<Integer, Y>> set = m.entrySet();
		set.stream().forEach(a -> System.out.println(a.getValue()));
	}
}
