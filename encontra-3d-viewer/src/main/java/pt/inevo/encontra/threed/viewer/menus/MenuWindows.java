/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */

package pt.inevo.encontra.threed.viewer.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import pt.inevo.encontra.threed.viewer.MultiviewFrame;

/**
 * 
 * @author Pedro B. Pascoal
 */
class MenuWindows extends JMenu {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MenuWindows() {
		super("Windows");

        this.add(menuMultiviewItem());
        this.addSeparator();
	}
	private JMenuItem menuMultiviewItem() {
		JMenuItem item = new JMenuItem("Multi-camera View");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new MultiviewFrame("Multi-camera View");
			}
        });
		return item;
	}
}