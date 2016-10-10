package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class FileReading {
	public static void main(String[] args) throws IOException {
		BufferedReader br = null;
		Map<String, Integer> options = new HashMap<String, Integer>();
		Map<String, Integer> importedOptions = new HashMap<String, Integer>();

		options.put("option_a", 1);
		options.put("option_b", 0);
		options.put("option_c", 3);
		options.put("option_d", 7);
		options.put("option_f", 1);
		try {
			System.out.println("---Original---");
			System.out.println(options);

			// Read in options
			br = new BufferedReader(new FileReader("data/test/my file.txt"));
			importedOptions = br.lines().map(s -> processOption(s)).filter(
					s -> s != null).collect(
							Collectors.toMap(FROption::getName,
									FROption::getValue));
			System.out.println("---Imported---");
			System.out.println(importedOptions);

			// Merge them with options list
			System.out.println(importedOptions.entrySet().stream().filter(
					k -> options.containsKey(k.getKey())).collect(
							Collectors.toList()));
			importedOptions.entrySet().stream().filter(
					k -> options.containsKey(k.getKey())).forEach(
							k -> options.put(k.getKey(), k.getValue()));

			System.out.println("---New---");
			System.out.println(options);

			for (int i = 0; i < 20; i++) {
				Random rand = new Random();
				int n = rand.nextInt(4);
				File file = new File("data/test/" + n + ".txt");

				if (file.exists()) {
					System.out.println(file.getPath() + " already exists!");
				} else {
					file.createNewFile();
					System.out.println("Created new file " + file.getPath());
				}
			}

		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	public static FROption processOption(String s) {
		String[] parts = s.split("=");

		if (parts.length == 2) {
			String key = parts[0].replace(" ", "");
			String val = parts[1].replace(" ", "");
			if (val.equals("TRUE")) {
				return new FROption(key, 1);
			} else if (val.equals("FALSE")) {
				return new FROption(key, 0);
			} else {
				try {
					int value = Integer.parseInt(val);
					return new FROption(key, value);
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
}

class FROption {
	String name;
	int value;

	public FROption(String name, int value) {
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
