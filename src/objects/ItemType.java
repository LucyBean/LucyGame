package objects;

public enum ItemType {
	WALL,
		GEM,
		YELLOW_KEY,
		BLUE_KEY,
		RED_KEY,
		GREEN_KEY,
		YELLOW_LOCK,
		BLUE_LOCK,
		RED_LOCK,
		GREEN_LOCK,
		DOOR_TOP,
		DOOR_BTM(DOOR_TOP);
	
	ItemType parent;
	
	ItemType(ItemType parent) {
		this.parent = parent;
	}
	
	ItemType() {
		this(null);
	}
	
	public boolean isPaintable() {
		return parent == null;
	}
	
	public ItemType getParent() {
		return parent;
	}
	
	public static ItemType byId(int id) {
		if (id >= 0 && id < ItemType.values().length) {
			return ItemType.values()[id];
		} else {
			return null;
		}
	}
}
