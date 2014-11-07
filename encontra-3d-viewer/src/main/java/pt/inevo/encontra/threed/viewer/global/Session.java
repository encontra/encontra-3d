/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */

package pt.inevo.encontra.threed.viewer.global;

import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.utils.Normalize;

import java.io.File;

import javax.swing.JFrame;

public class Session {
	
	private JFrame _mainframe = null;
	
	private Model _model = null;
	private int _glDrawMode = 0;
	private File _browseDirectory = new File(".");
	
	
	private static Session _instance = null;
	public static Session getInstance() {
		if(_instance == null)
			_instance = new Session();
		return _instance;
	}
	
	public void setMainFrame(JFrame frame) {
		_mainframe = frame;
	}
	public JFrame getMainFrame() {
		return _mainframe;
	}

	public void setModel(Model model) {
		_model = model;
		
		if(_model != null) {
			Normalize.translation(_model);
			Normalize.scale(_model);
			Normalize.rotation(_model);
		}
	}
	public Model getModel() {
		return _model;
	}
	
	public void setGlDrawMode(int glDrawMode) {
		_glDrawMode = glDrawMode;
	}
	public int getGlDrawMode() {
		return _glDrawMode;
	}
	
	public void setBrowseDirectory(File directory) {
		_browseDirectory = directory;
	}
	public File getBrowseDirectory() {
		return _browseDirectory;
	}
	
}
