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
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.terei.jvector.gui.ColorSelectedPanel;
import com.terei.jvector.gui.tools.LineOptions;


/**
 * Manages the operations for drawing a line.
 * 
 * @author David Terei
 * @since 26/04/2004
 * @version 1
 */
public class LineManager extends ShapeManager {
    
    /**
     * The line drawn.
     * 
     * @see Line
     */
    private Line line;
    /**
     * A point used as a reference for moving operations.
     * 
     * @see #moveShape(Shape, MouseEvent, int)
     */    
    private Point p = new Point();
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#getOptionPanel()
     */ 
    public static JPanel getOptionPanel() {
        JPanel panel = new LineOptions();
        
        return panel;
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e, int zoomLevel) {
        Point p = e.getPoint();  
        p.x /= ((double)zoomLevel/100);
        p.y /= ((double)zoomLevel/100);
        
        if (getMode() == ShapeManager.Mode.IDLE) {
            Color color = ColorSelectedPanel.getForeColor();
            int opacity = ColorSelectedPanel.getForeOpacity();
            line = new Line(p,LineOptions.getStroke(),color, opacity);
            line.resize(((double)zoomLevel/100));
            setMode(ShapeManager.Mode.DRAW);
        }        
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e, int zoomLevel) {        
        
        if (getMode() == ShapeManager.Mode.DRAW && line != null) {
            Point p = e.getPoint();  
            p.x /= ((double)zoomLevel/100);
            p.y /= ((double)zoomLevel/100);
            
            line.setP2(p);
            line.resize(((double)zoomLevel/100));
            setMode(ShapeManager.Mode.IDLE);
        }
        
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e, int zoomLevel) {
        
        if (getMode() == ShapeManager.Mode.DRAW && line != null) {
            Point p = e.getPoint();  
            p.x /= ((double)zoomLevel/100);
            p.y /= ((double)zoomLevel/100);
            
            line.setP2(p);
            line.resize(((double)zoomLevel/100));
        }
        
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#getShape()
     */
    public Shape getShape() {
        return line;
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
        
        //try to cast the shape to a line shape, catching an error
        //that may occur is the shape isnt a line shape.
        Line lTemp;
        try {
            lTemp = (Line)shape;
        } catch (ClassCastException exc) {
            //it wasnt a line shape, so print out a stacktrace for debugging
            exc.printStackTrace();
            //and return false
            return false;
        }
        
        //A drag operation works by, finding the distance between the shapes
        //refernce point, and the mouse, as this shouldnt change when moving
        //the shape. So once we have that distance, we just move the shapes
        //reference point, keeping it the same distance from the mouse.
        if (getMode() == ShapeManager.Mode.IDLE) {           
            p = new Point(pe.x - lTemp.getP1().x, pe.y- lTemp.getP1().y);
            setMode(ShapeManager.Mode.MOVE);
        } else {
            Point newP = new Point(pe.x - p.x, pe.y - p.y); 
            int w = lTemp.getP2().x - lTemp.getP1().x;
            int h = lTemp.getP2().y - lTemp.getP1().y;
            lTemp.setP1(newP);
            lTemp.setP2(new Point(newP.x + w, newP.y + h));
            lTemp.resize(((double)zoomLevel/100));
        }        
        
        return true;
    }
    
}
