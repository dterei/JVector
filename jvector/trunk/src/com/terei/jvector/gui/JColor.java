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
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.border.LineBorder;


/**
 * A component that just displays a color in a square.
 * 
 * @author David Terei
 * @since 12/05/2004
 * @version 0.8
 */
@SuppressWarnings("serial")
public class JColor extends JComponent {
    
    /**
     * The color to display.
     */
    private Color color = Color.WHITE;
    
    /**
     * If this component is enabled.
     */
    private boolean enabled = false;
    
    /**
     * Create a new JColor.
     * 
     * @param r The value of red of the color to display.
     * @param g The value of green of the color to display.
     * @param b The value of blue of the color to display.
     * @param enabled If this JColor component is enabled.
     */
    public JColor(int r, int g, int b, boolean enabled) {
        this(new Color(r,g,b), enabled);
    }
    
    /**
     * Create a new JColor.
     * 
     * @param color The color to display.
     * @param enabled If this JColor component is enabled.
     */
    public JColor(Color color, boolean enabled) {
        this.color = color;        
        this.setBorder(new LineBorder(Color.black, 1, false));
        this.enabled = enabled;
    }
    
    /* (non-Javadoc)
     * Paints the color sqaure.
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics g) {
        Dimension size = this.getSize();
        if (enabled) {
            g.setColor(color);
            g.fillRect(0, 0, size.width, size.height);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, size.width, size.height);
            g.setColor(Color.red);
            g.drawLine(0, 0, size.width, size.height);
        }
    }
    
    /**
     * Get the color of this JColor component.
     * 
     * @return The color.
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Set the color of this component.
     * 
     * @param color The color to set it to.
     */
    public void setColor(Color color) {
        Color oldValue = this.color;
        this.color = color;        
        //notify any event listeners something has changed.
        this.firePropertyChange("color", oldValue, color);
        
        repaint();
    }
    
}
