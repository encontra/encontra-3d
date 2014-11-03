/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed;

/**
 * The Vector3D class defines a spacial vector expressed numerically as ordered list of tuples (x,y,z).
 * All vectors have origin on (0, 0, 0).
 */
public class Vector3D extends Point3D {
	private double x;
	private double y;
	private double z;
	
	/** Constructs and initializes a Vector3D with coordinates (0, 0, 0).*/
	public Vector3D() {
		this(0.0);
	}
	protected Vector3D(double value) {
		this(value, value, value);
	}
	/**
	 * Constructs and initializes a Vector3D with the specified coordinates.
	 * @param x - the X coordinate of the newly constructed Vector3D.
	 * @param y - the Y coordinate of the newly constructed Vector3D.
	 * @param z - the Z coordinate of the newly constructed Vector3D.
	 */
	public Vector3D(double x, double y, double z) {
        super(x, y, z);
	}
	
	/**
	 * Normalizes the vector preserves the direction, but scales dimension to one unit.
	 */
	public void normalize() {
		float length = (float) this.lenght();
		if (length > 0) {
			x /= length;
			y /= length;
			z /= length;
		}
	}
	/**
	 * Get the dot product of vt and this object and return it.
	 * @param vt - the specified Vector3D to be calculated the dot product against this Vector3D
	 * @return
	 */
	public double DotProduct3(Vector3D vt)
	{
	   return this.x * vt.x + this.y * vt.y + this.z * vt.z;
	}
	/**
	 * Calculates the vector cross product of two vectors.
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Vector3D product(Vector3D v1, Vector3D v2) {
		Vector3D result = new Vector3D(	(v1.y * v2.z) - (v1.z * v2.y),
										(v1.z * v2.x) - (v1.x * v2.z),
										(v1.x * v2.y) - (v1.y * v2.x));
		return result;
	}
	/**
	 * Calculate the length of a vector.
	 * @return
	 */
	public double lenght() {
		return Math.sqrt(this.x * this.x +
						 this.y * this.y +
						 this.z * this.z);
	}
	
	/**
	 * Vector3D Addition. Sum of each of their coordinates. 
	 * @param v1 - the first Vector3D to be used.
	 * @param v2 - the second Vector3D to be used.
	 * @return Returns a Vector3D of the resulting addition
	 */
	public static Vector3D add(Vector3D v1, Vector3D v2) {
		Vector3D result = new Vector3D( v1.getX() + v2.getX(),
										v1.getY() + v2.getY(),
										v1.getZ() + v2.getZ());
		return result;
	}
	
	/**
	 * Vector3D Subtraction. Subs in each of their coordinates. 
	 * @param p1 - the first Point3D to be used.
	 * @param p2 - the second Point3D to be used.
	 * @return
	 * Returns a Vector3D of the resulting substration.
	 */
	public static Vector3D sub(Point3D p1, Point3D p2) {
		Vector3D result = new Vector3D( p1.getX() - p2.getX(),
										p1.getY() - p2.getY(),
										p1.getZ() - p2.getZ());
		return result;
	}
	
	/**
	 * Calculate the angle of a vector to the xy plan.
	 * @return
	 */
	public double getAngleXY() {
		double l = this.lenght();
		if (l==0)
			return 0;
		return Math.asin(this.z/l);
	}
	/**
	 * Calculate the angle of a vector to the xz plan.
	 * @return
	 */
	public double getAngleXZ() {
		double l = this.lenght();
		if (l==0)
			return 0;
		return Math.asin(this.y/l);
	}
	/**
	 * Calculate the angle of a vector to the yz plan.
	 * @return
	 */
	public double getAngleYZ() {
		double l = this.lenght();
		if (l==0)
			return 0;
		return Math.asin(this.x/l);
	}
	/**
	 * Calculate the angle between one vector to another.
	 * @param v
	 * @return
	 */
	public double angleBetweenVectors(Vector3D v) {
		//calculate the lengths of two vectors	
		double a = this.lenght();
		double b = v.lenght();
		
		if(a == 0 || b == 0) {
			return 0;
		}
		else {
			return Math.acos(this.DotProduct3(v));
		}
	}

}
