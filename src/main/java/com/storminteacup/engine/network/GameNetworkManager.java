package com.storminteacup.engine.network;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Storminteacup on 20-Dec-15.
 */
public class GameNetworkManager {

	private Receiver receiver;
	private Uploader uploader;

	private String host;
	private int port;

	public GameNetworkManager(String host, int port) {
		this.host = host;
		this.port = port;
		try {
			GameConnection.init(host, port);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void start() {
		receiver = new Receiver();
		uploader = new Uploader();
		receiver.start();
		uploader.start();
	}

	public void upload() {
		uploader.setSend(true);
	}

	public void setSpectator() {
		uploader.setSpecatator();
	}
}
