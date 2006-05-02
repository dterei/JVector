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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;


/**
 * This class stores all the information about a String of Text, and manages
 * its drawing.
 * 
 * @author David Terei
 * @since 17/05/2004
 * @version 0.1
 */
@SuppressWarnings("serial")
public class Text extends Shape {
    
    /**
     * The starting point (top left) of the text.
     */
    private Point p;
    /**
     * A working value for {@link #p p}, used for zooming in and out so that
     * p isn't modified.
     * 
     * @see #p
     */
    private Point pw;  
    /**
     * The text to display.
     */
    private String text = "";
    /**
     * Stores the size of the text.
     */
    private int size;
    /**
     * A working value for {@link #size size}, used for zooming in and out 
     * so that size isn't modified.
     * 
     * @see #size
     */
    private double sized;
    /**
     * The font to display the text with.
     */
    private Font font;
    /**
     * The color of the text.
     */
    private Color color = Color.BLACK;
    /**
     * The opacity of the text.
     */
    private int opacity = 100;

    /**
     * Create a new String.
     * 
     * @param p The starting (top left) point of the text.
     * @param text The text to display.
     * @param color The color of the text.
     * @param opacity The opacity of the text.
     * @param font The font for the text to use.
     */
    public Text(Point p, String text, Color color, int opacity, Font font) {
        this.p = p;
        pw = new Point(this.p.x, this.p.y);
        this.text = text;
        this.color = color;
        this.opacity = opacity;
        this.font = font;
        size = this.font.getSize();
        sized = size;
    }
    
    /**
     * Get the font used for the text.
     * 
     * @return Returns the font.
     */
    public Font getFont() {
        return font;
    }
    
    /**
     * Sets the font for the text to use.
     * 
     * @param font The font to set.
     */
    public void setFont(Font font) {
        this.font = font;
        size = this.font.getSize();
        sized = size;
    }
    
    /**
     * Get the starting point of the text.
     * 
     * @return Returns the starting point.
     */
    public Point getPoint() {
        return p;
    }
    
    /**
     * Set the stating point of the text.
     * 
     * @param p The point to set.
     */
    public void setPoint(Point p) {
        this.p = p;
        pw = new Point(p.x, p.y);
    }
   
    /**
     * Get the text.
     * 
     * @return Returns the text.
     */
    public String getText() {
        return text;
    }
    
    /**
     * Set the text.
     * 
     * @param text The text to set.
     */
    public void setText(String text) {
        this.text = text;
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#contain(java.awt.Point)
     */
    public boolean contain(Point p) {
        FontRenderContext frc = 
            new FontRenderContext(new AffineTransform(), true, false);
        TextLayout tl = new TextLayout(text, font, frc);
        
        Rectangle2D bounds  = tl.getBounds(); 
        bounds.setRect(bounds.getX() + pw.x, bounds.getY() + pw.y,
                                    bounds.getWidth(), bounds.getHeight());
        
        return bounds.contains(p.x, p.y);
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getShapeName()
     */
    public String getShapeName() {
        return "Text";
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getShapeCode()
     */
    public Shapes getShapeCode() {
        return Shapes.TEXT;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getFillColor()
     */
    public Color getFillColor() {
        return color;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getOutlineColor()
     */
    public Color getOutlineColor() {
        return getFillColor();
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getFillOpacity()
     */
    public int getFillOpacity() {
        return opacity;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#getOutlineOpacity()
     */
    public int getOutlineOpacity() {
        return getFillOpacity();
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color color) {
        this.color = color;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setOutlineColor(java.awt.Color)
     */
    public void setOutlineColor(Color color) {
        setFillColor(color);
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setFillOpacity(int)
     */
    public void setFillOpacity(int opacity) {
        if (opacity<0 || opacity>100)
            return;
        this.opacity = opacity;
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#setOutlineOpacity(int)
     */
    public void setOutlineOpacity(int opacity) {
        setFillOpacity(opacity);
    }   
    
    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#paint(java.awt.Graphics2D)
     */
    public void paint(Graphics2D g) {
        //get the original composite to restore later.
        //we dont want to modify the default composite
        //as we want to be able to restore it.
        Composite comp_orig = g.getComposite();
        
        //set the color of the text
        g.setColor(color);
        //set the font of the text
        g.setFont(font);
        //set the opacity
        float i = ((float)opacity/100);
        Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, i);        
        g.setComposite(alpha);
        
        //draw the text
        g.drawString(text, (int)pw.x, (int)pw.y);
        
        //restore back the original composite
        g.setComposite(comp_orig);                
        
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.paint.shapes.Shape#resize(double)
     */
    public void resize(double percent) {
        pw.x = (int)(p.x * percent);
        pw.y = (int)(p.y * percent);        
        
        sized = size * percent;
        font = new Font(font.getName(), font.getStyle(), (int)sized);
    }        

}
