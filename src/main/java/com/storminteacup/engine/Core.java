package com.storminteacup.engine;

import com.storminteacup.engine.graphics.Shader;
import com.storminteacup.engine.graphics.ShaderProgram;
import com.storminteacup.engine.input.Input;
import com.storminteacup.engine.models.Mesh;
import com.storminteacup.engine.models.Model;

import com.storminteacup.engine.renderEngine.Renderer;
import com.storminteacup.engine.states.GameStateMachine;
import com.storminteacup.engine.utils.Loader;
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

	private Renderer renderer;
	private Camera spectatorCamera;
	private Camera playerCamera;

	boolean spectator = false;

	private int screenWidth = 1680;
	private int screenHeight = 1050;

	private	int playerId = 0;

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
				if(Input.keys[GLFW_KEY_F1])
					spectator = !spectator;
				if(Input.keys[GLFW_KEY_UP])
					playerId = (playerId + 1) % 3;
			}
		});


		glfwSetCursorPosCallback(window, mouseCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				if(firstMouse) {
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

				if(!invertMouse)
					yoffset = 0.0f - yoffset;

				spectatorCamera.moveDirection(xoffset, yoffset);
			}
		} );

	}

	private void moveCamera() {
		if(Input.keys[GLFW_KEY_W])
			spectatorCamera.moveCamera(Camera.FORWARD_DIRECTION, 1.0f);
		if(Input.keys[GLFW_KEY_S])
			spectatorCamera.moveCamera(Camera.BACKWARD_DIRECTION, 1.0f);
		if(Input.keys[GLFW_KEY_A])
			spectatorCamera.moveCamera(Camera.LEFT_DIRECTION, 1.0f);
		if(Input.keys[GLFW_KEY_D])
			spectatorCamera.moveCamera(Camera.RIGHT_DIRECTION, 1.0f);
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
		Model[] car = new Model[3];

		car[0] = new Model(carMesh1);
		car[0].setPosition(new Vector3f(0.0f, 1.0f, 0.0f));
		car[0].setDirection(new Vector3f(-1.0f, 0.0f, 0.0f));

		car[1] = new Model(carMesh2);
		car[1].setPosition(new Vector3f(20.0f, 1.0f, 0.0f));
		car[1].setDirection(new Vector3f(-1.0f, 0.0f, 0.0f));

		car[2] = new Model(carMesh3);
		car[2].setPosition(new Vector3f(40.0f, 1.0f, 0.0f));
		car[2].setDirection(new Vector3f(-1.0f, 0.0f, 0.0f));

		Model track = new Model(mesh2);
		track.setScaling(1.0f);

		GameStateMachine.addModel(track);
		GameStateMachine.addModel(car[0]);
		GameStateMachine.addModel(car[1]);
		GameStateMachine.addModel(car[2]);

		//Player
		Vector3f playerDir = new Vector3f();


		//Temporary moving
		float modelRotate = 0.0f;
		float fwspeed = 0.0f;
		float angspeed = 0.0f;

		glViewport(0, 0, screenWidth, screenHeight);

		glEnable(GL_DEPTH_TEST);
		glEnable(GL_MULTISAMPLE);
		//glEnable(GL_BLEND);
		//glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		while(glfwWindowShouldClose(window) != GL_TRUE) {

			glfwPollEvents();

			moveCamera();

			//Temporary moving
			car[0] = new Model(GameStateMachine.entities.get(1));
			car[1] = new Model(GameStateMachine.entities.get(2));
			car[2] = new Model(GameStateMachine.entities.get(3));

			fwspeed = 0.0f;
			angspeed = 0.0f;

			if(!spectator) {
				if(Input.keys[GLFW_KEY_W])
					fwspeed = 0.4f;
				if(Input.keys[GLFW_KEY_S])
					fwspeed = -0.4f;
				if(Input.keys[GLFW_KEY_D])
					angspeed = -1.5f;
				if(Input.keys[GLFW_KEY_A])
					angspeed = 1.5f;
			}

			modelRotate += angspeed;


			Vector3f modelPos = new Vector3f(car[playerId].getPosition());
			Vector3f modelDir = new Vector3f(car[playerId].getDirection());

			Vector3f pr = car[playerId].getRotation();
			car[playerId].setRotation(new Vector3f(0.0f, pr.y + (float) Math.toRadians(angspeed), 0.0f));

			Vector4f newModelDir = new Vector4f(modelDir, 1.0f)
					.mul(new Matrix4f()
							.identity()
							.rotateY((float) Math.toRadians(angspeed)));
			modelDir = new Vector3f(newModelDir.x, newModelDir.y, newModelDir.z);
			car[playerId].setDirection(modelDir);

			modelPos.add(new Vector3f(modelDir).mul(fwspeed));
			car[playerId].setPosition(modelPos);

			GameStateMachine.entities.get(1 + playerId).setPosition(car[playerId].getPosition());
			GameStateMachine.entities.get(1 + playerId).setRotation(car[playerId].getRotation());
			GameStateMachine.entities.get(1 + playerId).setDirection(car[playerId].getDirection());

			playerDir = new Vector3f(modelDir.x, modelDir.y - 0.2f, modelDir.z);

			playerCamera.setCameraPos(new Vector3f(modelPos.x, modelPos.y + 4.5f, modelPos.z));
			playerCamera.setCameraDirection(playerDir);
			playerCamera.moveCamera(Camera.BACKWARD_DIRECTION, 8f);

			if(!spectator) {
				spectatorCamera.setCameraPos(new Vector3f(playerCamera.getCameraPos()));
				spectatorCamera.setCameraDirection(new Vector3f(playerCamera.getCameraDirection()));
			}

			Camera activeCamera = spectator? spectatorCamera : playerCamera;

			//Render

			Model[] toRender = new Model[GameStateMachine.entities.size()];

			GameStateMachine.entities.toArray(toRender);

			renderer.render(toRender, activeCamera.getMatrix(), activeCamera.getCameraPos(), shaderProgram);

			glfwSwapBuffers(window);
		}

		shaderProgram.destroy();
	}

}
