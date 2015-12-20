package com.storminteacup.engine.graphics;

import com.storminteacup.engine.utils.Loader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Storminteacup on 03-Dec-15.
 */
public class Shader {

	private String path;

	private int shaderID;
	private int shaderType;

	public Shader(String path, int shaderType) {
		this.path = path;
		this.shaderType = shaderType;
	}

	public void create() {
		CharSequence shaderSource = Loader.loadShaderSource(path);

		shaderID = glCreateShader(shaderType);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);

		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) != GL_TRUE) {
			System.err.println("Cant compile shader");
			System.err.println(glGetShaderInfoLog(shaderID, 500));
			System.exit(-1);
		}
	}

	public void destroy() {
		glDeleteShader(shaderID);
	}

	public int getID() {
		return shaderID;
	}

	public int getType() {
		return shaderType;
	}

	public String getPath() {
		return path;
	}

}
