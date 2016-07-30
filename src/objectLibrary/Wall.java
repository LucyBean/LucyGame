package objectLibrary;

import helpers.Point;
import objectLibs.SpriteLibrary;
import objects.Collider;
import objects.Static;
import worlds.WorldLayer;

public class Wall extends Static {
	public Wall(Point origin, int width, int height) {
		super(origin, WorldLayer.WORLD, SpriteLibrary.WALL,
				new Collider(Point.ZERO, width, height), null);
	}

	@Override
	protected void resetStaticState() {

	}
}
