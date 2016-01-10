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
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import com.terei.jvector.gui.ColorSelectedPanel;
import com.terei.jvector.gui.tools.RectangleOptions;


/**
 * Manages the operations for drawing a polygon.
 * 
 * @author David Terei
 * @since 16/05/2004
 * @version 1
 */
public class PolygonManager extends ShapeManager {
    
    /**
     * Holds the Polygon this class works with.
     */
    protected Polygon poly;
    
    /**
     * Holds a reference point for a polygon, used to move it.
     */
    private Point p = new Point();
    
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
        Point2D.Double p = new Point2D.Double(e.getPoint().x, e.getPoint().y);
        p.x /= ((double)zoomLevel/100);
        p.y /= ((double)zoomLevel/100);
        
        //test to see if a polygon is currently being drawn or not.
        if (getMode() == ShapeManager.Mode.IDLE) {
            
            setMode(ShapeManager.Mode.DRAW);            
            
            //no, one isnt being draw, so create a new one.
            poly = createPoly(p);
            //add a second point, for the one displayed when moving the mouse.
            poly.addPoint(p);
            poly.resize(((double)zoomLevel/100));
            
        } else if (getMode() == ShapeManager.Mode.DRAW) {
            
            //polygon currently being draw, so lets add a point or finish it.
            //test for a double click.
            if (e.getClickCount() == 2) {
                //delete the last point, as it is an extra point serving
                //as a visual guide when moving the mouse.
                poly.deletePoint(poly.getLength()-1);
                //make sure there are at least 3 points (triangle)
                //if there are not, delete the shape.
                if (poly.getLength()<3) {
                    poly = null;
                }

                setMode(ShapeManager.Mode.IDLE);               
                return;
            }
                   
            //add a new point to the polygon.
            poly.addPoint(p);
            poly.resize(((double)zoomLevel/100));
            
        }
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent e, int zoomLevel) {
        if (getMode() == ShapeManager.Mode.DRAW) {
            Point2D.Double p = new Point2D.Double(e.getPoint().x, e.getPoint().y);
            p.x /= ((double)zoomLevel/100);
            p.y /= ((double)zoomLevel/100);
            
            poly.setPoint(p, poly.getLength()-1);
            poly.resize(((double)zoomLevel/100));
        }
    }
    
    /**
     * Creates a new Polygon and sets its colours and stroke.
     * 
     * @param p The first point on the ploygon
     * @return The Polygon
     */
    private Polygon createPoly(Point2D.Double p) {
        Polygon pol = new Polygon(p, RectangleOptions.getStroke());            
        pol.setFillColor(ColorSelectedPanel.getBackColor());
        pol.setFillOpacity(ColorSelectedPanel.getBackOpacity());
        pol.setOutlineColor(ColorSelectedPanel.getForeColor());
        pol.setOutlineOpacity(ColorSelectedPanel.getForeOpacity());
        
        return pol;
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.ShapeManager#getShape()
     */
    public Shape getShape() {
        return poly;
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
        Polygon pTemp;
        try {
            pTemp = (Polygon)shape;
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
            p = new Point(pe.x, pe.y);
            setMode(ShapeManager.Mode.MOVE);
        } else { 
            int x = pe.x - p.x;
            int y = pe.y - p.y;   
            pTemp.translate(x,y);            
            p = new Point(pe.x, pe.y);
            pTemp.resize(((double)zoomLevel/100));
        }
                
        return true;
    }

}
