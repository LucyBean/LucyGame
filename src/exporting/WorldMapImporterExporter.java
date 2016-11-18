package exporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import objects.WorldObject;
import options.GlobalOptions;
import worlds.WorldMap;

public class WorldMapImporterExporter {
	private static final int version = 1;

	public static void export(WorldMap wm, String filename) {
		try {
			File file = new File("data/maps/" + filename + ".map");
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
			File file = new File("data/maps/" + filename + ".map");
			FileInputStream in = new FileInputStream(file);
			byte[] buffer = new byte[24];
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
		byte[] header = new byte[24];
		header[0] = version;
		ObjectByter hob = new ObjectByter(header);
		hob.writeToFile(out);
	}

	private static int readHeader(byte[] bytes) {
		int version = 15 & bytes[0];
		return version;
	}

	private static WorldObject readItem(byte[] bytes) {
		ObjectByter ob = new ObjectByter(bytes);
		return ob.getAsWorldObject();
	}

	private static void exportObject(WorldObject object, FileOutputStream out) {
		ObjectByter ob = new ObjectByter(object);
		ob.writeToFile(out);
	}

}
