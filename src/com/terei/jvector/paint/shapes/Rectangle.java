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
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Point;


/**
 * This class stores all the information about a rectangle, and manages
 * its drawing.
 * 
 * @author David Terei
 * @since 2/05/2004
 * @version 1
 */
@SuppressWarnings("serial")
public class Rectangle extends Shape {
    
    /**
     * The top left coordiante of the rectangle.
     */
    protected Point p1;
    /**
     * A working value for {@link #p1 p1}, used for zooming in and out so that
     * p1 isn't modified.
     * 
     * @see #p1
     */
    protected Point p1w;
    /**
     * The dimensions (width, height) of the rectangle.
     */
    protected Point dim;
    /**
     * A working value for {@link #dim dim}, used for zooming in and out so that
     * dim isn't modified.
     * 
     * @see #dim
     */
    protected Point dimw;
    /**
	 * The outline colour of the rectangle.
	 */ 
    protected Color color_out = Color.black;
    /**
     * The fill colour of the rectangle.
     */
    protected Color color_in = Color.white;
    /**
     * The stroke/width of the outline of the rectangle.
     */
    protected int stroke = 1;
    /**
     * A working value for {@link #stroke stroke}, used for zooming in and out 
     * so that stroke isn't modified.
     * 
     * @see #stroke
     */
    protected double stroked = 1;
    /**
     * The opacity of the fill of the rectangle.
     */
    protected int opacity_in = 100;
    /**
     * The opacity of the outline of the rectangle.
     */
    protected int opacity_out = 100;
    /**
     * The rectangle to draw
     */
    protected Rectangle_2D rect;    
    
    /**
     * Create a new Rectangle.
     * 
     * @param p1 The top left coordiantes of the rectangle.
     * @param Stroke The stroke/width of the outline of the rectangle.
     */
    public Rectangle(Point p1, int Stroke) {
        this(p1,new Point(0,0),Stroke);        
    }
    
    /**
     * Create a new Rectangle.
     * 
     * @param p1 The top left coordiantes of the rectangle.
     * @param dim The dimensions (width, height) of the rectangle.
     * @param Stroke The stroke/width of the outline of the rectangle.     
     */    
    public Rectangle(Point p1, Point dim, int Stroke) {
        this.p1 = p1;
        p1w = new Point(p1.x, p1.y);
        this.dim = dim;
        dimw = new Point(dim.x, dim.y);
        this.stroke = Stroke;    
        stroked = stroke;
        createRectangle();
    }
    
    /**
     * Creates a new Rectnagle.
     * 
     * @see com.terei.jvector.paint.shapes.Rectangle_2D
     */
    private void createRectangle() {
        rect = new Rectangle_2D((int)p1w.x, (int)p1w.y, (int)dimw.x, 
                                                    (int)dimw.y, (int)stroked);
    }   
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#contain(java.awt.Point)
     */
    public boolean contain(Point p) {
        return rect.contain(p);
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
        rect.fill(g);
        
        
        //draw the outline.
        if (stroke>0) {
            //g.setStroke(new BasicStroke(stroke));        
            //set the opacity.
            i = (float)opacity_out/100;
            Composite alpha2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, i);
            g.setComposite(alpha2);
            //set the color.
            g.setColor(color_out);
            //draw the outline.            
            rect.draw(g);
        }
        //restore the original composite.        
        g.setComposite(comp_orig);
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getOutlineColor()
     */
    public Color getOutlineColor() {
    	return color_out;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getFillColor()
     */
    public Color getFillColor() {
        return color_in;
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
        return "Rectangle";
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getShapeCode()
     */
    public Shapes getShapeCode() {
        return Shapes.RECTANGLE;
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setOutlineColor(java.awt.Color)
     */
    public void setOutlineColor(Color color) {
        color_out = color;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setFillColor(java.awt.Color)
     */
	 public void setFillColor(Color color) {
	 	color_in = color;
	 }
    
    /**
     * Sets the point starting point (top left).
     * 
     * @param p The point to set it to.
     */    
    public void setP1(Point p) {
        p1 = p;
        p1w = new Point(p1.x, p1.y);
        createRectangle();
    }
    
    /**
     * Sets the point diagonally opptisote the starting point of the line (top left).
     * 
     * @param p2 The point to set it to.
     */
    public void setP2(Point p2) {
        dim.x = p2.x - p1.x;
        dim.y = p2.y - p1.y; 
        dimw = new Point(dim.x, dim.y);
        createRectangle();
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

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#resize(double)
     */
    public void resize(double percent) {
        p1w.x = (int)(p1.x * percent);
        p1w.y = (int)(p1.y * percent);
        
        dimw.x = (int)(dim.x * percent);
        dimw.y = (int)(dim.y * percent);        
        
        stroked = stroke * percent;
        
        createRectangle();
    }
        
}
