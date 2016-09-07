package objectLibrary;

import helpers.Point;
import images.SpriteBuilder;
import objects.PickUpItem;
import player.InventoryItem;

public class Gem extends PickUpItem {
	
	public Gem(Point origin) {
		super(origin, SpriteBuilder.getGemImg(), InventoryItem.getGem());
	}

}
