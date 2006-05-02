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

import org.apache.log4j.Logger;

import java.io.File;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.terei.jvector.JVector;
import com.terei.jvector.paint.ImageFile;
import com.terei.jvector.plugin.ImagePlugin;
import com.terei.jvector.plugin.ImagePluginManager;


/**
 * This class manages the exporting of JVector images to various
 * other formats, which are availible as plugins. It also provides
 * the GUI for the user to do this. 
 * 
 * @author David Terei
 * @version 1
 * @since 2/06/2004
 * 
 * @see com.terei.jvector.plugin.ImagePluginManager
 * @see com.terei.jvector.plugin.ImagePlugin
 */
public class ImageExporter {
    /**
     * Log4J Logger for this class
     */
    private static final Logger logger = Logger.getLogger(ImageExporter.class);
    
    /**
     * The plugin manager used to get the availible plugins and access them.
     */
    private static ImagePluginManager ipm = new ImagePluginManager();
    /**
     * The file chooser dialog used to select the plugin and path to save to.
     */
    private static JFileChooser chooser = new JFileChooser();
    
    /**
     * Static Initialization block that sets up the JFileChooser used to export
     * images, so that it doesnt need to be done every time the static export method
     * is accessed.
     */
    static {
        ResourceBundle locale = JVector.getRBLocale();
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setAcceptAllFileFilterUsed(false);  
        
        //set the export dialog title.
        //if an error is thrown getting the string, then log it, and
        //just set the title to english.
        try {
            chooser.setDialogTitle(locale.getString("Dialog.Export.Title"));
        } catch (MissingResourceException e) {
            logger.error("Missing locale string for Save dialog title", e);
            chooser.setDialogTitle("Export the image");
        }    
        
        try {            
        	System.out.println("Hello");
            FileFilter[] filters = ipm.getImageTypes();
            System.out.println("Hello2");
            for (FileFilter i : filters) {
                System.out.println("i: " + i.toString());
            	chooser.setFileFilter(i);
            }
            
            chooser.setFileFilter(filters[0]);
        } catch (Exception e) {
            logger.error("static initilization block", e);
            //They are bad, so show an error dialog.
            JOptionPane.showMessageDialog(null,
                    	locale.getString("Dialog.Error.Plugins.Message"),
                        locale.getString("Dialog.Error.Plugins.Title"),
                        JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Don't allow the class to be initialized.
     */
    private ImageExporter() {         
    }
    
    /**
     * Exports the image specified to a format and path specified by the user.
     * This method shows a JFileChooser dialog box, listing the availible image
     * outputs, and allows tehm to choose one and export it to the path chossen.
     * 
     * @param image The image to export.
     * @param defaultDir The starting directory to show for the JFileChooser, the proveious
     * save locatio of the image is normally used.
     */
    public static void export(ImageFile image, File defaultDir) {
        if (logger.isDebugEnabled()) {
            logger.debug("export(ImageFile, File) - start");
        }
                
        chooser.setSelectedFile(defaultDir);              
        
        int returnVal = chooser.showSaveDialog(null);
        
        if(returnVal != JFileChooser.APPROVE_OPTION)
            return;
        
        String ext = null;
        if (chooser.getFileFilter() instanceof SwingFileFilter) {
            SwingFileFilter filter = (SwingFileFilter)chooser.getFileFilter();
            ext = filter.getAcceptExtension();
            if (ext == null) {
                logger.warn("export(ImageFile, File) - Selected file filter extension = null");
                return;
            }                
        }
        
        try {
            ImagePlugin plugin = ipm.getPlugin(ext);        
        
		    File file = chooser.getSelectedFile();
		    
		    String name = file.getName();
		    
		    if (!name.endsWith("." + plugin.getExtension())) {
		        String path = file.getAbsolutePath() + "." + plugin.getExtension();
		        file = new File(path);
		    }
		    
		    plugin.save(image, file);
        } catch (NullPointerException e) {
            logger.error("export(ImageFile, File) - catch", e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("export(ImageFile, File) - end");
        }
    }

}
