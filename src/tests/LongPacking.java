package tests;

import exporting.PackedLong;

public class LongPacking {
	public static void main(String[] args) {
		int itemType = 2;
		int x = -11;
		int y = 3;
		int lockID = 31;
		int keyID = 3;
		int npcID = 0;

		printBits(itemType);
		printBits(x);
		printBits(y);
		printBits(lockID);
		printBits(keyID);
		printBits(npcID);

		System.out.println("\n---As long---");
		long number = objectToLong(itemType, x, y, lockID, keyID, npcID);

		printBits(number);

		longToObject(number);
		
		PackedLong pl = new PackedLong();
		pl.setBits(8, 8, npcID);
		pl.setBits(16, 4, keyID);
		pl.setBits(20, 4, lockID);
		pl.setBits(24, 16, y);
		pl.setBits(40, 16, x);
		pl.setBits(56, 8, itemType);
		
		System.out.println(pl);
	}

	private static long setBit(long number, int position, boolean on) {
		if (position >= 0 && position < 64) {
			if (on) {
				long mask = 1L << position;
				number = number | mask;
			} else {
				long mask = ~(1L << position);
				number = number & mask;
			}
		}

		return number;
	}

	private static long setBits(long number, int startPosition, int numBits,
			long set) {
		for (int i = 0; i < numBits; i++) {
			long mask = 1 << i;
			long bit = mask & set;
			number = setBit(number, startPosition + i, (bit != 0));
		}

		return number;
	}

	private static long getBits(long number, int startPosition, int numBits) {
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

	private static long objectToLong(int itemType, int x, int y, int lockID,
			int keyID, int npcID) {
		long number = 0L;

		number = setBits(number, 8, 8, npcID);
		number = setBits(number, 16, 4, keyID);
		number = setBits(number, 20, 4, lockID);
		number = setBits(number, 24, 16, y);
		number = setBits(number, 40, 16, x);
		number = setBits(number, 56, 8, itemType);

		return number;
	}

	private static void longToObject(long number) {
		int itemType = (int) getBits(number, 56, 8);
		int x = (int) getBits(number, 40, 16);
		int y = (int) getBits(number, 24, 16);
		int lockID = (int) getBits(number, 20, 4);
		int keyID = (int) getBits(number, 16, 4);
		int npcID = (int) getBits(number, 8, 8);

		System.out.println(itemType + " " + x + " " + y + " " + lockID + " "
				+ keyID + " " + npcID);
	}

	private static void printBits(long number) {
		long bit = 1;
		String asBits = "";

		for (int i = 0; i < 64; i++) {
			long thisBit = number & bit;
			if (thisBit == 0) {
				asBits = "0" + asBits;
			} else {
				asBits = "1" + asBits;
			}
			bit = bit << 1;
		}

		System.out.println(asBits);
	}
}
