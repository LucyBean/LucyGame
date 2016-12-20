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
		
		Class<?> cls = cs.getClass();

		System.out.println("cs class: " + cls);
		System.out.println("Is anon? " + cls.isAnonymousClass());
		System.out.println("Superclass: " + cls.getSuperclass());
		
		s.sayHi();
		
		Child canon = new Child(){
			@Override
			void sayHi() {
				System.out.println("nope!");
			}
		};
		
		cls = canon.getClass();
		System.out.println("canon class: " + cls);
		System.out.println("Is anon? " + cls.isAnonymousClass());
		System.out.println("Superclass: " + cls.getSuperclass());
		
		System.out.println(Super.class.isAssignableFrom(Child.class));
		System.out.println(Super.class.isAssignableFrom(Super.class));
		System.out.println(Child.class.isAssignableFrom(Super.class));
	}
}

class Super {
	void sayHi() {
		System.out.println("hi!");
	}
}

class Child extends Super {

}
