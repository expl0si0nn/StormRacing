package com.storminteacup.engine;


/**
 * Created by Storminteacup on 11/7/2015.
 */
public class Main {
	public static void main(String args[]) {
		System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "target\\natives");
		new Core(args[0]).start();
	}
}
