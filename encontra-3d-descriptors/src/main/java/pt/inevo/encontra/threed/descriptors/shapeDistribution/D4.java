/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Vahid Hedayati
 */
package pt.inevo.encontra.threed.descriptors.shapeDistribution;

import pt.inevo.encontra.index.IndexedObject;
import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.Point3D;
import pt.inevo.encontra.threed.Vector3D;
import pt.inevo.encontra.threed.descriptors.Histogram;
import pt.inevo.encontra.threed.utils.HeronFormula;

import java.util.List;

import static pt.inevo.encontra.threed.descriptors.Histogram.*;

public class D4<O extends IndexedObject<Long, Model>> extends ShapeDistributionDescriptor {

	
	private final int LEVEL = 1024;			// default
	private int SAMPLES = 1024;

	// ------------------------------------------------------------------------------------
	public D4() {
	    super("D4", Histogram.class, IndexedObject.class);
	}

	// ------------------------------------------------------------------------------------
	public Histogram extract(Model model) {
        List<TriangleAreaInfo> areaProbability = getTriangleAreaProbability(model);
		
		// ------------------------------------------------------------------------------------
		// ---------------D4
		// Measures the cube root of the volume of the
		// tetrahedron between four random points on the surface.
		Point3D p1, p2, p3, p0;
        Double[] lst = new Double[SAMPLES];
		for (int j = 0; j < SAMPLES; j++) {
			p1 = getRandomPoint3d(model, areaProbability);
			p2 = getRandomPoint3d(model, areaProbability);
			p3 = getRandomPoint3d(model, areaProbability);
			p0 = getRandomPoint3d(model, areaProbability);

			double edgeA = Vector3D.distance(p1.getX(), p1.getY(), p1.getZ(),
                    p2.getX(), p2.getY(), p2.getZ());
			double edgeB = Vector3D.distance(p1.getX(), p1.getY(), p1.getZ(),
					p3.getX(), p3.getY(), p3.getZ());
			double edgeC = Vector3D.distance(p2.getX(), p2.getY(), p2.getZ(),
					p3.getX(), p3.getY(), p3.getZ());

			HeronFormula triangle = new HeronFormula();
			double area = triangle.findArea(edgeA, edgeB, edgeC);

			// calculate the height from p0 to the plane determined by p1,p2,p3
			double height = Math.abs(getDistanceFromPointToSurface(p0, p1, p2,
                    p3));
			double volumn = area * height / 3;
			// return volumn;
			lst[j] = volumn;
			//System.out.print(volumn);
			//System.out.print("\n");
		}
        double max = MIN_PIXEL;
        double min = MAX_PIXEL;
        for (int i=0; i < lst.length; i++) {
            if(lst[i] > max) max = lst[i];
            if(lst[i] < min) min = lst[i];
        }
        Histogram histogram = new Histogram(LEVEL, max, 0, lst);
        return histogram;
	}

	
	// -----------------------------------------------------------------------------------
	public static double cubeRoot(double x) {
		return Math.pow(x, 1.0 / 3);
	}

	// ------------------------------------------------------------------------------------

	double getDistanceFromPointToSurface(Point3D p0, Point3D p1, Point3D p2,
			Point3D p3) {
		// calculate the plane equation determined by p1,p2 and p3, i.e.
		// Ax+By+Cz+D=0
		double A = (p1.getY() * p2.getZ() + p2.getY() * p3.getZ() + p3.getY()
				* p1.getZ())
				- (p3.getY() * p2.getZ() + p2.getY() * p1.getZ() + p1.getY()
						* p3.getZ());
		double B = (p3.getX() * p2.getZ() + p2.getX() * p1.getZ() + p1.getX()
				* p3.getZ())
				- (p1.getX() * p2.getZ() + p2.getX() * p3.getZ() + p3.getX()
						* p1.getZ());
		double C = (p1.getX() * p2.getY() + p2.getX() * p3.getY() + p3.getX()
				* p1.getY())
				- (p3.getX() * p2.getY() + p2.getX() * p1.getY() + p1.getX()
						* p3.getY());
		double D = -(p1.getX() * p2.getY() * p3.getZ() + p2.getX() * p3.getY()
				* p1.getZ() + p3.getX() * p1.getY() * p2.getZ())
				+ (p3.getX() * p2.getY() * p1.getZ() + p2.getX() * p1.getY()
						* p3.getZ() + p1.getX() * p3.getY() * p2.getZ());
		if (A == 0 && B == 0 && C == 0)
			return 0;
		double distance = -(A * p0.getX() + B * p0.getY() + C * p0.getZ() + D)
				/ Math.sqrt(A * A + B * B + C * C);
		return distance;
	}

}