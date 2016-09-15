package worldLibrary;

import org.newdawn.slick.SlickException;

import gameInterface.Palette;
import gameInterface.PropertyPanel;
import helpers.Point;
import worlds.LucyGame;
import worlds.World;
import worlds.WorldState;

public class LevelBuildingWorld extends World {

	public LevelBuildingWorld(LucyGame game) {
		super(game, "Level building world");
	}
	
	@Override
	public void init() throws SlickException {
		startBuilding();
		
		Palette p = new Palette(new Point(0,406));
		addObject(p, WorldState.BUILDING);
		
		PropertyPanel pp = new PropertyPanel(new Point(300, 300));
		addObject(pp, WorldState.BUILDING);
	}

}
