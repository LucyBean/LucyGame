package objectLibrary;

import helpers.Point;
import helpers.Rectangle;

public class MenuButton extends Button {
	public final static int WIDTH = 200;
	public final static int HEIGHT = 32;
	
	public MenuButton(String text) {
		super(new Rectangle(Point.ZERO, WIDTH, HEIGHT));
		setText(text);
	}
	
}