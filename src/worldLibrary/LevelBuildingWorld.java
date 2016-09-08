package worldLibrary;

import org.newdawn.slick.SlickException;

import worlds.LucyGame;
import worlds.World;

public class LevelBuildingWorld extends World {

	public LevelBuildingWorld(LucyGame game) {
		super(game, "Level building world");
	}
	
	@Override
	public void init() throws SlickException {
		startBuilding();
	}

}
