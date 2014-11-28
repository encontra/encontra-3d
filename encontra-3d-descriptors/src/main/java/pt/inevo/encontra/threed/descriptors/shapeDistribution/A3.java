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

import static pt.inevo.encontra.threed.descriptors.Histogram.*;

import java.util.List;

public class A3<O extends IndexedObject<Long, Model>> extends ShapeDistributionDescriptor {

    public static final int LEVEL = 1024;
    private static int SAMPLES = 1024;

	public A3() {
	    super("A3", Histogram.class, IndexedObject.class);
	}

	@Override
	public Histogram extract(Model model) {

        Double[] lst = new Double[SAMPLES];

        //SurfaceArea=model.getMeshes().get(0).getSurfaceArea();
        List<TriangleAreaInfo> areaProbability = getTriangleAreaProbability(model);
		
		// A3 : Measures the angle between three random points on the surface 
		Point3D p1, p2, p3;

		for (int j = 0; j < SAMPLES; j++) {
			p1 = getRandomPoint3d(model, areaProbability);
			p2 = getRandomPoint3d(model, areaProbability);
			p3 = getRandomPoint3d(model, areaProbability);

			double edgeA = Vector3D.distance(p1.getX(), p1.getY(), p1.getZ(),
                    p2.getX(), p2.getY(), p2.getZ());
			double edgeB = Vector3D.distance(p1.getX(), p1.getY(), p1.getZ(),
					p3.getX(), p3.getY(), p3.getZ());
			double DotProductp1p2p3 = (((p2.getX() - p1.getX()) * (p3.getX() - p1
					.getX()))
					+ ((p2.getY() - p1.getY()) * (p3.getY() - p1.getY())) + ((p2
					.getZ() - p1.getZ()) * (p3.getZ() - p1.getZ())));

			double angle;
			if (edgeA == 0 || edgeB == 0) {
				angle = Math.PI / 2;
			} else {
				angle = Math.acos(DotProductp1p2p3 / (edgeA * edgeB));
			}
			lst[j] = angle;
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

}