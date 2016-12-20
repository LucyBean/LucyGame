package objects.world;

import helpers.Point;
import objects.world.characters.InventoryItem;
import objects.world.characters.Player;
import quests.EventInfo;
import quests.EventType;
import worlds.WorldLayer;

public class PickUpItem extends Static {
	InventoryItem inventoryItem;
	
	public PickUpItem(Point origin, ItemType itemType, InventoryItem inventoryItem) {
		super(origin, WorldLayer.WORLD, itemType);
		this.inventoryItem = inventoryItem;
		setInteractBoxFromSprite();
	}
	
	public InventoryItem getInventoryItem() {
		return inventoryItem;
	}

	@Override
	protected void resetStaticState() {
		
	}
	
	@Override
	public void overlapStart(WorldObject wo) {
		if (wo instanceof Player) {
			Player p = (Player) wo;
			disable();
			p.addToInventory(inventoryItem);
			EventInfo ei = new EventInfo(EventType.PICK_UP, p, this);
			getWorld().signalEvent(ei);
		}
	}

}
