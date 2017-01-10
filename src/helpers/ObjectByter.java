package helpers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import io.ErrorLogger;
import objects.world.ItemType;
import objects.world.ObjectMaker;
import objects.world.WorldObject;
import options.GlobalOptions;

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

	public ObjectByter() {
		byteBuffer = ByteBuffer.allocate(24);
	}

	public ObjectByter(byte[] bytes) {
		this();
		this.byteBuffer = ByteBuffer.wrap(bytes);
	}

	public ObjectByter(WorldObject object) {
		this();
		ItemType it = object.getType();
		if (it != null) {
			int itemType = it.ordinal();
			float x = object.getPosition().getX();
			float y = object.getPosition().getY();
			int lockID = object.getLockID();
			int keyID = object.getKeyID();
			int npcID = object.getNPCID();

			byteBuffer.position(0);
			byteBuffer.putInt(itemType);
			byteBuffer.putFloat(x);
			byteBuffer.putFloat(y);
			byteBuffer.putInt(lockID);
			byteBuffer.putInt(keyID);
			byteBuffer.putInt(npcID);
		} else {
			ErrorLogger.log("Unexportable object " + object, 2);
		}
	}

	public WorldObject getAsWorldObject() {
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
			return ObjectMaker.makeFromType(itemType, new Point(x, y), lockID,
					npcID);
		} else {
			return null;
		}
	}
}
