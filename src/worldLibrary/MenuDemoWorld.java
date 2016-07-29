package worldLibrary;

import org.newdawn.slick.SlickException;

import helpers.Point;
import helpers.Rectangle;
import objectLibrary.Button;
import worlds.LucyGame;
import worlds.World;

public class MenuDemoWorld extends World {
	World[] worlds;

	public MenuDemoWorld(LucyGame game) {
		super(game);
	}

	@Override
	public void init() throws SlickException {
		worlds = new World[10];

		worlds[0] = new MapImportingDemo(getGame());
		worlds[1] = new CameraObjectLoadingDemo(getGame());
		worlds[2] = new ColliderDemoWorld(getGame());
		worlds[3] = new HiddenObjectDemoWorld(getGame());
		worlds[4] = new PlatformerDemoWorld(getGame());

		for (int i = 0; i < worlds.length; i++) {
			if (worlds[i] != null) {
				Button wlb = new WorldLoaderButton(new Point(32, (i + 2) * 64),
						worlds[i], worlds[i].toString());
				addObject(wlb);
			}
		}
	}
}

class WorldLoaderButton extends Button {
	World world;

	public WorldLoaderButton(Point origin, World world, String name) {
		super(new Rectangle(origin, 400, 32));
		this.world = world;
		setText(name);
	}

	@Override
	public void onClick(int button) {
		getWorld().setNewWorld(world);
	}
}
