/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Joao Guerreiro
 *      Mostly Based on https://isda.ncsa.illinois.edu/svn/nara/trunk/3DUtilities_Loaders/src/edu/ncsa/model/loaders/MeshLoader_STL.java
 */
package pt.inevo.encontra.threed.model.io;

import pt.inevo.encontra.threed.model.BufferedModel;
import pt.inevo.encontra.threed.model.geoset.BufferedMesh;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

class ModelReaderStl extends ModelReader {

	public ModelReaderStl(String name, String extension) {
		super(name, extension);
	}

	@Override
	public BufferedModel read(String filename) throws IOException {
		BufferedModel model = new BufferedModel(_name, _extension);
		BufferedMesh mesh;

        //Open file and read in vertices/faces
        Scanner sc;
        String token;
        // At this time, I am assuming the file is properly formatted. Error handling can be added later


        //BufferedReader ins = new BufferedReader(new FileReader(filename));
        sc = new Scanner(new FileReader(filename));
        //sc.useLocale(Locale.US);
        token = sc.next();
        if("solid".equalsIgnoreCase(token)){ // ASCII Format
            sc.close();
            mesh = parseAscii(filename);
        }else { // Binary format
            sc.close(); // To reopen the file with FileInputStream
            mesh = parseBinary(filename);
        }

        model.addMesh(mesh);

        System.out.println("Model loaded");
        return model;
	}

    private BufferedMesh parseBinary(String filename) throws IOException {
        BufferedMesh mesh = new BufferedMesh();

        Map<String, Integer> added_vertices = new HashMap<String, Integer>();
        int v_index = 0;
        float n_index = 0;

        FileInputStream fis = new FileInputStream(filename);
        fis.skip(80); // The first 80 bytes are the header, which we don't need

        byte[] buffer = new byte[4];

        // The next 4 bytes represent an unsigned int which is the
        // number of facets in the file
        fis.read(buffer);
        int facets = bytesToInt(buffer);
        // We are assuming little-endian

        for(int faceCount = 0; faceCount < facets; faceCount++){
            //normals

            fis.read(buffer);
            float nx = bytesToFloat(buffer);
            fis.read(buffer);
            float ny = bytesToFloat(buffer);
            fis.read(buffer);
            float nz = bytesToFloat(buffer);

            mesh.addNormal(nx,ny,nz);

            List<Integer> v = new ArrayList<Integer>();
            List<Integer> vt = new ArrayList<Integer>();
            List<Float> vn = new ArrayList<Float>();

            vn.add(n_index);
            n_index++;

            // Next we have 3 floats representing a vertex (x, y, z)
            // This is repeated two more times
            for(int j = 0; j < 3; j++){
                fis.read(buffer);
                double vx = (double) bytesToFloat(buffer);
                fis.read(buffer);
                double vy = (double) bytesToFloat(buffer);
                fis.read(buffer);
                double vz = (double) bytesToFloat(buffer);

                String vert = Double.toString(vx) + Double.toString(vy) + Double.toString(vz);

                if (!added_vertices.containsKey(vert)) {
                    added_vertices.put(vert, v_index);
                    mesh.addVertex(vx, vy, vz);
                    v.add(v_index);
                    v_index++;
                } else {
                    v.add(added_vertices.get(vert));
                }
            }

            mesh.addFace(v);

            fis.skip(2);
            // These last two bytes are the attribute byte count
            // We don't need this information
        }

        fis.close();

        return mesh;
    }

    public BufferedMesh parseAscii(String filename) throws IOException {
        BufferedMesh mesh = new BufferedMesh();

        Map<String, Integer> added_vertices = new HashMap<String, Integer>();
        int v_index = 0;
        float n_index = 0;

//        BufferedReader ins = new BufferedReader(new FileReader(filename));
        Scanner sc = new Scanner(new FileReader(filename));
        //sc.useLocale(Locale.US);

        while(sc.hasNextLine()) {

            if (sc.findInLine("solid") != null) {
                sc.nextLine();
                continue;
            }


            if ((sc.findInLine("endsolid") != null) || !sc.hasNext()) { // We've reached the end of the file
                break;

            } else {

                String text = sc.next();

                if (text.compareTo("facet") == 0) {
                    //normal
                    text = sc.next();
                    float nx = Float.parseFloat(sc.next());
                    float ny = Float.parseFloat(sc.next());
                    float nz = Float.parseFloat(sc.next());
                    mesh.addNormal(nx, ny, nz);
                    sc.nextLine();

                    if (sc.findInLine("outer loop") != null) {
                        //removes outer loop line
                        sc.nextLine();
                        List<Integer> v = new ArrayList<Integer>();
                        List<Integer> vt = new ArrayList<Integer>();
                        List<Float> vn = new ArrayList<Float>();

                        vn.add(n_index);
                        n_index++;

                        while (sc.findInLine("vertex") != null) {
                            double vx = Double.parseDouble(sc.next());
                            double vy = Double.parseDouble(sc.next());
                            double vz = Double.parseDouble(sc.next());
                            String vert = Double.toString(vx) + Double.toString(vy) + Double.toString(vz);

                            if (!added_vertices.containsKey(vert)) {
                                added_vertices.put(vert, v_index);
                                mesh.addVertex(vx, vy, vz);
                                v.add(v_index);
                                v_index++;
                            } else {
                                v.add(added_vertices.get(vert));
                            }
                            sc.nextLine();
                        }

                        mesh.addFace(v, vt, vn);
                        if (sc.findInLine("endfacet") != null) {
                            sc.nextLine();
                        }
                    }
                }
            }
        }
        return mesh;

    }


    /**
     * Convert an array of 4 bytes to an int
     * @param array the array to convert
     * @return the array converted to an int
     */
    private static int bytesToInt(byte[] array)
    {
        // array is assumed to consist of 4 bytes
        int i = (array[3] << 24) | (array[2] << 16) | (array[1] << 8) | array[0];
        return i;
    }

    /**
     * Convert an array of 4 bytes to an int
     * @param array the array to convert
     * @return the array converted to an int
     */
    private static float bytesToFloat(byte[] array)
    {
        // array is assumed to consist of 4 bytes
        return ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }
}
