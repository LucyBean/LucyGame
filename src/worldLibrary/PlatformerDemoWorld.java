package worldLibrary;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Point;
import objectLibrary.Player;
import objectLibrary.Wall;
import objects.GameObject;
import objects.Sprite;
import objects.Static;
import worlds.World;
import worlds.WorldLayer;

public class PlatformerDemoWorld extends World {
	@Override
	public void init() throws SlickException {
		GameObject background = new Static(Point.ZERO,
				new Sprite(new Image("data/Desert.jpg"))) {
					@Override
					protected void resetStaticState() {
						
					}
		};
		addObject(background, WorldLayer.BACKGROUND);

		GameObject player = new Player(new Point(40, 50));
		addObject(player, WorldLayer.PLAYER);

		for (int i = 0; i < 10; i++) {
			addObject(new Wall(new Point(200 + 50 * i, 200)), WorldLayer.WORLD);
		}
	}
}
