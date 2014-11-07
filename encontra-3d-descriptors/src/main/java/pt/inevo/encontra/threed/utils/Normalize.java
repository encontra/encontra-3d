/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.utils;


import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.Point3D;

/**
 * Translation, scaling and rotation methods to apply first when need.
 * E.g.: to ensure that 3D model entirely contained in the rendered images.
 */
public class Normalize {
	/**
	 * 
	 * @param model - the model to apply the translation.
	 */
	public static void translation(Model model) {
		//find model barycenter
		Point3D barycenter = model.getBarycenter();
		
		//translate to origin
		model.translate(-barycenter.getX(), -barycenter.getY(), -barycenter.getZ());
	}

	/**
	 * 
	 * @param model - the model to apply the scalling.
	 */
	public static void scale(Model model) {
		// find model bounding box
		Point3D max = model.getMaxVertex();
		Point3D min = model.getMinVertex();
		
		// scale to unitary value
		model.scale(1/Math.max(max.getX() - min.getX(), Math.max(max.getY() - min.getY() , max.getZ() - min.getZ())));
	}
	
	/**
	 * 
	 */
	public static void rotation(Model model) {
		//TODO: PO3D Project - PCA/Rectiliniarity
	}
	
}

