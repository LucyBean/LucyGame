package worlds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GlobalOptions {
	private static Map<String, Integer> options = setDefaults();

	private static Map<String, Integer> setDefaults() {
		Map<String, Integer> defaults = new HashMap<String, Integer>();
		defaults.put("DRAW_ALL_COLLIDERS", 0);
		defaults.put("DRAW_INVIS_OBJ_COLLIDERS", 0);
		defaults.put("DRAW_INTERACT_BOXES", 0);
		defaults.put("DEBUG", 0);

		return defaults;
	}

	/**
	 * Sets the program's options by reading from "data/settings.ini".
	 */
	public static void init() {
		try {
			File file = new File("data/settings.ini");
			if (file.exists()) {
				// Read in settings file
				BufferedReader br = new BufferedReader(
						new FileReader(file));
				// Process options to a map
				Map<String, Integer> importedOptions = br.lines().map(
						s -> Option.process(s)).filter(s -> s != null).collect(
								Collectors.toMap(Option::getName,
										Option::getValue));
				// Replace original options with new options
				importedOptions.entrySet().stream().filter(
						k -> options.containsKey(k.getKey())).forEach(
								k -> options.put(k.getKey(), k.getValue()));
				br.close();
			}

		} catch (IOException ioe) {
			System.err.println("Error reading settitngs.");
			ioe.printStackTrace();
		}
	}

	public static boolean drawAllColliders() {
		return options.get("DRAW_ALL_COLLIDERS") == 1;
	}

	public static boolean drawInvisObjColliders() {
		return options.get("DRAW_INVIS_OBJ_COLLIDERS") == 1;
	}

	public static boolean drawInteractBoxes() {
		return options.get("DRAW_INTERACT_BOXES") == 1;
	}

	public static boolean debug() {
		return options.get("DEBUG") == 1;
	}

	public final static int WINDOW_WIDTH = 640;
	public final static int WINDOW_HEIGHT = 480;
	public final static int GRID_SIZE = 32;
	public final static int WINDOW_WIDTH_GRID = WINDOW_WIDTH / GRID_SIZE;
	public final static int WINDOW_HEIGHT_GRID = WINDOW_HEIGHT / GRID_SIZE;
}

class Option {
	String name;
	int value;

	public static Option process(String s) {
		String[] parts = s.split("=");

		if (parts.length == 2) {
			String key = parts[0].replace(" ", "");
			String val = parts[1].replace(" ", "");
			if (val.equals("TRUE")) {
				return new Option(key, 1);
			} else if (val.equals("FALSE")) {
				return new Option(key, 0);
			} else {
				try {
					int value = Integer.parseInt(val);
					return new Option(key, value);
				} catch (NumberFormatException nfe) {
					System.err.println("Attempted to parse option " + s
							+ " but it was badly formatted.");
					return null;
				}
			}
		} else {
			System.err.println("Attempted to parse option " + s
					+ " but it was badly formatted.");
			return null;
		}
	}

	public Option(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}
}
