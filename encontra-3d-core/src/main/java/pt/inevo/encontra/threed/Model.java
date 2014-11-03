/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed;

import pt.inevo.encontra.threed.model.geoset.Bone;
import pt.inevo.encontra.threed.model.geoset.Mesh;

import javax.media.opengl.GLAutoDrawable;
import java.util.ArrayList;
import java.util.List;

/**
 * The abstract class Model is the superclass of all classes that represent graphical 3d model.
 * The model must be obtained in a platform-specific manner.
 */
public abstract class Model {
	private String _name = null;
	private String _extension = null;
	
	protected List<Mesh> _meshes = new ArrayList<Mesh>();	// list of meshes/geosets
	protected List<Bone> _bones = new ArrayList<Bone>();
	//MYTODO: Add Attachments
	//MYTODO: Add Events
	//MYTODO: Add ParticleEmitters
	
	public Model(String name, String extension) {
		_name = name;
		_extension = extension;
	}

	public String getName() {
		return _name;
	}
	public String getModelName() {
		return _name.replaceAll("."+_extension, "");
	}
	public String getModelFormat() {
		return _extension;
	}
	
	public List<Mesh> getMeshes() {
		return _meshes;
	}
	
	
	public void translate(double x, double y, double z){
		for(Mesh mesh : _meshes) {
			mesh.translate(x, y, z);
		}
		
		for(Bone bone : _bones) {
			bone.translate(x, y, z);
		}
	}
	public void scale(double value) {
		for(Mesh mesh : _meshes) {
			mesh.scale(value);
		}

		for(Bone bone : _bones) {
			bone.scale(value);
		}
	}
	public void rotate() {
	}

	public Point3D getMaxVertex() {
		return _meshes.get(0).getMaxVertex();
	}
	public Point3D getMinVertex() {
		return _meshes.get(0).getMinVertex();
	}
	public Point3D getBarycenter() {
		return _meshes.get(0).getBarycenter();
	}
	
	public int countVertices() {
		int count = 0;
		for(Mesh mesh : _meshes) {
			count += mesh.countVertices();
		}
		return count;
	}
	public int countFaces() {
		int count = 0;
		for(Mesh mesh : _meshes) {
			count += mesh.countFaces();
		}
		return count;
	}
	public int countEdges() {
		int count = 0;
		for(Mesh mesh : _meshes) {
			count += mesh.countEdges();
		}
		return count;
	}
	
	public boolean isManifold() {
		return _meshes.get(0).isManifold();
	}
	public double getSurfaceArea() {
		return _meshes.get(0).getSurfaceArea();
	}

	public void draw(GLAutoDrawable drawable) {
		for(Mesh mesh : _meshes) {
			mesh.draw(drawable);
		}
		
		for(Bone bone : _bones) {
			bone.draw(drawable);
		}
	}
}
