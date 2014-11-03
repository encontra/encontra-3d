/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.model.geoset;

import pt.inevo.encontra.threed.Vector3D;

import java.util.ArrayList;
import java.util.List;

/**
 * A face is a closed set of vertices, in which a triangle face has three vertices, and a quad face has four vertices.
 */
public class Face {
	public Vector3D Normal;
	
	/**
	 * List of the vertices identifiers, that form the face.
	 */
	public List<Integer> Vertices = new ArrayList<Integer>();
	
	/**
	 * List of the texture coordinate identifiers. 
	 */
	public List<Integer> TextCoord = new ArrayList<Integer>();
	//public List<Float> VNormals = new ArrayList<Float>();	// Normals in (x,y,z) form; normals might not be unit.
	
}
