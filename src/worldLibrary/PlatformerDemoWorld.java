package worldLibrary;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import helpers.Dir;
import helpers.Point;
import objectLibrary.GravityPlayer;
import objectLibrary.StatusWindow;
import objects.Actor;
import objects.Sprite;
import objects.Static;
import objects.WorldObject;
import worlds.LucyGame;
import worlds.World;
import worlds.WorldLayer;
import worlds.WorldState;

/**
 * A demo of a basic platform game.
 * 
 * @author Lucy
 *
 */
public class PlatformerDemoWorld extends World {
	public PlatformerDemoWorld(LucyGame game) {
		super(game, "Platformer demo");
	}

	@Override
	public void init() throws SlickException {
		WorldObject background = new Static(Point.ZERO, WorldLayer.BACKGROUND,
				new Sprite(new Image("data/Desert.jpg"))) {
			@Override
			protected void resetStaticState() {

			}
		};
		addObject(background);

		Actor gravityPlayer = new GravityPlayer(new Point(3, 2));
		addObject(gravityPlayer);

		StatusWindow sw = new StatusWindow(new Point(340, 0));
		sw.setWatching(gravityPlayer);
		addObject(sw, WorldState.PLAYING);

		// Add some walls
		drawWall(new Point(2, 10), Dir.EAST, 10);
		drawWall(new Point(11, 9), Dir.EAST, 3);
	}
}
