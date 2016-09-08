package worlds;

import helpers.Point;
import objectLibrary.Gem;
import objectLibrary.Key;
import objectLibrary.Lock;
import objectLibrary.Wall;
import objects.ItemType;
import objects.WorldObject;

public class MapPainter {
	private Map map;
	private ItemType item;
	private int keyID = 1;

	public MapPainter(Map m) {
		map = m;
		if (ItemType.values().length > 0) {
			item = ItemType.values()[0];
		}
	}

	public void paint(Point p) {
		WorldObject wo = makeObject(p);
		if (wo != null) {
			map.addObject(wo);
		}
	}

	public void setItem(ItemType it) {
		item = it;
	}

	private WorldObject makeObject(Point p) {
		WorldObject wo;

		switch (item) {
			case WALL:
				wo = new Wall(p);
				break;
			case GEM:
				wo = new Gem(p);
				break;
			case YELLOW_KEY:
				wo = new Key(p, keyID);
				break;
			case YELLOW_LOCK:
				wo = new Lock(p, keyID);
				break;
			default:
				wo = null;
				break;
		}

		return wo;
	}
}
