package objects.world.characters;

import java.util.HashMap;
import java.util.Map;

import helpers.Point;
import objects.attachments.Collider;
import objects.attachments.InteractBox;
import objects.images.Sprite;
import objects.world.Actor;
import objects.world.ItemType;
import quests.Quest;
import worlds.WorldLayer;

public abstract class NPC extends Actor {
	private int npcID;
	private int storyState = 0;
	private ConversationSet conversations;
	private Map<Integer, Quest> quests;
	private static Conversation defaultChat = makeDefaultChat();

	private static Conversation makeDefaultChat() {
		Conversation c = new Conversation();
		c.add(null, "I have nothing to say to you.");
		return c;
	}

	public NPC(Point origin, ItemType itemType, Sprite sprite, Collider collider, InteractBox interactBox, int npcID) {
		super(origin, WorldLayer.WORLD, itemType, sprite, collider, interactBox);
		this.npcID = npcID;
		quests = new HashMap<>();
	}

	@Override
	public int getNPCID() {
		return npcID;
	}

	protected void setStoryState(int state) {
		storyState = state;
	}

	/**
	 * Shows the required conversation on screen for the current state.
	 */
	protected void talk() {
		if (conversations != null) {
			Conversation c = conversations.get(storyState);
			if (c != null) {
				getWorld().showConversation(c);
			}
			setStoryState(c.getEndState());
		} else {
			getWorld().showConversation(defaultChat);
		}
	}

	public void interactedBy(Actor a) {
		// Get the quest and maybe start it
		Quest q = quests.get(storyState);
		if (q != null) {
			q.start();
		}
		talk();
		
	}

	public void setConversations(ConversationSet cs) {
		conversations = cs;
	}

	/**
	 * Registers a quest. Talking to this NPC while in the required state will
	 * cause the quest to start.
	 * 
	 * @param q
	 * @param state
	 */
	public void registerQuest(int state, Quest q) {
		quests.put(state, q);
	}

	@Override
	public String getInfo() {
		String info = super.getInfo();
		info += "NPC ID: " + npcID + "\n";
		return info;
	}

}
