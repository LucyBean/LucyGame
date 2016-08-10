package tests;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import helpers.Point;
import helpers.Rectangle;

public class HaveIMadeThis {
	static Map<Integer, Map<Integer, Rectangle>> rectangles = new HashMap<Integer, Map<Integer, Rectangle>>();
	
	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			Random r = new Random();
			int width = r.nextInt(4) + 1;
			int height = r.nextInt(4) + 1;
			getRectangle(width, height);
		}
	}
	
	public static Rectangle getRectangle(int width, int height) {
		// look up the rectangle
		
		// look up by width first
		Map<Integer, Rectangle> m = rectangles.get(width);
		
		if (m == null) {
			// rectangle has not been made
			m = new HashMap<Integer, Rectangle>();
			Rectangle r = new Rectangle(Point.ZERO, width, height);
			m.put(height, r);
			rectangles.put(width, m);
			
			System.out.println("Made new rectangle " + r);
			
			return r;
		} else {
			// look up by height
			Rectangle r = m.get(height);
			
			if (r == null) {
				// rectangle has not been made
				r = new Rectangle(Point.ZERO, width, height);
				m.put(height, r);

				System.out.println("Made new rectangle " + r);
			} else {
				System.out.println("Rectangle " + r + " has already been made.");
			}
			
			return r;
		}
	}
}
