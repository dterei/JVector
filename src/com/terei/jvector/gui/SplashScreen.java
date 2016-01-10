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

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

import org.apache.log4j.Logger;


/**
 * This class shows a splash screen, used to notify users
 * that an application has started up, but is currently loading.
 * A splash screen is a undeccorated window, that simply displays
 * am image, and is centered in the center of the screen.
 * 
 * @author David Terei
 * @since 20/05/2004
 * @version 0.1
 */
@SuppressWarnings("serial")
public class SplashScreen extends JWindow {
    /**
     * Log4J Logger for this class
     */
    private static final Logger logger = Logger.getLogger(SplashScreen.class);    
    
    /**
     * Create a new Splash Screen.
     * 
     * @param image The splash image to display.
     */
    public SplashScreen(ImageIcon image) {     
        if (logger.isDebugEnabled()) {
            logger.debug("ImageIcon(ImageIcon " + image + ") - start");
        }
        JLabel label = new JLabel(image);       
        add(label);
        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)(dim.getWidth() - this.getWidth())/2;
        int y = (int)(dim.getHeight() - this.getHeight())/2;
        setLocation(x,y);
    }
    
}
