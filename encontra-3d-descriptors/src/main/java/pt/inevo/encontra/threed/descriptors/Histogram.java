package pt.inevo.encontra.threed.descriptors;

import pt.inevo.encontra.common.distance.DistanceMeasure;
import pt.inevo.encontra.common.distance.EuclideanDistanceMeasure;
import pt.inevo.encontra.descriptors.Descriptor;

import java.io.Serializable;

public class Histogram implements Descriptor {

    public static final double MAX_PIXEL = 1000;	// default
    public static final double MIN_PIXEL = -1000;	// default

    private final int level;
    private final double maxPixel, minPixel;
    double[] histBin;
    protected DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
    private String name;
    private Serializable id;

    public Histogram(int level) {
        this(level, MAX_PIXEL, MIN_PIXEL);
    }

    public Histogram(int level, double max, double min) {
        this.level = level;
        this.maxPixel = max;
        this.minPixel = min;
    }

    public void setHistogram(double[] featureVector) {

        double interval = (maxPixel - minPixel) / level;

        histBin = new double[featureVector.length];

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
        double[] histLevel = new double[level];

        histLevel[0] = minPixel;

        histBin[0] /= featureVector.length;

        for(int i = 1; i < level; i++) {
            histLevel[i] = histLevel[i - 1] + interval;
            histBin[i] /= featureVector.length;
        }
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
        return 0;
    }

    @Override
    public Object getValue() {
        return histBin;
    }

    @Override
    public void setValue(Object o) {
        setHistogram((double[]) o);
    }

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public void setId(Serializable id) {
        this.id = id;
    }
}
