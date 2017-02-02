package objects.world.characters;

import java.util.TreeMap;

import objects.world.ItemType;

public class Inventory extends TreeMap<ItemType, Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4108883944655371134L;

	/**
	 * Checks whether this Inventory contains this InventoryItem.
	 * 
	 * @param ii
	 * @return
	 */
	public boolean has(ItemType ii) {
		return has(ii, 1);
	}
	
	public boolean has(ItemType ii, int quantity) {
		Integer n = get(ii);
		
		return n != null && n >= quantity;
	}

	/**
	 * Adds a quantity of an InventoryItems to this Inventory.
	 * 
	 * @param ii
	 *            The InventoryItem to add.
	 * @param quantity
	 *            The amount to add.
	 */
	public void add(ItemType ii, int quantity) {
		int newAmount = getOrDefault(ii, 0) + quantity;

		if (newAmount <= 0) {
			remove(ii);
		} else {
			if (newAmount > 99) {
				newAmount = 99;
			}
			put(ii, newAmount);
		}
	}

	/**
	 * Adds one InventoryItem to this Inventory.
	 * 
	 * @param ii
	 *            The InventoryItem to add.
	 */
	public void add(ItemType ii) {
		add(ii, 1);
	}

	/**
	 * Removes one of the InventoryItem from this Inventory, if it is in this
	 * Inventory.
	 * 
	 * @param ii
	 */
	public void remove(ItemType ii, int amount) {
		Integer n = get(ii);

		if (n != null && n > 0) {
			int newAmount = n - amount;
			if (newAmount > 0) {
				put(ii, newAmount);
			} else {
				remove(ii);
			}
		}
	}
}
