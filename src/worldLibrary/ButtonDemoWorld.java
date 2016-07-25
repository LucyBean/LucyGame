package worldLibrary;

import org.newdawn.slick.SlickException;

import helpers.Point;
import helpers.Rectangle;
import objectLibrary.Button;
import worlds.LucyGame;
import worlds.World;

public class ButtonDemoWorld extends World {
	World[] worlds;

	public ButtonDemoWorld(LucyGame game) {
		super(game);
	}

	@Override
	public void init() throws SlickException {
		Button button = new Button(new Rectangle(new Point(100, 100), 6, 1));
		addObject(button);

		Button wlb = new WorldLoaderButton(new Point(100, 200),
				new MapImportingDemo(getGame()), "Map importing demo");
		addObject(wlb);
	}
}

class WorldLoaderButton extends Button {
	World world;

	public WorldLoaderButton(Point origin, World world, String name) {
		super(new Rectangle(origin, 6, 1));
		this.world = world;
		setText(name);
	}

	@Override
	public void onClick() {
		getWorld().setNewWorld(world);
	}
}
