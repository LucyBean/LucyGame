package worldLibrary;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Dir;
import helpers.Point;
import objectLibrary.GravityPlayer;
import objectLibrary.TextBox;
import objects.GameObject;
import objects.Sprite;
import objects.Static;
import worlds.LucyGame;
import worlds.World;
import worlds.WorldLayer;

/**
 * A demo of a basic platform game.
 * 
 * @author Lucy
 *
 */
public class PlatformerDemoWorld extends World {
	public PlatformerDemoWorld(LucyGame game) {
		super(game);
	}

	@Override
	public void init() throws SlickException {
		GameObject background = new Static(Point.ZERO, WorldLayer.BACKGROUND,
				new Sprite(new Image("data/Desert.jpg"))) {
			@Override
			protected void resetStaticState() {

			}
		};
		addObject(background);

		GameObject gravityPlayer = new GravityPlayer(new Point(3, 2));
		addObject(gravityPlayer);

		GameObject statusBox = new TextBox(new Point(440, 100), 200, 100);
		addObject(statusBox);

		// Add some walls
		drawWall(new Point(2, 10), Dir.EAST, 10);
		drawWall(new Point(11, 9), Dir.EAST, 3);
	}
}
