package gameInterface;

import helpers.Point;
import objectLibs.ImageBuilder;

public class MenuButton extends Button {
	public final static int WIDTH = 360;
	public final static int HEIGHT = 32;
	Menu menu;
	
	public MenuButton(String text) {
		super(Point.ZERO, ImageBuilder.getMenuButtonBackground());
		setTextCentered(text);
	}
	
	public void setMenu(Menu m) {
		menu = m;
	}
	
	public Menu getMenu() {
		return menu;
	}
}
