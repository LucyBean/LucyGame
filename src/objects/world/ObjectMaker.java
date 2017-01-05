package objects.world;

import helpers.Point;
import objects.world.characters.DogEnemy;
import objects.world.characters.Matt;
import objects.world.characters.Player;
import objects.world.lib.Door;
import objects.world.lib.Gem;
import objects.world.lib.Key;
import objects.world.lib.Lock;
import objects.world.lib.PushableBlock;
import objects.world.lib.Trampoline;
import objects.world.lib.Wall;

public class ObjectMaker {
	private ObjectMaker() {
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

		return wo;
	}
}
