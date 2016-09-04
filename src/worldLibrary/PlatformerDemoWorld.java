package worldLibrary;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import characters.Conversation;
import characters.ConversationCharacter;
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
		
		Conversation c = new Conversation();
		
		String longText = "This is a very long string. I need an exceptionally long string " 
		+ "to test that my conversation maker is working. I am just going to chat "
		+ "to myself until I write enough crap to test it is working properly. "
		+ "Who'd have thought that something like this would be so hard!";
		
		c.add(ConversationCharacter.LUCY, longText);
		c.add(ConversationCharacter.MATT, "I like bean.");
		
		showConversation(c);
	}
}
