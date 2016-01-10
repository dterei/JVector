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
package com.terei.jvector.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;


/**
 * 
 * 
 * @author David Terei
 * @since 17/05/2004
 * @version 0.1
 */
public class SwingFileFilter extends FileFilter {

    /**
     * The extension of the file to filter (accept).
     */
    private String ext = "";
    
    /**
     * The description of this filter.
     */
    private String description = ext;
    
    /**
     * If to aproove directory's or not
     */
    private boolean showDir = true;

    /**
     * Initializes the class.
     *
     * @param ext The extension (excluiding the dot) of the file to filter (accept).
     * @param description The description of this Filter.
     * @param showDir To aproove Directory's or not.
     */
    public SwingFileFilter(String ext, String description, boolean showDir) {
        this.ext = ext;
        this.description = description;
        this.showDir = showDir;
    }

    /**
     * Test To See if the file has the extension,
     *  <code>ext</code>, if so return true, else return false.
     *
     * @param f The file to check and aprove for display or not.
     * @return If the file/directory was accepted.
     * 
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f) {        
        if (f.isDirectory()) {
            return showDir;
        }

        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(ext))
                return true;
            else
                return false;
        }

        return false;
    }
    

    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the extension of the given file.
     *
     * @param f The File who's extension you whish returned.
     * @return The extension of the file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    /**
     * Returns the file extensions that this filter accepts.
     * 
     * @return Returns the ext.
     */
    public String getAcceptExtension() {
        return ext;
    }
}
