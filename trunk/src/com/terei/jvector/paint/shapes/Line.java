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
import java.awt.geom.Line2D;


/**
 * This class stores all the information about a line, and manages
 * its drawing.
 * 
 * @author David Terei
 * @since 25/04/2004
 * @version 1
 */
@SuppressWarnings("serial")
public class Line extends Shape {

    /**
     * The starting coordinates of the line.
     */
    private Point p1;
    /**
     * A working value for {@link #p1 p1}, used for zooming in and out so that
     * p1 isn't modified.
     * 
     * @see #p1
     */
    private Point p1w;    
    /**
     * The enidng coordinates of the line.
     */
    private Point p2;
    /**
     * A working value for {@link #p2 p2}, used for zooming in and out so that
     * p2 isn't modified.
     * 
     * @see #p2
     */
    private Point p2w;
    /**
     * The colour of the line.
     */
    private Color color;
    /**
     * The stroke/width of the line.
     */
    private int stroke = 1;
    /**
     * A working value for {@link #stroke stroke}, used for zooming in and out 
     * so that stroke isn't modified.
     * 
     * @see #stroke
     */
    private double stroked = 1;
    /**
     * The opacity of the line.
     */
    private int opacity = 100;
    
    /**
     * Create a new line.
     * 
     * @param p1 The starting point of the line.
     * @param Stroke The stroke/width of the line.
     * @param color The color of the line.
     * @param opacity The opacity of the line.
     */
    public Line(Point p1, int Stroke, Color color, int opacity) {
        this(p1,p1,Stroke, color, opacity);        
    }
    
    /**
     * Create a new line.
     * 
     * @param p1 The starting point of the line.
     * @param p2 The ending point of the line.
     * @param Stroke The stroke/width of the line.
     * @param color The color of the line.
     * @param opacity The opacity of the line.
     */    
    public Line(Point p1, Point p2, int Stroke, Color color, int opacity) {
        this.p1 = p1;
        p1w = new Point(p1.x, p1.y);
        this.p2 = p2;
        p2w = new Point(p2.x, p2.y);
        stroke = Stroke;
        stroked = stroke;
        this.color = color;
        this.opacity = opacity;
    }    
    
    /**
     * @return Returns The starting point of the line.
     */
    public Point getP1() {
        return p1;
    }
    /**
     * @return Returns The end point of the line.
     */
    public Point getP2() {
        return p2;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getShapeName()
     */
    public String getShapeName() {
        return "Line";
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getShapeCode()
     */
    public Shapes getShapeCode() {
        return Shapes.LINE;
    }
    
    /**
     * Get the thickness of the line.
     * 
     * @return Returns The stroke(thickness).
     */
    public int getStroke() {
        return stroke;
    }
    
    /**
     * Sets the coordinate of the line.
     * 
     * @param p1 The starting coordinate of the line.
     * 
     * @param p2 The ending coordinate of the line.
     */
    public void setCordinates(Point p1, Point p2) {
        this.p1 = p1;
        p1w = new Point(p1.x, p1.y);
        this.p2 = p2;
        p2w = new Point(p2.x, p2.y);
    }
    
    /**
     * Sets the coordinate of the line.
     * 
     * @param x1 The starting x coordinate of the line.
     * @param y1 The starting y coordinate of the line.
     * @param x2 The ending x coordinate of the line.
     * @param y2 The ending y coordinate of the line.
     */
    public void setCoordinates(int x1, int y1, int x2, int y2) {
       setCordinates(new Point(x1,y1), new Point(x2,y2));
    }
    
    /**
     * Set the starting point of the line.
     * 
     * @param p1 The starting point.
     */
    public void setP1(Point p1) {
        this.p1 = p1;
        p1w = new Point(p1.x, p1.y);
    }
    /**
     * Set the ending point of the line.
     * 
     * @param p2 The ending point.
     */
    public void setP2(Point p2) {
        this.p2 = p2;
        p2w = new Point(p2.x, p2.y);        
    }
    
    /**
     * Set the thickness of the line.
     * 
     * @param stroke The stroke to set (thickness).
     */
    public void setStroke(int stroke) {
        this.stroke = stroke;  
        stroked = stroke;
    }   
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#contain(java.awt.Point)
     */
    public boolean contain(Point p) {
        Line2D.Double line = new Line2D.Double(p1w.x,p1w.y,p2w.x,p2w.y);
        
        //get the perpendicular line distance from the point to the line.
        int d = (int)line.ptSegDist(p);
        //test if its less then the stroke, if so, then we know the point is
        //contained in the line.
        boolean b = false;
        if (d<((stroked+1)/2))
            b = true;
        
        return b;
    }         

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#paint(java.awt.Graphics)
     */
    public void paint(Graphics2D g) {
        //get the original composite to restore later.
        //we dont want to modify the default composite
        //as we want to be able to restore it.
        Composite comp_orig = g.getComposite();
        
        Line2D.Double line = new Line2D.Double(p1w.x, p1w.y, p2w.x, p2w.y);
        
        //set the color
        g.setColor(color);
        //set the stroke
        g.setStroke(new BasicStroke((int)stroked));
        //set the opacity
        float i = ((float)opacity/100);
        Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, i);        
        g.setComposite(alpha);
        
        //draw the line
        g.draw(line);
        
        //restore back the original composite
        g.setComposite(comp_orig);
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getFillColor()
     */
    public Color getFillColor() {
        return getFillColor();
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getOutlineColor()
     */
    public Color getOutlineColor() {
        return color;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color color) {
        setOutlineColor(color);       
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setOutlineColor(java.awt.Color)
     */
    public void setOutlineColor(Color color) {
        this.color = color;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setFillOpacity(int)
     */
    public void setFillOpacity(int opacity) {
        setOutlineOpacity(opacity);
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setOutlineOpacity(int)
     */
    public void setOutlineOpacity(int opacity) {
        this.opacity = opacity;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getFillOpacity()
     */
    public int getFillOpacity() {
        return getOutlineOpacity();
        
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getOutlineOpacity()
     */
    public int getOutlineOpacity() {
        return opacity;        
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#resize(double)
     */
    public void resize(double percent) {
        p1w.x = (int)(p1.x * percent);
        p1w.y = (int)(p1.y * percent);
        
        p2w.x = (int)(p2.x * percent);
        p2w.y = (int)(p2.y * percent);
        
        stroked = stroke * percent;              
    }

}
