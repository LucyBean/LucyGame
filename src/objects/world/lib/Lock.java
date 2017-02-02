package objects.world.lib;

import helpers.Point;
import objects.world.Actor;
import objects.world.ItemType;
import objects.world.Locker;
import objects.world.characters.Player;
import worlds.WorldLayer;

public class Lock extends Locker {
	private int keyID;
	private ItemType unlockKey;
	
	private static ItemType keyIDtoItemType(int keyID) {
		if (keyID < 0 || keyID >= 4) {
			keyID = 1;
		}
		int n = (keyID - 1) + ItemType.YELLOW_LOCK.ordinal();
		return ItemType.values()[n];
	}

	public Lock(Point origin, int keyID, int lockID) {
		super(lockID, origin, WorldLayer.WORLD, keyIDtoItemType(keyID));
		this.keyID = keyID;
		unlockKey = Key.keyIDtoItemType(keyID);
	}
	
	@Override
	public int getKeyID() {
		return keyID;
	}

	@Override
	protected void resetStaticState() {
		
	}

	@Override
	protected boolean lockCheck(Actor a) {
		return true;
	}

	@Override
	protected boolean unlockCheck(Actor a) {
		if (a instanceof Player) {
			Player p = (Player) a;
			if (p.has(unlockKey)) {
				p.use(unlockKey);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	protected void lockAction() {
		enable();
	}

	@Override
	protected void unlockAction() {
		disable();
	}
	

}
