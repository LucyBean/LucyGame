package objects;

import java.util.LinkedList;
import java.util.List;

import helpers.Point;
import worlds.WorldLayer;

public abstract class Locker extends Static {
	List<Lockable> linkedItems;
	boolean locked;

	public Locker(Point origin, WorldLayer layer, Sprite sprite,
			Collider collider, InteractBox interactBox) {
		super(origin, layer, sprite, collider, interactBox);
		linkedItems = new LinkedList<Lockable>();
		lock();
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void link(Lockable k) {
		linkedItems.add(k);
		
		if (locked) {
			k.lock();
		} else {
			k.unlock();
		}
	}

	/**
	 * Locks this Locker and all linked items.
	 * 
	 * @param a
	 *            The Actor initiating the action.
	 */
	public final void lock(Actor a) {
		if (lockCheck(a)) {
			lock();
		}
	}

	/**
	 * Locks this Locker and all linked items.
	 */
	private final void lock() {
		lockAction();
		linkedItems.stream().forEach(b -> b.lock());
		locked = true;
	}

	/**
	 * Unlocks this Locker and all linked items. This will allow validity checks
	 * to be performed on the initiating Actor.
	 * 
	 * @param a
	 *            The Actor initiating the action.
	 */
	public final void unlock(Actor a) {
		if (unlockCheck(a)) {
			unlock();
		}
	}

	/**
	 * Unlocks this Locker and all linked items.
	 */
	private final void unlock() {
		unlockAction();
		linkedItems.stream().forEach(b -> b.unlock());
		locked = false;
	}

	/**
	 * The Locker will be locked if this returns true. Use this to apply
	 * validity checks (e.g. "Player has required key")
	 * 
	 * @param a
	 *            The Actor initiating the lock action.
	 * @return Whether or not the Locker should be locked.
	 */
	protected abstract boolean lockCheck(Actor a);

	/**
	 * The Locker will be unlocked if this returns true. Use this to apply
	 * validity checks (e.g. "Player has required key")
	 * 
	 * @param a
	 *            The Actor initiating the unlock action.
	 * @return Whether or not the Locker should be unlocked.
	 */
	protected abstract boolean unlockCheck(Actor a);

	/**
	 * The action applied to this Locker upon locking. Use this to change image.
	 */
	protected abstract void lockAction();

	/**
	 * The action applied to this Locker upon unlocking. Use this to change
	 * image.
	 */
	protected abstract void unlockAction();
}
