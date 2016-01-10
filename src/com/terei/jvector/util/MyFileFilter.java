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
package com.terei.jvector.util;


import java.io.*;


/**
 * This class provides a file filter, that only returns
 * files who's extension is .properties.
 *
 * @author David Terei
 * @since 12/05/2004
 * @version 1
 */
public class MyFileFilter implements FileFilter {

    /**
     * The extension of the file to filter (accept).
     */
    private String ext = "";
    
    /**
     * If to aproove directory's or not
     */
    private boolean showDir = true;

    /**
     * Initializes the class.
     *
     * @param ext The extension (excluiding the dot) of the file to filter (accept).
     * @param showDir To aproove Directory's or not.
     */
    public MyFileFilter(String ext, boolean showDir) {
        this.ext = ext;
        this.showDir = showDir;
    }

    /**
     * Test To See if the file has the extension,
     *  <code>ext</code>, if so return true, else return false.
     *
     * @param f The file to check and aprove for display or not.
     * @return If the file/directory was accepted.
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
}
