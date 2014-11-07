/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */

package pt.inevo.encontra.threed.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.jogamp.opengl.util.awt.TextRenderer;
import pt.inevo.encontra.threed.Point3D;
import pt.inevo.encontra.threed.utils.Scene;
import pt.inevo.encontra.threed.viewer.filters.FolderFilter;
import pt.inevo.encontra.threed.viewer.global.Session;

public class MultiviewFrame implements IModelviewerFrame {

	private JFrame frame = null;

	private final int width = 450;
	private final int height = 450;

	private final Point3D FRONT = new Point3D(0.0, 0.0, 0.0);
	private final Point3D SIDE = new Point3D(0.0, 1.0, 0.0);
	private final Point3D TOP = new Point3D(1.0, 0.0, 0.0);
		
	public MultiviewFrame(String title) {
		
		frame = new JFrame(title);

		frame.setSize(width*2, height*2);
		frame.setContentPane(createContentPane());
		frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	frame.getContentPane().removeAll();
            	frame.removeAll();
            	frame.dispose();
            }
        });
		
        frame.pack();
        frame.setVisible(true);
	}
	
	private JPanel createContentPane() {
		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5) );
		
		panel.add(createGLCanvas(new GLView("X-Axis camera view", FRONT), new Dimension(width, height)));
		panel.add(createGLCanvas(new GLView("Y-Axis camera view", TOP), new Dimension(width, height)));
		panel.add(createGLCanvas(new GLView("Z-Axis camera view", SIDE), new Dimension(width, height)));
		panel.add(createSquareJPanel(Color.white, new Dimension(width, height)));
		
		return panel;
	}
	private GLCanvas createGLCanvas(GLView view, Dimension size) {
		GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);
        canvas.setPreferredSize(size);
        canvas.addGLEventListener(view);
        return canvas;
	}
	
	private JPanel createSquareJPanel(Color color, Dimension size) {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(color);
		
		JTextArea panelDescription = new JTextArea(
				"This Window contain views of the same model from three camera positions" + System.lineSeparator() +
				"X-Axis: Camera placed in the X-Axis" + System.lineSeparator() +
				"Y-Axis: Camera placed in the Y-Axis" + System.lineSeparator() +
				"Z-Axis: Camera placed in the Z-Axis" + System.lineSeparator() +
				System.lineSeparator() +
				"");
		panelDescription.setEditable(false);
		panel.add(panelDescription);
		
		JButton getInfoButton = new JButton("Show model info");
		getInfoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					Point3D barycenter = Session.getInstance().getModel().getBarycenter();
					
					JOptionPane.showMessageDialog(frame,
							"Model name: " + Session.getInstance().getModel().getName() + System.lineSeparator() +
							"Vertex Count: " + Session.getInstance().getModel().countVertices() + System.lineSeparator() +
							"Faces Count: " + Session.getInstance().getModel().countFaces() + System.lineSeparator() +
							System.lineSeparator() +
							"Barycenter: (" + barycenter.getX() + ", " + barycenter.getY() + ", " + barycenter.getZ() + ")" + System.lineSeparator() +
							"Is mainfold: " + Session.getInstance().getModel().isManifold() + System.lineSeparator() +
							"Surface Area: " + Session.getInstance().getModel().getSurfaceArea(),
							"Model Information",
							JOptionPane.INFORMATION_MESSAGE);
				} catch(java.lang.NullPointerException exception) {
					JOptionPane.showMessageDialog(Session.getInstance().getMainFrame(),
							exception.getMessage(),
							"Error information not found",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.add(getInfoButton);

		JButton screenshotButton = new JButton("Save Screenshot");
		screenshotButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(Session.getInstance().getBrowseDirectory());
				//_fileChooser.setControlButtonsAreShown( false );
				
				chooser.setFileFilter(new FolderFilter());
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int value = chooser.showSaveDialog(Session.getInstance().getMainFrame());

				if(value == JFileChooser.APPROVE_OPTION) {
					File output = null;
					Scene scene = new Scene();
					scene.addDrawable(Session.getInstance().getModel());

					// extract x-axis
					scene.setCameraPosition(1, 0, 0);
					output = new File( chooser.getSelectedFile().getAbsolutePath() + "\\" +
											Session.getInstance().getModel().getName() + "." +
											"x-axis" +
											".png");
					scene.extractSnapshot(Scene.Type.Silhouette, output, "png");
					// extract z-axis
					scene.setCameraPosition(0, 0, 1);
					output = new File( chooser.getSelectedFile().getAbsolutePath() + "\\" +
							Session.getInstance().getModel().getName() + "." +
							"z-axis" +
							".png");
					scene.extractSnapshot(Scene.Type.Silhouette, output, "png");
					// extract y-axis
					scene.setCameraPosition(0, 1, 0);
					scene.setCameraUp(0, 0, -1);
					output = new File( chooser.getSelectedFile().getAbsolutePath() + "\\" +
							Session.getInstance().getModel().getName() + "." +
							"y-axis" +
							".png");
					scene.extractSnapshot(Scene.Type.Silhouette, output, "png");
					
					JOptionPane.showMessageDialog(frame,
							"The model's silhouetes were extracted to folder: " + chooser.getSelectedFile().getAbsolutePath(),
							"Silhoete extraction complete",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		panel.add(screenshotButton);
		
		panel.setMinimumSize(size);
		panel.setMaximumSize(size);
		panel.setPreferredSize(size);
		return panel;
	}
	
	class GLView implements GLEventListener {
		private final GLU glu = new GLU(); // for the GL Utility
		
		private String title = "";
		private Point3D mode = new Point3D();
		private final double angle = 90.0;
		
		// Allocate textRenderer with the chosen font
		private TextRenderer textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 14));
		
		public GLView(String title, Point3D mode) {
			this.title = title;
			this.mode = mode;
		}
		
		@Override
		public void init(GLAutoDrawable drawable) {
			GL2 gl = drawable.getGL().getGL2();

			gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
			gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
			
			gl.glShadeModel(GL2.GL_FLAT);
			gl.glMatrixMode(GL2.GL_PROJECTION);
		    gl.glLoadIdentity();
		    gl.glOrtho(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 2.0f);
		    
		    
		    textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 14));
		    
		}
		@Override
		public void display(GLAutoDrawable drawable) {
			GL2 gl = drawable.getGL().getGL2();
			gl.glMatrixMode(GL2.GL_MODELVIEW);
		    gl.glLoadIdentity();
			
			glu.gluLookAt(	1.0f, 1.0f, 1.0f,
							0.0f, 0.0f, 0.0f,
							0.0f, 1.0f, 0.0f);
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, Session.getInstance().getGlDrawMode());
			
			gl.glPushMatrix();
			gl.glLoadIdentity();
			if(this.mode.distance(new Point3D()) > 0)
				gl.glRotated(this.angle, this.mode.getX(), this.mode.getY(), this.mode.getZ());
			
			gl.glColor3f(0.0f, 0.0f, 0.0f);
			if(Session.getInstance().getModel() != null)
				Session.getInstance().getModel().draw(drawable);
			
			gl.glPopMatrix();

			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
			
			gl.glPushMatrix();
		    gl.glLoadIdentity();
			// ----- Rendering 2D text using TextRenderer class -----
			textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight()); // Prepare to draw text
			textRenderer.setColor(Color.black); // Pulsing colors based on text position, set color in RGBA
			textRenderer.draw(title, 5, height - (int)textRenderer.getBounds(title).getHeight() - 5); // 2D text using int (x, y) coordinates in OpenGL coordinates system, i.e., (0,0) at the bottom-left corner, instead of Java Graphics coordinates.
			textRenderer.endRendering(); // finish rendering
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
	
	
}

