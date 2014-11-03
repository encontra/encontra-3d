/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.model.io;

import pt.inevo.encontra.threed.Point3D;
import pt.inevo.encontra.threed.model.BufferedModel;
import pt.inevo.encontra.threed.model.geoset.Bone;
import pt.inevo.encontra.threed.model.geoset.BufferedMesh;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class ModelReaderMdl extends ModelReader {

	private Map<Integer, Bone> bones = new HashMap<Integer, Bone>();
	private Map<Integer, Point3D> pivotPoint = new HashMap<Integer, Point3D>();

	public ModelReaderMdl(String name, String extension) {
		super(name, extension);
	}

	@Override
	public BufferedModel read(String filename) throws IOException {
		BufferedModel model = new BufferedModel(_name, _extension);

		Scanner line = null;
		Scanner root = new Scanner(new FileReader(filename));
		while(root.hasNext()) {
			line = new Scanner(root.nextLine());
			String type = line.next();
			if(type.startsWith("//")) {
				//ignore all commented text
			} else if(type.compareToIgnoreCase("Model") == 0) {
			} else if(type.compareToIgnoreCase("Sequences") == 0) {
			} else if(type.compareToIgnoreCase("Textures") == 0) {
			} else if(type.compareToIgnoreCase("Materials") == 0) {
			} else if(type.compareToIgnoreCase("Geoset") == 0) {
				if (line.next().compareTo("{") != 0) {
					line.close();
					throw new IOException("ModelReaderMdl: file format is not correct");
				}
				System.out.println("Geoset");
				model.addMesh(readGeoset(root));
			} else if(type.compareToIgnoreCase("GeosetAnim") == 0) {
			} else if(type.compareToIgnoreCase("Bone") == 0) {
				String name = readName(line);
				if (line.next().compareTo("{") != 0) {
					root.close();
					line.close();
					throw new IOException("ModelReaderMdl: attachment format is not correct");
				}
				System.out.println("Bone: " + name);
				int objectId = -1;
				int parentId = -1;
				
				line = new Scanner(root.nextLine());
				String entry = line.next();
				while(entry.compareToIgnoreCase("}") != 0) {
					System.out.println("Bone entry: " + entry);
					if(entry.compareToIgnoreCase("ObjectId") == 0) {
						objectId = readIdentifier(line);
						System.out.println("objectId: " + objectId);
					} else if(entry.compareToIgnoreCase("Parent") == 0) {
						parentId = readIdentifier(line);
						System.out.println("parentId: " + parentId);
					//} else if(type.compareToIgnoreCase("Rotation") == 0) {
					} else {
						break;
					}
					line = new Scanner(root.nextLine());
					entry = line.next();
				}
				if(objectId >= 0) {
					System.out.println("Bone added");
					bones.put(objectId, new Bone(name, objectId, parentId, new Point3D()));
				}

				
			} else if(type.compareToIgnoreCase("Attachment") == 0) {
				String name = readName(line);
				if (line.next().compareTo("{") != 0) {
					root.close();
					line.close();
					throw new IOException("ModelReaderMdl: attachment format is not correct");
				}
				System.out.println("Attachment: " + name);
			} else if(type.compareToIgnoreCase("PivotPoints") == 0) {
				int count = line.nextInt();
				if (line.next().compareTo("{") != 0) {
					root.close();
					line.close();
					throw new IOException("ModelReaderMdl: attachment format is not correct");
				}
				
				for(int i = 0; i < count; i++)
					pivotPoint.put(i, readPivotPoints(root));
			}
		}
		
		//assign pivotpoints to bones
		for(int boneId : bones.keySet()) {
			Bone bone = bones.get(boneId);
			int parentId = bone.getParentId();
			
			if(parentId >= 0) {
				bone.setParent(bones.get(parentId));
			}
			bone.setPivotPoint(pivotPoint.get(boneId));
			model.addBone(bones.get(boneId));
		}
		
		
		line.close();
		root.close();
		return model;
	}
	
	private static String readName(Scanner scanner) throws IOException {
		String name = scanner.next();
		if(name.startsWith("\"")) {
			while(!name.endsWith("\"")) {
				name += " " + scanner.next();
				if(!scanner.hasNext())
					throw new IOException("ModelReaderMdl: file format is not correct");
			}
		}
		return name.replace("\"", "");
	}
	private static int readIdentifier(Scanner scanner) {
		String value = scanner.next();
		String[] removables = { "," };
		
		for(String removable : removables)
			value = value.replaceAll(removable, "");
		
		return Integer.parseInt(value);
	}
	
	private static BufferedMesh readGeoset(Scanner scanner) throws IOException {
		BufferedMesh mesh = new BufferedMesh();
		int pCount = 1;
		Scanner line = null;
		while(pCount > 0 && scanner.hasNext()) {
			String next_line = scanner.nextLine();
			if(next_line.length() <= 0) continue;
			
			line = new Scanner(next_line);
			String type = line.next();
			System.out.println("Geoset type: " + type + " count: " + pCount);

			if(type.startsWith("//")) {
				//ignore all commented text
			} else if(type.compareToIgnoreCase("{") == 0) {
			} else if(type.compareToIgnoreCase("}") == 0) {
				pCount--;
			} else if(type.compareToIgnoreCase("Vertices") == 0) {
				int vCount = line.nextInt(); // number of vertices
				if (line.next().compareTo("{") != 0) {
					line.close();
					throw new IOException("ModelReaderMdl: geoset format is not correct");
				}

				for (int i = 0; i < vCount; i++) {
					String v_line = scanner.nextLine();
					v_line = v_line.replaceAll("\\s", "");
					//System.out.println("Vertices Line |" + v_line + "|");
					
					if( !(v_line.startsWith("{") && v_line.endsWith("},"))) {
						line.close();
						throw new IOException("ModelReaderMdl: geoset format is not correct");
					}
					
					v_line = v_line.replace('{', ' ');
					v_line = v_line.replace('}', ' ');
					String vertices[] = v_line.split(",");

					if(vertices.length < 3) {
						line.close();
						throw new IOException("ModelReaderMdl: geoset format is not correct");
					}
					
					float x = Float.parseFloat(vertices[0]);
					float y = Float.parseFloat(vertices[1]);
					float z = Float.parseFloat(vertices[2]);
					mesh.addVertex(x, y, z);
				}
				if (scanner.next().compareTo("}") != 0) {
					line.close();
					throw new IOException("ModelReaderMdl: file format is not correct");
				}
				
			} else if(type.compareToIgnoreCase("Normals") == 0) {
				pCount++;
			} else if(type.compareToIgnoreCase("TVertices") == 0) {
				pCount++;
			} else if(type.compareToIgnoreCase("VertexGroup") == 0) {
				if (line.next().compareTo("{") != 0) {
					line.close();
					throw new IOException("ModelReaderMdl: geoset format is not correct");
				}
				
				String vg_line = scanner.nextLine();
				vg_line = vg_line.replace(',', ' ');
				vg_line = vg_line.replaceAll("\\s", "");
				while(vg_line.compareTo("}") != 0) {
					Integer.parseInt(vg_line);
					
					//MyTODO: save vertex group information
					
					vg_line = scanner.nextLine();
					vg_line = vg_line.replace(',', ' ');
					vg_line = vg_line.replaceAll("\\s", "");
				}
			} else if(type.compareToIgnoreCase("Faces") == 0) {
				int fCount = line.nextInt(); // number of triangles
				@SuppressWarnings("unused")
				int vCount = line.nextInt(); // number of vertices on triangle

				if (line.next().compareTo("{") != 0) {
					line.close();
					throw new IOException("ModelReaderMdl: file format is not correct");
				}
				
				for (int i = 0; i < fCount; i++) {
					if (scanner.next().compareTo("Triangles") != 0) {
						line.close();
						throw new IOException("ModelReaderMdl: file format is not correct");
					}
					if (scanner.next().compareTo("{") != 0) {
						line.close();
						throw new IOException("ModelReaderMdl: file format is not correct");
					}

					String triangle_line = scanner.nextLine();
					triangle_line = triangle_line.replaceAll("\\s", "");
					while(triangle_line.length() <= 0) {
						triangle_line = scanner.nextLine();
						triangle_line = triangle_line.replaceAll("\\s", "");
					}
					if( !(triangle_line.startsWith("{") && triangle_line.endsWith("},"))) {
						line.close();
						throw new IOException("ModelReaderMdl: geoset format is not correct");
					}
					
					triangle_line = triangle_line.replace('{', ' ');
					triangle_line = triangle_line.replace('}', ' ');
					triangle_line = triangle_line.replaceAll("\\s", "");
					String allVertices[] = triangle_line.split(",");
					
					for (int j = 0; j < allVertices.length-2; j+=3) {
						List<Integer> vertices = new ArrayList<Integer>();
						vertices.add(Integer.parseInt(allVertices[j]));
						vertices.add(Integer.parseInt(allVertices[j+1]));
						vertices.add(Integer.parseInt(allVertices[j+2]));
						mesh.addFace(vertices);
					}
					if (scanner.next().compareTo("}") != 0) {
						line.close();
						throw new IOException("ModelReaderMdl: file format is not correct");
					}
				}
				if (scanner.next().compareTo("}") != 0) {
					line.close();
					throw new IOException("ModelReaderMdl: file format is not correct");
				}
			} //else
				//pCount++;
		}
		line.close();
		return mesh;
	}
	private static Point3D readPivotPoints(Scanner scanner) {
		String line = scanner.nextLine();
		System.out.println("readPivotPoints: " + line);
		
		line = line.replace("{", "");
		line = line.replace("}", "");
		String[] entries = line.split(",");
		
		Point3D point = new Point3D(Double.parseDouble(entries[0]),
                                    Double.parseDouble(entries[1]),
                                    Double.parseDouble(entries[2]));
		return point;
	}
	
	
}
