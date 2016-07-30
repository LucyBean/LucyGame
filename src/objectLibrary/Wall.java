package objectLibrary;

import helpers.Dir;
import helpers.Point;
import objectLibs.SpriteLibrary;
import objects.Collider;
import objects.Static;
import worlds.Camera;
import worlds.WorldLayer;

public class Wall extends Static {
	final int width;
	final int height;

	public Wall(Point origin, int width, int height) {
		super(origin, WorldLayer.WORLD, SpriteLibrary.WALL,
				new Collider(Point.ZERO, width, height), null);
		this.width = width;
		this.height = height;
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

	/**
	 * Draws the Wall object.
	 */
	@Override
	public void draw(Camera camera) {
		super.draw(camera, width, height);
	}
}
