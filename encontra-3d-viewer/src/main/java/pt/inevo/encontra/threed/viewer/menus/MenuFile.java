/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */

package pt.inevo.encontra.threed.viewer.menus;

import pt.inevo.encontra.threed.model.io.ModelIO;
import pt.inevo.encontra.threed.viewer.filters.ModelFilterFactory;
import pt.inevo.encontra.threed.viewer.global.Session;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

class MenuFile extends JMenu {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_EXT = "obj";
	
	public MenuFile() {
		super("File");
				
        this.add(menuOpenItem());
        this.addSeparator();
        this.add(menuCloseItem());
        this.add(menuSaveAsItem());
        this.add(menuExitItem());
	}
	
	private JMenuItem menuOpenItem() {
		JMenuItem item = new JMenuItem("Open File...");
        item.setMnemonic('O');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(Session.getInstance().getBrowseDirectory());

				chooser.setFileFilter(ModelFilterFactory.create(ModelFilterFactory.Type.MODEL3D));
				chooser.addChoosableFileFilter(ModelFilterFactory.create(ModelFilterFactory.Type.OBJ));
				chooser.addChoosableFileFilter(ModelFilterFactory.create(ModelFilterFactory.Type.OFF));
				int value = chooser.showOpenDialog(Session.getInstance().getMainFrame());
				
				if(value == JFileChooser.APPROVE_OPTION) {
					try {
						Session.getInstance().setModel(ModelIO.read(chooser.getSelectedFile()));
						Session.getInstance().setBrowseDirectory(chooser.getSelectedFile().getParentFile());
					} catch (IOException exception) {
						JOptionPane.showMessageDialog(Session.getInstance().getMainFrame(),
								exception.getMessage(),
								"Error: IOException",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
        return item;
	}
	
	private JMenuItem menuCloseItem() {
		JMenuItem item = new JMenuItem("Close");
        item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Session.getInstance().setModel(null);
			}
		});
		return item;
	}
	
	private JMenuItem menuSaveAsItem() {
		JMenuItem item = new JMenuItem("Save As...");
        item.setMnemonic('S');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        
        item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(Session.getInstance().getBrowseDirectory());
				
				chooser.setFileFilter(ModelFilterFactory.create(ModelFilterFactory.Type.ThORCODE));
				chooser.addChoosableFileFilter(ModelFilterFactory.create(ModelFilterFactory.Type.OBJ));
				chooser.addChoosableFileFilter(ModelFilterFactory.create(ModelFilterFactory.Type.OFF));
				
				int value = chooser.showSaveDialog(Session.getInstance().getMainFrame());
				
				if(value == JFileChooser.APPROVE_OPTION) {
					String extension = null;
					String s = chooser.getFileFilter().getDescription();
			        int i = s.lastIndexOf(".");
			        if (i > 0 &&  i < s.length() - 1)
			        	extension = s.substring(i+1, s.length()-1).toLowerCase();
			        
			        File selectedFile = chooser.getSelectedFile();
			        if(selectedFile.exists()) {
						//(parentComponent, message, title, optionType, messageType, icon, options, initialValue)	
			        	int n = JOptionPane.showOptionDialog(Session.getInstance().getMainFrame(),
								selectedFile.getName() + " already exists." +
								"Do you want to replace it?",
								"Confirm Save As",
							    JOptionPane.YES_NO_OPTION,
							    JOptionPane.WARNING_MESSAGE,
							    null,
							    null,
							    null);
			        	if(n != JOptionPane.YES_OPTION) return;
			        }
			        
			        // compares to see if the extension of the files are both the same
			        if(!selectedFile.getName().toLowerCase().endsWith(extension)) {
			        	String fullpath = selectedFile.getAbsolutePath();
			        	if(extension.length() > 4) { // the max lenght should be 4. E.g.: *.java
				        	// set to default
				        	selectedFile = new File(fullpath + "." + DEFAULT_EXT);
				        } else {
				        	selectedFile = new File(fullpath + "." + extension);
				        }
			        }
					try {
						ModelIO.write(Session.getInstance().getModel(), extension, selectedFile);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return item;
	}
	
	private JMenuItem menuExitItem() {
		JMenuItem item = new JMenuItem("Exit");
        item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
        return item;
	}
}

