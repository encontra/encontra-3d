/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.model.io;

import pt.inevo.encontra.threed.model.BufferedModel;
import pt.inevo.encontra.threed.model.geoset.BufferedMesh;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

// Object File Format
class ModelReaderOff extends ModelReader {
	
	private static final String COMMENT_SYMBOL = "#";
	
	public ModelReaderOff(String name, String extension) {
		super(name, extension);
	}
	public BufferedModel read(String filename) throws IOException {
		BufferedModel model = new BufferedModel(_name, _extension);
		
		//MYTODO: sub-divide this function
		BufferedMesh mesh = new BufferedMesh();
		int num_vertices = 0;
		int num_faces = 0;
		//int num_edges = 0;
		Scanner root = new Scanner(new FileReader(filename));
		if(!root.hasNext()) {
			root.close();
			throw new IOException("ModelReader: " + filename + " is empty");
		}
		if(root.nextLine().compareToIgnoreCase("OFF") != 0) {
			root.close();
			throw new IOException("ModelReader: " + filename + " format not correct");
		}
		
		
		while(root.hasNext()) {
			String nextLine = root.nextLine();
			if(nextLine.startsWith(COMMENT_SYMBOL))
				continue;
			
			Scanner line = new Scanner(nextLine);
			num_vertices = line.nextInt();
			num_faces = line.nextInt();
			line.close();
			break;
		}
		
		for(int i = 0; i < num_vertices; i++) {
			if(!root.hasNext()) {
				root.close();
				throw new IOException("ModelReader: " + filename + " does not contain correct number of vertices");
			}
			String nextLine = root.nextLine();
			if(nextLine.startsWith(COMMENT_SYMBOL)) {
				i--;continue;
			}
			Scanner line = new Scanner(nextLine);
			line.useLocale(Locale.US);
			float x = line.nextFloat();
			float y = line.nextFloat();
			float z = line.nextFloat();
			mesh.addVertex(x, y, z);
			line.close();
		}
		
		for(int i = 0; i < num_faces; i++) {
			if(!root.hasNext()) {
				root.close();
				throw new IOException("ModelReader: " + filename + " does not contain correct number of faces");
			}
			String nextLine = root.nextLine();
			if(nextLine.startsWith(COMMENT_SYMBOL)) {
				i--;continue;
			}
			Scanner line = new Scanner(nextLine);
			int num_face_vertices = line.nextInt();
			List<Integer> listVertices = new ArrayList<Integer>();
			for(int j = 0; j < num_face_vertices; j++) {
				listVertices.add(line.nextInt());
			}
			mesh.addFace(listVertices);
			line.close();
		}
		model.addMesh(mesh);
		root.close();
		System.out.println("Model loaded");
		
		return model;
	}
}