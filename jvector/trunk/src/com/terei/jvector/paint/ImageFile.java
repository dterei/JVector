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
package com.terei.jvector.paint;

import java.io.Serializable;

import com.terei.jvector.paint.shapes.Shape;


/**
 * This class provides the ability to save an image to JVector's native
 * format, which allows the image to be reopen, and worked on again.
 * 
 * This class stores all the information about an image, and it implements
 * the Serializable interface, so that it can be saved to a file through an
 * object output stream.
 * 
 * @author David Terei
 * @since 16/05/2004
 * @version 0.1
 */
public class ImageFile implements Serializable {

    /**
     * The signiture number for this class for use with serilization.
     * This number is normally generated on the fly by analyzing the
     * class and its fields. By manually specifing it, i am able to
     * change so parts of this class, while still allowing older saved
     * maps to remain compatible, as these id's must match with the saved
     * one and this class.
     */
    private static final long serialVersionUID = 843402619281875150L;
    
    /**
     * The name of the image.
     */
    private String name = "Default Name";    
    
    /**
     * The width of the image.
     */
    private int width = 640;
    
    /**
     * The height of the image.
     */
    private int height = 480;
   
    /**
     * The current zoom of the image.
     */
    private int zoom = 100;
    
    /**
     * Holds all the shapes in this image.
     */
    private Shape[] shapes = null;
    
    /**
     * Create a new ImageFile.
     * 
     * @param name The name of the image.
     * @param width The width of the image.
     * @param height The height of the image.
     * @param zoom The zoom of the image.
     * @param shapes The shapes contained in the image.
     */
    public ImageFile(String name, int width, int height, int zoom, 
    		Shape[] shapes) {
        setName(name);
        setWidth(width);
        setHeight(height);
        setZoom(zoom);
        setShapes(shapes);
    }

    /**
     * The height of the image.
     * @return the height
     */
	public int getHeight() {
		return height;
	}

    /**
     * The height of the image.
     * @param height the height
     */
	public void setHeight(int height) {
		this.height = height;
	}

    /**
     * The name of the image.
     * @return the name
     */
	public String getName() {
		return name;
	}

    /**
     * The name of the image.
     * @param name the name
     */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * Holds all the shapes in this image.
     * @return the shapes
     */
	public Shape[] getShapes() {
		return shapes;
	}

    /**
     * Holds all the shapes in this image.
     * @param shapes the shapes
     */
	public void setShapes(Shape[] shapes) {
		this.shapes = shapes;
	}

    /**
     * The width of the image.
     * @return the width
     */
	public int getWidth() {
		return width;
	}

    /**
     * The width of the image.
     * @param width the width
     */
	public void setWidth(int width) {
		this.width = width;
	}

    /**
     * The current zoom of the image.
     * When restoring an image it should be restored to the zoom returned here.
     * @return the zoom
     */
	public int getZoom() {
		return zoom;
	}

    /**
     * The current zoom of the image.
     * @param zoom the zoom
     */
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

}
