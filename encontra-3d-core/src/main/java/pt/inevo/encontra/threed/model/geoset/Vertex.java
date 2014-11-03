/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.model.geoset;

import pt.inevo.encontra.threed.Point3D;
import pt.inevo.encontra.threed.Vector3D;

/**
 * A vertex is a position along with other information such as color, normal vector and texture coordinates.
 */
public class Vertex extends Point3D {
	
	/**
	 * The normalized average of the surface normals of the faces that contain that vertex.
	 */
	public Vector3D Normal;
	
	public Vertex(double x, double y, double z) {
		super(x, y, z);
		
		Normal = new Vector3D(0, 0, 0);
	}
}