package pt.inevo.encontra.threed.descriptors;

import org.apache.commons.lang.ArrayUtils;
import pt.inevo.encontra.common.distance.DistanceMeasure;
import pt.inevo.encontra.common.distance.EuclideanDistanceMeasure;
import pt.inevo.encontra.descriptors.Descriptor;
import pt.inevo.encontra.index.IndexedObject;
import pt.inevo.encontra.index.Vector;
import pt.inevo.encontra.threed.model.BufferedModel;
import pt.inevo.encontra.threed.model.ThreedModel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Histogram<O extends IndexedObject> extends Vector implements Descriptor, Cloneable {

    public static final double MAX_PIXEL = 1000;	// default
    public static final double MIN_PIXEL = -1000;	// default

    private final int level;
    private final double maxPixel, minPixel;
    protected DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
    private String name;
    private Serializable id;

    public Histogram() {
        this(-1, 0, 0, new Double[1]);
    }

    public Histogram(int level, Double[] featureVector) {
        this(level, MAX_PIXEL, MIN_PIXEL, featureVector);
    }

    public Histogram(int level, double max, double min, Double[] featureVector) {
        super(Double.class, featureVector.length);
        this.level = level;
        this.maxPixel = max;
        this.minPixel = min;
        if (level != -1) {
            setHistogram(featureVector);
        }
    }

    public void setHistogram(Double[] featureVector) {

        double interval = (maxPixel - minPixel) / level;

        Double[] histBin = ArrayUtils.toObject(new double[featureVector.length]);

        for(int i=0; i < featureVector.length; i++) {
            //uniformed and ununiformed
            int index = (int) ((featureVector[i] - minPixel) / interval);

            if(index >= level)
                index = level-1;
            else if(index < 0)
                index = 0;

            histBin[index]++;
        }
        //normalization
        Double[] histLevel = new Double[level];

        histLevel[0] = minPixel;

        histBin[0] /= featureVector.length;

        for(int i = 1; i < level; i++) {
            histLevel[i] = histLevel[i - 1] + interval;
            histBin[i] /= featureVector.length;
        }
        this.setValues(histBin);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double getDistance(Descriptor other) {
        return distanceMeasure.distance(this, other);
    }

    @Override
    public double getNorm() {
        return super.norm(2);
    }

    //Errors in the lucene index, as the LuceneIndexEntryFactory is dealing with strings
    @Override
    public Serializable getValue() {
        return getStringRepresentation((Double[]) getValues());
    }

    @Override
    public void setValue(Object o) {
        Double[] histBin = setStringRepresentation((String) o);
        setValues(histBin);
    }

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public void setId(Serializable id) {
        this.id = id;
    }

    public String getStringRepresentation(Double[] vec)
    {
        String stringValues = "";
        for(Double val : vec)
        {
            stringValues += val.toString() + " ";
        }
        return stringValues;
    }

    public Double[] setStringRepresentation(String o)
    {

        String[] stringVec = o.split(" ");
        Double [] vec = new Double[stringVec.length];
        int i;
        for (i=0; i<stringVec.length; i++)
        {
            vec[i] =  Double.parseDouble(stringVec[i]);
        }
        return vec;
    }

}
