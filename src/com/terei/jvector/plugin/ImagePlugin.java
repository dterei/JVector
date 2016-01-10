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

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.terei.jvector.paint.ImageFile;

//TODO proivde a dialog for plugin options, eg quality.
/**
 * An interface that must be extended to create a class to
 * save JVector image to other various image formats (eg JPG).
 * 
 * @author David Terei
 * @version 1
 * @since JVector 0.16 (1/06/2004)
 */
public interface ImagePlugin {
    
    /**
     * Get the File Extension of the Image's
     * outputted by the plugin.
     * 
     * @return The Image Extension.
     */
    public String getExtension();
    
    /**
     * Get the extension for this image type.
     * 
     * @return The extension of the image type.
     */
    public FileFilter getFileFilter();   
    
    /**
     * Save the image to the file specified.
     * 
     * @param image The image to save.
     * @param file The file to save it to.
     * @return If the operation succeeded.
     */
    public boolean save(ImageFile image, File file);
	//TODO: Handle Zoom Level.
    
}
