/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro Miguel Bacalhau Pascoal
 *      Nelson Silva <nelson.silva@inevo.pt>
 */
package pt.inevo.encontra.threed.descriptors.lightfield;

import pt.inevo.encontra.descriptors.MultiDescriptorExtractor;
import pt.inevo.encontra.index.IndexedObject;
import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.descriptors.utils.Normalize;
import pt.inevo.encontra.threed.descriptors.utils.Scene;
import pt.inevo.encontra.threed.model.geoset.Vertex;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;


public class LightfieldExtractor extends MultiDescriptorExtractor<IndexedObject<Long, Model>, RegionShape.Coefficients> {

    // Number of camera positions
    private static final int N = 10;

    private boolean _imageOutputEnabled = true;
    private int _imageOutputSize = 512;
    private String _imageOutputFormat = "png";
    private String _imageOutputDirectory = "X:\\media\\models\\png\\"; //"C:\\Users\\t-pedrop\\Desktop\\image\\"

    // TODO - original version rotates dodecahedron
    private static List<Vertex> makeHalfDodecahedron(double r) {
        // Calculate constants that will be used to generate vertices
        final double phi = (Math.sqrt(5) - 1) / 2; // The golden ratio
        double R = r / Math.sqrt(3);

        double a = (R * 1);
        double b = R / phi;
        double c = R * phi;

        // Generate each vertex
        List<Vertex> vertices = new ArrayList<>();

        int i = -1;
        //for (int i = -1; i < 2; i += 2) {

            for (int j = -1; j < 2; j += 2) {
                vertices.add(new Vertex(
                        0,
                        i * c * R,
                        j * b * R));
                vertices.add(new Vertex(
                        i * c * R,
                        j * b * R,
                        0));
                vertices.add(new Vertex(
                        i * b * R,
                        0,
                        j * c * R));

                for (int k = -1; k < 2; k += 2)
                    vertices.add(new Vertex(
                            i * a * R,
                            j * a * R,
                            k * a * R));
            }
        //}
        return vertices;
    }

    @Override
    public List<RegionShape.Coefficients> extractDescriptors(IndexedObject<Long, Model> object) {
        Model model = object.getValue();


        // ===================================================================================
        // Step 1. Translation and scaling are applied first to ensure that 3D model is
        // entirely contained in the rendered images
        // ===================================================================================
        Normalize.translation(model);
        Normalize.scale(model);

        // ===================================================================================
        // Step 2-3. For a LightField Descriptor, 10 images are represented for 20 viewports,
        // and are in a pre-defined order for storage.LFD_NUM
        // ===================================================================================
        Scene scene = new Scene();
        scene.setDimensions(_imageOutputSize, _imageOutputSize);
        scene.addDrawable(model);

        List<Vertex> positions = makeHalfDodecahedron(1.0);

        BufferedImage[] silhouettes = new BufferedImage[N];

        for(int cam = 0; cam < N; cam++) {
            Vertex position = positions.get(cam);
            scene.setCameraPosition(position);
            BufferedImage silhouette;
            //TODO: BufferedImage contourn;
            if(_imageOutputEnabled) {
                String prefix = _imageOutputDirectory + model.getName() + "_" + cam;
                //silhouette = scene.extractSilhouette(new File(prefix + "_silhouette." + _imageOutputFormat), _imageOutputFormat);
                silhouette = scene.extractSketch(new File(prefix + "_sketch." + _imageOutputFormat), _imageOutputFormat);
                //TODO: contourn = scene.extractContourn(new File(prefix + "_contourn." + format), format);
            } else {
                silhouette = scene.extractSilhouette();
                //TODO: contourn = scene.extractContourn();
            }
            silhouettes[cam] = silhouette;
        }

        // ===================================================================================
        // Step 4. Descriptors for a 3D model are extracted from the LFD_NUM * CAM_NUM images
        // ===================================================================================
        RegionShape regionShape = new RegionShape();
        regionShape.findRadius(silhouettes);
        regionShape.generateBasisLUT();

        List<RegionShape.Coefficients> descriptors = new ArrayList<>();
        for(int cam = 0; cam < silhouettes.length; cam++) {
            descriptors.add(regionShape.extractCoefficients(silhouettes[cam]));
        }

        return descriptors;
    }
}
