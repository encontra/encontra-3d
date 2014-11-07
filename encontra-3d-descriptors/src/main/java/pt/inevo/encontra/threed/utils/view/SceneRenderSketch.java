package pt.inevo.encontra.threed.utils.view;

import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.Point3D;
import pt.inevo.encontra.threed.utils.Scene;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

class SceneRenderSketch extends SceneRender {

	public SceneRenderSketch(Scene scene) {
		super(scene);
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    
	    Point3D cameraPosition = scene.getCameraPosition();
	    Point3D cameraLookAt = scene.getCameraLookAt();
	    Point3D cameraUp = scene.getCameraUp();
	    
	    glu.gluLookAt(cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ(),
	    			  cameraLookAt.getX(), cameraLookAt.getY(), cameraLookAt.getZ(),
					  cameraUp.getX(), cameraUp.getY(), cameraUp.getZ());

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
	    gl.glPushMatrix();
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		for(Model model : scene.getDrawables()) {
			model.draw(drawable);
		}
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		
		gl.glHint (GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST); // Use The Good Calculations ( NEW )
		gl.glEnable (GL2.GL_LINE_SMOOTH); // Enable Anti-Aliasing ( NEW )
		
		gl.glEnable(GL2.GL_BLEND); // Enable Blending ( NEW )
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA); // Set The Blend Mode ( NEW )

		gl.glPolygonMode(GL2.GL_BACK, GL2.GL_LINE); // Draw Backfacing Polygons As Wireframes ( NEW )
		gl.glLineWidth(5.0f); // Set The Line Width ( NEW )
		
		gl.glCullFace(GL2.GL_FRONT); // Don't Draw Any Front-Facing Polygons ( NEW )
		gl.glDepthFunc(GL2.GL_LEQUAL); // Change The Depth Mode ( NEW )
		gl.glColor3f(0.0f, 0.0f, 0.0f); // Set The Outline Color ( NEW )
		
		for(Model model : scene.getDrawables()) {
			model.draw(drawable);
		}

		gl.glDepthFunc(GL2.GL_LESS); // Reset The Depth-Testing Mode ( NEW )
		gl.glCullFace(GL2.GL_BACK); // Reset The Face To Be Culled ( NEW )
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL); // Reset Back-Facing Polygon Drawing Mode ( NEW )
		gl.glDisable(GL2.GL_BLEND); // Disable Blending ( NEW )
		gl.glPopMatrix();
		
		gl.glFlush();
	}
}