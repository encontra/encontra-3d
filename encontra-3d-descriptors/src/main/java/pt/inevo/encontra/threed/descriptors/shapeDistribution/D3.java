/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Vahid Hedayati
 */
package pt.inevo.encontra.threed.descriptors.shapeDistribution;

import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.Point3D;
import pt.inevo.encontra.threed.Vector3D;
import pt.inevo.encontra.threed.descriptors.Histogram;
import pt.inevo.encontra.threed.descriptors.ShapeDistributionDescriptor;
import pt.inevo.encontra.threed.descriptors.utils.HeronFormula;

import java.util.List;

import static pt.inevo.encontra.threed.descriptors.Histogram.*;

public class D3 extends ShapeDistributionDescriptor {
	
	private final int LEVEL = 1024;			// default
	private int SAMPLES = 1024;

	// ------------------------------------------------------------------------------------
	public D3() {
	    super("D3");
	}

	// ------------------------------------------------------------------------------------
	public Histogram extract(Model model) {
        List<TriangleAreaInfo> areaProbability = getTriangleAreaProbability(model);

		// --------------------------------------------------------------------------------------------------
		// 3D model.
		// D3: Measures the square root of the area of the triangle
		// between three random points on the surface.
		Point3D p1, p2, p3;

        double[] lst = new double[SAMPLES];
		for (int j = 0; j < SAMPLES; j++) {
			p1 = getRandomPoint3d(model, areaProbability);
			p2 = getRandomPoint3d(model, areaProbability);
			p3 = getRandomPoint3d(model, areaProbability);

			double edgeA = Vector3D.distance(p1.getX(), p1.getY(), p1.getZ(),
                    p2.getX(), p2.getY(), p2.getZ());
			double edgeB = Vector3D.distance(p1.getX(), p1.getY(), p1.getZ(),
					p3.getX(), p3.getY(), p3.getZ());
			double edgeC = Vector3D.distance(p2.getX(), p2.getY(), p2.getZ(),
					p3.getX(), p3.getY(), p3.getZ());
			HeronFormula triangle = new HeronFormula();
			double D3Measure = Math
					.sqrt(triangle.findArea(edgeA, edgeB, edgeC));
			lst[j] = D3Measure;
			//System.out.print(D3Measure);
			//System.out.print("\n");
		}
        double max = MIN_PIXEL;
        double min = MAX_PIXEL;
        for (int i=0; i < lst.length; i++) {
            if(lst[i] > max) max = lst[i];
            if(lst[i] < min) min = lst[i];
        }
		Histogram histogram = new Histogram(LEVEL, max, 0);
		histogram.setHistogram(lst);
		return histogram;
	}

}