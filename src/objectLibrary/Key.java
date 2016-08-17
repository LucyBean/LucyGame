package objectLibrary;

import helpers.Point;
import inventoryItems.InventoryItem;
import objectLibs.SpriteBuilder;
import objects.PickUpItem;

public class Key extends PickUpItem {
	int keyID;
	
	public Key(Point origin, int keyID) {
		super(origin, SpriteBuilder.getKeyImg(keyID), InventoryItem.getKeyByID(keyID));
		this.keyID = keyID;
	}
	
	public int getKeyID() {
		return keyID;
	}
}
