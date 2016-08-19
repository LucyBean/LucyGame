package player;

import java.util.HashMap;
import java.util.Map;

import images.Sprite;
import images.SpriteBuilder;

public class InventoryItem {
	String name;
	Sprite sprite;
	
	protected InventoryItem(String name, Sprite sprite) {
		this.name = name;
		this.sprite = sprite;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	private static InventoryItem gem;
	private static Map<Integer, InventoryItem> keys;
	
	public static InventoryItem getGem() {
		if (gem == null) {
			gem = new InventoryItem("Gem", SpriteBuilder.getGemImg());
		}
		
		return gem;
	}
	
	public static InventoryItem getKeyByID(int keyID) {
		if (keys == null) {
			keys = new HashMap<Integer, InventoryItem>();
		}
		
		InventoryItem key = keys.get(keyID);
		if (key == null) {
			key = new InventoryItem("Key " + keyID, SpriteBuilder.getKeyImg(keyID));
			keys.put(keyID, key);
		}
		
		return key;
	}
}
