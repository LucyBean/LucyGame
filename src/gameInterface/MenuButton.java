package gameInterface;

import helpers.Point;
import helpers.Rectangle;
import objectLibrary.Button;

public class MenuButton extends Button {
	public final static int WIDTH = 200;
	public final static int HEIGHT = 32;
	Menu menu;
	
	public MenuButton(String text) {
		super(new Rectangle(Point.ZERO, WIDTH, HEIGHT));
		setText(text);
	}
	
	public void setMenu(Menu m) {
		menu = m;
	}
	
	public Menu getMenu() {
		return menu;
	}
}