package pt.inevo.encontra.threed.descriptors.test;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.descriptors.utils.Normalize;
import pt.inevo.encontra.threed.descriptors.utils.Scene;
import pt.inevo.encontra.threed.model.io.ModelIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

public class ModelSketchExtractor {

    private static final Log log = LogFactory
            .getLog(ModelSketchExtractor.class);

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
            } catch (IOException e) {
                e.printStackTrace();
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
                scene.extractSketch(new File(outputDirectory + model.getName() + "_" + id + "." + imageFormat), imageFormat);
                //id++;
            //}
            count++;
        }
    }
}
