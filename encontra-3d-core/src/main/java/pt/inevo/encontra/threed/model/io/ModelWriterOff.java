/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.model.io;

import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.model.geoset.Face;
import pt.inevo.encontra.threed.model.geoset.Mesh;
import pt.inevo.encontra.threed.model.geoset.Vertex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

//MyTODO: Javadoc
class ModelWriterOff extends ModelWriter {
	
	public void write(Model model) throws IOException {
		throw new IOException("Not yet implemented");
	}

	public void write(Model model, File output) throws IOException {
		FileWriter fstream = new FileWriter(output);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write("OFF"); out.newLine();
		out.write("# This file was saved using the ThOR framework."); out.newLine();
		out.write("#"); out.newLine();
		out.write("# Original File: " + model.getName()); out.newLine();
		out.write("# Original Format: " + model.getModelFormat()); out.newLine();
		out.write("#"); out.newLine();
		out.write("# Model Name: " + model.getModelName()); out.newLine();
		out.write("# Vertices: " + model.countVertices()); out.newLine();
		out.write("# Faces: " + model.countFaces()); out.newLine();
		out.write("#"); out.newLine();
		

		out.write(model.countVertices() + " " + model.countFaces() + " " + model.countEdges());
		out.newLine();
		
		// Write all meshes of the model
		List<Mesh> meshes = model.getMeshes();
		for(int m=0; m < meshes.size(); m++ ) {
			// Write all vertices
			List<Vertex> vertices = meshes.get(m).getVertices();
			for(int v=0; v < vertices.size(); v++) {
				out.write(vertices.get(v).getX() + " " + vertices.get(v).getY() + " " + vertices.get(v).getZ());
				out.newLine();
			}
			// Write all faces
			List<Face> faces = model.getMeshes().get(0).getFaces();
			for(int f=0; f < faces.size(); f++) {
				out.write("" + faces.get(f).Vertices.size());
				for(int i = 0; i < faces.get(f).Vertices.size(); i++) {
					out.write(" " + faces.get(f).Vertices.get(i));
				}
				out.newLine();
			}
		}
		out.close();
		fstream.close();
	}
}

