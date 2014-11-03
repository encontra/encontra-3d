package pt.inevo.encontra.threed.descriptors.test;

import org.junit.Test;
import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.descriptors.Histogram;
import pt.inevo.encontra.threed.descriptors.ShapeDistributionDescriptor;
import pt.inevo.encontra.threed.descriptors.shapeDistribution.*;
import pt.inevo.encontra.threed.descriptors.utils.Normalize;
import pt.inevo.encontra.threed.model.io.ModelIO;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ShapeDistributionTest {

    protected static String MODEL = "/psb/m0.off";

    @Test
	public void testExtraction() {
        String modelName = getClass().getResource(MODEL).getFile();
		try {
            Model model = ModelIO.read(new File(modelName));
            Normalize.translation(model);
            Normalize.scale(model);

            ShapeDistributionDescriptor[] descriptors = new ShapeDistributionDescriptor[] {
                new A3(), new D1(), new D2(), new D3(), new D4(),
            };

            Histogram histogram;

            for (ShapeDistributionDescriptor d : descriptors) {
                histogram = d.extract(model);
                assertNotNull(histogram);
            }

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
