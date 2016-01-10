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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.terei.jvector.JVector;

/**
 * Displays two colours, a foreground and background colour, allowing only the foreground
 * colour to be set. However, a button that swaps the two colours, then allows both to
 * be set. This component is meant to be used with a colour selector, to display the
 * currently selected colour.
 * 
 * @author David Terei
 * @since 12/05/2004
 * @version 1
 */
@SuppressWarnings("serial")
public class ColorSelectedPanel extends JPanel implements ActionListener,
		MouseListener {

	/**
	 * The component that displays the foreground colour.
	 */
	private static JColor jcFore;

	/**
	 * The component that displays the background colour.
	 */
	private static JColor jcBack;

	/**
	 * The button that allows the foreground and background colours to be swapped.
	 */
	private static JButton switcher;

	/**
	 * The JSpiner that allows the user to pickthe foreground opactity.
	 */
	private static JSpinner jsFore;

	/**
	 * The JSpiner that allows the user to pick the background opactity.
	 */
	private static JSpinner jsBack;

	/**
	 * A Static initialization block that sets up a few static components.
	 */
	static {
		int min = 0;
		int value = 100;
		int max = 100;
		int step = 1;
		try {
			//setup the forground colour.
			jcFore = new JColor(Color.black, true);
			jcFore.setBounds(45, 0, 30, 30);
			JVector.cSbm.addHintFor(jcFore, JVector.getRBLocale().getString(
					"ColorPanel.jcFore"));
			//setup the background colour.
			jcBack = new JColor(Color.white, true);
			jcBack.setBounds(62, 17, 30, 30);
			JVector.cSbm.addHintFor(jcBack, JVector.getRBLocale().getString(
					"ColorPanel.jcBack"));
			//setup the switcher.
			switcher = new JButton(JVector
					.loadImageIcon("Resources/switcher.png"));
			switcher.setContentAreaFilled(false);
			switcher.setBounds(77, 0, 16, 16);

			//setup the two spinners. Sepperate models must be used, as if the same one
			//is used, then the two spinners are linked together and miror each other.
			SpinnerNumberModel model1 = new SpinnerNumberModel(value, min, max,
					step);
			SpinnerNumberModel model2 = new SpinnerNumberModel(value, min, max,
					step);
			jsFore = new JSpinner(model1);
			jsFore.setBounds(0, 5, 40, 20);
			JVector.cSbm.addHintFor(jsFore, JVector.getRBLocale().getString(
					"ColorPanel.jsFore"));
			jsBack = new JSpinner(model2);
			JVector.cSbm.addHintFor(jsBack, JVector.getRBLocale().getString(
					"ColorPanel.jsBack"));
			jsBack.setBounds(97, 22, 40, 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create a new ColourSelectedPanel.
	 */
	public ColorSelectedPanel() {
		//setup the panel.
		this.setLayout(null);
		this.setBounds(0, 0, 137, 47);
		this.setMaximumSize(new Dimension(137, 47));
		this.setPreferredSize(new Dimension(137, 47));
		this.setMinimumSize(new Dimension(137, 47));
		//setup event handling
		switcher.addActionListener(this);
		jcFore.addMouseListener(this);
		jcBack.addMouseListener(this);
		//add in the components
		this.add(switcher);
		this.add(jcFore);
		this.add(jcBack);
		this.add(jsFore);
		this.add(jsBack);
	}

	/**
	 * Set the colour of the foreground.
	 * 
	 * @param color The colour to set it to.
	 */
	protected static void setColor(Color color) {
		jcFore.setColor(color);
	}

	/**
	 * Get the foreground colour.
	 * 
	 * @return The foreground colour.
	 */
	public static Color getForeColor() {
		return jcFore.getColor();
	}

	/**
	 * Get the foreground opacity.
	 * 
	 * @return The foreground opacity.
	 */
	public static int getForeOpacity() {
		int value = 100;
		try {
			jsFore.commitEdit();
			value = (Integer) jsFore.getValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return value;
	}

	/**
	 * Get the background colour.
	 * 
	 * @return The background colour.
	 */
	public static Color getBackColor() {
		return jcBack.getColor();
	}

	/**
	 * Get the background opacity.
	 * 
	 * @return The background opacity.
	 */
	public static int getBackOpacity() {
		int value = 100;
		try {
			jsBack.commitEdit();
			value = (Integer) jsBack.getValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return value;
	}

	/**
	 * swap the background and foreground colour.
	 * 
	 * @return True if succesfull.
	 */
	public static boolean swapForeBack() {
		Color cFore = jcFore.getColor();
		Color cBack = jcBack.getColor();

		jcFore.setColor(cBack);
		jcBack.setColor(cFore);

		return true;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src.equals(switcher))
			ColorSelectedPanel.swapForeBack();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		Object src = e.getSource();
		JColor jc = (JColor) src;
		Color col = JColorChooser.showDialog(this, "", jc.getColor());
		if (col != null)
			jc.setColor(col);
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

}
