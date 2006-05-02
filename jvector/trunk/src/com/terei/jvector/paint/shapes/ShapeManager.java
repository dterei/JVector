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
package com.terei.jvector.paint.shapes;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;


/**
 * Provides an interface to use for managers
 * of the various shapes. These classes handle all
 * the various operations associated with drawing
 * their shapes.
 * 
 * @author David Terei
 * @since 26/04/2004
 * @version 1
 */
public abstract class ShapeManager {
	
	/**
	 * The Current Mode the shape manager is in.
	 * 
	 * @author David Terei
	 * @version 1
	 * @since 30/10/2005
	 */
	public enum Mode {
		IDLE,
		DRAW,
		MOVE;
	}

	/**
	 * Stores the current mode for the class.
	 */
    private Mode MODE = Mode.IDLE; 
    
    /**
	 * Returns if the shape is complete, and a new shape
	 * ready to be drawn.
	 *
	 * @return If the shape is done.
	 */
   	public boolean getDone() {
   	    return (MODE == Mode.IDLE) ? true : false;
    }
    
    /**
	 * @return The shape being built, or done.
	 */
    public abstract Shape getShape();
    
    
    /**
     * Returns the JPanel that holds the various options associated with a tool/shape.
     * 
     * @return A JPanel that contains the various options associated with a shape.
     */    
    public static JPanel getOptionPanel() {
        return new JPanel();
    }
    
    /**
     * Manages the handling of Mouse Pressed events, subclasses will overide
     * this method to provide drawing.
     *
     * @param e The mouse event to handle.
     * @param zoomLevel The current zoom level of the image.
     *
     * @see com.terei.jvector.paint.PaintCanvas#getZoomLevel()
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e, int zoomLevel) {        
    }

    /**
     * Manages the handling of Mouse Released events, subclasses will overide
     * this method to provide drawing.
     *
     * @param e The mouse event to handle.
     * @param zoomLevel The current zoom level of the image.
     *
     * @see com.terei.jvector.paint.PaintCanvas#getZoomLevel()
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e, int zoomLevel) {        
    }

    /**
     * Manages the handling of Mouse Dragged events, subclasses will overide
     * this method to provide drawing.
     *
     * @param e The mouse event to handle.
     * @param zoomLevel The current zoom level of the image.
     *
     * @see com.terei.jvector.paint.PaintCanvas#getZoomLevel()
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e, int zoomLevel) {        
    }

    /**
     * Manages the handling of Mouse Moved events, subclasses will overide
     * this method to provide drawing.
     *
     * @param e The mouse event to handle
     * @param zoomLevel The current zoom level of the image.
     *
     * @see com.terei.jvector.paint.PaintCanvas#getZoomLevel()
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e, int zoomLevel) {        
    }
    
    /**
     * Move the shape given to where the mouse is.
     * This method is implemented so that it works even when the shape
     * is zoomed in on.
     * 
     * @param shape The shape to move.
     * @param e The mouse event to process and move the shape to.
     * @param zoomLevel The zoom level of the image. 100% = 100
     * 
     * @return If the operation succeeded.
     */
    public abstract boolean moveShape(Shape shape, MouseEvent e, int zoomLevel);

	/**
	 * Get the ShapeManagers current mode.
	 * 
	 * @return The current mode of the class.
	 */
	public Mode getMode() {
		return MODE;
	}

	/**
	 * Set the shape managers current Mode to mode.
	 * 
	 * @param mode The mode to set to.
	 */
	public void setMode(Mode mode) {
		MODE = mode;
	}
    
}
