package tests;

import helpers.Point;
import helpers.Rectangle;

public class RectangleTest {
	public static void main(String[] args) {
		Rectangle r = new Rectangle(new Point(10,10), 10, 10);
		Point[] points = new Point[20];
		points[0] = new Point(14, 14);	// true
		points[1] = new Point(30, 30);	// false
		points[2] = new Point(14, 30);	// false
		points[3] = new Point(4, 30);	// false
		points[4] = new Point(30, 14);	// false
		points[5] = new Point(4, 14);	// false
		points[6] = new Point(10, 15);	// true
		points[7] = new Point(9, 15);	// false
		points[8] = new Point(11, 15);	// true
		points[9] = new Point(19, 15);	// true
		points[10] = new Point(20, 15);	// false
		
		for (int i = 0; i < points.length; i++) {
			if (points[i] != null) {
				System.out.println("Test point: " + points[i] + "\t\t" + r.contains(points[i]));
			}
		}
	}
}
