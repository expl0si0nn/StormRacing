package com.storminteacup.engine.models;

import org.joml.Vector3f;

import java.util.ArrayList;

/**
 * Created by Storminteacup on 03-Dec-15.
 */
public class Model {

	private static int modelCount = 0;

	public int id;

	private static ArrayList<Model> models;

	private int meshId;

	private Vector3f direction;
	private Vector3f position;
	private Vector3f rotation;
	private float scaling;

	public Model(int meshId) {
		if (models == null)
			models = new ArrayList<Model>();
		id = modelCount++;
		this.meshId = meshId;
		scaling = Mesh.getMesh(meshId).getScaling();
		direction = new Vector3f();
		position = new Vector3f();
		rotation = new Vector3f();
		models.add(this);
	}

	public Model(Model model) {
		this.id = model.id;
		this.meshId = model.meshId;
		this.direction = new Vector3f(model.getDirection());
		this.position = new Vector3f(model.getPosition());
		this.rotation = new Vector3f(model.getRotation());
		this.scaling = model.scaling;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	public Mesh getMesh() {
		return Mesh.getMesh(meshId);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public void setScaling(float scaling) {
		this.scaling = scaling;
	}

	public float getScaling() {
		return scaling;
	}

	public static Model getModel(int id) {
		return models.get(id);
	}
}
