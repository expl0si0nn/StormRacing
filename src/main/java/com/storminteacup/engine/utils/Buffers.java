package com.storminteacup.engine.utils;

import org.lwjgl.BufferUtils;


import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Storminteacup on 03-Dec-15.
 */
public class Buffers {
	public static FloatBuffer arrayToBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static IntBuffer arrayToBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static int bytesToInt(byte[] bytes) {
		return ByteBuffer.allocate(4).put(bytes).getInt();
	}

	public static byte[] intToBytes(int value) {
		return ByteBuffer.allocate(4).putInt(value).array();
	}

	public static float bytesToFloat(byte[] bytes) {
		return ByteBuffer.allocate(4).put(bytes).getFloat();
	}

	public static byte [] floatToBytes (float value) {
		return ByteBuffer.allocate(4).putFloat(value).array();
	}
}
