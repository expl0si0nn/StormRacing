package com.storminteacup.engine.models;


import java.util.ArrayList;

/**
 * Created by Storminteacup on 11-Dec-15.
 */
public class Mesh {

	private static int modelCount = 0;

	public int id;

	private static ArrayList<Mesh> meshes;

	public MeshPart[] parts;
	private int vertexCount;
	private float scaling;

	public Mesh(MeshPart[] parts) {
		if (meshes == null)
			meshes = new ArrayList<Mesh>();
		id = modelCount++;
		this.parts = parts;
		for(MeshPart part : parts) {
			vertexCount += part.getVertexCount();
		}
		meshes.add(this);
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

	public static Mesh getMesh(int meshId) {
		return meshes.get(meshId);
	}
}
