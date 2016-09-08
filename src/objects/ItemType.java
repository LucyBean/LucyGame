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
		GREEN_LOCK;
	
	public static ItemType byId(int id) {
		if (id >= 0 && id < ItemType.values().length) {
			return ItemType.values()[id];
		} else {
			return null;
		}
	}
}
