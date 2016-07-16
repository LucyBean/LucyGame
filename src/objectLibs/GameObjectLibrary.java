package objectLibs;

import helpers.Point;
import objects.Actor;
import objects.Collider;
import objects.GameObject;
import objects.Static;

/**
 * Class for holding stock GameObjects
 * 
 * @author Lucy
 *
 */
public class GameObjectLibrary {
	public final static GameObject WALL(Point position) {
		return new Static(position, SpriteLibrary.WALL, new Collider(Point.ZERO, 50, 50),
				null);
	}

	public final static Actor BUTTON(Point position) {
		Actor button = new Actor(position, SpriteLibrary.BUTTON) {
			@Override
			public void onClick() {
				System.out.println("Button has been clicked!");
			}
		};

		return button;
	}
}
