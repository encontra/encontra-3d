/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Vahid Hedayati
 */
package pt.inevo.encontra.threed.descriptors.shapeDistribution;

import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.Point3D;
import pt.inevo.encontra.threed.descriptors.Histogram;
import pt.inevo.encontra.threed.descriptors.ShapeDistributionDescriptor;

import java.util.List;

import static pt.inevo.encontra.threed.descriptors.Histogram.*;

public class D2 extends ShapeDistributionDescriptor {

	private final int LEVEL = 256;			// default
	private int SAMPLES = 8192; // 2^13

	// ------------------------------------------------------------------------------------
	public D2() {
	    super("D2");
	}

	// ------------------------------------------------------------------------------------
	public Histogram extract(Model model) {
        List<TriangleAreaInfo> areaProbability = getTriangleAreaProbability(model);
	
		// ------------------------------------------------------------------------------------
		// D2 : Measures the distance between two random points on the surface.

        double[] lst = new double[SAMPLES];
		Point3D p1, p2;
		for (int j = 0; j < SAMPLES; j++) {

			p1 = getRandomPoint3d(model, areaProbability);
			p2 = getRandomPoint3d(model, areaProbability);

			double distance = p1.distance(p2);
            lst[j] = distance;
			//System.out.print(distance);
			//System.out.print("\n");
		}
		double max = MIN_PIXEL;
		double min = MAX_PIXEL;
		for (int i=0; i < lst.length; i++) {
			if(lst[i] > max) max = lst[i];
			if(lst[i] < min) min = lst[i];
		}
		Histogram histogram = new Histogram(LEVEL, max, min);
		histogram.setHistogram(lst);
        return histogram;
	}

}