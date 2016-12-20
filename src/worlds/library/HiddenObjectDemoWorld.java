package worlds.library;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

import helpers.Point;
import objects.attachments.InteractBox;
import objects.images.ImageBuilder;
import objects.images.LayeredImage;
import objects.images.SingleSprite;
import objects.images.Sprite;
import objects.lib.Door;
import objects.lib.Key;
import objects.lib.Lock;
import objects.world.Actor;
import objects.world.ItemType;
import objects.world.PickUpItem;
import objects.world.Static;
import objects.world.WorldObject;
import objects.world.characters.Player;
import options.GlobalOptions;
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
			Lock lock = new Lock(position, 1, i);
			addObject(lock);

			position = new Point(r.nextFloat() * objectWindowWidth + 1,
					r.nextFloat() * objectWindowHeight + 1);
			Door door = new Door(position, i);
			addObject(door);
		}
	}
}

class HiddenSquare extends Static {
	private final static int gridSize = GlobalOptions.GRID_SIZE;
	private final static Sprite sprite = new SingleSprite(new LayeredImage(
			ImageBuilder.makeRectangle(gridSize * 2, gridSize * 2,
					new Color(190, 60, 190))),
			gridSize);

	public HiddenSquare(Point origin) {
		super(origin, WorldLayer.WORLD, null, sprite, null,
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
		super(origin, ItemType.GEM, null);
	}
}
