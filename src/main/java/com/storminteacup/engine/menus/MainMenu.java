package com.storminteacup.engine.menus;

import com.storminteacup.engine.game.Game;
import com.storminteacup.engine.graphics.*;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWKeyCallback;


import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Storminteacup on 22-Dec-15.
 */
public class MainMenu {

	private boolean running;

	private long window;
	private GLFWKeyCallback keyCallback;

	private int screenWidth = 800;
	private int screenHeight = 600;

	private final String[] mainMenuOptions = {"Join game", "Exit"};
	private final String[] carPickMenuOptions = {"Car1", "Car2", "Car3"};

	private Menu mainMenu;
	private Menu carPickMenu;

	private String host;
	private int port;

	Menu current;
	private int menuPos = 0;

	public MainMenu(String host, int port, long window, int screenWidth, int screenHeight) {
		this.host = host;
		this.port = port;
		this.window = window;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	public void start() {
		running = true;

		init();

		setCallbacks();

		loop();
	}

	public void setCallbacks() {
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if(key == GLFW_KEY_ENTER && action == GLFW_PRESS)
					executeCurrent();
				if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
					if(current.prev != null) {
						current = current.prev;
						menuPos = 0;
					}
				}
				else if (key == GLFW_KEY_UP && action == GLFW_PRESS)
					menuPos = (menuPos - 1) < 0? (current.options.length - 1) : (menuPos - 1);
				else if (key == GLFW_KEY_DOWN && action == GLFW_PRESS)
					menuPos = (menuPos + 1) % current.options.length;
			}
		});
	}

	private void init() {
		mainMenu = new Menu("Main", mainMenuOptions, null);
		mainMenu.create();
		carPickMenu = new Menu("CarPick", carPickMenuOptions, mainMenu);
		carPickMenu.create();
		current = mainMenu;
	}

	private void executeCurrent() {
		if(current.getName().equals("Main")) {
			switch (menuPos) {
				case 0 :
					current = carPickMenu;
					menuPos = 0;
					break;
				case 1:
					glfwSetWindowShouldClose(window, GLFW_TRUE);
					break;
			}
		}
		else if(current.getName().equals("CarPick")) {
			// TODO: send server message
			new Game(host, port, window, screenWidth, screenHeight).start();
			setCallbacks();
		}
	}

	private void loop() {

		// Shaders
		Shader vertexShader = new Shader("src/main/resources/shaders/menuVertexShader.glsl", GL_VERTEX_SHADER);
		vertexShader.create();

		Shader fragmentShader = new Shader("src/main/resources/shaders/menuFragmentShader.glsl", GL_FRAGMENT_SHADER);
		fragmentShader.create();

		ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
		shaderProgram.create();

		vertexShader.destroy();
		fragmentShader.destroy();



		Matrix4f projection = new Matrix4f().ortho(0.0f, 1.0f, 1.0f, 0.0f, 0.1f, 100.0f);

		while (running) {

			glfwPollEvents();

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glClearColor(0.38f, 0.72f, 1.0f, 1.0f);

			current.bind();
			glActiveTexture(GL_TEXTURE0);
			current.tex.bind();
			shaderProgram.setUniform("tex", 0);

			glDrawArrays(GL_TRIANGLES, 0, 6);

			current.unbind();

			//System.out.println(current.getName() + " " + current.options[menuPos]);

			running = running && (glfwWindowShouldClose(window) == GL_FALSE);
			glfwSwapBuffers(window);
		}
	}
}
