package objectLibrary;

import helpers.Point;
import objectLibs.SpriteLibrary;
import objects.Collider;
import objects.Static;
import worlds.GlobalOptions;

public class Wall extends Static {
	public Wall(Point origin) {
		super(origin, SpriteLibrary.WALL, new Collider(Point.ZERO, GlobalOptions.GRID_SIZE, GlobalOptions.GRID_SIZE), null);
	}

	@Override
	protected void resetStaticState() {
		
	}
}
