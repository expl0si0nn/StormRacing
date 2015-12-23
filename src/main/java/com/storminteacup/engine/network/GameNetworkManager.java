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

	public int joinGame(int carId) {
		int packageStart = 0x0000A000;
		int packageEnd = 0x0000A100;

		try {
			ByteBuffer data = ByteBuffer.allocate(1024);
			data.putInt(packageStart);
			data.putInt(4, carId);
			data.putInt(8, packageEnd);
			GameConnection.sendData(data.array());

			int trackId = 0;

			while (true) {
				byte[] rec = GameConnection.receiveData();
				ByteBuffer received = ByteBuffer.wrap(rec);
				int pos = 0;
				int currentFlag = received.getInt(pos);
				pos += 4;
				if(currentFlag != packageStart)
					continue;
				trackId = received.getInt(pos);
				return trackId;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
