package objectLibrary;

import helpers.Point;
import objects.ItemType;
import objects.Lockable;
import objects.Static;
import worlds.WorldLayer;

public class Door extends Static implements Lockable {
	public Door(Point origin) {
		super(origin, WorldLayer.WORLD, ItemType.DOOR_TOP);
		setColliderFromSprite();
	}

	@Override
	public void lock() {
		enable();
	}

	@Override
	public void unlock() {
		disable();
	}

	@Override
	protected void resetStaticState() {
		
	}

}
