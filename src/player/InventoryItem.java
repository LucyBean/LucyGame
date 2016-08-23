package player;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;

import images.ImageBuilder;

public class InventoryItem implements Comparable {
	String name;
	Image img;
	
	protected InventoryItem(String name, Image img) {
		this.name = name;
		this.img = img;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public Image getImage() {
		return img;
	}
	
	public String getName() {
		return name;
	}
	
	private static InventoryItem gem;
	private static Map<Integer, InventoryItem> keys;
	
	public static InventoryItem getGem() {
		if (gem == null) {
			gem = new InventoryItem("Gem", ImageBuilder.getGemImg());
		}
		
		return gem;
	}
	
	public static InventoryItem getKeyByID(int keyID) {
		if (keys == null) {
			keys = new HashMap<Integer, InventoryItem>();
		}
		
		InventoryItem key = keys.get(keyID);
		if (key == null) {
			key = new InventoryItem("Key " + keyID, ImageBuilder.getKeyImg(keyID));
			keys.put(keyID, key);
		}
		
		return key;
	}

	@Override
	public int compareTo(Object arg0) {
		InventoryItem other = (InventoryItem) arg0;
		return getName().compareTo(other.getName());
	}
}
