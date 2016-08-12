package worldLibrary;

import java.util.Random;

import org.newdawn.slick.SlickException;

import helpers.Point;
import objectLibrary.Door;
import objectLibrary.Key;
import objectLibrary.Lock;
import objectLibrary.Player;
import objectLibs.SpriteBuilder;
import objects.Actor;
import objects.InteractBox;
import objects.PickUpItem;
import objects.Static;
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
		
		for (int i = 0; i < 5; i++) {
			Random r = new Random();
			Point position = new Point(r.nextFloat() * 17 + 1,
					r.nextFloat() * 12 + 1);
			Key key = new Key(position);
			addObject(key);
			
			position = new Point(r.nextFloat() * 17 + 1,
					r.nextFloat() * 12 + 1);
			Lock lock = new Lock(position, key);
			addObject(lock);
			
			position = new Point(r.nextFloat() * 17 + 1,
					r.nextFloat() * 12 + 1);
			Door door = new Door(position);
			lock.link(door);
			addObject(door);
		}
	}
}

class HiddenSquare extends Static {
	public HiddenSquare(Point origin) {
		super(origin, WorldLayer.WORLD, SpriteBuilder.HIDDEN_SQUARE, null,
				new InteractBox(Point.ZERO, 2, 2));
	}

	@Override
	public void overlapStart(WorldObject a) {
		if (a instanceof Player) {
			setVisibility(true);
		}
	}

	@Override
	public void overlapEnd(WorldObject a) {
		if (a instanceof Player) {
			setVisibility(false);
		}
	}

	@Override
	protected void resetStaticState() {
		setVisibility(false);
		
	}
}

class PickUpSquare extends PickUpItem {
	public PickUpSquare(Point origin) {
		super(origin, SpriteBuilder.PICK_UP_ITEM);
	}
}
