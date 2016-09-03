package objects;

import helpers.Point;
import images.Sprite;
import objects.characters.player.InventoryItem;
import worlds.WorldLayer;

public class PickUpItem extends Static {
	InventoryItem inventoryItem;
	
	public PickUpItem(Point origin, Sprite sprite, InventoryItem inventoryItem) {
		super(origin, WorldLayer.WORLD, sprite, null,
				new InteractBox(sprite.getBoundingRectangle()));
		this.inventoryItem = inventoryItem;
	}
	
	public InventoryItem getInventoryItem() {
		return inventoryItem;
	}

	@Override
	protected void resetStaticState() {
		
	}

}
