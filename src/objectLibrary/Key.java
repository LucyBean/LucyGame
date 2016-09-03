package objectLibrary;

import helpers.Point;
import images.SpriteBuilder;
import objects.PickUpItem;
import objects.characters.player.InventoryItem;

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
