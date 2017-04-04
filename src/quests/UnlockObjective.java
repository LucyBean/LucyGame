package quests;

import objects.world.Locker;
import objects.world.WorldObject;
import objects.world.characters.Player;
import worlds.World;

public class UnlockObjective extends Objective {
	private int lockID;
	
	public UnlockObjective(int lockID) {
		super("Unlock lock " + lockID);
		this.lockID = lockID;
	}

	@Override
	protected boolean eventSatisfy(EventInfo ei) {
		if (ei.getType() == EventType.UNLOCK && ei.getSubject() instanceof Player) {
			WorldObject obj = ei.getObject();
			assert obj instanceof Locker;
			Locker lock = (Locker) obj;
			return lock.getLockID() == lockID;
		}
		return false;
	}

	@Override
	protected boolean initSatisfy(World w) {
		// TODO: FIX!
		return false;
	}

}
