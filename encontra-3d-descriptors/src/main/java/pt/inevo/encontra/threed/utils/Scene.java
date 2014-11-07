/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.utils;

import com.jogamp.opengl.util.awt.AWTGLReadBufferUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.Point3D;
import pt.inevo.encontra.threed.utils.view.SceneRenderFactory;

import javax.imageio.ImageIO;
import javax.media.opengl.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scene {

    private static final Log log = LogFactory.getLog(Scene.class);

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
	public BufferedImage extractContourn() {
		return extractSnapshot(Scene.Type.Contourn);
	}
	public BufferedImage extractSilhouette() {
		return extractSnapshot(Scene.Type.Silhouette);
	}

    public BufferedImage extractSnapshot(Scene.Type type) {
        return extractSnapshot(type);
    }

	public BufferedImage extractSnapshot(Scene.Type type, File output, String formatName) {

		BufferedImage screenshot = null;

        GLAutoDrawable buffer = null;
		GLProfile glp = GLProfile.getDefault(GLProfile.getDefaultDevice());  
		GLCapabilities caps = new GLCapabilities(glp);
        caps.setFBO(true);
		if (GLDrawableFactory.getFactory(glp).canCreateFBO(GLProfile.getDefaultDevice(), GLProfile.getDefault())) {
		    try {
		        buffer = GLDrawableFactory.getFactory(glp).createOffscreenAutoDrawable(GLProfile.getDefaultDevice(), caps, null, _width, _height);
		        buffer.addGLEventListener(SceneRenderFactory.create(this, type));
		        //buffer.display();
                buffer.display(); // read from front buffer due to FBO+MSAA -> double-buffer
                buffer.display(); // now we have prev. image in front buffer to be read out

                buffer.getContext().makeCurrent();

                AWTGLReadBufferUtil glReadBufferUtil = new AWTGLReadBufferUtil(buffer.getGLProfile(), false);
                screenshot = glReadBufferUtil.readPixelsToBufferedImage(buffer.getGL(), true);

		        buffer.getContext().release();
		        buffer.destroy();
		    } catch (GLException e) {
		        log.error(e.getMessage());
		    }
		} else {  
		    log.error("Graphics processor does not have FBO capability!");
		}

        if (output != null) {
            log.info("Writing snapshot to " + output);
            writeToFile(screenshot, formatName, output);
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















