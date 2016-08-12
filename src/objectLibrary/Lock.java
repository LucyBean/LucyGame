package objectLibrary;

import helpers.Point;
import objectLibs.SpriteBuilder;
import objects.Actor;
import objects.InteractBox;
import objects.Locker;
import worlds.WorldLayer;

public class Lock extends Locker {
	Key key;

	public Lock(Point origin, Key key, int keyID) {
		super(origin, WorldLayer.WORLD, SpriteBuilder.getLockImg(keyID), null,
				new InteractBox(
						SpriteBuilder.getLockImg(keyID).getBoundingRectangle()));
		this.key = key;
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
			if (p.has(key)) {
				p.removeFromInventory(key);
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
