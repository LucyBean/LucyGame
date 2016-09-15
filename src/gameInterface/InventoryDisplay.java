package gameInterface;

import java.util.Iterator;

import org.newdawn.slick.Color;

import helpers.Point;
import images.InventoryDisplaySprite;
import images.Sprite;
import player.Inventory;
import player.InventoryItem;

public class InventoryDisplay extends IEList {
	Inventory inventory;

	InventoryItem minItem;
	InventoryItem maxItem;

	public InventoryDisplay(Point firstPoint, Inventory i) {
		super(firstPoint, 4, 1, 4,
				(s -> s.getImage().fillLayer(0, new Color(120, 120, 180))));
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
			Iterator<InventoryItem> iie = inventory.navigableKeySet().iterator();
			for (int i = 0; i < elementIndex; i++) {
				iie.next();
			}
			InventoryItem ii = iie.next();
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
	protected Sprite makeNewSprite() {
		return new InventoryDisplaySprite();
	}
}
