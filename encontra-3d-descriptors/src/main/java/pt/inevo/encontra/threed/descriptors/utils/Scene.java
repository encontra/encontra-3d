/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.descriptors.utils;

import com.jogamp.opengl.util.awt.Screenshot;
import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.Point3D;
import pt.inevo.encontra.threed.descriptors.utils.view.SceneRenderFactory;

import javax.imageio.ImageIO;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLPbuffer;
import javax.media.opengl.GLProfile;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scene {
	public enum Type {
		Silhouette,
		Contourn,
		Sketch,
		Default;
	}

	private int _width = 512;
	private int _height = 512;
	
	private Point3D _cameraPosition;
	private Point3D _cameraLookAt;
	private Point3D _cameraUp;

	private List<Model> _drawables = new ArrayList<Model>();
	
	public Scene() {
		_width = 512;
		_height = 512;
		
		_cameraPosition = new Point3D(1.0, 1.0, 1.0);
		_cameraLookAt = new Point3D();
		_cameraUp = new Point3D(0.0, 1.0, 0.0);
	}
	
	public BufferedImage extractView() {
		return extractSnapshot(Scene.Type.Default);
	}
	public BufferedImage extractSketch() {
		return extractSnapshot(Scene.Type.Sketch);
	}
	public BufferedImage extractSketch(File output, String formatName) {
		BufferedImage silhouette = extractSnapshot(Scene.Type.Sketch);
		if(silhouette != null)
			writeToFile(silhouette, formatName, output);
		return silhouette;
	}

	public BufferedImage extractContourn() {
		return extractSnapshot(Scene.Type.Contourn);
	}

	public BufferedImage extractSilhouette() {
		return extractSnapshot(Scene.Type.Silhouette);
	}
	public BufferedImage extractSilhouette(File output, String formatName) {
		BufferedImage silhouette = extractSnapshot(Scene.Type.Silhouette);
		if(silhouette != null)
			writeToFile(silhouette, formatName, output);
		return silhouette;
	}

	private BufferedImage extractSnapshot(Scene.Type type) {
		BufferedImage screenshot = null;
		
		GLPbuffer buffer = null;  
		GLProfile glp = GLProfile.getDefault(GLProfile.getDefaultDevice());  
		GLCapabilities caps = new GLCapabilities(glp);
		//caps.setDoubleBuffered(false);  
		if (GLDrawableFactory.getFactory(glp).canCreateGLPbuffer(GLProfile.getDefaultDevice(), GLProfile.getDefault())) {
		    try {  
		        buffer = GLDrawableFactory.getFactory(glp).createGLPbuffer(GLProfile.getDefaultDevice(), caps, null, _width, _height, null);
		        buffer.addGLEventListener(SceneRenderFactory.create(this, type));
		        buffer.getContext().makeCurrent();  
		        buffer.display();
		        
		        screenshot = Screenshot.readToBufferedImage(_width, _height);
		        
		        buffer.getContext().release();  
		        buffer.destroy(); 
		    } catch (Exception e) {  
		        buffer = null;  
		    }
		} else {  
		    System.out.println("Graphics processor does not have PBuffer capability!");  
		} 
		return screenshot;
	}
	private void writeToFile(BufferedImage image, String formatName, File output) {
		try {
			ImageIO.write(image, "png", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setDimensions(int width, int height) {
		this._width = width;
		this._height = height;
	}
	
	public void addDrawable(Model model) {
		_drawables.add(model);
	}
	public List<Model> getDrawables() {
		return _drawables;
	}

	public void setCameraPosition(double x, double y, double z) {
		setCameraPosition(new Point3D(x, y, z));
	}
	public void setCameraPosition(Point3D position) {
		_cameraPosition = position;
	}
	
	public void setCameraUp(double x, double y, double z) {
		setCameraUp(new Point3D(x, y, z));
	}
	public void setCameraUp(Point3D position) {
		_cameraUp = position;
	}
	
	public Point3D getCameraPosition() {
		return _cameraPosition;
	}
    public Point3D getCameraLookAt() {
    	return _cameraLookAt;
    }
    public Point3D getCameraUp() {
    	return _cameraUp;
    }
}















