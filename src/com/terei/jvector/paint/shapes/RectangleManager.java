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

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.terei.jvector.gui.ColorSelectedPanel;
import com.terei.jvector.gui.tools.RectangleOptions;


/**
 * Manages the operations for drawing a rectangle.
 * 
 * @author David Terei
 * @since 2/05/2004
 * @version 1
 */
public class RectangleManager extends ShapeManager {
    
    /**
     * Holds the Rectangle this class works with.
     */
    protected Rectangle rect;
    
    /**
     * This holds the 'working' value for the starting point of the current rectangle.
     */
    private Point p1;
    /**
     * This holds the 'working' value for the ending point of the current rectangle.
     */
    private Point p2;
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#getOptionPanel()
     */
    public static JPanel getOptionPanel() {
        return new RectangleOptions();
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e, int zoomLevel) {
        Point p = e.getPoint();  
        p.x /= ((double)zoomLevel/100);
        p.y /= ((double)zoomLevel/100);
        
        if (getMode() == ShapeManager.Mode.IDLE) {
            //create a new rectangle.
            p1 = new Point(p.x,p.y);
            rect = new Rectangle(p,RectangleOptions.getStroke());
            rect.setFillColor(ColorSelectedPanel.getBackColor());
            rect.setFillOpacity(ColorSelectedPanel.getBackOpacity());
            rect.setOutlineColor(ColorSelectedPanel.getForeColor());
            rect.setOutlineOpacity(ColorSelectedPanel.getForeOpacity());
            rect.resize(((double)zoomLevel/100));
            setMode(ShapeManager.Mode.DRAW);
        }
        
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent e, int zoomLevel) {                
        //check if a rectangle is being drawn.
        if (getMode() == ShapeManager.Mode.DRAW && rect != null) {
            //one being drawn, update it with the current mouse
            //location.
            Point p = e.getPoint();  
            p.x /= ((double)zoomLevel/100);
            p.y /= ((double)zoomLevel/100);
               
            p2 = new Point(p.x,p.y);
            
            adjustPoints();
            rect.resize(((double)zoomLevel/100));
        }
    }
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e, int zoomLevel) {
        Point p = e.getPoint();  
        p.x /= ((double)zoomLevel/100);
        p.y /= ((double)zoomLevel/100);
        //currently drawing a rectangle, add the end point.
        if (getMode() == ShapeManager.Mode.DRAW) {
            p2 = new Point(p.x,p.y);
            adjustPoints();  
            rect.resize(((double)zoomLevel/100));
            setMode(Mode.IDLE);
        }
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#getShape()
     */
    public Shape getShape() {
        return rect;
    }
    
    /**
     * This adjust the top left and bottom left point appropriatley,
     * to allow users to set any point first, instead of just the
     * top left point first.
     */
    private void adjustPoints() {
        //create 2 new Point2D.Double's to work with, so that
        //we dont alter p1, and p2.
        Point p = new Point(p1.x,p1.y);
        Point d = new Point(p2.x,p2.y);
        
        //Get the dimensions.
        d.x -= p1.x;
        d.y -= p1.y;
        
        //check if the dimensions are negative, unfourtuently
        //java cant handle drawing negative values.
        //if negative, p2<p1, therefore, p2 is the left most value
        //not p1.
        if(d.x<0) {        
            //set the 'bottom right' point, to the top left point, which it is.
            p = new Point(p.x+d.x,p.y);
            //get the absoloute value of the distance between p1.x and p2.x
            //this is the new width odf the rectangle.
            d.x = Math.abs(d.x);            
        }
        //do the same but with the y values.
        if(d.y<0) {
            p = new Point(p.x,p.y+d.y);
            d.y = Math.abs(d.y);            
        }
        
        //Create a new p1 value, the _t stands for temp.
        //remember, we cant modify p1 as we need that to stay
        //the same to serve as a reference value.
        //the new value is modified according to our calculations though.
        Point p1_t = new Point(p.x,p.y);
        //get a new p2 value as well.
        Point p2_t = new Point(p.x+d.x,p.y+d.y);
        
        //update the rectangle with the new values.
        rect.setP1(p1_t);
        rect.setP2(p2_t);
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
        
        //try to cast the shape to a Rectangle shape, catching an error
        //that may occur is the shape isnt a Rectangle shape.
        Rectangle rTemp;
        try {
            rTemp = (Rectangle)shape;
        } catch (ClassCastException exc) {
            //it wasnt a Rectangle shape, so print out a stacktrace for debugging
            exc.printStackTrace();
            //and return false
            return false;
        }
        
        //A drag operation works by, finding the distance between the shapes
        //refernce point, and the mouse, as this shouldnt change when moving
        //the shape. So once we have that distance, we just move the shapes
        //reference point, keeping it the same distance from the mouse.
        if (getMode() == ShapeManager.Mode.IDLE) {           
            p1 = new Point(pe.x - rTemp.p1.x, pe.y- rTemp.p1.y);
            setMode(ShapeManager.Mode.MOVE);
        } else {
            Point newP = new Point(pe.x - p1.x, pe.y - p1.y); 
            rTemp.setP1(newP);
            rTemp.resize(((double)zoomLevel/100));
        }
        
        return true;
    }
    
}
