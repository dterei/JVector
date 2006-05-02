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
package com.terei.jvector.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Creates a colour swatch from a properties file specifing the colours.
 * 
 * @author David Terei
 * @since 7/05/2004
 * @version 0.7
 */
@SuppressWarnings("serial")
public class ColorSwatch extends JPanel implements MouseListener {

	private static final int SWATCH_SIZE = 289;

	/**
	 * The number of colums to have in the colour swatch.
	 */
	private static int COLUMS = 15;

	/**
	 * The number of rows to have in the colour swatch.
	 */
	private static int ROWS = 17;

	/**
	 * Holds the resource bundle (.properties file) that contains
	 * the data that this swatch displays.
	 */
	private Properties rb;

	/**
	 * A list containg all the colour components.
	 */
	//private List<JColor> colours;

	/**
	 * Create a new ColourSwatch.
	 * 
	 * @param bundle The path to the properties file that contains the swatch colour
	 * 				 information. 
	 */
	public ColorSwatch(String bundle) {
		//get the properties file specifying the colours.
		rb = loadSwatch(bundle);
		//build the colours
		List<JColor> colours = loadColours(rb);
		//setup the panel to display the colorrs.
		setupPanel(colours);
	}

	/**
	 * Loads the properties file specifing the colours to display.
	 * 
	 * @param path The path to the properties file relative to the package base.
	 * @return The properties file.
	 */
	private Properties loadSwatch(String path) {
		Properties bundle = new Properties();
		try {
			//create a new InputStream pointing to the properties file.
			//this method finds the file using the path given, BUT
			//relative to the package base.
			InputStream in = this.getClass().getClassLoader()
					.getResourceAsStream(path);
			if (in != null) {
				//load the properties file.
				bundle.load(in); // Can throw IOException
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bundle;
	}

	/**
	 * Loads/builds the colours specified in the properties file
	 * by the reference 'Color.i=r,g,b', where i is a number which
	 * incremeants for each colour. And r,g,b are all ints with a range
	 * of 0-255 specfing the red, green, and blue colours respectivley.
	 * 
	 * @see com.terei.jvector.gui.JColor
	 * @param p The properties file that specifies the colours.
	 * @return A list containing all the colours, as JColor components
	 */
	private List<JColor> loadColours(Properties p) {
		int size = Integer.parseInt(p.getProperty("Colors"));
		List<JColor> cols = new ArrayList<JColor>(size);

		for (int i = 0; i < size; i++)
			cols.add(createColor(p.getProperty("Color." + (i + 1))));

		return cols;
	}

	/**
	 * Builds a JColour component from a string specifing 
	 * the red, green and blue colours in the format 'r,g,b'.
	 * 
	 * @param colors A string containg the red, green, and blue values of a colour.
	 * @return A JColor component with the colour specified in the string.
	 */
	private JColor createColor(String colors) {
		int[] rgb = getColors(colors);
		Color color = new Color(rgb[0], rgb[1], rgb[2]);
		JColor jcolor = new JColor(color, true);
		jcolor.addMouseListener(this);

		return jcolor;
	}

	/**
	 * Splits a string specifying the red, green and blue values of a colour in the
	 * format, r,g,b , into an int array.
	 * 
	 * @param string A string containg the red, green, and blue values of a colour.
	 * @return An array holding the three values, r,g,b.
	 */
	private int[] getColors(String string) {
		int[] rgb = new int[3];
		String[] s = string.split(",");
		rgb[0] = Integer.parseInt(s[0]);
		rgb[1] = Integer.parseInt(s[1]);
		rgb[2] = Integer.parseInt(s[2]);

		return rgb;
	}

	/**
	 * Sets up the swatch.
	 * 
	 * @param buts A list<JColor> containg the jcolour components to be added.
	 */
	private void setupPanel(List<JColor> buts) {
		this.setName(rb.getProperty("Name"));
		GridLayout layout = new GridLayout(ROWS, COLUMS);
		this.setLayout(layout);
		//how many colours have been added.
		int count = 0;
		int x = ColorPanel.WIDTH / COLUMS;

		for (JColor but : buts) {
			count++;
			but.setPreferredSize(new Dimension(x, x - 3));
			but.setMaximumSize(new Dimension(x, x - 3));
			this.add(but);
		}

		//If they dont specfy all the colours, then fill in the rest with blanks.        
		//TODO Make swatch size more dynamic
		for (int j = count; j < SWATCH_SIZE; j++) {
			JColor but = new JColor(Color.WHITE, false);
			but.setPreferredSize(new Dimension(x, x - 3));
			this.add(but);
		}

	}

	/*private void saveProperties(int[] rgb) {
	 
	 Properties p = new Properties();
	 
	 for (int i=0; i<rgb.length/3; i++) {
	 p.setProperty("Color."+(i+1), String.valueOf(rgb[i*3])+","
	 +String.valueOf(rgb[i*3+1])+","
	 +String.valueOf(rgb[i*3+2]));        
	 }       
	 
	 try {
	 FileOutputStream out = new FileOutputStream("javaStandard.properties");
	 p.store(out, "Test");
	 } catch (Exception e) {
	 
	 }
	 }*/

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		JColor jcolor = (JColor) e.getComponent();
		Color color = jcolor.getColor();
		ColorSelectedPanel.setColor(color);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {}

	/**
	 * Shows a dialog containg the information on this swatch.
	 */
	protected void showInfoDialog() {
		String msg = "Name: " + rb.getProperty("Name") + "\n" + "Author: "
				+ rb.getProperty("Author") + "\n" + "Description: "
				+ rb.getProperty("Description");
		String title = rb.getProperty("Name") + "Information";
		JOptionPane.showMessageDialog(this, msg, title,
				JOptionPane.INFORMATION_MESSAGE);
	}

}
