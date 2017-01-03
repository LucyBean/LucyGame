package objects.world.characters;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.attachments.InteractBox;
import objects.images.SpriteBuilder;
import objects.world.Actor;
import objects.world.ItemType;
import objects.world.lib.Gem;
import quests.EventType;
import quests.Objective;
import quests.Quest;

public class Matt extends NPC {
	private Map<Integer, Conversation> conversations;
	private int state = 0;

	public Matt(Point origin, int npcID) {
		super(origin, ItemType.MATT, SpriteBuilder.getCharacterSprite(1),
				null, new InteractBox(new Point(-1, 0), 3, 2), npcID);
		useGravity(false);
		getSprite().setOrigin(new Point(-0.25f, -0.5f));

		buildConversations();
	}

	private void buildConversations() {
		conversations = new HashMap<>();
		ConversationCharacter lucy = ConversationCharacter.LUCY;
		ConversationCharacter matt = ConversationCharacter.MATT;

		Conversation initial = new Conversation();
		initial.add(lucy, "Bby?");
		initial.add(matt, "Ys?");
		initial.add(lucy, "Cuddle");
		initial.add(matt, "Not again");
		initial.add(lucy, "Ys!");
		initial.add(matt, "Give me a gem to get a cuddle!");
		conversations.put(0, initial);

		Conversation middle = new Conversation();
		middle.add(matt, "Give me a gem to get a cuddle!");
		conversations.put(1, middle);

		Conversation end = new Conversation();
		end.add(matt, "Thank you for the gem! Here is your cuddle!");
		end.add(lucy, "Yay!");
		conversations.put(2, end);

	}

	@Override
	protected void resetActorState() {

	}

	@Override
	public void act(GameContainer gc, int delta) {

	}

	/**
	 * Shows the required conversation on screen for the current state.
	 */
	private void talk() {
		Conversation c = conversations.get(state);
		if (c != null) {
			getWorld().showConversation(c);
		}
	}

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
	}
}
