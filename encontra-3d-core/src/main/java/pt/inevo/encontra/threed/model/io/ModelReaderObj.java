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

//Obj Wavefront
class ModelReaderObj extends ModelReader {
	
	public ModelReaderObj(String name, String extension) {
		super(name, extension);
	}

	@Override
	public BufferedModel read(String filename) throws IOException {
		BufferedModel model = new BufferedModel(_name, _extension);
		BufferedMesh mesh = new BufferedMesh();

		Scanner root = new Scanner(new FileReader(filename));
		if(!root.hasNext()) {
			root.close();
			throw new IOException("ModelReader: " + filename + " is empty");
		}
		
		while(root.hasNext()) {
			Scanner line = new Scanner(root.nextLine());
			line.useLocale(Locale.US);
			
			String type = line.next();
			if(type.compareToIgnoreCase("#") == 0) {
				//ignore all commented text
			} else if(type.compareToIgnoreCase("v") == 0) { 
				//read vertex
				
				double x = line.nextDouble();
				double y = line.nextDouble();
				double z = line.nextDouble();
				mesh.addVertex(x, y, z);
			} else if(type.compareToIgnoreCase("vt") == 0) {
				//read texture coordinate
				float u = line.nextFloat();
				float v = line.nextFloat();
				
				mesh.addTextCoord(u, v*-1); //i guess images are flipped?
				//mesh.addTextCoord(u, v);
			} else if(type.compareToIgnoreCase("vn") == 0) {
				//read normal
				float x = line.nextFloat();
				float y = line.nextFloat();
				float z = line.nextFloat();
				mesh.addNormal(x, y, z);
			} else if(type.compareToIgnoreCase("f") == 0) {
				List<Integer> v = new ArrayList<Integer>(); // vertices id's
				List<Integer> vt = new ArrayList<Integer>();// texture coordinates id's
				List<Float> vn = new ArrayList<Float>();// normal id's

				while(line.hasNext()) {
					String points[] = line.next().split("/");
					
					// Horrible Hack: index starts at 0... so -1
					if(points.length > 2) vn.add(Float.parseFloat(points[2])-1);
					if(points.length > 1) vt.add(Integer.parseInt(points[1])-1);
					v.add(Integer.parseInt(points[0])-1);
				}
				mesh.addFace(v, vt, vn);
			}
			line.close();
		}
		model.addMesh(mesh);
		root.close();
		
		System.out.println("Model loaded");
		return model;
	}
}
