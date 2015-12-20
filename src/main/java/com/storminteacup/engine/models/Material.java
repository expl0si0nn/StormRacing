package com.storminteacup.engine.models;

import com.storminteacup.engine.graphics.Texture2D;
import org.joml.Vector3f;

/**
 * Created by Storminteacup on 16-Dec-15.
 */
public class Material {

	private String name;

	public Vector3f colorAmbient;
	public Vector3f colorDiffuse;
	public Vector3f colorSpecular;

	public float dissolved;
	public float shininess;
	public int illumination;

	public Texture2D mapAmbient;
	public Texture2D mapDiffuse;
	public Texture2D mapSpecular;

	public Material(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
