package player;

import java.util.TreeMap;

public class Inventory {
	TreeMap<InventoryItem, Integer> items;

	public Inventory() {
		items = new TreeMap<InventoryItem, Integer>();
	}

	public TreeMap<InventoryItem, Integer> getItems() {
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
	 * Adds a quantity of an InventoryItems to this Inventory.
	 * 
	 * @param ii
	 *            The InventoryItem to add.
	 * @param quantity
	 *            The amount to add.
	 */
	public void add(InventoryItem ii, int quantity) {
		int newAmount = items.getOrDefault(ii, 0) + quantity;

		if (newAmount <= 0) {
			items.remove(ii);
		} else {
			if (newAmount > 99) {
				newAmount = 99;
			}
			items.put(ii, newAmount);
		}
	}

	/**
	 * Adds one InventoryItem to this Inventory.
	 * 
	 * @param ii
	 *            The InventoryItem to add.
	 */
	public void add(InventoryItem ii) {
		add(ii, 1);
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
