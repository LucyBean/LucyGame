package characters;

import helpers.Point;
import images.Sprite;
import objects.Actor;
import objects.Collider;
import objects.InteractBox;
import objects.ItemType;
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
