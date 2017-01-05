package tests;

public class CharactersTesting {
	public static void main(String[] args) {
		String a = "a";
		String d = "a";
		System.out.println(a == d);
		
		for (int i = 0; i < 4; i++) {
			int number = 1 << i;
			char c = (char) number;
			byte b = (byte) number;
			System.out.print(number + " ");
			System.out.print(c);
			System.out.println(b);
		}
	}
}
