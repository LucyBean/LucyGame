package tests;

import java.nio.ByteBuffer;

public class ByteBufferTest {
	public static void main(String[] args) {
		ByteBuffer bb = ByteBuffer.allocate(20);
		bb.putInt(5437);
		bb.putInt(-2323);
		bb.putInt(0);
		
		System.out.println((byte) 4374343);
		
		byte[] bytes = bb.array();
		for (byte b : bytes) {
			System.out.format("0x%x ", b);
		}
		
		
	}
}
