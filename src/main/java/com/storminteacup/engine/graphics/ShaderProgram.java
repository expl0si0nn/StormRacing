package com.storminteacup.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Storminteacup on 03-Dec-15.
 */
public class ShaderProgram {

	private int programID;

	private Shader vertexShader;
	private Shader fragmentShader;

	public ShaderProgram(Shader vertexShader, Shader fragmentShader) {
		this.vertexShader = vertexShader;
		this.fragmentShader = fragmentShader;
	}

	public void create() {
		programID = glCreateProgram();
		glAttachShader(programID, vertexShader.getID());
		glAttachShader(programID, fragmentShader.getID());
		glLinkProgram(programID);

		if(glGetProgrami(programID, GL_LINK_STATUS) != GL_TRUE) {
			System.err.println("Cant link shader program");
			System.err.println(glGetShaderInfoLog(programID, 500));
			System.exit(-1);
		}
	}

	public void use() {
		glUseProgram(programID);
	}

	public int getUniformLocation(String variableName) {
		return glGetUniformLocation(programID, variableName);
	}

	public void setUniform(String name, int val) {
		glUniform1i(getUniformLocation(name), val);
	}

	public void setUniform(String name, float val) {
		glUniform1f(getUniformLocation(name), val);
	}

	public void setUniform(String name, Vector3f val) {
		glUniform3f(getUniformLocation(name), val.x, val.y, val.z);
	}

	public void setUniform(String name, Matrix4f val) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(16);
		val.get(buf);
		glUniformMatrix4fv(getUniformLocation(name), false, buf);
	}

	public void destroy() {
		glDeleteProgram(programID);
	}

	public int getProgramID() {
		return programID;
	}

	public Shader getVertexShader() {
		return vertexShader;
	}

	public Shader getFragmentShader() {
		return fragmentShader;
	}
}
