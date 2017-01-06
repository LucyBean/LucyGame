package worlds.lib;

import org.newdawn.slick.SlickException;

import helpers.Dir;
import helpers.Point;
import objects.world.Actor;
import objects.world.characters.Matt;
import objects.world.characters.Player;
import objects.world.lib.Gem;
import objects.world.lib.Key;
import objects.world.lib.Wall;
import worlds.LucyGame;
import worlds.World;

/**
 * A demo of a basic platform game.
 * 
 * @author Lucy
 *
 */
public class QuestDemoWorld extends World {
	public QuestDemoWorld(LucyGame game) {
		super(game, "Platformer demo");
	}

	@Override
	public void init() throws SlickException {
		Actor player = new Player(new Point(3, 2));
		addObject(player);

		// Add some walls
		addObject(Wall.drawWall(new Point(2, 10), Dir.EAST, 20));
		addObject(Wall.drawWall(new Point(11, 9), Dir.EAST, 3));
		addObject(Wall.drawWall(new Point(0, 13), Dir.EAST, 5));
		
		// Add a Matt
		Matt matt = new Matt(new Point(12,7), 1);
		addObject(matt);
		
		// Add a key
		Key k = new Key(new Point(4,12), 1);
		addObject(k);
		
		// Add a gem
		Gem g = new Gem(new Point(18,8));
		addObject(g);
		
		// Load conversations for this level
		loadConversations("level4");
		
	}
}
