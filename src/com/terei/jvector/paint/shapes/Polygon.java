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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import com.terei.jvector.util.DynamicDoubleArray;


/**
 * This class stores all the information about a polygon, and manages
 * its drawing.
 * 
 * @author David Terei
 * @since 16/05/2004
 * @version 0.8
 */
@SuppressWarnings("serial")
public class Polygon extends Shape {
    
    /**
     * The x points of the polygon.
     *
     * @see com.terei.jvector.util.DynamicDoubleArray
     */
    private DynamicDoubleArray x = new DynamicDoubleArray();
    /**
     * A working value for {@link #x x}, used for zooming in and out so that
     * x isn't modified.
     * 
     * @see #x
     * @see com.terei.jvector.util.DynamicDoubleArray
     */
    private DynamicDoubleArray dx = new DynamicDoubleArray();
    /**
     * The y points of the polygon.
     *
     * @see com.terei.jvector.util.DynamicDoubleArray
     */
    private DynamicDoubleArray y = new DynamicDoubleArray();
   /**
     * A working value for {@link #y y}, used for zooming in and out so that
     * y isn't modified.
     * 
     * @see #y
     * @see com.terei.jvector.util.DynamicDoubleArray
     */
    private DynamicDoubleArray dy = new DynamicDoubleArray();
    
    /**
     * The polygon.
     *
     * @see java.awt.Polygon
     */
    private java.awt.Polygon polygon;
    
    /**
     * The fill colour of the polygon.
     */
    private Color color_in = Color.white;
    /**
     * The opacity of the fill of the polygon.
     */
    private Color color_out = Color.black;
    /**
     * The opacity of the outline of the polygon.
     */
    private int opacity_in = 100;
    /**
     * The outline's opacity.
     */
    private int opacity_out = 100;
    /**
     * The stroke of the outline.
     */
    private int stroke = 1;
    
    /**
     * Create a new Polygon.
     * 
     * @param p The starting point of the new polygon.
     * @param stroke The outlien stroke of the new polygon.
     */       
    public Polygon(Point2D.Double p, int stroke) {
        x.add(p.x,0);
        dx.add(p.x, 0);
        y.add(p.y,0);
        dy.add(p.y, 0);
        this.stroke = stroke;
        
        createPolygon();
    }    
    
    /**
     * Add a point to the polygon.
     *
     * @param p The point to add.
     */
    public void addPoint(Point2D.Double p) {
        x.add(p.x);
        dx.add(p.x);
        y.add(p.y);
        dy.add(p.y);
        createPolygon();
    }
    
    /**
     * sets the posistion in the polygon specified to the point specified.

     * @param p The point to set it to.
     * @param position The posistion to set the point to.
     */
    public void setPoint(Point2D.Double p, int position) {
        x.add(p.x, position);
        dx.add(p.x, position);
        y.add(p.y, position);
        dy.add(p.y, position);
        createPolygon();
    }
        
    /**
     * Deletes the point from the polygon.
     * 
     * @param position The position in the array of points, of the one to delete.
     */
    public void deletePoint(int position) {
        x.delete(position);
        dx.delete(position);
        y.delete(position);
        dy.delete(position);
        createPolygon();
    }      
    
   /**
     * Get the number of points in the polygon.
     *
     * @return The number of points in the polygon.
     */
    public int getLength() {
        return x.length();
    } 
        
    /**
     * Creates the polygon from the current values.
     */
    private void createPolygon() {
        polygon = new java.awt.Polygon(dx.getAllInts(), dy.getAllInts(), 
                                                            dx.length());
    }    
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#contain(java.awt.Point)
     */
    public boolean contain(Point p) {
        return polygon.contains(p.x, p.y);
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getFillColor()
     */
    public Color getFillColor() {
        return color_in;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getOutlineColor()
     */
    public Color getOutlineColor() {
        return color_out;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getFillOpacity()
     */
    public int getFillOpacity() {
        return opacity_in;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getOutlineOpacity()
     */
    public int getOutlineOpacity() {
        return opacity_out;
    }     
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getShapeName()
     */
    public String getShapeName() {
        return "Polygon";
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getShapeCode()
     */
    public Shapes getShapeCode() {
       return Shapes.POLYGON;
    }
    
    public boolean translate(int x, int y) {
        
        for (int i=0; i<this.x.length(); i++) {
            this.x.add(this.x.get(i)+x, i);
            this.y.add(this.y.get(i)+y, i);
        }
        
        return true;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#paint(java.awt.Graphics)
     */
    public void paint(Graphics2D g) {
        Composite comp_orig = g.getComposite();
                
        //set the opacity.
        float i = (float)opacity_in/100;
        Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, i);
        g.setComposite(alpha);
        //set the color.
        g.setColor(color_in);
        //draw the fill.        
        g.fill(polygon);
        
        
        //draw the outline.
        if (stroke>0) {
            g.setStroke(new BasicStroke(stroke));        
            //set the opacity.
            i = (float)opacity_out/100;
            Composite alpha2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, i);
            g.setComposite(alpha2);
            //set the color.
            g.setColor(color_out);
            //draw the outline.            
            g.draw(polygon);
        }
        //restore the original composite.
        g.setComposite(comp_orig);
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color color) {
        color_in = color;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setOutlineColor(java.awt.Color)
     */
    public void setOutlineColor(Color color) {
        color_out = color;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setFillOpacity(int)
     */
    public void setFillOpacity(int opacity) {
        opacity_in = opacity;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setOutlineOpacity(int)
     */
    public void setOutlineOpacity(int opacity) {
        opacity_out = opacity;
    }

    /**
     * Get the polygon that this class is providing a wrapper for.
     * 
     * @return Returns the polygon.
     */
    public java.awt.Polygon getPolygon() {
        return polygon;
    }
    
    /**
     * Set the polygon this class should use.
     * 
     * @param polygon The polygon to set.
     */
    public void setPolygon(java.awt.Polygon polygon) {
        this.polygon = polygon;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#resize(double)
     */
    public void resize(double percent) {
        double[] dxa = new double[x.length()];        
        double[] dya = new double[y.length()];
        
        System.arraycopy(x.getAll(), 0, dxa, 0, x.length());
        System.arraycopy(y.getAll(), 0, dya, 0, y.length());
        
        for (int i=0; i<dxa.length; i++) {
            dxa[i] *= percent;
            dya[i] *= percent;           
        }
        
        dx.equals(dxa);
        dy.equals(dya);
        
        createPolygon();
    }
}
