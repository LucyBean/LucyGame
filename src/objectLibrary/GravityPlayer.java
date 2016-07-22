package objectLibrary;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Dir;
import helpers.Point;
import objectLibs.SpriteLibrary;
import objects.Collider;
import objects.GravityActor;

public class GravityPlayer extends GravityActor {
	float speed;
	float jumpStrength = 1.2f;

	public GravityPlayer(Point origin) {
		super(origin, SpriteLibrary.PLAYER, new Collider(Point.ZERO, 40, 80), null);
	}

	@Override
	public void gravityAct(GameContainer gc, int delta) {
		Input input = gc.getInput();
		if (input.isKeyDown(Input.KEY_A)) {
			move(Dir.WEST, speed*delta);
		}
		if (input.isKeyDown(Input.KEY_E)) {
			move(Dir.EAST, speed*delta);
		}
		if (input.isKeyDown(Input.KEY_SPACE)) {
			jump(jumpStrength);
		}
	}

	@Override
	protected void resetGravityActorState() {
		speed = 0.3f;
	}

}
