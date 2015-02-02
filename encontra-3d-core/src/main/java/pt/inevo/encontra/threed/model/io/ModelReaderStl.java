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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Obj Wavefront
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

        BufferedReader ins = new BufferedReader(new FileReader(filename));
        sc = new Scanner(ins.readLine());
        token = sc.next();
        if("solid".equalsIgnoreCase(token)){ // ASCII Format
            sc.close();
            mesh = parseAscii(ins);
        }else { // Binary format
            ins.close(); // To reopen the file with FileInputStream
            mesh = parseBinary(filename);
        }

        model.addMesh(mesh);

        System.out.println("Model loaded");
        return model;
	}

    /**
     * Convert an array of 4 bytes to an int
     * @param array the array to convert
     * @return the array converted to an int
     */
    private static int bytesToInt(byte[] array)
    {
        // array is assumed to consist of 4 bytes

        return (array[3] << 24) | (array[2] << 16) | (array[1] << 8) | array[0];
    }

    private BufferedMesh parseBinary(String filename) throws IOException {
        BufferedMesh mesh = new BufferedMesh();

        FileInputStream fis = new FileInputStream(filename);
        fis.skip(80); // The first 80 bytes are the header, which we don't need

        byte[] buffer = new byte[4];

        // The next 4 bytes represent an unsigned int which is the
        // number of facets in the file
        fis.read(buffer);
        int facets = bytesToInt(buffer);
        // We are assuming little-endian


        for(int faceCount = 0; faceCount < facets; faceCount++){
            fis.skip(12); // 12 bytes for the normals. We don't need these

            // Next we have 3 floats representing a vertex (x, y, z)
            // This is repeated two more times

            for(int j = 0; j < 3; j++){
                fis.read(buffer);
                double x = (double) Float.intBitsToFloat(bytesToInt(buffer));
                fis.read(buffer);
                double y = (double) Float.intBitsToFloat(bytesToInt(buffer));
                fis.read(buffer);
                double z = (double) Float.intBitsToFloat(bytesToInt(buffer));
                mesh.addVertex(x, y, z);
            }

            List<Integer> v = new ArrayList<Integer>();
            for (int i = 0; i<3; i++)
            {
                v.add(3*faceCount + i);
            }
            mesh.addFace(v);

            fis.skip(2);
            // These last two bytes are the attribute byte count
            // We don't need this information
        }

        fis.close();

        return mesh;
    }

    public BufferedMesh parseAscii(BufferedReader ins) throws IOException {
        BufferedMesh mesh = new BufferedMesh();
        int faceCount = 0;
        while(true){ // This loop will be broken with break statements

            Scanner sc = new Scanner(ins.readLine()); // If the file is properly formatted, we don't have to worry about readLine() returning null
            String token = sc.next();
            if(("endsolid".equalsIgnoreCase(token)) || ("end".equalsIgnoreCase(token))){ // We've reached the end of the file
                break;
            }else{ // Implicitly: else if("facet".equals(token)) . This is the only other valid possibility
                // The rest of the line is "normal" followed by three coordinates
                // We don't need any of this
                ins.readLine(); // The next line MUST be "outer loop"
                // The next three lines are vertex statements
                for(int i = 0; i < 3; i++){
                    sc.close();
                    sc = new Scanner(ins.readLine());
                    sc.next(); // This token is always "vertex"
                    // The three remaining tokens are the vertex coordinates
                    double vx = (double) sc.nextFloat();
                    double vy = (double) sc.nextFloat();
                    double vz = (double) sc.nextFloat();
                    mesh.addVertex(vx, vy, vz);
                }
                ins.readLine(); // This line MUST be "endloop"
                ins.readLine(); // This line MUST be "endfacet"
                List<Integer> v = new ArrayList<Integer>();
                for (int i = 0; i<3; i++)
                {
                    v.add(3*faceCount + i);
                }
                mesh.addFace(v);
                faceCount++;
            }
        }
        ins.close();
        return mesh;

    }

}
