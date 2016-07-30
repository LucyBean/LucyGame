package objectLibrary;

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
