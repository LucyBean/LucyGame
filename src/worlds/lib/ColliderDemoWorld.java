package worlds.lib;

import java.util.Random;

import org.newdawn.slick.SlickException;

import helpers.Point;
import objects.attachments.Collider;
import objects.attachments.InteractBox;
import objects.world.Actor;
import objects.world.Static;
import objects.world.WorldObject;
import objects.world.characters.Player;
import worlds.LucyGame;
import worlds.World;
import worlds.WorldLayer;

public class ColliderDemoWorld extends World {

	public ColliderDemoWorld(LucyGame game) {
		super(game, "Collider demo");
	}

	@Override
	public void init() throws SlickException {
		Actor mover = new Player(new Point(3, 3));
		mover.useGravity(false);
		addObject(mover);

		drawWallBorder();

		for (int i = 0; i < 10; i++) {
			Random r = new Random();
			Point position = new Point(r.nextFloat() * 18 + 1,
					r.nextFloat() * 13 + 1);
			WorldObject go = new ColliderBox(position);
			addObject(go);
		}
	}
}

class ColliderBox extends Static {
	public ColliderBox(Point origin) {
		super(origin, WorldLayer.WORLD, null, null,
				new Collider(Point.ZERO, 0.5f, 0.5f),
				new InteractBox(new Point(0.5f, 0), 0.5f, 0.5f));
	}

	@Override
	protected void resetStaticState() {

	}

	@Override
	public void interactedBy(Actor a) {
		disable();
	}
}
