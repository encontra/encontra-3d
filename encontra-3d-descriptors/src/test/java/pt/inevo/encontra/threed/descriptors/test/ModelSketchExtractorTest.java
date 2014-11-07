package pt.inevo.encontra.threed.descriptors.test;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.utils.Normalize;
import pt.inevo.encontra.threed.utils.Scene;
import pt.inevo.encontra.threed.model.io.ModelIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

public class ModelSketchExtractorTest {

    private static final Log log = LogFactory
            .getLog(ModelSketchExtractorTest.class);

    @Test
    public void extractSketchFromScene() {

        String modelDirectory = getClass().getResource("/psb").getPath();
        String outputDirectory = "./";

        String imageFormat = "png";

        File directory = new File(modelDirectory);
        FilenameFilter fileFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return FilenameUtils.getExtension(name).equalsIgnoreCase("off");
            }
        };

        int count = 1;
        for(File file : directory.listFiles(fileFilter)) {
            log.info("model number: " + count);
            Model model;
            try {
                model = ModelIO.read(new File(file.getAbsolutePath()));
            } catch (Exception e) {
                log.error("Failed to load " + file.getAbsolutePath() + ": " + e.getMessage());
                continue;
            }

            Normalize.translation(model);
            Normalize.scale(model);

            Scene scene = new Scene();
            scene.addDrawable(model);
            scene.setDimensions(512, 512);

            int id = 0;
            //for(Vertex camera : cameraSet) {
                //scene.setCameraPosition(camera);
            scene.extractSnapshot(Scene.Type.Default, new File(outputDirectory + model.getName() + "_" + id + "." + imageFormat), imageFormat);
            scene.extractSnapshot(Scene.Type.Sketch, new File(outputDirectory + model.getName() + "_" + id + "_silhouette." + imageFormat), imageFormat);
                //id++;
            //}
            count++;
        }
    }
}
