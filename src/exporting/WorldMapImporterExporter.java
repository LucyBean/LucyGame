package exporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import helpers.Point;
import objects.ItemType;
import objects.ObjectMaker;
import objects.WorldObject;
import options.GlobalOptions;
import worlds.WorldMap;

public class WorldMapImporterExporter {
	private static final int version = 1;

	public static void export(WorldMap wm, String filename) {
		try {
			File file = new File("data/" + filename + ".map");
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);

			writeHeader(out);

			Collection<WorldObject> objects = wm.getObjects();
			objects.stream().forEach(o -> exportObject(o, out));

			out.close();
		} catch (IOException ioe) {
			System.err.println("Error exporting map.");
			if (GlobalOptions.debug()) {
				ioe.printStackTrace();
			}
		}
	}

	public static Collection<WorldObject> importObjects(String filename) {
		try {
			File file = new File("data/" + filename + ".map");
			FileInputStream in = new FileInputStream(file);
			byte[] buffer = new byte[8];
			in.read(buffer);
			int fileVersion = readHeader(buffer);

			if (fileVersion != version) {
				System.err.println(
						"Map import/export version numbers do not match.");
			}

			Collection<WorldObject> objects = new HashSet<WorldObject>();

			while (in.read(buffer) != -1) {
				WorldObject wo = readItem(buffer);
				objects.add(wo);
			}

			in.close();
			return objects;
		} catch (IOException ioe) {
			System.err.println("Error importing map.");
			if (GlobalOptions.debug()) {
				ioe.printStackTrace();
			}
			return null;
		}
	}

	private static void writeHeader(FileOutputStream out) {
		PackedLong header = new PackedLong();
		header.setBits(0, 4, version);
		exportPackedLong(header, out);
	}

	private static int readHeader(byte[] bytes) {
		PackedLong pl = new PackedLong(bytes);

		int version = (int) pl.getBits(0, 4);
		return version;
	}

	private static WorldObject readItem(byte[] bytes) {
		PackedLong pl = new PackedLong(bytes);

		int itemTypeNum = (int) pl.getBits(56, 8);
		int x = (int) pl.getBits(40, 16);
		int y = (int) pl.getBits(24, 16);
		int lockID = (int) pl.getBits(20, 4);
		int npcID = (int) pl.getBits(8, 8);

		if (itemTypeNum >= 0 && itemTypeNum < ItemType.values().length) {
			ItemType itemType = ItemType.values()[itemTypeNum];
			return ObjectMaker.makeFromType(itemType, new Point(x, y), lockID,
					npcID);
		} else {
			return null;
		}
	}

	private static void exportObject(WorldObject object, FileOutputStream out) {
		ItemType it = object.getType();
		if (it != null) {
			int itemType = it.ordinal();
			int x = (int) object.getPosition().getX();
			int y = (int) object.getPosition().getY();
			int lockID = object.getLockID();
			int keyID = object.getKeyID();
			int npcID = object.getNPCID();

			PackedLong pl = new PackedLong();
			pl.setBits(8, 8, npcID);
			pl.setBits(16, 4, keyID);
			pl.setBits(20, 4, lockID);
			pl.setBits(24, 16, y);
			pl.setBits(40, 16, x);
			pl.setBits(56, 8, itemType);

			exportPackedLong(pl, out);
		} else {
			System.err.println("Unexportable object " + object);
		}
	}

	private static void exportPackedLong(PackedLong pl, FileOutputStream out) {
		try {
			out.write(pl.getBytes());
		} catch (IOException ioe) {
			System.err.println("Error exporting object.");
			if (GlobalOptions.debug()) {
				ioe.printStackTrace();
			}
		}
	}
}
