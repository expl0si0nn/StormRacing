package com.storminteacup.engine.graphics;

import com.storminteacup.engine.utils.Loader;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Storminteacup on 04-Dec-15.
 */
public class Texture2D {

	private String path;

	private int textureID;

	Image image;

	public Texture2D(String path) {
		this.path = path;
	}

	public void create() {
		image = Loader.loadImg(path);

		textureID = glGenTextures();


		glBindTexture(GL_TEXTURE_2D, textureID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_NEAREST);

		float maxAnisotropic = glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
		glTexParameterf(GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropic);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, image.getImg());
		glGenerateMipmap(GL_TEXTURE_2D);

		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void destroy() {
		glDeleteTextures(textureID);
	}

	public int getTextureID() {
		return textureID;
	}
}
