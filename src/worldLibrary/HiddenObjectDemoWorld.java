package worldLibrary;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import helpers.Point;
import images.ImageBuilder;
import images.Sprite;
import objectLibrary.Door;
import objectLibrary.Key;
import objectLibrary.Lock;
import objects.Actor;
import objects.InteractBox;
import objects.PickUpItem;
import objects.Static;
import objects.WorldObject;
import options.GlobalOptions;
import player.Player;
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

		int objectWindowWidth = GlobalOptions.WINDOW_WIDTH_GRID - 2;
		int objectWindowHeight = GlobalOptions.WINDOW_HEIGHT_GRID - 2;

		for (int i = 0; i < 10; i++) {
			Random r = new Random();
			Point position = new Point(r.nextFloat() * objectWindowWidth + 1,
					r.nextFloat() * objectWindowHeight + 1);
			WorldObject go = new HiddenSquare(position);
			addObject(go);
		}

		for (int i = 0; i < 5; i++) {
			Random r = new Random();
			Point position = new Point(r.nextFloat() * objectWindowWidth + 1,
					r.nextFloat() * objectWindowHeight + 1);
			Key key = new Key(position, 1);
			addObject(key);

			position = new Point(r.nextFloat() * objectWindowWidth + 1,
					r.nextFloat() * objectWindowHeight + 1);
			Lock lock = new Lock(position, 1);
			addObject(lock);

			position = new Point(r.nextFloat() * objectWindowWidth + 1,
					r.nextFloat() * objectWindowHeight + 1);
			Door door = new Door(position);
			lock.link(door);
			addObject(door);
		}
	}
}

class HiddenSquare extends Static {
	private final static int gridSize = GlobalOptions.GRID_SIZE;
	private final static Sprite sprite = new Sprite(
			ImageBuilder.makeRectangle(gridSize * 2, gridSize * 2,
					new Color(190, 60, 190)),
			gridSize);

	public HiddenSquare(Point origin) {
		super(origin, WorldLayer.WORLD, sprite, null,
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
	private final static int gridSize = GlobalOptions.GRID_SIZE;
	private final static Sprite sprite = new Sprite(
			ImageBuilder.makeRectangle(gridSize, gridSize,
					new Color(80, 250, 80)),
			gridSize);

	public PickUpSquare(Point origin) {
		super(origin, sprite, null);
	}
}
