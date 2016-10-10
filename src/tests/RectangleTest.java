package tests;

import helpers.Point;
import helpers.Rectangle;

public class RectangleTest {
	public static void main(String[] args) {
		Rectangle r = new Rectangle(new Point(10, 10), 10, 10);
		Point[] points = new Point[20];
		points[0] = new Point(14, 14); // true
		points[1] = new Point(30, 30); // false
		points[2] = new Point(14, 30); // false
		points[3] = new Point(4, 30); // false
		points[4] = new Point(30, 14); // false
		points[5] = new Point(4, 14); // false
		points[6] = new Point(10, 15); // true
		points[7] = new Point(9, 15); // false
		points[8] = new Point(11, 15); // true
		points[9] = new Point(19, 15); // true
		points[10] = new Point(20, 15); // false

		for (int i = 0; i < points.length; i++) {
			if (points[i] != null) {
				System.out.println("Test point: " + points[i] + "\t\t"
						+ r.contains(points[i]));
			}
		}

		System.out.println("\n\n--Corner test--");
		Point[] corners = new Point[4];
		corners[0] = r.getTopLeft(); // (10,10)
		corners[1] = r.getTopRight(); // (20,10)
		corners[2] = r.getBottomLeft(); // (10,20)
		corners[3] = r.getBottomRight();// (20,20)

		for (int i = 0; i < corners.length; i++) {
			boolean b = r.contains(corners[i]);
			System.out.println(corners[i] + " is contained? " + b);
		}

		System.out.println("\n\n--Rectangle overlap test--");

		Rectangle[] rects = new Rectangle[10];
		rects[0] = new Rectangle(new Point(5, 5), 15, 15);
		rects[1] = new Rectangle(new Point(25, 5), 15, 15);
		rects[2] = new Rectangle(new Point(5, 25), 15, 15);
		rects[3] = new Rectangle(new Point(25, 25), 15, 15);
		rects[4] = new Rectangle(new Point(15, 15), 15, 15);
		rects[5] = new Rectangle(new Point(60, 70), 30, 10);
		rects[6] = new Rectangle(new Point(70, 60), 10, 30);
		rects[7] = new Rectangle(new Point(80, 60), 10, 30);
		rects[8] = new Rectangle(new Point(50, 70), 30, 10);
		rects[9] = new Rectangle(new Point(50, 70), 30, 0);

		// Corner contain overlaps
		System.out.println("0,1 expected false: " + rects[0].overlaps(rects[1])
				+ " " + rects[1].overlaps(rects[0]));
		System.out.println("0,4 expected true: " + rects[0].overlaps(rects[4])
				+ " " + rects[4].overlaps(rects[0]));
		System.out.println("1,4 expected true: " + rects[1].overlaps(rects[4])
				+ " " + rects[4].overlaps(rects[1]));
		System.out.println("2,4 expected true: " + rects[2].overlaps(rects[4])
				+ " " + rects[4].overlaps(rects[2]));
		System.out.println("3,4 expected true: " + rects[3].overlaps(rects[4])
				+ " " + rects[4].overlaps(rects[3]));

		// Cross overlap
		System.out.println("5,6 expected true: " + rects[5].overlaps(rects[6])
				+ " " + rects[6].overlaps(rects[5]));

		// Adjacent rectangles
		System.out.println("6,7 expected false: " + rects[6].overlaps(rects[7])
				+ " " + rects[7].overlaps(rects[6]));

		// T overlap
		System.out.println("6,8 expected true: " + rects[6].overlaps(rects[8])
				+ " " + rects[8].overlaps(rects[6]));

		// Line overlap
		System.out.println("8,9 expected true: " + rects[8].overlaps(rects[9])
				+ " " + rects[9].overlaps(rects[8]));

		System.out.println("\n\n--Rectangle contains test--");
		rects[0] = new Rectangle(new Point(20, 20), 40, 40);
		rects[1] = new Rectangle(new Point(25, 25), 10, 10);
		rects[2] = new Rectangle(new Point(15, 15), 40, 40);

		System.out.println("0,1 expected t f: " + rects[0].contains(rects[1])
				+ " " + rects[1].contains(rects[0]));
		System.out.println("0,2 expected f f: " + rects[0].contains(rects[2])
				+ " " + rects[2].contains(rects[0]));
		System.out.println("0,0 expected true: " + rects[0].contains(rects[0]));
	}
}
