package objects.world;

import java.util.List;

import helpers.Point;
import io.ErrorLogger;
import objects.world.characters.DogEnemy;
import objects.world.characters.Matt;
import objects.world.characters.Player;
import objects.world.lib.Door;
import objects.world.lib.Gem;
import objects.world.lib.Key;
import objects.world.lib.Lock;
import objects.world.lib.MovingPlatform;
import objects.world.lib.PushableBlock;
import objects.world.lib.Trampoline;
import objects.world.lib.Wall;
import worlds.World;

public class ObjectMaker {
	private ObjectMaker() {
	}

	/**
	 * Creates an object that requires multiple points in its constructor
	 * 
	 * @param itemType
	 * @param positions
	 * @return
	 */
	public static WorldObject makeFromType(ItemType itemType,
			List<Point> positions, int lockID, int npcID) {
		WorldObject wo = null;

		switch (itemType) {
			case MOVING_PLATFORM:
				if (positions.size() >= 2) {
					Point start = positions.remove(0);
					Point end = positions.remove(0);
					wo = new MovingPlatform(start, end, 3000) {
						// Prompt for the period
						@Override
						public void addedToWorld(World w) {
							super.addedToWorld(w);
							w.getInput(this, "Enter period (ms):");
						}

						@Override
						public void acceptInput(String s) {
							if (s.matches("\\d+")) {
								int period = Integer.parseInt(s);
								setPeriod(period);
							}
						}
					};
				}
				break;
			default:
				assert false;
				break;
		}

		return wo;
	}

	public static WorldObject makeFromType(ItemType itemType, Point position) {
		WorldObject wo = null;

		switch (itemType) {
			case WALL:
				wo = new Wall(position);
				break;
			case GEM:
				wo = new Gem(position);
				break;
			case YELLOW_KEY:
				wo = new Key(position, 1);
				break;
			case BLUE_KEY:
				wo = new Key(position, 2);
				break;
			case RED_KEY:
				wo = new Key(position, 3);
				break;
			case GREEN_KEY:
				wo = new Key(position, 4);
				break;
			case PLAYER:
				wo = new Player(position);
				break;
			case DOG_ENEMY:
				wo = new DogEnemy(position);
				break;
			case TRAMPOLINE:
				wo = new Trampoline(position);
				break;
			case PUSHABLE_BLOCK:
				wo = new PushableBlock(position);
				break;
			default:
				assert false;
				break;
		}

		if (wo == null) {
			ErrorLogger.log("Unable to create " + itemType
					+ " due to missing information", 3);
		}

		return wo;
	}

	public static WorldObject makeFromType(ItemType itemType, Point position,
			int someID) {
		WorldObject wo = null;

		switch (itemType) {
			case YELLOW_LOCK:
				wo = new Lock(position, 1, someID);
				break;
			case BLUE_LOCK:
				wo = new Lock(position, 2, someID);
				break;
			case RED_LOCK:
				wo = new Lock(position, 3, someID);
				break;
			case GREEN_LOCK:
				wo = new Lock(position, 4, someID);
				break;
			case DOOR:
				wo = new Door(position, someID);
				break;
			case MATT:
				wo = new Matt(position, someID);
				break;
			default:
				assert false;
				break;
		}

		if (wo == null) {
			ErrorLogger.log("Unable to create " + itemType
					+ " due to missing information", 3);
		}

		return wo;
	}

	public static WorldObject makeFromType(ItemType itemType, Point firstPoint,
			Point secondPoint, int val) {
		WorldObject wo = null;

		switch (itemType) {
			case MOVING_PLATFORM:
				wo = new MovingPlatform(firstPoint, secondPoint, val);
				break;
			default:
				assert false;
				break;
		}

		if (wo == null) {
			ErrorLogger.log("Unable to create " + itemType
					+ " due to missing information", 3);
		}

		return wo;
	}
	
	public static WorldObject makeFromType(ItemType itemType, Point point, float val) {
		WorldObject wo = null;

		switch (itemType) {
			case CLIMBING_WALL_MARKER:
				wo = new ClimbingWallMarker(point, val);
				break;
			default:
				assert false;
				break;
		}

		if (wo == null) {
			ErrorLogger.log("Unable to create " + itemType
					+ " due to missing information", 3);
		}

		return wo;
	}

	public static WorldObject makeFromType(ItemType itemType, Point position,
			int lockID, int npcID) {

		WorldObject wo = null;

		switch (itemType) {
			case WALL:
				wo = new Wall(position);
				break;
			case GEM:
				wo = new Gem(position);
				break;
			case YELLOW_KEY:
				wo = new Key(position, 1);
				break;
			case YELLOW_LOCK:
				wo = new Lock(position, 1, lockID);
				break;
			case BLUE_KEY:
				wo = new Key(position, 2);
				break;
			case BLUE_LOCK:
				wo = new Lock(position, 2, lockID);
				break;
			case RED_KEY:
				wo = new Key(position, 3);
				break;
			case RED_LOCK:
				wo = new Lock(position, 3, lockID);
				break;
			case GREEN_KEY:
				wo = new Key(position, 4);
				break;
			case GREEN_LOCK:
				wo = new Lock(position, 4, lockID);
				break;
			case DOOR:
				wo = new Door(position, lockID);
				break;
			case MATT:
				wo = new Matt(position, npcID);
				break;
			case PLAYER:
				wo = new Player(position);
				break;
			case DOG_ENEMY:
				wo = new DogEnemy(position);
				break;
			case TRAMPOLINE:
				wo = new Trampoline(position);
				break;
			case PUSHABLE_BLOCK:
				wo = new PushableBlock(position);
				break;
			default:
				break;
		}

		if (wo == null) {
			ErrorLogger.log("Unable to create " + itemType
					+ " due to missing information", 3);
		}

		return wo;
	}
}
