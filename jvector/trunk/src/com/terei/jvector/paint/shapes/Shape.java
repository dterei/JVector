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
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;


/**
 * Lays a few basic properties/methods that all shapes must have.
 * All drawing shapes subclass this class.
 * 
 * @author David Terei
 * @since 25/04/2004
 * @version 1
 */
public abstract class Shape implements Serializable {    
    
    /**
     * The signiture number for this class for use with serilization.
     * This number is normally generated on the fly by analyzing the
     * class and its fields. By manually specifing it, i am able to
     * change so parts of this class, while still allowing older saved
     * maps to remain compatible, as these id's must match with the saved
     * one and this class.
     */
    private static final long serialVersionUID = 5692509975487695120L;
    
//    /**
//     * The int code value for a shape.
//     */
//    public static final int SHAPE = 0;
//    /**
//     * The int code value for a (@com.terei.jvector.paint.shapes.Line line).
//     */
//    public static final int SHAPE_LINE = 1;
//    /**
//     * The int code value for a (@com.terei.jvector.paint.shapes.Rectangle 
//     * rectangle}.
//     */
//    public static final int SHAPE_RECTANGLE = 2;
//    /**
//     * The int code value for a (@com.terei.jvector.paint.shapes.Oval Oval}.
//     */
//    public static final int SHAPE_OVAL = 3;
//    /**
//     * The int code value for a (@com.terei.jvector.paint.shapes.Polygon 
//     * polygon}.
//     */
//    public static final int SHAPE_POLYGON = 4;
//    /**
//     * The int code value for a (@com.terei.jvector.paint.shapes.Text Text}.
//     */
//    public static final int SHAPE_TEXT = 5;
    
    /**
     * Tests to see if the shape contains the point specified.
     * 
     * @param p The point to test with
     * @return If the shape contains the point
     */
    public abstract boolean contain(Point p);
    
    /**
     * Returns the fill color of the shape.
     * 
     * @return The fill color of the shape.
     */
    public abstract Color getFillColor();

    /**
     * Returns the outline color of the shape.
     * 
     * @return The outline color of the shape.
     */
    public abstract Color getOutlineColor();
    
    /**
     * Returns the fill opacity of the shape.
     * 
     * @return The fill's opacity.
     */
    public abstract int getFillOpacity();
    
    /**
     * Returns the outline opacity of the shape.
     * 
     * @return The outline's opacity.
     */
    public abstract int getOutlineOpacity();
    
    /**
     * Returns the name of the shape.
     * 
     * @return The name of the shape.
     */
    public String getShapeName() {
        return "Shape";
    }
    
    /**
     * Get the integer code that represents the shape.
     * 
     * @return The integer that represents this shape.
     */
    public Shapes getShapeCode() {
        return Shapes.SHAPE;
    }

    /**
     * Sets the fill colour of the shape.
     * 
     * @param color The colour to set the shape's fill to.
     */
    public abstract void setFillColor(Color color);
    
    /**
     * Sets the outline colour of the shape.
     * 
     * @param color The colour to set the shape's outline to.
     */
    public abstract void setOutlineColor(Color color);
    
    /**
     * Sets the opacity of the fill of the shape.
     * 
     * @param opacity The opacity to set.
     */
    public abstract void setFillOpacity(int opacity);
    
    /**
     * Sets the opacity of the outline of the shape.
     * 
     * @param opacity The opacity to set.
     */
    public abstract void setOutlineOpacity(int opacity);
    
    /**
     * Resize the shape, according to the double value percent, specified.
     * This method allows shapes to be 'zoomed' in on.
     * 
     * @param percent The size, in decimals, for the shape to be, relative to
     * its 'base' size. So a value of 2 will double the shape compared to its
     * original value.
     */
    public abstract void resize(double percent);      
    
    /**
     * The painting method for each shape.
     * 
     * @param g The graphics object to draw to.
     */
    public void paint(Graphics2D g) {
        
    }
    
}
