package com.storminteacup.engine.states;

import com.storminteacup.engine.models.Model;

import java.util.ArrayList;

/**
 * Created by Storminteacup on 21-Dec-15.
 */
public class GameStateMachine {

	public static ArrayList<Model> entities;

	public static void init() {
		entities = new ArrayList<Model>();
	}

	public static void addModel(Model model) {
		entities.add(new Model(model));
	}

	public static void removeModel(int id) {
		for(int i = 0; i < entities.size(); i++) {
			if(entities.get(i).id == id) {
				entities.remove(i);
				break;
			}
		}
	}
}
