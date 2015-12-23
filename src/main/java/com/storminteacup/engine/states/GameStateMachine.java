package com.storminteacup.engine.states;

import com.storminteacup.engine.models.Model;

import java.util.ArrayList;

/**
 * Created by Storminteacup on 21-Dec-15.
 */
public class GameStateMachine {

	public static ArrayList<Model> entities;
	public static ArrayList<Player> players;

	public static void init() {
		entities = new ArrayList<Model>();
		players = new ArrayList<Player>();
	}

	public static void addModel(Model model) {
		entities.add(new Model(model));
	}

	public static Player getPlayer(int id) {
		for(Player player : players) {
			if(player.getId() == id)
				return player;
		}
		return null;
	}
}
