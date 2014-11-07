/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */

package pt.inevo.encontra.threed.viewer.filters;

import java.io.File;

import javax.swing.filechooser.FileFilter;


public class FolderFilter extends FileFilter {
	@Override
	public boolean accept(File file) {
		return file.isDirectory();
	}

	@Override
	public String getDescription() {
		return "We only take directories";
	}
}