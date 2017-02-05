package worlds;

import helpers.Point;
import objects.world.ItemType;
import objects.world.ObjectMaker;
import objects.world.WorldObject;

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
	
	public ItemType getItemType() {
		return item;
	}

	private WorldObject makeObject(Point p) {
		int lockID = PainterProperty.getLockID().getValue();
		int npcID = PainterProperty.getNpcID().getValue();

		return ObjectMaker.makeFromType(item, p, lockID, npcID);
	}
}
