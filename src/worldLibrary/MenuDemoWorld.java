package worldLibrary;

import org.newdawn.slick.SlickException;

import helpers.Point;
import objectLibrary.WorldLoaderButton;
import worlds.LucyGame;
import worlds.World;

public class MenuDemoWorld extends World {
	public MenuDemoWorld(LucyGame game) {
		super(game, "Menu demo");
	}

	@Override
	public void init() throws SlickException {
		for (int i = 0; i < 5; i++) {
			WorldLoaderButton wlb = new WorldLoaderButton(
					new Point(32, (i + 2) * 64), i, "" + i);
			addObject(wlb);
		}
	}
}
