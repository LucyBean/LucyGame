package objectLibrary;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objectLibs.SpriteLibrary;
import objects.Actor;
import objects.InteractBox;
import worlds.WorldLayer;

public class HiddenSquare extends Actor {
	public HiddenSquare(Point origin) {
		super(origin, WorldLayer.WORLD, SpriteLibrary.HIDDEN_SQUARE, null,
				new InteractBox(Point.ZERO, 2, 2));
	}

	@Override
	protected void resetActorState() {
		setVisibility(false);
	}

	@Override
	public void overlapStart(Actor a) {
		if (a instanceof Player) {
			setVisibility(true);
		}
	}

	@Override
	public void overlapEnd(Actor a) {
		if (a instanceof Player) {
			setVisibility(false);
		}
	}

	@Override
	public void act(GameContainer gc, int delta) {
		// TODO Auto-generated method stub

	}
}
