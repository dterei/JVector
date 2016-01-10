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
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;


/**
 * This class stores all the information about a oval, and manages
 * its drawing.
 * 
 * @author David Terei
 * @since 15/05/2004
 * @version 1
 */
@SuppressWarnings("serial")
public class Oval extends Rectangle {
    
    /**
     * The oval to draw.
     */
    Oval2D oval;

    /**
     * Create a new Oval.
     * 
     * @param p1 The top left corner of the oval.
     * @param Stroke The thickness of the outline of the oval.
     */
    public Oval(Point p1, int Stroke) {
        super(p1, Stroke);
        createOval();
    }
    
    /**
     * Create a new Oval.
     * 
     * @param p1 The top left corner of the oval.
     * @param dim The dimension (width, height) of the oval.
     * @param Stroke The thickness of the outline of the oval.
     */
    public Oval(Point p1, Point dim, int Stroke) {
        super(p1,dim,Stroke);
        createOval();
    }
    
    /**
     * Create a new oval.
     * 
     * @param rect The rectangle to create the oval from.
     */
    public Oval(Rectangle rect) {
        super(rect.p1, rect.dim, rect.stroke);
        this.setFillColor(rect.getFillColor());
        this.setFillOpacity(rect.getFillOpacity());
        this.setOutlineColor(rect.getOutlineColor());
        this.setOutlineOpacity(rect.getOutlineOpacity());
        createOval();
    }
    
    /**
     * Creates a new Oval with the current values set.
     *
     * @see com.terei.jvector.paint.shapes.Oval2D
     */
    private void createOval() {
        oval = new Oval2D((int)this.p1w.x, (int)this.p1w.y, 
                   (int)this.dimw.x, (int)this.dimw.y, (int)this.stroked);
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#contain(java.awt.Point)
     */
    public boolean contain(Point p) {
        return oval.contain(p);
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getShapeName()
     */
    public String getShapeName() {
        return "Oval";
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getShapeCode()
     */
    public Shapes getShapeCode() {
       return Shapes.OVAL; 
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#paint(java.awt.Graphics)
     */
    public void paint(Graphics2D g) {
        Composite comp_orig = g.getComposite();        
        
        //createOval();
        
        //set the opacity.
        float i = (float)opacity_in/100;
        Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, i);
        g.setComposite(alpha);
        //set the color.
        g.setColor(color_in);
        //draw the fill. 
        oval.fill(g);
        
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
            oval.draw(g);
        }
        
        g.setComposite(comp_orig);
        
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Rectangle#setP1(java.awt.geom.Point2D.Double)
     */
    public void setP1(Point p) {
        super.setP1(p);
        createOval();
    }
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Rectangle#setP2(java.awt.geom.Point2D.Double)
     */
    public void setP2(Point p) {
        super.setP2(p);
        createOval();
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#resize(double)
     */
    public void resize(double times) {
        super.resize(times);
        createOval();
    }
}

