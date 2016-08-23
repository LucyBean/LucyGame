package gameInterface;

import java.util.TreeMap;

import helpers.Point;
import player.InventoryItem;

public class InventoryDisplay extends IEList<InventoryItemDisplayer> {
	int currentIndex;
	TreeMap<InventoryItem, Integer> items;
	final int size = 4;

	InventoryItem minItem;
	InventoryItem maxItem;

	InventoryItemDisplayer[] itemDisplayers = new InventoryItemDisplayer[size];

	public InventoryDisplay(Point firstPoint,
			TreeMap<InventoryItem, Integer> items) {
		super(firstPoint);
		this.items = items;
		currentIndex = 0;

		for (int i = 0; i < size; i++) {
			itemDisplayers[i] = new InventoryItemDisplayer();

			if (i == 0 && !items.isEmpty()) {
				minItem = items.firstKey();
				maxItem = items.firstKey();
			} else if (maxItem != null) {
				maxItem = items.higherKey(maxItem);
			}

			if (maxItem != null) {
				itemDisplayers[i].display(maxItem, items.get(maxItem));
			} else {
				itemDisplayers[i].displayBlank();
			}
			add(itemDisplayers[i]);
		}
	}

	public void showNext() {

	}

}
