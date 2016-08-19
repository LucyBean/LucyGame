package objectLibrary;

import helpers.Point;
import images.SpriteBuilder;
import objects.Actor;
import objects.InteractBox;
import objects.Locker;
import player.Player;
import worlds.WorldLayer;

public class Lock extends Locker {
	int keyID;

	public Lock(Point origin, int keyID) {
		super(origin, WorldLayer.WORLD, SpriteBuilder.getLockImg(keyID), null,
				new InteractBox(
						SpriteBuilder.getLockImg(keyID).getBoundingRectangle()));
		this.keyID = keyID;
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
			if (p.hasKey(keyID)) {
				p.useKey(keyID);
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
