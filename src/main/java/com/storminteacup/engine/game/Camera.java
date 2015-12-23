package com.storminteacup.engine.game;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Storminteacup on 05-Dec-15.
 */
public class Camera {

	public static final int FORWARD_DIRECTION  = 0;
	public static final int BACKWARD_DIRECTION = 1;
	public static final int RIGHT_DIRECTION    = 2;
	public static final int LEFT_DIRECTION     = 3;

	private float cameraSpeed;
	private float yaw;
	private float pitch;

	private Vector3f cameraPos;
	private Vector3f cameraDirection;
	private Vector3f cameraUp;


	public Camera(Vector3f cameraPos, Vector3f cameraDirection) {
		this.cameraSpeed = 0.04f;
		this.yaw = -90.0f;
		this.pitch = 0.0f;

		this.cameraPos = new Vector3f(cameraPos);
		this.cameraDirection = new Vector3f(cameraDirection);
		this.cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
	}

	public void moveDirection(float xoffset, float yoffset) {
		yaw += xoffset;
		pitch += yoffset;
		pitch = Math.max(-89.0f, pitch);
		pitch = Math.min(89.0f, pitch);

		Vector3f direction = new Vector3f();
		direction.x = (float) (Math.cos(Math.toRadians((double) pitch)) * Math.cos(Math.toRadians((double) yaw)));
		direction.y = (float) Math.sin(Math.toRadians((double) pitch));
		direction.z = (float) (Math.cos(Math.toRadians((double) pitch)) * Math.sin(Math.toRadians((double) yaw)));
		direction.normalize();
		cameraDirection = direction;
	}

	public void moveCamera(int direction, float offset) {
		if(offset == 0.0f)
			offset = cameraSpeed;
		if(direction == FORWARD_DIRECTION)
			cameraPos.add(new Vector3f(cameraDirection).mul(offset));
		if(direction == BACKWARD_DIRECTION)
			cameraPos.sub(new Vector3f(cameraDirection).mul(offset));
		if(direction == LEFT_DIRECTION)
			cameraPos.sub((new Vector3f(cameraDirection).cross(cameraUp)).normalize().mul(offset));
		if(direction == RIGHT_DIRECTION)
			cameraPos.add((new Vector3f(cameraDirection).cross(cameraUp)).normalize().mul(offset));

	}

	public Matrix4f getMatrix() {
		return new Matrix4f().lookAt(cameraPos, new Vector3f(cameraPos).add(cameraDirection), cameraUp);
	}

	public Vector3f getCameraPos() {
		return cameraPos;
	}

	public void setCameraPos(Vector3f cameraPos) {
		this.cameraPos = cameraPos;
	}

	public Vector3f getCameraDirection() {
		return cameraDirection;
	}

	public void setCameraDirection(Vector3f cameraDirection) {
		this.cameraDirection = cameraDirection;
	}

	public Vector3f getCameraUp() {
		return cameraUp;
	}
}
