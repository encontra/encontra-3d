package pt.inevo.encontra.threed.utils.view;

import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.Point3D;
import pt.inevo.encontra.threed.utils.Scene;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

class SceneRenderSilhouette extends SceneRender {
	public SceneRenderSilhouette(Scene scene) {
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
	    
	    //glu.gluLookAt(1.0f, 1.0f, 1.0f,
	    glu.gluLookAt(cameraPosition.getX(), cameraPosition.getY(), cameraPosition.getZ(),
  			  		  cameraLookAt.getX(), cameraLookAt.getY(), cameraLookAt.getZ(),
  			  		  cameraUp.getX(), cameraUp.getY(), cameraUp.getZ());

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL); // Draw Backfacing Polygons As Wireframes ( NEW )
		gl.glLineWidth(3.0f); // Set The Line Width ( NEW )

		gl.glCullFace(GL2.GL_FRONT); // Don't Draw Any Front-Facing Polygons ( NEW )
		gl.glDepthFunc(GL2.GL_LEQUAL); // Change The Depth Mode ( NEW )
		
	    gl.glPushMatrix();
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		for(Model model : scene.getDrawables()) {
			model.draw(drawable);
		}
		
		gl.glPopMatrix();
		gl.glFlush();
	}
}