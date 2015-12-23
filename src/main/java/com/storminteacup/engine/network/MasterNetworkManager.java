package com.storminteacup.engine.network;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Storminteacup on 22-Dec-15.
 */
public class MasterNetworkManager {

	private String host;
	private int port;

	public MasterNetworkManager(String host, int port) {
		this.host = host;
		this.port = port;
		try {
			MasterConnection.init(host, port);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public String sendGameRequest(int trackId) {
		int packageStart = 0x0000A000;
		int packageEnd = 0x0000A100;

		try {
			ByteBuffer data = ByteBuffer.allocate(12);
			data.putInt(packageStart);
			data.putInt(4, trackId);
			data.putInt(8, packageEnd);
			MasterConnection.sendData(data.array());

			String received = MasterConnection.receiveData();
			while(received.length() == 0)
				received = MasterConnection.receiveData();

			return received;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void joinGame() {

	}

}
