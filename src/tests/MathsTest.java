package tests;

import java.util.Random;

public class MathsTest {
	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			Random r = new Random();
			float f = r.nextFloat() * 100;
			float remainder = f % 10;
			float rounded = f - remainder + (remainder > 5 ? 10 : 0);

			System.out.println("f: " + f + "\t\trounded: " + rounded);
		}
	}
}
