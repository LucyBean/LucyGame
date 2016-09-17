package objectLibrary;

import helpers.Point;
import objects.ItemType;
import objects.PickUpItem;
import player.InventoryItem;

public class Key extends PickUpItem {
	int keyID;

	private static ItemType keyIDtoItemType(int keyID) {
		if (keyID < 0) {
			keyID = 1;
		}
		else if (keyID > 4) {
			keyID = (keyID % 4) + 1;
		}
		int n = (keyID - 1) + ItemType.YELLOW_KEY.ordinal();
		return ItemType.values()[n];
	}

	public Key(Point origin, int keyID) {
		super(origin, keyIDtoItemType(keyID), InventoryItem.getKeyByID(keyID));
		setInteractBoxFromSprite();
	}

	@Override
	public int getKeyID() {
		return keyID;
	}
}
