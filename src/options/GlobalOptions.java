package options;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import io.ErrorLogger;

public class GlobalOptions {
	/**
	 * Sets the program's options by reading from "data/settings.ini".
	 */
	public static void loadFromFile() {
		try {
			File file = new File("data/settings.ini");
			if (file.exists()) {
				// Read in settings file
				BufferedReader br = new BufferedReader(new FileReader(file));
				// Process options
				br.lines().forEach(s -> process(s));
				br.close();
			}
		} catch (IOException ioe) {
			ErrorLogger.log(ioe, "Error reading settings.", 4);
		}
	}

	/**
	 * Writes the program's currentoptions to "data/settings.ini"
	 */
	public static void saveToFile() {
		try {
			File file = new File("data/settings.ini");
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			for (Option o : Option.values()) {
				bw.write(o.ordinal() + " = " + o.getValue());
				bw.newLine();
			}
			
			bw.close();
		} catch (IOException ioe) {
			ErrorLogger.log(ioe, "Error writing settings.", 4);
		}
	}

	public static void process(String s) {
		String[] parts = s.split("=");

		if (parts.length == 2) {
			String key = parts[0].replace(" ", "");
			String val = parts[1].replace(" ", "");
			try {
				int kn = Integer.parseInt(key);
				int vn = Integer.parseInt(val);

				if (kn >= 0 && kn < Option.values().length) {
					Option.values()[kn].setValue(vn);
				} else {
					ErrorLogger.log("Found unknown option " + kn, 0);
				}

			} catch (NumberFormatException nfe) {
				ErrorLogger.log("Attempted to parse option " + s
						+ " but it was badly formatted.", 3);
			}
		} else {
			ErrorLogger.log("Attempted to parse option " + s
					+ " but it was badly formatted.", 3);
		}
	}

	public static boolean drawAllColliders() {
		return Option.DRAW_ALL_COLLIDERS.getValue() == 1;
	}

	public static boolean drawInvisObjColliders() {
		return Option.DRAW_INVIS_OBJ_COLLIDERS.getValue() == 1;
	}

	public static boolean drawInteractBoxes() {
		return Option.DRAW_INTERACT_BOXES.getValue() == 1;
	}
	
	public static boolean drawAttachments() {
		return Option.DRAW_ATTACHMENTS.getValue() == 1;
	}

	public static boolean debug() {
		return Option.DEBUG.getValue() == 1;
	}

	public final static int WINDOW_WIDTH = 1280;
	public final static int WINDOW_HEIGHT = 960;
	public final static int GRID_SIZE = 32;
	public final static int WINDOW_WIDTH_GRID = WINDOW_WIDTH / GRID_SIZE;
	public final static int WINDOW_HEIGHT_GRID = WINDOW_HEIGHT / GRID_SIZE;
}
