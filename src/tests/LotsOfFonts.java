package tests;

import java.awt.GraphicsEnvironment;

public class LotsOfFonts {

	public static void main(String[] args) {

		String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (String n : names) {
			System.out.println(n);
		}
	}

}
