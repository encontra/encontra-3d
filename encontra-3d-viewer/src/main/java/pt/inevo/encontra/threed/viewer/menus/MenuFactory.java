/*
 * (C) Copyright 2014 The EnContRA Project Developers.
 *
 * Contributors:
 *      Pedro B. Pascoal
 */

package pt.inevo.encontra.threed.viewer.menus;

import javax.swing.JMenu;

public class MenuFactory {
	
	public enum MenuType {
		File,
		View,
		Windows,
		Help
	}
	
	public static JMenu createMenu(MenuType type) {
		if(type == MenuType.File) {
			return new MenuFile();
		} else if(type == MenuType.View) {
			return new MenuView();
		} else if(type == MenuType.Windows) {
			return new MenuWindows();
		} else if(type == MenuType.Help) {
			return new MenuHelp();
		}
			
		return null;
	}
}