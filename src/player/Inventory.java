package player;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
	Map<InventoryItem, Integer> items;

	public Inventory() {
		items = new HashMap<InventoryItem, Integer>();
	}
	
	public Map<InventoryItem, Integer> getItems() {
		return items;
	}

	/**
	 * Checks whether this Inventory contains this InventoryItem.
	 * 
	 * @param ii
	 * @return
	 */
	public boolean has(InventoryItem ii) {
		Integer n = items.get(ii);

		return n != null && n > 0;
	}

	/**
	 * Adds an InventoryItem to this Inventory.
	 * 
	 * @param ii
	 */
	public void add(InventoryItem ii) {
		items.putIfAbsent(ii, 0);
		int newAmount = items.get(ii) + 1;
		items.put(ii, newAmount);
	}

	/**
	 * Removes one of the InventoryItem from this Inventory, if it is in this
	 * Inventory.
	 * 
	 * @param ii
	 */
	public void removeOne(InventoryItem ii) {
		Integer n = items.get(ii);

		if (n != null && n > 0) {
			int newAmount = n - 1;
			if (newAmount > 0) {
				items.put(ii, newAmount);
			} else {
				items.remove(ii);
			}
		}
	}

	@Override
	public String toString() {
		return items.toString();
	}
}
