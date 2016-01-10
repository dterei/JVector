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
package com.terei.jvector.gui.tools;

import java.awt.FlowLayout;
import java.text.ParseException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.terei.jvector.JVector;


/**
 * Creates a JPanel that gives the user access to several options to use when
 * drawing a line.
 * 
 * @author David Terei
 * @since 26/04/2004
 * @version 1
 */
@SuppressWarnings("serial")
public class LineOptions extends JPanel {

    /**
     * The stroke of the line.
     */
    public static int STROKE = 1;
    
    /**
     * The JSpiner that allows the user to pick the stroke.
     */
    private static JSpinner spinner;
    
    /**
     * Setup the JSpinner, spinner.
     */
    static {
        int min = 1;
        int value = 1;
        int max = 100;
        int step = 1;
        try {
            SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, step);
            spinner = new JSpinner(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Create a new LineOptions, the constructor does most the
     * work as most of it is GUI work.
     */
    public LineOptions() {
        this.setLayout(new FlowLayout());
        this.add(new JLabel(JVector.getRBLocale().getString("ExtraBar.Line.Weight")));
        this.add(spinner);
    }
    
    /**
     * Get the stroke set. (the weight/thickness of the line)
     * 
     * @return The stroke (weight).
     */
    public static int getStroke() {
        if (spinner!=null) {
            try {
                spinner.commitEdit();
                STROKE = (Integer)spinner.getValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }        
        return STROKE;
    }

}
