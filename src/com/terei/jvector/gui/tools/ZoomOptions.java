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
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;

import com.terei.jvector.JVector;


/**
 * Creates a JPanel that gives the user access to several options to use when
 * using the zoom tool.
 * 
 * @author David Terei
 * @since 23/05/2004
 * @version 0.1
 */
@SuppressWarnings("serial")
public class ZoomOptions extends JPanel {
    
    /**
     * The value for when the zoom in option is selected.
     * 
     * @see #getZoomMode()
     */
    public static int ZOOM_IN = 0;
    /**
     * The value for when the zoom out option is selected.
     * 
     * @see #getZoomMode()
     */
    public static int ZOOM_OUT = 1;
    
    /**
     * The zoom label.
     */
    private static JLabel jlZoom;
    /**
     * The zoom in radio button.
     */    
    private static JRadioButton jrIn;
    /**
     * The zoom out radio button.
     */
    private static JRadioButton jrOut;
    /**
     * The button group that holds the radio buttons.
     * 
     * @see #jrIn
     * @see #jrOut
     */
    private static ButtonGroup bg;
    /**
     * The label for the zoom level spinner.
     */
    private static JLabel jlSpin;
    /**
     * The JSpiner that allows the user to pick the zoom level.
     */
    private static JSpinner spinner;
    
    
    /**
     * Static initilization block.
     * Setups the swing components. 
     */
    static {
        ResourceBundle rb = JVector.getRBLocale();
        
        jlZoom = new JLabel(rb.getString("ExtraBar.Zoom.Zoom"));        
        jrIn = new JRadioButton(rb.getString("ExtraBar.Zoom.In"));
        jrOut = new JRadioButton(rb.getString("ExtraBar.Zoom.Out"));
        jlSpin = new JLabel(rb.getString("ExtraBar.Zoom.Level"));
        
        bg = new ButtonGroup();
        bg.add(jrIn);
        bg.add(jrOut);
        bg.setSelected(jrIn.getModel(),true);
        
        int min = 1;
        int value = 1;
        int max = 5;
        int step = 1;
        try {
            SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, step);
            spinner = new JSpinner(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new ZoomOptions, the constructor does most the
     * work as most of it is GUI work.
     */
    public ZoomOptions() {
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
                       
        FlowLayout layout2 = new FlowLayout(FlowLayout.LEFT, 10, 10);
        JPanel jp = new JPanel(layout2);
        jp.add(jlZoom);
        jp.add(jrIn);
        jp.add(jrOut);
        
        FlowLayout layout3 = new FlowLayout(FlowLayout.LEFT, 10, 10);
        JPanel jp2 = new JPanel(layout3);
        jp2.add(jlSpin);
        jp2.add(spinner);        
        
        this.add(jp);
        this.add(jp2);
        
        //layout jp.
        layout.putConstraint(SpringLayout.NORTH, jp, 5,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, jp, 10,
                SpringLayout.WEST, this);
        //layout spinner.
        layout.putConstraint(SpringLayout.WEST, jp2, 10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, jp2, 5,
                SpringLayout.SOUTH, jp);
    }
    
    /**
     * Get the currently selected zoom mode. That is,
     * to zoom in or out.
     * 
     * @return The zoom mode
     * 
     * @see #ZOOM_IN
     * @see #ZOOM_OUT
     */
    public static int getZoomMode() {
        if (bg.getSelection() == jrIn.getModel())
            return ZOOM_IN;
        else
            return ZOOM_OUT;
    }
    
    public static int getZoomLevel() {
        int i = 1;        
        try {
            spinner.commitEdit();
            i = (Integer)spinner.getValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }                    
        return i;     
    }

}
