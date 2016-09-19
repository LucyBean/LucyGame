package worldLibrary;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
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
	
	@Override
	public void update(GameContainer gc, int delta) {
		if (isIgnoringInput()) {
			Input input = gc.getInput();
			if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				ignoreInput(false);
			}
		} else {
			super.update(gc, delta);
		}
	}

}
