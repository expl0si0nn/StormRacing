package com.storminteacup.engine.input;

import org.lwjgl.glfw.GLFW;

/**
 * Created by Storminteacup on 21-Dec-15.
 */
public class Input {
	public static boolean[] keys = new boolean[1024];
	public static int moveForvard = GLFW.GLFW_KEY_W;
	public static int moveBackward = GLFW.GLFW_KEY_S;
	public static int moveLeft = GLFW.GLFW_KEY_A;
	public static int moveRight = GLFW.GLFW_KEY_D;

	public static boolean isKeyDown(int key) {
		return keys[key];
	}

	public static void setMoveForvard(int moveForvard) {
		Input.moveForvard = moveForvard;
	}

	public static void setMoveBackward(int moveBackward) {
		Input.moveBackward = moveBackward;
	}

	public static void setMoveLeft(int moveLeft) {
		Input.moveLeft = moveLeft;
	}

	public static void setMoveRight(int moveRight) {
		Input.moveRight = moveRight;
	}
}
