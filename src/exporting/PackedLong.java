package exporting;

public class PackedLong {
	private long number;

	public void setBit(int position, boolean on) {
		if (position >= 0 && position < 64) {
			if (on) {
				long mask = 1L << position;
				number = number | mask;
			} else {
				long mask = ~(1L << position);
				number = number & mask;
			}
		}
	}

	public void setBits(int startPosition, int numBits, long set) {
		for (int i = 0; i < numBits; i++) {
			long mask = 1 << i;
			long bit = mask & set;
			setBit(startPosition + i, (bit != 0));
		}
	}

	public long getBits(int startPosition, int numBits) {
		long mask = 0L;
		for (int i = 0; i < numBits; i++) {
			mask = mask | 1L << startPosition + i;
		}

		long bits = number & mask;
		// Shift back-and-forth for sign extension.
		bits = bits << 64 - (startPosition + numBits);
		bits = bits >> 64 - numBits;

		return bits;
	}

	@Override
	public String toString() {
		return getBitString();
	}
	
	public String getBitString() {
		long bit = 1;
		String asBits = "";

		for (int i = 0; i < 64; i++) {
			long thisBit = number & bit;
			if (thisBit == 0) {
				asBits = "0" + asBits;
			} else {
				asBits = "1" + asBits;
			}
			if (i % 8 == 7) asBits = " " + asBits;
			bit = bit << 1;
		}
		return asBits;
	}
	
	public byte[] getBytes() {
		byte[] bytes = new byte[8];
		for(int i = 0; i < 8; i++) {
			bytes[i] = (byte) (number >> i*8);
		}
		return bytes;
	}
	
	public PackedLong() {
		number = 0L;
	}
	
	public PackedLong(byte[] bytes) {
		number = 0L;
		if (bytes.length == 8) {
			for(int i = 0; i < 8; i++) {
				number = number | (((long) bytes[i]) << i*8);
			}
		}
	}
}
