package tests;

import java.util.HashMap;
import java.util.Map;

public class Maps {
	public static void main(String[] args) {
		Map<Integer, Y> m = new HashMap<Integer, Y>();
		
		m.put(0, new Y("Antelope"));
		m.put(3, new Y("Boy"));
		m.put(4, new Y("Caterpillar"));
		
		System.out.println(m);
		
		m.put(3, new Y("Dancer"));
		
		System.out.println(m);
	}
}
