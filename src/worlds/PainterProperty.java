package worlds;

public abstract class PainterProperty {
	private String name;
	private int value;
	
	private static PainterProperty lockID;
	private static PainterProperty npcID;
	
	public static PainterProperty getLockID() {
		if (lockID == null) {
			lockID = new PainterProperty("Lock ID", 1){
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
