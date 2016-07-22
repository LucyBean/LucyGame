package worldLibrary;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Point;
import objectLibrary.GravityPlayer;
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

		GameObject gravityPlayer = new GravityPlayer(new Point(80, 50));
		addObject(gravityPlayer, WorldLayer.PLAYER);

		// Add some walls
		for (int i = 0; i < 10; i++) {
			addObject(new Wall(new Point(30 + 50*i, 350)), WorldLayer.WORLD);
		}
		for (int i = 0; i < 5; i++) {
			addObject(new Wall(new Point(530, 350 - 50*i)), WorldLayer.WORLD);
		}
		for (int i = 0; i < 5; i++) {
			addObject(new Wall(new Point(280 + 50*i, 150)), WorldLayer.WORLD);
		}
	}
}
