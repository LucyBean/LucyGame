package tests;

import java.util.TreeMap;

import options.GlobalOptions;

public class Enumerations {
	
}

enum TestOption {
	TEXT_SIZE("Text size", 0, bigTextMap()),
		FLASHY_GRAPHICS("Flashy graphics", 1, flashyGraphicsMap()),
		WIDESCREEN("Widescreen", 0, wideScreenMap());

	String prettyName;
	int currentValue;
	TreeMap<Integer, String> values;

	TestOption(String prettyName, int currentValue,
			TreeMap<Integer, String> values) {
		this.prettyName = prettyName;
		this.values = values;
		setValue(currentValue);
	}

	/**
	 * Sets the current setting for this option to be a new value.
	 * 
	 * @param value
	 */
	public void setValue(Integer value) {
		if (value != null && values.containsKey(value)) {
			currentValue = value;
		} else if (GlobalOptions.debug()) {
			System.err.println("Attempted to set " + this.getName()
					+ " to invalid value " + value);
		}
	}

	public String getName() {
		return prettyName;
	}

	/**
	 * Sets the value of this enum to its next value. If it is currently at its
	 * highest value it will be set to its lowest value.
	 */
	public void setToNextValue() {
		Integer nextKey = values.higherKey(currentValue);
		if (nextKey == null) {
			currentValue = values.firstKey();
		} else {
			currentValue = nextKey;
		}
	}

	private static TreeMap<Integer, String> bigTextMap() {
		TreeMap<Integer, String> m = new TreeMap<Integer, String>();
		m.put(0, "8");
		m.put(1, "10");
		m.put(2, "12");
		m.put(3, "14");
		return m;
	}

	private static TreeMap<Integer, String> flashyGraphicsMap() {
		TreeMap<Integer, String> m = new TreeMap<Integer, String>();
		m.put(0, "No");
		m.put(1, "Yes");
		return m;
	}

	private static TreeMap<Integer, String> wideScreenMap() {
		TreeMap<Integer, String> m = new TreeMap<Integer, String>();
		m.put(0, "No");
		m.put(1, "Yes");
		return m;
	}

	@Override
	public String toString() {
		return getName() + ": " + values.get(currentValue);
	}
}
