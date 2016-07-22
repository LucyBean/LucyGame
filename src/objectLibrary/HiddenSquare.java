package objectLibrary;

import helpers.Point;
import objectLibs.SpriteLibrary;
import objects.Actor;
import objects.InteractBox;

public class HiddenSquare extends Actor {
	public HiddenSquare(Point origin) {
		super(origin, SpriteLibrary.HIDDEN_SQUARE, null, new InteractBox(Point.ZERO,60,60));
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
}
