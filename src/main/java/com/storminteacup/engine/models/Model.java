package com.storminteacup.engine.models;

import org.joml.Vector3f;

/**
 * Created by Storminteacup on 03-Dec-15.
 */
public class Model {

	private static int modelCount = 0;

	public int id;

	private Mesh mesh;

	private Vector3f direction;
	private Vector3f position;
	private Vector3f rotation;
	private float scaling;

	public Model(Mesh mesh) {
		id = modelCount++;
		this.mesh = mesh;
		scaling = mesh.getScaling();
		direction = new Vector3f();
		position = new Vector3f();
		rotation = new Vector3f();
	}

	public Model(Model model) {
		this.id = model.id;
		this.mesh = model.mesh;
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
		return mesh;
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
}
