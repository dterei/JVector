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

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.terei.jvector.gui.ColorSelectedPanel;
import com.terei.jvector.gui.tools.TextOptions;


/**
 * 
 * 
 * @author David Terei
 * @since 17/05/2004
 * @version 0.1
 */
public class TextManager extends ShapeManager {
    
    /**
     * A point used as a refernce for mocing operations.
     * 
     * @see #moveShape(Shape, MouseEvent, int)
     */
    private Point p = new Point();

    /**
     * The text to draw.
     * 
     * @see Text
     */
    private Text text;    
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#getOptionPanel()
     */
    public static JPanel getOptionPanel() {
        return new TextOptions();
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#getShape()
     */
    public Shape getShape() {
        return text;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e, int zoomLevel) {
        if (getMode() != ShapeManager.Mode.IDLE)
            return;
        
        Point p = new Point(e.getPoint().x, e.getPoint().y+6);
        p.x /= ((double)zoomLevel/100);
        p.y /= ((double)zoomLevel/100);
        
        Color color = ColorSelectedPanel.getForeColor();
        int opacity = ColorSelectedPanel.getForeOpacity();
        Font font = TextOptions.getFONT();
        String string = TextOptions.getTEXT();
        
        if(string == null || string.length() == 0)
        	text = null;
        else {
        	text = new Text(p, string, color, opacity, font);
        	text.resize(((double)zoomLevel/100));
        }
        
        setMode(ShapeManager.Mode.IDLE);
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#moveShape(com.terei.jvector.paint.shapes.Shape, java.awt.event.MouseEvent)
     */
    public boolean moveShape(Shape shape, MouseEvent e, int zoomLevel) {
        //get the point of where this mouse event occured.
        Point pe = e.getPoint();
        //convert it to the correct 100% zoom values.
        pe.x /= ((double)zoomLevel/100);
        pe.y /= ((double)zoomLevel/100);
        
        //try to cast the shape to a Text shape, catching an error
        //that may occur is the shape isnt a Text shape.
        Text tTemp;
        try {
            tTemp = (Text)shape;
        } catch (ClassCastException exc) {
            //it wasnt a text shape, so print out a stacktrace for debugging
            exc.printStackTrace();
            //and return false
            return false;
        }
                
        //A drag operation works by, finding the distance between the shapes
        //refernce point, and the mouse, as this shouldnt change when moving
        //the shape. So once we have that distance, we just move the shapes
        //reference point, keeping it the same distance from the mouse.
        if (getMode() == ShapeManager.Mode.IDLE) {
            //the move operation just started, so set up the distance variable
            p = new Point(pe.x - tTemp.getPoint().x, pe.y- tTemp.getPoint().y);
            //set the mode to now be dragging, as we have the info we need to go
            setMode(ShapeManager.Mode.MOVE);
        } else {
            //create the new point for the shape
            Point newP = new Point(pe.x - p.x, pe.y - p.y);
            //set it
            tTemp.setPoint(newP);
            tTemp.resize(((double)zoomLevel/100));
        }
        
        //operation successfull, so return true.
        return true;
    }    
    
}
