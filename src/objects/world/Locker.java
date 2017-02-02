package objects.world;

import java.util.stream.Stream;

import helpers.Point;
import objects.attachments.Collider;
import objects.attachments.InteractBox;
import objects.images.Sprite;
import objects.world.characters.Player;
import worlds.World;
import worlds.WorldLayer;

public abstract class Locker extends Static {
	private int lockID;
	private boolean locked;

	/**
	 * Creates a new Locker. This will start in the Locked state by default.
	 * 
	 * @param lockID
	 *            The ID of the lock. This is used to link Lockable objects to
	 *            their Locker.
	 * @param origin
	 *            The position of the Locker.
	 * @param layer
	 *            The WorldLayer for the Locker.
	 * @param sprite
	 * @param collider
	 * @param interactBox
	 */
	public Locker(int lockID, Point origin, WorldLayer layer, ItemType itemType,
			Sprite sprite, Collider collider, InteractBox interactBox) {
		super(origin, layer, itemType, sprite, collider, interactBox);
		this.lockID = lockID;
	}

	public Locker(int lockID, Point origin, WorldLayer layer,
			ItemType itemType) {
		this(lockID, origin, layer, itemType,
				itemType.getSprite(), null, null);
		setInteractBoxFromSprite();
	}

	@Override
	public void addedToWorld(World w) {
		lock();
	}

	public boolean isLocked() {
		return locked;
	}

	@Override
	public int getLockID() {
		return lockID;
	}

	@Override
	public void overlapStart(WorldObject wo) {
		if (wo instanceof Player) {
			Player p = (Player) wo;
			unlock(p);
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
		Stream<Lockable> linkedItems = getWorld().getMap().getLockablesByID(
				lockID);
		if (linkedItems != null) {
			linkedItems.forEach(b -> b.lock());
		}
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
		Stream<Lockable> linkedItems = getWorld().getMap().getLockablesByID(
				lockID);
		if (linkedItems != null) {
			linkedItems.forEach(b -> b.unlock());
		}
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
	
	@Override
	public String getInfo() {
		String info = super.getInfo();
		info += "Lock ID: " + lockID + "\n";
		return info;
	}
}
