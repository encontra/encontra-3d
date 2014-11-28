package pt.inevo.encontra.threed.descriptors.test;

import org.junit.Test;
import pt.inevo.encontra.common.DefaultResultProvider;
import pt.inevo.encontra.common.Result;
import pt.inevo.encontra.common.ResultSet;
import pt.inevo.encontra.engine.SimpleEngine;
import pt.inevo.encontra.index.IndexedObject;
import pt.inevo.encontra.index.search.SimpleSearcher;
import pt.inevo.encontra.lucene.index.LuceneEngine;
import pt.inevo.encontra.lucene.index.LuceneIndex;
import pt.inevo.encontra.nbtree.index.BTreeIndex;
import pt.inevo.encontra.nbtree.index.NBTreeSearcher;
import pt.inevo.encontra.query.CriteriaQuery;
import pt.inevo.encontra.query.QueryProcessorDefaultImpl;
import pt.inevo.encontra.query.criteria.CriteriaBuilderImpl;
import pt.inevo.encontra.storage.EntityStorage;
import pt.inevo.encontra.storage.SimpleFSObjectStorage;
import pt.inevo.encontra.storage.SimpleObjectStorage;
import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.model.ThreedModel;
import pt.inevo.encontra.threed.descriptors.Histogram;
import pt.inevo.encontra.threed.descriptors.shapeDistribution.ShapeDistributionDescriptor;
import pt.inevo.encontra.threed.descriptors.shapeDistribution.*;
import pt.inevo.encontra.threed.model.BufferedModel;
import pt.inevo.encontra.threed.model.utils.ThreedModelLoader;
import pt.inevo.encontra.threed.utils.Normalize;
import pt.inevo.encontra.threed.model.io.ModelIO;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.assertNotNull;

public class ShapeDistributionTest {

    protected static String MODEL = "/psb/m0.off";
    protected static String MODELS = "C:\\Users\\João\\Dropbox\\Vahid\\codebox\\model-samples";

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

    @Test
    public void testEngineProcess() {

        //Mudar para SimpleFSObjectStorage
        EntityStorage storage = new SimpleFSObjectStorage(ThreedModel.class, "data/threed/objects/"); //Create 3dModel

        SimpleEngine<ThreedModel> e = new SimpleEngine<ThreedModel>();
        e.setObjectStorage(storage);

        SimpleSearcher<IndexedObject> searcher = new SimpleSearcher();
        //NBTreeSearcher<IndexedObject> searcher = new NBTreeSearcher();

        //BTreeIndex index = new BTreeIndex("data/threed/indexes/btree/d2", Histogram.class);
        LuceneIndex index = new LuceneIndex("data/threed/indexes/lucene/d2", Histogram.class);

        searcher.setIndex(index);
        e.setSearcher("model", searcher);
        searcher.setDescriptorExtractor(new D2());

        ThreedModelLoader loader = new ThreedModelLoader(MODELS);
        loader.load();
        Iterator<File> it = loader.iterator();

        for (int i = 0; it.hasNext(); i++) {
            File f = it.next();
            ThreedModel tm = loader.loadModel(f);  //Normalization - translation and scale are done in the Loader
            Normalize.translation(tm.getModel());
            Normalize.scale(tm.getModel());

            e.insert(tm);
        }
        //index.close();
    }

    @Test
    public void testSimilar() {
        EntityStorage storage = new SimpleFSObjectStorage(ThreedModel.class, "data/threed/objects/"); //Create 3dModel

        SimpleEngine<ThreedModel> e = new SimpleEngine<ThreedModel>();
        e.setObjectStorage(storage);

        //NBTreeSearcher<IndexedObject> searcher = new NBTreeSearcher();
        SimpleSearcher<IndexedObject> searcher = new SimpleSearcher();

        //BTreeIndex index = new BTreeIndex<>("data/threed/indexes/btree/d2", Histogram.class);
        LuceneIndex index = new LuceneIndex("data/threed/indexes/lucene/d2", Histogram.class);
        searcher.setIndex(index);

        searcher.setDescriptorExtractor(new D2());
        searcher.setQueryProcessor(new QueryProcessorDefaultImpl());
        searcher.setResultProvider(new DefaultResultProvider());

        e.setSearcher("model", searcher);

        System.out.println("Creating a knn query...");
        ThreedModelLoader loader = new ThreedModelLoader();
        ThreedModel mdl = loader.loadModel(new File(getClass().getResource(MODEL).getFile()));
        Normalize.translation(mdl.getModel());
        Normalize.scale(mdl.getModel());


        CriteriaBuilderImpl cb = new CriteriaBuilderImpl();
        CriteriaQuery<ThreedModel> query = cb.createQuery(ThreedModel.class);
        pt.inevo.encontra.query.Path modelPath = query.from(ThreedModel.class).get("model");
        query = query.where(cb.similar(modelPath, mdl.getModel())).distinct(true).limit(20);

        ResultSet<ThreedModel> results = e.search(query);

        System.out.println("Number of retrieved elements: " + results.getSize());
        for (Result<ThreedModel> r : results) {
            System.out.print("Retrieved element: " + r.getResultObject().toString() + "\t");
            System.out.println("Similarity: " + r.getScore());
        }
        //index.close();
    }

}
