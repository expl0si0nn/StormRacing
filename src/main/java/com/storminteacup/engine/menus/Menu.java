package com.storminteacup.engine.menus;

import com.storminteacup.engine.graphics.Texture2D;
import com.storminteacup.engine.utils.Buffers;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Storminteacup on 22-Dec-15.
 */
public class Menu {

	private String imagePath;
	public Texture2D tex;
	private float[] vertices;
	private int vaoID;
	private int vboID;

	private String name;

	public String[] options;

	public Menu prev;

	public Menu(String name, String[] options, Menu prev) {
		this.imagePath = "src/main/resources/menus/" + name + ".png";
		tex = new Texture2D(imagePath);
		this.name = name;
		this.options = options;
		this.prev = prev;
	}

	public void create() {
		tex.create();
		float[] verts = {
				0.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 1.0f, 0.0f,
				1.0f, 1.0f, 1.0f, 1.0f,
		};
		vertices = verts;
		FloatBuffer verticesBuffer = Buffers.arrayToBuffer(vertices);

		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 4, GL_FLOAT, false, 4 * 4, 0);
		glEnableVertexAttribArray(0);

		glBindVertexArray(0);
	}

	public void bind() {
		glBindVertexArray(vaoID);
	}

	public void unbind() {
		glBindVertexArray(0);
	}

	public void destroy() {
		glDeleteVertexArrays(vaoID);
		glDeleteBuffers(vboID);
	}

	public String getName() {
		return name;
	}
}
