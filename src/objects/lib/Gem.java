package objects.lib;

import helpers.Point;
import objects.world.ItemType;
import objects.world.PickUpItem;
import objects.world.characters.InventoryItem;

public class Gem extends PickUpItem {
	
	public Gem(Point origin) {
		super(origin, ItemType.GEM, InventoryItem.getGem());
	}

}
