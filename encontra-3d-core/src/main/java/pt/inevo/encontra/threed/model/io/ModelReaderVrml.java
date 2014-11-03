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
import java.util.Scanner;

//Virtual Reality Modeling Language
class ModelReaderVrml extends ModelReader {
	private static final String COMMENT_SYMBOL = "#";

	public ModelReaderVrml(String name, String extension) {
		super(name, extension);
	}

	@Override
	public BufferedModel read(String filename) throws IOException {
		BufferedModel model = new BufferedModel(_name, _extension);
		BufferedMesh mesh = new BufferedMesh();

		Scanner root = new Scanner(new FileReader(filename));
		while(root.hasNext()) {
			String nextLine = root.nextLine();
			if(nextLine.startsWith(COMMENT_SYMBOL))
				continue;
			
			Scanner line = new Scanner(nextLine);
			
			line.close();
			
		}
		model.addMesh(mesh);
		root.close();
		
		return model;
	}
}