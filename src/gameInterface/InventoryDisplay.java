package gameInterface;

import java.util.Iterator;
import java.util.TreeMap;

import org.newdawn.slick.Color;

import helpers.Point;
import images.InventoryDisplaySprite;
import images.Sprite;
import player.Inventory;
import player.InventoryItem;

public class InventoryDisplay extends IEList {
	TreeMap<InventoryItem, Integer> items;

	InventoryItem minItem;
	InventoryItem maxItem;

	public InventoryDisplay(Point firstPoint, Inventory i) {
		super(firstPoint, 4, (() -> new InventoryDisplaySprite()),
				(s -> s.getImage().fillLayer(0, new Color(120, 120, 180))));
		this.items = i.getItems();
		updateSprites();
	}

	public InventoryDisplay(Point firstPoint) {
		super(firstPoint, 4, (() -> new InventoryDisplaySprite()),
				(s -> s.getImage().fillLayer(0, new Color(120, 120, 180))));
	}

	public void setInventory(Inventory i) {
		this.items = i.getItems();
		updateSprites();
	}

	@Override
	public void elementClicked(int elementIndex) {
		moveUp();
	}

	@Override
	public void getElementSprite(int elementIndex, Sprite s) {
		InventoryDisplaySprite ids = (InventoryDisplaySprite) s;
		
		if (items != null && elementIndex >= 0 && elementIndex < items.size()) {
			// There is information to display
			// Navigate to correct element
			Iterator<InventoryItem> iie = items.navigableKeySet().iterator();
			for (int i = 0; i < elementIndex; i++) {
				iie.next();
			}
			InventoryItem ii = iie.next();
			int quantity = items.get(ii);
			
			ids.setTo(ii, quantity);
			
		} else {
			// No information to display
			ids.setBlank();
		}
	}

	@Override
	public int getNumElements() {
		if (items == null) {
			return 0;
		} else {
			return items.size();
		}
	}
}
