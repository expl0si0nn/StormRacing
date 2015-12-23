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

}
