package objects.world;

import helpers.Point;
import objects.world.characters.Player;
import quests.EventInfo;
import quests.EventType;
import worlds.WorldLayer;

public class PickUpItem extends Static {
	
	public PickUpItem(Point origin, ItemType itemType) {
		super(origin, WorldLayer.WORLD, itemType);
		setInteractBoxFromSprite();
	}
	
	@Override
	protected void resetStaticState() {
		
	}
	
	@Override
	public void overlapStart(WorldObject wo) {
		if (wo instanceof Player) {
			Player p = (Player) wo;
			disable();
			p.addToInventory(getType());
			EventInfo ei = new EventInfo(EventType.PICK_UP, p, this);
			getWorld().signalEvent(ei);
		}
	}

}
