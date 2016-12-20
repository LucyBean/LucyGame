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

}
