package objectLibrary;

import helpers.Point;
import images.SpriteBuilder;
import objects.ItemType;
import objects.PickUpItem;
import player.InventoryItem;

public class Gem extends PickUpItem {
	
	public Gem(Point origin) {
		super(origin, SpriteBuilder.getWorldItem(ItemType.GEM), InventoryItem.getGem());
	}

}
