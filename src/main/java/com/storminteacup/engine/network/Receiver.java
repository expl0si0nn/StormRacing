package com.storminteacup.engine.network;

import com.storminteacup.engine.models.Model;
import com.storminteacup.engine.states.GameStateMachine;
import com.storminteacup.engine.states.Player;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
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
		int packageStart = 0x0000C000;
		int packageEnd   = 0x0000C100;
		int blockStart   = 0x0000F000;
		int blockEnd     = 0x0000F100;
		while(true) {
			ArrayList<Model> entities = new ArrayList<Model>();
			ArrayList<Player> players = new ArrayList<Player>();
			byte[] received;
			try {
				received = GameConnection.receiveData();
				ByteBuffer buffer = ByteBuffer.wrap(received);
				int pos = 0;
				int currentFlag = buffer.getInt(pos);
				pos += 4;
				if(currentFlag != packageStart)
					continue;
				currentFlag = buffer.getInt(pos);
				pos += 4;
				while(currentFlag != packageEnd) {
					if(currentFlag != blockStart)
						throw new RuntimeException("Package broken");

					int playerId = buffer.getInt(pos);
					pos += 4;
					int meshId = buffer.getInt(pos);
					pos += 4;
					Vector3f position = new Vector3f();
					position.x = buffer.getFloat(pos);
					pos += 4;
					position.y = buffer.getFloat(pos);
					pos += 4;
					position.z = buffer.getFloat(pos);
					pos += 4;

					Vector3f rotation = new Vector3f();
					rotation.x = buffer.getFloat(pos);
					pos += 4;
					rotation.y = buffer.getFloat(pos);
					pos += 4;
					rotation.z = buffer.getFloat(pos);
					pos += 4;

					Vector3f direction = new Vector3f();
					direction.x = buffer.getFloat(pos);
					pos += 4;
					direction.y = buffer.getFloat(pos);
					pos += 4;
					direction.z =buffer.getFloat(pos);
					pos += 4;

					currentFlag = buffer.getInt(pos);
					pos += 4;
					if(currentFlag != blockEnd)
						throw new RuntimeException("Package broken");

					currentFlag = buffer.getInt(pos);
					pos += 4;

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
