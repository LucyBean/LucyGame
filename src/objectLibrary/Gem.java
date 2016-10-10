package objectLibrary;

import helpers.Point;
import objects.ItemType;
import objects.PickUpItem;
import player.InventoryItem;

public class Gem extends PickUpItem {
	
	public Gem(Point origin) {
		super(origin, ItemType.GEM, InventoryItem.getGem());
	}

}
