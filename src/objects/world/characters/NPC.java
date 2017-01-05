package objects.world.characters;

import helpers.Point;
import objects.attachments.Collider;
import objects.attachments.InteractBox;
import objects.images.Sprite;
import objects.world.Actor;
import objects.world.ItemType;
import worlds.WorldLayer;

public abstract class NPC extends Actor {
	private int npcID;
	private int storyState = 0;
	private ConversationSet conversations;

	public NPC(Point origin, ItemType itemType, Sprite sprite,
			Collider collider, InteractBox interactBox, int npcID) {
		super(origin, WorldLayer.WORLD, itemType, sprite, collider,
				interactBox);
		this.npcID = npcID;
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
		Conversation c = conversations.get(storyState);
		if (c != null) {
			getWorld().showConversation(c);
		}
		setStoryState(c.getEndState());
	}
	
	public void interactedBy(Actor a) {
		talk();
	}
	
	public void setConversations(ConversationSet cs) {
		conversations = cs;
	}


}
