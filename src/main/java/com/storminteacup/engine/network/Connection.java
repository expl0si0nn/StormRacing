package com.storminteacup.engine.network;

import java.io.IOException;
import java.net.*;

/**
 * Created by Storminteacup on 21-Dec-15.
 */
public class Connection {

	private static DatagramSocket socket;
	private static InetAddress address;
	private static int port;

	public static void init(String _address, int port) throws Exception {
		socket = new DatagramSocket();
		address = InetAddress.getByName(_address);
		Connection.port = port;
	}

	public static void sendData(byte[] data) throws IOException {
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		socket.send(packet);
	}

	public static byte[] receiveData() throws IOException {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		socket.receive(packet);
		return packet.getData();
	}
}
