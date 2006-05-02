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
package com.terei.jvector.plugin;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

import com.terei.jvector.gui.SwingFileFilter;
import com.terei.jvector.paint.ImageFile;
import com.terei.jvector.paint.shapes.Shape;


/**
 * An ImagePlugin that allows the saving of a
 * JVector Image to a PNG image.
 * 
 * @author David Terei
 * @version 1
 * @since JVector 0.16 (1/06/2004)
 */
public class PngPlugin implements ImagePlugin {

    /**
     * The extnesion of this image type
     */
    private final String IMAGE_EXT = "png";
    
    /**
     * The description of the filter built by this plugin.
     */
    private final String IMAGE_DES = "PNG Image Format (." + IMAGE_EXT + ")";
    
    /* (non-Javadoc)
     * @see com.terei.jvector.plugin.ImagePlugin#getExtension()
     */
    public String getExtension() {
        return IMAGE_EXT;
    }
    
    /* (non-Javadoc)
     * @see com.terei.jvector.plugin.ImagePlugin#getFileFilter()
     */
    public FileFilter getFileFilter() {
        return new SwingFileFilter(IMAGE_EXT, IMAGE_DES, true);
    }

    /* (non-Javadoc)
     * @see com.terei.jvector.plugin.ImagePlugin#save(com.terei.jvector.paint.ImageFile)
     */
    public boolean save(ImageFile image, File file) {
    	//TODO: Handle Zoom Level.
        //create a bufferedimage to draw the jvector image to.
        //well then save this buffered image.
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), 
                BufferedImage.TYPE_INT_ARGB);               
        
        //get the buffered image's graphics to paint the jvector
        //image to.
        Graphics2D g = (Graphics2D)bi.getGraphics();
        //get the shapes to paint.
        Shape[] shapes = image.getShapes();
        
        //turn Antialiasing on.        
        //TODO provide option
    	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	
    	//draw all the shapes.
    	for(Shape s : shapes)
    		s.paint(g);
        
    	//check its got the image extension
    	String name = file.getName();
    	
    	if (!name.endsWith("."+IMAGE_EXT)) {
            String path = file.getAbsolutePath()+"."+IMAGE_EXT;
            file = new File(path);
        }
    	
    	//save the image
    	try {
    	    ImageIO.write(bi, IMAGE_EXT, file);
    	} catch (IOException e) {
    	    System.err.println("IOException Saving Image: " + e);
    	}
    	
    	
        return true;
    }

}
