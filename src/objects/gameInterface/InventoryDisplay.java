package objects.gameInterface;

import java.util.Iterator;

import helpers.Point;
import objects.images.InventoryDisplaySprite;
import objects.images.SingleSprite;
import objects.images.Sprite;
import objects.world.ItemType;
import objects.world.characters.Inventory;

public class InventoryDisplay extends IEList {
	Inventory inventory;

	public InventoryDisplay(Point firstPoint, Inventory i) {
		super(firstPoint, 4, 1, 4, (s -> s.getImage().fillLayer(0, 2)));
		if (i != null) {
			inventory = i;
			updateSprites();
		}
	}

	public InventoryDisplay(Point firstPoint) {
		this(firstPoint, null);
	}

	public void setInventory(Inventory i) {
		inventory = i;
		updateSprites();
	}

	@Override
	protected void elementClicked(int elementIndex) {

	}

	@Override
	protected void getElementSprite(int elementIndex, Sprite s) {
		InventoryDisplaySprite ids = (InventoryDisplaySprite) s;

		if (inventory != null && elementIndex >= 0
				&& elementIndex < inventory.size()) {
			// There is information to display
			// Navigate to correct element
			Iterator<ItemType> iie = inventory.navigableKeySet().iterator();
			for (int i = 0; i < elementIndex; i++) {
				iie.next();
			}
			ItemType ii = iie.next();
			int quantity = inventory.get(ii);

			ids.setTo(ii, quantity);

		} else {
			// No information to display
			ids.setBlank();
		}
	}

	@Override
	protected int getNumElements() {
		if (inventory == null) {
			return 0;
		} else {
			return inventory.size();
		}
	}

	@Override
	protected SingleSprite makeNewSprite() {
		return new InventoryDisplaySprite();
	}
}
