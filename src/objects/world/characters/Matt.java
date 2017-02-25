package objects.world.characters;

import java.util.Optional;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.attachments.InteractBox;
import objects.images.SpriteBuilder;
import objects.world.ItemType;

public class Matt extends NPC {
	public Matt(Point origin, int npcID) {
		super(origin, ItemType.MATT, SpriteBuilder.getCharacterSprite(1),
				Optional.empty(), Optional.of(new InteractBox(new Point(-1, 0), 3, 2)), npcID);
		useGravity(false);
		getSprite().get().setOrigin(new Point(-0.25f, -0.5f));
	}

	@Override
	protected void resetActorState() {

	}

	@Override
	public void act(GameContainer gc, int delta) {
		
	}
}
