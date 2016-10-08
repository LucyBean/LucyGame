package objects;

import characters.Matt;
import helpers.Point;
import objectLibrary.Door;
import objectLibrary.Gem;
import objectLibrary.Key;
import objectLibrary.Lock;
import objectLibrary.Wall;
import player.Player;

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
			case DOOR_BTM:
				wo = new Door(position, lockID);
				break;
			case DOOR_TOP:
				wo = new Door(position, lockID);
				break;
			case MATT:
				wo = new Matt(position, npcID);
				break;
			case PLAYER:
				wo = new Player(position);
				break;
		}
		
		return wo;
	}
}