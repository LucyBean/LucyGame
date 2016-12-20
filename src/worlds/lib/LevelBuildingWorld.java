package worlds.lib;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
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
