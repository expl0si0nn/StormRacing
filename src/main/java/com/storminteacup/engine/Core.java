package com.storminteacup.engine;

import com.storminteacup.engine.graphics.Shader;
import com.storminteacup.engine.graphics.ShaderProgram;
import com.storminteacup.engine.input.Input;
import com.storminteacup.engine.models.Mesh;
import com.storminteacup.engine.models.Model;

import com.storminteacup.engine.network.NetworkManager;
import com.storminteacup.engine.renderEngine.Renderer;
import com.storminteacup.engine.states.GameStateMachine;
import com.storminteacup.engine.utils.Loader;
import com.storminteacup.engine.utils.Timer;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import org.joml.*;


/**
 * Created by Storminteacup on 03-Dec-15.
 */
public class Core {

	private long window;
	private GLFWKeyCallback keyCallback;
	private GLFWCursorPosCallback mouseCallback;
	private GLFWErrorCallback errorCallback;

	private Timer timer;
	private NetworkManager networkManager;
	private Renderer renderer;
	private Camera spectatorCamera;
	private Camera playerCamera;

	boolean spectator = false;

	private int screenWidth = 1680;
	private int screenHeight = 1050;

	private int playerId = 0;

	private float sensetivity = 0.04f;
	private float lastX = (float) screenWidth / 2.0f;
	private float lastY = (float) screenHeight / 2.0f;
	private boolean firstMouse = true;
	private boolean invertMouse = false;

	public void start() {

		init();

		setCallbacks();

		loop();

		glfwTerminate();
	}

	private void init() {
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		glfwInit();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		glfwWindowHint(GLFW_SAMPLES, 4);


		window = glfwCreateWindow(screenWidth, screenHeight, "LearnOpenGL", NULL, NULL);
		if (window == NULL) {
			System.err.println("Failed to create GLFW window");
			glfwTerminate();
			return;
		}

		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		glfwSwapInterval(1);

		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

		GameStateMachine.init();
		timer = new Timer();
		networkManager = new NetworkManager();
		renderer = new Renderer(screenWidth, screenHeight);
		spectatorCamera = new Camera(new Vector3f(-5.0f, 5.0f, 7.0f), new Vector3f(0.0f, 0.0f, -1.0f));
		playerCamera = new Camera(new Vector3f(-5.0f, 5.0f, 7.0f), new Vector3f(0.0f, 0.0f, -1.0f));
	}

	private void setCallbacks() {
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
					glfwSetWindowShouldClose(window, GL_TRUE);
				if (action == GLFW_PRESS)
					Input.keys[key] = true;
				else if (action == GLFW_RELEASE)
					Input.keys[key] = false;
				if (Input.keys[GLFW_KEY_F1])
					spectator = !spectator;
				if (Input.keys[GLFW_KEY_UP])
					playerId = (playerId + 1) % 3;
			}
		});


		glfwSetCursorPosCallback(window, mouseCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				if (spectator) {
					if (firstMouse) {
						lastX = (float) xpos;
						lastY = (float) ypos;
						firstMouse = false;
						return;
					}
					float xoffset = (float) xpos - lastX;
					float yoffset = (float) ypos - lastY;

					lastX = (float) xpos;
					lastY = (float) ypos;

					xoffset *= sensetivity;
					yoffset *= sensetivity;

					if (!invertMouse)
						yoffset = 0.0f - yoffset;

					spectatorCamera.moveDirection(xoffset, yoffset);
				}
			}
		});

	}

	private void moveCamera() {
		if (spectator) {
			if (Input.keys[GLFW_KEY_W])
				spectatorCamera.moveCamera(Camera.FORWARD_DIRECTION, 1.0f);
			if (Input.keys[GLFW_KEY_S])
				spectatorCamera.moveCamera(Camera.BACKWARD_DIRECTION, 1.0f);
			if (Input.keys[GLFW_KEY_A])
				spectatorCamera.moveCamera(Camera.LEFT_DIRECTION, 1.0f);
			if (Input.keys[GLFW_KEY_D])
				spectatorCamera.moveCamera(Camera.RIGHT_DIRECTION, 1.0f);
		}
	}


	private void loop() {

		// Shaders
		Shader vertexShader = new Shader("src/main/resources/shaders/vertexShader.glsl", GL_VERTEX_SHADER);
		vertexShader.create();

		Shader fragmentShader = new Shader("src/main/resources/shaders/fragmentShader.glsl", GL_FRAGMENT_SHADER);
		fragmentShader.create();

		ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
		shaderProgram.create();

		vertexShader.destroy();
		fragmentShader.destroy();

		/*
		Shader lampVertexShader = new Shader("src/main/resources/shaders/lampVertexShader.glsl", GL_VERTEX_SHADER);
		lampVertexShader.create();

		Shader lampFragmentShader = new Shader("src/main/resources/shaders/lampFragmentShader.glsl", GL_FRAGMENT_SHADER);
		lampFragmentShader.create();

		ShaderProgram lampShaderProgram = new ShaderProgram(lampVertexShader, lampFragmentShader);
		lampShaderProgram.create();

		lampVertexShader.destroy();
		lampFragmentShader.destroy();
		*/
		// Models

		//Lamborgini Aventador
		//Ferrari California
		//Ferrari 458

		Mesh carMesh1 = Loader.loadObj("src/main/resources/models/Lamborgini Aventador/model.obj");
		carMesh1.create();

		Mesh carMesh2 = Loader.loadObj("src/main/resources/models/Ferrari California/model.obj");
		carMesh2.create();

		Mesh carMesh3 = Loader.loadObj("src/main/resources/models/Ferrari 458/model.obj");
		carMesh3.create();

		Mesh mesh2 = Loader.loadObj("src/main/resources/models/track/Estoril-track3.obj");
		mesh2.create();

		//Models

		Model[] cars = new Model[3];

		cars[0] = new Model(carMesh1.id);
		cars[0].setPosition(new Vector3f(0.0f, 1.0f, 0.0f));
		cars[0].setDirection(new Vector3f(-1.0f, 0.0f, 0.0f));

		cars[1] = new Model(carMesh2.id);
		cars[1].setPosition(new Vector3f(20.0f, 1.0f, 0.0f));
		cars[1].setDirection(new Vector3f(-1.0f, 0.0f, 0.0f));

		cars[2] = new Model(carMesh3.id);
		cars[2].setPosition(new Vector3f(40.0f, 1.0f, 0.0f));
		cars[2].setDirection(new Vector3f(-1.0f, 0.0f, 0.0f));


		GameStateMachine.addModel(cars[0]);
		GameStateMachine.addModel(cars[1]);
		GameStateMachine.addModel(cars[2]);


		Model track = new Model(mesh2.id);
		track.setScaling(1.0f);

		//Player
		Vector3f playerDir = new Vector3f();


		glViewport(0, 0, screenWidth, screenHeight);

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_MULTISAMPLE);
		//glEnable(GL_BLEND);
		//glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		float delta;
		float accumulator = 0.0f;
		float interval = 1.0f / 60.0f;
		float alpha;

		Camera activeCamera = spectator ? spectatorCamera : playerCamera;

		while (glfwWindowShouldClose(window) != GL_TRUE) {

			delta = timer.getDelta();
			accumulator += delta;

			glfwPollEvents();

			while (accumulator >= interval) {
				//Logic

				networkManager.upload();

				moveCamera();

				Model car = GameStateMachine.entities.get(playerId);

				Vector3f modelPos = new Vector3f(car.getPosition());
				Vector3f modelDir = new Vector3f(car.getDirection());

				playerDir = new Vector3f(modelDir.x, modelDir.y - 0.2f, modelDir.z);

				playerCamera.setCameraPos(new Vector3f(modelPos.x, modelPos.y + 4.5f, modelPos.z));
				playerCamera.setCameraDirection(playerDir);
				playerCamera.moveCamera(Camera.BACKWARD_DIRECTION, 8f);

				if (!spectator) {
					spectatorCamera.setCameraPos(new Vector3f(playerCamera.getCameraPos()));
					spectatorCamera.setCameraDirection(new Vector3f(playerCamera.getCameraDirection()));
				}

				activeCamera = spectator ? spectatorCamera : playerCamera;

				timer.updateUPS();
				accumulator -= interval;
			}

			alpha = accumulator / interval;

			//Render

			Model[] toRender = new Model[GameStateMachine.entities.size() + 1];
			GameStateMachine.entities.toArray(toRender);
			toRender[toRender.length - 1] = track;

			renderer.render(toRender, activeCamera.getMatrix(), activeCamera.getCameraPos(), shaderProgram);
			timer.updateFPS();

			timer.update();

			System.out.println("fps: " + timer.getFPS() + " ups: " + timer.getUPS());

			glfwSwapBuffers(window);
		}

		shaderProgram.destroy();
	}

}
