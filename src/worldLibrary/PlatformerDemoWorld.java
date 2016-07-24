package worldLibrary;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Dir;
import helpers.Point;
import objectLibrary.GravityPlayer;
import objectLibrary.Wall;
import objects.GameObject;
import objects.Sprite;
import objects.Static;
import objects.TextBox;
import worlds.World;
import worlds.WorldLayer;

public class PlatformerDemoWorld extends World {
	@Override
	public void init() throws SlickException {
		GameObject background = new Static(Point.ZERO, new Sprite(new Image("data/Desert.jpg"))) {
			@Override
			protected void resetStaticState() {

			}
		};
		addObject(background, WorldLayer.BACKGROUND);

		GameObject gravityPlayer = new GravityPlayer(new Point(3, 2));
		addObject(gravityPlayer, WorldLayer.PLAYER);
		
		GameObject statusBox = new TextBox(new Point(440,100), 200, 100);
		addObject(statusBox, WorldLayer.INTERFACE);

		// Add some walls
		drawWall(new Point(2, 10), Dir.EAST, 10);
		drawWall(new Point(11, 9), Dir.EAST, 3);
	}
}
