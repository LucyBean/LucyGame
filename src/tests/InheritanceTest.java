package tests;

public class InheritanceTest {
	static void foo(Super s) {
		System.out.println("foo!");
		
		if (s instanceof Child) {
			foo((Child) s);
		}
	}

	static void foo(Child c) {
		System.out.println("bar!");
	}

	public static void main(String[] args) {
		Super s = new Super();
		Child c = new Child();
		Super cs = c;

		foo(s);
		foo(c);
		foo(cs);
		
		s.sayHi();
	}
}

class Super {
	void sayHi() {
		System.out.println("hi!");
	}
}

class Child extends Super {

}
