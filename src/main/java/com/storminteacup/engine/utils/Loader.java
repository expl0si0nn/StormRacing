package com.storminteacup.engine.utils;

import com.storminteacup.engine.graphics.Image;
import com.storminteacup.engine.graphics.Texture2D;
import com.storminteacup.engine.models.Material;
import com.storminteacup.engine.models.Mesh;

import com.storminteacup.engine.models.MeshPart;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.joml.*;
import org.lwjgl.BufferUtils;

import static org.lwjgl.stb.STBImage.*;


/**
 * Created by Storminteacup on 03-Dec-15.
 */
public class Loader {

	public static ArrayList<Material> loadMtl(String path) {

		int lastSl = 0;
		for(int i = 0; i < path.length(); i++) {
			if(path.charAt(i) == '/')
				lastSl = i;
		}
		String dir = path.substring(0, lastSl + 1);

		ArrayList<Material> res = new ArrayList<Material>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;

			Material current = null;

			while ((line = reader.readLine()) != null) {
				String pline = "";
				boolean space = false;
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == ' ' || line.charAt(i) == '\t') {
						if (!space && pline.length() > 0)
							pline += " ";
						space = true;
					} else {
						space = false;
						pline += line.charAt(i);
					}

				}
				line = pline;

				if(line.startsWith("newmtl ")) {
					if(current != null)
						res.add(current);

					current = new Material(line.substring(7));
				} else if (line.startsWith("Ka ")) {
					String[] coords = line.split(" ");
					if (coords.length != 4)
						throw new RuntimeException("Cant load material file!");
					Vector3f vec = new Vector3f();
					vec.x = Float.parseFloat(coords[1]);
					vec.y = Float.parseFloat(coords[2]);
					vec.z = Float.parseFloat(coords[3]);
					current.colorAmbient = vec;
				} else if (line.startsWith("Kd ")) {
					String[] coords = line.split(" ");
					if (coords.length != 4)
						throw new RuntimeException("Cant load material file!");
					Vector3f vec = new Vector3f();
					vec.x = Float.parseFloat(coords[1]);
					vec.y = Float.parseFloat(coords[2]);
					vec.z = Float.parseFloat(coords[3]);
					current.colorDiffuse = vec;
				} else if (line.startsWith("Ks ")) {
					String[] coords = line.split(" ");
					if (coords.length != 4)
						throw new RuntimeException("Cant load material file!");
					Vector3f vec = new Vector3f();
					vec.x = Float.parseFloat(coords[1]);
					vec.y = Float.parseFloat(coords[2]);
					vec.z = Float.parseFloat(coords[3]);
					current.colorSpecular = vec;
				} else if (line.startsWith("d ")) {
					float dissolved = Float.parseFloat(line.substring(2));
					current.dissolved = dissolved;
				} else if (line.startsWith("illum ")) {
					int illumination = Integer.parseInt(line.substring(6));
					current.illumination = illumination;
				}  else if (line.startsWith("Ns ")) {
					float shininess = Float.parseFloat(line.substring(3));
					current.shininess = shininess;
				} else if(line.startsWith("map_Ka ")) {
					String texPath = line.substring(7);
					int lastSl1 = 0;
					for(int i = 0; i < texPath.length(); i++) {
						if(texPath.charAt(i) == '/' || texPath.charAt(i) == '\\')
							lastSl1 = i;
					}
					texPath = dir + texPath.substring(lastSl1);
					current.mapAmbient = new Texture2D(texPath);
					current.mapAmbient.create();
				} else if(line.startsWith("map_Kd ")) {
					String texPath = line.substring(7);
					int lastSl1 = 0;
					for(int i = 0; i < texPath.length(); i++) {
						if(texPath.charAt(i) == '/' || texPath.charAt(i) == '\\')
							lastSl1 = i + 1;
					}
					texPath = dir + texPath.substring(lastSl1);
					current.mapDiffuse = new Texture2D(texPath);
					current.mapDiffuse.create();
				} else if(line.startsWith("map_Ks ")) {
					String texPath =  line.substring(7);
					int lastSl1 = 0;
					for(int i = 0; i < texPath.length(); i++) {
						if(texPath.charAt(i) == '/' || texPath.charAt(i) == '\\')
							lastSl1 = i;
					}
					texPath = dir + texPath.substring(lastSl1);
					current.mapSpecular = new Texture2D(texPath);
					current.mapSpecular.create();
				}
			}

			if(current != null)
				res.add(current);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		return res;
	}

	public static Mesh loadObj(String path) {

		int lastSl = 0;
		for(int i = 0; i < path.length(); i++) {
			if(path.charAt(i) == '/')
				lastSl = i;
		}
		String dir = path.substring(0, lastSl + 1);

		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
		ArrayList<Vector2f> textureCoordinates = new ArrayList<Vector2f>();
		ArrayList<String> faces = new ArrayList<String>();
		ArrayList<Integer> faceMaterials = new ArrayList<Integer>();
		HashMap<String, Integer> materials = new HashMap<String, Integer>();

		String mtlLib = "";

		Vector3f v1 = new Vector3f();
		Vector3f v2 = new Vector3f();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;

			int currentMaterial = -1;
			int materialIndex = 0;

			while ((line = reader.readLine()) != null) {
				String pline = "";
				boolean space = false;
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == ' ' || line.charAt(i) == '\t') {
						if (!space && pline.length() > 0)
							pline += " ";
						space = true;
					} else {
						space = false;
						pline += line.charAt(i);
					}

				}
				line = pline;
				if (line.startsWith("v ")) {
					String[] coords = line.split(" ");
					if (coords.length != 4)
						throw new RuntimeException("Cant load model file!");
					Vector3f vec = new Vector3f();
					vec.x = Float.parseFloat(coords[1]);
					vec.y = Float.parseFloat(coords[2]);
					vec.z = Float.parseFloat(coords[3]);
					vertices.add(vec);
					if(vec.x > v1.x)
						v1 = new Vector3f(vec);
					if(vec.x < v2.x)
						v2 = new Vector3f(vec);
				} else if (line.startsWith("vn ")) {
					String[] coords = line.split(" ");
					if (coords.length != 4)
						throw new RuntimeException("Cant load model file!");
					Vector3f vec = new Vector3f();
					vec.x = Float.parseFloat(coords[1]);
					vec.y = Float.parseFloat(coords[2]);
					vec.z = Float.parseFloat(coords[3]);
					normals.add(vec);
				} else if (line.startsWith("vt ")) {
					String[] coords = line.split(" ");
					Vector2f vec = new Vector2f();
					vec.x = Float.parseFloat(coords[1]);
					vec.y = Float.parseFloat(coords[2]);
					textureCoordinates.add(vec);
				} else if (line.startsWith("f ")) {
					String[] verts_ = line.split(" ");
					for (int i = 2; i < (verts_.length - 1); i++) {
						faces.add(verts_[1] + " " + verts_[i] + " " + verts_[i + 1]);
						faceMaterials.add(currentMaterial);
					}
				} else if(line.startsWith("mtllib ")) {
					mtlLib = line.substring(7);

				} else if (line.startsWith("usemtl ")) {
					String materialName = line.substring(7);
					if(!materials.containsKey(materialName))
						materials.put(materialName, materialIndex++);
					currentMaterial = materials.get(materialName);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		ArrayList<ArrayList<Float>> parts = new ArrayList<ArrayList<Float>>();
		for(int i = 0; i < materials.size(); i++)
			parts.add(new ArrayList<Float>());
		MeshPart[] meshParts = new MeshPart[materials.size()];

		for (int i = 0; i < faces.size(); i++) {
			String[] verts = faces.get(i).split(" ");
			int[] vi = new int[3];

			int partID = faceMaterials.get(i);

			for (int j = 0; j < verts.length; j++) {
				String vert = verts[j];
				String[] inds = vert.split("/");
				int i1 = Integer.parseInt(inds[0]) - 1;
				int i2 = -1;
				if (inds[1].length() > 0)
					i2 = Integer.parseInt(inds[1]) - 1;
				int i3 = Integer.parseInt(inds[2]) - 1;
				vi[j] = i1;

				parts.get(partID).add(vertices.get(i1).x);
				parts.get(partID).add(vertices.get(i1).y);
				parts.get(partID).add(vertices.get(i1).z);

				parts.get(partID).add(normals.get(i3).x);
				parts.get(partID).add(normals.get(i3).y);
				parts.get(partID).add(normals.get(i3).z);

				if(i2 == -1) {
					parts.get(partID).add(0.0f);
					parts.get(partID).add(0.0f);
				} else {
					parts.get(partID).add(textureCoordinates.get(i2).x);
					parts.get(partID).add(textureCoordinates.get(i2).y);
				}
			}
		}

		ArrayList<Material> materialsList = loadMtl(dir + mtlLib);
		Material[] mats = new Material[materialsList.size()];

		for(int i = 0; i < materialsList.size(); i++) {
			if(materials.containsKey(materialsList.get(i).getName()))
				mats[materials.get(materialsList.get(i).getName())] = materialsList.get(i);
		}

		for(int i = 0; i < parts.size(); i++) {
			float[] data = new float[parts.get(i).size()];
			for(int j = 0; j < parts.get(i).size(); j++)
				data[j] = parts.get(i).get(j);

			meshParts[i] = new MeshPart(data);
			meshParts[i].setMaterial(mats[i]);
		}

		Mesh res = new Mesh(meshParts);
		//res.setScaling(1.0f);
		res.setScaling(10.0f / (v1.x - v2.x));
		return res;
	}

	public static CharSequence loadShaderSource(String path) {
		StringBuilder shaderSource = new StringBuilder();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			while ((line = reader.readLine()) != null)
				shaderSource.append(line).append("\n");
			reader.close();
		} catch (IOException e) {
			System.err.println("Cant read shader");
			e.printStackTrace();
			System.exit(-1);
		}

		return shaderSource;
	}


	public static Image loadImg(String path) {

		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		IntBuffer comp = BufferUtils.createIntBuffer(1);

		ByteBuffer img = stbi_load(path, w, h, comp, 4);

		int width = w.get(0);
		int height = h.get(0);
		int components = comp.get(0);

		if ( width == 0) {
			System.err.println("Failed to read image at " + path + " " + stbi_failure_reason());
			System.exit(-1);
		}

		img.flip();

		Image res = new Image(width, height);
		res.setImg(img);
		return res;
	}

}
