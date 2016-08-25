package gameInterface;

import java.util.TreeMap;

import helpers.Point;
import player.InventoryItem;

public class InventoryDisplay extends IEList<InventoryItemDisplayer> {
	TreeMap<InventoryItem, Integer> items;
	final int displaySize = 4;

	InventoryItem minItem;
	InventoryItem maxItem;

	InventoryItemDisplayer[] itemDisplayers = new InventoryItemDisplayer[displaySize];

	public InventoryDisplay(Point firstPoint,
			TreeMap<InventoryItem, Integer> items) {
		super(firstPoint);
		this.items = items;

		for (int i = 0; i < displaySize; i++) {
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

		// set the first object to be selected if it exists
		if (minItem != null) {
			itemDisplayers[0].setSelectedBackground();
		}
	}

}
