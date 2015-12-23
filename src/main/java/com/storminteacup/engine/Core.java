package com.storminteacup.engine;

import com.storminteacup.engine.game.Game;
import com.storminteacup.engine.menus.MainMenu;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Storminteacup on 22-Dec-15.
 */
public class Core {

	private long window;
	private GLFWKeyCallback keyCallback;
	private GLFWCursorPosCallback mouseCallback;
	private GLFWErrorCallback errorCallback;

	private int screenWidth = 800;
	private int screenHeight = 600;

	private String serverAddress;

	public Core(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public void start() {

		init();

		new Game(serverAddress, 7777, window, screenWidth, screenHeight).start();
		//new MainMenu(serverAddress, 7777, window, screenWidth, screenHeight).start();


		// TODO: add memory cleanup
		glfwTerminate();

		System.exit(0);
	}

	public void init() {
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		glfwInit();

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		glfwWindowHint(GLFW_SAMPLES, 4);

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		screenWidth = vidmode.width();
		screenHeight = vidmode.height();

		window = glfwCreateWindow(screenWidth, screenHeight, "StormRacing", NULL, NULL);
		if (window == NULL) {
			System.err.println("Failed to create GLFW window");
			glfwTerminate();
			return;
		}

		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		glfwSwapInterval(1);
	}

}
