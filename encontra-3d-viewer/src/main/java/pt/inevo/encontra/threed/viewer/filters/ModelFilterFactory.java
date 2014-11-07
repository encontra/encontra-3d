/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.viewer.filters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * This class implements the factory pattern, and returns a FileFilter, for a 3d model file format that is available.
 * All the available filters are in the ModelFilterFactory.Type enumerator. 
 * @author Pedro B. Pascoal
 */
public class ModelFilterFactory {
	
	/**
	 * Contains the list of all available 3D Model FileFilters.
	 * @author Pedro B. Pascoal
	 */
	public enum Type {
		ThORCODE,
		MODEL3D,
		OBJ,
		OFF,
		MDL
	}
	
	/**
	 * @param type - ModelFilterFactory.Type of the file filter.
	 * @return
	 * A FileFilter for the type of 3D model file format inputed in the type.
	 */
	public static FileFilter create(Type type) {
		
		if(type == Type.MODEL3D) { // Contains all 3d model file format
			return new FileFilter() {
				@Override
				public String getDescription() { return "3D Models (*.obj; *.off; *.mdl)"; }
				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) {
				        return true;
				    }
					String extension = getFileExtension(f);
				    if (extension != null) {
				        if (extension.equals("obj") ||
				            extension.equals("off") ||
				            extension.equals("mdl")) {
				                return true;
				        }
				    }
					return false;
				}
			};
		} else if(type == Type.OBJ) {
			return new FileFilter() {
				@Override
				public String getDescription() { return "WaveFront Object (*.obj)"; }
				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) { return true; }
					String extension = getFileExtension(f);
					if (extension != null) {
				        if (extension.equals("obj")) {
				                return true;
				        }
				    }
					return false;
				}
			};
		} else if(type == Type.OFF) {
			return new FileFilter() {
				@Override
				public String getDescription() { return "Object File Format (*.off)"; }
				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) { return true; }
					String extension = getFileExtension(f);
					if (extension != null) {
				        if (extension.equals("off")) {
				                return true;
				        }
				    }
					return false;
				}
			};
		} else if(type == Type.MDL) {
			return new FileFilter() {
				@Override
				public String getDescription() { return "Blizzard Warcraft III Model (*.mdl)"; }
				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) { return true; }
					String extension = getFileExtension(f);
					if (extension != null) {
				        if (extension.equals("off")) {
				                return true;
				        }
				    }
					return false;
				}
			};
		} else if(type == Type.ThORCODE) {
			return new FileFilter() {
				@Override
				public String getDescription() { return "ThOR Java Code (*.java)"; }
				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) { return true; }
					String extension = getFileExtension(f);
					if (extension != null) {
				        if (extension.equals("java")) {
				                return true;
				        }
				    }
					return false;
				}
			};
		}
		else
			return null;
	}
	
	/**
	 * @param f - the file to identify the file extension.
	 * @return
	 * The extension of the File.
	 */
	private static String getFileExtension(File f) {
		String extension = null;
		String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1)
        	extension = s.substring(i+1).toLowerCase();
        return extension;
	}
}
