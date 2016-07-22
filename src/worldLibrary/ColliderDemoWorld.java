package worldLibrary;

import java.util.Random;

import org.newdawn.slick.SlickException;

import helpers.Point;
import objectLibrary.Player;
import objects.Actor;
import objects.Collider;
import objects.GameObject;
import objects.InteractBox;
import objects.Static;
import worlds.World;
import worlds.WorldLayer;

public class ColliderDemoWorld extends World {
	@Override
	public void init() throws SlickException {
		GameObject mover = new Player(new Point(80, 300));
		addObject(mover, WorldLayer.PLAYER);

		drawWallBorder();

		for (int i = 0; i < 10; i++) {
			Random r = new Random();
			Point position = new Point(r.nextFloat() * 550 + 50,
					r.nextFloat() * 400 + 50);
			GameObject go = new ColliderBox(position);
			addObject(go, WorldLayer.WORLD);
		}
	}
}

class ColliderBox extends Static {
	public ColliderBox(Point origin) {
		super(origin, null, new Collider(Point.ZERO, 20, 20), new InteractBox(new Point(20,0), 20, 20));
	}

	@Override
	protected void resetStaticState() {
		
	}
	
	@Override
	public void interactedBy(Actor a) {
		disable();
	}
	
}
