/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.descriptors.utils;

public class HeronFormula {

	double area;
	double s;

	public double findArea(double sideA, double sideB, double sideC) {
		s = 0.5 * (sideA + sideB + sideC);
		area = Math.sqrt(s * (s - sideA) * (s - sideB) * (s - sideC));
		//System.out.println("The area of the triangle is " + area);
		return area;

	}

}

