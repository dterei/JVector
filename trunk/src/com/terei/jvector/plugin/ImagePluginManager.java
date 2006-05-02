/*
 * Copyright 2004 David Terei
 * 
 * This file is part of JVector.
 * 
 * JVector is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * JVector is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JVector; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.terei.jvector.plugin;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import javax.swing.filechooser.FileFilter;

import com.terei.jvector.gui.SwingFileFilter;
import com.terei.jvector.util.MyFileFilter;

/**
 * This class manages all the Image plugins, loading and referencing them
 * for use by other components.
 * 
 * @author David Terei
 * @version 1
 * @since 2/06/2004
 */
public class ImagePluginManager {

	/**
	 * The path to the plugins.
	 */
	private static String plug_path = "Plug-Ins" + File.separator
			+ "File Formats";

	/**
	 * The plugins avaibile.
	 */
	private ImagePlugin[] plugins;

	/**
	 * Construct a new <code>ImagePluginManager</code>.
	 */
	public ImagePluginManager() {
		plugins = loadPlugins();
		System.out.println("Plugins Loaded");
	}

	/**
	 * Load the availble plugins from the Plug-Ins/File Formats directory.
	 * 
	 * @return The loaded plugins.
	 */
	public ImagePlugin[] loadPlugins() {
		ImagePlugin[] plugins = null;

		try {
			File file = new File(plug_path);
			File[] files = file.listFiles(new MyFileFilter("jar", false));
			plugins = new ImagePlugin[files.length];

			//specify the location of the class, for the class loader.
			URL url = file.toURL();

			for (int i = 0; i < files.length; i++) {
				URL url2 = new URL(url.toExternalForm() + files[i].getName());
				plugins[i] = loadPlugin(url2);
				url2 = null;
			}

		} catch (Exception e) {
			System.err.println("Exception: " + e);
		}

		return plugins;
	}

	/**
	 * Load an individual plugin, given the url path to it.
	 * 
	 * @param url The URL pointing to the plugin to load.
	 * @return The loaded plugin.
	 */
	private ImagePlugin loadPlugin(URL url) {
		ImagePlugin imPlug = null;

		//create a new URLClass loader, that points to the jar archive 
		//containing the plugin.
		URL[] url2 = new URL[] { url };
		URLClassLoader loader = new URLClassLoader(url2);

		try {
			//load the plugin properties file that specify's where the main 
			//plugin class is.
			InputStream in = loader.getResourceAsStream("plugin.properties");
			Properties prop = new Properties();
			prop.load(in);

			//get the string that specify's the pugins main class.
			String pluginPath = prop.getProperty("Plugin-Main-Class");

			//get a reference to the plugin class.     
			Class pluginLoader = loader.loadClass(pluginPath);

			//specify the class paramaters, the types, then the values.
			Class[] params = {};
			Object[] params2 = {};

			//get the plugins constrcutor.
			java.lang.reflect.Constructor cons = pluginLoader
					.getDeclaredConstructor(params);

			//get an instance of the class (it should implement ImagePlugin)
			Object object = cons.newInstance(params2);

			//check it is a image plugin, and then cast it to one if so.
			if (object instanceof ImagePlugin)
				imPlug = (ImagePlugin) object;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			url = null;
			url2 = null;
			loader = null;
		}

		return imPlug;
	}

	/**
	 * Get the file filters for a Swing dialog box for the various plugin file
	 * types.
	 *  
	 * @return The Swing FileFilters for the file types of the loaded plugins.
	 * @throws ArrayIndexOutOfBoundsException
	 * @throws NullPointerException
	 */
	public FileFilter[] getImageTypes() throws ArrayIndexOutOfBoundsException,
			NullPointerException {

		if (plugins.length == 0)
			throw new NullPointerException("No Plugins Loaded");

		FileFilter[] filters = new FileFilter[plugins.length];
		int i = 0;
		try {
			for ( ; i < plugins.length; i++)
				filters[i] = plugins[i].getFileFilter();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(i);
		}

		return filters;
	}

	/**
	 * Get the plugin that has the specified image extension.
	 * 
	 * @param extension The image extension of the plugin.
	 * @return The plugin.
	 */
	public ImagePlugin getPlugin(String extension) {
		for (ImagePlugin p : plugins) {
			FileFilter ff = p.getFileFilter();
			if (ff instanceof SwingFileFilter) {
				SwingFileFilter sff = (SwingFileFilter) ff;
				if (sff.getAcceptExtension() == extension)
					return p;
			}

		}
		return null;
	}

}
