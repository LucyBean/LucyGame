package tests;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ListRemovalTest {
	
	public static void main (String[] args) {
		List<A> list = new LinkedList<A>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public String toString() {
				String s = "(";
				Iterator<A> i = iterator();
				while (i.hasNext()) {
					A a = i.next();
					s += a.b + ", ";
				}
				s += ")";
				return s;
			}
		};
		
		list.add(new A(true));
		list.add(new A(true));
		list.add(new A(false));
		list.add(new A(true));
		list.add(new A(true));
		list.add(new A(true));
		list.add(new A(false));
		list.add(new A(false));
		
		System.out.println(list);
		
		Iterator<A> i = list.iterator();
		while (i.hasNext()) {
			A a = i.next();
			if (a.b) {
				list.remove(a);
			}
		}
		
		System.out.println(list);
		
	}
}

class A {
	public boolean b;
	public A(boolean b) {
		this.b = b;
	}
}
