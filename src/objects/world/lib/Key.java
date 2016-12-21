package objects.world.lib;

import helpers.Point;
import objects.world.ItemType;
import objects.world.PickUpItem;
import objects.world.characters.InventoryItem;

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
