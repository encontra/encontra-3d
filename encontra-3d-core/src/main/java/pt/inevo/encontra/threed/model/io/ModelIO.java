/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.model.io;

import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.model.BufferedModel;

import java.io.File;
import java.io.IOException;

/**
 * A class containing static convenience methods for locating ModelReaders and ModelWriters, and performing simple encoding and decoding.
 */
public final class ModelIO extends Object {


	public enum OutputFormat {
        OBJ("obj"),
        OFF("off");

        private final String ext;

        private OutputFormat(final String ext) {
            this.ext = ext;
        }

        @Override
        public String toString() {
            return ext;
        }
	}
	/**
	 * Returns a BufferedModel as the result of decoding a supplied File with an ModelReader chosen automatically from among those currently registered.
	 * 
	 * @param file - an InputStream to read from.
	 * @return
	 * a BufferedModel containing the decoded contents of the input, or null.
	 * @throws IllegalArgumentException - if input is null.
	 * @throws java.io.IOException - if an error occurs during reading.
	 */
	public static BufferedModel read(File file) throws IOException, IllegalArgumentException {
		if(file == null)
			throw new IllegalArgumentException("ModelIO: Argument cannot be null");

		System.out.println("ModelIO read file: " + file.getName());

		if(!file.exists() || !file.isFile())
			throw new IOException("ModelIO: file not found!");

		String name = file.getName();
		String extension = null;
        int i = name.lastIndexOf('.');
        if (i > 0 &&  i < name.length() - 1) {
        	extension = name.substring(i+1).toLowerCase();
        }
		String absolutePath = file.getAbsolutePath();

		if(extension.toString().compareToIgnoreCase(OutputFormat.OFF.toString()) == 0) {
			// Object File Format (OFF)
			return (new ModelReaderOff(name, extension)).read(absolutePath);
		} else if(extension.toString().compareToIgnoreCase(OutputFormat.OBJ.toString()) == 0) {
			// Wavefront
			return (new ModelReaderObj(name, extension)).read(absolutePath);
		} else if(extension.toString().compareToIgnoreCase("mdl") == 0) {
			// MDL Blizzard Entertainment 3D model file
			return (new ModelReaderMdl(name, extension)).read(absolutePath);
		} else if(extension.toString().compareToIgnoreCase("stl") == 0) {
            // STL 3D model file
            return (new ModelReaderStl(name, extension)).read(absolutePath);
        }
        else {
			throw new IOException("ModelIO: file format not recognized");
		}
	}

	/**
	 * Writes an image using an arbitrary ModelWriter that supports the given format to a File.
	 * If there is already a File present, its contents are discarded.
	 *
	 * @param model - a Model to be written.
	 * @param format - a OutputFormat containing the format.
	 * @param output - a File to be written to.
	 * @return
	 * false if no appropriate writer is found.
	 * @throws IllegalArgumentException - if any parameter is null.
	 * @throws java.io.IOException - if an error occurs during writing.
	 */
	public static boolean write(Model model, OutputFormat format, File output) throws IOException, IllegalArgumentException {
		if(output == null)
			throw new IllegalArgumentException("ModelIO: Argument cannot be null");

		if(format == OutputFormat.OBJ) {
			(new ModelWriterObj()).write(model, output);
			return true;
		} else if(format == OutputFormat.OFF) {
			(new ModelWriterOff()).write(model, output);
			return true;
		} else {
			return false;
		}
	}

    /**
     * Writes an image using an arbitrary ImageWriter that supports the given format to a File.
     * If there is already a File present, its contents are discarded.
     *
     * @param model - a Model to be written.
     * @param formatName - a String containing the informal name of the format.
     * @param output - a File to be written to.
     * @return
     * false if no appropriate writer is found.
     * @throws IllegalArgumentException - if any parameter is null.
     * @throws java.io.IOException - if an error occurs during writing.
     */
    public static boolean write(Model model, String formatName, File output) throws IOException, IllegalArgumentException {
        return write(model, OutputFormat.valueOf(formatName), output);
    }
}
