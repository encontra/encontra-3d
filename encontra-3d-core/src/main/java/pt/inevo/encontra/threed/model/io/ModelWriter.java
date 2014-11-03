/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.model.io;

import pt.inevo.encontra.threed.Model;

import java.io.IOException;

//MYTODO: javadoc
public abstract class ModelWriter {

	public abstract void write(Model model) throws IOException;
	
}

