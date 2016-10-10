package worldLibrary;

import org.newdawn.slick.SlickException;

import gameInterface.MenuSet;
import worlds.LucyGame;
import worlds.World;
import worlds.WorldState;

public class MenuDemoWorld extends World {
	public MenuDemoWorld(LucyGame game) {
		super(game, "Menu demo");
	}

	@Override
	public void init() throws SlickException {
		MenuSet m = new MenuSet();
		for (int i = 0; i < 6; i++) {
			final int w = i;
			m.add((() -> "Level " + w),
					s -> s.getWorld().getGame().loadLevel(w));
		}
		addObject(m, WorldState.PLAYING);
	}
}
