package worldLibrary;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import characters.Matt;
import helpers.Dir;
import helpers.Point;
import images.Sprite;
import objectLibrary.Wall;
import objects.Actor;
import objects.Static;
import objects.WorldObject;
import player.Player;
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
		super(game, "Platformer demo");
	}

	@Override
	public void init() throws SlickException {
		WorldObject background = new Static(Point.ZERO, WorldLayer.BACKGROUND,
				new Sprite(new Image("data/Desert.jpg"), Point.ZERO, 1)) {
			@Override
			protected void resetStaticState() {

			}
		};
		addObject(background);

		Actor gravityPlayer = new Player(new Point(3, 2));
		addObject(gravityPlayer);

		// Add some walls
		addObject(Wall.drawWall(new Point(2, 10), Dir.EAST, 10));
		addObject(Wall.drawWall(new Point(11, 9), Dir.EAST, 3));
		addObject(Wall.drawWall(new Point(0, 13), Dir.EAST, 5));
		
		// Add a Matt
		Matt matt = new Matt(new Point(12,7));
		addObject(matt);
	}
}
