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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;


/**
 * Class that provides some similar functions to a java.awt.geom.Rectangle2D,
 * but doesn't have the problem with outlines. Rectangle2D and its subclasses
 * all draw the outline of the shape, 50% on the shape fill, 50% off it. When
 * using transperency, this results in the area where there are both fill and
 * outline, thus they blend together. This is not desired, and so this class 
 * deals with that problem. 
 * 
 * @author David Terei
 * @since 15/05/2004
 * @version 0.1
 */
public class Rectangle_2D implements Serializable {
    
    /**
     * The signiture number for this class for use with serilization.
     * This number is normally generated on the fly by analyzing the
     * class and its fields. By manually specifing it, i am able to
     * change so parts of this class, while still allowing older saved
     * maps to remain compatible, as these id's must match with the saved
     * one and this class.
     */
    private static final long serialVersionUID = 973050406872567120L;
    
    /**
     * The starting point of the rectangle.
     */
    Point p;
    /**
     * The dimensions of the rectangle.
     */
    Point dim;
    /**
     * The outline stroke of the rectangle.
     */
    int stroke = 1;
    
    /**
     * The rectangle that draws the outline of the overall rectangle.
     */
    java.awt.Rectangle outline;
    /**
     * The rectangle that draws the fill of the overall rectangle.
     */
    java.awt.Rectangle fill;
    
    /**
     * Create a new rectangle.
     * 
     * @param x The starting x point of the rectangle.
     * @param y The starting y point of the rectangle.
     * @param width The width of the rectangle.
     * @param height The height of the rectangle.
     * @param stroke The width of the rectangle.
     */
    public Rectangle_2D(int x, int y, int width, int height, int stroke) {
        p = new Point(x, y);
        dim = new Point(width, height);
        this.stroke = stroke;
        calculateSizes();
    }
    
    /**
     * Calculates the sizes of the two individual rectangle's which
     * make up the overall rectangle.
     * 
     * @see Rectangle_2D#outline
     * @see Rectangle_2D#fill
     */
    protected void calculateSizes() {
    	//java places the starting and dimensions to be in the middle of the
    	//stroke of the shape, we want to make it so that its the outside of the
    	//shape as this allows for more accurate drawing.
    	p.x+=(stroke/2);
    	p.y+=(stroke/2);
    	dim.x-=stroke;
    	dim.y-=stroke;
        
    	outline = new java.awt.Rectangle(p.x, p.y, 
        									dim.x, dim.y);
        //since were working in a int drawing space we have to move the fill
    	//according to how the stroke divides.
    	int oe = (stroke%2==0)?1:0;
        float d = ((float)stroke+1)/2;
        int s = stroke-oe;
        fill = new java.awt.Rectangle((int)(p.x+d),(int)(p.y+d),dim.x-s,dim.y-s);
    }
    
    /**
     * Test to see if the rectangle contains the point specified.
     * 
     * @param p The point to test
     * @return If the point is contained within the rectangle
     */
    public boolean contain(Point p) {
        return outline.contains(p.x, p.y);
    }
    
    /**
     * Sets the stroke of the rectangle's outline.
     * 
     * @param stroke The value to set the stroke to.
     */
    public void setStroke(int stroke) {
        this.stroke = stroke;
        calculateSizes();
    }
    
    /**
     * Paints the fill of the rectangle.
     * 
     * @param g The graphics component to paint to.
     */
    public void fill(Graphics2D g) {
        g.fill(fill);
    }
    
    /**
     * Paints the outline of the rectangle.
     * 
     * @param g The graphics component to paint to.
     */    
    public void draw(Graphics2D g) {
        BasicStroke basicStroke = new BasicStroke(stroke);
        g.setStroke(basicStroke);
        g.draw(outline);
    }
    
}
