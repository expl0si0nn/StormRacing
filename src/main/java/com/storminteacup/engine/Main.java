package com.storminteacup.engine;


/**
 * Created by Storminteacup on 11/7/2015.
 */
public class Main {
	public static void main(String args[]) {
		System.out.println("Working Directory = " +	System.getProperty("user.dir"));
		System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "target\\natives");
		System.out.println("Working Directory = " +	System.getProperty("org.lwjgl.librarypath"));
		new Core().start();
	}
}
