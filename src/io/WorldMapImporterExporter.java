package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import helpers.ObjectByter;
import objects.world.WorldObject;
import worlds.WorldMap;

public class WorldMapImporterExporter {
	private static final int version = 2;

	public static void export(WorldMap wm, String filename) {
		try {
			File file = new File("data/maps/" + filename + ".map");
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);

			writeHeader(out);

			Collection<WorldObject> objects = wm.getObjects();
			objects.forEach(o -> exportObject(o, out));

			out.close();
		} catch (IOException ioe) {
			ErrorLogger.log(ioe, "Error exporting map.", 4);
		}
	}

	public static Optional<Collection<WorldObject>> importObjects(String filename) {
		try {
			File file = new File("data/maps/" + filename + ".map");
			if (!file.exists()) {
				return Optional.empty();
			}
			FileInputStream in = new FileInputStream(file);
			byte[] buffer = new byte[24];
			in.read(buffer);
			int fileVersion = readHeader(buffer);

			Collection<WorldObject> objects = new HashSet<WorldObject>();

			while (in.read(buffer) != -1) {
				WorldObject wo = readItem(buffer, fileVersion);
				objects.add(wo);
			}
			in.close();
			return Optional.of(objects);
		} catch (IOException ioe) {
			ErrorLogger.log(ioe, "Error importing map.", 4);
			return Optional.empty();
		}
	}

	private static void writeHeader(FileOutputStream out) {
		byte[] header = new byte[24];
		header[0] = version;
		ObjectByter hob = new ObjectByter(header);
		hob.writeToFile(out);
	}

	private static int readHeader(byte[] bytes) {
		int version = 15 & bytes[0];
		return version;
	}

	private static WorldObject readItem(byte[] bytes, int fileVersion) {
		ObjectByter ob = new ObjectByter(bytes);
		return ob.getAsWorldObject(fileVersion);
	}

	private static void exportObject(WorldObject object, FileOutputStream out) {
		ObjectByter ob = ObjectByter.make(object, version);
		if (ob != null) {
			ob.writeToFile(out);
		}
	}

}
