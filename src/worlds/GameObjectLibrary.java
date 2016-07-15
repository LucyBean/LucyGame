package worlds;

import helpers.Point;

/**
 * Class for holding pre-made GameObjects
 * @author Lucy
 *
 */
public class GameObjectLibrary {
	public final static GameObject WALL(Point position) {
		return new Static(position, new Sprite(ImageLibrary.WALL_IMAGE), new Collider(Point.ZERO, 50, 50), null);
	}
}
