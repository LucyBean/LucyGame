package objects.world.characters;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.attachments.InteractBox;
import objects.images.SpriteBuilder;
import objects.world.ItemType;

public class Matt extends NPC {
	public Matt(Point origin, int npcID) {
		super(origin, ItemType.MATT, SpriteBuilder.getCharacterSprite(1),
				null, new InteractBox(new Point(-1, 0), 3, 2), npcID);
		useGravity(false);
		getSprite().setOrigin(new Point(-0.25f, -0.5f));
	}

	@Override
	protected void resetActorState() {

	}

	@Override
	public void act(GameContainer gc, int delta) {

	}

	/*
	@Override
	public void interactedBy(Actor a) {
		super.interactedBy(a);
		if (a instanceof Player) {
			Player p = (Player) a;

			switch (state) {
				case 0:
					talk();
					Quest q = new Quest();
					q.add(new Objective("Get a gem!",
							(ei -> ei.getType() == EventType.PICK_UP
									&& ei.getSubject() instanceof Player
									&& ei.getObject() instanceof Gem)));
					q.add(new Objective("Talk to Matt!",
							(ei -> ei.getType() == EventType.INTERACT
									&& ei.getSubject() instanceof Player
									&& ei.getObject() instanceof Matt)));
					getWorld().startQuest(q);
					state = 1;
					break;
				case 1:
					if (!p.has(InventoryItem.getGem())) {
						talk();
					} else {
						state = 2;
						talk();
					}
					break;
				case 2:
					talk();
					break;
				default:
					break;
			}
		}
	}*/
}
