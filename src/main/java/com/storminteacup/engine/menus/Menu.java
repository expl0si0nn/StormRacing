package com.storminteacup.engine.menus;

/**
 * Created by Storminteacup on 22-Dec-15.
 */
public class Menu {

	private String name;

	public String[] options;

	public Menu prev;

	public Menu(String name, String[] options, Menu prev) {
		this.name = name;
		this.options = options;
		this.prev = prev;
	}

	public String getName() {
		return name;
	}
}
