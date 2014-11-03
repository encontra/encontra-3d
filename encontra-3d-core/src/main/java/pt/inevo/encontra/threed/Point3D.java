/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 *      Nelson Silva <nelson.silva@inevo.pt>
 */
package pt.inevo.encontra.threed;

/**
 * The Point3D class defines a point representing a location in (x,y,z) coordinate space.
 */
public class Point3D implements Cloneable {

    /**
     * The coordinates of this Point3D.
     */
    public double x, y, z;

    public Point3D(double x, double y, double z) {
        set(x, y, z);
    }

    public Point3D() {
        this(0, 0, 0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return x;
    }

    public double getZ() {
        return z;
    }

    /**
     * Sets the location of this Point2D to the specified double coordinates.
     *
     * @param x - the X coordinate of the newly constructed Point3D.
     * @param y - the Y coordinate of the newly constructed Point3D.
     * @param z - the Z coordinate of the newly constructed Point3D.
     */
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the distance from this Point3D to a specified Point3D. Also known as the euclidean distance (L-2).
     *
     * @param pt - the specified point to be measured against this Point3D
     * @return the distance between this Point3D and the specified Point3D.
     */
    public double distance(Point3D pt) {
        return distance(this.getX(), this.getY(), this.getZ(), pt.getX(), pt.getY(), pt.getZ());
    }

    /**
     * Returns the distance between two points. Also known as the euclidean distance (L-2).
     *
     * @param x1 - the X coordinate of the first specified point
     * @param y1 - the Y coordinate of the first specified point
     * @param z1 - the Z coordinate of the first specified point
     * @param x2 - the X coordinate of the second specified point
     * @param y2 - the Y coordinate of the second specified point
     * @param z2 - the Z coordinate of the second specified point
     * @return The distance between the two sets of specified coordinates.
     */
    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(distanceSq(x1, y1, z1, x2, y2, z2));
    }

    /**
     * Returns the square of the distance from this Point3D to a specified Point3D.
     *
     * @param pt - the specified point to be measured against this Point3D
     * @return the square of the distance between this Point3D to a specified Point3D.
     */
    public double distanceSq(Point3D pt) {
        return this.distanceSq(pt.getX(), pt.getY(), pt.getZ());
    }

    /**
     * Returns the square of the distance from this Point3D to a specified point.
     *
     * @param px - the X coordinate of the specified point to be measured against this Point3D
     * @param py - the Y coordinate of the specified point to be measured against this Point3D
     * @param py - the Z coordinate of the specified point to be measured against this Point3D
     * @return the square of the distance between this Point3D and the specified point.
     */
    public double distanceSq(double px, double py, double pz) {
        return Point3D.distanceSq(this.getX(), this.getY(), this.getZ(), px, py, pz);
    }

    private static double distanceSq(double x1, double y1, double z1, double x2, double y2, double z2) {
        return ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));
    }

    /**
     * Adds a Point3D coordinates values to this Point3D.
     *
     * @param pt - the specified point to add to this Point3D
     */
    public void add(Point3D pt) {
        this.set(this.getX() + pt.getX(),
                this.getY() + pt.getY(),
                this.getZ() + pt.getZ());
    }

    /**
     * Subs a Point3D coordinates values to this Point3D.
     *
     * @param pt - the specified point to sub to this Point3D
     */
    public void sub(Point3D pt) {
        this.set(this.getX() - pt.getX(),
                this.getY() - pt.getY(),
                this.getZ() - pt.getZ());
    }

    /**
     * Multiples each coordinate with a value.
     *
     * @param value - value to multiple each coordinate with
     * @return
     */
    public void mul(double value) {
        this.set(this.getX() * value,
                this.getY() * value,
                this.getZ() * value);
    }

    /**
     * Divides each coordinate of the Point3D with a specific value.
     *
     * @param value
     */
    public void div(int value) {
        this.set(this.getX() / value,
                this.getY() / value,
                this.getZ() / value);
    }
}






