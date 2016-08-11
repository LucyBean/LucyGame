package gameInterface;

import helpers.Point;
import helpers.Rectangle;
import objectLibrary.Button;

public class MenuButton extends Button {
	public final static int WIDTH = 360;
	public final static int HEIGHT = 32;
	Menu menu;
	
	public MenuButton(String text) {
		super(new Rectangle(Point.ZERO, WIDTH, HEIGHT));
		setTextCentered(text);
	}
	
	public void setMenu(Menu m) {
		menu = m;
	}
	
	public Menu getMenu() {
		return menu;
	}
}
