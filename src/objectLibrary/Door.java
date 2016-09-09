package objectLibrary;

import helpers.Point;
import objects.ItemType;
import objects.Lockable;
import objects.Static;
import worlds.WorldLayer;

public class Door extends Static implements Lockable {
	private int lockID;
	
	public Door(Point origin, int lockID) {
		super(origin, WorldLayer.WORLD, ItemType.DOOR_TOP);
		setColliderFromSprite();
		this.lockID = lockID;
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

	@Override
	public int getLockID() {
		return lockID;
	}

}
