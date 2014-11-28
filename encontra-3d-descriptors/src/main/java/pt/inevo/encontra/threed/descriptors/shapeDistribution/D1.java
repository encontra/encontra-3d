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
import pt.inevo.encontra.threed.descriptors.Histogram;

import java.util.List;

import static pt.inevo.encontra.threed.descriptors.Histogram.*;

public class D1<O extends IndexedObject<Long, Model>> extends ShapeDistributionDescriptor {

    public static final int LEVEL = 1024;
	private static int SAMPLES = 1024;

	// ------------------------------------------------------------------------------------
	public D1() {
        super("D1", Histogram.class, IndexedObject.class);
	}

	// ------------------------------------------------------------------------------------
	public Histogram extract(Model model) {

        List<TriangleAreaInfo> areaProbability = getTriangleAreaProbability(model);
		// int SamplePoint = 1000;
		
		// ---------------------------------------------------------------------------------
		// D1 : Measures the distance between a fixed point and one random point on
		// the surface. We use the centroid
		// of the boundary of the model as the fixed point.

		Point3D p1, p2;
		p1 = model.getMeshes().get(0).getBarycenter();

        Double[] lst = new Double[SAMPLES];
		for (int j = 0; j < SAMPLES; j++) {
			p2 = getRandomPoint3d(model, areaProbability);
			double distanceD1 = p1.distance(p2);
			lst[j] = distanceD1;
			//System.out.print(distanceD1);
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

}