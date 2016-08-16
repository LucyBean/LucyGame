package worldLibrary;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import helpers.Dir;
import helpers.Point;
import objectLibrary.Door;
import objectLibrary.Key;
import objectLibrary.Lock;
import objectLibrary.Player;
import objectLibrary.Wall;
import objects.Lockable;
import options.GlobalOptions;
import worlds.LucyGame;
import worlds.World;

public class MapImportingDemo extends World {
	Player player;
	List<Integer> usedKeyIDs;
	Map<Integer, Lock> locks;
	Map<Integer, List<Lockable>> lockables;
	boolean[][] checkedTiles;
	TiledMap tm;

	public MapImportingDemo(LucyGame game) {
		super(game, "Map importing demo");
	}

	@Override
	public void init() throws SlickException {
		buildMap();
		player = null;
	}

	private void buildMap() throws SlickException {
		tm = new TiledMap("data/exampleMap.tmx");
		lockables = new HashMap<Integer, List<Lockable>>();
		usedKeyIDs = new LinkedList<Integer>();
		locks = new HashMap<Integer, Lock>();
		checkedTiles = new boolean[tm.getHeight()][tm.getWidth()];

		for (int y = 0; y < tm.getHeight(); y++) {
			for (int x = 0; x < tm.getWidth(); x++) {
				if (!checkedTiles[y][x]) {
					Point pos = new Point(x, y);
					int blockID = getBlockID(pos);

					switch (blockID) {
						case 0: // Nothing
							break;
						case 1: // Wall
							addWall(pos);
							break;

						case 2: // Player spawn
							if (player == null) {
								player = new Player(pos);
								addObject(player);
								setCameraTarget(player);
							} else {
								player.setPosition(pos);
							}
							break;

						case 3: // Key
							addKey(pos, getKeyID(pos));
							break;

						case 4: // Lock
							addLock(pos, getKeyID(pos), getLockID(pos));
							break;

						case 5: // Door
							Door d = new Door(pos);
							addObject(d);
							registerLockable(d, getLockID(pos));
							break;

						default:
							System.err.println(
									"Unknown block ID " + blockID + " at " + pos
											+ " when importing map " + tm);
							break;
					}

					setChecked(pos);
				}
			}
		}

		linkLockables();

		if (GlobalOptions.debug()) {
			checkUnusedKeys();
			checkUnlockableLocks();
		}
	}

	private boolean validMapPoint(Point pos) {
		int x = (int) pos.getX();
		int y = (int) pos.getY();

		return !(x < 0 || x >= tm.getWidth() || y < 0 || y >= tm.getHeight());
	}

	/**
	 * Finds the BlockID for the tile at the given position in the map. The
	 * BlockID represents the specific WorldObject that should be created (e.g.
	 * Wall, Player, Door).
	 * 
	 * @param pos
	 *            The position for which the BlockID is required.
	 * @return The BlockID of the tile on layer 0 of the given TiledMap at the
	 *         given Point.
	 */
	private int getBlockID(Point pos) {
		if (validMapPoint(pos)) {
			int tileID = tm.getTileId((int) pos.getX(), (int) pos.getY(), 0);
			return Integer.parseInt(
					tm.getTileProperty(tileID, "block_id", "0"));
		} else {
			return 0;
		}
	}

	/**
	 * Finds the KeyID for the tile at the given position in the map. The KeyID
	 * represents the specific key/lock item that should be placed on the map.
	 * 
	 * @param pos
	 *            The position for which the KeyID is required.
	 * @return The KeyID of the tile on layer 0 of the given TiledMap at the
	 *         given Point.
	 */
	private int getKeyID(Point pos) {
		if (validMapPoint(pos)) {
			int tileID = tm.getTileId((int) pos.getX(), (int) pos.getY(), 0);
			return Integer.parseInt(tm.getTileProperty(tileID, "key_id", "0"));
		} else {
			return 0;
		}
	}

	/**
	 * Finds the LockID for the tile at the given position in the map. The
	 * LockID is used to link Lockers (e.g. Locks) to Lockables (e.g. Doors).
	 * 
	 * @param pos
	 *            The position for which the KeyID is required.
	 * @return The LockID of the tile on layer 1 of the given TiledMap at the
	 *         given Point.
	 */
	private int getLockID(Point pos) {
		if (validMapPoint(pos)) {
			int tileID = tm.getTileId((int) pos.getX(), (int) pos.getY(), 1);
			return Integer.parseInt(tm.getTileProperty(tileID, "lock_id", "0"));
		} else {
			return 0;
		}
	}

	private void setChecked(Point pos) {
		if (validMapPoint(pos)) {
			checkedTiles[(int) pos.getY()][(int) pos.getX()] = true;
		}
	}

	private void addWall(Point pos) {
		// Try to grow the Wall as far as possible to East then South
		Point nextEast = pos.move(Dir.EAST, 1);
		Point nextSouth = pos.move(Dir.SOUTH, 1);
		if (getBlockID(nextEast) == 1) {
			int width = 1;
			while (getBlockID(nextEast) == 1) {
				width++;
				setChecked(nextEast);
				nextEast = nextEast.move(Dir.EAST, 1);
			}
			addObject(new Wall(pos, width, 1));
		} else if (getBlockID(nextSouth) == 1) {
			int height = 1;
			while (getBlockID(nextSouth) == 1) {
				height++;
				setChecked(nextSouth);
				nextSouth = nextSouth.move(Dir.SOUTH, 1);
			}
			addObject(new Wall(pos, 1, height));
		} else {
			addObject(new Wall(pos, 1, 1));
		}
	}

	private void addKey(Point point, int keyID) {
		Key k = new Key(point, keyID);
		usedKeyIDs.add(keyID);
		addObject(k);
	}

	private void addLock(Point point, int keyID, int lockID) {
		Lock lock = new Lock(point, keyID);
		if (GlobalOptions.debug() && locks.get(lockID) != null) {
			System.err.println("Locks with identical lockID " + lockID);
		}
		if (lockID != 0) {
			locks.put(lockID, lock);
		} else if (GlobalOptions.debug()) {
			System.err.println("Lock with no lockID at " + point);
		}
		addObject(lock);
	}

	private void registerLockable(Lockable l, int lockID) {
		if (lockID != 0) {
			lockables.putIfAbsent(lockID, new LinkedList<Lockable>());
			lockables.get(lockID).add(l);
		} else if (GlobalOptions.debug()) {
			System.err.println("Lockable with no lockID");
		}
	}

	private void checkUnusedKeys() {
		for (int keyID : usedKeyIDs) {
			Lock lock = locks.get(keyID);
			if (lock == null) {
				System.err.println("Unused key with keyID " + keyID);
			}
		}
	}

	private void checkUnlockableLocks() {
		for (int keyID : locks.keySet()) {
			if (!usedKeyIDs.contains(keyID)) {
				System.err.println("Key-less lock with keyID " + keyID);
			}
		}
	}

	private void linkLockables() {
		for (int lockID : lockables.keySet()) {
			List<Lockable> ll = lockables.get(lockID);
			Lock l = locks.get(lockID);
			if (l != null) {
				ll.stream().forEach(s -> l.link(s));
			} else {
				if (GlobalOptions.debug()) {
					System.err.println(
							"Un-unlockable Lockables with lockID " + lockID);
				}
			}
		}
	}
}
