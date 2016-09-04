package characters;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import images.SpriteBuilder;
import objects.Actor;
import objects.InteractBox;
import player.Player;
import worlds.WorldLayer;

public class Matt extends Actor {
	public Matt(Point origin) {
		super(origin, WorldLayer.WORLD, SpriteBuilder.getCharacterSprite(1),
				null,
				new InteractBox(new Point(-1, 0), 3, 2));
		useGravity(false);
	}

	@Override
	protected void resetActorState() {
		
	}

	@Override
	public void act(GameContainer gc, int delta) {
		
	}
	
	@Override
	public void interactedBy(Actor a) {
		if (a instanceof Player) {
			Conversation c = new Conversation();
			
			ConversationCharacter lucy = ConversationCharacter.LUCY;
			ConversationCharacter matt = ConversationCharacter.MATT;
			c.add(lucy, "Bby?");
			c.add(matt, "Ys?");
			c.add(lucy, "Cuddle");
			c.add(matt, "Not again");
			c.add(lucy, "Ys!");
			
			getWorld().showConversation(c);
		}
	}
}
