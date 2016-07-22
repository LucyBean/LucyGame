package worldLibrary;

import org.newdawn.slick.SlickException;

import helpers.Point;
import objectLibrary.Button;
import objects.Actor;
import worlds.World;
import worlds.WorldLayer;

public class ButtonDemoWorld extends World {
	@Override
	public void init() throws SlickException {
		Actor button = new Button(new Point(100, 100));
		addObject(button, WorldLayer.INTERFACE);

	}
}
