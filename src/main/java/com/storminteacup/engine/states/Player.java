package com.storminteacup.engine.states;

/**
 * Created by Storminteacup on 21-Dec-15.
 */
public class Player {
	private int id;
	private int modelId;
	private String name;

	public Player(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public int getModelId() {
		return modelId;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
