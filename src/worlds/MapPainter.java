package worlds;

import helpers.Point;
import objectLibrary.Door;
import objectLibrary.Gem;
import objectLibrary.Key;
import objectLibrary.Lock;
import objectLibrary.Wall;
import objects.ItemType;
import objects.WorldObject;

public class MapPainter {
	private WorldMap map;
	private ItemType item;

	public MapPainter(WorldMap m) {
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
		int lockID = PainterProperty.getLockID().getValue();

		switch (item) {
			case WALL:
				wo = new Wall(p);
				break;
			case GEM:
				wo = new Gem(p);
				break;
			case YELLOW_KEY:
				wo = new Key(p, 1);
				break;
			case YELLOW_LOCK:
				wo = new Lock(p, 1, lockID);
				PainterProperty.getLockID().increment();
				break;
			case BLUE_KEY:
				wo = new Key(p, 2);
				break;
			case BLUE_LOCK:
				wo = new Lock(p, 2, lockID);
				PainterProperty.getLockID().increment();
				break;
			case RED_KEY:
				wo = new Key(p,3);
				break;
			case RED_LOCK:
				wo = new Lock(p,3, lockID);
				PainterProperty.getLockID().increment();
				break;
			case GREEN_KEY:
				wo = new Key(p,4);
				break;
			case GREEN_LOCK:
				wo = new Lock(p,4, lockID);
				PainterProperty.getLockID().increment();
				break;
			case DOOR_BTM:
				wo = new Door(p, lockID);
				break;
			case DOOR_TOP:
				wo = new Door(p, lockID);
				break;
			default:
				wo = null;
				break;
		}

		return wo;
	}
}
