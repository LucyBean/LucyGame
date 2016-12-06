package tests;

import java.util.Collection;
import java.util.HashSet;

public class Generics {
	public static void main(String[] args) {
		Collection<Super> things = new HashSet<>();
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				things.add(new Super());
			} else {
				things.add(new Child());
			}
		}
		System.out.println(things);

		Collection<Child> children = new HashSet<>();
		Class<Child> childClass = Child.class;

		things.stream().filter(t -> childClass.isInstance(t)).map(
				a -> childClass.cast(a)).forEach(a -> children.add(a));
		
		System.out.println(children);
	}
}
