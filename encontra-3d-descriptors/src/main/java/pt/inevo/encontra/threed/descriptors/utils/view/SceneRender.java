// Copyright 2013 Pedro B. Pascoal
package pt.inevo.encontra.threed.descriptors.utils.view;

import pt.inevo.encontra.threed.Model;
import pt.inevo.encontra.threed.Point3D;
import pt.inevo.encontra.threed.descriptors.utils.Scene;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class SceneRender implements GLEventListener {
	
	protected GLU glu; // for the GL Utility
	
	protected Scene scene = null;
	public SceneRender(Scene scene) {
		this.scene = scene;
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		glu = new GLU();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glClearDepth (1.0f); // Depth Buffer Setup

		gl.glEnable(GL.GL_DEPTH_TEST); // Enable Depth Testing
		gl.glDepthFunc(GL.GL_LESS); // The Type Of Depth Test To Do
		
		gl.glShadeModel(GL2.GL_SMOOTH); // Enables Smooth Color Shading ( NEW )
		gl.glDisable(GL2.GL_LINE_SMOOTH); // Initially Disable Line Smoothing ( NEW )

		gl.glEnable (GL.GL_CULL_FACE); // Enable OpenGL Face Culling ( NEW )
		gl.glDisable (GL2.GL_LIGHTING); // Disable OpenGL Lighting ( NEW )
		
		gl.glShadeModel(GL2.GL_FLAT);
		gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();
	    gl.glOrtho(-1.0f, 1.0f, -1.0f, 1.0f, -2.0f, 2.0f);
	    
		//required for the contourn creation
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_POINT_SMOOTH);
		gl.glEnable(GL.GL_LINE_SMOOTH);
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
	@Override
	public void dispose(GLAutoDrawable drawable) {
	}
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	}
}
