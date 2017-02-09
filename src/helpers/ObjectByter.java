package helpers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import io.ErrorLogger;
import objects.world.INeedSpecialBuildInfo;
import objects.world.ItemType;
import objects.world.ObjectMaker;
import objects.world.WorldObject;
import options.GlobalOptions;

/**
 * Class for converting WorldObjects to and from bytes for exporting
 * 
 * @author Lucy
 *
 */
public class ObjectByter {
	private ByteBuffer byteBuffer;

	@Override
	public String toString() {
		byte[] bytes = byteBuffer.array();
		String ret = "";
		for (byte b : bytes) {
			ret += String.format("0x%x ", b);
		}
		return ret;
	}

	private byte[] getBytes() {
		return byteBuffer.array();
	}

	public void writeToFile(FileOutputStream out) {
		try {
			out.write(getBytes());
		} catch (IOException ioe) {
			ErrorLogger.log(ioe, "Error exporting object.", 2);
			if (GlobalOptions.debug()) {
				ioe.printStackTrace();
			}
		}
	}

	public ObjectByter(int len) {
		byteBuffer = ByteBuffer.allocate(len);
	}

	public ObjectByter(byte[] bytes) {
		this(bytes.length);
		this.byteBuffer = ByteBuffer.wrap(bytes);
	}

	/**
	 * Gets the layout type of this object. This determines the contents of each
	 * byte
	 * 
	 * @param it
	 *            The ItemType of the object
	 * @return
	 */
	private int getObjectLayoutType(ItemType it) {
		switch (it) {
			case NONE:
				// Cannot be loaded
				return 0;

			case WALL:
			case GEM:
			case YELLOW_KEY:
			case BLUE_KEY:
			case GREEN_KEY:
			case RED_KEY:
			case PLAYER:
			case DOG_ENEMY:
			case TRAMPOLINE:
			case PUSHABLE_BLOCK:
				// Only require position
				return 1;

			case BLUE_LOCK:
			case GREEN_LOCK:
			case RED_LOCK:
			case YELLOW_LOCK:
			case DOOR:
				// Require position and lock ID
				return 2;

			case MATT:
				// Require position and NPC ID
				return 3;

			case MOVING_PLATFORM:
				// Require two positions and an int
				return 4;

			case CLIMBING_WALL_MARKER:
				// Require one position and a float
				return 5;
		}

		return 0;
	}

	public static ObjectByter make(WorldObject object, int mapVersion) {
		ObjectByter ob = new ObjectByter(24);

		ItemType it = object.getType();
		if (it != null) {
			int itemType = it.ordinal();
			float x = object.getPosition().getX();
			float y = object.getPosition().getY();
			if (mapVersion == 1) {
				int lockID = object.getLockID();
				int keyID = object.getKeyID();
				int npcID = object.getNPCID();

				ob.byteBuffer.position(0);
				ob.byteBuffer.putInt(itemType);
				ob.byteBuffer.putFloat(x);
				ob.byteBuffer.putFloat(y);
				ob.byteBuffer.putInt(lockID);
				ob.byteBuffer.putInt(keyID);
				ob.byteBuffer.putInt(npcID);

				return ob;
			} else if (mapVersion == 2) {
				ob.byteBuffer.position(0);
				ob.byteBuffer.putInt(itemType);

				int layoutType = ob.getObjectLayoutType(it);
				switch (layoutType) {
					case 0:
						// Unexportable
						return null;
					case 1:
						// Only requires position
						ob.byteBuffer.putFloat(x);
						ob.byteBuffer.putFloat(y);
						break;
					case 2:
						// Requires position and lock ID
						ob.byteBuffer.putFloat(x);
						ob.byteBuffer.putFloat(y);
						int lockID = object.getLockID();
						ob.byteBuffer.putInt(lockID);
						break;
					case 3:
						// Requires position and NPC ID
						ob.byteBuffer.putFloat(x);
						ob.byteBuffer.putFloat(y);
						int npcID = object.getNPCID();
						ob.byteBuffer.putInt(npcID);
						break;
					case 4: {
						// Requires two positions and an int
						INeedSpecialBuildInfo insbi = (INeedSpecialBuildInfo) object;
						Point firstPos = insbi.getFirstPoint();
						Point secondPos = insbi.getSecondPoint();
						int vali = insbi.getExtraInt();
						ob.byteBuffer.putFloat(firstPos.getX());
						ob.byteBuffer.putFloat(firstPos.getY());
						ob.byteBuffer.putFloat(secondPos.getX());
						ob.byteBuffer.putFloat(secondPos.getY());
						ob.byteBuffer.putInt(vali);
					}
						break;
					case 5: {
						// Requires one position and a float
						INeedSpecialBuildInfo insbi = (INeedSpecialBuildInfo) object;
						Point firstPos = insbi.getFirstPoint();
						float valf = insbi.getExtraFloat();
						ob.byteBuffer.putFloat(firstPos.getX());
						ob.byteBuffer.putFloat(firstPos.getY());
						ob.byteBuffer.putFloat(valf);
					}
						break;
				}
				return ob;
			} else {
				ErrorLogger.log("Unknown map version " + mapVersion, 4);
				return null;
			}
		} else {
			ErrorLogger.log("Unexportable object " + object, 2);
			return null;
		}
	}

	public WorldObject getAsWorldObject(int mapVersion) {
		if (mapVersion == 1) {
			byteBuffer.position(0);
			int itemTypeNum = byteBuffer.getInt();
			float x = byteBuffer.getFloat();
			float y = byteBuffer.getFloat();
			int lockID = byteBuffer.getInt();
			@SuppressWarnings("unused")
			int keyID = byteBuffer.getInt(); // This is not actually used
			int npcID = byteBuffer.getInt();

			if (itemTypeNum >= 0 && itemTypeNum < ItemType.values().length) {
				ItemType itemType = ItemType.values()[itemTypeNum];
				return ObjectMaker.makeFromType(itemType, new Point(x, y),
						lockID, npcID);
			} else {
				return null;
			}
		} else if (mapVersion == 2) {
			byteBuffer.position(0);
			int itemTypeNum = byteBuffer.getInt();
			if (itemTypeNum >= 0 && itemTypeNum < ItemType.values().length) {
				ItemType itemType = ItemType.values()[itemTypeNum];
				int layoutType = getObjectLayoutType(itemType);
				WorldObject wo = null;
				
				switch (layoutType) {
					case 0:
						// Unexportable
						return null;
					case 1:
					{
						// Only requires position
						float x = byteBuffer.getFloat();
						float y = byteBuffer.getFloat();
						wo = ObjectMaker.makeFromType(itemType, new Point(x,y));
					}
						break;
					case 2:
					case 3:
					{
						// Requires position and an integer (lock ID or NPC ID)
						float x = byteBuffer.getFloat();
						float y = byteBuffer.getFloat();
						int someID = byteBuffer.getInt();
						wo = ObjectMaker.makeFromType(itemType, new Point(x,y), someID);
					}
						break;
					case 4: {
						// Requires two positions and an int
						float x1 = byteBuffer.getFloat();
						float y1 = byteBuffer.getFloat();
						float x2 = byteBuffer.getFloat();
						float y2 = byteBuffer.getFloat();
						int vali = byteBuffer.getInt();
						wo = ObjectMaker.makeFromType(itemType, new Point(x1,y1), new Point(x2,y2), vali);
					}
						break;
					case 5: {
						// Requires one position and a float
						float x = byteBuffer.getFloat();
						float y = byteBuffer.getFloat();
						float valf = byteBuffer.getFloat();
						wo = ObjectMaker.makeFromType(itemType, new Point(x,y), valf);
					}
						break;
					default:
						break;
				}
				
				return wo;
			} else {
				ErrorLogger.log("Unknown item type " + itemTypeNum, 3);
				return null;
			}
		} else {
			ErrorLogger.log("Unknown map version " + mapVersion, 4);
			return null;			
		}
	}
}
