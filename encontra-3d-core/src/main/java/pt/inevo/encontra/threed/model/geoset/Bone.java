/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */
package pt.inevo.encontra.threed.model.geoset;
import pt.inevo.encontra.threed.Point3D;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

public class Bone {
	int _objectId = 0;
	int _parentId = 0;
	
	Bone _parentBone = null;
	
	String _name = "";
	Point3D _pivotPoint = null;
	
	Point3D _barycenter = new Point3D();
	
	public Bone(String name, int objectId, int parentId, Point3D pivotPoint) {
		_name = name;
		_objectId = objectId;
		_parentId = parentId;
		_pivotPoint = pivotPoint;
	}
	
	
	public int getObjectId() {
		return _objectId;
	}
	public int getParentId() {
		return _parentId;
	}
	public String getName() {
		return _name;
	}
	public void setPivotPoint(Point3D point) {
		_pivotPoint = point;
	}
	public Point3D getPivotPoint() {
		return _pivotPoint;
	}
	public void setParent(Bone bone) {
		_parentBone = bone;
	}
	
	public void draw(GLAutoDrawable drawable) {
		
		GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();
		
        final float radius = 0.02f;
        final int slices = 8;
        final int stacks = 8;
        
		GLUquadric quadric = glu.gluNewQuadric();
        glu.gluQuadricDrawStyle(quadric, GLU.GLU_FILL);
        glu.gluQuadricNormals(quadric, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(quadric, GLU.GLU_OUTSIDE);
        
        gl.glPushMatrix();
		gl.glColor3f(0.3f, 0.5f, 1.0f);
        gl.glTranslated(_pivotPoint.getX(), _pivotPoint.getY(), _pivotPoint.getZ());
        glu.gluSphere(quadric, radius, slices, stacks);
        glu.gluDeleteQuadric(quadric);
        gl.glPopMatrix();
        

        // draw the bone connection between parent-child
        if(_parentBone != null) {
	        Point3D origin = _parentBone.getPivotPoint();
	        //Point3D medianPoint = new Point3D.Double();

	        /*
	        medianPoint.add(origin);
	        medianPoint.add(_pivotPoint);
	        medianPoint.div(2);
	        */
	        
	        gl.glPushMatrix();
			gl.glColor3f(1.0f, 1.0f, 0.2f);
			//gl.glColor3f(0.3f, 0.5f, 1.0f);
			
			gl.glLineWidth(2.0f);
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex3d(origin.getX(),origin.getY(),origin.getZ());
			gl.glVertex3d(_pivotPoint.getX(),_pivotPoint.getY(),_pivotPoint.getZ());
			gl.glEnd();

			gl.glLineWidth(1.0f);
	        gl.glPopMatrix();
        }
	}

	public void translate(double x, double y, double z) {
		_pivotPoint.add(new Point3D(x, y, z));
		//_barycenter = new Point3D.Double(x, y, z);
	}


	public void scale(double value) {
		_pivotPoint.mul(value);
	}
}