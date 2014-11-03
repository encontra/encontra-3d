// Copyright 2013 Pedro B. Pascoal
package pt.inevo.encontra.threed.descriptors;

import pt.inevo.encontra.descriptors.DescriptorExtractor;
import pt.inevo.encontra.index.IndexedObject;
import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.Point3D;
import pt.inevo.encontra.threed.Vector3D;
import pt.inevo.encontra.threed.descriptors.shapeDistribution.TriangleAreaInfo;
import pt.inevo.encontra.threed.descriptors.utils.HeronFormula;
import pt.inevo.encontra.threed.model.geoset.Face;
import pt.inevo.encontra.threed.model.geoset.Mesh;
import pt.inevo.encontra.threed.model.geoset.Vertex;

import java.util.*;

public abstract class ShapeDistributionDescriptor extends DescriptorExtractor<IndexedObject<Long, Model>, Histogram> {

    private double EPSILON = 0.0001;
    private double RAND_MAX = 0x7fff;
    public double SurfaceArea = 0.0;

    private final String name;

    public ShapeDistributionDescriptor(String name) {
        this.name = name;
    }

    @Override
    protected IndexedObject setupIndexedObject(Histogram descriptor, IndexedObject object) {
        object.setId(descriptor.getId());
        object.setId(descriptor.getId());
        object.setValue(descriptor.getValue());

        return object;
    }

    @Override
    public Histogram extract(IndexedObject<Long, Model> object) {
        Model model = object.getValue();

        Histogram histogram = extract(model);
        histogram.setName(name);
        return histogram;
    }

    abstract public Histogram extract(Model model);

    // --------------------------------------------------------------------------------
    // Get random points
    // --------------------------------------------------------------------------------

    public Point3D getRandomPoint3d(Model model, List<TriangleAreaInfo> areaProbability) {

        double d = Math.random();
        int i = 0; /* "i" indicates the triangle in the ordered area list */
        while (d >= 0 && i < areaProbability.size()) {
            d = d - areaProbability.get(i).getArea();
            i++;
        }
        if (d < 0 || Math.abs(d) < EPSILON) {
            i--;
        } else {
            // System.out.print("d=%lf\nindex=%d\tarea_size=%d\n",d,i,AreaPrbability.size());
            // return false;
        }

        //"index" indicates the elected triangle
        int index = areaProbability.get(i).getindex();
        Mesh mesh = model.getMeshes().get(0);
        Face face = mesh.getFaces().get(index);

        List<Integer> vertices_id = face.Vertices;
        List<Vertex> vertices = mesh.getVertices();
        Vertex p1 = vertices.get(vertices_id.get(0));
        Vertex p2 = vertices.get(vertices_id.get(1));
        Vertex p3 = vertices.get(vertices_id.get(2));


        // random numbers between 0 and 1,
        double r1 = (Math.random() / RAND_MAX);
        double r2 = (Math.random() / RAND_MAX);

        double x = (1 - Math.sqrt(r1)) * p1.x + Math.sqrt(r1) * (1 - r2) * p2.x
                + Math.sqrt(r1) * r2 * p3.x;
        double y = (1 - Math.sqrt(r1)) * p1.y + Math.sqrt(r1) * (1 - r2) * p2.y
                + Math.sqrt(r1) * r2 * p3.y;
        double z = (1 - Math.sqrt(r1)) * p1.z + Math.sqrt(r1) * (1 - r2) * p2.z
                + Math.sqrt(r1) * r2 * p3.z;
        Point3D p = new Vector3D(x, y, z);

        return p;

    }

    // --------------------------------------------------------------------------------------------
    public List<TriangleAreaInfo> getTriangleAreaProbability(Model model) {
        List<TriangleAreaInfo> areaProbability = new ArrayList<>();
        for (Mesh mesh : model.getMeshes()) {
            for (int i = 0; i < mesh.getFaces().size(); i++) {
                List<Integer> vertices_id = mesh.getFaces().get(i).Vertices;
                List<Vertex> vertices = mesh.getVertices();
                Vertex point1 = vertices.get(vertices_id.get(0));
                Vertex point2 = vertices.get(vertices_id.get(1));
                Vertex point3 = vertices.get(vertices_id.get(2));

                double edgeA = Vertex.distance(point1.x, point1.y, point1.z,
                        point2.x, point2.y, point2.z);
                double edgeB = Vertex.distance(point1.x, point1.y, point1.z,
                        point3.x, point3.y, point3.z);
                double edgeC = Vertex.distance(point3.x, point3.y, point3.z,
                        point2.x, point2.y, point2.z);

                // calculate the areas of triangle meshes
                HeronFormula triangle = new HeronFormula();
                double area = triangle.findArea(edgeA, edgeB, edgeC);

                // Normalize the Area probabilities to [0-1] and add to
                // TriangleAreaInfo
                TriangleAreaInfo triInfo = new TriangleAreaInfo(i, area / SurfaceArea);

                areaProbability.add(triInfo);

            }
        }
        // sort areaProbability
        Collections.sort(areaProbability, new Comparator<TriangleAreaInfo>() {
            @Override
            public int compare(TriangleAreaInfo t1, TriangleAreaInfo t2) {
                if (t1.area < t2.area) return -1;
                if (t1.area > t2.area) return 1;
                return 0;
            }
        });
        return areaProbability;
    }
}
