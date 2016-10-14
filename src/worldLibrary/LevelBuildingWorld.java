package worldLibrary;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import gameInterface.Palette;
import gameInterface.PropertyPanel;
import helpers.Point;
import options.GlobalOptions;
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

		Palette p = new Palette(Point.ZERO);
		p.setPosition(new Point(0,
				GlobalOptions.WINDOW_HEIGHT - p.getHeightPixels()));
		addObject(p, WorldState.BUILDING);

		PropertyPanel pp = new PropertyPanel(Point.ZERO);
		pp.setPosition(
				new Point(GlobalOptions.WINDOW_WIDTH - pp.getWidthPixels(),
						GlobalOptions.WINDOW_HEIGHT - pp.getHeightPixels()));
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
