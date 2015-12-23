package com.storminteacup.engine.network;

import com.storminteacup.engine.input.Input;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Storminteacup on 21-Dec-15.
 */
public class Uploader implements Runnable {

	Thread thread;

	private boolean send = false;
	private boolean specatator = false;

	public void start() {
		thread = new Thread(this, "network.uploader");
		thread.start();
	}

	@Override
	public void run() {
		System.out.println("Yay!");
		while(true) {
			System.out.println("Yay!!");
			if(send && !specatator) {
				System.out.println("Yay!!!");
				int packageStart = 0x0000C000;
				int packageEnd   = 0x0000C100;
				int blockStart   = 0x0000F000;
				int blockEnd     = 0x0000F100;

				ByteBuffer data = ByteBuffer.allocate(1024);
				int pos = 0;
				data.putInt(pos, packageStart);
				pos += 4;

				if(Input.isKeyDown(Input.moveForvard)) {
					int ttwtw = 521525;
				}
				data.putInt(pos, blockStart);
				pos += 4;
				int t = Input.isKeyDown(Input.moveForvard) ? 1 : 0;
				data.putInt(pos, t);
				pos += 4;

				int t1 = Input.isKeyDown(Input.moveBackward) ? 1 : 0;
				data.putInt(pos, t1);
				pos += 4;

				int t2 = Input.isKeyDown(Input.moveLeft) ? 1 : 0;
				data.putInt(pos, t2);
				pos += 4;

				int t3 = Input.isKeyDown(Input.moveRight) ? 1 : 0;
				data.putInt(pos, t3);
				pos += 4;
				data.putInt(pos, blockEnd);
				pos += 4;

				data.putInt(pos, packageEnd);

				byte[] packed = data.array();

				try {
					GameConnection.sendData(packed);
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

	public void setSpecatator() {
		this.specatator = !specatator;
	}
}
