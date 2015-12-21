package com.storminteacup.engine.network;

/**
 * Created by Storminteacup on 20-Dec-15.
 */
public class NetworkManager {

	private static Receiver receiver;
	private static Uploader uploader;

	public NetworkManager() {
		try {
			Connection.init("127.0.0.1", 9876);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		receiver = new Receiver();
		uploader = new Uploader();
		receiver.start();
		uploader.start();
	}

	public void upload() {
		uploader.setSend(true);
	}
}
