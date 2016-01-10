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
import java.awt.geom.Ellipse2D;


/**
 * Class that provides some similar functions to a java.awt.geom.Ellipse2D,
 * but doesn't have the problem with outlines. Ellipse2D draws the outline 
 * of the shape, 50% on the shape fill, 50% off it. When
 * using transperency, this results in the area where there are both fill and
 * outline, thus they blend together. This is not desired, and so this class 
 * deals with that problem. 
 * 
 * @author David Terei
 * @since 15/05/2004
 * @version 0.1
 */
@SuppressWarnings("serial")
public class Oval2D extends Rectangle_2D {    
    
    /**
     * Create a new Oval.
     * 
     * @param x The starting x point of the oval.
     * @param y The starting y point of the oval.
     * @param width The width of the oval.
     * @param height The height of the oval.
     * @param stroke The stroke of the oval's outline.
     */
    public Oval2D(int x, int y, int width, int height, int stroke) {
        super(x,y,width,height,stroke);
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Rectangle_2D#calculateSizes()
     */
    protected void calculateSizes() {
        outline = new java.awt.Rectangle(p.x, p.y, dim.x, dim.y);
        fill = new java.awt.Rectangle(p.x+(int)(stroke/2+0.5), p.y+(int)(stroke/2+0.5), 
                                  dim.x-(int)(stroke-0.5), dim.y-(int)(stroke-0.5));
    }    
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Rectangle_2D#contain(java.awt.Point)
     */
    public boolean contain(Point p) {
        Ellipse2D oval = new Ellipse2D.Double(outline.x, outline.y, 
                                            outline.width, outline.height);
        
        return oval.contains(p.x, p.y);
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Rectangle_2D#fill(java.awt.Graphics2D)
     */
    public void fill(Graphics2D g) {
        g.fillOval(fill.x, fill.y, fill.width, fill.height);
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Rectangle_2D#draw(java.awt.Graphics2D)
     */
    public void draw(Graphics2D g) {
        BasicStroke basicStroke = new BasicStroke(this.stroke);
        g.setStroke(basicStroke);
        g.drawOval(outline.x, outline.y, outline.width, outline.height);
    }
    
}
