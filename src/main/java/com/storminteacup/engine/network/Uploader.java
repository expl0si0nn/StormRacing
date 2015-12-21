package com.storminteacup.engine.network;

import com.storminteacup.engine.input.Input;
import com.storminteacup.engine.utils.Buffers;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Storminteacup on 21-Dec-15.
 */
public class Uploader implements Runnable {

	Thread thread;

	private boolean send = false;

	public void start() {
		thread = new Thread(this, "network.uploader");
		thread.start();
	}

	@Override
	public void run() {
		while(true) {
			if(send) {
				int packageStart = 0xFFFFA000;
				int packageEnd = 0xFFFFA100;
				int blockStart = 0xFFFFB000;
				int blockEnd = 0xFFFFB100;

				ByteBuffer data = ByteBuffer.allocate(56);
				int pos = 0;
				data.putInt(pos++, packageStart);

				data.putInt(pos++, blockStart);
				data.putInt(pos++, Input.isKeyDown(Input.moveForvard) ? 1 : 0);
				data.putInt(pos++, blockEnd);

				data.putInt(pos++, blockStart);
				data.putInt(pos++, Input.isKeyDown(Input.moveBackward) ? 1 : 0);
				data.putInt(pos++, blockEnd);

				data.putInt(pos++, blockStart);
				data.putInt(pos++, Input.isKeyDown(Input.moveLeft) ? 1 : 0);
				data.putInt(pos++, blockEnd);

				data.putInt(pos++, blockStart);
				data.putInt(pos++, Input.isKeyDown(Input.moveRight) ? 1 : 0);
				data.putInt(pos++, blockEnd);

				data.putInt(pos, packageEnd);

				byte[] packed = data.array();

				try {
					Connection.sendData(packed);
				} catch (IOException e) {
					e.printStackTrace();
				}
				send = false;
			}
		}
	}

	public void setSend(boolean send) {
		this.send = send;
	}
}
