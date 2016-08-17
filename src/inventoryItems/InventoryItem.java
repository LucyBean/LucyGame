package inventoryItems;

import java.util.HashMap;
import java.util.Map;

public class InventoryItem {
	String name;
	
	protected InventoryItem(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	private static InventoryItem gem;
	private static Map<Integer, InventoryItem> keys;
	
	public static InventoryItem getGem() {
		if (gem == null) {
			gem = new InventoryItem("Gem");
		}
		
		return gem;
	}
	
	public static InventoryItem getKeyByID(int keyID) {
		if (keys == null) {
			keys = new HashMap<Integer, InventoryItem>();
		}
		
		InventoryItem key = keys.get(keyID);
		if (key == null) {
			key = new InventoryItem("Key " + keyID);
			keys.put(keyID, key);
		}
		
		return key;
	}
}
