package com.storminteacup.engine.graphics;

import java.nio.ByteBuffer;

/**
 * Created by Storminteacup on 19-Dec-15.
 */
public class Image {
	private int width;
	private int height;
	private ByteBuffer img;

	public Image(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public ByteBuffer getImg() {
		return img;
	}

	public void setImg(ByteBuffer img) {
		this.img = img;
	}
}
