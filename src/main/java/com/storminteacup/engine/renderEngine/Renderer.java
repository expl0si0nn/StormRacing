package com.storminteacup.engine.renderEngine;

import com.storminteacup.engine.graphics.ShaderProgram;
import com.storminteacup.engine.models.Material;
import com.storminteacup.engine.models.Mesh;
import com.storminteacup.engine.models.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Created by Storminteacup on 20-Dec-15.
 */
public class Renderer{

	private int screenWidth;
	private int screenHeight;

	private ShaderProgram shaderProgram;

	private Matrix4f modelMatrix;
	private Matrix4f normalMatrix;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	private Vector3f viewPos;


	public Renderer(int screenWidth, int screenHeight) {

		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		modelMatrix = new Matrix4f();
		normalMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f()
				.perspective((float) Math.toRadians(45.0D), (float) screenWidth / (float) screenHeight, 0.1f, 10000.0f);
	}

	public void render(Model[] models, Matrix4f _viewMatrix, Vector3f _viewPos, ShaderProgram _shaderProgram) {

		this.shaderProgram = _shaderProgram;
		viewMatrix = new Matrix4f(_viewMatrix);
		viewPos = new Vector3f(_viewPos);

		//Prepare
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		shaderProgram.use();

		//Light
		shaderProgram.setUniform("directlight.direction", new Vector3f(00.0f, -1.0f, 0.0f));
		shaderProgram.setUniform("directlight.ambient", new Vector3f(0.05f, 0.05f, 0.05f));
		shaderProgram.setUniform("directlight.diffuse", new Vector3f(0.4f, 0.4f, 0.4f));
		shaderProgram.setUniform("directlight.specular", new Vector3f(0.5f, 0.5f, 0.5f));

		shaderProgram.setUniform("pointLight.position", new Vector3f(0.0f, 500.0f, 0.0f));
		shaderProgram.setUniform("pointLight.ambient", new Vector3f(0.7f, 0.7f, 0.7f));
		shaderProgram.setUniform("pointLight.diffuse", new Vector3f(0.9f, 0.8f, 0.9f));
		shaderProgram.setUniform("pointLight.specular", new Vector3f(1.0f, 1.0f, 1.0f));
		shaderProgram.setUniform("pointLight.constant", 1.0f);
		shaderProgram.setUniform("pointLight.linear", 0.0014f);
		shaderProgram.setUniform("pointLight.quadratic", 0.000007f);

		shaderProgram.setUniform("view", viewMatrix);
		shaderProgram.setUniform("projection", projectionMatrix);


		shaderProgram.setUniform("viewPos", viewPos);


		for(Model model : models) {
			renderModel(model);
		}
	}

	private void renderModel(Model model) {
		modelMatrix = new Matrix4f().identity()
					.translate(model.getPosition())
					.rotateY(model.getRotation().y)
					.scale(model.getScaling());
		// add rotateX and rotateY

		normalMatrix = new Matrix4f(modelMatrix)
				.invert().transpose();
		shaderProgram.setUniform("model", modelMatrix);
		shaderProgram.setUniform("normalMatrix", normalMatrix);

		Mesh currentMesh = model.getMesh();

		for(int i = 0; i < currentMesh.parts.length; i++) {
			currentMesh.parts[i].bind();

			Material mat = currentMesh.parts[i].getMaterial();

			int[] texEnabled = new int[3];
			if(mat.mapAmbient != null)
				texEnabled[0] = 1;
			if(mat.mapDiffuse != null)
				texEnabled[1] = 1;
			if(mat.mapSpecular != null)
				texEnabled[2] = 1;

			shaderProgram.setUniform("material.ambient", mat.colorAmbient);
			shaderProgram.setUniform("material.diffuse", mat.colorDiffuse);
			shaderProgram.setUniform("material.specular", mat.colorSpecular);
			shaderProgram.setUniform("material.shininess", mat.shininess); //32.0f
			shaderProgram.setUniform("material.dissolved", mat.dissolved);

			shaderProgram.setUniform("material.texEnabled[0]", texEnabled[0]);
			shaderProgram.setUniform("material.texEnabled[1]", texEnabled[1]);
			shaderProgram.setUniform("material.texEnabled[2]", texEnabled[2]);

			if(texEnabled[0] == 1) {
				glActiveTexture(GL_TEXTURE0);
				mat.mapAmbient.bind();
				shaderProgram.setUniform("material.mapAmbient", 0);
			}
			if(texEnabled[1] == 1) {
				glActiveTexture(GL_TEXTURE1);
				mat.mapDiffuse.bind();
				shaderProgram.setUniform("material.mapDiffuse", 1);
			}
			if(texEnabled[2] == 1) {
				glActiveTexture(GL_TEXTURE2);
				mat.mapSpecular.bind();
				shaderProgram.setUniform("material.mapSpecular", 2);
			}

			glDrawArrays(GL_TRIANGLES, 0, currentMesh.parts[i].getVertexCount());

			currentMesh.parts[i].unbind();
		}
	}
}
