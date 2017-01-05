package tests;

import java.awt.GraphicsEnvironment;

public class LotsOfFonts {

	public static void main(String[] args) {

		String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (String n : names) {
			System.out.println(n);
		}
		
		int major = 2;
		int minor = 1;
		int revision = 3;
		int build = 102;
		
		System.out.println(String.format("v %d.%d.%02d build %04d", major, minor, revision, build));
	}

}
