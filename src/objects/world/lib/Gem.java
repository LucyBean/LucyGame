package objects.world.lib;

import helpers.Point;
import objects.world.ItemType;
import objects.world.PickUpItem;

public class Gem extends PickUpItem {
	
	public Gem(Point origin) {
		super(origin, ItemType.GEM);
	}

}
