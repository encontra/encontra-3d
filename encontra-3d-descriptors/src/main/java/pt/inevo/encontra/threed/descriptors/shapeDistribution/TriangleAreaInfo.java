/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Vahid Hedayati
 */
package pt.inevo.encontra.threed.descriptors.shapeDistribution;

public class TriangleAreaInfo {
	 public double area;
	 public int index;
	 public TriangleAreaInfo(int i,double A) {

		this.area = A;;
		this.index = i;
	}

	public double getArea() {
		return area;
	}
	
	public int getindex() {

		return index;
	}
	
	public void setInfo(int i,double A){
		area=A;
		index=i;
	}
}
