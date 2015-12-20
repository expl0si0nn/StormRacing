package com.storminteacup.engine.models;


/**
 * Created by Storminteacup on 11-Dec-15.
 */
public class Mesh {

	public MeshPart[] parts;
	private int vertexCount;
	private float scaling;

	public Mesh(MeshPart[] parts) {
		this.parts = parts;
		for(MeshPart part : parts) {
			vertexCount += part.getVertexCount();
		}
	}

	public void create() {
		for(int i = 0; i < parts.length; i++)
			parts[i].create();
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public float getScaling() {
		return scaling;
	}

	public void setScaling(float scaling) {
		this.scaling = scaling;
	}
}
