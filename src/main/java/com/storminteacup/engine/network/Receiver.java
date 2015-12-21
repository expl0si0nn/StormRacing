package com.storminteacup.engine.network;

import com.storminteacup.engine.models.Model;
import com.storminteacup.engine.states.GameStateMachine;
import com.storminteacup.engine.states.Player;
import com.storminteacup.engine.utils.Buffers;
import org.joml.Vector3f;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;


/**
 * Created by Storminteacup on 21-Dec-15.
 */
public class Receiver implements Runnable {

	Thread thread;

	public void start() {
		thread = new Thread(this, "network.receiver");
		thread.start();
	}

	@Override
	public void run() {
		int packageStart = 0xFFFFA000;
		int packageEnd = 0xFFFFA100;
		int blockStart = 0xFFFFB000;
		int blockEnd = 0xFFFFB100;
		while(true) {
			ArrayList<Model> entities = new ArrayList<Model>();
			ArrayList<Player> players = new ArrayList<Player>();
			byte[] received;
			try {
				received = Connection.receiveData();
				int pos = 0;
				int currentFlag = Buffers.bytesToInt(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});
				if(currentFlag != packageStart)
					throw new RuntimeException("Package broken");
				while(currentFlag != packageEnd) {
					int flag = Buffers.bytesToInt(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});
					if(flag != blockStart)
						throw new RuntimeException("Package broken");

					int playerId = Buffers.bytesToInt(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});
					int meshId = Buffers.bytesToInt(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});
					Vector3f position = new Vector3f();
					position.x = Buffers.bytesToFloat(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});
					position.y = Buffers.bytesToFloat(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});
					position.z = Buffers.bytesToFloat(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});

					Vector3f rotation = new Vector3f();
					rotation.x = Buffers.bytesToFloat(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});
					rotation.y = Buffers.bytesToFloat(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});
					rotation.z = Buffers.bytesToFloat(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});

					Vector3f direction = new Vector3f();
					direction.x = Buffers.bytesToFloat(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});
					direction.y = Buffers.bytesToFloat(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});
					direction.z = Buffers.bytesToFloat(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});

					flag = Buffers.bytesToInt(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});
					if(flag != blockEnd)
						throw new RuntimeException("Package broken");

					currentFlag = Buffers.bytesToInt(new byte[]{received[pos++], received[pos++], received[pos++], received[pos++]});

					Model model = new Model(meshId);
					model.setPosition(position);
					model.setRotation(rotation);
					model.setDirection(direction);
					entities.add(model);

					Player player = new Player(playerId);
					player.setModelId(model.id);
					players.add(player);
				}

				GameStateMachine.entities = entities;
				GameStateMachine.players = players;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
