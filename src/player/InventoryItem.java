package player;

import java.util.HashMap;
import java.util.Map;

import images.ImageBuilder;
import images.LucyImage;
import objects.ItemType;

public class InventoryItem implements Comparable<InventoryItem> {
	String name;
	LucyImage img;
	
	protected InventoryItem(String name, LucyImage img) {
		this.name = name;
		this.img = img;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public LucyImage getImage() {
		return img;
	}
	
	public String getName() {
		return name;
	}
	
	private static InventoryItem gem;
	private static Map<Integer, InventoryItem> keys;
	
	public static InventoryItem getGem() {
		if (gem == null) {
			gem = new InventoryItem("Gem", ImageBuilder.getItemImage(ItemType.GEM));
		}
		
		return gem;
	}
	
	public static InventoryItem getKeyByID(int keyID) {
		if (keys == null) {
			keys = new HashMap<Integer, InventoryItem>();
		}
		
		if (keyID < 0) {
			keyID = 0;
		}
		
		InventoryItem key = keys.get(keyID);
		if (key == null) {
			key = new InventoryItem("Key " + keyID, ImageBuilder.getKeyImg(keyID));
			keys.put(keyID, key);
		}
		
		return key;
	}

	@Override
	public int compareTo(InventoryItem other) {
		return getName().compareTo(other.getName());
	}
}
