package tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PassToAll {
	public static void main(String[] args) {
		List<Y> ys = new ArrayList<Y>();
		
		Y b1 = new Y("Angela");
		Y b2 = new Y("Barbara");
		Y b3 = new Y("Celia");
		Y b4 = new Y("Davina");
		
		ys.add(b1);
		ys.add(b2);
		ys.add(b3);
		ys.add(b4);
	}
	
	public <Z> void applyToAll(List<Y> ys, Function<Y> f) {
		Iterator<Y> yi = ys.iterator();
		
		while (yi.hasNext()) {
			Y y = yi.next();
			f.exec(y);
		}
	}
}

abstract class Function<W> {
	public abstract void exec(W a);
}
