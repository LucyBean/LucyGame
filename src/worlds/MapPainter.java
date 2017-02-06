package worlds;

import java.util.ArrayList;
import java.util.List;

import helpers.Point;
import objects.world.ItemType;
import objects.world.ObjectMaker;
import objects.world.WorldObject;

public class MapPainter {
	private WorldMap map;
	private ItemType item;
	private List<Point> tempPoints;

	public MapPainter(WorldMap m) {
		map = m;
		if (ItemType.values().length > 0) {
			item = ItemType.values()[0];
		}
	}

	public void paint(Point p) {
		if (tempPoints != null) {
			tempPoints.add(p);
		}
		WorldObject wo = makeObject(p);
		if (wo != null) {
			map.addObject(wo);
		}
	}

	public void setItem(ItemType it) {
		if (it.isMultiClick()) {
			tempPoints = new ArrayList<>();
		}
		item = it;
	}

	public ItemType getItemType() {
		return item;
	}

	private WorldObject makeObject(Point p) {
		int lockID = PainterProperty.getLockID().getValue();
		int npcID = PainterProperty.getNpcID().getValue();

		if (item.isMultiClick()) {
			return ObjectMaker.makeFromType(item, tempPoints, lockID, npcID);
		} else {
			return ObjectMaker.makeFromType(item, p, lockID, npcID);
		}
	}
}
