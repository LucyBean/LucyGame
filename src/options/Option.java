package options;

import java.util.TreeMap;

public enum Option {
	DRAW_ALL_COLLIDERS("Display all colliders", onOffMap()),
	DRAW_INVIS_OBJ_COLLIDERS("Display invisible object colliders", onOffMap()),
	DRAW_INTERACT_BOXES("Draw interact boxes", onOffMap()),
	DRAW_SENSORS("Draw sensors", onOffMap()),
	DEBUG("Debugging mode", onOffMap()),
	DRAW_ATTACK_BOXES("Draw attack boxes", onOffMap());

	String prettyName;
	int currentValue;
	TreeMap<Integer, String> values;

	Option(String prettyName, int currentValue,
			TreeMap<Integer, String> values) {
		this.prettyName = prettyName;
		this.values = values;
		setValue(currentValue);
	}
	
	Option(String prettyName, TreeMap<Integer, String> values) {
		this(prettyName, 0, values);
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
	
	public int getValue() {
		return currentValue;
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

	private static TreeMap<Integer, String> onOffMap() {
		TreeMap<Integer, String> m = new TreeMap<Integer, String>();
		
		m.put(0, "Off");
		m.put(1, "On");
		
		return m;
	}

	@Override
	public String toString() {
		return getName() + ": " + values.get(currentValue);
	}
}
