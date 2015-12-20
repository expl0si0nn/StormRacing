package com.storminteacup.engine.models;

import com.storminteacup.engine.utils.Buffers;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by Storminteacup on 16-Dec-15.
 */
public class MeshPart {

	private Material material;
	private float[] vertices;
	private int vertexCount;

	private int vaoID;
	private int vboID;

	public MeshPart(float[] vertices) {
		this.vertices = vertices;
		vertexCount = vertices.length / 8;
	}

	public void create() {
		vertexCount = vertices.length / 8;

		FloatBuffer verticesBuffer = Buffers.arrayToBuffer(vertices);

		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);


		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * 4, 3 * 4);
		glEnableVertexAttribArray(1);

		glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * 4, 6 * 4);
		glEnableVertexAttribArray(2);

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

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public float[] getVertices() {
		return vertices;
	}

	public int getVertexCount() {
		return vertexCount;
	}
}
