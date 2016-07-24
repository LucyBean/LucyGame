package objectLibrary;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import helpers.Dir;
import helpers.Point;
import objectLibs.SpriteLibrary;
import objects.Actor;
import objects.Collider;
import worlds.WorldLayer;

public class Player extends Actor {
	float speed;

	public Player(Point origin) {
		super(origin, WorldLayer.PLAYER, SpriteLibrary.PLAYER,
				new Collider(Point.ZERO, 1, 2), null);
	}

	@Override
	public void act(GameContainer gc, int delta) {
		Input input = gc.getInput();
		if (input.isKeyDown(Input.KEY_COMMA)) {
			move(Dir.NORTH, speed * delta);
		}
		if (input.isKeyDown(Input.KEY_O)) {
			move(Dir.SOUTH, speed * delta);
		}
		if (input.isKeyDown(Input.KEY_A)) {
			move(Dir.WEST, speed * delta);
		}
		if (input.isKeyDown(Input.KEY_E)) {
			move(Dir.EAST, speed * delta);
		}
		if (input.isKeyDown(Input.KEY_PERIOD)) {
			interactWithAll();
		}
	}

	@Override
	protected void resetActorState() {
		speed = 0.01f;
	}
}
