package objectLibrary;

import helpers.Point;
import objectLibs.SpriteLibrary;
import objects.Collider;
import objects.Static;

public class Wall extends Static {
	public Wall(Point origin) {
		super(origin, SpriteLibrary.WALL, new Collider(Point.ZERO, 50, 50), null);
	}
}
