package objects.world.lib;

import org.newdawn.slick.GameContainer;

import helpers.Point;
import objects.world.ItemType;
import objects.world.Lockable;
import objects.world.Static;
import worlds.WorldLayer;

public class Door extends Static implements Lockable {
	private int lockID;
	private int transparency = 1000;
	private boolean unlocking = false;
	
	public Door(Point origin, int lockID) {
		super(origin, WorldLayer.WORLD, ItemType.DOOR);
		setColliderFromSprite();
		this.lockID = lockID;
		getCollider().setSolid(true);
	}

	@Override
	public void lock() {
		enable();
		transparency = 1000;
		unlocking = false;
	}

	@Override
	public void unlock() {
		unlocking = true;
	}

	@Override
	public int getLockID() {
		return lockID;
	}

	@Override
	protected void resetStaticState() {
		
	}

	@Override
	public void update(GameContainer gc, int delta) {
		if (unlocking) {
			transparency -= delta;
			if (transparency <= 0) {
				disable();
			} else {
				getSprite().setAlpha(transparency / 1000.0f);
			}
		}
	}

}
