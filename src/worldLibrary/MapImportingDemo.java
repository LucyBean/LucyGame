package worldLibrary;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import helpers.Pair;
import helpers.Point;
import objectLibrary.Key;
import objectLibrary.Lock;
import objectLibrary.Player;
import objectLibrary.Wall;
import options.GlobalOptions;
import worlds.LucyGame;
import worlds.World;

public class MapImportingDemo extends World {
	Player player;
	Map<Integer, Pair<Key, Boolean>> keys;

	public MapImportingDemo(LucyGame game) {
		super(game, "Map importing demo");
	}

	@Override
	public void init() throws SlickException {
		buildMap(new TiledMap("data/exampleMap.tmx"));
	}

	private void buildMap(TiledMap tm) {
		List<Pair<Point, Integer>> locksToAdd = new LinkedList<Pair<Point, Integer>>();
		keys = new HashMap<Integer, Pair<Key, Boolean>>();

		for (int y = 0; y < tm.getHeight(); y++) {
			for (int x = 0; x < tm.getWidth(); x++) {
				int tileID = tm.getTileId(x, y, 0);
				String s = tm.getTileProperty(tileID, "block_id", "0");
				int blockID = Integer.parseInt(s);
				int keyID = Integer.parseInt(
						tm.getTileProperty(tileID, "key_id", "0"));
				Point point = new Point(x, y);

				switch (blockID) {
					case 0: // Nothing
						break;
					case 1: // Wall
						addObject(new Wall(point, 1, 1));
						break;

					case 2: // Player spawn
						if (player == null) {
							player = new Player(point);
							addObject(player);
							setCameraTarget(player);
						} else {
							player.setPosition(point);
						}
						break;

					case 3: // Key
						addKey(point, keyID);
						break;

					case 4: // Lock
						// Adding locks should be deferred until the whole map
						// has been parsed.
						locksToAdd.add(new Pair<Point, Integer>(point, keyID));
						break;

					default:
						System.err.println("Unknown block ID " + blockID
								+ " at " + point + " when importing map " + tm);
						break;
				}
			}
		}

		addLocks(locksToAdd);
		if (GlobalOptions.debug()) {
			checkUnusedKeys();
		}
	}

	private void addKey(Point point, int keyID) {
		Key k = new Key(point, keyID);
		keys.put(keyID, new Pair<Key, Boolean>(k, false));
		addObject(k);
	}

	private void addLock(Point point, int keyID) {
		Pair<Key, Boolean> kp = keys.get(keyID);
		Lock lock;
		if (kp == null) {
			if (GlobalOptions.debug()) {
				System.err.println("Adding lock with unused keyID " + keyID
						+ " at " + point);
			}
			lock = new Lock(point, null, keyID);
		} else {
			Key k = kp.getFirst();
			kp.setSecond(true);
			lock = new Lock(point, k, keyID);
		}
		addObject(lock);
	}

	private void addLocks(List<Pair<Point, Integer>> locksToAdd) {
		locksToAdd.stream().forEach(s -> addLock(s.getFirst(), s.getSecond()));
	}
	
	private void checkUnusedKeys() {
		for (int i : keys.keySet()) {
			Pair<Key, Boolean> kp = keys.get(i);
			if (!kp.getSecond()) {
				System.err.println("Unused key with ID " + i);
			}
		}
	}
}
