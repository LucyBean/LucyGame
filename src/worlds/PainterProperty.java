package worlds;

import gameInterface.PropertyPanel;

public abstract class PainterProperty {
	private String name;
	private int value;

	private static PropertyPanel panel;

	private static PainterProperty lockID;
	private static PainterProperty npcID;

	/**
	 * Sets the PropertyPanel that is used to represent the PainterProperties.
	 * This panel will be updated whether the PainterProperties change.
	 * 
	 * @param panel
	 */
	public static void setPanel(PropertyPanel panel) {
		PainterProperty.panel = panel;
	}

	public static PainterProperty getLockID() {
		if (lockID == null) {
			lockID = new PainterProperty("Lock ID", 1) {
				@Override
				protected boolean isValid(int value) {
					return (value > 0 && value < 256);
				}
			};
		}

		return lockID;
	}

	public static PainterProperty getNpcID() {
		if (npcID == null) {
			npcID = new PainterProperty("NPC ID", 1) {
				@Override
				protected boolean isValid(int value) {
					return (value > 0 && value < 256);
				}
			};
		}

		return npcID;
	}

	private PainterProperty(String name, int value) {
		this.name = name;
		setValue(value);
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	private void setValue(int newValue) {
		if (isValid(newValue)) {
			value = newValue;
			
			if (panel != null) {
				panel.updateSprites();
			}
		}
	}

	public void increment() {
		int newValue = value + 1;
		setValue(newValue);
	}

	public void decrement() {
		int newValue = value - 1;
		setValue(newValue);
	}

	protected abstract boolean isValid(int value);
}
