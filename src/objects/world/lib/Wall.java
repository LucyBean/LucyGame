package objects.world.lib;

import java.util.Optional;

import helpers.Dir;
import helpers.Point;
import objects.attachments.Collider;
import objects.images.SpriteBuilder;
import objects.world.ItemType;
import objects.world.Static;
import worlds.WorldLayer;

public class Wall extends Static {
	final int width;
	final int height;

	public Wall(Point origin, int width, int height) {
		super(origin, WorldLayer.WORLD, ItemType.WALL, null,
				Optional.of(new Collider(Point.ZERO, width, height)), Optional.empty());
		this.width = width;
		this.height = height;
		setSprite(SpriteBuilder.drawWall(width, height));
		getCollider().get().setSolid(true);
	}
	
	public Wall(Point origin) {
		this(origin, 1, 1);
	}
	
	public static Wall drawWall(Point start, Dir d, int length) {
		Wall w;
		switch (d) {
			case SOUTH:
				w = new Wall(start, 1, length);
				break;
			case EAST:
				w = new Wall(start, length, 1);
				break;
			case NORTH:
				w = new Wall(start.move(Dir.NORTH, length - 1), 1, length);
				break;
			case WEST:
				w = new Wall(start.move(Dir.WEST, length - 1), 1, length);
				break;
			default:
				w = null;
		}

		return w;
	}

	@Override
	protected void resetStaticState() {

	}
}
