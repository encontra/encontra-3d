package pt.inevo.encontra.threed.model.utils;

import pt.inevo.encontra.storage.ModelLoader;
import pt.inevo.encontra.threed.model.BufferedModel;
import pt.inevo.encontra.threed.model.ThreedModel;
import pt.inevo.encontra.threed.model.io.ModelIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Loader for Objects of the type: ImageModel.
 * @author Ricardo
 */
public class ThreedModelLoader extends ModelLoader{

    public ThreedModelLoader() {
    }

    public ThreedModelLoader(String modelsPath) {
        this.modelsPath = modelsPath;
    }

    @Override
    public ThreedModel loadModel(File model) {

        //for now only sets the filename
        ThreedModel ml = new ThreedModel(model.getAbsolutePath(), null);

        //get the description
        //TO DO - load the description from here

        //get the bufferedmodel
        try {
            BufferedModel bufMdl = ModelIO.read(model);

            ml.setThreedmodel(bufMdl);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return ml;
    }

    @Override
    public BufferedModel loadBuffered(File model) {

        BufferedModel bufMdl=null;
        try {
            bufMdl = ModelIO.read(model);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bufMdl;
    }


    public List<ThreedModel> getModels(String path) {
        File root = new File(path);
        String[] extensions = {"off","obj","mdl"};

        List<File> modelFiles = FileUtil.findFilesRecursively(root, extensions);
        List<ThreedModel> models = new ArrayList<ThreedModel>();

        for (File f : modelFiles) {
            models.add(loadModel(f));
        }

        return models;
    }

    public void load(String path) {
        File root = new File(path);
        String[] extensions = {"off","obj","mdl"};

        modelsFiles = FileUtil.findFilesRecursively(root, extensions);
    }
}
