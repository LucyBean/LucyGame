package worldLibrary;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import helpers.Point;
import objectLibrary.Player;
import objectLibs.SpriteBuilder;
import objects.Actor;
import objects.InteractBox;
import objects.WorldObject;
import worlds.LucyGame;
import worlds.World;
import worlds.WorldLayer;

public class HiddenObjectDemoWorld extends World {

	public HiddenObjectDemoWorld(LucyGame game) {
		super(game, "Hidden objects");
	}

	@Override
	public void init() throws SlickException {
		Actor mover = new Player(new Point(2, 2));
		mover.useGravity(false);
		addObject(mover);

		drawWallBorder();

		for (int i = 0; i < 10; i++) {
			Random r = new Random();
			Point position = new Point(r.nextFloat() * 17 + 1,
					r.nextFloat() * 12 + 1);
			WorldObject go = new HiddenSquare(position);
			addObject(go);
		}
	}
}

class HiddenSquare extends Actor {
	public HiddenSquare(Point origin) {
		super(origin, WorldLayer.WORLD, SpriteBuilder.HIDDEN_SQUARE, null,
				new InteractBox(Point.ZERO, 2, 2));
		useGravity(false);
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
