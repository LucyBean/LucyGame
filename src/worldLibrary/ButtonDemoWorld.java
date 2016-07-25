package worldLibrary;

import org.newdawn.slick.SlickException;

import helpers.Point;
import objectLibrary.Button;
import worlds.World;

public class ButtonDemoWorld extends World {
	@Override
	public void init() throws SlickException {
		Button button = new Button(new Point(100, 100));
		addObject(button);
		button.setText("Hello, world!");
	}
}
