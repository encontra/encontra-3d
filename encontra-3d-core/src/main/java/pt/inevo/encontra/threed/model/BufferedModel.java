/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.model;

import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.model.geoset.Bone;
import pt.inevo.encontra.threed.model.geoset.Mesh;

/**
 * The BufferedModel subclass describes a Model with an accessible buffer of model data.
 */
public class BufferedModel extends Model {

	public BufferedModel(String name, String extension) {
		super(name, extension);
	}

	public void addMesh(Mesh mesh) {
		_meshes.add(mesh);
	}
	
	public void addBone(Bone bone) {
		_bones.add(bone);
		System.out.println("added bone:" + bone.getName() );
	}
}

