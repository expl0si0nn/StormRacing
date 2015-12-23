package com.storminteacup.engine.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Storminteacup on 22-Dec-15.
 */
public class MasterConnection {

	private static Socket socket;
	private static String address;
	private static int port;

	private static DataInputStream dataInputStream;
	private static DataOutputStream dataOutputStream;

	public static void init(String _address, int port) throws Exception {
		socket = new Socket(_address, port);
		address = _address;
		MasterConnection.port = port;
		dataInputStream = new DataInputStream(socket.getInputStream());
		dataOutputStream = new DataOutputStream(socket.getOutputStream());
	}

	public static void sendData(byte[] data) throws IOException {
		dataOutputStream.write(data);
		dataOutputStream.flush();
	}

	public static String receiveData() throws IOException {
		String data = dataInputStream.readUTF();
		return data;
	}
}
